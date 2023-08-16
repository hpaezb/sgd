/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.oracle;

//import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.DocumentoDatoBean;
//import pe.gob.onpe.tramitedoc.bean.NotificacionBean;
import pe.gob.onpe.tramitedoc.dao.NotificacionDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;

/**
 *
 * @author ecueva
 */
@Repository("notificacionDao")
public class NotificacionDaoImp extends SimpleJdbcDaoBase implements NotificacionDao{

    /*@Override
    public String insNotificacion(NotificacionBean noti) {
        String vReturn = "NO_OK";
        StringBuilder sqlIns = new StringBuilder();
        sqlIns.append("INSERT INTO TDTV_NOTIFICACIONES(\n" +
                        "NU_ANN,\n" +
                        "NU_EMI,\n" +
                        "CO_EMP,\n" +
                        "CO_DEP,\n" +
                        "TIPO,\n" +
                        "CO_USER_CRE,\n" +
                        "CO_USER_MOD)\n" +
                        "VALUES(?,?,?,?,?,?,?)");

        try {
            this.jdbcTemplate.update(sqlIns.toString(), new Object[]{noti.getNuAnn(), noti.getNuEmi(), noti.getCoEmp(), noti.getCoDep(), noti.getTipo(),
                noti.getCoUser(), noti.getCoUser()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public String delNotificacion(NotificacionBean noti) {
        String vReturn = "NO_OK";
        StringBuilder sqlDel = new StringBuilder();
        sqlDel.append("DELETE FROM TDTV_NOTIFICACIONES\n" +
                "WHERE \n" +
                "NU_ANN=?\n" +
                "AND NU_EMI=?\n" +
                "AND CO_EMP=?\n" +
                "AND CO_DEP=?");
        try {
            this.jdbcTemplate.update(sqlDel.toString(), new Object[]{noti.getNuAnn(), noti.getNuEmi(), noti.getCoEmp(), noti.getCoDep()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public String updNotificacion(NotificacionBean noti) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("UPDATE TDTV_NOTIFICACIONES \n"
                + "SET EST_ENV = ?, \n"
                + "FE_ENV = SYSDATE,\n"
                + "EMAIL_ENV = ?,\n"
                + "CO_USE_MOD = ?,\n"
                + "FE_USE_MOD = SYSDATE\n"
                + "WHERE NU_ANN = ? AND NU_EMI = ? \n"
                + "AND CO_EMP = ? AND CO_DEP = ? AND ESTADO='1'");
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{noti.getEstadoEnvio(), noti.getToEmail(), noti.getCoUser(), noti.getNuAnn(), noti.getNuEmi(),
                noti.getCoEmp(), noti.getCoDep()});
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;    
    }
    
    @Override
    public List<NotificacionBean> getLsNotiPendienteEnvio(String pnuAnn, String pnuEmi){
        StringBuilder sql = new StringBuilder();
        List<NotificacionBean> list = null;
        sql.append("SELECT A.NU_ANN, A.NU_EMI, A.CO_EMP, A.CO_DEP, B.CEMP_EMAIL TO_EMAIL, B.CEMP_APEPAT ||' '|| B.CEMP_APEMAT ||' '|| B.CEMP_DENOM DE_NOM\n" +
                    "FROM TDTV_NOTIFICACIONES A, RHTM_PER_EMPLEADOS B\n" +
                    "WHERE \n" +
                    "A.NU_ANN=? AND A.NU_EMI=?\n" +
                    "AND B.CEMP_CODEMP=A.CO_EMP\n" +
                    "AND A.EST_ENV='0' AND A.ESTADO='1'");

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(NotificacionBean.class),
                    new Object[]{pnuAnn, pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }*/
    
    @Override
    public DocumentoDatoBean getDatosDoc(String nuAnn, String nuEmi){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  A.NU_EMI, ");
        sql.append("        A.NU_ANN, ");
        sql.append("        TO_CHAR(A.FE_EMI, 'DD/MM/YYYY HH24:MI') FECHA_DOC, ");
        sql.append("        B.NU_DOC SIGLAS_DOC, ");
        sql.append("        A.DE_ASU, ");
        sql.append("        A.ES_DOC_EMI, ");
        sql.append("        PK_SGD_DESCRIPCION.DE_DOCUMENTO(A.CO_TIP_DOC_ADM) TIPO_DOC, ");
        sql.append("        PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_EMI) DE_DEP_EMI ");
        sql.append("FROM TDTV_REMITOS A, ");
        sql.append("     TDTX_REMITOS_RESUMEN B ");
        sql.append("WHERE A.NU_ANN=? ");
        sql.append("AND A.NU_EMI=? ");
        sql.append("AND B.NU_ANN=? ");
        sql.append("AND B.NU_EMI=? ");
   
        DocumentoDatoBean docDatoBean = new DocumentoDatoBean();

        try {
            docDatoBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoDatoBean.class),new Object[]{nuAnn, nuEmi, nuAnn, nuEmi});
        } catch (EmptyResultDataAccessException e) {
            docDatoBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docDatoBean;        
    }    
}
