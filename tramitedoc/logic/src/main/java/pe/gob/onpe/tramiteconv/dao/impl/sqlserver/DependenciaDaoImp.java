/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.sqlserver;

import java.util.ArrayList;
import java.util.List;
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
        sql.append("SELECT DE_DEPENDENCIA, CO_DEPENDENCIA FROM IDOSGD.RHTM_DEPENDENCIA WHERE ");
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
                "de_corta_depen,co_empleado,IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(co_empleado) de_empleado,\n" +
                "de_sigla,co_cargo,IDOSGD.PK_SGD_DESCRIPCION_DE_CARGO(co_cargo) de_cargo,co_tipo_encargatura,\n" +
                "(SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_DE_DOMINIOS('CO_TIPO_ENC',co_tipo_encargatura)) de_tipo_encargatura,\n" +
                "de_cargo_completo,co_depen_padre,IDOSGD.PK_SGD_DESCRIPCION_DE_DEPENDENCIA(co_depen_padre) de_depen_padre,\n" +
                "titulo_dep,\n" +
                "ti_dependencia tiDependencia \n"+
                "FROM IDOSGD.RHTM_DEPENDENCIA\n" +
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
                    "IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(co_empleado) de_empleado, \n" +
                    "ti_dependencia, \n" +
                    "de_sigla, \n" +
                    "co_cargo, \n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_DE_CARGO(co_cargo) de_cargo,\n" +
                    "co_tipo_encargatura, \n" +
                    "(SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_DE_DOMINIOS('CO_TIPO_ENC',co_tipo_encargatura)) de_tipo_encargatura,\n" +
                    "co_sec_dep, \n" +
                    "titulo_dep, \n" +
                    "de_cargo_completo \n" +
                    "FROM IDOSGD.RHTM_DEPENDENCIA \n" +
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
        
        sql.append("SELECT  co_dependencia, ");
        sql.append("        de_dependencia, "); 
        sql.append("        in_baja, ");
        sql.append("        co_nivel, ");
        sql.append("        de_corta_depen, ");
        sql.append("        co_depen_padre, ");
        sql.append("        co_empleado, ");
        sql.append("        IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(co_empleado) de_empleado, ");  
        sql.append("        ti_dependencia, ");
        sql.append("        de_sigla, ");
        sql.append("        co_cargo, ");
        sql.append("        IDOSGD.PK_SGD_DESCRIPCION_DE_CARGO(co_cargo) de_cargo, ");
        sql.append("        co_tipo_encargatura, ");
        sql.append("        (SELECT RETORNO FROM IDOSGD.PK_SGD_DESCRIPCION_DE_DOMINIOS('CO_TIPO_ENC',co_tipo_encargatura)) de_tipo_encargatura, ");
        sql.append("        co_sec_dep, ");
        sql.append("        titulo_dep, ");
        sql.append("        de_cargo_completo ");
        sql.append("FROM IDOSGD.RHTM_DEPENDENCIA ");
        sql.append("where de_dependencia like '%'+?+'%' ");
        sql.append(sqlDep);
        sql.append("order by ti_dependencia, co_dependencia ");
        
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
            String sql1 = "(SELECT RIGHT(REPLICATE('0',5),5)+CONVERT(VARCHAR(5),COALESCE(CAST(MAX(CO_DEPENDENCIA)AS INT),0)+1) FROM IDOSGD.RHTM_DEPENDENCIA WHERE TI_DEPENDENCIA='1')";
            String coDep = this.jdbcTemplate.queryForObject(sql1, String.class);
            dep.setCoDependencia(coDep);
        }
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE IDOSGD.RHTM_DEPENDENCIA SET\n" +
                    "CO_DEPENDENCIA='"+dep.getCoDependencia()+"' ,"
                    +"DE_DEPENDENCIA=?,IN_BAJA=?,CO_NIVEL=?,DE_CORTA_DEPEN=?,\n" +
                    "CO_DEPEN_PADRE=?,CO_EMPLEADO=?,FE_ACT=CURRENT_TIMESTAMP,ID_ACT=?,\n" +
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
            String sql1 = "SELECT RIGHT(REPLICATE('0', 5) + CAST((ISNULL(CAST(MAX(CO_DEPENDENCIA) AS INT), 0) + 1) AS VARCHAR), 5) FROM IDOSGD.RHTM_DEPENDENCIA WHERE TI_DEPENDENCIA='1'";
            String coDep = this.jdbcTemplate.queryForObject(sql1, String.class);
            dep.setCoDependencia(coDep);
        }
        String vReturn = "ERROR";
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO IDOSGD.RHTM_DEPENDENCIA(\n" +
                "CO_DEPENDENCIA,DE_DEPENDENCIA,IN_BAJA,CO_NIVEL,DE_CORTA_DEPEN,\n" +
                "CO_DEPEN_PADRE,CO_EMPLEADO,TI_DEPENDENCIA,FE_CREA,ID_CREA,FE_ACT,ID_ACT,\n" +
                "DE_SIGLA,CO_CARGO,CO_TIPO_ENCARGATURA,TITULO_DEP,DE_CARGO_COMPLETO)\n" +
                "VALUES(?,?,'0',?,?,\n" +
                "?,?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP,?,\n" +
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
        sql.append("INSERT INTO IDOSGD.SITM_LOCAL_DEPENDENCIA(CO_DEP,CO_LOC,NU_CORRELATIVO,ES_ELI,\n" +
                    "CO_USE_CRE,FE_USE_CRE,CO_USE_MOD,FE_USE_MOD)\n" +
                    "VALUES(?,?,?,'0',?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP)");        
        
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
        sql.append("SELECT CO_DEP,CO_LOC,NU_CORRELATIVO NUCORR,ES_ELI FROM IDOSGD.SITM_LOCAL_DEPENDENCIA\n" +
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
        sql.append("DELETE FROM IDOSGD.SITM_LOCAL_DEPENDENCIA\n" +
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
        sql.append("INSERT INTO IDOSGD.TDTX_DEPENDENCIA_EMPLEADO(CO_DEP,CO_EMP,ES_EMP)\n" +
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
        sql.append("DELETE FROM IDOSGD.TDTX_DEPENDENCIA_EMPLEADO\n" +
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
        sql.append("SELECT CO_EMP CEMP_CODEMP,ES_EMP ESTADO,IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(CO_EMP) NOMBRE\n" +
                    "FROM IDOSGD.TDTX_DEPENDENCIA_EMPLEADO\n" +
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

    @Override
    public String delDepAdicDelEmp(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String delDepAdic(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String insAccesosDepAdicDelEmp(String coEmp, String coDep, String coLocal, String tiAcceso, String inConsulta, String inConsultaMp, String tiAccesoMp) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String insDepAdicDelEmp(String coUsuario, String coDependencia) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String updAccesosDepAdicDelEmp(String string, String string1, String string2, String string3, String string4, String string5) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String delEmpDependenciaLst(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String updEncargadoDependenciaBean(String string, String string1) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String delEmpDepen(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
