package pe.gob.onpe.tramiteconv.dao.impl.oracle;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.libreria.util.Mensaje;

import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioDepAcceso;
import pe.gob.onpe.tramitedoc.bean.UsuarioDependenciaAcceso;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
import pe.gob.onpe.tramitedoc.dao.UsuarioConfigDao;

@Repository("usuarioConfigDao")
public class UsuarioConfigDaoImp extends SimpleJdbcDaoBase implements UsuarioConfigDao {

    @Override
    public UsuarioConfigBean getConfig(Mensaje msg, String cempCodemp, String coDep) {
        if(msg==null){
            msg = new Mensaje();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select\n" +
                    "replace(a.de_dir_emi,'\\','|') de_dir_emi ,\n" +
                    "a.IN_TIPO_DOC in_tipo_doc ,\n" +
                    "a.CO_EMP cemp_codemp ,\n" +
                    "a.CO_DEP co_dep ,\n" +
                    "PK_SGD_DESCRIPCION.DE_DEPENDENCIA(a.CO_DEP)  de_dep,\n" +
                    "PK_SGD_DESCRIPCION.DE_SIGLA(a.CO_DEP)  de_Siglas_Dep,\n" +
                    "a.CO_LOC co_local,\n" +
                    "PK_SGD_DESCRIPCION.DE_LOCAL(a.CO_LOC) de_local,\n" +
                    "a.DE_DIR_ANE de_Ruta_Anexo,\n" +
                    "a.DE_PIE_PAGINA ,\n" +
                    "a.TI_ACCESO ,\n" +
                    "a.IN_CONSULTA TI_CONSULTA,\n" +
                    "a.IN_MAIL in_Correo,\n" +
                    "a.IN_CARGA_DOC in_Carga_Doc_Mesa_Partes,\n" +
                    "a.IN_FIRMA, \n" +
                    "a.IN_OBS_DOCU in_Obs_Documento,\n" +
                    "a.IN_REVI_DOCU in_Revi_Documento,\n" +
                    "b.in_numero_mp in_numero_mp,\n" +
                    "b.in_mesa_partes in_mesa_partes,\n" +
                    "(SELECT DE_PAR FROM tdtr_parametros WHERE CO_PAR='ID_IMG_PORTADA_SGD') NAME_IMG_PORTADA_SGD,\n" +
                    "(SELECT DE_PAR FROM tdtr_parametros WHERE CO_PAR='DIAS_EXPIRACION_CLAVE') DIAS_EXPIRACION_CLAVE,\n" +                
                    "(SELECT DEP.CO_DEPENDENCIA FROM RHTM_DEPENDENCIA DEP WHERE DEP.IN_MESA_PARTES='1' AND DEP.IN_BAJA='0'\n" +
                    "AND 0 < (SELECT COUNT(1) FROM TDTR_DEPENDENCIA_MP DMP WHERE DMP.CO_DEP=B.CO_DEPENDENCIA AND DMP.ES_ELI='0' AND DMP.IN_MP='1')) CO_DEP_MP,\n" +
                    "a.ti_acceso_mp,\n" +
                    "a.in_consulta_mp ti_consulta_mp\n" +
                    "from  TDTX_CONFIG_EMP a , rhtm_dependencia b\n" +
                    "where a.co_dep= ? \n" +
                    "and a.co_emp=? \n" +
                    "and a.co_dep = b.co_dependencia");

        UsuarioConfigBean usuConfig = new UsuarioConfigBean();

        try {
            usuConfig = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(UsuarioConfigBean.class), new Object[]{coDep, cempCodemp});
            msg.setCoRespuesta("00");
            msg.setDeRespuesta("Usuario Valido");                        
        } catch (EmptyResultDataAccessException e) {
            usuConfig = null;
            msg.setCoRespuesta("00");
            msg.setDeRespuesta("Usuario Valido");                          
        } catch (Exception e) {
            usuConfig = null;
            msg.setCoRespuesta("5001");
            msg.setDeRespuesta("Error interno de BASE DE DATOS.");            
            e.printStackTrace();
        }

        return usuConfig;

    }

    
    @Override
    public String insUsuarioConfingBasico(UsuarioConfigBean usuarioConf) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();
        sql.append("insert into TDTX_CONFIG_EMP(\n" +
                    "co_emp, \n" +
                    "co_dep, \n" +
                    "co_loc, \n" +
                    "fe_use_cre, \n" +
                    "fe_use_mod, \n" +
                    "ti_acceso, \n" +
                    "es_reg, \n" +
                    "in_carga_doc, \n" +
                    "in_firma, \n" +
                    "in_tipo_doc, \n" +
                    "in_obs_docu, \n" +
                    "in_revi_docu, \n" +
                    "in_consulta \n" +
                    ")\n" +
                    "values(?,?,'001',sysdate,sysdate,?,'0',?,?,?,?,?,?)");
        
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                usuarioConf.getCempCodemp(),usuarioConf.getCoDep(),
                usuarioConf.getTiAcceso(),usuarioConf.getInCargaDocMesaPartes(),
                usuarioConf.getInFirma(),usuarioConf.getInTipoDoc(),
                usuarioConf.getInObsDocumento(),usuarioConf.getInReviDocumento(),
                usuarioConf.getTiConsulta(), 
                
            });
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    @Override
    public String updUsuarioConfing(UsuarioConfigBean usuarioConf) {
        String vReturn = "NO_OK";
        StringBuilder sql = new StringBuilder();
        sql.append("update TDTX_CONFIG_EMP set ");
        sql.append("de_dir_emi=?, de_pie_pagina=?, ti_acceso=?, in_carga_doc=?, in_firma=?, in_tipo_doc =?, fe_use_mod=sysdate ");
        sql.append("where co_dep = ? AND co_emp = ? ");
        try {
            this.jdbcTemplate.update(sql.toString(), new Object[]{
                usuarioConf.getDeDirEmi(), usuarioConf.getDePiePagina(), usuarioConf.getTiAcceso(),
                usuarioConf.getInCargaDocMesaPartes(), usuarioConf.getInFirma(), usuarioConf.getInTipoDoc(),
                usuarioConf.getCoDep(), usuarioConf.getCempCodemp()
            });
            vReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }
    
    
    @Override
	public List<UsuarioDepAcceso> getListDepAccesos(String cempCodemp , String coUsuario){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CO_DEPENDENCIA, DE_DEPENDENCIA , de_Corta_Depen \n" +
                    "  FROM RHTM_DEPENDENCIA\n" +
                    " WHERE CO_DEPENDENCIA IN (\n" +
                    "                    SELECT CO_DEPENDENCIA\n" +
                    "                    FROM RHTM_DEPENDENCIA\n" +
                    "                    WHERE IN_BAJA = '0'\n" +
                    "                    AND CO_EMPLEADO = ? \n" +
                    "                    UNION\n" +
                    "                    SELECT CO_DEPENDENCIA \n" +
                    "                      FROM RHTM_PER_EMPLEADOS\n" +
                    "                    WHERE CEMP_CODEMP = ?\n" +
                    "                   UNION\n" +
                    "                   SELECT CO_DEP\n" +
                    "                     FROM TDTX_DEPENDENCIA_EMPLEADO\n" +
                    "                    WHERE CO_EMP = ?\n" +
                    "                      AND ES_EMP = '0'\n" +
                    "                   UNION\n" +
                    "                   SELECT CO_DEP COD_DEP\n" +
                    "                     FROM TDTR_PERMISOS\n" +
                    "                    WHERE CO_USE = ?\n" +
                    "                      and ES_ACT = '0'\n" +
                    "                   MINUS\n" +
                    "                   SELECT CO_DEP COD_DEP\n" +
                    "                     FROM TDTR_PERMISOS\n" +
                    "                    WHERE CO_USE = ?\n" +
                    "                      and ES_ACT = '1')\n" +
                    "   AND IN_BAJA <> '1'\n" +
                    " ORDER BY ti_dependencia, DE_DEPENDENCIA");
        List<UsuarioDepAcceso> list=new ArrayList<UsuarioDepAcceso>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(UsuarioDepAcceso.class),
                    new Object[]{cempCodemp,cempCodemp,cempCodemp,coUsuario,coUsuario});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        
        return list;
    }

    @Override
	public List<UsuarioDepAcceso> getListDepTotal(String cempCodemp){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CO_DEPENDENCIA, DE_DEPENDENCIA , de_Corta_Depen \n" +
                    "  FROM RHTM_DEPENDENCIA\n" +
                    " ORDER BY ti_dependencia, CO_DEPENDENCIA");
        List<UsuarioDepAcceso> list=new ArrayList<UsuarioDepAcceso>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(UsuarioDepAcceso.class),
                    new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        
        return list;
    }
        
        
    @Override
    public String getCoDepUsuario(String pcoEmp) {
        String vcoDep=null;
        StringBuilder sql = new StringBuilder();
        sql.append("select B.CO_DEPENDENCIA\n"
                + "from RHTM_PER_EMPLEADOS B\n"
                + "WHERE B.CEMP_CODEMP = ? \n"
                + "AND B.CEMP_EST_EMP ='1' ");

        try {
            vcoDep = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pcoEmp});
        } catch (EmptyResultDataAccessException e) {
            vcoDep = null;
        } catch (Exception e) {
            vcoDep = null;
            e.printStackTrace();
        }
        return vcoDep;
    }

    @Override
    public String getTiEncargadoDep(String pcoEmp, String pcoDep) {
        String vcoDep=null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nvl(co_tipo_encargatura,'3') \n" +
                    "FROM RHTM_DEPENDENCIA\n" +
                    "WHERE IN_BAJA = '0'\n" +
                    "AND CO_DEPENDENCIA = ?\n" +
                    "AND CO_EMPLEADO = ?");
        try {
            vcoDep = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{pcoDep, pcoEmp});
        } catch (EmptyResultDataAccessException e) {
            vcoDep = null;
        } catch (Exception e) {
            vcoDep = null;
            e.printStackTrace();
        }
        return vcoDep;
    }
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    @Override
    public List<UsuarioDependenciaAcceso> getListDependenciaAccesos(String cempCodemp, String coUsuario) {
        StringBuilder sql = new StringBuilder();        
        sql.append("SELECT A.CO_DEPENDENCIA, A.DE_DEPENDENCIA, B.TI_ACCESO, B.TI_ACCESO_MP, B.IN_CONSULTA, B.IN_CONSULTA_MP, A.IN_MESA_PARTES\n" +
                    /*-- [HPB] Inicio 16/01/23 CLS-087-2022 --*/
                    ",(SELECT COUNT(*) FROM TDTX_DEPENDENCIA_EMPLEADO WHERE CO_EMP = ? AND ES_EMP = '0' AND CO_DEP=A.CO_DEPENDENCIA) AS IN_TRABAJADOR"+
                    /*-- [HPB] Fin 16/01/23 CLS-087-2022 --*/
                    "  FROM RHTM_DEPENDENCIA A, TDTX_CONFIG_EMP B\n" +
                    " WHERE A.CO_DEPENDENCIA = B.CO_DEP\n" +
                    "AND A.CO_DEPENDENCIA IN (SELECT CO_DEPENDENCIA\n" +
                    "                            FROM RHTM_DEPENDENCIA\n" +
                    "                           WHERE IN_BAJA = '0'\n" +
                    "                             AND CO_EMPLEADO = ?\n" +
                    "                          UNION\n" +
                    "                          SELECT CO_DEPENDENCIA\n" +
                    "                            FROM RHTM_PER_EMPLEADOS\n" +
                    "                           WHERE CEMP_CODEMP = ?\n" +
                    "                          UNION\n" +
                    "                          SELECT CO_DEP\n" +
                    "                            FROM TDTX_DEPENDENCIA_EMPLEADO\n" +
                    "                           WHERE CO_EMP = ?\n" +
                    "                             AND ES_EMP = '0'\n" +
                    "                          UNION\n" +
                    "                          SELECT CO_DEP COD_DEP\n" +
                    "                            FROM TDTR_PERMISOS\n" +
                    "                           WHERE CO_USE = ?\n" +
                    "                             and ES_ACT = '0'\n" +
                    "                          MINUS\n" +
                    "                          SELECT CO_DEP COD_DEP\n" +
                    "                            FROM TDTR_PERMISOS\n" +
                    "                           WHERE CO_USE = ?\n" +
                    "                             and ES_ACT = '1')\n" +
                    "   AND A.IN_BAJA <> '1'\n" +
                    "   AND B.CO_EMP = ?\n" +
                    " ORDER BY A.ti_dependencia, A.DE_DEPENDENCIA");
        List<UsuarioDependenciaAcceso> list=new ArrayList<UsuarioDependenciaAcceso>();
        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(UsuarioDependenciaAcceso.class),
                    /*-- [HPB] Inicio 16/01/23 CLS-087-2022 --*/
                    //new Object[]{cempCodemp,cempCodemp,cempCodemp,coUsuario,coUsuario,cempCodemp});
                    new Object[]{cempCodemp,cempCodemp,cempCodemp,cempCodemp,coUsuario,coUsuario,cempCodemp});
                    /*-- [HPB] Fin 16/01/23 CLS-087-2022 --*/
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }   
        return list;
    }
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
    
}
