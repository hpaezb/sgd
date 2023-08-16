package pe.gob.onpe.autentica.dao.impl.oracle;

import java.text.SimpleDateFormat;
import org.springframework.jdbc.core.*;
import pe.gob.onpe.autentica.dao.UsuarioDao;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.autentica.model.UsuarioAcceso;
import pe.gob.onpe.libreria.util.Mensaje;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.directory.Attributes;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import pe.gob.onpe.autentica.model.DatosUsuario;
import pe.gob.onpe.autentica.model.DatosUsuarioLog;
import pe.gob.onpe.autentica.util.Constantes;
import pe.gob.onpe.autentica.util.LDAPTest;
import pe.gob.onpe.libreria.util.ConstantesSec;
import pe.gob.onpe.libreria.util.Utility;


public class UsuarioDaoImpl implements UsuarioDao {
    private JdbcTemplate jdbcTemplate;

    public UsuarioDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
//       public void getRptaIdentificacion(Mensaje msg, Usuario usuario, boolean isEncripted) throws Exception {
//        if (isEncripted) {
//            desencripta(msg, usuario);
//        }
//        
//        if(msg==null){
//            msg = new Mensaje();
//        }
//        
//        StringBuffer sql = new StringBuffer();
//
//        sql.append("select \n" +
//            "cemp_codemp, \n" +
//          "cclave de_Password, TO_DATE(TO_CHAR(DFE_MOD_CLAVE,'YYYY-MM-DD'),'YYYY-MM-DD') FE_MOD_CLAVE, NVL(ES_USUARIO,'N') ES_ACTIVO, TO_DATE(TO_CHAR(SYSDATE,'YYYY-MM-DD'),'YYYY-MM-DD') FE_ACTUAL,\n" +                
//            "DFEC_MOD D_FEC_MOD, SYSDATE FULL_FECHA_ACTUAL, NVL(NU_INTENTO,0) + 1 NRO_INTENTO\n" +
//            "from seg_usuarios1 \n" +
//            "where cod_user = ?");
//        
//        DatosUsuario datosUsuario =null;
//                
//        try {
//            datosUsuario= this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DatosUsuario.class), usuario.getCoUsuario());
//            usuario.setCempCodemp(datosUsuario.getCempCodemp());
//           // usuario.setInAD(datosUsuario.getInAD());
//            String esUsuario=datosUsuario.getEsActivo();
//            boolean userBloqMaxIntentos = false;
//            if(esUsuario!=null&&(esUsuario.equals("A")||esUsuario.equals("N")||esUsuario.equals("M"))){
//                boolean passOk = false;
//                //para el caso de migracion de contraseña fuerte.
//                if(esUsuario.equals("M")){
//                    usuario.setDePassword(usuario.getDePassword().toUpperCase());
//                    datosUsuario.setEsActivo("A");
//                }
//               
//                //YUAL
//              //  if(usuario.getInAD().equals("1"))
//             //   {
//               //    msg.setCoRespuesta("2829");
//              //      msg.setDeRespuesta("Validación por Active Directory");
//              //  }
//              //  else
//               // {                            
//                    String dePass=Utility.getInstancia().cifrar(usuario.getDePassword(),ConstantesSec.SGD_SECRET_KEY_PASSWORD);
//                    if(dePass.equals(datosUsuario.getDePassword())){ 
//                        passOk=true;
//                        usuario.setFeModClave(datosUsuario.getFeModClave());
//                        usuario.setEsUsuario(datosUsuario.getEsActivo());
//                        usuario.setFeActual(datosUsuario.getFeActual());
//                        msg.setCoRespuesta("00");
//                        msg.setDeRespuesta("Usuario Valido");
//                    }else{
//                        msg.setCoRespuesta("005");
//                        msg.setDeRespuesta("El Usuario o la Contraseña es incorrecta");                    
//                    }
//                    if(esUsuario.equals("A")){
//                        if(passOk){
//                            this.desbloquearUsuario(usuario.getCoUsuario());
//                        }else{
//                            if(datosUsuario.getNroIntento()==10){
//                                userBloqMaxIntentos=true;
//                            }
//                            this.incrementarNumeroIntentos(usuario.getCoUsuario());
//                        }
//                    }
//                //}
//            }else if(esUsuario!=null && esUsuario.equals("I")){
//                userBloqMaxIntentos=true;
//            }else{
//                msg.setCoRespuesta("005");
//                msg.setDeRespuesta("Usuario Bloqueado.");                
//            }
//            if(userBloqMaxIntentos){
//                usuario.setFullFechaActual(datosUsuario.getFullFechaActual());
//                usuario.setdFecMod(datosUsuario.getdFecMod());                
//                msg.setCoRespuesta("009");
//                //msg.setDeRespuesta("Usuario bloqueado por exceder el número máximo de intentos.");                   
//                msg.setDeRespuesta("Usuario bloqueado por máximo de intentos.");                   
//            }
//        }catch (EmptyResultDataAccessException e){
//            msg.setCoRespuesta("005");
//            msg.setDeRespuesta("Usuario no existe");
//        } catch(Exception e) {
//            msg.setCoRespuesta("5001");
//            msg.setDeRespuesta("Error interno de base de datos, no es posible iniciar session");
//            throw e;
//        }
//        
//    }

    
    
    //VERSION PRESIDENCIA
    public void getRptaIdentificacion(Mensaje msg, Usuario usuario, boolean isEncripted) throws Exception {
        if (isEncripted) {
            desencripta(msg, usuario);
        }
        
        if(msg==null){
            msg = new Mensaje();
        }
        
        StringBuffer sql = new StringBuffer();
        sql.append("select \n" +
                    "cemp_codemp, \n" +
                    "cclave de_Password, TO_DATE(TO_CHAR(DFE_MOD_CLAVE,'YYYY-MM-DD'),'YYYY-MM-DD') FE_MOD_CLAVE, NVL(ES_USUARIO,'N') ES_ACTIVO, TO_DATE(TO_CHAR(SYSDATE,'YYYY-MM-DD'),'YYYY-MM-DD') FE_ACTUAL,\n" +                
                    "DFEC_MOD D_FEC_MOD, SYSDATE FULL_FECHA_ACTUAL, NVL(NU_INTENTO,0) + 1 NRO_INTENTO\n" +
                    "from seg_usuarios1 \n" +
                    "where cod_user = ?");
        
        DatosUsuario datosUsuario =null;
        LDAPTest.LDAP ldap = new LDAPTest.LDAP();
        System.out.println("Usuario: "+ usuario.getCoUsuario());                
        try {
            datosUsuario= this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DatosUsuario.class), usuario.getCoUsuario());
            usuario.setCempCodemp(datosUsuario.getCempCodemp());
            String esUsuario=datosUsuario.getEsActivo();
            boolean userBloqMaxIntentos = false;
            if(esUsuario!=null&&(esUsuario.equals("A")||esUsuario.equals("N")||esUsuario.equals("M"))){
                boolean passOk = false;
                //para el caso de migracion de contraseña fuerte.
                if(esUsuario.equals("M")){
                    usuario.setDePassword(usuario.getDePassword().toUpperCase());
                    datosUsuario.setEsActivo("A");
                }
                String dePass=Utility.getInstancia().cifrar(usuario.getDePassword(),ConstantesSec.SGD_SECRET_KEY_PASSWORD);
                String vParametro = obtenerValorParam("LOGIN_LDAP");//Hermes 07/10/19 LDAP
                String vIndicadorDirectActivoUser = obtUsuarioHabDirecActivo(usuario);//Hermes 07/10/19 LDAP
                if(vParametro.equals("0")){//Hermes 07/10/19 SGD
                    if(dePass.equals(datosUsuario.getDePassword())){
                        System.out.println("AD Deshabilitado - Login con SGD");
                        passOk=true;
                        usuario.setFeModClave(datosUsuario.getFeModClave());
                        usuario.setEsUsuario(datosUsuario.getEsActivo());
                        usuario.setFeActual(datosUsuario.getFeActual());
                        msg.setCoRespuesta("00");
                        msg.setDeRespuesta("Usuario Valido");
                    }else{
                        msg.setCoRespuesta("005");
                        msg.setDeRespuesta("El Usuario o la Contraseña es incorrecta");                    
                    }                      
                    if(esUsuario.equals("A")){
                        if(passOk){
                            this.desbloquearUsuario(usuario.getCoUsuario());
                        }else{
                            if(datosUsuario.getNroIntento()==5){
                                userBloqMaxIntentos=true;
                            }
                            this.incrementarNumeroIntentos(usuario.getCoUsuario());
                        }
                    }                    
                }else{/*Hermes 07/10/19 LDAP*/                    
                    if(vIndicadorDirectActivoUser.equals("0")){//AD Habilitado - Login con AD
                        try {
                            System.out.println("AD Habilitado - Login con AD");
                            String usuarioDominio = obtenerValorUsuarioDominio(usuario);//Obtiene el usuario registrado en el dominio (tabla SI_USUARIO)
                            System.out.println("USUARIO DOMINIO--> "+usuarioDominio);
                            String vDominioLdap = obtenerValorParam("DOMINIO_LDAP");//Hermes 16/04/20 LDAP
                            String vIpLdap = obtenerValorParam("IP_URL_LDAP");//Hermes 16/04/20 LDAP
                            Attributes att = null;
                            if(usuarioDominio!=null || !usuarioDominio.equals(""))                   
                                att = ldap.authenticateUser(usuarioDominio, usuario.getDePassword(), vDominioLdap, vIpLdap, "DC=vipdp,DC=dpperu,DC=local"); //Hermes 16/04/20 LDAP                   
                            if (att == null){
                                System.out.println("1--> ");
                                msg.setCoRespuesta("005");
                                msg.setDeRespuesta("El Usuario o la Contraseña es incorrecta");                                                                    
                            }else{
                                System.out.println("2--> ");
                                passOk=true;
                                long fileTime = (Long.parseLong(att.get("pwdLastSet").get().toString()) / 10000L) - + 11644473600000L;
                                Date inputDate = new Date(fileTime);
                                String fechaFinal = "31/12/2100";
                                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                                System.out.println("Fecha Modifica PWD LDAP: "+ inputDate);
                                //usuario.setFeModClave(inputDate);
                                usuario.setFeModClave(formato.parse(fechaFinal));
                                usuario.setEsUsuario(datosUsuario.getEsActivo());
                                usuario.setFeActual(datosUsuario.getFeActual());
                                msg.setCoRespuesta("00");
                                msg.setDeRespuesta("Usuario Valido");
                            }                           
                        } catch (Exception e) {
                            System.out.println("3--> ");
                            e.printStackTrace();
                            msg.setCoRespuesta("005");
                            msg.setDeRespuesta(LDAPTest.decodeMessage(e.getMessage()));
                        } /*Hermes 07/10/19 LDAP*/                                           
                    }else{//AD Habilitado - Login con SGD
                        if(dePass.equals(datosUsuario.getDePassword())){
                            System.out.println("AD Habilitado - Login con SGD");
                            passOk=true;
                            usuario.setFeModClave(datosUsuario.getFeModClave());
                            usuario.setEsUsuario(datosUsuario.getEsActivo());
                            usuario.setFeActual(datosUsuario.getFeActual());
                            msg.setCoRespuesta("00");
                            msg.setDeRespuesta("Usuario Valido");
                        }else{
                            msg.setCoRespuesta("005");
                            msg.setDeRespuesta("El Usuario o la Contraseña es incorrecta");                    
                        }                      
                        if(esUsuario.equals("A")){
                            if(passOk){
                                this.desbloquearUsuario(usuario.getCoUsuario());
                            }else{
                                if(datosUsuario.getNroIntento()==5){
                                    userBloqMaxIntentos=true;
                                }
                                this.incrementarNumeroIntentos(usuario.getCoUsuario());
                            }
                        }                               
                    }           
                }
            }else if(esUsuario!=null && esUsuario.equals("I")){
                userBloqMaxIntentos=true;
            }else{
                msg.setCoRespuesta("005");
                msg.setDeRespuesta("Usuario Bloqueado.");                
            }
            if(userBloqMaxIntentos){
                usuario.setFullFechaActual(datosUsuario.getFullFechaActual());
                usuario.setdFecMod(datosUsuario.getdFecMod());                
                msg.setCoRespuesta("009");
                //msg.setDeRespuesta("Usuario bloqueado por exceder el número máximo de intentos.");                   
                msg.setDeRespuesta("Usuario bloqueado por máximo de intentos.");                   
            }
        }catch (EmptyResultDataAccessException e){
            msg.setCoRespuesta("005");
            msg.setDeRespuesta("Usuario no existe");
        } catch(Exception e) {
            msg.setCoRespuesta("5001");
            msg.setDeRespuesta("Error interno de base de datos, no es posible iniciar session");
            throw e;
        }
        
    }

    @Override
    public String getRptaAplicativo(Mensaje msg, Usuario usuario, String coAplicativo)  {
        String vret="NO";
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT nvl(ES_ADMIN,'0') es_admin , nvl(ES_ACTIVO,'0') es_activo \n" +
                   " FROM SEG_USER_APLICA\n" +
                   " WHERE COD_USER = ? \n" +
                   " AND COD_APLICA = ? ");
        
        DatosUsuario datosUsuario =null;
                
        try {
            datosUsuario= this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DatosUsuario.class), usuario.getCoUsuario(),coAplicativo );
            if (datosUsuario !=null){
                if (datosUsuario.getEsActivo().equals("1")){
                    vret="OK";
                    msg.setCoRespuesta("00");
                    msg.setDeRespuesta("Usuario Valido");
                    if (datosUsuario.getEsAdmin().equals("1")){
                        usuario.setInAdmin("1");
                    }else{
                        usuario.setInAdmin("0");
                    }
                }else{
                    msg.setCoRespuesta("005");
                    msg.setDeRespuesta("Usuario no tiene accesos al aplicativo");
                }
            }
        }catch (EmptyResultDataAccessException e){
            msg.setCoRespuesta("005");
            msg.setDeRespuesta("Usuario no tiene accesos al aplicativo");
        } catch(Exception e) {
            msg.setCoRespuesta("5001");
            msg.setDeRespuesta("Error interno de base de datos, no es posible iniciar session");
        }
        
        return(vret);
        
    }
    
    
    
    @Override
    public void getModificaClave(Mensaje msg, Usuario usuario) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" update seg_usuarios1 \n" +
                    "set cclave = ? , dfec_mod = sysdate , cuser_mod = ?, DFE_MOD_CLAVE=SYSDATE, ES_USUARIO=? \n" +                
                    //"set cclave = ? , dfec_mod = sysdate , cuser_mod = ?, DFE_MOD_CLAVE=SYSDATE, ES_USUARIO=? \n" +                
                    "where cod_user = ?");
        try{
            int nAct= this.jdbcTemplate.update(sql.toString(), new Object[]{
                            usuario.getDePasswordNuevo(),usuario.getCoUsuario(),usuario.getEsUsuario(),usuario.getCoUsuario()});            
            
            if (nAct>0){
                msg.setCoRespuesta("00");
                msg.setDeRespuesta("Su contraseña ha sido actualizada correctamente");
            }else{
                msg.setCoRespuesta("5002");
                msg.setDeRespuesta("Error en datos, Intente Nuevamente");
            }
            
        }catch(Exception e){
            msg.setCoRespuesta("5001");
            msg.setDeRespuesta("Error en base de datos, no es posible modificar clave");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void cerrarSesionAplicativo(Usuario usuario, String coAplicativo) throws Exception {
    }

    @Override
    public void getParametrosGlobales(Mensaje msg, Usuario usuario) throws Exception {
        if(msg==null){
            msg = new Mensaje();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select " 
               +" cemp_codemp cemp_codemp, "
               +" cemp_nu_dni nu_dni, "
               +" cemp_apepat de_apellido_paterno, "
               +" cemp_apemat de_apellido_materno, "
               +" cemp_denom de_prenombres, "
               +" cemp_co_depend co_dep, " 
               +" cemp_co_local co_local "
               +" from rhtm_per_empleados "
               +" where cemp_codemp  = ? "                
               +" AND CEMP_INDBAJ='1'");

        
        Usuario usr=null;
        try{
            usr= this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(Usuario.class), usuario.getCempCodemp());
            usuario.setNuDni(usr.getNuDni());
            usuario.setDeApellidoPaterno(usr.getDeApellidoPaterno());
            usuario.setDeApellidoMaterno(usr.getDeApellidoMaterno());
            usuario.setDePrenombres(usr.getDePrenombres());
            //usuario.setCoDep(usr.getCoDep());
            usuario.setCoLocal(usr.getCoLocal());
        }catch (EmptyResultDataAccessException e){
            msg.setCoRespuesta("007");
            msg.setDeRespuesta("Empleado fue dado de \"BAJA\".");
        }catch(Exception e){
            msg.setCoRespuesta("5003");
            msg.setDeRespuesta("Error Interno, la instruccion SQL contiene errores");
            throw e;
        }
    }

    @Override
    public List<UsuarioAcceso> getPermisosUsuario(Usuario usuario, String coAplicativo) throws Exception {
        StringBuffer sql = new StringBuffer();
        List<UsuarioAcceso> list = new ArrayList<UsuarioAcceso>();
        try{
            sql.append("SELECT substr(cod_opc,1,3) co_modulo, cod_opc co_Opcion\n" +
                        "FROM seg_user_aplica_opc\n" +
                        "where cod_user= ? \n" +
                        "and cod_aplica= ? \n" +
                        "and es_habilitado='1'\n" +
                        "group by cod_opc \n" +
                        "order by 1,2 ");
            
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(UsuarioAcceso.class), new Object[]{usuario.getCoUsuario(),coAplicativo});
        }catch(Exception e){
            list = null;
            e.printStackTrace();
        }
        
        return list;
    }

    @Override
    public void desencripta(Mensaje msg, Usuario usuario) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT fn_desencripta('");
        sql.append(usuario.getNuDni());
        sql.append("','Onpe01') as NU_DNI_USUARIO, ");
        sql.append(" fn_desencripta('");
        sql.append(usuario.getDePassword());
        sql.append("','Onpe01') as DE_PASSWORD FROM DUAL ");
        Usuario usr=null;
        try{
            usr= this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(Usuario.class));
            usuario.setNuDni(usr.getNuDni());
            usuario.setDePassword(usr.getDePassword());
        }catch(Exception e){
            msg.setCoRespuesta("5004");
            msg.setDeRespuesta("Error Interno, la instruccion SQL contiene errores");
        }
    }

    @Override
    public String encriptaDato(Mensaje msg, String pdato ) {
        String vdato = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT fn_desencripta('");
        sql.append(pdato);
        sql.append("','Onpe01') as DE_DATO ");
        sql.append(" FROM DUAL ");
        try{
            vdato= this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(String.class));
        }catch(Exception e){
            msg.setCoRespuesta("5004");

            msg.setCoRespuesta("Error Interno, la instruccion SQL contiene errores");
        }
        return vdato;
    }

    
    @Override
	public DatosUsuario getDepUsuario(String coUsuario){
        StringBuffer sql = new StringBuffer();
        sql.append("select B.CEMP_CODEMP, C.CO_DEPENDENCIA CO_DEP, C.DE_DEPENDENCIA DE_DEP \n" +
                    "from SEG_USUARIOS1 A, RHTM_PER_EMPLEADOS B, RHTM_DEPENDENCIA C\n" +
                    "WHERE A.COD_USER = ? \n" +
                    "AND A.CEMP_CODEMP = B.CEMP_CODEMP\n" +
                    "AND B.CEMP_EST_EMP ='1' \n" +
                    "AND B.CO_DEPENDENCIA = C.CO_DEPENDENCIA ");

        DatosUsuario usuDep = new DatosUsuario();

        try {
            usuDep = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DatosUsuario.class), new Object[]{coUsuario});
        } catch (EmptyResultDataAccessException e) {
            usuDep = null;
        } catch (Exception e) {
            usuDep = null;
            e.printStackTrace();
        }
        return usuDep;
    }
    
    @Override
    public List<DatosUsuarioLog> getHistorialClave(String coUsuario){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT X.*,ROWNUM FROM(\n" +
                    "SELECT COD_USER, NU_CORR, DFE_INI, VCLAVE, VUS_CRE, CESTADO FROM SEG_USUARIOS_LOG\n" +
                    //"SELECT COD_USER, NU_CORR, DFE_INI, VCLAVE, VUS_CRE, CESTADO FROM SEG_USUARIOS_LOG\n" +
                    "WHERE COD_USER=?\n" +
                    "ORDER BY NU_CORR DESC\n" +
                    ") X\n" +
                    "WHERE ROWNUM < 4");                

        List<DatosUsuarioLog> list = null;

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DatosUsuarioLog.class), new Object[]{coUsuario});
        }catch(Exception e){
            list = null;
            e.printStackTrace();
        }
        return list;            
    }

    @Override
    public String getNroSgtCorrHistorialClave(String coUsuario){
        String vReturn = "1";
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT NVL(MAX(NU_CORR),0) + 1 FROM SEG_USUARIOS_LOG WHERE COD_USER=?");

            vReturn = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{coUsuario});
        } catch (Exception e) {
            e.printStackTrace();
            vReturn = "1";
        }
        return vReturn;            
    }    

    @Override
    public String insHistorialClave(DatosUsuarioLog data){
        String vReturn = "NO_OK";
        StringBuffer sqlUpd = new StringBuffer();
        sqlUpd.append("INSERT INTO SEG_USUARIOS_LOG\n" +
                    " (COD_USER, NU_CORR, DFE_INI, VCLAVE, VUS_CRE, DFE_CRE, CESTADO, CO_EMP, DE_IPPC, DE_NAMEPC, DE_USERPC)\n" +
                    "VALUES\n" +
                    " (?, ?, SYSDATE, ?, ?, SYSDATE, '1', ?, ?, ?, ?)");
                    //" (?, ?, SYSDATE, ?, ?, SYSDATE, '1', ?, ?, ?, ?)");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{data.getCodUser(),data.getNuCorr(),data.getvClave(),data.getCodUser(),
            data.getCoEmp(),data.getDeIppc(),data.getDeNamepc(),data.getDeUserpc()});
            vReturn = "OK";
        } catch (DuplicateKeyException con) {
            vReturn = "Datos Duplicado INSERT SEG_USUARIOS_LOG.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;             
    }
    

    @Override
    public String insAccesoLog(DatosUsuarioLog data){
        String vReturn = "NO_OK";
        StringBuffer sqlUpd = new StringBuffer();
        sqlUpd.append("INSERT INTO SEG_USUARIOS_ACCESO\n" +
                    " (COD_USER, DFE_ACCESO, CESTADO, CO_EMP, DE_IPPC, DE_NAMEPC, DE_USERPC, VUS_CRE, DFE_CRE, IN_SUCCESS)\n" +
                    "VALUES\n" +
                    " (?, SYSDATE, '1', ?, ?, ?, ?, ?, SYSDATE, ?)");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{data.getCodUser(),data.getCoEmp(),data.getDeIppc(),data.getDeNamepc(),
            data.getDeUserpc(),data.getCodUser(), data.getSuccess()});
            vReturn = "OK";
        }catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;             
    }
    
    private String incrementarNumeroIntentos(String coUsuario){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("UPDATE SEG_USUARIOS1 SET \n" +
                    "NU_INTENTO = NVL(NU_INTENTO,0) + 1,\n" +
                    "ES_USUARIO=DECODE(NU_INTENTO,4,'I',ES_USUARIO),\n" +
                    "CUSER_MOD = ?,\n" +
                    "DFEC_MOD = SYSDATE\n" +
                    "WHERE COD_USER=?");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{coUsuario, coUsuario});
            vReturn = "OK";
        }catch (Exception e) {
            vReturn = "NO_OK";
            //e.printStackTrace();
        }        
        return vReturn;
    }
    
    @Override
    public String desbloquearUsuario(String coUsuario){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("UPDATE SEG_USUARIOS1 SET \n" +
                    "NU_INTENTO = 0,\n" +
                    "ES_USUARIO = 'A',\n" +
                    "CUSER_MOD = ?,\n" +
                    "DFEC_MOD = SYSDATE\n" +
                    "WHERE COD_USER=?");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{coUsuario, coUsuario});
            vReturn = "OK";
        }catch (Exception e) {
            vReturn = "NO_OK";
            //e.printStackTrace();
        }        
        return vReturn;
    }
    
    @Override
    public String resetPassword(String coUse, String clave, String coUseMod){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("UPDATE SEG_USUARIOS1 SET \n" +
                    "ES_USUARIO = 'N',\n" +
                    "CCLAVE = ?,\n" +
                    "CUSER_MOD = ?,\n" +
                    "DFEC_MOD = SYSDATE\n" +
                    "WHERE COD_USER=?");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{clave, coUseMod, coUse});
            vReturn = "OK";
        }catch (Exception e) {
            vReturn = "NO_OK";
            //e.printStackTrace();
        }        
        return vReturn;
    }
    
    @Override
    public String delUserAplicaOpc(String coUse){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("DELETE FROM seg_user_aplica_opc op \n" +
                        "WHERE op.cod_user = ? \n" +
                        "AND op.cod_aplica = '9'");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{coUse});
            vReturn = "OK";
        }catch (Exception e) {
            vReturn = "NO_OK";
            //e.printStackTrace();
        }        
        return vReturn;        
    }
    
    @Override
    public String delUserAplica(String coUse){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("DELETE FROM seg_user_aplica op \n" +
                        "WHERE cod_aplica = '9' \n" +
                        "AND cod_user = ?");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{coUse});
            vReturn = "OK";
        }catch (Exception e) {
            vReturn = "NO_OK";
            //e.printStackTrace();
        }        
        return vReturn;          
    }   
    
    @Override
    public String delUser(String coUse){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("DELETE FROM seg_usuarios1\n" +
                        "where cod_user= ?");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{coUse});
            vReturn = "OK";
        }catch (Exception e) {
            vReturn = "NO_OK";
            //e.printStackTrace();
        }        
        return vReturn;            
    }
  
    @Override
    public String delClaveUserHis(String coUse){
        String vReturn = "NO_OK";
        StringBuilder sqlUpd = new StringBuilder();
        sqlUpd.append("DELETE FROM SEG_USUARIOS_LOG\n" +
                        "WHERE COD_USER= ?");

        try {
            this.jdbcTemplate.update(sqlUpd.toString(), new Object[]{coUse});
            vReturn = "OK";
        }catch (Exception e) {
            vReturn = "NO_OK";
            //e.printStackTrace();
        }        
        return vReturn;          
    }    
    //Hermes 07/10/19 LDAP
    @Override
    public String obtenerValorParam(String nombreParametro) {
        StringBuffer sql = new StringBuffer();
        String result = null;
        sql.append("SELECT UPPER(DE_PAR) DE_PAR FROM TDTR_PARAMETROS WHERE CO_PAR=?");        
        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{nombreParametro});
        } catch (EmptyResultDataAccessException e) {
            result = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;   
    }

    @Override
    public String obtenerValorUsuarioDominio(Usuario usuario) {
        StringBuffer sql = new StringBuffer();
        String result = null;
        sql.append( "SELECT NVL2(CUSU_DOMINIO, CUSU_DOMINIO, CUSU_CODUSU) USUARIO\n" +
                    "  FROM SI_USUARIO\n" +
                    " WHERE CUSU_CODUSU =?");        
        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{usuario.getCoUsuario()});
        } catch (EmptyResultDataAccessException e) {
            result = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;          
    }

    @Override
    public String obtUsuarioHabDirecActivo(Usuario usuario) {
        StringBuffer sql = new StringBuffer();
        String result = null;
        sql.append( " SELECT NVL2(CUSU_USUHABLDAP, CUSU_USUHABLDAP, '0') USUARIO\n" +
                    " FROM SI_USUARIO\n" +
                    " WHERE CUSU_CODUSU =?");        
        try {
            result = this.jdbcTemplate.queryForObject(sql.toString(), String.class, new Object[]{usuario.getCoUsuario()});
        } catch (EmptyResultDataAccessException e) {
            result = "";
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return result;            
    }
    //Hermes 07/10/19 LDAP        
}
