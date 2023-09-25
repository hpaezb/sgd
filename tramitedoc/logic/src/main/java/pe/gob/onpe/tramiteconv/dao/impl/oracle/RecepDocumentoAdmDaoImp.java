/**
 *
 */
package pe.gob.onpe.tramiteconv.dao.impl.oracle;

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
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import pe.gob.onpe.tramitedoc.util.Paginacion;

/**
 * @author ecueva
 *
 */
@Repository("recepDocumentoAdmDao")
public class RecepDocumentoAdmDaoImp extends SimpleJdbcDaoBase implements RecepDocumentoAdmDao {

    private SimpleJdbcCall spPuActualizaGuiaMp, spActualizaEstado;
    private static Logger logger=Logger.getLogger(RecepDocumentoAdmDaoImp.class);
    /* (non-Javadoc)
     * @see pe.gob.onpe.tramitedoc.dao.impl.RecepDocumentoAdmDao#getDocumentosRecepAdm(pe.gob.onpe.tramitedoc.bean.BuscarDocumentoRecepBean)
     */
//	@Override
//	public List<DocumentoBean> getDocumentosRecepAdm(BuscarDocumentoRecepBean buscarDocumentoRecep){
//        StringBuffer sql = new StringBuffer();
//        sql.append("SELECT NU_ANN,NU_EMI,TI_CAP,NU_COR_DES,TO_CHAR(FE_EMI,'DD/MM/YY') FE_EMI_CORTA,FE_EMI,DE_ORI_EMI,DE_TIP_DOC_ADM,NU_DOC,DE_EMP_DES,DE_ASU_M,NU_EXPEDIENTE,DE_ES_DOC_DES,");
//        sql.append("'1' EXISTE_DOC,PK_SGD_TRAMITE.FU_DOC_ANE(NU_ANN,NU_EMI) EXISTE_ANEXO,NU_DES ");
//        sql.append("FROM TDVV_DESTINOS_ADM ");
//        sql.append("WHERE ");        
//        sql.append(" NU_ANN = NVL(?/*:B_01_ANN.DE_ANIO*/, NU_ANN)");
//        sql.append(" AND CO_DEP_DES = ?/*:GLOBAL.VCOD_DEP*/ ");
//        sql.append(" AND CO_TIP_DOC_ADM = NVL(?/*:B_01_ANN.DE_DOC_DEP*/,CO_TIP_DOC_ADM)");
//        sql.append(" AND ES_DOC_REC = DECODE(?/*:B_01_ANN.DE_ESTADOS*/, NULL, ES_DOC_REC, ?/*:B_01_ANN.DE_ESTADOS*/)");
//        sql.append(" AND CO_PRI     = DECODE(?/*:B_01_ANN.DE_PRI*/, NULL, CO_PRI, ?/*:B_01_ANN.DE_PRI*/) ");
//        sql.append(" AND (  CO_EMP_DES = DECODE(?/*:GLOBAL.TI_ACCESO*/, 0, CO_EMP_DES, ?/*:GLOBAL.USER*/)");
//        sql.append("	OR (   CO_EMP_DES IS NULL ");
//        sql.append("	   AND TI_DES ='01'");
//        sql.append("	   )");
//        sql.append("	)");
//        sql.append(" AND nvl(TI_EMI_REF,'0') = NVL(?/*:B_01_ANN.TI_EMI_REF*/, nvl(TI_EMI_REF,'0'))");
//        sql.append(" AND NVL(CO_EMP_DES,'NULO') = NVL(?/*:B_01_ANN.TI_EMP_DES*/, NVL(CO_EMP_DES,'NULO'))");
//        sql.append(" AND (  (   NVL(?/*:B_01_ANN.LI_EXPE*/, 'NULO') = 'SINEX'");
//        sql.append("	   AND CO_EXP IS NULL");
//        sql.append("	   )");
//        sql.append("	OR (   NVL(?/*:B_01_ANN.LI_EXPE*/, 'NULO') != 'NULO'");
//        sql.append("	   AND CO_EXP = ?/*:B_01_ANN.LI_EXPE*/");
//        sql.append("	   )");
//        sql.append("	OR ?/*:B_01_ANN.LI_EXPE*/ IS NULL");
//        sql.append("	)");
//        sql.append(" ORDER BY FE_EMI DESC ");
//        List<DocumentoBean> list = new ArrayList<DocumentoBean>();
//        try {
//            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class),
//                    new Object[]{buscarDocumentoRecep.getsCoAnnio(),buscarDocumentoRecep.getsCoDependencia(),buscarDocumentoRecep.getsTipoDoc(),
//                    buscarDocumentoRecep.getsEstadoDoc(),buscarDocumentoRecep.getsEstadoDoc(),buscarDocumentoRecep.getsPrioridadDoc(),buscarDocumentoRecep.getsPrioridadDoc(),
//                    buscarDocumentoRecep.getsTiAcceso(),buscarDocumentoRecep.getsCoEmpleado(),buscarDocumentoRecep.getsRemitente(),buscarDocumentoRecep.getsDestinatario(),
//                    buscarDocumentoRecep.getsExpediente(),buscarDocumentoRecep.getsExpediente(),buscarDocumentoRecep.getsExpediente(),buscarDocumentoRecep.getsExpediente()});
//        }catch (EmptyResultDataAccessException e) {
//            list = null;
//        }catch (Exception e) {
//            list = null;
//            e.printStackTrace();
//        }
//        return list;
//    }
    @Override
    public List<DocumentoBean> getDocumentosBuscaRecepAdm(BuscarDocumentoRecepBean buscarDocumentoRecep) {
        StringBuilder sql = new StringBuilder();
        boolean bBusqFiltro = false;
        String sOrdenList="";
        Map<String, Object> objectParam = new HashMap<String, Object>();

        List<DocumentoBean> list = new ArrayList<DocumentoBean>();

        sql.append("SELECT X.*,");
        sql.append(" PK_SGD_DESCRIPCION.TI_EMI_EMP(X.NU_ANN, X.NU_EMI) DE_ORI_EMI,");
        sql.append(" PK_SGD_DESCRIPCION.DE_DOCUMENTO(X.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append(" PK_SGD_DESCRIPCION.ESTADOS(X.ES_DOC_REC,'TDTV_DESTINOS') DE_ES_DOC_DES,");
        sql.append(" DECODE (X.TI_DES,");
        sql.append(" '01', PK_SGD_DESCRIPCION.DE_NOM_EMP(X.CO_EMP_DES),");
        sql.append(" '02', PK_SGD_DESCRIPCION.DE_NOM_EMP(X.NU_RUC_DES),");
        sql.append(" '03', PK_SGD_DESCRIPCION.ANI_SIMIL(X.NU_DNI_DES),");
        sql.append(" '04', PK_SGD_DESCRIPCION.OTRO_ORIGEN(X.CO_OTR_ORI_DES)");
        sql.append(" ) DE_EMP_DES,");
        sql.append("ROWNUM");
        sql.append(" FROM ( ");
        sql.append("SELECT A.NU_ANN,A.NU_EMI,A.TI_CAP,B.NU_COR_DES,TO_CHAR(A.FE_EMI,'DD/MM/YYYY') FE_EMI_CORTA,A.FE_EMI,");
        sql.append(" A.CO_TIP_DOC_ADM , C.NU_DOC,");
        sql.append(" B.TI_DES,B.CO_EMP_DES,B.NU_RUC_DES,B.NU_DNI_DES, B.CO_OTR_ORI_DES,");
        sql.append(" UPPER (A.DE_ASU) DE_ASU_M,C.NU_EXPEDIENTE,");
        sql.append(" C.IN_EXISTE_DOC EXISTE_DOC,DECODE(NVL(C.TI_EMI_REF,'0')||NVL(C.IN_EXISTE_ANEXO,'2'),'00',0,'02',0,1) EXISTE_ANEXO,NU_DES,NVL(B.CO_PRI,'1') CO_PRI, B.ES_DOC_REC,");
        sql.append(" B.CO_ETIQUETA_REC");
        sql.append(" FROM TDTV_REMITOS A, TDTV_DESTINOS B,TDTX_REMITOS_RESUMEN C");
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
            
            //jaznaero
            if (buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                //selecciono todo
            }
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                //selecciona nada    
            }
            if (buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                //oficina    
                sql.append(" AND B.CO_EMP_DES  in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina  )");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
            } 
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                //otros    
                sql.append(" AND not B.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina ) "); 
                    sql.append(" AND not B.CO_EMP_DES in ( :pcoEmpDesAux ) ");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
                    objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
            }                        
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                //personal    
                sql.append(" AND not B.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina ) "); 
                    sql.append(" AND B.CO_EMP_DES in ( :pcoEmpDesAux ) ");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
                    objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
            }  
            if (buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                
                sql.append(" AND (B.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina) or  B.CO_EMP_DES = :pcoEmpDesAux) ");
                objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
                objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
                
            }            
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                    sql.append(" AND not B.CO_EMP_DES  in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina  )");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
            }
            if (buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                    sql.append(" AND not B.CO_EMP_DES in ( :pcoEmpDesAux ) ");
                    objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
            }
            //jazanero
            
            if (buscarDocumentoRecep.getCoTema()!= null && buscarDocumentoRecep.getCoTema().trim().length() > 0) {
                sql.append(" AND B.CO_TEMA = :pCoTema ");
                objectParam.put("pCoTema", buscarDocumentoRecep.getCoTema());
            }
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
                sql.append(" AND nvl(C.CO_DEP_EMI_REF,'0') = :pTiEmiRef ");
                objectParam.put("pTiEmiRef", buscarDocumentoRecep.getsRemitente());
            }
            if (buscarDocumentoRecep.getIdEtiqueta() != null && buscarDocumentoRecep.getIdEtiqueta().trim().length() > 0) {
                sql.append(" AND B.CO_ETIQUETA_REC = :pcoEtiquetaRec ");
                objectParam.put("pcoEtiquetaRec", buscarDocumentoRecep.getIdEtiqueta());
            }
            
            if(buscarDocumentoRecep.getBusResultado().equals("1"))
            {
                if(buscarDocumentoRecep.getCoTipoPersona().equals("03")){
                    sql.append(" AND A.NU_DNI_EMI = :pNumDni");
                    objectParam.put("pNumDni", buscarDocumentoRecep.getBusNumDni());
                    }
                    else if(buscarDocumentoRecep.getCoTipoPersona().equals("02")){
                        sql.append(" AND A.NU_RUC_EMI = :pNumRuc");
                        objectParam.put("pNumRuc", buscarDocumentoRecep.getBusNumRuc());
                    }
                    else if(buscarDocumentoRecep.getCoTipoPersona().equals("04")){
                        sql.append(" AND A.CO_OTR_ORI_EMI = :pCoOtr");
                        objectParam.put("pCoOtr", buscarDocumentoRecep.getBusCoOtros());
                }
            }    
            if (buscarDocumentoRecep.getEsFiltroFecha() != null
                    && (buscarDocumentoRecep.getEsFiltroFecha().equals("1") || buscarDocumentoRecep.getEsFiltroFecha().equals("3"))) {
                String vFeEmiIni = buscarDocumentoRecep.getsFeEmiIni();
                String vFeEmiFin = buscarDocumentoRecep.getsFeEmiFin();
                if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
                        && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                    sql.append(" AND A.FE_EMI between TO_DATE(:pFeEmiIni,'dd/mm/yyyy') AND TO_DATE(:pFeEmiFin,'dd/mm/yyyy') + 0.99999");
                    objectParam.put("pFeEmiIni", vFeEmiIni);
                    objectParam.put("pFeEmiFin", vFeEmiFin);
                }
            }
        }

        if (pTipoBusqueda.equals("1")) {
            if (buscarDocumentoRecep.getsNroDocumento() != null && buscarDocumentoRecep.getsNroDocumento().trim().length() > 1) {
                sql.append(" AND A.NU_DOC_EMI LIKE ''||:pnuDocEmi||'%' ");
                objectParam.put("pnuDocEmi", buscarDocumentoRecep.getsNroDocumento());
            }

            if (buscarDocumentoRecep.getsBuscNroExpediente() != null && buscarDocumentoRecep.getsBuscNroExpediente().trim().length() > 1) {
                sql.append(" AND C.NU_EXPEDIENTE LIKE '%'||:pnuExpediente||'%' ");
                objectParam.put("pnuExpediente", buscarDocumentoRecep.getsBuscNroExpediente());
            }

            // Busqueda del Asunto
            if (buscarDocumentoRecep.getsBuscAsunto() != null && buscarDocumentoRecep.getsBuscAsunto().trim().length() > 1) {
                //sql.append(" AND CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue( buscarDocumentoRecep.getsBuscAsunto())+"', 1 ) > 1 ");
                sql.append(" AND UPPER(A.DE_ASU) LIKE '%'||:pDeAsunto||'%' ");
                objectParam.put("pDeAsunto", buscarDocumentoRecep.getsBuscAsunto());
            }

        }

        sql.append(" ORDER BY A.FE_EMI").append(sOrdenList);
        sql.append(") X ");
        sql.append("WHERE ROWNUM < 101");

        try {

            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoBean.class));

            /*
             list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class),
             new Object[]{buscarDocumentoRecep.getsCoAnnio(),buscarDocumentoRecep.getsCoDependencia(),buscarDocumentoRecep.getsNroDocumento(),buscarDocumentoRecep.getsNroDocumento(),
             buscarDocumentoRecep.getsBuscNroExpediente(),buscarDocumentoRecep.getsBuscNroExpediente(),buscarDocumentoRecep.getsBuscNroExpediente(),buscarDocumentoRecep.getsBuscTipoDoc(),buscarDocumentoRecep.getsBuscTipoDoc(),
             buscarDocumentoRecep.getsBuscEstado(),buscarDocumentoRecep.getsBuscEstado(),buscarDocumentoRecep.getsBuscAsunto(),buscarDocumentoRecep.getsBuscAsunto(),
             buscarDocumentoRecep.getsFechaEmisionIni(),buscarDocumentoRecep.getsFechaEmisionIni(),buscarDocumentoRecep.getsFechaEmisionIni(),buscarDocumentoRecep.getsFechaEmisionFin(),buscarDocumentoRecep.getsFechaEmisionFin(),buscarDocumentoRecep.getsUoremitente(),buscarDocumentoRecep.getsUoremitente(),
             buscarDocumentoRecep.getsUoDestinatario(),buscarDocumentoRecep.getsUoDestinatario(),buscarDocumentoRecep.getsNroEmision()});
             */
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DocumentoBean> getDocumentosBuscaRecepAdm2(BuscarDocumentoRecepBean buscarDocumentoRecep, String pnuPagina, int pnuRegistros) {
        StringBuilder sql = new StringBuilder();
        boolean bBusqFiltro = false;
        String sOrdenList="";
        Map<String, Object> objectParam = new HashMap<String, Object>();

        List<DocumentoBean> list = new ArrayList<DocumentoBean>();
        sql.append("SELECT * FROM ( SELECT A.*, ROWNUM row_number FROM ( ");/*HPB 20/02/2020 - Requerimiento Paginación recepcionados*/
        sql.append("SELECT X.*, ");
        sql.append(" PK_SGD_DESCRIPCION.TI_EMI_EMP(X.NU_ANN, X.NU_EMI) DE_ORI_EMI, ");
        sql.append(" PK_SGD_DESCRIPCION.DE_DOCUMENTO(X.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append(" PK_SGD_DESCRIPCION.ESTADOS(X.ES_DOC_REC, 'TDTV_DESTINOS') DE_ES_DOC_DES, ");
        sql.append(" DECODE(X.TI_DES, ");
        sql.append(" '01',IDOSGD.PK_SGD_DESCRIPCION.DE_NOM_EMP(X.CO_EMP_DES), ");
        sql.append(" '02',IDOSGD.PK_SGD_DESCRIPCION.DE_NOM_EMP(X.NU_RUC_DES), ");
        sql.append(" '03',IDOSGD.PK_SGD_DESCRIPCION.ANI_SIMIL(X.NU_DNI_DES), ");
        sql.append(" '04',IDOSGD.PK_SGD_DESCRIPCION.OTRO_ORIGEN(X.CO_OTR_ORI_DES)) DE_EMP_DES, ");
        sql.append(" ROWNUM ");
        sql.append(" FROM ( ");
        sql.append(" SELECT A.NU_ANN, A.NU_EMI, A.TI_CAP, B.NU_COR_DES, TO_CHAR(A.FE_EMI, 'DD/MM/YYYY') FE_EMI_CORTA, A.FE_EMI, A.CO_TIP_DOC_ADM,C.NU_DOC, ");
        sql.append(" B.TI_DES, B.CO_EMP_DES, B.NU_RUC_DES, B.NU_DNI_DES, B.CO_OTR_ORI_DES, UPPER(A.DE_ASU) DE_ASU_M, C.NU_EXPEDIENTE, C.IN_EXISTE_DOC EXISTE_DOC, ");
        sql.append(" DECODE(NVL(C.TI_EMI_REF, '0') || NVL(C.IN_EXISTE_ANEXO, '2'), ");
        sql.append(" '00', 0, ");
        sql.append(" '02', 0, 1) EXISTE_ANEXO, ");
        /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
        sql.append(" TO_CHAR(A.FE_PLA_ATE, 'DD/MM/YYYY') FE_PLA_ATE,");        
        sql.append(" CASE ");
        sql.append(" WHEN (TRUNC(A.FE_PLA_ATE) - TRUNC(SYSDATE)) <= 2 AND ");
        sql.append(" (TRUNC(A.FE_PLA_ATE) - TRUNC(SYSDATE)) > 0 AND a.fe_ate_doc_emi IS NULL");
        //sql.append(" AND b.fe_ate_doc IS NULL AND b.fe_arc_doc IS NULL AND d.in_dia = 0 ");
        sql.append(" THEN ");
        sql.append(" '2' ");
        sql.append(" WHEN TRUNC(SYSDATE) > TRUNC(A.FE_PLA_ATE) AND a.fe_ate_doc_emi IS NULL ");
        //sql.append(" AND b.fe_ate_doc IS NULL AND b.fe_arc_doc IS NULL AND d.in_dia = 0 ");
        sql.append(" THEN ");
        sql.append(" '5' ");
        sql.append(" WHEN TRUNC(SYSDATE) = TRUNC(A.FE_PLA_ATE) AND a.fe_ate_doc_emi IS NULL ");
        //sql.append(" AND b.fe_ate_doc IS NULL AND b.fe_arc_doc IS NULL AND d.in_dia = 0 ");
        sql.append(" THEN ");
        sql.append(" '3' ");
        sql.append(" WHEN a.fe_ate_doc_emi IS NOT NULL ");
        sql.append(" THEN ");
        sql.append(" '4' ");
        sql.append(" WHEN (TRUNC(A.FE_PLA_ATE) - TRUNC(SYSDATE)) > 2 AND a.fe_ate_doc_emi IS NULL");
        //sql.append(" AND b.fe_ate_doc IS NULL AND b.fe_arc_doc IS NULL AND d.in_dia = 0 ");
        sql.append(" THEN ");
        sql.append(" '1' ");
        //sql.append(" WHEN b.fe_ate_doc IS NOT NULL OR b.fe_arc_doc IS NOT NULL THEN ");
        //sql.append(" '4' ");
        sql.append(" WHEN A.FE_PLA_ATE IS NULL AND a.fe_ate_doc_emi IS NULL /*= 0 OR D.in_dia = 0 */ THEN ");
        sql.append(" '0' ");
        sql.append(" END AS CO_EST_PLA, ");
        /*[HPB-02/10/20] Fin - Plazo de Atencion*/         
        sql.append(" B.NU_DES, NVL(B.CO_PRI, '1') CO_PRI, B.ES_DOC_REC, B.CO_ETIQUETA_REC ");
        sql.append(", NVL2(B.DE_AVANCE, '1', '0') CO_AVANCE_REC"); /*HPB 18/09/2019 Avance de recepcionados*/
        sql.append(" FROM IDOSGD.TDTV_REMITOS A ");
        sql.append(" LEFT JOIN IDOSGD.TDTV_DESTINOS B ");
        sql.append(" ON A.NU_ANN = B.NU_ANN ");
        sql.append(" AND A.NU_EMI = B.NU_EMI ");
        sql.append(" LEFT JOIN IDOSGD.TDTX_REMITOS_RESUMEN C ");
        sql.append(" ON B.NU_ANN = C.NU_ANN ");
        sql.append(" AND B.NU_EMI = C.NU_EMI ");
        
        String estadoDoc1=buscarDocumentoRecep.getsEstadoDoc();
        if(estadoDoc1.equals("1")){
            sql.append(" LEFT JOIN (SELECT R.NU_ANN     AS NU_ANN2, ");
            sql.append(" R.NU_EMI     AS NU_EMI2, ");
            sql.append(" R.NU_ANN_REF AS NU_ANN_REF2, ");
            sql.append(" R.NU_EMI_REF AS NU_EMI_REF2, ");
            sql.append(" R.NU_DES_REF AS NU_DES_REF2 ");
            sql.append(" FROM IDOSGD.TDTR_REFERENCIA R ");
            sql.append(" LEFT JOIN IDOSGD.TDTV_REMITOS RE ");
            sql.append(" ON R.NU_ANN = RE.NU_ANN ");
            sql.append(" AND R.NU_EMI = RE.NU_EMI ");
            sql.append(" LEFT JOIN IDOSGD.TDTX_REMITOS_RESUMEN RS ");
            sql.append(" ON RE.NU_ANN = RS.NU_ANN ");
            sql.append(" AND RE.NU_EMI = RS.NU_EMI ");
            /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
            //sql.append(" WHERE RE.ES_DOC_EMI NOT IN ('5', '9', '7')) W ");
            sql.append(" WHERE RE.ES_DOC_EMI NOT IN ('5', '9', '7', 'A', 'B')) W ");
            /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
            sql.append(" ON B.NU_ANN = W.NU_ANN_REF2 ");
            sql.append(" AND B.NU_EMI = W.NU_EMI_REF2 ");
            sql.append(" AND B.NU_DES = W.NU_DES_REF2 ");    
        }
                                        
        sql.append(" WHERE B.CO_DEP_DES = :pCoDepDes");
        
        String pNUAnn = buscarDocumentoRecep.getsCoAnnio();
        if (!(buscarDocumentoRecep.getEsFiltroFecha().equals("1") && pNUAnn.equals("0"))) {
            sql.append(" AND A.NU_ANN = :pNuAnn");
            objectParam.put("pNuAnn", pNUAnn);
        }        
        sql.append(" AND A.ES_ELI = '0'");
        sql.append(" AND A.CO_GRU IN ('1','2','3')");
        /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
        if (buscarDocumentoRecep.isEsPorVencer()) {
            sql.append(" AND ((TRUNC(A.FE_PLA_ATE) - TRUNC(SYSDATE)) <= 2 AND (TRUNC(A.FE_PLA_ATE) - TRUNC(SYSDATE)) > 0 OR (TRUNC(SYSDATE) > TRUNC(A.FE_PLA_ATE)) OR TRUNC(SYSDATE) = TRUNC(A.FE_PLA_ATE) )");
        }
        /*[HPB-02/10/20] Fin - Plazo de Atencion*/
        sql.append(" AND B.ES_ELI = '0'");
        /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
        //sql.append(" AND A.ES_DOC_EMI NOT IN ('5', '9', '7')");
        sql.append(" AND A.ES_DOC_EMI NOT IN ('5', '9', '7', 'A', 'B')");
        /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
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
            
            //jaznaero
            if (buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                //selecciono todo
            }
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                //selecciona nada    
            }
            if (buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                //oficina    
                sql.append(" AND B.CO_EMP_DES  in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina  )");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
            } 
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                //otros    
                sql.append(" AND not B.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina ) "); 
                    sql.append(" AND not B.CO_EMP_DES in ( :pcoEmpDesAux ) ");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
                    objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
            }                        
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                //personal    
                sql.append(" AND not B.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina ) "); 
                    sql.append(" AND B.CO_EMP_DES in ( :pcoEmpDesAux ) ");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
                    objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
            }  
            if (buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                
                sql.append(" AND (B.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina) or  B.CO_EMP_DES = :pcoEmpDesAux) ");
                objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
                objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
                
            }            
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                    sql.append(" AND not B.CO_EMP_DES  in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina  )");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
            }
            if (buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                    sql.append(" AND not B.CO_EMP_DES in ( :pcoEmpDesAux ) ");
                    objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
            }
            //jazanero
            
            if (buscarDocumentoRecep.getCoTema()!= null && buscarDocumentoRecep.getCoTema().trim().length() > 0) {
                sql.append(" AND B.CO_TEMA = :pCoTema ");
                objectParam.put("pCoTema", buscarDocumentoRecep.getCoTema());
            }
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
                sql.append(" AND nvl(C.CO_DEP_EMI_REF,'0') = :pTiEmiRef ");
                objectParam.put("pTiEmiRef", buscarDocumentoRecep.getsRemitente());
            }
            if (buscarDocumentoRecep.getIdEtiqueta() != null && buscarDocumentoRecep.getIdEtiqueta().trim().length() > 0) {
                sql.append(" AND B.CO_ETIQUETA_REC = :pcoEtiquetaRec ");
                objectParam.put("pcoEtiquetaRec", buscarDocumentoRecep.getIdEtiqueta());
            }
            
            if(buscarDocumentoRecep.getBusResultado().equals("1"))
            {
                if(buscarDocumentoRecep.getCoTipoPersona().equals("03")){
                    sql.append(" AND A.NU_DNI_EMI = :pNumDni");
                    objectParam.put("pNumDni", buscarDocumentoRecep.getBusNumDni());
                    }
                    else if(buscarDocumentoRecep.getCoTipoPersona().equals("02")){
                        sql.append(" AND A.NU_RUC_EMI = :pNumRuc");
                        objectParam.put("pNumRuc", buscarDocumentoRecep.getBusNumRuc());
                    }
                    else if(buscarDocumentoRecep.getCoTipoPersona().equals("04")){
                        sql.append(" AND A.CO_OTR_ORI_EMI = :pCoOtr");
                        objectParam.put("pCoOtr", buscarDocumentoRecep.getBusCoOtros());
                }
            }    
            if (buscarDocumentoRecep.getEsFiltroFecha() != null
                    && (buscarDocumentoRecep.getEsFiltroFecha().equals("1") || buscarDocumentoRecep.getEsFiltroFecha().equals("3"))) {
                String vFeEmiIni = buscarDocumentoRecep.getsFeEmiIni();
                String vFeEmiFin = buscarDocumentoRecep.getsFeEmiFin();
                if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
                        && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                    sql.append(" AND A.FE_EMI between TO_DATE(:pFeEmiIni,'dd/mm/yyyy') AND TO_DATE(:pFeEmiFin,'dd/mm/yyyy') + 0.99999");
                    objectParam.put("pFeEmiIni", vFeEmiIni);
                    objectParam.put("pFeEmiFin", vFeEmiFin);
                }
            }
        }

        if (pTipoBusqueda.equals("1")) {
            if (buscarDocumentoRecep.getsNroDocumento() != null && buscarDocumentoRecep.getsNroDocumento().trim().length() > 1) {
                sql.append(" AND A.NU_DOC_EMI LIKE ''||:pnuDocEmi||'%' ");
                objectParam.put("pnuDocEmi", buscarDocumentoRecep.getsNroDocumento());
            }

            if (buscarDocumentoRecep.getsBuscNroExpediente() != null && buscarDocumentoRecep.getsBuscNroExpediente().trim().length() > 1) {
                sql.append(" AND C.NU_EXPEDIENTE LIKE '%'||:pnuExpediente||'%' ");
                objectParam.put("pnuExpediente", buscarDocumentoRecep.getsBuscNroExpediente());
            }

            // Busqueda del Asunto
            if (buscarDocumentoRecep.getsBuscAsunto() != null && buscarDocumentoRecep.getsBuscAsunto().trim().length() > 1) {
                //sql.append(" AND CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue( buscarDocumentoRecep.getsBuscAsunto())+"', 1 ) > 1 ");
                sql.append(" AND UPPER(A.DE_ASU) LIKE '%'||:pDeAsunto||'%' ");
                objectParam.put("pDeAsunto", buscarDocumentoRecep.getsBuscAsunto());
            }

        }
        if(estadoDoc1.equals("1")){
            sql.append(" AND W.NU_ANN2 IS NULL ");
        }
        sql.append(" ORDER BY A.FE_EMI").append(sOrdenList);
        sql.append(") X ");
        sql.append(" ) A WHERE ROWNUM < ((:pNuPagina * :pNuRegistros) + 1 ))WHERE row_number >= (((:pNuPagina-1) * :pNuRegistros) + 1)");/*HPB 20/02/2020 - Requerimiento Paginación recepcionados*/
        objectParam.put("pNuPagina", pnuPagina);
        objectParam.put("pNuRegistros", pnuRegistros);        
        //sql.append("WHERE ROWNUM < 101");
        //logger.info("SQL Recepcion Doc: "+sql);
        System.out.println("SQL Recepcion Doc: "+sql);
        try {

            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoBean.class));

            /*
             list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoBean.class),
             new Object[]{buscarDocumentoRecep.getsCoAnnio(),buscarDocumentoRecep.getsCoDependencia(),buscarDocumentoRecep.getsNroDocumento(),buscarDocumentoRecep.getsNroDocumento(),
             buscarDocumentoRecep.getsBuscNroExpediente(),buscarDocumentoRecep.getsBuscNroExpediente(),buscarDocumentoRecep.getsBuscNroExpediente(),buscarDocumentoRecep.getsBuscTipoDoc(),buscarDocumentoRecep.getsBuscTipoDoc(),
             buscarDocumentoRecep.getsBuscEstado(),buscarDocumentoRecep.getsBuscEstado(),buscarDocumentoRecep.getsBuscAsunto(),buscarDocumentoRecep.getsBuscAsunto(),
             buscarDocumentoRecep.getsFechaEmisionIni(),buscarDocumentoRecep.getsFechaEmisionIni(),buscarDocumentoRecep.getsFechaEmisionIni(),buscarDocumentoRecep.getsFechaEmisionFin(),buscarDocumentoRecep.getsFechaEmisionFin(),buscarDocumentoRecep.getsUoremitente(),buscarDocumentoRecep.getsUoremitente(),
             buscarDocumentoRecep.getsUoDestinatario(),buscarDocumentoRecep.getsUoDestinatario(),buscarDocumentoRecep.getsNroEmision()});
             */
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
        sql.append("SELECT A.NU_ANN,A.NU_EMI,A.TI_EMI,PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_RES) DE_EMP_RES,A.FE_EMI,\n"
                + "PK_SGD_DESCRIPCION.TI_EMI_EMP (A.NU_ANN, A.NU_EMI) DE_ORI_EMI,A.CO_TIP_DOC_ADM,PK_SGD_DESCRIPCION.DE_DOCUMENTO (A.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM,\n"
                + "DECODE (A.TI_EMI, '01', A.NU_DOC_EMI || '-' || A.NU_ANN || '-' || A.DE_DOC_SIG, '05', A.NU_DOC_EMI || '-' || A.NU_ANN || '-' || A.DE_DOC_SIG, A.DE_DOC_SIG) NU_DOC,\n"
                + "DECODE(A.TI_EMI,'02',A.NU_RUC_EMI,'03', A.NU_DNI_EMI, '04', A.CO_OTR_ORI_EMI) NU_ORI_EMI,\n" /*HPB 11/02/2020 - Requerimiento Remitente expediente*/
                + "A.NU_DIA_ATE,\n"
                + "A.FE_PLA_ATE, A.IN_PLA_ATE,\n"/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
                + "A.DE_ASU,B.DE_ANE,B.NU_DES,B.NU_COR_DES,B.CO_DEP_DES,PK_SGD_DESCRIPCION.DE_DEPENDENCIA (B.CO_DEP_DES) DE_DEP_DES,PK_SGD_DESCRIPCION.DE_PRIORIDAD (B.CO_PRI) DE_PRI,\n"
                + "DECODE (B.TI_DES,'01', PK_SGD_DESCRIPCION.DE_NOM_EMP (B.CO_EMP_DES),'02', PK_SGD_DESCRIPCION.DE_NOM_EMP (B.NU_RUC_DES), '03', PK_SGD_DESCRIPCION.ANI_SIMIL (B.NU_DNI_DES), '04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (B.CO_OTR_ORI_DES)) DE_EMP_DES,\n"
                + "PK_SGD_DESCRIPCION.MOTIVO (B.CO_MOT) DE_MOT,B.DE_PRO,B.CO_EMP_REC,PK_SGD_DESCRIPCION.DE_NOM_EMP (B.CO_EMP_REC) DE_EMP_REC, B.ES_DOC_REC, PK_SGD_DESCRIPCION.ESTADOS (B.ES_DOC_REC,'TDTV_DESTINOS') DE_ES_DOC_DES, B.FE_REC_DOC,\n"
                + "B.FE_ARC_DOC, B.FE_ATE_DOC,A.NU_ANN_EXP,A.NU_SEC_EXP,TO_CHAR(A.FE_EMI,'DD/MM/YYYY HH24:MI') FE_EMI_CORTA,TO_CHAR(A.FE_EMI,'DD/MM/YY') FE_EMI_CORTA2,\n"
                + "TO_CHAR(B.FE_REC_DOC,'DD/MM/YYYY HH24:MI') FE_REC_DOC_CORTA, TO_CHAR(B.FE_ARC_DOC,'DD/MM/YYYY HH24:MI') FE_ARC_DOC_CORTA, \n"
                + "TO_CHAR(B.FE_ATE_DOC,'DD/MM/YYYY HH24:MI') FE_ATE_DOC_CORTA,'1' EXISTE_DOC,PK_SGD_DESCRIPCION.FU_DOC_ANE(A.NU_ANN,A.NU_EMI) EXISTE_ANEXO\n"
                + ",B.Ti_Fisico_Rec,B.Co_Etiqueta_Rec\n"
                + "FROM TDTV_REMITOS A,TDTV_DESTINOS B\n"
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
            logger.error("nu_ann=" + pnuAnn + "," + "nu_emi=" + pnuEmi + "nu_des=" + pnuDes);
            e.printStackTrace();
        }
        return documentoBean;
    }

    @Override
    public ExpedienteBean getExpDocumentoRecepAdm(String pnuAnnExp, String pnuSecExp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.NU_ANN_EXP,B.NU_SEC_EXP,FE_EXP,TO_CHAR(FE_EXP,'DD/MM/YYYY') FE_EXP_CORTA,FE_VENCE,CO_PROCESO,PK_SGD_DESCRIPCION.DE_PROCESO_EXP(CO_PROCESO) DE_PROCESO,\n"
                + " DE_DETALLE,CO_DEP_EXP,B.CO_GRU,NU_CORR_EXP,NU_EXPEDIENTE,B.NU_FOLIOS,NU_PLAZO,US_CREA_AUDI,FE_CREA_AUDI,US_MODI_AUDI,FE_MODI_AUDI,ES_ESTADO\n"
                /*[HPB] Inicio 18/08/23 OS-0000786-2023 Mejoras: Mostrar numero expediente al recepcionar*/                 
                //+ " ,A.TI_EMI, DECODE(A.TI_EMI, '02', A.NU_RUC_EMI, '03', A.NU_DNI_EMI, '04', A.CO_OTR_ORI_EMI) NU_ORI_EMI\n"               
                //+ "FROM TDTC_EXPEDIENTE\n"
                + "FROM TDTC_EXPEDIENTE B\n"
                //+ " B INNER JOIN TDTV_REMITOS A\n"
                //+ " ON A.NU_ANN_EXP = B.NU_ANN_EXP AND A.NU_SEC_EXP = B.NU_SEC_EXP \n"
                /*[HPB] Fin 18/08/23 OS-0000786-2023 Mejoras: Mostrar numero expediente al recepcionar*/
                + "where\n"
                /*[HPB] Inicio 18/08/23 OS-0000786-2023 Mejoras: Mostrar numero expediente al recepcionar*/
                //+ "B.NU_ANN_EXP=? and B.NU_SEC_EXP=? AND A.CO_GRU='3'");
                + "B.NU_ANN_EXP=? and B.NU_SEC_EXP=?");
                /*[HPB] Fin 18/08/23 OS-0000786-2023 Mejoras: Mostrar numero expediente al recepcionar*/
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
        sql.append("SELECT PK_SGD_DESCRIPCION.de_documento (a.co_tip_doc_adm) li_tip_doc,           DECODE (a.ti_emi,\n"
                + "                  '01', a.nu_doc_emi || '-' || a.nu_ann || '/' || a.de_doc_sig,\n"
                + "                  '05', a.nu_doc_emi || '-' || a.nu_ann || '/' || a.de_doc_sig,\n"
                + "                  a.de_doc_sig\n"
                + "                 ) li_nu_doc,a.fe_emi,TO_CHAR(a.fe_emi,'DD/MM/YYYY') fe_emi_corta,b.co_ref,b.nu_ann,b.nu_emi,b.nu_des,b.nu_ann_ref,\n"
                + "                 b.nu_emi_ref,b.nu_des_ref\n"
                + " ,a.in_pla_ate, a.IN_RES_PLA_ATE \n" /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
                + "FROM tdtv_remitos a,TDTR_REFERENCIA b\n"
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
        this.spPuActualizaGuiaMp = new SimpleJdbcCall(this.dataSource).withCatalogName("PK_SGD_TRAMITE").withProcedureName("pu_actualiza_guia_mp")
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
        sql.append("SELECT nvl(MAX(nu_cor_des), 0) + 1\n"
                + "FROM tdtv_destinos\n"
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
        this.spActualizaEstado = new SimpleJdbcCall(this.dataSource).withCatalogName("PK_SGD_TRAMITE").withProcedureName("actualiza_estado")
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
        sql.append("UPDATE TDTV_DESTINOS ");
        sql.append("SET ES_DOC_REC = ?, CO_USE_MOD = ?, FE_USE_MOD = SYSDATE");
        //documento recepcionados atendido y derivado esos son los estados.
        if (paccion.equals("1") || paccion.equals("0")/*&& !documentoBean.getFeRecDoc().equals("")*/) {//no leido
            sql.append(",FE_REC_DOC = TO_DATE('");
            sql.append(documentoBean.getFeRecDoc());
            sql.append("','DD/MM/YYYY HH24:MI:SS'),");
            sql.append("CO_EMP_REC ='");
            sql.append(documentoBean.getCoEmpRec());
            sql.append("',FE_ATE_DOC=TO_DATE('");
            sql.append(documentoBean.getFeAteDoc());
            sql.append("','DD/MM/YYYY HH24:MI')");
            sql.append(",FE_ARC_DOC=TO_DATE('");
            sql.append(documentoBean.getFeArcDoc());
            sql.append("','DD/MM/YYYY HH24:MI')");
        }/*else if(paccion.equals("1") && documentoBean.getFeRecDoc().equals("")){//no leido
         sql.append(",FE_REC_DOC=NULL");
         sql.append(",CO_EMP_REC ='");
         sql.append(documentoBean.getCoEmpRec());
         sql.append("'");
         }*/ else if (paccion.equals("2")) {
            sql.append(",CO_EMP_REC=NULL,FE_REC_DOC=NULL,FE_ATE_DOC=NULL,FE_ARC_DOC=NULL");
        }
        sql.append(",NU_COR_DES=?,DE_ANE=?");
        sql.append(" WHERE NU_ANN = ? ");
        sql.append("AND NU_EMI = ? ");
        sql.append("AND NU_DES = ? ");
        String vReturn = "NO_OK";
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                documentoBean.getEsDocRec(), documentoBean.getCodUseMod(), documentoBean.getNuCorDes(),
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
        sql.append("'1' EXISTE_DOC,PK_SGD_TRAMITE.FU_DOC_ANE(NU_ANN,NU_EMI) EXISTE_ANEXO,NU_DES ");
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
//        StringBuffer sql = new StringBuffer();
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
        sql.append("select PK_SGD_DESCRIPCION.ESTADOS(?,'TDTV_DESTINOS') from dual");

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
                + "FROM tdtr_referencia rf,\n"
                + "tdtv_remitos   re\n"
                + "WHERE re.nu_ann = rf.nu_ann\n"
                + "AND re.nu_emi = rf.nu_emi\n"
                + "AND rf.nu_ann_ref = ?\n"
                + "AND rf.nu_emi_ref = ?\n"
                + "AND rf.nu_des_ref = ?\n"
                + "AND re.ES_DOC_EMI <> '9'");
        StringBuilder sqlQry2 = new StringBuilder();
        sqlQry2.append("SELECT COUNT(nu_emi)\n"
                + "FROM tdtv_destinos\n"
                + "WHERE nu_ann = ?\n"
                + "AND nu_emi = ?\n"
                + "AND nu_des <> ?\n"
                + "AND es_doc_rec <> '0'\n"
                + "AND es_eli = '0'");
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("UPDATE tdtv_remitos\n"
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
        sql.append("SELECT A.NU_ANN,A.NU_EMI,A.NU_COR_DOC FROM TDTV_REMITOS A\n"
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
        sql.append("SELECT * FROM (SELECT A.*, ROWNUM row_number FROM ( ");
        sql.append("SELECT A.*, ROWNUM");
        sql.append(" FROM ( ");
        sql.append("SELECT DISTINCT A.NU_ANN,A.NU_EMI,A.TI_CAP,B.NU_COR_DES,TO_CHAR(A.FE_EMI,'DD/MM/YY') FE_EMI_CORTA,A.FE_EMI,");
        sql.append(" PK_SGD_DESCRIPCION.TI_EMI_EMP (A.NU_ANN, A.NU_EMI) DE_ORI_EMI,");
        sql.append(" PK_SGD_DESCRIPCION.DE_DOCUMENTO (A.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM,C.NU_DOC,");
        sql.append(" DECODE (B.TI_DES,");
        sql.append("				  '01', PK_SGD_DESCRIPCION.DE_NOM_EMP (B.CO_EMP_DES),");
        sql.append("				  '02', PK_SGD_DESCRIPCION.DE_NOM_EMP (B.NU_RUC_DES),");
        sql.append("				  '03', PK_SGD_DESCRIPCION.ANI_SIMIL (B.NU_DNI_DES),");
        sql.append("				  '04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (B.CO_OTR_ORI_DES)");
        sql.append("				 ) DE_EMP_DES,");
        sql.append(" UPPER (A.DE_ASU) DE_ASU_M,C.NU_EXPEDIENTE,");
        sql.append(" PK_SGD_DESCRIPCION.ESTADOS (B.ES_DOC_REC,'TDTV_DESTINOS') DE_ES_DOC_DES,");
        sql.append(" C.IN_EXISTE_DOC EXISTE_DOC,DECODE(NVL(C.TI_EMI_REF,'0')||NVL(C.IN_EXISTE_ANEXO,'2'),'00',0,'02',0,1) EXISTE_ANEXO,B.NU_DES,NVL(B.CO_PRI,'1') CO_PRI,B.ES_DOC_REC");
        sql.append(" FROM ( ");
        sql.append("   SELECT NU_ANN, NU_EMI, NU_DES ");
        sql.append("	 FROM TDTR_ARBOL_SEG ");
        sql.append("		  START WITH PK_REF = :pCoAnio||:pNuEmi||'0' ");
        sql.append("		 CONNECT BY PRIOR pk_EMI = PK_REF  ");
        sql.append(" ) D, TDTV_REMITOS A, TDTV_DESTINOS B,TDTX_REMITOS_RESUMEN C");
        sql.append(" WHERE");
        sql.append(" D.NU_ANN = A.NU_ANN");
        sql.append(" AND D.NU_EMI = A.NU_EMI");
        sql.append(" AND B.NU_ANN = A.NU_ANN");
        sql.append(" AND B.NU_EMI = A.NU_EMI");
        sql.append(" AND C.NU_ANN = B.NU_ANN");
        sql.append(" AND C.NU_EMI = B.NU_EMI");
        sql.append(" AND B.CO_DEP_DES = :pCoDepDes");
        if (sTipoAcceso.equals("1")) {
            sql.append(" AND B.CO_EMP_DES = :pcoEmpDes");
            objectParam.put("pcoEmpDes", documentoBean.getCoEmpDes());
        }
        sql.append(" AND A.ES_ELI = '0'");
        sql.append(" AND B.ES_ELI = '0'");
        sql.append(" AND A.ES_DOC_EMI NOT IN ('5', '9', '7')");
        sql.append(" AND A.IN_OFICIO = '0'");

        objectParam.put("pCoAnio", documentoBean.getNuAnn());
        objectParam.put("pNuEmi", documentoBean.getNuEmi());
        objectParam.put("pCoDepDes", documentoBean.getCoDepDes());

        sql.append(" ORDER BY A.FE_EMI DESC");
        sql.append(") A ");
        sql.append(" ) A WHERE ROWNUM < ((:pNuPagina * :pNuRegistros) + 1 ))WHERE row_number >= (((:pNuPagina-1) * :pNuRegistros) + 1)");/*HPB 20/02/2020 - Requerimiento Paginación recepcionados*/
        //sql.append("WHERE ROWNUM < 101");
        objectParam.put("pNuPagina", pnuPagina);
        objectParam.put("pNuRegistros", pnuRegistros);        
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
                + "FROM tdtr_referencia a, tdtv_remitos b \n"
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
        sql.append("SELECT \n"
                + "PK_SGD_DESCRIPCION.de_documento (b.co_tip_doc_adm) li_tip_doc, \n"
                + "DECODE (b.ti_emi,'01', b.nu_doc_emi || '-' || b.nu_ann || '-' || b.de_doc_sig, \n"
                + "                 '05', b.nu_doc_emi || '-' || b.nu_ann || '-' || b.de_doc_sig,b.de_doc_sig) li_nu_doc, \n"
                + "TO_CHAR(b.fe_emi,'DD/MM/YYYY') fe_emi_corta, \n"
                + "b.nu_ann,\n"
                + "b.nu_emi,\n"
                + "to_char(b.nu_cor_emi) nu_cor_emi,\n"
                + "PK_SGD_DESCRIPCION.ESTADOS(b.es_doc_emi,'TDTV_REMITOS') DE_ES_DOC_EMI,\n"
                + "a.nu_ann_ref,\n"
                + "a.nu_emi_ref \n"
                + "FROM tdtr_referencia a, tdtv_remitos b \n"
                + "WHERE a.nu_ann = b.nu_ann \n"
                + "and a.nu_emi = b.nu_emi \n"
                + "and b.es_eli = '0' \n"
                + "and b.es_doc_emi <> '9'\n"
                + "and a.nu_ann_ref = ? \n"
                + "and a.nu_emi_ref = ? \n"
                + "and a.nu_des_ref = ? \n"
                + "order by b.nu_cor_emi desc");

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
        sql.append("UPDATE TDTV_DESTINOS ");
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
        "FROM TDTV_DESTINOS\n" +
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
    public String getEstadoDocAdmBasico(String nuAnn, String nuEmi){
        String vResult="NO_OK";
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ES_DOC_EMI\n" +
                    "FROM TDTV_REMITOS\n" +
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
    
    //servicio rest notificaciones movil
    //Anula la recepcion de un documento
    @Override
    public String updAnulaRecepecionDocumentoBean(String nuAnn, String nuEmi, String nuDes, String coUseMod) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TDTV_DESTINOS ");
        sql.append("SET ES_DOC_REC = '0', CO_USE_MOD = ?, FE_USE_MOD = SYSDATE");
        sql.append(",CO_EMP_REC=NULL,FE_REC_DOC=NULL,FE_ATE_DOC=NULL,FE_ARC_DOC=NULL");
        sql.append(",NU_COR_DES=NULL");
        sql.append(" WHERE NU_ANN = ? ");
        sql.append("AND NU_EMI = ? ");
        sql.append("AND NU_DES = ? ");
        String vReturn = "NO_OK";
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                coUseMod, nuAnn, nuEmi, nuDes});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public String updRecepcionDocumentoBean(DocumentoBean documentoBean) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TDTV_DESTINOS SET ");
        sql.append("ES_DOC_REC = ?,");
        sql.append("CO_USE_MOD = ?,");
        sql.append("CO_EMP_REC ='");
        sql.append(documentoBean.getCoEmpRec());
        sql.append("',FE_USE_MOD = SYSDATE,");
        sql.append(" FE_REC_DOC = TO_DATE('");
        sql.append(documentoBean.getFeRecDoc());
        sql.append("','DD/MM/YYYY HH24:MI:SS'),");
        sql.append(" FE_ATE_DOC=TO_DATE('");
        sql.append(documentoBean.getFeAteDoc());
        sql.append("','DD/MM/YYYY HH24:MI'),");
        sql.append(" FE_ARC_DOC=TO_DATE('");
        sql.append(documentoBean.getFeArcDoc());
        sql.append("','DD/MM/YYYY HH24:MI'),");
        sql.append(" NU_COR_DES=?,");
        sql.append(" DE_ANE=?");
        sql.append(" WHERE NU_ANN = ? ");
        sql.append(" AND NU_EMI = ? ");
        sql.append(" AND NU_DES = ? ");
        String vReturn = "NO_OK";
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                documentoBean.getEsDocRec(), documentoBean.getCodUseMod(), documentoBean.getNuCorDes(),
                documentoBean.getDeAne(), documentoBean.getNuAnn(), documentoBean.getNuEmi(), documentoBean.getNuDes()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public String updAtencionDocumentoBean(DocumentoBean documentoBean) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TDTV_DESTINOS SET ");
        sql.append("ES_DOC_REC = ?,");
        sql.append("CO_USE_MOD = ?,");                          
        if(documentoBean.getFeAteDoc()!=null && documentoBean.getFeAteDoc().trim().length()>0){
            sql.append(" FE_ATE_DOC=TO_DATE('");
            sql.append(documentoBean.getFeAteDoc());
            sql.append("','DD/MM/YYYY HH24:MI'),");
        }
        if(documentoBean.getFeArcDoc()!=null && documentoBean.getFeArcDoc().trim().length()>0){
            sql.append(" FE_ARC_DOC=TO_DATE('");
            sql.append(documentoBean.getFeArcDoc());
            sql.append("','DD/MM/YYYY HH24:MI'), ");
        }        
        sql.append(" FE_USE_MOD = SYSDATE ");   
        sql.append(" WHERE NU_ANN = ? ");
        sql.append(" AND NU_EMI = ? ");
        sql.append(" AND NU_DES = ? ");
        String vReturn = "NO_OK";
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                documentoBean.getEsDocRec(), documentoBean.getCodUseMod(), documentoBean.getNuAnn(), documentoBean.getNuEmi(), documentoBean.getNuDes()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }
    //servicio rest notificaciones movil    
    /*HPB 20/02/2020 - Inicio - Requerimiento Paginación recepcionados*/
    @Override
    public List<DocumentoBean> getDocumentosBuscaRecepAdm2Size(BuscarDocumentoRecepBean buscarDocumentoRecep) {
        StringBuilder sql = new StringBuilder();
        boolean bBusqFiltro = false;
        String sOrdenList="";
        Map<String, Object> objectParam = new HashMap<String, Object>();

        List<DocumentoBean> list = new ArrayList<DocumentoBean>();
        sql.append("SELECT X.*, ");
        sql.append(" PK_SGD_DESCRIPCION.TI_EMI_EMP(X.NU_ANN, X.NU_EMI) DE_ORI_EMI, ");
        sql.append(" PK_SGD_DESCRIPCION.DE_DOCUMENTO(X.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append(" PK_SGD_DESCRIPCION.ESTADOS(X.ES_DOC_REC, 'TDTV_DESTINOS') DE_ES_DOC_DES, ");
        sql.append(" DECODE(X.TI_DES, ");
        sql.append(" '01',IDOSGD.PK_SGD_DESCRIPCION.DE_NOM_EMP(X.CO_EMP_DES), ");
        sql.append(" '02',IDOSGD.PK_SGD_DESCRIPCION.DE_NOM_EMP(X.NU_RUC_DES), ");
        sql.append(" '03',IDOSGD.PK_SGD_DESCRIPCION.ANI_SIMIL(X.NU_DNI_DES), ");
        sql.append(" '04',IDOSGD.PK_SGD_DESCRIPCION.OTRO_ORIGEN(X.CO_OTR_ORI_DES)) DE_EMP_DES, ");
        sql.append(" ROWNUM ");
        sql.append(" FROM ( ");
        sql.append(" SELECT A.NU_ANN, A.NU_EMI, A.TI_CAP, B.NU_COR_DES, TO_CHAR(A.FE_EMI, 'DD/MM/YYYY') FE_EMI_CORTA, A.FE_EMI, A.CO_TIP_DOC_ADM,C.NU_DOC, ");
        sql.append(" B.TI_DES, B.CO_EMP_DES, B.NU_RUC_DES, B.NU_DNI_DES, B.CO_OTR_ORI_DES, UPPER(A.DE_ASU) DE_ASU_M, C.NU_EXPEDIENTE, C.IN_EXISTE_DOC EXISTE_DOC, ");
        sql.append(" DECODE(NVL(C.TI_EMI_REF, '0') || NVL(C.IN_EXISTE_ANEXO, '2'), ");
        sql.append(" '00', 0, ");
        sql.append(" '02', 0, 1) EXISTE_ANEXO, ");
        sql.append(" B.NU_DES, NVL(B.CO_PRI, '1') CO_PRI, B.ES_DOC_REC, B.CO_ETIQUETA_REC ");
        sql.append(", NVL2(B.DE_AVANCE, '1', '0') CO_AVANCE_REC"); /*HPB 18/09/2019 Avance de recepcionados*/
        sql.append(" FROM IDOSGD.TDTV_REMITOS A ");
        sql.append(" LEFT JOIN IDOSGD.TDTV_DESTINOS B ");
        sql.append(" ON A.NU_ANN = B.NU_ANN ");
        sql.append(" AND A.NU_EMI = B.NU_EMI ");
        sql.append(" LEFT JOIN IDOSGD.TDTX_REMITOS_RESUMEN C ");
        sql.append(" ON B.NU_ANN = C.NU_ANN ");
        sql.append(" AND B.NU_EMI = C.NU_EMI ");
        
        String estadoDoc1=buscarDocumentoRecep.getsEstadoDoc();
        if(estadoDoc1.equals("1")){
            sql.append(" LEFT JOIN (SELECT R.NU_ANN     AS NU_ANN2, ");
            sql.append(" R.NU_EMI     AS NU_EMI2, ");
            sql.append(" R.NU_ANN_REF AS NU_ANN_REF2, ");
            sql.append(" R.NU_EMI_REF AS NU_EMI_REF2, ");
            sql.append(" R.NU_DES_REF AS NU_DES_REF2 ");
            sql.append(" FROM IDOSGD.TDTR_REFERENCIA R ");
            sql.append(" LEFT JOIN IDOSGD.TDTV_REMITOS RE ");
            sql.append(" ON R.NU_ANN = RE.NU_ANN ");
            sql.append(" AND R.NU_EMI = RE.NU_EMI ");
            sql.append(" LEFT JOIN IDOSGD.TDTX_REMITOS_RESUMEN RS ");
            sql.append(" ON RE.NU_ANN = RS.NU_ANN ");
            sql.append(" AND RE.NU_EMI = RS.NU_EMI ");
            /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
            //sql.append(" WHERE RE.ES_DOC_EMI NOT IN ('5', '9', '7')) W ");
            sql.append(" WHERE RE.ES_DOC_EMI NOT IN ('5', '9', '7', 'A', 'B')) W ");
            /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
            sql.append(" ON B.NU_ANN = W.NU_ANN_REF2 ");
            sql.append(" AND B.NU_EMI = W.NU_EMI_REF2 ");
            sql.append(" AND B.NU_DES = W.NU_DES_REF2 ");    
        }
                                        
        sql.append(" WHERE B.CO_DEP_DES = :pCoDepDes");
        
        String pNUAnn = buscarDocumentoRecep.getsCoAnnio();
        if (!(buscarDocumentoRecep.getEsFiltroFecha().equals("1") && pNUAnn.equals("0"))) {
            sql.append(" AND A.NU_ANN = :pNuAnn");
            objectParam.put("pNuAnn", pNUAnn);
        }        
        sql.append(" AND A.ES_ELI = '0'");
        sql.append(" AND A.CO_GRU IN ('1','2','3')");
        sql.append(" AND B.ES_ELI = '0'");
        /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
        //sql.append(" AND A.ES_DOC_EMI NOT IN ('5', '9', '7')");
        sql.append(" AND A.ES_DOC_EMI NOT IN ('5', '9', '7', 'A', 'B')");
        /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
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
            
            //jaznaero
            if (buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                //selecciono todo
            }
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                //selecciona nada    
            }
            if (buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                //oficina    
                sql.append(" AND B.CO_EMP_DES  in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina  )");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
            } 
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                //otros    
                sql.append(" AND not B.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina ) "); 
                    sql.append(" AND not B.CO_EMP_DES in ( :pcoEmpDesAux ) ");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
                    objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
            }                        
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                //personal    
                sql.append(" AND not B.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina ) "); 
                    sql.append(" AND B.CO_EMP_DES in ( :pcoEmpDesAux ) ");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
                    objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
            }  
            if (buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && !buscarDocumentoRecep.isEsIncluyeProfesional()){
                
                sql.append(" AND (B.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina) or  B.CO_EMP_DES = :pcoEmpDesAux) ");
                objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
                objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
                
            }            
            if (!buscarDocumentoRecep.isEsIncluyeOficina() && buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                    sql.append(" AND not B.CO_EMP_DES  in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pcoEmpDesOficina  )");
                    objectParam.put("pcoEmpDesOficina", buscarDocumentoRecep.getsCoDependencia());
            }
            if (buscarDocumentoRecep.isEsIncluyeOficina() && !buscarDocumentoRecep.isEsIncluyePersonal() && buscarDocumentoRecep.isEsIncluyeProfesional()){
                    sql.append(" AND not B.CO_EMP_DES in ( :pcoEmpDesAux ) ");
                    objectParam.put("pcoEmpDesAux", buscarDocumentoRecep.getsCoEmpleado());
            }
            //jazanero
            
            if (buscarDocumentoRecep.getCoTema()!= null && buscarDocumentoRecep.getCoTema().trim().length() > 0) {
                sql.append(" AND B.CO_TEMA = :pCoTema ");
                objectParam.put("pCoTema", buscarDocumentoRecep.getCoTema());
            }
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
                sql.append(" AND nvl(C.CO_DEP_EMI_REF,'0') = :pTiEmiRef ");
                objectParam.put("pTiEmiRef", buscarDocumentoRecep.getsRemitente());
            }
            if (buscarDocumentoRecep.getIdEtiqueta() != null && buscarDocumentoRecep.getIdEtiqueta().trim().length() > 0) {
                sql.append(" AND B.CO_ETIQUETA_REC = :pcoEtiquetaRec ");
                objectParam.put("pcoEtiquetaRec", buscarDocumentoRecep.getIdEtiqueta());
            }
            
            if(buscarDocumentoRecep.getBusResultado().equals("1"))
            {
                if(buscarDocumentoRecep.getCoTipoPersona().equals("03")){
                    sql.append(" AND A.NU_DNI_EMI = :pNumDni");
                    objectParam.put("pNumDni", buscarDocumentoRecep.getBusNumDni());
                    }
                    else if(buscarDocumentoRecep.getCoTipoPersona().equals("02")){
                        sql.append(" AND A.NU_RUC_EMI = :pNumRuc");
                        objectParam.put("pNumRuc", buscarDocumentoRecep.getBusNumRuc());
                    }
                    else if(buscarDocumentoRecep.getCoTipoPersona().equals("04")){
                        sql.append(" AND A.CO_OTR_ORI_EMI = :pCoOtr");
                        objectParam.put("pCoOtr", buscarDocumentoRecep.getBusCoOtros());
                }
            }    
            if (buscarDocumentoRecep.getEsFiltroFecha() != null
                    && (buscarDocumentoRecep.getEsFiltroFecha().equals("1") || buscarDocumentoRecep.getEsFiltroFecha().equals("3"))) {
                String vFeEmiIni = buscarDocumentoRecep.getsFeEmiIni();
                String vFeEmiFin = buscarDocumentoRecep.getsFeEmiFin();
                if (vFeEmiIni != null && vFeEmiIni.trim().length() > 0
                        && vFeEmiFin != null && vFeEmiFin.trim().length() > 0) {
                    sql.append(" AND A.FE_EMI between TO_DATE(:pFeEmiIni,'dd/mm/yyyy') AND TO_DATE(:pFeEmiFin,'dd/mm/yyyy') + 0.99999");
                    objectParam.put("pFeEmiIni", vFeEmiIni);
                    objectParam.put("pFeEmiFin", vFeEmiFin);
                }
            }
        }

        if (pTipoBusqueda.equals("1")) {
            if (buscarDocumentoRecep.getsNroDocumento() != null && buscarDocumentoRecep.getsNroDocumento().trim().length() > 1) {
                sql.append(" AND A.NU_DOC_EMI LIKE ''||:pnuDocEmi||'%' ");
                objectParam.put("pnuDocEmi", buscarDocumentoRecep.getsNroDocumento());
            }

            if (buscarDocumentoRecep.getsBuscNroExpediente() != null && buscarDocumentoRecep.getsBuscNroExpediente().trim().length() > 1) {
                sql.append(" AND C.NU_EXPEDIENTE LIKE '%'||:pnuExpediente||'%' ");
                objectParam.put("pnuExpediente", buscarDocumentoRecep.getsBuscNroExpediente());
            }

            // Busqueda del Asunto
            if (buscarDocumentoRecep.getsBuscAsunto() != null && buscarDocumentoRecep.getsBuscAsunto().trim().length() > 1) {
                sql.append(" AND UPPER(A.DE_ASU) LIKE '%'||:pDeAsunto||'%' ");
                objectParam.put("pDeAsunto", buscarDocumentoRecep.getsBuscAsunto());
            }
        }
        if(estadoDoc1.equals("1")){
            sql.append(" AND W.NU_ANN2 IS NULL ");
        }
        sql.append(" ORDER BY A.FE_EMI").append(sOrdenList);
        sql.append(") X ");

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
    public List<DocumentoBean> getDocumentosReferenciadoBusqSize(DocumentoBean documentoBean, String sTipoAcceso) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List<DocumentoBean> list = new ArrayList<DocumentoBean>();
        sql.append("SELECT A.*, ROWNUM");
        sql.append(" FROM ( ");
        sql.append("SELECT DISTINCT A.NU_ANN,A.NU_EMI,A.TI_CAP,B.NU_COR_DES,TO_CHAR(A.FE_EMI,'DD/MM/YY') FE_EMI_CORTA,A.FE_EMI,");
        sql.append(" PK_SGD_DESCRIPCION.TI_EMI_EMP (A.NU_ANN, A.NU_EMI) DE_ORI_EMI,");
        sql.append(" PK_SGD_DESCRIPCION.DE_DOCUMENTO (A.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM,C.NU_DOC,");
        sql.append(" DECODE (B.TI_DES,");
        sql.append("				  '01', PK_SGD_DESCRIPCION.DE_NOM_EMP (B.CO_EMP_DES),");
        sql.append("				  '02', PK_SGD_DESCRIPCION.DE_NOM_EMP (B.NU_RUC_DES),");
        sql.append("				  '03', PK_SGD_DESCRIPCION.ANI_SIMIL (B.NU_DNI_DES),");
        sql.append("				  '04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (B.CO_OTR_ORI_DES)");
        sql.append("				 ) DE_EMP_DES,");
        sql.append(" UPPER (A.DE_ASU) DE_ASU_M,C.NU_EXPEDIENTE,");
        sql.append(" PK_SGD_DESCRIPCION.ESTADOS (B.ES_DOC_REC,'TDTV_DESTINOS') DE_ES_DOC_DES,");
        sql.append(" C.IN_EXISTE_DOC EXISTE_DOC,DECODE(NVL(C.TI_EMI_REF,'0')||NVL(C.IN_EXISTE_ANEXO,'2'),'00',0,'02',0,1) EXISTE_ANEXO,B.NU_DES,NVL(B.CO_PRI,'1') CO_PRI,B.ES_DOC_REC");
        sql.append(" FROM ( ");
        sql.append("   SELECT NU_ANN, NU_EMI, NU_DES ");
        sql.append("	 FROM TDTR_ARBOL_SEG ");
        sql.append("		  START WITH PK_REF = :pCoAnio||:pNuEmi||'0' ");
        sql.append("		 CONNECT BY PRIOR pk_EMI = PK_REF  ");
        sql.append(" ) D, TDTV_REMITOS A, TDTV_DESTINOS B,TDTX_REMITOS_RESUMEN C");
        sql.append(" WHERE");
        sql.append(" D.NU_ANN = A.NU_ANN");
        sql.append(" AND D.NU_EMI = A.NU_EMI");
        sql.append(" AND B.NU_ANN = A.NU_ANN");
        sql.append(" AND B.NU_EMI = A.NU_EMI");
        sql.append(" AND C.NU_ANN = B.NU_ANN");
        sql.append(" AND C.NU_EMI = B.NU_EMI");
        sql.append(" AND B.CO_DEP_DES = :pCoDepDes");
        if (sTipoAcceso.equals("1")) {
            sql.append(" AND B.CO_EMP_DES = :pcoEmpDes");
            objectParam.put("pcoEmpDes", documentoBean.getCoEmpDes());
        }
        sql.append(" AND A.ES_ELI = '0'");
        sql.append(" AND B.ES_ELI = '0'");
        sql.append(" AND A.ES_DOC_EMI NOT IN ('5', '9', '7')");
        sql.append(" AND A.IN_OFICIO = '0'");

        objectParam.put("pCoAnio", documentoBean.getNuAnn());
        objectParam.put("pNuEmi", documentoBean.getNuEmi());
        objectParam.put("pCoDepDes", documentoBean.getCoDepDes());

        sql.append(" ORDER BY A.FE_EMI DESC");
        sql.append(") A ");

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
    /* HPB 20/02/2020 - Fin - Requerimiento Paginación recepcionados*/
    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    @Override
    public String actualizarEstadoPlazoAtencion(DocumentoBean documentoBean, String inButtonRed) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TDTV_REMITOS ");
        sql.append("SET fe_ate_doc_emi = SYSDATE,");
        sql.append("CO_USE_MOD=?,FE_USE_MOD=SYSDATE, ");/*[HPB-21/06/21] Campos Auditoria-*/
        if (inButtonRed.equals("1")) {
            sql.append(" ES_DOC_EMI = '3',");
        }        
        sql.append(" IN_RES_PLA_ATE = ? ");
        sql.append(" WHERE NU_ANN = ? ");
        sql.append("AND NU_EMI = ? ");
        String vReturn = "NO_OK";
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{documentoBean.getCodUseMod(), documentoBean.getInResPlaAte(),
                documentoBean.getNuAnnDocOriPlazoAtencion(), documentoBean.getNuEmiDocOriPlazoAtencion()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public String actualizarEstadoPlazoAtencionSinDoc(DocumentoBean documentoBean) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TDTV_REMITOS ");
        sql.append("SET fe_ate_doc_emi = SYSDATE,");
        sql.append("CO_USE_MOD=?,FE_USE_MOD=SYSDATE, ");/*[HPB-21/06/21] Campos Auditoria-*/
        sql.append(" IN_RES_PLA_ATE = ? ");
        sql.append(" WHERE NU_ANN = ? ");
        sql.append("AND NU_EMI = ? ");
        String vReturn = "NO_OK";
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{documentoBean.getCodUseMod(), documentoBean.getInPlaAte(),
                documentoBean.getNuAnn(), documentoBean.getNuEmi()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }
    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
}
