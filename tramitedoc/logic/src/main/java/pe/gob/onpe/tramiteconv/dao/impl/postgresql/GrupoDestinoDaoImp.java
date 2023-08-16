/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.postgresql;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.GrupoDestinoBean;
import pe.gob.onpe.tramitedoc.bean.GrupoDestinoDetalleBean;
import pe.gob.onpe.tramitedoc.dao.GrupoDestinoDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;

/**
 *
 * @author ngilt
 */
@Repository("grupoDestinoDao")
public class GrupoDestinoDaoImp extends SimpleJdbcDaoBase implements GrupoDestinoDao {

    public List<GrupoDestinoBean> getGruposDestinosList(String codDependencia) {
        StringBuilder sql = new StringBuilder();
        sql.append("select co_gru_des,co_dep,de_gru_des from IDOSGD.TDTM_GRU_DES where co_dep=? and es_gru_des='1' ");
        List<GrupoDestinoBean> list = new ArrayList<GrupoDestinoBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(GrupoDestinoBean.class),
                    new Object[]{codDependencia});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    public List<GrupoDestinoDetalleBean> getGrupoDestinoDetalleList(String codGrupoDestino) {
        StringBuilder sql = new StringBuilder();
        sql.append("select co_gru_des,co_dep , IDOSGD.PK_SGD_DESCRIPCION_de_dependencia(co_dep) de_dependencia,co_emp, IDOSGD.PK_SGD_DESCRIPCION_de_nom_emp(co_emp) de_nombre_emp from IDOSGD.TDTD_DEP_GRU where co_gru_des=?");
        List<GrupoDestinoDetalleBean> list = new ArrayList<GrupoDestinoDetalleBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(GrupoDestinoDetalleBean.class),
                    new Object[]{codGrupoDestino});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    public List<DependenciaBean> getDependenciasList(String codDepen) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CO_DEPENDENCIA , DE_DEPENDENCIA  FROM IDOSGD.RHTM_DEPENDENCIA ");
        sql.append("WHERE CO_NIVEL <> '04' AND IN_BAJA = '0' ");
        sql.append("UNION SELECT CO_DEPENDENCIA COD_DEP, DE_DEPENDENCIA DESCRIP FROM IDOSGD.RHTM_DEPENDENCIA ");
        sql.append("WHERE CO_NIVEL = '04' AND IN_BAJA = '0' AND CO_DEPEN_PADRE =? ");
        sql.append("ORDER BY 2 ");
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class),
                    new Object[]{codDepen});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    public String insDependenciaDestino(GrupoDestinoDetalleBean destDet) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();
        sql.append("insert into IDOSGD.TDTD_DEP_GRU(co_gru_des,co_dep,es_dep_gru,co_use_cre,co_use_mod,fe_use_cre,fe_use_mod,co_emp) ");
        sql.append("values (?,?,?,?,?,now(),now(),?)");
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                destDet.getCo_gru_des(), destDet.getCo_dep(), destDet.getEs_dep_gru(), destDet.getCo_use_cre(), destDet.getCo_use_mod(),destDet.getCo_emp()
            });
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    public List<EmpleadoBean> getEmpleadosDestList(String codDepen, String codGrupoDest) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom nombre, e.CEMP_CODEMP cempCodemp ");
        sql.append("FROM IDOSGD.RHTM_PER_EMPLEADOS e ");
        sql.append("where e.CEMP_EST_EMP = '1' ");
        sql.append("and (CO_DEPENDENCIA=? ");
        sql.append("or CEMP_CO_DEPEND=?) ");
        sql.append("and e.CEMP_CODEMP not in (select a.co_emp from IDOSGD.TDTD_DEP_GRU a where a.co_emp=e.cemp_codemp and  a.co_gru_des=? ) ");
        sql.append("union ");
        sql.append("SELECT e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom nombre, e.CEMP_CODEMP cempCodemp ");
        sql.append("FROM IDOSGD.RHTM_PER_EMPLEADOS e ");
        sql.append("where e.CEMP_EST_EMP = '1' ");
        sql.append("and cemp_codemp ");
        sql.append("in (select co_emp from IDOSGD.tdtx_dependencia_empleado  where co_dep=? and es_emp='0' ) ");
        sql.append("UNION ");
        sql.append("select '- Funcionario -', null ");
        sql.append("ORDER BY 1 ");

        List<EmpleadoBean> list = new ArrayList<EmpleadoBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class),
                    new Object[]{codDepen, codDepen, codGrupoDest, codDepen});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    public String updDependenciaDestino(GrupoDestinoDetalleBean destDet, String codEmpActual) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();
        if (codEmpActual.isEmpty()) {
            sql.append("update IDOSGD.TDTD_DEP_GRU set co_emp=?,co_use_mod=?,fe_use_mod=now()  where co_gru_des=? and co_dep=? and co_emp IS NULL");
        } else {
            sql.append("update IDOSGD.TDTD_DEP_GRU set co_emp=?,co_use_mod=?,fe_use_mod=now()  where co_gru_des=? and co_dep=? and co_emp=?");
        }
        try {

            if (codEmpActual.isEmpty()) {
                this.jdbcTemplate.update(sql.toString(), new Object[]{
                    destDet.getCo_emp(), destDet.getCo_use_mod(), destDet.getCo_gru_des(), destDet.getCo_dep()
                });
            } else {
                this.jdbcTemplate.update(sql.toString(), new Object[]{
                    destDet.getCo_emp(), destDet.getCo_use_mod(), destDet.getCo_gru_des(), destDet.getCo_dep(), codEmpActual
                });
            }
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    public String eliDetalleGrupoDest(GrupoDestinoDetalleBean destDet) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();
        if (destDet.getCo_emp()==null) {
            sql.append("delete from IDOSGD.TDTD_DEP_GRU where co_gru_des=? and co_dep=? and co_emp is null");
        } else {
            sql.append("delete from IDOSGD.TDTD_DEP_GRU where co_gru_des=? and co_dep=? and co_emp=?");
        }
        try {
            if (destDet.getCo_emp()==null) {
                this.jdbcTemplate.update(sql.toString(), new Object[]{
                    destDet.getCo_gru_des(), destDet.getCo_dep()
                });
            } else {
                this.jdbcTemplate.update(sql.toString(), new Object[]{
                    destDet.getCo_gru_des(), destDet.getCo_dep(), destDet.getCo_emp()
                });
            }
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    public String insGrupoDest(GrupoDestinoBean gruDest) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();
        sql.append("insert into IDOSGD.TDTM_GRU_DES(co_gru_des,co_dep,de_gru_des, es_gru_des, co_use_cre, co_use_mod, fe_use_cre ,fe_use_mod) ");
        sql.append("values(?,?,?,?,?,?,now(),now()) ");
        try {

            String sec_coGruDes = (String) this.jdbcTemplate.queryForObject("SELECT LPAD(CAST(COALESCE(MAX(CAST(CO_GRU_DES AS INT)),0)+1 AS TEXT),4,'0') FROM IDOSGD.TDTM_GRU_DES", String.class);
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                sec_coGruDes, gruDest.getCoDep(), gruDest.getDeGruDes(), gruDest.getEsGruDes(), gruDest.getCoUseCre(), gruDest.getCoUseMod()
            });
            vReturn = sec_coGruDes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    public String eliGrupoDestino(GrupoDestinoBean gruDest) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();
        sql.append("update IDOSGD.TDTM_GRU_DES set es_gru_des=0, co_use_mod=?,fe_use_mod=now()  where co_gru_des=? ");
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                gruDest.getCoUseMod(), gruDest.getCoGruDes()
            });
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }
    public String updGrupoDestinoCabecera(GrupoDestinoBean gruDest) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();
        sql.append("update IDOSGD.TDTM_GRU_DES set de_gru_des=?, co_use_mod=?,fe_use_mod=now()  where co_gru_des=? ");
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                gruDest.getDeGruDes(),gruDest.getCoUseMod(), gruDest.getCoGruDes()
            });
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    public List<EmpleadoBean> getEmpleadosDependenciaList(String codDepen) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom nombre, e.CEMP_CODEMP cempCodemp ");
        sql.append("FROM IDOSGD.RHTM_PER_EMPLEADOS e ");
        sql.append("where e.CEMP_EST_EMP = '1' ");
        sql.append("and (CO_DEPENDENCIA=? or CEMP_CO_DEPEND=?)union ");
        sql.append("SELECT e.cemp_apepat||' '||e.cemp_apemat||' '||e.cemp_denom nombre, e.CEMP_CODEMP Codigo ");
        sql.append("FROM IDOSGD.RHTM_PER_EMPLEADOS e ");
        sql.append("where e.CEMP_EST_EMP = '1' ");
        sql.append("and cemp_codemp ");
        sql.append("in (select co_emp from IDOSGD.tdtx_dependencia_empleado  where co_dep=? and es_emp='0' ) ");
        sql.append("UNION ");
        sql.append("select '- Funcionario -', null ");
        sql.append("ORDER BY 1");

        List<EmpleadoBean> list = new ArrayList<EmpleadoBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class),
                    new Object[]{codDepen, codDepen, codDepen});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public String getCantDuplicadoGrupoDestino(String deGrupo, String coDep){
        String vResult= "1";// >=1 existe grupo repetido.
        try {
            vResult = this.jdbcTemplate.queryForObject("SELECT COUNT(1) FROM IDOSGD.TDTM_GRU_DES WHERE DE_GRU_DES=? AND CO_DEP=? AND ES_GRU_DES='1'", 
                    String.class, new Object[]{deGrupo, coDep});
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return vResult;            
    }
}
