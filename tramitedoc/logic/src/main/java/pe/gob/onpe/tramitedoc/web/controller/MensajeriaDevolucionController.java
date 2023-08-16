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
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoDevolucionMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.DocumentoDevolucionMensajeriaService;
import pe.gob.onpe.tramitedoc.service.MensajesData;
import pe.gob.onpe.tramitedoc.service.ReferencedData;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author hpaez
 */
@Controller
@RequestMapping("/{version}/srMensajeriaDevolucion.do")
public class MensajeriaDevolucionController {
  
    @Autowired
    private ReferencedData referencedData;
    
    @Autowired
    private MensajesData mensajesData;
    
    @Autowired
    private DocumentoDevolucionMensajeriaService documentoDevolucionMensajeriaService;
    
    @Autowired
    private ApplicationProperties applicationProperties;
      
    @RequestMapping(method = RequestMethod.GET, params = "accion=goInicio")
    public String goInicio(HttpServletRequest request, Model model){
        String codDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "coDep");
        String sCoAnnio = Utility.getInstancia().dateToFormatStringYYYY(new Date());
        
        BuscarDocumentoDevolucionMensajeriaBean buscarDocumentoDevolucionMensajeriaBean = new BuscarDocumentoDevolucionMensajeriaBean();   
        buscarDocumentoDevolucionMensajeriaBean.setCoEstadoDoc("6");
        buscarDocumentoDevolucionMensajeriaBean.setCoAnnio(sCoAnnio);
        
        model.addAttribute("deTipoDocumentoList",referencedData.getTipoDocumentoEmiList(codDependencia));           
        model.addAttribute("deEstadosList",referencedData.getLstEstadoCargoEntrega("ENVIO_DOCUMENTO_MSJ"));
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute("annioActual", Utility.getInstancia().dateToFormatStringYYYY(new Date())); 
        model.addAttribute("deDependenciaList",mensajesData.getListOficina());   
        model.addAttribute("deTipoEnvList",mensajesData.getListTipoEnvMsj("RE_ENV_MSJ_MENS"));
        model.addAttribute(buscarDocumentoDevolucionMensajeriaBean);
        return "/mpMensajeriaDevolucion/buscarMensajeriaDevolucion";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goInicio")
    public String goInicio(BuscarDocumentoDevolucionMensajeriaBean buscarDocumentoDevolucionMensajeriaBean, HttpServletRequest request,  BindingResult result, Model model){
            String mensaje = "NO_OK";
         List list = null;
        try{
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String codDependencia = usuarioConfigBean.getCoDep();
        String codEmpleado = usuarioConfigBean.getCempCodemp();
        String tipAcceso = usuarioConfigBean.getTiAccesoMp();
//        buscarDocumentoDevolucionMensajeriaBean.setCoDependencia(codDependencia);
//        buscarDocumentoDevolucionMensajeriaBean.setCoEmpleado(codEmpleado);
//        buscarDocumentoDevolucionMensajeriaBean.setTiAcceso(tipAcceso);
//        buscarDocumentoDevolucionMensajeriaBean.setCoDepEmi(usuarioConfigBean.getCoDepMp());
//        buscarDocumentoDevolucionMensajeriaBean.setInMesaPartes(usuarioConfigBean.getInMesaPartes());
         
            list = documentoDevolucionMensajeriaService.getDocumentoDevolucionMensajeria(buscarDocumentoDevolucionMensajeriaBean);
        }catch(Exception ex){
            mensaje = ex.getMessage();
        }

        if (list!=null) {
            if (list.size()>=50) {
                model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
            }
            model.addAttribute("docMensajDevolucionLst",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        if (mensaje.equals("OK")) {
            return "/mpMensajeriaDevolucion/tblDocMensajeriaDevolucion";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goExportarArchivoLista")
    public @ResponseBody String goExportarArchivoLista(BuscarDocumentoDevolucionMensajeriaBean buscarDocumentoDevolucionMensajeriaBean,HttpServletRequest request,
        BindingResult result, Model model) throws IOException{
        ServletContext sc = request.getSession().getServletContext();
        
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);        
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        
        String codDependencia = usuario.getCoDep();
        String codEmpleado = usuario.getCempCodemp();
        String tipAcceso = usuarioConfigBean.getTiConsulta();
        String anio = ServletUtility.getInstancia().loadRequestParameter(request, "coAnnio");
        String rutaReporte=sc.getRealPath("/reports/");
        String logo = applicationProperties.getLogoReporteB64();                
        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(logo));
        
        String coReporte = ServletUtility.getInstancia().loadRequestParameter(request, "pformatRepor");
        
        Map parametros = new HashMap();
        parametros.put("P_DE_DEPENDENCIA", usuarioConfigBean.getDeDep());
        parametros.put("P_USER", usuario.getCoUsuario());
        parametros.put("P_LOGO_DIR", bis.available()==0?null:bis);  
        
        buscarDocumentoDevolucionMensajeriaBean.setCoDependencia(codDependencia);
        buscarDocumentoDevolucionMensajeriaBean.setCoEmpleado(codEmpleado);
        buscarDocumentoDevolucionMensajeriaBean.setTiAcceso(tipAcceso);
        buscarDocumentoDevolucionMensajeriaBean.setCoAnnio(anio);  
        
        buscarDocumentoDevolucionMensajeriaBean.setCoDepEmi(usuarioConfigBean.getCoDepMp());
        buscarDocumentoDevolucionMensajeriaBean.setInMesaPartes(usuarioConfigBean.getInMesaPartes());

        buscarDocumentoDevolucionMensajeriaBean.setRutaReporteJasper(rutaReporte);
        buscarDocumentoDevolucionMensajeriaBean.setFormatoReporte(coReporte);
        
        ReporteBean objReporteBean = documentoDevolucionMensajeriaService.getGenerarReporte(buscarDocumentoDevolucionMensajeriaBean, parametros);
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
