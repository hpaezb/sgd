/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramiteconv.dao.impl.sqlserver;

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
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteDocExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.MotivoBean;
import pe.gob.onpe.tramitedoc.bean.ProcesoExpBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaDocExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteDocExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.RequisitoBean;
import pe.gob.onpe.tramitedoc.dao.MesaPartesDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
import pe.gob.onpe.tramitedoc.web.util.BusquedaTextual;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author ECueva
 */
@Repository("mesaPartesDao")
public class MesaPartesDaoImp extends SimpleJdbcDaoBase  implements MesaPartesDao{

    @Override
    public List<DocumentoExtRecepBean> getDocumentosExtRecep(BuscarDocumentoExtRecepBean buscarDocumentoExtRecepBean) {
        System.out.println("SQLSERVER");
        boolean bBusqFiltro = false;
        boolean bBusqDep = false;
        String sqlContains = "";
        StringBuilder sql = new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        sql.append("SELECT TOP 100  X.*, ");
        sql.append("        [IDOSGD].[PK_SGD_DESCRIPCION_TI_DESTINO](X.TI_EMI) DE_TI_EMI, ");
        sql.append("        CASE X.TI_EMI ");
        sql.append("            WHEN '01' THEN [IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](X.CO_DEP_EMI) + ' - ' + [IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](X.CO_EMP_EMI) ");
        sql.append("            WHEN '02' THEN [IDOSGD].[PK_SGD_DESCRIPCION_DE_PROVEEDOR](X.NU_RUC_EMI) ");
        sql.append("            WHEN '03' THEN [IDOSGD].[PK_SGD_DESCRIPCION_ANI_SIMIL](X.NU_DNI_EMI) ");
        sql.append("            WHEN '04' THEN [IDOSGD].[PK_SGD_DESCRIPCION_OTRO_ORIGEN](X.CO_OTR_ORI_EMI) ");
        sql.append("            WHEN '05' THEN [IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](X.CO_EMP_EMI) ");
        sql.append("        END DE_ORI_EMI_MP, ");
        sql.append("        [IDOSGD].[PK_SGD_DESCRIPCION_DE_DOCUMENTO](X.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append("        CASE X.TI_EMI ");
        sql.append("            WHEN '01' THEN X.NU_DOC_EMI + '-' + X.NU_ANN + '/' + X.DE_DOC_SIG ");
        sql.append("            WHEN '05' THEN X.NU_DOC_EMI + '-' + X.NU_ANN + '/' + X.DE_DOC_SIG ");
        sql.append("            ELSE X.DE_DOC_SIG ");
        sql.append("        END NU_DOC, ");
        sql.append("        CASE X.NU_CANDES ");
        sql.append("            WHEN 1 THEN (SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_TI_DES_EMP](X.NU_ANN, X.NU_EMI)) ");
        sql.append("            ELSE (SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_TI_DES_EMP_V](X.NU_ANN, X.NU_EMI)) ");
        sql.append("        END DE_EMP_DES, ");
        sql.append("        [IDOSGD].[PK_SGD_DESCRIPCION_ESTADOS_MP](X.ES_DOC_EMI, 'TDTV_REMITOS') DE_ES_DOC_EMI_MP, ");
        sql.append("        [IDOSGD].[PK_SGD_DESCRIPCION_DE_LOCAL](X.CO_LOC_EMI) DE_LOC_EMI, ");
        sql.append("        [IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](X.CO_DEP) DE_DEPENDENCIA ");
        sql.append("FROM ( ");
        sql.append("SELECT  A.NU_ANN, ");
        sql.append("        A.NU_EMI, ");
        sql.append("        B.NU_EXPEDIENTE, ");
        sql.append("        CONVERT(VARCHAR(10), A.FE_EMI, 103) FE_EMI_CORTA, ");
        sql.append("        A.DE_ASU, ");
        sql.append("        A.CO_DEP_EMI, ");
        sql.append("        A.CO_EMP_EMI, ");
        sql.append("        A.CO_OTR_ORI_EMI, ");
        sql.append("        A.CO_TIP_DOC_ADM, ");
        sql.append("        A.NU_DOC_EMI, ");
        sql.append("        A.DE_DOC_SIG, ");
        sql.append("        B.IN_EXISTE_DOC EXISTE_DOC, ");
        sql.append("        A.TI_EMI, ");
        sql.append("        A.NU_RUC_EMI, ");
        sql.append("        A.NU_DNI_EMI, ");
        sql.append("        A.NU_CANDES, ");
        sql.append("        A.ES_DOC_EMI, ");
        sql.append("        A.CO_LOC_EMI, ");
        sql.append("        A.CO_DEP, ");
        sql.append("        A.NU_COR_EMI, ");
        sql.append("        ROW_NUMBER() OVER (ORDER BY A.NU_COR_EMI) AS ROWNUM ");
        sql.append("FROM IDOSGD.TDTV_REMITOS A, "); 
        sql.append("	  IDOSGD.TDTX_REMITOS_RESUMEN B ");
        sql.append("WHERE B.NU_ANN=A.NU_ANN ");
        sql.append("AND B.NU_EMI=A.NU_EMI ");
       
        String pNuAnn = buscarDocumentoExtRecepBean.getCoAnnio();
        String pEsFiltroFecha = buscarDocumentoExtRecepBean.getEsFiltroFecha();
        if (!(pEsFiltroFecha.equals("1")&&pNuAnn.equals("0"))) {
            sql.append(" AND A.NU_ANN = :pNuAnn");
            // Parametros Basicos
            objectParam.put("pNuAnn", pNuAnn);
        }
        sql.append(" AND A.CO_GRU = :pCoGru");        
        objectParam.put("pCoGru", buscarDocumentoExtRecepBean.getCoGrupo());
//        sql.append(" AND A.ES_DOC_EMI<>9");
        sql.append(" AND A.TI_EMI<>'01'");
        sql.append(" AND A.ES_ELI='0'");
        sql.append(" AND A.CO_DEP_EMI = :pCoDepEmi");        
        objectParam.put("pCoDepEmi", buscarDocumentoExtRecepBean.getCoDepEmi());

        String pTipoBusqueda = buscarDocumentoExtRecepBean.getTipoBusqueda();
        if (pTipoBusqueda.equals("1") && buscarDocumentoExtRecepBean.isEsIncluyeFiltro()) {
            bBusqFiltro = true;
        }
        
        String auxTipoAcceso=buscarDocumentoExtRecepBean.getTiAcceso();
        String tiAcceso=auxTipoAcceso!=null&&(auxTipoAcceso.equals("0")||auxTipoAcceso.equals("1"))?auxTipoAcceso:"1";           
        if(tiAcceso.equals("1")){//acceso personal
            sql.append(" AND A.CO_EMP_RES = :pcoEmpRes ");
            objectParam.put("pcoEmpRes", buscarDocumentoExtRecepBean.getCoEmpleado());            
        }else {//acceso total
            if(buscarDocumentoExtRecepBean.getInMesaPartes().equals("0") /*&& buscarDocumentoExtRecepBean.getInCambioEst().equals("0")*/){
                bBusqDep = true;
                sql.append(" AND A.CO_DEP = :pCoDep");        
                objectParam.put("pCoDep", buscarDocumentoExtRecepBean.getCoDependencia());                   
            }
        }/*else if(tiAcceso.equals("0")){//acceso total
            if(!buscarDocumentoExtRecepBean.getInCambioEst().equals("1")){
                bBusqLocal = true;
                sql.append(" AND A.CO_LOC_EMI = :pcoLocEmi");
                objectParam.put("pcoLocEmi", buscarDocumentoExtRecepBean.getCoLocal());
            }
        }*/
        
        //Filtro
        if (pTipoBusqueda.equals("0") || bBusqFiltro) {
            if (buscarDocumentoExtRecepBean.getCoTipoDoc()!= null && buscarDocumentoExtRecepBean.getCoTipoDoc().trim().length() > 0) {
                sql.append(" AND A.CO_TIP_DOC_ADM = :pCoDocEmi ");
                objectParam.put("pCoDocEmi", buscarDocumentoExtRecepBean.getCoTipoDoc());
            }            
            if (buscarDocumentoExtRecepBean.getCoEstadoDoc()!= null && buscarDocumentoExtRecepBean.getCoEstadoDoc().trim().length() > 0) {
                sql.append(" AND A.ES_DOC_EMI = :pCoEsDocEmi ");
                objectParam.put("pCoEsDocEmi", buscarDocumentoExtRecepBean.getCoEstadoDoc());
            }
            if(buscarDocumentoExtRecepBean.getCoTipoEmisor()!= null && buscarDocumentoExtRecepBean.getCoTipoEmisor().trim().length() > 0){
                sql.append(" AND A.TI_EMI = :pCoTipoEmisor ");
                objectParam.put("pCoTipoEmisor", buscarDocumentoExtRecepBean.getCoTipoEmisor());                
            }
            /*if (buscarDocumentoExtRecepBean.getCoLocEmi()!= null && buscarDocumentoExtRecepBean.getCoLocEmi().trim().length() > 0 && !bBusqLocal) {
                sql.append(" AND A.CO_LOC_EMI = :pcoLocEmi ");
                objectParam.put("pcoLocEmi", buscarDocumentoExtRecepBean.getCoLocEmi());
            } */     
            if (buscarDocumentoExtRecepBean.getCoDepOriRec()!= null && buscarDocumentoExtRecepBean.getCoDepOriRec().trim().length() > 0 && !bBusqDep) {
                sql.append(" AND A.CO_DEP = :pCoDep");        
                objectParam.put("pCoDep", buscarDocumentoExtRecepBean.getCoDepOriRec());                
            }
            if (buscarDocumentoExtRecepBean.getEsFiltroFecha() != null
                    && (pEsFiltroFecha.equals("1") || pEsFiltroFecha.equals("3"))) {
                String vFeEmiIni = buscarDocumentoExtRecepBean.getFeEmiIni();
                String vFeEmiFin = buscarDocumentoExtRecepBean.getFeEmiFin();
                if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
                        && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                    sql.append(" AND CAST(A.FE_EMI AS DATE) between CAST(:pFeEmiIni AS DATE) AND CAST(:pFeEmiFin AS DATE) ");
                    objectParam.put("pFeEmiIni", vFeEmiIni);
                    objectParam.put("pFeEmiFin", vFeEmiFin);
                }
            }            
        }   
        
        //Busqueda
        if (pTipoBusqueda.equals("1")) {
            if (buscarDocumentoExtRecepBean.getBusNumDoc() != null && buscarDocumentoExtRecepBean.getBusNumDoc().trim().length() > 1) {
                sql.append(" AND B.NU_DOC LIKE '%'+:pnuDocEmi+'%' ");
                objectParam.put("pnuDocEmi", buscarDocumentoExtRecepBean.getBusNumDoc());
            }
            if (buscarDocumentoExtRecepBean.getBusNumExpediente() != null && buscarDocumentoExtRecepBean.getBusNumExpediente().trim().length() > 1) {
                sql.append(" AND B.NU_EXPEDIENTE LIKE '%'+:pnuExpediente+'%' ");
                objectParam.put("pnuExpediente", buscarDocumentoExtRecepBean.getBusNumExpediente());
            }
            if (buscarDocumentoExtRecepBean.getBusAsunto() != null && buscarDocumentoExtRecepBean.getBusAsunto().trim().length() > 1) {
                sql.append(" AND CONTAINS(A.*, :pBusquedaTextual) ");
                sqlContains = "SELECT RETORNO FROM [IDOSGD].PK_SGD_DESCRIPCION_QUERYFILTER('"+buscarDocumentoExtRecepBean.getBusAsunto()+"')";
            }            
        }        
        
        sql.append(")  AS X "); 
        sql.append("ORDER BY X.NU_COR_EMI DESC ");        
        
        List<DocumentoExtRecepBean> list = new ArrayList<DocumentoExtRecepBean>();

        try {
            //Obteniendo el parametro textual si es requerido
            if (sqlContains.length() > 0) {
                String cadena = this.jdbcTemplate.queryForObject(sqlContains, String.class);            
                objectParam.put("pBusquedaTextual", cadena);
            }
            list = this.namedParameterJdbcTemplate.query(sql.toString(),objectParam,BeanPropertyRowMapper.newInstance(DocumentoExtRecepBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public String insExpedienteBean(ExpedienteDocExtRecepBean expedienteBean) {
        String vReturn = "NO_OK";
        StringBuilder sqlQry1 = new StringBuilder();
        sqlQry1.append("select RIGHT(REPLICATE('0', 10) + CAST((ISNULL(MAX(nu_sec_exp), 0) + 1) AS VARCHAR), 10) ");
        sqlQry1.append("from IDOSGD.tdtc_expediente ");
        sqlQry1.append("where nu_ann_exp = ? ");      
        
        StringBuilder sqlIns = new StringBuilder();
        sqlIns.append("INSERT INTO IDOSGD.TDTC_EXPEDIENTE(\n" +
                        "nu_ann_exp,\n" +
                        "nu_sec_exp,\n" +
                        "fe_exp,\n" +
                        "fe_vence,\n" +
                        "co_proceso,\n" +
                        "de_detalle,\n" +
                        "co_dep_exp,\n" +
                        "co_gru,\n" +
                        "nu_corr_exp,\n" +
                        "nu_expediente,\n" +
                        "us_crea_audi,\n" +
                        "fe_crea_audi,\n" +
                        "us_modi_audi,\n" +
                        "fe_modi_audi,\n" +
                        "es_estado)\n" +
                        "VALUES(?,?,(SELECT CONVERT(DATETIME, ?, 103))," +
                        "(SELECT CONVERT(DATETIME, ?, 103)),?,?,?,'3',?,?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP,'0')");
        try{
            String feVence=expedienteBean.getFeVence();
            if(!(feVence!=null&&feVence.trim().length()>0)){
                feVence=null;
            }
            String snuSecExp = this.jdbcTemplate.queryForObject(sqlQry1.toString(), String.class, new Object[]{expedienteBean.getNuAnnExp()});            
            expedienteBean.setNuSecExp(snuSecExp);
            
            this.jdbcTemplate.update(sqlIns.toString(), new Object[]{expedienteBean.getNuAnnExp(), snuSecExp, expedienteBean.getFeExp(),
                feVence,expedienteBean.getCoProceso(), expedienteBean.getDeDetalle(), expedienteBean.getCoDepEmi(),
                expedienteBean.getNuCorrExp(), expedienteBean.getNuExpediente(),expedienteBean.getUsCreaAudi(), 
                expedienteBean.getUsCreaAudi()});
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Numero de Expediente Duplicado.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }
    
    @Override
    public String updExpedienteBean(ExpedienteDocExtRecepBean expedienteBean) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("update IDOSGD.TDTC_EXPEDIENTE \n"
                + "set US_MODI_AUDI=?\n"
                + ",FE_VENCE=(SELECT CONVERT(DATETIME, ?, 103))\n"
                + ",CO_PROCESO=?\n"
                + ",FE_MODI_AUDI=CURRENT_TIMESTAMP\n"
                + "where\n"
                + "NU_ANN_EXP=? and\n"
                + "NU_SEC_EXP=?");
        try {
            String feVence=expedienteBean.getFeVence();
            if(!(feVence!=null&&feVence.trim().length()>0)){
                feVence=null;
            }            
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{expedienteBean.getUsCreaAudi(),feVence,
            expedienteBean.getCoProceso(),expedienteBean.getNuAnnExp(),expedienteBean.getNuSecExp()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();

        }
        return vReturn;
    }    
    
    @Override
    public String updDocumentoExtBean(String nuAnn, String nuEmi, DocumentoExtRecepBean documentoExtRecepBean, ExpedienteDocExtRecepBean expedienteBean, RemitenteDocExtRecepBean remitenteDocExtRecepBean, String pcoUserMod){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("update IDOSGD.tdtv_remitos \n"
                + "set CO_USE_MOD=?,");
        if (documentoExtRecepBean != null) {
            documentoExtRecepBean.setNuAnn(nuAnn);
            sqlUpd.append("DE_ASU=?\n"
                    + ",DE_DOC_SIG=?\n"
                    + ",CO_TIP_DOC_ADM=?\n"
                    + ",NU_FOLIOS=?\n"
                    + ",NU_DIA_ATE=?,\n");

            //para verificar numero correlativo de documento
            if(documentoExtRecepBean.getNuCorDoc()==null || documentoExtRecepBean.getNuCorDoc().trim().equals("") ){
                String vnuCorDoc = getNroCorrelativoDocumento(nuAnn,documentoExtRecepBean.getCoDepEmi(),documentoExtRecepBean.getTiEmi());
                documentoExtRecepBean.setNuCorDoc(vnuCorDoc);
                sqlUpd.append("NU_COR_DOC=").append(vnuCorDoc).append(",\n");
            }
//            //Para verificar si ya tiene un numero correlativo de emision
//            if (documentoExtRecepBean.getNuCorEmi()==null || documentoExtRecepBean.getNuCorEmi().trim().equals("") ){
//                String vnuCorEmi = getNroCorrelativoEmision(documentoExtRecepBean.getNuAnn(),documentoExtRecepBean.getCoDepEmi());
//                documentoExtRecepBean.setNuCorEmi(vnuCorEmi);
//                sqlUpd.append("A.NU_COR_EMI=").append(vnuCorEmi).append(",\n");
//            }
        }
//        if (expedienteBean != null) {
//            sqlUpd.append("A.NU_ANN_EXP='").append(expedienteBean.getNuAnnExp()).append("',\n");
//            sqlUpd.append("A.NU_SEC_EXP='").append(expedienteBean.getNuSecExp()).append("',\n");
//        }
        if (remitenteDocExtRecepBean != null) {
            sqlUpd.append("TI_EMI='").append(remitenteDocExtRecepBean.getTiEmi()).append("',\n");
            if(remitenteDocExtRecepBean.getNuRuc()!=null&&remitenteDocExtRecepBean.getNuRuc().trim().length() > 0){
              sqlUpd.append("NU_RUC_EMI='" ).append(remitenteDocExtRecepBean.getNuRuc()).append("',\n");
            }else if(remitenteDocExtRecepBean.getNuDni()!=null&&remitenteDocExtRecepBean.getNuDni().trim().length() > 0){
              sqlUpd.append("NU_DNI_EMI='" ).append(remitenteDocExtRecepBean.getNuDni()).append("',\n");  
            }else if(remitenteDocExtRecepBean.getCoOtros()!=null&&remitenteDocExtRecepBean.getCoOtros().trim().length() > 0){
              sqlUpd.append("CO_OTR_ORI_EMI='" ).append(remitenteDocExtRecepBean.getCoOtros()).append("',\n");    
            }
        }
        sqlUpd.append("FE_USE_MOD=CURRENT_TIMESTAMP "
                + "where\n"
                + "NU_ANN=? and\n"
                + "NU_EMI=?");

        try {
            if (documentoExtRecepBean != null) {
                this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{pcoUserMod, documentoExtRecepBean.getDeAsu(), documentoExtRecepBean.getDeDocSig(),
                    documentoExtRecepBean.getCoTipDocAdm(), documentoExtRecepBean.getNuFolios(), documentoExtRecepBean.getNuDiaAte(),nuAnn, nuEmi});
            } else {
                this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{pcoUserMod, nuAnn, nuEmi});
            }
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Numero de Documento Duplicado";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }    

    @Override
    public String insDocumentoExtBean(DocumentoExtRecepBean documentoExtRecepBean, ExpedienteDocExtRecepBean expedienteBean, RemitenteDocExtRecepBean remitenteDocExtRecepBean) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("INSERT INTO IDOSGD.tdtv_remitos(\n"
                + "NU_ANN,\n"
                + "NU_EMI,\n"
                + "NU_COR_EMI,\n"
                + "CO_LOC_EMI,\n"
                + "CO_DEP_EMI,\n"
                + "CO_EMP_EMI,\n"
                + "CO_EMP_RES, \n"
                + "TI_EMI,\n"
                + "NU_DNI_EMI,\n"
                + "NU_RUC_EMI,\n"
                + "CO_OTR_ORI_EMI,\n"
                + "FE_EMI,\n"
                + "CO_GRU,\n"
                + "DE_ASU, \n"
                + "ES_DOC_EMI, \n"
                + "NU_DIA_ATE, \n"
                + "CO_TIP_DOC_ADM, \n"
                + "NU_COR_DOC, \n"
                + "DE_DOC_SIG, \n"
                + "NU_ANN_EXP, \n"
                + "NU_SEC_EXP, \n"
                + "NU_DET_EXP, \n"
                + "NU_FOLIOS, \n"
                + "ES_ELI,\n"
                + "CO_USE_CRE,\n"
                + "FE_USE_CRE,\n"
                + "CO_USE_MOD,\n"
                + "CO_DEP,\n"
                + "FE_USE_MOD)\n"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,(SELECT CONVERT(DATETIME, ?, 103)),'3',?,'5',?,?,?,?,?,?,'1',?,'0',?,CURRENT_TIMESTAMP,?,?,CURRENT_TIMESTAMP)");


        try {
            String coOtros=remitenteDocExtRecepBean.getCoOtros();
            if(!(coOtros!=null&&coOtros.trim().length()>0)){
                coOtros=null;
            }
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.dataSource).withProcedureName("[IDOSGD].[PK_SGD_UTILS_SEC_REMITOS_NU_EMI]")
                                            .withoutProcedureColumnMetaDataAccess()
                                            .declareParameters(new SqlOutParameter("COD_NU_EMI", Types.INTEGER));
                    
            Map out = simpleJdbcCall.execute();
            int codigo = ((Integer)out.get("COD_NU_EMI"));
            String snuEmi = String.format("%010d", codigo);

            documentoExtRecepBean.setNuEmi(snuEmi);

            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoExtRecepBean.getNuAnn(), snuEmi, expedienteBean.getNuCorrExp(),
                remitenteDocExtRecepBean.getCoLocEmi(), remitenteDocExtRecepBean.getCoDepEmi(), remitenteDocExtRecepBean.getCoEmpEmi(), remitenteDocExtRecepBean.getCoEmpRes(),
                remitenteDocExtRecepBean.getTiEmi(),remitenteDocExtRecepBean.getNuDni(),remitenteDocExtRecepBean.getNuRuc(),coOtros,expedienteBean.getFeExp(),
                documentoExtRecepBean.getDeAsu(),documentoExtRecepBean.getNuDiaAte(), documentoExtRecepBean.getCoTipDocAdm(),documentoExtRecepBean.getNuCorDoc(), documentoExtRecepBean.getDeDocSig(),
                expedienteBean.getNuAnnExp(), expedienteBean.getNuSecExp(),documentoExtRecepBean.getNuFolios(), documentoExtRecepBean.getCoUseMod(), documentoExtRecepBean.getCoUseMod(), remitenteDocExtRecepBean.getCoDep()});

            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Numero de Documento Duplicado.";
        } catch (Exception e) {
            e.printStackTrace();
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
                /*+ "CO_OTR_ORI_DES, \n"
                + "NU_DNI_DES, \n"
                + "NU_RUC_DES, \n"*/
                + "ES_ELI,\n"
                + "FE_USE_CRE,\n"
                + "FE_USE_MOD,\n"
                + "CO_USE_MOD,\n"
                + "CO_USE_CRE,\n"
                + "ES_DOC_REC,\n"
                + "ES_ENV_POR_TRA)\n"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,'0',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,?,'0','0')");

        try {
            
            String vnuDes = this.jdbcTemplate.queryForObject("select ISNULL(MAX(a.nu_des) + 1,1) FROM IDOSGD.tdtv_destinos a where \n" +
                                                            "A.NU_ANN=? and\n" +
                                                            "A.NU_EMI=?", String.class, new Object[]{nuAnn,nuEmi});
            destinatarioDocumentoEmiBean.setNuDes(vnuDes);
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{nuAnn, nuEmi, vnuDes, destinatarioDocumentoEmiBean.getCoLocal(), destinatarioDocumentoEmiBean.getCoDependencia(), destinatarioDocumentoEmiBean.getCoTipoDestino(),
                destinatarioDocumentoEmiBean.getCoEmpleado(), destinatarioDocumentoEmiBean.getCoPrioridad(), destinatarioDocumentoEmiBean.getDeIndicaciones(), destinatarioDocumentoEmiBean.getCoTramite(),
                /*destinatarioDocumentoEmiBean.getCoOtroOrigen(), destinatarioDocumentoEmiBean.getNuDni(), destinatarioDocumentoEmiBean.getNuRuc(),*/
                destinatarioDocumentoEmiBean.getCoUseCre(), destinatarioDocumentoEmiBean.getCoUseCre()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();

        }
        return vReturn;
    }
    
    @Override
    public String insReferenciaDocumentoEmi(String pnuAnn, String pnuEmi, ReferenciaDocExtRecepBean ref){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("INSERT INTO IDOSGD.tdtr_referencia(\n"
                + "NU_ANN,\n"
                + "NU_EMI,\n"
                + "CO_REF,\n"
                + "NU_ANN_REF,\n"
                + "NU_EMI_REF,\n"
                + "NU_DES_REF,\n"
                + "CO_USE_CRE,\n"
                + "FE_USE_CRE)\n"
                + "VALUES(?,?,?,?,?,?,?,CURRENT_TIMESTAMP)");
        
        /*StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("select lpad(SEC_REFERENCIA_CO_REF.NextVal, 10, '0') from dual");        */

        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.dataSource).withProcedureName("[IDOSGD].[PK_SGD_UTILS_SEC_REFERENCIA_CO_REF]")
                                            .withoutProcedureColumnMetaDataAccess()
                                            .declareParameters(new SqlOutParameter("COD_NU_ANN_REF", Types.INTEGER));            
            Map out = simpleJdbcCall.execute();
            int codigo = ((Integer)out.get("COD_NU_ANN_REF"));
            String cod_num_ann_ref = String.format("%010d", codigo);                
            
            //String scoRef = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class);
            ref.setCoRef(cod_num_ann_ref);
            String nuDes=ref.getNuDes();
            if(!(nuDes!=null&&nuDes.trim().length()>0)){
                nuDes=null;
            }
            
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{pnuAnn, pnuEmi, cod_num_ann_ref, ref.getNuAnn(), ref.getNuEmi(),nuDes,
                ref.getCoUseCre()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }    
    
    @Override
    public String getNroCorrelativoDocumento(String pnuAnn, String pcoDepEmi, String ptiEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("select ISNULL(MAX(nu_cor_doc), 0) + 1 "); 
        sqlQry.append("from IDOSGD.tdtv_remitos ");
        sqlQry.append("where nu_ann=? ");
        sqlQry.append("and co_dep_emi=? ");
        sqlQry.append("AND TI_EMI=? ");
        try {
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{pnuAnn,pcoDepEmi,ptiEmi});
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = "NO_OK";
        }
        return vReturn;
    }
    
    @Override
    public DocumentoExtRecepBean getDocumentoExtRecepNew(String coDependencia) {
        StringBuilder sql = new StringBuilder();
        sql.append("select  SUBSTRING((SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_DE_SIGLA_CORTA(CO_DEPENDENCIA)), 1, 6) DE_DOC_SIG_G, ");
        sql.append("        [IDOSGD].[PK_SGD_DESCRIPCION_ESTADOS_MP]('5', 'TDTV_REMITOS') DE_ES_DOC_EMI_MP, ");
        sql.append("        CO_EMPLEADO co_emp_emi ");
        sql.append("from IDOSGD.rhtm_dependencia ");
        sql.append("where CO_DEPENDENCIA = ? ");

        DocumentoExtRecepBean documentoExtRecepBean = null;
        try {
            documentoExtRecepBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoExtRecepBean.class),
                    new Object[]{coDependencia});
        } catch (EmptyResultDataAccessException e) {
            documentoExtRecepBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoExtRecepBean;
    }    
    
    @Override
    public List<MotivoBean> getLstMotivoxTipoDocumento(){
        StringBuilder sql = new StringBuilder();
        List<MotivoBean> list = new ArrayList<MotivoBean>();

        sql.append("SELECT  DE_MOT, ");
        sql.append("        CO_MOT "); 
        sql.append("FROM IDOSGD.TDTR_MOTIVO "); 
        sql.append("order by 1 ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(MotivoBean.class),
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
    public List<ReferenciaDocExtRecepBean> getLstDocExtReferencia(String pnuAnn,String pcoTiDoc,String pcoDepEmi,String pnuExpediente){
        StringBuilder sql = new StringBuilder();
        List<ReferenciaDocExtRecepBean> list = new ArrayList<ReferenciaDocExtRecepBean>();

        sql.append("SELECT  A.NU_ANN, ");
        sql.append("        A.NU_EMI, ");
        sql.append("        A.CO_TIP_DOC_ADM CO_TIP_DOC, ");
        sql.append("        B.NU_EXPEDIENTE, ");
        sql.append("        CONVERT(VARCHAR(10), B.FE_EXP, 3) FE_EXP, ");
        sql.append("        SUBSTRING(a.de_asu, 1, 59) de_asu,a.de_doc_sig ");
        sql.append("FROM IDOSGD.TDTV_REMITOS A, ");
        sql.append("	 IDOSGD.TDTC_EXPEDIENTE B ");
        sql.append("WHERE A.NU_ANN_EXP=B.NU_ANN_EXP AND ");
        sql.append("A.NU_SEC_EXP=B.NU_SEC_EXP AND ");
        sql.append("A.NU_COR_EMI=B.NU_CORR_EXP AND ");
        sql.append("A.NU_ANN=? AND ");
        sql.append("A.CO_TIP_DOC_ADM=? AND ");
        sql.append("A.CO_DEP_EMI=? AND ");
        sql.append("B.NU_CORR_EXP = ? AND ");
        sql.append("A.ES_DOC_EMI NOT IN ('9','7','5')AND ");
        sql.append("A.TI_EMI NOT IN('01','05') AND ");
        sql.append("A.CO_GRU = '3' AND ");
        sql.append("A.NU_DET_EXP =1 AND ");
        sql.append("A.ES_ELI='0' AND ");
        sql.append("B.ES_ESTADO='0' ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ReferenciaDocExtRecepBean.class),
                    new Object[]{pnuAnn,pcoTiDoc,pcoDepEmi,pnuExpediente});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public String getNroCorrelativoEmision(String pnuAnn, String pcoDepEmi) {
        String vReturn = "1";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT ISNULL(MAX(a.NU_COR_EMI), 0) + 1 ");
        sqlQry.append("FROM IDOSGD.TDTV_REMITOS a ");
        sqlQry.append("WHERE NU_ANN = ? ");
        sqlQry.append("AND CO_DEP_EMI=? ");
        sqlQry.append("AND co_gru ='3' ");
        try {
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{pnuAnn,pcoDepEmi});
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = "1";
        }
        return vReturn;
    }
    
    @Override
    public String updDestinatarioDocumentoEmi(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean) {
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
                + "where\n"
                + "NU_ANN=? and\n"
                + "NU_EMI=? and\n"
                + "NU_DES=?");
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{destinatarioDocumentoEmiBean.getCoLocal(), destinatarioDocumentoEmiBean.getCoDependencia(),
                destinatarioDocumentoEmiBean.getCoEmpleado(), destinatarioDocumentoEmiBean.getCoPrioridad(), destinatarioDocumentoEmiBean.getDeIndicaciones(), destinatarioDocumentoEmiBean.getCoTramite(),
                destinatarioDocumentoEmiBean.getCoOtroOrigen(), destinatarioDocumentoEmiBean.getNuDni(), destinatarioDocumentoEmiBean.getNuRuc(),
                destinatarioDocumentoEmiBean.getCoUseCre(), nuAnn, nuEmi, destinatarioDocumentoEmiBean.getNuDes()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }
    
    @Override
    public String delDestinatarioDocumentoEmi(String nuAnn, String nuEmi, String pnuDes){
        String vReturn = "NO_OK";
        StringBuilder sqlDel = new StringBuilder();
        sqlDel.append("delete from IDOSGD.TDTV_DESTINOS\n"
                + "WHERE NU_ANN = ?\n"
                + "AND NU_EMI = ?\n"
                + "and NU_DES=?");
        try {
            this.jdbcTemplate.update(sqlDel.toString(), new Object[]{nuAnn, nuEmi, pnuDes});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    } 
    
    @Override
    public String updReferenciaDocumentoEmi(String pnuAnn, String pnuEmi, ReferenciaDocExtRecepBean ref) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("update IDOSGD.tdtr_referencia \n"
                + "set NU_ANN_REF = ?, \n"
                + "NU_EMI_REF = ?, \n"
                + "NU_DES_REF = ?\n"
                + "WHERE NU_ANN = ? AND NU_EMI = ? \n"
                + "AND CO_REF = ?");
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{ref.getNuAnn(), ref.getNuEmi(), ref.getNuDes(),
                pnuAnn, pnuEmi, ref.getCoRef()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;        
    }
    
    @Override
    public String delReferenciaDocumentoEmi(String pnuAnn, String pnuEmi, String pnuAnnRef,String pnuEmiRef){
        String vReturn = "NO_OK";
        StringBuilder sqlIns = new StringBuilder();
        sqlIns.append("DELETE FROM IDOSGD.tdtr_referencia\n"
                + "WHERE NU_ANN = ? AND NU_EMI = ? \n"
                + "AND NU_ANN_REF = ? AND NU_EMI_REF = ?");

        try {
            this.jdbcTemplate.update(sqlIns.toString(), new Object[]{pnuAnn, pnuEmi, pnuAnnRef, pnuEmiRef});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;        
    }    

    @Override
    public DocumentoExtRecepBean getDocumentoExtRec(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.NU_ANN, ");
        sql.append("A.NU_EMI, ");
        sql.append("(SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_DE_SIGLA_CORTA](A.CO_DEP_EMI)) DE_DOC_SIG_G, ");
        sql.append("A.CO_LOC_EMI, ");
        sql.append("A.CO_EMP_EMI, ");
        sql.append("A.CO_DEP_EMI, ");
        sql.append("A.NU_COR_DOC, ");
        sql.append("B.NU_ANN_EXP, ");
        sql.append("B.NU_SEC_EXP, ");
        sql.append("(CONVERT(VARCHAR(10), B.FE_EXP, 103) + ' ' + CONVERT(VARCHAR(5), B.FE_EXP, 108)) FE_EXP, ");
        sql.append("CONVERT(VARCHAR(10), B.FE_VENCE, 103) FE_VENCE, ");
        sql.append("B.NU_CORR_EXP, ");
        sql.append("B.NU_EXPEDIENTE, ");
        sql.append("B.CO_PROCESO, ");
        sql.append("A.ES_DOC_EMI CO_ES_DOC_EMI_MP, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_ESTADOS_MP](A.ES_DOC_EMI, 'TDTV_REMITOS') DE_ES_DOC_EMI_MP, ");
        sql.append("A.TI_EMI, ");
        sql.append("A.NU_DNI_EMI NU_DNI, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_ANI_SIMIL](A.NU_DNI_EMI) DE_NU_DNI, ");
        sql.append("A.NU_RUC_EMI NU_RUC, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_PROVEEDOR](A.NU_RUC_EMI) DE_NU_RUC, ");
        sql.append("A.CO_OTR_ORI_EMI CO_OTROS, ");
        sql.append("A.CO_EMP_RES, ");
        sql.append("[IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](A.CO_EMP_RES) DE_EMP_RES, ");
        sql.append("A.CO_TIP_DOC_ADM, ");
        sql.append("A.DE_DOC_SIG, ");
        sql.append("A.NU_FOLIOS, ");
        sql.append("A.DE_ASU, ");
        sql.append("A.NU_DIA_ATE ");
        sql.append("FROM IDOSGD.TDTV_REMITOS A "); 
        sql.append("left join IDOSGD.TDTC_EXPEDIENTE B ");
        sql.append("ON A.NU_ANN_EXP = B.NU_ANN_EXP AND A.NU_SEC_EXP = B.NU_SEC_EXP ");
        sql.append("WHERE A.NU_ANN=? ");
        sql.append("AND A.NU_EMI=? ");

        DocumentoExtRecepBean documentoExtRecepBean = null;
        try {
            documentoExtRecepBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoExtRecepBean.class),
                    new Object[]{pnuAnn,pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            documentoExtRecepBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoExtRecepBean;
    }
    
    @Override
    public Map getDesFieldOtro(String pcoOtros){
        Map mapResult=null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  C.DE_APE_PAT_OTR + ' ' + C.DE_APE_MAT_OTR + ', ' + C.DE_NOM_OTR + ' - ' + C.DE_RAZ_SOC_OTR DE_NOM_OTROS, ");
        sql.append("        ISNULL(B.CELE_DESELE, '   ') DE_DOC_OTROS, ");
        sql.append("        C.NU_DOC_OTR_ORI NU_DOC_OTROS ");
        sql.append("FROM IDOSGD.TDTR_OTRO_ORIGEN C ");
        sql.append("	LEFT OUTER JOIN ");
        sql.append("   (SELECT CELE_CODELE, CELE_DESELE ");
        sql.append("    FROM IDOSGD.SI_ELEMENTO ");
        sql.append("   WHERE CTAB_CODTAB ='TIP_DOC_IDENT') B ");
        sql.append("   ON C.CO_TIP_OTR_ORI = B.CELE_CODELE ");
        sql.append("WHERE C.CO_OTR_ORI = ? ");
        
        try {
               mapResult = this.jdbcTemplate.queryForMap(sql.toString(), new Object[]{pcoOtros});    
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapResult;
    }
    
    @Override
    public List<DestinatarioDocumentoEmiBean> getLstDestinoEmiDoc(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        List<DestinatarioDocumentoEmiBean> list = null;
        sql.append("select  a.nu_ann, ");
        sql.append("        a.nu_emi, ");
        sql.append("        a.nu_des, ");
        sql.append("        a.co_loc_des co_local, ");
        sql.append("        CASE WHEN a.co_loc_des IS NULL THEN NULL ");
        sql.append("		 ELSE SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_LOCAL](a.co_loc_des), 1, 100) ");
        sql.append("        END de_local, ");
        sql.append("        a.co_dep_des co_dependencia, ");
        sql.append("        CASE WHEN a.co_dep_des IS NULL THEN NULL ");
        sql.append("		 ELSE SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](a.co_dep_des), 1, 100) ");
        sql.append("        END de_dependencia, ");
        sql.append("        a.co_emp_des co_empleado, ");
        sql.append("        CASE WHEN a.co_emp_des IS NULL THEN NULL ");
        sql.append("		 ELSE SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](a.co_emp_des), 1, 100) ");
        sql.append("        END de_empleado, ");
        sql.append("        a.co_mot co_tramite, ");
        sql.append("        CASE WHEN a.co_mot IS NULL THEN NULL ");
        sql.append("		 ELSE SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_MOTIVO](a.co_mot), 1, 100) ");
        sql.append("        END de_tramite, ");
        sql.append("        a.co_pri co_prioridad, ");
        sql.append("        a.de_pro de_indicaciones, ");
        sql.append("        a.ti_des co_tipo_destino, ");
        sql.append("        'BD' accionBD ");
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
    public List<ReferenciaDocExtRecepBean> getLstDocExtReferenciaEmitido(String pnuAnn,String pcoTiDoc,String pcoDepEmi,String pnuExpediente){
        StringBuilder sql = new StringBuilder();
        List<ReferenciaDocExtRecepBean> list = new ArrayList<ReferenciaDocExtRecepBean>();
        return list;        
    }
    
    @Override
    public List<ReferenciaDocExtRecepBean> getLstReferenciaDoc(String pnuAnn, String pnuEmi){
        StringBuilder sql = new StringBuilder();
        List<ReferenciaDocExtRecepBean> list;
        sql.append("SELECT  A.NU_ANN, ");
        sql.append("        A.NU_EMI, ");
        sql.append("        A.CO_TIP_DOC_ADM CO_TIP_DOC, ");
        sql.append("        SUBSTRING([IDOSGD].[PK_SGD_DESCRIPCION_DE_NU_EXPEDIENTE](A.NU_ANN_EXP, A.NU_SEC_EXP), 1, 20) NU_EXPEDIENTE, ");
        sql.append("        CONVERT(VARCHAR(10), A.FE_EMI, 3) FE_EXP, ");
        sql.append("        'BD' ACCION_BD, ");
        sql.append("        B.CO_REF ");
        sql.append("FROM IDOSGD.TDTV_REMITOS A, ");
        sql.append("	 IDOSGD.TDTR_REFERENCIA B ");
        sql.append("WHERE A.NU_ANN = B.NU_ANN_REF AND ");
        sql.append("A.NU_EMI = B.NU_EMI_REF AND ");
        sql.append("B.NU_ANN = ? AND ");
        sql.append("B.NU_EMI = ? AND ");
        sql.append("A.ES_DOC_EMI NOT IN ('9', '7', '5') AND ");
        sql.append("A.NU_DET_EXP =1 AND ");
        sql.append("A.ES_ELI='0' ");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ReferenciaDocExtRecepBean.class),
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
    public DocumentoExtRecepBean getDocumentoExtRecBasic(String pnuAnn, String pnuEmi){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  A.NU_ANN,A.NU_EMI, ");
        sql.append("        A.CO_LOC_EMI, ");
        sql.append("        A.CO_EMP_EMI, ");
        sql.append("        A.CO_DEP_EMI, ");
        sql.append("        A.NU_COR_DOC, ");
        sql.append("        A.ES_DOC_EMI CO_ES_DOC_EMI_MP, ");
        sql.append("        A.TI_EMI, ");
        sql.append("        A.CO_EMP_RES, ");
        sql.append("        A.CO_TIP_DOC_ADM, ");
        sql.append("        A.DE_DOC_SIG, ");
        sql.append("        A.NU_FOLIOS, ");
        sql.append("        A.NU_ANN_EXP, ");
        sql.append("        A.NU_SEC_EXP ");
        sql.append("FROM IDOSGD.TDTV_REMITOS A ");
        sql.append("WHERE A.NU_ANN=? ");
        sql.append("AND A.NU_EMI=? ");
        sql.append("AND CO_GRU='3' ");
        sql.append("AND ES_ELI='0' ");
        sql.append("AND NU_DET_EXP='1' ");
        sql.append("AND ES_DOC_EMI <> '9' ");

        DocumentoExtRecepBean documentoExtRecepBean = null;
        try {
            documentoExtRecepBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoExtRecepBean.class),
                    new Object[]{pnuAnn,pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            documentoExtRecepBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoExtRecepBean;
    }
    
    @Override
    public String updEstadoDocumentoExt(DocumentoExtRecepBean documentoExtRecepBean){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("update IDOSGD.tdtv_remitos \n"
                + "set CO_USE_MOD=? \n"
                + ",FE_USE_MOD=CURRENT_TIMESTAMP \n"
                + ",FE_EMI=CURRENT_TIMESTAMP \n"
                + ",ES_DOC_EMI=? \n"
                + "WHERE NU_ANN=? \n"
                + "AND NU_EMI=? ");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoExtRecepBean.getCoUseMod(),
                documentoExtRecepBean.getCoEsDocEmiMp(), documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi()});
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
    public String anularDocumentoExtRecep(DocumentoExtRecepBean documentoExtRecepBean){
        String vReturn = "NO_OK";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT ISNULL(MAX(nu_cor_doc), 1) + 1\n"
                + "FROM IDOSGD.tdtv_remitos\n"
                + "WHERE nu_ann         = ?\n"
                + "AND ti_emi         = ?\n"
                + "AND co_dep_emi     = ?\n"
                + "AND co_tip_doc_adm = ?");

        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("update IDOSGD.tdtv_remitos \n"
                + "set es_doc_emi = ?,\n"
                + "nu_cor_doc = ?,fe_use_mod=CURRENT_TIMESTAMP,co_use_mod=? \n"
                + "WHERE nu_ann = ?\n"
                + "AND nu_emi = ?\n"
                + "AND es_eli = '0'");

        try {
            String snuCorDoc = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getTiEmi(),
                documentoExtRecepBean.getCoDepEmi(), documentoExtRecepBean.getCoTipDocAdm()});

            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoExtRecepBean.getCoEsDocEmiMp(), snuCorDoc, documentoExtRecepBean.getCoUseMod(),
                documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();
        }
        return vReturn;
    }
    
    @Override
    public ReferenciaDocExtRecepBean getRefAtenderDocExtRec(String pnuAnn,String pnuEmi){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT	A.NU_ANN, ");
        sql.append("		A.NU_EMI, ");
        sql.append("		SUBSTRING(B.NU_EXPEDIENTE, 1, 20) NU_EXPEDIENTE, ");
        sql.append("		CONVERT(VARCHAR(10), B.FE_EXP, 3) FE_EXP, ");
        sql.append("		A.CO_TIP_DOC_ADM CO_TIP_DOC, ");
        sql.append("		'INS' ACCION_BD ");
        sql.append("FROM IDOSGD.TDTV_REMITOS A, ");
        sql.append("	 IDOSGD.TDTC_EXPEDIENTE B ");
        sql.append("WHERE A.NU_ANN = ? ");
        sql.append("AND A.NU_EMI = ? ");
        sql.append("AND B.NU_ANN_EXP=A.NU_ANN_EXP ");
        sql.append("AND B.NU_SEC_EXP=A.NU_SEC_EXP ");
        sql.append("AND A.ES_DOC_EMI NOT IN ('9', '7', '5') ");
        sql.append("AND A.ES_ELI='0' ");
        sql.append("AND B.ES_ESTADO='0' ");
        ReferenciaDocExtRecepBean referenciaDocExtRecepBean = null;
        try {
            referenciaDocExtRecepBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(ReferenciaDocExtRecepBean.class),
                    new Object[]{pnuAnn,pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            referenciaDocExtRecepBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return referenciaDocExtRecepBean;        
    }

    @Override
    public String getNumeroExpediente(ExpedienteDocExtRecepBean expedienteBean) {
        String vReturn = "NO_OK";
        StringBuilder sqlQry1 = new StringBuilder();
        sqlQry1.append("select	RIGHT(REPLICATE('0', 7) + (RTRIM(LTRIM(CAST((ISNULL(MAX(NU_CORR_EXP), 0) + 1) AS VARCHAR)))), 7) nuCorrExp, ");
        sqlQry1.append("	SUBSTRING((SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_DE_SIGLA_CORTA](?)), 1, 6) deSiglaCorta ");
        sqlQry1.append("from IDOSGD.tdtc_expediente ");
        sqlQry1.append("where nu_ann_exp = ? ");
        sqlQry1.append("and co_dep_exp = ? ");
        sqlQry1.append("and co_gru = '3' ");
        
        try{
            Map mapResult = this.jdbcTemplate.queryForMap(sqlQry1.toString(), new Object[]{expedienteBean.getCoDepEmi(), expedienteBean.getNuAnnExp(), expedienteBean.getCoDepEmi()});
            String snuCorrExp = String.valueOf(mapResult.get("nuCorrExp"));
            String sdeSiglaCorta = String.valueOf(mapResult.get("deSiglaCorta"));
            expedienteBean.setNuCorrExp(snuCorrExp);
            expedienteBean.setNuExpediente(Utilidades.AjustaCampoIzquierda(sdeSiglaCorta, 6, "0") + expedienteBean.getNuAnnExp() + snuCorrExp);
            
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }
    
    @Override
    public String updFechaExpedienteMP(String coUser, String nuAnnExp, String nuSecExp){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("UPDATE IDOSGD.TDTC_EXPEDIENTE \n"
                + "SET US_MODI_AUDI=? \n"
                + ",FE_MODI_AUDI=CURRENT_TIMESTAMP \n"
                + ",FE_EXP=SYSDATE \n"
                + "WHERE NU_ANN_EXP=? \n"
                + "AND NU_SEC_EXP=? ");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{coUser,
                nuAnnExp, nuSecExp});
            vReturn = "OK";
        }catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();
        }
        return vReturn;
    }
    
    @Override
    public String getPermisoChangeEstadoMP(String coEmp, String coDep){
        String vReturn="NO_OK";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT IN_CAMBIO_EST FROM IDOSGD.TDTR_PERMISO_MP\n" +
                        "WHERE CO_EMP=? AND CO_DEP=?\n" +
                        "AND ES_ELI='0'");       
        try{
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{coEmp, coDep});
        } catch (EmptyResultDataAccessException e) {
            vReturn = "0";
        }  catch (Exception e) {
            e.printStackTrace();
        }           
        
        return vReturn;
    }
    
    @Override
    public ProcesoExpBean getProcesoExpediente(String pcoProceso){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT NU_PLAZO NU_DIAS_PLAZO, DE_ASUNTO FROM IDOSGD.TDTR_PROCESOS_EXP\n" +
                    "WHERE CO_PROCESO=? AND ES_ESTADO = '1'");
        ProcesoExpBean proceso = null;
        try {
            proceso = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(ProcesoExpBean.class),
                    new Object[]{pcoProceso});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proceso;        
    }
    
    @Override
    public DestinatarioDocumentoEmiBean getEmpleadoDestinoDocExtMp(String pcoDependencia) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  A.CO_LOC CO_LOCAL, ");
        sql.append("        [IDOSGD].[PK_SGD_DESCRIPCION_DE_LOCAL](A.CO_LOC) DE_LOCAL, ");
        sql.append("        B.CO_EMPLEADO CO_EMPLEADO, ");
        sql.append("        [IDOSGD].[PK_SGD_DESCRIPCION_DE_NOM_EMP](B.CO_EMPLEADO) DE_EMPLEADO, ");
        sql.append("        [IDOSGD].[PK_SGD_DESCRIPCION_DE_DEPENDENCIA](B.CO_DEPENDENCIA) DE_DEPENDENCIA, ");
        sql.append("        B.CO_DEPENDENCIA ");
        sql.append("FROM IDOSGD.SITM_LOCAL_DEPENDENCIA A, ");
        sql.append("	 IDOSGD.RHTM_DEPENDENCIA B ");
        sql.append("WHERE B.CO_DEPENDENCIA = A.CO_DEP ");
        sql.append("AND B.IN_MESA_PARTES='1' ");

        DestinatarioDocumentoEmiBean destino = null;
        try {
//            destino = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioDocumentoEmiBean.class),
//                    new Object[]{pcoDependencia, pcoDependencia});
            destino = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DestinatarioDocumentoEmiBean.class));            
        } catch (EmptyResultDataAccessException e) {
            destino = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destino;
    }
    
    @Override
    public List<RemitenteBean> getAllDependencias(){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT  DE_DEPENDENCIA descrip, ");
        sql.append("        CO_DEPENDENCIA codDep, ");
        sql.append("        DE_CORTA_DEPEN ");
        sql.append("FROM IDOSGD.RHTM_DEPENDENCIA ");
        sql.append("WHERE co_nivel <> '6' ");
        sql.append("AND IN_BAJA = '0' ");
        sql.append("order by 1 ");
        
        List<RemitenteBean> list;

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(RemitenteBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }

    @Override
    public List<RequisitoBean> getAllRequisitoExpediente(String nuAnnExp, String nuSecExp, String codProceso) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT \n" +
                    "(SELECT DE_DESCRIPCION FROM IDOSGD.TDTR_REQUISITOS RQ WHERE RQ.COD_REQ=RQEXP.COD_REQUISITO AND RQ.ES_ESTADO='1') DESCRIPCION,\n" +
                    "RQEXP.IN_REQUISITO_PRESENTE DOC_PRESENTE,\n" +
                    "RQEXP.COD_REQUISITO COD_REQUISITO,\n" +
                    "RQEXP.NU_CORRELATIVO NU_CORRELATIVO\n" +
                    "FROM IDOSGD.TDTX_REQUISITO_EXPEDIENTE RQEXP\n" +
                    "WHERE RQEXP.NU_ANN_EXP=?\n" +
                    "AND RQEXP.NU_SEC_EXP=?\n" +
                    "AND RQEXP.ES_ESTADO='1'");

        
        List<RequisitoBean> list;

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(RequisitoBean.class),
                    new Object[]{nuAnnExp, nuSecExp});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;  
    }

    @Override
    public String updReqExpedienteDocExtRec(RequisitoBean req, String nuAnnExp, String nuSecExp, String codProceso, String coUsuario) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();

        sqlUpd.append("UPDATE IDOSGD.TDTX_REQUISITO_EXPEDIENTE ");
        sqlUpd.append("SET IN_REQUISITO_OK=(CASE IN_OBLIGATORIO WHEN '1' THEN (CASE ? WHEN '1' THEN '1' ELSE '0' END) ELSE '1' END), ");
        sqlUpd.append("IN_REQUISITO_PRESENTE=?, ");
        sqlUpd.append("US_MODI_AUDI=?, ");
        sqlUpd.append("FE_MODI_AUDI=CURRENT_TIMESTAMP ");
        sqlUpd.append("WHERE NU_ANN_EXP=? ");
        sqlUpd.append("AND NU_SEC_EXP=? ");
        sqlUpd.append("AND COD_REQUISITO=? ");
        sqlUpd.append("AND COD_PROCESO=? ");
        sqlUpd.append("AND NU_CORRELATIVO=? ");
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{req.isDocPresente(), req.isDocPresente(), coUsuario,
                                        nuAnnExp, nuSecExp, req.getCodRequisito(), codProceso, req.getNuCorrelativo()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();

        }
        return vReturn;
    }

    @Override
    public String getRequisitoPendiente(String nuAnnExp, String nuSecExp) {
        String vReturn = "NO_OK";
        StringBuilder sqlQry = new StringBuilder();
        sqlQry.append("SELECT COUNT(1)\n" +
                    "FROM IDOSGD.TDTX_REQUISITO_EXPEDIENTE\n" +
                    "WHERE NU_ANN_EXP=? AND NU_SEC_EXP=?\n" +
                    "AND IN_REQUISITO_OK='0'\n" +
                    "AND ES_ESTADO='1'");
        try {
            vReturn = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class, new Object[]{nuAnnExp, nuSecExp});
            if(vReturn!=null&&vReturn.equals("0")){
                vReturn="OK";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;  
    }

    @Override
    public String getPkExpDocExtOrigen(String pnuAnn, String pnuEmi){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT REX.NU_ANN_EXP + REX.NU_SEC_EXP ");
        sql.append("FROM (SELECT NU_ANN, NU_EMI, NU_DES FROM IDOSGD.PK_SGD_DESCRIPCION_ARBOL_SEG(?, ?)) DO, ");
        sql.append("	 IDOSGD.TDTV_REMITOS RE, ");
        sql.append("	 IDOSGD.TDTX_REQUISITO_EXPEDIENTE REX ");
        sql.append("WHERE RE.NU_ANN=DO.NU_ANN ");
        sql.append("AND RE.NU_EMI=DO.NU_EMI ");
        sql.append("AND RE.ES_ELI='0' ");
        sql.append("AND RE.CO_GRU='3' ");
        sql.append("AND RE.TI_EMI<>'01' ");
        sql.append("AND REX.NU_ANN_EXP=RE.NU_ANN_EXP ");
        sql.append("AND REX.NU_SEC_EXP=RE.NU_SEC_EXP ");

        String pkExp = null;
        try {
            pkExp = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pnuAnn, pnuEmi});            
        } catch (EmptyResultDataAccessException e) {
            pkExp = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pkExp;
    }
    
    @Override
    public List<RequisitoBean> getAllRequisitoExpediente(String nuAnnExp, String nuSecExp){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT      (SELECT DE_DESCRIPCION ");
        sql.append("		FROM IDOSGD.TDTR_REQUISITOS RQ ");
        sql.append("		WHERE RQ.COD_REQ=RQEXP.COD_REQUISITO AND RQ.ES_ESTADO='1') DESCRIPCION, ");
        sql.append("		RQEXP.IN_REQUISITO_PRESENTE DOC_PRESENTE, ");
        sql.append("		RQEXP.COD_REQUISITO COD_REQUISITO, ");
        sql.append("		RQEXP.NU_CORRELATIVO NU_CORRELATIVO ");
        sql.append("FROM IDOSGD.TDTX_REQUISITO_EXPEDIENTE RQEXP ");
        sql.append("WHERE RQEXP.NU_ANN_EXP=? ");
        sql.append("AND RQEXP.NU_SEC_EXP=? ");
        sql.append("AND RQEXP.ES_ESTADO='1' ");
        
        List<RequisitoBean> list;

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(RequisitoBean.class),
                    new Object[]{nuAnnExp, nuSecExp});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;  
    }

    @Override
    public List<DocumentoBean> getListaReporteBusquedaVoucher(DocumentoBean documento) {
        String vResult;
        StringBuilder prutaReporte = new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List list = null;
        
        prutaReporte.append("SELECT     (EX.NU_CORR_EXP + '-' + EX.NU_ANN_EXP) AS nuExpediente, ");
        prutaReporte.append("		EX.NU_CORR_EXP, ");
        prutaReporte.append("		(SELECT DEP.TITULO_DEP FROM IDOSGD.RHTM_DEPENDENCIA DEP WHERE DEP.CO_DEPENDENCIA=RE.CO_DEP_EMI) DE_DEPENDENCIA, ");
        prutaReporte.append("		(SELECT PAR.DE_PAR FROM IDOSGD.TDTR_PARAMETROS PAR WHERE PAR.CO_PAR='DE_INSTITUCION') DE_INSTITUCION, ");
        prutaReporte.append("		ISNULL((SELECT PAR.DE_PAR FROM IDOSGD.TDTR_PARAMETROS PAR WHERE PAR.CO_PAR='ANEXO_MESA_PARTES'), '') ANEXO_MESA_PARTES, ");
        prutaReporte.append("		ISNULL((SELECT PAR.DE_PAR FROM IDOSGD.TDTR_PARAMETROS PAR WHERE PAR.CO_PAR='FONO_INSTITUCION'), '') FONO_INSTITUCION, ");
        prutaReporte.append("		(SELECT PAR.DE_PAR FROM IDOSGD.TDTR_PARAMETROS PAR WHERE PAR.CO_PAR='PAG_WEB') PAG_WEB, ");
        prutaReporte.append("		ISNULL((SELECT PAR.DE_PAR FROM IDOSGD.TDTR_PARAMETROS PAR WHERE PAR.CO_PAR='DE_MESA_PARTES'), '') DE_MESA_PARTES, ");
        prutaReporte.append("		ISNULL((SELECT PAR.DE_PAR FROM IDOSGD.TDTR_PARAMETROS PAR WHERE PAR.CO_PAR='DE_SLOGAN'), '') DE_SLOGAN, ");                
        prutaReporte.append("		EX.FE_EXP ");
        prutaReporte.append("FROM IDOSGD.TDTV_REMITOS RE, ");
        prutaReporte.append("	  IDOSGD.TDTC_EXPEDIENTE EX ");       
        
        try {
            prutaReporte.append("WHERE RE.NU_ANN = :nu_ann ");
            objectParam.put("nu_ann", documento.getNuAnn());
            prutaReporte.append("AND RE.NU_EMI = :nu_emi ");
            objectParam.put("nu_emi", documento.getNuEmi());
            prutaReporte.append("AND RE.NU_ANN_EXP=EX.NU_ANN_EXP ");
            prutaReporte.append("AND RE.NU_SEC_EXP=EX.NU_SEC_EXP ");

            list = this.namedParameterJdbcTemplate.query(prutaReporte.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoBean.class));

        } catch (Exception ex) {
            vResult = "1" + ex.getMessage();
            ex.printStackTrace();
        }        
        return list;
    }
    
    @Override
    public List<DocumentoExtRecepBean> getListaReporteBusqueda(BuscarDocumentoExtRecepBean buscarDocumentoExtRecepBean) {
        String vResult;
        StringBuilder prutaReporte = new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List list = null;
        boolean bBusqFiltro = false;
        boolean bBusqDep = false;
        String sqlContains = "";
        
        prutaReporte.append("SELECT  A.NU_COR_EMI, ");
        prutaReporte.append("	     TO_CHAR(A.FE_EMI, 'DD/MM/YYYY') FE_EMI_CORTA, ");
        prutaReporte.append("	     (SELECT CDOC_DESDOC ");
        prutaReporte.append("	     FROM SI_MAE_TIPO_DOC ");
        prutaReporte.append("	     WHERE CDOC_TIPDOC = A.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM,B.NU_DOC, ");
        prutaReporte.append("	     (select CELE_DESELE ");
        prutaReporte.append("	     from SI_ELEMENTO ");
        prutaReporte.append("	     WHERE CTAB_CODTAB='TIP_DESTINO' ");
        prutaReporte.append("	     AND CELE_CODELE=A.TI_EMI) tiRemitente, ");
        prutaReporte.append("	     PK_SGD_DESCRIPCION.TI_EMI_EMP(A.NU_ANN, A.NU_EMI) deRemitente, ");
        prutaReporte.append("	     CASE A.NU_CANDES ");
        prutaReporte.append("	     	WHEN 1 THEN PK_SGD_DESCRIPCION.TI_DES_EMP(A.NU_ANN, A.NU_EMI) ");
        prutaReporte.append("	     	ELSE PK_SGD_DESCRIPCION.TI_DES_EMP_V(A.NU_ANN, A.NU_EMI) ");
        prutaReporte.append("	     END deEmpPro, ");
        prutaReporte.append("	     A.DE_ASU, ");
        prutaReporte.append("	     (SELECT DE_EST ");
        prutaReporte.append("	     FROM TDTR_ESTADOS ");
        prutaReporte.append("	     WHERE CO_EST || DE_TAB = A.ES_DOC_EMI || 'TDTV_REMITOS') DE_ES_DOC_EMI, ");
        prutaReporte.append("	     A.NU_DIA_ATE, ");
        prutaReporte.append("	     B.NU_EXPEDIENTE, ");
        prutaReporte.append("	     TO_CHAR(C.FE_VENCE, 'DD/MM/YYYY') FE_EXP_VENCE_CORTA, ");
        prutaReporte.append("	     (SELECT DE_NOMBRE ");
        prutaReporte.append("	     FROM TDTR_PROCESOS_EXP ");
        prutaReporte.append("	     WHERE CO_PROCESO = B.CO_PROCESO_EXP ");
        prutaReporte.append("	     AND ES_ESTADO='1') DE_PROCESO_EXP, ");
        prutaReporte.append("	     B.CO_PROCESO_EXP AS coProcesoExp, ");
        prutaReporte.append("	     A.NU_FOLIOS AS nuFolios, ");
        prutaReporte.append("	     PK_SGD_DESCRIPCION.DE_NOM_EMP(A.CO_EMP_RES) DE_EMP_REC ");
        prutaReporte.append("FROM TDTV_REMITOS A, ");
        prutaReporte.append("     TDTX_REMITOS_RESUMEN B, ");
        prutaReporte.append("     TDTC_EXPEDIENTE C ");
        prutaReporte.append("WHERE A.NU_ANN=B.NU_ANN ");
        prutaReporte.append("AND A.NU_EMI=B.NU_EMI ");
        prutaReporte.append("AND C.NU_ANN_EXP=A.NU_ANN_EXP ");
        prutaReporte.append("AND C.NU_SEC_EXP=A.NU_SEC_EXP ");
        
        try {
            String pnuAnn = buscarDocumentoExtRecepBean.getCoAnnio();
            if (!(buscarDocumentoExtRecepBean.getEsFiltroFecha().equals("1") && pnuAnn.equals("0"))) {
                // Parametros Basicos
                prutaReporte.append(" AND A.NU_ANN = '").append(pnuAnn).append("'");
            }
            prutaReporte.append(" AND A.CO_GRU = '3'");
            prutaReporte.append(" AND A.ES_ELI = '0'");
            // Parametros Basicos
            prutaReporte.append(" AND A.CO_DEP_EMI = '").append(buscarDocumentoExtRecepBean.getCoDependencia()).append("'");

            String auxTipoAcceso=buscarDocumentoExtRecepBean.getTiAcceso();
            String tiAcceso=auxTipoAcceso!=null&&(auxTipoAcceso.equals("0")||auxTipoAcceso.equals("1"))?auxTipoAcceso:"1";           
            if (tiAcceso.equals("1")) { // acceso personal
                prutaReporte.append(" AND A.CO_EMP_RES = '").append(buscarDocumentoExtRecepBean.getCoEmpleado()).append("'");
            } else {
                if(buscarDocumentoExtRecepBean.getInMesaPartes().equals("0")/* && buscarDocExt.getInCambioEst().equals("0")*/){
                    bBusqDep = true;
                    prutaReporte.append(" AND A.CO_DEP = '").append(buscarDocumentoExtRecepBean.getCoDependencia()).append("'");        
                }
            }            
            String pTipoBusqueda = buscarDocumentoExtRecepBean.getTipoBusqueda();
            
            if (pTipoBusqueda.equals("1") && buscarDocumentoExtRecepBean.isEsIncluyeFiltro()) {
                bBusqFiltro = true;
            }
            
            //Filtro
            if (pTipoBusqueda.equals("0") || bBusqFiltro) {
                if (buscarDocumentoExtRecepBean.getCoTipoDoc() != null && buscarDocumentoExtRecepBean.getCoTipoDoc().trim().length() > 0) {
                    prutaReporte.append(" AND A.CO_TIP_DOC_ADM = '").append(buscarDocumentoExtRecepBean.getCoTipoDoc()).append("'");
                }
                if (buscarDocumentoExtRecepBean.getCoEstadoDoc() != null && buscarDocumentoExtRecepBean.getCoEstadoDoc().trim().length() > 0) {
                    prutaReporte.append(" AND A.ES_DOC_EMI = '").append(buscarDocumentoExtRecepBean.getCoEstadoDoc()).append("'");
                }
                if (buscarDocumentoExtRecepBean.getCoTipoEmisor()!= null && buscarDocumentoExtRecepBean.getCoTipoEmisor().trim().length() > 0) {
                    prutaReporte.append(" AND A.TI_EMI = '").append(buscarDocumentoExtRecepBean.getCoTipoEmisor()).append("'");
                }
                if (buscarDocumentoExtRecepBean.getCoDepOriRec()!= null && buscarDocumentoExtRecepBean.getCoDepOriRec().trim().length() > 0 && !bBusqDep) {
                    prutaReporte.append(" AND A.CO_DEP = '").append(buscarDocumentoExtRecepBean.getCoDepOriRec()).append("'");    
                }               

                if(buscarDocumentoExtRecepBean.getCoProceso()!=null&&buscarDocumentoExtRecepBean.getCoProceso().trim().length()>0){
                    prutaReporte.append(" AND B.CO_PROCESO_EXP = '").append(buscarDocumentoExtRecepBean.getCoProceso()).append("'");
                }            
                if (buscarDocumentoExtRecepBean.getCoDepDestino() != null && buscarDocumentoExtRecepBean.getCoDepDestino().trim().length() > 0) {
                    prutaReporte.append(" AND INSTR(B.TI_EMI_DES, '").append(buscarDocumentoExtRecepBean.getCoDepDestino()).append("') > 0 ");
                }
                if (buscarDocumentoExtRecepBean.getEsFiltroFecha() != null
                        && (buscarDocumentoExtRecepBean.getEsFiltroFecha().equals("1") || buscarDocumentoExtRecepBean.getEsFiltroFecha().equals("3"))) {
                    String vFeEmiIni = buscarDocumentoExtRecepBean.getFeEmiIni();
                    String vFeEmiFin = buscarDocumentoExtRecepBean.getFeEmiFin();
                    if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0 && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                        prutaReporte.append(" AND A.FE_EMI between TO_DATE('").append(vFeEmiIni).append("','dd/mm/yyyy') AND TO_DATE('").append(vFeEmiFin)
                            .append("','dd/mm/yyyy') %2B 0.99999 ");
                    }
                }
            }
            //Busqueda
            if (pTipoBusqueda.equals("1")) {         
                if (buscarDocumentoExtRecepBean.getBusNumEmision() != null && buscarDocumentoExtRecepBean.getBusNumEmision().trim().length() > 0) {
                    prutaReporte.append(" AND A.NU_COR_EMI = :pnuCorEmi ");
                    objectParam.put("pnuCorEmi", Integer.getInteger(buscarDocumentoExtRecepBean.getBusNumEmision()));
                }

                if (buscarDocumentoExtRecepBean.getBusNumDoc()!= null && buscarDocumentoExtRecepBean.getBusNumDoc().trim().length() > 1) {
                    prutaReporte.append(" AND B.NU_DOC LIKE '%'||:pnuDocEmi||'%' ");
                    objectParam.put("pnuDocEmi", buscarDocumentoExtRecepBean.getBusNumDoc());
                }

                if (buscarDocumentoExtRecepBean.getBusNumExpediente() != null && buscarDocumentoExtRecepBean.getBusNumExpediente().trim().length() > 1) {
                    prutaReporte.append(" AND B.NU_EXPEDIENTE LIKE '%'||:pnuExpediente||'%' ");
                    objectParam.put("pnuExpediente", buscarDocumentoExtRecepBean.getBusNumExpediente());
                }
                // Busqueda del Asunto
                if (buscarDocumentoExtRecepBean.getBusAsunto()!= null && buscarDocumentoExtRecepBean.getBusAsunto().trim().length() > 1) {                    
                    //prutaReporte.append(" AND CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextMixto(buscarDocumentoExtRecepBean.getBusAsunto())+"', 1 ) > 1 ");
                    prutaReporte.append(" AND CONTAINS(A.*, :pBusquedaTextual) ");
                    sqlContains = "SELECT RETORNO FROM [IDOSGD].PK_SGD_DESCRIPCION_QUERYFILTER('"+buscarDocumentoExtRecepBean.getBusAsunto()+"')";                    
                }
            }

            prutaReporte.append(" ORDER BY A.NU_COR_EMI DESC ");
            
            list = this.namedParameterJdbcTemplate.query(prutaReporte.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoExtRecepBean.class));

        } catch (Exception ex) {
            vResult = "1" + ex.getMessage();
            ex.printStackTrace();
        }        
        return list;
    }
    
    @Override
    public List<DocumentoBean> obtenerHojaDeRuta(String pnuAnn, String pnuEmi, String pTipo) {
        List<DocumentoBean> list = new ArrayList<DocumentoBean>();
        return list;
    }
    
    @Override
    public DocumentoExtRecepBean getDocumentoExtRecep(String pnuAnn, String pnuEmi) {
        return null;
    }
    
    @Override
    public List<DocumentoBean> obtenerSituacionExpediente(String pnuAnn, String pnuEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String vReturn = "NO_OK";
        List<DocumentoBean> list = new ArrayList<DocumentoBean>();
        return list;
    }

    @Override
    public List<ReferenciaDocExtRecepBean> getLstEnlaceExpedientes(String pnuAnn, String pnuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getExisteExpedientesEnlazados(String pkEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getValidaExpEquals(DocumentoExtRecepBean documentoExtRecepBean) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updRefDocumentoEmiAntesEliminar(String nuAnn, String nuEmi, ReferenciaDocExtRecepBean ref) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String enviarNotificacion(DocumentoExtRecepBean documentoExtRecepBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updObservacionExpedienteBean(ExpedienteDocExtRecepBean expedienteBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ExpedienteDocExtRecepBean getObsExpediente(String nuAnnExp, String nuSecExp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String enviarNotificacionDeArchivado(DocumentoExtRecepBean documentoExtRecepBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String insPermisoMP(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String updPermisoMP(String string, String string1, String string2) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String delPermisoMP(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
