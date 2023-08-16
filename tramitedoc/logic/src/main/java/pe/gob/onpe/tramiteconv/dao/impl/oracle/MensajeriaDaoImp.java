package pe.gob.onpe.tramiteconv.dao.impl.oracle;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoRecepMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.DestiDocumentoEnvMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.DestinoResBen;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoRecepMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.EstacionDocumento;
import pe.gob.onpe.tramitedoc.bean.TipoElementoMensajeriaBean;
import pe.gob.onpe.tramitedoc.dao.MensajeriaDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
/**
 *
 * @author wcondori
 */
@Repository("mensajeriaDao")
public class MensajeriaDaoImp extends SimpleJdbcDaoBase  implements MensajeriaDao{ 
    
    private SimpleJdbcCall spInsertaEstacionDoc;
    
    private static Logger logger=Logger.getLogger(MensajeriaDaoImp.class);
    
    @Override
    public List<DocumentoRecepMensajeriaBean> getDocumentoRecepMensajeria(BuscarDocumentoRecepMensajeriaBean buscarDocumentoRecepMensajeriaBean) {
        boolean bBusqFiltro = false;
        boolean bBusqDep = false;
        StringBuilder sql = new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();        
        sql.append("SELECT  ROWNUM,NU_ANN,NU_EMI,\n" +
                "     PK_SGD_DESCRIPCION.DE_DOCUMENTO(X.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM,FE_EMI_CORTA,TOTAL_DESTINO,TOTAL_ENVIADO,TOTAL_PENDIENTE,\n" +
                "        PK_SGD_DESCRIPCION.DE_DEPENDENCIA (X.CO_DEP_EMI) DE_DEPENDENCIA,\n" +                
                "            DE_ASU,\n" +
                "         DECODE (X.TI_EMI,\n" +
                "         		'01', X.NU_DOC_EMI || '-' || X.NU_ANN || '/' || X.DE_DOC_SIG,\n" +
                "         		'05', X.NU_DOC_EMI || '-' || X.NU_ANN || '/' || X.DE_DOC_SIG,\n" +
                "         		X.DE_DOC_SIG\n" +
                "         	   ) NU_DOC,\n" +
                "         DECODE (X.NU_CANDES,\n" +
                "         		1, PK_SGD_DESCRIPCION.TI_DES_EMP_MENSAJERIA (X.NU_ANN, X.NU_EMI),\n" +
                "         		PK_SGD_DESCRIPCION.TI_DES_EMP_V (X.NU_ANN, X.NU_EMI)\n" +
                "         	   )  ||  CASE WHEN PK_SGD_DESCRIPCION.ani_simil(X.REMI_NU_DNI_EMI) IS NULL THEN '' ELSE ' - ' || PK_SGD_DESCRIPCION.ani_simil(X.REMI_NU_DNI_EMI) END  DE_EMP_DES, \n" +
                "         DE_ES_DOC_EMI_MP, DOC_ESTADO_MSJ, FEC_RECEPMP,      \n" +  //PK_SGD_DESCRIPCION.ESTADOS_MP(X.ES_DOC_EMI,'TDTV_REMITOS')
                "         PK_SGD_DESCRIPCION.DE_LOCAL(X.CO_LOC_EMI) DE_LOC_EMI \n" +
                "        ,TO_CHAR(FEC_ENVIOMSJ,'DD/MM/YYYY hh24:mi:ss') FEC_ENVIOMSJ, DE_AMBITO,DE_TIP_ENV, DE_TIP_MSJ, TIPO_ZONA, NU_SER_MSJ, NU_MSJ, NU_EXPEDIENTE, FE_PLA_MSJ_D,DIAS_PLA_DEVO_D  diasDevoluvion ,DIAS_PLA_ENTR_D diasEntrega,TO_CHAR(FE_ENV_MES,'DD/MM/YYYY hh24:mi:ss') fechaenvioamensajeria \n"+
                
                ",X.CDIR_REMITE direccion, X.DEPARTAMENTO_1 ubigeo ,X.CARGO \n"+
                                
                
                "         FROM ( \n" +
                "         SELECT A.NU_ANN,A.NU_EMI ,TO_CHAR(A.FE_EMI,'DD/MM/YYYY hh24:mi:ss') FE_EMI_CORTA,\n" +
                "         A.DE_ASU,A.CO_DEP_EMI,A.CO_EMP_EMI,A.CO_OTR_ORI_EMI,A.CO_TIP_DOC_ADM,A.NU_DOC_EMI,A.DE_DOC_SIG,\n" +
                "          DE_ES_DOC_EMI_MP, DOC_ESTADO_MSJ,TO_CHAR(FEC_RECEPMP,'DD/MM/YYYY hh24:mi:ss') FEC_RECEPMP, \n" +
                "         A.TI_EMI,A.NU_RUC_EMI,A.NU_DNI_EMI,A.NU_CANDES,A.ES_DOC_EMI,A.CO_LOC_EMI,\n" +
                "         A.CO_DEP,TO_CHAR(NVL(TOTAL_DESTINO,0)) TOTAL_DESTINO ,TO_CHAR(NVL(TOTAL_ENVIADO,0))TOTAL_ENVIADO ,TO_CHAR(NVL(TOTAL_PENDIENTE,0)) TOTAL_PENDIENTE\n" +
                "        ,FEC_ENVIOMSJ, DE_AMBITO,DE_TIP_ENV, DE_TIP_MSJ, TIPO_ZONA, NU_SER_MSJ, NU_MSJ, B.NU_EXPEDIENTE, E.FE_PLA_MSJ_D \n"+
                "        ,  E.DIAS_PLA_DEVO_D,E.DIAS_PLA_ENTR_D,A.FE_ENV_MES \n"+
                "         , E.REMI_NU_DNI_EMI \n"+
                
                
                " , E.CDIR_REMITE\n" +
                " ,DECODE(E.TI_DES\n" +
                "         ,'01', TRIM(DEP.NODIS)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODEP)\n" +
                "         ,'02', TRIM(UBIDESTINO.NODIS)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODEP)\n" +
                "         ,'03', TRIM(UBIDESTINO.NODIS)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODEP)\n" +
                "         ,'04', TRIM(UBIDESTINO.NODIS)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODEP)\n" +
                "         ,'05', TRIM(DEP.NODIS)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODEP)) DEPARTAMENTO_1 "+
                
                "         , NVL(E.DE_CARGO, ' ')   CARGO \n"+
                
                "        FROM TDTV_REMITOS A "+
                "        INNER JOIN TDTX_REMITOS_RESUMEN B ON A.NU_ANN = B.NU_ANN AND A.NU_EMI = B.NU_EMI\n" +                
            " LEFT JOIN (SELECT NU_ANN, NU_EMI,NVL(COUNT(NU_DES), 0) TOTAL_ENVIADO,REMI_NU_DNI_EMI,CDIR_REMITE,CCOD_DPTO, CCOD_PROV, CCOD_DIST, co_dep_DES, co_loc_des, TI_DES, DE_CARGO,NVL(COUNT(NU_DES), 0) TOTAL_DESTINO,\n" +
            "            M.FEC_ENVIOMSJ,DIAS_PLA_DEVO_D,DIAS_PLA_ENTR_D,M.DE_AMBITO,M.DE_TIP_ENV, D.NU_MSJ, M.NU_SER_MSJ || '-' || M.AN_SER_MSJ NU_SER_MSJ,FE_PLA_MSJ_D, M.DE_TIP_MSJ,\n" +
            "        (SELECT CELE_DESELE FROM SI_ELEMENTO WHERE CTAB_CODTAB = 'SERVICIO_COURRIER' AND CELE_CODELE = (SELECT COCLASERCOUR FROM IDTUBIAS WHERE UBDEP = CCOD_DPTO AND UBPRV = CCOD_PROV\n" +
            "         AND UBDIS = CCOD_DIST AND STUBI = '1') AND ESTADO = '1') TIPO_ZONA\n" +
            "          FROM TDTV_DESTINOS D LEFT JOIN TD_MENSAJERIA M\n" +
            "          ON M.NU_MSJ = D.NU_MSJ\n" +
            "         WHERE TI_DES <> '01'\n" +
            "           AND ES_DOC_REC IN ('0', '1', '2', '3', '4')\n" +
            "         GROUP BY NU_ANN,NU_EMI,REMI_NU_DNI_EMI,CDIR_REMITE,CCOD_DPTO,CCOD_PROV,CCOD_DIST,co_dep_DES,co_loc_des,TI_DES,DE_CARGO,\n" +
            "         M.FEC_ENVIOMSJ, DIAS_PLA_DEVO_D, DIAS_PLA_ENTR_D,M.DE_AMBITO,M.DE_TIP_ENV,M.NU_SER_MSJ,M.AN_SER_MSJ,FE_PLA_MSJ_D, M.DE_TIP_MSJ, D.NU_MSJ) E\n" +
            "       ON A.NU_ANN = E.NU_ANN\n" +
            "       AND A.NU_EMI = E.NU_EMI\n" +                
//                "        LEFT JOIN (SELECT NU_ANN,NU_EMI,REMI_NU_DNI_EMI, CDIR_REMITE, CCOD_DPTO, CCOD_PROV, CCOD_DIST, co_dep_DES, co_loc_des,TI_DES, DE_CARGO, NVL(COUNT(NU_DES),0) TOTAL_DESTINO, \n" +
//                "       (SELECT CELE_DESELE FROM SI_ELEMENTO WHERE CTAB_CODTAB = 'SERVICIO_COURRIER' AND CELE_CODELE = (SELECT COCLASERCOUR FROM IDTUBIAS WHERE UBDEP = CCOD_DPTO AND UBPRV = CCOD_PROV\n" +
//                "        AND UBDIS = CCOD_DIST AND STUBI = '1') AND ESTADO = '1') TIPO_ZONA \n" +
//                "       FROM TDTV_DESTINOS WHERE TI_DES<>'01' AND ES_DOC_REC IN  ('0','1','2','3','4') GROUP BY NU_ANN,NU_EMI, REMI_NU_DNI_EMI, CDIR_REMITE, CCOD_DPTO, CCOD_PROV, CCOD_DIST, co_dep_DES, co_loc_des,TI_DES, DE_CARGO ) E ON A.NU_ANN = E.NU_ANN AND A.NU_EMI = E.NU_EMI\n" +
//                //"        LEFT JOIN (SELECT NU_ANN,NU_EMI,NVL(COUNT(NU_DES),0) TOTAL_ENVIADO ,M.FEC_ENVIOMSJ,M.DIAS_PLA_DEVO,M.DIAS_PLA_ENTR, M.DE_AMBITO,M.DE_TIP_ENV,M.NU_SER_MSJ||'-'||M.AN_SER_MSJ NU_SER_MSJ, M.FE_PLA_MSJ\n" + Hermes 30/07/2018
//                "        LEFT JOIN (SELECT NU_ANN,NU_EMI,NVL(COUNT(NU_DES),0) TOTAL_ENVIADO ,M.FEC_ENVIOMSJ,DIAS_PLA_DEVO_D,DIAS_PLA_ENTR_D, M.DE_AMBITO,M.DE_TIP_ENV,M.NU_SER_MSJ||'-'||M.AN_SER_MSJ NU_SER_MSJ, FE_PLA_MSJ_D,M.DE_TIP_MSJ\n" +
//                "                   FROM TDTV_DESTINOS D LEFT JOIN TD_MENSAJERIA M ON M.NU_MSJ=D.NU_MSJ\n" +
//                "                   WHERE D.TI_DES <>'01' AND ES_DOC_REC IN ('2','3','4') GROUP BY NU_ANN,NU_EMI\n" +
//                //"                   ,M.FEC_ENVIOMSJ, M.DIAS_PLA_DEVO,M.DIAS_PLA_ENTR,M.DE_AMBITO,M.DE_TIP_ENV,M.NU_SER_MSJ,M.AN_SER_MSJ, M.FE_PLA_MSJ) C ON A.NU_ANN = C.NU_ANN AND A.NU_EMI = C.NU_EMI \n" + Hermes 30/07/2018
//                "                   ,M.FEC_ENVIOMSJ, DIAS_PLA_DEVO_D,DIAS_PLA_ENTR_D,M.DE_AMBITO,M.DE_TIP_ENV,M.NU_SER_MSJ,M.AN_SER_MSJ, FE_PLA_MSJ_D, M.DE_TIP_MSJ) C ON A.NU_ANN = C.NU_ANN AND A.NU_EMI = C.NU_EMI \n" +
                "        LEFT JOIN (SELECT NU_ANN,NU_EMI, NVL(COUNT(NU_DES),0) TOTAL_PENDIENTE FROM TDTV_DESTINOS WHERE TI_DES <>'01' AND ES_DOC_REC IN ('0','1') GROUP BY NU_ANN,NU_EMI) D ON A.NU_ANN = D.NU_ANN AND A.NU_EMI = D.NU_EMI\n" +
                "     LEFT JOIN (SELECT CO_EST,DE_EST DE_ES_DOC_EMI_MP FROM TDTR_ESTADOS WHERE DE_TAB='ENVIO_DOCUMENTO_MSJ') EST ON EST.CO_EST= A.DOC_ESTADO_MSJ \n"+
                
                
                " LEFT JOIN ( SELECT D.CO_DEPENDENCIA,D.DE_DEPENDENCIA,D.DE_CORTA_DEPEN,L.DE_NOMBRE_LOCAL,L.DE_DIRECCION_LOCAL,NODEP,NOPRV,NODIS ,L.CCOD_LOCAL FROM RHTM_DEPENDENCIA D \n" +
                "                  INNER JOIN SITM_LOCAL_DEPENDENCIA DL ON DL.CO_DEP=D.CO_DEPENDENCIA \n" +
                "                  INNER JOIN SI_MAE_LOCAL L ON L.CCOD_LOCAL=DL.CO_LOC \n" +
                "                  LEFT JOIN IDTUBIAS U ON U.UBDEP||U.UBPRV||U.UBDIS =L.CO_UBIGEO  ) DEP ON DEP.CO_DEPENDENCIA=E.co_dep_DES AND DEP.CCOD_LOCAL=E.co_loc_des  \n" +
                " LEFT JOIN IDTUBIAS UBIDESTINO ON UBIDESTINO.UBDEP||UBIDESTINO.UBPRV||UBIDESTINO.UBDIS =E.CCOD_DPTO||E.CCOD_PROV||E.CCOD_DIST "+
        /*--22/08/19 HPB Devolucion Doc a Oficina--*/                                        
                //"  WHERE NOT A.DOC_ESTADO_MSJ IS NULL AND TI_ENV_MSJ='0'   ");  
                "  WHERE NOT A.DOC_ESTADO_MSJ IS NULL "+
                " AND A.ES_DOC_EMI <> '9'");
        /*--22/08/19 HPB Devolucion Doc a Oficina--*/
        String pNuAnn = buscarDocumentoRecepMensajeriaBean.getCoAnnio();
        String pEsFiltroFecha = buscarDocumentoRecepMensajeriaBean.getEsFiltroFecha();
        String pEsFiltroFechaENVmSJ = buscarDocumentoRecepMensajeriaBean.getEsFiltroFechaEnvMsj();
        
        if (!(pEsFiltroFecha.equals("1")&&pNuAnn.equals("0"))) {
            sql.append(" AND A.NU_ANN = :pNuAnn");
            // Parametros Basicos
            objectParam.put("pNuAnn", pNuAnn);
        }
        /*----------------Hermes 15/08/2018-------------*/
        if (buscarDocumentoRecepMensajeriaBean.getCoAmbitoMsj()!= null && buscarDocumentoRecepMensajeriaBean.getCoAmbitoMsj().trim().length() > 0 && !buscarDocumentoRecepMensajeriaBean.getCoAmbitoMsj().equals(".: TODOS :.") ) {
            sql.append(" AND DE_AMBITO= :pAmbito ");
            objectParam.put("pAmbito", buscarDocumentoRecepMensajeriaBean.getCoAmbitoMsj());
        }
        
        if (buscarDocumentoRecepMensajeriaBean.getBusNuSerMsj()!= null && buscarDocumentoRecepMensajeriaBean.getBusNuSerMsj().trim().length() > 0 ) {
            sql.append(" AND NU_SER_MSJ LIKE '%'||:pNuSerMsj||'%' ");
            objectParam.put("pNuSerMsj", buscarDocumentoRecepMensajeriaBean.getBusNuSerMsj() +'-'+ buscarDocumentoRecepMensajeriaBean.getBusAnSerMsj());
        }   
        
        if (buscarDocumentoRecepMensajeriaBean.getBusDesti()!= null && buscarDocumentoRecepMensajeriaBean.getBusDesti().trim().length() > 0  ) {
            sql.append(" AND UPPER(DECODE(A.NU_CANDES,");
            sql.append(" 1,PK_SGD_DESCRIPCION.TI_DES_EMP_MENSAJERIA(E.NU_ANN, E.NU_EMI), PK_SGD_DESCRIPCION.TI_DES_EMP_V(E.NU_ANN, E.NU_EMI)) || CASE ");
            sql.append(" WHEN PK_SGD_DESCRIPCION.ani_simil(E.REMI_NU_DNI_EMI) IS NULL THEN '' ELSE ' - ' || PK_SGD_DESCRIPCION.ani_simil(E.REMI_NU_DNI_EMI) END ");
            sql.append(" ) LIKE '%'||UPPER(:pDesti)||'%' ");
            objectParam.put("pDesti", buscarDocumentoRecepMensajeriaBean.getBusDesti());
        }  
        /*-----Hermes 30/04/2019- Cambiar posición en campos de filtro módulo gestión de documentos-----*/
        if(buscarDocumentoRecepMensajeriaBean.getCoTipoEnvMsj()!= null && buscarDocumentoRecepMensajeriaBean.getCoTipoEnvMsj().trim().length()>0 && !buscarDocumentoRecepMensajeriaBean.getCoTipoEnvMsj().equals(".: TODOS :.")){
            sql.append(" AND DE_TIP_ENV = :pTipoPrioridad ");
            objectParam.put("pTipoPrioridad", buscarDocumentoRecepMensajeriaBean.getCoTipoEnvMsj());
        }
        
        if(buscarDocumentoRecepMensajeriaBean.getCoTipoMsj()!= null && buscarDocumentoRecepMensajeriaBean.getCoTipoMsj().trim().length()>0 && !buscarDocumentoRecepMensajeriaBean.getCoTipoMsj().equals(".: TODOS :.")){
            sql.append(" AND DE_TIP_MSJ = :pTipoMensajero ");
            objectParam.put("pTipoMensajero", buscarDocumentoRecepMensajeriaBean.getCoTipoMsj());
        }
        if(buscarDocumentoRecepMensajeriaBean.getTipoZona()!= null && buscarDocumentoRecepMensajeriaBean.getTipoZona().trim().length()>0 && !buscarDocumentoRecepMensajeriaBean.getTipoZona().equals(".: TODOS :.")){
            sql.append(" AND TIPO_ZONA = :pTipoZona ");
            objectParam.put("pTipoZona", buscarDocumentoRecepMensajeriaBean.getTipoZona());
        }
        /*-----Hermes 30/04/2019- Cambiar posición en campos de filtro módulo gestión de documentos-----*/
        /*----------------Hermes 15/08/2018-------------*/  
        sql.append(" AND A.CO_GRU = :pCoGru");        
        objectParam.put("pCoGru", buscarDocumentoRecepMensajeriaBean.getCoGrupo());
//        sql.append(" AND A.ES_DOC_EMI<>9");
        //sql.append(" AND A.TI_EMI<>'01'");
        sql.append(" AND A.ES_ELI='0'");
        //sql.append(" AND A.CO_DEP_EMI = :pCoDepEmi");        
        //objectParam.put("pCoDepEmi", buscarDocumentoRecepMensajeriaBean.getCoDepEmi());

        String pTipoBusqueda = buscarDocumentoRecepMensajeriaBean.getTipoBusqueda();
        if (pTipoBusqueda.equals("1") && buscarDocumentoRecepMensajeriaBean.isEsIncluyeFiltro()) {
            bBusqFiltro = true;
        }
        
        //String auxTipoAcceso=buscarDocumentoRecepMensajeriaBean.getTiAcceso();
        //String tiAcceso=auxTipoAcceso!=null&&(auxTipoAcceso.equals("0")||auxTipoAcceso.equals("1"))?auxTipoAcceso:"1";           
        //if(tiAcceso.equals("1")){//acceso personal
        //    sql.append(" AND A.CO_EMP_RES = :pcoEmpRes ");
        //    objectParam.put("pcoEmpRes", buscarDocumentoRecepMensajeriaBean.getCoEmpleado());            
        //}else {//acceso total
            //if(buscarDocumentoRecepMensajeriaBean.getInMesaPartes().equals("0") /*&& buscarDocumentoExtRecepBean.getInCambioEst().equals("0")*/){
             //   bBusqDep = true;
                //sql.append(" AND A.CO_DEP = :pCoDep"); 
        /*--22/08/19 HPB Devolucion Doc a Oficina--*/
        //if(!(buscarDocumentoRecepMensajeriaBean.getCoEstadoDoc().equals("")) && !(buscarDocumentoRecepMensajeriaBean.getCoEstadoDoc().equals("6"))){
        if(!(buscarDocumentoRecepMensajeriaBean.getCoEstadoDoc().equals("6"))){//HPB 25/10/19
            sql.append(" AND A.COD_DEP_MSJ = :pCoDep");         
            sql.append(" AND TI_ENV_MSJ='0'  ");
        }        
        /*--22/08/19 HPB Devolucion Doc a Oficina--*/
        objectParam.put("pCoDep", buscarDocumentoRecepMensajeriaBean.getCoDependencia());                   
           // }
        //} 
        //Filtro
        if (pTipoBusqueda.equals("0") || bBusqFiltro) {
            
             if (buscarDocumentoRecepMensajeriaBean.getCoDependenciaBusca()!= null && buscarDocumentoRecepMensajeriaBean.getCoDependenciaBusca().trim().length() > 0) {
                sql.append(" AND A.CO_DEP_EMI = :pCoDependenciaBusca ");
                objectParam.put("pCoDependenciaBusca", buscarDocumentoRecepMensajeriaBean.getCoDependenciaBusca());
            }   
            
            if (buscarDocumentoRecepMensajeriaBean.getCoTipoDoc()!= null && buscarDocumentoRecepMensajeriaBean.getCoTipoDoc().trim().length() > 0) {
                sql.append(" AND A.CO_TIP_DOC_ADM = :pCoDocEmi ");
                objectParam.put("pCoDocEmi", buscarDocumentoRecepMensajeriaBean.getCoTipoDoc());
            }            
            if (buscarDocumentoRecepMensajeriaBean.getCoEstadoDoc()!= null && buscarDocumentoRecepMensajeriaBean.getCoEstadoDoc().trim().length() > 0) {
                //sql.append(" AND A.ES_DOC_EMI = :pCoEsDocEmi ");
                sql.append(" AND A.DOC_ESTADO_MSJ = :pCoEsDocEmi ");
                objectParam.put("pCoEsDocEmi", buscarDocumentoRecepMensajeriaBean.getCoEstadoDoc());
            }
            if(buscarDocumentoRecepMensajeriaBean.getCoTipoEmisor()!= null && buscarDocumentoRecepMensajeriaBean.getCoTipoEmisor().trim().length() > 0){
                sql.append(" AND A.TI_EMI = :pCoTipoEmisor ");
                objectParam.put("pCoTipoEmisor", buscarDocumentoRecepMensajeriaBean.getCoTipoEmisor());                
            }
            /*if (buscarDocumentoExtRecepBean.getCoLocEmi()!= null && buscarDocumentoExtRecepBean.getCoLocEmi().trim().length() > 0 && !bBusqLocal) {
                sql.append(" AND A.CO_LOC_EMI = :pcoLocEmi ");
                objectParam.put("pcoLocEmi", buscarDocumentoExtRecepBean.getCoLocEmi());
            } */     
            if (buscarDocumentoRecepMensajeriaBean.getCoDepOriRec()!= null && buscarDocumentoRecepMensajeriaBean.getCoDepOriRec().trim().length() > 0 && !bBusqDep) {
                sql.append(" AND A.CO_DEP = :pCoDep");        
                objectParam.put("pCoDep", buscarDocumentoRecepMensajeriaBean.getCoDepOriRec());                
            }
            if (buscarDocumentoRecepMensajeriaBean.getEsFiltroFecha() != null  && (pEsFiltroFecha.equals("1") || pEsFiltroFecha.equals("3"))) {
                String vFeEmiIni = buscarDocumentoRecepMensajeriaBean.getFeEmiIni();
                String vFeEmiFin = buscarDocumentoRecepMensajeriaBean.getFeEmiFin();
                if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0  && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                    sql.append(" AND A.FE_EMI between TO_DATE(:pFeEmiIni,'DD/MM/YYYY') AND TO_DATE(:pFeEmiFin,'DD/MM/YYYY') + 0.99999");
                    objectParam.put("pFeEmiIni", vFeEmiIni);
                    objectParam.put("pFeEmiFin", vFeEmiFin);
                }
            }
            if (buscarDocumentoRecepMensajeriaBean.getEsFiltroFechaEnvMsj()!= null  && (pEsFiltroFechaENVmSJ.equals("1") || pEsFiltroFechaENVmSJ.equals("3"))) {
                String vFeEmiIni = buscarDocumentoRecepMensajeriaBean.getFeEmiIniEnvMSJ();
                String vFeEmiFin = buscarDocumentoRecepMensajeriaBean.getFeEmiFinEnvMSJ();
                if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0  && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                    sql.append(" AND A.FE_ENV_MES between TO_DATE(:pFeEmiIniEnv,'DD/MM/YYYY') AND TO_DATE(:pFeEmiFinEnv,'DD/MM/YYYY') + 0.99999");
                    objectParam.put("pFeEmiIniEnv", vFeEmiIni);
                    objectParam.put("pFeEmiFinEnv", vFeEmiFin);
                }
            }
        }   
        
        //Busqueda
        //if (pTipoBusqueda.equals("1")) {
            if (buscarDocumentoRecepMensajeriaBean.getBusNumDoc() != null && buscarDocumentoRecepMensajeriaBean.getBusNumDoc().trim().length() > 1) {
                sql.append(" AND B.NU_DOC LIKE '%'||:pnuDocEmi||'%' ");
                objectParam.put("pnuDocEmi", buscarDocumentoRecepMensajeriaBean.getBusNumDoc());
            }
            if (buscarDocumentoRecepMensajeriaBean.getBusNumExpediente() != null && buscarDocumentoRecepMensajeriaBean.getBusNumExpediente().trim().length() > 1) {
                sql.append(" AND B.NU_EXPEDIENTE LIKE '%'||:pnuExpediente||'%' ");
                objectParam.put("pnuExpediente", buscarDocumentoRecepMensajeriaBean.getBusNumExpediente());
            }
            if (buscarDocumentoRecepMensajeriaBean.getBusAsunto() != null && buscarDocumentoRecepMensajeriaBean.getBusAsunto().trim().length() > 1) {
                //sql.append(" AND CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextMixto(buscarDocumentoExtRecepBean.getBusAsunto())+"', 1 ) > 1 ");
                //sql.append(" AND CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue( buscarDocumentoExtRecepBean.getBusAsunto())+"', 1 ) > 1 ");
                sql.append(" AND UPPER(A.DE_ASU) LIKE '%'||:pDeAsunto||'%' ");
                objectParam.put("pDeAsunto", buscarDocumentoRecepMensajeriaBean.getBusAsunto());
            }            
      //  }        
        
        sql.append(" ORDER BY A.NU_COR_EMI DESC");
        sql.append(") X ");
        //System.out.println("SQL GESTION DE DOCUMENTOS: "+sql);
        logger.info("SQL GD-Mensajeria: "+ sql);
        List<DocumentoRecepMensajeriaBean> list = new ArrayList<DocumentoRecepMensajeriaBean>();

        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(),objectParam,BeanPropertyRowMapper.newInstance(DocumentoRecepMensajeriaBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;             
        }catch (Exception e) {
            list = null;
            e.printStackTrace();            
        }
        return list;
    }
    @Override
    public List<DestiDocumentoEnvMensajeriaBean> getLstDestinoEnvMensajeria(String pnuAnnpnuEmi) {
        StringBuilder sql = new StringBuilder();
        List<DestiDocumentoEnvMensajeriaBean> list = null;
        sql.append("select ROWNUM fila, a.nu_ann,a.nu_emi,a.nu_des,a.co_loc_des co_local,NVL2(a.co_loc_des,substr(PK_SGD_DESCRIPCION.DE_LOCAL(a.co_loc_des), 1, 100),NULL) de_local, \n" +
"                    B.co_dep_emi co_dependencia,\n" +
"                    NVL2(B.co_dep_emi,substr(PK_SGD_DESCRIPCION.DE_DEPENDENCIA(B.co_dep_emi), 1, 100),NULL) de_dependencia, \n" +
"                  --  a.co_emp_des co_empleado,\n" +
"                    --NVL2(a.co_emp_des,substr(PK_SGD_DESCRIPCION.DE_NOM_EMP(a.co_emp_des), 1, 100),NULL) de_empleado, \n" +
"                    PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM) documento,\n" +
"                    DECODE(A.TI_DES,\n" +
"                            '01', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.co_dep_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.co_emp_des),\n" +
"                            '02', PK_SGD_DESCRIPCION.DE_PROVEEDOR (A.NU_RUC_DES),\n" +
"                            '03', PK_SGD_DESCRIPCION.ANI_SIMIL (A.NU_DNI_DES),\n" +
"                            '04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (A.CO_OTR_ORI_DES),\n" +
"                            '05', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.co_dep_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES)\n" +
"                            )  destinatario,\n" +
"                    a.co_mot co_tramite,NVL2(A.co_mot,substr(PK_SGD_DESCRIPCION.MOTIVO(a.co_mot), 1, 100),NULL) de_tramite, \n" +
"                    a.co_pri co_prioridad, \n" +
/* [INICIO] - [VMBP - 15/08/2019] - agregar prioridad*/
"PK_SGD_DESCRIPCION.DE_PRIORIDAD (a.co_pri) de_prioridad, \n" +
/* [FIN] - [VMBP - 15/08/2019] - agregar prioridad*/                
"                    a.de_pro de_indicaciones, \n" +
"                    a.ti_des co_tipo_destino,\n" +
"                      a.CDIR_REMITE direccion,\n" +
"                            DECODE(A.TI_DES,\n" +
"                            '01', TRIM(DEP.NODEP)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODIS),\n" +
"                            '02', TRIM(UBIDESTINO.NODEP)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODIS),\n" +
"                            '03', TRIM(UBIDESTINO.NODEP)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODIS),\n" +
"                            '04', TRIM(UBIDESTINO.NODEP)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODIS),\n" +
"                            '05', TRIM(DEP.NODEP)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODIS)\n" +
"                            ) departamento,CCOD_TIPO_UBI AS ambito,\n" +
"                 DECODE (B.TI_EMI, \n" +
"                                 '01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG, \n" +
"                                '05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG, \n" +
"                                 B.DE_DOC_SIG ) NU_DOC,TO_CHAR(B.FE_EMI,'DD/MM/YYYY') FE_EMI_CORTA,a.CCOD_DPTO,a.CCOD_PROV,a.CCOD_DIST, \n" +               
"                           (SELECT COCLASERCOUR FROM IDTUBIAS WHERE UBDEP=a.CCOD_DPTO AND UBPRV=a.CCOD_PROV AND UBDIS=a.CCOD_DIST AND STUBI='1') CO_SERVICIO, \n" + //Hermes 30/07/2018
"(SELECT CELE_DESELE FROM SI_ELEMENTO WHERE CTAB_CODTAB = 'SERVICIO_COURRIER' AND CELE_CODELE = (SELECT COCLASERCOUR FROM IDTUBIAS WHERE UBDEP=a.CCOD_DPTO AND UBPRV=a.CCOD_PROV AND UBDIS=a.CCOD_DIST AND STUBI='1') AND ESTADO = '1') TI_ZONA, \n" + //Hermes 03/07/2018
"                           (select TO_CHAR(PK_SGD_CONSULTAS.CALCULAR_DIAS_HABILES(TO_DATE((SELECT TO_CHAR(SYSDATE, 'DD/MM/YYYY') FROM DUAL),'DD/MM/YYYY'), \n" + //Hermes 30/07/2018
"                           (SELECT COCLASERCOUR FROM IDTUBIAS WHERE UBDEP=a.CCOD_DPTO AND UBPRV=a.CCOD_PROV AND UBDIS=a.CCOD_DIST AND STUBI='1')),'DD/MM/YYYY') FROM DUAL) FE_PLA_MSJ \n" + //Hermes 30/07/2018
"                    FROM tdtv_destinos a INNER JOIN TDTV_REMITOS B ON a.NU_ANN = B.NU_ANN AND a.NU_EMI = B.NU_EMI\n" +
"                    LEFT JOIN ( SELECT D.CO_DEPENDENCIA,D.DE_DEPENDENCIA,D.DE_CORTA_DEPEN,L.DE_NOMBRE_LOCAL,L.DE_DIRECCION_LOCAL,NODEP,NOPRV,NODIS ,L.CCOD_LOCAL\n" +
"                                FROM RHTM_DEPENDENCIA D \n" +
"                                INNER JOIN SITM_LOCAL_DEPENDENCIA DL ON DL.CO_DEP=D.CO_DEPENDENCIA\n" +
"                                INNER JOIN SI_MAE_LOCAL L ON L.CCOD_LOCAL=DL.CO_LOC\n" +
"                                LEFT JOIN IDTUBIAS U ON U.UBDEP||U.UBPRV||U.UBDIS =L.CO_UBIGEO  ) DEP ON DEP.CO_DEPENDENCIA=A.co_dep_DES AND DEP.CCOD_LOCAL=a.co_loc_des " +
                " LEFT JOIN IDTUBIAS UBIDESTINO ON UBIDESTINO.UBDEP||UBIDESTINO.UBPRV||UBIDESTINO.UBDIS =a.CCOD_DPTO||a.CCOD_PROV||a.CCOD_DIST  \n"+
                    "where a.TI_DES <>'01' AND a.nu_ann || a.nu_emi in ("+pnuAnnpnuEmi+") \n" +
                    "AND a.ES_ELI='0' AND  a.ES_DOC_REC='1'  \n" +
                    "order by 1");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestiDocumentoEnvMensajeriaBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    
    
    @Override
    public List<TipoElementoMensajeriaBean> getlistTipoElementoMensajeria(String tipo){
                StringBuilder sql = new StringBuilder();        
        sql.append("select CELE_DESELE de_destinatario,CELE_CODELE co_destinatario from SI_ELEMENTO WHERE CTAB_CODTAB=? \n" +
                   " AND ESTADO = '1' \n" +         
                   " ORDER BY CELE_CODELE ");         
        List<TipoElementoMensajeriaBean> list = new ArrayList<TipoElementoMensajeriaBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(TipoElementoMensajeriaBean.class)
                    , new Object[]{tipo});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public List<TipoElementoMensajeriaBean> getListResponsableMensajeria(String tipo,String Ambito){
        StringBuilder sql = new StringBuilder();         
        //if (tipo.equals("MOTORIZADO")){
        //sql.append("SELECT CEMP_NU_DNI codigo,CEMP_APEPAT||' '||CEMP_APEMAT ||' '|| CEMP_DENOM nombre FROM RHTM_PER_EMPLEADOS WHERE CEMP_CO_CARGO='038'");         
        //}
        if(tipo.equals("COURRIER")){        //NELE_NUMSEC
        //sql.append("SELECT CPRO_RUC codigo ,CPRO_RAZSOC nombre FROM LG_PRO_PROVEEDOR WHERE CPRO_RUC IN ( select CELE_DESELE from SI_ELEMENTO WHERE CTAB_CODTAB='DE_COURRIER' AND CELE_CODELE2 IN (SELECT  CELE_CODELE FROM  si_elemento WHERE CTAB_CODTAB='DE_AMBITO_MENS' AND cele_desele='"+Ambito+"')  ) ");
//        sql.append("SELECT CPRO_RUC codigo ,CPRO_RAZSOC nombre FROM LG_PRO_PROVEEDOR WHERE CPRO_RUC IN (  select CU.CELE_DESELE from SI_ELEMENTO CU \n" +
//                    " INNER JOIN SI_ELEMENTO AM ON CU.cele_codele2=AM.cele_codele AND AM.CTAB_CODTAB='DE_AMBITO_MENS' \n" +
//                    " WHERE CU.CTAB_CODTAB='DE_COURRIER' AND AM.cele_desele='"+Ambito+"'  ) ");
        sql.append("SELECT CPRO_RUC codigo, CPRO_RAZSOC nombre FROM LG_PRO_PROVEEDOR\n" +
                   " WHERE CPRO_RUC IN (SELECT A.CELE_DESELE FROM SI_ELEMENTO A WHERE A.CTAB_CODTAB = 'COURRIERS' AND A.ESTADO = '1')");
        }
        else {
          sql.append("select NULEM codigo,DEAPP||' '||DEAPM||' '||DENOM nombre from TDTX_ANI_SIMIL where NULEM in (select CELE_DESELE from SI_ELEMENTO WHERE CTAB_CODTAB='"+tipo+"' AND ESTADO = '1')");      
        }
        List<TipoElementoMensajeriaBean> list = new ArrayList<TipoElementoMensajeriaBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(TipoElementoMensajeriaBean.class) );
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public String insMensajeriaDocumento(DocumentoRecepMensajeriaBean documentoMensajeria) {
        String vReturn = "NO_OK";
        StringBuilder sqlQry1 = new StringBuilder();
        StringBuilder sqlQry2 = new StringBuilder();
        sqlQry1.append("select SEC_TD_MENSAJERIA.NextVal from dual"); 
        sqlQry2.append("select to_char(sysdate,'dd/mm/yyyy hh24:mi:ss') from dual");        
        StringBuilder sqlIns = new StringBuilder();
        StringBuilder sqlUpd = new StringBuilder();
        StringBuilder sqlUpdRemito = new StringBuilder();
        StringBuilder sqlUpdRemitoParcial = new StringBuilder();

        sqlIns.append("INSERT INTO  td_mensajeria ( nu_msj,fe_reg_msj,co_use_cre,de_ambito,de_tip_msj,re_env_msj,\n" +
                    "    de_tip_env,nu_ser_msj, an_ser_msj, fec_enviomsj,ho_env_msj, flag_msj) \n" +
                    "    VALUES(?,(select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual),?,?,?,?,?,?,?, "
                  + " (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual),(select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual), '0')");

        try{
            String snuNumsj = this.jdbcTemplate.queryForObject(sqlQry1.toString(), String.class);            
            String snufecha = this.jdbcTemplate.queryForObject(sqlQry2.toString(), String.class);    
            documentoMensajeria.setNumsj(snuNumsj);
            documentoMensajeria.setFeregmsj(snufecha);

            sqlUpd.append("UPDATE TDTV_DESTINOS SET ES_DOC_REC='2', NU_MSJ='"+snuNumsj+"', FEC_ENVIOMSJ = (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual),"
                    + " ho_env_msj_D = (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual), fe_pla_msj_D = (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual),"
                    + " ho_pla_msj_D = (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual), dias_pla_entr_D = ?, dias_pla_devo_D = ? , PE_ENV_MSJ_D = ? "
                    + " WHERE  NU_ANN||NU_EMI||NU_DES in ( "+ documentoMensajeria.getCodigo()+" ) ");
            
            sqlUpdRemito.append("UPDATE TDTV_REMITOS  SET  FEC_ENVIODEP=SYSDATE,DOC_ESTADO_MSJ='2' \n" +
                    " WHERE  NU_ANN||NU_EMI IN ( SELECT NU_ANN||NU_EMI FROM  TDTV_DESTINOS WHERE NU_ANN||NU_EMI||NU_DES in (  "+documentoMensajeria.getCodigo()+"  ) ) ");
            sqlUpdRemitoParcial.append("UPDATE TDTV_REMITOS  SET  FEC_ENVIODEP=SYSDATE,DOC_ESTADO_MSJ='3' \n" +
"                     WHERE  NU_ANN||NU_EMI IN (SELECT NU_ANN||NU_EMI FROM  TDTV_DESTINOS WHERE ES_DOC_REC<>'2' \n" +
"                     and NU_ANN||NU_EMI in ( SELECT NU_ANN||NU_EMI FROM  TDTV_DESTINOS WHERE   NU_ANN||NU_EMI||NU_DES in (  "+documentoMensajeria.getCodigo()+"  )  ) )");

            this.jdbcTemplate.update(sqlIns.toString(), new Object[]{snuNumsj,snufecha,
            documentoMensajeria.getCousecre(), documentoMensajeria.getDeambito(), documentoMensajeria.getDetipmsj(), documentoMensajeria.getReenvmsj(),
            documentoMensajeria.getDetipenv(), documentoMensajeria.getNusermsj(), documentoMensajeria.getAnsermsj(), documentoMensajeria.getFecenviomsj(),
            documentoMensajeria.getHoenvmsj()});

            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoMensajeria.getFecenviomsj(),
                documentoMensajeria.getHoenvmsj(),documentoMensajeria.getFeplamsj(),documentoMensajeria.getHoplamsj(),
                documentoMensajeria.getDiasEntrega(),documentoMensajeria.getDiasDevoluvion(),documentoMensajeria.getCalculaPenalizacion()});
            this.jdbcTemplate.update(sqlUpdRemito.toString());          
            this.jdbcTemplate.update(sqlUpdRemitoParcial.toString());          
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Numero de Mensajeria Duplicado.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }
    
    @Override
    public String updMensajeriaDocumentoRecibir(String codigo, String usuario) {
        String vReturn = "NO_OK";    
        StringBuilder sqlIns = new StringBuilder(); 
        StringBuilder sqlUpd = new StringBuilder(); 
        //sqlIns.append("UPDATE TDTV_REMITOS  SET FEC_RECEPMP=SYSDATE,DOC_ESTADO_MSJ='1'  WHERE  NU_ANN||NU_EMI IN ("+codigo+" ) ");
        //sqlUpd.append("UPDATE TDTV_DESTINOS SET ES_DOC_REC='1'  WHERE TI_DES <>'01' AND NU_ANN||NU_EMI in ( "+ codigo +" ) ");
        sqlIns.append("UPDATE TDTV_REMITOS  SET FEC_RECEPMP=SYSDATE,DOC_ESTADO_MSJ='1', CO_USE_MOD='"+usuario+"', FE_USE_MOD=SYSDATE  WHERE  NU_ANN||NU_EMI IN ("+codigo+" ) ");/*[HPB-21/06/21]Fin Campos Auditoria-*/
        sqlUpd.append("UPDATE TDTV_DESTINOS SET ES_DOC_REC='1',CO_USE_MOD='"+usuario+"', FE_USE_MOD=SYSDATE  WHERE TI_DES <>'01' AND NU_ANN||NU_EMI in ( "+ codigo +" ) ");/*[HPB-21/06/21]Fin Campos Auditoria-*/
        try{
            this.jdbcTemplate.update(sqlIns.toString() );
            this.jdbcTemplate.update(sqlUpd.toString() );
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Numero de Mensajeria Duplicado.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }
      @Override
    public String updMensajeriaDocumentoDevolver(String codigo, String pEmiInsEstacionDoc, DocumentoEmiBean documentoEmiBean) {
        String vReturn = "NO_OK";    
        StringBuilder sqlIns = new StringBuilder();
        StringBuilder sqlUpd = new StringBuilder(); 

        sqlIns.append("UPDATE TDTV_REMITOS  SET  DOC_ESTADO_MSJ='6',COD_DEP_MSJ=NULL ,TI_ENV_MSJ='3', CO_EMP_DEV='"+documentoEmiBean.getCoEmpDev()+"', OBS_DEV='"+documentoEmiBean.getObsDev()+"', FE_DEV_OFI=SYSDATE WHERE  NU_ANN||NU_EMI IN ("+codigo+" ) ");/*--21/08/19 HPB Devolucion Doc a Oficina--*/ 
        sqlUpd.append("UPDATE TDTV_DESTINOS SET ES_DOC_REC='0'  WHERE TI_DES <>'01' AND NU_ANN||NU_EMI in ( "+ codigo +" ) ");
        String[] partes = pEmiInsEstacionDoc.split(",");/*--23/08/19 HPB Devolucion Doc a Oficina--*/ 
        String[] partes1 = documentoEmiBean.getFeEnvOfi().split(",");/*--23/08/19 HPB Devolucion Doc a Oficina--*/ 
        EstacionDocumento estacionDocumento = new EstacionDocumento();/*--23/08/19 HPB Devolucion Doc a Oficina--*/ 

        try{
            this.jdbcTemplate.update(sqlIns.toString() );
            this.jdbcTemplate.update(sqlUpd.toString() ); 
            vReturn = "OK";
            /*--23/08/19 HPB Devolucion Doc a Oficina--*/ 
            if(vReturn.equals("OK")){
                for (int i=0; i<partes.length; i++){
                    String nuEmision = partes[i];
                    String sSubCadenaAnio = nuEmision.substring(0,4);
                    String sSubCadenaEmi = nuEmision.substring(4,14);
                    String sSubCadenaEstado = documentoEmiBean.getDocEstadoMsj().substring(i, i+1);
                    documentoEmiBean.setNuAnn(sSubCadenaAnio);
                    documentoEmiBean.setNuEmi(sSubCadenaEmi);
                    documentoEmiBean.setFeEnvOfi(partes1[i]);
                    estacionDocumento.setEsDocIni(sSubCadenaEstado);
                    estacionDocumento.setEsDocFin("6");
                    estacionDocumento.setObsDev(documentoEmiBean.getObsDev());
                    vReturn = insEstacionDocumento(documentoEmiBean, estacionDocumento);
                }                            
            }else{
                vReturn = "NO_OK";
            }
            /*--23/08/19 HPB Devolucion Doc a Oficina--*/ 
        } catch (DuplicateKeyException con) {
            vReturn = "Numero de Mensajeria Duplicado.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }
    
    @Override
    public String selectCalcularFechaPlazo(DocumentoRecepMensajeriaBean documentoMensajeria) {
        String vReturn = "NO_OK";
        StringBuilder sqlQry0 = new StringBuilder();
        StringBuilder sqlQry1 = new StringBuilder();
        StringBuilder sqlQry2 = new StringBuilder();
        
        /*-------------------------------------------Hermes 20/07/2018--------------------------------------*/
        StringBuilder sqlQryUbi = new StringBuilder();
        StringBuilder sqlQryUbiOtro = new StringBuilder();
        String servicio ="";
        String sTipoServicioCourier ="";
        /*-------------------------------------------Fin 20/07/2018-----------------------------------------*/
        sqlQry0.append("SELECT TO_CHAR(NELE_NUMSEC) FROM SI_ELEMENTO WHERE CTAB_CODTAB='DE_TIP_MSJ_MENS' AND CELE_DESELE='"+documentoMensajeria.getDeTipEnv()+"'");  
        if(documentoMensajeria.getDeTipEnv().equals("COURRIER")){            
            
            sqlQryUbi.append("SELECT B.CELE_CODELE2 FROM SI_ELEMENTO B WHERE B.CTAB_CODTAB = 'COURRIERS' AND B.CELE_DESELE = '"+documentoMensajeria.getDeNuRuc()+"'");
            sTipoServicioCourier = this.jdbcTemplate.queryForObject(sqlQryUbi.toString(), String.class);

            if(sTipoServicioCourier.equals("C")){
                if(documentoMensajeria.getDeambito().equals("LOCAL"))
                    servicio = "03";
                else if(documentoMensajeria.getDeambito().equals("NACIONAL"))
                    servicio = "08";
                else
                    servicio = "09";
            }else{
                sqlQryUbiOtro.append("SELECT COCLASERCOUR FROM IDTUBIAS WHERE UBDEP='"+documentoMensajeria.getCoUbiDptoDestinoMsj()+"' AND UBPRV='"+documentoMensajeria.getCoUbiProvDestinoMsj()+"' AND UBDIS='"+documentoMensajeria.getCoUbiDistDestinoMsj()+"' AND STUBI='1'");
                servicio = this.jdbcTemplate.queryForObject(sqlQryUbiOtro.toString(), String.class);
            }
                    
//           sqlQry1.append("SELECT  CELE_DESCOR FROM  si_elemento WHERE CTAB_CODTAB='DE_COURRIER' AND cele_desele='"+documentoMensajeria.getDeNuRuc()+"'  \n" +
//                    "AND cele_codele2 IN (SELECT  cele_codele FROM  si_elemento WHERE CTAB_CODTAB='DE_AMBITO_MENS' AND cele_desele='"+documentoMensajeria.getDeambito()+"')");
            sqlQry1.append("SELECT A.DI_ENTREGA || '|' ||A.DI_DEVOLUCION \n" +
                           "  FROM TDTM_COURIER_PLAZOS A WHERE A.CO_COURIER IN \n" +
                           "       (SELECT B.CELE_CODELE FROM SI_ELEMENTO B WHERE B.CTAB_CODTAB = 'COURRIERS' AND B.CELE_DESELE = '"+documentoMensajeria.getDeNuRuc()+"') \n" +
                           "   AND A.CO_TIPO_SERVICIO IN \n" +
                           " (SELECT cele_codele FROM si_elemento WHERE CTAB_CODTAB = 'SERVICIO_COURRIER' AND cele_codele = '"+servicio+"' )");
//                           "       (SELECT cele_codele FROM si_elemento WHERE CTAB_CODTAB = 'SERVICIO_COURRIER' AND cele_codele = '"+servicio+"' \n" +
//                           "           AND CELE_CODELE2 IN \n" +
//                           "           (SELECT CELE_CODELE FROM SI_ELEMENTO WHERE CTAB_CODTAB = 'DE_AMBITO_MENS' AND CELE_DESELE = '"+documentoMensajeria.getDeambito()+"'))");
        }
        else {
           sqlQry1.append("SELECT  CELE_DESCOR FROM  si_elemento WHERE CTAB_CODTAB='"+documentoMensajeria.getDeTipEnv()+"' AND cele_desele='"+documentoMensajeria.getDeNuRuc()+"'  ");
        }
        try{
            String confCalculaPena = this.jdbcTemplate.queryForObject(sqlQry0.toString(), String.class);
            if(confCalculaPena.equals("1")){
                String sdias = this.jdbcTemplate.queryForObject(sqlQry1.toString(), String.class);
                 String sdiasDev="";
                 if(sdias!=""){
                 String[] dias = sdias.split(Pattern.quote("|"));
                 if(dias.length>0){
                     sdias=dias[0];
                     sdiasDev=dias[1];
                 }else {
                 sdias="-1";
                 sdiasDev="-1";
                 }
                 }
                 else {
                 sdias="-1";
                 sdiasDev="-1";
                 }
                sqlQry2.append("select TO_CHAR(PK_SGD_CONSULTAS.CALCULAR_DIAS_HABILES(TO_DATE('"+documentoMensajeria.getFecenviomsj()+"','DD/MM/YYYY'),"+sdias+"),'DD/MM/YYYY') FROM DUAL");  
                String snufecha = this.jdbcTemplate.queryForObject(sqlQry2.toString(), String.class); 

                documentoMensajeria.setFeVence(snufecha);  
                documentoMensajeria.setDiasDevoluvion(sdiasDev); 
                documentoMensajeria.setDiasEntrega(sdias); 
                
            }
            else {
            documentoMensajeria.setFeVence(documentoMensajeria.getFecenviomsj());  
            documentoMensajeria.setDiasDevoluvion("0"); 
            documentoMensajeria.setDiasEntrega("0"); 
            }
            documentoMensajeria.setCalculaPenalizacion(confCalculaPena); 
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }
    
    
    @Override
    public List<DocumentoRecepMensajeriaBean> getLstDetalleMensajeria(DestinoResBen oDestinoResBen) {
        StringBuilder sql = new StringBuilder();
        List<DocumentoRecepMensajeriaBean> list = null;
        sql.append("SELECT M.NU_MSJ as numsj,M.DE_TIP_MSJ as detipmsj, M.DE_AMBITO as deambito, D.OB_MSJ as obMsj,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION.DE_DOMINIOS('SERVICIO_COURRIER', \n" +
                    "(SELECT COCLASERCOUR FROM IDOSGD.IDTUBIAS WHERE UBDEP = D.CCOD_DPTO AND UBPRV = D.CCOD_PROV AND UBDIS = D.CCOD_DIST AND STUBI = '1')) tiZona, \n" +
                    "TO_CHAR(D.FE_ENT_MSJ,'dd/MM/yyyy HH24:MI') feEntMsj,TO_CHAR(D.FE_DEV_MSJ,'dd/MM/yyyy HH24:MI') feDevMsj, D.MO_MSJ_DEV moMsjDev, \n" +
                    "CASE WHEN TOTAL>0 THEN 'SI' ELSE 'NO' END tieneanexocargo, \n" +
                    "(CASE WHEN M.DE_TIP_MSJ='MOTORIZADO' THEN R.CEMP_APEPAT||' '||R.CEMP_APEMAT ||' '|| R.CEMP_DENOM\n" +
                    "ELSE P.CPRO_RAZSOC\n" +
                    "END) as reenvmsj\n" +
                    ",NVL2(M.FEC_ENVIOMSJ, TO_CHAR(M.FEC_ENVIOMSJ, 'DD/MM/YYYY hh24:mi:ss'), '') FEC_ENVIOMSJ,M.DE_TIP_ENV,\n" +
                    //"PK_SGD_DESCRIPCION.ESTADOS (D.ES_DOC_REC,'TD_MENSAJERIA') docEstadoMsj,FE_PLA_MSJ\n" +
                    "PK_SGD_DESCRIPCION.ESTADOS (D.ES_DOC_REC,'TD_MENSAJERIA') docEstadoMsj,NVL2(D.FE_PLA_MSJ_D, TO_CHAR(D.FE_PLA_MSJ_D, 'DD/MM/YYYY hh24:mi:ss'), '') FE_PLA_MSJ_D\n" +
                    "FROM TDTV_DESTINOS D \n" +
                    "INNER JOIN TD_MENSAJERIA M ON M.NU_MSJ=D.NU_MSJ\n" +
                    "LEFT JOIN RHTM_PER_EMPLEADOS R ON R.CEMP_NU_DNI=M.RE_ENV_MSJ \n" +
                    "LEFT JOIN LG_PRO_PROVEEDOR P ON P.CPRO_RUC=M.RE_ENV_MSJ "+ 
                    "LEFT JOIN ( select  nu_ann,nu_emi,nu_des, COUNT(1) TOTAL    from TDTV_ANEXOS_MSJ  GROUP BY nu_ann,nu_emi,nu_des ) TB ON  TB.nu_ann = D.NU_ANN  AND TB.nu_emi=D.nu_emi AND TB.nu_des=D.nu_des "+
                    " where D.TI_DES <>'01' AND D.nu_ann="+oDestinoResBen.getNuAnn().toString()+
                    " AND D.NU_EMI="+oDestinoResBen.getNuEmi()+
                    " AND D.NU_DES="+oDestinoResBen.getNuDes()+
                    " order by 1");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoRecepMensajeriaBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
      @Override
    public List<DestiDocumentoEnvMensajeriaBean> getEditLstDetalleMensajeria(String  nroMensajeria) {
        StringBuilder sql = new StringBuilder();
        List<DestiDocumentoEnvMensajeriaBean> list = null;
       
         sql.append("select ROWNUM fila, a.nu_ann,a.nu_emi,a.nu_des,a.co_loc_des co_local,NVL2(a.co_loc_des,substr(PK_SGD_DESCRIPCION.DE_LOCAL(a.co_loc_des), 1, 100),NULL) de_local, \n" +
"                    B.co_dep_emi co_dependencia,\n" +
"                    NVL2(B.co_dep_emi,substr(PK_SGD_DESCRIPCION.DE_DEPENDENCIA(B.co_dep_emi), 1, 100),NULL) de_dependencia, \n" +
"                    PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM) documento,\n" +
"                    DECODE(A.TI_DES,\n" +
"                            '01', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.co_dep_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.co_emp_des),\n" +
"                            '02', PK_SGD_DESCRIPCION.DE_PROVEEDOR (A.NU_RUC_DES) ||'-'|| DECODE(A.REMI_TI_EMI, '03', PK_SGD_DESCRIPCION.ANI_SIMIL (A.REMI_NU_DNI_EMI), '04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (A.REMI_CO_OTR_ORI_EMI) )  ,\n" +
"                            '03', PK_SGD_DESCRIPCION.ANI_SIMIL (A.NU_DNI_DES),\n" +
"                            '04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (A.CO_OTR_ORI_DES),\n" +
"                            '05', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.co_dep_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES)\n" +
"                            ) destinatario,\n" +
"                    a.co_mot co_tramite,NVL2(A.co_mot,substr(PK_SGD_DESCRIPCION.MOTIVO(a.co_mot), 1, 100),NULL) de_tramite, \n" +
"                    a.co_pri co_prioridad, \n" +
"                    a.de_pro de_indicaciones, \n" +
"                    a.ti_des co_tipo_destino,\n" +
"                      a.CDIR_REMITE direccion,\n" +
"                            DECODE(A.TI_DES,\n" +
"                            '01', TRIM(DEP.NODEP)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODIS),\n" +
"                            '02', TRIM(UBIDESTINO.NODEP)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODIS),\n" +
"                            '03', TRIM(UBIDESTINO.NODEP)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODIS),\n" +
"                            '04', TRIM(UBIDESTINO.NODEP)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODIS),\n" +
"                            '05', TRIM(DEP.NODEP)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODIS)\n" +
"                            ) departamento,CCOD_TIPO_UBI AS ambito,\n" +
"                 DECODE (B.TI_EMI, \n" +
"                                 '01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG, \n" +
"                                '05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG, \n" +
"                                 B.DE_DOC_SIG ) NU_DOC  ,TO_CHAR(B.FE_EMI,'DD/MM/YYYY') FE_EMI_CORTA,M.FEC_ENVIOMSJ, a.DE_CARGO,M.NU_SER_MSJ||'-'||M.AN_SER_MSJ guia, M.DE_AMBITO ambito, \n" +
"(SELECT CELE_DESELE FROM SI_ELEMENTO WHERE CTAB_CODTAB = 'SERVICIO_COURRIER' AND CELE_CODELE = (SELECT COCLASERCOUR FROM IDTUBIAS WHERE UBDEP = a.CCOD_DPTO AND UBPRV = a.CCOD_PROV \n" +
" AND UBDIS = a.CCOD_DIST AND STUBI = '1')AND ESTADO = '1') TI_ZONA \n" +
"                    FROM tdtv_destinos a INNER JOIN TDTV_REMITOS B ON a.NU_ANN = B.NU_ANN AND a.NU_EMI = B.NU_EMI\n" +
          "          INNER JOIN TD_MENSAJERIA M ON M.NU_MSJ=a.NU_MSJ \n"+     
"                    LEFT JOIN ( SELECT D.CO_DEPENDENCIA,D.DE_DEPENDENCIA,D.DE_CORTA_DEPEN,L.DE_NOMBRE_LOCAL,L.DE_DIRECCION_LOCAL,NODEP,NOPRV,NODIS ,L.CCOD_LOCAL\n" +
"                                FROM RHTM_DEPENDENCIA D \n" +
"                                INNER JOIN SITM_LOCAL_DEPENDENCIA DL ON DL.CO_DEP=D.CO_DEPENDENCIA\n" +
"                                INNER JOIN SI_MAE_LOCAL L ON L.CCOD_LOCAL=DL.CO_LOC\n" +
"                                LEFT JOIN IDTUBIAS U ON U.UBDEP||U.UBPRV||U.UBDIS =L.CO_UBIGEO  ) DEP ON DEP.CO_DEPENDENCIA=A.co_dep_DES AND DEP.CCOD_LOCAL=a.co_loc_des " +
                " LEFT JOIN IDTUBIAS UBIDESTINO ON UBIDESTINO.UBDEP||UBIDESTINO.UBPRV||UBIDESTINO.UBDIS =a.CCOD_DPTO||a.CCOD_PROV||a.CCOD_DIST  \n"+
                    "where a.TI_DES <>'01' AND M.NU_MSJ="+nroMensajeria+"   \n" +                
                    "order by 1");

        
        
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestiDocumentoEnvMensajeriaBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DestiDocumentoEnvMensajeriaBean> getLstDestinoEnvMensajeria2(String pnuAnnpnuEmi, DocumentoRecepMensajeriaBean documentoRecepMensajeriaBean) {
        StringBuilder sqlQry0 = new StringBuilder();
        List<DestiDocumentoEnvMensajeriaBean> list = null;

        sqlQry0.append("SELECT TO_CHAR(NELE_NUMSEC) FROM SI_ELEMENTO WHERE CTAB_CODTAB='DE_TIP_MSJ_MENS' AND CELE_DESELE='"+documentoRecepMensajeriaBean.getDetipmsj()+"'");  
        
        try {
            String confCalculaPena = this.jdbcTemplate.queryForObject(sqlQry0.toString(), String.class);
            if(confCalculaPena.equals("1")){
            
            }else {
                //documentoMensajeria.setFeVence(documentoMensajeria.getFecenviomsj());  
                //documentoMensajeria.setDiasDevoluvion("0"); 
                //documentoMensajeria.setDiasEntrega("0"); 
            }
            list = this.jdbcTemplate.query(sqlQry0.toString(), BeanPropertyRowMapper.newInstance(DestiDocumentoEnvMensajeriaBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String insMensajeriaDocumento2(DocumentoRecepMensajeriaBean documentoMensajeria, List<DestiDocumentoEnvMensajeriaBean> lstDestinos) {
        String vReturn = "NO_OK";      
        StringBuilder sqlIns = new StringBuilder();
        StringBuilder sqlQryCFP0 = new StringBuilder();
        StringBuilder sqlQryCFP1 = new StringBuilder();
        StringBuilder sqlQryCFP2 = new StringBuilder();               
        StringBuilder sqlQryUbi = new StringBuilder();
        StringBuilder sqlQryUbiOtro = new StringBuilder();        
        StringBuilder sqlQry1 = new StringBuilder();
        StringBuilder sqlQry2 = new StringBuilder();
        String sTipoServicioCourier ="";        
        String servicio ="";
        String horaPlazoMsj = documentoMensajeria.getHoplamsj().substring(11, 19);                       
        
        StringBuilder sqlUrgente = new StringBuilder();
        
        sqlQry1.append("select SEC_TD_MENSAJERIA.NextVal from dual"); 
        sqlQry2.append("select to_char(sysdate,'dd/mm/yyyy hh24:mi:ss') from dual");  
        
        sqlQryCFP0.append("SELECT TO_CHAR(NELE_NUMSEC) FROM SI_ELEMENTO WHERE CTAB_CODTAB='DE_TIP_MSJ_MENS' AND CELE_DESELE='"+documentoMensajeria.getDetipmsj()+"'"); 
        
        sqlIns.append("INSERT INTO  td_mensajeria ( nu_msj,fe_reg_msj,co_use_cre,de_ambito,de_tip_msj,re_env_msj,\n" +
            " CO_USE_MOD, FE_USE_MOD, \n" + /*[HPB-21/06/21] Campos Auditoria-*/
            " de_tip_env,nu_ser_msj, an_ser_msj, fec_enviomsj,ho_env_msj, fe_pla_msj, ho_pla_msj, flag_msj) \n" +
            " VALUES(?,(select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual),?,?,?,?,?, SYSDATE, ?,?,?, \n" +
            " (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual),(select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual),\n" +
            " (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual),(select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual), '0')");

        if(documentoMensajeria.getDetipmsj().equals("COURRIER")){//Tipo de mensajero            
            sqlQryUbi.append("SELECT B.CELE_CODELE2 FROM SI_ELEMENTO B WHERE B.CTAB_CODTAB = 'COURRIERS' AND B.CELE_DESELE = '"+documentoMensajeria.getReenvmsj()+"'");
            sTipoServicioCourier = this.jdbcTemplate.queryForObject(sqlQryUbi.toString(), String.class);

            if(sTipoServicioCourier.equals("C")){//Tipo de servicio courier por ambito segun modalidad de courier
                if(documentoMensajeria.getDeambito().equals("LOCAL"))
                    servicio = "03";
                else if(documentoMensajeria.getDeambito().equals("NACIONAL"))
                    servicio = "08";
                else
                    servicio = "09";
                
                if(documentoMensajeria.getDetipenv().equals("NORMAL")){
                    sqlQryCFP1.append("SELECT A.DI_ENTREGA || '|' ||A.DI_DEVOLUCION \n" +
                        " FROM TDTM_COURIER_PLAZOS A WHERE A.CO_COURIER IN \n" +
                        " (SELECT B.CELE_CODELE FROM SI_ELEMENTO B WHERE B.CTAB_CODTAB = 'COURRIERS' AND B.CELE_DESELE = '"+documentoMensajeria.getReenvmsj()+"') \n" +
                        " AND A.CO_TIPO_SERVICIO IN \n" +
                        " (SELECT cele_codele FROM si_elemento WHERE CTAB_CODTAB = 'SERVICIO_COURRIER' AND cele_codele = '"+servicio+"' )");                
                }else{//URGENTE
                    if(servicio.equals("03") || servicio.equals("08")){
                        sqlQryCFP1.append("SELECT CELE_DESCOR FROM SI_ELEMENTO WHERE CTAB_CODTAB='SERVICIO_COURRIER' AND CELE_CODELE='"+servicio+"'");
                    }
                }
                
//                sqlQryCFP1.append("SELECT A.DI_ENTREGA || '|' ||A.DI_DEVOLUCION \n" +
//                    " FROM TDTM_COURIER_PLAZOS A WHERE A.CO_COURIER IN \n" +
//                    " (SELECT B.CELE_CODELE FROM SI_ELEMENTO B WHERE B.CTAB_CODTAB = 'COURRIERS' AND B.CELE_DESELE = '"+documentoMensajeria.getReenvmsj()+"') \n" +
//                    " AND A.CO_TIPO_SERVICIO IN \n" +
//                    " (SELECT cele_codele FROM si_elemento WHERE CTAB_CODTAB = 'SERVICIO_COURRIER' AND cele_codele = '"+servicio+"' )");
            }else{//Tipo de servicio courier por tipo de acceso(facil acceso, dificil acceso, etc)
                for (int i=0; i< lstDestinos.size(); i++){//Recorre el listado de documentos a enviar
                    StringBuilder sqlQryCFP3 = new StringBuilder();
                    StringBuilder sqlQryCFP4 = new StringBuilder();
                    
                    if(documentoMensajeria.getDetipenv().equals("NORMAL")){//Prioridad NORMAL
                        sqlQryCFP3.append("SELECT A.DI_ENTREGA || '|' ||A.DI_DEVOLUCION \n" +
                            " FROM TDTM_COURIER_PLAZOS A WHERE A.CO_COURIER IN \n" +
                            " (SELECT B.CELE_CODELE FROM SI_ELEMENTO B WHERE B.CTAB_CODTAB = 'COURRIERS' AND B.CELE_DESELE = '"+documentoMensajeria.getReenvmsj()+"') \n" +
                            " AND A.CO_TIPO_SERVICIO IN \n" +
                            " (SELECT cele_codele FROM si_elemento WHERE CTAB_CODTAB = 'SERVICIO_COURRIER' AND cele_codele = '"+lstDestinos.get(i).getCoServicio()+"' )");                    
                    }else{//Prioridad URGENTE
                        if(documentoMensajeria.getDeambito().equals("LOCAL") && lstDestinos.get(i).getCoServicio().equals("01")){
                            sqlQryCFP3.append("SELECT CELE_DESCOR FROM SI_ELEMENTO WHERE CTAB_CODTAB='SERVICIO_COURRIER' AND CELE_CODELE='"+lstDestinos.get(i).getCoServicio()+"'");
                        }
                        if(documentoMensajeria.getDeambito().equals("NACIONAL") && lstDestinos.get(i).getCoServicio().equals("04")){
                            sqlQryCFP3.append("SELECT CELE_DESCOR FROM SI_ELEMENTO WHERE CTAB_CODTAB='SERVICIO_COURRIER' AND CELE_CODELE='"+lstDestinos.get(i).getCoServicio()+"'");
                        }                        
                    }

                    
                    try {
                        //Calcula dias de entrega y devolucion
                        String confCalculaPena = this.jdbcTemplate.queryForObject(sqlQryCFP0.toString(), String.class);//Verifica si corresponde penalidad(0 no; 1 si)
                        if(confCalculaPena.equals("1")){//si le corresponde penalidad (solo courier)
                            String sdias = this.jdbcTemplate.queryForObject(sqlQryCFP3.toString(), String.class);// Días de entrega y devolucion en base al courier y tipo de servicio
                            String sdiasDev="";
                            if(sdias!=""){
                                String[] dias = sdias.split(Pattern.quote("|"));
                                if(dias.length>0){
                                    sdias=dias[0];//Días de entrega
                                    sdiasDev=dias[1];//Días de devolucion
                                }else {
                                    sdias="-1";
                                    sdiasDev="-1";
                                }
                            }else {
                                sdias="-1";
                                sdiasDev="-1";
                            }
                            sqlQryCFP4.append("select TO_CHAR(PK_SGD_CONSULTAS.CALCULAR_DIAS_HABILES(TO_DATE('"+documentoMensajeria.getFecenviomsj()+"','DD/MM/YYYY hh24:mi:ss'),"+sdias+"),'DD/MM/YYYY') FROM DUAL");  
                            String snufecha1 = this.jdbcTemplate.queryForObject(sqlQryCFP4.toString(), String.class);//En base a los días de entrega calcula la fecha de plazo de entrega 
                            String newFecha = snufecha1+" "+horaPlazoMsj;
                            lstDestinos.get(i).setFePlaMsj(newFecha);
                            lstDestinos.get(i).setHoPlaMsj(newFecha);
                            lstDestinos.get(i).setDiasDevolucion(sdiasDev);
                            lstDestinos.get(i).setDiasEntrega(sdias);
                            lstDestinos.get(i).setCalculaPenalizacion(confCalculaPena);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }                
            }                    
        }else {//Tipo de mensajero diferente a courier
            sqlQryCFP1.append("SELECT  CELE_DESCOR FROM  si_elemento WHERE CTAB_CODTAB='"+documentoMensajeria.getDetipmsj()+"' AND cele_desele='"+documentoMensajeria.getReenvmsj()+"'  ");
        }
        
        try{
            String snuNumsj = this.jdbcTemplate.queryForObject(sqlQry1.toString(), String.class);            
            String snufecha = this.jdbcTemplate.queryForObject(sqlQry2.toString(), String.class);    
            documentoMensajeria.setNumsj(snuNumsj);
            documentoMensajeria.setFeregmsj(snufecha);
                                    
            String confCalculaPena = this.jdbcTemplate.queryForObject(sqlQryCFP0.toString(), String.class);
            if(confCalculaPena.equals("1")){
                if(sTipoServicioCourier.equals("C")){//Calcula dias por ambito
                    String sdias = this.jdbcTemplate.queryForObject(sqlQryCFP1.toString(), String.class);
                    String sdiasDev="";
                    if(sdias!=""){
                        String[] dias = sdias.split(Pattern.quote("|"));
                        if(dias.length>0){
                            sdias=dias[0];
                            sdiasDev=dias[1];
                        }else {
                            sdias="-1";
                            sdiasDev="-1";
                        }
                    }else {
                        sdias="-1";
                        sdiasDev="-1";
                    }
                    
                    sqlQryCFP2.append("select TO_CHAR(PK_SGD_CONSULTAS.CALCULAR_DIAS_HABILES(TO_DATE('"+documentoMensajeria.getFecenviomsj()+"','DD/MM/YYYY hh24:mi:ss'),"+sdias+"),'DD/MM/YYYY') FROM DUAL");  
                    String snufecha1 = this.jdbcTemplate.queryForObject(sqlQryCFP2.toString(), String.class); 
                    String newFecha = snufecha1+" "+horaPlazoMsj;
                    for (int i=0; i< lstDestinos.size(); i++){
                        lstDestinos.get(i).setFePlaMsj(newFecha);
                        lstDestinos.get(i).setHoPlaMsj(newFecha);
                        lstDestinos.get(i).setDiasDevolucion(sdiasDev);
                        lstDestinos.get(i).setDiasEntrega(sdias);
                        lstDestinos.get(i).setCalculaPenalizacion(confCalculaPena);
                    }
                }               
            }else {//Si tipo de mensajero es diferente a courier no tiene penalidad ni dias de entrega y devolucion
                for (int i=0; i< lstDestinos.size(); i++){
                    lstDestinos.get(i).setFePlaMsj(documentoMensajeria.getFecenviomsj());
                    lstDestinos.get(i).setHoPlaMsj(documentoMensajeria.getFecenviomsj());
                    lstDestinos.get(i).setDiasDevolucion("0");
                    lstDestinos.get(i).setDiasEntrega("0");
                    lstDestinos.get(i).setCalculaPenalizacion(confCalculaPena);
                }
                if(!(documentoMensajeria.getDetipmsj().equals("COURRIER") || documentoMensajeria.getDetipmsj().equals("MOTORIZADO"))){
                    documentoMensajeria.setReenvmsj("");
                }
            }      
            
            /*---------------Obtiene la menor fecha de plazo----------*/
             Collections.sort(lstDestinos, new Comparator<DestiDocumentoEnvMensajeriaBean>() {
                @Override
                public int compare(DestiDocumentoEnvMensajeriaBean o1, DestiDocumentoEnvMensajeriaBean o2) {
                    //return o1.compareTo(o2);
                    return o1.getFePlaMsj().compareTo(o2.getFePlaMsj());
                }
            });
             
            for(int i=0; i < lstDestinos.size(); i++){
                logger.info(lstDestinos.get(i).getFePlaMsj().toString());
            }
            /*---------------Obtiene la menor fecha de plazo----------*/
            
            this.jdbcTemplate.update(sqlIns.toString(), new Object[]{snuNumsj,snufecha,
                documentoMensajeria.getCousecre(), documentoMensajeria.getDeambito(), documentoMensajeria.getDetipmsj(), documentoMensajeria.getReenvmsj(),
                documentoMensajeria.getCousecre(),/*[HPB-21/06/21] Campos Auditoria-*/
                documentoMensajeria.getDetipenv(), documentoMensajeria.getNusermsj(), documentoMensajeria.getAnsermsj(), documentoMensajeria.getFecenviomsj(),
                documentoMensajeria.getHoenvmsj(), lstDestinos.get(0).getFePlaMsj(), lstDestinos.get(0).getFePlaMsj()});
                        
            for(int i=0; i < lstDestinos.size(); i++){
                StringBuilder sqlUpd = new StringBuilder();
                StringBuilder sqlUpdRemito = new StringBuilder();
                StringBuilder sqlUpdRemitoParcial = new StringBuilder();
                
                sqlUpd.append("UPDATE TDTV_DESTINOS SET ES_DOC_REC='2', NU_MSJ='"+snuNumsj+"', FEC_ENVIOMSJ = (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual),"
                    + " ho_env_msj_D = (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual), fe_pla_msj_D = (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual),"
                    + " ho_pla_msj_D = (select to_date(?,'dd/mm/yyyy hh24:mi:ss') from dual), dias_pla_entr_D = ?, dias_pla_devo_D = ? , PE_ENV_MSJ_D = ? "
                    + " ,CO_USE_MOD = ?, FE_USE_MOD = SYSDATE " /*[HPB-21/06/21] Campos Auditoria-*/
                    + " WHERE  NU_ANN = '"+lstDestinos.get(i).getNuAnn()+"' AND NU_EMI ='"+lstDestinos.get(i).getNuEmi()+"' AND NU_DES ='"+lstDestinos.get(i).getNuDes()+"'");
                
                sqlUpdRemito.append("UPDATE TDTV_REMITOS  SET  FEC_ENVIODEP=SYSDATE,DOC_ESTADO_MSJ='2' \n" +
                    " ,CO_USE_MOD = '"+documentoMensajeria.getCoUseMod()+"', FE_USE_MOD = SYSDATE \n" +/*[HPB-21/06/21] Campos Auditoria-*/
                    " WHERE  NU_ANN||NU_EMI IN ( SELECT NU_ANN||NU_EMI FROM  TDTV_DESTINOS WHERE NU_ANN = '"+lstDestinos.get(i).getNuAnn()+"' \n" +
                    " AND NU_EMI = '"+lstDestinos.get(i).getNuEmi()+"' AND NU_DES = '"+lstDestinos.get(i).getNuDes()+"') ");
                
                sqlUpdRemitoParcial.append("UPDATE TDTV_REMITOS  SET  FEC_ENVIODEP=SYSDATE,DOC_ESTADO_MSJ='3' \n" +
                    " ,CO_USE_MOD = '"+documentoMensajeria.getCoUseMod()+"', FE_USE_MOD = SYSDATE \n" +/*[HPB-21/06/21] Campos Auditoria-*/
                    " WHERE  NU_ANN||NU_EMI IN (SELECT NU_ANN||NU_EMI FROM  TDTV_DESTINOS WHERE ES_DOC_REC<>'2' \n" +
                    " and NU_ANN||NU_EMI in (SELECT NU_ANN||NU_EMI FROM  TDTV_DESTINOS WHERE   NU_ANN = '"+lstDestinos.get(i).getNuAnn()+"' \n" +
                    " AND NU_EMI = '"+lstDestinos.get(i).getNuEmi()+"' AND NU_DES = '"+lstDestinos.get(i).getNuDes()+"'))");
                
                this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{documentoMensajeria.getFecenviomsj(),
                    documentoMensajeria.getHoenvmsj(),lstDestinos.get(i).getFePlaMsj(),lstDestinos.get(i).getHoPlaMsj(),
                    lstDestinos.get(i).getDiasEntrega(),lstDestinos.get(i).getDiasDevolucion(),lstDestinos.get(i).getCalculaPenalizacion(),
                    documentoMensajeria.getCoUseMod()});/*[HPB-21/06/21] Campos Auditoria-*/
                this.jdbcTemplate.update(sqlUpdRemito.toString());          
                this.jdbcTemplate.update(sqlUpdRemitoParcial.toString());          

            }                        
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }               
        return vReturn;             
    }    

     /*--23/08/19 HPB Devolucion Doc a Oficina--*/ 
    @Override
    public String insEstacionDocumento(DocumentoEmiBean documentoEmiBean, EstacionDocumento estacionDocumento) {
        String mensaje = "NO_OK";
        this.spInsertaEstacionDoc = new SimpleJdbcCall(this.dataSource).withCatalogName("PK_SGD_TRAMITE").withProcedureName("SP_INS_DOC_EST")
                .withoutProcedureColumnMetaDataAccess()
                .useInParameterNames("p_vNU_ANN", "p_vNU_EMI", "p_vES_DOC_INI", "p_vES_DOC_FIN", "p_vOBS_DEV", "p_vUSER", "p_vFE_ENV_OFI", "p_vOK")                                        
                .declareParameters(
                        new SqlParameter("p_vNU_ANN", Types.VARCHAR),
                        new SqlParameter("p_vNU_EMI", Types.VARCHAR),
                        new SqlParameter("p_vES_DOC_INI", Types.VARCHAR),
                        new SqlParameter("p_vES_DOC_FIN", Types.VARCHAR),
                        new SqlParameter("p_vOBS_DEV", Types.VARCHAR),
                        new SqlParameter("p_vUSER", Types.VARCHAR),
                        new SqlParameter("p_vFE_ENV_OFI", Types.VARCHAR),
                        new SqlParameter("p_vOK", Types.VARCHAR));
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_vNU_ANN", documentoEmiBean.getNuAnn())
                .addValue("p_vNU_EMI", documentoEmiBean.getNuEmi())
                .addValue("p_vES_DOC_INI", estacionDocumento.getEsDocIni())
                .addValue("p_vES_DOC_FIN", estacionDocumento.getEsDocFin())
                .addValue("p_vOBS_DEV", estacionDocumento.getObsDev())
                .addValue("p_vUSER", documentoEmiBean.getCoUseMod())
                .addValue("p_vFE_ENV_OFI", documentoEmiBean.getFeEnvOfi())
                .addValue("p_vOK", "OK");
        try {
            this.spInsertaEstacionDoc.execute(in);
            mensaje = "OK";
        } catch (Exception ex) {
            mensaje = "NO_OK";
            ex.printStackTrace();
        }
        return mensaje;        
    }
     /*--23/08/19 HPB Devolucion Doc a Oficina--*/ 
    /*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
    @Override
    public List<DocumentoRecepMensajeriaBean> getLstDetalleMensajeriaVirtual(DestinoResBen oDestinoResBen) {
        StringBuilder sql = new StringBuilder();
        List<DocumentoRecepMensajeriaBean> list = null;
        
        sql.append("SELECT A.NU_ANN, A.NU_EMI, A.ES_DOC_EMI, A.DOC_ESTADO_MSJ, A.TI_ENV_MSJ \n" +
                    " FROM TDTV_REMITOS A \n" +
                    " WHERE A.NU_ANN ="+oDestinoResBen.getNuAnn().toString()+
                    " AND A.NU_EMI ="+oDestinoResBen.getNuEmi()+
                    " AND A.TI_ENV_MSJ = '2'");           

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoRecepMensajeriaBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    /*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
}
