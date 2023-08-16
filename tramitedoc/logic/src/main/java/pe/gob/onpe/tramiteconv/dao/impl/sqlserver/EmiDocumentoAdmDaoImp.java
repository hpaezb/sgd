package pe.gob.onpe.tramiteconv.dao.impl.sqlserver;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.AudiEstadosMovDocBean;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DatosInterBean;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DestinoBean;
import pe.gob.onpe.tramitedoc.bean.DetalleEnvioDeCorreoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoProyectoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteBean;
import pe.gob.onpe.tramitedoc.bean.MotivoBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaEmiDocBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaRemitoBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteEmiBean;
import pe.gob.onpe.tramitedoc.bean.TblRemitosBean;
import pe.gob.onpe.tramitedoc.dao.EmiDocumentoAdmDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
import pe.gob.onpe.tramitedoc.util.Constantes;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

@Repository("emiDocumentoAdmDao")
public class EmiDocumentoAdmDaoImp extends SimpleJdbcDaoBase implements EmiDocumentoAdmDao {

    @Override
    public List<DocumentoEmiBean> getDocumentosBuscaEmiAdm(BuscarDocumentoEmiBean buscarDocumentoEmi) {
        StringBuilder sql = new StringBuilder();
        boolean bBusqFiltro = false;
        String sOrdenList="";
        Map<String, Object> objectParam = new HashMap<String, Object>();

        List<DocumentoEmiBean> list = new ArrayList<DocumentoEmiBean>();

        sql.append("SELECT TOP 100 X.*, ");
        sql.append(" (SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_DE_EMI_REF](X.NU_ANN, X.NU_EMI)) DE_EMI_REF, ");        
        sql.append(" [IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](X.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append(" CASE X.NU_CANDES ");
        sql.append("	WHEN 1 THEN (SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_TI_DES_EMP](X.NU_ANN, X.NU_EMI)) ");
        sql.append("	ELSE (SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_TI_DES_EMP_V](X.NU_ANN, X.NU_EMI)) ");
        sql.append(" END DE_EMP_PRO, ");
        sql.append(" [IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](X.CO_EMP_RES) DE_EMP_RES, ");        
        sql.append(" [IDOSGD].[PK_SGD_DESCRIPCION_ESTADOS](X.ES_DOC_EMI,'TDTV_REMITOS') DE_ES_DOC_EMI, ");
        sql.append(" ROW_NUMBER() OVER (ORDER BY X.NU_ANN) AS ROWNUM ");    
        sql.append(" FROM ( "); 
        sql.append(" SELECT "); 
        sql.append(" A.NU_COR_EMI, A.FE_EMI, "); 
        sql.append(" CASE ISNULL(B.TI_EMI_REF, '0') + ISNULL(B.IN_EXISTE_ANEXO, '2') "); 
        sql.append("	WHEN '00' THEN 0 ");
        sql.append("	WHEN '02' THEN 0 ");
        sql.append("    ELSE 1 ");
        sql.append(" END EXISTE_ANEXO, ");
        sql.append(" ISNULL(B.CO_PRIORIDAD, '1') CO_PRIORIDAD, "); 
        sql.append(" CONVERT(VARCHAR(10), A.FE_EMI, 103) FE_EMI_CORTA, ");
        sql.append(" B.NU_DOC,UPPER(A.DE_ASU) DE_ASU_M,A.NU_ANN,B.NU_EXPEDIENTE,A.NU_EMI,A.TI_CAP,B.IN_EXISTE_DOC EXISTE_DOC, ");
        sql.append(" A.ES_DOC_EMI,A.CO_TIP_DOC_ADM,A.NU_CANDES,A.CO_EMP_RES ");
        sql.append(" FROM IDOSGD.TDTV_REMITOS A, "); 
        sql.append("	  IDOSGD.TDTX_REMITOS_RESUMEN B ");
        sql.append(" WHERE B.NU_ANN=A.NU_ANN ");
        sql.append(" AND B.NU_EMI=A.NU_EMI ");
        String pNUAnn = buscarDocumentoEmi.getsCoAnnio();
        if (!(buscarDocumentoEmi.getEsFiltroFecha().equals("1") && pNUAnn.equals("0"))) {
            sql.append(" AND A.NU_ANN = :pNuAnn");
            // Parametros Basicos
            objectParam.put("pNuAnn", pNUAnn);
        }
        sql.append(" AND A.TI_EMI='01'");
        sql.append(" AND B.TI_EMI='01'");
        sql.append(" AND A.ES_ELI = '0'");
        sql.append(" AND A.CO_GRU = '1'");
        sql.append(" AND A.CO_DEP_EMI = :pCoDepEmi");

        // Parametros Basicos
        objectParam.put("pCoDepEmi", buscarDocumentoEmi.getsCoDependencia());

        String pTipoBusqueda = buscarDocumentoEmi.getsTipoBusqueda();
        if (pTipoBusqueda.equals("1") && buscarDocumentoEmi.isEsIncluyeFiltro()) {
            bBusqFiltro = true;
        }
        String auxTipoAcceso=buscarDocumentoEmi.getsTiAcceso();
        String tiAcceso=auxTipoAcceso!=null&&(auxTipoAcceso.equals("0")||auxTipoAcceso.equals("1")||auxTipoAcceso.equals("2"))?auxTipoAcceso:"1";                
        if (buscarDocumentoEmi.getsElaboradoPor()!=null&&buscarDocumentoEmi.getsElaboradoPor().trim().length()>0&&(pTipoBusqueda.equals("0")||bBusqFiltro)&&tiAcceso.equals("0")) {
            sql.append(" AND A.CO_EMP_RES = :pcoEmpRes ");
            objectParam.put("pcoEmpRes", buscarDocumentoEmi.getsElaboradoPor());
        } else {
            if (tiAcceso.equals("1")) {
                sql.append(" AND A.CO_EMP_RES = :pcoEmpRes ");
                objectParam.put("pcoEmpRes", buscarDocumentoEmi.getsCoEmpleado());
            } else if (tiAcceso.equals("2")) {
                sql.append(" AND (A.CO_EMP_RES = :pcoEmpRes OR A.CO_EMP_EMI = :pcoEmpRes) ");
                objectParam.put("pcoEmpRes", buscarDocumentoEmi.getsCoEmpleado());
            }
        }

        //Filtro
        if (pTipoBusqueda.equals("0") || bBusqFiltro) {
            if (buscarDocumentoEmi.getsTipoDoc() != null && buscarDocumentoEmi.getsTipoDoc().trim().length() > 0) {
                sql.append(" AND A.CO_TIP_DOC_ADM = :pCoDocEmi ");
                objectParam.put("pCoDocEmi", buscarDocumentoEmi.getsTipoDoc());
            }
            if (buscarDocumentoEmi.getsEstadoDoc() != null && buscarDocumentoEmi.getsEstadoDoc().trim().length() > 0) {
                sql.append(" AND A.ES_DOC_EMI = :pEsDocEmi ");
                objectParam.put("pEsDocEmi", buscarDocumentoEmi.getsEstadoDoc());
            }else{
                sOrdenList=" DESC";
            }
            if (buscarDocumentoEmi.getsPrioridadDoc() != null && buscarDocumentoEmi.getsPrioridadDoc().trim().length() > 0) {
                sql.append(" AND B.CO_PRIORIDAD = :pCoPrioridad ");
                objectParam.put("pCoPrioridad", buscarDocumentoEmi.getsPrioridadDoc());
            }
            if (buscarDocumentoEmi.getsRefOrigen() != null && buscarDocumentoEmi.getsRefOrigen().trim().length() > 0) {
                sql.append(" AND CHARINDEX(:pTiEmiRef,B.TI_EMI_REF) > 0 ");
                objectParam.put("pTiEmiRef", buscarDocumentoEmi.getsRefOrigen());
            }
            if (buscarDocumentoEmi.getsDestinatario() != null && buscarDocumentoEmi.getsDestinatario().trim().length() > 0) {
                sql.append(" AND CHARINDEX(:pTiEmpPro, B.TI_EMI_DES) > 0 ");
                objectParam.put("pTiEmpPro", buscarDocumentoEmi.getsDestinatario());
            }
            if (buscarDocumentoEmi.getEsFiltroFecha() != null
                    && (buscarDocumentoEmi.getEsFiltroFecha().equals("1") || buscarDocumentoEmi.getEsFiltroFecha().equals("3"))) {
                String vFeEmiIni = buscarDocumentoEmi.getsFeEmiIni();
                String vFeEmiFin = buscarDocumentoEmi.getsFeEmiFin();
                if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
                        && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                    sql.append(" AND CAST(FE_EMI AS DATE) between CAST(:pFeEmiIni AS DATE) AND CAST(:pFeEmiFin AS DATE) ");
                    objectParam.put("pFeEmiIni", vFeEmiIni);
                    objectParam.put("pFeEmiFin", vFeEmiFin);
                }
            }

        }

        //Busqueda
        if (pTipoBusqueda.equals("1")) {
            if (buscarDocumentoEmi.getsNumCorEmision() != null && buscarDocumentoEmi.getsNumCorEmision().trim().length() > 0) {
                sql.append(" AND A.NU_COR_EMI = :pnuCorEmi ");
                objectParam.put("pnuCorEmi", buscarDocumentoEmi.getsNumCorEmision());
            }

            if (buscarDocumentoEmi.getsNumDoc() != null && buscarDocumentoEmi.getsNumDoc().trim().length() > 1) {
                sql.append(" AND B.NU_DOC LIKE ''+:pnuDocEmi+'%' ");
                objectParam.put("pnuDocEmi", buscarDocumentoEmi.getsNumDoc());
            }

            if (buscarDocumentoEmi.getsBuscNroExpediente() != null && buscarDocumentoEmi.getsBuscNroExpediente().trim().length() > 1) {
                sql.append(" AND B.NU_EXPEDIENTE LIKE '%'+:pnuExpediente+'%' ");
                objectParam.put("pnuExpediente", buscarDocumentoEmi.getsBuscNroExpediente());
            }

            // Busqueda del Asunto
            if (buscarDocumentoEmi.getsDeAsuM() != null && buscarDocumentoEmi.getsDeAsuM().trim().length() > 1) {
                //sql.append(" AND CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue(buscarDocumentoEmi.getsDeAsuM())+"', 1 ) > 1 ");
                sql.append(" AND UPPER(A.DE_ASU) LIKE '%'+:pDeAsunto+'%' ");
                objectParam.put("pDeAsunto", buscarDocumentoEmi.getsDeAsuM());
            }
        }        
        sql.append(") AS X ");
        sql.append(" ORDER BY X.FE_EMI").append(sOrdenList);

        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoEmiBean.class));

        } catch (EmptyResultDataAccessException e) {
            list = null;
            e.printStackTrace();
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public DocumentoEmiBean getDocumentoEmitido(String pnuAnn, String pnuEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT "); 
        sql.append("a.nu_ann, "); 
        sql.append("a.nu_emi, "); 
        sql.append("CAST(a.nu_cor_emi AS VARCHAR(10)) nu_cor_emi, "); 
        sql.append("a.co_loc_emi, "); 
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_LOCAL](a.co_loc_emi) de_loc_emi, "); 
        sql.append("a.co_dep_emi, "); 
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](a.co_dep_emi) de_dep_emi, ");
        sql.append("a.ti_emi, "); 
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_TI_DESTINO](a.ti_emi) de_tip_emi, ");
        sql.append("a.co_emp_emi, "); 
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](co_emp_emi) de_emp_emi, ");
        sql.append("a.co_emp_res, "); 
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](a.co_emp_res) de_emp_res, ");
        sql.append("a.nu_dni_emi, "); 
        sql.append("a.nu_ruc_emi, "); 
        sql.append("a.co_otr_ori_emi, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_TI_EMI_EMP](a.nu_ann, a.nu_emi) de_ori_emi, ");
        sql.append("CONVERT(VARCHAR(10), a.fe_emi, 103) fe_emi, "); 
        sql.append("CONVERT(VARCHAR(10), A.FE_EMI, 103) FE_EMI_CORTA, ");
        sql.append("a.co_tip_doc_adm, "); 
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](a.co_tip_doc_adm) de_tip_doc_adm, ");
        sql.append("a.ti_emi, ");
        sql.append("a.nu_doc_emi, ");
        sql.append("a.de_doc_sig, ");
        sql.append("a.es_doc_emi, "); 
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_ESTADOS](a.es_doc_emi, 'TDTV_REMITOS') de_es_doc_emi, ");
        sql.append("a.nu_dia_ate, "); 
        sql.append("a.de_asu, "); 
        sql.append("-- a.co_pro, "); 
        sql.append("-- a.co_sub, "); 
        sql.append("a.ti_cap, "); 
        sql.append("a.co_exp, ");
        sql.append("a.co_use_cre, ");
        sql.append("a.fe_use_cre, "); 
        sql.append("a.co_use_mod, "); 
        sql.append("a.fe_use_mod, "); 
        sql.append("a.nu_ann_exp, ");
        sql.append("a.nu_sec_exp, "); 
        sql.append("a.nu_det_exp, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_NU_EXPEDIENTE](a.nu_ann_exp,a.nu_sec_exp) NU_EXPEDIENTE ");
        sql.append("FROM IDOSGD.TDTV_REMITOS a "); 
        sql.append("WHERE A.NU_ANN = ? ");
        sql.append("AND A.NU_EMI = ? ");

        DocumentoEmiBean documentoEmiBean = new DocumentoEmiBean();
        try {
            documentoEmiBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoEmiBean.class),
                    new Object[]{pnuAnn, pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            documentoEmiBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoEmiBean;
    }

    @Override
    public DocumentoEmiBean getDocumentoEmiAdm(String pnuAnn, String pnuEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sql = new StringBuilder();
        sql.append("select A.*, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](A.CO_EMP_EMI) DE_EMP_EMI, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](A.CO_EMP_RES) DE_EMP_RES, ");
        sql.append("CONVERT(VARCHAR(10), A.FE_EMI, 103) FE_EMI_CORTA, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_ESTADOS](A.ES_DOC_EMI,'TDTV_REMITOS') DE_ES_DOC_EMI, ");
        sql.append("B.FE_EXP, ");
        sql.append("CONVERT(VARCHAR(10), B.FE_EXP, 103) FE_EXP_CORTA,B.NU_EXPEDIENTE, ");
        sql.append("B.CO_PROCESO, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_PROCESO_EXP](B.CO_PROCESO) DE_PROCESO, ");
        sql.append("RR.IN_FIRMA_ANEXO ");
        sql.append("from IDOSGD.TDTV_REMITOS A "); 
        sql.append("left join IDOSGD.TDTC_EXPEDIENTE B on A.NU_ANN_EXP = B.NU_ANN_EXP and A.NU_SEC_EXP = B.NU_SEC_EXP, ");
        sql.append("IDOSGD.TDTX_REMITOS_RESUMEN RR ");
        sql.append("where A.NU_ANN = ? "); 
        sql.append("AND A.NU_EMI = ? ");
        sql.append("AND RR.NU_ANN = ?  ");
        sql.append("AND RR.NU_EMI = ? ");

        DocumentoEmiBean documentoEmiBean = new DocumentoEmiBean();
        try {
            documentoEmiBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoEmiBean.class),
                    new Object[]{pnuAnn, pnuEmi, pnuAnn, pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            documentoEmiBean = null;
        } catch (Exception e) {
            System.out.println("nu_ann="+pnuAnn+","+"nu_emi="+pnuEmi);
            e.printStackTrace();
        }
        return documentoEmiBean;
    }

    @Override
    public ExpedienteBean getExpDocumentoEmitido(String pnuAnnExp, String pnuSecExp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sql = new StringBuilder();
        sql.append("select B.NU_EXPEDIENTE, ");
        sql.append("B.FE_EXP, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_PROCESO_EXP](B.CO_PROCESO) DE_PROCESO, ");
        sql.append("CONVERT(VARCHAR(10), B.FE_EXP, 103) FE_EXP_CORTA, ");
        sql.append("B.NU_ANN_EXP, ");
        sql.append("B.NU_SEC_EXP, ");
        sql.append("B.CO_PROCESO ");
        sql.append("from IDOSGD.TDTC_EXPEDIENTE B "); 
        sql.append("where B.NU_ANN_EXP = ? "); 
        sql.append("AND B.NU_SEC_EXP = ? ");

        ExpedienteBean expedienteBean = new ExpedienteBean();
        try {
            expedienteBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(ExpedienteBean.class),
                    new Object[]{pnuAnnExp, pnuSecExp});
        } catch (EmptyResultDataAccessException e) {
            expedienteBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expedienteBean;
    }

    @Override
    public EmpleadoBean getEmpleadoLocaltblDestinatario(String pcoDependencia) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.CO_LOC CO_LOCAL, ");
        sql.append("	   [IDOSGD].[PK_SGD_DESCRIPCION_DE_LOCAL](A.CO_LOC) DE_LOCAL, ");
        sql.append("	   B.CO_EMPLEADO CEMP_CODEMP, ");
        sql.append("	   [IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](B.CO_EMPLEADO) COMP_NAME ");
        sql.append("FROM IDOSGD.sitm_local_dependencia A, ");
        sql.append("	 IDOSGD.rhtm_dependencia B ");
        sql.append("WHERE B.CO_DEPENDENCIA = ? ");
        sql.append("AND A.CO_DEP = ? ");

        EmpleadoBean empleadoBean = new EmpleadoBean();
        try {
            empleadoBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class),
                    new Object[]{pcoDependencia, pcoDependencia});
        } catch (EmptyResultDataAccessException e) {
            empleadoBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empleadoBean;
    }

    @Override
    public List<EmpleadoBean> getPersonalDestinatario(String pcoDepen) {
        StringBuilder sql = new StringBuilder();
        List<EmpleadoBean> list = new ArrayList<EmpleadoBean>();

        sql.append("SELECT e.cemp_apepat, e.cemp_apemat, e.cemp_denom, e.CEMP_CODEMP "); 
        sql.append("FROM IDOSGD.RHTM_PER_EMPLEADOS e, "); 
        sql.append("( ");
        sql.append("SELECT CEMP_CODEMP "); 
        sql.append("FROM IDOSGD.RHTM_PER_EMPLEADOS "); 
        sql.append("where CEMP_EST_EMP = '1' "); 
        sql.append("  and (CO_DEPENDENCIA=? or CEMP_CO_DEPEND=?) "); 
        sql.append("union "); 
        sql.append("select co_emp from IDOSGD.tdtx_dependencia_empleado  where co_dep=? and es_emp='0' "); 
        sql.append("union "); 
        sql.append("select co_empleado from IDOSGD.rhtm_dependencia where co_dependencia = ? "); 
        sql.append(") a "); 
        sql.append("where e.cemp_codemp = a.cemp_codemp ");
        sql.append("order by 1 ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class),
                    new Object[]{pcoDepen, pcoDepen, pcoDepen,pcoDepen});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<MotivoBean> getLstMotivoxTipoDocumento(String pcoDepen, String pcoTipDoc) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sql = new StringBuilder();
        List<MotivoBean> list = new ArrayList<MotivoBean>();

        sql.append("SELECT a.de_mot, ");
        sql.append("       a.co_mot ");
        sql.append("  FROM IDOSGD.tdtr_motivo         a, ");
        sql.append("       IDOSGD.tdtx_moti_docu_depe b ");
        sql.append(" WHERE a.co_mot     = b.co_mot ");
        sql.append("   AND b.co_dep     = ? ");
        sql.append("   AND b.co_tip_doc = ? ");
        sql.append("UNION ");
        sql.append("SELECT de_mot, ");
        sql.append("       co_mot ");
        sql.append("FROM IDOSGD.tdtr_motivo ");  
        sql.append("where co_mot in ('0','1') ");
        sql.append(" ORDER BY 1 ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(MotivoBean.class),
                    new Object[]{pcoDepen, pcoTipDoc});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DocumentoBean> getLstDocEmitidoRef(String pcoEmpEmi, String pcoDepen, String pannio, String ptiDoc, String pnuDoc) {
        StringBuilder sql = new StringBuilder();
        List<DocumentoBean> list = new ArrayList<DocumentoBean>();

        sql.append("SELECT A.* ");
        sql.append("FROM ( ");        

        sql.append("select E.*, ROW_NUMBER() OVER (ORDER BY E.fe_emi) AS ROWNUM ");
        sql.append("from( ");
        sql.append("select "); 
        sql.append("RE.fe_emi, ");
        sql.append("SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](RE.co_tip_doc_adm), 1, 100) DE_TIP_DOC_ADM, ");
        sql.append("RR.NU_DOC, ");                
        sql.append("CONVERT(VARCHAR(10), RE.fe_emi, 3) FE_EMI_CORTA, ");
        sql.append("null FE_REC_DOC_CORTA, ");
        sql.append("RE.NU_ANN, ");
        sql.append("RE.NU_EMI, ");
        sql.append("NULL NU_DES, ");
        sql.append("replace(ltrim(rtrim(RE.de_asu)), char(10), ' ') DE_ASU, "); 
        sql.append("RE.CO_TIP_DOC_ADM, ");  
        sql.append("SUBSTRING(RR.NU_EXPEDIENTE, 1, 20) NU_EXPEDIENTE, ");                
        sql.append("RE.NU_ANN_EXP, ");
        sql.append("RE.NU_SEC_EXP ");
        sql.append("from IDOSGD.TDTV_REMITOS RE, IDOSGD.TDTX_REMITOS_RESUMEN RR ");
        sql.append("where RE.nu_ann = ? ");
        sql.append("AND RE.NU_ANN=RR.NU_ANN ");
        sql.append("AND RE.NU_EMI=RR.NU_EMI ");
        sql.append("AND RE.es_eli = '0' ");
        sql.append("and RE.es_doc_emi not in ('9','7','5') ");
        sql.append("AND RE.CO_GRU = '1' ");
        sql.append("AND RE.co_dep_emi in "); 
        sql.append("(select ? ");
        sql.append("union "); 
        sql.append("  SELECT co_dep_ref ");
        sql.append("    FROM IDOSGD.tdtx_referencia ");
        sql.append("   WHERE co_dep_emi = ? ");
        sql.append("     AND ti_emi = 'D' ");
        sql.append("     AND es_ref = '1') ");
        sql.append("AND RE.co_tip_doc_adm = ? ");
        if(pnuDoc != null && pnuDoc.trim().length() > 0) {
            pnuDoc = Utility.getInstancia().rellenaCerosIzquierda(pnuDoc, 6);
            sql.append("AND (RE.NU_DOC_EMI = '").append(pnuDoc).append("' OR RR.NU_EXPEDIENTE LIKE '%").append(pnuDoc).append("') ");
        }
        
        sql.append("UNION ");

        sql.append("select "); 
        sql.append("RE.fe_emi, ");                        
        sql.append("SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](RE.co_tip_doc_adm), 1, 100) TIPO, ");
        sql.append("RR.NU_DOC numero, ");
        sql.append("CONVERT(VARCHAR(10), RE.fe_emi, 3) fecha_emision, ");
        sql.append("null fecha_recepcion, ");
        sql.append("RE.NU_ANN, ");
        sql.append("RE.NU_EMI, ");
        sql.append("NULL NU_DES, ");
        sql.append("replace(ltrim(rtrim(RE.de_asu)), char(10), ' ') ASUNTO, "); 
        sql.append("RE.CO_TIP_DOC_ADM, ");  
        sql.append("SUBSTRING(RR.NU_EXPEDIENTE, 1, 20) NU_EXPEDIENTE, ");
        sql.append("RE.NU_ANN_EXP, ");
        sql.append("RE.NU_SEC_EXP ");
        sql.append("from IDOSGD.TDTV_REMITOS RE, IDOSGD.TDTX_REMITOS_RESUMEN RR ");
        sql.append("where RE.nu_ann = ? ");
        sql.append("AND RE.NU_ANN=RR.NU_ANN ");
        sql.append("AND RE.NU_EMI=RR.NU_EMI ");
        sql.append("AND RE.es_eli = '0' ");
        sql.append("AND RE.es_doc_emi not in ('9','7','5') ");
        sql.append("AND RE.CO_GRU = '2' ");
        sql.append("AND RE.co_tip_doc_adm = ? ");
        if (pnuDoc != null && pnuDoc.trim().length() > 0) {
            pnuDoc = Utility.getInstancia().rellenaCerosIzquierda(pnuDoc, 6);
            sql.append("AND (RE.NU_DOC_EMI = '").append(pnuDoc).append("' OR RR.NU_EXPEDIENTE LIKE '%").append(pnuDoc).append("') ");
        }
        sql.append("AND RE.co_emp_emi = ? ");

        sql.append(") E ");

        sql.append(")AS A "); 

        sql.append(" WHERE ROWNUM < 201 ");
        sql.append(" ORDER BY A.fe_emi DESC "); // Validar

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class),
                    new Object[]{pannio, pcoDepen, pcoDepen, ptiDoc, pannio, ptiDoc, pcoEmpEmi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DocumentoBean> getLstDocRecepcionadoRef(String pcoDepen, String pannio, String ptiDoc, String pnuDoc) {
        StringBuilder sql = new StringBuilder();
        List<DocumentoBean> list = new ArrayList<DocumentoBean>();

        sql.append("SELECT A.* ");
        sql.append("FROM ( ");        

        sql.append("select E.*, ROW_NUMBER() OVER (ORDER BY E.FE_REC_DOC) AS ROWNUM ");
        sql.append("from( ");
        sql.append("select ");
        sql.append("d.FE_REC_DOC, ");
        sql.append("SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](r.co_tip_doc_adm), 1, 100) de_tip_doc_adm, ");
        sql.append("SUBSTRING(rr.nu_doc,1,50) NU_DOC, ");
        sql.append("CONVERT(VARCHAR(10), r.FE_EMI, 3) FE_EMI_CORTA, ");
        sql.append("CONVERT(VARCHAR(10), d.FE_REC_DOC, 3) FE_REC_DOC_CORTA, ");
        sql.append("r.nu_ann, ");
        sql.append("r.nu_emi, ");
        sql.append("d.nu_des, ");
        sql.append("REPLACE(LTRIM(RTRIM(r.DE_ASU)), CHAR(10),' ') DE_ASU, ");
        sql.append("r.co_tip_doc_adm, ");
        sql.append("SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](d.co_dep_des), 1, 200) de_dep_des, ");
        sql.append("SUBSTRING(rr.nu_expediente,1,20) NU_EXPEDIENTE, ");
        sql.append("r.NU_ANN_EXP, ");
        sql.append("r.NU_SEC_EXP ");
        sql.append("from IDOSGD.TDTV_REMITOS R, ");
        sql.append("	 IDOSGD.TDTV_DESTINOS D, ");
        sql.append("	 IDOSGD.TDTX_REMITOS_RESUMEN RR ");
        sql.append("where ");
        sql.append("r.es_eli = '0' ");
        sql.append("AND d.es_eli = '0' ");
        sql.append("AND r.nu_ann = ? ");
        sql.append("and rr.nu_ann=r.nu_ann ");
        sql.append("AND rr.nu_emi = r.nu_emi ");
        sql.append("AND d.nu_ann = r.nu_ann ");
        sql.append("AND d.nu_emi = r.nu_emi ");
        sql.append("AND r.es_doc_emi NOT IN ('5', '9', '7') ");
        sql.append("and d.es_doc_rec <> '0' ");  
        sql.append("and d.co_dep_des in "); 
        sql.append("(select ? ");  
        sql.append("union "); 
        sql.append("  SELECT co_dep_ref ");
        sql.append("    FROM IDOSGD.tdtx_referencia ");
        sql.append("   WHERE co_dep_emi = ? ");
        sql.append("     AND ti_emi = 'D' ");
        sql.append("     AND es_ref = '1') ");
        sql.append("AND r.co_tip_doc_adm = ? ");
        if (pnuDoc != null && pnuDoc.trim().length() > 0){
            pnuDoc = Utility.getInstancia().rellenaCerosIzquierda(pnuDoc, 6);
            sql.append("AND (R.NU_DOC_EMI = '").append(pnuDoc).append("' OR RR.NU_EXPEDIENTE LIKE '%").append(pnuDoc).append("') ");
        }
        sql.append(") E ");

        sql.append(") A "); 
        sql.append("WHERE ROWNUM < 201 ");
        sql.append("order by FE_REC_DOC desc ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class),
                    new Object[]{pannio, pcoDepen, pcoDepen, ptiDoc});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    //@Override
    public List<DestinatarioDocumentoEmiBean> _getLstDestintariotlbEmi(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        List<DestinatarioDocumentoEmiBean> list = new ArrayList<DestinatarioDocumentoEmiBean>();
        sql.append("SSELECT a.nu_ann, ");
        sql.append("Sa.nu_emi, ");
        sql.append("Sa.nu_des, ");
        sql.append("Sa.co_loc_des co_local, ");
        sql.append("SSUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_LOCAL](a.co_loc_des), 1, 100) de_local, ");
        sql.append("Sa.co_dep_des co_dependencia, ");
        sql.append("SSUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](a.co_dep_des), 1, 100) de_dependencia, ");
        sql.append("Sa.co_emp_des co_empleado, ");
        sql.append("SSUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](a.co_emp_des), 1, 100) de_empleado, ");
        sql.append("Sa.co_mot co_tramite, ");
        sql.append("SSUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_MOTIVO](a.co_mot), 1, 100) de_tramite, ");
        sql.append("Sa.co_pri co_prioridad, ");
        sql.append("Sa.de_pro de_indicaciones ");
        sql.append("SFROM IDOSGD.tdtv_destinos a ");
        sql.append("SWHERE a.nu_ann = ? "); 
        sql.append("SAND a.nu_emi = ? ");
        sql.append("SAND a.TI_DES = '01' "); 
        sql.append("SAND a.ES_ELI='0' "); 
        sql.append("SAND a.NU_EMI_REF is null ");
        sql.append("SORDER BY 3 ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioDocumentoEmiBean.class),
                    new Object[]{pnuAnn, pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DestinatarioDocumentoEmiBean> getLstDestintariotlbEmi(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        List<DestinatarioDocumentoEmiBean> list = new ArrayList<DestinatarioDocumentoEmiBean>();
        sql.append("select a.nu_ann, ");
        sql.append("a.nu_emi, ");
        sql.append("a.nu_des, ");
        sql.append("a.co_loc_des co_local, ");
        sql.append("CASE "); 
        sql.append("WHEN a.co_loc_des IS NULL THEN NULL ");
        sql.append("ELSE SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_LOCAL](a.co_loc_des), 1, 100) ");
        sql.append("END de_local, ");
        sql.append("a.co_dep_des co_dependencia, ");
        sql.append("CASE "); 
        sql.append("WHEN a.co_dep_des IS NULL THEN NULL ");
        sql.append("ELSE SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](a.co_dep_des), 1, 100) ");
        sql.append("END de_dependencia, ");
        sql.append("a.co_emp_des co_empleado, ");
        sql.append("CASE "); 
        sql.append("WHEN a.co_emp_des IS NULL THEN NULL ");
        sql.append("ELSE SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](a.co_emp_des), 1, 100) ");
        sql.append("END de_empleado, ");
        sql.append("a.co_mot co_tramite, ");
        sql.append("CASE "); 
        sql.append("WHEN a.co_mot IS NULL THEN NULL ");
        sql.append("ELSE SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_MOTIVO](a.co_mot), 1, 100) ");
        sql.append("END de_tramite, ");
        sql.append("a.co_pri co_prioridad, ");
        sql.append("a.de_pro de_indicaciones, ");
        sql.append("a.NU_RUC_DES nu_ruc, ");
        sql.append("CASE "); 
        sql.append("WHEN a.NU_RUC_DES IS NULL THEN NULL ");
        sql.append("ELSE SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_PROVEEDOR](a.NU_RUC_DES), 1, 100) ");
        sql.append("END de_proveedor, ");
        sql.append("a.NU_DNI_DES nu_dni, ");
        sql.append("CASE "); 
        sql.append("WHEN a.NU_DNI_DES IS NULL THEN NULL ");
        sql.append("ELSE SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_ANI_SIMIL](a.NU_DNI_DES), 1, 100) ");
        sql.append("END de_ciudadano, ");
        sql.append("a.CO_OTR_ORI_DES co_otro_origen, ");
        sql.append("CASE "); 
        sql.append("WHEN a.CO_OTR_ORI_DES IS NULL THEN NULL ");
        sql.append("ELSE (SELECT C.DE_APE_PAT_OTR + ' ' + C.DE_APE_MAT_OTR + ', ' + C.DE_NOM_OTR + ' - ' + ");
        sql.append("			 C.DE_RAZ_SOC_OTR + '##' + ISNULL(B.CELE_DESELE,'   ') + '##' + ");
        sql.append("			 C.NU_DOC_OTR_ORI ");  
        sql.append("	  FROM IDOSGD.TDTR_OTRO_ORIGEN C ");
        sql.append("		   LEFT OUTER JOIN ( ");
        sql.append("			   SELECT CELE_CODELE, CELE_DESELE ");
        sql.append("			   FROM IDOSGD.SI_ELEMENTO ");
        sql.append("			   WHERE CTAB_CODTAB = 'TIP_DOC_IDENT') B "); 
        sql.append("		   ON C.CO_TIP_OTR_ORI = B.CELE_CODELE ");
        sql.append("	  WHERE C.CO_OTR_ORI = a.CO_OTR_ORI_DES ");
        sql.append("	 ) ");
        sql.append("END de_otro_origen_full, ");
        sql.append("a.ti_des co_tipo_destino, ");
        sql.append("ISNULL(a.ES_ENV_POR_TRA, '0') ENV_MESA_PARTES ");
        sql.append("FROM IDOSGD.tdtv_destinos a ");
        sql.append("where nu_ann = ? "); 
        sql.append("and nu_emi = ? ");
        sql.append("AND ES_ELI='0' "); 
        sql.append("AND NU_EMI_REF is null ");
        sql.append("order by 3 ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioDocumentoEmiBean.class),
                    new Object[]{pnuAnn, pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ReferenciaBean> getLstDocumReferenciatblEmi(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        List<ReferenciaBean> list = new ArrayList<ReferenciaBean>();
        sql.append("SELECT "); 
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](a.co_tip_doc_adm) de_tipo_doc, "); 
        sql.append("CASE a.ti_emi "); 
        sql.append("	 WHEN '01' THEN a.nu_doc_emi + '-' + a.nu_ann + '-' + a.de_doc_sig ");
        sql.append("	 WHEN '05' THEN a.nu_doc_emi + '-' + a.nu_ann + '-' + a.de_doc_sig ");
        sql.append("	 ELSE a.de_doc_sig ");
        sql.append("END li_nu_doc, ");
        sql.append("SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](a.co_dep_emi), 1, 100) de_dep_emi, "); 
        sql.append("CONVERT(VARCHAR(10), a.fe_emi, 3) fe_emi_corta, "); 
        sql.append("b.nu_ann, ");
        sql.append("b.nu_emi, ");
        sql.append("ISNULL(LTRIM(RTRIM(CAST(b.nu_des AS varchar))),'N') nu_des, ");
        sql.append("b.nu_ann_ref, ");
        sql.append("b.nu_emi_ref, ");
        sql.append("ISNULL(LTRIM(RTRIM(CAST(b.nu_des_ref AS varchar))),'N') nu_des_ref, ");
        sql.append("b.co_ref, ");
        sql.append("CASE ISNULL(LTRIM(RTRIM(CAST(b.nu_des_ref AS varchar))),'N') ");
        sql.append("	 WHEN 'N' THEN 'emi' ");
        sql.append("	 ELSE 'rec' ");
        sql.append("END tip_doc_ref, ");
        sql.append("a.co_tip_doc_adm, ");
        sql.append("'BD' accion_bd ");
        sql.append("FROM IDOSGD.tdtv_remitos a, ");
        sql.append("	 IDOSGD.TDTR_REFERENCIA b "); 
        sql.append("WHERE a.nu_ann = b.nu_ann_ref ");
        sql.append("AND a.nu_emi = b.nu_emi_ref ");
        sql.append("AND b.nu_ann = ? ");
        sql.append("AND b.nu_emi = ? ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ReferenciaBean.class),
                    new Object[]{pnuAnn, pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String getTipoDestinatarioEmi(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        String result = null;
        sql.append("SELECT MAX(ti_des) ");
        sql.append("FROM IDOSGD.tdtv_destinos ");
        sql.append("WHERE nu_emi = ? ");
        sql.append("AND nu_ann = ? ");
        sql.append("AND ES_ELI = '0' ");

        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pnuEmi, pnuAnn});
        } catch (Exception e) {
            result = "1";
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String updDocumentoEmiBean(String nuAnn, String nuEmi, DocumentoEmiBean documentoEmiBean, ExpedienteBean expedienteBean, RemitenteEmiBean remitenteEmiBean, String pcoUserMod) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("update IDOSGD.tdtv_remitos A \n"
                + "set A.CO_USE_MOD=?,");
        if (documentoEmiBean != null) {
//                    sqlUpd.append("A.DE_ASU='"+ documentoEmiBean.getDeAsu() + "'\n" +
//                    ",A.NU_DIA_ATE='"+ documentoEmiBean.getNuDiaAte() + "'\n" +
//                    ",A.CO_TIP_DOC_ADM='"+ documentoEmiBean.getCoTipDocAdm() + "'\n" +        
//                    ",A.ES_DOC_EMI='"+ documentoEmiBean.getEsDocEmi() + "',\n" +
//                    ",A.NU_DOC_EMI='"+ documentoEmiBean.getNuDocEmi() + "',\n" +
//                    ",A.NU_COR_DOC='"+ documentoEmiBean.getNuCorDoc()+ "',\n");
            sqlUpd.append("A.DE_ASU=?\n"
                    + ",A.NU_DIA_ATE=?\n"
                    + ",A.CO_TIP_DOC_ADM=?\n"
                    + ",A.ES_DOC_EMI=?\n"
                    + ",A.NU_DOC_EMI=?\n"
                    + ",A.DE_DOC_SIG=?\n"
                    + ",A.NU_COR_DOC= CASE \n"
                    + "WHEN ? IS NULL THEN ? "
                    + "ELSE (SELECT nvl(MAX(nu_cor_doc), 0) + 1\n"
                    + "FROM tdtv_remitos\n"
                    + "WHERE nu_ann = ?\n"
                    + "AND co_dep_emi = ?\n"
                    + "AND co_tip_doc_adm = ?\n"
                    + "AND ti_emi = ?) END,\n");
        }
        if (expedienteBean != null) {
            sqlUpd.append("A.NU_SEC_EXP='" + expedienteBean.getNuSecExp() + "',\n");
        }
        if (remitenteEmiBean != null) {
            sqlUpd.append("A.CO_LOC_EMI='" + remitenteEmiBean.getCoLocal() + "'\n"
                    + ",A.CO_DEP_EMI='" + remitenteEmiBean.getCoDependencia() + "'\n"
                    + ",A.CO_EMP_EMI='" + remitenteEmiBean.getCoEmpFirma() + "',\n");
        }
        sqlUpd.append("A.FE_USE_MOD=CURRENT_TIMESTAMP "
                + "where\n"
                + "A.NU_ANN=? and\n"
                + "A.NU_EMI=?");

        try {
            if (documentoEmiBean != null) {
                this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{pcoUserMod, documentoEmiBean.getDeAsu(), documentoEmiBean.getNuDiaAte(),
                    documentoEmiBean.getCoTipDocAdm(), documentoEmiBean.getEsDocEmi(), documentoEmiBean.getNuDocEmi(), documentoEmiBean.getDeDocSig(),
                    documentoEmiBean.getNuDocEmi(), documentoEmiBean.getNuCorDoc(), nuAnn, documentoEmiBean.getCoDepEmi(), documentoEmiBean.getCoTipDocAdm(),
                    documentoEmiBean.getTiEmi(), nuAnn, nuEmi});
            } else {
                this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{pcoUserMod, nuAnn, nuEmi});
            }
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();

        }
        return vReturn;
    }

    @Override
    public String insDocumentoEmiBean(DocumentoEmiBean documentoEmiBean, ExpedienteBean expedienteBean, RemitenteEmiBean remitenteEmiBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        documentoEmiBean.setTiEmi("01");
        StringBuilder sqlUpd = new StringBuilder();
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT ISNULL(MAX(nu_cor_emi), 0) + 1 ");
        sqlQry.append("FROM IDOSGD.tdtv_remitos ");
        sqlQry.append("WHERE nu_ann = ? ");
        sqlQry.append("AND co_dep_emi = ? ");
        sqlQry.append("AND ti_emi = '01' ");

        sqlUpd.append("INSERT INTO IDOSGD.tdtv_remitos(\n"
                + "NU_ANN,\n"
                + "NU_EMI,\n"
                + "NU_COR_EMI,\n"
                + "CO_LOC_EMI,\n"
                + "CO_DEP_EMI,\n"
                + "CO_EMP_EMI,\n"
                + "CO_EMP_RES, \n"
                + "TI_EMI,\n"
                + "FE_EMI,\n"
                + "CO_GRU,\n"
                + "DE_ASU, \n"
                + "ES_DOC_EMI, \n"
                + "NU_DIA_ATE, \n"
                + "TI_CAP, \n"
                + "CO_TIP_DOC_ADM, \n"
                + "NU_COR_DOC, \n"
                + "DE_DOC_SIG, \n"
                + "NU_ANN_EXP, \n"
                + "NU_SEC_EXP, \n"
                + "NU_DET_EXP, \n"
                + "ES_ELI,\n"
                + "NU_DOC_EMI,\n"
                + "CO_USE_CRE,\n"
                + "FE_USE_CRE,\n"
                + "CO_USE_MOD,\n"
                + "FE_USE_MOD)\n"
                + "VALUES(?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,'1',?,?,?,'03',?,\n"
                + "?,?,?,?,1,\n"
                + "'0',?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP)");
        
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.dataSource).withProcedureName("[IDOSGD].[PK_SGD_UTILS_SEC_REMITOS_NU_EMI]")
                                            .withoutProcedureColumnMetaDataAccess()
                                            .declareParameters(new SqlOutParameter("COD_NU_EMI", Types.INTEGER));
                    
            Map out = simpleJdbcCall.execute();
            int codigo = ((Integer)out.get("COD_NU_EMI"));
            String snuEmi = String.format("%010d", codigo);
    
            // String snuEmi = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class);
            
            String snuCorEmi = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{documentoEmiBean.getNuAnn(), documentoEmiBean.getCoDepEmi()});
            documentoEmiBean.setNuEmi(snuEmi);
            documentoEmiBean.setNuCorEmi(snuCorEmi);
            //documentoEmiBean.setNuCorDoc(vnuCorDoc);

            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoEmiBean.getNuAnn(), snuEmi,snuCorEmi==null?null:Integer.parseInt(snuCorEmi),
                documentoEmiBean.getCoLocEmi(), documentoEmiBean.getCoDepEmi(), documentoEmiBean.getCoEmpEmi(), documentoEmiBean.getCoEmpRes(),
                documentoEmiBean.getTiEmi(), documentoEmiBean.getDeAsu(), documentoEmiBean.getEsDocEmi(),documentoEmiBean.getNuDiaAte()==null?null:Integer.parseInt(documentoEmiBean.getNuDiaAte()),documentoEmiBean.getCoTipDocAdm(),
                documentoEmiBean.getNuCorDoc()==null?null:Integer.parseInt(documentoEmiBean.getNuCorDoc()), documentoEmiBean.getDeDocSig(),documentoEmiBean.getNuAnnExp(), documentoEmiBean.getNuSecExp(), 
                documentoEmiBean.getNuDocEmi()==null?null:Integer.parseInt(documentoEmiBean.getNuDocEmi()),documentoEmiBean.getCoUseMod(), documentoEmiBean.getCoUseMod()});

            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Numero de Documento Duplicado.";
            con.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();

        }
        return vReturn;
    }

    @Override
    public String insDestinatarioDocumentoEmi(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("INSERT INTO IDOSGD.TDTV_DESTINOS(\n"
                + "NU_ANN,\n"
                + "NU_EMI,\n"
                + "NU_DES,\n"
                + "CO_LOC_DES,\n"
                + "CO_DEP_DES,\n"
                + "TI_DES,\n"
                + "CO_EMP_DES,\n"
                + "CO_PRI,\n"
                + "DE_PRO,\n"
                + "CO_MOT, \n"
                + "CO_OTR_ORI_DES, \n"
                + "NU_DNI_DES, \n"
                + "NU_RUC_DES, \n"
                + "ES_ELI,\n"
                + "FE_USE_CRE,\n"
                + "FE_USE_MOD,\n"
                + "CO_USE_MOD,\n"
                + "CO_USE_CRE,\n"
                + "ES_DOC_REC,\n"
                + "ES_ENV_POR_TRA)\n"
                + "VALUES(?,?,(select ISNULL(MAX(a.nu_des) + 1, 1) FROM IDOSGD.tdtv_destinos a where \n"
                + "A.NU_ANN=? and\n"
                + "A.NU_EMI=?),?,?,?,?,?,?,?,?,?,?,'0',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,?,'0',?)");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{nuAnn, nuEmi, nuAnn, nuEmi, destinatarioDocumentoEmiBean.getCoLocal(), destinatarioDocumentoEmiBean.getCoDependencia(), destinatarioDocumentoEmiBean.getCoTipoDestino(),
                destinatarioDocumentoEmiBean.getCoEmpleado(), destinatarioDocumentoEmiBean.getCoPrioridad(), destinatarioDocumentoEmiBean.getDeIndicaciones(), destinatarioDocumentoEmiBean.getCoTramite(),
                destinatarioDocumentoEmiBean.getCoOtroOrigen(), destinatarioDocumentoEmiBean.getNuDni(), destinatarioDocumentoEmiBean.getNuRuc(),
                destinatarioDocumentoEmiBean.getCoUseCre(), destinatarioDocumentoEmiBean.getCoUseCre(),destinatarioDocumentoEmiBean.getEnvMesaPartes()});
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Datos Duplicado Destino.";
            //con.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();

        }
        return vReturn;
    }

    @Override
    public String updDestinatarioDocumentoEmi(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("update IDOSGD.TDTV_DESTINOS \n"
                + "set CO_LOC_DES=?\n"
                + ",CO_DEP_DES=?\n"
                + ",CO_EMP_DES=?\n"
                + ",CO_PRI=?\n"
                + ",DE_PRO=?\n"
                + ",CO_MOT=?\n"
                + ",CO_OTR_ORI_DES=? \n"
                + ",NU_DNI_DES=? \n"
                + ",NU_RUC_DES=? \n"
                + ",FE_USE_MOD=CURRENT_TIMESTAMP\n"
                + ",CO_USE_MOD=?\n"
                + ",ES_ENV_POR_TRA=?\n"
                + "where\n"
                + "NU_ANN=? and\n"
                + "NU_EMI=? and\n"
                + "NU_DES=?");
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{destinatarioDocumentoEmiBean.getCoLocal(), destinatarioDocumentoEmiBean.getCoDependencia(),
                destinatarioDocumentoEmiBean.getCoEmpleado(), destinatarioDocumentoEmiBean.getCoPrioridad(), destinatarioDocumentoEmiBean.getDeIndicaciones(), destinatarioDocumentoEmiBean.getCoTramite(),
                destinatarioDocumentoEmiBean.getCoOtroOrigen(), destinatarioDocumentoEmiBean.getNuDni(), destinatarioDocumentoEmiBean.getNuRuc(),
                destinatarioDocumentoEmiBean.getCoUseCre(), destinatarioDocumentoEmiBean.getEnvMesaPartes(), nuAnn, nuEmi, destinatarioDocumentoEmiBean.getNuDes()});
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Datos Duplicado Destino.";
            //con.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();

        }
        return vReturn;
    }

    @Override
    public String delDestinatarioDocumentoEmi(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlDel = new StringBuilder();
        sqlDel.append("delete from IDOSGD.TDTV_DESTINOS\n"
                + "WHERE NU_ANN = ?\n"
                + "AND NU_EMI = ?\n"
                + "and NU_DES=?");
//        sqlUpd.append("update TDTV_DESTINOS \n"
//                + "set ES_ELI='1'\n"
//                + ",FE_USE_CRE=CURRENT_TIMESTAMP\n"
//                + ",FE_USE_MOD=CURRENT_TIMESTAMP\n"
//                + ",CO_USE_MOD='ADM'\n"
//                + ",CO_USE_CRE='ADM'\n"
//                + "where\n"
//                + "NU_ANN=? and\n"
//                + "NU_EMI=? and\n"
//                + "NU_DES=?");
        try {
            this.jdbcTemplate.update(sqlDel.toString(), new Object[]{nuAnn, nuEmi, destinatarioDocumentoEmiBean.getNuDes()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();

        }
        return vReturn;
    }

    @Override
    public String insReferenciaDocumentoEmi(String nuAnn, String nuEmi, ReferenciaEmiDocBean referenciaEmiDocBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("INSERT INTO IDOSGD.tdtr_referencia( ");
        sqlUpd.append("NU_ANN, ");
        sqlUpd.append("NU_EMI, ");
        sqlUpd.append("CO_REF, ");
        sqlUpd.append("NU_ANN_REF, ");
        sqlUpd.append("NU_EMI_REF, ");
        sqlUpd.append("NU_DES_REF, ");
        sqlUpd.append("CO_USE_CRE, ");
        sqlUpd.append("FE_USE_CRE) ");
        sqlUpd.append("VALUES(?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) ");

        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.dataSource).withProcedureName("[IDOSGD].[PK_SGD_UTILS_SEC_REFERENCIA_CO_REF]")
                                            .withoutProcedureColumnMetaDataAccess()
                                            .declareParameters(new SqlOutParameter("COD_NU_ANN_REF", Types.INTEGER));
                    
            Map out = simpleJdbcCall.execute();
            int codigo = ((Integer)out.get("COD_NU_ANN_REF"));
            String cod_num_ann_ref = String.format("%010d", codigo);            
            
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{nuAnn, nuEmi, cod_num_ann_ref, referenciaEmiDocBean.getNuAnn(), referenciaEmiDocBean.getNuEmi(),
                referenciaEmiDocBean.getNuDes().equals("")?null:referenciaEmiDocBean.getNuDes(), referenciaEmiDocBean.getCoUseCre()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();

        }
        return vReturn;
    }

    @Override
    public String updReferenciaDocumentoEmi(String nuAnn, String nuEmi, ReferenciaEmiDocBean referenciaEmiDocBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("update IDOSGD.tdtr_referencia \n"
                + "set NU_ANN_REF = ?,\n"
                + "NU_EMI_REF = ?,\n"
                + "NU_DES_REF = ?\n"
                + "WHERE NU_ANN = ? AND NU_EMI = ? \n"
                + "AND CO_REF = ?");
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{referenciaEmiDocBean.getNuAnn(), referenciaEmiDocBean.getNuEmi(),
                referenciaEmiDocBean.getNuDes(), nuAnn, nuEmi, referenciaEmiDocBean.getCoRef()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String delReferenciaDocumentoEmi(String nuAnn, String nuEmi, ReferenciaEmiDocBean referenciaEmiDocBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlIns = new StringBuilder();
        sqlIns.append("DELETE FROM IDOSGD.tdtr_referencia\n"
                + "WHERE NU_ANN = ? AND NU_EMI = ? \n"
                + "AND NU_ANN_REF = ? AND NU_EMI_REF = ?");

        try {
            this.jdbcTemplate.update(sqlIns.toString(), new Object[]{nuAnn, nuEmi, referenciaEmiDocBean.getNuAnn(), referenciaEmiDocBean.getNuEmi()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public DocumentoEmiBean getDocumentoEmiAdmNew(String sEstadoDocEmi, String codDependencia) {
        StringBuilder sql = new StringBuilder();
        sql.append("select [IDOSGD].[PK_SGD_DESCRIPCION_DE_SIGLA](?) de_doc_sig, ");
        sql.append("CO_EMPLEADO co_emp_emi, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](CO_EMPLEADO) de_emp_emi, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_ESTADOS](?, 'TDTV_REMITOS') de_es_doc_emi, ");
        sql.append("'0' existe_doc, ");
        sql.append("'0' existe_anexo, ");
        sql.append("? co_es_doc_emi "); 
        sql.append("from IDOSGD.rhtm_dependencia ");
        sql.append("where CO_DEPENDENCIA = ? ");

        DocumentoEmiBean documentoEmiBean = new DocumentoEmiBean();
        try {
            documentoEmiBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoEmiBean.class),
                    new Object[]{codDependencia, sEstadoDocEmi, sEstadoDocEmi, codDependencia});
        } catch (EmptyResultDataAccessException e) {
            documentoEmiBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoEmiBean;
    }

    @Override
    public String insExpedienteBean(ExpedienteBean expedienteBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlQry1 = new StringBuilder();
        sqlQry1.append("select RIGHT(REPLICATE('0', 7) + rtrim(ltrim(CAST((ISNULL(max(NU_CORR_EXP), 0) + 1) AS varchar))), 7) nuCorrExp, "); 
        sqlQry1.append("SUBSTRING((SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_DE_SIGLA_CORTA(?)), 1, 6) deSiglaCorta ");
        sqlQry1.append("from IDOSGD.tdtc_expediente ");
        sqlQry1.append("where nu_ann_exp = ? ");
        sqlQry1.append("and co_dep_exp = ? ");
        sqlQry1.append("and co_gru = '1' ");

        StringBuilder sqlQry2 = new StringBuilder();
        sqlQry2.append("select RIGHT(REPLICATE('0', 10) + CAST((ISNULL(max(nu_sec_exp), 0) + 1) AS VARCHAR), 10) ");
        sqlQry2.append("from IDOSGD.tdtc_expediente ");
        sqlQry2.append("where nu_ann_exp = ? ");

        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("INSERT INTO IDOSGD.TDTC_EXPEDIENTE(\n"
                + "nu_ann_exp,\n"
                + "nu_sec_exp,\n"
                + "fe_exp,\n"
                + "co_proceso,\n"
                + "de_detalle,\n"
                + "co_dep_exp,\n"
                + "co_gru,\n"
                + "nu_corr_exp,\n"
                + "nu_expediente,\n"
                + "nu_folios,\n"
                + "nu_plazo,\n"
                + "us_crea_audi,\n"
                + "fe_crea_audi,\n"
                + "us_modi_audi,\n"
                + "fe_modi_audi,\n"
                + "es_estado)\n"
                + "VALUES(?, ?, (SELECT CONVERT(DATETIME, ?, 103)),?,?,?,'1',?,?,?,?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP,'0')");

        try {
            Map mapResult = this.jdbcTemplate.queryForMap(sqlQry1.toString(), new Object[]{expedienteBean.getCoDepExp(), expedienteBean.getNuAnnExp(), expedienteBean.getCoDepExp()});
            String snuSecExp = this.jdbcTemplate.queryForObject(sqlQry2.toString(), String.class, new Object[]{expedienteBean.getNuAnnExp()});
            expedienteBean.setNuSecExp(snuSecExp);
            String snuCorrExp = String.valueOf(mapResult.get("nuCorrExp"));
            String sdeSiglaCorta = String.valueOf(mapResult.get("deSiglaCorta"));

            expedienteBean.setNuExpediente(Utilidades.AjustaCampoIzquierda(sdeSiglaCorta, 6, "0") + expedienteBean.getNuAnnExp() + snuCorrExp);

            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{expedienteBean.getNuAnnExp(), snuSecExp, expedienteBean.getFeExp(),
                expedienteBean.getCoProceso(), expedienteBean.getDeProceso(), expedienteBean.getCoDepExp(), snuCorrExp, expedienteBean.getNuExpediente(),
                expedienteBean.getNuFolios(), expedienteBean.getNuPlazo(), expedienteBean.getUsCreaAudi(), expedienteBean.getUsCreaAudi()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String verificarDocumentoLeido(String pnuAnn, String pnuEmi) {
        String vReturn = "NO_OK";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT ISNULL(MAX(es_doc_rec),'0') ");
        sqlQry.append("FROM IDOSGD.tdtv_destinos ");
        sqlQry.append("WHERE nu_ann = ? ");
        sqlQry.append("AND nu_emi = ? ");
        sqlQry.append("AND es_eli = '0' ");

        try {
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{pnuAnn, pnuEmi});
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String anularDocumentoEmiAdm(DocumentoEmiBean documentoEmiBean) {
        String vReturn = "NO_OK";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT ISNULL(MAX(nu_cor_doc), 1) + 1 ");
        sqlQry.append("FROM IDOSGD.tdtv_remitos ");
        sqlQry.append("WHERE nu_ann         = ? ");
        sqlQry.append("AND ti_emi         = ? ");
        sqlQry.append("AND co_dep_emi     = ? ");
        sqlQry.append("AND co_tip_doc_adm = ? ");
        sqlQry.append("AND ((? IS NOT NULL AND nu_doc_emi = ?) ");
        sqlQry.append("OR ? IS NULL) ");

        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("UPDATE IDOSGD.tdtv_remitos "); 
        sqlUpd.append("set es_doc_emi = ?, ");
        sqlUpd.append("nu_cor_doc = ?, ");
        sqlUpd.append("fe_use_mod=CURRENT_TIMESTAMP, ");
        sqlUpd.append("co_use_mod=? "); 
        sqlUpd.append("WHERE nu_ann = ? ");
        sqlUpd.append("AND nu_emi = ? ");
        sqlUpd.append("AND es_eli = '0' ");

        try {
            String snuCorDoc = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{documentoEmiBean.getNuAnn(), documentoEmiBean.getTiEmi(),
                documentoEmiBean.getCoDepEmi(), documentoEmiBean.getCoTipDocAdm(), documentoEmiBean.getNuDocEmi(), documentoEmiBean.getNuDocEmi(), documentoEmiBean.getNuDocEmi()});

            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoEmiBean.getEsDocEmi(), snuCorDoc, documentoEmiBean.getCoUseMod(),
                documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public List<DestinatarioDocumentoEmiBean> getLstDestinatarioGrupo(String pcoGrupo,String pcoTipDoc) {
        StringBuilder sql = new StringBuilder();
        List<DestinatarioDocumentoEmiBean> list = new ArrayList<DestinatarioDocumentoEmiBean>();

        sql.append("SELECT a.co_dep co_dependencia, "); 
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](a.co_dep) de_dependencia, ");
        sql.append("ISNULL(a.co_emp, b.co_empleado) co_empleado, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](ISNULL(a.co_emp, b.co_empleado)) de_empleado, ");
        sql.append("c.co_loc co_local, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_LOCAL](c.co_loc) de_local,'1' co_prioridad, ");
        sql.append("? co_tramite_first, "); 
        sql.append("? de_tramite_first, "); 
        sql.append("? co_tramite_next, "); 
        sql.append("? de_tramite_next "); 
        sql.append("FROM ( ");   
        sql.append("  select y.cemp_codemp co_emp, "); 
        sql.append("		 x.co_dep ");
        sql.append("  from IDOSGD.tdtd_dep_gru x ");
        sql.append("	   RIGHT OUTER JOIN IDOSGD.rhtm_per_empleados y "); 
        sql.append("	   ON y.cemp_codemp = x.co_emp AND y.cemp_est_emp = '1' ");
        sql.append("  where x.co_gru_des= ? ");
        sql.append("  and x.es_dep_gru = '1' ");
        // sql.append(" order by y.cemp_codemp ");
        sql.append(") a, "); 
        sql.append("IDOSGD.rhtm_dependencia b, "); 
        sql.append("IDOSGD.sitm_local_dependencia c ");
        sql.append("WHERE a.co_dep = b.co_dependencia ");
        sql.append("AND a.co_dep = c.co_dep "); 
        sql.append("order by a.co_dep "); 

        try {
            if(pcoTipDoc.equals("232")){
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioDocumentoEmiBean.class),
                        new Object[]{Constantes.CO_TRAMITE_ATENDER, Constantes.DE_TRAMITE_ATENDER,Constantes.CO_TRAMITE_FINES, Constantes.DE_TRAMITE_FINES, pcoGrupo});                                
            }else{
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioDocumentoEmiBean.class),
                        new Object[]{Constantes.CO_TRAMITE_ORIGINAL, Constantes.DE_TRAMITE_ORIGINAL,Constantes.CO_TRAMITE_COPIA, Constantes.DE_TRAMITE_COPIA, pcoGrupo});                
            }
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    public String updDocumentoObj(final DocumentoObjBean docObjBean) {
        String vReturn = "NO_OK";
        boolean inInsert = false;
        /*Verificamos si es Insert o Update*/
        try {
            int cant = this.jdbcTemplate.queryForInt("select count(nu_ann) from IDOSGD.tdtv_archivo_doc where nu_ann = ? and nu_emi = ?", new Object[]{docObjBean.getNuAnn(), docObjBean.getNuEmi()});
            if (cant > 0) {
                inInsert = false;
            } else {
                inInsert = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();
        }

        /**/

        final LobHandler lobhandler = new DefaultLobHandler();
        if (inInsert) {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO IDOSGD.tdtv_archivo_doc (NU_ANN,NU_EMI,BL_DOC,DE_RUTA_ORIGEN,ES_FIRMA,FEULA)\n"
                    + "VALUES(?,?,?,?,'0', (SELECT CONVERT(VARCHAR(10), CURRENT_TIMESTAMP, 112)))");
            try {
                this.jdbcTemplate.execute(sql.toString(),
                        new AbstractLobCreatingPreparedStatementCallback(lobhandler) {
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                            throws SQLException {
                        ps.setString(1, docObjBean.getNuAnn());
                        ps.setString(2, docObjBean.getNuEmi());
                        lobCreator.setBlobAsBytes(ps, 3, docObjBean.getDocumento());
                        ps.setString(4, docObjBean.getNombreArchivo());
                    }
                });
                vReturn = "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE IDOSGD.TDTV_ARCHIVO_DOC SET BL_DOC = ?, DE_RUTA_ORIGEN = ?, FEULA=(SELECT CONVERT(VARCHAR(10), CURRENT_TIMESTAMP, 112)) \n"
                    + "WHERE NU_ANN = ? \n"
                    + "AND NU_EMI = ? ");
            try {
                this.jdbcTemplate.execute(sql.toString(),
                        new AbstractLobCreatingPreparedStatementCallback(lobhandler) {
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                            throws SQLException {
                        lobCreator.setBlobAsBytes(ps, 1, docObjBean.getDocumento());
                        ps.setString(2, docObjBean.getNombreArchivo());
                        ps.setString(3, docObjBean.getNuAnn());
                        ps.setString(4, docObjBean.getNuEmi());
                    }
                });
                vReturn = "OK";
            } catch (Exception e) {
                e.printStackTrace();
                vReturn = e.getMessage().substring(0, 20);
            }
        }

        return vReturn;
    }

    @Override
    public String getCanDocumentoEmiDuplicados(DocumentoEmiBean documentoEmiBean) {
        String vReturn = "NO_OK";
        try {
            vReturn = this.jdbcTemplate.queryForObject("   SELECT COUNT(1)\n"
                    + "     FROM IDOSGD.tdtv_remitos tr\n"
                    + "    WHERE tr.nu_ann = ?\n"
                    + "      AND tr.co_dep_emi = ?\n"
                    + "      AND tr.co_tip_doc_adm = ?\n"
                    + "      AND tr.nu_doc_emi = ?\n"
                    + "      AND tr.ti_emi = '01'\n"
                    + "      AND nu_cor_doc = 1\n"
                    + "      AND tr.es_doc_emi NOT IN ('9')", String.class, new Object[]{documentoEmiBean.getNuAnn(), documentoEmiBean.getCoDepEmi(),
                documentoEmiBean.getCoTipDocAdm(), documentoEmiBean.getNuDocEmi()});
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public List<EmpleadoBean> getPersonalEditDocAdmEmision(String pcoDepEmi) {
        StringBuilder sql = new StringBuilder();
        List<EmpleadoBean> list = new ArrayList<EmpleadoBean>();
        sql.append("SELECT e.cemp_apepat, ");
        sql.append("	   e.cemp_apemat, ");
        sql.append("	   e.cemp_denom, "); 
        sql.append("	   e.CEMP_CODEMP ");
        sql.append("FROM IDOSGD.RHTM_PER_EMPLEADOS e ");
        sql.append("WHERE e.CEMP_EST_EMP = '1' ");
        sql.append("AND (CEMP_CO_DEPEND in (select CO_DEPENDENCIA "); 
        sql.append("		            from IDOSGD.rhTM_dependencia d "); 
        sql.append("		 	    where d.CO_DEPENDENCIA =? "); 
        sql.append("			    or CO_DEPEN_PADRE=?) "); 
        sql.append("			    OR CO_DEPENDENCIA=? ) ");
        sql.append("UNION ");
        sql.append("SELECT  e.cemp_apepat, ");
        sql.append("	    e.cemp_apemat, ");
        sql.append("	    e.cemp_denom, "); 
        sql.append("	    e.CEMP_CODEMP ");
        sql.append("FROM IDOSGD.RHTM_PER_EMPLEADOS e ");
        sql.append("WHERE e.CEMP_EST_EMP = '1' ");
        sql.append("AND cemp_codemp "); 
        sql.append("in ( ");
        sql.append("select co_emp "); 
        sql.append("from IDOSGD.tdtx_dependencia_empleado "); 
        sql.append("where co_dep=? "); 
        sql.append("and es_emp='0' ");
        sql.append("UNION ");
        sql.append("select CO_EMPLEADO "); 
        sql.append("from IDOSGD.rhtm_dependencia "); 
        sql.append("where co_dependencia=? ");
        sql.append(") ");
        sql.append("ORDER BY 1 ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class),
                    new Object[]{pcoDepEmi, pcoDepEmi, pcoDepEmi, pcoDepEmi, pcoDepEmi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String updEstadoDocumentoEmiAdm(DocumentoEmiBean documentoEmiBean) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("update IDOSGD.tdtv_remitos \n"
                + "set CO_USE_MOD=?\n"
                + ",ES_DOC_EMI=?\n"
                + ",FE_USE_MOD=CURRENT_TIMESTAMP\n"
                + "where\n"
                + "NU_ANN=? and\n"
                + "NU_EMI=?");
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoEmiBean.getCoUseMod(), documentoEmiBean.getEsDocEmi(),
                documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();

        }
        return vReturn;
    }

    @Override
    public String updEstadoDocumentoEmitido(DocumentoEmiBean documentoEmiBean) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("update IDOSGD.tdtv_remitos \n"
                + "set CO_USE_MOD=? \n"
                + ",FE_USE_MOD=CURRENT_TIMESTAMP \n"
                + ",FE_EMI=CURRENT_TIMESTAMP \n"
                + ",NU_COR_DOC=? \n"
                + ",NU_DOC_EMI=? \n"
                + ",ES_DOC_EMI=? \n"
                + "WHERE NU_ANN=? \n"
                + "AND NU_EMI=? ");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoEmiBean.getCoUseMod(), documentoEmiBean.getNuCorDoc(),
                documentoEmiBean.getNuDocEmi(), documentoEmiBean.getEsDocEmi(), documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi()});
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            //con.printStackTrace();
            vReturn = "Numero de Documento Duplicado.";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String updDocumentoEmiAdmBean(String nuAnn, String nuEmi, DocumentoEmiBean documentoEmiBean, ExpedienteBean expedienteBean, RemitenteEmiBean remitenteEmiBean, String pcoUserMod) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("update IDOSGD.tdtv_remitos \n"
                + "set CO_USE_MOD=?,");
        if (documentoEmiBean != null) {
            documentoEmiBean.setNuAnn(nuAnn);
            sqlUpd.append("DE_ASU=?\n"
                    + ",NU_DIA_ATE=?\n"
                    + ",CO_TIP_DOC_ADM=?\n"
                    + ",NU_DOC_EMI=?\n"
                    + ",DE_DOC_SIG=?\n"
                    + ",NU_COR_DOC= (CASE WHEN ? IS NOT NULL THEN ? ELSE ? END),\n"
                    /*(SELECT nvl(MAX(nu_cor_doc), 0) + 1\n"
                    + "FROM tdtv_remitos\n"
                    + "WHERE nu_ann = ?\n"
                    + "AND co_dep_emi = ?\n"
                    + "AND co_tip_doc_adm = ?\n"
                    + "AND ti_emi = ?)),\n"*/);
            
            //para verificar numero correlativo de documento
            if(documentoEmiBean.getNuDocEmi()==null || documentoEmiBean.getNuDocEmi().trim().equals("") ){
                String vnuCorDoc = getNroCorrelativoDocumento(documentoEmiBean);
                documentoEmiBean.setNuCorDoc(vnuCorDoc);
            }
            //sqlUpd.append(",A.NU_COR_DOC=").append(documentoEmiBean.getNuCorDoc()).append(",\n");
            
            //Para verificar si ya tiene un numero correlativo de emision
            if (documentoEmiBean.getNuCorEmi()==null || documentoEmiBean.getNuCorEmi().trim().equals("") ){
                String vnuCorEmi = getNroCorrelativoEmision(documentoEmiBean);
                documentoEmiBean.setNuCorEmi(vnuCorEmi);
                sqlUpd.append("NU_COR_EMI='" + documentoEmiBean.getNuCorEmi()+ "',\n");
            }
        }
        if (expedienteBean != null) {
            sqlUpd.append("NU_ANN_EXP='" + expedienteBean.getNuAnnExp() + "',\n");
            sqlUpd.append("NU_SEC_EXP='" + expedienteBean.getNuSecExp() + "',\n");
        }
        if (remitenteEmiBean != null) {
            sqlUpd.append("CO_LOC_EMI='" + remitenteEmiBean.getCoLocal() + "'\n"
                    + ",CO_DEP_EMI='" + remitenteEmiBean.getCoDependencia() + "'\n"
                    + ",CO_EMP_EMI='" + remitenteEmiBean.getCoEmpFirma() + "',\n");
        }
        sqlUpd.append("FE_USE_MOD=CURRENT_TIMESTAMP "
                + "where\n"
                + "NU_ANN=? and\n"
                + "NU_EMI=?");

        try {
            if (documentoEmiBean != null) {
                this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{pcoUserMod, documentoEmiBean.getDeAsu(), documentoEmiBean.getNuDiaAte(),
                    documentoEmiBean.getCoTipDocAdm(), documentoEmiBean.getNuDocEmi(), documentoEmiBean.getDeDocSig(),
                    documentoEmiBean.getNuDocEmi(), documentoEmiBean.getNuCorDoc(),documentoEmiBean.getNuCorDoc(),/*, nuAnn, documentoEmiBean.getCoDepEmi(), documentoEmiBean.getCoTipDocAdm(),
                    documentoEmiBean.getTiEmi(),*/ nuAnn, nuEmi});
            } else {
                this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{pcoUserMod, nuAnn, nuEmi});
            }
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            //con.printStackTrace();
            vReturn = "Numero de Documento Duplicado";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();

        }
        return vReturn;
    }

    @Override
    public String getNumeroDocSiguienteAdm(String pnuAnn, String pcoDepEmi, String pcoDoc) {
        String vReturn = "1";
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT LTRIM(RTRIM(CAST((ISNULL(MAX(nu_doc_emi), 0) + 1) AS VARCHAR))) "); 
            sql.append("FROM IDOSGD.tdtv_remitos tr ");
            sql.append("WHERE tr.nu_ann = ? ");
            sql.append("AND tr.co_dep_emi = ? ");
            sql.append("AND tr.co_tip_doc_adm = ? ");
            sql.append("AND tr.ti_emi = '01' ");
            sql.append("AND tr.nu_cor_doc = 1 ");
            sql.append("AND tr.es_doc_emi != '9' ");
            sql.append("AND ISNUMERIC(nu_doc_emi) = 1 ");
            vReturn = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pnuAnn, pcoDepEmi, pcoDoc});
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = "1";
            //vReturn=e.getMessage();
        }
        vReturn = Utility.getInstancia().rellenaCerosIzquierda(vReturn, 6);
        return vReturn;
    }

    @Override
    public DocumentoObjBean getPropiedadesArchivo(String pnuAnn, String pnuEmi, String ptiCap) {
        StringBuilder sql = new StringBuilder();
        sql.append("select "); 
        sql.append("nu_ann, "); 
        sql.append("nu_emi, "); 
        sql.append("de_ruta_origen nombre_Archivo, ");
        sql.append("UPPER(SUBSTRING(de_ruta_origen, ((LEN(de_ruta_origen) - CHARINDEX('.', REVERSE(de_ruta_origen)) + 1) + 1), LEN(de_ruta_origen))) tipo_doc ");
        sql.append("from IDOSGD.tdtv_archivo_doc "); 
        sql.append("where nu_ann = ? ");
        sql.append("and nu_emi = ? ");

        DocumentoObjBean docObjBean = new DocumentoObjBean();

        try {
            docObjBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoObjBean.class), new Object[]{pnuAnn, pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            docObjBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (docObjBean);
    }

    @Override
    public String getNroCorrelativoDocumento(DocumentoEmiBean documentoEmiBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT ISNULL(MAX(nu_cor_doc), 0) + 1 ");
        sqlQry.append("FROM IDOSGD.tdtv_remitos ");
        sqlQry.append("WHERE nu_ann       = ? ");
        sqlQry.append("AND ti_emi         = ? ");
        sqlQry.append("AND co_dep_emi     = ? ");
        sqlQry.append("AND co_tip_doc_adm = ? ");

        try {
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{documentoEmiBean.getNuAnn(), documentoEmiBean.getTiEmi(),
                documentoEmiBean.getCoDepEmi(), documentoEmiBean.getCoTipDocAdm()});
//            documentoEmiBean.setNuCorDoc(vReturn);
//            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();

        }
        return vReturn;
    }
    
    @Override
    public String getNroCorrelativoDocumentoDel(DocumentoEmiBean documentoEmiBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT ISNULL(MAX(nu_cor_doc), 1) + 1 ");
        sqlQry.append("FROM IDOSGD.tdtv_remitos ");
        sqlQry.append("WHERE nu_ann       = ? ");
        sqlQry.append("AND ti_emi         = ? ");
        sqlQry.append("AND co_dep_emi     = ? ");
        sqlQry.append("AND co_tip_doc_adm = ? ");

        try {
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{documentoEmiBean.getNuAnn(), documentoEmiBean.getTiEmi(),
                documentoEmiBean.getCoDepEmi(), documentoEmiBean.getCoTipDocAdm()});
//            documentoEmiBean.setNuCorDoc(vReturn);
//            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();

        }
        return vReturn;
    }    

    @Override
    public String delAllReferenciaDocumentoEmi(String nuAnn, String nuEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlDel = new StringBuilder();
        sqlDel.append("DELETE FROM IDOSGD.tdtr_referencia ");
        sqlDel.append("WHERE NU_ANN = ? "); 
        sqlDel.append("AND NU_EMI = ? ");

        try {
            this.jdbcTemplate.update(sqlDel.toString(), new Object[]{nuAnn, nuEmi});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String delAllDestinatarioDocumentoEmi(String nuAnn, String nuEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlDel = new StringBuilder();
        sqlDel.append("DELETE FROM IDOSGD.TDTV_DESTINOS ");
        sqlDel.append("WHERE NU_ANN = ? "); 
        sqlDel.append("AND NU_EMI = ? ");

        try {
            this.jdbcTemplate.update(sqlDel.toString(), new Object[]{nuAnn, nuEmi});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String delDocumentoEmiAdm(DocumentoEmiBean documentoEmiBean) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("update IDOSGD.tdtv_remitos "); 
        sqlUpd.append("set CO_USE_MOD=?, ");
        sqlUpd.append("ES_ELI='1', ");
        sqlUpd.append("NU_DOC_EMI=?, ");
        sqlUpd.append("NU_COR_DOC=?, ");
        sqlUpd.append("NU_ANN_EXP=?, ");
        sqlUpd.append("NU_SEC_EXP=?, ");
        sqlUpd.append("NU_DET_EXP=?, ");
        sqlUpd.append("FE_USE_MOD=CURRENT_TIMESTAMP ");
        sqlUpd.append("where NU_ANN=? "); 
        sqlUpd.append("and NU_EMI=? ");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoEmiBean.getCoUseMod(), documentoEmiBean.getNuDocEmi(),
                documentoEmiBean.getNuCorDoc(), documentoEmiBean.getNuAnnExp(), documentoEmiBean.getNuSecExp(), documentoEmiBean.getNuDetExp(),
                documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();

        }
        return vReturn;
    }

    @Override
    public List<DependenciaBean> getListDestinatarioEmi(String pcoDepen, String pdeDepEmite) {
        StringBuilder sql = new StringBuilder();

        boolean vfiltro = pdeDepEmite != null && !pdeDepEmite.trim().equals("") ? true : false;

        sql.append("SELECT DE_DEPENDENCIA, ");
        sql.append("       CO_DEPENDENCIA, ");
        sql.append("       DE_CORTA_DEPEN ");
        sql.append("  FROM IDOSGD.RHTM_DEPENDENCIA ");
        sql.append(" WHERE co_nivel <> '6' ");
        sql.append("   AND IN_BAJA = '0' ");
        if (vfiltro) {
            sql.append(" AND (DE_DEPENDENCIA LIKE '%' || ? || '%' OR DE_CORTA_DEPEN LIKE ? || '%' ) ");
        }
        sql.append("UNION ");  
        sql.append("SELECT DE_DEPENDENCIA, ");
        sql.append("       CO_DEPENDENCIA, ");
        sql.append("       DE_CORTA_DEPEN ");
        sql.append("  FROM IDOSGD.RHTM_DEPENDENCIA ");
        sql.append(" WHERE co_nivel = '6' ");
        sql.append("   AND IN_BAJA = '0' ");
        sql.append("   AND (CO_DEPEN_PADRE in ( select CO_DEPEN_PADRE co_dep "); 
        sql.append("							from IDOSGD.RHTM_DEPENDENCIA where CO_DEPENDENCIA = ?) ");
        sql.append("							OR ? IN (CO_DEPENDENCIA, CO_DEPEN_PADRE)) ");
        if (vfiltro) {
            sql.append(" AND (DE_DEPENDENCIA LIKE '%' || ? || '%' OR DE_CORTA_DEPEN LIKE ? || '%') ");
        }
        sql.append("UNION ");
        sql.append("SELECT ' [TODOS]', ");
        sql.append(" 	   NULL, ");
        sql.append(" 	   NULL ");
        if (vfiltro) {
            sql.append(" where ' [TODOS]' like '%'||?||'%' ");
        }
        sql.append("  ORDER BY 1");


        List<DependenciaBean> list = new ArrayList<DependenciaBean>();

        try {
            if (vfiltro) {
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class), new Object[]{pdeDepEmite, pdeDepEmite, pcoDepen, pcoDepen,
                    pdeDepEmite, pdeDepEmite, pdeDepEmite});
            } else {
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class), new Object[]{pcoDepen, pcoDepen});
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
    public List<DestinoBean> getListaDestinosCodDepTipoDes(String nu_ann, String nu_emi) {
        StringBuilder sql = new StringBuilder();
        List<DestinoBean> list = new ArrayList<DestinoBean>();
        sql.append("SELECT DISTINCT co_dep_des, ");
        sql.append("ti_des ");
        sql.append("FROM IDOSGD.tdtv_destinos ");
        sql.append("WHERE es_eli = '0' ");
        sql.append("AND nu_ann = ? ");
        sql.append("AND nu_emi = ? ");
        sql.append("ORDER BY ti_des");
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinoBean.class),
                    new Object[]{nu_ann, nu_emi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DestinoBean> getListaDestinosOriTipoDes(String nu_ann, String nu_emi) {
        StringBuilder sql = new StringBuilder();
        List<DestinoBean> list = new ArrayList<DestinoBean>();
        sql.append("SELECT ti_des, ");
        sql.append("CASE ti_des ");
        sql.append("	WHEN '02' THEN [IDOSGD].[PK_SGD_DESCRIPCION_DE_PROVEEDOR](nu_ruc_des) ");
        sql.append("	WHEN '03' THEN [IDOSGD].[PK_SGD_DESCRIPCION_ANI_SIMIL](nu_dni_des) ");
        sql.append("	WHEN '04' THEN [IDOSGD].[PK_SGD_DESCRIPCION_OTRO_ORIGEN](co_otr_ori_des) ");
        sql.append("	ELSE ' ' ");
        sql.append("END co_dep_des ");
        sql.append("FROM IDOSGD.tdtv_destinos "); 
        sql.append("where nu_ann = ? ");
        sql.append("and nu_emi = ? ");
        sql.append("and es_eli = '0' "); 
        sql.append("ORDER BY ti_des ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinoBean.class),
                    new Object[]{nu_ann, nu_emi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<ReferenciaRemitoBean> getListaReferenciaRemitos(String nu_ann, String nu_emi) {
        StringBuilder sql = new StringBuilder();
        List<ReferenciaRemitoBean> list = new ArrayList<ReferenciaRemitoBean>();
        sql.append("SELECT DISTINCT b.ti_emi, b.co_dep_emi ");
        sql.append("FROM   IDOSGD.tdtr_referencia a, IDOSGD.tdtv_remitos b ");
        sql.append("WHERE  a.nu_ann_ref = b.nu_ann ");
        sql.append("AND    a.nu_emi_ref = b.nu_emi ");
        sql.append("AND    a.nu_ann = ? ");
        sql.append("AND    a.nu_emi = ? ");
        sql.append("ORDER  BY b.ti_emi");
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ReferenciaRemitoBean.class),
                    new Object[]{nu_ann, nu_emi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<ReferenciaRemitoBean> getOriReferenciaLista(String nu_ann, String nu_emi) {
        StringBuilder sql = new StringBuilder();
        List<ReferenciaRemitoBean> list = new ArrayList<ReferenciaRemitoBean>();
        sql.append("SELECT b.ti_emi, ");
        sql.append("CASE b.ti_emi "); 
        sql.append("	WHEN '02' THEN [IDOSGD].[PK_SGD_DESCRIPCION_DE_PROVEEDOR](b.nu_ruc_emi) "); 
        sql.append("	WHEN '03' THEN [IDOSGD].[PK_SGD_DESCRIPCION_ANI_SIMIL](b.nu_dni_emi) "); 
        sql.append("	WHEN '04' THEN [IDOSGD].[PK_SGD_DESCRIPCION_OTRO_ORIGEN](b.co_otr_ori_emi) "); 
        sql.append("	ELSE ' ' "); 
        sql.append("END co_dep_emi ");
        sql.append("FROM   IDOSGD.tdtr_referencia a, "); 
        sql.append("	   IDOSGD.tdtv_remitos b "); 
        sql.append("WHERE  a.nu_ann_ref = b.nu_ann "); 
        sql.append("AND    a.nu_emi_ref = b.nu_emi "); 
        sql.append("AND    a.nu_ann = ? "); 
        sql.append("AND    a.nu_emi = ? "); 
        sql.append("ORDER  BY b.ti_emi ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ReferenciaRemitoBean.class),
                    new Object[]{nu_ann, nu_emi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DestinoBean> getListaDestinosCodPri(String nu_ann, String nu_emi) {
        StringBuilder sql = new StringBuilder();
        List<DestinoBean> list = new ArrayList<DestinoBean>();
        sql.append("SELECT co_pri ");
        sql.append("FROM IDOSGD.tdtv_destinos ");
        sql.append("WHERE es_eli = '0' ");
        sql.append("AND nu_ann = ? ");
        sql.append("AND nu_emi = ? ");
        sql.append("ORDER BY co_pri desc");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinoBean.class),
                    new Object[]{nu_ann, nu_emi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String getNumDestinos(String nu_ann, String nu_emi) {
        String vReturn = "0";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT count(nu_des) ");
        sqlQry.append("FROM IDOSGD.tdtv_destinos ");
        sqlQry.append("WHERE nu_ann = ? ");
        sqlQry.append("AND nu_emi = ? ");
        try {
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{nu_ann, nu_emi});
        } catch (Exception e) {
            vReturn = "0";
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public String updRemitoResumenDestinatario(String pnuAnn, String pnuEmi, String vti_des, String vco_pri, String vnu_cant_des, String vresOriDes) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("UPDATE IDOSGD.TDTX_REMITOS_RESUMEN SET ");
        sqlUpd.append("TI_EMI_DES = ?,CO_PRIORIDAD = ? ,nu_candes=? ");
        sqlUpd.append("WHERE NU_ANN = ? ");
        sqlUpd.append("AND NU_EMI = ? ");

        StringBuilder sqlUpd2 = new StringBuilder();
        sqlUpd2.append("UPDATE IDOSGD.TDTV_REMITOS SET DE_ORI_DES = ?\n" +
                        "WHERE NU_ANN = ? \n" +
                        "AND NU_EMI = ? ");        
        
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{
                vti_des, vco_pri, vnu_cant_des, pnuAnn, pnuEmi
            });
            
            this.jdbcTemplate.update(sqlUpd2.toString(), new Object[]{
                vresOriDes, pnuAnn, pnuEmi
            });
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;

    }

    public String updRemitoResumenReferencia(String pnuAnn, String pnuEmi, String vti_ori, String vdeOriEmi) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("UPDATE IDOSGD.TDTX_REMITOS_RESUMEN SET ");
        sqlUpd.append("TI_EMI_REF = ? ");
        sqlUpd.append("WHERE NU_ANN = ? ");
        sqlUpd.append("AND NU_EMI = ? ");

        StringBuilder sqlUpd2 = new StringBuilder();
        sqlUpd2.append("UPDATE IDOSGD.TDTV_REMITOS SET DE_ORI_EMI = ? ");
        sqlUpd2.append("WHERE NU_ANN = ? ");
        sqlUpd2.append("AND NU_EMI = ? ");
        
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{
                vti_ori, pnuAnn, pnuEmi
            });
            
            // Actualizacion de de_ori_emi
            this.jdbcTemplate.update(sqlUpd2.toString(), new Object[]{
                vdeOriEmi, pnuAnn, pnuEmi
            });
            
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;
    }
    @Override
    public DocumentoEmiBean existeDocumentoReferenciado(BuscarDocumentoEmiBean buscarDocumentoEmiBean){
        StringBuilder sql = new StringBuilder();
        DocumentoEmiBean documentoEmiBean = null;
        sql.append("SELECT A.NU_ANN, "); 
        sql.append("A.NU_EMI, ");
        sql.append("A.NU_COR_DOC "); 
        sql.append("FROM IDOSGD.TDTV_REMITOS A ");
        sql.append("WHERE A.NU_ANN=? ");
        sql.append("AND A.CO_DEP_EMI=? ");
        sql.append("AND A.TI_EMI='01' ");
        sql.append("AND A.CO_TIP_DOC_ADM=? ");
        sql.append("AND A.NU_DOC_EMI=? ");
        sql.append("AND A.ES_ELI='0' ");
        sql.append("AND A.ES_DOC_EMI NOT IN ('5','7','9') ");        
        
        try {
             documentoEmiBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoEmiBean.class),
                    new Object[]{buscarDocumentoEmiBean.getsCoAnnioBus(),buscarDocumentoEmiBean.getsBuscDestinatario(),buscarDocumentoEmiBean.getsDeTipoDocAdm(),
                    buscarDocumentoEmiBean.getsNumDocRef()});
             documentoEmiBean.setMsjResult("OK");
        } catch (EmptyResultDataAccessException e) {
            documentoEmiBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoEmiBean;          
    }
    
    @Override
    public List<DocumentoEmiBean> getDocumentosReferenciadoBusq(DocumentoEmiBean documentoEmiBean,String sTipoAcceso, String pnuPagina, int pnuRegistros){       
        StringBuilder sql = new StringBuilder(); 
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List<DocumentoEmiBean> list = new ArrayList<DocumentoEmiBean>();        
        
        sql.append("SELECT A.* ");
        sql.append("FROM ( ");         
        sql.append("	SELECT  DISTINCT B.NU_COR_EMI, ");        
        sql.append("            (SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_DE_EMI_REF](B.NU_ANN, B.NU_EMI)) DE_EMI_REF, "); 
        sql.append("		B.FE_EMI, ");
        sql.append("		CONVERT(VARCHAR(10), B.FE_EMI, 3) FE_EMI_CORTA, ");
        sql.append("		(SELECT CDOC_DESDOC "); 
        sql.append("		FROM IDOSGD.SI_MAE_TIPO_DOC "); 
        sql.append("		WHERE CDOC_TIPDOC = B.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append("		CASE B.NU_CANDES ");
        sql.append("                WHEN 1 THEN (SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_TI_DES_EMP](B.NU_ANN, B.NU_EMI)) ");
        sql.append("                ELSE (SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_TI_DES_EMP_V](B.NU_ANN, B.NU_EMI)) ");
        sql.append("		END DE_EMP_PRO, ");			
        sql.append("		(SELECT CEMP_APEPAT + ' ' + CEMP_APEMAT + ' ' + CEMP_DENOM "); 
        sql.append("		FROM IDOSGD.RHTM_PER_EMPLEADOS "); 
        sql.append("		WHERE CEMP_CODEMP = B.CO_EMP_RES) DE_EMP_RES, ");
        sql.append("            (SELECT DE_EST "); 
        sql.append("		FROM IDOSGD.TDTR_ESTADOS "); 
        sql.append("		WHERE (CO_EST + DE_TAB) = (B.ES_DOC_EMI + 'TDTV_REMITOS')) DE_ES_DOC_EMI, ");
        sql.append("		C.NU_DOC, ");
        sql.append("		UPPER(B.DE_ASU) DE_ASU_M, ");
        sql.append("		B.NU_ANN, ");
        sql.append("		C.NU_EXPEDIENTE, ");
        sql.append("		B.NU_EMI, ");
        sql.append("		B.TI_CAP, ");
        sql.append("		C.IN_EXISTE_DOC EXISTE_DOC, ");
        sql.append("		CASE ISNULL(C.TI_EMI_REF,'0') + ISNULL(C.IN_EXISTE_ANEXO,'2') ");
        sql.append("                WHEN '00' THEN 0 ");
        sql.append("                WHEN '02' THEN 0 ");
        sql.append("                ELSE 1 ");
        sql.append("		END EXISTE_ANEXO, "); 
        sql.append("		ISNULL(C.CO_PRIORIDAD,'1') CO_PRIORIDAD, ");
        sql.append("		B.ES_DOC_EMI, ");
        sql.append("		ROW_NUMBER() OVER (ORDER BY B.NU_COR_EMI) AS ROWNUM ");
        sql.append("	FROM ( "); 
        sql.append("		 SELECT NU_ANN, NU_EMI FROM [IDOSGD].[PK_SGD_DESCRIPCION_ARBOL_SEG](:pCoAnio, :pNuEmi) ");
        sql.append("	) A, "); 
        sql.append("	IDOSGD.TDTV_REMITOS B, "); 
        sql.append("	IDOSGD.TDTX_REMITOS_RESUMEN C ");
        sql.append("	WHERE A.NU_ANN = B.NU_ANN ");
        sql.append("	AND A.NU_EMI = B.NU_EMI ");
        sql.append("	AND C.NU_ANN = B.NU_ANN ");
        sql.append("	AND C.NU_EMI = B.NU_EMI ");
        sql.append("	AND B.TI_EMI='01' ");
        sql.append("	AND C.TI_EMI='01' ");
        sql.append("	AND B.ES_ELI = '0' ");
        sql.append("	AND B.CO_DEP_EMI = :pCoDepEmi ");        
        if (sTipoAcceso.equals("1")) {
            sql.append(" AND B.CO_EMP_RES = :pcoEmpRes ");      
            objectParam.put("pcoEmpRes", documentoEmiBean.getCoEmpRes());           
        }
        objectParam.put("pCoAnio", documentoEmiBean.getNuAnn());           
        objectParam.put("pNuEmi", documentoEmiBean.getNuEmi());   
        objectParam.put("pCoDepEmi", documentoEmiBean.getCoDepEmi());        
        sql.append(") A "); 
        sql.append("WHERE ROWNUM < 101 ");
        sql.append("ORDER BY A.NU_COR_EMI DESC ");
        
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoEmiBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;          
    }
    
    @Override
    public TblRemitosBean getDatosDocumento(String pnuAnn,String pnuEmi){
        StringBuilder sql = new StringBuilder();
        TblRemitosBean tblRemitosBean = null;
        sql.append("SELECT A.NU_ANN, ");
        sql.append("A.NU_EMI, ");
        sql.append("A.CO_DEP_EMI, ");
        sql.append("A.FE_EMI "); 
        sql.append("FROM IDOSGD.TDTV_REMITOS A ");
        sql.append("WHERE A.NU_ANN=? "); 
        sql.append("AND A.NU_EMI=? "); 
        sql.append("AND A.ES_ELI='0' ");      
        
        try {
             tblRemitosBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(TblRemitosBean.class),
                    new Object[]{pnuAnn, pnuEmi});
             tblRemitosBean.setMsgResult("OK");
        } catch (EmptyResultDataAccessException e) {
            tblRemitosBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tblRemitosBean;              
    }
    
    @Override
    public String getCoEmplFirmoDocumento(String pnuAnn,String pnuEmi){
        StringBuilder sql = new StringBuilder();
        String result = "NO_OK";
        sql.append("SELECT CO_EMP_EMI ");
        sql.append("FROM IDOSGD.TDTV_REMITOS ");
        sql.append("WHERE nu_emi = ? ");
        sql.append("AND nu_ann = ? ");

        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pnuEmi, pnuAnn});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;        
    }
    
    @Override
    public DocumentoEmiBean getEstadoDocumento(String pnuAnn,String pnuEmi){
        StringBuilder sql = new StringBuilder();
        DocumentoEmiBean documentoEmiBean = null;
        sql.append("SELECT CO_EMP_EMI, ");
        sql.append("	   ES_DOC_EMI, ");
        sql.append("	   NU_ANN,NU_EMI, ");
        sql.append("	   TI_EMI, ");
        sql.append("	   CO_DEP_EMI, ");
        sql.append("	   CO_TIP_DOC_ADM, ");
        sql.append("	   NU_DOC_EMI, ");
        sql.append("	   CO_EMP_RES ");
        sql.append("FROM IDOSGD.TDTV_REMITOS ");
        sql.append("WHERE nu_ann = ? ");
        sql.append("AND nu_emi = ? "); 
        sql.append("AND ES_ELI='0' ");        
        
        try {
             documentoEmiBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoEmiBean.class),
                    new Object[]{pnuAnn, pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            documentoEmiBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoEmiBean;             
    }
    
    
    @Override
    public String getNroCorrelativoEmision(DocumentoEmiBean documentoEmiBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "0";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT ISNULL(MAX(nu_cor_emi), 0) + 1 ");
        sqlQry.append("FROM IDOSGD.tdtv_remitos ");
        sqlQry.append("WHERE nu_ann = ? ");
        sqlQry.append("AND co_dep_emi = ? ");
        sqlQry.append("AND ti_emi = ? ");

        try {
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{documentoEmiBean.getNuAnn(), documentoEmiBean.getCoDepEmi(), documentoEmiBean.getTiEmi()});
//            documentoEmiBean.setNuCorDoc(vReturn);
//            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = "0";
        }
        return vReturn;
    }
    
    @Override
    public String updChangeToDespacho(DocumentoEmiBean documentoEmiBean) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("update IDOSGD.tdtv_remitos \n"
                + "set CO_USE_MOD=?\n"
                + ",ES_DOC_EMI=?\n"
                + ",FE_USE_MOD=CURRENT_TIMESTAMP\n"
                + ",FE_EMI=CURRENT_TIMESTAMP\n"
                + "where\n"
                + "NU_ANN=? and\n"
                + "NU_EMI=?");
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoEmiBean.getCoUseMod(), documentoEmiBean.getEsDocEmi(),
                documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();

        }
        return vReturn;
    }
    
        @Override
    public String updEstadoDocumentoEnvioNotificacion(DocumentoEmiBean documentoEmiBean) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("update tdtv_destinos \n"
                + "set CO_USE_MOD=?\n"
                + ",ES_DOC_REC=?\n"
                + ",FE_USE_MOD=SYSDATE\n"
                + ",FE_EMI=SYSDATE\n"
                + "where\n"
                + "NU_ANN=? and\n"
                + "NU_EMI=?");
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoEmiBean.getCoUseMod(), documentoEmiBean.getEsDocEmi(),
                documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();

        }
        return vReturn;
    }
    
    @Override
    public List<DocumentoBean> getLstDocRecepcionadoRefMp(String pcoDepen, String pannio, String ptiDoc, String pnuDoc) {
        StringBuilder sql = new StringBuilder();
        List<DocumentoBean> list = new ArrayList<DocumentoBean>();
        
        sql.append("SELECT A.* ");
        sql.append("FROM ( "); 

        sql.append("	select E.*, ROW_NUMBER() OVER (ORDER BY FE_REC_DOC) AS ROWNUM ");
        sql.append("	from ( ");
        sql.append("		select ");
        sql.append("		d.FE_REC_DOC, ");
        sql.append("		SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](r.co_tip_doc_adm),1,100) de_tip_doc_adm, ");
        sql.append("		SUBSTRING(rr.nu_doc,1,50) NU_DOC, ");
        sql.append("		CONVERT(VARCHAR(10), r.FE_EMI, 3) FE_EMI_CORTA, ");
        sql.append("		CONVERT(VARCHAR(10), d.FE_REC_DOC, 3) FE_REC_DOC_CORTA, ");
        sql.append("		r.nu_ann, ");
        sql.append("		r.nu_emi, ");
        sql.append("		d.nu_des, ");
        sql.append("		REPLACE(LTRIM(RTRIM(r.DE_ASU)), CHAR(10), ' ') DE_ASU, ");
        sql.append("		r.co_tip_doc_adm, ");
        sql.append("		SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](d.co_dep_des),1,200) de_dep_des, ");
        sql.append("		SUBSTRING(rr.nu_expediente,1,20) NU_EXPEDIENTE, ");
        sql.append("		r.NU_ANN_EXP, ");
        sql.append("		r.NU_SEC_EXP ");
        sql.append("		from IDOSGD.TDTV_REMITOS R, ");
        sql.append("		IDOSGD.TDTV_DESTINOS D, ");
        sql.append("		IDOSGD.TDTX_REMITOS_RESUMEN RR ");
        sql.append("		where ");
        sql.append("		r.es_eli = '0' ");
        sql.append("		AND d.es_eli = '0' ");
        sql.append("		AND r.nu_ann = ? ");
        sql.append("		and rr.nu_ann=r.nu_ann ");
        sql.append("		AND rr.nu_emi = r.nu_emi ");
        sql.append("		AND d.nu_ann = r.nu_ann ");
        sql.append("		AND d.nu_emi = r.nu_emi ");
        sql.append("		AND r.es_doc_emi NOT IN ('5', '9', '7') ");
        sql.append("		and d.es_doc_rec <> '0' ");  
        sql.append("		and d.co_dep_des in "); 
        sql.append("		(select ? ");  
        sql.append("		UNION "); 
        sql.append("		  SELECT co_dep_ref ");
        sql.append("			FROM IDOSGD.tdtx_referencia ");
        sql.append("		   WHERE co_dep_emi = ? ");
        sql.append("			 AND ti_emi = 'D' ");
        sql.append("			 AND es_ref = '1') ");
        sql.append("		AND r.co_tip_doc_adm = ? ");
        if (pnuDoc != null && pnuDoc.trim().length() > 0){
            pnuDoc=Utility.getInstancia().rellenaCerosIzquierda(pnuDoc, 6);
            sql.append("AND (R.NU_DOC_EMI = '").append(pnuDoc).append("' OR RR.NU_EXPEDIENTE LIKE '%").append(pnuDoc).append("')\n");
        }
        sql.append("		UNION "); 
        sql.append("		SELECT ");
        sql.append("		DE.FE_REC_DOC, ");
        sql.append("		SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](r.co_tip_doc_adm),1,100) de_tip_doc_adm, ");
        sql.append("		SUBSTRING(rr.nu_doc,1,50) NU_DOC, ");
        sql.append("		CONVERT(VARCHAR(10), r.FE_EMI, 3) FE_EMI_CORTA, ");
        sql.append("		CONVERT(VARCHAR(10), DE.FE_REC_DOC, 3) FE_REC_DOC_CORTA, ");
        sql.append("		r.nu_ann, ");
        sql.append("		r.nu_emi, ");
        sql.append("		DE.nu_des, ");
        sql.append("		REPLACE(LTRIM(RTRIM(r.DE_ASU)), CHAR(10), ' ') DE_ASU, ");
        sql.append("		r.co_tip_doc_adm, ");
        sql.append("		SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](DE.co_dep_des),1,200) de_dep_des, ");
        sql.append("		SUBSTRING(rr.nu_expediente, 1, 20) NU_EXPEDIENTE, ");
        sql.append("		r.NU_ANN_EXP, ");
        sql.append("		r.NU_SEC_EXP ");
        sql.append("		FROM IDOSGD.TDTV_DESTINOS DE, ");
        sql.append("		IDOSGD.TDTV_REMITOS R, ");
        sql.append("		IDOSGD.TDTX_REMITOS_RESUMEN RR ");
        sql.append("		WHERE R.NU_ANN = ? ");
        sql.append("		AND DE.NU_ANN=R.NU_ANN ");
        sql.append("		AND RR.NU_ANN=R.NU_ANN ");
        sql.append("		AND DE.NU_EMI=RR.NU_EMI ");
        sql.append("		AND R.NU_EMI=RR.NU_EMI ");
        sql.append("		AND R.es_eli = '0' ");
        sql.append("		AND DE.ES_ELI='0' ");
        sql.append("		and R.es_doc_emi not in ('9','7','5') ");
        sql.append("		AND R.CO_GRU = '1' ");
        sql.append("		AND DE.ES_ENV_POR_TRA='1' ");
        sql.append("		AND r.co_tip_doc_adm = ? ");
        if (pnuDoc != null && pnuDoc.trim().length() > 0) {
            pnuDoc=Utility.getInstancia().rellenaCerosIzquierda(pnuDoc, 6);
            sql.append("AND (R.NU_DOC_EMI = '").append(pnuDoc).append("' OR RR.NU_EXPEDIENTE LIKE '%").append(pnuDoc).append("')\n");
        }
        sql.append("	) E ");

        sql.append(") A ");
        sql.append("WHERE ROWNUM < 201 ");
        sql.append("order by 1 desc "); 

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class),
                    new Object[]{pannio,pcoDepen, pcoDepen, ptiDoc, pannio, ptiDoc});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }    
    
    @Override
    public DocumentoEmiBean getEstadoDocumentoAudi(String pnuAnn,String pnuEmi){
        StringBuilder sql = new StringBuilder();
        DocumentoEmiBean documentoEmiBean = null;
        sql.append("SELECT CO_EMP_EMI, ");
        sql.append("ES_DOC_EMI,NU_ANN, ");
        sql.append("NU_EMI, ");
        sql.append("TI_EMI, ");
        sql.append("CO_DEP_EMI, ");
        sql.append("CO_TIP_DOC_ADM, ");
        sql.append("NU_DOC_EMI ");
        sql.append("FROM IDOSGD.TDTV_REMITOS ");
        sql.append("WHERE nu_ann = ? ");
        sql.append("AND nu_emi = ? ");      
        
        try {
             documentoEmiBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoEmiBean.class),
                    new Object[]{pnuAnn,pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            documentoEmiBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoEmiBean;             
    }    
    
    @Override
    public String insPersonalVoBo(String nuAnn,String nuEmi,String coDep,String coEmp,String coUser){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("INSERT INTO IDOSGD.TDTV_PERSONAL_VB(NU_ANN,NU_EMI,CO_DEP,\n" +
                    "CO_EMP_VB,IN_VB,CO_USE_CRE,\n" +
                    "FE_USE_CRE,CO_USE_MOD,FE_USE_MOD)\n" +
                    "VALUES(?,?,?,\n" +
                    "?,'0',?,\n" +
                    "CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP)");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{nuAnn,nuEmi,coDep,coEmp,coUser,coUser});
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Datos Duplicado INSERT IDOSGD.TDTV_PERSONAL_VB.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }
    
    @Override
    public String delPersonalVoBo(String nuAnn,String nuEmi,String coDep,String coEmp){
        String vReturn = "NO_OK";
        StringBuilder sqlDel = new StringBuilder();
        sqlDel.append("DELETE FROM IDOSGD.TDTV_PERSONAL_VB\n" +
                        "WHERE\n" +
                        "NU_ANN=?\n" +
                        "AND NU_EMI=?\n" +
                        "AND CO_DEP=?\n" +
                        "AND CO_EMP_VB=?\n" +
                        "AND IN_VB <> '1'");

        try {
            this.jdbcTemplate.update(sqlDel.toString(), new Object[]{nuAnn,nuEmi,coDep,coEmp});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }    

    @Override
    public String getInNumeraDocAdm(String tipoDoc) {
        String vReturn = "0";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT IN_NUMERACION FROM IDOSGD.SI_MAE_TIPO_DOC\n" +
                    "WHERE CDOC_INDBAJ='0'\n" +
                    "AND CDOC_TIPDOC=?");
        try {
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{tipoDoc});
            if(vReturn==null){
                vReturn="0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public String getInFirmaDocAdm(String tipoDoc) {
        String vReturn = "2";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT IN_TIPO_FIRMA FROM IDOSGD.SI_MAE_TIPO_DOC\n" +
                    "WHERE CDOC_INDBAJ='0'\n" +
                    "AND CDOC_TIPDOC=?");
        try {
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{tipoDoc});
            if(vReturn==null){
                vReturn="2";
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }
    
    @Override
    public List<DestinatarioDocumentoEmiBean> getLstPersVoBoGrupo(String pcoGrupo) {
        StringBuilder sql = new StringBuilder();
        List<DestinatarioDocumentoEmiBean> list = new ArrayList<DestinatarioDocumentoEmiBean>();

        sql.append("SELECT a.co_dep co_dependencia, ");
        sql.append("b.DE_CORTA_DEPEN de_dependencia, ");
        sql.append("ISNULL(a.co_emp, b.co_empleado) co_empleado, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](ISNULL(a.co_emp, b.co_empleado)) de_empleado ");
        sql.append("FROM ( ");   
        sql.append("  select y.cemp_codemp co_emp, "); 
        sql.append("		 x.co_dep ");
        sql.append("  from IDOSGD.tdtd_dep_gru x ");
        sql.append("	   RIGHT OUTER JOIN IDOSGD.rhtm_per_empleados y ");
        sql.append("	   ON y.cemp_codemp = x.co_emp AND y.cemp_est_emp = '1' ");
        sql.append("  where x.co_gru_des= ? ");
        sql.append("  and x.es_dep_gru = '1' ");
        sql.append(") a, "); 
        sql.append("IDOSGD.rhtm_dependencia b, "); 
        sql.append("IDOSGD.sitm_local_dependencia c ");
        sql.append("WHERE a.co_dep = b.co_dependencia ");
        sql.append("AND a.co_dep = c.co_dep "); 
        sql.append("order by 1 ");

        try {
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioDocumentoEmiBean.class),
                        new Object[]{pcoGrupo});                
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }    

    @Override
    public String updArchivarDocumento(DocumentoEmiBean documentoEmiBean, Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getCantidadAnexo(DocumentoEmiBean documentoEmiBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DetalleEnvioDeCorreoBean getDetalleEnvioCorreo(String pnuAnn, String pnuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updClaveDocumentoEmi(String nuAnn, String nuEmi, String referenciaEmiDocBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String insDocumentoEmiBeanProyecto(DocumentoEmiBean documentoEmiBean, ExpedienteBean expedienteBean, RemitenteEmiBean remitenteEmiBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String insDestinatarioDocumentoEmiProyecto(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DestinatarioDocumentoEmiBean> getLstDestintariotlbEmiProyecto(String pnuAnn, String pnuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updDestinatarioDocumentoEmiProyecto(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String delDestinatarioDocumentoEmiProyecto(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DocumentoProyectoBean getDocumentoEmiAdmProyecto(String pnuAnn, String pnuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTipoDestinatarioEmiProyecto(String pnuAnn, String pnuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DocumentoEmiBean getDocumentoEmiAdmProyectoAEmiBean(String pnuAnn, String pnuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DocumentoEmiBean> getDocumentosBuscaEmiAdmFiltroReg(BuscarDocumentoEmiBean buscarDocumentoEmi, String pnuPagina, int pnuRegistros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String insMesaVitual(DatosInterBean datosInter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DocumentoEmiBean> getLstTipoDestEmi(String pnuAnn, String pnuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DocumentoEmiBean> getDocumentosBuscaEmiAdmFiltroRegSize(BuscarDocumentoEmiBean buscarDocumentoEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DocumentoEmiBean> getDocumentosReferenciadoBusqSize(DocumentoEmiBean documentoEmiBean, String sTipoAcceso) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String insLogDocumentoEmiAdm(AudiEstadosMovDocBean usuario, String pnuCorEmi, String pcoDepEmi) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updReferenciaDocumentoEmiAntesEliminar(String nuAnn, String nuEmi, ReferenciaEmiDocBean referenciaEmiDocBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updReferenciaDocumentoEmiAntesEliminarTodo(String nuAnn, String nuEmi, String usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updDestinoDocumentoEmiAntesEliminarTodo(String nuAnn, String nuEmi, String usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
//    public String insExtExpedienteBean(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean, ExpedienteBean expedienteBean) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    @Override
//    public DocumentoEmiBean getExtensionExpediente(String pnuAnn, String pnuEmi, String pnuDes) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    @Override
//    public String delExtExpedienteBean(String string, String string1, DestinatarioDocumentoEmiBean ddeb) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    @Override
//    public String updExtExpedienteBean(String string, String string1, DestinatarioDocumentoEmiBean ddeb) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    @Override
//    public List<ReferenciaBean> getDocSegParaExtExp(String string, String string1, String string2, String string3) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
}