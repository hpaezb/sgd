/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.web.controller;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.libreria.exception.validarDatoException;
import pe.gob.onpe.libreria.util.ServletUtility;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.CitizenBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DistritoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteDocExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.ProvinciaBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.RequisitoBean;
import pe.gob.onpe.tramitedoc.bean.TrxDocExternoRecepBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.CommonQryService;
import pe.gob.onpe.tramitedoc.service.DocumentoExtRecepService;
import pe.gob.onpe.tramitedoc.service.OtroOrigenService;
import pe.gob.onpe.tramitedoc.service.ReferencedData;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;


/**
 *
 * @author ecueva
 */
@Controller
@RequestMapping("/{version}/srMesaPartes.do")
public class MesaPartesController {
    
    @Autowired
    private ReferencedData referencedData;
    
    @Autowired
    private DocumentoExtRecepService documentoExtRecepService;
    
    @Autowired
    private OtroOrigenService otroOrigenService;
    
    @Autowired
    private CommonQryService commonQryService;  
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    private static final Logger logger = LogManager.getLogger(MesaPartesController.class);
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goInicio")
    public String goInicio(HttpServletRequest request, Model model){
        System.out.println("accion=goInicio GET--> ");
        String codDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "coDep");
        String pEstado = ServletUtility.getInstancia().loadRequestParameter(request, "estadoDoc");/*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR*/
        System.out.println("pEstado--> "+pEstado);
        String sCoAnnio = Utility.getInstancia().dateToFormatStringYYYY(new Date());
        BuscarDocumentoExtRecepBean buscarDocumentoExtRecepBean = new BuscarDocumentoExtRecepBean();
        buscarDocumentoExtRecepBean.setCoAnnio(sCoAnnio);
        buscarDocumentoExtRecepBean.setCoEstadoDoc("5");//en registro
        if(pEstado.equals("B")){
            buscarDocumentoExtRecepBean.setCoEstadoDoc(pEstado);/*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR*/
            buscarDocumentoExtRecepBean.setEsFiltroFecha("2");
        }
        buscarDocumentoExtRecepBean.setCoDependencia(codDependencia);
        model.addAttribute(buscarDocumentoExtRecepBean);
        /* [HPB] Inicio 06/10/23 OS-0000786-2023 Listar tipo de expedientes activo */
        //model.addAttribute("lstTipoExp", referencedData.grpElementoList("TIP_EXPEDIENTE"));
        model.addAttribute("lstTipoExp", referencedData.grpElementoListTipoExpediente("TIP_EXPEDIENTE"));
        /* [HPB] Fin 06/10/23 OS-0000786-2023 Listar tipo de expedientes activo */
        model.addAttribute("lstOrigen", referencedData.grpElementoList("ORI_DOCUMENTO"));
        model.addAttribute("lstTupaExp",referencedData.getTupaExpList());
        model.addAttribute("deTipoDocumentoList",referencedData.getTipoDocumentoEmiList(codDependencia));                
        model.addAttribute("deEmisorList",referencedData.getLstEmisorDocExtRecep());        
        model.addAttribute("deEstadosList",referencedData.getLstEstadoDocumentoExtRecep("TDTV_REMITOS"));
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute("annioActual", Utility.getInstancia().dateToFormatStringYYYY(new Date()));        
        return "/mpRegistroDocExt/buscaDocExtRecep";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goInicio")
    public String goInicio(BuscarDocumentoExtRecepBean buscarDocumentoExtRecepBean, HttpServletRequest request,  BindingResult result, Model model){
        String mensaje = "NO_OK";
        System.out.println("accion=goInicio POST--> ");
//        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String codDependencia = usuarioConfigBean.getCoDep();
        String codEmpleado = usuarioConfigBean.getCempCodemp();
        String tipAcceso = usuarioConfigBean.getTiAccesoMp();
        buscarDocumentoExtRecepBean.setCoDependencia(codDependencia);
        buscarDocumentoExtRecepBean.setCoEmpleado(codEmpleado);
        buscarDocumentoExtRecepBean.setTiAcceso(tipAcceso);
        //buscarDocumentoExtRecepBean.setCoLocal(usuarioConfigBean.getCoLocal());
        /* [HPB] Inicio 11/12/23 OS-0001287-2023 Registrar encargado de la mesa de partes correspondiente */
        if("11351".equals(codDependencia)){
        /* [HPB] Fin 11/12/23 OS-0001287-2023 Registrar encargado de la mesa de partes correspondiente */
        buscarDocumentoExtRecepBean.setCoDepEmi(usuarioConfigBean.getCoDepMp());
        /* [HPB] Inicio 11/12/23 OS-0001287-2023 Registrar encargado de la mesa de partes correspondiente */
        }else{
            buscarDocumentoExtRecepBean.setCoDepEmi(codDependencia);
        }
        /* [HPB] Fin 11/12/23 OS-0001287-2023 Registrar encargado de la mesa de partes correspondiente */
        buscarDocumentoExtRecepBean.setInMesaPartes(usuarioConfigBean.getInMesaPartes());

        List list = null;

        try{
            list = documentoExtRecepService.getDocumentosExtRecep(buscarDocumentoExtRecepBean);
        }catch(Exception ex){
            mensaje = ex.getMessage();
        }

        if (list!=null) {
            //if (list.size()>=50) {
            if (list.size()>= applicationProperties.getTopRegistrosConsultas()) {
    
                model.addAttribute("msjEmision","Solo se muestra "+applicationProperties.getTopRegistrosConsultas()+" filas en pantalla, descargue el excel o pdf para visualizar todos los registros");
            }
            model.addAttribute("docExtRecepLst",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        if (mensaje.equals("OK")) {
            return "/mpRegistroDocExt/tblDocExtRecep";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goNewDocumentoExternoRecep")
    public String goNewDocumentoExternoRecep(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
       
       String nuAnn = Utility.getInstancia().dateToFormatStringYYYY(new Date());
       String fechaActual = Utility.getInstancia().dateToFormatString(new Date());
       String fechaHoraActual = Utility.getInstancia().dateToFormatString2(new Date());
       /* [HPB] Inicio 11/12/23 OS-0001287-2023 Registrar encargado de la mesa de partes correspondiente */
       String codDependencia = usuarioConfigBean.getCoDepMp();//usuario.getCoDep();
       String codDependenciaUsuario = usuario.getCoDep();
       /* [HPB] Fin 11/12/23 OS-0001287-2023 Registrar encargado de la mesa de partes correspondiente */
       DocumentoExtRecepBean documentoExtRecepBean;
       String pdeDepen = null;
       try{
           documentoExtRecepBean = documentoExtRecepService.getDocumentoExtRecepNew(codDependencia);
           if(codDependencia!=null&&codDependencia.trim().length()>0){
                /*modificaciones para mostrar plantilla de documentos externo recepcion*/
//                model.addAttribute("lstDestintarioDocAdmEmi",documentoExtRecepService.getLsDestinoNewDocExtMesaPartes());
                model.addAttribute("lstDestintarioDocAdmEmi",new ArrayList<DestinatarioDocumentoEmiBean>());
                /*modificaciones para mostrar plantilla de documentos externo recepcion*/
                documentoExtRecepBean.setCoDepEmi(codDependencia);
                documentoExtRecepBean.setCoLocEmi(usuarioConfigBean.getCoLocal());
                documentoExtRecepBean.setCoEmpRes(usuario.getCempCodemp());
                /* [HPB] Inicio 11/12/23 OS-0001287-2023 Registrar encargado de la mesa de partes correspondiente */
                if(!"11351".equals(codDependenciaUsuario)){
                    documentoExtRecepBean.setCoEmpEmi(usuario.getCempCodemp());
                    //documentoExtRecepBean.setCoDepEmi(usuario.getCoDep());
                }
                /* [HPB] Fin 11/12/23 OS-0001287-2023 Registrar encargado de la mesa de partes correspondiente */
                documentoExtRecepBean.setDeEmpRes(usuario.getDeFullName());
                documentoExtRecepBean.setFeExp(fechaHoraActual);
                documentoExtRecepBean.setNuAnn(nuAnn);
                documentoExtRecepBean.setNuAnnExp(nuAnn);
                documentoExtRecepBean.setNuDiaAte("0");
                documentoExtRecepBean.setInNumeroMp(usuarioConfigBean.getInNumeroMp());
                model.addAttribute("lstTupaExp",referencedData.getTupaExpListNew());           
                model.addAttribute("lstAnnio",referencedData.getAnnioList());
                model.addAttribute("lstTipoRemi",referencedData.getLstEmisorDocExtRecep());        
                model.addAttribute("lstTipDocReferenciaEmi",referencedData.getTipoDocRefList(codDependencia));           
                model.addAttribute("lstTipDocDependencia",referencedData.listTipDocXDependencia(codDependencia));
                model.addAttribute("lstPrioridadDestEmi",referencedData.getLstPrioridadDestEmi());           
                model.addAttribute("pfechaHoraActual",fechaHoraActual);
                /*--HPB 13/01/20 Integrar PIDE--*/
                String vIndicadorPide = commonQryService.obtenerValorParametro("IN_PIDE");
                model.addAttribute("vIndicadorPide", vIndicadorPide);
                /*--HPB 13/01/20 Integrar PIDE--*/                
                //Nuevos Campos
                model.addAttribute("lstDpto", referencedData.getlistaDepartamento());
                /* [HPB] Inicio 06/10/23 OS-0000786-2023 Listar tipo de expedientes activo */
                //model.addAttribute("lstTipoExp", referencedData.grpElementoList("TIP_EXPEDIENTE"));
                model.addAttribute("lstTipoExp", referencedData.grpElementoListTipoExpediente("TIP_EXPEDIENTE"));
                /* [HPB] Fin 06/10/23 OS-0000786-2023 Listar tipo de expedientes activo */
                model.addAttribute("lstOrigen", referencedData.grpElementoList("ORI_DOCUMENTO"));
                
                model.addAttribute("lstTraDest", referencedData.getNewUpdListDependenciaDestinatarioEmi(usuario.getCoDep(), pdeDepen));
                model.addAttribute("lstComision", otroOrigenService.getOtrosOrigenesPorTipo("09") );
                model.addAttribute("lstTipoCong", referencedData.grpElementoList("CO_TIPO_CONG"));
                model.addAttribute("lstTipoInv", referencedData.grpElementoList("CO_TIPO_INV"));
                model.addAttribute("rucCongreso", referencedData.grpElementoList("RUC_CONGRESO").get(0).getCeleDesele());
                model.addAttribute(documentoExtRecepBean);
                model.addAttribute("inDniTramitadorMp",commonQryService.obtenerValorParametro("IN_DNI_TRAMITADOR_MP"));                
                model.addAttribute("inDestinoTramitanteMp",commonQryService.obtenerValorParametro("IN_DESTINO_TRAMITANTE_MP"));
                mensaje = "OK";

           }else{
                mensaje = "<h3>SIN ACCESO.</h3>";   
           }
       }catch(Exception ex){
          mensaje = ex.getMessage(); 
       }
       if (mensaje.equals("OK")) {
           return "/mpRegistroDocExt/newDocExtRecep";           
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }       
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goGrabarDocumento",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goGrabarDocumento(@RequestBody TrxDocExternoRecepBean trxDocExternoRecepBean,HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String coRespuesta;
        String deRespuesta;
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        trxDocExternoRecepBean.setCoUserMod(usuarioConfigBean.getCoUsuario());
        trxDocExternoRecepBean.setCempCodEmp(usuarioConfigBean.getCempCodemp());
        trxDocExternoRecepBean.setCoDependencia(usuarioConfigBean.getCoDepMp());
        String resultJsonStr="";
        try{
            mensaje = documentoExtRecepService.grabaDocumentoExternoRecep(trxDocExternoRecepBean,usuario);
            if(mensaje.equals("OK")){
               resultJsonStr=","+documentoExtRecepService.getJsonRptGrabaDocumentoExternoRecep(trxDocExternoRecepBean);
            }
        }catch(validarDatoException e){
           mensaje=(e.valorMsg);
        }catch (Exception e) { 
           e.printStackTrace(); 
        } 
        
        if (mensaje.equals("OK")) {
            coRespuesta = "1";
        }else{
            coRespuesta = "0";
        }  
        deRespuesta = mensaje;
        
        StringBuilder retval = new StringBuilder();
        retval.append("{\"coRespuesta\":\"");
        retval.append(coRespuesta);
        retval.append("\",\"deRespuesta\":\"");
        retval.append(deRespuesta);
        retval.append("\"");
        retval.append(resultJsonStr);
//        retval.append("\",\"nuCorEmi\":\"");
//        retval.append(trxDocumentoEmiBean.getNuCorEmi());
        retval.append("}");

        return retval.toString();        
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goIsExpedienteDuplicado",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goIsExpedienteDuplicado(HttpServletRequest request, Model model){
       String mensaje="1";//es duplicado
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       String pnuAnnExp = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnnExp");       
       String pnuCorrExp = ServletUtility.getInstancia().loadRequestParameter(request, "pnuCorrExp");       
       //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
       String pcoDepExp=usuarioConfigBean.getCoDepMp();//usuario.getCoDep();
       
       try{
           mensaje = documentoExtRecepService.isExpedienteDuplicado(pnuAnnExp, pcoDepExp, pnuCorrExp);
       }catch(Exception e){
           e.printStackTrace();
       }
       
       vRespuesta = mensaje;
       if (mensaje.equals("0")) {
            coRespuesta = "0";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\"}");
       } else {
            coRespuesta = "1";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append(vRespuesta);                            
            retval.append("\"}");
       }
       return retval.toString();        
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaAccionDestinatario")
    public String goBuscaAccionDestinatario(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String pcoDepen = usuarioConfigBean.getCoDep();
        List list = null;
        try{
            list = documentoExtRecepService.getLstMotivoxTipoDocumento();
        }catch(Exception ex){
             mensaje = ex.getMessage();
        }
        if (list!=null) {
            model.addAttribute("listaAccionDestEmi",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaMotivoDestEmi";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }        
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDocumentotblReferencia")
    public String goBuscaDocumentotblReferencia(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String pannio = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String ptiDoc = ServletUtility.getInstancia().loadRequestParameter(request, "ptiDoc");
        String pnuExpediente = ServletUtility.getInstancia().loadRequestParameter(request, "pnroExpediente");
        String ptiBusqueda = ServletUtility.getInstancia().loadRequestParameter(request, "ptiBusqueda");// rec doc recepcionado, emi doc emitido
        //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String pcoDepen = usuarioConfigBean.getCoDepMp();//usuario.getCoDep();        
        List list = null;
        try{
            if(ptiBusqueda.equals("emi")){ // doc emitido
                list = documentoExtRecepService.getLstDocExtReferenciaEmitido(pannio,ptiDoc,pcoDepen,pnuExpediente);                
            }else if(ptiBusqueda.equals("rec")){ // doc recepcionado
                list = documentoExtRecepService.getLstDocExtReferencia(pannio,ptiDoc,pcoDepen,pnuExpediente);
            }
                            
        }catch(Exception ex){
             mensaje = ex.getMessage();
        }
        if (list!=null) {
            model.addAttribute("lstDocReferencia",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaDocumentoExtRecep";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goEditDocumentoExtRec")
    public String goEditDocumentoExtRec(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       String snuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String snuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
       
       //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
       String fechaHoraActual = Utility.getInstancia().dateToFormatString2(new Date());
       String codDependencia = usuarioConfigBean.getCoDepMp();//usuario.getCoDep();
       DocumentoExtRecepBean documentoExtRecepBean;
       List listReferenciaDoc;
       
       String pdeDepen = null;
       List listDestintarioEmiDoc;
       
       try{
           documentoExtRecepBean = documentoExtRecepService.getDocumentoExtRec(snuAnn,snuEmi);

           listDestintarioEmiDoc = documentoExtRecepService.getLstDestinoEmiDoc(snuAnn,snuEmi);
           listReferenciaDoc = documentoExtRecepService.getLstReferenciaDoc(snuAnn,snuEmi);
           String dpto = documentoExtRecepBean.getIdDepartamento();
           String prov = documentoExtRecepBean.getIdProvincia();
           
           model.addAttribute("lstDestintarioDocAdmEmi",listDestintarioEmiDoc);
           model.addAttribute(documentoExtRecepBean);
           /* [HPB] Inicio 15/03/23 CLS-087-2022 */
           String alphaAndDigits = documentoExtRecepBean.getDeDocSig().replaceAll("[¿,@?+^]*", "");
           documentoExtRecepBean.setDeDocSig(alphaAndDigits);
           /* [HPB] Fin 15/03/23 CLS-087-2022 */
           model.addAttribute("lstReferenciaDocAdmEmi",listReferenciaDoc);
           model.addAttribute("lstTupaExp",referencedData.getTupaExpListNew());           
           model.addAttribute("lstAnnio",referencedData.getAnnioList());
           model.addAttribute("lstTipoRemi",referencedData.getLstEmisorDocExtRecep());        
           model.addAttribute("lstTipDocReferenciaEmi",referencedData.getTipoDocRefList(codDependencia));           
           model.addAttribute("lstTipDocDependencia",referencedData.listTipDocXDependencia(codDependencia));
           model.addAttribute("lstPrioridadDestEmi",referencedData.getLstPrioridadDestEmi());           
           model.addAttribute("pfechaHoraActual",fechaHoraActual);
           
           model.addAttribute("lstDpto", referencedData.getlistaDepartamento());
           model.addAttribute("lstProv", referencedData.listProvincia(dpto));
           model.addAttribute("lstDist", referencedData.listDistrito(dpto,prov));
           /* [HPB] Inicio 06/10/23 OS-0000786-2023 Listar tipo de expedientes activo */
           //model.addAttribute("lstTipoExp", referencedData.grpElementoList("TIP_EXPEDIENTE"));
           model.addAttribute("lstTipoExp", referencedData.grpElementoListTipoExpediente("TIP_EXPEDIENTE"));
           /* [HPB] Fin 06/10/23 OS-0000786-2023 Listar tipo de expedientes activo */
           model.addAttribute("lstOrigen", referencedData.grpElementoList("ORI_DOCUMENTO"));
           
           model.addAttribute("lstTraDest", referencedData.getNewUpdListDependenciaDestinatarioEmi(usuario.getCoDep(), pdeDepen));
           model.addAttribute("lstComision", otroOrigenService.getOtrosOrigenesPorTipo("09") );
           model.addAttribute("lstTipoCong", referencedData.grpElementoList("CO_TIPO_CONG"));
           model.addAttribute("lstTipoInv", referencedData.grpElementoList("CO_TIPO_INV"));
           model.addAttribute("rucCongreso", referencedData.grpElementoList("RUC_CONGRESO").get(0).getCeleDesele());
           model.addAttribute("inDniTramitadorMp",commonQryService.obtenerValorParametro("IN_DNI_TRAMITADOR_MP"));           
           model.addAttribute("inDestinoTramitanteMp",commonQryService.obtenerValorParametro("IN_DESTINO_TRAMITANTE_MP"));     
           model.addAttribute("lstFormaEntrega", referencedData.listFormaEntrega("FORMA_ENTREGA_SAI"));/*[HPB-11/11/21] Fin Mejoras SAI*/
           mensaje = "OK";
       }catch(Exception ex){
          mensaje = ex.getMessage(); 
       }
       if (mensaje.equals("OK")) {
           return "/mpRegistroDocExt/editDocExtRecep";           
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }       
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaProveedor")
    public String goBuscaDestProveedor(HttpServletRequest request, Model model){
        String prazonSocial = ServletUtility.getInstancia().loadRequestParameter(request, "prazonSocial");
        model.addAttribute("lsDestProveedor",documentoExtRecepService.getLstProveedores(prazonSocial));
        model.addAttribute("iniFuncionParm","2");
        return "/modalGeneral/consultaDestProveedor";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaProveedorJson",produces="text/plain; charset=utf-8")
    public @ResponseBody String goBuscaProveedorJson(HttpServletRequest request, Model model){
        String result=""; 
        String pnuRuc = ServletUtility.getInstancia().loadRequestParameter(request, "pnuRuc");
        try{
            result = documentoExtRecepService.getProveedor(pnuRuc);
        }catch(Exception e){
            e.printStackTrace();
        }       
        return result;
    }    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadano")
    public @ResponseBody String goBuscaCiudadano(HttpServletRequest request, Model model){
        String result=""; 
        String pnuDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDoc");
        try{
            result = documentoExtRecepService.getCiudadano(pnuDoc);
        }catch(Exception e){
            e.getMessage();
        }       
        return result;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBus")
    public String goBuscaCiudadanoBus(HttpServletRequest request, Model model){
        String sDescCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sDescCiudadano");
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sDescCiudadano));
        model.addAttribute("iniFuncionParm","1");
        return "/modalGeneral/consultaDestCiudadano";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusDE")
    public String goBuscaCiudadanoBusDE(HttpServletRequest request, Model model){
        String sDescCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sDescCiudadano");
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sDescCiudadano));
        model.addAttribute("iniFuncionParm","2");
        return "/modalGeneral/consultaDestCiudadano";
    }
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusSeg")
    public String goBuscaCiudadanoBusSeg(HttpServletRequest request, Model model){
        String sDescCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sDescCiudadano");
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sDescCiudadano));
        model.addAttribute("iniFuncionParm","3");
        return "/modalGeneral/consultaDestCiudadano";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusIni")
    public String goBuscaCiudadanoBusIni(HttpServletRequest request, Model model){
        String sDescCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sDescCiudadano");
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sDescCiudadano));
        model.addAttribute("iniFuncionParm","4");
        return "/modalGeneral/consultaDestCiudadano";
    }
    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaProveedorBus")
    public String goBuscaDestProveedorBus(HttpServletRequest request, Model model){
        String prazonSocial = ServletUtility.getInstancia().loadRequestParameter(request, "prazonSocial");
        model.addAttribute("lsDestProveedor",documentoExtRecepService.getLstProveedores(prazonSocial));
        model.addAttribute("iniFuncionParm","2");
        return "/modalGeneral/consultaDestProveedorBus";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaProveedorBusDE")
    public String goBuscaProveedorBusDE(HttpServletRequest request, Model model){
        String prazonSocial = ServletUtility.getInstancia().loadRequestParameter(request, "prazonSocial");
        model.addAttribute("lsDestProveedor",documentoExtRecepService.getLstProveedores(prazonSocial));
        model.addAttribute("iniFuncionParm","3");
        return "/modalGeneral/consultaDestProveedorBus";
    }
    
       
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaOtroOrigen")
    public String goBuscaOtroOrigen(HttpServletRequest request, Model model){        
        String pdesOtroOri = ServletUtility.getInstancia().loadRequestParameter(request, "pdesOtroOri");         
        model.addAttribute("lsDestOtroOrigen",documentoExtRecepService.getLstOtrosOrigenes(pdesOtroOri));
        model.addAttribute("iniFuncionParm","2");
        return "/modalGeneral/consultaDestOtroOrigenBus";        
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaProveedorBusSeg")
    public String goBuscaProveedorBusSeg(HttpServletRequest request, Model model){
        String prazonSocial = ServletUtility.getInstancia().loadRequestParameter(request, "prazonSocial");
        model.addAttribute("lsDestProveedor",documentoExtRecepService.getLstProveedores(prazonSocial));
        model.addAttribute("iniFuncionParm","4");
        return "/modalGeneral/consultaDestProveedorBus";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaOtroOrigenDE")
    public String goBuscaOtroOrigenDE(HttpServletRequest request, Model model){        
        String pdesOtroOri = ServletUtility.getInstancia().loadRequestParameter(request, "pdesOtroOri");         
        model.addAttribute("lsDestOtroOrigen",documentoExtRecepService.getLstOtrosOrigenes(pdesOtroOri));
        model.addAttribute("iniFuncionParm","3");
        return "/modalGeneral/consultaDestOtroOrigenBus";        
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaOtroOrigenSeg")
    public String goBuscaOtroOrigenSeg(HttpServletRequest request, Model model){        
        String pdesOtroOri = ServletUtility.getInstancia().loadRequestParameter(request, "pdesOtroOri");         
        model.addAttribute("lsDestOtroOrigen",documentoExtRecepService.getLstOtrosOrigenes(pdesOtroOri));
        model.addAttribute("iniFuncionParm","4");
        return "/modalGeneral/consultaDestOtroOrigenBus";        
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestOtroOrigen")
    public String goBuscaDestOtroOrigen(HttpServletRequest request, Model model){
        String pnomOtroOrigen = ServletUtility.getInstancia().loadRequestParameter(request, "pnoOtroOrigen");
        model.addAttribute("lsDestOtroOrigen",documentoExtRecepService.getLstOtrosOrigenes(pnomOtroOrigen));
        model.addAttribute("iniFuncionParm","2");
        return "/modalGeneral/consultaDestOtroOrigen";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goChangeToRegistrado",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goChangeToRegistrado(HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       DocumentoExtRecepBean documentoExtRecepBean = new DocumentoExtRecepBean();
       documentoExtRecepBean.setNuAnn(pnuAnn);
       documentoExtRecepBean.setNuEmi(pnuEmi);
       documentoExtRecepBean.setCoUseMod(usuario.getCoUsuario());
       /* [HPB] Inicio 07/11/22 OS-0000768-2022 */
       documentoExtRecepBean.setCoEmpRes(usuario.getCempCodemp());
       /* [HPB] Fin 07/11/22 OS-0000768-2022 */
       try{
          mensaje = documentoExtRecepService.changeToRegistrado(documentoExtRecepBean, usuario);
       }catch(validarDatoException e){
           mensaje=(e.valorMsg);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goChangeToEnRegistro",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goChangeToEnRegistro(HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       DocumentoExtRecepBean documentoExtRecepBean = new DocumentoExtRecepBean();
       documentoExtRecepBean.setNuAnn(pnuAnn);
       documentoExtRecepBean.setNuEmi(pnuEmi);
       documentoExtRecepBean.setCoUseMod(usuario.getCoUsuario());
       documentoExtRecepBean.setCoEmpRes(usuario.getCempCodemp());
        System.out.println("goChangeToEnRegistro 1");
       try{
          mensaje = documentoExtRecepService.changeToEnRegistro(documentoExtRecepBean, usuario);
          System.out.println("mensaje");
       }catch(validarDatoException e){
           mensaje=(e.valorMsg);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAnularDocExtRecep")
    public @ResponseBody String goAnularDocExtRecep(HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");       
       String pesDocEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pesDocEmi");
       DocumentoExtRecepBean documentoExtRecepBean = new DocumentoExtRecepBean();
       documentoExtRecepBean.setCoUseMod(usuario.getCoUsuario());
       documentoExtRecepBean.setNuAnn(pnuAnn);
       documentoExtRecepBean.setNuEmi(pnuEmi);
       documentoExtRecepBean.setCoEsDocEmiMp(pesDocEmi);
       try{
          mensaje = documentoExtRecepService.anularDocumentoExtRecep(documentoExtRecepBean, usuario);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goNewDocumentoExternoAtender")
    public String goNewDocumentoExternoAtender(DocumentoBean docRecBean,HttpServletRequest request, Model model){
       String mensaje = "NO_OK";
       Map map;
       DocumentoExtRecepBean docExterno=null;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
       
       String nuAnn = Utility.getInstancia().dateToFormatStringYYYY(new Date());
       //String fechaActual = Utility.getInstancia().dateToFormatString(new Date());
       String fechaHoraActual = Utility.getInstancia().dateToFormatString3(new Date());
       String codDependencia = usuarioConfigBean.getCoDepMp();
       docRecBean.setCoDepEmi(codDependencia);
       try{
           map = documentoExtRecepService.getNewDocExtRecepAtender(docRecBean);
           if(map!=null){
               docExterno=(DocumentoExtRecepBean)map.get("docExterno");
               if(docExterno!=null){
                    docExterno.setCoDepEmi(codDependencia);
                    docExterno.setCoLocEmi(usuarioConfigBean.getCoLocal());
                    docExterno.setCoEmpRes(usuarioConfigBean.getCempCodemp());
                    docExterno.setDeEmpRes(usuario.getDeFullName());
                    docExterno.setFeExp(fechaHoraActual);
                    docExterno.setNuAnn(nuAnn);
                    docExterno.setNuAnnExp(nuAnn);    
                    docExterno.setInNumeroMp(usuarioConfigBean.getInNumeroMp());                    
               }
              model.addAttribute("lstReferenciaDocAdmEmi",map.get("lsReferencia"));    
           }
           model.addAttribute("lstTupaExp",referencedData.getTupaExpListNew());           
           model.addAttribute("lstAnnio",referencedData.getAnnioList());
           model.addAttribute("lstTipoRemi",referencedData.getLstEmisorDocExtRecep());        
           model.addAttribute("lstTipDocReferenciaEmi",referencedData.getTipoDocRefList(codDependencia));           
           model.addAttribute("lstTipDocDependencia",referencedData.listTipDocXDependencia(codDependencia));
           model.addAttribute("lstPrioridadDestEmi",referencedData.getLstPrioridadDestEmi());           
           model.addAttribute("pfechaHoraActual",fechaHoraActual);
           model.addAttribute(docExterno);
           model.addAttribute("inDniTramitadorMp",commonQryService.obtenerValorParametro("IN_DNI_TRAMITADOR_MP"));            
           model.addAttribute("inDestinoTramitanteMp",commonQryService.obtenerValorParametro("IN_DESTINO_TRAMITANTE_MP"));
           mensaje = "OK";
       }catch(Exception ex){
          mensaje = ex.getMessage(); 
       }
       if (mensaje.equals("OK")) {
           return "/mpRegistroDocExt/newDocExtRecep";           
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }       
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goProcesoExp")
    public @ResponseBody String goProcesoExp(HttpServletRequest request, Model model){
       String vRespuesta=null;
       String pcoProceso = ServletUtility.getInstancia().loadRequestParameter(request, "pcoProceso");
        try {
            vRespuesta=documentoExtRecepService.getProcesoExpediente(pcoProceso);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vRespuesta;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestinatario")
    public String goBuscaDestinatario(HttpServletRequest request, Model model){
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        model.addAttribute("iniFuncionParm","7");
        model.addAttribute("listaDestinatario",referencedData.getListDestinatarioEmiDocExt(pnuAnn,usuarioConfigBean.getCoDepMp()));
        return "/modalGeneral/consultaDestinatarioEmiConsul";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goChangeToParaVerificar",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goChangeToParaVerificar(HttpServletRequest request, Model model){
       String mensaje="";
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       DocumentoExtRecepBean documentoExtRecepBean = new DocumentoExtRecepBean();
       documentoExtRecepBean.setNuAnn(pnuAnn);
       documentoExtRecepBean.setNuEmi(pnuEmi);
       documentoExtRecepBean.setCoUseMod(usuario.getCoUsuario());
       try{
          mensaje = documentoExtRecepService.changeToParaVerificar(documentoExtRecepBean, usuario);
       }catch(validarDatoException e){
           mensaje=(e.valorMsg);
       }catch(Exception e){
           if(e.getMessage()!=null)
           {
           mensaje = e.getMessage();
           }
           else
           {mensaje="0";}
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
  
    /*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goChangeToParaSubsanar",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goChangeToParaSubsanar(HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       DocumentoExtRecepBean documentoExtRecepBean = new DocumentoExtRecepBean();
       documentoExtRecepBean.setNuAnn(pnuAnn);
       documentoExtRecepBean.setNuEmi(pnuEmi);
       documentoExtRecepBean.setCoUseMod(usuario.getCoUsuario());
       try{
          mensaje = documentoExtRecepService.changeToParaSubsanar(documentoExtRecepBean, usuario);
       }catch(validarDatoException e){
           mensaje=(e.valorMsg);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goChangeToArchivar",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goChangeToArchivar(HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       DocumentoExtRecepBean documentoExtRecepBean = new DocumentoExtRecepBean();
       documentoExtRecepBean.setNuAnn(pnuAnn);
       documentoExtRecepBean.setNuEmi(pnuEmi);
       documentoExtRecepBean.setCoUseMod(usuario.getCoUsuario());
       try{
          mensaje = documentoExtRecepService.changeToArchivar(documentoExtRecepBean, usuario);
       }catch(validarDatoException e){
           mensaje=(e.valorMsg);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goModalObsPorSubsanar")
    public String goModalObsPorSubsanar(HttpServletRequest request, Model model) {
        String pag = "/modalGeneral/observacionPorSubsanarReadOnly";
        String mensaje;
        DocumentoExtRecepBean documentoExtRecepBean;
        String pnuAnnExp = ServletUtility.getInstancia().loadRequestParameter(request, "pNuAnnExp");
        String pnuSecExp = ServletUtility.getInstancia().loadRequestParameter(request, "pNuSecExp");
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pNuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pNuEmi");
        ExpedienteDocExtRecepBean expedienteDocExtRecepBean = new ExpedienteDocExtRecepBean();
        documentoExtRecepBean = documentoExtRecepService.getDocumentoExtRec(pnuAnn,pnuEmi);
        
        model.addAttribute(expedienteDocExtRecepBean);
        System.out.println("pnuAnnExp--> "+ pnuAnnExp);
        System.out.println("pnuSecExp--> "+ pnuSecExp);
        try {
            String esDocEmi=documentoExtRecepService.getEsDocEmi(pnuAnn, pnuEmi);
            if(esDocEmi!=null&&(esDocEmi.equals("5")||esDocEmi.equals("7"))){
                pag = "/modalGeneral/observacionPorSubsanar";
            } 
            model.addAttribute("expedienteDocExtRecepBean",documentoExtRecepService.getObsExpediente(pnuAnnExp, pnuSecExp));
            model.addAttribute("validaCorreo", documentoExtRecepBean.getDeCorreo());
            mensaje = "OK";
        } catch (Exception e) {
            mensaje = e.getMessage();
            e.printStackTrace();
        }
        if (mensaje.equals("OK")) {
            return pag;
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }   
        //return "/modalGeneral/observacionPorSubsanar";
    } 
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goSaveObsPorSubsanar")
    public @ResponseBody String goSaveObsPorSubsanar(@RequestBody ExpedienteDocExtRecepBean expedienteBean,HttpServletRequest request, Model model) {
        StringBuilder retval = new StringBuilder();
        String coRespuesta = "0";
        String vResult = "NO_OK";
        String pnuAnnExp = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnnExp");
        String pnuSecExp = ServletUtility.getInstancia().loadRequestParameter(request, "nuSecExp");                  
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        try{
            vResult = documentoExtRecepService.updObservacionExpedienteBean(pnuAnnExp, pnuSecExp, expedienteBean.getDeDetalle(), usuario.getCempCodemp());
        }catch(validarDatoException e){
           vResult=(e.valorMsg);
        }catch (Exception e) { 
           e.printStackTrace(); 
        }         
        
       if (vResult.equals("OK")) {
            coRespuesta = "1";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\"}");
       } else {
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append(vResult);                            
            retval.append("\"}");
       }      
        return retval.toString();
    }    
    /*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR */  
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaLocal")
    public String goBuscaLocal(HttpServletRequest request, Model model){
        //String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        model.addAttribute("iniFuncionParm","2");
        model.addAttribute("deLocalesList",referencedData.getLsLocales());
        return "/modalGeneral/consultaLocal";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaAllDependencia")
    public String goBuscaAllDependencia(HttpServletRequest request, Model model){
        model.addAttribute("iniFuncionParm","4");
        model.addAttribute("listaReferenOrig",documentoExtRecepService.getAllDependencias());
        return "/modalGeneral/consultaReferenciaOrigenConsul";
    }    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRutaReporteVoucherExp")
    public @ResponseBody
    String goRutaReporteVoucherExp(HttpServletRequest request, Model model) {
        String coRespuesta = "0";
        String deNoDoc;

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");

        String fecha = Utility.getInstancia().getStringFechaYYYYMMDDHHmm(new Date());
        deNoDoc = "temp|VOUCHER_" + fecha + ".pdf";

        String prutaReporte = "reporte?coReporte=TDR15&coParametros=CO_USER=" + usuario.getCoUsuario()
                + "|NU_ANN=" + pnuAnn + "|NU_EMI=" + pnuEmi + "&coImagenes=P_LOGO_DIR=logo_onpe.jpg";

        StringBuilder retval = new StringBuilder();
        retval.append("{\"coRespuesta\":\"");
        retval.append(coRespuesta);
        retval.append("\",\"noUrl\":\"");
        retval.append(prutaReporte);
        retval.append("\",\"noDoc\":\"");
        retval.append(deNoDoc);
        retval.append("\"}");

        return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRequisitosExpDocExtRec")
    public String goRequisitosExpDocExtRec(HttpServletRequest request, Model model){
        String pnuAnnExp = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnnExp");
        String pnuSecExp = ServletUtility.getInstancia().loadRequestParameter(request, "nuSecExp");        
        String pcoProceso = ServletUtility.getInstancia().loadRequestParameter(request, "coProceso");        
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");        
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");        
        String pag = "/mpRegistroDocExt/reqDocExtRecReadOnly";
        String mensaje;
        try {
            String esDocEmi=documentoExtRecepService.getEsDocEmi(pnuAnn, pnuEmi);
            if(esDocEmi!=null&&(esDocEmi.equals("5")||esDocEmi.equals("7"))){
                pag = "/mpRegistroDocExt/reqDocExtRec";
            }            
            model.addAttribute("lRequisito",documentoExtRecepService.getAllRequisitoExpediente(pnuAnnExp, pnuSecExp, pcoProceso));
            mensaje = "OK";
        } catch (Exception e) {
            mensaje = e.getMessage();
            e.printStackTrace();
        }
        if (mensaje.equals("OK")) {
            return pag;
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }          
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goSaveReqExpDocExtRec")
    public @ResponseBody
    String goSaveReqExpDocExtRec(@RequestBody List<RequisitoBean> lsReq,HttpServletRequest request, Model model) {
        StringBuilder retval = new StringBuilder();
        String coRespuesta="0";
        String vResult="NO_OK";
        
        String pnuAnnExp = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnnExp");
        String pnuSecExp = ServletUtility.getInstancia().loadRequestParameter(request, "nuSecExp");        
        String pcoProceso = ServletUtility.getInstancia().loadRequestParameter(request, "coProceso");            
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        try{
            vResult = documentoExtRecepService.guardarReqExpDocExtRec(lsReq, pnuAnnExp, pnuSecExp, pcoProceso, usuario.getCoUsuario());
        }catch(validarDatoException e){
           vResult=(e.valorMsg);
        }catch (Exception e) { 
           e.printStackTrace(); 
        }         
        
       if (vResult.equals("OK")) {
            coRespuesta = "1";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\"}");
       } else {
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append(vResult);                            
            retval.append("\"}");
       }
       
        return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRequisitosExpDocExtSeg")
    public String goRequisitosExpDocExtSeg(HttpServletRequest request, Model model){
        String pnuAnnExp = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnnExp");
        String pnuSecExp = ServletUtility.getInstancia().loadRequestParameter(request, "pnuSecExp");        
        String pag = "/mpRegistroDocExt/reqDocExtRecReadOnly";
        String mensaje;
        try {
            model.addAttribute("lRequisito",documentoExtRecepService.getAllRequisitoExpediente(pnuAnnExp, pnuSecExp));
            mensaje = "OK";
        } catch (Exception e) {
            mensaje = e.getMessage();
            e.printStackTrace();
        }
        if (mensaje.equals("OK")) {
            return pag;
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }    
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goExportarVoucher")
    public @ResponseBody String goExportarVoucher(HttpServletRequest request, Model model) throws IOException {           
        ServletContext sc = request.getSession().getServletContext();
        
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi"); 

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
        
        DocumentoBean documento = new DocumentoBean();
        documento.setNuAnn(pnuAnn);
        documento.setNuEmi(pnuEmi);        
        documento.setRutaReporteJasper(rutaReporte);
        
        ReporteBean objReporteBean = documentoExtRecepService.getGenerarReporteVoucher(parametros, documento);
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
    
    // Obtiene las provincia en base al id del departamento
    @RequestMapping(method = RequestMethod.POST, params = "accion=getProvincias", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ProvinciaBean> getProvincias(@RequestParam("idDepartamento") String idDepartamento) {
        List<ProvinciaBean> provincias = referencedData.listProvincia(idDepartamento);
        return provincias;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=getDistrito", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DistritoBean> getDistrito(@RequestParam("idDepartamento") String idDepartamento,@RequestParam("idProvincia") String idProvincia) {
        List<DistritoBean> distritos = referencedData.listDistrito(idDepartamento,idProvincia);
        return distritos;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goExportarArchivoLista")
    public @ResponseBody String goExportarArchivoLista(BuscarDocumentoExtRecepBean buscarDocumentoExtRecepBean, HttpServletRequest request,
            BindingResult result, Model model) throws JRException, FileNotFoundException, IOException 
    {           
        ServletContext sc = request.getSession().getServletContext();
        
        String coReporte = ServletUtility.getInstancia().loadRequestParameter(request, "pformatRepor");

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String codDependencia = usuarioConfigBean.getCoDep();
                
        String rutaReporte=sc.getRealPath("/reports/");
//        String rutaImagen = sc.getRealPath("/reports/logo_onpe.jpg");
        String logo = applicationProperties.getLogoReporteB64();                
        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(logo));
        
        Map parametros = new HashMap();
        parametros.put("P_DE_DEPENDENCIA", usuarioConfigBean.getDeDep());
        parametros.put("P_USER", usuario.getCoUsuario());
        parametros.put("P_LOGO_DIR", bis.available()==0?null:bis);
                
        //setter
        buscarDocumentoExtRecepBean.setCoDependencia(codDependencia);        
        buscarDocumentoExtRecepBean.setInMesaPartes(usuarioConfigBean.getInMesaPartes());     
        buscarDocumentoExtRecepBean.setTiAcceso(usuarioConfigBean.getTiConsultaMp());
        buscarDocumentoExtRecepBean.setCoEmpleado(usuario.getCempCodemp());
        buscarDocumentoExtRecepBean.setCoDependencia(usuario.getCoDep());
  
        buscarDocumentoExtRecepBean.setRutaReporteJasper(rutaReporte);
        buscarDocumentoExtRecepBean.setFormatoReporte(coReporte);
        
        ReporteBean objReporteBean = documentoExtRecepService.getGenerarReporte(buscarDocumentoExtRecepBean, parametros);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goHojaDeRuta")
    public String goHojaDeRuta(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";        
        List list = null;
        String pNuAnn, pNuEmi, pTipo;
        pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        pTipo = ServletUtility.getInstancia().loadRequestParameter(request, "ptipo");
        DocumentoExtRecepBean documentoExtRecepBean = null;
        try{
                documentoExtRecepBean = documentoExtRecepService.getDocumentoExtRecep(pNuAnn,pNuEmi);
                list = documentoExtRecepService.obtenerHojaDeRuta(pNuAnn,pNuEmi, pTipo);                

        }catch(Exception ex){
             mensaje = ex.getMessage();
        }
        if (list!=null) {          
            model.addAttribute("lstHojaDeRuta",list);
            model.addAttribute("documentoExtRecepBean",documentoExtRecepBean);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaHojaDeRuta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goSituacionDeExpediente")
    public String goSituacionDeExpediente(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";        
        List list = null;
        String pNuAnn, pNuEmi, pTipo;
        pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        DocumentoExtRecepBean documentoExtRecepBean = null;
        try{
                documentoExtRecepBean = documentoExtRecepService.getDocumentoExtRecep(pNuAnn,pNuEmi);
                list = documentoExtRecepService.obtenerSituacionExpediente(pNuAnn,pNuEmi);                

        }catch(Exception ex){
             mensaje = ex.getMessage();
        }
        if (list!=null) {          
            model.addAttribute("lstSituacionExpediente",list);
            model.addAttribute("documentoExtRecepBean",documentoExtRecepBean);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaSituacionExpediente";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goHojaDeRutaDet")
    public String goHojaDeRutaDet(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";        
        List list = null;
        String pNuAnn, pNuEmi, pTipo;
        pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        pTipo = ServletUtility.getInstancia().loadRequestParameter(request, "ptipo");
        DocumentoExtRecepBean documentoExtRecepBean = null;
        try{
                documentoExtRecepBean = documentoExtRecepService.getDocumentoExtRecep(pNuAnn,pNuEmi);
                list = documentoExtRecepService.obtenerHojaDeRuta(pNuAnn,pNuEmi, pTipo);                

        }catch(Exception ex){
             mensaje = ex.getMessage();
        }
        if (list!=null) {          
            model.addAttribute("lstHojaDeRuta",list);
            model.addAttribute("documentoExtRecepBean",documentoExtRecepBean);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaHojaDeRutaDet";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusIniPrevio")
    public String goBuscaCiudadanoBusIniPrevio(HttpServletRequest request, Model model){
        
        CitizenBean citizenBean = new CitizenBean();
        
        model.addAttribute("citizenBean",citizenBean);
        
        return "/modalGeneral/consultaDestCiudadanoPrevio";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusIniNuevo")
    public String goBuscaCiudadanoBusIniNuevo(HttpServletRequest request, Model model){
        
        String sApePatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApePatCiudadano");
        String sApeMatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApeMatCiudadano");
        String sNombreCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sNombreCiudadano");
            
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sApePatCiudadano,sApeMatCiudadano,sNombreCiudadano));
        model.addAttribute("iniFuncionParm","4");
        
        return "/modalGeneral/consultaDestCiudadano";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusquedaPrevio")
    public String goBuscaCiudadanoBusquedaPrevio(HttpServletRequest request, Model model){
        
        CitizenBean citizenBean = new CitizenBean();
        
        model.addAttribute("citizenBean",citizenBean);
        
        return "/modalGeneral/consultaDestCiudadanoBusqPrevio";
    }
       
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusquedaNuevo")
    public String goBuscaCiudadanoBusquedaNuevo(HttpServletRequest request, Model model){
        
        String sApePatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApePatCiudadano");
        String sApeMatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApeMatCiudadano");
        String sNombreCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sNombreCiudadano");
            
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sApePatCiudadano,sApeMatCiudadano,sNombreCiudadano));
        model.addAttribute("iniFuncionParm","1");
        
        return "/modalGeneral/consultaDestCiudadano";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusDEPrevio")
    public String goBuscaCiudadanoBusDEPrevio(HttpServletRequest request, Model model){
        
         
        CitizenBean citizenBean = new CitizenBean();        
        model.addAttribute("citizenBean",citizenBean);        
        return "/modalGeneral/consultaDestCiudadanoBusqDEPrevio";

    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusDENuevo")
    public String goBuscaCiudadanoBusDENuevo(HttpServletRequest request, Model model){
        
        String sApePatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApePatCiudadano");
        String sApeMatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApeMatCiudadano");
        String sNombreCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sNombreCiudadano");
            
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sApePatCiudadano,sApeMatCiudadano,sNombreCiudadano));
        model.addAttribute("iniFuncionParm","2");
        return "/modalGeneral/consultaDestCiudadano";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusSEPrevio")
    public String goBuscaCiudadanoBusSEPrevio(HttpServletRequest request, Model model){
        
         
        CitizenBean citizenBean = new CitizenBean();        
        model.addAttribute("citizenBean",citizenBean);        
        return "/modalGeneral/consultaDestCiudadanoBusqSEPrevio";

    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusSENuevo")
    public String goBuscaCiudadanoBusSENuevo(HttpServletRequest request, Model model){
        
        String sApePatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApePatCiudadano");
        String sApeMatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApeMatCiudadano");
        String sNombreCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sNombreCiudadano");
            
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sApePatCiudadano,sApeMatCiudadano,sNombreCiudadano));
        model.addAttribute("iniFuncionParm","3");
        return "/modalGeneral/consultaDestCiudadano";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusquedaNuevoProy")
    public String goBuscaCiudadanoBusquedaNuevoProy(HttpServletRequest request, Model model){
        
        String sApePatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApePatCiudadano");
        String sApeMatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApeMatCiudadano");
        String sNombreCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sNombreCiudadano");
            
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sApePatCiudadano,sApeMatCiudadano,sNombreCiudadano));
        model.addAttribute("iniFuncionParm","6");
        
        return "/modalGeneral/consultaDestCiudadano";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusIniPrevioEmiResp")
    public String goBuscaCiudadanoBusIniPrevioEmiResp(HttpServletRequest request, Model model){
        
        CitizenBean citizenBean = new CitizenBean();
        
        model.addAttribute("citizenBean",citizenBean);
        
        return "/modalGeneral/consultaDestCiudadanoPrevioEmiResp";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadanoBusIniNuevoEmiResp")
    public String goBuscaCiudadanoBusIniNuevoEmiResp(HttpServletRequest request, Model model){
        
        String sApePatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApePatCiudadano");
        String sApeMatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApeMatCiudadano");
        String sNombreCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sNombreCiudadano");
            
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sApePatCiudadano,sApeMatCiudadano,sNombreCiudadano));
        model.addAttribute("iniFuncionParm","7");
        
        return "/modalGeneral/consultaDestCiudadano";
    }
    /*Enlace Expedientes - Hermes 17/09/2018*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goLstEnlaceExpediente")
    public String goLstEnlaceExpediente(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        //String ptiDoc = ServletUtility.getInstancia().loadRequestParameter(request, "ptiDoc");
        //String pnuExpediente = ServletUtility.getInstancia().loadRequestParameter(request, "pnroExpediente");
        //String ptiBusqueda = ServletUtility.getInstancia().loadRequestParameter(request, "ptiBusqueda");// rec doc recepcionado, emi doc emitido
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String pcoDepen = usuarioConfigBean.getCoDepMp();//usuario.getCoDep();        
        List list = null;
        //int cantExpEnl=0;
        try{
            //cantExpEnl = documentoExtRecepService.getExisteExpedientesEnlazados(pnuAnn, pnuEmi);
            
            list = documentoExtRecepService.getLstEnlaceExpedientes(pnuAnn,pnuEmi);          
        }catch(Exception ex){
             mensaje = ex.getMessage();
        }
        if (list!=null) {
            model.addAttribute("lstDocReferencia",list);
            //model.addAttribute("cantExpEnl",cantExpEnl);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaExpedientesEnlazados";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }    
    /*Enlace Expedientes - Hermes 17/09/2018*/
    /*-- INICIO [HPB] VALIDA DATOS DEL DOCUMENTO EN EXPEDIENTE 14/07/20--*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goVerificaExpedienteIgual")
    public @ResponseBody String goVerificaExpEquals(HttpServletRequest request, Model model){
        String result=""; 
        DocumentoExtRecepBean documentoExtRecepBean = new DocumentoExtRecepBean();
        String pcoTipDocAdm = ServletUtility.getInstancia().loadRequestParameter(request, "pcoTipDocAdm");
        String pdeDocSig = ServletUtility.getInstancia().loadRequestParameter(request, "pdeDocSig");
        String pcoOtros = ServletUtility.getInstancia().loadRequestParameter(request, "pcoOtros");
        String pnuDniAux = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDniAux");
        String pnuRucAux = ServletUtility.getInstancia().loadRequestParameter(request, "pnuRucAux");
        String ptiEmi = ServletUtility.getInstancia().loadRequestParameter(request, "ptiEmi");

        documentoExtRecepBean.setCoTipDocAdm(pcoTipDocAdm);
        documentoExtRecepBean.setDeDocSig(pdeDocSig);
        documentoExtRecepBean.setCoOtros(pcoOtros);
        documentoExtRecepBean.setNuDni(pnuDniAux);
        documentoExtRecepBean.setNuRuc(pnuRucAux);
        documentoExtRecepBean.setTiEmi(ptiEmi);
        
        System.out.println("pcoTipDocAdm: "+documentoExtRecepBean.getCoTipDocAdm());
        System.out.println("pdeDocSig: "+ documentoExtRecepBean.getDeDocSig());
        System.out.println("pcoOtros: "+ documentoExtRecepBean.getCoOtros());
        System.out.println("pnuDniAux: "+documentoExtRecepBean.getNuDni());
        System.out.println("pnuRucAux: "+documentoExtRecepBean.getNuRuc());
        System.out.println("ptiEmi: "+ documentoExtRecepBean.getTiEmi());
        try{
            result = documentoExtRecepService.getValidaExpEquals(documentoExtRecepBean);
        }catch(Exception e){
            e.getMessage();
        }       
        return result;
    }
    /*-- FIN [HPB] VALIDA DATOS DEL DOCUMENTO EN EXPEDIENTE 14/07/20--*/
    /* [HPB] Inicio 27/02/23 CLS-087-2022 */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goExportarArchivoHojaRutaLista")
    public @ResponseBody String goExportarArchivoHojaRutaLista(BuscarDocumentoExtRecepBean buscarDocumentoExtRecepBean, HttpServletRequest request,
            BindingResult result, Model model) throws JRException, FileNotFoundException, IOException 
    {    
        DocumentoExtRecepBean documentoExtRecepBean = null;
        ServletContext sc = request.getSession().getServletContext();
        String pNuAnn, pNuEmi, pTipo;
        pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        pTipo = ServletUtility.getInstancia().loadRequestParameter(request, "ptipo");
        String coReporte = ServletUtility.getInstancia().loadRequestParameter(request, "pformatRepor");

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String codDependencia = usuarioConfigBean.getCoDep();
        documentoExtRecepBean = documentoExtRecepService.getDocumentoExtRecep(pNuAnn,pNuEmi);
        
        String rutaReporte=sc.getRealPath("/reports/");
        String logo = applicationProperties.getLogoReporteB64();                
        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(logo));
        
        Map parametros = new HashMap();
        parametros.put("P_DE_DEPENDENCIA", usuarioConfigBean.getDeDep());
        parametros.put("P_USER", usuario.getCoUsuario());
        parametros.put("P_LOGO_DIR", bis.available()==0?null:bis);
        parametros.put("P_NU_ANN", pNuAnn);
        parametros.put("P_NU_EMI", pNuEmi);
        
        parametros.put("P_TI_DOCUMENTO", documentoExtRecepBean.getDeTipDocAdm());
        parametros.put("P_NU_DOCUMENTO", documentoExtRecepBean.getNuDoc());
        parametros.put("P_NU_EXPEDIENTE", documentoExtRecepBean.getNuExpediente());
        parametros.put("P_FE_EXPEDIENTE", documentoExtRecepBean.getFeExp());
        parametros.put("P_ASUNTO", documentoExtRecepBean.getDeAsu());
        //setter
        buscarDocumentoExtRecepBean.setCoDependencia(codDependencia);        
        buscarDocumentoExtRecepBean.setInMesaPartes(usuarioConfigBean.getInMesaPartes());     
        buscarDocumentoExtRecepBean.setTiAcceso(usuarioConfigBean.getTiConsultaMp());
        buscarDocumentoExtRecepBean.setCoEmpleado(usuario.getCempCodemp());
        buscarDocumentoExtRecepBean.setCoDependencia(usuario.getCoDep());
  
        buscarDocumentoExtRecepBean.setRutaReporteJasper(rutaReporte);
        buscarDocumentoExtRecepBean.setFormatoReporte(coReporte);
        
        ReporteBean objReporteBean = documentoExtRecepService.getGenerarReporteHojaRuta(buscarDocumentoExtRecepBean, parametros, pNuAnn, pNuEmi, pTipo);
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
    /* [HPB] Fin 27/02/23 CLS-087-2022 */
}
