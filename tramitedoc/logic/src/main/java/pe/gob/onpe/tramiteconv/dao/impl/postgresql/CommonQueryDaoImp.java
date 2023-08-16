/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramiteconv.dao.impl.postgresql;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.CargoFuncionBean;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBean;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBeanNew;
import pe.gob.onpe.tramitedoc.bean.DatosInterBean;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioOtroOrigenBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoVoBoBean;
import pe.gob.onpe.tramitedoc.bean.ProveedorBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;
import pe.gob.onpe.tramitedoc.dao.CommonQueryDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
import pe.gob.onpe.tramitedoc.web.util.BusquedaTextual;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author ECueva
 */
@Repository("commonQueryDao")
public class CommonQueryDaoImp extends SimpleJdbcDaoBase implements CommonQueryDao {

    @Override
    public DependenciaBean getDependenciaxCoDependencia(String coDepen) {
        StringBuilder sql = new StringBuilder();
        DependenciaBean dependenciaBean = null;
        sql.append("SELECT DE_DEPENDENCIA, CO_DEPENDENCIA FROM IDOSGD.RHTM_DEPENDENCIA WHERE\n" +
        "CO_DEPENDENCIA=? AND IN_BAJA='0'");
        
        try {
            dependenciaBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class),
                    new Object[]{coDepen});            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependenciaBean;
    }

    @Override
    public DependenciaBean getDependenciaBasico(String coDepen) {
        StringBuilder sql = new StringBuilder();
        DependenciaBean dependenciaBean = null;
        sql.append("SELECT \n" +
                    "X.DE_DEPENDENCIA, \n" +
                    "X.CO_DEPENDENCIA, \n" +
                    "X.de_sigla,\n" +
                    "X.in_mesa_partes,\n" +
                    "(SELECT DEP.CO_DEPENDENCIA FROM IDOSGD.RHTM_DEPENDENCIA DEP WHERE DEP.IN_MESA_PARTES='1' AND DEP.IN_BAJA='0'\n" +
                    "AND 0 < (SELECT COUNT(1) FROM IDOSGD.TDTR_DEPENDENCIA_MP DMP WHERE DMP.CO_DEP=X.CO_DEPENDENCIA AND DMP.ES_ELI='0' AND DMP.IN_MP='1')) CO_DEP_MP \n" +
                    "FROM IDOSGD.RHTM_DEPENDENCIA X \n" +
                    "WHERE X.CO_DEPENDENCIA=?");
        
        try {
            dependenciaBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class),
                    new Object[]{coDepen});            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependenciaBean;
    }
    
    @Override
    public List<RemitenteBean> getListRemitente(String coDepen){
        List<RemitenteBean> list = new ArrayList<RemitenteBean>();
        StringBuilder sql = new StringBuilder(); 
        sql.append("select D.DE_DEPENDENCIA descrip, D.CO_DEPENDENCIA cod_dep,D.DE_CORTA_DEPEN de_corta_depen from IDOSGD.RHTM_DEPENDENCIA D \n" +
                    "WHERE ? IN (CO_DEPENDENCIA,CO_DEPEN_PADRE) OR \n" +
                    "CO_DEPENDENCIA IN (SELECT co_dep_ref FROM IDOSGD.tdtx_referencia \n" +
                    "WHERE co_dep_emi = ?  AND ti_emi = 'D'   AND es_ref = '1') ORDER BY 1");
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(RemitenteBean.class),
                    new Object[]{coDepen, coDepen});            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public String verificarExpedienteDuplicado(String pnuAnnExp, String pcoDepExp, String pcoGru, String pnuCorrExp){
        String vReturn="1";//duplicado "si".
        try {
            vReturn = this.jdbcTemplate.queryForObject("select count(1) from IDOSGD.TDTC_EXPEDIENTE \n" +
                      "WHERE NU_ANN_EXP=? and CO_DEP_EXP=? and CO_GRU=? and NU_CORR_EXP=?", 
                    String.class, new Object[]{pnuAnnExp, pcoDepExp,
                pcoGru, pnuCorrExp});
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return vReturn;
    }
    
    @Override
    public String verficarNumeroDuplicadoEmiDocExtRecep(String pnuAnn,String pcoDepEmi,String pnuCorEmi){
        String vReturn="1";//duplicado "si".
        try {
            vReturn = this.jdbcTemplate.queryForObject("SELECT count(1)\n" +
                                "FROM IDOSGD.tdtv_remitos \n" +
                                "WHERE NU_ANN = ?\n" +//:B_02.NU_ANN
                                "AND CO_DEP_EMI = ?\n" +//:B_02.CO_DEP_EMI
                                "AND CO_GRU = '3' \n" +
                                "AND NU_COR_EMI = ?", //:B_02.NU_COR_EMI
                    String.class, new Object[]{pnuAnn, pcoDepEmi,Integer.parseInt(pnuCorEmi)});            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public List<ProveedorBean> getLstProveedores(String prazonSocial) {
        StringBuilder sql = new StringBuilder();
        prazonSocial=Utilidades.fn_getCleanTextLenGreathree(Utilidades.fn_getCleanTextExpReg(prazonSocial));
        
        List<ProveedorBean> list = new ArrayList<ProveedorBean>();
        sql.append("SELECT CPRO_RUC NU_RUC, CPRO_RAZSOC DESCRIPCION FROM IDOSGD.LG_PRO_PROVEEDOR \n"
                + "where cpro_razsoc LIKE '%'||?||'%'\n"
                + "order by 2 \n"
                + " LIMIT 50");

        
        
        try {
            if(prazonSocial!=null && prazonSocial.trim().length()>1){            
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ProveedorBean.class),
                        new Object[]{prazonSocial.toUpperCase()});
            }
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<DestinatarioOtroOrigenBean> getLstOtrosOrigenes(String pdescripcion){
        StringBuilder sql = new StringBuilder();
        pdescripcion=Utilidades.fn_getCleanTextLenGreathree(Utilidades.fn_getCleanTextExpReg(pdescripcion));                
        
        List<DestinatarioOtroOrigenBean> list = new ArrayList<DestinatarioOtroOrigenBean>();
        sql.append("SELECT DE_APE_PAT_OTR||' '|| DE_APE_MAT_OTR||', '|| DE_NOM_OTR || ' - ' || DE_RAZ_SOC_OTR DESCRIPCION,\n"
                + "       IDOSGD.PK_SGD_DESCRIPCION_DE_DOMINIOS('TIP_DOC_IDENT', CO_TIP_OTR_ORI ) TIPO_DOC_IDENTIDAD,\n"
                + "       NU_DOC_OTR_ORI NRO_DOC_IDENTIDAD, CO_OTR_ORI CO_OTRO_ORIGEN \n"
                + "  FROM IDOSGD.TDTR_OTRO_ORIGEN \n"
                + "  WHERE UPPER(de_raz_soc_otr) like '%'||?||'%'\n" 
//                + "where CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue(pdescripcion) +"'\n"
//                + ", 1 ) > 1\n"
//                + " order by score(1) desc");
                + " order by 1 desc");

        try {
            if(pdescripcion!=null && pdescripcion.trim().length()>1){
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioOtroOrigenBean.class),
                        new Object[]{pdescripcion});
            }
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public CiudadanoBean getCiudadano(String pnuDoc){
        StringBuilder sql = new StringBuilder();
        sql.append("select I.NULEM NU_DOCUMENTO,substr(RTRIM(I.DEAPP)||' '||RTRIM(I.DEAPM)||' '||RTRIM(I.DENOM),1,200) NOMBRE \n"
                + "from IDOSGD.IDTANIRS I \n"
                + "where\n"
                + "NULEM like ?||'%'\n"
                + "order by 2");

        CiudadanoBean ciudadanoBean = new CiudadanoBean();
        try {
            ciudadanoBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(CiudadanoBean.class),
                    new Object[]{pnuDoc});
        } catch (EmptyResultDataAccessException e) {
            ciudadanoBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ciudadanoBean;
    }  
    
    @Override
    public ProveedorBean getProveedor(String pnuRuc){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CPRO_RUC NU_RUC, CPRO_RAZSOC DESCRIPCION FROM IDOSGD.LG_PRO_PROVEEDOR\n" +
                    "where CPRO_RUC=?");

        ProveedorBean proveedorBean=null;
        try {
            proveedorBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(ProveedorBean.class),
                    new Object[]{pnuRuc});
        } catch (EmptyResultDataAccessException e) {
            proveedorBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proveedorBean;
    }
    
    @Override
    public List<EmpleadoBean> getLsEmpleado(String coDep){
        StringBuilder sql = new StringBuilder();
        List<EmpleadoBean> list;
        sql.append("SELECT e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom nombre, e.CEMP_CODEMP\n" +
            "FROM IDOSGD.RHTM_PER_EMPLEADOS e\n" +
            "where e.CEMP_EST_EMP = '1'\n" +
            "ORDER BY 1");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class),
                    new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public List<CargoFuncionBean> getLsEmpleo(){
        StringBuilder sql = new StringBuilder();
        List<CargoFuncionBean> list;
        sql.append("SELECT CCAR_DESCAR DESCRIPCION, CCAR_CO_CARGO CODIGO\n" +
                    "FROM IDOSGD.RHTM_CARGOS\n" +
                    "WHERE CCAR_CO_CARGO <> '000'\n" +
                    "ORDER BY 1");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(CargoFuncionBean.class),
                    new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }    
    
    @Override
    public List<DependenciaBean> getLsDepencia(){
        StringBuilder sql = new StringBuilder();
        List<DependenciaBean> list;
        sql.append("SELECT DE_DEPENDENCIA,\n" +
                    "       CO_DEPENDENCIA,\n" +
                    "       DE_CORTA_DEPEN\n" +
                    "  FROM IDOSGD.RHTM_DEPENDENCIA\n" +
                    " WHERE co_nivel <> '6'\n" +
                    "   AND IN_BAJA = '0'\n" +
                    "   order by 1");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class),
                    new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public String[] getNroCorrLocalDependencia(){
        String vReturn = "ERROR|";
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT coalesce( MAX( a.NU_CORRELATIVO) , 0)+1\n" +
                    "FROM IDOSGD.SITM_LOCAL_DEPENDENCIA a");

        try {
            String nroCorr = this.jdbcTemplate.queryForObject(sql.toString(), String.class);
            vReturn="OK|"+nroCorr;
        } catch (EmptyResultDataAccessException e) {
            vReturn="OK|1";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn.split("\\|");         
    }
    
    @Override
    public List<EmpleadoBean> getLsEmpleadoIntitu(String deEmp){
        StringBuilder sql = new StringBuilder();
        List<EmpleadoBean> list;
        sql.append("SELECT e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom nombre, e.CEMP_CODEMP\n" +
            "FROM IDOSGD.RHTM_PER_EMPLEADOS e\n" +
            "where e.CEMP_EST_EMP = '1' \n" +
            "and  e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom LIKE '%'||?||'%'\n" +
            "ORDER BY 1");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class),
                    new Object[]{deEmp});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public String[] getCodigoMotivoDocRec(String nuAnn,String nuEmi,String nuDes){
        String vReturn = "ERROR|";
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CO_MOT FROM IDOSGD.TDTV_DESTINOS\n" +
                    "WHERE NU_ANN=?\n" +
                    "AND NU_EMI=?\n" +
                    "AND NU_DES=?\n" +
                    "AND ES_ELI='0'");

        try {
            String coMot = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{nuAnn,nuEmi,Integer.parseInt(nuDes)});
            vReturn="OK|"+coMot;
        } catch (EmptyResultDataAccessException e) {
            vReturn="ERROR|";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn.split("\\|");               
    }
    
    @Override
    public EmpleadoBean getEmpJefeDep(String coDep){
        StringBuilder sql = new StringBuilder();
        sql.append("select CO_EMPLEADO CEMP_CODEMP,idosgd.PK_SGD_DESCRIPCION_DE_NOM_EMP(CO_EMPLEADO) NOMBRE\n" +
                    "from idosgd.rhtm_dependencia\n" +
                    "where CO_DEPENDENCIA = ?\n" +
                    "AND IN_BAJA='0'");

        EmpleadoBean empleadoBean = new EmpleadoBean();
        try {
            empleadoBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class),
                    new Object[]{coDep});
        } catch (EmptyResultDataAccessException e) {
            empleadoBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empleadoBean;        
    }
    
    @Override
    public List<EmpleadoBean> getLsEmpDepDesEmp(String deEmp,String coDep){
        StringBuilder sql = new StringBuilder();
        List<EmpleadoBean> list;
        sql.append("SELECT e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom nombre, e.CEMP_CODEMP \n" +
                    "FROM IDOSGD.RHTM_PER_EMPLEADOS e, \n" +
                    "( \n" +
                    "SELECT CEMP_CODEMP \n" +
                    "FROM IDOSGD.RHTM_PER_EMPLEADOS \n" +
                    "where CEMP_EST_EMP = '1' \n" +
                    "  and (CO_DEPENDENCIA=? or CEMP_CO_DEPEND=?) \n" +
                    "union \n" +
                    "select co_emp from idosgd.tdtx_dependencia_empleado  where co_dep=? and es_emp='0' \n" +
                    "union \n" +
                    "select co_empleado from idosgd.rhtm_dependencia where co_dependencia=? \n" +
                    ") a \n" +
                    "where e.cemp_codemp = a.cemp_codemp\n" +
                    "and  e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom LIKE '%'||?||'%'\n" +
                    "order by 1");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class),
                    new Object[]{coDep,coDep,coDep,coDep,deEmp});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public List<EmpleadoVoBoBean> getLsPersonalVoBo(String nuAnn,String nuEmi){
        List<EmpleadoVoBoBean> list;
        String sql = "SELECT VB.CO_EMP_VB CO_EMPLEADO,"
                + "(SELECT CEMP_APEPAT || ' ' || CEMP_APEMAT || ' ' || CEMP_DENOM "
                + "FROM IDOSGD.RHTM_PER_EMPLEADOS WHERE CEMP_CODEMP = VB.CO_EMP_VB) NOMBRE,\n"
                + "VB.CO_DEP CO_DEPENDENCIA,IDOSGD.PK_SGD_DESCRIPCION_DE_SIGLA(VB.CO_DEP) DE_DEPENDENCIA,\n"
                + "CASE WHEN VB.IN_VB='B' THEN '0' ELSE VB.IN_VB END AS IN_VOBO\n"
                + "FROM IDOSGD.TDTV_PERSONAL_VB VB\n"
                + "WHERE VB.NU_ANN=?\n"
                + "AND VB.NU_EMI=?\n"
                + "ORDER BY NOMBRE";

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoVoBoBean.class),
                    new Object[]{nuAnn,nuEmi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public String getInFirmaDoc(String pcoDep,String pcoTipoDoc){
        StringBuilder sql = new StringBuilder();
        String result = null;
        sql.append(" SELECT CASE \n" +
                            "WHEN es_obl_firma='0' THEN 'N'\n" +
                            "ELSE 'F'\n" +
                            "END\n" +
                    "FROM IDOSGD.sitm_doc_dependencia\n" +
                    "WHERE co_dep     = ?\n" +
                    "AND co_tip_doc = ?");        
        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pcoDep, pcoTipoDoc});
        } catch (EmptyResultDataAccessException e) {
            result = "F";
        } catch (Exception e) {
            result = "F";
            e.printStackTrace();
        }
        
        return result;        
    }
    @Override
    public List<CiudadanoBean> getLstCiudadanos(String sDescCiudadano){
        List<CiudadanoBean> list = new ArrayList<CiudadanoBean>();
        StringBuilder sql = new StringBuilder();
        sql.append("select I.NULEM NU_DOCUMENTO,substr(RTRIM(I.DEAPP)||' '||RTRIM(I.DEAPM)||' '||RTRIM(I.DENOM),1,200) NOMBRE,\n"
                + "NVL(UBDEP,'') ID_DEPARTAMENTO, NVL(UBPRV,'') ID_PROVINCIA, NVL(UBDIS,'') ID_DISTRITO, NVL(DEDOMICIL,'') DE_DIRECCION, NVL(DEEMAIL,'') DE_CORREO,NVL(DETELEFO,'') TELEFONO \n"
                + "from IDTANIRS I \n"
                + "where\n"
                + "(substr(RTRIM(I.DEAPP)||' '||RTRIM(I.DEAPM)||' '||RTRIM(I.DENOM),1,200)) like '%'||?||'%'\n"
                + "order by 2");

        CiudadanoBean ciudadanoBean = new CiudadanoBean();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(CiudadanoBean.class),
                    new Object[]{sDescCiudadano.toUpperCase()});
        } catch (EmptyResultDataAccessException e) {
            ciudadanoBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }  
    
         @Override
    public List<SiElementoBean> getLsDepenciaMensjeria(){
        StringBuilder sql = new StringBuilder();
        List<SiElementoBean> list;
        sql.append("SELECT DE_DEPENDENCIA,\n" +
                    "       CO_DEPENDENCIA,\n" +
                    "       DE_CORTA_DEPEN\n" +
                    "  FROM IDOSGD.RHTM_DEPENDENCIA\n" +
                    " WHERE co_nivel <> '6'\n" +
                    "   AND IN_BAJA = '0'\n" +
                    "   order by 1");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(SiElementoBean.class),
                    new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
     @Override
    public String obtenerValorParametro(String nombreParametro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CiudadanoBean> getLstCiudadanos(String sApePatCiudadano, String sApeMatCiudadano, String sNombreCiudadano) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DatosInterBean DatosInter(String nuAnn, String nuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   

    @Override
    public CiudadanoBeanNew getLstCiudadanoStored(String pnuDoc, String pcoTipoDoc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CiudadanoBean getLstCiudadanoAni(String pnuDoc, String pcoTipoDoc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProveedorBean getLstProveedor(String pnuRuc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDescripcionUbigeo(String pcoDep, String pcoPrv, String pcoDis, String pcoCar) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProveedorBean getProveedorExpediente(String pnuRuc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DestinatarioOtroOrigenBean getDestinatarioOtroOrigenExpediente(String pnuCodigo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String obtenerIndicadorEntidadPide(String nroRuc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
