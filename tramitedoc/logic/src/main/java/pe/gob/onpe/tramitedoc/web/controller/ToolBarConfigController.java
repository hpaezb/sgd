/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.web.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pe.gob.onpe.libreria.util.ServletUtility;
import pe.gob.onpe.tramitedoc.bean.ToolBarConfigBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author WCutipa
 */
@Controller
@RequestMapping("/{version}/srToolBar.do")
public class ToolBarConfigController {
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goToolEmisionAdm")
    public String goToolEmisionAdm(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        ToolBarConfigBean toolBarConfig = new ToolBarConfigBean();
        String pcoEstado = ServletUtility.getInstancia().loadRequestParameter(request, "pcoEstado");
        String pcoTipoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoTipoDoc");
        String pcoFormatoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoFormato");
        
        if(pcoEstado!=null){
            toolBarConfig.setCoEstado(pcoEstado);
        }else{
            toolBarConfig.setCoEstado("0");
        }
        
        // Para el cado de Proveidos y Hoja de Envio
        if(pcoTipoDoc!=null){
            toolBarConfig.setCoTipoDoc(pcoTipoDoc);
        }else{
            toolBarConfig.setCoTipoDoc("1");
        }

        if(pcoFormatoDoc!=null){
            if(pcoFormatoDoc.equals("NO")){
                toolBarConfig.setCoFormatoDoc("0");
            }else if(pcoFormatoDoc.equals("PDF")){
                toolBarConfig.setCoFormatoDoc("2");
            }else{
                toolBarConfig.setCoFormatoDoc("1");
            }
        }else{
            toolBarConfig.setCoFormatoDoc("0");
        }
        
        model.addAttribute("toolBarConfig",toolBarConfig);
        return "/toolbar/toolbarDocEmiEdit";
    }

    @RequestMapping(method = RequestMethod.GET, params = "accion=goToolEmisionPersonal")
    public String goToolEmisionPersonal(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        ToolBarConfigBean toolBarConfig = new ToolBarConfigBean();
        String pcoEstado = ServletUtility.getInstancia().loadRequestParameter(request, "pcoEstado");
        String pcoTipoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoTipoDoc");
        String pcoFormatoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoFormato");
        
        if(pcoEstado!=null){
            toolBarConfig.setCoEstado(pcoEstado);
        }else{
            toolBarConfig.setCoEstado("0");
        }
        
        // Para el cado de Proveidos y Hoja de Envio
        if(pcoTipoDoc!=null){
            toolBarConfig.setCoTipoDoc(pcoTipoDoc);
        }else{
            toolBarConfig.setCoTipoDoc("1");
        }

        if(pcoFormatoDoc!=null){
            if(pcoFormatoDoc.equals("NO")){
                toolBarConfig.setCoFormatoDoc("0");
            }else if(pcoFormatoDoc.equals("PDF")){
                toolBarConfig.setCoFormatoDoc("2");
            }else{
                toolBarConfig.setCoFormatoDoc("1");
            }
        }else{
            toolBarConfig.setCoFormatoDoc("0");
        }
        
        model.addAttribute("toolBarConfig",toolBarConfig);
        return "/toolbar/toolbarDocEmiPersonalEdit";
    }    
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goToolRecepcionAdm")
    public String goToolRecepcionAdm(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        ToolBarConfigBean toolBarConfig = new ToolBarConfigBean();
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String pcoEstado = ServletUtility.getInstancia().loadRequestParameter(request, "pcoEstado");
        toolBarConfig.setInOpcionMP(usuarioConfigBean.getInMesaPartes());
        if(pcoEstado!=null){
            toolBarConfig.setCoEstado(pcoEstado);
        }else{
            toolBarConfig.setCoEstado("0");
        }
        
        model.addAttribute("toolBarConfig",toolBarConfig);
        return "/toolbar/toolbarDocRecEdit";
    }

    @RequestMapping(method = RequestMethod.GET, params = "accion=goToolEmisionAlta")
    public String goToolEmisionAlta(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        ToolBarConfigBean toolBarConfig = new ToolBarConfigBean();
        String pcoEstado = ServletUtility.getInstancia().loadRequestParameter(request, "pcoEstado");
        String pcoTipoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoTipoDoc");
        String pcoFormatoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoFormato");
        
        if(pcoEstado!=null){
            toolBarConfig.setCoEstado(pcoEstado);
        }else{
            toolBarConfig.setCoEstado("0");
        }
        
        // Para el cado de Proveidos y Hoja de Envio
        if(pcoTipoDoc!=null){
            toolBarConfig.setCoTipoDoc(pcoTipoDoc);
        }else{
            toolBarConfig.setCoTipoDoc("1");
        }

        if(pcoFormatoDoc!=null){
            if(pcoFormatoDoc.equals("NO")){
                toolBarConfig.setCoFormatoDoc("0");
            }else if(pcoFormatoDoc.equals("PDF")){
                toolBarConfig.setCoFormatoDoc("2");
            }else{
                toolBarConfig.setCoFormatoDoc("1");
            }
        }else{
            toolBarConfig.setCoFormatoDoc("0");
        }
        
        model.addAttribute("toolBarConfig",toolBarConfig);
        return "/toolbar/toolbarDocEmiAltaEdit";
    }    
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goToolEmisionDocExt")
    public String goToolEmisionDocExt(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        ToolBarConfigBean toolBarConfig = new ToolBarConfigBean();
        String pcoEstado = ServletUtility.getInstancia().loadRequestParameter(request, "pcoEstado");
        /* [HPB] Inicio 26/09/22 OS-0000768-2022 */
        String pcoOrigen = ServletUtility.getInstancia().loadRequestParameter(request, "pcoOrigen");
        toolBarConfig.setCoOrigen(pcoOrigen);
        /* [HPB] Fin 26/09/22 OS-0000768-2022 */
        toolBarConfig.setCoEstado(pcoEstado);
        model.addAttribute("toolBarConfig",toolBarConfig);
        return "/toolbar/toolbarDocExtEdit";
    }
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goToolDocVoBo")
    public String goToolDocVoBo(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        ToolBarConfigBean toolBarConfig = new ToolBarConfigBean();
        String pcoEstado = ServletUtility.getInstancia().loadRequestParameter(request, "pcoEstado");
        String pcoTipoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoTipoDoc");
        String pcoFormatoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoFormato");
        
        if(pcoEstado!=null){
            toolBarConfig.setCoEstado(pcoEstado);
        }else{
            toolBarConfig.setCoEstado("1");
        }
        
        // Para el cado de Proveidos y Hoja de Envio
        if(pcoTipoDoc!=null){
            toolBarConfig.setCoTipoDoc(pcoTipoDoc);
        }else{
            toolBarConfig.setCoTipoDoc("1");
        }

        if(pcoFormatoDoc!=null){
            if(pcoFormatoDoc.equals("NO")){
                toolBarConfig.setCoFormatoDoc("0");
            }else if(pcoFormatoDoc.equals("PDF")){
                toolBarConfig.setCoFormatoDoc("2");
            }else{
                toolBarConfig.setCoFormatoDoc("1");
            }
        }else{
            toolBarConfig.setCoFormatoDoc("0");
        }
        
        model.addAttribute("toolBarConfig",toolBarConfig);
        return "/toolbar/toolbarDocVoBoEdit";
    }    
}
