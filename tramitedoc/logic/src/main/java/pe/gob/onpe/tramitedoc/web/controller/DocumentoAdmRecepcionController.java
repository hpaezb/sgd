package pe.gob.onpe.tramitedoc.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.libreria.exception.validarDatoException;
import pe.gob.onpe.libreria.util.ServletUtility;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoRecepBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaBean;
import pe.gob.onpe.tramitedoc.bean.TemaBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.CommonQryService;
import pe.gob.onpe.tramitedoc.service.DocumentoExtRecepService;
import pe.gob.onpe.tramitedoc.service.RecepDocumentoAdmService;
import pe.gob.onpe.tramitedoc.service.ReferencedData;
import pe.gob.onpe.tramitedoc.service.TemaService;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;
@Controller
@RequestMapping("/{version}/srDocumentoAdmRecepcion.do")
public class DocumentoAdmRecepcionController {

    @Autowired
    private RecepDocumentoAdmService recepDocumentoAdmService;
    
    @Autowired
    private ReferencedData referencedData;

    @Autowired
    private TemaService temaService; 
    
    @Autowired
    private DocumentoExtRecepService documentoExtRecepService;
    
    @Autowired
    private CommonQryService commonQryService;   /* HPB 20/02/2020 - Requerimiento Paginación recepcionados */
    
    private static Logger logger=Logger.getLogger("SGDDocumentoAdmRecepcionController");
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goInicio")
    public String goInicio(HttpServletRequest request, Model model){
        String annio = Utility.getInstancia().dateToFormatStringYYYY(new Date());
        String pEstado = ServletUtility.getInstancia().loadRequestParameter(request, "estadoDoc");//'0' documento no leido
        String pEtiqueta = ServletUtility.getInstancia().loadRequestParameter(request, "idEtiqueta");//'0' documento no leido
        String codDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "coDep");
        
        //jazanero
        String coBandeja = ServletUtility.getInstancia().loadRequestParameter(request, "coBandeja");
        //jazanero
        
        BuscarDocumentoRecepBean buscarDocumentoRecepBean = new BuscarDocumentoRecepBean();
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request); 
        TemaBean temabean =  new TemaBean();
        temabean.setCoDependencia(usuario.getCoDep());
        List<TemaBean> tema = temaService.getListTema(temabean);
        buscarDocumentoRecepBean.setsCoAnnio(annio);
        buscarDocumentoRecepBean.setsCoAnnioBus(annio);
        buscarDocumentoRecepBean.setsEstadoDoc(pEstado);
        buscarDocumentoRecepBean.setsCoDependencia(codDependencia);
        buscarDocumentoRecepBean.setIdEtiqueta(pEtiqueta);
        
        //jazanero
        if(coBandeja.equals("P")){ //otros
            buscarDocumentoRecepBean.setEsIncluyeProfesional(true);
        }
        if(coBandeja.equals("O")){ //encargado
            buscarDocumentoRecepBean.setEsIncluyeOficina(true);
        }
        if(coBandeja.equals("E")){ //personal
            buscarDocumentoRecepBean.setEsIncluyePersonal(true);
        }
        //jazanero
        //ADD SIS YPA
        if(coBandeja.equals("T")){ //encargado
           // buscarDocumentoRecepBean.setEsIncluyeOficina(true);
            buscarDocumentoRecepBean.setEsIncluyePersonal(true);
            buscarDocumentoRecepBean.setEsIncluyeProfesional(true);
        }
           
        recepDocumentoAdmService.estadoRecepcionDocumento(buscarDocumentoRecepBean,"");
        //model.addAttribute("recepDocumAdmList",recepDocumentoAdmService.getDocumentosRecepAdm(buscarDocumentoRecepBean));
        model.addAttribute(buscarDocumentoRecepBean);
        model.addAttribute("deListTema", tema);
        model.addAttribute("deAnnioList",referencedData.getAnnioList());
        model.addAttribute("deEstadosList",referencedData.getLstEstadosDocumentoRec("TDTV_DESTINOS"));
        model.addAttribute("dePrioridadesList",referencedData.getPrioridadesDocumentoList());
        model.addAttribute("deTipoDocumentoList",referencedData.getTipoDocumentoList(codDependencia));
        //model.addAttribute("deExpedienteList",referencedData.getExpedienteList(codDependencia));
        model.addAttribute("deEmisorList",referencedData.getLstEmisorDocExtRecep());
        model.addAttribute("deEtiquetasList",referencedData.getEtiquetasList());
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute("annioActual", Utility.getInstancia().dateToFormatStringYYYY(new Date()));
        return "/recepDocumentosAdm/recepDocumentoAdm";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goInicio")
    public String goInicio(BuscarDocumentoRecepBean buscarDocumentoRecepBean, HttpServletRequest request,  BindingResult result, Model model){
        String mensaje = "NO_OK";
        
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        buscarDocumentoRecepBean.setsCoDependencia(usuario.getCoDep());
        buscarDocumentoRecepBean.setsCoEmpleado(usuario.getCempCodemp());
        buscarDocumentoRecepBean.setsTiAcceso(usuarioConfigBean.getTiAcceso());
        
        List list = null;
        /* HPB 20/02/2020 - Inicio - Requerimiento Paginación recepcionados */
        List<Integer> lsCantidadPg = new ArrayList<Integer>();
        List listCantidad = null;
        String vPagina = "1";
        int paginas = 0;
        int paginasx = 0;
        vPagina = ServletUtility.getInstancia().loadRequestParameter(request, "vPagina");
        /* HPB 20/02/2020 - Fin - Requerimiento Paginación recepcionados */
        try{
            //list = recepDocumentoAdmService.getDocumentosBuscaRecepAdm(buscarDocumentoRecepBean);
            /* HPB 20/02/2020 - Inicio - Requerimiento Paginación recepcionados */
            listCantidad = recepDocumentoAdmService.getDocumentosBuscaRecepAdm2Size(buscarDocumentoRecepBean);
            String cantPorPagina = commonQryService.obtenerValorParametro("CANT_POR_PAGINA");
            int cantidad = Integer.parseInt(cantPorPagina);
            paginas = listCantidad.size() / cantidad;
            if(paginas == 0){
                paginas = paginas + 1;
            }else{
                paginasx = listCantidad.size() % cantidad;
                if(paginasx > 0)
                    paginas = paginas + 1;            
            }
            list = recepDocumentoAdmService.getDocumentosBuscaRecepAdm2(buscarDocumentoRecepBean, vPagina, cantidad);
            
            for (int i=1; i<=paginas; i++){
                lsCantidadPg.add(i);
            }
            /* HPB 20/02/2020 - Fin - Requerimiento Paginación recepcionados */            
        }catch(Exception ex){
            mensaje = ex.getMessage();
        }

        if (list!=null) {
            if (list.size()>=100) {
                //model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
            }
            /*HPB 20/02/2020 - Inicio - Requerimiento Paginación recepcionados*/
            model.addAttribute("vPagina", vPagina);
            model.addAttribute("vTotalPaginas", paginas);
            model.addAttribute("lsCantidadPg", lsCantidadPg);
            model.addAttribute("cantRegistros", listCantidad.size());
            /*HPB 20/02/2020 - Fin - Requerimiento Paginación recepcionados*/
            model.addAttribute("recepDocumAdmList",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        if (mensaje.equals("OK")) {
            return "/recepDocumentosAdm/tablaDocumentoAdm";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goEditarDocumento")
    public String goEditarDocumento(HttpServletRequest request, Model model){
       String mensaje = "NO_OK";
       String snuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String snuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
       String snuDes = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDes");        
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       String fechaHoraActual = Utility.getInstancia().dateToFormatString3(new Date());
       DocumentoBean documentoBean;
       ExpedienteBean expedienteBean;
       List list;
        System.out.println("snuEmi: "+snuEmi);
       try{
           documentoBean = recepDocumentoAdmService.getDocumentoRecepAdm(snuAnn, snuEmi, snuDes);
           /*[HPB-02/10/20] Inicio - Plazo de Atencion-*/
            if(documentoBean.getFePlaAte() != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf.parse(documentoBean.getFePlaAte());
                sdf.applyPattern("dd/MM/yyyy");
                documentoBean.setFePlaAte(sdf.format(d));
            }
           /*[HPB-02/10/20] Fin - Plazo de Atencion-*/
           expedienteBean = recepDocumentoAdmService.getExpDocumentoRecepAdm(documentoBean.getNuAnnExp(),documentoBean.getNuSecExp());
           if(expedienteBean!=null){
            documentoBean.setNuExpediente(expedienteBean.getNuExpediente());
            documentoBean.setNuAnnExp(expedienteBean.getNuAnnExp());
            documentoBean.setNuSecExp(expedienteBean.getNuSecExp());
            documentoBean.setFeExpCorta(expedienteBean.getFeExpCorta());
            documentoBean.setCoProceso(expedienteBean.getCoProceso());
            documentoBean.setDeProceso(expedienteBean.getDeProceso());
            /*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
            documentoBean.setCoGru(expedienteBean.getCoGru());
            documentoBean.setTiEmi(expedienteBean.getTiEmi());
            documentoBean.setNuOriEmi(expedienteBean.getNuOriEmi());
            /*HPB 11/02/2020 - Fin - Requerimiento Remitente expediente*/
           }
           list = recepDocumentoAdmService.getDocumentosRefRecepAdm(snuAnn, snuEmi);
           model.addAttribute("pfechaHoraActual",fechaHoraActual);
           model.addAttribute("pcodEmp",usuario.getCempCodemp());
           model.addAttribute("pdesEmp",usuario.getDeFullName());
           model.addAttribute("documentoRecepBean",documentoBean);
           model.addAttribute("expedienteBean",expedienteBean);
           model.addAttribute("refRecepDocumAdmList",list);
           mensaje = "OK";
       }catch(Exception ex){
          logger.error(snuAnn+":"+ snuEmi+":"+snuDes,ex);            
          mensaje = "Error en Editar Documento"; 
       }
       if (mensaje.equals("OK")) {
           return "/recepDocumentosAdm/editRecepDocumAdm";
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }       
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdDocumento")
    public @ResponseBody String goUpdDocumento(DocumentoBean documentoBean, HttpServletRequest request,  BindingResult result, Model model){
        String mensaje = "NO_OK";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        documentoBean.setCodUseMod(usuario.getCoUsuario());
        String sAccion = ServletUtility.getInstancia().loadRequestParameter(request, "vAccion");
        String sEsDocAnu = ServletUtility.getInstancia().loadRequestParameter(request, "vEsDocAnu");
        sEsDocAnu = sEsDocAnu.equals("") ? "A" : sEsDocAnu;
        String coRespuesta;
        String deRespuesta;
        
        try{
            mensaje = recepDocumentoAdmService.updDocumentoRecepAdm(documentoBean,sAccion,sEsDocAnu,usuario);
            //mensaje = recepDocumentoAdmService.updDocumentoRecepAdm(documentoBean,sAccion,usuario); servicio rest notificaciones movil
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
        retval.append("\",\"nuCorDes\":\"");
        retval.append(documentoBean.getNuCorDes());  
        retval.append("\",\"esDocRec\":\"");
        retval.append(documentoBean.getEsDocRec());
        retval.append("\",\"deEsDocRec\":\"");
        retval.append(documentoBean.getDeEsDocDes());
        retval.append("\"}");

        return retval.toString();         
    }    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaEmpleado")
    public String goBuscaEmpleado(HttpServletRequest request, Model model){
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        model.addAttribute("listaEmpleado",referencedData.getListEmpleadoDependencia(usuario.getCoDep()));
        return "/modalGeneral/consultaEmpleado";
    }    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaRemitente")
    public String goBuscaRemitente(HttpServletRequest request, Model model){
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        model.addAttribute("listaRemitente",referencedData.getListRemitente(pnuAnn,usuarioConfigBean));
        return "/modalGeneral/consultaRemitente";
    } 

        @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestinatario")
    public String goBuscaDestinatario(HttpServletRequest request, Model model){
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);        
        model.addAttribute("listaDestinatario",referencedData.getListDestinatario(pnuAnn,usuarioConfigBean.getCoDep(),usuarioConfigBean.getTiAcceso(),usuarioConfigBean.getCempCodemp()));    
        return "/modalGeneral/consultaDestinatario";
    }
        
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDocumentoEnReferencia")
    public String goBuscaDocumentoEnReferencia(BuscarDocumentoRecepBean buscarDocumentoRecepBean, HttpServletRequest request,  BindingResult result, Model model){
        String mensaje = "NO_OK";
        
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        buscarDocumentoRecepBean.setsCoDependencia(usuario.getCoDep());
        buscarDocumentoRecepBean.setsCoEmpleado(usuario.getCempCodemp());
        buscarDocumentoRecepBean.setsTiAcceso(usuarioConfigBean.getTiAcceso());

        HashMap mp = null;
        List list = null;
        /* HPB 20/02/2020 - Inicio - Requerimiento Paginación recepcionados */
        List<Integer> lsCantidadPg = new ArrayList<Integer>();
        List listCantidad = null;
        String vPagina = "1";
        int paginas = 0;
        int paginasx = 0;
        vPagina = ServletUtility.getInstancia().loadRequestParameter(request, "vPagina");
        /* HPB 20/02/2020 - Fin - Requerimiento Paginación recepcionados */        
        try{
            //mp = recepDocumentoAdmService.getDocumentosEnReferencia(buscarDocumentoRecepBean);
            
            /* HPB 20/02/2020 - Inicio - Requerimiento Paginación recepcionados */
            mp = recepDocumentoAdmService.getDocumentosEnReferenciaSize(buscarDocumentoRecepBean);
            String cantPorPagina = commonQryService.obtenerValorParametro("CANT_POR_PAGINA");
            listCantidad = (List) mp.get("recepDocumAdmList");
            int cantidad = Integer.parseInt(cantPorPagina);
            paginas = listCantidad.size() / cantidad;
            paginasx = listCantidad.size() % cantidad;
            if(paginasx > 0)
                paginas = paginas + 1;            
            mp = recepDocumentoAdmService.getDocumentosEnReferencia(buscarDocumentoRecepBean, vPagina, cantidad);
          
            for (int i=1; i<=paginas; i++){
                lsCantidadPg.add(i);
            }
            /* HPB 20/02/2020 - Fin - Requerimiento Paginación recepcionados */                
        }catch(Exception ex){
            mensaje = ex.getMessage();
        } 
        mensaje = String.valueOf(mp.get("msjResult"));
        if(mensaje.equals("1")){
            list = (List) mp.get("recepDocumAdmList");
            if(list!=null){
                if (list.size()>=100){
                    //model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
                }
                /*HPB 20/02/2020 - Inicio - Requerimiento Paginación recepcionados*/
                model.addAttribute("vPagina", vPagina);
                model.addAttribute("vTotalPaginas", paginas);
                model.addAttribute("lsCantidadPg", lsCantidadPg);
                model.addAttribute("cantRegistros", listCantidad.size());
                /*HPB 20/02/2020 - Fin - Requerimiento Paginación recepcionados*/                
                model.addAttribute("recepDocumAdmList",list);
                mensaje = "OK";                
            }
        }
        if (mensaje.equals("OK")) {
            return "/recepDocumentosAdm/tablaDocumentoAdm";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }          
     }        

    @RequestMapping(method = RequestMethod.POST, params = "accion=goVerAtendido")
    public @ResponseBody String goVerAtendido(DocumentoBean documentoBean, HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        String pnuDes = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDes");
                
        try{
            String vret  = recepDocumentoAdmService.getVerificaAtendido(pnuAnn, pnuEmi, pnuDes);
            if (vret==null || vret.equals("0")){
                mensaje = "OK";
            }else{
                mensaje = "NO_OK";
            }
        }catch (Exception e) { 
           e.printStackTrace(); 
        }         
        
        StringBuilder retval = new StringBuilder();
        retval.append("{\"retval\":\"");
        retval.append(mensaje);
        retval.append("\"}");

        return retval.toString();         
    }        
    @RequestMapping(method = RequestMethod.POST, params = "accion=goConsultaAtendido")
    public String goConsultaAtendido(BuscarDocumentoRecepBean buscarDocumentoRecepBean, HttpServletRequest request,  Model model){
        String mensaje = "NO_OK";
        
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        String pnuDes = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDes");
        List<ReferenciaBean> listSeg = null;
                
        try{
            listSeg = recepDocumentoAdmService.getConsultaAtendido(pnuAnn, pnuEmi, pnuDes);
        }catch (Exception e) { 
           e.printStackTrace(); 
        }         
        
        model.addAttribute("listAtendido", listSeg);
        
        return "/recepDocumentosAdm/consultaAtendido";
     }
    
     @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadano")
     public String goBuscaCiudadano(HttpServletRequest request, Model model){
        String sDescCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sDescCiudadano");
        model.addAttribute("lsDestCiudadano",documentoExtRecepService.getLstCiudadanos(sDescCiudadano));
        model.addAttribute("iniFuncionParm","5");
        return "/modalGeneral/consultaDestCiudadano";
     }
     
     @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaProveedorBus")
    public String goBuscaDestProveedorBus(HttpServletRequest request, Model model){
        String prazonSocial = ServletUtility.getInstancia().loadRequestParameter(request, "prazonSocial");
        model.addAttribute("lsDestProveedor",documentoExtRecepService.getLstProveedores(prazonSocial));
        model.addAttribute("iniFuncionParm","5");
        return "/modalGeneral/consultaDestProveedorBus";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaOtroOrigen")
    public String goBuscaOtroOrigen(HttpServletRequest request, Model model){        
        String pdesOtroOri = ServletUtility.getInstancia().loadRequestParameter(request, "pdesOtroOri");         
        model.addAttribute("lsDestOtroOrigen",documentoExtRecepService.getLstOtrosOrigenes(pdesOtroOri));
        model.addAttribute("iniFuncionParm","5");
        return "/modalGeneral/consultaDestOtroOrigenBus";        
    }

    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goCerrarDocumentoPlazoAtencion",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goCerrarDocumentoPlazoAtencion(@RequestBody String codigos,HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String coRespuesta;
        String deRespuesta;
        DocumentoBean documentoBean = new DocumentoBean();
        String[] partes = codigos.split(",");
        String nuAnio = partes[0];
        String nuEmision = partes[1];
        String inButtonRed = "1";
        documentoBean.setNuEmiDocOriPlazoAtencion(nuEmision);
        documentoBean.setNuAnnDocOriPlazoAtencion(nuAnio);
        documentoBean.setInResPlaAte("1");
        String resultJsonStr="";
        try{     
            mensaje = recepDocumentoAdmService.actualizarEstadoPlazoAtencion(documentoBean, inButtonRed);
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
        retval.append("}");

        return retval.toString();        
    }
    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
}
