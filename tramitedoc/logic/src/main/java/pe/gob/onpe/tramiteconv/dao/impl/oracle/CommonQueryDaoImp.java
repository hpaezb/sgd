/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramiteconv.dao.impl.oracle;

import java.sql.ResultSet;
import java.sql.SQLType;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.CargoFuncionBean;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBean;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioOtroOrigenBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoExtConsulBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoVoBoBean;
import pe.gob.onpe.tramitedoc.bean.ProveedorBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;
import pe.gob.onpe.tramitedoc.dao.CommonQueryDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
import pe.gob.onpe.tramitedoc.web.util.BusquedaTextual;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;
/*interoperabilidad*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBeanNew;
import pe.gob.onpe.tramitedoc.bean.DatosInterBean;
/*interoperabilidad*/
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
        sql.append("SELECT DE_DEPENDENCIA, CO_DEPENDENCIA FROM RHTM_DEPENDENCIA WHERE\n" +
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
                    "(SELECT DEP.CO_DEPENDENCIA FROM RHTM_DEPENDENCIA DEP WHERE DEP.IN_MESA_PARTES='1' AND DEP.IN_BAJA='0'\n" +
                    "AND 0 < (SELECT COUNT(1) FROM TDTR_DEPENDENCIA_MP DMP WHERE DMP.CO_DEP=X.CO_DEPENDENCIA AND DMP.ES_ELI='0' AND DMP.IN_MP='1')) CO_DEP_MP \n" +
                    "FROM RHTM_DEPENDENCIA X \n" +
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
        sql.append("select D.DE_DEPENDENCIA descrip, D.CO_DEPENDENCIA cod_dep,D.DE_CORTA_DEPEN de_corta_depen from RHTM_DEPENDENCIA D \n" +
                    "WHERE ? IN (CO_DEPENDENCIA,CO_DEPEN_PADRE) OR \n" +
                    "CO_DEPENDENCIA IN (SELECT co_dep_ref FROM tdtx_referencia \n" +
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
            vReturn = this.jdbcTemplate.queryForObject("select count(1) from TDTC_EXPEDIENTE \n" +
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
                                "FROM tdtv_remitos \n" +
                                "WHERE NU_ANN = ?\n" +//:B_02.NU_ANN
                                "AND CO_DEP_EMI = ?\n" +//:B_02.CO_DEP_EMI
                                "AND CO_GRU = '3' \n" +
                                "AND NU_COR_EMI = ?", //:B_02.NU_COR_EMI
                    String.class, new Object[]{pnuAnn, pcoDepEmi,pnuCorEmi});            
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
        sql.append("SELECT A.*, ROWNUM\n"
                + "FROM (\n"
                + "SELECT CPRO_RUC NU_RUC, CPRO_RAZSOC DESCRIPCION,CPRO_DOMICIL,CPRO_TELEFO,CPRO_EMAIL, \n"
                + "TRIM(NODEP) NODEP,TRIM(NOPRV) NOPRV,TRIM(NODIS) NODIS, TRIM(CUBI_CODDEP) CUBI_CODDEP,TRIM(CUBI_CODPRO) CUBI_CODPRO,TRIM(CUBI_CODDIS) CUBI_CODDIS,  \n"
                + "NVL(CUBI_CODDEP,'') ID_DEPARTAMENTO, NVL(CUBI_CODPRO,'') ID_PROVINCIA, NVL(CUBI_CODDIS,'') ID_DISTRITO, NVL(CPRO_DOMICIL,'') DE_DIRECCION, NVL(CPRO_EMAIL,'') DE_CORREO,NVL(CPRO_TELEFO,'') TELEFONO \n"
                + "FROM LG_PRO_PROVEEDOR LEFT JOIN IDTUBIAS ON UBDEP||UBPRV||UBDIS=CUBI_CODDEP||CUBI_CODPRO||CUBI_CODDIS\n"
                /*+ "where CONTAINS(cpro_razsoc, '" + BusquedaTextual.getContextValue(prazonSocial) + "'"
                + ", 1 ) > 1\n"
                + "order by score(1) desc \n"
                + ") A WHERE ROWNUM < 51");  */              
                
                + "where cpro_razsoc like REPLACE('%"+prazonSocial+"%',' ','%')\n"
                + " order by 2 \n"
                + ") A");      
        
        try {
            if(prazonSocial!=null && prazonSocial.trim().length()>1){            
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ProveedorBean.class),
                        new Object[]{});
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
        //pdescripcion=Utilidades.fn_getCleanTextLenGreathree(Utilidades.fn_getCleanTextExpReg(pdescripcion));        
        
        List<DestinatarioOtroOrigenBean> list = new ArrayList<DestinatarioOtroOrigenBean>();
        sql.append("SELECT DE_APE_PAT_OTR||' '|| DE_APE_MAT_OTR||', '|| DE_NOM_OTR || ' - ' || DE_RAZ_SOC_OTR DESCRIPCION,\n"
                + " PK_SGD_DESCRIPCION.DE_DOMINIOS('TIP_DOC_IDENT', CO_TIP_OTR_ORI ) TIPO_DOC_IDENTIDAD,\n"
                + " NU_DOC_OTR_ORI NRO_DOC_IDENTIDAD, CO_OTR_ORI CO_OTRO_ORIGEN,\n"
                + "NVL(UB_DEP,'') ID_DEPARTAMENTO, NVL(UB_PRO,'') ID_PROVINCIA, NVL(UB_DIS,'') ID_DISTRITO, NVL(DE_DIR_OTRO_ORI,'') DE_DIRECCION, NVL(DE_EMAIL,'') DE_CORREO,NVL(DE_TELEFO,'') TELEFONO \n"
                + "  ,UB_DEP,UB_PRO,UB_DIS\n" 
                + "  ,trim(UB.NODEP)||' '||trim(UB.NOPRV)||' '||trim(UB.NODIS) ubigeo,DE_EMAIL,a.DE_TELEFO,a.DE_DIR_OTRO_ORI\n" 
                + " ,trim(UB.NODEP) NODEP, trim(UB.NOPRV) NOPRV, trim(UB.NODIS) NODIS  \n"
                
                + "  FROM TDTR_OTRO_ORIGEN a\n"
                +" LEFT JOIN IDTUBIAS UB ON UB.UBDEP=a.UB_DEP AND UB.UBPRV=a.UB_PRO AND UB.UBDIS=a.UB_DIS\n" 
                + "where (DE_RAZ_SOC_OTR like '%"+pdescripcion+"%' \n"//-- CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue(pdescripcion) +"'\n"
                //+ "--, 1 ) > 1\n"
                + "OR (DE_APE_PAT_OTR||' '|| DE_APE_MAT_OTR||' '||DE_NOM_OTR) like '%"+pdescripcion+"%' \n"
                + "OR (DE_NOM_OTR ||' '||DE_APE_PAT_OTR||' '|| DE_APE_MAT_OTR) like '%"+pdescripcion+"%' \n"
                + "OR NU_DOC_OTR_ORI like REPLACE('%"+pdescripcion+"%',' ','%') \n"
                + ") and nvl(CO_TIP_OTR_ORI,'--') not in('09')\n"
//                + " order by score(1) desc");
                 + " order by 1 desc");
      
      

        
        try {
            if(pdescripcion!=null && pdescripcion.trim().length()>1){
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioOtroOrigenBean.class),
                        new Object[]{});
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
        sql.append("select I.NULEM NU_DOCUMENTO,substr(RTRIM(I.DEAPP)||' '||RTRIM(I.DEAPM)||' '||RTRIM(I.DENOM),1,200) NOMBRE,\n"
                + "NVL(I.UBDEP,'') ID_DEPARTAMENTO, NVL(I.UBPRV,'') ID_PROVINCIA, NVL(I.UBDIS,'') ID_DISTRITO, NVL(I.DEDOMICIL,'') DE_DIRECCION, NVL(I.DEEMAIL,'') DE_CORREO,NVL(I.DETELEFO,'') TELEFONO \n"
                + ",I.UBDEP, I.UBPRV,I.UBDIS,I.DEDOMICIL,I.DEEMAIL, I.DETELEFO ,  trim(UB.NODEP)||'/'||trim(UB.NOPRV)||'/'||trim(UB.NODIS) ubigeo \n"
                + "from IDTANIRS I LEFT JOIN IDTUBIAS UB ON UB.UBDEP=I.UBDEP AND UB.UBPRV=I.UBPRV AND UB.UBDIS=I.UBDIS \n"
                + "where\n"
                + "NULEM like ?||'%'\n"
                + "order by 2");
        

        CiudadanoBean ciudadanoBean = new CiudadanoBean();
        try {
            ciudadanoBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(CiudadanoBean.class),
                    new Object[]{pnuDoc});
            
            
            /*buscar ciudadano de persona juridica SIS
                    StringBuilder sqlUpdateCiudadano = new StringBuilder();                
                    sqlUpdateCiudadano.append("INSERT INTO IDOSGD.TDTX_ANI_SIMIL(NULEM,UBDEP,UBPRV,UBDIS,DEAPP,DEAPM,DENOM,DEDOMICIL)\n" +
                    "SELECT I.NULEM NU_DOCUMENTO,NVL(I.UBDEP,''),NVL(I.UBPRV,''),NVL(I.UBDIS,''),TRIM(I.DEAPP),RTRIM(I.DEAPM),RTRIM(I.DENOM),I.DEDOMICIL       \n" +
                    "    FROM IDOSGD.IDTANIRS I\n" +
                    "    WHERE  NULEM =?");
                    this.jdbcTemplate.update(sqlUpdateCiudadano.toString(), new Object[]{pnuDoc});
              
                     
                     fin buscar ciudadano de persona juridica SIS*/              
                
            
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
        sql.append("SELECT CPRO_RUC NU_RUC, CPRO_RAZSOC DESCRIPCION, \n"
                + "NVL(CUBI_CODDEP,'') ID_DEPARTAMENTO, NVL(CUBI_CODPRO,'') ID_PROVINCIA, NVL(CUBI_CODDIS,'') ID_DISTRITO, NVL(CPRO_DOMICIL,'') DE_DIRECCION, NVL(CPRO_EMAIL,'') DE_CORREO,NVL(CPRO_TELEFO,'') TELEFONO \n"
                +"FROM LG_PRO_PROVEEDOR\n"
                +"where CPRO_RUC=?");

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
            "FROM RHTM_PER_EMPLEADOS e\n" +
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
                    "FROM RHTM_CARGOS\n" +
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
                    "  FROM RHTM_DEPENDENCIA\n" +
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
        sql.append("SELECT NVL( MAX( a.NU_CORRELATIVO) , 0)+1\n" +
                    "FROM SITM_LOCAL_DEPENDENCIA a");

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
            "FROM RHTM_PER_EMPLEADOS e\n" +
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
        sql.append("SELECT CO_MOT FROM TDTV_DESTINOS\n" +
                    "WHERE NU_ANN=?\n" +
                    "AND NU_EMI=?\n" +
                    "AND NU_DES=?\n" +
                    "AND ES_ELI='0'");

        try {
            String coMot = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{nuAnn,nuEmi,nuDes});
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
        sql.append("select CO_EMPLEADO CEMP_CODEMP,PK_SGD_DESCRIPCION.DE_NOM_EMP(CO_EMPLEADO) NOMBRE\n" +
                    "from rhtm_dependencia\n" +
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
                    "FROM RHTM_PER_EMPLEADOS e, \n" +
                    "( \n" +
                    "SELECT CEMP_CODEMP \n" +
                    "FROM RHTM_PER_EMPLEADOS \n" +
                    "where CEMP_EST_EMP = '1' \n" +
                    "  and (CO_DEPENDENCIA=? or CEMP_CO_DEPEND=?) \n" +
                    "union \n" +
                    "select co_emp from tdtx_dependencia_empleado  where co_dep=? and es_emp='0' \n" +
                    "union \n" +
                    "select co_empleado from rhtm_dependencia where co_dependencia=? \n" +
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
        StringBuilder sql = new StringBuilder();
        List<EmpleadoVoBoBean> list;

        sql.append("SELECT VB.CO_EMP_VB CO_EMPLEADO,(SELECT CEMP_APEPAT || ' ' || CEMP_APEMAT || ' ' || CEMP_DENOM FROM RHTM_PER_EMPLEADOS WHERE CEMP_CODEMP = VB.CO_EMP_VB) NOMBRE,\n" +
                    "VB.CO_DEP CO_DEPENDENCIA,PK_SGD_DESCRIPCION.DE_SIGLA(VB.CO_DEP) DE_DEPENDENCIA,DECODE(VB.IN_VB,'B','0',VB.IN_VB) IN_VOBO\n" +
                    "FROM TDTV_PERSONAL_VB VB\n" +
                    "WHERE VB.NU_ANN=?\n" +
                    "AND VB.NU_EMI=?\n" +
                    "ORDER BY NOMBRE");

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
        sql.append(" SELECT DECODE(es_obl_firma,'0','N','F')\n" +
                    "FROM sitm_doc_dependencia\n" +
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
        
//        if(sDescCiudadano != null){
//           sDescCiudadano = sDescCiudadano.replace(" ", "%"); 
//        }
        sql.append("select I.NULEM NU_DOCUMENTO,substr(RTRIM(I.DEAPP)||' '||RTRIM(I.DEAPM)||' '||RTRIM(I.DENOM),1,200) NOMBRE,\n"
                + "NVL(UBDEP,'') ID_DEPARTAMENTO, NVL(UBPRV,'') ID_PROVINCIA, NVL(UBDIS,'') ID_DISTRITO, NVL(DEDOMICIL,'') DE_DIRECCION, NVL(DEEMAIL,'') DE_CORREO,NVL(DETELEFO,'') TELEFONO \n"
                + "from IDTANIRS I \n"
                + "where\n"
                + "(substr(RTRIM(I.DEAPP)||' '||RTRIM(I.DEAPM)||' '||RTRIM(I.DENOM),1,200)) like '%'||?||'%'\n"
//                + "(substr(RTRIM(I.DEAPP)||' '||RTRIM(I.DEAPM)||' '||RTRIM(I.DENOM),1,200)) like '%'||?||'%' OR\n"
//                + "(substr(RTRIM(I.DEAPP)||' '||RTRIM(I.DENOM)||' '||RTRIM(I.DEAPM),1,200)) like '%'||?||'%' OR\n"
//                + "(substr(RTRIM(I.DEAPM)||' '||RTRIM(I.DEAPP)||' '||RTRIM(I.DENOM),1,200)) like '%'||?||'%' OR\n"
//                + "(substr(RTRIM(I.DENOM)||' '||RTRIM(I.DEAPP)||' '||RTRIM(I.DEAPM),1,200)) like '%'||?||'%' OR\n"
//                + "(substr(RTRIM(I.DEAPM)||' '||RTRIM(I.DENOM)||' '||RTRIM(I.DEAPP),1,200)) like '%'||?||'%' OR\n"
//                + "(substr(RTRIM(I.DENOM)||' '||RTRIM(I.DEAPM)||' '||RTRIM(I.DEAPP),1,200)) like '%'||?||'%' \n"
                + "order by 2");

        CiudadanoBean ciudadanoBean = new CiudadanoBean();
        try {
              list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(CiudadanoBean.class),
                    new Object[]{sDescCiudadano.toUpperCase()});
//            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(CiudadanoBean.class),
//                    new Object[]{sDescCiudadano.toUpperCase(), sDescCiudadano.toUpperCase(), sDescCiudadano.toUpperCase(),
//                                 sDescCiudadano.toUpperCase(), sDescCiudadano.toUpperCase(), sDescCiudadano.toUpperCase()});
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
        sql.append("SELECT CELE_DESELE,\n" +
                    "CELE_DESCOR \n" +
                    "FROM SI_ELEMENTO\n" +
                    "WHERE SI_ELEMENTO.CTAB_CODTAB='MSJ_DEPENDENCIA'\n" +
                    "order by SI_ELEMENTO.NELE_NUMSEC");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(SiElementoBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
     @Override
    public String obtenerValorParametro(String nombreParametro){
        StringBuilder sql = new StringBuilder();
        String result = null;
        sql.append("SELECT UPPER(DE_PAR) DE_PAR FROM TDTR_PARAMETROS WHERE CO_PAR=?");        
        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{nombreParametro});
        } catch (EmptyResultDataAccessException e) {
            result = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;        
    }

    @Override
    public List<CiudadanoBean> getLstCiudadanos(String sApePatCiudadano, String sApeMatCiudadano, String sNombreCiudadano) {
        List<CiudadanoBean> list = new ArrayList<CiudadanoBean>();
        StringBuilder sql = new StringBuilder();
        String vApePatCiudadano =sApePatCiudadano==null? "":sApePatCiudadano.toUpperCase();
        String vApeMatCiudadano =sApeMatCiudadano==null? "":sApeMatCiudadano.toUpperCase();
        String vNombreCiudadano =sNombreCiudadano==null? "":sNombreCiudadano.toUpperCase();
        
        Map<String, Object> objectParam = new HashMap<String, Object>();
        
        sql.append("select I.NULEM NU_DOCUMENTO,substr(RTRIM(I.DEAPP)||' '||RTRIM(I.DEAPM)||' '||RTRIM(I.DENOM),1,200) NOMBRE,\n"
                + "NVL(UBDEP,'') ID_DEPARTAMENTO, NVL(UBPRV,'') ID_PROVINCIA, NVL(UBDIS,'') ID_DISTRITO, NVL(DEDOMICIL,'') DE_DIRECCION, NVL(DEEMAIL,'') DE_CORREO,NVL(DETELEFO,'') TELEFONO \n"
                + "from IDTANIRS I \n"
                + "where\n"
                //+ "(substr(RTRIM(I.DEAPP)||' '||RTRIM(I.DEAPM)||' '||RTRIM(I.DENOM),1,200)) like '%'||?||'%'\n"
                
                +" I.DEAPP = :papePat ");
                objectParam.put("papePat", vApePatCiudadano);
        
                if(!vApeMatCiudadano.equals("")){
                    sql.append("AND ");
                    sql.append(" I.DEAPM = :papeMat ");
                    objectParam.put("papeMat", vApeMatCiudadano);
                }
                
                if(!vNombreCiudadano.equals("")){
                    sql.append("AND ");
                    sql.append(" I.DENOM LIKE '%'||:pNom||'%' ");
                    objectParam.put("pNom", vNombreCiudadano);
                }
                               
                sql.append("order by 2");

        CiudadanoBean ciudadanoBean = new CiudadanoBean();
        try {
              //list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(CiudadanoBean.class),objectParam);
                list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(CiudadanoBean.class));
        } catch (EmptyResultDataAccessException e) {
            ciudadanoBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
/*interoperabilidad*/
    @Override
    public DatosInterBean DatosInter(String nuAnn, String nuEmi) {
        
        DatosInterBean datosInter=new DatosInterBean();
        
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT (CASE WHEN LENGTH(A.NU_RUC_DES)>0 THEN A.NU_RUC_DES ELSE '0' END) AS NU_RUC_DES,"+
                "(CASE WHEN LENGTH(A.DE_NOM_DES)>0 THEN a.DE_NOM_DES ELSE \n" +
                " CASE WHEN LENGTH(A.REMI_NU_DNI_EMI)>0 THEN PK_SGD_DESCRIPCION.ANI_SIMIL(a.REMI_NU_DNI_EMI) ELSE \n" +
                " CASE WHEN LENGTH(A.REMI_CO_OTR_ORI_EMI)>0 THEN PK_SGD_DESCRIPCION.OTRO_ORIGEN(a.remi_co_otr_ori_emi) ELSE '' END END END) DE_NOM_DES,\n"+
                "(CASE WHEN LENGTH(A.DE_DEP_DES)>0 THEN DE_DEP_DES ELSE '' END) DE_DEP_DES,\n"+
                "(CASE WHEN LENGTH(A.DE_CAR_DES)>0 THEN DE_CAR_DES ELSE A.DE_CARGO END) DE_CAR_DES FROM TDTV_DESTINOS A INNER JOIN TDTV_REMITOS B ON A.NU_ANN=B.NU_ANN AND A.NU_EMI=B.NU_EMI \n" +
                "WHERE A.NU_ANN=?\n" +
                "AND A.NU_EMI=?\n" +
                "AND A.NU_DES=1 AND B.CO_TIP_DOC_ADM IN (SELECT CDOC_TIPDOC FROM IOTDTX_TIPO_DOCUMENTO)");
                //"AND A.NU_DES=1 AND B.CO_TIP_DOC_ADM IN (SELECT CDOC_TIPDOC FROM SI_MAE_TIPO_DOC)");

        try {
             datosInter = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DatosInterBean.class), new Object[]{nuAnn,nuEmi});
//            vReturn="OK|"+coMot;
        } catch (EmptyResultDataAccessException e) {
//            vReturn="ERROR|";
            datosInter=null;
        } catch (Exception e) {
            e.printStackTrace();
            datosInter=null;
        }
        
        return datosInter;
    }
    
    /*interoperabilidad*/    
    /* HPB - [INICIO - 13/01/2020] - Integrar PIDE */
    /*
     public CiudadanoBeanNew getLstCiudadanoStored(String pnuDoc, String pcoTipoDoc) {
        CiudadanoBeanNew ciudadanoBeanNew = new CiudadanoBeanNew();
        try{
            if(pnuDoc!=null){
                SimpleJdbcCall call = new SimpleJdbcCall(this.dataSource)
                    .withCatalogName("PK_SEV_PROCESA_PERSONA")
                    .withFunctionName("DATOS_PERSONA")
                    .withoutProcedureColumnMetaDataAccess()
                    .returningResultSet("c_cursor", BeanPropertyRowMapper.newInstance(CiudadanoBeanNew.class))
                    .useInParameterNames("TDOC", "NDOC")                     
                    .declareParameters(
                        new SqlOutParameter("c_cursor", Types.REF_CURSOR),                                                 
                        new SqlParameter("TDOC", Types.VARCHAR),
                        new SqlParameter("NDOC", Types.VARCHAR));
                
                SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("TDOC", pcoTipoDoc)
                    .addValue("NDOC", pnuDoc);
                    
                Map out;
                try {
                    out = (Map) call.execute(in);
                    List<CiudadanoBeanNew> listContacts = (List<CiudadanoBeanNew>) out.get("c_cursor");
                    
                    ciudadanoBeanNew.setCpersonaApepat(listContacts.get(0).getCpersonaApepat());
                    ciudadanoBeanNew.setCpersonaApemat(listContacts.get(0).getCpersonaApemat());
                    ciudadanoBeanNew.setCpersonaNombre(listContacts.get(0).getCpersonaNombre());
                    ciudadanoBeanNew.setCpersonaUbigeo(listContacts.get(0).getCpersonaUbigeo());
                    ciudadanoBeanNew.setCpersonaDirec(listContacts.get(0).getCpersonaDirec());
                    ciudadanoBeanNew.setNpersonaId(listContacts.get(0).getNpersonaId());
                    ciudadanoBeanNew.setCpersonaNrodoc(listContacts.get(0).getCpersonaNrodoc());
                } catch (Exception e) {
                    e.printStackTrace();
                    ciudadanoBeanNew = null;
                    throw e;
                }                
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            ciudadanoBeanNew = null;
        }  
        return ciudadanoBeanNew;        
    }  
    */
    /*YPA-SIS modifica el package para apuntar a la BD del SGD del SIS consume web sercices PIDE*/
    @Override
    public CiudadanoBeanNew getLstCiudadanoStored(String pnuDoc, String pcoTipoDoc) {
        CiudadanoBeanNew ciudadanoBeanNew = new CiudadanoBeanNew();
        try{
            if(pnuDoc!=null){
                SimpleJdbcCall call = new SimpleJdbcCall(this.dataSource)
                    .withCatalogName("PK_SGD_TRAMITE")
                    .withFunctionName("FU_GET_DATOS_DNI")
                    .withoutProcedureColumnMetaDataAccess()
                    .returningResultSet("c_cursor", BeanPropertyRowMapper.newInstance(CiudadanoBeanNew.class))
                    .useInParameterNames("p_vCO_TIPO_DOC", "p_vNU_DNI")                     
                    .declareParameters(
                        new SqlOutParameter("c_cursor", Types.REF_CURSOR),                                                 
                        new SqlParameter("p_vCO_TIPO_DOC", Types.VARCHAR),
                        new SqlParameter("p_vNU_DNI", Types.VARCHAR));
                
                SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("p_vCO_TIPO_DOC", pcoTipoDoc)
                    .addValue("p_vNU_DNI", pnuDoc);
                    
                Map out;
                try {
                    out = (Map) call.execute(in);
                    List<CiudadanoBeanNew> listContacts = (List<CiudadanoBeanNew>) out.get("c_cursor");
                    
                    ciudadanoBeanNew.setCpersonaApepat(listContacts.get(0).getCpersonaApepat());
                    ciudadanoBeanNew.setCpersonaApemat(listContacts.get(0).getCpersonaApemat());
                    ciudadanoBeanNew.setCpersonaNombre(listContacts.get(0).getCpersonaNombre());
                    ciudadanoBeanNew.setCpersonaUbigeo(listContacts.get(0).getCpersonaUbigeo());
                    ciudadanoBeanNew.setCpersonaDirec(listContacts.get(0).getCpersonaDirec());
                    ciudadanoBeanNew.setNpersonaId(listContacts.get(0).getNpersonaId());
                    ciudadanoBeanNew.setCpersonaNrodoc(listContacts.get(0).getCpersonaNrodoc());
                } catch (Exception e) {
                    e.printStackTrace();
                    ciudadanoBeanNew = null;
                    throw e;
                }                
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            ciudadanoBeanNew = null;
        }  
        return ciudadanoBeanNew;        
    }    

    @Override
    public CiudadanoBean getLstCiudadanoAni(String pnuDoc, String pcoTipoDoc) {
        CiudadanoBean ciudadanoBean = new CiudadanoBean();
        try{
            if(pnuDoc!=null){
                SimpleJdbcCall call = new SimpleJdbcCall(this.dataSource)
                    .withCatalogName("PK_SGD_TRAMITE")
                    .withFunctionName("FU_GET_DATOS_ANI")
                    .withoutProcedureColumnMetaDataAccess()
                    .returningResultSet("vCU_DATOS_CIUDADANO", BeanPropertyRowMapper.newInstance(CiudadanoBean.class))
                    .useInParameterNames("p_vCO_TIPO_DOC", "p_vNU_DNI")                     
                    .declareParameters(
                        new SqlOutParameter("vCU_DATOS_CIUDADANO", Types.REF_CURSOR),                                                 
                        new SqlParameter("p_vCO_TIPO_DOC", Types.VARCHAR),
                        new SqlParameter("p_vNU_DNI", Types.VARCHAR));
                
                SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("p_vCO_TIPO_DOC", pcoTipoDoc)
                    .addValue("p_vNU_DNI", pnuDoc);
                    
                Map out;
                try {
                    out = (Map) call.execute(in);
                    List<CiudadanoBean> listContacts = (List<CiudadanoBean>) out.get("vCU_DATOS_CIUDADANO");
                     
                    ciudadanoBean.setNuDocumento(listContacts.get(0).getNuDocumento());
                    ciudadanoBean.setNombre(listContacts.get(0).getNombre());
                    ciudadanoBean.setIdDepartamento(listContacts.get(0).getIdDepartamento());
                    ciudadanoBean.setIdProvincia(listContacts.get(0).getIdProvincia());
                    ciudadanoBean.setIdDistrito(listContacts.get(0).getIdDistrito());
                    ciudadanoBean.setDeDireccion(listContacts.get(0).getDeDireccion());
                    ciudadanoBean.setDeCorreo(listContacts.get(0).getDeCorreo());
                    ciudadanoBean.setTelefono(listContacts.get(0).getTelefono());
                    ciudadanoBean.setUbigeo(listContacts.get(0).getUbigeo());

                } catch (Exception e) {
                    e.printStackTrace();
                    ciudadanoBean = null;
                    throw e;
                }                
            }
        } catch (Exception e) {
            e.printStackTrace();
            ciudadanoBean = null;
        }         
        return ciudadanoBean;         
    }    

    @Override
    public ProveedorBean getLstProveedor(String pnuRuc) {
        ProveedorBean proveedorBean = new ProveedorBean();
        try{
            if(pnuRuc!=null){
                SimpleJdbcCall call = new SimpleJdbcCall(this.dataSource)
                    .withCatalogName("PK_SGD_TRAMITE")
                    .withFunctionName("FU_GET_DATOS_RUC")
                    .withoutProcedureColumnMetaDataAccess()
                    .returningResultSet("vCU_DATOS_RUC", BeanPropertyRowMapper.newInstance(ProveedorBean.class))
                    .useInParameterNames("p_vNU_RUC")                     
                    .declareParameters(
                        new SqlOutParameter("vCU_DATOS_RUC", Types.REF_CURSOR),                                                 
                        new SqlParameter("p_vNU_RUC", Types.VARCHAR));
                
                SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("p_vNU_RUC", pnuRuc);
                    
                Map out;
                try {
                    out = (Map) call.execute(in);
                    List<ProveedorBean> listProveedor = (List<ProveedorBean>) out.get("vCU_DATOS_RUC");
                     
                    proveedorBean.setNuRuc(listProveedor.get(0).getNuRuc());
                    proveedorBean.setDescripcion(listProveedor.get(0).getDescripcion());
                    proveedorBean.setIdDepartamento(listProveedor.get(0).getIdDepartamento());
                    proveedorBean.setIdProvincia(listProveedor.get(0).getIdProvincia());
                    proveedorBean.setIdDistrito(listProveedor.get(0).getIdDistrito());
                    proveedorBean.setDeDireccion(listProveedor.get(0).getDeDireccion());
                    proveedorBean.setDeCorreo(listProveedor.get(0).getDeCorreo());
                    proveedorBean.setTelefono(listProveedor.get(0).getTelefono());
                } catch (Exception e) {
                    e.printStackTrace();
                    proveedorBean = null;
                    throw e;
                }                
            }
        } catch (Exception e) {
            e.printStackTrace();
            proveedorBean = null;
        }         
        return proveedorBean;                 
    }

    @Override
    public String getDescripcionUbigeo(String pcoDep, String pcoPrv, String pcoDis, String pcoCar) throws Exception {
        try{
            if(pcoDep!=null && pcoPrv!=null && pcoDis!=null){
                SimpleJdbcCall simpleJdbcCall = 
                new SimpleJdbcCall(this.dataSource).withCatalogName("PK_SGD_TRAMITE").withFunctionName("FU_GET_UBIGEO")
                    .withoutProcedureColumnMetaDataAccess()
                    .useInParameterNames("p_vCO_DEP", "p_vCO_PRV", "p_vCO_DIS", "p_vCO_CAR")
                    .declareParameters(
                        new SqlOutParameter("RESULT", Types.VARCHAR),
                        new SqlParameter("p_vCO_DEP", Types.VARCHAR),
                        new SqlParameter("p_vCO_PRV", Types.VARCHAR),
                        new SqlParameter("p_vCO_DIS", Types.VARCHAR),
                        new SqlParameter("p_vCO_CAR", Types.VARCHAR));
                
                SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("p_vCO_DEP", pcoDep)
                    .addValue("p_vCO_PRV", pcoPrv)
                    .addValue("p_vCO_DIS", pcoDis)
                    .addValue("p_vCO_CAR", pcoCar);
                    
                Map out;
                String mensaje= null;
                try {
                     out = simpleJdbcCall.execute(in);
                     mensaje = (String)out.get("RESULT");
                } catch (Exception e) {
                     e.printStackTrace();
                     throw e;
                }
                return mensaje;
            } else {
                return "Sin retorno valor autorizado";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }    
    }
    /* HPB - [FIN - 13/01/2020] - Integrar PIDE */
    /*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
    @Override
    public ProveedorBean getProveedorExpediente(String pnuRuc) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CPRO_RUC NU_RUC, CPRO_RAZSOC DESCRIPCION, \n"
                + "TRIM(NODEP) NODEP,TRIM(NOPRV) NOPRV,TRIM(NODIS) NODIS, TRIM(CUBI_CODDEP) CUBI_CODDEP,TRIM(CUBI_CODPRO) CUBI_CODPRO,TRIM(CUBI_CODDIS) CUBI_CODDIS, \n"
                + "NVL(CUBI_CODDEP,'') ID_DEPARTAMENTO, NVL(CUBI_CODPRO,'') ID_PROVINCIA, NVL(CUBI_CODDIS,'') ID_DISTRITO, NVL(CPRO_DOMICIL,'') DE_DIRECCION, NVL(CPRO_EMAIL,'') DE_CORREO,NVL(CPRO_TELEFO,'') TELEFONO \n"
                +"FROM LG_PRO_PROVEEDOR LEFT JOIN IDTUBIAS ON UBDEP||UBPRV||UBDIS=CUBI_CODDEP||CUBI_CODPRO||CUBI_CODDIS \n"
                +"where CPRO_RUC=?");

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
    public DestinatarioOtroOrigenBean getDestinatarioOtroOrigenExpediente(String pnuCodigo) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DE_APE_PAT_OTR||' '|| DE_APE_MAT_OTR||', '|| DE_NOM_OTR || ' - ' || DE_RAZ_SOC_OTR DESCRIPCION,\n"
                + " PK_SGD_DESCRIPCION.DE_DOMINIOS('TIP_DOC_IDENT', CO_TIP_OTR_ORI ) TIPO_DOC_IDENTIDAD,\n"
                + " NU_DOC_OTR_ORI NRO_DOC_IDENTIDAD, CO_OTR_ORI CO_OTRO_ORIGEN,\n"
                + "NVL(UB_DEP,'') ID_DEPARTAMENTO, NVL(UB_PRO,'') ID_PROVINCIA, NVL(UB_DIS,'') ID_DISTRITO, NVL(DE_DIR_OTRO_ORI,'') DE_DIRECCION, NVL(DE_EMAIL,'') DE_CORREO,NVL(DE_TELEFO,'') TELEFONO \n"
                + "  ,UB_DEP,UB_PRO,UB_DIS\n" 
                + "  ,trim(UB.NODEP)||' '||trim(UB.NOPRV)||' '||trim(UB.NODIS) ubigeo,DE_EMAIL,a.DE_TELEFO,a.DE_DIR_OTRO_ORI\n" 
                + " ,trim(UB.NODEP) NODEP, trim(UB.NOPRV) NOPRV, trim(UB.NODIS) NODIS  \n"                
                + "  FROM TDTR_OTRO_ORIGEN a\n"
                +" LEFT JOIN IDTUBIAS UB ON UB.UBDEP=a.UB_DEP AND UB.UBPRV=a.UB_PRO AND UB.UBDIS=a.UB_DIS\n" 
                + " where CO_OTR_ORI =? and nvl(CO_TIP_OTR_ORI,'--') not in('09')\n"
                + " order by 1 desc");       
        
        DestinatarioOtroOrigenBean destinatarioOtroOrigenBean=null;
        try {
            destinatarioOtroOrigenBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioOtroOrigenBean.class),
                    new Object[]{pnuCodigo});
        } catch (EmptyResultDataAccessException e) {
            destinatarioOtroOrigenBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destinatarioOtroOrigenBean;         
    }
    /*HPB 11/02/2020 - Fin - Requerimiento Remitente expediente*/    
    /*[HPB] 06/11/20 Modificaciones en el envío de documentos. Listado de entidades que interoperan*/
    @Override
    public String obtenerIndicadorEntidadPide(String nroRuc) {
        StringBuilder sql = new StringBuilder();
        String result = null;
        sql.append("SELECT A.IN_ENT_MPV\n" +
                   "  FROM LG_PRO_PROVEEDOR A\n" +
                   " WHERE A.IN_ENT_MPV = '1'\n" +
                   "   AND A.FE_INI_MPV IS NOT NULL\n" +
                   "   AND A.CPRO_RUC =?");        
        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{nroRuc});
        } catch (EmptyResultDataAccessException e) {
            result = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;  
    }
}
