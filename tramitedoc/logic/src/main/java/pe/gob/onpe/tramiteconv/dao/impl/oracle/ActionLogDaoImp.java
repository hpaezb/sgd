package pe.gob.onpe.tramiteconv.dao.impl.oracle;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.Formatter;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.tramitedoc.bean.AccionLog;
import pe.gob.onpe.tramitedoc.bean.BuscarAccionLog;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;
import pe.gob.onpe.tramitedoc.dao.ActionLogDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
/**
 *
 * @author hpaez
 */
@Repository("actionLogDao")
public class ActionLogDaoImp extends SimpleJdbcDaoBase implements ActionLogDao{

    private SimpleJdbcCall spInsActionLog;
    private static Logger logger=Logger.getLogger(ActionLogDaoImp.class);
    
    @Override
    public String insActionLog(AccionLog accionLog, Usuario usuario) throws Exception {
        String vReturn = "NO_OK";
        logger.info("P_ID_ACTION: "+ accionLog.getnIdAction());
        logger.info("P_NU_ANN: "+ accionLog.getcNuAnn());
        logger.info("P_NU_EMI: "+ accionLog.getvNuEmi());
        logger.info("P_CO_USE_AC: "+ usuario.getCoUsuario());
        logger.info("P_DEPENDENCIA: "+ usuario.getCoDep());
        logger.info("P_OPCION: "+ accionLog.getcOpcion());
        logger.info("P_ACCION: "+ accionLog.getcAccion());
        logger.info("P_NU_DES: "+ accionLog.getcNuDes());
        this.spInsActionLog = new SimpleJdbcCall(this.dataSource).withCatalogName("SGD_PKG_REGISTRAR_ACTION").withProcedureName("SGD_SP_INSERTAR_ACTION")
        .withoutProcedureColumnMetaDataAccess()
        .useInParameterNames("P_ID_ACTION", "P_NU_ANN", "P_NU_EMI", "P_CO_USE_AC", "P_DEPENDENCIA", "P_OPCION", "P_ACCION", "P_NU_DES")
        .declareParameters(
                new SqlParameter("P_ID_ACTION", Types.VARCHAR),
                new SqlParameter("P_NU_ANN", Types.VARCHAR),
                new SqlParameter("P_NU_EMI", Types.VARCHAR),
                new SqlParameter("P_CO_USE_AC", Types.VARCHAR) ,
                new SqlParameter("P_DEPENDENCIA", Types.VARCHAR),
                new SqlParameter("P_OPCION", Types.VARCHAR),
                new SqlParameter("P_ACCION", Types.VARCHAR),
                new SqlParameter("P_NU_DES", Types.VARCHAR)
                        );
        SqlParameterSource in = new MapSqlParameterSource()
        .addValue("P_ID_ACTION", accionLog.getnIdAction())
        .addValue("P_NU_ANN", accionLog.getcNuAnn())
        .addValue("P_NU_EMI", accionLog.getvNuEmi())
        .addValue("P_CO_USE_AC", usuario.getCoUsuario())               
        .addValue("P_DEPENDENCIA", usuario.getCoDep()) 
        .addValue("P_OPCION", accionLog.getcOpcion())               
        .addValue("P_ACCION", accionLog.getcAccion())
        .addValue("P_NU_DES", accionLog.getcNuDes()); 
        try {
            this.spInsActionLog.execute(in);
            vReturn = "OK";
        } catch (Exception ex) {
            ex.printStackTrace();
        }                  
        return vReturn;        
    }    

    @Override
    public String getNextAction() {
        String vReturn = "1";
        String sql = "SELECT coalesce(MAX(N_ID_ACTION),0) + 1 N_ID_ACTION FROM IDOSGD.SGD_TBLA_ACCION";        
        try {
            vReturn = this.jdbcTemplate.queryForObject(sql, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }

    @Override
    public AccionLog getIndicadorMenu(String coOpcion) {
        StringBuilder sql = new StringBuilder();
        AccionLog accionLog = null;
        sql.append("SELECT COD_OPC C_OPCION FROM SEG_OPCIONES WHERE COD_OPC=? AND IND_ACTION='1'");        
        
        try {
             accionLog = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(AccionLog.class),
                    new Object[]{coOpcion});
        } catch (EmptyResultDataAccessException e) {
            accionLog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accionLog; 
    }

    @Override
    public SiElementoBean getConfigLog(String coOpcion, String coEstadoDoc) throws Exception {
        StringBuilder sql = new StringBuilder();
        SiElementoBean siElementoBean = null;
        sql.append( "SELECT CELE_DESELE, CELE_DESCOR, CELE_CODELE2, ESTADO\n" +
                    "  FROM SI_ELEMENTO\n" +
                    " WHERE CTAB_CODTAB = 'LOG_SEGUIMIENTO'\n" +
                    "   AND CELE_DESELE = ?\n" +
                    "   AND CELE_CODELE2 = ?\n" +
                    "   AND ESTADO = '1'");        
        
        try {
             siElementoBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(SiElementoBean.class),
                    new Object[]{coOpcion, coEstadoDoc});
        } catch (EmptyResultDataAccessException e) {
            siElementoBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return siElementoBean; 
    }

    @Override
    public List<AccionLog> getLstDocumentosLog(BuscarAccionLog buscarAccionLog) throws Exception {
        StringBuilder sql = new StringBuilder();      
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List<AccionLog> list = new ArrayList<AccionLog>();
        String nuDocumento = buscarAccionLog.getNuDocumentoAccion().trim().toUpperCase();
        sql.append("SELECT ROWNUM Item, A.C_NU_ANN cNuAnn, A.V_NU_EMI vNuEmi, PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append("B.NU_DOC_EMI || '-' || B.NU_ANN || '-' || B.DE_DOC_SIG Documento, "); 
        sql.append("B.DE_ASU Asunto, PK_SGD_DESCRIPCION.TI_EMI_EMP(A.C_NU_ANN, A.V_NU_EMI) DE_ORI_EMI, ");
        sql.append("DECODE(C.TI_DES, ");
        sql.append("'01', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(C.CO_DEP_DES) || '-' || PK_SGD_DESCRIPCION.DE_NOM_EMP(C.CO_EMP_DES), ");
        sql.append("'02', PK_SGD_DESCRIPCION.DE_PROVEEDOR(C.NU_RUC_DES), ");
        sql.append("'03', IDOSGD.PK_SGD_DESCRIPCION.ANI_SIMIL(C.NU_DNI_DES), ");
        sql.append("'04', IDOSGD.PK_SGD_DESCRIPCION.OTRO_ORIGEN(C.CO_OTR_ORI_DES)) DE_EMP_DES, ");
        sql.append("(SELECT CDES_USER FROM SEG_USUARIOS1 WHERE COD_USER = V_CO_USE_AC) Usuario, ");
        sql.append("TO_CHAR(A.D_FEC_AC, 'DD/MM/YYYY  hh24:mi:ss') Fecha, ");
        sql.append("(SELECT O.DES_OPC FROM SEG_OPCIONES O WHERE O.COD_OPC = A.C_OPCION) Opcion, ");
        sql.append("(SELECT R.DE_DEPENDENCIA FROM IDOSGD.RHTM_DEPENDENCIA R WHERE R.CO_DEPENDENCIA = A.V_DEPENDENCIA) Dependencia, C.Es_Doc_Rec coEstado ");
        sql.append("FROM SGD_TBLA_ACCION A INNER JOIN TDTV_REMITOS B ON A.C_NU_ANN = B.NU_ANN AND A.V_NU_EMI = B.NU_EMI ");
        sql.append("INNER JOIN TDTV_DESTINOS C ON A.C_NU_ANN = C.NU_ANN AND A.V_NU_EMI = C.NU_EMI AND A.C_NU_DES = C.NU_DES ");
        sql.append("WHERE A.C_ESTADO = '1' ");

        if (buscarAccionLog.getTipoDocumentoAccion()!= null && buscarAccionLog.getTipoDocumentoAccion().trim().length() > 0) {
            sql.append(" AND B.CO_TIP_DOC_ADM = :pCoDocEmi ");
            objectParam.put("pCoDocEmi", buscarAccionLog.getTipoDocumentoAccion());
        } 
            
        if (buscarAccionLog.getNuDocumentoAccion().trim()!= null && buscarAccionLog.getNuDocumentoAccion().trim().length() > 0) {
            objectParam.put("pnuDocAccion1", buscarAccionLog.getNuDocumentoAccion().trim());                     
            sql.append(" AND B.NU_DOC_EMI || '-' || B.NU_ANN || '-' || B.DE_DOC_SIG ");            
            sql.append("LIKE REPLACE('%"+nuDocumento+"%',' ','%') ");
        }
        objectParam.put("pCoDepDes", buscarAccionLog.getCoDependencia());        
        sql.append(" ORDER BY A.D_FEC_AC DESC");
        logger.info("SQL ACCIONES: "+sql);
        
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(AccionLog.class));

        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;  
    }

    @Override
    public List<AccionLog> getListaReporteBusqueda(BuscarAccionLog buscarAccionLog) {
        StringBuilder sql = new StringBuilder();      
        Map<String, Object> objectParam = new HashMap<String, Object>();
        List<AccionLog> list = new ArrayList<AccionLog>();
        String nuDocumento = buscarAccionLog.getNuDocumentoAccion().trim().toUpperCase();      
        sql.append("SELECT ROWNUM Item, A.C_NU_ANN cNuAnn, A.V_NU_EMI vNuEmi, PK_SGD_DESCRIPCION.DE_DOCUMENTO(B.CO_TIP_DOC_ADM) DE_TIP_DOC_ADM, ");
        sql.append("B.NU_DOC_EMI || '-' || B.NU_ANN || '-' || B.DE_DOC_SIG Documento, "); 
        sql.append("B.DE_ASU Asunto, PK_SGD_DESCRIPCION.TI_EMI_EMP(A.C_NU_ANN, A.V_NU_EMI) DE_ORI_EMI, ");
        sql.append("DECODE(C.TI_DES, ");
        sql.append("'01', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(C.CO_DEP_DES) || '-' || PK_SGD_DESCRIPCION.DE_NOM_EMP(C.CO_EMP_DES), ");
        sql.append("'02', PK_SGD_DESCRIPCION.DE_PROVEEDOR(C.NU_RUC_DES), ");
        sql.append("'03', IDOSGD.PK_SGD_DESCRIPCION.ANI_SIMIL(C.NU_DNI_DES), ");
        sql.append("'04', IDOSGD.PK_SGD_DESCRIPCION.OTRO_ORIGEN(C.CO_OTR_ORI_DES)) DE_EMP_DES, ");
        sql.append("(SELECT CDES_USER FROM SEG_USUARIOS1 WHERE COD_USER = V_CO_USE_AC) Usuario, ");
        sql.append("TO_CHAR(A.D_FEC_AC, 'DD/MM/YYYY  hh24:mi:ss') Fecha, ");
        sql.append("(SELECT O.DES_OPC FROM SEG_OPCIONES O WHERE O.COD_OPC = A.C_OPCION) Opcion, ");
        sql.append("(SELECT R.DE_DEPENDENCIA FROM IDOSGD.RHTM_DEPENDENCIA R WHERE R.CO_DEPENDENCIA = A.V_DEPENDENCIA) Dependencia, C.Es_Doc_Rec coEstado ");
        sql.append("FROM SGD_TBLA_ACCION A INNER JOIN TDTV_REMITOS B ON A.C_NU_ANN = B.NU_ANN AND A.V_NU_EMI = B.NU_EMI ");
        sql.append("INNER JOIN TDTV_DESTINOS C ON A.C_NU_ANN = C.NU_ANN AND A.V_NU_EMI = C.NU_EMI AND A.C_NU_DES = C.NU_DES ");
        sql.append("WHERE A.C_ESTADO = '1' ");

        if (buscarAccionLog.getTipoDocumentoAccion()!= null && buscarAccionLog.getTipoDocumentoAccion().trim().length() > 0) {
            sql.append(" AND B.CO_TIP_DOC_ADM = :pCoDocEmi ");
            objectParam.put("pCoDocEmi", buscarAccionLog.getTipoDocumentoAccion());
        } 
            
        if (buscarAccionLog.getNuDocumentoAccion().trim()!= null && buscarAccionLog.getNuDocumentoAccion().trim().length() > 0) {
            objectParam.put("pnuDocAccion1", buscarAccionLog.getNuDocumentoAccion().trim());                     
            sql.append(" AND B.NU_DOC_EMI || '-' || B.NU_ANN || '-' || B.DE_DOC_SIG ");            
            sql.append("LIKE REPLACE('%"+nuDocumento+"%',' ','%') ");
        }
        objectParam.put("pCoDepDes", buscarAccionLog.getCoDependencia());        
        sql.append(" ORDER BY A.D_FEC_AC DESC");
        logger.info("SQL: "+sql);
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(AccionLog.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;  
    }

    @Override
    public int getCantidadDocEncontrados(String tipoDoc, String numeroDoc) {
        String sql= "SELECT count(*)  cant_docs FROM (\n" +
                    "  SELECT distinct    A.V_NU_EMI vNuEmi\n" +
                    "  FROM SGD_TBLA_ACCION A\n" +
                    "         INNER JOIN TDTV_REMITOS B\n" +
                    "            ON A.C_NU_ANN = B.NU_ANN\n" +
                    "           AND A.V_NU_EMI = B.NU_EMI\n" +
                    "         INNER JOIN TDTV_DESTINOS C\n" +
                    "            ON A.C_NU_ANN = C.NU_ANN\n" +
                    "           AND A.V_NU_EMI = C.NU_EMI\n" +
                    "           AND A.C_NU_DES = C.NU_DES\n" +
                    "         WHERE A.C_ESTADO = '1'\n" +
                    "           AND B.CO_TIP_DOC_ADM = ?\n" +
                    "           AND (CASE\n" +
                    "                 WHEN B.NU_DOC_EMI IS NULL THEN\n" +
                    "                  (SELECT B.DE_DOC_SIG\n" +
                    "                     FROM TDTV_REMITOS B\n" +
                    "                    WHERE B.NU_EMI = A.V_NU_EMI)\n" +
                    "                 ELSE\n" +
                    "                  (SELECT B.NU_DOC_EMI || '-' || B.NU_ANN || '-' ||\n" +
                    "                          B.DE_DOC_SIG\n" +
                    "                     FROM TDTV_REMITOS B\n" +
                    "                    WHERE B.NU_EMI = A.V_NU_EMI)\n" +
                    "               END) LIKE REPLACE('%'||?||'%', ' ', '%')--Ingresar numero de documento\n" +
                    "\n" +
                    "         ) X";
        int cant=1;
        try {
            cant=this.jdbcTemplate.queryForObject(sql, new Object[]{tipoDoc, numeroDoc.trim().toUpperCase()}, Integer.class);
        } catch (Exception e) {
            cant=0;
        }
        logger.info("Cantidad -> "+cant+" Query Cantidad Log Doc -> "+sql);
        return cant;
    }
}
