/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.sqlserver;

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
        String sqlContains = "";
        Map<String, Object> objectParam = new HashMap<String, Object>();
        
        sql.append("SELECT A.* FROM ( ");
        sql.append("	select	cpro_ruc nuRuc, ");
        sql.append("		CONVERT(VARCHAR(10), dpro_fecins, 103) dpro_fecins, ");
        sql.append("		cpro_razsoc descripcion, ");
        sql.append("		cpro_domicil, ");
        sql.append("		cubi_coddep, ");
        sql.append("		cubi_codpro, ");
        sql.append("		cubi_coddis, ");
        sql.append("		(SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_FU_GET_DEPARTAMENTO](cubi_coddep)) no_dep, ");
        sql.append("		(SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_FU_GET_PROVINCIA](cubi_coddep, cubi_codpro)) no_prv, ");
        sql.append("		(SELECT RETORNO FROM [IDOSGD].[PK_SGD_DESCRIPCION_FU_GET_DISTRITO](cubi_coddep, cubi_codpro, cubi_coddis)) no_dis, ");
        sql.append("		cpro_telefo, ");
        sql.append("		ROW_NUMBER() OVER (ORDER BY cpro_ruc) AS ROWNUM ");
        sql.append("	from IDOSGD.LG_PRO_PROVEEDOR ");
        sql.append("	WHERE LEN(cpro_ruc) = 11 ");
        
        if (busNroRuc!=null&&busNroRuc.trim().length()>0){
            sql.append(" and cpro_ruc like '%'+:pbusNroRuc+'%' ");
            objectParam.put("pbusNroRuc", busNroRuc);
        }       

        if( busRazonSocial!=null&&busRazonSocial.trim().length()>0) {
            sql.append(" AND CONTAINS(cpro_razsoc, :pBusquedaTextual) ");
            sqlContains = "SELECT RETORNO FROM [IDOSGD].PK_SGD_DESCRIPCION_QUERYFILTER('"+busRazonSocial.trim()+"')";
        }

        sql.append(") A ");
        sql.append("WHERE ROWNUM <= ").append(applicationProperties.getTopRegistrosConsultas());
        
        List<ProveedorBean> list = new ArrayList<ProveedorBean>();
        try {
            //Obteniendo el parametro textual si es requerido
            if (sqlContains.length() > 0) {
                String cadena = this.jdbcTemplate.queryForObject(sqlContains, String.class);            
                objectParam.put("pBusquedaTextual", cadena);
            }
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
