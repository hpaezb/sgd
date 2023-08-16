/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.oracle;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import pe.gob.onpe.tramitedoc.dao.MaestrosDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import pe.gob.onpe.tramitedoc.bean.AnnioBean;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.*;
import pe.gob.onpe.tramitedoc.bean.MensajesConsulBean;
import pe.gob.onpe.tramitedoc.web.util.BusquedaTextual;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;
import pe.gob.onpe.tramitedoc.dao.MensajesDao;

/**
 *
 * @author oti3
 */
@Repository("mensajesDao")
public class MensajesDaoImp extends SimpleJdbcDaoBase implements MensajesDao {
    private SimpleJdbcCall spDescargamsj;
    private static Logger logger=Logger.getLogger(MensajesDaoImp.class);
    
    @Override
    public List<EstadoDocumentoBean> listEstadosMsj(String nomTabla) {
        StringBuilder sql = new StringBuilder();
       
                sql.append("SELECT DE_EST_MP DE_EST,CO_EST \n" +
                    "FROM TDTR_ESTADOS \n" +
                    "WHERE DE_TAB=?\n" +
                    "AND DE_EST_MP IS NOT NULL UNION SELECT '.: TODOS :.',NULL \n" +
                    "FROM DUAL \n" +
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
    public List<SiElementoBean> getLsSiElementoBean(String pctabCodtab) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CELE_DESELE,CELE_CODELE,NELE_NUMSEC,CELE_DESCOR\n" +
                   "FROM   SI_ELEMENTO\n" +
                   //"WHERE  CTAB_CODTAB = ? UNION SELECT '.: TODOS :.',NULL,NULL,NULL \n" +
                    "WHERE  CTAB_CODTAB = ? AND ESTADO='1'  UNION SELECT '.: TODOS :.',NULL,NULL,NULL \n" +
                   "FROM DUAL \n" +
                   "ORDER BY 1");

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
    public List<ElementoMensajeroBean> getListMensajero(String tipo){
        Boolean flag = false;
        
        StringBuilder sql = new StringBuilder();                 
        if (tipo.equals("MOTORIZADO")){
            sql.append("SELECT CEMP_NU_DNI codigo,CEMP_APEPAT||' '||CEMP_APEMAT ||' '|| CEMP_DENOM nombre FROM RHTM_PER_EMPLEADOS WHERE CEMP_CO_CARGO='038'");         
            flag = true;
        }
        if(tipo.equals("COURRIER")){        
            //sql.append("SELECT CPRO_RUC codigo ,CPRO_RAZSOC nombre FROM LG_PRO_PROVEEDOR WHERE CPRO_RUC IN ( select CELE_DESELE from SI_ELEMENTO WHERE CTAB_CODTAB='DE_COURRIER' )");         
            sql.append("SELECT CPRO_RUC codigo ,CPRO_RAZSOC nombre FROM LG_PRO_PROVEEDOR WHERE CPRO_RUC IN ( select CELE_DESELE from SI_ELEMENTO WHERE CTAB_CODTAB='COURRIERS' )"); //Hermes 20/07/2018        
            flag = true;
        }
        List<ElementoMensajeroBean> list = new ArrayList<ElementoMensajeroBean>();
        
        if(flag){
            try {
                list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(ElementoMensajeroBean.class) );
            }catch (EmptyResultDataAccessException e) {
                list = null;
            }catch (Exception e) {
                list = null;
                e.printStackTrace();
            }
        }  
        
        return list;
    }

    @Override
    public List<EstadoDocumentoBean> listEstadosCarga(String nomTabla) {
        
        StringBuilder sql = new StringBuilder();
       
                sql.append("SELECT DE_EST,CO_EST \n" +
                    "FROM TDTR_ESTADOS \n" +
                    "WHERE CO_EST!=2 AND DE_TAB=?\n" +
                    "AND DE_EST IS NOT NULL AND DE_EST_MP IS NOT NULL " +
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
    
//    @Override
//    public List<MensajesConsulBean> getBuscaDocumentosCarga(BuscarDocumentoCargaMsjBean buscarDocumentoCargaMsjBean) {
//
//        StringBuilder sql = new StringBuilder();
//        
//        boolean bBusqFiltro = false;
//        
//        Map<String, Object> objectParam = new HashMap<String, Object>();
//
//        List<MensajesConsulBean> list = new ArrayList<MensajesConsulBean>();
//
//        
//        sql.append("SELECT  ");
//        sql.append("A.NU_MSJ, TO_CHAR(M.FEC_ENVIOMSJ,'DD/MM/YYYY') FEC_ENVIOMSJ, A.NU_ANN");
//        
//        sql.append(",PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM) TIPODOCUMENTO ");
//        sql.append(",DECODE (B.TI_EMI,'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,B.DE_DOC_SIG) NUMERODOCUMENTO");
//                
//        sql.append(",A.NU_EMI,A.NU_DES,PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");        
//        sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
//        sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
//        sql.append("B.DE_DOC_SIG)");
//        sql.append("DE_TIP_DOC,UPPER(B.DE_ASU) DE_ASU,A.CO_LOC_DES CO_LOCAL,NVL2(A.CO_LOC_DES,SUBSTR(PK_SGD_DESCRIPCION.DE_LOCAL(A.CO_LOC_DES), 1, 100),NULL) DE_LOCAL, ");
//        sql.append("B.CO_DEP_EMI CO_DEPENDENCIA,");
//        sql.append("NVL2(B.CO_DEP_EMI,SUBSTR(PK_SGD_DESCRIPCION.DE_DEPENDENCIA(B.CO_DEP_EMI), 1, 100),NULL) DE_DEPENDENCIA,");                    
//        sql.append("DECODE(A.TI_DES,");
//        sql.append("'01', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES),");
//        sql.append("'02', PK_SGD_DESCRIPCION.DE_PROVEEDOR (A.NU_RUC_DES),");
//        sql.append("'03', PK_SGD_DESCRIPCION.ANI_SIMIL (A.NU_DNI_DES),");
//        sql.append("'04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (A.CO_OTR_ORI_DES),");
//        sql.append("'05', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES)");
//        sql.append(")  DESTINATARIO,");
//        sql.append("A.CO_MOT CO_TRAMITE,NVL2(A.CO_MOT,SUBSTR(PK_SGD_DESCRIPCION.MOTIVO(A.CO_MOT), 1, 100),NULL) DE_TRAMITE, ");
//        sql.append("A.CO_PRI CO_PRIORIDAD, ");
//        sql.append("A.DE_PRO DE_INDICACIONES, ");
//        sql.append("A.TI_DES CO_TIPO_DESTINO,");
//        sql.append("A.CDIR_REMITE direccion,");
//        
//        sql.append("DECODE(A.TI_DES,");        
//        sql.append("'01', TRIM(DEP.NODIS)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODEP),");
//        sql.append("'02', TRIM(UBIDESTINO.NODIS)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODEP),");
//        sql.append("'03', TRIM(UBIDESTINO.NODIS)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODEP),");
//        sql.append("'04', TRIM(UBIDESTINO.NODIS)||'/'||TRIM(UBIDESTINO.NOPRV)||'/'||TRIM(UBIDESTINO.NODEP),");
//        sql.append("'05', TRIM(DEP.NODIS)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODEP)");
//        sql.append(") DEPARTAMENTO,M.DE_TIP_MSJ,M.DE_TIP_ENV,");
//        
//        sql.append("(CASE WHEN M.DE_TIP_MSJ='COURRIER' THEN (SELECT TRIM(CPRO_RAZSOC) FROM LG_PRO_PROVEEDOR WHERE CPRO_RUC=M.RE_ENV_MSJ) ELSE (SELECT TRIM(CEMP_APEPAT)||' '||TRIM(CEMP_APEMAT) ||' '|| TRIM(CEMP_DENOM) FROM RHTM_PER_EMPLEADOS WHERE CEMP_NU_DNI=M.RE_ENV_MSJ) END) RE_ENV_MSJ, "); 
//        sql.append("NU_SER_MSJ||'-'||AN_SER_MSJ NU_SERVICIO,");
//        sql.append("NVL2(A.FE_ENT_MSJ,TO_CHAR(A.FE_ENT_MSJ,'DD/MM/YYYY'),' ') FE_ENT_MSJ, ");
//        sql.append("NVL2(A.FE_DEV_MSJ,TO_CHAR(A.FE_DEV_MSJ,'DD/MM/YYYY'),' ') FE_DEV_MSJ,");
//        sql.append("(SELECT DE_EST FROM TDTR_ESTADOS WHERE CO_EST=A.ES_DOC_REC AND DE_TAB='TD_MENSAJERIA') EST_MSJ,A.ES_DOC_REC COD_EST_MSJ,M.DE_AMBITO,TO_CHAR(M.FE_PLA_MSJ,'DD/MM/YYYY') FE_PLA_MSJ,A.ES_PEN_MSJ,NVL2(A.OB_MSJ,A.OB_MSJ,' ') OB_MSJ,NVL2(A.MO_MSJ_DEV,A.MO_MSJ_DEV,' ') MO_MSJ_DEV, ");   
//        sql.append("trunc(M.FE_PLA_MSJ)-trunc(M.FEC_ENVIOMSJ)+1 DIA_PLA,");
//        sql.append("(CASE WHEN A.ES_DOC_REC=2 THEN PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,SysDate) ELSE ");
//        sql.append(" PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,A.FE_ENT_MSJ)  END) DIA_TRA,");
//        sql.append("(CASE WHEN M.PE_ENV_MSJ='1' AND (PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,M.FE_PLA_MSJ)-PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,SysDate))>0 AND A.ES_DOC_REC='2' ");
//        sql.append("THEN (PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,M.FE_PLA_MSJ))-(PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,SysDate)) ELSE ");
//        sql.append("0 END) DIA_PEN, ");
//        sql.append("(CASE WHEN M.PE_ENV_MSJ='1' AND (PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,M.FE_PLA_MSJ)-PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,SysDate))<0 AND A.ES_DOC_REC='2' ");
//        sql.append("THEN (PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,SysDate)-PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,M.FE_PLA_MSJ)) ELSE CASE WHEN M.PE_ENV_MSJ='1' AND (PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,M.FE_PLA_MSJ)-PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,A.FE_ENT_MSJ))<0 AND A.ES_DOC_REC='3' ");
//        sql.append("THEN (PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,A.FE_ENT_MSJ)-PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,M.FE_PLA_MSJ)) ELSE CASE WHEN M.PE_ENV_MSJ='1' AND  (PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,M.FE_PLA_MSJ)-PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,A.FE_DEV_MSJ))<0 AND A.ES_DOC_REC='4' THEN (PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,A.FE_DEV_MSJ)-PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FEC_ENVIOMSJ,M.FE_PLA_MSJ)) ELSE 0 END END END) DIA_VEN, ");
//        sql.append("(CASE WHEN A.ES_DOC_REC='3' AND M.PE_ENV_MSJ='1' AND PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FE_PLA_MSJ,A.FE_ENT_MSJ)>0 THEN PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(M.FE_PLA_MSJ,A.FE_ENT_MSJ) ELSE 0 END) DIA_ENT,");
//        sql.append("(CASE WHEN A.ES_DOC_REC='4' AND M.PE_ENV_MSJ='1' AND PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FE_PLA_DEV,A.FE_DEV_MSJ)>0 THEN PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FE_PLA_DEV,A.FE_DEV_MSJ) ELSE 0 END) DIA_DEV,");
//        sql.append("B.ES_DOC_EMI,NVL2(A.FE_PLA_DEV,TO_CHAR(A.FE_PLA_DEV,'DD/MM/YYYY'),' ') FE_PLA_DEV, ");
//        sql.append("NVL2(B.FE_ENV_MES,TO_CHAR(B.FE_ENV_MES,'DD/MM/YYYY'),' ') FE_ENV_MES,NVL2(B.FE_EMI,TO_CHAR(B.FE_EMI,'DD/MM/YYYY'),' ') FE_EMI, ");
//        sql.append("NVL2(B.FEC_RECEPMP,TO_CHAR(B.FEC_RECEPMP,'DD/MM/YYYY'),' ') FEC_RECEPMP ");
//        
//        
//        sql.append("FROM TDTV_DESTINOS A INNER JOIN TDTV_REMITOS B ON A.NU_ANN = B.NU_ANN AND A.NU_EMI = B.NU_EMI ");
//        sql.append("LEFT JOIN ( SELECT D.CO_DEPENDENCIA,D.DE_DEPENDENCIA,D.DE_CORTA_DEPEN,L.DE_NOMBRE_LOCAL,L.DE_DIRECCION_LOCAL,NODEP,NOPRV,NODIS ,L.CCOD_LOCAL " );
//        sql.append("FROM RHTM_DEPENDENCIA D " );
//        sql.append("INNER JOIN SITM_LOCAL_DEPENDENCIA DL ON DL.CO_DEP=D.CO_DEPENDENCIA " );
//        sql.append("INNER JOIN SI_MAE_LOCAL L ON L.CCOD_LOCAL=DL.CO_LOC " );
//        sql.append("LEFT JOIN IDTUBIAS U ON U.UBDEP||U.UBPRV||U.UBDIS =L.CO_UBIGEO  ) DEP ON DEP.CO_DEPENDENCIA=A.co_dep_DES AND DEP.CCOD_LOCAL=a.co_loc_des " );
//        sql.append(" LEFT JOIN IDTUBIAS UBIDESTINO ON UBIDESTINO.UBDEP||UBIDESTINO.UBPRV||UBIDESTINO.UBDIS =a.CCOD_DPTO||a.CCOD_PROV||a.CCOD_DIST  ");
//        sql.append(" INNER JOIN TD_MENSAJERIA M ON A.NU_MSJ=M.NU_MSJ "); 
//        sql.append(" WHERE B.TI_ENV_MSJ='0' AND B.COD_DEP_MSJ ='"+buscarDocumentoCargaMsjBean.getCoDependencia()+"' "); 
//        String pNUAnn = buscarDocumentoCargaMsjBean.getCoAnnio();
//  
//        String pTipoBusqueda = buscarDocumentoCargaMsjBean.getTipoBusqueda();
//        
//        if (pTipoBusqueda.equals("1") && buscarDocumentoCargaMsjBean.isEsIncluyeFiltro()) {
//            bBusqFiltro = true;
//        }
//        
//        
//        String pEsFiltroFecha = buscarDocumentoCargaMsjBean.getEsFiltroFecha();
//        if (!(pEsFiltroFecha.equals("1")&&pNUAnn.equals("0"))) {
//            sql.append(" AND A.NU_ANN = :pNuAnn");
//            // Parametros Basicos
//            objectParam.put("pNuAnn", pNUAnn);
//        }
//        
//        if (buscarDocumentoCargaMsjBean.getCoAmbitoMsj()!= null && buscarDocumentoCargaMsjBean.getCoAmbitoMsj().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoAmbitoMsj().equals(".: TODOS :.") ) {
//                sql.append(" AND M.DE_AMBITO= :pAmbito ");
//                objectParam.put("pAmbito", buscarDocumentoCargaMsjBean.getCoAmbitoMsj());
//        }
//     
//        if (buscarDocumentoCargaMsjBean.getCoTipoEnvMsj()!= null && buscarDocumentoCargaMsjBean.getCoTipoEnvMsj().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoTipoEnvMsj().equals(".: TODOS :.") ) {
//                sql.append(" AND M.DE_TIP_ENV= :pTipoEnv ");
//                objectParam.put("pTipoEnv", buscarDocumentoCargaMsjBean.getCoTipoEnvMsj());
//        }
//        
//        if (buscarDocumentoCargaMsjBean.getCoTipoMsj()!= null && buscarDocumentoCargaMsjBean.getCoTipoMsj().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoTipoMsj().equals(".: TODOS :.") ) {
//                sql.append(" AND M.DE_TIP_MSJ= :pTipoMsj ");
//                objectParam.put("pTipoMsj", buscarDocumentoCargaMsjBean.getCoTipoMsj());
//        }
//
//        if (buscarDocumentoCargaMsjBean.getCoEstadoDoc()!= null && buscarDocumentoCargaMsjBean.getCoEstadoDoc().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoEstadoDoc().equals(".: TODOS :.") ) {
//                sql.append(" AND A.ES_DOC_REC= :pEstadoDoc ");
//                objectParam.put("pEstadoDoc", buscarDocumentoCargaMsjBean.getCoEstadoDoc());
//        }
//        else
//        {
//            sql.append(" AND A.ES_DOC_REC IN ('2','3','4') ");
//                
//        }
//       
//        if (buscarDocumentoCargaMsjBean.getCoResponsable()!= null && buscarDocumentoCargaMsjBean.getCoResponsable().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoResponsable().equals("-1") ) {
//                sql.append(" AND RE_ENV_MSJ= :pResponsable ");
//                objectParam.put("pResponsable", buscarDocumentoCargaMsjBean.getCoResponsable());
//        }
//        
//        if (buscarDocumentoCargaMsjBean.getEsFiltroFecha() != null
//                    && (pEsFiltroFecha.equals("1") || pEsFiltroFecha.equals("3"))) {
//                String vFeEmiIni = buscarDocumentoCargaMsjBean.getFeEmiIni();
//                String vFeEmiFin = buscarDocumentoCargaMsjBean.getFeEmiFin();
//                if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
//                        && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
//                    sql.append(" AND M.FEC_ENVIOMSJ between TO_DATE(:pFeEmiIni,'dd/mm/yyyy') AND TO_DATE(:pFeEmiFin,'dd/mm/yyyy') + 0.99999");
//                    objectParam.put("pFeEmiIni", vFeEmiIni);
//                    objectParam.put("pFeEmiFin", vFeEmiFin);
//                }
//            }
//        
//        
//        
//       // if (pTipoBusqueda.equals("0") || bBusqFiltro)
//       // {
////            System.out.println(buscarDocumentoCargaMsjBean.getBusNuMsj());
//            if (buscarDocumentoCargaMsjBean.getBusNuMsj()!= null && buscarDocumentoCargaMsjBean.getBusNuMsj().trim().length() > 0  ) {
//                sql.append(" AND A.NU_MSJ= :pNuMsj ");
//                objectParam.put("pNuMsj", buscarDocumentoCargaMsjBean.getBusNuMsj());
//            }
//            
//            if (buscarDocumentoCargaMsjBean.getBusAnSerMsj()!= null && buscarDocumentoCargaMsjBean.getBusAnSerMsj().trim().length() > 0  ) {
//                sql.append(" AND M.AN_SER_MSJ= :pAnSerMsj ");
//                objectParam.put("pAnSerMsj", buscarDocumentoCargaMsjBean.getBusAnSerMsj());
//            }
//            
//            if (buscarDocumentoCargaMsjBean.getBusNuSerMsj()!= null && buscarDocumentoCargaMsjBean.getBusNuSerMsj().trim().length() > 0  ) {
//                sql.append(" AND M.NU_SER_MSJ LIKE '%'||:pNuSerMsj||'%' ");
//                objectParam.put("pNuSerMsj", buscarDocumentoCargaMsjBean.getBusNuSerMsj());
//            }  
//            
//            if (buscarDocumentoCargaMsjBean.getBusDesti()!= null && buscarDocumentoCargaMsjBean.getBusDesti().trim().length() > 0  ) {
//                
//                sql.append(" AND UPPER(DECODE(A.TI_DES,");
//                sql.append("'01', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES),");
//                sql.append("'02', PK_SGD_DESCRIPCION.DE_PROVEEDOR (A.NU_RUC_DES),");
//                sql.append("'03', PK_SGD_DESCRIPCION.ANI_SIMIL (A.NU_DNI_DES),");
//                sql.append("'04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (A.CO_OTR_ORI_DES),");
//                sql.append("'05', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES)");
//                sql.append(")) LIKE '%'||UPPER(:pDesti)||'%' ");
//                
//                objectParam.put("pDesti", buscarDocumentoCargaMsjBean.getBusDesti());
//            }  
//            
//            if (buscarDocumentoCargaMsjBean.getBusNuDoc()!= null && buscarDocumentoCargaMsjBean.getBusNuDoc().trim().length() > 0  ) {
//                String palabra="";
//                String cadena;
//                int cont=0;
//                cadena=buscarDocumentoCargaMsjBean.getBusNuDoc();
//                sql.append(" AND (");
//                
//                for(int i=0;i<cadena.length();i++)
//                {
//                    
//                        if(cadena.charAt(i)==' ')
//                        {    
//                            
//                            if (palabra.trim().length()>0)
//                            {
//                                if (cont==0)
//                                {
//                                    sql.append("UPPER(PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");
//                                    sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
//                                    sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
//                                    sql.append("B.DE_DOC_SIG)) LIKE '%'||UPPER(:pNuDoc)||'%' ");
//                                    objectParam.put("pNuDoc", palabra.trim());
//                                }
//                                else
//                                {
//                                    sql.append(" OR UPPER(PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");
//                                    sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
//                                    sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
//                                    sql.append("B.DE_DOC_SIG)) LIKE '%'||UPPER(:pNuDoc)||'%' ");
//                                    objectParam.put("pNuDoc", palabra.trim());
//                                }
//
//                            }
//                            
//                            cont++;
//                            palabra="";
//                        } 
//                        else
//                        {
//                            palabra=palabra+cadena.charAt(i);
//                        }
//                    
//                }
//                if (palabra.trim().length()>0)
//                {
//                    
//                    if (cont==0)
//                    {
//                        sql.append("UPPER(PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");
//                        sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
//                        sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
//                        sql.append("B.DE_DOC_SIG)) LIKE '%'||UPPER(:pNuDoc)||'%' ");
//                        objectParam.put("pNuDoc", palabra.trim());
//                    }
//                    else
//                    {
//                        sql.append(" OR UPPER(PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");
//                        sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
//                        sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
//                        sql.append("B.DE_DOC_SIG)) LIKE '%'||UPPER(:pNuDoc)||'%' ");
//                        objectParam.put("pNuDoc", palabra.trim());
//                    }
//
//                }
//                sql.append(" ) ");
//                
//                
//
//
//                
//            }
//
//            if (buscarDocumentoCargaMsjBean.getCoOficina()!= null && buscarDocumentoCargaMsjBean.getCoOficina().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoOficina().equals(".: TODOS :.") ) {
//                    sql.append(" AND B.CO_DEP_EMI= :pOficina ");
//                    objectParam.put("pOficina", buscarDocumentoCargaMsjBean.getCoOficina());
//            }
//            
//             //JAZANERO
//            if (buscarDocumentoCargaMsjBean.getBusTipoDoc()!= null && buscarDocumentoCargaMsjBean.getBusTipoDoc().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getBusTipoDoc().equals(".: TODOS :.") ) {
//                    sql.append(" AND B.CO_TIP_DOC_ADM= :pTipDocumento ");
//                    objectParam.put("pTipDocumento", buscarDocumentoCargaMsjBean.getBusTipoDoc());
//            }
//        
//     //   }
//        
//        sql.append("ORDER BY 3");
//
//               
//        System.out.println(sql);
// 
//        
//        try {
//            //objectParam,
//            list = this.namedParameterJdbcTemplate.query(sql.toString(),objectParam,  BeanPropertyRowMapper.newInstance(MensajesConsulBean.class));
//
//        } catch (EmptyResultDataAccessException e) {
//            list = null;
//        } catch (Exception e) {
//            list = null;
//            e.printStackTrace();
//        }
//        return list;
//    
//    
//
//    }

    @Override
    public List<MensajesConsulBean> getBuscaDocumentosCarga(BuscarDocumentoCargaMsjBean buscarDocumentoCargaMsjBean) {

        StringBuilder sql = new StringBuilder();        
        boolean bBusqFiltro = false;        
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List<MensajesConsulBean> list = new ArrayList<MensajesConsulBean>();
        String pTipoMensajero = "COURRIER";
        String pServicioCourier = "SERVICIO_COURRIER";
        String pFormatoFecha = "DD/MM/YYYY";
        String pTablaMensajeria = "TD_MENSAJERIA";
        String pTipoEnvio = "0";                      

        sql.append(" SELECT /*+ NO_CPU_COSTING */ A.NU_MSJ, TO_CHAR(M.FEC_ENVIOMSJ, :pFormatoFecha) FEC_ENVIOMSJ, A.NU_ANN, IDOSGD.PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM) TIPODOCUMENTO, ");
        sql.append(" CASE WHEN B.TI_EMI = '01' THEN B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG ");
        sql.append(" WHEN B.TI_EMI = '05' THEN B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG ");
        sql.append(" ELSE B.DE_DOC_SIG END NUMERODOCUMENTO, ");
        sql.append(" A.NU_EMI, A.NU_DES, IDOSGD.PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM) || ' ' || CASE WHEN B.TI_EMI = '01' THEN B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG ");
        sql.append(" WHEN B.TI_EMI = '05' THEN B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG ELSE B.DE_DOC_SIG END DE_TIP_DOC, B.DE_ASU DE_ASU, ");
        sql.append(" A.CO_LOC_DES CO_LOCAL, NVL2(A.CO_LOC_DES, SUBSTR(IDOSGD.PK_SGD_DESCRIPCION.DE_LOCAL(A.CO_LOC_DES), 1, 100), NULL) DE_LOCAL, B.CO_DEP_EMI CO_DEPENDENCIA, ");
        sql.append(" NVL2(B.CO_DEP_EMI, SUBSTR(IDOSGD.PK_SGD_DESCRIPCION.DE_DEPENDENCIA(B.CO_DEP_EMI), 1, 100), NULL) DE_DEPENDENCIA, ");
        sql.append(" CASE WHEN A.TI_DES = '01' THEN IDOSGD.PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) || '-' || IDOSGD.PK_SGD_DESCRIPCION.DE_NOM_EMP(A.CO_EMP_DES) ");
        sql.append(" WHEN A.TI_DES = '02' THEN IDOSGD.PK_SGD_DESCRIPCION.DE_PROVEEDOR(A.NU_RUC_DES) ");
        sql.append(" WHEN A.TI_DES = '03' THEN IDOSGD.PK_SGD_DESCRIPCION.ANI_SIMIL(A.NU_DNI_DES) ");
        sql.append(" WHEN A.TI_DES = '04' THEN IDOSGD.PK_SGD_DESCRIPCION.OTRO_ORIGEN(A.CO_OTR_ORI_DES) ");
        sql.append(" ELSE IDOSGD.PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) || '-' || IDOSGD.PK_SGD_DESCRIPCION.DE_NOM_EMP(A.CO_EMP_DES) END DESTINATARIO, ");
        sql.append(" A.CO_MOT CO_TRAMITE, NVL2(A.CO_MOT, SUBSTR(IDOSGD.PK_SGD_DESCRIPCION.MOTIVO(A.CO_MOT), 1, 100), NULL) DE_TRAMITE, A.CO_PRI CO_PRIORIDAD, ");
        sql.append(" A.DE_PRO DE_INDICACIONES, A.TI_DES CO_TIPO_DESTINO, A.CDIR_REMITE direccion, ");                
        sql.append(" CASE WHEN A.TI_DES = '01' THEN TRIM(DEP.NODIS) || '/' || TRIM(DEP.NOPRV) || '/' || TRIM(DEP.NODEP) ");
        sql.append(" WHEN A.TI_DES = '02' THEN TRIM(UBIDESTINO.NODIS) || '/' || TRIM(UBIDESTINO.NOPRV) || '/' || TRIM(UBIDESTINO.NODEP) ");
        sql.append(" WHEN A.TI_DES = '03' THEN TRIM(UBIDESTINO.NODIS) || '/' || TRIM(UBIDESTINO.NOPRV) || '/' || TRIM(UBIDESTINO.NODEP) ");
        sql.append(" WHEN A.TI_DES = '04' THEN TRIM(UBIDESTINO.NODIS) || '/' || TRIM(UBIDESTINO.NOPRV) || '/' || TRIM(UBIDESTINO.NODEP) ");
        sql.append(" WHEN A.TI_DES = '05' THEN TRIM(DEP.NODIS) || '/' || TRIM(DEP.NOPRV) || '/' || TRIM(DEP.NODEP) END DEPARTAMENTO, ");         
        sql.append(" M.DE_TIP_MSJ, M.DE_TIP_ENV, ");
        sql.append("(CASE WHEN M.DE_TIP_MSJ = 'COURRIER' THEN (PK_SGD_DESCRIPCION.DE_PROVEEDOR(M.RE_ENV_MSJ)) ELSE ");
        sql.append(" CASE WHEN M.RE_ENV_MSJ = '-2' THEN 'SIN RESPONSABLE' ELSE (SELECT TRIM(CEMP_APEPAT) || ' ' || TRIM(CEMP_APEMAT) || ' ' || TRIM(CEMP_DENOM) FROM RHTM_PER_EMPLEADOS ");
        sql.append(" WHERE CEMP_NU_DNI = M.RE_ENV_MSJ) END END) RE_ENV_MSJ, ");
        //sql.append(" CASE WHEN M.DE_TIP_MSJ = :pTipoMensajero THEN IDOSGD.PK_SGD_DESCRIPCION.DE_PROVEEDOR(M.RE_ENV_MSJ) ");
        //sql.append(" ELSE (SELECT TRIM(CEMP_APEPAT) || ' ' || TRIM(CEMP_APEMAT) || ' ' || TRIM(CEMP_DENOM) FROM IDOSGD.RHTM_PER_EMPLEADOS WHERE CEMP_NU_DNI = M.RE_ENV_MSJ) END RE_ENV_MSJ, ");
        sql.append(" NU_SER_MSJ || '-' || AN_SER_MSJ NU_SERVICIO, NVL2(A.FE_ENT_MSJ, TO_CHAR(A.FE_ENT_MSJ, :pFormatoFecha), ' ') FE_ENT_MSJ, NVL2(A.FE_DEV_MSJ, TO_CHAR(A.FE_DEV_MSJ, :pFormatoFecha), ' ') FE_DEV_MSJ, ");
        sql.append(" IDOSGD.PK_SGD_DESCRIPCION.ESTADOS(A.ES_DOC_REC, :pTablaMensajeria) EST_MSJ, A.ES_DOC_REC COD_EST_MSJ, M.DE_AMBITO, ");        
        sql.append(" IDOSGD.PK_SGD_DESCRIPCION.DE_DOMINIOS(:pServicioCourier, (SELECT COCLASERCOUR FROM IDOSGD.IDTUBIAS WHERE UBDEP = A.CCOD_DPTO AND UBPRV = A.CCOD_PROV AND UBDIS = A.CCOD_DIST AND STUBI = '1')) TI_ZONA,");
        sql.append(" TO_CHAR(A.FE_PLA_MSJ_D, 'DD/MM/YYYY hh24:mi:ss') FE_PLA_MSJ, A.ES_PEN_MSJ, NVL2(A.OB_MSJ, A.OB_MSJ, ' ') OB_MSJ, NVL2(A.MO_MSJ_DEV, A.MO_MSJ_DEV, ' ') MO_MSJ_DEV, A.DIAS_PLA_ENTR_D, ");
        sql.append(" TRUNC(A.FE_PLA_MSJ_D) - TRUNC(A.FEC_ENVIOMSJ) + 1 DIA_PLA, ");
        sql.append(" CASE WHEN A.ES_DOC_REC = 2 THEN IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, SYSDATE) ");
        sql.append(" ELSE IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_ENT_MSJ) END DIA_TRA, ");
        sql.append(" CASE WHEN A.PE_ENV_MSJ_D = '1' ");
        sql.append(" AND IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_DEV_MSJ) - IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FE_ENT_MSJ, A.FE_DEV_MSJ) > 0 THEN IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FE_ENT_MSJ, A.FE_DEV_MSJ) ");
        sql.append(" ELSE 0 END DIA_PEN, ");
        sql.append(" CASE WHEN A.PE_ENV_MSJ_D = '1' ");
        sql.append(" AND IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_PLA_MSJ_D) - IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, SYSDATE) < 0 ");
        sql.append(" AND A.ES_DOC_REC = '2' THEN IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, SYSDATE) - IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_PLA_MSJ_D) ");
        sql.append(" ELSE CASE WHEN A.PE_ENV_MSJ_D = '1' ");
        sql.append(" AND IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_PLA_MSJ_D) - IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_ENT_MSJ) < 0 ");
        sql.append(" AND A.ES_DOC_REC = '3' THEN IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_ENT_MSJ) - IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_PLA_MSJ_D) ");
        sql.append(" ELSE CASE WHEN A.PE_ENV_MSJ_D = '1' ");
        sql.append(" AND IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_PLA_MSJ_D) - IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_DEV_MSJ) < 0 ");
        sql.append(" AND A.ES_DOC_REC = '4' THEN IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_DEV_MSJ) - IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FEC_ENVIOMSJ, A.FE_PLA_MSJ_D) ");
        sql.append(" ELSE 0 END END END DIA_VEN, ");
        sql.append(" CASE WHEN (A.ES_DOC_REC = '3' ");
        sql.append(" OR A.ES_DOC_REC = '4') AND A.PE_ENV_MSJ_D = '1' ");
        sql.append(" AND IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FE_PLA_MSJ_D, A.FE_ENT_MSJ) > 0 THEN IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FE_PLA_MSJ_D, A.FE_ENT_MSJ) ");
        sql.append(" ELSE 0 END DIA_ENT, CASE WHEN A.PE_ENV_MSJ_D = '1' ");
        sql.append(" AND IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FE_PLA_DEV, A.FE_DEV_MSJ) > 0 THEN IDOSGD.PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(A.FE_PLA_DEV, A.FE_DEV_MSJ) ");
        sql.append(" ELSE 0 END DIA_DEV, B.ES_DOC_EMI, ");
        sql.append(" NVL2(A.FE_PLA_DEV, TO_CHAR(A.FE_PLA_DEV, :pFormatoFecha), ' ') FE_PLA_DEV, ");
        sql.append(" NVL2(B.FE_ENV_MES, TO_CHAR(B.FE_ENV_MES, :pFormatoFecha), ' ') FE_ENV_MES, ");
        sql.append(" NVL2(B.FE_EMI, TO_CHAR(B.FE_EMI, :pFormatoFecha), ' ') FE_EMI, ");
        sql.append(" NVL2(B.FEC_RECEPMP, TO_CHAR(B.FEC_RECEPMP, :pFormatoFecha), ' ') FEC_RECEPMP, F.NU_EXPEDIENTE ");
        sql.append(" FROM IDOSGD.TDTV_DESTINOS A INNER JOIN IDOSGD.TDTV_REMITOS B ON A.NU_ANN = B.NU_ANN AND A.NU_EMI = B.NU_EMI ");
        sql.append(" INNER JOIN TDTX_REMITOS_RESUMEN F ON B.NU_ANN = F.NU_ANN AND B.NU_EMI = F.NU_EMI ");
        sql.append(" LEFT JOIN (SELECT D.CO_DEPENDENCIA, D.DE_DEPENDENCIA, D.DE_CORTA_DEPEN, L.DE_NOMBRE_LOCAL, L.DE_DIRECCION_LOCAL, NODEP, NOPRV, NODIS, L.CCOD_LOCAL ");
        sql.append(" FROM IDOSGD.RHTM_DEPENDENCIA D INNER JOIN IDOSGD.SITM_LOCAL_DEPENDENCIA DL ON DL.CO_DEP = D.CO_DEPENDENCIA INNER JOIN IDOSGD.SI_MAE_LOCAL L ");
        sql.append(" ON L.CCOD_LOCAL = DL.CO_LOC LEFT JOIN IDOSGD.IDTUBIAS U ON L.CO_UBIGEO = U.UBDEP || U.UBPRV || U.UBDIS) DEP ");
        sql.append(" ON DEP.CO_DEPENDENCIA = A.co_dep_DES AND DEP.CCOD_LOCAL = a.co_loc_des LEFT JOIN IDOSGD.IDTUBIAS UBIDESTINO ");
        //sql.append(" ON UBIDESTINO.UBDEP || UBIDESTINO.UBPRV || UBIDESTINO.UBDIS = a.CCOD_DPTO || a.CCOD_PROV || a.CCOD_DIST, IDOSGD.TD_MENSAJERIA M ");
        sql.append(" ON (UBIDESTINO.UBDEP = a.CCOD_DPTO and  UBIDESTINO.UBPRV = a.CCOD_PROV and  UBIDESTINO.UBDIS  = a.CCOD_DIST), IDOSGD.TD_MENSAJERIA M ");
        sql.append(" WHERE B.TI_ENV_MSJ = :pTipoEnvio AND B.COD_DEP_MSJ = :pCoDependencia ");        
        
        objectParam.put("pServicioCourier", pServicioCourier);
        objectParam.put("pTipoMensajero", pTipoMensajero);
        objectParam.put("pFormatoFecha", pFormatoFecha);
        objectParam.put("pTablaMensajeria", pTablaMensajeria);
        objectParam.put("pTipoEnvio", pTipoEnvio);
        objectParam.put("pCoDependencia", buscarDocumentoCargaMsjBean.getCoDependencia());        
        String pNUAnn = buscarDocumentoCargaMsjBean.getCoAnnio();
        String pTipoBusqueda = buscarDocumentoCargaMsjBean.getTipoBusqueda();
        
        if (pTipoBusqueda.equals("1") && buscarDocumentoCargaMsjBean.isEsIncluyeFiltro()) {
            bBusqFiltro = true;
        }
                
        String pEsFiltroFecha = buscarDocumentoCargaMsjBean.getEsFiltroFecha();
        if (!(pEsFiltroFecha.equals("1")&&pNUAnn.equals("0"))) {
            sql.append(" AND A.NU_ANN = :pNuAnn");
            objectParam.put("pNuAnn", pNUAnn);
        }
        
        if (buscarDocumentoCargaMsjBean.getCoAmbitoMsj()!= null && buscarDocumentoCargaMsjBean.getCoAmbitoMsj().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoAmbitoMsj().equals(".: TODOS :.") ) {
            sql.append(" AND M.DE_AMBITO= :pAmbito ");
            objectParam.put("pAmbito", buscarDocumentoCargaMsjBean.getCoAmbitoMsj());
        }
     
        if (buscarDocumentoCargaMsjBean.getCoTipoEnvMsj()!= null && buscarDocumentoCargaMsjBean.getCoTipoEnvMsj().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoTipoEnvMsj().equals(".: TODOS :.") ) {
            sql.append(" AND M.DE_TIP_ENV= :pTipoEnv ");
            objectParam.put("pTipoEnv", buscarDocumentoCargaMsjBean.getCoTipoEnvMsj());
        }
        
        if (buscarDocumentoCargaMsjBean.getCoTipoMsj()!= null && buscarDocumentoCargaMsjBean.getCoTipoMsj().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoTipoMsj().equals(".: TODOS :.") ) {
            sql.append(" AND M.DE_TIP_MSJ= :pTipoMsj ");
            objectParam.put("pTipoMsj", buscarDocumentoCargaMsjBean.getCoTipoMsj());
        }

        if (buscarDocumentoCargaMsjBean.getCoEstadoDoc()!= null && buscarDocumentoCargaMsjBean.getCoEstadoDoc().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoEstadoDoc().equals(".: TODOS :.") ) {
            sql.append(" AND A.ES_DOC_REC= :pEstadoDoc ");
            objectParam.put("pEstadoDoc", buscarDocumentoCargaMsjBean.getCoEstadoDoc());
        }else{
            sql.append(" AND A.ES_DOC_REC IN ('2','3','4') ");                
        }
       
        if (buscarDocumentoCargaMsjBean.getCoResponsable()!= null && buscarDocumentoCargaMsjBean.getCoResponsable().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoResponsable().equals("-1") ) {
            if(!buscarDocumentoCargaMsjBean.getCoResponsable().equals("-3")){//TODOS LOS RESPONSABLES
                sql.append(" AND RE_ENV_MSJ= :pResponsable ");
                objectParam.put("pResponsable", buscarDocumentoCargaMsjBean.getCoResponsable());
            }            
        }
        
        if (buscarDocumentoCargaMsjBean.getEsFiltroFecha() != null && (pEsFiltroFecha.equals("1") || pEsFiltroFecha.equals("3"))) {
            String vFeEmiIni = buscarDocumentoCargaMsjBean.getFeEmiIni();
            String vFeEmiFin = buscarDocumentoCargaMsjBean.getFeEmiFin();
            if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0 && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                sql.append(" AND M.FEC_ENVIOMSJ between TO_DATE(:pFeEmiIni,'dd/mm/yyyy') AND TO_DATE(:pFeEmiFin,'dd/mm/yyyy') + 0.99999");
                objectParam.put("pFeEmiIni", vFeEmiIni);
                objectParam.put("pFeEmiFin", vFeEmiFin);
            }
        }
       // if (pTipoBusqueda.equals("0") || bBusqFiltro)
       // {
            if (buscarDocumentoCargaMsjBean.getBusNuMsj()!= null && buscarDocumentoCargaMsjBean.getBusNuMsj().trim().length() > 0  ) {
                sql.append(" AND A.NU_MSJ= :pNuMsj ");
                objectParam.put("pNuMsj", buscarDocumentoCargaMsjBean.getBusNuMsj());
            }
            
            if (buscarDocumentoCargaMsjBean.getBusAnSerMsj()!= null && buscarDocumentoCargaMsjBean.getBusAnSerMsj().trim().length() > 0  ) {
                sql.append(" AND M.AN_SER_MSJ= :pAnSerMsj ");
                objectParam.put("pAnSerMsj", buscarDocumentoCargaMsjBean.getBusAnSerMsj());
            }
            
            if (buscarDocumentoCargaMsjBean.getBusNuSerMsj()!= null && buscarDocumentoCargaMsjBean.getBusNuSerMsj().trim().length() > 0  ) {
                sql.append(" AND M.NU_SER_MSJ LIKE '%'||:pNuSerMsj||'%' ");
                objectParam.put("pNuSerMsj", buscarDocumentoCargaMsjBean.getBusNuSerMsj());
            }  
            
            if (buscarDocumentoCargaMsjBean.getBusDesti()!= null && buscarDocumentoCargaMsjBean.getBusDesti().trim().length() > 0  ) {                
                sql.append(" AND UPPER(DECODE(A.TI_DES,");
                sql.append("'01', IDOSGD.PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'|| IDOSGD.PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES),");
                sql.append("'02', IDOSGD.PK_SGD_DESCRIPCION.DE_PROVEEDOR (A.NU_RUC_DES),");
                sql.append("'03', IDOSGD.PK_SGD_DESCRIPCION.ANI_SIMIL (A.NU_DNI_DES),");
                sql.append("'04', IDOSGD.PK_SGD_DESCRIPCION.OTRO_ORIGEN (A.CO_OTR_ORI_DES),");
                sql.append("'05', IDOSGD.PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'|| IDOSGD.PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES)");
                sql.append(")) LIKE '%'||UPPER(:pDesti)||'%' ");
                
                objectParam.put("pDesti", buscarDocumentoCargaMsjBean.getBusDesti());
            }  
            /*-----Hermes 30/04/2019- Cambiar posición en campos de filtro módulo gestión de documentos-----*/            
            if(buscarDocumentoCargaMsjBean.getTipoZona()!= null && buscarDocumentoCargaMsjBean.getTipoZona().trim().length()>0 && !buscarDocumentoCargaMsjBean.getTipoZona().equals(".: TODOS :.")){
                sql.append(" AND UBIDESTINO.COCLASERCOUR= :pTipoZona ");
                objectParam.put("pTipoZona", buscarDocumentoCargaMsjBean.getTipoZona());
            }
            if (buscarDocumentoCargaMsjBean.getBusNumExpediente() != null && buscarDocumentoCargaMsjBean.getBusNumExpediente().trim().length() > 1) {
                sql.append(" AND F.NU_EXPEDIENTE LIKE '%'||:pnuExpediente||'%' ");
                objectParam.put("pnuExpediente", buscarDocumentoCargaMsjBean.getBusNumExpediente());
            }            
            /*-----Hermes 30/04/2019- Cambiar posición en campos de filtro módulo gestión de documentos-----*/            
            if (buscarDocumentoCargaMsjBean.getBusNuDoc()!= null && buscarDocumentoCargaMsjBean.getBusNuDoc().trim().length() > 0  ) {
                String palabra="";
                String cadena;
                int cont=0;
                cadena=buscarDocumentoCargaMsjBean.getBusNuDoc();
                sql.append(" AND (");
                
                for(int i=0;i<cadena.length();i++){                    
                        if(cadena.charAt(i)==' '){                                
                            if (palabra.trim().length()>0){
                                if (cont==0){
                                    sql.append("UPPER(IDOSGD.PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");
                                    sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
                                    sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
                                    sql.append("B.DE_DOC_SIG)) LIKE '%'||UPPER(:pNuDoc)||'%' ");
                                    objectParam.put("pNuDoc", palabra.trim());
                                }else{
                                    sql.append(" OR UPPER(IDOSGD.PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");
                                    sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
                                    sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
                                    sql.append("B.DE_DOC_SIG)) LIKE '%'||UPPER(:pNuDoc)||'%' ");
                                    objectParam.put("pNuDoc", palabra.trim());
                                }
                            }                            
                            cont++;
                            palabra="";
                        }else{
                            palabra=palabra+cadena.charAt(i);
                        }                    
                }
                if (palabra.trim().length()>0){                    
                    if (cont==0){
                        sql.append("UPPER(IDOSGD.PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");
                        sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
                        sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
                        sql.append("B.DE_DOC_SIG)) LIKE '%'||UPPER(:pNuDoc)||'%' ");
                        objectParam.put("pNuDoc", palabra.trim());
                    }else{
                        sql.append(" OR UPPER(IDOSGD.PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");
                        sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
                        sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
                        sql.append("B.DE_DOC_SIG)) LIKE '%'||UPPER(:pNuDoc)||'%' ");
                        objectParam.put("pNuDoc", palabra.trim());
                    }
                }
                sql.append(" ) ");                               
            }

            if (buscarDocumentoCargaMsjBean.getCoOficina()!= null && buscarDocumentoCargaMsjBean.getCoOficina().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getCoOficina().equals(".: TODOS :.") ) {
                sql.append(" AND B.CO_DEP_EMI= :pOficina ");
                objectParam.put("pOficina", buscarDocumentoCargaMsjBean.getCoOficina());
            }            
             //JAZANERO
            if (buscarDocumentoCargaMsjBean.getBusTipoDoc()!= null && buscarDocumentoCargaMsjBean.getBusTipoDoc().trim().length() > 0 && !buscarDocumentoCargaMsjBean.getBusTipoDoc().equals(".: TODOS :.") ) {
                sql.append(" AND B.CO_TIP_DOC_ADM= :pTipDocumento ");
                objectParam.put("pTipDocumento", buscarDocumentoCargaMsjBean.getBusTipoDoc());
            }       
     //   }
        sql.append(" AND A.NU_MSJ = M.NU_MSJ ");        
        sql.append("ORDER BY 3");               
        logger.info(sql);
         
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(),objectParam,  BeanPropertyRowMapper.newInstance(MensajesConsulBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
        
    @Override
    public String updMensajeriaDocumento(DescargaMensajeBean descargaMensaje) {
        String vReturn = "NO_OK";   
        StringBuilder sqlQry = new StringBuilder();
        StringBuilder sqlQry1 = new StringBuilder();
        StringBuilder sqlQry2 = new StringBuilder();
        /*StringBuilder sql = new StringBuilder(); 
        
        sql.append("UPDATE TDTV_DESTINOS ");
        sql.append("SET ES_DOC_REC='"+descargaMensaje.getCo_EstadoDoc()+"', ");
        sql.append("Ob_msj='"+descargaMensaje.getOb_msj()+"', ");
        if (descargaMensaje.getCo_EstadoDoc().equals("3"))
        {
            sql.append("fe_ent_msj='"+descargaMensaje.getFe_ent_msj()+"' ");
        }
        else
        {
            sql.append("fe_dev_msj='"+descargaMensaje.getFe_ent_msj()+"' ");
        }
    
        sql.append("WHERE NU_ANN||NU_EMI in ( "+ descargaMensaje.getNu_ann()+descargaMensaje.getNu_emi()+" ) ");
        sql.append("AND nu_des in ( "+ descargaMensaje.getNu_des()+" ) AND Nu_msj in ( "+ descargaMensaje.getNu_msj()+" )");
        
        try{
            
            this.jdbcTemplate.update(sql.toString() );
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Numero de Mensajeria Duplicado.";
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*----------------------------------Hermes 15/08/2018-------------------------------*/
        if(descargaMensaje.getPe_env_msj().equals("1")){
            String horaPlazoMsj = descargaMensaje.getHo_ent_msj().substring(11, 19);                       
            sqlQry.append("select TO_CHAR(PK_SGD_CONSULTAS.CALCULAR_DIAS_HABILES(TO_DATE('"+descargaMensaje.getFe_ent_msj()+"','DD/MM/YYYY hh24:mi:ss'),"+descargaMensaje.getDi_pla_dev()+"),'DD/MM/YYYY') FROM DUAL");  
            sqlQry1.append("SELECT PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(TO_DATE('"+descargaMensaje.getFe_pla_msj()+"','DD/MM/YYYY hh24:mi:ss'), TO_DATE('"+descargaMensaje.getFe_ent_msj()+"','DD/MM/YYYY hh24:mi:ss')) FROM DUAL");  
            String snufecha = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class);  
            int vDifDias = this.jdbcTemplate.queryForObject(sqlQry1.toString(), Integer.class); 
            String newFecha = snufecha+" "+horaPlazoMsj;                
            descargaMensaje.setFe_pla_dev(newFecha); 

            if(vDifDias > 0)
                descargaMensaje.setEs_pen_msj("S");

            sqlQry2.append("SELECT PK_SGD_MENSAJERIA.CAL_DIF_FECHAS_HABILES(TO_DATE('"+descargaMensaje.getFe_pla_dev()+"','DD/MM/YYYY hh24:mi:ss'), TO_DATE('"+descargaMensaje.getFe_dev_msj()+"','DD/MM/YYYY hh24:mi:ss')) FROM DUAL");  
            int vDias = this.jdbcTemplate.queryForObject(sqlQry2.toString(), Integer.class);
            if(vDias > 0)
                descargaMensaje.setEs_pen_dev("S");
        
        }
        /*----------------------------------Hermes 15/08/2018-------------------------------*/  
        
        if (descargaMensaje.getCo_EstadoDoc().equals("3"))
        {
            descargaMensaje.setMo_msj_dev("");
        }

        if (descargaMensaje.getEs_pen_msj().equals("0"))
        {
            descargaMensaje.setFe_pla_dev("");
            descargaMensaje.setEs_pen_msj("");
            descargaMensaje.setEs_pen_dev("");
        }
        
        
                
                this.spDescargamsj = new SimpleJdbcCall(this.dataSource).withCatalogName("PK_SGD_MENSAJERIA").withProcedureName("descargamsj")
                .withoutProcedureColumnMetaDataAccess()
                .useInParameterNames("pnu_ann", "pnu_emi", "pnu_des", "pnu_msj", "pfechaent","phoraent","pfechadev","phoradev", "pob_msj", "pmo_msj_dev", "pes_pen_msj","pes_pen_dev", "pest","pfepladev")
                        //descargamsj(pnu_ann tdtv_remitos.nu_ann%TYPE,pnu_emi tdtv_remitos.nu_emi%TYPE,pnu_des tdtv_destinos.nu_des%TYPE,pnu_msj tdtv_destinos.nu_msj%TYPE,pfecha tdtv_destinos.fec_finmsj%TYPE,pob_msj tdtv_destinos.ob_msj%TYPE,pmo_msj_dev tdtv_destinos.mo_msj_dev%TYPE,pes_pen_msj tdtv_destinos.es_pen_msj%TYPE,pest VARCHAR2);
                .declareParameters(
                        new SqlParameter("pnu_ann", Types.VARCHAR),
                        new SqlParameter("pnu_emi", Types.VARCHAR),
                        new SqlParameter("pnu_des", Types.VARCHAR),
                        new SqlParameter("pnu_msj", Types.VARCHAR),
                        new SqlParameter("pfechaent", Types.VARCHAR),
                        new SqlParameter("phoraent", Types.VARCHAR), 
                        new SqlParameter("pfechadev", Types.VARCHAR),
                        new SqlParameter("phoradev", Types.VARCHAR),
                        new SqlParameter("pob_msj", Types.VARCHAR),
                        new SqlParameter("pmo_msj_dev", Types.VARCHAR),
                        new SqlParameter("pes_pen_msj", Types.VARCHAR),
                        new SqlParameter("pes_pen_dev", Types.VARCHAR),
                        new SqlParameter("pest", Types.VARCHAR)  ,
                        new SqlParameter("pfepladev", Types.VARCHAR)
                                );
                SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pnu_ann", descargaMensaje.getNu_ann())
                .addValue("pnu_emi", descargaMensaje.getNu_emi())
                .addValue("pnu_des", descargaMensaje.getNu_des())
                .addValue("pnu_msj", descargaMensaje.getNu_msj())
                .addValue("pfechaent", descargaMensaje.getFe_ent_msj())
                .addValue("phoraent", descargaMensaje.getHo_ent_msj())   
                .addValue("pfechadev", descargaMensaje.getFe_dev_msj())
                .addValue("phoradev", descargaMensaje.getHo_dev_msj())
                .addValue("pob_msj", descargaMensaje.getOb_msj())
                .addValue("pmo_msj_dev", descargaMensaje.getMo_msj_dev())
                .addValue("pes_pen_msj", descargaMensaje.getEs_pen_msj())
                .addValue("pes_pen_dev", descargaMensaje.getEs_pen_dev())
                .addValue("pest", descargaMensaje.getCo_EstadoDoc())
                .addValue("pfepladev", descargaMensaje.getFe_pla_dev());       
        try {
            this.spDescargamsj.execute(in);
            vReturn = "OK";
            
        } catch (Exception ex) {
            //mensaje = "NO_OK";
            ex.printStackTrace();
        }
        
        return vReturn;   
    }

    @Override
    public String insArchivoAnexoDes(final DocumentoAnexoDesBean docAnexo,final  InputStream archivoAnexo,final int size) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();        
        String pcount=getCount(docAnexo.getNuAnn(),docAnexo.getNuEmi(),docAnexo.getNuDes());

        if (pcount.equals("0"))
        {
            sql.append("insert into TDTV_ANEXOS_MSJ(\n"//Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
            + "nu_ann,\n"
            + "nu_emi,\n"
            + "nu_ane,\n"
            + "de_det,\n"
            + "de_rut_ori,\n"
            + "co_use_cre,\n"
            + "fe_use_cre,\n"
            + "co_use_mod,\n"
            + "fe_use_mod,\n"
            + "feula, \n"
            + "nu_des, \n"
            + "tip_doc_msj, \n"
            + "es_estado, \n"
            + "bl_doc )\n"
            + "values(?,?,?,REGEXP_REPLACE(?, '[^-_ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0-9ÁÉÍÓÚáéíóú. ]', ''),REGEXP_REPLACE(?, '[^-_ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0-9ÁÉÍÓÚáéíóú. ]', ' '),?,sysdate,?,sysdate,to_char(sysdate,'yyyymmdd'),?, '1','0',?)");

            final LobHandler lobhandler = new DefaultLobHandler();

            try {
                this.jdbcTemplate.execute(sql.toString(),
                        new AbstractLobCreatingPreparedStatementCallback(lobhandler) {
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                            throws SQLException {
                        ps.setString(1, docAnexo.getNuAnn());
                        ps.setString(2, docAnexo.getNuEmi());
                        ps.setString(3, docAnexo.getNuAne());
                        ps.setString(4, docAnexo.getDeDet());
                        ps.setString(5, docAnexo.getDeRutOri());
                        ps.setString(6, docAnexo.getCoUseCre());
                        ps.setString(7, docAnexo.getCoUseMod());
                        ps.setString(8, docAnexo.getNuDes());
                        lobCreator.setBlobAsBinaryStream(ps, 9, archivoAnexo, size);
                    }
                });
                vReturn = "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        }
        else
        {
            //sql.append("update tdtv_anexos set\n"  
            sql.append("update TDTV_ANEXOS_MSJ set\n"  //Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
                + "de_det=REGEXP_REPLACE(?, '[^-_ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0-9ÁÉÍÓÚáéíóú. ]', ' '),\n"
                + "de_rut_ori=REGEXP_REPLACE(?, '[^-_ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0-9ÁÉÍÓÚáéíóú. ]', ' '),\n"
                + "co_use_cre=?,\n"
                + "fe_use_cre=sysdate,\n"
                + "co_use_mod=?,\n"
                + "fe_use_mod=sysdate,\n"
                + "feula=to_char(sysdate,'yyyymmdd'),\n"
                + "bl_doc=?\n"
                + "where \n"
                + "nu_ann=? and nu_emi=? and nu_des=?");

            final LobHandler lobhandler = new DefaultLobHandler();

            try {
                this.jdbcTemplate.execute(sql.toString(),
                        new AbstractLobCreatingPreparedStatementCallback(lobhandler) {
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                            throws SQLException {
            
                        ps.setString(1, docAnexo.getDeDet());
                        ps.setString(2, docAnexo.getDeRutOri());
                        ps.setString(3, docAnexo.getCoUseCre());
                        ps.setString(4, docAnexo.getCoUseMod());
                        lobCreator.setBlobAsBinaryStream(ps, 5, archivoAnexo, size);
                        ps.setString(6, docAnexo.getNuAnn());
                        ps.setString(7, docAnexo.getNuEmi());
                        ps.setString(8, docAnexo.getNuDes());
                    }
                });
                vReturn = "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }    


        return vReturn;
    
    }

    public String getCount(String pnuAnn, String pnuEmi,String pnuDes) {
        String result = null;

        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) \n"
                //+ "from tdtv_anexos \n"
                + "from TDTV_ANEXOS_MSJ \n" //Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
                + "where nu_ann = ? \n"
                + "and nu_emi = ? \n"
                + "and nu_des = ? ");

        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pnuAnn, pnuEmi,pnuDes});
        } catch (Exception e) {
            result = "0";
            e.printStackTrace();
        }
        return result;

    }
    
    @Override
    public String getUltimoAnexoAdicional(String pnuAnn, String pnuEmi) {
        String result = null;

        StringBuilder sql = new StringBuilder();
        sql.append("select to_char(nvl(max(nu_ane),0)) nu_ane \n"
                + "from TDTV_ANEXOS_MSJ \n" //Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
                + "where nu_ann = ? \n"
                + "and nu_emi = ? ");

        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pnuAnn, pnuEmi});
        } catch (Exception e) {
            result = "1";
            e.printStackTrace();
        }
        return result;

    }
    
    @Override
    public String getUltimoAnexo(String pnuAnn, String pnuEmi) {
        String result = null;

        StringBuilder sql = new StringBuilder();
        sql.append("select to_char(nvl(max(nu_ane),0) + 1) nu_ane \n"
                //+ "from tdtv_anexos \n"
                + "from TDTV_ANEXOS_MSJ \n" //Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
                + "where nu_ann = ? \n"
                + "and nu_emi = ? ");

        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pnuAnn, pnuEmi});
        } catch (Exception e) {
            result = "1";
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public String delMensajeriaDocumento(DescargaMensajeBean descargaMensaje) {
        String vReturn = "NO_OK";    
       
        this.spDescargamsj = new SimpleJdbcCall(this.dataSource).withCatalogName("PK_SGD_MENSAJERIA").withProcedureName("delmsj")
        .withoutProcedureColumnMetaDataAccess()
        .useInParameterNames("pnu_ann", "pnu_emi", "pnu_des", "pnu_msj")
                //descargamsj(pnu_ann tdtv_remitos.nu_ann%TYPE,pnu_emi tdtv_remitos.nu_emi%TYPE,pnu_des tdtv_destinos.nu_des%TYPE,pnu_msj tdtv_destinos.nu_msj%TYPE,pfecha tdtv_destinos.fec_finmsj%TYPE,pob_msj tdtv_destinos.ob_msj%TYPE,pmo_msj_dev tdtv_destinos.mo_msj_dev%TYPE,pes_pen_msj tdtv_destinos.es_pen_msj%TYPE,pest VARCHAR2);
        .declareParameters(
                new SqlParameter("pnu_ann", Types.VARCHAR),
                new SqlParameter("pnu_emi", Types.VARCHAR),
                new SqlParameter("pnu_des", Types.VARCHAR),
                new SqlParameter("pnu_msj", Types.VARCHAR)               
                        );
        SqlParameterSource in = new MapSqlParameterSource()
        .addValue("pnu_ann", descargaMensaje.getNu_ann())
        .addValue("pnu_emi", descargaMensaje.getNu_emi())
        .addValue("pnu_des", descargaMensaje.getNu_des())
        .addValue("pnu_msj", descargaMensaje.getNu_msj());                
       
        try {
            this.spDescargamsj.execute(in);
            vReturn = "OK";
        } catch (Exception ex) {
            //mensaje = "NO_OK";
            ex.printStackTrace();
        }   
       
       
       
        return vReturn;   
    }

    @Override
    public MensajesConsulBean getBuscaDocumentosMsj(String nu_ann, String nu_emi, String nu_des, String nu_msj) {
    StringBuilder sql = new StringBuilder();
        MensajesConsulBean mensajesConsulBean=new MensajesConsulBean();
        
//        boolean bBusqFiltro = false;
//        
//        Map<String, Object> objectParam = new HashMap<String, Object>();
//
//        List<MensajesConsulBean> list = new ArrayList<MensajesConsulBean>();

        
        sql.append("SELECT  ");
        sql.append("A.NU_MSJ,TO_CHAR(M.FEC_ENVIOMSJ,'DD/MM/YYYY') FEC_ENVIOMSJ, A.NU_ANN,A.NU_EMI,A.NU_DES,PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");
        sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
        sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
        sql.append("B.DE_DOC_SIG)");
        sql.append("DE_TIP_DOC,UPPER(B.DE_ASU) DE_ASU,A.CO_LOC_DES CO_LOCAL,NVL2(A.CO_LOC_DES,SUBSTR(PK_SGD_DESCRIPCION.DE_LOCAL(A.CO_LOC_DES), 1, 100),NULL) DE_LOCAL, ");
        sql.append("B.CO_DEP_EMI CO_DEPENDENCIA,");
        sql.append("NVL2(B.CO_DEP_EMI,SUBSTR(PK_SGD_DESCRIPCION.DE_DEPENDENCIA(B.CO_DEP_EMI), 1, 100),NULL) DE_DEPENDENCIA,");                    
        sql.append("DECODE(A.TI_DES,");
        sql.append("'01', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES),");
        sql.append("'02', PK_SGD_DESCRIPCION.DE_PROVEEDOR (A.NU_RUC_DES),");
        sql.append("'03', PK_SGD_DESCRIPCION.ANI_SIMIL (A.NU_DNI_DES),");
        sql.append("'04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (A.CO_OTR_ORI_DES),");
        sql.append("'05', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES)");
        sql.append(")  DESTINATARIO,");
        sql.append("A.CO_MOT CO_TRAMITE,NVL2(A.CO_MOT,SUBSTR(PK_SGD_DESCRIPCION.MOTIVO(A.CO_MOT), 1, 100),NULL) DE_TRAMITE, ");
        sql.append("A.CO_PRI CO_PRIORIDAD, ");
        sql.append("A.DE_PRO DE_INDICACIONES, ");
        sql.append("A.TI_DES CO_TIPO_DESTINO,");
        sql.append("DECODE(A.TI_DES,");
        sql.append("'01', TRIM(DEP.DE_DIRECCION_LOCAL),");
        sql.append("'02', 'DIRECCION PROVEEDOR',");
        sql.append("'03', 'DIRECCION ASIMIL',");
        sql.append("'04', 'DIRECCION OTROS',");
        sql.append("'05', TRIM(DEP.DE_DIRECCION_LOCAL)) DIRECCION,");
        sql.append("DECODE(A.TI_DES,");
        sql.append("'01', TRIM(DEP.NODEP)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODIS),");
        sql.append("'02', 'DEPARTAMENTO PROVEEDOR',");
        sql.append("'03', 'DEPARTAMENTO ASIMIL',");
        sql.append("'04', 'DEPARTAMENTO OTROS',");
        sql.append("'05', TRIM(DEP.NODEP)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODIS)) DEPARTAMENTO,M.DE_TIP_MSJ,M.DE_TIP_ENV,");
        sql.append("(CASE WHEN M.DE_TIP_MSJ='COURRIER' THEN (SELECT TRIM(CPRO_RAZSOC) FROM LG_PRO_PROVEEDOR WHERE CPRO_RUC=M.RE_ENV_MSJ) ELSE (SELECT TRIM(CEMP_APEPAT)||' '||TRIM(CEMP_APEMAT) ||' '|| TRIM(CEMP_DENOM) FROM RHTM_PER_EMPLEADOS WHERE CEMP_NU_DNI=M.RE_ENV_MSJ) END) RE_ENV_MSJ, ");
        sql.append("NU_SER_MSJ||'-'||AN_SER_MSJ NU_SERVICIO,");
        sql.append("NVL2(A.FE_ENT_MSJ,TO_CHAR(A.FE_ENT_MSJ,'DD/MM/YYYY'),' ') FE_ENT_MSJ, ");
        sql.append("NVL2(A.FE_DEV_MSJ,TO_CHAR(A.FE_DEV_MSJ,'DD/MM/YYYY'),' ') FE_DEV_MSJ,");
        sql.append("(SELECT DE_EST FROM TDTR_ESTADOS WHERE CO_EST=A.ES_DOC_REC AND DE_TAB='TD_MENSAJERIA') EST_MSJ,A.ES_DOC_REC COD_EST_MSJ,M.DE_AMBITO,A.FE_PLA_MSJ_D,A.ES_PEN_MSJ,NVL2(A.OB_MSJ,A.OB_MSJ,' ') OB_MSJ,NVL2(A.MO_MSJ_DEV,A.MO_MSJ_DEV,' ') MO_MSJ_DEV, "); 
        sql.append("NVL2(A.HO_ENT_MSJ,TO_CHAR(A.HO_ENT_MSJ,'HH24:MI'),' ') HO_ENT_MSJ,");
        sql.append("NVL2(A.HO_DEV_MSJ,TO_CHAR(A.HO_DEV_MSJ,'HH24:MI'),' ') HO_DEV_MSJ,");
        sql.append(" CASE WHEN TOTAL>0 THEN 'SI' ELSE 'NO' END tieneanexocargo,TO_CHAR(B.FE_EMI,'DD/MM/YYYY') FE_EMI,TO_CHAR(B.FEC_RECEPMP,'DD/MM/YYYY') FEC_RECEPMP,A.DIAS_PLA_DEVO_D,A.ES_PEN_DEV,A.PE_ENV_MSJ_D ");
        sql.append("FROM TDTV_DESTINOS A INNER JOIN TDTV_REMITOS B ON A.NU_ANN = B.NU_ANN AND A.NU_EMI = B.NU_EMI ");
        sql.append(" LEFT JOIN ( SELECT D.CO_DEPENDENCIA,D.DE_DEPENDENCIA,D.DE_CORTA_DEPEN,L.DE_NOMBRE_LOCAL,L.DE_DIRECCION_LOCAL,NODEP,NOPRV,NODIS ,L.CCOD_LOCAL ");
        sql.append(" FROM RHTM_DEPENDENCIA D ");
        sql.append(" INNER JOIN SITM_LOCAL_DEPENDENCIA DL ON DL.CO_DEP=D.CO_DEPENDENCIA ");
        sql.append(" INNER JOIN SI_MAE_LOCAL L ON L.CCOD_LOCAL=DL.CO_LOC ");
        sql.append(" LEFT JOIN IDTUBIAS U ON U.UBDEP||U.UBPRV||U.UBDIS =L.CO_UBIGEO) DEP ON DEP.CO_DEPENDENCIA=A.CO_DEP_DES AND DEP.CCOD_LOCAL=A.CO_LOC_DES ");
        sql.append(" INNER JOIN TD_MENSAJERIA M ON A.NU_MSJ=M.NU_MSJ "); 
        //sql.append(" LEFT JOIN ( select  nu_ann,nu_emi,nu_des, COUNT(1) TOTAL    from tdtv_anexos  GROUP BY nu_ann,nu_emi,nu_des ) TB ON  TB.nu_ann = A.nu_ann  AND TB.nu_emi=A.nu_emi AND TB.nu_des=A.nu_des "); 
        sql.append(" LEFT JOIN ( select  nu_ann,nu_emi,nu_des, COUNT(1) TOTAL    from TDTV_ANEXOS_MSJ  GROUP BY nu_ann,nu_emi,nu_des ) TB ON  TB.nu_ann = A.nu_ann  AND TB.nu_emi=A.nu_emi AND TB.nu_des=A.nu_des "); //Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
        sql.append(" WHERE  A.NU_ANN =? AND A.NU_EMI =? AND A.NU_DES =? AND  A.NU_MSJ =? ");                        
        sql.append("ORDER BY 3");
                      
        try {
            //objectParam,
            mensajesConsulBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(MensajesConsulBean.class),
                    new Object[]{nu_ann, nu_emi, nu_des, nu_msj});

        } catch (EmptyResultDataAccessException e) {
//            list = null;
        } catch (Exception e) {
//            list = null;
            e.printStackTrace();
        }
        return mensajesConsulBean;
    }

    @Override
    public DocumentoObjBean getNombreArchivoMsj(String pnuAnn, String pnuEmi, String pnuAnexo) {
                StringBuilder sql = new StringBuilder();
        sql.append("select \n" +
                    "nu_ann,\n" +
                    "nu_emi,\n" +
                    "nu_ane,\n" +
                    "de_rut_ori nombre_Archivo,\n" +
                    "nvl(dbms_lob.getlength(bl_doc),0) nu_Tamano\n" +
                    //"from tdtv_anexos\n" +
                    "from TDTV_ANEXOS_MSJ\n" + //Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
                    "where nu_ann = ?\n" +
                    "and nu_emi = ?\n" +
                    "and nu_des = ?");
        logger.info(""+pnuAnn);
        logger.info(""+pnuEmi);
        logger.info(""+pnuAnexo);
        DocumentoObjBean docObjBean = new DocumentoObjBean();

        try {
            docObjBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoObjBean.class),new Object[]{ pnuAnn,pnuEmi,pnuAnexo} );
        } catch (EmptyResultDataAccessException e) {
            docObjBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(docObjBean);
    }

    @Override
    public String deleteMsj(DescargaMensajeBean descargaMensaje) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder(); 
              
        sqlUpd.append("DELETE FROM TDTV_ANEXOS_MSJ \n"  //Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
                + "WHERE\n"
                + "NU_ANN=? AND\n"
                + "NU_EMI=? AND\n"
                + "NU_DES=? AND\n"
                + "NU_ANE=?");//Hermes 31/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
        
        logger.info(descargaMensaje.getNu_ann());
        logger.info(descargaMensaje.getNu_emi());
        logger.info(descargaMensaje.getNu_des());
        
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{descargaMensaje.getNu_ann(), descargaMensaje.getNu_emi(),
                //descargaMensaje.getNu_des()});
                descargaMensaje.getNu_des(), descargaMensaje.getNu_ane()});//Hermes 31/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
                      
            vReturn = "OK";//+"NU_ANN:"+documentoEmiBean.getNuAnn().toString()+"\n NU_EMI:"+documentoEmiBean.getNuEmi();
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();

        }
        return vReturn;   
    }

    @Override
    public String revMensajeriaDocumento(DescargaMensajeBean descargaMensaje) {
        String vReturn = "NO_OK";    
       
        this.spDescargamsj = new SimpleJdbcCall(this.dataSource).withCatalogName("PK_SGD_MENSAJERIA").withProcedureName("revmsj")
        .withoutProcedureColumnMetaDataAccess()
        .useInParameterNames("pnu_ann", "pnu_emi", "pnu_des", "pnu_msj")
                //descargamsj(pnu_ann tdtv_remitos.nu_ann%TYPE,pnu_emi tdtv_remitos.nu_emi%TYPE,pnu_des tdtv_destinos.nu_des%TYPE,pnu_msj tdtv_destinos.nu_msj%TYPE,pfecha tdtv_destinos.fec_finmsj%TYPE,pob_msj tdtv_destinos.ob_msj%TYPE,pmo_msj_dev tdtv_destinos.mo_msj_dev%TYPE,pes_pen_msj tdtv_destinos.es_pen_msj%TYPE,pest VARCHAR2);
        .declareParameters(
                new SqlParameter("pnu_ann", Types.VARCHAR),
                new SqlParameter("pnu_emi", Types.VARCHAR),
                new SqlParameter("pnu_des", Types.VARCHAR),
                new SqlParameter("pnu_msj", Types.VARCHAR)               
                        );
        SqlParameterSource in = new MapSqlParameterSource()
        .addValue("pnu_ann", descargaMensaje.getNu_ann())
        .addValue("pnu_emi", descargaMensaje.getNu_emi())
        .addValue("pnu_des", descargaMensaje.getNu_des())
        .addValue("pnu_msj", descargaMensaje.getNu_msj());                
       
        try {
            this.spDescargamsj.execute(in);
            logger.info("se revirtio");
            vReturn = "OK";
        } catch (Exception ex) {
            //mensaje = "NO_OK";
            ex.printStackTrace();
        }   
       
       
       
        return vReturn;   
    }

    @Override
    public List<DependenciaBean> getLsOficina() {
        StringBuilder sql = new StringBuilder();
       
        sql.append("SELECT deDependencia,coDependencia FROM (SELECT NVL2(CO_DEP_EMI,SUBSTR(PK_SGD_DESCRIPCION.DE_DEPENDENCIA(CO_DEP_EMI), 1, 100),NULL) deDependencia,CO_DEP_EMI coDependencia \n" +
                    "FROM TDTV_REMITOS \n" +
                    "WHERE CO_DEP_EMI IS NOT NULL \n" +
                    "UNION SELECT '.: TODOS :.',NULL \n" +
                    "FROM DUAL ) TB \n" +
                    "ORDER BY deDependencia");
        
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DependenciaBean.class));
            
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public String selectCalFechaPlazo(DescargaMensajeBean descargaMensaje) {
        String vReturn = "NO_OK";
        StringBuilder sqlQry = new StringBuilder();
        StringBuilder sqlQry2 = new StringBuilder();
        
        try{
            
            sqlQry2.append("select TO_CHAR(PK_SGD_CONSULTAS.CALCULAR_DIAS_HABILES(TO_DATE('"+descargaMensaje.getFe_ent_msj()+"','DD/MM/YYYY'),1),'DD/MM/YYYY') FROM DUAL");  
            String snufechai = this.jdbcTemplate.queryForObject(sqlQry2.toString(), String.class);    
             
            int diadev=Integer.parseInt(descargaMensaje.getDi_pla_dev())-1;
            if (diadev<0)
            {
                diadev=0;
            }
            sqlQry.append("select TO_CHAR(PK_SGD_CONSULTAS.CALCULAR_DIAS_HABILES(TO_DATE('"+snufechai+"','DD/MM/YYYY'),"+diadev+"),'DD/MM/YYYY') FROM DUAL");  
            String snufecha = this.jdbcTemplate.queryForObject(sqlQry.toString(), String.class);    
            descargaMensaje.setFe_pla_dev(snufecha); 

            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }

    @Override
    public List<SiElementoBean> getLsMotivo(String pctabCodtab) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CELE_DESELE,CELE_CODELE,NELE_NUMSEC,CELE_DESCOR\n" +
                   "FROM   SI_ELEMENTO\n" +
                   "WHERE  CTAB_CODTAB = ? \n" +
                   "ORDER BY CELE_CODELE");

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
    public List<MensajesConsulBean> getBuscaDocumentosMsj(String codigos) {
        StringBuilder sql = new StringBuilder();
        //MensajesConsulBean mensajesConsulBean=new MensajesConsulBean();
        
//        boolean bBusqFiltro = false;
//        
//        Map<String, Object> objectParam = new HashMap<String, Object>();
//
        List<MensajesConsulBean> list = new ArrayList<MensajesConsulBean>();

        
        sql.append("SELECT  ");
        sql.append("A.NU_MSJ,TO_CHAR(M.FEC_ENVIOMSJ,'DD/MM/YYYY') FEC_ENVIOMSJ, A.NU_ANN,A.NU_EMI,A.NU_DES,PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM)|| ' ' ||DECODE (B.TI_EMI,");
        sql.append("'01', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
        sql.append("'05', B.NU_DOC_EMI || '-' || B.NU_ANN || '/' || B.DE_DOC_SIG,");
        sql.append("B.DE_DOC_SIG)");
        sql.append("DE_TIP_DOC,UPPER(B.DE_ASU) DE_ASU,A.CO_LOC_DES CO_LOCAL,NVL2(A.CO_LOC_DES,SUBSTR(PK_SGD_DESCRIPCION.DE_LOCAL(A.CO_LOC_DES), 1, 100),NULL) DE_LOCAL, ");
        sql.append("B.CO_DEP_EMI CO_DEPENDENCIA,");
        sql.append("NVL2(B.CO_DEP_EMI,SUBSTR(PK_SGD_DESCRIPCION.DE_DEPENDENCIA(B.CO_DEP_EMI), 1, 100),NULL) DE_DEPENDENCIA,");                    
        sql.append("DECODE(A.TI_DES,");
        sql.append("'01', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES),");
        sql.append("'02', PK_SGD_DESCRIPCION.DE_PROVEEDOR (A.NU_RUC_DES),");
        sql.append("'03', PK_SGD_DESCRIPCION.ANI_SIMIL (A.NU_DNI_DES),");
        sql.append("'04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (A.CO_OTR_ORI_DES),");
        sql.append("'05', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES)");
        sql.append(")  DESTINATARIO,");
        sql.append("A.CO_MOT CO_TRAMITE,NVL2(A.CO_MOT,SUBSTR(PK_SGD_DESCRIPCION.MOTIVO(A.CO_MOT), 1, 100),NULL) DE_TRAMITE, ");
        sql.append("A.CO_PRI CO_PRIORIDAD, ");
        sql.append("A.DE_PRO DE_INDICACIONES, ");
        sql.append("A.TI_DES CO_TIPO_DESTINO,");
        sql.append("DECODE(A.TI_DES,");
        sql.append("'01', TRIM(DEP.DE_DIRECCION_LOCAL),");
        sql.append("'02', 'DIRECCION PROVEEDOR',");
        sql.append("'03', 'DIRECCION ASIMIL',");
        sql.append("'04', 'DIRECCION OTROS',");
        sql.append("'05', TRIM(DEP.DE_DIRECCION_LOCAL)) DIRECCION,");
        sql.append("DECODE(A.TI_DES,");
        sql.append("'01', TRIM(DEP.NODEP)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODIS),");
        sql.append("'02', 'DEPARTAMENTO PROVEEDOR',");
        sql.append("'03', 'DEPARTAMENTO ASIMIL',");
        sql.append("'04', 'DEPARTAMENTO OTROS',");
        sql.append("'05', TRIM(DEP.NODEP)||'/'||TRIM(DEP.NOPRV)||'/'||TRIM(DEP.NODIS)) DEPARTAMENTO,M.DE_TIP_MSJ,M.DE_TIP_ENV,");
        sql.append("(CASE WHEN M.DE_TIP_MSJ='COURRIER' THEN (SELECT TRIM(CPRO_RAZSOC) FROM LG_PRO_PROVEEDOR WHERE CPRO_RUC=M.RE_ENV_MSJ) ELSE (SELECT TRIM(CEMP_APEPAT)||' '||TRIM(CEMP_APEMAT) ||' '|| TRIM(CEMP_DENOM) FROM RHTM_PER_EMPLEADOS WHERE CEMP_NU_DNI=M.RE_ENV_MSJ) END) RE_ENV_MSJ, ");
        sql.append("NU_SER_MSJ||'-'||AN_SER_MSJ NU_SERVICIO,");
        sql.append("NVL2(A.FE_ENT_MSJ,TO_CHAR(A.FE_ENT_MSJ,'DD/MM/YYYY'),' ') FE_ENT_MSJ, ");
        sql.append("NVL2(A.FE_DEV_MSJ,TO_CHAR(A.FE_DEV_MSJ,'DD/MM/YYYY'),' ') FE_DEV_MSJ,");
        sql.append("(SELECT DE_EST FROM TDTR_ESTADOS WHERE CO_EST=A.ES_DOC_REC AND DE_TAB='TD_MENSAJERIA') EST_MSJ,A.ES_DOC_REC COD_EST_MSJ,M.DE_AMBITO,TO_CHAR(A.FE_PLA_MSJ_D, 'DD/MM/YYYY hh24:mi:ss') FE_PLA_MSJ,A.ES_PEN_MSJ,NVL2(A.OB_MSJ,A.OB_MSJ,' ') OB_MSJ,NVL2(A.MO_MSJ_DEV,A.MO_MSJ_DEV,' ') MO_MSJ_DEV, "); 
        sql.append("NVL2(A.HO_ENT_MSJ,TO_CHAR(A.HO_ENT_MSJ,'HH24:MI'),' ') HO_ENT_MSJ,");
        sql.append("NVL2(A.HO_DEV_MSJ,TO_CHAR(A.HO_DEV_MSJ,'HH24:MI'),' ') HO_DEV_MSJ,");
        sql.append(" CASE WHEN TOTAL>0 THEN 'SI' ELSE 'NO' END tieneanexocargo,TO_CHAR(B.FE_EMI,'DD/MM/YYYY') FE_EMI,TO_CHAR(B.FEC_RECEPMP,'DD/MM/YYYY') FEC_RECEPMP,A.DIAS_PLA_DEVO_D,A.ES_PEN_DEV,A.PE_ENV_MSJ_D ");
        sql.append("FROM TDTV_DESTINOS A INNER JOIN TDTV_REMITOS B ON A.NU_ANN = B.NU_ANN AND A.NU_EMI = B.NU_EMI ");
        sql.append(" LEFT JOIN ( SELECT D.CO_DEPENDENCIA,D.DE_DEPENDENCIA,D.DE_CORTA_DEPEN,L.DE_NOMBRE_LOCAL,L.DE_DIRECCION_LOCAL,NODEP,NOPRV,NODIS ,L.CCOD_LOCAL ");
        sql.append(" FROM RHTM_DEPENDENCIA D ");
        sql.append(" INNER JOIN SITM_LOCAL_DEPENDENCIA DL ON DL.CO_DEP=D.CO_DEPENDENCIA ");
        sql.append(" INNER JOIN SI_MAE_LOCAL L ON L.CCOD_LOCAL=DL.CO_LOC ");
        sql.append(" LEFT JOIN IDTUBIAS U ON U.UBDEP||U.UBPRV||U.UBDIS =L.CO_UBIGEO) DEP ON DEP.CO_DEPENDENCIA=A.CO_DEP_DES AND DEP.CCOD_LOCAL=A.CO_LOC_DES ");
        sql.append(" INNER JOIN TD_MENSAJERIA M ON A.NU_MSJ=M.NU_MSJ "); 
        //sql.append(" LEFT JOIN ( select  nu_ann,nu_emi,nu_des, COUNT(1) TOTAL    from tdtv_anexos  GROUP BY nu_ann,nu_emi,nu_des ) TB ON  TB.nu_ann = A.nu_ann  AND TB.nu_emi=A.nu_emi AND TB.nu_des=A.nu_des "); 
        sql.append(" LEFT JOIN ( select  nu_ann,nu_emi,nu_des, COUNT(1) TOTAL    from TDTV_ANEXOS_MSJ  GROUP BY nu_ann,nu_emi,nu_des ) TB ON  TB.nu_ann = A.nu_ann  AND TB.nu_emi=A.nu_emi AND TB.nu_des=A.nu_des "); //Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
        
        sql.append(" WHERE  A.NU_ANN || A.NU_EMI || A.NU_DES || A.NU_MSJ in (");
        sql.append(codigos);
        sql.append(") ");
        sql.append("ORDER BY 3");

        try {
            //objectParam,
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(MensajesConsulBean.class));

        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String updMensajeriaDocumentoMasivo(DescargaMensajeBean descargaMensaje) {
        String vReturn = "NO_OK";    
                
        if (descargaMensaje.getCo_EstadoDoc().equals("3"))
        {
            descargaMensaje.setMo_msj_dev("");
        }

        if (descargaMensaje.getEs_pen_msj().equals("0"))
        {
            descargaMensaje.setFe_pla_dev("");
            descargaMensaje.setEs_pen_msj("");
            descargaMensaje.setEs_pen_dev("");
        }
        
        
                
                this.spDescargamsj = new SimpleJdbcCall(this.dataSource).withCatalogName("PK_SGD_MENSAJERIA").withProcedureName("descargamsj")
                .withoutProcedureColumnMetaDataAccess()
                .useInParameterNames("pnu_ann", "pnu_emi", "pnu_des", "pnu_msj", "pfechaent","phoraent","pfechadev","phoradev", "pob_msj", "pmo_msj_dev", "pes_pen_msj","pes_pen_dev", "pest","pfepladev")
                        //descargamsj(pnu_ann tdtv_remitos.nu_ann%TYPE,pnu_emi tdtv_remitos.nu_emi%TYPE,pnu_des tdtv_destinos.nu_des%TYPE,pnu_msj tdtv_destinos.nu_msj%TYPE,pfecha tdtv_destinos.fec_finmsj%TYPE,pob_msj tdtv_destinos.ob_msj%TYPE,pmo_msj_dev tdtv_destinos.mo_msj_dev%TYPE,pes_pen_msj tdtv_destinos.es_pen_msj%TYPE,pest VARCHAR2);
                .declareParameters(
                        new SqlParameter("pnu_ann", Types.VARCHAR),
                        new SqlParameter("pnu_emi", Types.VARCHAR),
                        new SqlParameter("pnu_des", Types.VARCHAR),
                        new SqlParameter("pnu_msj", Types.VARCHAR),
                        new SqlParameter("pfechaent", Types.VARCHAR),
                        new SqlParameter("phoraent", Types.VARCHAR), 
                        new SqlParameter("pfechadev", Types.VARCHAR),
                        new SqlParameter("phoradev", Types.VARCHAR),
                        new SqlParameter("pob_msj", Types.VARCHAR),
                        new SqlParameter("pmo_msj_dev", Types.VARCHAR),
                        new SqlParameter("pes_pen_msj", Types.VARCHAR),
                        new SqlParameter("pes_pen_dev", Types.VARCHAR),
                        new SqlParameter("pest", Types.VARCHAR)  ,
                        new SqlParameter("pfepladev", Types.VARCHAR)
                                );
                SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pnu_ann", descargaMensaje.getNu_ann())
                .addValue("pnu_emi", descargaMensaje.getNu_emi())
                .addValue("pnu_des", descargaMensaje.getNu_des())
                .addValue("pnu_msj", descargaMensaje.getNu_msj())
                .addValue("pfechaent", descargaMensaje.getFe_ent_msj())
                .addValue("phoraent", descargaMensaje.getHo_ent_msj())   
                .addValue("pfechadev", descargaMensaje.getFe_dev_msj())
                .addValue("phoradev", descargaMensaje.getHo_dev_msj())
                .addValue("pob_msj", descargaMensaje.getOb_msj())
                .addValue("pmo_msj_dev", descargaMensaje.getMo_msj_dev())
                .addValue("pes_pen_msj", descargaMensaje.getEs_pen_msj())
                .addValue("pes_pen_dev", descargaMensaje.getEs_pen_dev())
                .addValue("pest", descargaMensaje.getCo_EstadoDoc())
                .addValue("pfepladev", descargaMensaje.getFe_pla_dev());
                       
        try {
            this.spDescargamsj.execute(in);
            vReturn = "OK";
            
        } catch (Exception ex) {
            //mensaje = "NO_OK";
            ex.printStackTrace();
        }
        
        return vReturn;  
    }

    //Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
    @Override
    public String deleteMsjAdicional(DescargaMensajeBean descargaMensaje) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder(); 
       
        sqlUpd.append("DELETE FROM TDTV_ANEXOS_MSJ \n"
                + "WHERE\n"
                + "NU_ANN=? AND\n"
                + "NU_EMI=? AND\n"
                + "ES_ESTADO='0'");
        
        logger.info(descargaMensaje.getNu_ann());
        logger.info(descargaMensaje.getNu_emi());
        logger.info(descargaMensaje.getNu_des());
        
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{descargaMensaje.getNu_ann(), descargaMensaje.getNu_emi()});                      
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = e.getMessage();
        }
        return vReturn; 
    }

    @Override
    public String updMensajeriaDocumentoAdicional(final DescargaMensajeBean descargaMensaje, final String coUsu) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();  
        
        sql.append("UPDATE TDTV_ANEXOS_MSJ SET OB_MSJ = ?, CO_USE_MOD= ?, FE_USE_MOD=SYSDATE, ES_ESTADO='1', TIP_DOC_MSJ=? "
                + "WHERE NU_ANN=? AND NU_EMI=? AND NU_ANE=? AND NU_DES=?");

        final LobHandler lobhandler = new DefaultLobHandler();

        try {
            this.jdbcTemplate.execute(sql.toString(),
                    new AbstractLobCreatingPreparedStatementCallback(lobhandler) {
                protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                        throws SQLException {

                    ps.setString(1, descargaMensaje.getOb_msj());
                    ps.setString(2, coUsu);
                    ps.setString(3, descargaMensaje.getTip_doc_msj());
                    ps.setString(4, descargaMensaje.getNu_ann());
                    ps.setString(5, descargaMensaje.getNu_emi());
                    ps.setString(6, descargaMensaje.getNu_ane());
                    ps.setString(7, descargaMensaje.getNu_des());
                }
            });
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }
    
    @Override
    public List<DocumentoAnexoBean> getAnexosListMsj(String pnuAnn, String pnuEmi, String pnuDes) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ROWNUM,  \n" +
                    "nu_ann,\n" +
                    "nu_emi,\n" +
                    "nu_ane,\n" +
                    "de_det,\n" +
                    "de_rut_ori,\n" +
                    "co_use_cre,\n" +
                    //"to_char(fe_use_cre,'yyyy-MM-dd') || ' '|| to_char(fe_use_cre,'HH24:MI') fe_use_cre,\n" +
                    "fe_use_cre,\n" +
                    "co_use_mod,\n" +
                    "fe_use_mod,\n" +
                    "nvl(req_firma,'0') req_firma, \n" +
                    "nu_des,\n" +
                    "nvl(es_proyecto,'0') es_proyecto, \n" +
                    "UPPER(ob_msj) ob_msj, \n" +
                    "tip_doc_msj, \n"+
                    "IDOSGD.PK_SGD_DESCRIPCION.DE_DOMINIOS('TIP_DOC_MSJ', tip_doc_msj) de_doc_msj "+
                    "from tdtv_anexos_msj \n" +
                    "where nu_ann = ? \n" +
                    "and nu_emi = ? \n"+
                    "and nu_des = ? \n"+
                    "and es_estado = '1' \n"+
                    "order by nu_ane desc");
           
        List<DocumentoAnexoBean> list = new ArrayList<DocumentoAnexoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoAnexoBean.class), new Object[]{pnuAnn, pnuEmi, pnuDes});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(list);        
    }    

    @Override
    public String insArchivoAnexoMsj(final DocumentoAnexoDesBean docAnexo,final  InputStream archivoAnexo,final int size, final String inUpd) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();        
        String pcount=getCount(docAnexo.getNuAnn(),docAnexo.getNuEmi(),docAnexo.getNuDes());

        if (inUpd.equals("0")){
            sql.append("insert into TDTV_ANEXOS_MSJ(\n"//Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
            + "nu_ann,\n"
            + "nu_emi,\n"
            + "nu_ane,\n"
            + "de_det,\n"
            + "de_rut_ori,\n"
            + "co_use_cre,\n"
            + "fe_use_cre,\n"
            + "co_use_mod,\n"
            + "fe_use_mod,\n"
            + "feula, \n"
            + "nu_des, \n"
            + "es_estado, \n"
            + "bl_doc )\n"
            + "values(?,?,?,REGEXP_REPLACE(?, '[^-_ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0-9ÁÉÍÓÚáéíóú. ]', ''),REGEXP_REPLACE(?, '[^-_ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0-9ÁÉÍÓÚáéíóú. ]', ' '),?,sysdate,?,sysdate,to_char(sysdate,'yyyymmdd'),?,'0',?)");

            final LobHandler lobhandler = new DefaultLobHandler();

            try {
                this.jdbcTemplate.execute(sql.toString(),
                        new AbstractLobCreatingPreparedStatementCallback(lobhandler) {
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                            throws SQLException {
                        ps.setString(1, docAnexo.getNuAnn());
                        ps.setString(2, docAnexo.getNuEmi());
                        ps.setString(3, docAnexo.getNuAne());
                        ps.setString(4, docAnexo.getDeDet());
                        ps.setString(5, docAnexo.getDeRutOri());
                        ps.setString(6, docAnexo.getCoUseCre());
                        ps.setString(7, docAnexo.getCoUseMod());
                        ps.setString(8, docAnexo.getNuDes());
                        lobCreator.setBlobAsBinaryStream(ps, 9, archivoAnexo, size);
                    }
                });
                vReturn = "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        }else{
            sql.append("update TDTV_ANEXOS_MSJ set\n"  //Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
                + "de_det=REGEXP_REPLACE(?, '[^-_ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0-9ÁÉÍÓÚáéíóú. ]', ' '),\n"
                + "de_rut_ori=REGEXP_REPLACE(?, '[^-_ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0-9ÁÉÍÓÚáéíóú. ]', ' '),\n"
                + "co_use_cre=?,\n"
                + "fe_use_cre=sysdate,\n"
                + "co_use_mod=?,\n"
                + "fe_use_mod=sysdate,\n"
                + "feula=to_char(sysdate,'yyyymmdd'),\n"
                + "bl_doc=?\n"
                + "where \n"
                + "nu_ann=? and nu_emi=? and nu_ane=?");

            final LobHandler lobhandler = new DefaultLobHandler();

            try {
                this.jdbcTemplate.execute(sql.toString(),
                        new AbstractLobCreatingPreparedStatementCallback(lobhandler) {
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                            throws SQLException {
            
                        ps.setString(1, docAnexo.getDeDet());
                        ps.setString(2, docAnexo.getDeRutOri());
                        ps.setString(3, docAnexo.getCoUseCre());
                        ps.setString(4, docAnexo.getCoUseMod());
                        lobCreator.setBlobAsBinaryStream(ps, 5, archivoAnexo, size);
                        ps.setString(6, docAnexo.getNuAnn());
                        ps.setString(7, docAnexo.getNuEmi());
                        ps.setString(8, docAnexo.getNuAne());
                    }
                });
                vReturn = "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }  
        return vReturn;                
    }     
    
    @Override
    public DocumentoObjBean getNombreArchivoMsjAdicional(String pnuAnn, String pnuEmi, String pnuAnexo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select \n" +
                    "nu_ann,\n" +
                    "nu_emi,\n" +
                    "nu_ane,\n" +
                    "de_rut_ori nombre_Archivo,\n" +
                    "nvl(dbms_lob.getlength(bl_doc),0) nu_Tamano\n" +
                    "from TDTV_ANEXOS_MSJ\n" +
                    "where nu_ann = ?\n" +
                    "and nu_emi = ?\n" +
                    "and nu_ane = ?");
        logger.info(""+pnuAnn);
        logger.info(""+pnuEmi);
        logger.info(""+pnuAnexo);
        DocumentoObjBean docObjBean = new DocumentoObjBean();

        try {
            docObjBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoObjBean.class),new Object[]{ pnuAnn,pnuEmi,pnuAnexo} );
        } catch (EmptyResultDataAccessException e) {
            docObjBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(docObjBean);        
    }    
    //Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019    

    @Override
    public List<DocumentoAnexoBean> getAnexosListMsj2(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ROWNUM,  \n" +
                    "nu_ann,\n" +
                    "nu_emi,\n" +
                    "nu_ane,\n" +
                    "de_det,\n" +
                    "de_rut_ori,\n" +
                    "co_use_cre,\n" +
                    "fe_use_cre,\n" +
                    "co_use_mod,\n" +
                    "fe_use_mod,\n" +
                    "nvl(req_firma,'0') req_firma, \n" +
                    "nu_des,\n" +
                    "nvl(es_proyecto,'0') es_proyecto, \n" +
                    "UPPER(ob_msj) ob_msj, \n" +
                    "tip_doc_msj, \n"+
                    "IDOSGD.PK_SGD_DESCRIPCION.DE_DOMINIOS('TIP_DOC_MSJ', tip_doc_msj) de_doc_msj "+
                    "from tdtv_anexos_msj \n" +
                    "where nu_ann = ? \n" +
                    "and nu_emi = ? \n"+
                    "and es_estado = '1' \n"+
                    "order by nu_ane desc");
           
        List<DocumentoAnexoBean> list = new ArrayList<DocumentoAnexoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoAnexoBean.class), new Object[]{pnuAnn, pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(list);         
    }

    @Override
    public List<SiElementoBean> getListTipoDocAdicionalMsj(String pctabCodtab) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT CELE_DESELE,CELE_CODELE\n" +
                   " FROM   SI_ELEMENTO\n" +
                   " WHERE  CTAB_CODTAB = ? AND ESTADO='1'\n" +
                   " ORDER BY CELE_CODELE");

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
    public String updMensajeriaDocumentoAntesEliminar(final DescargaMensajeBean descargaMensaje) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();  
        
        sql.append("UPDATE TDTV_ANEXOS_MSJ SET CO_USE_ELI= ?, FE_USE_ELI=SYSDATE "
                + "WHERE NU_ANN=? AND NU_EMI=? AND NU_ANE=? AND NU_DES=?");

        final LobHandler lobhandler = new DefaultLobHandler();

        try {
            this.jdbcTemplate.execute(sql.toString(),
                    new AbstractLobCreatingPreparedStatementCallback(lobhandler) {
                protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                        throws SQLException {

                    ps.setString(1, descargaMensaje.getCoUseMod());
                    ps.setString(2, descargaMensaje.getNu_ann());
                    ps.setString(3, descargaMensaje.getNu_emi());
                    ps.setString(4, descargaMensaje.getNu_ane());
                    ps.setString(5, descargaMensaje.getNu_des());
                }
            });
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }
}
