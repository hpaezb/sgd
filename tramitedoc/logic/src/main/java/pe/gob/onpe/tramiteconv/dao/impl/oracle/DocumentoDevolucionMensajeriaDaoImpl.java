package pe.gob.onpe.tramiteconv.dao.impl.oracle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoDevolucionMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoDevolucionMensajeriaBean;
import pe.gob.onpe.tramitedoc.dao.DocumentoDevolucionMensajeriaDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;

/**
 *
 * @author hpaez
 */
@Repository("documentoDevolucionMensajeriaDao")
public class DocumentoDevolucionMensajeriaDaoImpl extends SimpleJdbcDaoBase implements DocumentoDevolucionMensajeriaDao{
    
    private static Logger logger=Logger.getLogger(DocumentoDevolucionMensajeriaDaoImpl.class);
    
    @Override
    public List<DocumentoDevolucionMensajeriaBean> getDocumentoDevolucionMensajeria(BuscarDocumentoDevolucionMensajeriaBean buscarDocumentoDevolucionMensajeriaBean) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();               
        
        sql.append("SELECT A.NU_ANN, ");
        sql.append("A.NU_EMI, A.NU_SEC, ");
        sql.append("B.NU_DOC_EMI, ");
        sql.append("B.DE_DOC_SIG, ");
        sql.append("PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append("DECODE(B.TI_EMI, ");
        sql.append("'01', ");
        sql.append("B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG, ");
        sql.append("'05', ");
        sql.append("B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG, ");
        sql.append("B.DE_DOC_SIG) NU_DOC, ");
        sql.append("PK_SGD_DESCRIPCION.DE_DEPENDENCIA(B.CO_DEP_EMI) DE_DEPENDENCIA, ");
        sql.append("DECODE(B.NU_CANDES, ");
        sql.append("1, ");
        sql.append("PK_SGD_DESCRIPCION.TI_DES_EMP_MENSAJERIA(B.NU_ANN, B.NU_EMI), ");
        sql.append("PK_SGD_DESCRIPCION.TI_DES_EMP_V(B.NU_ANN, B.NU_EMI)) || CASE ");
        sql.append("WHEN PK_SGD_DESCRIPCION.ani_simil(B.REMI_NU_DNI_EMI) IS NULL THEN ");
        sql.append("'' ");
        sql.append("ELSE ");
        sql.append("' - ' || PK_SGD_DESCRIPCION.ani_simil(B.REMI_NU_DNI_EMI) ");
        sql.append("END DE_EMP_DES, ");
        sql.append("B.DE_ASU, ");
        sql.append("A.OBS_DEV, ");
        sql.append("C.CDIR_REMITE DIRECCION, ");
        sql.append("DECODE(C.TI_DES, ");
        sql.append("'01', ");
        sql.append("TRIM(DEP.NODIS) || '/' || TRIM(DEP.NOPRV) || '/' || ");
        sql.append("TRIM(DEP.NODEP), ");
        sql.append("'02', ");
        sql.append("TRIM(UBIDESTINO.NODIS) || '/' || TRIM(UBIDESTINO.NOPRV) || '/' || ");
        sql.append("TRIM(UBIDESTINO.NODEP), ");
        sql.append("'03', ");
        sql.append("TRIM(UBIDESTINO.NODIS) || '/' || TRIM(UBIDESTINO.NOPRV) || '/' || ");
        sql.append("TRIM(UBIDESTINO.NODEP), ");
        sql.append("'04', ");
        sql.append("TRIM(UBIDESTINO.NODIS) || '/' || TRIM(UBIDESTINO.NOPRV) || '/' || ");
        sql.append("TRIM(UBIDESTINO.NODEP), ");
        sql.append("'05', ");
        sql.append("TRIM(DEP.NODIS) || '/' || TRIM(DEP.NOPRV) || '/' || ");
        sql.append("TRIM(DEP.NODEP)) UBIGEO, ");
        sql.append("(SELECT DE_PRI FROM TDTR_PRIORIDAD WHERE CO_PRI = C.CO_PRI) DE_PRI, ");
        sql.append("TO_CHAR(B.FE_EMI, 'DD/MM/YYYY hh24:mi:ss') FE_EMI_CORTA, ");
        sql.append("TO_CHAR(A.Fe_Env_OFI, 'DD/MM/YYYY hh24:mi:ss') FE_ENV_MES, ");
        sql.append("TO_CHAR(A.FE_USE_CRE, 'DD/MM/YYYY hh24:mi:ss') FE_DEV_OFI, ");
        sql.append("A.CO_USE_CRE CO_EMP_DEV, ");
        sql.append("A.ES_DOC_FIN, ");
        sql.append("EST.DE_ES_DOC_EMI_MP DE_DOC_FIN, ");
        sql.append("B.ES_DOC_EMI, ");
        sql.append("(SELECT DE_EST FROM TDTR_ESTADOS WHERE DE_TAB = 'TDTV_REMITOS' AND CO_EST=B.ES_DOC_EMI) DE_DOC_EMI, E.IN_EXISTE_DOC, E.IN_EXISTE_ANEXO ");
        sql.append("FROM TDTV_ESTACION_DOC A ");
        sql.append("INNER JOIN TDTV_REMITOS B ");
        sql.append("ON A.NU_ANN = B.NU_ANN ");
        sql.append("AND A.NU_EMI = B.NU_EMI ");
        sql.append("INNER JOIN TDTV_DESTINOS C ");
        sql.append("ON B.NU_ANN = C.NU_ANN ");
        sql.append("AND B.NU_EMI = C.NU_EMI ");
        sql.append("LEFT JOIN (SELECT D.CO_DEPENDENCIA, ");
        sql.append("D.DE_DEPENDENCIA, ");
        sql.append("D.DE_CORTA_DEPEN, ");
        sql.append("L.DE_NOMBRE_LOCAL, ");
        sql.append("L.DE_DIRECCION_LOCAL, ");
        sql.append("NODEP, ");
        sql.append("NOPRV, ");
        sql.append("NODIS, ");
        sql.append("L.CCOD_LOCAL ");
        sql.append("FROM RHTM_DEPENDENCIA D ");
        sql.append("INNER JOIN SITM_LOCAL_DEPENDENCIA DL ");
        sql.append("ON DL.CO_DEP = D.CO_DEPENDENCIA ");
        sql.append("INNER JOIN SI_MAE_LOCAL L ");
        sql.append("ON L.CCOD_LOCAL = DL.CO_LOC ");
        sql.append("LEFT JOIN IDTUBIAS U ");
        sql.append("ON U.UBDEP || U.UBPRV || U.UBDIS = L.CO_UBIGEO) DEP ");
        sql.append("ON DEP.CO_DEPENDENCIA = C.co_dep_DES ");
        sql.append("AND DEP.CCOD_LOCAL = C.co_loc_des ");
        sql.append("LEFT JOIN IDTUBIAS UBIDESTINO ");
        sql.append("ON UBIDESTINO.UBDEP || UBIDESTINO.UBPRV || UBIDESTINO.UBDIS = ");
        sql.append("C.CCOD_DPTO || C.CCOD_PROV || C.CCOD_DIST ");
        sql.append("LEFT JOIN (SELECT CO_EST, DE_EST DE_ES_DOC_EMI_MP ");
        sql.append("FROM TDTR_ESTADOS ");
        sql.append("WHERE DE_TAB = 'ENVIO_DOCUMENTO_MSJ') EST ");
        sql.append("ON EST.CO_EST = A.ES_DOC_FIN ");
        sql.append("LEFT JOIN TDTX_REMITOS_RESUMEN E ON B.NU_ANN = E.NU_ANN AND B.NU_EMI = E.NU_EMI ");
        sql.append("WHERE A.ES_DOC_EST='1' ");
        String pNuAnn = buscarDocumentoDevolucionMensajeriaBean.getCoAnnio();
        String pEsFiltroFecha = buscarDocumentoDevolucionMensajeriaBean.getEsFiltroFecha();
        String pEsFiltroFechaENVmSJ = buscarDocumentoDevolucionMensajeriaBean.getEsFiltroFechaEnvMsj();
        String pEsFiltroFechaDevOfi = buscarDocumentoDevolucionMensajeriaBean.getEsFiltroFechaDevOfi();

        if (!(pEsFiltroFecha.equals("1")&&pNuAnn.equals("0"))) {
            sql.append("AND B.NU_ANN = :pNuAnn ");
            objectParam.put("pNuAnn", pNuAnn);        
        }

        if(buscarDocumentoDevolucionMensajeriaBean.getCoTipoEnvMsj()!= null && buscarDocumentoDevolucionMensajeriaBean.getCoTipoEnvMsj().trim().length()>0 && !buscarDocumentoDevolucionMensajeriaBean.getCoTipoEnvMsj().equals(".: TODOS :.")){
            sql.append(" AND C.CO_PRI = :pTipoPrioridad ");
            objectParam.put("pTipoPrioridad", buscarDocumentoDevolucionMensajeriaBean.getCoTipoEnvMsj());
        }
        
        if (buscarDocumentoDevolucionMensajeriaBean.getCoTipoDoc()!= null && buscarDocumentoDevolucionMensajeriaBean.getCoTipoDoc().trim().length() > 0) {
            sql.append(" AND B.CO_TIP_DOC_ADM = :pCoDocEmi ");
            objectParam.put("pCoDocEmi", buscarDocumentoDevolucionMensajeriaBean.getCoTipoDoc());
        } 
        
        if (buscarDocumentoDevolucionMensajeriaBean.getCoDependenciaBusca()!= null && buscarDocumentoDevolucionMensajeriaBean.getCoDependenciaBusca().trim().length() > 0) {
            sql.append(" AND B.CO_DEP_EMI = :pCoDependenciaBusca ");
            objectParam.put("pCoDependenciaBusca", buscarDocumentoDevolucionMensajeriaBean.getCoDependenciaBusca());
        } 
        
        if (buscarDocumentoDevolucionMensajeriaBean.getBusNumDoc() != null && buscarDocumentoDevolucionMensajeriaBean.getBusNumDoc().trim().length() > 1) {
            sql.append(" AND E.NU_DOC LIKE '%'||:pnuDocEmi||'%' ");
            objectParam.put("pnuDocEmi", buscarDocumentoDevolucionMensajeriaBean.getBusNumDoc());
        }
         
        if (buscarDocumentoDevolucionMensajeriaBean.getBusAsunto() != null && buscarDocumentoDevolucionMensajeriaBean.getBusAsunto().trim().length() > 1) {
            sql.append(" AND UPPER(B.DE_ASU) LIKE '%'||:pDeAsunto||'%' ");
            objectParam.put("pDeAsunto", buscarDocumentoDevolucionMensajeriaBean.getBusAsunto().toUpperCase());
        } 

        if (buscarDocumentoDevolucionMensajeriaBean.getBusObsDev() != null && buscarDocumentoDevolucionMensajeriaBean.getBusObsDev().trim().length() > 1) {
            sql.append(" AND UPPER(A.OBS_DEV) LIKE '%'||:pDeObsDevOfi||'%' ");
            objectParam.put("pDeObsDevOfi", buscarDocumentoDevolucionMensajeriaBean.getBusObsDev().toUpperCase());
        } 
        
        if (buscarDocumentoDevolucionMensajeriaBean.getBusDesti()!= null && buscarDocumentoDevolucionMensajeriaBean.getBusDesti().trim().length() > 0  ) {
            sql.append(" AND UPPER(DECODE(B.NU_CANDES,");
            sql.append(" 1,PK_SGD_DESCRIPCION.TI_DES_EMP_MENSAJERIA(B.NU_ANN, B.NU_EMI), PK_SGD_DESCRIPCION.TI_DES_EMP_V(B.NU_ANN, B.NU_EMI)) || CASE ");
            sql.append(" WHEN PK_SGD_DESCRIPCION.ani_simil(B.REMI_NU_DNI_EMI) IS NULL THEN '' ELSE ' - ' || PK_SGD_DESCRIPCION.ani_simil(B.REMI_NU_DNI_EMI) END ");
            sql.append(" ) LIKE '%'||UPPER(:pDesti)||'%' ");
            objectParam.put("pDesti", buscarDocumentoDevolucionMensajeriaBean.getBusDesti());
        } 
        
        if (buscarDocumentoDevolucionMensajeriaBean.getEsFiltroFecha() != null  && (pEsFiltroFecha.equals("1") || pEsFiltroFecha.equals("3"))) {
            String vFeEmiIni = buscarDocumentoDevolucionMensajeriaBean.getFeEmiIni();
            String vFeEmiFin = buscarDocumentoDevolucionMensajeriaBean.getFeEmiFin();
            if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0  && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                sql.append(" AND B.FE_EMI between TO_DATE(:pFeEmiIni,'DD/MM/YYYY') AND TO_DATE(:pFeEmiFin,'DD/MM/YYYY') + 0.99999");
                objectParam.put("pFeEmiIni", vFeEmiIni);
                objectParam.put("pFeEmiFin", vFeEmiFin);
            }
        }

        if (buscarDocumentoDevolucionMensajeriaBean.getEsFiltroFechaEnvMsj()!= null  && (pEsFiltroFechaENVmSJ.equals("1") || pEsFiltroFechaENVmSJ.equals("3"))) {
            String vFeEmiIni = buscarDocumentoDevolucionMensajeriaBean.getFeEmiIniEnvMSJ();
            String vFeEmiFin = buscarDocumentoDevolucionMensajeriaBean.getFeEmiFinEnvMSJ();
            if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0  && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                sql.append(" AND B.FE_ENV_MES between TO_DATE(:pFeEmiIniEnv,'DD/MM/YYYY') AND TO_DATE(:pFeEmiFinEnv,'DD/MM/YYYY') + 0.99999");
                objectParam.put("pFeEmiIniEnv", vFeEmiIni);
                objectParam.put("pFeEmiFinEnv", vFeEmiFin);
            }
        } 

        if (buscarDocumentoDevolucionMensajeriaBean.getEsFiltroFechaDevOfi()!= null  && (pEsFiltroFechaDevOfi.equals("1") || pEsFiltroFechaDevOfi.equals("3"))) {
            String vFeEmiIni = buscarDocumentoDevolucionMensajeriaBean.getFeEmiIniDevOfi();
            String vFeEmiFin = buscarDocumentoDevolucionMensajeriaBean.getFeEmiFinDevOfi();
            if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0  && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                sql.append(" AND A.FE_USE_CRE between TO_DATE(:pFeEmiIniDevOfi,'DD/MM/YYYY') AND TO_DATE(:pFeEmiFinDevOfi,'DD/MM/YYYY') + 0.99999");
                objectParam.put("pFeEmiIniDevOfi", vFeEmiIni);
                objectParam.put("pFeEmiFinDevOfi", vFeEmiFin);
            }
        } 
        
        sql.append("ORDER BY A.FE_USE_CRE DESC ");
        logger.info("SQL Doc. Dev. a Oficina:"+ sql);
        List<DocumentoDevolucionMensajeriaBean> list = new ArrayList<DocumentoDevolucionMensajeriaBean>();

        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(),objectParam,BeanPropertyRowMapper.newInstance(DocumentoDevolucionMensajeriaBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;             
        }catch (Exception e) {
            list = null;
            e.printStackTrace();            
        }
        return list;
    }    
}
