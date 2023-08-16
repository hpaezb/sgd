package pe.gob.onpe.tramiteconv.dao.impl.oracle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiPersConsulBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.dao.ReporteAcervoDocPerDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoQuery;
import pe.gob.onpe.tramitedoc.web.util.BusquedaTextual;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

@Repository("reporteAcervoDocPerDao")
public class ReporteAcervoDocPerDaoImp extends SimpleJdbcDaoQuery implements ReporteAcervoDocPerDao{

    @Override
    public List<DocumentoEmiPersConsulBean> getDocsPersAcervoDocumentario(DocumentoEmiPersConsulBean buscarDocPer) {
        StringBuilder sql = new StringBuilder();
        boolean bBusqFiltro=false;
        Map<String, Object> objectParam = new HashMap<String, Object>();
        
        sql.append("SELECT X.*,");
        sql.append(" PK_SGD_DESCRIPCION.DE_DOCUMENTO(X.CO_TIP_DOC_ADM) TIPO_DOC,");
        sql.append(" DECODE(X.NU_CANDES, 1, PK_SGD_DESCRIPCION.TI_DES_EMP(X.NU_ANN, X.NU_EMI),PK_SGD_DESCRIPCION.TI_DES_EMP_V(X.NU_ANN, X.NU_EMI)) DE_DEP_DESTINO,");        
        sql.append(" PK_SGD_DESCRIPCION.ESTADOS (X.ES_DOC_EMI,'TDTV_REMITOS') ESTADO_DOC,");
        sql.append(" PK_SGD_DESCRIPCION.DE_NOM_EMP(X.CO_EMP_RES) DE_EMP_ELABORO,"); 
        sql.append(" PK_SGD_DESCRIPCION.DE_EMI_REF(X.NU_ANN, X.NU_EMI) DE_DEP_REF,"); 
        sql.append(" PK_SGD_DESCRIPCION.DE_DEPENDENCIA(X.CO_DEP_EMI) DE_DEP_EMI,"); 
        sql.append(" PK_SGD_DESCRIPCION.DE_NOM_EMP(X.CO_EMP_EMI) DE_EMP_FIRMO, ");
        sql.append(" ROWNUM");
        sql.append(" FROM ( ");        
        sql.append(" SELECT R.NU_ANN,R.NU_EMI,R.NU_COR_EMI,TO_CHAR(R.FE_EMI,'DD/MM/YYYY') FE_EMI_CORTA,");
        sql.append(" RR.NU_DOC,R.CO_TIP_DOC_ADM,R.NU_CANDES,R.ES_DOC_EMI,R.CO_EMP_RES,");
        sql.append(" R.CO_EMP_EMI,");//--EMPLEADO QUE EMITE-FIRMA
        sql.append(" R.DE_ASU,RR.NU_EXPEDIENTE,R.CO_DEP_EMI,RR.IN_EXISTE_DOC EXISTE_DOC,RR.IN_EXISTE_ANEXO EXISTE_ANEXO,");
        sql.append(" R.NU_DIA_ATE");
        sql.append(" FROM TDTV_REMITOS R,TDTX_REMITOS_RESUMEN RR");
        sql.append(" WHERE");
        sql.append(" R.NU_ANN=RR.NU_ANN");
        sql.append(" AND R.NU_EMI=RR.NU_EMI");   
        
        String pNUAnn = buscarDocPer.getNuAnn();
        if(!(buscarDocPer.getEsFiltroFecha().equals("1") && pNUAnn.equals("0"))){
            sql.append(" AND R.NU_ANN = :pNuAnn");
            objectParam.put("pNuAnn", pNUAnn);                
        }        

        sql.append(" AND R.TI_EMI='05'");
        //sql.append(" AND R.CO_GRU = '2'");    Se comento para que muestre de todos los grupos    
        sql.append(" AND RR.TI_EMI='05'");
        sql.append(" AND R.ES_ELI = '0'");
      
        String pTipoBusqueda = buscarDocPer.getTipoBusqueda();        

        //sql.append(" AND R.CO_EMP_RES = :pcoEmpRes ");
        //objectParam.put("pcoEmpRes", buscarDocPer.getCoEmpleado());

        if(pTipoBusqueda.equals("1") && buscarDocPer.isEsIncluyeFiltro()){
            bBusqFiltro=true;
        }            
            
        //Filtro
        if(pTipoBusqueda.equals("0") || bBusqFiltro){
            if (buscarDocPer.getCoEmpFirmo()!= null && buscarDocPer.getCoEmpFirmo().trim().length()>0){
               sql.append(" AND R.CO_EMP_EMI = :pcoEmpFirmo ");
               objectParam.put("pcoEmpFirmo", buscarDocPer.getCoEmpFirmo());
            }             
            if (buscarDocPer.getTipoDoc()!= null && buscarDocPer.getTipoDoc().trim().length()>0){
               sql.append(" AND R.CO_TIP_DOC_ADM = :pCoDocEmi ");
               objectParam.put("pCoDocEmi", buscarDocPer.getTipoDoc());
            }
            if (buscarDocPer.getEstadoDoc()!= null && buscarDocPer.getEstadoDoc().trim().length()>0){
               sql.append(" AND R.ES_DOC_EMI = :pEsDocEmi ");
               objectParam.put("pEsDocEmi", buscarDocPer.getEstadoDoc());
            }
            if (buscarDocPer.getCoDepDestino()!= null && buscarDocPer.getCoDepDestino().trim().length()>0){
               sql.append(" AND INSTR(RR.TI_EMI_DES, :pTiEmiDes) > 0 ");
               objectParam.put("pTiEmiDes", buscarDocPer.getCoDepDestino());
            }
            if (buscarDocPer.getCoDepRef()!= null && buscarDocPer.getCoDepRef().trim().length()>0){
               sql.append(" AND INSTR(RR.TI_EMI_REF, :pTiEmiRef) > 0 ");
               objectParam.put("pTiEmiRef", buscarDocPer.getCoDepRef());
            }  
            if (buscarDocPer.getCoDepRemite()!= null && buscarDocPer.getCoDepRemite().trim().length()>0){
               sql.append(" AND R.CO_DEP_EMI = :pTiEmi ");
               objectParam.put("pTiEmi", buscarDocPer.getCoDepRemite());
            }            
            if(buscarDocPer.getEsFiltroFecha()!= null && 
               (buscarDocPer.getEsFiltroFecha().equals("1") || buscarDocPer.getEsFiltroFecha().equals("3"))){
                String vFeEmiIni = buscarDocPer.getFeEmiIni();
                String vFeEmiFin = buscarDocPer.getFeEmiFin();       
                if(vFeEmiIni!= null && vFeEmiIni.trim().length()>0 &&
                   vFeEmiFin!= null && vFeEmiFin.trim().length()>0){
                    sql.append(" AND R.FE_EMI between TO_DATE(:pFeEmiIni,'dd/mm/yyyy') AND TO_DATE(:pFeEmiFin,'dd/mm/yyyy') + 0.99999"); 
                    objectParam.put("pFeEmiIni", vFeEmiIni);
                    objectParam.put("pFeEmiFin", vFeEmiFin);                    
                }
            }

        }    

        //Busqueda
        if (pTipoBusqueda.equals("1"))
        {
            if (buscarDocPer.getNuCorEmi()!= null && buscarDocPer.getNuCorEmi().trim().length()>0){
               sql.append(" AND R.NU_COR_EMI = :pnuCorEmi ");
               objectParam.put("pnuCorEmi", buscarDocPer.getNuCorEmi());
            }

            if (buscarDocPer.getNuDoc()!= null && buscarDocPer.getNuDoc().trim().length()>1){
               sql.append(" AND RR.NU_DOC LIKE '%'||:pnuDocEmi||'%' ");
               objectParam.put("pnuDocEmi", buscarDocPer.getNuDoc());
            }

            // Busqueda del Asunto
            buscarDocPer.setDeAsu(Utilidades.fn_getCleanTextLenGreathree(Utilidades.fn_getCleanTextExpReg(buscarDocPer.getDeAsu())));                        
            if (buscarDocPer.getDeAsu()!= null && buscarDocPer.getDeAsu().trim().length()>1){
                sql.append(" AND UPPER(R.DE_ASU) LIKE '%'||:pDeAsunto||'%' ");
                objectParam.put("pDeAsunto", buscarDocPer.getDeAsu());                
               //sql.append(" AND CONTAINS(in_busca_texto, '").append(BusquedaTextual.getContextValue( buscarDocPer.getDeAsu())).append("', 1 ) > 1 ");
                //sql.append(" AND CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextMixto(buscarDocPer.getDeAsu())+"', 1 ) > 1 ");                                
            }
        }
        sql.append(" ORDER BY R.NU_COR_EMI DESC");
        sql.append(") X ");
        sql.append("WHERE ROWNUM < 201");


        List<DocumentoEmiPersConsulBean> list;
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoEmiPersConsulBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }

    @Override
    public List<EmpleadoBean> getPersonalEditAcervoDoc(String pcoDepEmi) {
        StringBuilder sql = new StringBuilder();
        List<EmpleadoBean> list = new ArrayList<EmpleadoBean>();

        sql.append("SELECT e.cemp_apepat,e.cemp_apemat,e.cemp_denom, e.CEMP_CODEMP\n"
                + "FROM RHTM_PER_EMPLEADOS e\n"
                + "WHERE e.CEMP_EST_EMP = '1'\n"
                + "AND (CEMP_CO_DEPEND in (select CO_DEPENDENCIA from rhTM_dependencia d where d.CO_DEPENDENCIA =? or CO_DEPEN_PADRE=?) \n"
                + "OR CO_DEPENDENCIA=? )\n"
                + "UNION\n"
                + "SELECT e.cemp_apepat,e.cemp_apemat,e.cemp_denom, e.CEMP_CODEMP\n"
                + "FROM RHTM_PER_EMPLEADOS e\n"
                + "WHERE e.CEMP_EST_EMP = '1'\n"
                + "AND cemp_codemp \n"
                + "in (\n"
                + "select co_emp from tdtx_dependencia_empleado  where co_dep=? and es_emp='0'\n"
                + "union select CO_EMPLEADO from rhtm_dependencia  where co_dependencia=?\n"
                + " )\n"
                + "ORDER BY 1");
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
    public List<DocumentoEmiPersConsulBean> getListaReporteAcervoDocPers(DocumentoEmiPersConsulBean buscarDocPer) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List<DocumentoEmiPersConsulBean> list;
        boolean bBusqFiltro=false;
        
        sql.append("SELECT	R.NU_COR_EMI,\n" +
                    "	TO_CHAR(R.FE_EMI,'DD/MM/YYYY') FE_EMI_CORTA,\n" +
                    "	(SELECT CDOC_DESDOC\n" +
                    "	FROM SI_MAE_TIPO_DOC\n" +
                    "	WHERE CDOC_TIPDOC = R.CO_TIP_DOC_ADM) TIPO_DOC,RR.NU_DOC,\n" +
                    "	CASE R.NU_CANDES\n" +
                    "		WHEN 1 THEN PK_SGD_DESCRIPCION.TI_DES_EMP(R.NU_ANN, R.NU_EMI)\n" +
                    "		ELSE PK_SGD_DESCRIPCION.TI_DES_EMP_V(R.NU_ANN, R.NU_EMI)\n" +
                    "	END DE_DEP_DESTINO,\n" +
                    "	PK_SGD_DESCRIPCION.DE_DEPENDENCIA(R.CO_DEP_EMI) DE_DEP_EMI,\n" +
                    "	(SELECT DE_EST\n" +
                    "	FROM TDTR_ESTADOS\n" +
                    "	WHERE CO_EST || DE_TAB = R.ES_DOC_EMI || 'TDTV_REMITOS') ESTADO_DOC,\n" +
                    "	(SELECT CEMP_APEPAT || ' ' || CEMP_APEMAT || ' ' || CEMP_DENOM\n" +
                    "	FROM RHTM_PER_EMPLEADOS\n" +
                    "	WHERE CEMP_CODEMP = R.CO_EMP_RES) DE_EMP_ELABORO,\n" +
                    "	R.DE_ASU,\n" +
                    "	RR.NU_EXPEDIENTE,\n" +
                    "	PK_SGD_DESCRIPCION.DE_EMI_REF(R.NU_ANN, R.NU_EMI) DE_DEP_REF,\n" +
                    "   PK_SGD_DESCRIPCION.DE_NOM_EMP(R.CO_EMP_EMI) DE_EMP_FIRMO,\n" +
                    "	R.NU_DIA_ATE\n" +
                    "FROM TDTV_REMITOS R,\n" +
                    "     TDTX_REMITOS_RESUMEN RR WHERE ");       
        
        try {
            sql.append(" R.NU_ANN=RR.NU_ANN");
            sql.append(" AND R.NU_EMI=RR.NU_EMI");               
            String pNUAnn = buscarDocPer.getNuAnn();
            if(!(buscarDocPer.getEsFiltroFecha().equals("1") && pNUAnn.equals("0"))){
                sql.append(" AND R.NU_ANN = :pNuAnn");
                objectParam.put("pNuAnn", pNUAnn);                
            }             
            sql.append(" AND R.TI_EMI='05'");
            sql.append(" AND RR.TI_EMI='05'");
            sql.append(" AND R.ES_ELI = '0'");             
            //sql.append(" AND R.CO_EMP_RES = :pcoEmpRes ");//Hermes 12/09/2018
            //objectParam.put("pcoEmpRes", buscarDocPer.getCoEmpleado());
        
            String pTipoBusqueda = buscarDocPer.getTipoBusqueda();
            if(pTipoBusqueda.equals("1") && buscarDocPer.isEsIncluyeFiltro()){
                bBusqFiltro=true;
            }
            
            //Filtro
            if (pTipoBusqueda.equals("0") || bBusqFiltro) {
                if (buscarDocPer.getCoEmpFirmo()!= null && buscarDocPer.getCoEmpFirmo().trim().length()>0){
                   sql.append(" AND R.CO_EMP_EMI = :pcoEmpFirmo ");
                   objectParam.put("pcoEmpFirmo", buscarDocPer.getCoEmpFirmo());
                }                  
                if (buscarDocPer.getTipoDoc()!= null && buscarDocPer.getTipoDoc().trim().length()>0){
                    sql.append(" AND R.CO_TIP_DOC_ADM = :pCoDocEmi ");
                    objectParam.put("pCoDocEmi", buscarDocPer.getTipoDoc());
                 }
                 if (buscarDocPer.getEstadoDoc()!= null && buscarDocPer.getEstadoDoc().trim().length()>0){
                    sql.append(" AND R.ES_DOC_EMI = :pEsDocEmi ");
                    objectParam.put("pEsDocEmi", buscarDocPer.getEstadoDoc());
                 }
                 if (buscarDocPer.getCoDepDestino()!= null && buscarDocPer.getCoDepDestino().trim().length()>0){
                    //sql.append(" AND STRPOS(RR.TI_EMI_DES,:pTiEmiDes) > 0 ");
                    sql.append(" AND INSTR(RR.TI_EMI_DES, :pTiEmiDes) > 0 ");
                    objectParam.put("pTiEmiDes", buscarDocPer.getCoDepDestino());
                 }
                 if (buscarDocPer.getCoDepRef()!= null && buscarDocPer.getCoDepRef().trim().length()>0){
                    //sql.append(" AND STRPOS(RR.TI_EMI_REF,:pTiEmiRef) > 0 ");
                    sql.append(" AND INSTR(RR.TI_EMI_REF, :pTiEmiRef) > 0 ");
                    objectParam.put("pTiEmiRef", buscarDocPer.getCoDepRef());
                 }   
                if (buscarDocPer.getCoDepRemite()!= null && buscarDocPer.getCoDepRemite().trim().length()>0){
                   sql.append(" AND R.CO_DEP_EMI = :pTiEmi ");
                   objectParam.put("pTiEmi", buscarDocPer.getCoDepRemite());
                }                   
                 if(buscarDocPer.getEsFiltroFecha()!= null && 
                    (buscarDocPer.getEsFiltroFecha().equals("1") || buscarDocPer.getEsFiltroFecha().equals("3"))){
                     String vFeEmiIni = buscarDocPer.getFeEmiIni();
                     String vFeEmiFin = buscarDocPer.getFeEmiFin();       
                     if(vFeEmiIni!= null && vFeEmiIni.trim().length()>0 && vFeEmiFin!= null && vFeEmiFin.trim().length()>0){                    
                         sql.append(" AND R.FE_EMI between TO_DATE(:pFeEmiIni, 'dd/mm/yyyy') AND TO_DATE(:pFeEmiFin, 'dd/mm/yyyy') + 0.99999 ");
                         objectParam.put("pFeEmiIni", vFeEmiIni);
                         objectParam.put("pFeEmiFin", vFeEmiFin);     
                     }
                 }   
            }
                     
            //Busqueda
            if (pTipoBusqueda.equals("1")) {
                if (buscarDocPer.getNuCorEmi()!= null && buscarDocPer.getNuCorEmi().trim().length()>0){
                   sql.append(" AND R.NU_COR_EMI = :pnuCorEmi ");
                   objectParam.put("pnuCorEmi",Integer.parseInt(buscarDocPer.getNuCorEmi()));
                }

                if (buscarDocPer.getNuDoc()!= null && buscarDocPer.getNuDoc().trim().length()>1){
                   sql.append(" AND RR.NU_DOC LIKE '%'||:pnuDocEmi||'%' ");
                   objectParam.put("pnuDocEmi", buscarDocPer.getNuDoc());
                }
                
               // Busqueda del Asunto
                buscarDocPer.setDeAsu(Utilidades.fn_getCleanTextLenGreathree(Utilidades.fn_getCleanTextExpReg(buscarDocPer.getDeAsu())));                        
                if (buscarDocPer.getDeAsu()!= null && buscarDocPer.getDeAsu().trim().length()>1){                   
                    //sql.append(" AND CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextMixto(buscarDocPer.getDeAsu())+"', 1 ) > 1 ");                                
                    sql.append(" AND UPPER(R.DE_ASU) LIKE '%'||:pDeAsunto||'%' ");
                    objectParam.put("pDeAsunto", buscarDocPer.getDeAsu());                                    
                }
            }
            sql.append(" ORDER BY R.NU_COR_EMI DESC");
            
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(DocumentoEmiPersConsulBean.class));
        } catch (Exception ex) {
            //vResult="1"+ex.getMessage();
            list = null;
            ex.printStackTrace();            
        }
        
        return list;
    }
    
}
