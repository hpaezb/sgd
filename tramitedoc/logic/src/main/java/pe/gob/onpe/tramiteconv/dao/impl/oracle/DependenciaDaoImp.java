/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.oracle;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.LocalDepBean;
import pe.gob.onpe.tramitedoc.dao.DependenciaDao;

/**
 *
 * @author ECueva
 */
@Repository("dependenciaDao")
public class DependenciaDaoImp extends SimpleJdbcDaoBase implements DependenciaDao{

    @Override
    public List<DependenciaBean> getAllDependencia(boolean esAdmin,String codDependencia) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DE_DEPENDENCIA, CO_DEPENDENCIA FROM RHTM_DEPENDENCIA WHERE ");
        if(!esAdmin){
            sql.append(" CO_DEPENDENCIA='");
            sql.append(codDependencia);
            sql.append("'");
            sql.append(" AND");
        }
        sql.append(" IN_BAJA='0' ORDER BY 1 ");
        
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }

    @Override
    public DependenciaBean getDependencia(String coDep) {
        DependenciaBean dependenciaBean=null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT co_dependencia,de_dependencia,in_baja,co_nivel,\n" +
                "de_corta_depen,co_empleado,PK_SGD_DESCRIPCION.DE_NOM_EMP(co_empleado) de_empleado,\n" +
                "de_sigla,co_cargo,PK_SGD_DESCRIPCION.DE_CARGO(co_cargo) de_cargo,co_tipo_encargatura,\n" +
                "PK_SGD_DESCRIPCION.DE_DOMINIOS('CO_TIPO_ENC',co_tipo_encargatura) de_tipo_encargatura,\n" +
                "de_cargo_completo,co_depen_padre,PK_SGD_DESCRIPCION.DE_DEPENDENCIA(co_depen_padre) de_depen_padre,\n" +
                "titulo_dep,\n" +
                "ti_dependencia tiDependencia \n"+
                "FROM RHTM_DEPENDENCIA\n" +
                "WHERE CO_DEPENDENCIA=?");
        try {
            dependenciaBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class),
                    new Object[]{coDep});            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependenciaBean;
    }

    @Override
    public List<DependenciaBean> getDependenciaHijo(String coDepPadre) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n" +
                    "co_dependencia, \n" +
                    "de_dependencia, \n" +
                    "in_baja, \n" +
                    "co_nivel, \n" +
                    "de_corta_depen, \n" +
                    "co_depen_padre, \n" +
                    "co_empleado,\n" +
                    "PK_SGD_DESCRIPCION.DE_NOM_EMP(co_empleado) de_empleado, \n" +
                    "ti_dependencia, \n" +
                    "de_sigla, \n" +
                    "co_cargo, \n" +
                    "PK_SGD_DESCRIPCION.DE_CARGO(co_cargo) de_cargo,\n" +
                    "co_tipo_encargatura, \n" +
                    "PK_SGD_DESCRIPCION.DE_DOMINIOS('CO_TIPO_ENC',co_tipo_encargatura) de_tipo_encargatura,\n" +
                    "co_sec_dep, \n" +
                    "titulo_dep, \n" +
                    "de_cargo_completo \n" +
                    "FROM RHTM_DEPENDENCIA \n" +
                    "where co_depen_padre = ? \n" +
                    "order by co_dependencia");
        
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class),new Object[]{coDepPadre});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public List<DependenciaBean> getBuscaDependencia(String busDep, String busTipo) {
        StringBuilder sql = new StringBuilder();
        String sqlDep = " and ti_dependencia='"+busTipo+"' ";
        if(busTipo.equals("2")){
            sqlDep = "";//no aplica filtro 2=todos
        }
        sql.append("SELECT \n" +
                    "co_dependencia, \n" +
                    "de_dependencia, \n" +
                    "in_baja, \n" +
                    "co_nivel, \n" +
                    "de_corta_depen, \n" +
                    "co_depen_padre, \n" +
                    "co_empleado,\n" +
                    "PK_SGD_DESCRIPCION.DE_NOM_EMP(co_empleado) de_empleado, \n" +
                    "ti_dependencia, \n" +
                    "de_sigla, \n" +
                    "co_cargo, \n" +
                    "PK_SGD_DESCRIPCION.DE_CARGO(co_cargo) de_cargo,\n" +
                    "co_tipo_encargatura, \n" +
                    "PK_SGD_DESCRIPCION.DE_DOMINIOS('CO_TIPO_ENC',co_tipo_encargatura) de_tipo_encargatura,\n" +
                    "co_sec_dep, \n" +
                    "titulo_dep, \n" +
                    "de_cargo_completo \n" +
                    "FROM RHTM_DEPENDENCIA \n" +
                    "where de_dependencia like '%'||?||'%' \n" +
                    sqlDep+
                    "order by ti_dependencia , co_dependencia ");
        List<DependenciaBean> list = new ArrayList<DependenciaBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DependenciaBean.class),new Object[]{busDep});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public String updDependencia(DependenciaBean dep,String coDepAn, String pTipoAnt){
        // tipo INSTITUCION => COMITE ESPECIAL, entonces generar MAX()+1 al 
        if(pTipoAnt.equals("0") && dep.getTiDependencia().equals("1")){ 
            String sql1 = "(SELECT lpad(to_char(to_number(MAX(CO_DEPENDENCIA))+1),5,'0') FROM RHTM_DEPENDENCIA WHERE TI_DEPENDENCIA=1)";
            String coDep = this.jdbcTemplate.queryForObject(sql1, String.class);
            dep.setCoDependencia(coDep);
        }
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE RHTM_DEPENDENCIA SET\n" +
                    "CO_DEPENDENCIA='"+dep.getCoDependencia()+"' ,"
                    +"DE_DEPENDENCIA=?,IN_BAJA=?,CO_NIVEL=?,DE_CORTA_DEPEN=?,\n" +
                    "CO_DEPEN_PADRE=?,CO_EMPLEADO=?,FE_ACT=SYSDATE,ID_ACT=?,\n" +
                    "DE_SIGLA=?,CO_CARGO=?,CO_TIPO_ENCARGATURA=?,TITULO_DEP=?,\n" +
                    "DE_CARGO_COMPLETO=?,TI_DEPENDENCIA=? \n" +
                    "WHERE CO_DEPENDENCIA=?");
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{dep.getDeDependencia(),dep.getInBaja(),dep.getCoNivel(),
            dep.getDeCortaDepen(),dep.getCoDepenPadre(),dep.getCoEmpleado(),dep.getIdAct(),dep.getDeSigla(),dep.getCoCargo(),
            dep.getCoTipoEncargatura(),dep.getTituloDep(),dep.getDeCargoCompleto(),dep.getTiDependencia(),coDepAn});
            //vReturn = "OK";
            vReturn = dep.getCoDependencia();
        } catch (DuplicateKeyException con) {
            vReturn = "DUPLICADO";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;        
    }
    
    @Override
    public String insDependencia(DependenciaBean dep){
        if(dep.getTiDependencia().equals("1")){ 
            String sql1 = "(SELECT lpad(to_char(to_number(COALESCE(MAX(CO_DEPENDENCIA),'0'))+1),5,'0') FROM RHTM_DEPENDENCIA WHERE TI_DEPENDENCIA='1')";
            String coDep = this.jdbcTemplate.queryForObject(sql1, String.class);
            dep.setCoDependencia(coDep);
        }
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO RHTM_DEPENDENCIA(\n" +
                "CO_DEPENDENCIA,DE_DEPENDENCIA,IN_BAJA,CO_NIVEL,DE_CORTA_DEPEN,\n" +
                "CO_DEPEN_PADRE,CO_EMPLEADO,TI_DEPENDENCIA,FE_CREA,ID_CREA,FE_ACT,ID_ACT,\n" +
                "DE_SIGLA,CO_CARGO,CO_TIPO_ENCARGATURA,TITULO_DEP,DE_CARGO_COMPLETO)\n" +
                "VALUES(?,?,'0',?,?,\n" +
                "?,?,?,SYSDATE,?,SYSDATE,?,\n" +
                "?,?,?,?,?)");
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{dep.getCoDependencia(),dep.getDeDependencia(),dep.getCoNivel(),
            dep.getDeCortaDepen(),dep.getCoDepenPadre(),dep.getCoEmpleado(),dep.getTiDependencia(),dep.getIdAct(),dep.getIdAct(),
            dep.getDeSigla(),dep.getCoCargo(),dep.getCoTipoEncargatura(),dep.getTituloDep(),dep.getDeCargoCompleto()});
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Código Dependencia Duplicado.";
            //con.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;
    }
    
    @Override
    public String insLocalDependencia(LocalDepBean localDep){
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO SITM_LOCAL_DEPENDENCIA(CO_DEP,CO_LOC,NU_CORRELATIVO,ES_ELI,\n" +
                    "CO_USE_CRE,FE_USE_CRE,CO_USE_MOD,FE_USE_MOD)\n" +
                    "VALUES(?,?,?,'0',?,SYSDATE,?,SYSDATE)");        
        
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{localDep.getCoDep(),localDep.getCoLoc(),localDep.getNuCorr(),
            localDep.getCoUseMod(),localDep.getCoUseMod()});
            vReturn = "OK";
        }catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;
    }
    
    @Override
    public LocalDepBean getLocalDepBean(String coDep){
        LocalDepBean localDepBean=null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CO_DEP,CO_LOC,NU_CORRELATIVO NUCORR,ES_ELI FROM SITM_LOCAL_DEPENDENCIA\n" +
                    "WHERE CO_DEP=?");
        
        try {
            localDepBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(LocalDepBean.class),
                    new Object[]{coDep});            
        } catch (EmptyResultDataAccessException e) {
            localDepBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localDepBean;         
    }
    
    @Override
    public String delLocalDependencia(String coDep){
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM SITM_LOCAL_DEPENDENCIA\n" +
                    "WHERE CO_DEP=?");        
        
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{coDep});
            vReturn = "OK";
        }catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }    
    
    @Override
    public String insEmpDependencia(String coEmp,String coDep){
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TDTX_DEPENDENCIA_EMPLEADO(CO_DEP,CO_EMP,ES_EMP)\n" +
                    "VALUES(?,?,'0')");        
        
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{coDep,coEmp});
            vReturn = "OK";
        }catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;
    }
    
    @Override
    public String delEmpDependencia(String coDep){
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM TDTX_DEPENDENCIA_EMPLEADO\n" +
                    "WHERE CO_DEP=?");        
        
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{coDep});
            vReturn = "OK";
        }catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;
    }   
    
    @Override
    public List<EmpleadoBean> getLsEmpDepen(String coDep) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CO_EMP CEMP_CODEMP,ES_EMP ESTADO,PK_SGD_DESCRIPCION.DE_NOM_EMP(CO_EMP) NOMBRE\n" +
                    "FROM TDTX_DEPENDENCIA_EMPLEADO\n" +
                    "WHERE CO_DEP=?");
        
        List<EmpleadoBean> list = new ArrayList<EmpleadoBean>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(EmpleadoBean.class),new Object[]{coDep});
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;        
    }    
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    @Override
    public String delDepAdicDelEmp(String coEmp) {
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM TDTX_CONFIG_EMP\n" +
                    "WHERE CO_EMP=?");             
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{coEmp});
            vReturn = "OK";
        }catch (DataAccessException e) {
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String delDepAdic(String coUsuario, String coDependencia) {
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM TDTR_PERMISOS WHERE CO_USE = ? AND CO_DEP = ?");             
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{coUsuario, coDependencia});
            vReturn = "OK";
        }catch (DataAccessException e) {
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String insAccesosDepAdicDelEmp(String coEmp, String coDep, String coLocal,
                                          String tiAcceso, String inConsulta,
                                          String inConsultaMp, String tiAccesoMp) {
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlUpd = new StringBuilder();
        String sql1 = "SELECT COUNT(*) ENCONTRADO FROM tdtx_config_emp WHERE CO_DEP=? AND CO_EMP = ?";
        String encontrado = jdbcTemplate.queryForObject(sql1, new Object[]{coDep, coEmp}, String.class);

        if(encontrado.equals("0")){
            sql.append("INSERT INTO TDTX_CONFIG_EMP\n" +
                       "(co_emp, co_dep, co_loc, fe_use_cre, fe_use_mod, ti_acceso,\n" +
                       "es_reg, in_consulta, in_consulta_mp, ti_acceso_mp)\n" +
                       "values\n" +
                       "(?, ?, ?, SYSDATE, SYSDATE, ?, '0', ?, ?, ?)");        
        }else{
            sqlUpd.append("update TDTX_CONFIG_EMP \n" +
                          "set ti_acceso = ?,\n" +
                          "in_consulta = ?,\n" +
                          "in_consulta_mp = ?,\n" +
                          "ti_acceso_mp = ?,\n" +
                          "fe_use_mod=SYSDATE \n" +
                          "WHERE co_emp = ? AND co_dep = ?");
        }
        try {
            if(encontrado.equals("0")){
                this.jdbcTemplate.update(sql.toString(), new Object[]{
                                        coEmp, coDep, coLocal,
                                        tiAcceso, inConsulta, 
                                        inConsultaMp, tiAccesoMp});
            }else{
                this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{
                                        tiAcceso, inConsulta, inConsultaMp, 
                                        tiAccesoMp, coEmp, coDep});
            }
            vReturn = "OK";
        }catch (DataAccessException e) {
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String insDepAdicDelEmp(String coUsuario, String coDependencia) {
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("insert into tdtr_permisos (co_use, co_dep, es_act) values (?, ?, '0')");        
        
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                                     coUsuario, coDependencia});
            vReturn = "OK";
        }catch (DataAccessException e) {
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String updAccesosDepAdicDelEmp(String coEmp, String coDep, 
                                          String tiAcceso, String inConsulta, 
                                          String inConsultaMp, String tiAccesoMp) {
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        
        sqlUpd.append("update TDTX_CONFIG_EMP \n"
                    + "set ti_acceso = ?,\n"
                    + "in_consulta = ?,\n"
                    + "in_consulta_mp = ?,\n"
                    + "ti_acceso_mp = ?,\n"
                    + "fe_use_mod=SYSDATE \n"
                    + "WHERE co_emp = ? AND co_dep = ?");
        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{tiAcceso, 
                            inConsulta, inConsultaMp, tiAccesoMp, coEmp, coDep});
            vReturn = "OK";
        } catch (DataAccessException e) {
            vReturn = e.getMessage();
        }
        return vReturn;
    }
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
    /*-- [HPB] Inicio 16/01/23 CLS-087-2022 --*/
    @Override
    public String delEmpDependenciaLst(String coEmp,String coDep) {
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM TDTX_DEPENDENCIA_EMPLEADO\n" +
                    "WHERE CO_DEP=? AND CO_EMP=?");        
        
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{coDep, coEmp});
            vReturn = "OK";
        }catch (Exception e) {
            e.printStackTrace();
            //vReturn = e.getMessage();
        }
        return vReturn;
    }
    /*-- [HPB] Fin 16/01/23 CLS-087-2022 --*/
}
