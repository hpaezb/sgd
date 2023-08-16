package pe.gob.onpe.tramitedoc.web.controller;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.autentica.service.UsuarioService;
import pe.gob.onpe.libreria.util.Mensaje;
import pe.gob.onpe.libreria.util.ServletUtility;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramiteconv.dao.impl.oracle.MensajesDaoImp;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.UsuarioConfigService;
import pe.gob.onpe.tramitedoc.util.Constantes;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.validator.UsuarioValidator;

@Controller(value = "loginController")
@RequestMapping("/login.do")
@SessionAttributes(value = {"usuario", "usuarioConfig"})

public class LoginController extends GeneralController{

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioConfigService usuarioConfigService;
    
    private static Logger logger=Logger.getLogger(LoginController.class);
    
    public LoginController() {
    }
    
    @RequestMapping(method = RequestMethod.GET)
//  public String doShowForm(HttpServletRequest request, Model model) {    
    public String doShowForm(@RequestParam(value="tid", required = false) String tid, HttpServletRequest request, HttpSession session, Model model) {
        Usuario usuario = new Usuario();
        String codUsuario;
        logger.info("LoginController tid--->" + tid);
        
        if(tid == null){
            logger.info("LoginController Ingreso Acceso Directo--->");            
        }else{
            codUsuario = tid.substring(tid.indexOf("*") + 1);
            logger.info("LoginController Ingreso Intranet--->" + codUsuario);            
            model.addAttribute("vParametroUsuario", codUsuario);            
        }
        
        String vParametro = usuarioService.obtenerValorParam("LOGIN_LDAP");
        /*Hermes 11/06/20 Inicio - Agregar Captcha*/
        String indCaptchaExterno = applicationProperties.getIndicadorCaptchaExterno();
        System.out.println("captcha_externo 1: "+ indCaptchaExterno);
        model.addAttribute("vIndCaptchaExt", indCaptchaExterno);
        /*Hermes 11/06/20 Fin - Agregar Captcha*/
        model.addAttribute("vParametro", vParametro);
        model.addAttribute("usuario",usuario);
        return "login";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String doProcessForm(@ModelAttribute("usuario") Usuario usuario, Model model, BindingResult result, SessionStatus status, HttpServletRequest request, HttpServletResponse response){
        /*Hermes 11/06/20 Inicio - Agregar Captcha*/
        /* [HPB] Inicio 17/07/23 OS-0000786-2023 Mejoras */
        String fechaHoraActual = Utility.getInstancia().dateToFormatString4(new Date());
        /* [HPB] Fin 17/07/23 OS-0000786-2023 Mejoras */
        String captcha=(String) ServletUtility.getInstancia().loadSessionAttribute(request, "CAPTCHA");
        String indCaptchaExterno = applicationProperties.getIndicadorCaptchaExterno();
        System.out.println("captcha_externo 2: "+ indCaptchaExterno);
        System.out.println("CAPTCHA INGRESADO: " + usuario.getCaptcha());
        System.out.println("CAPTCHA GENERADO: " + captcha);  
        /*Hermes 11/06/20 Fin - Agregar Captcha*/
        UsuarioConfigBean usuarioConfig = new UsuarioConfigBean();
    	String destino = "login";
        boolean isAccessOk=true;
        Mensaje msg = new Mensaje();
        
        if (usuario==null) {
            destino = "login";
            isAccessOk = false;
        }
        usuario.setTiIdentificacion(Constantes.TI_IDENTIFICACION);
        String vParametro = usuarioService.obtenerValorParam("LOGIN_LDAP");//Hermes 07/10/19 LDAP
        UsuarioValidator.getInstancia(usuarioService).validate(usuario, result);
        if(captcha==null || ((captcha!=null && !captcha.equals(usuario.getCaptcha())) && (indCaptchaExterno!=null && indCaptchaExterno.equals("1")))){/*Hermes 11/06/20 Inicio - Agregar Captcha*/
            System.out.println("Son DIFERENTES");
            msg.setCoRespuesta("005");
            msg.setDeRespuesta("El código de imagen no coinciden");  
            isOkAndLoadAccess(msg, usuario, result, request);
            usuario.setCaptcha("");
            if(usuario.getContIntentos()>=3)
                destino = "forward:/{version}/LoginValidate.do";
            model.addAttribute("vParametro", vParametro);
            model.addAttribute("vIndCaptchaExt", indCaptchaExterno);
            usuarioService.registraLogAcceso(usuario, "0");             
        }else{/*Hermes 11/06/20 Fin - Agregar Captcha*/
            System.out.println("Son IGUALES");
            if(!result.hasErrors() && isAccessOk){
                //String vParametro = usuarioService.obtenerValorParam("LOGIN_LDAP");//Hermes 07/10/19 LDAP
                //COMENTADO SIS 
                //String vIndicadorDirectActivoUser = usuarioService.obtUsuarioHabDirecActivo(usuario);//Hermes 07/10/19 LDAP
               String vIndicadorDirectActivoUser ="0";
                isAccessOk = false;
                loadUsuarioRemoteAttribs(usuario, request);
                usuarioService.autenticarUsuario(msg, usuario, applicationProperties.getCoAplicativo(), false);
                if(isOkAndLoadAccess(msg, usuario, result, request)){
                    if(vParametro.equals("0")){//VALIDA SGD
                        if(usuario.getEsUsuario().equals("A")){
                            usuarioConfig=usuarioConfigService.getConfig(msg,usuario.getCempCodemp() , usuario.getCoDep());
                            if(isOkAndLoadAccess(msg,result,request)){
                                usuarioConfig.setCoUsuario(usuario.getCoUsuario());                        
                                usuario.setDiasAntesExpiraClave(usuarioService.getNroDiasAntesExpiraClave(usuarioConfig.getDiasExpiracionClave(),usuario.getFeModClave(),usuario.getFeActual()));
                                String expiro=usuarioService.verificarClaveExpiro(usuarioConfig.getDiasExpiracionClave(),usuario.getFeModClave(),usuario.getFeActual(),applicationProperties.getDiasAntesExpira());
                                if(!expiro.equals("EXP")&&usuario.getInClave()!=null&&!usuario.getInClave().equals("EXP")){
                                    expiro="NE";
                                }
                                usuario.setInClave(expiro);
                                if(expiro.equals("NE")){
                                    usuarioConfig.setInEsAdmin(usuario.getInAdmin());
                                    usuario.setCoDep(usuarioConfig.getCoDep());
                                    model.addAttribute("usuarioConfig",usuarioConfig);
                                    model.addAttribute("vParametro", vParametro);/*Hermes 07/10/19 LDAP*/
                                    isAccessOk = true;                        
                                }                        
                            }
                            System.out.println("USUARIO SGD-> " + usuario.getInClave());
                            System.out.println("EXPIRA SGD-> " + usuario.getDiasAntesExpiraClave());
                            System.out.println("INTENTOS SGD-> " + usuario.getContIntentos());                        
                            /*
                            for(UsuarioAcceso usuarioAcceso: usuario.getUsuarioAccesos()){
                                if(StringUtils.hasLength(usuarioAcceso.getCoModulo()) && StringUtils.hasLength(usuarioAcceso.getCoOpcion())){
                                    // tiene accesos asignados
                                    isAccessOk = true;
                                    break;
                                }
                            }
                            */                    
                        }else{
                            usuario.setInClave("NF");
                        }                    
                    }else{//VALIDA LDAP                    
                        if(vIndicadorDirectActivoUser.equals("0")){//AD Habilitado - Login con AD
                            //if(usuario.getEsUsuario().equals("A")){
                                usuarioConfig=usuarioConfigService.getConfig(msg,usuario.getCempCodemp() , usuario.getCoDep());
                                if(isOkAndLoadAccess(msg,result,request)){
                                    usuarioConfig.setCoUsuario(usuario.getCoUsuario());                        
                                    usuario.setDiasAntesExpiraClave(usuarioService.getNroDiasAntesExpiraClave(usuarioConfig.getDiasExpiracionClave(),usuario.getFeModClave(),usuario.getFeActual()));
                                    String expiro=usuarioService.verificarClaveExpiro(usuarioConfig.getDiasExpiracionClave(),usuario.getFeModClave(),usuario.getFeActual(),applicationProperties.getDiasAntesExpira());
                                    if(!expiro.equals("EXP")&&usuario.getInClave()!=null&&!usuario.getInClave().equals("EXP")){
                                        expiro="NE";
                                    }
                                    usuario.setInClave(expiro);
                                    if(expiro.equals("NE")){
                                        usuarioConfig.setInEsAdmin(usuario.getInAdmin());
                                        usuario.setCoDep(usuarioConfig.getCoDep());
                                        model.addAttribute("usuarioConfig",usuarioConfig);
                                        model.addAttribute("vParametro", vParametro);/*Hermes 07/10/19 LDAP*/
                                        isAccessOk = true;                        
                                    }                        
                                }
                                System.out.println("USUARIO LDAP-> " + usuario.getInClave());
                                System.out.println("EXPIRA LDAP-> " + usuario.getDiasAntesExpiraClave());
                                System.out.println("INTENTOS LDAP-> " + usuario.getContIntentos());
                                System.out.println("FECHA LDAP-> " + usuario.getFeModClave());                 
                            //} else{
                                //usuario.setInClave("NF");
                            //}                                                  

                        }else{//AD Habilitado - Login con SGD
                            if(usuario.getEsUsuario().equals("A")){
                                usuarioConfig=usuarioConfigService.getConfig(msg,usuario.getCempCodemp() , usuario.getCoDep());
                                if(isOkAndLoadAccess(msg,result,request)){
                                    usuarioConfig.setCoUsuario(usuario.getCoUsuario());                        
                                    usuario.setDiasAntesExpiraClave(usuarioService.getNroDiasAntesExpiraClave(usuarioConfig.getDiasExpiracionClave(),usuario.getFeModClave(),usuario.getFeActual()));
                                    String expiro=usuarioService.verificarClaveExpiro(usuarioConfig.getDiasExpiracionClave(),usuario.getFeModClave(),usuario.getFeActual(),applicationProperties.getDiasAntesExpira());
                                    if(!expiro.equals("EXP")&&usuario.getInClave()!=null&&!usuario.getInClave().equals("EXP")){
                                        expiro="NE";
                                    }
                                    usuario.setInClave(expiro);
                                    if(expiro.equals("NE")){
                                        usuarioConfig.setInEsAdmin(usuario.getInAdmin());
                                        usuario.setCoDep(usuarioConfig.getCoDep());
                                        model.addAttribute("usuarioConfig",usuarioConfig);
                                        model.addAttribute("vParametro", vParametro);/*Hermes 07/10/19 LDAP*/
                                        isAccessOk = true;                        
                                    }                        
                                }
                                System.out.println("USUARIO DESHABILITADO DEL LOGUEO LDAP-> " + usuario.getInClave());
                                System.out.println("EXPIRA DESHABILITADO DEL LOGUEO LDAP-> " + usuario.getDiasAntesExpiraClave());
                                System.out.println("INTENTOS DESHABILITADO DEL LOGUEO LDAP-> " + usuario.getContIntentos());                              
                            }else{
                                usuario.setInClave("NF");
                            }   
                        }
                    }
                }
                int cont =usuario.getContIntentos()+1;
                usuario.setContIntentos(cont);
                if(isAccessOk){
                    usuarioService.registraLogAcceso(usuario, "1");                                
                    usuario.setSessionId(ServletUtility.getInstancia().loadSessionId(request));
                    /* [HPB] Inicio 17/07/23 OS-0000786-2023 Mejoras */
                    model.addAttribute("fechaHoraActual", fechaHoraActual);
                    /* [HPB] Fin 17/07/23 OS-0000786-2023 Mejoras */
                    destino = "forward:/{version}/mainpanel.do";
                    //destino = "forward:mainpanel";
                }else{
                    if(usuario.getContIntentos()>=3)
                        destino = "forward:/{version}/LoginValidate.do";
                    model.addAttribute("vParametro", vParametro);/*Hermes 07/10/19 LDAP*/
                    model.addAttribute("vIndCaptchaExt", indCaptchaExterno);/*Hermes 11/06/20 - Agregar Captcha*/
                    usuarioService.registraLogAcceso(usuario, "0");                
                }
            }            
        }    
        return destino;
    }

    private boolean isOkAndLoadAccess(Mensaje msg, Usuario usuario, BindingResult result,  HttpServletRequest request){
        if (msg.getCoRespuesta().equals("00")) {
            // acceso valido a ok
            usuarioService.obtienePermisos(msg, usuario,applicationProperties.getCoAplicativo());
            return true;
        } else {
            // no accede y imprime lel mensaje
            printErrorMessaje(result, msg);
            return false;
        }
    }

    private boolean isOkAndLoadAccess(Mensaje msg, BindingResult result,  HttpServletRequest request){
        if (msg.getCoRespuesta().equals("00")) {
            // acceso valido a ok
            return true;
        } else {
            // no accede y imprime lel mensaje
            printErrorMessaje(result, msg);
            return false;
        }
    }   
        
    
}
