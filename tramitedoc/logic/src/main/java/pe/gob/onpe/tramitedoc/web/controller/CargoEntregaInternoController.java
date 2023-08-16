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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.libreria.exception.validarDatoException;
import pe.gob.onpe.libreria.util.ServletUtility;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.CargoEntregaBean;
import pe.gob.onpe.tramitedoc.bean.DocPedienteEntregaBean;
import pe.gob.onpe.tramitedoc.bean.GuiaMesaPartesBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.TrxGeneraGuiaMpBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.CargoEntregaInternoService;
import pe.gob.onpe.tramitedoc.service.ReferencedData;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author consultor_ogti_80
 */
@Controller
@RequestMapping("/{version}/srCargoEntregaInterno.do")
public class CargoEntregaInternoController {
 
    @Autowired
    private ReferencedData referencedData;
     
    @Autowired
    private CargoEntregaInternoService cargoEntregaInternoService;  
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goInicioCargo")
    public String goInicioCargo(HttpServletRequest request, Model model){
        String coLocal = ServletUtility.getInstancia().loadRequestParameter(request, "coLoc");
        String coDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "coDep");
        
        String annio = Utility.getInstancia().dateToFormatStringYYYY(new Date());
        CargoEntregaBean cargoEntregaBean = new CargoEntregaBean();
        cargoEntregaBean.setNuAnnGuia(annio);
        cargoEntregaBean.setEstadoGuiaMp("0");//generado
        cargoEntregaBean.setCoLocDes(coLocal);
        cargoEntregaBean.setCoDepOri(coDependencia);
        model.addAttribute("buscarCargoEntregaBean",cargoEntregaBean);        
        
        model.addAttribute("deLocalesList",referencedData.getLsLocales());        
        model.addAttribute("deEstadosList",referencedData.getLstEstadoCargoEntrega("TDTC_GUIA_MP"));
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute("annioActual", Utility.getInstancia().dateToFormatStringYYYY(new Date()));        
        return "/mpCargoEntregaInterno/buscaCargoEntregaInterno";    
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goInicioCargo")
    public String goInicioCargo(CargoEntregaBean cargo, HttpServletRequest request,  BindingResult result, Model model){
        String mensaje = "NO_OK";

        //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        cargo.setCoDepOri(usuarioConfigBean.getCoDep());
        //cargo.setCoLocOri(usuarioConfigBean.getCoLocal());

        List list=null;

        try{
                list = cargoEntregaInternoService.getCargosEntregaInterno(cargo);
        }catch(Exception ex){
            mensaje = ex.getMessage();
        }

        if (list!=null) {
            if (list.size()>=50) {
                model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
            }
            model.addAttribute("cargoEntregaLs",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        if (mensaje.equals("OK")) {
            return "/mpCargoEntregaInterno/tblCargoEntregaInterno";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }    
    }
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goInicioNewDocPendienteEntregaInterno")
    public String goInicioNewDocPendienteEntregaInterno(HttpServletRequest request, Model model){
        String coLocal = ServletUtility.getInstancia().loadRequestParameter(request, "coLoc");
        String codDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "coDep");
        
        String annio = Utility.getInstancia().dateToFormatStringYYYY(new Date());
        DocPedienteEntregaBean DocPendienteEntrega = new DocPedienteEntregaBean();
        DocPendienteEntrega.setNuAnn(annio);
        DocPendienteEntrega.setCoLocDes(coLocal);
        model.addAttribute("buscarDocPendienteEntregaBean",DocPendienteEntrega);        
        
        model.addAttribute("deLocalesList",referencedData.getLsLocales());        
        model.addAttribute("deTipoDocumentoList",referencedData.getTipoDocumentoEmiList(codDependencia));        
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute("annioActual", Utility.getInstancia().dateToFormatStringYYYY(new Date()));        
        return "/mpCargoEntregaInterno/buscarDocPendEntregaInterno";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goInicioNewDocPendienteEntregaInterno")
    public String goInicioNewDocPendienteEntregaInterno(DocPedienteEntregaBean doc, HttpServletRequest request,  BindingResult result, Model model){
        String mensaje = "NO_OK";

        //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        doc.setCoDepEmi(usuarioConfigBean.getCoDep());
        //doc.setCoDepEmi(usuarioConfigBean.getCoDepMp());
        doc.setCoLocEmi(usuarioConfigBean.getCoLocal());
        String tipAcceso = usuarioConfigBean.getTiAcceso();
        doc.setTiAcceso(tipAcceso);

        List list=null;
        System.out.println("doc 1-->"+doc.getCoLocDes());
        System.out.println("doc 2-->"+doc.getCoDepEmi());
        System.out.println("doc 3-->"+doc.getNuExpediente());
        String[] nuExpedientes = doc.getNuExpediente().split(",");
        String nuExpedienteTodos = "";
        //20230000006, 20230000007, 20230000008, 20230000009,OGTI0020220000049
        for (int i=0; i<nuExpedientes.length; i++){
            nuExpedienteTodos = nuExpedienteTodos+"'".concat(nuExpedientes[i].trim())+"',";
        }
        System.out.println("nuExpedienteTodos 1--> "+ nuExpedienteTodos);
        nuExpedienteTodos = nuExpedienteTodos.substring(0, nuExpedienteTodos.length()-1);
        System.out.println("nuExpedienteTodos 2--> "+ nuExpedienteTodos);
        doc.setNuExpediente(nuExpedienteTodos);
        try{
            list = cargoEntregaInternoService.getDocsPendienteEntregaInterno(doc);
        }catch(Exception ex){
            mensaje = ex.getMessage();
        }
        System.out.println("list.size()--> "+ list.size());
        if (list!=null) {
            if (list.size()>=50) {
                model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
            }
            model.addAttribute("docPendEntregaLs",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        if (mensaje.equals("OK")) {
            return "/mpCargoEntregaInterno/tblDocPendEntregaInterno";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goGeneraCargoEntregaInterno")
    public String goGeneraCargoEntregaInterno(@RequestBody List<DocPedienteEntregaBean> list,HttpServletRequest request, Model model){
        String vResult="";
        String coRespuesta;
        StringBuilder retval = new StringBuilder();
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        try {
            GuiaMesaPartesBean guia=cargoEntregaInternoService.getGuiaMpInterno(list);
            if(guia!=null){
                String annio = Utility.getInstancia().dateToFormatStringYYYY(new Date());
                String fechaHoraActual = Utility.getInstancia().dateToFormatString3(new Date());
                guia.setNuAnn(annio);
                guia.setCoDepOri(usuario.getCoDep());
                guia.setCoLocOri(usuarioConfigBean.getCoLocal());
                guia.setDeDepOri(usuarioConfigBean.getDeDep());
                guia.setDeLocOri(usuarioConfigBean.getDeLocal());
                guia.setDeEstadoGuia("GENERADO");
                guia.setFeGuiMp(fechaHoraActual);

                list=cargoEntregaInternoService.getDocsPendienteEntregaInterno(list);
                model.addAttribute("lDetalleGuia",list);
                model.addAttribute("tblDetCarga","0");//cargar tabla detalle
                model.addAttribute(guia); 
                vResult="/mpCargoEntregaInterno/cargoEntregaInterno";
            }else{
                coRespuesta = "0";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"deRespuesta\":\"");
                retval.append("Seleccionar registros de una misma dependencia.");                
                retval.append("\"}"); 
                model.addAttribute("pMensaje", retval.toString());
                vResult = "respuesta";
            }              
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vResult;
    } 

    @RequestMapping(method = RequestMethod.POST, params = "accion=goGrabarGuiaMpInterno",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goGrabarGuiaMpInterno(@RequestBody TrxGeneraGuiaMpBean trxGuia,HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String coRespuesta="0";
        String deRespuesta;        
        //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        trxGuia.setCoUsuario(usuarioConfigBean.getCoUsuario());
        String resultJsonStr="";
        try {
            mensaje = cargoEntregaInternoService.grabarCargoEntregaInterno(trxGuia);
            if(mensaje.equals("OK")){
              coRespuesta = "1"; 
              resultJsonStr=","+cargoEntregaInternoService.getJsonRptGrabarCargoEntregaInterno(trxGuia);
            }
        }catch(validarDatoException e){
           mensaje=(e.valorMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        deRespuesta = mensaje;
        
        StringBuilder retval = new StringBuilder();
        retval.append("{\"coRespuesta\":\"");
        retval.append(coRespuesta);
        retval.append("\",\"deRespuesta\":\"");
        retval.append(deRespuesta);
        retval.append("\"");
        retval.append(resultJsonStr);
        retval.append("}");

        return retval.toString();               
    }  
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goExportarArchivoListaInterno")
    public @ResponseBody String goExportarArchivoListaInterno(HttpServletRequest request, Model model) throws IOException {           
        ServletContext sc = request.getSession().getServletContext();
        
        String pnuAnnGuia = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnnGuia");
        String pnuGuia = ServletUtility.getInstancia().loadRequestParameter(request, "pnuGuia"); 

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
                
        String rutaReporte = sc.getRealPath("/reports/");
//        String rutaImagen = sc.getRealPath("/reports/logo_onpe.jpg");
        String logo = applicationProperties.getLogoReporteB64();                
        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(logo));
        
        Map parametros = new HashMap();
        parametros.put("P_DE_DEPENDENCIA", usuarioConfigBean.getDeDep());
        parametros.put("P_USER", usuario.getCoUsuario());
        parametros.put("P_LOGO_DIR", bis.available()==0?null:bis);
        
        CargoEntregaBean cargo = new CargoEntregaBean();
        cargo.setNuAnnGuia(pnuAnnGuia);
        cargo.setNuGuia(pnuGuia);        
        cargo.setRutaReporteJasper(rutaReporte);
        
        ReporteBean objReporteBean = cargoEntregaInternoService.getGenerarReporteInterno(parametros, cargo);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAnularGuiaMpInterno")
    public @ResponseBody String goAnularGuiaMpInterno(GuiaMesaPartesBean guia, HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
       guia.setCoUseMod(usuarioConfigBean.getCoUsuario());
       try{
          mensaje = cargoEntregaInternoService.anularGuiaMpInterno(guia);
       }catch(Exception e){
           mensaje = e.getMessage();
       }
       
       vRespuesta = mensaje;
       if (mensaje.equals("OK")) {
            coRespuesta = "1";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\"}");
       } else {
            coRespuesta = "0";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append(vRespuesta);                            
            retval.append("\"}");
       }
       return retval.toString();
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goEditCargoEntregaInterno")
    public String goEditCargoEntregaInterno(HttpServletRequest request, Model model){
       String pnuAnnGuia = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnnGuia");
       String pnuGuia = ServletUtility.getInstancia().loadRequestParameter(request, "pnuGuia");       
       List<DocPedienteEntregaBean> list;
       try {
            GuiaMesaPartesBean guia=cargoEntregaInternoService.getGuiaMpInterno(pnuAnnGuia, pnuGuia);
            list=cargoEntregaInternoService.getDetalleGuiaMpInterno(pnuAnnGuia, pnuGuia);
            model.addAttribute("lDetalleGuia",list);
            model.addAttribute("tblDetCarga","1");//cargar tabla detalle edit
            model.addAttribute(guia);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "/mpCargoEntregaInterno/cargoEntregaInterno";
    } 

    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDependenciaInterno")
    public String goBuscaDependenciaInterno(HttpServletRequest request, Model model){
        String pcoLocDes = ServletUtility.getInstancia().loadRequestParameter(request, "pcoLocDes");
        model.addAttribute("iniFuncionParm","3");
        model.addAttribute("listaDestinatario",referencedData.getListDestinatarioDocPendEntrega(pcoLocDes));
        return "/modalGeneral/consultaDestinatarioEmiConsul";
    } 
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDependenciaCargoEntregaInterno")
    public String goBuscaDependenciaCargoEntregaInterno(HttpServletRequest request, Model model){
        String pcoLocDes = ServletUtility.getInstancia().loadRequestParameter(request, "pcoLocDes");
        model.addAttribute("iniFuncionParm","4");
        model.addAttribute("listaDestinatario",referencedData.getListDestinatarioDocPendEntrega(pcoLocDes));
        return "/modalGeneral/consultaDestinatarioEmiConsul";
    }    
}
