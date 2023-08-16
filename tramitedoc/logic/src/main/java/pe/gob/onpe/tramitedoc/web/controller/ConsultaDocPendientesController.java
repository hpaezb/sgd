package pe.gob.onpe.tramitedoc.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoPendienteConsulBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.ConsultaPendienteDocService;
import pe.gob.onpe.tramitedoc.service.DocumentoExtRecepService;
import pe.gob.onpe.tramitedoc.service.ReferencedData;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author hpaez
 */
@Controller
@RequestMapping("/{version}/srConsultaDocPendientes.do")
public class ConsultaDocPendientesController {
  
    @Autowired
    private ReferencedData referencedData; 
    
    @Autowired
    private DocumentoExtRecepService documentoExtRecepService;
    
    @Autowired
    private ConsultaPendienteDocService consultaPendienteDocService;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goInicio")
    public String goInicio(HttpServletRequest request, Model model){
        String codDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "coDep");
        String pEstado = ServletUtility.getInstancia().loadRequestParameter(request, "estadoDoc");

        BuscarDocumentoPendienteConsulBean buscarDocumentoPendienteConsulBean = new BuscarDocumentoPendienteConsulBean();
        String sCoAnnio = Utility.getInstancia().dateToFormatStringYYYY(new Date());
        buscarDocumentoPendienteConsulBean.setsCoAnnio(sCoAnnio);
        buscarDocumentoPendienteConsulBean.setsCoAnnioBus(sCoAnnio);
        buscarDocumentoPendienteConsulBean.setsEstadoDoc(pEstado);
        buscarDocumentoPendienteConsulBean.setsCoDependencia(codDependencia);
        
        model.addAttribute(buscarDocumentoPendienteConsulBean);
        model.addAttribute("deEstadosList",referencedData.getLstEstadosDocumentoPendientes("TDTV_DESTINOS"));
        model.addAttribute("deTipoDocumentoList",referencedData.getTipoDocumentoEmiList(codDependencia));        
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute("annioActual", Utility.getInstancia().dateToFormatStringYYYY(new Date()));   
        model.addAttribute("deEmisorList",referencedData.getLstEmisorDocExtRecep());
        
        return "/consultaDocPendiente/consultaDocPendiente";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goInicio")
    public String goInicio(BuscarDocumentoPendienteConsulBean buscarDocumentoPendienteConsulBean, HttpServletRequest request,  BindingResult result, Model model){
        
        String mensaje = "NO_OK";
        
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        buscarDocumentoPendienteConsulBean.setsCoDependencia(usuario.getCoDep());
        buscarDocumentoPendienteConsulBean.setsCoEmpleado(usuario.getCempCodemp());
        buscarDocumentoPendienteConsulBean.setsTiAcceso(usuarioConfigBean.getTiConsulta());
        
        List list = null;

        try{
                list = consultaPendienteDocService.getDocumentosBuscaPendientes(buscarDocumentoPendienteConsulBean);
        }catch(Exception ex){
            mensaje = ex.getMessage();
        }

        if (list!=null) {
            if (list.size()>=300) {
                model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
            }
            
            model.addAttribute("consulDocPendientesList",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        if (mensaje.equals("OK")) {
            return "/consultaDocPendiente/tblDocPendientes";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaProveedorBus")
    public String goBuscaDestProveedorBus(HttpServletRequest request, Model model){
        String prazonSocial = ServletUtility.getInstancia().loadRequestParameter(request, "prazonSocial");
        model.addAttribute("lsDestProveedor",documentoExtRecepService.getLstProveedores(prazonSocial));
        model.addAttribute("iniFuncionParm","6");
        return "/modalGeneral/consultaDestProveedorBus";
    }
    
     @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadano")
     public String goBuscaCiudadano(HttpServletRequest request, Model model){
        String sDescCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sDescCiudadano");
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sDescCiudadano));
        model.addAttribute("iniFuncionParm","8");
        return "/modalGeneral/consultaDestCiudadano";
     }
     
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaOtroOrigen")
    public String goBuscaOtroOrigen(HttpServletRequest request, Model model){        
        String pdesOtroOri = ServletUtility.getInstancia().loadRequestParameter(request, "pdesOtroOri");         
        model.addAttribute("lsDestOtroOrigen",documentoExtRecepService.getLstOtrosOrigenes(pdesOtroOri));
        model.addAttribute("iniFuncionParm","6");
        return "/modalGeneral/consultaDestOtroOrigenBus";        
    } 

    @RequestMapping(method = RequestMethod.POST, params = "accion=goExportarArchivoLista")
    public @ResponseBody String goExportarArchivoLista(BuscarDocumentoPendienteConsulBean buscarDocumentoPendienteConsulBean,HttpServletRequest request,
        BindingResult result, Model model) throws IOException{
        ServletContext sc = request.getSession().getServletContext();
        
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);        
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        
        String codDependencia = usuario.getCoDep();
        String codEmpleado = usuario.getCempCodemp();
        String tipAcceso = usuarioConfigBean.getTiConsulta();
        String anio = ServletUtility.getInstancia().loadRequestParameter(request, "sCoAnnio");
        String rutaReporte=sc.getRealPath("/reports/");
        String logo = applicationProperties.getLogoReporteB64();                
        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(logo));
        
        String coReporte = ServletUtility.getInstancia().loadRequestParameter(request, "pformatRepor");
        
        Map parametros = new HashMap();
        parametros.put("P_DE_DEPENDENCIA", usuarioConfigBean.getDeDep());
        parametros.put("P_USER", usuario.getCoUsuario());
        parametros.put("P_LOGO_DIR", bis.available()==0?null:bis);  
        
        buscarDocumentoPendienteConsulBean.setsCoDependencia(codDependencia);
        buscarDocumentoPendienteConsulBean.setsCoEmpleado(codEmpleado);
        buscarDocumentoPendienteConsulBean.setsTiAcceso(tipAcceso);
        buscarDocumentoPendienteConsulBean.setsCoAnnio(anio);          
        buscarDocumentoPendienteConsulBean.setRutaReporteJasper(rutaReporte);
        buscarDocumentoPendienteConsulBean.setFormatoReporte(coReporte);
        
        ReporteBean objReporteBean = consultaPendienteDocService.getGenerarReporte(buscarDocumentoPendienteConsulBean, parametros);
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
}
