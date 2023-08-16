package pe.gob.onpe.tramiteconv.dao.impl.oracle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoPendienteConsulBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.dao.ConsultaPendienteDocDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;

/**
 *
 * @author hpaez
 */
@Repository("consultaPendienteDocDao")
public class ConsultaPendienteDocDaoImp extends SimpleJdbcDaoBase implements ConsultaPendienteDocDao {

    private static Logger logger=Logger.getLogger(ConsultaPendienteDocDaoImp.class);
    
    @Override
    public List<DocumentoBean> getDocumentosBuscaPendientes(BuscarDocumentoPendienteConsulBean buscarDocumentoPendienteConsulBean) {
        
        StringBuilder sql = new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        String tiAcceso = buscarDocumentoPendienteConsulBean.getsTiAcceso();
        
        sql.append("SELECT ");
        sql.append("X.NU_ANN, X.NU_EMI, X.NU_DES, ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION.DE_DOCUMENTO(X.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append("X.NU_DOC NU_DOC, ");
        /*-- [HPB] Inicio 16/01/23 CLS-087-2022 --*/
        //sql.append("IDOSGD.PK_SGD_DESCRIPCION.TI_EMI_EMP(X.NU_ANN, X.NU_EMI) DE_ORI_EMI, ");
        sql.append("REPLACE(PK_SGD_DESCRIPCION.TI_EMI_EMP(X.NU_ANN, X.NU_EMI),'PROVEEDOR - ','') DE_ORI_EMI, ");
        /*-- [HPB] Fin 16/01/23 CLS-087-2022 --*/
        sql.append("TO_CHAR(X.FE_EMI, 'DD/MM/YYYY') FE_EMI, ");
        sql.append("X.DE_ASU_M DE_ASU, ");
        sql.append("X.NU_EXPEDIENTE NU_EXPEDIENTE, X.IN_EXISTE_DOC existeDoc, X.IN_EXISTE_ANEXO existeAnexo, ");
        sql.append("DECODE (X.TI_DES, '01', IDOSGD.PK_SGD_DESCRIPCION.DE_NOM_EMP(X.CO_EMP_DES), '02', ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION.DE_PROVEEDOR(X.NU_RUC_DES), '03', IDOSGD.PK_SGD_DESCRIPCION.ANI_SIMIL(X.NU_DNI_DES), '04', IDOSGD.PK_SGD_DESCRIPCION.OTRO_ORIGEN(X.CO_OTR_ORI_DES) ) DE_EMP_DES, ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION.ESTADOS(X.ES_DOC_REC,'TDTV_DESTINOS') ESTADO_DESTINO, X.ES_DOC_REC ES_DOC_REC ");
        sql.append("FROM ( SELECT A.NU_ANN, A.NU_EMI, A.TI_CAP, B.NU_COR_DES, A.FE_EMI, A.CO_TIP_DOC_ADM, C.NU_DOC, B.TI_DES, B.CO_EMP_DES, B.NU_RUC_DES, ");
        sql.append("B.NU_DNI_DES, B.CO_OTR_ORI_DES, UPPER (A.DE_ASU) DE_ASU_M, C.NU_EXPEDIENTE,  C.IN_EXISTE_DOC, C.IN_EXISTE_ANEXO, B.NU_DES, B.ES_DOC_REC, B.CO_ETIQUETA_REC ");
        sql.append("FROM IDOSGD.TDTV_REMITOS A ");
        sql.append("LEFT JOIN IDOSGD.TDTV_DESTINOS B ON A.NU_ANN = B.NU_ANN AND A.NU_EMI = B.NU_EMI ");
        sql.append("LEFT JOIN IDOSGD.TDTX_REMITOS_RESUMEN C ON B.NU_ANN = C.NU_ANN AND B.NU_EMI = C.NU_EMI ");
        sql.append("LEFT JOIN (SELECT  R.NU_ANN AS NU_ANN2, R.NU_EMI AS NU_EMI2, R.NU_ANN_REF AS NU_ANN_REF2, R.NU_EMI_REF AS NU_EMI_REF2, R.NU_DES_REF  AS NU_DES_REF2 ");
        sql.append("FROM IDOSGD.TDTR_REFERENCIA R ");
        sql.append("LEFT JOIN IDOSGD.TDTV_REMITOS RE ON R.NU_ANN = RE.NU_ANN AND R.NU_EMI=RE.NU_EMI ");
        sql.append("LEFT JOIN IDOSGD.TDTX_REMITOS_RESUMEN RS ON RE.NU_ANN = RS.NU_ANN AND RE.NU_EMI = RS.NU_EMI ");
        sql.append("WHERE RE.ES_DOC_EMI NOT IN ('5', '9', '7')) W ");
        sql.append("ON B.NU_ANN=W.NU_ANN_REF2 AND B.NU_EMI=W.NU_EMI_REF2 AND B.NU_DES = W.NU_DES_REF2 ");
        sql.append("WHERE B.CO_DEP_DES = :pCoDepDes ");
        
        objectParam.put("pCoDepDes", buscarDocumentoPendienteConsulBean.getsCoDependencia());
        if(tiAcceso.equals("1") || tiAcceso=="1"){
            sql.append(" AND B.CO_EMP_DES = :pcoEmpDes ");
            objectParam.put("pcoEmpDes", buscarDocumentoPendienteConsulBean.getsCoEmpleado());
        }
        
        String pNUAnn = buscarDocumentoPendienteConsulBean.getsCoAnnio();
        if (!(buscarDocumentoPendienteConsulBean.getEsFiltroFecha().equals("1") && pNUAnn.equals("0"))) {
            sql.append("AND A.NU_ANN = :pNuAnn ");
            objectParam.put("pNuAnn", pNUAnn);
        }
        
        sql.append("AND A.ES_ELI = '0' ");
        sql.append("AND B.ES_ELI = '0' ");
        sql.append("AND A.ES_DOC_EMI NOT IN ('5', '9', '7') ");
        sql.append("AND A.IN_OFICIO = '0' ");
        
        if (buscarDocumentoPendienteConsulBean.getsEstadoDoc() != null && buscarDocumentoPendienteConsulBean.getsEstadoDoc().trim().length() > 0) {
            String estadoDoc=buscarDocumentoPendienteConsulBean.getsEstadoDoc();
            sql.append(" AND B.ES_DOC_REC = :pEsDocRec ");
            objectParam.put("pEsDocRec", estadoDoc);
        }else{        
            sql.append("AND B.ES_DOC_REC IN ('0','1') ");
        }
        
        if (buscarDocumentoPendienteConsulBean.getEsFiltroFecha() != null && (buscarDocumentoPendienteConsulBean.getEsFiltroFecha().equals("1") || buscarDocumentoPendienteConsulBean.getEsFiltroFecha().equals("3"))) {
            String vFeEmiIni = buscarDocumentoPendienteConsulBean.getsFeEmiIni();
            String vFeEmiFin = buscarDocumentoPendienteConsulBean.getsFeEmiFin();
            if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
                    && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                sql.append(" AND A.FE_EMI between TO_DATE(:pFeEmiIni,'dd/mm/yyyy') AND TO_DATE(:pFeEmiFin,'dd/mm/yyyy') + 0.99999");
                objectParam.put("pFeEmiIni", vFeEmiIni);
                objectParam.put("pFeEmiFin", vFeEmiFin);
            }
        } 
        
        if (buscarDocumentoPendienteConsulBean.getsDestinatario()!= null && buscarDocumentoPendienteConsulBean.getsDestinatario().trim().length() > 0  ) {            
            sql.append("AND UPPER(DECODE (B.TI_DES, ");
            sql.append("'01', IDOSGD.PK_SGD_DESCRIPCION.DE_NOM_EMP(B.CO_EMP_DES),  ");
            sql.append("'02', IDOSGD.PK_SGD_DESCRIPCION.DE_PROVEEDOR(B.NU_RUC_DES), ");
            sql.append("'03', IDOSGD.PK_SGD_DESCRIPCION.ANI_SIMIL(B.NU_DNI_DES), ");
            sql.append("'04', IDOSGD.PK_SGD_DESCRIPCION.OTRO_ORIGEN(B.CO_OTR_ORI_DES)) ");            
            sql.append(" ) LIKE '%'||UPPER(:pDesti)||'%' ");
            objectParam.put("pDesti", buscarDocumentoPendienteConsulBean.getsDestinatario());
        }    
        
        if (buscarDocumentoPendienteConsulBean.getsTipoDoc() != null && buscarDocumentoPendienteConsulBean.getsTipoDoc().trim().length() > 0) {
            sql.append(" AND A.CO_TIP_DOC_ADM = :pCoDocEmi ");
            objectParam.put("pCoDocEmi", buscarDocumentoPendienteConsulBean.getsTipoDoc());
        }
        
        if (buscarDocumentoPendienteConsulBean.getsNroDocumento() != null && buscarDocumentoPendienteConsulBean.getsNroDocumento().trim().length() > 1) {
            sql.append(" AND A.NU_DOC_EMI LIKE ''||:pnuDocEmi||'%' ");
            objectParam.put("pnuDocEmi", buscarDocumentoPendienteConsulBean.getsNroDocumento());
        }
        
        if (buscarDocumentoPendienteConsulBean.getsBuscAsunto() != null && buscarDocumentoPendienteConsulBean.getsBuscAsunto().trim().length() > 1) {
            sql.append(" AND UPPER(A.DE_ASU) LIKE '%'||:pDeAsunto||'%' ");
            objectParam.put("pDeAsunto", buscarDocumentoPendienteConsulBean.getsBuscAsunto().toUpperCase());
        } 
        if (buscarDocumentoPendienteConsulBean.getsBuscNroExpediente() != null && buscarDocumentoPendienteConsulBean.getsBuscNroExpediente().trim().length() > 1) {
            sql.append(" AND C.NU_EXPEDIENTE LIKE '%'||:pnuExpediente||'%' ");
            objectParam.put("pnuExpediente", buscarDocumentoPendienteConsulBean.getsBuscNroExpediente());
        }
        
        if(buscarDocumentoPendienteConsulBean.getBusResultado().equals("1")){
            if(buscarDocumentoPendienteConsulBean.getCoTipoPersona().equals("03")){
                sql.append(" AND A.NU_DNI_EMI = :pNumDni ");
                objectParam.put("pNumDni", buscarDocumentoPendienteConsulBean.getBusNumDni());
            }else if(buscarDocumentoPendienteConsulBean.getCoTipoPersona().equals("02")){
                sql.append(" AND A.NU_RUC_EMI = :pNumRuc ");
                objectParam.put("pNumRuc", buscarDocumentoPendienteConsulBean.getBusNumRuc());
            }else if(buscarDocumentoPendienteConsulBean.getCoTipoPersona().equals("04")){
                sql.append(" AND A.CO_OTR_ORI_EMI = :pCoOtr ");
                objectParam.put("pCoOtr", buscarDocumentoPendienteConsulBean.getBusCoOtros());
            }
        } 
        
        sql.append(" AND W.NU_ANN2 IS NULL ");
        sql.append("ORDER BY A.FE_EMI) X ");
        
        logger.info("SQL DOC. PENDIENTES: "+ sql);        
        
        List<DocumentoBean> list = new ArrayList<DocumentoBean>();        
        
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
}
