package pe.gob.onpe.autentica.service;

import java.util.Date;
import pe.gob.onpe.autentica.dao.UsuarioDao;
import pe.gob.onpe.autentica.model.DatosUsuario;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.libreria.util.Mensaje;


public interface UsuarioService {
    void autenticarUsuario(Mensaje msg, Usuario usuario, String coAplicativo, boolean isEncripted);

    void obtienePermisos(Mensaje msg, Usuario usuario, String coAplicativo);

    void buildDefaultAdminAccess(Usuario usuario, String name, String coLocal, String DeLocal);

    void cerrarSession(Usuario usuario, String coAplicativo);

    void cambiarClave(Mensaje msg, Usuario usuario,boolean isEncripted);

    void desencripta(Mensaje msg, Usuario usuario);

    //    Setters and Getters
    void setUsuarioDao(UsuarioDao usuarioDao);

    public String encriptaDato(Mensaje msg, String pdato );
	public DatosUsuario getDepUsuario(String coUsuario);

    boolean checkClaveFuerte(String pass);
    String verificarClaveExpiro(int diasExpira,Date feModClave, Date feActual,int nroDiasAnteShowExpiraClave);
    boolean verificarClaveDiferente(String coUser,String pass);
    String registraLogAcceso(Usuario usuario, String success);
    int getNroDiasAntesExpiraClave(int diasExpira,Date feModClave, Date feActual);
    String resetPassword(String coUse, String clave, String coUseMod);
    String delUser(String coUse);
    
    public String obtenerValorParam(String nombreParametro);//Hermes 07/10/19 LDAP
    public String obtenerValorUsuarioDominio(Usuario usuario);//Hermes 07/10/19 LDAP
    public String obtUsuarioHabDirecActivo(Usuario usuario);//Hermes 07/10/19 LDAP
}
