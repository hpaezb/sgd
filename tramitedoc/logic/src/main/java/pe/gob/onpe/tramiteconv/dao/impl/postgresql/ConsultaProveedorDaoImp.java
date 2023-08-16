/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.postgresql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.ProveedorBean;
import pe.gob.onpe.tramitedoc.dao.ConsultaProveedorDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoQuery;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;

/**
 *
 * @author ECueva
 */
@Repository("consultaProveedorDao")
public class ConsultaProveedorDaoImp extends SimpleJdbcDaoQuery implements ConsultaProveedorDao{
    
    @Autowired
    private ApplicationProperties applicationProperties;        
    
    @Override
   public List<ProveedorBean> getProveedoresBusqList(String busNroRuc, String busRazonSocial) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        
        sql.append("SELECT cpro_ruc nuRuc, to_char(dpro_fecins,'dd/mm/yyyy'), cpro_razsoc descripcion, cpro_domicil, cubi_coddep,\n" +
                    "          cubi_codpro, cubi_coddis,\n" +
                    "          IDOSGD.PK_SGD_DESCRIPCION_fu_get_departamento (cubi_coddep) no_dep,\n" +
                    "          IDOSGD.PK_SGD_DESCRIPCION_fu_get_provincia (cubi_coddep, cubi_codpro) no_prv,\n" +
                    "          IDOSGD.PK_SGD_DESCRIPCION_fu_get_distrito (cubi_coddep,\n" +
                    "                                         cubi_codpro,\n" +
                    "                                         cubi_coddis\n" +
                    "                                        ) no_dis,\n" +
                    "          cpro_telefo\n" +
                    "    FROM IDOSGD.lg_pro_proveedor WHERE LENGTH (cpro_ruc) = 11 ");
        
        if (busNroRuc!=null&&busNroRuc.trim().length()>0){
            sql.append(" and cpro_ruc like '%'||:pnroRuc||'%' ");
            objectParam.put("pnroRuc", busNroRuc);
            
        }       
        if( busRazonSocial!=null && !busRazonSocial.equals("")) {
            /*sql.append(" and CONTAINS(cpro_razsoc, '"+BusquedaTextual.getContextValue(busRazonSocial)+"'\n" +
                        ", 1 ) > 1\n" +
                        "order by score(1) desc");*/
            sql.append(" and cpro_razsoc like '%'||:prazSoc||'%' ");
            objectParam.put("prazSoc", busRazonSocial); 
        }
        sql.append(" LIMIT ").append(applicationProperties.getTopRegistrosConsultas());
        
        List<ProveedorBean> list = new ArrayList<ProveedorBean>();
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(ProveedorBean.class));
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
}
