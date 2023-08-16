package pe.gob.onpe.tramitedoc.web.controller;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.libreria.util.ServletUtility;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.BuscarAccionLog;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.ActionLogService;
import pe.gob.onpe.tramitedoc.service.ReferencedData;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author hpaez
 */
@Controller
@RequestMapping("/{version}/srAction.do")
public class ActionController {
    
    @Autowired
    private ActionLogService actionLogService;
    
    @Autowired
    private ReferencedData referencedData; 
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goInicio")
    public String goInicio(HttpServletRequest request, Model model){
        String sCoAnnio = Utility.getInstancia().dateToFormatStringYYYY(new Date());
        BuscarAccionLog buscarAccionLog = new BuscarAccionLog();
        
        buscarAccionLog.setsCoAnnio(sCoAnnio);
        model.addAttribute(buscarAccionLog);
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute("deTipoDocumentoList",referencedData.getTipoDocListAccion());        
        
        try{
            UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "/action/actionDocumento";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goInicio")
    public String goInicio(BuscarAccionLog buscarAccionLog, HttpServletRequest request,  BindingResult result, Model model){
        String mensaje = "NO_OK";
        List list = null;
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        buscarAccionLog.setCoDependencia(usuario.getCoDep());
        
        try{
            list = actionLogService.getLstDocumentosLog(buscarAccionLog);
        }catch(Exception ex){
            mensaje = ex.getMessage();
        }

        if (list!=null) {
            if (list.size()>=100) {
                model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
            }
            model.addAttribute("accionDocumList",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        if (mensaje.equals("OK")) {
            return "/action/actionDocumentoList";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    } 
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goExportarArchivoLista")
    public @ResponseBody String goExportarArchivoLista(BuscarAccionLog buscarAccionLog,HttpServletRequest request,
            BindingResult result, Model model) throws JRException, FileNotFoundException, IOException{           
        ServletContext sc = request.getSession().getServletContext();
        
        String coReporte = ServletUtility.getInstancia().loadRequestParameter(request, "pformatRepor");

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String codDependencia = usuario.getCoDep();
        String codEmpleado = usuario.getCempCodemp();
        String tipAcceso = usuarioConfigBean.getTiConsulta();
        String anio = ServletUtility.getInstancia().loadRequestParameter(request, "sCoAnnio");
        
        String rutaReporte=sc.getRealPath("/reports/");
        String logo = applicationProperties.getLogoReporteB64();                
        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(logo));
        
        Map parametros = new HashMap();
        parametros.put("P_DE_DEPENDENCIA", usuarioConfigBean.getDeDep());
        parametros.put("P_USER", usuario.getCoUsuario());
        parametros.put("P_LOGO_DIR", bis.available()==0?null:bis);  
        
        buscarAccionLog.setCoDependencia(codDependencia);
        buscarAccionLog.setCoEmpleado(codEmpleado);
        buscarAccionLog.setTiAcceso(tipAcceso);
        buscarAccionLog.setCoAnnio(anio);    
        buscarAccionLog.setRutaReporteJasper(rutaReporte);
        buscarAccionLog.setFormatoReporte(coReporte);
        
        ReporteBean objReporteBean = actionLogService.getGenerarReporte(buscarAccionLog, parametros);
        if(bis!=null)bis.close();
        StringBuilder retval = new StringBuilder();
        retval.append("{\"coRespuesta\":\"");        
        retval.append(objReporteBean.getcoRespuesta());
        retval.append("\",\"deRespuesta\":\"");        
        retval.append(objReporteBean.getdeRespuesta());
        retval.append("\",\"noUrl\":\"");
        retval.append(objReporteBean.getnoUrl());
        retval.append("\",\"noDoc\":\"");
        retval.append(objReporteBean.getnoDoc());
        retval.append("\"}");
        
        return retval.toString();
    } 
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goValidaCantDocLog", produces = "text/plain; charset=utf-8")
    public @ResponseBody String goValidaCantDocLog(HttpServletRequest request, Model model) {
        String coFiltro = ServletUtility.getInstancia().loadRequestParameter(request, "coFiltro");
        String coRespuesta = "0";
        String deRespuesta = "";
        String[] partes = coFiltro.split(",");
        String tipoDoc = partes[0];
        String numeroDoc = partes[1];
        System.out.println("coFiltro: "+coFiltro+" tipoDoc: "+tipoDoc+" numeroDoc: "+numeroDoc);
        try {
            int existe = actionLogService.getCantidadDocEncontrados(tipoDoc, numeroDoc);
            if (existe > 1) {
                coRespuesta = "1";                
                deRespuesta = "Buscar: Se encontraron varios registros. Favor de ingresar mas filtro de busqueda.";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        StringBuilder retval = new StringBuilder();
        retval.append("{\"coRespuesta\":\"");
        retval.append(coRespuesta);
        retval.append("\",\"deRespuesta\":\"");
        retval.append(deRespuesta);
        retval.append("\"}");
        return retval.toString();
    }    
}
