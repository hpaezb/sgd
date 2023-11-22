/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.postgresql;

import pe.gob.onpe.tramitedoc.dao.MaestrosDao;
import java.util.ArrayList;
import java.util.List;
import pe.gob.onpe.tramitedoc.bean.AnnioBean;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.DepartamentoBean;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioBean;
import pe.gob.onpe.tramitedoc.bean.DistritoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.EstadoDocumentoBean;
import pe.gob.onpe.tramitedoc.bean.EtiquetaBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteBean;
import pe.gob.onpe.tramitedoc.bean.GrupoDestinatarioBean;
import pe.gob.onpe.tramitedoc.bean.LocalBean;
import pe.gob.onpe.tramitedoc.bean.MotivoBean;
import pe.gob.onpe.tramitedoc.bean.OrigenDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.PrioridadDocumentoBean;
import pe.gob.onpe.tramitedoc.bean.ProvinciaBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;
import pe.gob.onpe.tramitedoc.bean.TipoDestinatarioEmiBean;
import pe.gob.onpe.tramitedoc.bean.TipoDocumentoBean;
import pe.gob.onpe.tramitedoc.bean.TipoEnvioMsj;
import pe.gob.onpe.tramitedoc.bean.TipoExpedienteEmiBean;
import pe.gob.onpe.tramitedoc.bean.TupaExpedienteBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.bean.VencimientoBean;

/**
 *
 * @author ECueva
 */
@Repository("maestrosDao")
public class MaestrosDaoImp extends SimpleJdbcDaoBase implements MaestrosDao{

    @Override
        public List<AnnioBean> listAnnioEjec() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CANO_ANOEJE CANO, CANO_ANOEJE DE_ANIO ");
        sql.append("FROM IDOSGD.SI_MAE_ANO_EJECUCION UNION SELECT '.:TODOS.:', ");
        sql.append("NULL ORDER BY 1 DESC ");
        
        List<AnnioBean> list = new ArrayList<AnnioBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(AnnioBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }

        return list;
    }
    
    @Override
        public List<EstadoDocumentoBean> listEstadosDocumento(String nomTabla) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DE_EST,CO_EST ");
        sql.append("FROM IDOSGD.TDTR_ESTADOS ");
        sql.append("WHERE DE_TAB = ? ");
        sql.append("UNION ");
        sql.append("SELECT '.:TODOS.:',NULL  ORDER BY CO_EST ");
        
        List<EstadoDocumentoBean> list = new ArrayList<EstadoDocumentoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(EstadoDocumentoBean.class),
                    new Object[]{nomTabla});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }

        return list;
    }
    
    @Override
        public List<PrioridadDocumentoBean> listPrioridadDocumento() {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DE_PRI,CO_PRI ");
        sql.append("FROM IDOSGD.TDTR_PRIORIDAD ");
        sql.append("UNION ");
        sql.append("SELECT '.:TODOS.:',NULL ORDER BY 2");
        
        List<PrioridadDocumentoBean> list = new ArrayList<PrioridadDocumentoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(PrioridadDocumentoBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }

        return list;
    }    
    
    @Override
        public List<TipoDocumentoBean> listTipoDocumento(String coDependencia) {
        String sql = "SELECT DISTINCT\n"
                + "B.CDOC_DESDOC DE_TIP_DOC, \n"
                + "CO_DOC CO_TIP_DOC \n"
                + "from IDOSGD.tdtx_resumen_doc A , IDOSGD.SI_MAE_TIPO_DOC B\n"
                + "WHERE ti_tab = 'D'\n"
                + "AND A.CO_DOC = B.CDOC_TIPDOC\n"
                + "AND A.CO_DEP= ?\n"
                + "UNION\n"
                + "SELECT '.:TODOS:.' DESCRI ,NULL CODIGO ORDER BY 1 ";
        
        List<TipoDocumentoBean> list = new ArrayList<TipoDocumentoBean>();

        try {
            list = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(TipoDocumentoBean.class), new Object[]{coDependencia});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }

        return list;
    }
    
    @Override
        public List<TipoDocumentoBean> listTipoDocumentoEmi(String coDependencia) {             
        String sql = "SELECT  UPPER(A.CDOC_DESDOC) DE_TIP_DOC,A.CDOC_TIPDOC CO_TIP_DOC \n"
                + "FROM IDOSGD.SI_MAE_TIPO_DOC A,IDOSGD.SITM_DOC_DEPENDENCIA B\n"
                + "WHERE A.CDOC_INDBAJ ='0'\n"
                + "AND A.CDOC_TIPDOC = B.CO_TIP_DOC\n"
                + "AND B.CO_DEP=?\n"
                + "UNION\n"
                + "SELECT '.:TODOS:.' DESCRI ,NULL CODIGO ORDER BY 1";        
        List<TipoDocumentoBean> list = new ArrayList<TipoDocumentoBean>();

        try {
            list = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(TipoDocumentoBean.class), new Object[]{coDependencia});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }

        return list;
    }    
    
    @Override
        public List<ExpedienteBean> listExpedientes(String coDependencia) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT DE_EXP, CO_EXP ");
        sql.append("FROM IDOSGD.TDTR_EXPEDIENTE ");
        sql.append("WHERE CO_DEP = ? ");
        sql.append("UNION SELECT '.: TODOS :.', NULL  ");
        sql.append("UNION SELECT '.:SIN EXPEDIENTE:.', 'SINEX' ORDER BY 1 ");
        
        List<ExpedienteBean> list = new ArrayList<ExpedienteBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ExpedienteBean.class), new Object[]{coDependencia});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }

        return list;
    }    

    @Override
        public List<RemitenteBean> listRemitente(String annio, UsuarioConfigBean usuarioConfigBean) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select distinct   ");
        sql.append("	DE_DEPENDENCIA descrip,");
        sql.append("	CO_DEPENDENCIA cod_dep,");
        sql.append("	b.de_corta_depen");
        sql.append("  from IDOSGD.tdtx_resumen_dep a , IDOSGD.RHTM_DEPENDENCIA b");
        sql.append(" where a.nu_ann = ?/*:B_01_ANN.DE_ANIO*/");
        sql.append("   and a.co_dep_ref = ?/*:global.vcod_dep*/");
        sql.append("   and a.ti_tab='D'");
        sql.append("   and a.ti_emi = '01'");
        sql.append("   AND (a.co_emp_res = (CASE WHEN ?='0' THEN a.co_emp_res ELSE ? END) ");
        sql.append("	   OR ( a.co_emp_res IS NULL )  )");
        sql.append("   and a.CO_DEP = b.CO_DEPENDENCIA");
        sql.append(" union  ");
        sql.append("select distinct   ");
//        sql.append("  DECODE(a.ti_emi,");
//        sql.append("	'02', ' [PROVEEDORES]',");
//        sql.append("	'03', ' [CIUDADANOS]',");
//        sql.append("	'04', ' [OTROS]',");
//        sql.append("	'05', ' [PERSONALES]') descrip,");

        sql.append(" CASE \n" +
                    "          WHEN    a.ti_emi='02' THEN ' [PROVEEDORES]' \n" +
                    "          WHEN    a.ti_emi='03' THEN ' [CIUDADANOS]' \n" +
                    "          WHEN    a.ti_emi='04' THEN ' [OTROS]' END AS descrip,\n" );
        sql.append("  a.co_dep,");
        sql.append("  NULL");
        sql.append("  from IDOSGD.tdtx_resumen_dep a  ");
        sql.append(" where a.nu_ann = ?/*:B_01_ANN.DE_ANIO*/");
        sql.append("   and a.co_dep_ref = ?/*:global.vcod_dep*/");
        sql.append("   and a.ti_tab='D'");
        sql.append("   and a.ti_emi <> '01'");
        sql.append("   AND (a.co_emp_res = (CASE WHEN ?='0' THEN a.co_emp_res ELSE ? END) ");
        sql.append("	   OR ( a.co_emp_res IS NULL )  )");
        sql.append(" UNION");
        sql.append(" SELECT ' [TODOS]',");
        sql.append("	   NULL,");
        sql.append("       NULL");
        //sql.append("  FROM dual");
        sql.append(" ORDER BY 1");

        
        List<RemitenteBean> list = new ArrayList<RemitenteBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(RemitenteBean.class), new Object[]{annio,usuarioConfigBean.getCoDep(),
            usuarioConfigBean.getTiAcceso(),usuarioConfigBean.getCempCodemp(),annio,usuarioConfigBean.getCoDep(),usuarioConfigBean.getTiAcceso(),usuarioConfigBean.getCempCodemp()});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }        

    @Override
        public List<DestinatarioBean> listDestinatario(String annio, String coDependencia,String ptiAcceso,String pcoEmpleado) {
        StringBuilder sql = new StringBuilder();
        boolean bquery=false;
        
        if(ptiAcceso!=null&&(ptiAcceso.equals("0")||ptiAcceso.equals("2"))){
            sql.append("SELECT SUBSTR(IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(CO_EMP_RES),1,200) nombres , CO_EMP_RES CO_EMP_DES        ");
            sql.append("FROM ( select DISTINCT CO_EMP_RES ");
            sql.append("		 from IDOSGD.TDTX_RESUMEN_DEP");
            sql.append("		where NU_ANN = ?/*:B_01_ANN.DE_ANIO*/");
            sql.append("		  AND co_dep_REF = ?/*:global.vcod_dep*/");
            sql.append("		  AND TI_TAB = 'D') A");
            sql.append(" UNION");
            sql.append(" SELECT ' [TODOS]',");
            sql.append("	   NULL");
            //sql.append("  FROM dual");
            sql.append(" ORDER BY 1"); 
            bquery=true;
        }else{//personal
            sql.append("SELECT SUBSTR(IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(CO_EMP_RES),1,200) nombres , CO_EMP_RES CO_EMP_DES        ");
            sql.append("FROM ( select DISTINCT CO_EMP_RES ");
            sql.append("		 from IDOSGD.TDTX_RESUMEN_DEP");
            sql.append("		where NU_ANN = ?/*:B_01_ANN.DE_ANIO*/");
            sql.append("		  AND co_dep_REF = ?/*:global.vcod_dep*/");
            sql.append("		  AND TI_TAB = 'D' and CO_EMP_RES=?) A");
            sql.append(" UNION");
            sql.append(" SELECT ' [TODOS]',");
            sql.append("	   NULL");
            //sql.append("  FROM dual");
            sql.append(" ORDER BY 1");              
        }

        
        List<DestinatarioBean> list = new ArrayList<DestinatarioBean>();

        try {
            if(bquery){
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioBean.class), new Object[]{annio,coDependencia});    
            }else{
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioBean.class), new Object[]{annio,coDependencia,pcoEmpleado});
            }
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<EmpleadoBean> listEmpleadoDependencia(String pcodDependencia){
        StringBuilder sql = new StringBuilder();
       
        sql.append("SELECT e.cemp_apepat,e.cemp_apemat,e.CEMP_DENOM, e.CEMP_CODEMP\n" +
        "FROM IDOSGD.RHTM_PER_EMPLEADOS e\n" +
        "where e.CEMP_EST_EMP = '1'\n" +
        "and (CEMP_CO_DEPEND = ?\n" +
        "or CO_DEPENDENCIA = ?\n" +
        ")\n" +
        "ORDER BY 1");
        
        List<EmpleadoBean> list = new ArrayList<EmpleadoBean>();
        
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class), new Object[]{pcodDependencia,pcodDependencia});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
        public List<RemitenteBean> listReferenciaOrigen(String annio, UsuarioConfigBean usuarioConfigBean) {
        StringBuilder sql = new StringBuilder();
        
        sql.append(" select distinct   ");
        sql.append(" 	b.DE_DEPENDENCIA descrip,");
        sql.append(" 	b.CO_DEPENDENCIA cod_dep,");
        sql.append(" 	b.DE_CORTA_DEPEN");
        sql.append("   from IDOSGD.tdtx_resumen_dep a , IDOSGD.RHTM_DEPENDENCIA b");
        sql.append("  where a.nu_ann = ?/*:b_01_ann.de_anio*/");
        sql.append("    and a.co_dep = ?/*:GLOBAL.vcod_dep*/");
        sql.append("    and a.ti_tab='R'");
        sql.append("    and a.ti_emi = '01'");
        sql.append("    and a.co_emp_res = (CASE WHEN ?='0' THEN a.co_emp_res ELSE ? END) ");
        sql.append("    and a.CO_DEP_REF = b.CO_DEPENDENCIA");
        sql.append(" union  ");
        sql.append(" select distinct   ");
        sql.append(" 	CASE ");
        sql.append(" 	WHEN a.ti_emi='02' THEN ' [PROVEEDORES]'");
        sql.append(" 	WHEN a.ti_emi='03' THEN ' [CIUDADANOS]'");
        sql.append(" 	WHEN a.ti_emi='04' THEN ' [OTROS]'");
        sql.append(" 	WHEN a.ti_emi='05' THEN ' [PERSONALES]' END AS descrip,");
        sql.append(" 	a.co_dep_ref cod_dep,");
        sql.append(" 	NULL");
        sql.append("   from IDOSGD.tdtx_resumen_dep a  ");
        sql.append("  where a.nu_ann = ?/*:b_01_ann.de_anio*/");
        sql.append("    and a.co_dep = ?/*:GLOBAL.vcod_dep*/");
        sql.append("    and a.ti_tab='R'");
        sql.append("    and a.ti_emi <> '01'");
        sql.append("    and a.co_emp_res = (CASE WHEN ?='0' THEN a.co_emp_res ELSE ? END) ");
        sql.append("  UNION");
        sql.append(" SELECT ' [TODOS]',");
        sql.append(" 	   NULL,");
        sql.append(" 	   NULL");
        //sql.append("   FROM dual");
        sql.append("  ORDER BY 1");

        
        List<RemitenteBean> list = new ArrayList<RemitenteBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(RemitenteBean.class), new Object[]{annio,usuarioConfigBean.getCoDep(),
            usuarioConfigBean.getTiAcceso(),usuarioConfigBean.getCempCodemp(),annio,usuarioConfigBean.getCoDep(),usuarioConfigBean.getTiAcceso(),usuarioConfigBean.getCempCodemp()});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
        @Override
        public List<DependenciaBean> listDestinatarioEmi(String annio, UsuarioConfigBean usuarioConfigBean) {
        StringBuilder sql = new StringBuilder();
        
        sql.append(" select distinct   ");
        sql.append(" 	b.DE_DEPENDENCIA deDependencia,");
        sql.append(" 	a.co_dep_ref coDependencia,");
        sql.append(" 	b.DE_CORTA_DEPEN");
        sql.append("   from IDOSGD.tdtx_resumen_dep a , IDOSGD.RHTM_DEPENDENCIA b");
        sql.append("  where a.nu_ann = ?/*:b_01_ann.de_anio*/");
        sql.append("    and a.co_dep = ?/*:global.vcod_dep*/");
        sql.append("    and a.ti_tab='D'");
        sql.append("    and a.ti_emi = '01'");
        sql.append("    and a.co_emp_res = (CASE WHEN ?='0' OR ?='2' THEN a.co_emp_res ELSE ? END) ");
        sql.append("    and a.CO_DEP_ref = b.CO_DEPENDENCIA");
        sql.append(" union  ");
        sql.append(" select distinct   ");
//        sql.append("   DECODE(a.ti_emi,");
//        sql.append(" '02', ' [PROVEEDORES]',");
//        sql.append(" '03', ' [CIUDADANOS]',");
//        sql.append(" '04', ' [OTROS]',");
//        sql.append(" '05', ' [PERSONALES]') deDependencia,");
        sql.append(" 	CASE ");
        sql.append(" 	WHEN a.ti_emi='02' THEN ' [PROVEEDORES]'");
        sql.append(" 	WHEN a.ti_emi='03' THEN ' [CIUDADANOS]'");
        sql.append(" 	WHEN a.ti_emi='04' THEN ' [OTROS]'");
        sql.append(" 	WHEN a.ti_emi='05' THEN ' [PERSONALES]' END AS deDependencia,");
        sql.append("   a.co_dep_ref coDependencia,");
        sql.append("   NULL");
        sql.append("   from IDOSGD.tdtx_resumen_dep a  ");
        sql.append("  where a.nu_ann = ?/*:b_01_ann.de_anio*/");
        sql.append("    and a.co_dep = ?/*:global.vcod_dep*/");
        sql.append("    and a.ti_tab='D'");
        sql.append("    and a.ti_emi <> '01'");
        sql.append("    and a.co_emp_res = (CASE WHEN ?='0' OR ?='2' THEN a.co_emp_res ELSE ? END) ");
        sql.append("  UNION");
        sql.append(" SELECT ' [TODOS]',");
        sql.append(" 	   NULL,");
        sql.append(" 	   NULL");
        //sql.append("   FROM dual");
        sql.append("  ORDER BY 1");

        
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class), new Object[]{annio,usuarioConfigBean.getCoDep(),
            usuarioConfigBean.getTiConsulta(),usuarioConfigBean.getTiConsulta(),usuarioConfigBean.getCempCodemp(),annio,usuarioConfigBean.getCoDep(),usuarioConfigBean.getTiConsulta(),usuarioConfigBean.getTiConsulta(),usuarioConfigBean.getCempCodemp()});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
        
    @Override
    public List<EmpleadoBean> listEmpleadoElaboradoPor(String pcodDependencia,String ptiAcceso,String pcoEmpleado){
        StringBuilder sql = new StringBuilder();
        boolean bquery=false;
       
        if(ptiAcceso!=null&&(ptiAcceso.equals("0")||ptiAcceso.equals("2"))){
            sql.append(" SELECT e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom nombre, e.CEMP_CODEMP");
            sql.append(" FROM IDOSGD.RHTM_PER_EMPLEADOS e");
            sql.append(" where e.CEMP_EST_EMP = '1'");
            sql.append(" AND (CEMP_CO_DEPEND in (select CO_DEPENDENCIA from IDOSGD.RHTM_DEPENDENCIA d where d.CO_DEPENDENCIA=?/*:global.VCOD_DEP*/ or d.CO_DEPEN_PADRE=?/*:global.VCOD_DEP*/) ");
            sql.append(" OR CO_DEPENDENCIA =  ?/*:B_02.CO_DEP_EMI */)");
            sql.append(" UNION");
            sql.append(" SELECT e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom nombre, e.CEMP_CODEMP");
            sql.append(" FROM IDOSGD.RHTM_PER_EMPLEADOS e");
            sql.append(" where e.CEMP_EST_EMP = '1'");
            sql.append(" AND cemp_codemp ");
            sql.append(" in ( select co_emp from IDOSGD.tdtx_dependencia_empleado  where co_dep=?/*:global.VCOD_DEP*/ and es_emp='0')");
            sql.append(" UNION ");
            sql.append(" SELECT ' [TODOS]', NULL ");
            sql.append(" ORDER BY 1");            
            bquery=true;
        }else{//personal
            sql.append(" SELECT e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom nombre, e.CEMP_CODEMP");
            sql.append(" FROM IDOSGD.RHTM_PER_EMPLEADOS e");
            sql.append(" where e.CEMP_EST_EMP = '1'");
            sql.append(" AND (CEMP_CO_DEPEND in (select CO_DEPENDENCIA from IDOSGD.RHTM_DEPENDENCIA d where d.CO_DEPENDENCIA=?/*:global.VCOD_DEP*/ or d.CO_DEPEN_PADRE=?/*:global.VCOD_DEP*/) ");
            sql.append(" OR CO_DEPENDENCIA =  ?/*:B_02.CO_DEP_EMI */) and e.CEMP_CODEMP=?");
            sql.append(" UNION ");
            sql.append(" SELECT ' [TODOS]', NULL ");
            sql.append(" ORDER BY 1");              
        }

        
        List<EmpleadoBean> list = new ArrayList<EmpleadoBean>();
        
        try {
            if(bquery){
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class), new Object[]{pcodDependencia,pcodDependencia,
                                                                                                                    pcodDependencia,pcodDependencia});                
            }else{
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class), new Object[]{pcodDependencia,pcodDependencia,
                                                                                                                pcodDependencia,pcoEmpleado});                
            }
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
        @Override
        public List<DependenciaBean> listDependenciaDestinatarioEmi(String coDepen, String deDepen) {
        StringBuilder sql = new StringBuilder();
        
            sql.append("SELECT DE_DEPENDENCIA,\n"
                    + "       CO_DEPENDENCIA,\n"
                    + "       DE_CORTA_DEPEN\n"
                    + "  FROM IDOSGD.RHTM_DEPENDENCIA\n"
                    + " WHERE co_nivel <> '6'\n"
                    + "   AND IN_BAJA = '0'\n"
                    + "   AND (DE_DEPENDENCIA LIKE '%' || ? || '%' OR DE_CORTA_DEPEN LIKE ? || '%' )\n"
                    + "UNION\n"
                    + "SELECT DE_DEPENDENCIA,\n"
                    + "       CO_DEPENDENCIA,\n"
                    + "       DE_CORTA_DEPEN\n"
                    + "  FROM IDOSGD.RHTM_DEPENDENCIA\n"
                    + " WHERE co_nivel = '6'\n"
                    + "   AND IN_BAJA = '0'\n"
                    + "   AND (CO_DEPEN_PADRE in ( select CO_DEPEN_PADRE co_dep from IDOSGD.RHTM_DEPENDENCIA where CO_DEPENDENCIA = ?)\n"
                    + "       OR ? IN (CO_DEPENDENCIA, CO_DEPEN_PADRE))\n"
                    + "   AND (DE_DEPENDENCIA LIKE '%' || ? || '%' OR DE_CORTA_DEPEN LIKE ? || '%')\n"
                    + " ORDER BY 1");
        
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class), new Object[]{deDepen,deDepen,
            coDepen,coDepen,deDepen,deDepen});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
        
    @Override
    public List<TipoDocumentoBean> listTipDocXDependencia(String coDependencia){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  UPPER(A.CDOC_DESDOC) DE_TIP_DOC, A.CDOC_TIPDOC CO_TIP_DOC\n" +
            "FROM IDOSGD.SI_MAE_TIPO_DOC A,IDOSGD.SITM_DOC_DEPENDENCIA B\n" +
            "WHERE A.CDOC_INDBAJ ='0'\n" +
            "AND A.CDOC_TIPDOC = B.CO_TIP_DOC\n" +
            "AND B.CO_DEP=?\n" +
            "ORDER BY 1"); 
        
        List<TipoDocumentoBean> list = new ArrayList<TipoDocumentoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(TipoDocumentoBean.class), new Object[]{coDependencia});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<PrioridadDocumentoBean> listPrioridadDocTblEmi() {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DE_PRI,CO_PRI ");
        sql.append("FROM IDOSGD.TDTR_PRIORIDAD ");
        
        List<PrioridadDocumentoBean> list = new ArrayList<PrioridadDocumentoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(PrioridadDocumentoBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }

        return list;        
    }    
    
    @Override
    public List<TipoDocumentoBean> listTipDocReferencia(String coDependencia){
                StringBuilder sql = new StringBuilder();
        
        sql.append("select a.CO_DOC CO_TIP_DOC , B.CDOC_DESDOC DE_TIP_DOC\n" +
                    "from (\n" +
                    "    select DISTINCT co_doc \n" +
                    "    from IDOSGD.tdtx_resumen_doc\n" +
                    "    where CO_DEP = ?) AS a, IDOSGD.SI_MAE_TIPO_DOC b\n" +
                    "WHERE  A.CO_DOC = B.CDOC_TIPDOC\n" +
                    "order by 2"); 
        
        List<TipoDocumentoBean> list = new ArrayList<TipoDocumentoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(TipoDocumentoBean.class), new Object[]{coDependencia});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<TipoDestinatarioEmiBean> listTipDestinatarioEmi(){
                StringBuilder sql = new StringBuilder();
        
        sql.append("select CELE_DESELE de_destinatario,CELE_CODELE co_destinatario from IDOSGD.SI_ELEMENTO WHERE CTAB_CODTAB='TIP_DESTINO' AND CELE_CODELE not in  ('05')"); 
        
        List<TipoDestinatarioEmiBean> list = new ArrayList<TipoDestinatarioEmiBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(TipoDestinatarioEmiBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public List<GrupoDestinatarioBean> listGrupoDestinatario(String codDependencia){
                StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT DE_GRU_DES de_grupo,CO_GRU_DES co_grupo FROM IDOSGD.TDTM_GRU_DES WHERE CO_DEP=? AND ES_GRU_DES='1' ORDER BY 2"); 
        
        List<GrupoDestinatarioBean> list = new ArrayList<GrupoDestinatarioBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(GrupoDestinatarioBean.class), new Object[]{codDependencia});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;           
    }
    
    @Override
    public List<MotivoBean> listMotivoDestinatario(String codDependencia, String coTipoDoc){
                String sql ="SELECT a.de_mot, \n"
                        + "a.co_mot  \n"
                        + "FROM IDOSGD.tdtr_motivo a, IDOSGD.tdtx_moti_docu_depe b  \n"
                        + "WHERE a.co_mot = b.co_mot AND b.co_dep = ?/*:B_02.CO_DEP_EMI*/ AND b.co_tip_doc = ?/*:b_02.co_tip_doc_adm*/  \n"
                        + "UNION SELECT de_mot, co_mot FROM IDOSGD.tdtr_motivo  where co_mot in ('0','1') ORDER BY 1 \n"; 
        
        List<MotivoBean> list = new ArrayList<MotivoBean>();

        try {
            list = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(MotivoBean.class), new Object[]{codDependencia,coTipoDoc});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;           
    }
    
    @Override
    public List<DependenciaBean> listDependenciaRemitenteEmi(String coDependencia){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DE_DEPENDENCIA, CO_DEPENDENCIA \n" +
                    "FROM IDOSGD.RHTM_DEPENDENCIA \n" +
                    "where CO_DEPENDENCIA in (SELECT ?  \n" +//FROM DUAL
                    "                        UNION SELECT co_dep_ref FROM IDOSGD.tdtx_referencia  \n" +
                    "                              WHERE co_dep_emi = ?\n" +
                    "                              AND ti_emi = 'R' \n" +
                    "                              AND es_ref = '1')"); 
        
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class), new Object[]{coDependencia,coDependencia});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;         
    }
    
    @Override    
    public List<LocalBean> listLocalRemitenteEmi(String coDependencia){
        StringBuilder sql = new StringBuilder();
        sql.append("select DE_NOMBRE_LOCAL DE_LOCAL,CCOD_LOCAL CO_LOCAL from IDOSGD.SI_MAE_LOCAL WHERE CCOD_LOCAL IN (SELECT CO_LOC FROM IDOSGD.SITM_LOCAL_DEPENDENCIA WHERE CO_DEP = ?)"); 
        
        List<LocalBean> list = new ArrayList<LocalBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(LocalBean.class), new Object[]{coDependencia});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;          
    }

    @Override    
    public List<LocalBean> listLocal(){
        StringBuilder sql = new StringBuilder();
        sql.append("select DE_NOMBRE_LOCAL DE_LOCAL,CCOD_LOCAL CO_LOCAL from IDOSGD.SI_MAE_LOCAL ORDER BY 2"); 
        
        List<LocalBean> list = new ArrayList<LocalBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(LocalBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;          
    }
    
    @Override
    public List<RemitenteBean> listReferenciaOrigenPersonal(String sCoEmpEmi) {
    StringBuilder sql = new StringBuilder();

    sql.append(""+
//             "SELECT DECODE(c.ti_emi,\n" +
//                "              '01', d.de_dependencia,\n" +
//                "              '02', ' [PROVEEDORES]',\n" +
//                "              '03', ' [CIUDADANOS]',\n" +
//                "              '04', ' [OTROS]') descrip,\n" +
//                "       DECODE(c.ti_emi,\n" +
//                "              '01', c.co_dep_emi,\n" +
//                "              '02', 'P',\n" +
//                "              '03', 'C',\n" +
//                "              '04', 'O') cod_dep,\n" +
                    "SELECT CASE  \n" +
                    "             WHEN c.ti_emi='01' THEN d.de_dependencia \n" +
                    "             WHEN c.ti_emi='02' THEN ' [PROVEEDORES]' \n" +
                    "             WHEN c.ti_emi='03' THEN ' [CIUDADANOS]' \n" +
                    "             WHEN c.ti_emi='04' THEN ' [OTROS]' END AS descrip,\n" +
                    "      CASE  \n" +
                    "             WHEN c.ti_emi='01' THEN c.co_dep_emi \n" +
                    "             WHEN c.ti_emi='02' THEN 'P' \n" +
                    "             WHEN c.ti_emi='03' THEN 'C' \n" +
                    "             WHEN c.ti_emi='04' THEN 'O' END AS cod_dep,\n" +
                "              d.de_corta_depen\n" +
                "  FROM IDOSGD.tdtr_referencia a,\n" +
                "       IDOSGD.tdtv_remitos    b,\n" +
                "       IDOSGD.tdtv_remitos    c,\n" +
                "       IDOSGD.rhtm_dependencia  d\n" +
                " WHERE b.es_eli = '0'\n" +
                "   AND b.co_gru = '1'\n" +
                "   AND a.nu_ann = b.nu_ann\n" +
                "   AND a.nu_emi = b.nu_emi\n" +
                "   AND a.nu_ann_ref = c.nu_ann\n" +
                "   AND a.nu_emi_ref = c.nu_emi\n" +
                "   AND c.co_dep_emi = d.co_dependencia\n" +
                "   AND b.co_emp_emi = ?/*:GLOBAL.USER */\n" +
                " GROUP BY c.ti_emi,\n" +
                "          c.co_dep_emi,\n" +
                "          d.de_dependencia,\n" +
                "          d.de_corta_depen\n" +
                " UNION\n" +
                "SELECT ' [TODOS]',\n" +
                "       NULL,\n" +
                "       NULL\n" +
                //"  FROM dual\n" +
                " ORDER BY 1");


    List<RemitenteBean> list = new ArrayList<RemitenteBean>();

    try {
        list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(RemitenteBean.class), new Object[]{sCoEmpEmi});
    }catch (EmptyResultDataAccessException e) {
        list = null;
    }catch (Exception e) {
        list = null;
        e.printStackTrace();
    }
    return list;
}
    
        @Override
        public List<DependenciaBean> listDestinatarioEmiPersonal(String sCoEmpEmi) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT CASE  \n" +
                    "            WHEN  c.ti_des='01' THEN d.de_dependencia \n" +
                    "            WHEN  c.ti_des='02' THEN ' [PROVEEDORES]' \n" +
                    "            WHEN  c.ti_des='03' THEN ' [CIUDADANOS]' \n" +
                    "            WHEN  c.ti_des='04' THEN ' [OTROS]' END AS deDependencia,\n" +
                    "      CASE  \n" +
                    "           WHEN   c.ti_des='01' THEN c.co_dep_des \n" +
                    "           WHEN   c.ti_des='02' THEN 'P' \n" +
                    "           WHEN   c.ti_des='03' THEN 'C' \n" +
                    "           WHEN   c.ti_des='04' THEN 'O' END AS coDependencia,\n" +
                    "       d.DE_CORTA_DEPEN\n" +
                    "  FROM IDOSGD.tdtv_remitos    b,\n" +
                    "       IDOSGD.tdtv_destinos   c LEFT JOIN \n" +
                    "       IDOSGD.rhtm_dependencia  d ON ( c.co_dep_des = d.co_dependencia )\n" +
                    " WHERE b.es_eli = '0'\n" +
                    "   AND b.co_gru = '1'\n" +
                    "   AND b.nu_ann = c.nu_ann\n" +
                    "   AND b.nu_emi = c.nu_emi\n" +
//                    "   AND c.co_dep_des = d.co_dependencia (+)\n" +
                    "   AND b.co_emp_emi = ? \n" +
                    " GROUP BY c.ti_des,\n" +
                    "          c.co_dep_des,\n" +
                    "          d.de_dependencia,\n" +
                    "          d.de_corta_depen\n" +
                    " UNION\n" +
                    "SELECT ' [TODOS]',\n" +
                    "       NULL,\n" +
                    "       NULL\n" +
                    //"  FROM dual\n" +
                    " ORDER BY 1");

        
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class), new Object[]{sCoEmpEmi});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }    

    @Override
    public DependenciaBean getDatosDependencia(String codDependencia){
        StringBuilder sql = new StringBuilder();
        sql.append("select \n" +
                    "co_dependencia,\n" +
                    "co_empleado,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(co_empleado) de_dependencia,\n" +
                    "de_sigla \n" +
                    "from IDOSGD.rhtm_dependencia \n" +
                    "where co_dependencia = ?");
        
        DependenciaBean depEmi = new DependenciaBean();
        
        try {
            depEmi = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class), new Object[]{codDependencia});
        } catch (EmptyResultDataAccessException e) {
            depEmi = null;
        } catch (Exception e) {
            depEmi = null;
            e.printStackTrace();
        }
        
        return depEmi;

    }
    
    @Override
        public List<EstadoDocumentoBean> listEstadosDocumentoMP(String nomTabla) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DE_EST_MP DE_EST,CO_EST \n" +
                    "FROM IDOSGD.TDTR_ESTADOS \n" +
                    "WHERE DE_TAB=?\n" +
                    "AND DE_EST_MP IS NOT NULL UNION SELECT '.: TODOS :.',NULL \n" +
                    //"FROM DUAL \n" +
                    "ORDER BY CO_EST");
        
        List<EstadoDocumentoBean> list = new ArrayList<EstadoDocumentoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(EstadoDocumentoBean.class),
                    new Object[]{nomTabla});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }

        return list;
    }    

    @Override
    public List<TipoDestinatarioEmiBean> listTipEmisorDocExtRecep(){
                StringBuilder sql = new StringBuilder();
        
        sql.append("select CELE_DESELE de_destinatario,CELE_CODELE co_destinatario from IDOSGD.SI_ELEMENTO \n" +
                    "WHERE CTAB_CODTAB='TIP_DESTINO' \n" +
                    "AND CELE_CODELE NOT IN ('01','05')\n" +
                    "UNION SELECT '.: TODOS :.',NULL\n" +
                    //"FROM DUAL\n" +
                    "ORDER BY 2"); 
        
        List<TipoDestinatarioEmiBean> list = new ArrayList<TipoDestinatarioEmiBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(TipoDestinatarioEmiBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public List<TupaExpedienteBean> listTupaExpedienteNew(){
                StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT DE_NOMBRE DE_TUPA, CO_PROCESO CO_TUPA\n" +
                    "FROM IDOSGD.TDTR_PROCESOS_EXP \n" +
                    "WHERE TI_PROCESO = '1' \n" +
                    "AND ES_ESTADO = '1'"+
                    "UNION  \n" +
                    "SELECT '[SIN TUPA]','0000' \n" +
                    //"FROM DUAL  \n" +
                    "ORDER BY 2"); 
        
        List<TupaExpedienteBean> list = new ArrayList<TupaExpedienteBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(TupaExpedienteBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }    
    
    @Override
    public List<TupaExpedienteBean> listTupaExpediente(){
                StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT DE_NOMBRE DE_TUPA, CO_PROCESO CO_TUPA\n" +
                    "FROM IDOSGD.TDTR_PROCESOS_EXP \n" +
                    "WHERE TI_PROCESO = '1' \n" +
                    "UNION  \n" +
                    "SELECT '[SIN TUPA]','0000' \n" +
                    //"FROM DUAL  \n" +
                    "ORDER BY 2"); 
        
        List<TupaExpedienteBean> list = new ArrayList<TupaExpedienteBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(TupaExpedienteBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public List<LocalBean> lsLocal(){
        StringBuilder sql = new StringBuilder();
        sql.append("select DE_NOMBRE_LOCAL DE_LOCAL,CCOD_LOCAL CO_LOCAL from IDOSGD.SI_MAE_LOCAL \n" +
                    "union\n" +
                    "select ' [TODOS]' DE_LOCAL,NULL CO_LOCAL\n" +
                    //"from dual\n" +
                    "ORDER BY 1"); 

        List<LocalBean> list;

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(LocalBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;            
    }
    
    @Override
    public List<EtiquetaBean> getEtiquetasList() {
        StringBuilder sql = new StringBuilder();

        sql.append("select co_est,de_est \n"
                + "from IDOSGD.tdtr_estados \n"
                + "where de_tab = 'CO_ETIQUETA_REC' \n"
                + "UNION SELECT NULL,'.: TODOS :.'  ORDER BY 1 \n");
                //+ "FROM DUAL\n"

        List<EtiquetaBean> list = new ArrayList<EtiquetaBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EtiquetaBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }    
    
    @Override
    public List<DependenciaBean> getlsDestinatarioEmiDocExt(String annio,String coDep){
        StringBuilder sql = new StringBuilder();
        
        sql.append(" select\n" +
                    "   b.DE_DEPENDENCIA deDependencia,\n" +
                    "   a.co_dep_ref coDependencia,\n" +
                    "   b.DE_CORTA_DEPEN\n" +
                    "   from IDOSGD.tdtx_resumen_dep a , IDOSGD.RHTM_DEPENDENCIA b\n" +
                    "  where a.nu_ann = ?\n" +
                    "    and a.co_dep = ?\n" +
                    "    and a.ti_tab='D'\n" +
                    "    and a.ti_emi in ('02','03','04')\n" +
                    "    and a.CO_DEP_ref = b.CO_DEPENDENCIA\n" +
                    "  UNION\n" +
                    " SELECT ' [TODOS]',\n" +
                    "      NULL,\n" +
                    "      NULL\n" +
                   // "   FROM dual\n" +
                    "  ORDER BY 1");
        
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class), 
                    new Object[]{annio,coDep});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public List<DependenciaBean> getlsDestinatarioDocPendEntrega(String coDep){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT DE_DEPENDENCIA , CO_DEPENDENCIA , DE_CORTA_DEPEN\n" +
                "FROM IDOSGD.RHTM_DEPENDENCIA \n" +
                "where CO_DEPENDENCIA in \n" +
                "(select CO_DEP from IDOSGD.sitm_local_dependencia WHERE CO_LOC=?) UNION select ' [TODOS]',NULL,NULL "
                //+ " FROM DUAL  "
                + "order by 1");
        
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class), 
                    new Object[]{coDep});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }    

    @Override
    public List<VencimientoBean> getVencimientoList() {
         StringBuilder sql = new StringBuilder();

        sql.append("select co_est,de_est \n"
                + "from IDOSGD.tdtr_estados \n"
                + "where de_tab = 'VENCIMIENTO' \n"
                + "UNION SELECT NULL,'.: TODOS :.'  ORDER BY 1 \n");
                //+ "FROM DUAL\n";

        List<VencimientoBean> list = new ArrayList<VencimientoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(VencimientoBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<EstadoDocumentoBean> listEstadosDocumentoEmiSegui(String tdtv_destinos) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DE_EST,CO_EST ");
        sql.append("FROM IDOSGD.TDTR_ESTADOS ");
        sql.append("WHERE DE_TAB = ? ");
        sql.append("AND CO_EST NOT IN('5','9') ");
        sql.append("UNION ");
        sql.append("SELECT '.:TODOS.:',NULL  ORDER BY CO_EST ");
        
        List<EstadoDocumentoBean> list = new ArrayList<EstadoDocumentoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(EstadoDocumentoBean.class),
                    new Object[]{tdtv_destinos});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<SiElementoBean> getLsSiElementoBean(String pctabCodtab){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CELE_CODELE,CELE_DESELE,NELE_NUMSEC,CELE_DESCOR\n" +
            "FROM   IDOSGD.SI_ELEMENTO\n" +
            "WHERE  CTAB_CODTAB = ?");

        List<SiElementoBean> list = null;
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(SiElementoBean.class),
                    new Object[]{pctabCodtab});
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<DepartamentoBean> listDepartamentos(){
                StringBuilder sql = new StringBuilder();

        sql.append("select ubdep coDepartamento,nodep noDepartamento "
                + "from idosgd.idtubias "
                + "where nodep != '               ' "
                + "and nodep !='***************' "
                + "and ub_inei is not null "
                + "group by ubdep,nodep order by 1");

        List<DepartamentoBean> list = new ArrayList<DepartamentoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DepartamentoBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
        public List<DependenciaBean> listNewUpdDependenciaDestinatarioEmi(String coDepen, String deDepen) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM\n"+        
        "(SELECT DE_DEPENDENCIA,\n" +
        "       CO_DEPENDENCIA,\n" +
        "       DE_CORTA_DEPEN\n" +
        "  FROM RHTM_DEPENDENCIA\n" +
        " WHERE co_nivel <> '6'\n" +
        "   AND IN_BAJA = '0'\n" +
        "   AND (DE_DEPENDENCIA LIKE '%' || ? || '%' OR DE_CORTA_DEPEN LIKE ? || '%' )\n" +
        "UNION\n" +
        "SELECT DE_DEPENDENCIA,\n" +
        "       CO_DEPENDENCIA,\n" +
        "       DE_CORTA_DEPEN\n" +
        "  FROM RHTM_DEPENDENCIA\n" +
        " WHERE co_nivel = '6'\n" +
        "   AND IN_BAJA = '0'\n" +
        "   AND (CO_DEPEN_PADRE in ( select CO_DEPEN_PADRE co_dep from RHTM_DEPENDENCIA where CO_DEPENDENCIA = ?)\n" +
        "       OR ? IN (CO_DEPENDENCIA, CO_DEPEN_PADRE))\n" +
        "   AND (DE_DEPENDENCIA LIKE '%' || ? || '%' OR DE_CORTA_DEPEN LIKE ? || '%')\n" +
        "ORDER BY 1)\n" +
        "UNION\n" +
        "SELECT '[Elija Opción]','',''\n" +
        "FROM DUAL");

        
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class), new Object[]{deDepen,deDepen,
            coDepen,coDepen,deDepen,deDepen});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
     
    @Override
    public List<ProvinciaBean> listProvincia(String coDep){
                StringBuilder sql = new StringBuilder();

        sql.append("select ubprv coProvincia, noprv noProvincia from idtubias where ubdep=? and trim(noprv) is not null group by ubprv,noprv order by 2");

        List<ProvinciaBean> list;

        try {
            list = this.jdbcTemplate.query(sql.toString(),new Object[]{coDep}, BeanPropertyRowMapper.newInstance(ProvinciaBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }    
    
    @Override
    public List<DistritoBean> listDistrito(String coDep,String coDis){
                StringBuilder sql = new StringBuilder();

        sql.append("select ubdis coDistrito, cpdis noDistrito from idtubias where ubdep=? and ubprv=? and ubdis not in('00') and trim(cpdis) is not null group by ubdis,cpdis order by 2");
        List<DistritoBean> list;

        try {
            list = this.jdbcTemplate.query(sql.toString(),new Object[]{coDep,coDis}, BeanPropertyRowMapper.newInstance(DistritoBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }    

    @Override
    public List<DependenciaBean> listDependenciaRemitenteEmi2() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DependenciaBean> listDependenciaDestinatarioEmi2(String coDepen, String deDepen) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     
    @Override
    public List<DependenciaBean> listDependenciaRemitenteEmi2(String coDependencia) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }	 

    @Override
    public List<DependenciaBean> listDestinatarioEmiFiltro(String annio, UsuarioConfigBean usuarioConfigBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DependenciaBean> listDestinatarioEmiFiltro2(String annio, UsuarioConfigBean usuarioConfigBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<RemitenteBean> listRemitenteAcervo(String annio, UsuarioConfigBean usuarioConfigBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TipoEnvioMsj> listTipoEnvMsj() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EstadoDocumentoBean> listEstadosDocumentoPendiente(String nomTabla) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TipoDocumentoBean> listTipDocListAccion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DependenciaBean> listDependenciaDestinatarioEmiOT(String coDepen, String deDepen) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TipoDocumentoBean> listTipDocXDependenciaEmiPersonal(String coDependencia) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SiElementoBean> listFormaEntrega(String pctabCodtab) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SiElementoBean> getLsSiElementoPerfil(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SiElementoBean> getLsSiElementoTipoExpediente(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
