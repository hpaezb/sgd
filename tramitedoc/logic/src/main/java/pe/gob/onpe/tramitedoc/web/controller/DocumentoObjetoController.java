package pe.gob.onpe.tramitedoc.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths; 
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.libreria.exception.validarDatoException;
import pe.gob.onpe.libreria.util.ServletUtility;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.AccionLog;
import pe.gob.onpe.tramitedoc.bean.AsignacionFuncionarioBean;
import pe.gob.onpe.tramitedoc.bean.AvanceBean;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoRecepMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.DescargaMensajeBean;
import pe.gob.onpe.tramitedoc.bean.DestiDocumentoEnvMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.DestinoResBen;
import pe.gob.onpe.tramitedoc.bean.DetalleEnvioDeCorreoBean;
import pe.gob.onpe.tramitedoc.bean.DocRespuestaBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoBeansContenedor;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoFileBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoRecepMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoVerBean;
import pe.gob.onpe.tramitedoc.bean.RemitosResBean;
import pe.gob.onpe.tramitedoc.bean.TemaBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.AnexoDocumentoService;
import pe.gob.onpe.tramitedoc.service.AvanceService;
import pe.gob.onpe.tramitedoc.service.CommonQryService;
import pe.gob.onpe.tramitedoc.service.DocumentoBasicoService;
import pe.gob.onpe.tramitedoc.service.DocumentoExtRecepService;
import pe.gob.onpe.tramitedoc.service.DocumentoMensajeriaService;
import pe.gob.onpe.tramitedoc.service.DocumentoObjService;
import pe.gob.onpe.tramitedoc.service.EmiDocumentoAdmService;
import pe.gob.onpe.tramitedoc.service.SmtpSendService;
import pe.gob.onpe.tramitedoc.service.TemaService;
import pe.gob.onpe.tramitedoc.service.impl.ConsultaEmiDocServiceImp;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.SmtpObject;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/*interoperabilidad*/
import pe.gob.segdi.pide.wsentidad.WsEntidadInter;
import pe.gob.onpe.tramitedoc.dao.DatosPlantillaDao;
import pe.gob.onpe.tramitedoc.bean.DatosInterBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;
import pe.gob.onpe.tramitedoc.service.ActionLogService;
import pe.gob.onpe.tramitedoc.service.DocumentoMensajesService;
/*interoperabilidad*/

@Controller
@RequestMapping("/{version}/srDocObjeto.do")
@SessionAttributes(value = {"docSession"})
public class DocumentoObjetoController {

    @Autowired
    private DocumentoObjService documentoObjService;
     @Autowired
    private EmiDocumentoAdmService EmiDocumentoAdmService;
    @Autowired
    private AnexoDocumentoService anexoDocumentoService;
    @Autowired
    private DocumentoBasicoService documentoBasicoService;
    @Autowired
    private DocumentoExtRecepService documentoExtRecepService;    
    
    @Autowired
    private CommonQryService commonQryService; 
    
    @Autowired
    private ApplicationProperties applicationProperties;   
    
    @Autowired    
    private DocumentoMensajeriaService documentoMensajeriaService;
    
    @Autowired
    private TemaService temaService; 
    
    @Autowired
    private AvanceService avanceService; 
    
    @Autowired
    private SmtpSendService smtpSendService;
    
    @Autowired
    private DocumentoMensajesService documentoMensajesService;
  
    /*interoperabilidad*/
    @Autowired
    private DatosPlantillaDao datosPlantillaDao;
    /*interoperabilidad*/   
    
    @Autowired
    private ActionLogService actionLogService;
     
    @RequestMapping(method = RequestMethod.POST, params ="accion=goDocRutaAbrir")
    private @ResponseBody String goObtieneDocumento(HttpServletRequest request, Model model) throws Exception {
        String retval = "";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        DocRespuestaBean retRespuesta = new DocRespuestaBean();

        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");       
        /*------Hermes 14/05/2019-----*/
        String coUsu=usuario.getCoUsuario();
        String resultado = anexoDocumentoService.getDocAutorizado(coUsu, pNuAnn, pNuEmi, usuario.getCoDep());
        /*------Hermes 14/05/2019-----*/
         /*-----Hermes - Log 28/05/19----------------------------*/
        String coOpcion = ServletUtility.getInstancia().loadRequestParameter(request, "coOpcion");
        String coAccion = ServletUtility.getInstancia().loadRequestParameter(request, "coAccion");
        String coEstado = ServletUtility.getInstancia().loadRequestParameter(request, "coEstado");
        String pNuDes = ServletUtility.getInstancia().loadRequestParameter(request, "nuDes");
        System.out.println("pCoOpcion->"+coOpcion+" coAccion->"+coAccion+ " coEstado->"+coEstado+" pNuDes->"+pNuDes);
         /*-----Hermes - Log 28/05/19----------------------------*/
        if(resultado.equals("1") || resultado=="1"){
            if (pNuAnn != null && pNuEmi != null && pTiOpe != null) {
                try {
                    /*--Hermes - Log 28/05/19----------------------------*/
                    String newCoEstado ="";
                    if(!coOpcion.equals("") && !coEstado.equals("") && !pNuDes.equals("")){
                        newCoEstado = coEstado.substring(0, 1);
                    }
                    AccionLog accionLog = new AccionLog();
                    SiElementoBean siElementoBean = new SiElementoBean();
                    System.out.println("pCoOpcion->"+coOpcion);
                    System.out.println("pCoEstado->"+newCoEstado);
                    if(!coOpcion.equals("") && !newCoEstado.equals("") && !pNuDes.equals("")){
                        //accionLog = actionLogService.getIndicadorMenu(coOpcion);
                        siElementoBean = actionLogService.getConfigLog(coOpcion, newCoEstado);
                        if(siElementoBean!=null){
                            accionLog.setcOpcion(coOpcion);
                            accionLog.setcAccion(coAccion);
                            accionLog.setcNuAnn(pNuAnn);
                            accionLog.setvNuEmi(pNuEmi);
                            accionLog.setcNuDes(pNuDes);

                            String retorno = actionLogService.insActionLog(accionLog, usuario);
                        }
                    }                   
                    /*--Hermes - Log 28/05/19------------------------------*/
                    DocumentoVerBean docVerBean = new DocumentoVerBean();
                    docVerBean = documentoObjService.getNombreDoc(pNuAnn, pNuEmi, pTiOpe, usuario);

                    retRespuesta.setRetval(docVerBean.getDeMensaje());
                    retRespuesta.setNuAnn(docVerBean.getNuAnn());
                    retRespuesta.setNuEmi(docVerBean.getNuEmi());
                    retRespuesta.setNoDoc(docVerBean.getNoDocumento());
                    retRespuesta.setNoFirma(docVerBean.getNoFirma());
                    retRespuesta.setNoUrl(docVerBean.getUrlDocumento());

                } catch (Exception e) {
                    retRespuesta.setRetval("Error en Obtener nombre del documento");
                    e.printStackTrace();
                }

                try {
                    Writer strWriter = new StringWriter();
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(strWriter, retRespuesta );
                    retval = strWriter.toString();   
                } catch (Exception e) {
                    retval = "";
                }
            }        
        }else{
            try {
                retRespuesta.setRetval("Documento debe estar en estado EMITIDO para poder visualizarlo.");
                Writer strWriter = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(strWriter, retRespuesta );
                retval = strWriter.toString();   
            } catch (Exception e) {
                retval = "";
            }
        }
        return retval;
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goRutaGeneraDocx")
    private @ResponseBody String goRutaGeneraDocx(HttpServletRequest request, Model model) throws Exception {
        String retval = "";
        System.out.println("goRutaGeneraDocx---> ");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        DocRespuestaBean retRespuesta = new DocRespuestaBean();
        DocumentoObjBean documentoObjBean = null;

        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");
        String pPlantilla = ServletUtility.getInstancia().loadRequestParameter(request, "plantilla");
        
        pPlantilla=pPlantilla==null? "NO":pPlantilla;
        

        if (pNuAnn != null && pNuEmi != null && pTiOpe != null) {
            try {

                DocumentoVerBean docVerBean = new DocumentoVerBean();
                
                if(pPlantilla.equals("SI")){
                    //plantilla
                    docVerBean = documentoObjService.getNombreGeneraDocxProyecto(pNuAnn, pNuEmi, pTiOpe);
                }else{
                    docVerBean = documentoObjService.getNombreGeneraDocx(pNuAnn, pNuEmi, pTiOpe);
                }                
                //jazanero
                documentoObjBean = documentoObjService.getNombreArchivo(pNuAnn, pNuEmi, pTiOpe);
                
                retRespuesta.setRetval(docVerBean.getDeMensaje());
                retRespuesta.setNuAnn(docVerBean.getNuAnn());
                retRespuesta.setNuEmi(docVerBean.getNuEmi());
                retRespuesta.setNoDoc(docVerBean.getNoDocumento());
                retRespuesta.setNoFirma(docVerBean.getNoFirma());
                retRespuesta.setNoUrl(docVerBean.getUrlDocumento());                
                
                //jazanero
                if(documentoObjBean!=null){
                    retRespuesta.setTieneWord((documentoObjBean.getwNombreArchivo()!=null && documentoObjBean.getwNombreArchivo().length()>0)? "SI":"NO");
                    retRespuesta.setUsuario(documentoObjBean.getUsuario()!=null? documentoObjBean.getUsuario():"");
                    retRespuesta.setTipoDocumentoPrincipal(documentoObjBean.getTipoDoc());
                }else{
                    retRespuesta.setTieneWord("NO");
                    retRespuesta.setUsuario("");
                    retRespuesta.setTipoDocumentoPrincipal("archivo");
                }
                
            } catch (Exception e) {
                retRespuesta.setRetval("Error en Obtener nombre del documento");
                e.printStackTrace();
            }

            try {
                Writer strWriter = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(strWriter, retRespuesta );
                retval = strWriter.toString();   
            } catch (Exception e) {
                retval = "";
            }

        }
        System.out.println("retRespuesta--> "+ retval.toString());
        return retval;
    }
    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goDocRutaAnexo")
    private @ResponseBody String goObtieneDocumentoAnexo(HttpServletRequest request, Model model) throws Exception {
        String retval = "";
        DocRespuestaBean retRespuesta = new DocRespuestaBean();
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);

        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pNuAne = ServletUtility.getInstancia().loadRequestParameter(request, "nuAne");

        if (pNuAnn != null && pNuEmi != null && pNuAne != null) {
            try {

                DocumentoVerBean docVerBean = new DocumentoVerBean();
                docVerBean = documentoObjService.getNombreDocAnexo(pNuAnn, pNuEmi, pNuAne);

                retRespuesta.setRetval(docVerBean.getDeMensaje());
                retRespuesta.setNuAnn(docVerBean.getNuAnn());
                retRespuesta.setNuEmi(docVerBean.getNuEmi());
                retRespuesta.setNuAne(docVerBean.getNuAne());
                retRespuesta.setNoDoc(docVerBean.getNoDocumento());
                retRespuesta.setNoUrl(docVerBean.getUrlDocumento());
                
            } catch (Exception e) {
                retRespuesta.setRetval("Error en Obtener nombre del Anexo");
                e.printStackTrace();
            }
            try {
                Writer strWriter = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(strWriter, retRespuesta );
                retval = strWriter.toString();   
            } catch (Exception e) {
                retval = "";
            }
            

        }
        return retval;
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goDocSeguimiento")
    public String goDocSeguimiento(HttpServletRequest request, Model model) {
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pkNuSecDevolucion = ServletUtility.getInstancia().loadRequestParameter(request, "pkNuSecDevolucion");/*12/09/2019 HPB Devolucion*/
        String pkExp = null;
        int cantExpEnl = 0;/*-----Hermes Enlace de expedientes 17/09/2018-----*/
        /*[HPB-02/10/20] Inicio - Plazo de Atencion-*/
        String pNuAnn = pkEmi.substring(0, 4);
        String pNuEmi = pkEmi.substring(4, 14);
        RemitosResBean docRemitoBean = documentoBasicoService.getRemitoResumen(pNuAnn, pNuEmi);
        /*[HPB-02/10/20] Fin - Plazo de Atencion-*/
        try {
            pkExp = documentoExtRecepService.getPkExpDocExtOrigen(pkEmi);
            
            cantExpEnl = documentoExtRecepService.getExisteExpedientesEnlazados(pkEmi);/*-----Hermes Enlace de expedientes 17/09/2018-----*/
            
        } catch (Exception e) {
            e.printStackTrace();
        }    
        model.addAttribute("pCoEmpEmi", docRemitoBean.getCoEmpEmi());/*[HPB-02/10/20] Inicio - Plazo de Atencion-*/
        model.addAttribute("frm_docOrigenBean_pkExp", pkExp);        
        model.addAttribute("pkEmiDoc", pkEmi);
        model.addAttribute("pkNuSecDevolucion", pkNuSecDevolucion);/*12/09/2019 HPB Devolucion*/
        model.addAttribute("cantExpEnl",cantExpEnl);/*-----Hermes Enlace de expedientes 17/09/2018-----*/
        /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente--*/
        //model.addAttribute("pNuEmiPadre", pkEmi);
        /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente--*/
        return "/modalGeneral/consultaSeguimiento";
    }

    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goVincularTema")
    public String goVincularTema(HttpServletRequest request, Model model) {
        
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String tipoRecep = ServletUtility.getInstancia().loadRequestParameter(request, "tipoRecep");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request); 
        
        TemaBean temabean =  new TemaBean();
        temabean.setCoDependencia(usuario.getCoDep());
        List<TemaBean> tema = temaService.getListTema(temabean);
        String codTema="";
        if(tipoRecep.equals("RECEPCION")){
            codTema = temaService.getEditTemaDestinos(pkEmi);         
        }
        if(tipoRecep.equals("EMISION")){
            codTema = temaService.getEditTemaRemitos(pkEmi);        
        }
        temabean.setCoTema(codTema);
        model.addAttribute("deListTema", tema);
        model.addAttribute("temaBean", temabean);
        model.addAttribute("codigoEmision", pkEmi);
        model.addAttribute("codigoTipo", tipoRecep);
        return "/modalGeneral/registroVincularTema";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goGrabarTema",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goGrabarTema(HttpServletRequest request, Model model){ 
        String mensaje = "NO_OK";
        String codigoTema = ServletUtility.getInstancia().loadRequestParameter(request, "coTema");
        String codigoEmision = ServletUtility.getInstancia().loadRequestParameter(request, "codigoEmision");
        String codTipo = ServletUtility.getInstancia().loadRequestParameter(request, "codTipo");
        String coRespuesta;
        String deRespuesta;         
        try{   
            if(codTipo.equals("EMISION"))
            mensaje = temaService.updTemaRemitos(codigoEmision,codigoTema);  
            if(codTipo.equals("RECEPCION"))
            mensaje = temaService.updTemaDestinos(codigoEmision,codigoTema);      
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
        retval.append("}");

        return retval.toString();        
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goVerAvance")
    public String goVerAvance(HttpServletRequest request, Model model) {
             
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn"); 
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi"); 
        String pnuDes = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDes"); 
        AvanceBean avance= new AvanceBean();
        avance.setTiAvance("RECEP");
        avance.setNuAnn(pnuAnn);
        avance.setNuEmi(pnuEmi);
        avance.setNuDes(pnuDes);        
        List<AvanceBean> ListAvance = avanceService.getListAvance(avance);        
        model.addAttribute("recepAvanceList", ListAvance); 
        model.addAttribute("tipoAvance", "RECEP"); 
        model.addAttribute("pnuAnn", pnuAnn); 
        model.addAttribute("pnuEmi", pnuEmi); 
        model.addAttribute("pnuDes", pnuDes); 
        return "/modalGeneral/registroAvance";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goVerAvanceConsulta")
    public String goVerAvanceConsulta(HttpServletRequest request, Model model) {
             
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn"); 
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi"); 
        String pnuDes = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDes"); 
        AvanceBean avance= new AvanceBean(); 
        avance.setNuAnn(pnuAnn);
        avance.setNuEmi(pnuEmi);
        avance.setNuDes(pnuDes);
        avance.setTiAvance("RECEP");        
        List<AvanceBean> ListAvance = avanceService.getListAvance(avance);        
        model.addAttribute("recepAvanceList", ListAvance); 
        model.addAttribute("pnuAnn", pnuAnn); 
        model.addAttribute("pnuEmi", pnuEmi); 
        model.addAttribute("pnuDes", pnuDes); 
        return "/modalGeneral/consultaAvance";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goGrabarAvance",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goGrabarAvance(HttpServletRequest request, Model model){ 
        String mensaje = "NO_OK";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);     
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn"); 
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi"); 
        String pnuDes = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDes"); 
        String tipoAvance = ServletUtility.getInstancia().loadRequestParameter(request, "tipAvance"); 
        String desAvance = ServletUtility.getInstancia().loadRequestParameter(request, "desAvance"); 
        AvanceBean avance= new AvanceBean();
        avance.setTiAvance(tipoAvance);
        avance.setNuAnn(pnuAnn);
        avance.setNuEmi(pnuEmi);
        avance.setNuDes(pnuDes);
        avance.setCoUser(usuario.getCoUsuario());
        avance.setDeAvance(desAvance);
        String coRespuesta;
        String deRespuesta;         
        try{                
            mensaje = avanceService.insertAvance(avance);      
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
        retval.append("}");

        return retval.toString();        
    }
    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goDocAnexo")
    public String goDocAnexo(HttpServletRequest request, Model model) {
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        model.addAttribute("pkEmiDoc", pkEmi);
        return "/modalGeneral/consultaAnexos";
    }
    
     @RequestMapping(method = RequestMethod.POST, params = "accion=goDocEnviarNotificacion")
    public String goDocEnviarNotificacion(HttpServletRequest request, Model model) {
        
        //String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        String ptiEnvMsj = ServletUtility.getInstancia().loadRequestParameter(request, "ptiEnvMsj");
        String pexisteDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pexisteDoc");
        String pexisteAnexo = ServletUtility.getInstancia().loadRequestParameter(request, "pexisteAnexo");
        String tipoBandeja = ServletUtility.getInstancia().loadRequestParameter(request, "tipoBandeja");
        
       DocumentoEmiBean documentoEmiBean = EmiDocumentoAdmService.getDocumentoEmiAdm(pnuAnn,pnuEmi);
        
//jazanero
        //agregar logica para mostrar modal
        //para saber si viene de libro de reclamaciones
         System.out.println("pnuAnn->"+pnuAnn);
         System.out.println("pnuEmi->"+pnuEmi);
         System.out.println("tipoBandeja->"+tipoBandeja);
         System.out.println("ptiEnv->"+ptiEnvMsj);
                  
         //metodo
         DetalleEnvioDeCorreoBean detalleEnvioDeCorreoBean = EmiDocumentoAdmService.getDetalleEnvioCorreo(pnuAnn, pnuEmi);
                          
        //model.addAttribute("pkEmiDoc", pkEmi);
        model.addAttribute("pnuAnn", pnuAnn);
        model.addAttribute("pnuEmi", pnuEmi);
        model.addAttribute("ptiEnv", ptiEnvMsj);
        
        model.addAttribute("pexisteDoc", pexisteDoc);
        model.addAttribute("pexisteAnexo", pexisteAnexo);
        model.addAttribute("tipoBandeja", tipoBandeja);
        
        /*ADD YPA SIS MENSAJERIA*/
         List<SiElementoBean> listMensajeria = commonQryService.getLsDepenciaMensjeria();
        model.addAttribute("MensajeriaList",listMensajeria);
       // model.addAttribute("pnuDes", pnuDes);
       
       String c="0";
       String a="";
       String b="";
       for(int i=0; i<listMensajeria.size();i++){
           a=listMensajeria.get(i).getCeleDescor();
           b=documentoEmiBean.getCoDepEmi();
           if(a.equals(b)){
               c="1";
           }
       }
       
       model.addAttribute("pCodDepUsuario", documentoEmiBean.getCoDepEmi());
       model.addAttribute("pbDepMsjUsuario", c);
       model.addAttribute("pCodDepDocMsj", documentoEmiBean.getCoDepMsj());
       
       
       
       /*END ADD YPA SIS MENSAJERIA*/
       
       //nuevo   
        model.addAttribute("pcomboestado", "00");
        model.addAttribute("pdisplay", "block");
        model.addAttribute("penviarCorreo", "00");
        if(detalleEnvioDeCorreoBean!=null){
            if(detalleEnvioDeCorreoBean.getCodtipoexp()!=null && detalleEnvioDeCorreoBean.getCodtipoexp().equals("65")){
                //if(detalleEnvioDeCorreoBean.getAutcorreo()!=null && detalleEnvioDeCorreoBean.getAutcorreo().equals("1")){
                    if(detalleEnvioDeCorreoBean.getCorreoexp()!=null && detalleEnvioDeCorreoBean.getCorreoexp().length()>0){
                        /*----Hermes 22/10/18 - Validar cuando el ciudadano pide se le notifique por correo--Se comentaron 4 lineas siguientes*/
                        /*model.addAttribute("pcomboestado", "05");
                        model.addAttribute("pdisplay", "none");
                        model.addAttribute("penviarCorreo", "01");
                        model.addAttribute("pCorreo", detalleEnvioDeCorreoBean.getCorreoexp());*/
                        /*----Hermes 22/10/18 - Validar cuando el ciudadano pide se le notifique por correo*/
                    }
                //}
            }
        }        
       
        /*interoperabilidad*/       
        DatosInterBean datosInter=new DatosInterBean();         

        String vDeDepDes="";
        String vDeNomDes="";
        String vDeCarDes="";              
        String vResult="N"; 
        
        datosInter=commonQryService.DatosInter(pnuAnn,pnuEmi);
        
        if (datosInter!=null){
            if(datosInter.getNuRucDes().length()>0 && !datosInter.getNuRucDes().equals("0")){
                String inEntidadInteropera = commonQryService.obtenerIndicadorEntidadPide(datosInter.getNuRucDes());/*[HPB] 06/11/20 Modificaciones en el envío de documentos. Listado de entidades que interoperan*/           
                WsEntidadInter wSEntidad = new WsEntidadInter();
                    try {
                        //vResult= wSEntidad.getInter(datosInter.getNuRucDes(),datosPlantillaDao.getParametros("URL_ENTIDADES_INTER"));
                        if(inEntidadInteropera.equals("1")){
                            vResult="S"; 
                            vDeDepDes=datosInter.getDeDepDes();
                            vDeNomDes=datosInter.getDeNomDes();
                            vDeCarDes=datosInter.getDeCarDes();  
                        }
                   }catch(Exception e){
                       vResult = e.getMessage();
                   }
            }            
        }    
       model.addAttribute("psiInter",vResult);
       model.addAttribute("pDeDepDes",vDeDepDes);
       model.addAttribute("pDeNomDes",vDeNomDes);
       model.addAttribute("pDeCarDes",vDeCarDes);       
       /*interoperabilidad*/
         System.out.println("vResult: "+vResult);
         System.out.println("ptiEnvMsj: "+ ptiEnvMsj);
        //return "/modalGeneral/consultaEnvioNotificacion";
        if (vResult.equals("N"))
            return "/modalGeneral/consultaEnvioNotificacionMP";/*[HPB] 06/11/20 Modificaciones en el envío de documentos. Listado de entidades que interoperan*/
        else
            return "/modalGeneral/consultaEnvioNotificacionMV";/*[HPB] 06/11/20 Modificaciones en el envío de documentos. Listado de entidades que interoperan*/
    }
     //YUAL
    @RequestMapping(method = RequestMethod.POST, params = "accion=goDocEnviarNotificacionPopUp")
    public String goDocEnviarNotificacionPopUp(HttpServletRequest request, Model model){
        DocumentoVerBean docSession =Utilidades.getInstancia().loadDocFromSession(request);
       
        model.addAttribute("pnuAnn", docSession.getNuAnn());
        model.addAttribute("pnuEmi", docSession.getNumeroDoc());
        model.addAttribute("MensajeriaList",commonQryService.getLsDepenciaMensjeria());
       // model.addAttribute("pnuDes", pnuDes);
        
        return "/modalGeneral/consultaEnvioNotificacion";
    }

    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goArDocMsjEnviados")
    public String goArDocMsjEnviados(HttpServletRequest request, Model model) {
        //String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        //String pnuDes = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDes");
       DocumentoEmiBean documentoEmiArBean=new DocumentoEmiBean();
        //model.addAttribute("pkEmiDoc", pkEmi);
        
        DescargaMensajeBean descargaMensajeBean = new DescargaMensajeBean();        
        descargaMensajeBean.setNu_ann(pnuAnn);
        descargaMensajeBean.setNu_emi(pnuEmi);
        
        String respuesta = documentoMensajesService.deleteMsjAdicional(descargaMensajeBean); 
        
        List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosMsjList(pnuAnn, pnuEmi);   
        
        if(docAnexoList.size()>0)
            model.addAttribute("pnuDes", docAnexoList.get(0).getNuDes());
        model.addAttribute("pnuAnn", pnuAnn);
        model.addAttribute("pnuEmi", pnuEmi);
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute(documentoEmiArBean);
        //model.addAttribute("pnuDes", pnuDes);
        
        return "/modalGeneral/archivarDocMsj";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdArchivarDoc")
     public String goUpdDescargarMensaje(@RequestBody DocumentoEmiBean documentoEmiBean, HttpServletRequest request,Model model) {
        String mensaje = "NO_OK";
        
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coUsu=usuario.getCoUsuario();          
                       
                      
        try {
            String respuesta = EmiDocumentoAdmService.updArchivarDocumento(documentoEmiBean,usuario);                       
            
            mensaje = respuesta;
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (mensaje.equals("OK")) {
            model.addAttribute("pMensaje", "Datos guardados.");
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
        
    }
    
    
    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goCargarDocumentosAnexos")
    public String goCargarDocumentosAnexos(HttpServletRequest request, Model model) {
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        model.addAttribute("pkEmiDoc", pkEmi);
        //model.addAttribute("listaDestinatario",referencedData.getListDestinatario(pnuAnn,usuarioConfigBean.getCoDep()));    
        return "/modalGeneral/consultaCargarAnexos";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goDetalleDocAnexo")
    public String goDetalleDocAnexo(HttpServletRequest request, Model model) {
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String mensaje = "NO_OK";

        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;

        if (pkEmi != null) {

            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            try {
                RemitosResBean docRemitoBean = documentoBasicoService.getRemitoResumen(pNuAnn, pNuEmi);
                DestinoResBen docDestinoBean = documentoBasicoService.getDestinoResumen(pNuAnn, pNuEmi, pNuDes);
                List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosList(pNuAnn, pNuEmi);

                if (docRemitoBean.getFeEmi()!=null)
                    docRemitoBean.setFeEmi(docRemitoBean.getFeEmi().substring(0,19));
                
                model.addAttribute("docRemitoBean", docRemitoBean);
                model.addAttribute("docDestinoBean", docDestinoBean);
                model.addAttribute("docAnexoList", docAnexoList);
                /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
                model.addAttribute("cantidadAnexos", docAnexoList.size());
                /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
                mensaje = "OK";

            } catch (Exception e) {
                e.printStackTrace();
                mensaje = e.getMessage();
            }
        } else {
            mensaje = "Faltan Datos";
        }
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaAnexosDetalle";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }

    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goDetalleCargarAnexo")
    public String goDetalleCargarAnexo(HttpServletRequest request, Model model) {
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String mensaje = "NO_OK";

        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;
        String tamanioMaxAnexos = commonQryService.obtenerValorParametro("CAP_MAXIMA_ANEXOS");/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
        if (pkEmi != null) {

            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            try {
                RemitosResBean docRemitoBean = documentoBasicoService.getRemitoResumen(pNuAnn, pNuEmi);
                DestinoResBen docDestinoBean = documentoBasicoService.getDestinoResumen(pNuAnn, pNuEmi, pNuDes);
                List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosList(pNuAnn, pNuEmi);

                model.addAttribute("docRemitoBean", docRemitoBean);
                model.addAttribute("docDestinoBean", docDestinoBean);
                model.addAttribute("docAnexoList", docAnexoList);
                model.addAttribute("tamanioMaxAnexos", tamanioMaxAnexos);/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
                mensaje = "OK";

            } catch (Exception e) {
                e.printStackTrace();
                mensaje = e.getMessage();
            }
        } else {
            mensaje = "Faltan Datos";
        }
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaCargarAnexosDetalle";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }

    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListadoArchivosAnexos")
    public String goListadoArchivosAnexos(HttpServletRequest request, Model model) {
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String vPublico = ServletUtility.getInstancia().loadRequestParameter(request, "vPublico");
        String mensaje = "NO_OK";

        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;

        if (pkEmi != null) {

            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            try {
                RemitosResBean docRemitoBean = documentoBasicoService.getRemitoProyecto(pNuAnn, pNuEmi);
                
                if(docRemitoBean!=null && docRemitoBean.getCoTipDocAdm().equals("332")){
                    model.addAttribute("esProyecto", "1");
                }else{
                    model.addAttribute("esProyecto", "0");
                }
                model.addAttribute("esPublico", vPublico);
                List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosList(pNuAnn, pNuEmi);
                model.addAttribute("docAnexoList", docAnexoList);                
                mensaje = "OK";
                //jazanero
            } catch (Exception e) {
                e.printStackTrace();
                mensaje = e.getMessage();
            }
        } else {
            mensaje = "Faltan Datos";
        }
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaCargarAnexosListado";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }

    }
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListadoArchivosAnexosModoLectura")
    public String goListadoArchivosAnexosModoLectura(HttpServletRequest request, Model model) {
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String mensaje = "NO_OK";

        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;

        if (pkEmi != null) {

            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            try {
                List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosList(pNuAnn, pNuEmi);
                model.addAttribute("docAnexoList", docAnexoList);
                mensaje = "OK";

            } catch (Exception e) {
                e.printStackTrace();
                mensaje = e.getMessage();
            }
        } else {
            mensaje = "Faltan Datos";
        }
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaCargarAnexosListadoModoLectura";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }

    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goListadoMsjAnexos")
    public String goListadoMsjAnexos(HttpServletRequest request, Model model) {
        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String mensaje = "NO_OK";
        String tamanioMaxAnexos = commonQryService.obtenerValorParametro("CAP_MAXIMA_ANEXOS");/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
        /*String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;*/

        if (pNuAnn != null) {
            /*System.out.println(pNuAnn);
            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);*/
            try {
                List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosMsjList(pNuAnn, pNuEmi);
                model.addAttribute("docAnexoMsjList", docAnexoList);
                model.addAttribute("tamanioMaxAnexos", tamanioMaxAnexos);/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
                mensaje = "OK";

            } catch (Exception e) {
                e.printStackTrace();
                mensaje = e.getMessage();
            }
        } else {
            mensaje = "Faltan Datos";
        }
        
        if (mensaje.equals("OK")) {
            return "/modalGeneral/archivarDocMsjListado";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }

    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goDetalleSeguimiento")
    public String goDetalleSeguimiento(HttpServletRequest request, Model model) {
        System.out.println("goDetalleSeguimiento--> ");
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pkNuSec = ServletUtility.getInstancia().loadRequestParameter(request, "pkNuSec");/*12/09/2019 HPB Devolucion*/
        String pkExp = ServletUtility.getInstancia().loadRequestParameter(request, "pkExp");   
        String pkCoEmpEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkCoEmpEmi");/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
        String mensaje = "NO_OK";
        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;
        int cantExpEnl = 0;/*-----Hermes Enlace de expedientes 17/09/2018-----*/
        /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente --*/
//        String pNuAnnPadre = null;
//        String pNuEmiPadre = null;
//        String pNuDesPadre = null;
//        String pkExpPadre = ServletUtility.getInstancia().loadRequestParameter(request, "pkExpPadre");
//        System.out.println("pkExpPadre--> "+pkExpPadre);
        /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente --*/
        if (pkEmi != null) {

            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente --*/
//            if (pkExpPadre != null) {
//                pNuAnnPadre = pkExpPadre.substring(0, 4);
//                pNuEmiPadre = pkExpPadre.substring(4, 14);
//                pNuDesPadre = pkExpPadre.substring(14);
//            }
            /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente --*/
            try {
                
                cantExpEnl = documentoExtRecepService.getExisteExpedientesEnlazados(pkEmi);/*-----Hermes Enlace de expedientes 17/09/2018-----*/
                RemitosResBean docRemitoBean=null;
                if (pkNuSec.equals("0")){
                    docRemitoBean = documentoBasicoService.getRemitoResumen(pNuAnn, pNuEmi);
                }else{
                    docRemitoBean = documentoBasicoService.getRemitoResumenDevolucion(pNuAnn, pNuEmi, pkNuSec);/*12/09/2019 HPB Devolucion*/
                }
                                
                DestinoResBen docDestinoBean = documentoBasicoService.getDestinoResumen(pNuAnn, pNuEmi, pNuDes);
                
                List<DocumentoAnexoBean> docAnexoListMsj = documentoMensajesService.getAnexosListMsj(pNuAnn, pNuEmi, docDestinoBean.getNuDes());
                
                List<DocumentoRecepMensajeriaBean> docMensajeriaList=documentoMensajeriaService.getLstDetalleMensajeria(docDestinoBean);
                /*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
                List<DocumentoRecepMensajeriaBean> docMensajeriaListVirtual=documentoMensajeriaService.getLstDetalleMensajeriaVirtual(docDestinoBean);
                String indMesaVirtual = "0";
                if(!docMensajeriaListVirtual.isEmpty()){
                    indMesaVirtual = "2";
                }
                /*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
                DocumentoRecepMensajeriaBean docMensajeriaBean=new DocumentoRecepMensajeriaBean();
                if(!docMensajeriaList.isEmpty()){
                     docMensajeriaBean= docMensajeriaList.get(0);
                     docMensajeriaBean.setFecEnviomsj(docMensajeriaBean.getFecEnviomsj().substring(0,16));
                     //docMensajeriaBean.setFePlaMsj(docMensajeriaBean.getFePlaMsj().substring(0,19));                   
                     
                    if(docMensajeriaBean.getFePlaMsjD()!=null){                   
                        docMensajeriaBean.setFePlaMsjD(docMensajeriaBean.getFePlaMsjD().substring(0,19));                    
                    }                                          
                }
                else
                {
                docMensajeriaBean.setNumsj("0");
                }
               if(docRemitoBean.getFeEmi()!=null && docRemitoBean.getFeEmi().length()>19) 
                        docRemitoBean.setFeEmi(docRemitoBean.getFeEmi().substring(0,20));                  
               
               if(docDestinoBean.getFeRecDoc()!=null && docDestinoBean.getFeRecDoc().length()>19){                   
                       docDestinoBean.setFeRecDoc(docDestinoBean.getFeRecDoc().substring(0,19));                     
                }
               if(docDestinoBean.getFeArcDoc()!=null && docDestinoBean.getFeArcDoc().length()>19){      
                docDestinoBean.setFeArcDoc(docDestinoBean.getFeArcDoc().substring(0,19));
               }
                /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente --*/
                //List<ReferenciaBean> referenciaBean = anexoDocumentoService.getDocumentosSeguimiento(pNuAnn, pNuEmi, docDestinoBean.getNuDes(), null, null);
//                List<ReferenciaBean> referenciaBean = documentoBasicoService.getDocSegParaExtExp(pNuAnn, pNuEmi, pNuAnnPadre, pNuEmiPadre);
//                if(referenciaBean!=null && !referenciaBean.isEmpty()){
//                    if(!referenciaBean.get(0).getNuEmiRef().equals(pNuEmiPadre)){
//                        System.out.println("Referencia-->");
//                        referenciaBean = documentoBasicoService.getDocSegParaExtExp(pNuAnn, referenciaBean.get(0).getNuEmiRef(), pNuAnnPadre, pNuEmiPadre);
//                    }
//                    pNuAnn = referenciaBean.get(0).getNuAnnRef();
//                    pNuEmi = referenciaBean.get(0).getNuEmiRef();
//                    docDestinoBean.setNuDes(referenciaBean.get(0).getNuDesRef());
//                }
//                if(docRemitoBean.getCoTipDocAdm()!=null){
//                     DocumentoEmiBean documentoEmiBean = documentoBasicoService.getExtensionExpediente(pNuAnn, pNuEmi, docDestinoBean.getNuDes());
//                     if(documentoEmiBean!=null){
//                         System.out.println("Nuevo Expediente 1-->");
//                         docRemitoBean.setNuExpediente(documentoEmiBean.getNuExpediente()+"-"+documentoEmiBean.getNuExtension());
//                         System.out.println("Nuevo Expediente--> "+ docRemitoBean.getNuExpediente());
//                     }
//                }
                /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente --*/
                List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosList(pNuAnn, pNuEmi);
                model.addAttribute("pkExp", pkExp);
                model.addAttribute("docRemitoBean", docRemitoBean);
                model.addAttribute("docDestinoBean", docDestinoBean);
                model.addAttribute("docAnexoList", docAnexoList);
                model.addAttribute("docMensajeriaBean", docMensajeriaBean);
                model.addAttribute("cantExpEnl",cantExpEnl);/*-----Hermes Enlace de expedientes 17/09/2018-----*/
                model.addAttribute("docAnexoListMsj", docAnexoListMsj);//Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
                model.addAttribute("indMesaVirtual", indMesaVirtual);//HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS
                model.addAttribute("pkCoEmpEmi", pkCoEmpEmi);/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
                mensaje = "OK";

            } catch (Exception e) {
                e.printStackTrace();
                mensaje = e.getMessage();
            }
        } else {
            mensaje = "Faltan Datos";
        }
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaSeguimientoDetalle";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }

    }

    @RequestMapping(method = RequestMethod.GET, params = "accion=goDocAnexoJson")
    private @ResponseBody String goDocAnexoJson(HttpServletRequest request, Model model) throws Exception {
        String retval = " ";
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;

        if (pkEmi != null) {

            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            try {
                retval = anexoDocumentoService.getReferenciaJson(pNuAnn, pNuEmi, 3);
            } catch (Exception e) {
                retval = " ";
                e.printStackTrace();
            }

        }
        return retval;
    }

    @RequestMapping(method = RequestMethod.GET, params = "accion=goDocAnexoRoot")
    private @ResponseBody String goDocAnexoRoot(HttpServletRequest request, Model model) throws Exception {
        String retval = " ";
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;

        if (pkEmi != null) {

            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            try {
                retval = anexoDocumentoService.getReferenciaRoot(pNuAnn, pNuEmi, pNuDes, 3);
            } catch (Exception e) {
                retval = " ";
                e.printStackTrace();
            }

        }
        return retval;
    }
// @RequestMapping(method = RequestMethod.GET, params = "accion=goDocAutorizado")
//    private @ResponseBody String goDocAutorizado(HttpServletRequest request, Model model) throws Exception {
//        String retval = " ";
//        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
//        String pNuAnn = null;
//        String pNuEmi = null;
//        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
//        String coUsu=usuario.getCoUsuario();
//        
//        if (pkEmi != null) {
//            pNuAnn = pkEmi.substring(0, 4);
//            pNuEmi = pkEmi.substring(4, 14);
//            try {
//                retval = anexoDocumentoService.getDocAutorizado(coUsu,pNuAnn, pNuEmi);
//            } catch (Exception e) {
//                retval = " ";
//                e.printStackTrace();
//            }
//
//        }
//        return retval;
//    }
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goDocSeguimientoRoot")
    private @ResponseBody String goDocSeguimientoRoot(HttpServletRequest request, Model model) throws Exception {
        String retval = " ";
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coUsu=usuario.getCoUsuario();
        
        if (pkEmi != null) {
            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            try {
                retval = anexoDocumentoService.getSeguimientoRoot(pNuAnn, pNuEmi, pNuDes, 3,coUsu,usuario.getCoDep());
            } catch (Exception e) {
                retval = " ";
                e.printStackTrace();
            }
        }
        return retval;
    }

    @RequestMapping(method = RequestMethod.GET, params = "accion=goDocSeguimientoJson")
    private @ResponseBody String goDocSeguimientoJson(HttpServletRequest request, Model model) throws Exception {
        String retval = " ";
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coUsu=usuario.getCoUsuario();
        if (pkEmi != null) {
            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            try {
                retval = anexoDocumentoService.getSeguimientoJson(pNuAnn, pNuEmi, pNuDes, 3,coUsu,usuario.getCoDep());
            } catch (Exception e) {
                retval = " ";
                e.printStackTrace();
            }
        }
        return retval;
    }
    
    LinkedList<DocumentoFileBean> files = new LinkedList<DocumentoFileBean>();
    DocumentoFileBean fileMeta = null;

    /**
     * *************************************************
     * URL: /rest/controller/upload upload(): receives files
     *
     * @param request : MultipartHttpServletRequest auto passed
     * @param response : HttpServletResponse auto passed
     * @return LinkedList<DocumentoFileBean> as json format
	 ***************************************************
     */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpload")    
    public @ResponseBody String goUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = null;
        String pNuAnn = null;
        String pNuEmi = null;
        String pkEmi = null;
        pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coUsu=usuario.getCoUsuario();
        
        String vreturn = "NO_OK";
        if (pkEmi != null) {
            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
        }
        
        while (itr.hasNext()) {
            mpf = request.getFile(itr.next());
            //System.out.println(mpf.getOriginalFilename() + " uploaded! " + files.size());
            //2.3 create new fileMeta
            fileMeta = new DocumentoFileBean();
            fileMeta.setNombreArchivo(mpf.getOriginalFilename());
            fileMeta.setTamanoArchivo(mpf.getSize() / 1024 + " Kb");
            fileMeta.setTipoArchivo(mpf.getContentType());
            try {
                fileMeta.setArchivoBytes(mpf.getBytes());
                vreturn=anexoDocumentoService.insArchivoAnexo(coUsu,pNuAnn, pNuEmi, fileMeta);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //2.4 add to files
            files.add(fileMeta);
        }

        // result will be like this
        // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
        
        String id=fileMeta.getIdDocumento();
        String nombreArchivo=fileMeta.getNombreArchivo();
        String tamanoArchivo=fileMeta.getTamanoArchivo();
        String tipoArchivo=fileMeta.getTipoArchivo();
        
        String res= "[{\"id\":\"%s\",\"name\":\"%s\",\"fileSize\":\"%s\",\"fileType\":\"%s\"}]";
        res=String.format(res, id,nombreArchivo,tamanoArchivo,tipoArchivo);
        return res;
        //return files;

    }
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUploadIE",produces = "text/plain; charset=utf-8")    
    public @ResponseBody String goUploadIE(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = null;

        String pNuAnn = null;
        String pNuEmi = null;
        String pkEmi = null;
        
        //pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pNuAnn");
        pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coUsu=usuario.getCoUsuario();
        String vreturn = "NO_OK";
        if (pkEmi != null) {
            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
        }
        
        while (itr.hasNext()) {
            mpf = request.getFile(itr.next());
            //System.out.println(mpf.getOriginalFilename() + " uploaded! " + files.size());
            //2.3 create new fileMeta
            fileMeta = new DocumentoFileBean();
            fileMeta.setNombreArchivo(mpf.getOriginalFilename());
            fileMeta.setTamanoArchivo(mpf.getSize() / 1024 + " Kb");
            fileMeta.setTipoArchivo(mpf.getContentType());
            try {
                fileMeta.setArchivoBytes(mpf.getBytes());
                vreturn=anexoDocumentoService.insArchivoAnexo(coUsu,pNuAnn, pNuEmi, fileMeta);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //2.4 add to files
            files.add(fileMeta);
        }

        String id=fileMeta.getIdDocumento();
        String nombreArchivo=fileMeta.getNombreArchivo();
        String tamanoArchivo=fileMeta.getTamanoArchivo();
        String tipoArchivo=fileMeta.getTipoArchivo();
        
        String res= "[{\"id\":\"%s\",\"name\":\"%s\",\"fileSize\":\"%s\",\"fileType\":\"%s\"}]";
        res=String.format(res, id,nombreArchivo,tamanoArchivo,tipoArchivo);
        return res;

    }
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUploadReplace")
    public @ResponseBody String goUploadReplace(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = null;
        String pNuAnn = null;
        String pNuEmi = null;
        String pNuAne = null;
        String pkEmi = null;
        
        //pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pNuAnn");
        pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        pNuAne = ServletUtility.getInstancia().loadRequestParameter(request, "pNuAne");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coUsu=usuario.getCoUsuario();
        
        String vreturn = "NO_OK";
        if (pkEmi != null) {
            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
        }
        
        while (itr.hasNext()) {

            mpf = request.getFile(itr.next());
            fileMeta = new DocumentoFileBean();
            fileMeta.setNombreArchivo(mpf.getOriginalFilename());
            fileMeta.setTamanoArchivo(mpf.getSize() / 1024 + " Kb");
            fileMeta.setTipoArchivo(mpf.getContentType());

            try {
                fileMeta.setArchivoBytes(mpf.getBytes());
                vreturn=anexoDocumentoService.updArchivoAnexo(coUsu,pNuAnn, pNuEmi,pNuAne, fileMeta);
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            //2.4 add to files
            files.add(fileMeta);
     
        }

        
        String id=fileMeta.getIdDocumento();
        //String nombreArchivo=fileMeta.getNombreArchivo();
        String nombreArchivo=anexoDocumentoService.getNombreArchivo(pNuAnn,pNuEmi,pNuAne);
        String tamanoArchivo=fileMeta.getTamanoArchivo();
        String tipoArchivo=fileMeta.getTipoArchivo();
        
        String res= "[{\"id\":\"%s\",\"name\":\"%s\",\"fileSize\":\"%s\",\"fileType\":\"%s\"}]";
        res=String.format(res, id,nombreArchivo,tamanoArchivo,tipoArchivo);
        
        
        return res;
        //return files;

    }
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUploadReplaceIE",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goUploadReplaceIE(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = null;

        String pNuAnn = null;
        String pNuEmi = null;
        String pNuAne = null;
        String pkEmi = null;
        
        //pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pNuAnn");
        pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        pNuAne = ServletUtility.getInstancia().loadRequestParameter(request, "pNuAne");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coUsu=usuario.getCoUsuario();
        
        String vreturn = "NO_OK";
        if (pkEmi != null) {
            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
        }
        
        while (itr.hasNext()) {

            mpf = request.getFile(itr.next());
            //System.out.println(mpf.getOriginalFilename() + " uploaded! " + files.size());

          
            //2.3 create new fileMeta
            fileMeta = new DocumentoFileBean();
            fileMeta.setNombreArchivo(mpf.getOriginalFilename());
            fileMeta.setTamanoArchivo(mpf.getSize() / 1024 + " Kb");
            fileMeta.setTipoArchivo(mpf.getContentType());

            try {
                fileMeta.setArchivoBytes(mpf.getBytes());
                vreturn=anexoDocumentoService.updArchivoAnexo(coUsu,pNuAnn, pNuEmi,pNuAne, fileMeta);
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            //2.4 add to files
            files.add(fileMeta);
     
        }

        // result will be like this
        // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
        
        String id=fileMeta.getIdDocumento();
        String nombreArchivo=fileMeta.getNombreArchivo();
        String tamanoArchivo=fileMeta.getTamanoArchivo();
        String tipoArchivo=fileMeta.getTipoArchivo();
        
        String res= "[{\"id\":\"%s\",\"name\":\"%s\",\"fileSize\":\"%s\",\"fileType\":\"%s\"}]";
        res=String.format(res, id,nombreArchivo,tamanoArchivo,tipoArchivo);
        
        
        return res;
        //return files;

    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goActualizaDescripAnex")
    public String goActualizaDescripAnex(@RequestBody DocumentoAnexoBeansContenedor listaDocs,HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String rowCount = null;
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coUsu=usuario.getCoUsuario();
        rowCount = ServletUtility.getInstancia().loadRequestParameter(request, "rowCount");
        
        try{
            mensaje=anexoDocumentoService.updAnexoDetalle(listaDocs,coUsu,rowCount);
        }catch(validarDatoException e){
           mensaje=(e.valorMsg);
        }catch (Exception e) { 
           e.printStackTrace(); 
        } 
        
        if (mensaje.equals("OK")) {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }        
    }
    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRutaCargaDoc")
    private @ResponseBody String goRutaCargaDoc(HttpServletRequest request, Model model) throws Exception {
        StringBuilder retval = new StringBuilder();
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);

        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");
        String pinFor = ServletUtility.getInstancia().loadRequestParameter(request, "inFor"); // Indica si va retornar el formato del documento

        if (pNuAnn != null && pNuEmi != null && pTiOpe != null) {
            try {

                DocumentoVerBean docVerBean = new DocumentoVerBean();
                docVerBean = documentoObjService.getNombreCargaDoc(pNuAnn, pNuEmi, pTiOpe);

                retval.append("{\"retval\":\"");
                retval.append(docVerBean.getDeMensaje());
                retval.append("\",\"nuAnn\":\"");
                retval.append(docVerBean.getNuAnn());
                retval.append("\",\"nuEmi\":\"");
                retval.append(docVerBean.getNuEmi());
                retval.append("\",\"nuSecFirma\":\"");
                retval.append(docVerBean.getNuSecFirma());
                retval.append("\",\"coDoc\":\"");
                retval.append(docVerBean.getCoDocEmi());
                retval.append("\",\"noDoc\":\"");
                retval.append(docVerBean.getNoDocumento());
                retval.append("\",\"noFirma\":\"");
                retval.append(docVerBean.getNoFirma());
                retval.append("\",\"noUrl\":\"");
                retval.append(docVerBean.getUrlDocumento());
                if (pinFor!=null && pinFor.equals("1")){
                    retval.append("\",\"inFor\":\"");
                    retval.append(documentoObjService.getFormatoDoc(pNuAnn, pNuEmi, pTiOpe));
                }
                retval.append("\"}");
            } catch (Exception e) {
                retval.append("{\"retval\":");
                retval.append("false");
                retval.append("}");
                e.printStackTrace();
            }

        }
        return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRutaFirmaDoc")
    private @ResponseBody String goRutaFirmaDoc(HttpServletRequest request, Model model) throws Exception {
        StringBuilder retval = new StringBuilder();
        
        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        DocumentoVerBean docSession = new DocumentoVerBean();
        
        if (pNuAnn != null && pNuEmi != null && pTiOpe != null) {
            try {
                ServletContext sc = request.getSession().getServletContext();
                String rutaBase = sc.getRealPath("/reports/");
                
                docSession = documentoObjService.getNombreFirmaDoc(pNuAnn, pNuEmi, pTiOpe, usuarioConfigBean, rutaBase);

                retval.append("{\"retval\":\"");
                retval.append(docSession.getDeMensaje());
                retval.append("\",\"nuAnn\":\"");
                retval.append(docSession.getNuAnn());
                retval.append("\",\"nuEmi\":\"");
                retval.append(docSession.getNuEmi());
                retval.append("\",\"inFirma\":\"");
                retval.append(docSession.getInTipoFirma());
                retval.append("\",\"nuSecFirma\":\"");
                retval.append(docSession.getNuSecFirma());
                retval.append("\",\"noPrefijo\":\"");
                retval.append(docSession.getNoPrefijo());
                retval.append("\",\"numeroDoc\":\"");
                retval.append(docSession.getNumeroDoc());
                retval.append("\",\"fechaFirma\":\"");
                retval.append(docSession.getFeFirma());
                retval.append("\",\"noDoc\":\"");
                retval.append(docSession.getNoDocumento());
                retval.append("\",\"noUrl\":\"");
                retval.append(docSession.getUrlDocumento());
                retval.append("\"}");
            } catch (Exception e) {
                retval.append("{\"retval\":");
                retval.append("false");
                retval.append("}");
                e.printStackTrace();
            }

        }
        model.addAttribute("docSession",docSession);
        return retval.toString();
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goRutaCargaFirmaDoc")
    private @ResponseBody String goRutaCargaFirmaDoc(HttpServletRequest request, Model model) throws Exception {
        StringBuilder retval = new StringBuilder();

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        DocumentoVerBean docSession =Utilidades.getInstancia().loadDocFromSession(request);

        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");
        String pNuSecFirma = ServletUtility.getInstancia().loadRequestParameter(request, "nuSecFirma");
        
        if (pNuAnn != null && pNuEmi != null && pTiOpe != null && pNuSecFirma != null ) {
            if (pNuSecFirma.equals(docSession.getNuSecFirma())) {
                try {
                    DocumentoVerBean docVerBean = new DocumentoVerBean();
                    docVerBean = documentoObjService.getNombreCargaFirmaDoc(pNuAnn, pNuEmi, pTiOpe);
                    if(docVerBean.getDeMensaje().equals("OK")){
                        docSession.setDeMensaje(docVerBean.getDeMensaje());
                        docSession.setUrlDocumento(docVerBean.getUrlDocumento());
                        docSession.setNuSecFirma(docVerBean.getNuSecFirma());
                        
                        retval.append("{\"retval\":\"");
                        retval.append(docSession.getDeMensaje());
                        retval.append("\",\"nuAnn\":\"");
                        retval.append(docSession.getNuAnn());
                        retval.append("\",\"nuEmi\":\"");
                        retval.append(docSession.getNuEmi());
                        retval.append("\",\"nuSecFirma\":\"");
                        retval.append(docSession.getNuSecFirma());
                        retval.append("\",\"noFirma\":\"");
                        retval.append(docSession.getNoFirma());
                        retval.append("\",\"noUrl\":\"");
                        retval.append(docSession.getUrlDocumento());
                        retval.append("\"}");                        
                    }

                } catch (Exception e) {
                    retval.append("{\"retval\":");
                    retval.append("false");
                    retval.append("}");
                    e.printStackTrace();
                }
            }else{
                retval.append("{\"retval\":");
                retval.append("\"Documento ha cambiado, proceda a firmar nuevamente\"");
                retval.append("}");
            }
        }else{
            retval.append("{\"retval\":");
            retval.append("\"Error en Datos\"");
            retval.append("}");
        }
        
        model.addAttribute("docSession",docSession);
        return retval.toString();
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goDocRutaAbrirEmi")
    private @ResponseBody String goDocRutaAbrirEmi(HttpServletRequest request, Model model) throws Exception {
        String retval = "";
        DocRespuestaBean retRespuesta = new DocRespuestaBean();
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");

        if (pNuAnn != null && pNuEmi != null && pTiOpe != null) {
            try {
                ServletContext sc = request.getSession().getServletContext();
                String rutaBase = sc.getRealPath("/reports/");

                DocumentoVerBean docVerBean = new DocumentoVerBean();
                docVerBean = documentoObjService.getNombreDocEmi(pNuAnn, pNuEmi, pTiOpe,usuario, rutaBase);

                retRespuesta.setRetval(docVerBean.getDeMensaje());
                retRespuesta.setNuAnn(docVerBean.getNuAnn());
                retRespuesta.setNuEmi(docVerBean.getNuEmi());
                retRespuesta.setNoDoc(docVerBean.getNoDocumento());
                retRespuesta.setNoFirma(docVerBean.getNoFirma());
                retRespuesta.setNoUrl(docVerBean.getUrlDocumento());

            } catch (Exception e) {
                retRespuesta.setRetval("Error al obtener nombre del Documento");
                e.printStackTrace();
            }
            try {
                Writer strWriter = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(strWriter, retRespuesta );
                retval = strWriter.toString();   
            } catch (Exception e) {
                retval = "";
            }
        }
        return retval;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRutaVoBoDoc")
    private @ResponseBody String goRutaVoBoDoc(HttpServletRequest request, Model model) throws Exception {
        StringBuilder retval = new StringBuilder();
        
        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        DocumentoVerBean docSession = new DocumentoVerBean();
        
        if (pNuAnn != null && pNuEmi != null && pTiOpe != null) {
            try {
                
                ServletContext sc = request.getSession().getServletContext();
                String rutaBase = sc.getRealPath("/reports/");

                docSession = documentoObjService.getNombreVoBoDoc(pNuAnn, pNuEmi, pTiOpe, usuarioConfigBean, rutaBase);

                retval.append("{\"retval\":\"");
                retval.append(docSession.getDeMensaje());
                retval.append("\",\"nuAnn\":\"");
                retval.append(docSession.getNuAnn());
                retval.append("\",\"nuEmi\":\"");
                retval.append(docSession.getNuEmi());
                retval.append("\",\"inFirma\":\"");
                retval.append(docSession.getInTipoFirma());
                retval.append("\",\"nuSecFirma\":\"");
                retval.append(docSession.getNuSecFirma());
                retval.append("\",\"noPrefijo\":\"");
                retval.append(docSession.getNoPrefijo());
                retval.append("\",\"numeroDoc\":\"");
                retval.append(docSession.getNumeroDoc());
                retval.append("\",\"fechaFirma\":\"");
                retval.append(docSession.getFeFirma());
                retval.append("\",\"noDoc\":\"");
                retval.append(docSession.getNoDocumento());
                retval.append("\",\"noUrl\":\"");
                retval.append(docSession.getUrlDocumento());
                retval.append("\"}");
            } catch (Exception e) {
                retval.append("{\"retval\":");
                retval.append("false");
                retval.append("}");
                e.printStackTrace();
            }

        }
        model.addAttribute("docSession",docSession);
        return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRutaCargaVoBoDoc")
    private @ResponseBody String goRutaCargaVoBoDoc(HttpServletRequest request, Model model) throws Exception {
        StringBuilder retval = new StringBuilder();

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        DocumentoVerBean docSession =Utilidades.getInstancia().loadDocFromSession(request);

        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");
        String pNuSecFirma = ServletUtility.getInstancia().loadRequestParameter(request, "nuSecFirma");
        
        if (pNuAnn != null && pNuEmi != null && pTiOpe != null && pNuSecFirma != null ) {
            if (pNuSecFirma.equals(docSession.getNuSecFirma())) {
                try {
                    DocumentoVerBean docVerBean = new DocumentoVerBean();
                    docVerBean = documentoObjService.getNombreCargaVoBoDoc(pNuAnn, pNuEmi, pTiOpe);
                    if(docVerBean.getDeMensaje().equals("OK")){
                        docSession.setDeMensaje(docVerBean.getDeMensaje());
                        docSession.setUrlDocumento(docVerBean.getUrlDocumento());
                        docSession.setNuSecFirma(docVerBean.getNuSecFirma());
                        
                        retval.append("{\"retval\":\"");
                        retval.append(docSession.getDeMensaje());
                        retval.append("\",\"nuAnn\":\"");
                        retval.append(docSession.getNuAnn());
                        retval.append("\",\"nuEmi\":\"");
                        retval.append(docSession.getNuEmi());
                        retval.append("\",\"nuSecFirma\":\"");
                        retval.append(docSession.getNuSecFirma());
                        retval.append("\",\"noFirma\":\"");
                        retval.append(docSession.getNoFirma());
                        retval.append("\",\"noUrl\":\"");
                        retval.append(docSession.getUrlDocumento());
                        retval.append("\"}");                        
                    }

                } catch (Exception e) {
                    retval.append("{\"retval\":");
                    retval.append("false");
                    retval.append("}");
                    e.printStackTrace();
                }
            }else{
                retval.append("{\"retval\":");
                retval.append("\"Documento ha cambiado, proceda a firmar nuevamente\"");
                retval.append("}");
            }
        }else{
            retval.append("{\"retval\":");
            retval.append("\"Error en Datos\"");
            retval.append("}");
        }
        
        model.addAttribute("docSession",docSession);
        return retval.toString();
    }    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRutaFirmaDocAnexo")
    private @ResponseBody String goRutaFirmaDocAnexo(HttpServletRequest request, Model model) throws Exception {
        StringBuilder retval = new StringBuilder();
        
        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pNuAne = ServletUtility.getInstancia().loadRequestParameter(request, "nuAne");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        DocumentoVerBean docSession = new DocumentoVerBean();
        
        if (pNuAnn != null && pNuEmi != null && pNuAne != null) {
            try {

                docSession = documentoObjService.getNombreFirmaDocAnexo(pNuAnn, pNuEmi, pNuAne, pTiOpe, usuarioConfigBean);

                retval.append("{\"retval\":\"");
                retval.append(docSession.getDeMensaje());
                retval.append("\",\"nuAnn\":\"");
                retval.append(docSession.getNuAnn());
                retval.append("\",\"nuEmi\":\"");
                retval.append(docSession.getNuEmi());
                retval.append("\",\"inFirma\":\"");
                retval.append(docSession.getInTipoFirma());
                retval.append("\",\"nuSecFirma\":\"");
                retval.append(docSession.getNuSecFirma());
                retval.append("\",\"noPrefijo\":\"");
                retval.append(docSession.getNoPrefijo());
                retval.append("\",\"numeroDoc\":\"");
                retval.append(docSession.getNumeroDoc());
                retval.append("\",\"fechaFirma\":\"");
                retval.append(docSession.getFeFirma());
                retval.append("\",\"noDoc\":\"");
                retval.append(docSession.getNoDocumento());
                retval.append("\",\"noUrl\":\"");
                retval.append(docSession.getUrlDocumento());
                retval.append("\"}");
            } catch (Exception e) {
                retval.append("{\"retval\":");
                retval.append("false");
                retval.append("}");
                e.printStackTrace();
            }

        }
        model.addAttribute("docSession",docSession);
        return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRutaCargaFirmaDocAnexo")
    private @ResponseBody String goRutaCargaFirmaDocAnexo(HttpServletRequest request, Model model) throws Exception {
        StringBuilder retval = new StringBuilder();

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        DocumentoVerBean docSession =Utilidades.getInstancia().loadDocFromSession(request);

        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pNuAne = ServletUtility.getInstancia().loadRequestParameter(request, "nuAne");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");
        String pNuSecFirma = ServletUtility.getInstancia().loadRequestParameter(request, "nuSecFirma");
        
        if (pNuAnn != null && pNuEmi != null && pTiOpe != null && pNuSecFirma != null && pNuAne != null) {
            if (pNuSecFirma.equals(docSession.getNuSecFirma())) {
                try {
                    DocumentoVerBean docVerBean = new DocumentoVerBean();
                    docVerBean = documentoObjService.getNombreCargaFirmaDocAnexo(pNuAnn, pNuEmi, pNuAne, pTiOpe);
                    if(docVerBean.getDeMensaje().equals("OK")){
                        docSession.setDeMensaje(docVerBean.getDeMensaje());
                        docSession.setUrlDocumento(docVerBean.getUrlDocumento());
                        docSession.setNuSecFirma(docVerBean.getNuSecFirma());
                        
                        retval.append("{\"retval\":\"");
                        retval.append(docSession.getDeMensaje());
                        retval.append("\",\"nuAnn\":\"");
                        retval.append(docSession.getNuAnn());
                        retval.append("\",\"nuEmi\":\"");
                        retval.append(docSession.getNuEmi());
                        retval.append("\",\"nuSecFirma\":\"");
                        retval.append(docSession.getNuSecFirma());
                        retval.append("\",\"noFirma\":\"");
                        retval.append(docSession.getNoFirma());
                        retval.append("\",\"noUrl\":\"");
                        retval.append(docSession.getUrlDocumento());
                        retval.append("\"}");                        
                    }

                } catch (Exception e) {
                    retval.append("{\"retval\":");
                    retval.append("false");
                    retval.append("}");
                    e.printStackTrace();
                }
            }else{
                retval.append("{\"retval\":");
                retval.append("\"Documento ha cambiado, proceda a firmar nuevamente\"");
                retval.append("}");
            }
        }else{
            retval.append("{\"retval\":");
            retval.append("\"Error en Datos\"");
            retval.append("}");
        }
        
        model.addAttribute("docSession",docSession);
        return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goCargaDocAnexo",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goCargaDocAnexo(HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       DocumentoVerBean docSession =Utilidades.getInstancia().loadDocFromSession(request);
       
        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pNuAne = ServletUtility.getInstancia().loadRequestParameter(request, "nuAne");
        String pNuSecFirma = ServletUtility.getInstancia().loadRequestParameter(request, "nuSecFirma");
        
       pNuSecFirma=applicationProperties.getRutaTemporal()+"//"+pNuSecFirma;
       try{
          mensaje = documentoObjService.cargaDocAnexo(pNuAnn,pNuEmi,pNuAne,pNuSecFirma,usuario.getCoUsuario(),docSession.getNoDocumento());

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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goDocRutaAbrirEmiReporte")
    public @ResponseBody String goDocRutaAbrirEmiReporte(HttpServletRequest request, Model model)
    {
        String retval = "";
        DocRespuestaBean retRespuesta = new DocRespuestaBean();
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");
        
        //jazanero
        String pAbreWord = ServletUtility.getInstancia().loadRequestParameter(request, "pAbreWord");        
        String pAbreWordTipoProyecto = ServletUtility.getInstancia().loadRequestParameter(request, "pAbreWordTipoProyecto");        
        pAbreWord = pAbreWord.equals("")?"NO":pAbreWord;  
        pAbreWordTipoProyecto = pAbreWordTipoProyecto.equals("")?"NO":pAbreWordTipoProyecto;
        //jazanero
        
        String rutaBase = "";
        if (pNuAnn != null && pNuEmi != null && pTiOpe != null) {
            try {
                ServletContext sc = request.getSession().getServletContext();
                rutaBase = sc.getRealPath("/reports/");
                DocumentoVerBean docVerBean = new DocumentoVerBean();
                //docVerBean = documentoObjService.getNombreDocEmi(pNuAnn, pNuEmi, pTiOpe,usuario);
                //documentoObjService.getNombreDoc(pNuAnn, pNuEmi, pTiOpe, usuario);
                //logica de negocio previa que devuelve si el archivo existe en BD
                docVerBean = documentoObjService.getNombreDocEmiReporte(pNuAnn, pNuEmi, pTiOpe,usuario,rutaBase);
                
                retRespuesta.setRetval(docVerBean.getDeMensaje());
                retRespuesta.setNuAnn(docVerBean.getNuAnn());
                retRespuesta.setNuEmi(docVerBean.getNuEmi());
                
                if(pAbreWord.equals("SI")){
                    retRespuesta.setNoDoc(docVerBean.getNombreDocumentoWord());
                }else{
                    retRespuesta.setNoDoc(docVerBean.getNoDocumento());
                }
                
                retRespuesta.setNoFirma(docVerBean.getNoFirma());
                retRespuesta.setNoUrl(docVerBean.getUrlDocumento());

            } catch (Exception e) {
                retRespuesta.setRetval("Error al obtener nombre del Documento");
                e.printStackTrace();
            }
            try {
                Writer strWriter = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(strWriter, retRespuesta );
                retval = strWriter.toString();   
            } catch (Exception e) {
                retval = "";
            }
        }
        return retval;
    }
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRutaFirmaDocReporte")
    private @ResponseBody String goRutaFirmaDocReporte(HttpServletRequest request, Model model) throws Exception {
        StringBuilder retval = new StringBuilder();
        
        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String pTiOpe = ServletUtility.getInstancia().loadRequestParameter(request, "tiOpe");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        DocumentoVerBean docSession = new DocumentoVerBean();
        String rutaBase = "";
        if (pNuAnn != null && pNuEmi != null && pTiOpe != null) {
            try {
                ServletContext sc = request.getSession().getServletContext();
                rutaBase = sc.getRealPath("/reports/");
                DocumentoVerBean docVerBean = new DocumentoVerBean();    
                docSession = documentoObjService.getNombreFirmaDocReporte(pNuAnn, pNuEmi, pTiOpe, usuarioConfigBean, rutaBase);

                System.out.println("docSession.getDeMensaje()->"+docSession.getDeMensaje());
                System.out.println("docSession.getNuAnn()->"+docSession.getNuAnn());
                System.out.println("docSession.getNuEmi()->"+docSession.getNuEmi());
                System.out.println("docSession.getInTipoFirma()->"+docSession.getInTipoFirma());
                System.out.println("docSession.getNuSecFirma()->"+docSession.getNuSecFirma());
                System.out.println("docSession.getNoPrefijo()->"+docSession.getNoPrefijo());
                System.out.println("docSession.getNumeroDoc()->"+docSession.getNumeroDoc());
                System.out.println("docSession.getFeFirma()->"+docSession.getFeFirma());
                System.out.println("docSession.getNoDocumento()->"+docSession.getNoDocumento());
                System.out.println("docSession.getUrlDocumento()->"+docSession.getUrlDocumento());
                
                
                retval.append("{\"retval\":\"");
                retval.append(docSession.getDeMensaje());
                retval.append("\",\"nuAnn\":\"");
                retval.append(docSession.getNuAnn());
                retval.append("\",\"nuEmi\":\"");
                retval.append(docSession.getNuEmi());
                retval.append("\",\"inFirma\":\"");
                retval.append(docSession.getInTipoFirma());
                retval.append("\",\"nuSecFirma\":\"");
                retval.append(docSession.getNuSecFirma());
                retval.append("\",\"noPrefijo\":\"");
                retval.append(docSession.getNoPrefijo());
                retval.append("\",\"numeroDoc\":\"");
                retval.append(docSession.getNumeroDoc());
                retval.append("\",\"fechaFirma\":\"");
                retval.append(docSession.getFeFirma());
                retval.append("\",\"noDoc\":\"");
                retval.append(docSession.getNoDocumento());
                retval.append("\",\"noUrl\":\"");
                retval.append(docSession.getUrlDocumento());
                retval.append("\"}");
            } catch (Exception e) {
                retval.append("{\"retval\":");
                retval.append("false");
                retval.append("}");
                e.printStackTrace();
            }

        }
        model.addAttribute("docSession",docSession);
        return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdEnvioNotificacion", produces = "text/plain; charset=utf-8")
    public @ResponseBody
    String goUpdEnvioNotificacion(HttpServletRequest request, Model model) {
        //String codDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "coDep");
        
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        //String pnuDes = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDes");
        String nCodAccion = ServletUtility.getInstancia().loadRequestParameter(request, "nCodAccion");
        // [INICIO] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
        /*String nCodUrgencia = ServletUtility.getInstancia().loadRequestParameter(request, "nCodUrgencia");*/
        // [FIN] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
        String nCodDepMsj = ServletUtility.getInstancia().loadRequestParameter(request, "nCodDepMsj");
        
        //jazanero
        String nEnviaCorreo = ServletUtility.getInstancia().loadRequestParameter(request, "nEnviaCorreo");
        String nCorreo = ServletUtility.getInstancia().loadRequestParameter(request, "nCorreo");
        nCorreo = nCorreo==null? "":nCorreo;
        nEnviaCorreo = nEnviaCorreo==null? "":nEnviaCorreo;
        System.out.println("nEnviaCorreo->"+nEnviaCorreo);
        System.out.println("nCorreo->"+nCorreo);
        //enviamos correo electronico
        
        System.out.println("entro al action AActionNotificacionSistema");
				
        boolean swEmail = false;
        String nombreArchivo = "";
        byte[] archivo;                
        String eserror="1";//si
        String coRespuesta="";
        String deRespuesta="";
        String prutaReporte = "";
        String rutaBase = "";
        DocumentoObjBean docObjBean = null;
        List<DocumentoObjBean> lstdocObjBean = null;
        String msg[] = new String[2];
        
        if(nEnviaCorreo.equals("01")){
            
        try {
            
            //necesito sacar el nro del documento
            //necesito saacar mas info para el correo
            //metodo
            DetalleEnvioDeCorreoBean detalleEnvioDeCorreoBean = EmiDocumentoAdmService.getDetalleEnvioCorreo(pnuAnn, pnuEmi);    
            docObjBean = documentoObjService.leerDocumento(pnuAnn,pnuEmi,"01");
            lstdocObjBean = documentoObjService.leerDocumentoAnexo(pnuAnn, pnuEmi);
            
            if (docObjBean!=null){
                
                //CORREOS ADJUNTOS
                if(lstdocObjBean!=null && lstdocObjBean.size()>0){
                    for(DocumentoObjBean obj: lstdocObjBean){
                        nombreArchivo = Utilidades.generateRandomNumber(10);
                        prutaReporte = "RPTDOCUMENTOCORREO_"+nombreArchivo + "_"+obj.getNombreArchivo();
                        archivo = obj.getDocumento();

                        if (archivo==null) {
                            eserror = "0";
                            coRespuesta=eserror;
                            throw new Exception("No generó correctamente el archivo del reporte.");
                        }

                        obj.setRutabase(applicationProperties.getRutaTemporal() + "/" + prutaReporte);

                        File tmpFile = new File(obj.getRutabase());
                        FileOutputStream fos = new FileOutputStream(tmpFile);
                        fos.write(archivo);
                        fos.flush();
                        fos.close();
                        
                        if(eserror.equals("0")){
                            coRespuesta=eserror;
                        }else{
                            coRespuesta="1";
                        }
                    }
                }
                                
                                
                //ENVIO DE CORREO

                SmtpObject smtpObject = new SmtpObject();
                smtpObject.setAccountMail("reclamaciones@presidencia.gob.pe");
                smtpObject.setPersonalName("reclamaciones@presidencia.gob.pe");
                smtpObject.setHost("10.200.0.27");
                smtpObject.setPort("25");
                smtpObject.setAuth("true");
                smtpObject.setFilePathAdjunto(rutaBase);
                smtpObject.setFileNameAdjunto(detalleEnvioDeCorreoBean.getTipodoc()+"-"+detalleEnvioDeCorreoBean.getDocrespuesta()+".pdf");
                smtpObject.setDocumento(docObjBean.getDocumento());

                System.out.println("--->"+detalleEnvioDeCorreoBean.getTipodoc()+"-"+detalleEnvioDeCorreoBean.getDocrespuesta()+".pdf");
                
                String plantillaCorreo = "<html>\n" +
                    "<head>\n" +
                    "		   <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial,Helvetica,sans-serif;font-size:12px;color:rgb(51,51,51)\" width=\"700\">\n" +
                    "<tbody><tr><td height=\"40\" style=\"padding:7px 7px;)\" valign=\"middle\">\n" +
                    "<img align=\"left\" border=\"0\" class=\"CToWUd\" src=\"http://www.presidencia.gob.pe/logodp/logo_oficial_dp2.jpg\" /><br /><br />\n" +
                    "</td></tr>\n" +
                    "\n" +
                    "</tbody>\n" +
                    "</table>\n" +
                    "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial,Helvetica,sans-serif;font-size:12px\" width=\"700\">\n" +
                    "	<tbody>\n" +
                    "	\n" +
                    "		<tr><hr style=\"height: 5px; background-color: #747474;\" width=\"700\"> </hr>	</tr>	\n" +
                    "	</tbody>\n" +
                    "</table>\n" +
                    "<br />\n" +
                    "<table align=\"center\" width=\"700\">\n" +
                    "<tr>\n" +
                    "<td align=\"center\">\n" +
                    "<table align=\"center\">\n" +
                    "<tr>\n" +
                    "<th style=\"text-align:right\">&nbsp;</th>\n" +
                    "	<td style=\"text-align:left\">Estimado(a) Usuario:<br/><br/>\n" +
                    "	<CUERPO> </td>\n" +
                    "</tr>\n" +
                    "<br />\n" +
                    "<tr>\n" +
                    "	<td style=\"text-align:right\">&nbsp;</td>\n" +
                    "	<td> \n" +
                    "Atte,<br /> \n" +
                    "____________________________________________________________________<br /> \n" +
                    "Oficina de Atención al Ciudadano y Gestión Documentaria \n" +
                    "</p> \n" +
                    "</td> \n" +
                    "</tr> \n" +
                    "\n" +
                    "</table>\n" +
                    "\n" +
                    "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<br />\n" +
                    "</table>\n" +
                    "\n" +
                    "<br />\n" +
                    "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border:1px solid rgb(221,221,221);font-family:Arial,Helvetica,sans-serif;font-size:12px;color:rgb(51,51,51)\" width=\"700\">\n" +
                    "	<tbody><tr><td bgcolor=\"#b80400\" width=\"100%\">\n" +
                    "	<p style=\"margin:0px;text-align:center;color:rgb(255,255,255);padding:5px;line-height:14px;font-family:Arial,Helvetica,sans-serif\">Despacho Presidencial - Derechos Reservados  <br />Jr. de la Union s/n 1era cuadra<br />Lima - Perú</p></td></tr>\n" +
                    "	</tbody>\n" +
                    "</table><p>&nbsp;</p>\n" +
                    "</body>\n" +
                    "</html>";

                String cuerpo = "Referencia: Libro de Reclamación "+detalleEnvioDeCorreoBean.getDocorigen()+".\n" +
                    "<br /><br />\n" +
                    " \n" +
                    "De nuestra consideración:\n" +
                    "<br /><br />\n" +
                    "Es grato dirigirnos a usted para saludarlo y a la vez dar respuesta a su reclamo de la referencia mediante carta adjunta al presente correo.\n" +
                    "<br /><br />\n" +
                    "Nos despedimos agradeciendo su comunicación asimismo la atención que se sirva dispensar a la presente."+
                    "<br />\n";
                
                String correo = nCorreo;//; "ma.elguera@gmail.com"
                String asunto = "Notificación de Reclamo Virtual "+detalleEnvioDeCorreoBean.getDocorigen()+ " - Despacho Presidencial";

                plantillaCorreo = plantillaCorreo.replace("<CUERPO>", cuerpo);
                //plantillaCorreo = plantillaCorreo.replace("{asunto}", "");

                try {
                        swEmail = smtpSendService.envioCorreoUsuario(asunto, plantillaCorreo, correo, smtpObject, lstdocObjBean);

                } catch (Exception e) {			
                        e.printStackTrace();
                }

                if(swEmail){
                        System.out.println("Envio Correo ");
                }else{
                     System.out.println("No Envio Correo ");
                }


                /***
                 * fin
                 */                
            }            
            
        } catch (Exception e) {
            Logger.getLogger(DocumentoObjetoController.class.getName()).log(Level.SEVERE, null, e);
            eserror="0";
            deRespuesta=e.getMessage();
        }finally{
            
        }
        
        System.out.println("-->"+eserror+", "+swEmail);
        
        //VERIFICO SI CONTINUAMOS 
        //PARA ESTO ES NECESARIO QUE LA CARGA DE ARCHIVOS SEA CORRECTA
        //NECESITAMOS QUE EL CORREO SE ENVIE
        if(eserror.equals("1") && swEmail){
            
             //ACTUALIZACION DE TRANSACCION
            Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);

            try {
              DocumentoEmiBean documentoEmiBean= new DocumentoEmiBean();
              documentoEmiBean.setNuEmi(pnuEmi);
              documentoEmiBean.setNuAnn(pnuAnn);
              //documentoEmiBean.setEsDocEmi(nCodAccion);
              documentoEmiBean.setTiEnvMsj(nCodAccion);
              // [INICIO] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
              /*documentoEmiBean.setCoPrioridad(nCodUrgencia);*/
              // [FIN] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
              documentoEmiBean.setCoUseMod(usuario.getCoUsuario());
              
              documentoEmiBean.setnEnviaCorreo(nEnviaCorreo);
              
              
              if(nCodAccion.equals("0"))
              {
               documentoEmiBean.setCoDepEmi(nCodDepMsj);
              }
              else
              { documentoEmiBean.setCoDepEmi(usuario.getCoDep());
              }

                String mensaje = "NO_OK";
                String vReturn= EmiDocumentoAdmService.changeToEnvioNotificacion(documentoEmiBean,usuario);
                
                System.out.println("vReturn: "+vReturn);
                
                if(vReturn.equals("OK")){                                       
                    //SI REALIZA TRANSACCION
                    //ARCHIVAMOS EL REGISTRO                    
                    try {
                        
                        String respuesta = EmiDocumentoAdmService.updArchivarDocumento(documentoEmiBean,usuario);                       

                        mensaje = respuesta;
                    } catch (Exception ex) {
                        mensaje = ex.getMessage();
                    }
                    
                    if (mensaje.equals("OK")){
                            msg[0]="1";
                            msg[1]="Datos Guardados";
                    }else{
                        msg[0]="0";
                        msg[1]="Error al actualizar el archivar";
                    }                    
                    
                }else{
                    if (vReturn.equals("NO_OK"))
                    {                       
                        msg[0]="0";
                        msg[1]="Verificar sino existen archivos registrados";                    
                    }
                    else
                    {
                        msg[0]="0";
                        msg[1]="Error al actualizar el registro:"+vReturn;                      
                    }  

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                msg[0] = "0";
                msg[1] = "Error en el metodo";
            }
            
        }else{
            msg[0] = "0";
            msg[1] = "Error en preparación de correo.";
        }
        
        
        }else{
            //MENSAJERIA  NORMAL
            //ACTUALIZACION DE TRANSACCION
            Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);

            try {
              DocumentoEmiBean documentoEmiBean= new DocumentoEmiBean();
              documentoEmiBean.setNuEmi(pnuEmi);
              documentoEmiBean.setNuAnn(pnuAnn);
              //documentoEmiBean.setEsDocEmi(nCodAccion);
              documentoEmiBean.setTiEnvMsj(nCodAccion);
              // [INICIO] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
              /*documentoEmiBean.setCoPrioridad(nCodUrgencia);*/
              // [FIN] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
              documentoEmiBean.setCoUseMod(usuario.getCoUsuario());
                            
              documentoEmiBean.setnEnviaCorreo(nEnviaCorreo);
                            
              if(nCodAccion.equals("0"))
              {
               documentoEmiBean.setCoDepEmi(nCodDepMsj);
              }
              else
              { documentoEmiBean.setCoDepEmi(usuario.getCoDep());
              }


                String vReturn= EmiDocumentoAdmService.changeToEnvioNotificacion(documentoEmiBean,usuario);
                if(vReturn.equals("OK")){
                    msg[0]="1";
                    msg[1]="Datos Guardados";
                } 

                else{
                    if (vReturn.equals("NO_OK"))
                    {                       
                    msg[0]="0";
                    msg[1]="Verificar sino existen archivos registrados";                    
                    }
                    else
                    {
                    msg[0]="0";
                    msg[1]="Error al actualizar el registro:"+vReturn;                      
                    }    


                }
            } catch (Exception ex) {
                ex.printStackTrace();
                msg[0] = "0";
                msg[1] = "Error en el metodo";
            }
        }
        
        
        StringBuilder retval = new StringBuilder();
       // retval.append("{\"coRespuesta\":\"");
        retval.append(msg[0]);
       // retval.append("\",\"deRespuesta\":\"");
        retval.append(msg[1]);
        //retval.append("\"}");
        return retval.toString();
    }
    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goProyectosAnexos")
    private @ResponseBody String goProyectosAnexos(HttpServletRequest request, Model model) throws Exception {
        StringBuilder retval = new StringBuilder();
        
        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        
        if (pNuAnn != null && pNuEmi != null) {
            try {

                List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosList(pNuAnn, pNuEmi);
                DocumentoEmiBean documentoEmiBean = EmiDocumentoAdmService.getDocumentoEmiAdmProyectoAEmiBean(pNuAnn,pNuEmi);
                                
                int contador = 0;
                if(documentoEmiBean!=null){
                    if(docAnexoList!=null && docAnexoList.size()>0){
                        for(DocumentoAnexoBean doc: docAnexoList){
                                if(doc.getEsProyecto().equals("1")){
                                    contador++;
                                }
                        }
                    }
                }
                
                if(contador==1){
                    retval.append("{\"retval\":\"");
                    retval.append("OK");                    
                    retval.append("\"}");
                }else{
                    retval.append("{\"retval\":\"");
                    retval.append("NO_OK");                    
                    retval.append("\"}");
                }                            
                
            } catch (Exception e) {
                retval.append("{\"retval\":");
                retval.append("false");
                retval.append("}");
                e.printStackTrace();
            }

        }
        return retval.toString();
    }
    /*-----Inicio Hermes Enlace de expedientes 17/09/2018-----*/
    @RequestMapping(method = RequestMethod.GET, params = "accion=goDocSeguimientoRootEE")
    private @ResponseBody String goDocSeguimientoRootEE(HttpServletRequest request, Model model) throws Exception {
        String retval = " ";
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pCondicion = ServletUtility.getInstancia().loadRequestParameter(request, "pCondicion");
        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coUsu=usuario.getCoUsuario();
        
        if (pkEmi != null) {
            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            try {
                retval = anexoDocumentoService.getSeguimientoRoot(pNuAnn, pNuEmi, pNuDes, 3,coUsu,usuario.getCoDep());
            } catch (Exception e) {
                retval = " ";
                e.printStackTrace();
            }
        }
        return retval;
    }  
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goDocSeguimientoEE")
    public String goDocSeguimientoEE(HttpServletRequest request, Model model) {
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pCondicion = ServletUtility.getInstancia().loadRequestParameter(request, "pCondicion");
        String pkExp = null;
        
        try {
            pkExp = documentoExtRecepService.getPkExpDocExtOrigen(pkEmi);
        } catch (Exception e) {
            e.printStackTrace();
        }    
        
        model.addAttribute("frm_docOrigenBean_pkExp", pkExp);        
        model.addAttribute("pkEmiDoc", pkEmi);
        return "/modalGeneral/consultaSeguimientoEE";
    }   
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goDetalleSeguimientoEE")
    public String goDetalleSeguimientoEE(HttpServletRequest request, Model model) {
        String pkEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pkEmi");
        String pkExp = ServletUtility.getInstancia().loadRequestParameter(request, "pkExp");        
        String mensaje = "NO_OK";
        String pNuAnn = null;
        String pNuEmi = null;
        String pNuDes = null;

        if (pkEmi != null) {
            pNuAnn = pkEmi.substring(0, 4);
            pNuEmi = pkEmi.substring(4, 14);
            pNuDes = pkEmi.substring(14);
            try {
                RemitosResBean docRemitoBean = documentoBasicoService.getRemitoResumen(pNuAnn, pNuEmi);
                DestinoResBen docDestinoBean = documentoBasicoService.getDestinoResumen(pNuAnn, pNuEmi, pNuDes);
                                
                List<DocumentoRecepMensajeriaBean> docMensajeriaList=documentoMensajeriaService.getLstDetalleMensajeria(docDestinoBean);
                DocumentoRecepMensajeriaBean docMensajeriaBean=new DocumentoRecepMensajeriaBean();
                if(!docMensajeriaList.isEmpty()){
                     docMensajeriaBean= docMensajeriaList.get(0);
                     docMensajeriaBean.setFecEnviomsj(docMensajeriaBean.getFecEnviomsj().substring(0,19));
                     //docMensajeriaBean.setFePlaMsj(docMensajeriaBean.getFePlaMsj().substring(0,19));
                    if(docMensajeriaBean.getFePlaMsjD()!=null){                   
                        docMensajeriaBean.setFePlaMsjD(docMensajeriaBean.getFePlaMsjD().substring(0,19));                    
                    }                                          
                }else{
                    docMensajeriaBean.setNumsj("0");
                }
                
               if(docRemitoBean.getFeEmi()!=null && docRemitoBean.getFeEmi().length()>19) 
                        docRemitoBean.setFeEmi(docRemitoBean.getFeEmi().substring(0,19));                  
               
               if(docDestinoBean.getFeRecDoc()!=null && docDestinoBean.getFeRecDoc().length()>19){                   
                       docDestinoBean.setFeRecDoc(docDestinoBean.getFeRecDoc().substring(0,19));                     
                }
               if(docDestinoBean.getFeArcDoc()!=null && docDestinoBean.getFeArcDoc().length()>19){      
                docDestinoBean.setFeArcDoc(docDestinoBean.getFeArcDoc().substring(0,19));
               }
                List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosList(pNuAnn, pNuEmi);
                model.addAttribute("pkExp", pkExp);
                model.addAttribute("docRemitoBean", docRemitoBean);
                model.addAttribute("docDestinoBean", docDestinoBean);
                model.addAttribute("docAnexoList", docAnexoList);
                model.addAttribute("docMensajeriaBean", docMensajeriaBean);
                mensaje = "OK";

            } catch (Exception e) {
                e.printStackTrace();
                mensaje = e.getMessage();
            }
        } else {
            mensaje = "Faltan Datos";
        }
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaSeguimientoEEDetalle";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
   /*-----Fin Hermes Enlace de expedientes 17/09/2018-----*/
    
    /*interoperabilidad*/
   @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdEnvioMesaVirtual", produces = "text/plain; charset=utf-8")
    public @ResponseBody String goUpdEnvioMesaVirtual(HttpServletRequest request, Model model) {
        //String codDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "coDep");        
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        //String pnuDes = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDes");
        String nDeDepDes= ServletUtility.getInstancia().loadRequestParameter(request, "nDeDepDes");
        String nDeNomDes = ServletUtility.getInstancia().loadRequestParameter(request, "nDeNomDes");
        String nDeCarDes = ServletUtility.getInstancia().loadRequestParameter(request, "nDeCarDes");           
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String msg[] = new String[2];
        
        try {          
          DatosInterBean datosInter=new DatosInterBean();          
          datosInter.setNuEmi(pnuEmi);
          datosInter.setNuAnn(pnuAnn);
          //documentoEmiBean.setEsDocEmi(nCodAccion);
          datosInter.setDeDepDes(nDeDepDes);
          datosInter.setDeNomDes(nDeNomDes);
          datosInter.setDeCarDes(nDeCarDes);
          datosInter.setCoUseMod(usuario.getCoUsuario());                              

            String vReturn=EmiDocumentoAdmService.changeToEnvioMesaVirtual(datosInter,usuario);

            if(vReturn.equals("OK")){
                msg[0]="1";
                msg[1]="Datos Guardados";
            }else{
                if (vReturn.equals("NO_OK")){                       
                    msg[0]="0";
                    msg[1]="Verificar sino existen archivos registrados";                    
                }else{
                    msg[0]="0";
                    msg[1]="Error al actualizar el registro:"+vReturn;                      
                }    
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            msg[0] = "0";
            msg[1] = "Error en el metodo";
        }
        StringBuilder retval = new StringBuilder();
       // retval.append("{\"coRespuesta\":\"");
        retval.append(msg[0]);
       // retval.append("\",\"deRespuesta\":\"");
        retval.append(msg[1]);
        //retval.append("\"}");
        return retval.toString();
    }
    /*interoperabilidad*/   
    
    //Hermes 28/01/2019 - Requerimiento Mensajeria: Acta 0005-2019    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAdjuntarDocMsjEnviados")
    public String goAdjuntarDocMsjEnviados(HttpServletRequest request, Model model) {
        String pexisteAnexo = ServletUtility.getInstancia().loadRequestParameter(request, "pexisteAnexo");
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        DocumentoEmiBean documentoEmiArBean=new DocumentoEmiBean();
       
        List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosMsjList(pnuAnn, pnuEmi);                
        List listTipDocAdicMsj = documentoMensajesService.getListTipoDocAdicionalMsj("TIP_DOC_MSJ");
        String tamanioMaxAnexos = commonQryService.obtenerValorParametro("CAP_MAXIMA_ANEXOS");/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
        DescargaMensajeBean descargaMensajeBean = new DescargaMensajeBean();
        
        descargaMensajeBean.setNu_ann(pnuAnn);
        descargaMensajeBean.setNu_emi(pnuEmi);
        descargaMensajeBean.setNu_des(docAnexoList.get(0).getNuDes());
        
        String respuesta = documentoMensajesService.deleteMsjAdicional(descargaMensajeBean); 
       
        List<DocumentoAnexoBean> docAnexoListMsj = documentoMensajesService.getAnexosListMsj2(pnuAnn, pnuEmi);
        
        model.addAttribute("pnuAnn", pnuAnn);
        model.addAttribute("pnuEmi", pnuEmi);
        model.addAttribute("pnuDes", docAnexoList.get(0).getNuDes());
        model.addAttribute("tiene_cargo", pexisteAnexo);
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute("docAnexoListMsj", docAnexoListMsj);
        model.addAttribute("deTipoDocAdicMsjList",listTipDocAdicMsj);//Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019        
        model.addAttribute("documentoEmiArBean", new DocumentoEmiBean());
        model.addAttribute("tamanioMaxAnexos", tamanioMaxAnexos);/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/        
        return "/modalGeneral/adjuntarDocMsj";
    }    
    //Hermes 28/01/2019 - Requerimiento Mensajeria: Acta 0005-2019  
    /*[HPB] Inicio 18/08/23 OS-0000786-2023 Mejoras*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListadoAdjuntarDocAnexos")
    public String goListadoAdjuntarDocAnexos(HttpServletRequest request, Model model) {
        String pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
        String pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
        String mensaje = "NO_OK";
        String tamanioMaxAnexos = commonQryService.obtenerValorParametro("CAP_MAXIMA_ANEXOS");

        if (pNuAnn != null) {
            try {
                List<DocumentoAnexoBean> docAnexoListMsj = documentoMensajesService.getAnexosListMsj2(pNuAnn, pNuEmi);
                model.addAttribute("docAnexoListMsj", docAnexoListMsj);
                model.addAttribute("tamanioMaxAnexos", tamanioMaxAnexos);
                mensaje = "OK";
            } catch (Exception e) {
                e.printStackTrace();
                mensaje = e.getMessage();
            }
        } else {
            mensaje = "Faltan Datos";
        }
        
        if (mensaje.equals("OK")) {
            return "/modalGeneral/adjuntarDocMsjListado";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    /*[HPB] Fin 18/08/23 OS-0000786-2023 Mejoras*/
    /*--HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS--*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goArcDocObsMesaPartesVirtual")
    public String goArcDocObsMesaPartesVirtual(HttpServletRequest request, Model model) {

        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
        
        DocumentoEmiBean documentoEmiArBean=new DocumentoEmiBean();               
        DescargaMensajeBean descargaMensajeBean = new DescargaMensajeBean();        
        
        descargaMensajeBean.setNu_ann(pnuAnn);
        descargaMensajeBean.setNu_emi(pnuEmi);               
        
        List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosMsjList(pnuAnn, pnuEmi);   
        
        if(docAnexoList.size()>0)
            model.addAttribute("pnuDes", docAnexoList.get(0).getNuDes());
        model.addAttribute("pnuAnn", pnuAnn);
        model.addAttribute("pnuEmi", pnuEmi);
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute(documentoEmiArBean);
        
        return "/modalGeneral/archivarDocMPV";
    }
    /*--HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS--*/    
    
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    @RequestMapping(value = "/downloadZip", method = RequestMethod.POST)
    public void zipFiles(@RequestParam("pnuAnn") String pnuAnn, @RequestParam("pnuEmi") String pnuEmi,  
                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombreArchivo = "";
        String filePath = "/glassfish//tmppcm//";
        int BUFFER_SIZE = 4096;
        byte[] archivo;  
        List<DocumentoObjBean> lstdocObjBean = null;
        lstdocObjBean = documentoObjService.leerDocumentoAnexo(pnuAnn, pnuEmi);
        System.out.println("lstdocObjBean--> "+ lstdocObjBean.size());
        if(!lstdocObjBean.isEmpty()){
            for(DocumentoObjBean obj: lstdocObjBean){
                archivo = obj.getDocumento();
                nombreArchivo = obj.getNombreArchivo();
                InputStream inputStream = new ByteArrayInputStream(archivo);
                OutputStream outputStream = new FileOutputStream(filePath + nombreArchivo);
                
                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();
            }
        }
        //setting headers  
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        // create a list to add files to be zipped
        List<File> files = new ArrayList<File>();
        if(!lstdocObjBean.isEmpty()){
            for(DocumentoObjBean obj: lstdocObjBean){
                files.add(new File(filePath + obj.getNombreArchivo()));
            }
        }

        // package files
        for (File file : files) {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }    
        zipOutputStream.close();
        for (File file : files) {
            file.delete();
        }
    } 
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
}
