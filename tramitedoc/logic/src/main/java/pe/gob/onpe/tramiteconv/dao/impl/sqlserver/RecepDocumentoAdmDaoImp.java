/**
 *
 */
package pe.gob.onpe.tramiteconv.dao.impl.sqlserver;

import java.util.List;
import java.util.ArrayList;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import pe.gob.onpe.tramitedoc.dao.RecepDocumentoAdmDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoRecepBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaBean;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import pe.gob.onpe.tramitedoc.util.Paginacion;
import pe.gob.onpe.tramitedoc.web.util.BusquedaTextual;

/**
 * @author ecueva
 *
 */
@Repository("recepDocumentoAdmDao")
public class RecepDocumentoAdmDaoImp extends SimpleJdbcDaoBase implements RecepDocumentoAdmDao {

    private SimpleJdbcCall spPuActualizaGuiaMp, spActualizaEstado;

    @Override
    public List<DocumentoBean> getDocumentosBuscaRecepAdm(BuscarDocumentoRecepBean buscarDocumentoRecep) {
        StringBuilder sql = new StringBuilder();
        String sqlContains="";
        boolean bBusqFiltro = false;
        String sOrdenList="";
        Map<String, Object> objectParam = new HashMap<String, Object>();

        List<DocumentoBean> list = new ArrayList<DocumentoBean>();

        sql.append("SELECT TOP 100 X.*,");
        sql.append(" IDOSGD.PK_SGD_DESCRIPCION_TI_EMI_EMP(X.NU_ANN, X.NU_EMI) DE_ORI_EMI,");
        sql.append(" IDOSGD.PK_SGD_DESCRIPCION_DE_DOCUMENTO(X.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append(" IDOSGD.PK_SGD_DESCRIPCION_ESTADOS(X.ES_DOC_REC,'TDTV_DESTINOS') DE_ES_DOC_DES,");
        sql.append(" CASE X.TI_DES");
        sql.append("  WHEN '01' THEN  IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(X.CO_EMP_DES)");
        sql.append("  WHEN '02' THEN  IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(X.NU_RUC_DES)");
        sql.append("  WHEN '03' THEN  IDOSGD.PK_SGD_DESCRIPCION_ANI_SIMIL(X.NU_DNI_DES)");
        sql.append("  WHEN '04' THEN  IDOSGD.PK_SGD_DESCRIPCION_OTRO_ORIGEN(X.CO_OTR_ORI_DES)");
        sql.append(" END DE_EMP_DES");        
        sql.append(" FROM ( ");
        sql.append("SELECT A.NU_ANN,A.NU_EMI,A.TI_CAP,B.NU_COR_DES,IDOSGD.PK_SGD_DESCRIPCION_FORMAT(A.FE_EMI,'DD/MM/YYYY') FE_EMI_CORTA,A.FE_EMI,");
        sql.append(" A.CO_TIP_DOC_ADM , C.NU_DOC,");
        sql.append(" B.TI_DES,B.CO_EMP_DES,B.NU_RUC_DES,B.NU_DNI_DES, B.CO_OTR_ORI_DES,");
        sql.append(" UPPER (A.DE_ASU) DE_ASU_M,C.NU_EXPEDIENTE,");
        sql.append(" C.IN_EXISTE_DOC EXISTE_DOC,");
        sql.append(" CASE ISNULL(C.TI_EMI_REF, '0') + ISNULL(C.IN_EXISTE_ANEXO, '2') "); 
        sql.append("	WHEN '00' THEN 0 ");
        sql.append("	WHEN '02' THEN 0 ");
        sql.append("    ELSE 1 ");
        sql.append(" END EXISTE_ANEXO, ");        
        sql.append(" NU_DES,ISNULL(B.CO_PRI,'1') CO_PRI, B.ES_DOC_REC,");
        sql.append(" B.CO_ETIQUETA_REC");
        sql.append(" FROM IDOSGD.TDTV_REMITOS A, IDOSGD.TDTV_DESTINOS B, IDOSGD.TDTX_REMITOS_RESUMEN C");
        sql.append(" WHERE");
        sql.append(" B.NU_ANN = A.NU_ANN");
        sql.append(" AND B.NU_EMI = A.NU_EMI");
        sql.append(" AND C.NU_ANN = B.NU_ANN");
        sql.append(" AND C.NU_EMI = B.NU_EMI");
        String pNUAnn = buscarDocumentoRecep.getsCoAnnio();
        if (!(buscarDocumentoRecep.getEsFiltroFecha().equals("1") && pNUAnn.equals("0"))) {
            sql.append(" AND A.NU_ANN = :pNuAnn");
            // Parametros Basicos
            objectParam.put("pNuAnn", pNUAnn);
        }

        sql.append(" AND B.CO_DEP_DES = :pCoDepDes");
        sql.append(" AND A.ES_ELI = '0'");
        sql.append(" AND A.CO_GRU IN ('1','2','3')");
        sql.append(" AND B.ES_ELI = '0'");
        sql.append(" AND A.ES_DOC_EMI NOT IN ('5', '9', '7')");
        sql.append(" AND A.IN_OFICIO = '0'");

        // Parametros Basicos
        objectParam.put("pCoDepDes", buscarDocumentoRecep.getsCoDependencia());

        String pTipoBusqueda = buscarDocumentoRecep.getsTipoBusqueda();
        if (pTipoBusqueda.equals("1") && buscarDocumentoRecep.isEsIncluyeFiltro()) {
            bBusqFiltro = true;
        }
        String auxTipoAcceso=buscarDocumentoRecep.getsTiAcceso();
        String tiAcceso=auxTipoAcceso!=null&&(auxTipoAcceso.equals("0")||auxTipoAcceso.equals("1")||auxTipoAcceso.equals("2"))?auxTipoAcceso:"1";         
        if (buscarDocumentoRecep.getsDestinatario()!=null&&buscarDocumentoRecep.getsDestinatario().trim().length()>0&&(pTipoBusqueda.equals("0")||bBusqFiltro)&&tiAcceso.equals("0")) {
            sql.append(" AND B.CO_EMP_DES = :pcoEmpDes ");
            objectParam.put("pcoEmpDes", buscarDocumentoRecep.getsDestinatario());
        } else {
            if (tiAcceso.equals("1")) {
                sql.append(" AND B.CO_EMP_DES = :pcoEmpDes ");
                objectParam.put("pcoEmpDes", buscarDocumentoRecep.getsCoEmpleado());
            } else if (tiAcceso.equals("2")) {
                sql.append(" AND B.CO_EMP_DES = :pcoEmpDes  ");
                objectParam.put("pcoEmpDes", buscarDocumentoRecep.getsCoEmpleado());
            }
        }

        //Filtro
        if (pTipoBusqueda.equals("0") || bBusqFiltro) {
            if (buscarDocumentoRecep.getsTipoDoc() != null && buscarDocumentoRecep.getsTipoDoc().trim().length() > 0) {
                sql.append(" AND A.CO_TIP_DOC_ADM = :pCoDocEmi ");
                objectParam.put("pCoDocEmi", buscarDocumentoRecep.getsTipoDoc());
            }
            if (buscarDocumentoRecep.getsEstadoDoc() != null && buscarDocumentoRecep.getsEstadoDoc().trim().length() > 0) {
                String estadoDoc=buscarDocumentoRecep.getsEstadoDoc();
                sql.append(" AND B.ES_DOC_REC = :pEsDocRec ");
                objectParam.put("pEsDocRec", estadoDoc);
                if(!estadoDoc.equals("0")){
                    sOrdenList=" DESC";
                }
            }else{
                sOrdenList=" DESC";
            }
            if (buscarDocumentoRecep.getsPrioridadDoc() != null && buscarDocumentoRecep.getsPrioridadDoc().trim().length() > 0) {
                sql.append(" AND B.CO_PRI = :pCoPrioridad ");
                objectParam.put("pCoPrioridad", buscarDocumentoRecep.getsPrioridadDoc());
            }
            if (buscarDocumentoRecep.getsRemitente() != null && buscarDocumentoRecep.getsRemitente().trim().length() > 0) {
                sql.append(" AND ISNULL(C.CO_DEP_EMI_REF,'0') = :pTiEmiRef ");
                objectParam.put("pTiEmiRef", buscarDocumentoRecep.getsRemitente());
            }
            if (buscarDocumentoRecep.getIdEtiqueta() != null && buscarDocumentoRecep.getIdEtiqueta().trim().length() > 0) {
                sql.append(" AND B.CO_ETIQUETA_REC = :pcoEtiquetaRec ");
                objectParam.put("pcoEtiquetaRec", buscarDocumentoRecep.getIdEtiqueta());
            }
            if (buscarDocumentoRecep.getEsFiltroFecha() != null
                    && (buscarDocumentoRecep.getEsFiltroFecha().equals("1") || buscarDocumentoRecep.getEsFiltroFecha().equals("3"))) {
                String vFeEmiIni = buscarDocumentoRecep.getsFeEmiIni();
                String vFeEmiFin = buscarDocumentoRecep.getsFeEmiFin();
                if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
                        && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                    sql.append(" AND CAST(FE_EMI AS DATE) between CAST(:pFeEmiIni AS DATE) AND CAST(:pFeEmiFin AS DATE) ");
                    objectParam.put("pFeEmiIni", vFeEmiIni);
                    objectParam.put("pFeEmiFin", vFeEmiFin);
                }
            }
        }

        if (pTipoBusqueda.equals("1")) {
            if (buscarDocumentoRecep.getsNroDocumento() != null && buscarDocumentoRecep.getsNroDocumento().trim().length() > 1) {
                sql.append(" AND A.NU_DOC_EMI LIKE ''+:pnuDocEmi+'%' ");
                objectParam.put("pnuDocEmi", buscarDocumentoRecep.getsNroDocumento());
            }

            if (buscarDocumentoRecep.getsBuscNroExpediente() != null && buscarDocumentoRecep.getsBuscNroExpediente().trim().length() > 1) {
                sql.append(" AND C.NU_EXPEDIENTE LIKE '%'+:pnuExpediente+'%' ");
                objectParam.put("pnuExpediente", buscarDocumentoRecep.getsBuscNroExpediente());
            }

            // Busqueda del Asunto
            if (buscarDocumentoRecep.getsBuscAsunto() != null && buscarDocumentoRecep.getsBuscAsunto().trim().length() > 1) {
                //sql.append(" AND CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue( buscarDocumentoRecep.getsBuscAsunto())+"', 1 ) > 1 ");
                //sql.append(" AND UPPER(A.DE_ASU) LIKE '%'+:pDeAsunto+'%' ");
                //objectParam.put("pDeAsunto", buscarDocumentoRecep.getsBuscAsunto());
                sql.append(" AND CONTAINS(A.DE_ASU, :pBusquedaTextual) ");
                sqlContains="SELECT RETORNO FROM [IDOSGD].PK_SGD_DESCRIPCION_QUERYFILTER('"+buscarDocumentoRecep.getsBuscAsunto()+"')";
            }

        }

        sql.append(") X ");
        sql.append(" ORDER BY X.FE_EMI").append(sOrdenList);

        try {
            if(sqlContains.length()>0){
                String cadena = this.jdbcTemplate.queryForObject(sqlContains, String.class);            
                objectParam.put("pBusquedaTextual", cadena);
            }            
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoBean.class));

        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public DocumentoBean getDocumentoRecepAdm(String pnuAnn, String pnuEmi, String pnuDes) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.NU_ANN,A.NU_EMI,A.TI_EMI,IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(A.CO_EMP_RES) DE_EMP_RES,A.FE_EMI,\n"
                + "IDOSGD.PK_SGD_DESCRIPCION_TI_EMI_EMP(A.NU_ANN, A.NU_EMI) DE_ORI_EMI,A.CO_TIP_DOC_ADM,IDOSGD.PK_SGD_DESCRIPCION_DE_DOCUMENTO(A.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM,\n"
                + "CASE A.TI_EMI WHEN '01' THEN A.NU_DOC_EMI + '-' + A.NU_ANN + '-' + A.DE_DOC_SIG WHEN '05' THEN A.NU_DOC_EMI + '-' + A.NU_ANN + '-' + A.DE_DOC_SIG ELSE A.DE_DOC_SIG END NU_DOC,\n"
                + "A.NU_DIA_ATE,A.DE_ASU,B.DE_ANE,B.NU_DES,B.NU_COR_DES,B.CO_DEP_DES,IDOSGD.PK_SGD_DESCRIPCION_DE_DEPENDENCIA(B.CO_DEP_DES) DE_DEP_DES,(SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_DE_PRIORIDAD(B.CO_PRI)) DE_PRI,\n"
                + "CASE B.TI_DES WHEN '01' THEN IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP (B.CO_EMP_DES) WHEN '02' THEN IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP (B.NU_RUC_DES) WHEN '03' THEN IDOSGD.PK_SGD_DESCRIPCION_ANI_SIMIL (B.NU_DNI_DES) WHEN '04' THEN IDOSGD.PK_SGD_DESCRIPCION_OTRO_ORIGEN (B.CO_OTR_ORI_DES) END DE_EMP_DES,\n"
                + "IDOSGD.PK_SGD_DESCRIPCION_MOTIVO (B.CO_MOT) DE_MOT,B.DE_PRO,B.CO_EMP_REC,IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP (B.CO_EMP_REC) DE_EMP_REC, B.ES_DOC_REC, IDOSGD.PK_SGD_DESCRIPCION_ESTADOS (B.ES_DOC_REC,'TDTV_DESTINOS') DE_ES_DOC_DES, B.FE_REC_DOC,\n"
                + "B.FE_ARC_DOC, B.FE_ATE_DOC,A.NU_ANN_EXP,A.NU_SEC_EXP,IDOSGD.PK_SGD_DESCRIPCION_FORMAT(A.FE_EMI,'DD/MM/YYYY HH24:MI') FE_EMI_CORTA,IDOSGD.PK_SGD_DESCRIPCION_FORMAT(A.FE_EMI,'DD/MM/YY') FE_EMI_CORTA2,\n"
                + "IDOSGD.PK_SGD_DESCRIPCION_FORMAT(B.FE_REC_DOC,'DD/MM/YYYY HH24:MI') FE_REC_DOC_CORTA, IDOSGD.PK_SGD_DESCRIPCION_FORMAT(B.FE_ARC_DOC,'DD/MM/YYYY HH24:MI') FE_ARC_DOC_CORTA, \n"
                + "IDOSGD.PK_SGD_DESCRIPCION_FORMAT(B.FE_ATE_DOC,'DD/MM/YYYY HH24:MI') FE_ATE_DOC_CORTA,'1' EXISTE_DOC,(SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_FU_DOC_ANE(A.NU_ANN,A.NU_EMI)) EXISTE_ANEXO,\n"
                + "B.Ti_Fisico_Rec,B.Co_Etiqueta_Rec\n"
                + "FROM IDOSGD.TDTV_REMITOS A, IDOSGD.TDTV_DESTINOS B\n"
                + "WHERE A.ES_ELI='0' AND B.ES_ELI='0'\n"
                + "AND A.NU_ANN = B.NU_ANN AND A.NU_EMI = B.NU_EMI\n"
                + "AND A.ES_DOC_EMI NOT IN ('5', '9', '7')\n"
                + "AND A.IN_OFICIO = '0'\n"
                + "AND B.NU_ANN=? AND B.NU_EMI=? AND B.NU_DES=?");

        DocumentoBean documentoBean = new DocumentoBean();
        try {
            documentoBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class),
                    new Object[]{pnuAnn, pnuEmi, pnuDes});
        } catch (EmptyResultDataAccessException e) {
            documentoBean = null;
        } catch (Exception e) {
            System.out.println("nu_ann=" + pnuAnn + "," + "nu_emi=" + pnuEmi + "nu_des=" + pnuDes);
            e.printStackTrace();
        }
        return documentoBean;
    }

    @Override
    public ExpedienteBean getExpDocumentoRecepAdm(String pnuAnnExp, String pnuSecExp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT NU_ANN_EXP,NU_SEC_EXP,FE_EXP,IDOSGD.PK_SGD_DESCRIPCION_FORMAT(FE_EXP,'DD/MM/YYYY') FE_EXP_CORTA,FE_VENCE,CO_PROCESO,IDOSGD.PK_SGD_DESCRIPCION_DE_PROCESO_EXP(CO_PROCESO) DE_PROCESO,DE_DETALLE,CO_DEP_EXP,CO_GRU,NU_CORR_EXP,NU_EXPEDIENTE,NU_FOLIOS,NU_PLAZO,US_CREA_AUDI,FE_CREA_AUDI,US_MODI_AUDI,FE_MODI_AUDI,ES_ESTADO\n"
                + "FROM IDOSGD.TDTC_EXPEDIENTE\n"
                + "where\n"
                + "NU_ANN_EXP=? and NU_SEC_EXP=?");

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
    public List<ReferenciaBean> getDocumentosRefRecepAdm(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IDOSGD.PK_SGD_DESCRIPCION_DE_DOCUMENTO (a.co_tip_doc_adm) li_tip_doc, CASE a.ti_emi\n"
                + "                  WHEN '01' THEN a.nu_doc_emi + '-' + a.nu_ann + '/' + a.de_doc_sig\n"
                + "                  WHEN '05' THEN a.nu_doc_emi + '-' + a.nu_ann + '/' + a.de_doc_sig\n"
                + "                  ELSE a.de_doc_sig\n"
                + "                  END li_nu_doc,a.fe_emi,IDOSGD.PK_SGD_DESCRIPCION_FORMAT(a.fe_emi,'DD/MM/YYYY') fe_emi_corta,b.co_ref,b.nu_ann,b.nu_emi,b.nu_des,b.nu_ann_ref,\n"
                + "                 b.nu_emi_ref,b.nu_des_ref\n"
                + "FROM IDOSGD.tdtv_remitos a, IDOSGD.TDTR_REFERENCIA b\n"
                + "WHERE a.nu_ann = b.nu_ann_ref\n"
                + "AND a.nu_emi = b.nu_emi_ref\n"
                + "AND b.NU_EMI=? \n"
                + "AND b.NU_ANN=?");

        List<ReferenciaBean> list = new ArrayList<ReferenciaBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ReferenciaBean.class),
                    new Object[]{pnuEmi, pnuAnn});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String actualizarGuiaMesaPartes(DocumentoBean documentoBean) {
        String mensaje = "NO_OK";
        this.spPuActualizaGuiaMp = new SimpleJdbcCall(this.dataSource).withProcedureName("IDOSGD.PK_SGD_TRAMITE_PU_ACTUALIZA_GUIA_MP")
                .withoutProcedureColumnMetaDataAccess()
                .useInParameterNames("pnu_ann", "pnu_emi", "pnu_ann", "pnu_emi")
                .declareParameters(
                        new SqlParameter("pnu_ann", Types.VARCHAR),
                        new SqlParameter("pnu_emi", Types.VARCHAR),
                        new SqlParameter("pnu_des", Types.VARCHAR),
                        new SqlParameter("pes_doc_rec", Types.VARCHAR));
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pnu_ann", documentoBean.getNuAnn())
                .addValue("pnu_emi", documentoBean.getNuEmi())
                .addValue("pnu_des", documentoBean.getNuDes())
                .addValue("pes_doc_rec", documentoBean.getEsDocRec());

        try {
            this.spPuActualizaGuiaMp.execute(in);
            mensaje = "OK";
        } catch (Exception ex) {
            //mensaje = "NO_OK";
            ex.printStackTrace();
        }
        return mensaje;
    }

    @Override
    public String getNumCorrelativoDestino(String nuAnn, String coDepDes) {
        String result = "1";
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ISNULL(MAX(nu_cor_des), 0) + 1\n"
                + "FROM IDOSGD.tdtv_destinos\n"
                + "WHERE nu_ann = ?\n"
                + "AND co_dep_des = ?");

        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{nuAnn, coDepDes});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String actualizarEstado(DocumentoBean documentoBean) {
        String mensaje = "NO_OK";
        this.spActualizaEstado = new SimpleJdbcCall(this.dataSource).withProcedureName("IDOSGD.PK_SGD_TRAMITE_ACTUALIZA_ESTADO")        
                .withoutProcedureColumnMetaDataAccess()
                .useInParameterNames("pnu_ann", "pnu_emi", "pnu_des", "pest")
                .declareParameters(
                        new SqlParameter("pnu_ann", Types.VARCHAR),
                        new SqlParameter("pnu_emi", Types.VARCHAR),
                        new SqlParameter("pnu_des", Types.NUMERIC),
                        new SqlParameter("pest", Types.VARCHAR));
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pnu_ann", documentoBean.getNuAnn())
                .addValue("pnu_emi", documentoBean.getNuEmi())
                .addValue("pnu_des", Integer.parseInt(documentoBean.getNuDes()))
                .addValue("pest", documentoBean.getEsDocRec());

        try {
            this.spActualizaEstado.execute(in);
            mensaje = "OK";
        } catch (Exception ex) {
            //mensaje = "NO_OK";
            ex.printStackTrace();
        }
        return mensaje;
    }

    @Override
    public String updDocumentoBean(DocumentoBean documentoBean, String paccion) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE IDOSGD.TDTV_DESTINOS ");
        sql.append("SET ES_DOC_REC = ?, CO_USE_MOD = ?, FE_USE_MOD = CURRENT_TIMESTAMP");
        //documento recepcionados atendido y derivado esos son los estados.
        if (paccion.equals("1") || paccion.equals("0")/*&& !documentoBean.getFeRecDoc().equals("")*/) {//no leido
            sql.append(",FE_REC_DOC = CONVERT(DATETIME,'");
            sql.append(documentoBean.getFeRecDoc());
            sql.append("'),");
            sql.append("CO_EMP_REC ='");
            sql.append(documentoBean.getCoEmpRec()).append("'");
            
            if(documentoBean.getFeAteDoc()!=null&&documentoBean.getFeAteDoc().trim().length()>0){
                sql.append(",FE_ATE_DOC=CONVERT(DATETIME,'"); 
                sql.append(documentoBean.getFeAteDoc());                    
                sql.append("')");
            }
            if(documentoBean.getFeArcDoc()!=null&&documentoBean.getFeArcDoc().trim().length()>0){
                sql.append(",FE_ARC_DOC=CONVERT(DATETIME,'");
                sql.append(documentoBean.getFeArcDoc());
                sql.append("')");                    
            }            
        }/*else if(paccion.equals("1") && documentoBean.getFeRecDoc().equals("")){//no leido
         sql.append(",FE_REC_DOC=NULL");
         sql.append(",CO_EMP_REC ='");
         sql.append(documentoBean.getCoEmpRec());
         sql.append("'");
         }*/ else if (paccion.equals("2")) {
            sql.append(",CO_EMP_REC=NULL,FE_REC_DOC=NULL,FE_ATE_DOC=NULL,FE_ARC_DOC=NULL");
        }
        if(documentoBean.getNuCorDes()!=null&&documentoBean.getNuCorDes().trim().length()>0){
            sql.append(",NU_COR_DES=").append(documentoBean.getNuCorDes());
        }else{
            sql.append(",NU_COR_DES=NULL");
        }        
        sql.append(",DE_ANE=?");
        sql.append(" WHERE NU_ANN = ? ");
        sql.append("AND NU_EMI = ? ");
        sql.append("AND NU_DES = ? ");
        String vReturn = "NO_OK";
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                documentoBean.getEsDocRec(), documentoBean.getCodUseMod(),
                documentoBean.getDeAne(),
                documentoBean.getNuAnn(), documentoBean.getNuEmi(), documentoBean.getNuDes()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public List<DocumentoBean> getDocumentosRecepAdm(BuscarDocumentoRecepBean buscarDocumentoRecep, Paginacion paginacion) {
        StringBuilder sql = new StringBuilder();
        //para paginacion
        sql.append("SELECT * ");
        sql.append("FROM ( SELECT A.*, ROWNUM row_number ");
        sql.append("FROM ( ");
        //para paginacion
        sql.append("SELECT NU_ANN,NU_EMI,TI_CAP,NU_COR_DES,TO_CHAR(FE_EMI,'DD/MM/YY') FE_EMI_CORTA,FE_EMI,DE_ORI_EMI,DE_TIP_DOC_ADM,NU_DOC,DE_EMP_DES,DE_ASU_M,NU_EXPEDIENTE,DE_ES_DOC_DES,");
        sql.append("'1' EXISTE_DOC,TD_PK_TRAMITE.FU_DOC_ANE(NU_ANN,NU_EMI) EXISTE_ANEXO,NU_DES ");
        sql.append("FROM TDVV_DESTINOS_ADM ");
        sql.append("WHERE ");
        sql.append(" NU_ANN = NVL(?/*:B_01_ANN.DE_ANIO*/, NU_ANN)");
        sql.append(" AND CO_DEP_DES = ?/*:GLOBAL.VCOD_DEP*/ ");
        sql.append(" AND CO_TIP_DOC_ADM = NVL(?/*:B_01_ANN.DE_DOC_DEP*/,CO_TIP_DOC_ADM)");
        sql.append(" AND ES_DOC_REC = DECODE(?/*:B_01_ANN.DE_ESTADOS*/, NULL, ES_DOC_REC, ?/*:B_01_ANN.DE_ESTADOS*/)");
        sql.append(" AND CO_PRI     = DECODE(?/*:B_01_ANN.DE_PRI*/, NULL, CO_PRI, ?/*:B_01_ANN.DE_PRI*/) ");
        sql.append(" AND (  CO_EMP_DES = DECODE(?/*:GLOBAL.TI_ACCESO*/, 0, CO_EMP_DES, ?/*:GLOBAL.USER*/)");
        sql.append("	OR (   CO_EMP_DES IS NULL ");
        sql.append("	   AND TI_DES ='01'");
        sql.append("	   )");
        sql.append("	)");
        sql.append(" AND nvl(TI_EMI_REF,'0') = NVL(?/*:B_01_ANN.TI_EMI_REF*/, nvl(TI_EMI_REF,'0'))");
        sql.append(" AND NVL(CO_EMP_DES,'NULO') = NVL(?/*:B_01_ANN.TI_EMP_DES*/, NVL(CO_EMP_DES,'NULO'))");
        sql.append(" AND (  (   NVL(?/*:B_01_ANN.LI_EXPE*/, 'NULO') = 'SINEX'");
        sql.append("	   AND CO_EXP IS NULL");
        sql.append("	   )");
        sql.append("	OR (   NVL(?/*:B_01_ANN.LI_EXPE*/, 'NULO') != 'NULO'");
        sql.append("	   AND CO_EXP = ?/*:B_01_ANN.LI_EXPE*/");
        sql.append("	   )");
        sql.append("	OR ?/*:B_01_ANN.LI_EXPE*/ IS NULL");
        sql.append("	)");
        sql.append(" ORDER BY FE_EMI DESC ");
        //para paginacion
        sql.append(") A ");
        sql.append("WHERE ROWNUM < ((? * ?) + 1 ) ");
        sql.append(") ");
        sql.append("WHERE row_number >= (((?-1) * ?) + 1) ");
        //para paginacion
        List<DocumentoBean> list = new ArrayList<DocumentoBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class),
                    new Object[]{buscarDocumentoRecep.getsCoAnnio(), buscarDocumentoRecep.getsCoDependencia(), buscarDocumentoRecep.getsTipoDoc(),
                        buscarDocumentoRecep.getsEstadoDoc(), buscarDocumentoRecep.getsEstadoDoc(), buscarDocumentoRecep.getsPrioridadDoc(), buscarDocumentoRecep.getsPrioridadDoc(),
                        buscarDocumentoRecep.getsTiAcceso(), buscarDocumentoRecep.getsCoEmpleado(), buscarDocumentoRecep.getsRemitente(), buscarDocumentoRecep.getsDestinatario(),
                        buscarDocumentoRecep.getsExpediente(), buscarDocumentoRecep.getsExpediente(), buscarDocumentoRecep.getsExpediente(), buscarDocumentoRecep.getsExpediente(),
                        paginacion.getNumeroDePagina(), paginacion.getRegistrosPorPagina(), paginacion.getNumeroDePagina(), paginacion.getRegistrosPorPagina()});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int getRowDocumentosRecepAdm(BuscarDocumentoRecepBean buscarDocumentoRecep) {
        int result = -1;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) ");
        sql.append("FROM TDVV_DESTINOS_ADM ");
        sql.append("WHERE ");
        sql.append(" NU_ANN = NVL(?/*:B_01_ANN.DE_ANIO*/, NU_ANN)");
        sql.append(" AND CO_DEP_DES = ?/*:GLOBAL.VCOD_DEP*/ ");
        sql.append(" AND CO_TIP_DOC_ADM = NVL(?/*:B_01_ANN.DE_DOC_DEP*/,CO_TIP_DOC_ADM)");
        sql.append(" AND ES_DOC_REC = DECODE(?/*:B_01_ANN.DE_ESTADOS*/, NULL, ES_DOC_REC, ?/*:B_01_ANN.DE_ESTADOS*/)");
        sql.append(" AND CO_PRI     = DECODE(?/*:B_01_ANN.DE_PRI*/, NULL, CO_PRI, ?/*:B_01_ANN.DE_PRI*/) ");
        sql.append(" AND (  CO_EMP_DES = DECODE(?/*:GLOBAL.TI_ACCESO*/, 0, CO_EMP_DES, ?/*:GLOBAL.USER*/)");
        sql.append("	OR (   CO_EMP_DES IS NULL ");
        sql.append("	   AND TI_DES ='01'");
        sql.append("	   )");
        sql.append("	)");
        sql.append(" AND nvl(TI_EMI_REF,'0') = NVL(?/*:B_01_ANN.TI_EMI_REF*/, nvl(TI_EMI_REF,'0'))");
        sql.append(" AND NVL(CO_EMP_DES,'NULO') = NVL(?/*:B_01_ANN.TI_EMP_DES*/, NVL(CO_EMP_DES,'NULO'))");
        sql.append(" AND (  (   NVL(?/*:B_01_ANN.LI_EXPE*/, 'NULO') = 'SINEX'");
        sql.append("	   AND CO_EXP IS NULL");
        sql.append("	   )");
        sql.append("	OR (   NVL(?/*:B_01_ANN.LI_EXPE*/, 'NULO') != 'NULO'");
        sql.append("	   AND CO_EXP = ?/*:B_01_ANN.LI_EXPE*/");
        sql.append("	   )");
        sql.append("	OR ?/*:B_01_ANN.LI_EXPE*/ IS NULL");
        sql.append("	)");

        try {
            result = this.jdbcTemplate.queryForInt(sql.toString(), new Object[]{buscarDocumentoRecep.getsCoAnnio(), buscarDocumentoRecep.getsCoDependencia(), buscarDocumentoRecep.getsTipoDoc(),
                buscarDocumentoRecep.getsEstadoDoc(), buscarDocumentoRecep.getsEstadoDoc(), buscarDocumentoRecep.getsPrioridadDoc(), buscarDocumentoRecep.getsPrioridadDoc(),
                buscarDocumentoRecep.getsTiAcceso(), buscarDocumentoRecep.getsCoEmpleado(), buscarDocumentoRecep.getsRemitente(), buscarDocumentoRecep.getsDestinatario(),
                buscarDocumentoRecep.getsExpediente(), buscarDocumentoRecep.getsExpediente(), buscarDocumentoRecep.getsExpediente(), buscarDocumentoRecep.getsExpediente()});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*comentado ini*/
//	@Override
//	public List<DocumentoBean> _getDocumentosRecepAdm(BuscarDocumentoRecepBean buscarDocumentoRecep){
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT NU_ANN,NU_EMI,TI_CAP,NU_COR_DES,TO_CHAR(FE_EMI,'DD/MM/YY') FE_EMI_CORTA,FE_EMI,DE_ORI_EMI,DE_TIP_DOC_ADM,NU_DOC,DE_EMP_DES,DE_ASU_M,NU_EXPEDIENTE,DE_ES_DOC_DES ");
//        sql.append("FROM TDVV_DESTINOS_ADM ");
//        sql.append("WHERE ");
//        if(!buscarDocumentoRecep.getsCoAnnio().equals("")){
//           sql.append("NU_ANN = '");
//           sql.append(buscarDocumentoRecep.getsCoAnnio());
//           sql.append("' ");
//        }else{
//           sql.append("NU_ANN = NU_ANN ");
//        }
//        sql.append("AND CO_DEP_DES = '"); 
//        sql.append(buscarDocumentoRecep.getsCoDependencia());
//        sql.append("' ");
//        if(!buscarDocumentoRecep.getsTipoDoc().equals("")){
//           sql.append("AND CO_TIP_DOC_ADM = '"); 
//           sql.append(buscarDocumentoRecep.getsTipoDoc());
//           sql.append("' ");           
//        }else{
//            sql.append("AND CO_TIP_DOC_ADM = CO_TIP_DOC_ADM ");
//        }
//        if(!buscarDocumentoRecep.getsEstadoDoc().equals("")){
//           sql.append("AND ES_DOC_REC = '"); 
//           sql.append(buscarDocumentoRecep.getsEstadoDoc());
//           sql.append("' ");           
//        }else{
//            sql.append("AND ES_DOC_REC = ES_DOC_REC ");
//        }
//        if(!buscarDocumentoRecep.getsPrioridadDoc().equals("")){
//           sql.append("AND CO_PRI = '"); 
//           sql.append(buscarDocumentoRecep.getsPrioridadDoc());
//           sql.append("' ");           
//        }else{
//            sql.append("AND CO_PRI = CO_PRI ");
//        }        
//        if(!buscarDocumentoRecep.getsPrioridadDoc().equals("")){
//           sql.append("AND CO_PRI = '"); 
//           sql.append(buscarDocumentoRecep.getsPrioridadDoc());
//           sql.append("' ");           
//        }else{
//            sql.append("AND CO_PRI = CO_PRI ");
//        } 
//        sql.append("AND (  CO_EMP_DES = DECODE('0'/*:GLOBAL.TI_ACCESO*/, 0, CO_EMP_DES, '");
//        sql.append(buscarDocumentoRecep.getsCoEmpleado()/*:GLOBAL.USER*/);
//        sql.append("') ");
//        sql.append("OR (CO_EMP_DES IS NULL AND TI_DES ='01')) ");
//        if(!buscarDocumentoRecep.getsRemitente().equals("")){
//            sql.append("AND nvl(TI_EMI_REF,'0') = '");
//            sql.append(buscarDocumentoRecep.getsRemitente());
//            sql.append("' ");
//        }else{
//            sql.append("AND nvl(TI_EMI_REF,'0') = nvl(TI_EMI_REF,'0') ");
//        }
//        if(!buscarDocumentoRecep.getsDestinatario().equals("")){
//            sql.append("AND nvl(CO_EMP_DES,'NULO') = '");
//            sql.append(buscarDocumentoRecep.getsDestinatario());
//            sql.append("' ");
//        }else{
//            sql.append("AND nvl(CO_EMP_DES,'NULO') = nvl(CO_EMP_DES,'NULO') ");
//        }
//        if(!buscarDocumentoRecep.getsExpediente().equals("")){
//            sql.append("AND (  (   '");
//            sql.append(buscarDocumentoRecep.getsExpediente());
//            sql.append("' = 'SINEX' ");
//            sql.append("AND CO_EXP IS NULL ");
//            sql.append(") OR (   '");
//            sql.append(buscarDocumentoRecep.getsExpediente());
//            sql.append("'!= 'NULO' ");
//            sql.append("AND CO_EXP = '");
//            sql.append(buscarDocumentoRecep.getsExpediente());
//            sql.append("') ");       
//            sql.append("OR '"); 
//            sql.append(buscarDocumentoRecep.getsExpediente());        
//            sql.append("' IS NULL");
//            sql.append(") ");              
//        }else{
//           sql.append("AND (  (   'NULO' = 'SINEX' ");
//           sql.append("AND CO_EXP IS NULL ");
//           sql.append(") OR (   'NULO' != 'NULO' ");
//           sql.append("AND CO_EXP = NULL ");
//           sql.append(") OR NULL IS NULL) ");
//        }
//        sql.append("ORDER BY FE_EMI DESC ");
//        
//        List<DocumentoBean> list = new ArrayList<DocumentoBean>();
//        try {
//            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class));
//        }catch (EmptyResultDataAccessException e) {
//            list = null;
//        }catch (Exception e) {
//            list = null;
//            e.printStackTrace();
//        }
//        return list;
//    }
    /*comentado fin*/
    public String getDesEstadoDocRecepcion(String sesDocRec) {
        String result = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select IDOSGD.PK_SGD_DESCRIPCION_ESTADOS(?,'TDTV_DESTINOS')");

        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{sesDocRec});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String validarAnulacionDocRecepcion(String pnuAnn, String pnuEmi, String pnuDes) {
        String vReturn = "NO_OK";
        StringBuilder sqlQry1 = new StringBuilder();
        sqlQry1.append("SELECT COUNT(re.nu_emi)\n"
                + "FROM IDOSGD.tdtr_referencia rf,\n"
                + "IDOSGD.tdtv_remitos   re\n"
                + "WHERE re.nu_ann = rf.nu_ann\n"
                + "AND re.nu_emi = rf.nu_emi\n"
                + "AND rf.nu_ann_ref = ?\n"
                + "AND rf.nu_emi_ref = ?\n"
                + "AND rf.nu_des_ref = ?\n"
                + "AND re.ES_DOC_EMI <> '9'");
        StringBuilder sqlQry2 = new StringBuilder();
        sqlQry2.append("SELECT COUNT(nu_emi)\n"
                + "FROM IDOSGD.tdtv_destinos\n"
                + "WHERE nu_ann = ?\n"
                + "AND nu_emi = ?\n"
                + "AND nu_des <> ?\n"
                + "AND es_doc_rec <> '0'\n"
                + "AND es_eli = '0'");
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("UPDATE IDOSGD.tdtv_remitos\n"
                + "SET es_doc_emi = '0' -- EMITIDO\n"
                + "WHERE nu_ann = ?\n"
                + "AND nu_emi = ?");
        try {
            String vResult = this.jdbcTemplate.queryForObject(sqlQry1.toString(), String.class, new Object[]{pnuAnn, pnuEmi, pnuDes});
            if (vResult != null && vResult.equals("0")) {
                vResult = this.jdbcTemplate.queryForObject(sqlQry2.toString(), String.class, new Object[]{pnuAnn, pnuEmi, pnuDes});
                if (vResult != null) {
                    if (vResult.equals("0")) {
                        this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{pnuAnn, pnuEmi});
                    }
                    vReturn = "OK";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn=e.getMessage();
        }
        return vReturn;
    }

    @Override
    public DocumentoBean existeDocumentoReferenciado(BuscarDocumentoRecepBean buscarDocumentoRecep) {
        StringBuilder sql = new StringBuilder();
        DocumentoBean documentoBean = null;
        sql.append("SELECT A.NU_ANN,A.NU_EMI,A.NU_COR_DOC FROM IDOSGD.TDTV_REMITOS A\n"
                + "WHERE A.NU_ANN=?\n"
                + "AND A.CO_DEP_EMI=?\n"
                + "AND A.TI_EMI='01'\n"
                + "AND A.CO_TIP_DOC_ADM=?\n"
                + "AND A.NU_DOC_EMI=?\n"
                + "AND A.ES_ELI='0'\n"
                + "AND A.ES_DOC_EMI NOT IN ('5','7','9')");

        try {
            documentoBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class),
                    new Object[]{buscarDocumentoRecep.getsCoAnnioBus(), buscarDocumentoRecep.getsBuscDestinatario(), buscarDocumentoRecep.getsDeTipoDocAdm(),
                        buscarDocumentoRecep.getsNumDocRef()});
            documentoBean.setMsjResult("OK");
        } catch (EmptyResultDataAccessException e) {
            documentoBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoBean;
    }

    @Override
    public List<DocumentoBean> getDocumentosReferenciadoBusq(DocumentoBean documentoBean, String sTipoAcceso, String pnuPagina, int pnuRegistros) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List<DocumentoBean> list = new ArrayList<DocumentoBean>();

        sql.append("SELECT Z.* ");
        sql.append("FROM ( ");
        sql.append("	SELECT  DISTINCT A.NU_ANN, ");
        sql.append("		A.NU_EMI, ");
        sql.append("		A.TI_CAP, ");
        sql.append("		B.NU_COR_DES, ");
        sql.append("		CONVERT(VARCHAR(10), A.FE_EMI, 3) FE_EMI_CORTA, ");
        sql.append("		A.FE_EMI, ");
        sql.append("		[IDOSGD].[PK_SGD_DESCRIPCION_TI_EMI_EMP](A.NU_ANN, A.NU_EMI) DE_ORI_EMI, ");
        sql.append("		[IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](A.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append("		C.NU_DOC, ");
        sql.append("		CASE B.TI_DES ");
        sql.append("                WHEN '01' THEN [IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](B.CO_EMP_DES) ");
        sql.append("                WHEN '02' THEN [IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](B.NU_RUC_DES) ");
        sql.append("                WHEN '03' THEN [IDOSGD].[PK_SGD_DESCRIPCION_ANI_SIMIL](B.NU_DNI_DES) ");
        sql.append("                WHEN '04' THEN [IDOSGD].[PK_SGD_DESCRIPCION_OTRO_ORIGEN](B.CO_OTR_ORI_DES) ");
        sql.append("		END DE_EMP_DES, ");
        sql.append("		UPPER (A.DE_ASU) DE_ASU_M, ");
        sql.append("		C.NU_EXPEDIENTE, ");
        sql.append("		[IDOSGD].[PK_SGD_DESCRIPCION_ESTADOS](B.ES_DOC_REC, 'TDTV_DESTINOS') DE_ES_DOC_DES, ");
        sql.append("		C.IN_EXISTE_DOC EXISTE_DOC, ");
        sql.append("		CASE ISNULL(C.TI_EMI_REF, '0') + ISNULL(C.IN_EXISTE_ANEXO, '2') ");
        sql.append("                WHEN '00' THEN 0 ");
        sql.append("                WHEN '02' THEN 0 ");
        sql.append("                ELSE 1 ");
        sql.append("		END EXISTE_ANEXO, ");
        sql.append("		B.NU_DES, ");
        sql.append("		ISNULL(B.CO_PRI, '1') CO_PRI, ");
        sql.append("		B.ES_DOC_REC, ");
        sql.append("		ROW_NUMBER() OVER (ORDER BY A.FE_EMI DESC) AS ROWNUM ");
        sql.append("	 FROM	[IDOSGD].[PK_SGD_DESCRIPCION_ARBOL_SEG](:pCoAnio, :pNuEmi) D, ");
        sql.append("		IDOSGD.TDTV_REMITOS A, ");
        sql.append("		IDOSGD.TDTV_DESTINOS B, ");
        sql.append("		IDOSGD.TDTX_REMITOS_RESUMEN C ");
        sql.append("	 WHERE D.NU_ANN = A.NU_ANN ");
        sql.append("	 AND D.NU_EMI = A.NU_EMI ");
        sql.append("	 AND B.NU_ANN = A.NU_ANN ");
        sql.append("	 AND B.NU_EMI = A.NU_EMI ");
        sql.append("	 AND C.NU_ANN = B.NU_ANN ");
        sql.append("	 AND C.NU_EMI = B.NU_EMI ");
        sql.append("	 AND B.CO_DEP_DES = :pCoDepDes ");
        if (sTipoAcceso.equals("1")) {
            sql.append(" AND B.CO_EMP_DES = :pcoEmpDes ");
            objectParam.put("pcoEmpDes", documentoBean.getCoEmpDes());
        }
        sql.append("	AND A.ES_ELI = '0' ");
        sql.append("	AND B.ES_ELI = '0' ");
        sql.append("	AND A.ES_DOC_EMI NOT IN ('5', '9', '7') ");
        sql.append("	AND A.IN_OFICIO = '0' ");
        
        objectParam.put("pCoAnio", documentoBean.getNuAnn());
        objectParam.put("pNuEmi", documentoBean.getNuEmi());
        objectParam.put("pCoDepDes", documentoBean.getCoDepDes());
        
        sql.append(") Z ");
        sql.append("WHERE Z.ROWNUM < 101 ");
        sql.append("ORDER BY Z.FE_EMI DESC ");
        
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String getVerificaAtendido(String pnuAnn, String pnuEmi, String pnuDes) {
        String vResult = "0";
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(a.nu_ann) nu_cantidad \n"
                + "FROM IDOSGD.tdtr_referencia a, IDOSGD.tdtv_remitos b \n"
                + "WHERE a.nu_ann = b.nu_ann \n"
                + "and a.nu_emi = b.nu_emi \n"
                + "and b.es_eli = '0' \n"
                + "and b.es_doc_emi <> '9' \n"
                + "and a.nu_ann_ref = ?\n"
                + "and a.nu_emi_ref = ? \n"
                + "and a.nu_des_ref = ?");

        try {
            vResult = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pnuAnn, pnuEmi, pnuDes});
        } catch (Exception e) {
            vResult = "0";
            e.printStackTrace();
        }
        return vResult;
    }

    @Override
    public List<ReferenciaBean> getConsultaAtendido(String pnuAnn, String pnuEmi, String pnuDes) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  [IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](b.co_tip_doc_adm) li_tip_doc, ");
        sql.append("        CASE b.ti_emi ");
        sql.append("		WHEN '01' THEN b.nu_doc_emi + '-' + b.nu_ann + '-' + b.de_doc_sig ");
        sql.append("		WHEN '05' THEN b.nu_doc_emi + '-' + b.nu_ann + '-' + b.de_doc_sig ");
        sql.append("		ELSE b.de_doc_sig ");
        sql.append("        END li_nu_doc, ");
        sql.append("        CONVERT(VARCHAR(10), b.fe_emi, 103) fe_emi_corta, ");
        sql.append("        b.nu_ann, ");
        sql.append("        b.nu_emi, ");
        sql.append("        CAST(b.nu_cor_emi AS VARCHAR) nu_cor_emi, ");
        sql.append("        [IDOSGD].[PK_SGD_DESCRIPCION_ESTADOS](b.es_doc_emi,'TDTV_REMITOS') DE_ES_DOC_EMI, ");
        sql.append("        a.nu_ann_ref, ");
        sql.append("        a.nu_emi_ref ");
        sql.append("FROM IDOSGD.tdtr_referencia a, ");
        sql.append("	 IDOSGD.tdtv_remitos b ");
        sql.append("WHERE a.nu_ann = b.nu_ann ");
        sql.append("and a.nu_emi = b.nu_emi ");
        sql.append("and b.es_eli = '0' ");
        sql.append("and b.es_doc_emi <> '9' ");
        sql.append("and a.nu_ann_ref = ? ");
        sql.append("and a.nu_emi_ref = ? ");
        sql.append("and a.nu_des_ref = ? ");
        sql.append("order by b.nu_cor_emi desc ");

        List<ReferenciaBean> list = new ArrayList<ReferenciaBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ReferenciaBean.class), new Object[]{pnuAnn, pnuEmi, pnuDes});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;

    }

    @Override
    public String updEtiquetaTipoRecepDocumento(DocumentoBean documentoBean) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE IDOSGD.TDTV_DESTINOS ");
        sql.append("SET ti_fisico_rec =?,co_etiqueta_rec =?");
        sql.append(" WHERE NU_ANN = ? ");
        sql.append("AND NU_EMI = ? ");
        sql.append("AND NU_DES = ? ");
        String vReturn = "NO_OK";
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                documentoBean.getTiFisicoRec(),documentoBean.getCoEtiquetaRec(),
                documentoBean.getNuAnn(), documentoBean.getNuEmi(), documentoBean.getNuDes()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public DocumentoBean getEstadoDocumento(String nuAnn, String nuEmi, String nuDes) {
        StringBuilder sql = new StringBuilder();
        DocumentoBean documentoBean = null;
        sql.append("SELECT CO_EMP_DES,ES_DOC_REC,NU_ANN,NU_EMI,NU_DES,TI_DES,CO_DEP_DES\n" +
        "FROM IDOSGD.TDTV_DESTINOS\n" +
        "WHERE NU_ANN = ?\n" +
        "AND NU_EMI = ? AND NU_DES = ? AND ES_ELI='0'");        
        
        try {
             documentoBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class),
                    new Object[]{nuAnn,nuEmi,nuDes});
        } catch (EmptyResultDataAccessException e) {
            documentoBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoBean;          
    }

    @Override
    public String getEstadoDocAdmBasico(String nuAnn, String nuEmi) {
        String vResult="NO_OK";
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ES_DOC_EMI\n" +
                    "FROM IDOSGD.TDTV_REMITOS\n" +
                    "WHERE nu_ann = ? \n" +
                    "AND nu_emi = ? \n" +
                    //"AND ES_DOC_EMI NOT IN ('5', '9', '7') AND ES_ELI='0'");        
                    "AND ES_ELI='0'");        
        
        try {
             vResult = this.jdbcTemplate.queryForObject(sql.toString(), String.class,
                    new Object[]{nuAnn,nuEmi});
        } catch (EmptyResultDataAccessException e) {
            vResult = "NO_OK";
        } catch (Exception e) {
            vResult = "NO_OK";
            e.printStackTrace();
        }
        return vResult;
    }

    @Override
    public String updAnulaRecepecionDocumentoBean(String nuAnn, String nuEmi, String nuDes, String coUseMod) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updRecepcionDocumentoBean(DocumentoBean documentoBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updAtencionDocumentoBean(DocumentoBean documentoBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DocumentoBean> getDocumentosBuscaRecepAdm2(BuscarDocumentoRecepBean buscarDocumentoRecep, String pnuPagina, int pnuRegistros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DocumentoBean> getDocumentosBuscaRecepAdm2Size(BuscarDocumentoRecepBean buscarDocumentoRecep) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DocumentoBean> getDocumentosReferenciadoBusqSize(DocumentoBean documentoBean, String sTipoAcceso) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String actualizarEstadoPlazoAtencion(DocumentoBean documentoBean, String inButtonRed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String actualizarEstadoPlazoAtencionSinDoc(DocumentoBean documentoBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
