package pe.gob.onpe.tramitedoc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.autentica.service.UsuarioService;
import pe.gob.onpe.libreria.util.ServletUtility;
import pe.gob.onpe.libreria.util.Mensaje;
import pe.gob.onpe.tramitedoc.util.Constantes;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: crosales
 * Date: 09/05/12
 * Time: 06:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller(value = "logoutController")
@RequestMapping("/{version}/logout.do")
public class LogoutController extends GeneralController {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UsuarioService usuarioService;


    @RequestMapping(method = RequestMethod.POST, params = "accion=salir")
    public @ResponseBody String doLogout(HttpServletRequest request){
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String destino = "OK";
        // esto es para limpiar la session
        //status.setComplete();
        if (usuario != null)
        {
            if(usuario.getInicioId().equals("inicio") ){
                destino = "inicio";
            }
                 //usuarioService.cerrarSession(usuario, applicationProperties.getCoAplicativo());
           usuario.setNuDni(null);
           usuario.setCoUsuario(null);
        }
        
        ServletUtility.getInstancia().invalidateSession(request);

        return destino;
    }
    
    //ISO-8859-1

    @RequestMapping(method = RequestMethod.POST, params = "accion=changepwd",produces = "text/plain; charset=utf-8")
    public @ResponseBody String dochangePwd(@ModelAttribute Usuario usuario, HttpServletRequest request){

        //var params = {nuDni:nuDni, dePassword:clave, dePasswordNuevo:pwd1,dePasswordRepeat:pwd2};

        String nuDni = ServletUtility.getInstancia().loadRequestParameter(request, "nuDni");
        String dePassword = ServletUtility.getInstancia().loadRequestParameter(request, "dePassword");
        String dePasswordNuevo = ServletUtility.getInstancia().loadRequestParameter(request, "dePasswordNuevo");
        String dePasswordRepeat = ServletUtility.getInstancia().loadRequestParameter(request, "dePasswordRepeat");
        
        usuario.setTiIdentificacion(Constantes.TI_IDENTIFICACION);
        loadUsuarioRemoteAttribs(usuario, request);

        usuario.setCoUsuario(nuDni);
        usuario.setDePassword(dePassword);
        usuario.setDePasswordNuevo(dePasswordNuevo);

        String changeIt = "0";
        String retval = "No se puede cambiar la contraseña";
        if(!usuario.getDePasswordNuevo().equals(dePasswordRepeat)){
              //retval =  "Error - Las nuevas contraseñas no coinciden";
              retval =  "Las nuevas contraseñas no coinciden";
        }else{
            Mensaje msg =  new Mensaje();
            usuarioService.cambiarClave(msg, usuario,false);
            if (msg != null) {
                if (msg.getCoRespuesta().equals("00")){
                    changeIt = "1";
                    //retval =  "Exito -" + msg.getDeRespuesta();
                    retval =  msg.getDeRespuesta();
                } else {
                    //retval =  "Error -" + msg.getCoRespuesta() + " " + msg.getDeRespuesta();
                    retval = msg.getDeRespuesta();
                }
            }
        }


        StringBuilder respuesta = new StringBuilder();
        respuesta.append("{\"coRespuesta\":\"");
        respuesta.append(changeIt);
        respuesta.append("\",\"deRespuesta\":\"");
        respuesta.append(retval);
        respuesta.append("\"}");

        return respuesta.toString();

    }    

}
