/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.sqlserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoEmiConsulBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioDocumentoEmiConsulBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiConsulBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaConsulBean;
import pe.gob.onpe.tramitedoc.dao.ConsultaEmiDocDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;

/**
 *
 * @author ECueva
 */
@Repository("consultaEmiDocDao")
public class ConsultaEmiDocDaoImp extends SimpleJdbcDaoBase implements ConsultaEmiDocDao{

    @Override
    public List<DocumentoEmiConsulBean> getDocumentosBuscaEmiAdm(BuscarDocumentoEmiConsulBean buscarDocumentoEmiConsulBean) {
        StringBuilder sql = new StringBuilder();
        boolean bBusqFiltro = false;
        String sqlContains = "";
        Map<String, Object> objectParam = new HashMap<String, Object>();

        List<DocumentoEmiConsulBean> list = new ArrayList<DocumentoEmiConsulBean>();
        sql.append("SELECT  Z.* FROM ( ");
        sql.append("	SELECT	X.*, ");
        sql.append("		(SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_DE_EMI_REF(X.NU_ANN, X.NU_EMI)) DE_EMI_REF, ");
        sql.append("		IDOSGD.PK_SGD_DESCRIPCION_DE_DOCUMENTO(X.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append("		(CASE WHEN X.NU_CANDES = 1 THEN (SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_TI_DES_EMP(X.NU_ANN, X.NU_EMI)) ");
        sql.append("                  ELSE (SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_TI_DES_EMP_V(X.NU_ANN, X.NU_EMI)) ");
        sql.append("		END) DE_EMP_PRO, ");
        sql.append("		IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(X.CO_EMP_RES) DE_EMP_RES, ");
        sql.append("		IDOSGD.PK_SGD_DESCRIPCION_ESTADOS (X.ES_DOC_EMI,'TDTV_REMITOS') DE_ES_DOC_EMI, ");
        sql.append("		ROW_NUMBER() OVER (ORDER BY X.NU_COR_EMI DESC) AS ROWNUM ");
        sql.append("	FROM ( ");
        sql.append("		SELECT  A.NU_COR_EMI, ");
        sql.append("			CONVERT(VARCHAR(10), A.FE_EMI, 103) FE_EMI_CORTA,A.FE_EMI, ");
        sql.append("			B.NU_DOC,UPPER(A.DE_ASU) DE_ASU_M,A.NU_ANN,B.NU_EXPEDIENTE,A.NU_EMI,A.TI_CAP,B.IN_EXISTE_DOC EXISTE_DOC, ");
        sql.append("			(CASE WHEN ISNULL(B.TI_EMI_REF,'0') + ISNULL(B.IN_EXISTE_ANEXO,'2') = '00' OR ISNULL(B.TI_EMI_REF,'0') + ISNULL(B.IN_EXISTE_ANEXO,'2') = '02' THEN 0 ");
        sql.append("				  ELSE 1 ");
        sql.append("			END) CO_PRIORIDAD, ");
        sql.append("			A.ES_DOC_EMI, ");
        sql.append("			A.NU_DIA_ATE, ");
        sql.append("			A.CO_TIP_DOC_ADM, ");
        sql.append("			A.NU_CANDES, ");
        sql.append("			A.CO_EMP_RES ");
        sql.append("		FROM IDOSGD.TDTV_REMITOS A, ");
        sql.append("                 IDOSGD.TDTX_REMITOS_RESUMEN B ");
        sql.append("		WHERE B.NU_ANN = A.NU_ANN ");
        sql.append("		AND B.NU_EMI = A.NU_EMI ");
        
        String pNUAnn = buscarDocumentoEmiConsulBean.getCoAnnio();
        if (!(buscarDocumentoEmiConsulBean.getEsFiltroFecha().equals("1") && pNUAnn.equals("0"))) {
            sql.append(" AND A.NU_ANN = :pNuAnn");
            // Parametros Basicos
            objectParam.put("pNuAnn", pNUAnn);
        }
        sql.append(" AND A.TI_EMI='01'");
        sql.append(" AND B.TI_EMI='01'");
        sql.append(" AND A.CO_GRU = '1'");
        sql.append(" AND A.ES_ELI = '0'");
        //sql.append(" AND A.CO_DEP_EMI = :pCoDepEmi");

        // Parametros Basicos
        //objectParam.put("pCoDepEmi", buscarDocumentoEmiConsulBean.getCoDependencia());

        String pTipoBusqueda = buscarDocumentoEmiConsulBean.getTipoBusqueda();
        if (pTipoBusqueda.equals("1") && buscarDocumentoEmiConsulBean.isEsIncluyeFiltro()) {
            bBusqFiltro = true;
        }
        String auxTipoAcceso=buscarDocumentoEmiConsulBean.getTiAcceso();
        String tiAcceso=auxTipoAcceso!=null&&(auxTipoAcceso.equals("0")||auxTipoAcceso.equals("1")||auxTipoAcceso.equals("2"))?auxTipoAcceso:"1";
        if (buscarDocumentoEmiConsulBean.getCoEmpElaboro()!=null&&buscarDocumentoEmiConsulBean.getCoEmpElaboro().trim().length()>0&&(pTipoBusqueda.equals("0")||bBusqFiltro)&&tiAcceso.equals("0")){
            sql.append(" AND A.CO_EMP_RES = :pcoEmpRes ");
            objectParam.put("pcoEmpRes", buscarDocumentoEmiConsulBean.getCoEmpElaboro());
        } else {
            if (tiAcceso.equals("1")) {//PERSONAL ,'0' TOTAL
                sql.append(" AND A.CO_EMP_RES = :pcoEmpRes ");
                objectParam.put("pcoEmpRes", buscarDocumentoEmiConsulBean.getCoEmpleado());
            } else if (tiAcceso.equals("2")) {//TOTAL
                sql.append(" AND (A.CO_EMP_RES = :pcoEmpRes OR A.CO_EMP_EMI = :pcoEmpRes) ");
                objectParam.put("pcoEmpRes", buscarDocumentoEmiConsulBean.getCoEmpleado());
            }
        }

        //Filtro
        if (pTipoBusqueda.equals("0") || bBusqFiltro) {
            if (buscarDocumentoEmiConsulBean.getTipoDoc() != null && buscarDocumentoEmiConsulBean.getTipoDoc().trim().length() > 0) {
                sql.append(" AND A.CO_TIP_DOC_ADM = :pCoDocEmi ");
                objectParam.put("pCoDocEmi", buscarDocumentoEmiConsulBean.getTipoDoc());
            }
            if (buscarDocumentoEmiConsulBean.getEstadoDoc() != null && buscarDocumentoEmiConsulBean.getEstadoDoc().trim().length() > 0) {
                sql.append(" AND A.ES_DOC_EMI = :pEsDocEmi ");
                objectParam.put("pEsDocEmi", buscarDocumentoEmiConsulBean.getEstadoDoc());
            }
            if (buscarDocumentoEmiConsulBean.getPrioridadDoc() != null && buscarDocumentoEmiConsulBean.getPrioridadDoc().trim().length() > 0) {
                sql.append(" AND B.CO_PRIORIDAD = :pCoPrioridad ");
                objectParam.put("pCoPrioridad", buscarDocumentoEmiConsulBean.getPrioridadDoc());
            }
            if (buscarDocumentoEmiConsulBean.getCoRefOrigen() != null && buscarDocumentoEmiConsulBean.getCoRefOrigen().trim().length() > 0) {
                sql.append(" AND CHARINDEX(:pTiEmiRef, B.TI_EMI_REF) > 0 ");
                objectParam.put("pTiEmiRef", buscarDocumentoEmiConsulBean.getCoRefOrigen());
            }            
            if (buscarDocumentoEmiConsulBean.getCoDepOrigen() != null && buscarDocumentoEmiConsulBean.getCoDepOrigen().trim().length() > 0) {
                sql.append(" AND A.CO_DEP_EMI = :pCoDepOrigen ");
                objectParam.put("pCoDepOrigen", buscarDocumentoEmiConsulBean.getCoDepOrigen());
            }
            if (buscarDocumentoEmiConsulBean.getCoDepDestino() != null && buscarDocumentoEmiConsulBean.getCoDepDestino().trim().length() > 0) {
                sql.append(" AND CHARINDEX(B.TI_EMI_DES, :pTiEmpPro) > 0 ");
                objectParam.put("pTiEmpPro", buscarDocumentoEmiConsulBean.getCoDepDestino());
            }
            if (buscarDocumentoEmiConsulBean.getEsFiltroFecha() != null
                    && (buscarDocumentoEmiConsulBean.getEsFiltroFecha().equals("1") || buscarDocumentoEmiConsulBean.getEsFiltroFecha().equals("3"))) {
                String vFeEmiIni = buscarDocumentoEmiConsulBean.getFeEmiIni();
                String vFeEmiFin = buscarDocumentoEmiConsulBean.getFeEmiFin();
                if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
                        && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                    sql.append(" AND CAST(A.FE_EMI AS DATE) BETWEEN CAST(:pFeEmiIni AS DATE) AND CAST(:pFeEmiFin AS DATE) ");
                    objectParam.put("pFeEmiIni", vFeEmiIni);
                    objectParam.put("pFeEmiFin", vFeEmiFin);
                }
            }

        }

        //Busqueda
        if (pTipoBusqueda.equals("1")) {
            if(!bBusqFiltro){
                if (buscarDocumentoEmiConsulBean.getCoDependencia() != null && buscarDocumentoEmiConsulBean.getCoDependencia().trim().length() > 0) {
                    sql.append(" AND A.CO_DEP_EMI = :pCoDepEmi ");
                    objectParam.put("pCoDepEmi", buscarDocumentoEmiConsulBean.getCoDependencia());
                }                
            }            
            if (buscarDocumentoEmiConsulBean.getBusNumEmision() != null && buscarDocumentoEmiConsulBean.getBusNumEmision().trim().length() > 0) {
                sql.append(" AND A.NU_COR_EMI = :pnuCorEmi ");
                objectParam.put("pnuCorEmi", buscarDocumentoEmiConsulBean.getBusNumEmision());
            }

            if (buscarDocumentoEmiConsulBean.getBusNumDoc()!= null && buscarDocumentoEmiConsulBean.getBusNumDoc().trim().length() > 1) {
                sql.append(" AND B.NU_DOC LIKE ''+:pnuDocEmi+'%' ");
                objectParam.put("pnuDocEmi", buscarDocumentoEmiConsulBean.getBusNumDoc());
            }

            if (buscarDocumentoEmiConsulBean.getBusNumExpediente() != null && buscarDocumentoEmiConsulBean.getBusNumExpediente().trim().length() > 1) {
                sql.append(" AND B.NU_EXPEDIENTE LIKE '%'+:pnuExpediente+'%' ");
                objectParam.put("pnuExpediente", buscarDocumentoEmiConsulBean.getBusNumExpediente());
            }

            // Busqueda del Asunto
            if (buscarDocumentoEmiConsulBean.getBusAsunto()!= null && buscarDocumentoEmiConsulBean.getBusAsunto().trim().length() > 1) {
                sql.append(" AND CONTAINS(A.*, :pBusquedaTextual) ");
                sqlContains = "SELECT RETORNO FROM [IDOSGD].PK_SGD_DESCRIPCION_QUERYFILTER('"+buscarDocumentoEmiConsulBean.getBusAsunto()+"')";
            }
        }
        
        sql.append("	) X ");
        sql.append(") Z ");
        sql.append("WHERE Z.ROWNUM < 301 ");
        sql.append("ORDER BY Z.NU_COR_EMI DESC ");

        try {
            //Obteniendo el parametro textual si es requerido
            if (sqlContains.length() > 0) {
                String cadena = this.jdbcTemplate.queryForObject(sqlContains, String.class);            
                objectParam.put("pBusquedaTextual", cadena);
            }
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoEmiConsulBean.class));            
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public DocumentoEmiConsulBean getDocumentoEmiAdm(String pnuAnn, String pnuEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CONVERT(VARCHAR(10),(SELECT FE_EXP FROM IDOSGD.TDTC_EXPEDIENTE WHERE NU_ANN_EXP=A.NU_ANN_EXP AND NU_SEC_EXP=A.NU_SEC_EXP),103) FE_EXP_CORTA,\n" +
                    "A.NU_ANN,A.NU_EMI,A.NU_COR_EMI,IDOSGD.PK_SGD_DESCRIPCION_DE_DEPENDENCIA(A.CO_DEP_EMI) DE_DEP_EMI,IDOSGD.PK_SGD_DESCRIPCION_DE_LOCAL(A.CO_LOC_EMI) DE_LOC_EMI,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(A.CO_EMP_EMI) DE_EMP_EMI,IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(A.CO_EMP_RES) DE_EMP_RES,IDOSGD.PK_SGD_DESCRIPCION_DE_DOCUMENTO(A.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM,\n" +
                    "B.NU_DOC,CONVERT(VARCHAR(10),A.FE_EMI,103) FE_EMI_CORTA,A.NU_DIA_ATE,A.DE_ASU,B.IN_EXISTE_DOC EXISTE_DOC,B.IN_EXISTE_ANEXO EXISTE_ANEXO,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_DE_PROCESO_EXP(B.CO_PROCESO_EXP) DE_PROCESO_EXP,IDOSGD.PK_SGD_DESCRIPCION_ESTADOS(A.ES_DOC_EMI,'TDTV_REMITOS') DE_ES_DOC_EMI,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_DE_NU_EXPEDIENTE(A.NU_ANN_EXP,A.NU_SEC_EXP) NU_EXPEDIENTE\n" +
                    "from IDOSGD.TDTV_REMITOS A,IDOSGD.TDTX_REMITOS_RESUMEN B\n" +
                    "where A.NU_ANN=?\n" +
                    "AND A.NU_EMI=?\n" +
                    "AND B.NU_ANN=A.NU_ANN\n" +
                    "AND B.NU_EMI=A.NU_EMI");

        DocumentoEmiConsulBean documentoEmiConsulBean = new DocumentoEmiConsulBean();
        try {
            documentoEmiConsulBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoEmiConsulBean.class),
                    new Object[]{pnuAnn, pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            documentoEmiConsulBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoEmiConsulBean;
    }
    
    @Override
    public String getTipoDestinatarioEmi(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        String result = "NO_OK";

        sql.append("SELECT MAX(ti_des) ");
        sql.append("FROM IDOSGD.TDTV_DESTINOS ");
        sql.append("WHERE nu_emi = ? ");
        sql.append("AND nu_ann = ? ");
        sql.append("AND ES_ELI = '0' ");
        
        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pnuEmi, pnuAnn});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }    
    
    @Override
    public List<DestinatarioDocumentoEmiConsulBean> getLstDestintariotlbEmi(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        List<DestinatarioDocumentoEmiConsulBean> list = new ArrayList<DestinatarioDocumentoEmiConsulBean>();
        sql.append("select a.nu_ann,a.nu_emi,a.nu_des,a.co_loc_des co_local,SUBSTRING(IDOSGD.PK_SGD_DESCRIPCION_DE_LOCAL(a.co_loc_des), 1, 100) de_local,\n"
                + "a.co_dep_des co_dependencia,SUBSTRING(IDOSGD.PK_SGD_DESCRIPCION_DE_DEPENDENCIA(a.co_dep_des), 1, 100) de_dependencia,\n"
                + "a.co_emp_des co_empleado,SUBSTRING(IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(a.co_emp_des), 1, 100) de_empleado,\n"
                + "a.co_mot co_tramite,SUBSTRING(IDOSGD.PK_SGD_DESCRIPCION_MOTIVO(a.co_mot), 1, 100) de_tramite,\n"
                + "a.co_pri co_prioridad,(SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_DE_PRIORIDAD(a.co_pri)) de_prioridad,\n"
                + "a.de_pro de_indicaciones,\n"
                + "a.NU_RUC_DES nu_ruc,SUBSTRING(IDOSGD.PK_SGD_DESCRIPCION_DE_PROVEEDOR(a.NU_RUC_DES), 1, 100) de_proveedor,\n"
                + "a.NU_DNI_DES nu_dni,SUBSTRING(IDOSGD.PK_SGD_DESCRIPCION_ANI_SIMIL(a.NU_DNI_DES), 1, 100) de_ciudadano,\n"
                + "a.CO_OTR_ORI_DES co_otro_origen,\n"
                + "COALESCE(\n"
                + "a.CO_OTR_ORI_DES,\n"
                + "(SELECT C.DE_APE_PAT_OTR+' '+C.DE_APE_MAT_OTR+', '+C.DE_NOM_OTR + ' - ' +\n"
                + "     C.DE_RAZ_SOC_OTR +'##'+\n"
                + "     COALESCE(B.CELE_DESELE,'   ') +'##'+\n"
                + "     C.NU_DOC_OTR_ORI  \n"
                + "  FROM IDOSGD.TDTR_OTRO_ORIGEN C\n"
                + "  LEFT OUTER JOIN"
                + "  ("
                + "  SELECT CELE_CODELE, CELE_DESELE\n"
                + "    FROM IDOSGD.SI_ELEMENTO\n"
                + "   WHERE CTAB_CODTAB ='TIP_DOC_IDENT') B\n"
                + " ON C.CO_TIP_OTR_ORI = B.CELE_CODELE\n"
                + " WHERE C.CO_OTR_ORI = a.CO_OTR_ORI_DES\n"
                + "),\n"
                + "NULL\n"
                + ") de_otro_origen_full,\n"
                + " a.ti_des co_tipo_destino\n"
                + " FROM IDOSGD.tdtv_destinos a\n"
                + " where nu_ann = ? and nu_emi = ?\n"
                + " AND ES_ELI='0' AND NU_EMI_REF is null\n"
                + " order by 3");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioDocumentoEmiConsulBean.class),
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
    public List<ReferenciaConsulBean> getLstDocumReferenciatblEmi(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        List<ReferenciaConsulBean> list = new ArrayList<ReferenciaConsulBean>();
        sql.append("SELECT \n"
                + "IDOSGD.PK_SGD_DESCRIPCION_de_documento (a.co_tip_doc_adm) de_tipo_doc,\n"
                + "(CASE WHEN a.ti_emi='01' OR a.ti_emi='05' THEN  a.nu_doc_emi + '-' + a.nu_ann + '-' + a.de_doc_sig ELSE a.de_doc_sig END)li_nu_doc,\n"
                + "SUBSTRING(IDOSGD.PK_SGD_DESCRIPCION_DE_DEPENDENCIA(a.co_dep_emi), 1, 100) de_dep_emi, \n"
                + "CONVERT(VARCHAR(10),a.fe_emi,103),\n"
                + "b.nu_ann,\n"
                + "b.nu_emi,\n"
                + "COALESCE(RTRIM(LTRIM(CAST(b.nu_des AS VARCHAR))),'N') nu_des ,\n"
                + "b.nu_ann_ref,\n"
                + "b.nu_emi_ref,\n"
                + "COALESCE(RTRIM(LTRIM(CAST(b.nu_des_ref AS VARCHAR))),'N') nu_des_ref,\n"
                + "b.co_ref,\n"
                + "(CASE WHEN COALESCE(RTRIM(LTRIM(CAST(b.nu_des_ref AS VARCHAR))),'N')='N' THEN 'emi'	ELSE 'rec' END)tip_doc_ref, \n"
                + "a.co_tip_doc_adm,'BD' accion_bd\n"
                + "FROM IDOSGD.tdtv_remitos a,IDOSGD.TDTR_REFERENCIA b \n"
                + "WHERE a.nu_ann = b.nu_ann_ref\n"
                + "AND a.nu_emi = b.nu_emi_ref\n"
                + "and b.nu_ann = ?\n"
                + "and b.nu_emi = ?");
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ReferenciaConsulBean.class),
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
    public DocumentoEmiConsulBean existeDocumentoReferenciado(BuscarDocumentoEmiConsulBean buscarDocumentoEmiConsulBean){
        StringBuilder sql = new StringBuilder();
        DocumentoEmiConsulBean documentoEmiConsulBean = null;
        sql.append("SELECT A.NU_ANN,A.NU_EMI,A.NU_COR_DOC FROM IDOSGD.TDTV_REMITOS A\n" +
                    "WHERE A.NU_ANN=?\n" +
                    "AND A.CO_DEP_EMI=?\n" +
                    "AND A.TI_EMI='01'\n" +
                    "AND A.CO_TIP_DOC_ADM=?\n" +
                    "AND A.NU_DOC_EMI=?\n" +
                    "AND A.ES_ELI='0'\n" +
                    "AND A.ES_DOC_EMI NOT IN ('5','7','9')");        
        
        try {
             documentoEmiConsulBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoEmiConsulBean.class),
                    new Object[]{buscarDocumentoEmiConsulBean.getBusCoAnnio(),buscarDocumentoEmiConsulBean.getBusCodDepEmiRef(),buscarDocumentoEmiConsulBean.getBusCodTipoDocRef(),
                    buscarDocumentoEmiConsulBean.getBusNumDocRef()});
             documentoEmiConsulBean.setMsjResult("OK");
        } catch (EmptyResultDataAccessException e) {
            documentoEmiConsulBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoEmiConsulBean;          
    }
    
    @Override
    public List<DocumentoEmiConsulBean> getDocumentosReferenciadoBusq(DocumentoEmiConsulBean documentoEmiConsulBean,String sTipoAcceso, String pnuPagina, int pnuRegistros){       
        StringBuilder sql = new StringBuilder(); 
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List<DocumentoEmiConsulBean> list = new ArrayList<DocumentoEmiConsulBean>();        
        
        sql.append("SELECT TOP 301 *");
        sql.append(" FROM ( ");        
        sql.append(" SELECT ");        
        sql.append(" DISTINCT B.NU_COR_EMI,(SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_DE_EMI_REF(B.NU_ANN, B.NU_EMI)) DE_EMI_REF,B.FE_EMI,");
        sql.append(" CONVERT(VARCHAR(10),B.FE_EMI,103)FE_EMI_CORTA,");
        sql.append(" (SELECT CDOC_DESDOC FROM IDOSGD.SI_MAE_TIPO_DOC WHERE CDOC_TIPDOC = B.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM,");
        sql.append(" (CASE WHEN B.NU_CANDES = 1 THEN (SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_TI_DES_EMP(B.NU_ANN, B.NU_EMI)) ELSE (SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_TI_DES_EMP_V(B.NU_ANN, B.NU_EMI)) END)DE_EMP_PRO,");
        sql.append(" (SELECT CEMP_APEPAT + ' ' + CEMP_APEMAT + ' ' + CEMP_DENOM FROM IDOSGD.RHTM_PER_EMPLEADOS WHERE CEMP_CODEMP = B.CO_EMP_RES) DE_EMP_RES,");
        sql.append(" (SELECT DE_EST FROM IDOSGD.TDTR_ESTADOS WHERE CO_EST + DE_TAB = B.ES_DOC_EMI + 'TDTV_REMITOS') DE_ES_DOC_EMI,");
        sql.append(" C.NU_DOC,UPPER(B.DE_ASU) DE_ASU_M,B.NU_ANN,C.NU_EXPEDIENTE,B.NU_EMI,B.TI_CAP,C.IN_EXISTE_DOC EXISTE_DOC,");
        sql.append(" (CASE WHEN COALESCE(C.TI_EMI_REF,'0')+COALESCE(C.IN_EXISTE_ANEXO,'2') = '00' OR COALESCE(C.TI_EMI_REF,'0')+COALESCE(C.IN_EXISTE_ANEXO,'2') = '02' THEN 0 ELSE 1 END)EXISTE_ANEXO,");
        sql.append(" COALESCE(C.CO_PRIORIDAD,'1') CO_PRIORIDAD,");
        sql.append(" B.ES_DOC_EMI");
        sql.append(" FROM ( ");
        sql.append("  SELECT NU_ANN,NU_EMI FROM IDOSGD.PK_SGD_DESCRIPCION_ARBOL_SEG(:pCoAnio,:pNuEmi)");
        sql.append(" ) A , TDTV_REMITOS B, TDTX_REMITOS_RESUMEN C");
        sql.append(" WHERE A.NU_ANN = B.NU_ANN");
        sql.append(" AND A.NU_EMI = B.NU_EMI");
        sql.append(" AND C.NU_ANN = B.NU_ANN");
        sql.append(" AND C.NU_EMI = B.NU_EMI");
        sql.append(" AND B.TI_EMI='01'");
        sql.append(" AND C.TI_EMI='01'");
        sql.append(" AND B.ES_ELI = '0'");
        sql.append(" AND B.CO_DEP_EMI = :pCoDepEmi");        
        if(sTipoAcceso.equals("1")){
            sql.append(" AND B.CO_EMP_RES = :pcoEmpRes");      
            objectParam.put("pcoEmpRes", documentoEmiConsulBean.getCoEmpRes());           
        }
        objectParam.put("pCoAnio", documentoEmiConsulBean.getNuAnn());           
        objectParam.put("pNuEmi", documentoEmiConsulBean.getNuEmi());   
        objectParam.put("pCoDepEmi", documentoEmiConsulBean.getCoDepEmi());   
        
        sql.append(" ORDER BY B.NU_COR_EMI DESC");
        sql.append(") A ");
        //sql.append("WHERE ROWNUM < 301");            
        
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoEmiConsulBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;          
    }
    
    @Override
    public String getRutaReporte(BuscarDocumentoEmiConsulBean buscarDocumentoEmiConsulBean) {
        String vResult;
        StringBuilder prutaReporte =  new StringBuilder();
        prutaReporte.append(" B.NU_ANN=A.NU_ANN");
        prutaReporte.append(" AND B.NU_EMI=A.NU_EMI");
        try {
            String pNUAnn = buscarDocumentoEmiConsulBean.getCoAnnio();
            if (!(buscarDocumentoEmiConsulBean.getEsFiltroFecha().equals("1") && pNUAnn.equals("0"))) {
                // Parametros Basicos
                prutaReporte.append(" AND A.NU_ANN = '").append(pNUAnn).append("'");
            }
            prutaReporte.append(" AND A.TI_EMI='01'");
            prutaReporte.append(" AND B.TI_EMI='01'");
            prutaReporte.append(" AND A.ES_ELI = '0'");
            // Parametros Basicos
            //prutaReporte.append(" AND A.CO_DEP_EMI = '").append(buscarDocumentoEmiConsulBean.getCoDependencia()).append("'");

            String pTipoBusqueda = buscarDocumentoEmiConsulBean.getTipoBusqueda();                
            
            if (buscarDocumentoEmiConsulBean.getCoEmpElaboro() != null && buscarDocumentoEmiConsulBean.getCoEmpElaboro().trim().length() > 0 && pTipoBusqueda.equals("0")) {
                prutaReporte.append(" AND A.CO_EMP_RES = '").append(buscarDocumentoEmiConsulBean.getCoEmpElaboro()).append("'");
            } else {
                if (buscarDocumentoEmiConsulBean.getTiAcceso().equals("1")) {
                    prutaReporte.append(" AND A.CO_EMP_RES = '").append(buscarDocumentoEmiConsulBean.getCoEmpleado()).append("'");
                } else if (buscarDocumentoEmiConsulBean.getTiAcceso().equals("2")) {
                    prutaReporte.append(" AND (A.CO_EMP_RES = '").append(buscarDocumentoEmiConsulBean.getCoEmpleado()).append("' OR A.CO_EMP_EMI = '")
                    .append(buscarDocumentoEmiConsulBean.getCoEmpleado()).append("')");
                }
            }          
            
            if (pTipoBusqueda.equals("0")) {
                if (buscarDocumentoEmiConsulBean.getTipoDoc() != null && buscarDocumentoEmiConsulBean.getTipoDoc().trim().length() > 0) {
                    prutaReporte.append(" AND A.CO_TIP_DOC_ADM = '").append(buscarDocumentoEmiConsulBean.getTipoDoc()).append("'");
                }
                if (buscarDocumentoEmiConsulBean.getEstadoDoc() != null && buscarDocumentoEmiConsulBean.getEstadoDoc().trim().length() > 0) {
                    prutaReporte.append(" AND A.ES_DOC_EMI = '").append(buscarDocumentoEmiConsulBean.getEstadoDoc()).append("'");
                }
                if (buscarDocumentoEmiConsulBean.getPrioridadDoc() != null && buscarDocumentoEmiConsulBean.getPrioridadDoc().trim().length() > 0) {
                    prutaReporte.append(" AND B.CO_PRIORIDAD = '").append(buscarDocumentoEmiConsulBean.getPrioridadDoc()).append("'");
                }
                if (buscarDocumentoEmiConsulBean.getCoRefOrigen() != null && buscarDocumentoEmiConsulBean.getCoRefOrigen().trim().length() > 0) {
                    prutaReporte.append(" AND CHARINDEX('").append(buscarDocumentoEmiConsulBean.getCoRefOrigen()).append("', B.TI_EMI_REF) > 0 ");
                }                 
                if (buscarDocumentoEmiConsulBean.getCoDepOrigen() != null && buscarDocumentoEmiConsulBean.getCoDepOrigen().trim().length() > 0) {
                    prutaReporte.append(" AND A.CO_DEP_EMI = '").append(buscarDocumentoEmiConsulBean.getCoDepOrigen()).append("' ");
                }
                if (buscarDocumentoEmiConsulBean.getCoDepDestino() != null && buscarDocumentoEmiConsulBean.getCoDepDestino().trim().length() > 0) {
                    prutaReporte.append(" AND CHARINDEX('").append(buscarDocumentoEmiConsulBean.getCoDepDestino()).append("', B.TI_EMI_DES) > 0 ");
                }
                if (buscarDocumentoEmiConsulBean.getEsFiltroFecha() != null
                        && (buscarDocumentoEmiConsulBean.getEsFiltroFecha().equals("1") || buscarDocumentoEmiConsulBean.getEsFiltroFecha().equals("3"))) {
                    String vFeEmiIni = buscarDocumentoEmiConsulBean.getFeEmiIni();
                    String vFeEmiFin = buscarDocumentoEmiConsulBean.getFeEmiFin();
                    if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
                            && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                        prutaReporte.append(" AND CAST(A.FE_EMI AS DATE) between CAST('").append(vFeEmiIni).append("' AS DATE)").append(" AND CAST('").append(vFeEmiFin)
                        .append("' AS DATE) ");
                    }
                }

            }
            prutaReporte.append(" ");
            vResult = "0"+prutaReporte.toString();
        } catch (Exception ex) {
            vResult="1"+ex.getMessage();
            ex.printStackTrace();
        }
        return vResult;
    }
    
    @Override
    public List<DocumentoEmiConsulBean> getListaReporteBusqueda(BuscarDocumentoEmiConsulBean buscarDocumentoEmiConsulBean) {
        String vResult;
        boolean bBusqFiltro = false;
        String sqlContains = "";
        StringBuilder prutaReporte =  new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List list = null;
        
        prutaReporte.append("SELECT	A.NU_COR_EMI, ");
        prutaReporte.append("CONVERT(VARCHAR(10), A.FE_EMI, 103) FE_EMI_CORTA, (SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_DE_EMI_REF(A.NU_ANN, A.NU_EMI)) DE_EMI_REF,");
        prutaReporte.append("(SELECT CDOC_DESDOC ");
        prutaReporte.append("FROM IDOSGD.SI_MAE_TIPO_DOC ");
        prutaReporte.append("WHERE CDOC_TIPDOC = A.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        prutaReporte.append("B.NU_DOC, ");
        prutaReporte.append("CASE A.NU_CANDES ");
        prutaReporte.append("WHEN 1 THEN (SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_TI_DES_EMP](A.NU_ANN, A.NU_EMI)) ");
        prutaReporte.append("ELSE (SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_TI_DES_EMP_V](A.NU_ANN, A.NU_EMI)) ");
        prutaReporte.append("END AS DE_EMP_PRO, ");
        prutaReporte.append("A.DE_ASU, ");
        prutaReporte.append("(SELECT DE_EST ");
        prutaReporte.append("FROM IDOSGD.TDTR_ESTADOS ");
        prutaReporte.append("WHERE CO_EST + DE_TAB = A.ES_DOC_EMI + 'TDTV_REMITOS') DE_ES_DOC_EMI, ");
        prutaReporte.append("A.NU_DIA_ATE, ");
        prutaReporte.append("(SELECT CEMP_APEPAT + ' ' + CEMP_APEMAT + ' ' + CEMP_DENOM ");
        prutaReporte.append("FROM IDOSGD.RHTM_PER_EMPLEADOS ");
        prutaReporte.append("WHERE CEMP_CODEMP = A.CO_EMP_RES) DE_EMP_RES, ");
        prutaReporte.append("B.NU_EXPEDIENTE ");
        prutaReporte.append("FROM IDOSGD.TDTV_REMITOS A, ");
        prutaReporte.append("IDOSGD.TDTX_REMITOS_RESUMEN B ");
        prutaReporte.append("WHERE");
        
        prutaReporte.append(" B.NU_ANN=A.NU_ANN");
        prutaReporte.append(" AND B.NU_EMI=A.NU_EMI");
        try {
            String pNUAnn = buscarDocumentoEmiConsulBean.getCoAnnio();
            if (!(buscarDocumentoEmiConsulBean.getEsFiltroFecha().equals("1") && pNUAnn.equals("0"))) {
                // Parametros Basicos
                //prutaReporte.append(" AND A.NU_ANN = '").append(pNUAnn).append("'");
                prutaReporte.append(" AND A.NU_ANN = :pnuAnn ");
                objectParam.put("pnuAnn", pNUAnn);
            }
            prutaReporte.append(" AND A.TI_EMI='01'");
            prutaReporte.append(" AND B.TI_EMI='01'");
            prutaReporte.append(" AND A.ES_ELI = '0'");
            // Parametros Basicos
            //prutaReporte.append(" AND A.CO_DEP_EMI = '").append(buscarDocumentoEmiConsulBean.getCoDependencia()).append("'");

            String pTipoBusqueda = buscarDocumentoEmiConsulBean.getTipoBusqueda();
            if (pTipoBusqueda.equals("1") && buscarDocumentoEmiConsulBean.isEsIncluyeFiltro()) {
                bBusqFiltro = true;
            }
            
            if (buscarDocumentoEmiConsulBean.getCoEmpElaboro() != null && buscarDocumentoEmiConsulBean.getCoEmpElaboro().trim().length() > 0 && pTipoBusqueda.equals("0")) {
                //prutaReporte.append(" AND A.CO_EMP_RES = '").append(buscarDocumentoEmiConsulBean.getCoEmpElaboro()).append("'");
                prutaReporte.append(" AND A.CO_EMP_RES = :pcoEmpRes ");
                objectParam.put("pcoEmpRes", buscarDocumentoEmiConsulBean.getCoEmpElaboro());
            } else {
                if (buscarDocumentoEmiConsulBean.getTiAcceso().equals("1")) {
                    //prutaReporte.append(" AND A.CO_EMP_RES = '").append(buscarDocumentoEmiConsulBean.getCoEmpleado()).append("'");
                    prutaReporte.append(" AND A.CO_EMP_RES = :pcoEmpRes ");
                    objectParam.put("pcoEmpRes", buscarDocumentoEmiConsulBean.getCoEmpleado());
                } else if (buscarDocumentoEmiConsulBean.getTiAcceso().equals("2")) {
                    //prutaReporte.append(" AND (A.CO_EMP_RES = '").append(buscarDocumentoEmiConsulBean.getCoEmpleado()).append("' OR A.CO_EMP_EMI = '")
                    //.append(buscarDocumentoEmiConsulBean.getCoEmpleado()).append("')");
                    prutaReporte.append(" AND (A.CO_EMP_RES = :pcoEmpRes OR A.CO_EMP_EMI = :pcoEmpRes ");
                    objectParam.put("pcoEmpRes", buscarDocumentoEmiConsulBean.getCoEmpleado());
                }
            }          
            
            if (pTipoBusqueda.equals("0")) {
                if (buscarDocumentoEmiConsulBean.getTipoDoc() != null && buscarDocumentoEmiConsulBean.getTipoDoc().trim().length() > 0) {
                    //prutaReporte.append(" AND A.CO_TIP_DOC_ADM = '").append(buscarDocumentoEmiConsulBean.getTipoDoc()).append("'");
                    prutaReporte.append(" AND A.CO_TIP_DOC_ADM = :pCoDocEmi ");
                    objectParam.put("pCoDocEmi", buscarDocumentoEmiConsulBean.getTipoDoc());
                }
                if (buscarDocumentoEmiConsulBean.getEstadoDoc() != null && buscarDocumentoEmiConsulBean.getEstadoDoc().trim().length() > 0) {
                    //prutaReporte.append(" AND A.ES_DOC_EMI = '").append(buscarDocumentoEmiConsulBean.getEstadoDoc()).append("'");
                    prutaReporte.append(" AND A.ES_DOC_EMI = :pEsDocEmi ");
                    objectParam.put("pEsDocEmi", buscarDocumentoEmiConsulBean.getEstadoDoc());
                }
                if (buscarDocumentoEmiConsulBean.getPrioridadDoc() != null && buscarDocumentoEmiConsulBean.getPrioridadDoc().trim().length() > 0) {
                    //prutaReporte.append(" AND B.CO_PRIORIDAD = '").append(buscarDocumentoEmiConsulBean.getPrioridadDoc()).append("'");
                    prutaReporte.append(" AND B.CO_PRIORIDAD = :pCoPrioridad ");
                    objectParam.put("pCoPrioridad", buscarDocumentoEmiConsulBean.getPrioridadDoc());
                }
                if (buscarDocumentoEmiConsulBean.getCoRefOrigen() != null && buscarDocumentoEmiConsulBean.getCoRefOrigen().trim().length() > 0) {
                    //prutaReporte.append(" AND CHARINDEX('").append(buscarDocumentoEmiConsulBean.getCoRefOrigen()).append("', B.TI_EMI_REF) > 0 ");
                    prutaReporte.append(" AND CHARINDEX(:pTiEmiRef, B.TI_EMI_REF) > 0 ");
                    objectParam.put("pTiEmiRef", buscarDocumentoEmiConsulBean.getCoRefOrigen());
                }                 
                if (buscarDocumentoEmiConsulBean.getCoDepOrigen() != null && buscarDocumentoEmiConsulBean.getCoDepOrigen().trim().length() > 0) {
                    //prutaReporte.append(" AND A.CO_DEP_EMI = '").append(buscarDocumentoEmiConsulBean.getCoDepOrigen()).append("' ");
                    prutaReporte.append(" AND A.CO_DEP_EMI = :pCoDepOrigen ");
                    objectParam.put("pCoDepOrigen", buscarDocumentoEmiConsulBean.getCoDepOrigen());
                }
                if (buscarDocumentoEmiConsulBean.getCoDepDestino() != null && buscarDocumentoEmiConsulBean.getCoDepDestino().trim().length() > 0) {
                    //prutaReporte.append(" AND CHARINDEX('").append(buscarDocumentoEmiConsulBean.getCoDepDestino()).append("', B.TI_EMI_DES) > 0 ");
                     prutaReporte.append(" AND CHARINDEX(B.TI_EMI_DES, :pTiEmpPro) > 0 ");
                     objectParam.put("pTiEmpPro", buscarDocumentoEmiConsulBean.getCoDepDestino());
                }
                if (buscarDocumentoEmiConsulBean.getEsFiltroFecha() != null
                        && (buscarDocumentoEmiConsulBean.getEsFiltroFecha().equals("1") || buscarDocumentoEmiConsulBean.getEsFiltroFecha().equals("3"))) {
                    String vFeEmiIni = buscarDocumentoEmiConsulBean.getFeEmiIni();
                    String vFeEmiFin = buscarDocumentoEmiConsulBean.getFeEmiFin();
                    if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
                            && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                        //prutaReporte.append(" AND CAST(A.FE_EMI AS DATE) between CAST('").append(vFeEmiIni).append("' AS DATE)").append(" AND CAST('").append(vFeEmiFin)
                        //.append("' AS DATE) ");
                        prutaReporte.append(" AND CAST(A.FE_EMI AS DATE) BETWEEN CAST(:pFeEmiIni AS DATE) AND CAST(:pFeEmiFin AS DATE) ");
                        objectParam.put("pFeEmiIni", vFeEmiIni);
                        objectParam.put("pFeEmiFin", vFeEmiFin);
                    }
                }

            }
            
            //Busqueda
            if (pTipoBusqueda.equals("1")) {
                if(!bBusqFiltro){
                    if (buscarDocumentoEmiConsulBean.getCoDependencia() != null && buscarDocumentoEmiConsulBean.getCoDependencia().trim().length() > 0) {
                        prutaReporte.append(" AND A.CO_DEP_EMI = :pCoDepEmi ");
                        objectParam.put("pCoDepEmi", buscarDocumentoEmiConsulBean.getCoDependencia());
                    }                
                }            
                if (buscarDocumentoEmiConsulBean.getBusNumEmision() != null && buscarDocumentoEmiConsulBean.getBusNumEmision().trim().length() > 0) {
                    prutaReporte.append(" AND A.NU_COR_EMI = :pnuCorEmi ");
                    objectParam.put("pnuCorEmi", buscarDocumentoEmiConsulBean.getBusNumEmision());
                }

                if (buscarDocumentoEmiConsulBean.getBusNumDoc()!= null && buscarDocumentoEmiConsulBean.getBusNumDoc().trim().length() > 1) {
                    prutaReporte.append(" AND B.NU_DOC LIKE ''+:pnuDocEmi+'%' ");
                    objectParam.put("pnuDocEmi", buscarDocumentoEmiConsulBean.getBusNumDoc());
                }

                if (buscarDocumentoEmiConsulBean.getBusNumExpediente() != null && buscarDocumentoEmiConsulBean.getBusNumExpediente().trim().length() > 1) {
                    prutaReporte.append(" AND B.NU_EXPEDIENTE LIKE '%'+:pnuExpediente+'%' ");
                    objectParam.put("pnuExpediente", buscarDocumentoEmiConsulBean.getBusNumExpediente());
                }

                // Busqueda del Asunto
                if (buscarDocumentoEmiConsulBean.getBusAsunto()!= null && buscarDocumentoEmiConsulBean.getBusAsunto().trim().length() > 1) {
                    prutaReporte.append(" AND CONTAINS(A.*, :pBusquedaTextual) ");
                    sqlContains = "SELECT RETORNO FROM [IDOSGD].PK_SGD_DESCRIPCION_QUERYFILTER('"+buscarDocumentoEmiConsulBean.getBusAsunto()+"')";
                }
            }
            
            prutaReporte.append(" ORDER BY A.NU_COR_EMI DESC");
            
            
            //Obteniendo el parametro textual si es requerido
            if (sqlContains.length() > 0) {
                String cadena = this.jdbcTemplate.queryForObject(sqlContains, String.class);            
                objectParam.put("pBusquedaTextual", cadena);
            }
            
            list = this.namedParameterJdbcTemplate.query(prutaReporte.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoEmiConsulBean.class));
            
            //vResult = "0"+prutaReporte.toString();
        } catch (Exception ex) {
            vResult="1"+ex.getMessage();
            ex.printStackTrace();
        }        
        return list;
    }

    @Override
    public List<DocumentoEmiConsulBean> getDocumentosBuscaEmiAdmFiltro(BuscarDocumentoEmiConsulBean buscarDocumentoEmiConsulBean, String pnuPagina, int pnuRegistros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DocumentoEmiConsulBean> getDocumentosBuscaEmiAdmFiltroSize(BuscarDocumentoEmiConsulBean buscarDocumentoEmiConsulBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DocumentoEmiConsulBean> getDocumentosReferenciadoBusqSize(DocumentoEmiConsulBean documentoEmiConsulBean, String sTipoAcceso) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
