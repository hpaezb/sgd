/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.postgresql;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.OtroOrigenBean;
import pe.gob.onpe.tramitedoc.dao.ConsultaOtroOrigenDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoQuery;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.BusquedaTextual;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author ECueva
 */
@Repository("ConsultaOtroOrigenDao")
public class ConsultaOtroOrigenDaoImp extends SimpleJdbcDaoQuery implements ConsultaOtroOrigenDao{

    @Autowired
    private ApplicationProperties applicationProperties;      
    
//    @Override
//    public List<OtroOrigenBean> getOtroOrigenBusqList(OtroOrigenBean otroOrigen) {
//        StringBuffer sql = new StringBuffer();
//        sql.append("SELECT co_otr_ori, nu_doc_otr_ori, co_tip_otr_ori, de_ape_pat_otr , ");
//        sql.append("de_ape_mat_otr, de_nom_otr, de_raz_soc_otr, de_dir_otro_ori, ");
//        sql.append("ti_origen ,ref_co_otr_ori, td_pk_tramite.fu_get_departamento (ub_dep) no_dep, ");
//        sql.append("td_pk_tramite.fu_get_provincia (ub_dep, ub_pro) no_pro, ");
//        sql.append("td_pk_tramite.fu_get_distrito (ub_dep,ub_pro,ub_dis) no_dis, es_activo FROM tdtr_otro_origen WHERE rownum <= ").append(applicationProperties.getTopRegistrosConsultas());
//        
//        if( otroOrigen.getDeRazSocOtr()!=null&&otroOrigen.getDeRazSocOtr().trim().length()>0) {
//            //Se comenta la version orginal
//            //sql.append(" and CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue(otroOrigen.getDeRazSocOtr()) +"'\n" +
//            //            ", 1 ) > 1\n" +
//            //            "order by score(1) desc");
//            sql.append(" and CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue(Utilidades.fn_getCleanTextLenGreathree(Utilidades.fn_getCleanTextExpReg(otroOrigen.getDeRazSocOtr()))) +"'\n" +
//                        ", 1 ) > 1\n");
//        }
//        sql.append(" ORDER BY de_ape_pat_otr");
//        List<OtroOrigenBean> list = new ArrayList<OtroOrigenBean>();
//        try {
//            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(OtroOrigenBean.class),
//                    new Object[]{});
//        } catch (EmptyResultDataAccessException e) {
//            list = null;
//        } catch (Exception e) {
//            list = null;
//            e.printStackTrace();
//        }
//        return list;
//    }
    
    public List<OtroOrigenBean> getOtroOrigenBusqList(OtroOrigenBean otroOrigen) {
        Object[] objectParam = null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT co_otr_ori, nu_doc_otr_ori, co_tip_otr_ori, de_ape_pat_otr , ");
        sql.append("de_ape_mat_otr, de_nom_otr, de_raz_soc_otr, de_dir_otro_ori, ");
        sql.append("ti_origen ,ref_co_otr_ori, IDOSGD.PK_SGD_DESCRIPCION_fu_get_departamento ( ub_dep) no_dep, ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_provincia ( ub_dep,  ub_pro) no_pro, ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_distrito ( ub_dep, ub_pro, ub_dis) no_dis, es_activo FROM IDOSGD.tdtr_otro_origen WHERE TRUE ");
        
        if( otroOrigen.getDeRazSocOtr()!=null&&otroOrigen.getDeRazSocOtr().trim().length()>0) {
            //Se comenta la version orginal
            //sql.append(" and CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue(otroOrigen.getDeRazSocOtr()) +"'\n" +
            //            ", 1 ) > 1\n" +
            //            "order by score(1) desc");
            /*sql.append(" and CONTAINS(in_busca_texto, '"+BusquedaTextual.getContextValue(otroOrigen.getDeRazSocOtr()) +"'\n" +
                        ", 1 ) > 1\n");*/
            sql.append(" AND UPPER(de_raz_soc_otr) like '%'||?||'%'\n"); 
            objectParam = new Object[]{otroOrigen.getDeRazSocOtr()};
        }
        sql.append(" ORDER BY de_ape_pat_otr");
        sql.append(" LIMIT ").append(applicationProperties.getTopRegistrosConsultas()); 
        List<OtroOrigenBean> list = new ArrayList<OtroOrigenBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(OtroOrigenBean.class),objectParam);            
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
}
