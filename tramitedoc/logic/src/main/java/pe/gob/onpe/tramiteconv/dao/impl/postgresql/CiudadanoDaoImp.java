/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.postgresql;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.CitizenBean;
import pe.gob.onpe.tramitedoc.dao.CiudadanoDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
/**
 *
 * @author RBerrocal
 */
@Repository("ciudadanoDao")
public class CiudadanoDaoImp extends SimpleJdbcDaoBase implements CiudadanoDao {
    @Autowired
    private ApplicationProperties applicationProperties;  
    
    public List<CitizenBean> getCiudadanoList() {
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT NULEM, UBDEP, UBPRV, UBDIS, DEAPP, DEAPM, DENOM ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_departamento (ubdep) NO_DEP, ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_provincia (ubdep, ubprv) NO_PRV, ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_distrito (ubdep,ubprv,ubdis) NO_DIS ");
        sql.append("FROM IDOSGD.TDTX_ANI_SIMIL ");
        List<CitizenBean> list = new ArrayList<CitizenBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(CitizenBean.class),
                    new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    public List<CitizenBean> getCiudadanoBusqList(String busCiudDocumento, String busCiudApPaterno, String busCiudApMaterno, String busCiudNombres) {
        
        StringBuilder sql = new StringBuilder();
        Object[] objectParam = null;
        
        sql.append("SELECT NULEM, UBDEP, UBPRV, UBDIS, DEAPP, DEAPM, DENOM,\n");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_departamento (ubdep) NO_DEP,\n");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_provincia (ubdep, ubprv) NO_PRV,\n");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_distrito (ubdep,ubprv,ubdis) NO_DIS\n");
        sql.append("FROM IDOSGD.TDTX_ANI_SIMIL WHERE TRUE \n");
        
        if (busCiudDocumento != null && !busCiudDocumento.equals("")){
            sql.append(" AND UPPER(NULEM) like '%'||?||'%'\n");
            objectParam = new Object[]{busCiudDocumento};
        }else if (busCiudApPaterno != null && !busCiudApPaterno.equals("")){
            sql.append(" AND UPPER(DEAPP) like '%'||?||'%'\n");
            objectParam = new Object[]{busCiudApPaterno};
        }
        else if( busCiudApMaterno != null && !busCiudApMaterno.equals("")) {
            sql.append(" AND UPPER(DEAPM) like '%'||?||'%'\n"); 
            objectParam = new Object[]{busCiudApMaterno};
        }
        else if( busCiudNombres != null && !busCiudNombres.equals("")) {
            sql.append(" AND UPPER(DENOM) like '%'||?||'%'\n"); 
            objectParam = new Object[]{busCiudNombres};
        }
        
        sql.append("order by DEAPP \n"); 
        sql.append("LIMIT ").append(applicationProperties.getTopRegistrosConsultas()); 
        List<CitizenBean> list = new ArrayList<CitizenBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(CitizenBean.class),objectParam);
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    public String insCiudadano(CitizenBean ciudadano, String codUsuario) {
        String vReturn      = "NO_OK";
        StringBuilder sql    = new StringBuilder();
        String genCoProceso = "";
        
        sql.append("insert into IDOSGD.TDTX_ANI_SIMIL ");
        sql.append("(NULEM, UBDEP, UBPRV, UBDIS, DEAPP, DEAPM, DENOM) ");
        sql.append("values (?,?,?,?,?, ?,? ) ");

        try {        
            //El campo Estado no se incluye en el bloque del insert pq es un valor default en la tabla
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                ciudadano.getNuLem(), ciudadano.getUbDep(), ciudadano.getUbPrv(), ciudadano.getUbDis(), 
                ciudadano.getDeApp(), ciudadano.getDeApm(),  ciudadano.getDeNom()
            });
            
            vReturn = "OK";
            
        } catch (DuplicateKeyException con) {
            vReturn = "Número de documento duplicado.";
            con.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn; 
    }

    public CitizenBean getCiudadano(String codCiudadano) {
        CitizenBean ciudadanoBean = new CitizenBean();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT NULEM, UBDEP, UBPRV, UBDIS, DEAPP, DEAPM, DENOM, ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_departamento (ubdep) NO_DEP, ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_provincia (ubdep, ubprv) NO_PRV, ");
        sql.append("IDOSGD.PK_SGD_DESCRIPCION_fu_get_distrito (ubdep,ubprv,ubdis) NO_DIS ");
        sql.append("FROM IDOSGD.TDTX_ANI_SIMIL WHERE NULEM = ?");
        
        try {
            ciudadanoBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(CitizenBean.class),
                    new Object[]{codCiudadano});
        } catch (EmptyResultDataAccessException e) {
            ciudadanoBean = null;
        } catch (Exception e) {
            ciudadanoBean = null;
            e.printStackTrace();
        }
        
        return ciudadanoBean;
    }

    public String updCiudadano(CitizenBean ciudadano, String codUsuario) {
        String vReturn = "NO_OK";
        
        StringBuilder sql = new StringBuilder();
        sql.append("update IDOSGD.TDTX_ANI_SIMIL set ");
        sql.append("UBDEP=?, UBPRV=?, UBDIS=?, DEAPP=?, DEAPM = ?, DENOM= ? ");
        sql.append("where NULEM=? ");
        
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                ciudadano.getUbDep(), ciudadano.getUbPrv(), ciudadano.getUbDis(), ciudadano.getDeApp(), 
                ciudadano.getDeApm(), ciudadano.getDeNom(), ciudadano.getNuLem()
            });
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return vReturn;
    }
    
}
