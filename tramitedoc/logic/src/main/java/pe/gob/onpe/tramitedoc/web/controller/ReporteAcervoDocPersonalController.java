/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiPersConsulBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.ConsultaEmiDocPerService;
import pe.gob.onpe.tramitedoc.service.ReferencedData;
import pe.gob.onpe.tramitedoc.service.ReporteAcervoDocPerService;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author oti18
 */
@Controller
@RequestMapping("/{version}/srReporteAcervoDocPersonal.do")
public class ReporteAcervoDocPersonalController {
    
    @Autowired
    private ReferencedData referencedData;    
    
    @Autowired
    private ConsultaEmiDocPerService consultaDocPersonalSrv;  
    
    @Autowired
    private ReporteAcervoDocPerService reporteAcervoDocPerService;         
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goInicio")
    public String goInicio(HttpServletRequest request, Model model){
        String annio = Utility.getInstancia().dateToFormatStringYYYY(new Date());
        String codDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "coDep");
        DocumentoEmiPersConsulBean buscarDocPer = new DocumentoEmiPersConsulBean();
        buscarDocPer.setCoAnnio(annio);
        buscarDocPer.setCoDepEmite(codDependencia);
        
        model.addAttribute("buscarConsulDocPersEmiBean",buscarDocPer);
        model.addAttribute("deEstadosList",referencedData.getLstEstadosDocumentoEmi("TDTV_REMITOS"));
        model.addAttribute("deTipoDocumentoList",referencedData.getTipoDocumentoEmiList(codDependencia));        
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute("annioActual", Utility.getInstancia().dateToFormatStringYYYY(new Date()));                
        return "/reporteAcervoDocPersonal/reporteAcervoDocPersonal";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goInicio")
    public String goInicio(DocumentoEmiPersConsulBean buscarDocPer, HttpServletRequest request,  BindingResult result, Model model){
        String mensaje = "NO_OK";

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        buscarDocPer.setCoEmpleado(usuario.getCempCodemp());
        
        List list = null;

        try{
            list = reporteAcervoDocPerService.getDocsPersAcervoDocumentario(buscarDocPer);
        }catch(Exception ex){
            mensaje = ex.getMessage();
        }

        if (list!=null) {
            if (list.size()>=200) {
                model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
            }         
            model.addAttribute("emiDocumAdmList",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        if (mensaje.equals("OK")) {
            return "/reporteAcervoDocPersonal/tblRepAceDocPer";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }        
    }   
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goExportarReporteAcervoDocPers")
    public @ResponseBody String goExportarReporteAcervoDocPers(DocumentoEmiPersConsulBean buscarDocPer,HttpServletRequest request,
            BindingResult result, Model model) throws IOException{
        
        ServletContext sc = request.getSession().getServletContext();
        String coReporte = ServletUtility.getInstancia().loadRequestParameter(request, "pformatRepor");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        
        String codDependencia = usuario.getCoDep();
        String codEmpleado = usuario.getCempCodemp();
        String tipAcceso = usuarioConfigBean.getTiConsulta();
        String anio = ServletUtility.getInstancia().loadRequestParameter(request, "coAnnio");
        
        String rutaReporte=sc.getRealPath("/reports/");
//        String rutaImagen = sc.getRealPath("/reports/logo_onpe.jpg");
        String logo = applicationProperties.getLogoReporteB64();                
        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(logo));
        
        Map parametros = new HashMap();
        parametros.put("P_DE_DEPENDENCIA", usuarioConfigBean.getDeDep());
        parametros.put("P_USER", usuario.getCoUsuario());
        parametros.put("P_LOGO_DIR", bis.available()==0?null:bis);  
        
        buscarDocPer.setCoEmpleado(usuarioConfigBean.getCempCodemp());
        buscarDocPer.setRutaReporteJasper(rutaReporte);
        buscarDocPer.setFormatoReporte(coReporte);
        
        ReporteBean objReporteBean = reporteAcervoDocPerService.getGenerarReporteAcervoDocPer(buscarDocPer, parametros);
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaRemitenteAcervo")
    public String goBuscaRemitenteAcervo(HttpServletRequest request, Model model){
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        model.addAttribute("listaRemitente",referencedData.getListRemitenteAcervo(pnuAnn,usuarioConfigBean));
        return "/modalGeneral/consultaRemitenteRecepConsul";
    }    

    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaFirmadoPorEditAcervo")
    public String goBuscaFirmadoPorEditAcervo(HttpServletRequest request, Model model){
        String pcoDep = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDep");
        model.addAttribute("listaEmpleado",reporteAcervoDocPerService.getPersonalEditAcervoDoc(pcoDep));
        return "/modalGeneral/consultaFirmadoPorEditAcervo";
    }    
}
