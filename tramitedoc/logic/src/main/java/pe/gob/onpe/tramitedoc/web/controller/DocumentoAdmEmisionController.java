package pe.gob.onpe.tramitedoc.web.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.CitizenBean;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBean;
import pe.gob.onpe.tramitedoc.bean.DatosPlantillaDoc;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioOtroOrigenBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoProyectoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoVerBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoVoBoBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteBean;
import pe.gob.onpe.tramitedoc.bean.MotivoBean;
import pe.gob.onpe.tramitedoc.bean.ProveedorBean;
import pe.gob.onpe.tramitedoc.bean.RemitosResBean;
import pe.gob.onpe.tramitedoc.bean.TemaBean;
import pe.gob.onpe.tramitedoc.bean.TipoDestinatarioEmiBean;
import pe.gob.onpe.tramitedoc.bean.TipoDocumentoBean;
import pe.gob.onpe.tramitedoc.bean.TrxDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.dao.DatosPlantillaDao;
import pe.gob.onpe.tramitedoc.service.CommonQryService;
import pe.gob.onpe.tramitedoc.service.DocumentoBasicoService;
import pe.gob.onpe.tramitedoc.service.DocumentoExtRecepService;
import pe.gob.onpe.tramitedoc.service.DocumentoObjService;
import pe.gob.onpe.tramitedoc.service.DocumentoVoBoService;
import pe.gob.onpe.tramitedoc.service.DocumentoXmlService;
import pe.gob.onpe.tramitedoc.service.EmiDocumentoAdmService;
import pe.gob.onpe.tramitedoc.service.NotificacionService;
import pe.gob.onpe.tramitedoc.service.ReferencedData;
import pe.gob.onpe.tramitedoc.service.TemaService;
import pe.gob.onpe.tramitedoc.util.Constantes;
import pe.gob.onpe.tramitedoc.util.Plantillas;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

@Controller
@RequestMapping("/{version}/srDocumentoAdmEmision.do")
public class DocumentoAdmEmisionController {
    
    @Autowired
    private EmiDocumentoAdmService emiDocumentoAdmService;
    
    @Autowired
    private ReferencedData referencedData;

    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private CommonQryService commonQryService;    
    
    @Autowired
    private DocumentoVoBoService documentoVoBoService;
    @Autowired
    private NotificacionService notiService;
    @Autowired
    private DocumentoExtRecepService documentoExtRecepService;
    @Autowired
    private TemaService temaService;
    @Autowired
    private DocumentoObjService documentoObjService;
    @Autowired
    private DocumentoXmlService documentoXmlService;
    @Autowired
    private DocumentoBasicoService documentoBasicoService;
    /* [HPB] Inicio 14/03/23 CLS-087-2022 */
    @Autowired
    private DatosPlantillaDao datosPlantillaDao;  
    /* [HPB] Fin 14/03/23 CLS-087-2022 */     
    @RequestMapping(method = RequestMethod.GET, params = "accion=goInicio")
    public String goInicio(HttpServletRequest request, Model model){
        //BuscarDocumentoEmiBean buscarDocumentoEmiBean = new BuscarDocumentoEmiBean();
        String codDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "coDep");
        String pEstado = ServletUtility.getInstancia().loadRequestParameter(request, "estadoDoc");        
        String pTiEnvMsj = ServletUtility.getInstancia().loadRequestParameter(request, "tiEnvMsj");/*--28/08/19 HPB Devolucion Doc a Oficina--*/        
        String coBandeja = ServletUtility.getInstancia().loadRequestParameter(request, "coBandeja");/*--HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS--*/        
        BuscarDocumentoEmiBean buscarDocumentoEmiBean = new BuscarDocumentoEmiBean();
        String sCoAnnio = Utility.getInstancia().dateToFormatStringYYYY(new Date());
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request); 
        TemaBean temabean =  new TemaBean();
        temabean.setCoDependencia(usuario.getCoDep());
        List<TemaBean> tema = temaService.getListTema(temabean);

        buscarDocumentoEmiBean.setsCoAnnio(sCoAnnio);
        buscarDocumentoEmiBean.setsCoAnnioBus(sCoAnnio);
        buscarDocumentoEmiBean.setsEstadoDoc(pEstado);
        buscarDocumentoEmiBean.setsCoDependencia(codDependencia);
        buscarDocumentoEmiBean.setsTiEnvMsj(pTiEnvMsj);/*--28/08/19 HPB Devolucion Doc a Oficina--*/
        buscarDocumentoEmiBean.setCoBandeja(coBandeja);/*--HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS--*/
        model.addAttribute(buscarDocumentoEmiBean);
        model.addAttribute("deListTema", tema);
        model.addAttribute("deAnnioList",referencedData.getAnnioList());
        model.addAttribute("deEstadosList",referencedData.getLstEstadosDocumentoEmi("TDTV_REMITOS"));
        model.addAttribute("dePrioridadesList",referencedData.getPrioridadesDocumentoList());
        model.addAttribute("deTipoDocumentoList",referencedData.getTipoDocumentoEmiList(codDependencia));        
        //model.addAttribute("deExpedienteList",referencedData.getExpedienteList(codDependencia));
        model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
        model.addAttribute("annioActual", Utility.getInstancia().dateToFormatStringYYYY(new Date()));
                DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("yyyy/MM/dd hh:mm:ss-> "+dtf5.format(LocalDateTime.now()));
        model.addAttribute("fechaHoraActual", dtf5.format(LocalDateTime.now()));
        model.addAttribute("deTipoEnvMsjList", referencedData.getLstTipoEnvioMsj());/*--28/08/19 HPB Devolucion Doc a Oficina--*/
        return "/DocumentoAdmEmi/documentoAdmEmi";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goInicio")
    public String goInicio(BuscarDocumentoEmiBean buscarDocumentoEmiBean, HttpServletRequest request,  BindingResult result, Model model){
        String mensaje = "NO_OK";

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String codDependencia = usuario.getCoDep();
        String codEmpleado = usuario.getCempCodemp();
        String tipAcceso = usuarioConfigBean.getTiAcceso();
        buscarDocumentoEmiBean.setsCoDependencia(codDependencia);
        buscarDocumentoEmiBean.setsCoEmpleado(codEmpleado);
        buscarDocumentoEmiBean.setsTiAcceso(tipAcceso);
        DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("yyyy/MM/dd hh:mm:ss-> "+dtf5.format(LocalDateTime.now()));
        List list = null;
        /* HPB 27/05/2020 - Inicio - Requerimiento Paginación emitidos */
        List<Integer> lsCantidadPg = new ArrayList<Integer>();
        List listCantidad = null;
        String vPagina = "1";
        int paginas = 0;
        int paginasx = 0;
        vPagina = ServletUtility.getInstancia().loadRequestParameter(request, "vPagina");
        /* HPB 27/05/2020 - Fin - Requerimiento Paginación emitidos */
        System.out.println("vPagina: "+vPagina);
        try{
            //list = emiDocumentoAdmService.getDocumentosBuscaEmiAdm(buscarDocumentoEmiBean);
            //list = emiDocumentoAdmService.getDocumentosBuscaEmiAdmFiltroReg(buscarDocumentoEmiBean);//Hermes 05/09/2018
            /* HPB 27/05/2020 - Inicio - Requerimiento Paginación emitidos */
            listCantidad = emiDocumentoAdmService.getDocumentosBuscaEmiAdmFiltroRegSize(buscarDocumentoEmiBean);
            System.out.println("SIZE: "+listCantidad.size());
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
            //list = recepDocumentoAdmService.getDocumentosBuscaRecepAdm2(buscarDocumentoRecepBean, vPagina, cantidad);
            list = emiDocumentoAdmService.getDocumentosBuscaEmiAdmFiltroReg(buscarDocumentoEmiBean, vPagina, cantidad);
            for (int i=1; i<=paginas; i++){
                lsCantidadPg.add(i);
            }
            /* HPB 27/05/2020 - Fin - Requerimiento Paginación emitidos */                
        }catch(Exception ex){
            mensaje = ex.getMessage();
        }

        if (list!=null) {
            if (list.size()>=100) {
                //model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
            }
            /* HPB 27/05/2020 - Inicio - Requerimiento Paginación emitidos */
            model.addAttribute("vPagina", vPagina);
            model.addAttribute("vTotalPaginas", paginas);
            model.addAttribute("lsCantidadPg", lsCantidadPg);
            model.addAttribute("cantRegistros", listCantidad.size());
            /* HPB 27/05/2020 - Fin - Requerimiento Paginación emitidos */         
            model.addAttribute("emiDocumAdmList",list);
            model.addAttribute("fechaHoraActual", dtf5.format(LocalDateTime.now()));
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        if (mensaje.equals("OK")) {
            return "/DocumentoAdmEmi/tblDocAdmEmision";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goNuevoDocumentoEmi")
    public String goNuevoDocumentoEmi(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       String nuAnn = Utility.getInstancia().dateToFormatStringYYYY(new Date());
       String fechaActual = Utility.getInstancia().dateToFormatString(new Date());
       String fechaHoraActual = Utility.getInstancia().dateToFormatString2(new Date());
       String codDependencia = usuario.getCoDep();
       DocumentoEmiBean documentoEmiBean = new DocumentoEmiBean();
       System.out.println("NUEVO");
       try{
           String esDocEmi = "5";
           documentoEmiBean = emiDocumentoAdmService.getDocumentoEmiAdmNew(esDocEmi,codDependencia);//estadoDocEmi, codigo de dependencia
           documentoEmiBean.setEsDocEmi(esDocEmi);
           documentoEmiBean.setCoDepEmi(codDependencia);
           documentoEmiBean.setCoLocEmi(usuario.getCoLocal());
           documentoEmiBean.setCoEmpRes(usuario.getCempCodemp());
           documentoEmiBean.setDeEmpRes(usuario.getDeFullName());
           documentoEmiBean.setFeEmiCorta(fechaActual);
           documentoEmiBean.setTiEmi("01");
           documentoEmiBean.setNuDiaAte("0");
           documentoEmiBean.setNuAnn(nuAnn);
            /*interoperabilidad*/
           documentoEmiBean.setTiEnvMsj("-1");
           model.addAttribute("pDatosOblDesJur",commonQryService.obtenerValorParametro("DATOS_OBL_DES_JUR"));
           /*interoperabilidad*/
           /* [HPB] Inicio 14/03/23 CLS-087-2022 */
           documentoEmiBean.setDeLocEmi(datosPlantillaDao.getDistritoLocal(usuario.getCoLocal()));
           /* [HPB] Inicio 14/03/23 CLS-087-2022 */
           /*model.addAttribute("snuAnn","");
           model.addAttribute("snuEmi","");
           model.addAttribute("docEstadoMsj",documentoEmiBean.getDocEstadoMsj());
           model.addAttribute("esDocEmi",documentoEmiBean.getEsDocEmi());
           model.addAttribute("tiDest",documentoEmiBean.getTiDest());
           model.addAttribute("tiEnvMsj",documentoEmiBean.getTiEnvMsj());*/
           
           model.addAttribute("sEsNuevoDocAdm","1");
           model.addAttribute("sTipoDestEmi","01");
           model.addAttribute("deAnnioList",referencedData.getAnnioList());
           model.addAttribute("lstTipDocReferenciaEmi",referencedData.getLstTipDocReferencia(codDependencia));           
           model.addAttribute("lstTipDocDependencia",referencedData.listTipDocXDependencia(codDependencia));
           model.addAttribute("lstTipoDestinatarioEmi",referencedData.getTipoDestinatarioEmiList());
           model.addAttribute("lstGrupoDestinatario",referencedData.getGrupoDestinatario(codDependencia));
           model.addAttribute("lstLocalRemitenteEmi",referencedData.getLstLocalRemitenteEmi(codDependencia));
           model.addAttribute("lstDependenciaRemitenteEmi",referencedData.getLstDependenciaRemitenteEmi(codDependencia));            
           model.addAttribute("lstPrioridadDestEmi",referencedData.getLstPrioridadDestEmi());           
           model.addAttribute("pfechaHoraActual",fechaHoraActual);
           model.addAttribute("pcodEmp",usuario.getCempCodemp());
           model.addAttribute("pdesEmp",usuario.getDeFullName());
           model.addAttribute(documentoEmiBean);
           model.addAttribute("inCreaExpediente",commonQryService.obtenerValorParametro("IN_CREA_EXPEDIENTE"));
           System.out.println("documentoEmiBean.getCoLocEmi()--> "+ documentoEmiBean.getCoLocEmi());
           System.out.println("documentoEmiBean.getDeLocEmi()--> "+ documentoEmiBean.getDeLocEmi());
           //jazanero
           model.addAttribute("pusuario",usuario.getCoUsuario());
           
           
           
           
           mensaje = "OK";
       }catch(Exception ex){
          mensaje = ex.getMessage(); 
       }
       if (mensaje.equals("OK")) {
           return "/DocumentoAdmEmi/nuevoDocumAdmEmi";
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }       
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goEditDocumentoEmi")
    public String goEditDocumentoEmi(HttpServletRequest request, Model model){
       String mensaje = "NO_OK";
       String snuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String snuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
       String sexisteDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pexisteDoc");
       String sexisteAnexo = ServletUtility.getInstancia().loadRequestParameter(request, "pexisteAnexo");
       
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       String fechaHoraActual = Utility.getInstancia().dateToFormatString2(new Date());
       String codDependencia = usuario.getCoDep();
       DocumentoEmiBean documentoEmiBean;
       //ExpedienteBean expedienteBean;
       List listReferenciaDocAdmEmi;
       System.out.println("EDITAR");        
       try{
           documentoEmiBean = emiDocumentoAdmService.getDocumentoEmiAdm(snuAnn,snuEmi);
           //expedienteBean = emiDocumentoAdmService.getExpDocumentoEmitido(documentoEmiBean.getNuAnnExp(), documentoEmiBean.getNuSecExp());
           System.out.println("goEditDocumentoEmi--> "+ documentoEmiBean.getDocEstadoMsj());
            /*[HPB-02/10/20] Inicio - Plazo de Atencion-*/
            if(documentoEmiBean.getFePlaAte() != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf.parse(documentoEmiBean.getFePlaAte());
                sdf.applyPattern("dd/MM/yyyy");
                documentoEmiBean.setFePlaAte(sdf.format(d));
                String pVerificDocParaCerrarPA = documentoBasicoService.getVerificaDocParaCerraPlazoAtencion(usuario.getCempCodemp(), snuAnn,snuEmi);
                model.addAttribute("sVerificDocParaCerrarPA", pVerificDocParaCerrarPA);
            }
            /*[HPB-02/10/20] Fin - Plazo de Atencion-*/
           String stipoDestinatario = emiDocumentoAdmService.getTipoDestinatarioEmi(snuAnn, snuEmi);//obtener tipo de Destinatario
           if(stipoDestinatario != null){
               HashMap map = emiDocumentoAdmService.getLstDestintariotlbEmi(snuAnn,snuEmi);
               model.addAttribute("lstDestintarioDocAdmEmi",map.get("lst1"));
               model.addAttribute("lstDestintarioProveedorDocAdmEmi",map.get("lst2"));
               model.addAttribute("lstDestintarioCiudadanoDocAdmEmi",map.get("lst3"));
               model.addAttribute("lstDestintarioOtroOrigenDocAdmEmi",map.get("lst4"));
           }else{
              stipoDestinatario = "01"; 
           }
          /* model.addAttribute("snuAnn",snuAnn);
           model.addAttribute("snuEmi",snuEmi);
           model.addAttribute("docEstadoMsj",documentoEmiBean.getDocEstadoMsj());
           model.addAttribute("esDocEmi",documentoEmiBean.getEsDocEmi());
           model.addAttribute("tiDest",documentoEmiBean.getTiDest());
           model.addAttribute("tiEnvMsj",documentoEmiBean.getTiEnvMsj());*/
           model.addAttribute("sexisteDoc",sexisteDoc);
           model.addAttribute("sexisteAnexo",sexisteAnexo);
           
           model.addAttribute("sTipoDestEmi",stipoDestinatario);
           model.addAttribute("sEsNuevoDocAdm","0");
           /* [HPB] Inicio 14/03/23 CLS-087-2022 */
           documentoEmiBean.setDeLocEmi(datosPlantillaDao.getDistritoLocal(documentoEmiBean.getCoLocEmi()));
           /* [HPB] Inicio 14/03/23 CLS-087-2022 */
           listReferenciaDocAdmEmi = emiDocumentoAdmService.getLstDocumReferenciatblEmi(snuAnn,snuEmi);
           model.addAttribute("lstTipDocDependencia",referencedData.listTipDocXDependencia(codDependencia));
           model.addAttribute("pfechaHoraActual",fechaHoraActual);
           model.addAttribute("pcodEmp",usuario.getCempCodemp());
           model.addAttribute("pdesEmp",usuario.getDeFullName());
           documentoEmiBean.setExisteDoc(sexisteDoc);
           documentoEmiBean.setExisteAnexo(sexisteAnexo);
           model.addAttribute(documentoEmiBean);
           //model.addAttribute("expedienteBean",expedienteBean);
           model.addAttribute("lstPrioridadDestEmi",referencedData.getLstPrioridadDestEmi());
           model.addAttribute("lstTipDocReferenciaEmi",referencedData.getLstTipDocReferencia(codDependencia));
           model.addAttribute("deAnnioList",referencedData.getAnnioList());
           
           model.addAttribute("lstReferenciaDocAdmEmi",listReferenciaDocAdmEmi);
           model.addAttribute("lstTipoDestinatarioEmi",referencedData.getTipoDestinatarioEmiList());
           model.addAttribute("lstGrupoDestinatario",referencedData.getGrupoDestinatario(codDependencia));
           model.addAttribute("lstMotivoDestinatario",referencedData.getLstMotivoDestinatario(documentoEmiBean.getCoDepEmi(),documentoEmiBean.getCoTipDocAdm()));
           model.addAttribute("lstDependenciaRemitenteEmi",referencedData.getLstDependenciaRemitenteEmi(documentoEmiBean.getCoDepEmi()));
           model.addAttribute("lstLocalRemitenteEmi",referencedData.getLstLocalRemitenteEmi(documentoEmiBean.getCoDepEmi()));
           List<EmpleadoVoBoBean> lsEmpVobo=commonQryService.getLsPersonalVoBo(snuAnn,snuEmi);
           model.addAttribute("lstEmpVoBoDocAdm",lsEmpVobo);
           model.addAttribute("lstNotiDocAdmEmi",emiDocumentoAdmService.getNotificaciones(lsEmpVobo,documentoEmiBean.getInFirmaAnexo()));
           //model.addAttribute("refRecepDocumAdmList",list);
           model.addAttribute("inCreaExpediente",commonQryService.obtenerValorParametro("IN_CREA_EXPEDIENTE"));
           /*interoperabilidad*/           
           model.addAttribute("pDatosOblDesJur",commonQryService.obtenerValorParametro("DATOS_OBL_DES_JUR"));
           /*interoperabilidad*/           
           model.addAttribute("pusuario",usuario.getCoUsuario());
           
           //jazanero
           if(documentoEmiBean.getNuEmiProyecto()!=null && documentoEmiBean.getNuEmiProyecto().length()>0 
                   && documentoEmiBean.getNuAnnProyecto()!=null && documentoEmiBean.getNuAnnProyecto().length()>0)
                model.addAttribute("sEsTipoProyecto", "SI");
           else
                model.addAttribute("sEsTipoProyecto", "NO");
            
            model.addAttribute("pNuEmiProyecto", documentoEmiBean.getNuEmiProyecto());
            model.addAttribute("pNuAnnProyecto", documentoEmiBean.getNuAnnProyecto());
            //jazanero
           
           mensaje = "OK";
           /*--------------------Hermes 29/04/2019--------------------*/
           /*-----Valida check de un documento proyecto en estado PARA DESPACHO----*/
           List<DocumentoAnexoBean> docAnexoList = documentoBasicoService.getAnexosList(snuAnn,snuEmi);
           if(documentoEmiBean.getCoTipDocAdm().equals("332")){
                int contador = 0;
                boolean  vValidaProyectoDoc = false; 
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
                    vValidaProyectoDoc = true;
                }
                model.addAttribute("pValidaProyectoDoc", vValidaProyectoDoc);
            }           
           /*--------------------Hermes 29/04/2019--------------------*/
           //model.addAttribute("pNuEmiProyecto", documentoEmiBean.getNuEmiProyecto());
       }catch(Exception ex){
          mensaje = ex.getMessage(); 
       }
       if (mensaje.equals("OK")) {
           return "/DocumentoAdmEmi/nuevoDocumAdmEmi";
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }       
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goGrabaDocumentoEmi",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goGrabaDocumentoEmi(@RequestBody TrxDocumentoEmiBean trxDocumentoEmiBean,HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String coRespuesta;
        String deRespuesta;
        String sCrearExpediente = ServletUtility.getInstancia().loadRequestParameter(request,"pcrearExpediente");//0 no crea, 1 crea
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        trxDocumentoEmiBean.setCoUserMod(usuario.getCoUsuario());
        trxDocumentoEmiBean.setCempCodEmp(usuario.getCempCodemp());

        try{
            mensaje = emiDocumentoAdmService.grabaDocumentoEmiAdm(trxDocumentoEmiBean,sCrearExpediente,usuario);
            //ProbarFrankSilva
//            if (mensaje.equals("OK")) {
//                String vReturn = notiService.procesarNotificacion(trxDocumentoEmiBean.getNuAnn(), trxDocumentoEmiBean.getNuEmi(), usuario.getCoUsuario());
//                if (!vReturn.equals("OK")) {
//                    throw new validarDatoException(vReturn);
//                }
//            }
        }catch(validarDatoException e){
           mensaje=(e.valorMsg);
        }catch (Exception e) { 
           e.printStackTrace(); 
        } 
        
        if (mensaje.equals("OK")) {
            coRespuesta = "1";
        }else if(mensaje.equals("NO_OK")){
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
        retval.append("\",\"nuEmi\":\"");
        retval.append(trxDocumentoEmiBean.getNuEmi());  
        retval.append("\",\"nuCorEmi\":\"");
        retval.append(trxDocumentoEmiBean.getNuCorEmi());
        retval.append("\",\"nuDocEmi\":\"");
        retval.append(trxDocumentoEmiBean.getNuDocEmi());        
        retval.append("\",\"nuAnnExp\":\"");
        retval.append(trxDocumentoEmiBean.getNuAnnExp());
        retval.append("\",\"nuSecExp\":\"");
        retval.append(trxDocumentoEmiBean.getNuSecExp());
        retval.append("\",\"feExp\":\"");
        retval.append(trxDocumentoEmiBean.getFeExp());                 
        retval.append("\",\"nuExpediente\":\"");
        retval.append(trxDocumentoEmiBean.getNuExpediente());                                  
        retval.append("\",\"nuDetExp\":\"");
        retval.append("1");        
        retval.append("\"}");

        return retval.toString();        
    } 
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdTlbsDestinatario")
    public String goUpdTlbsDestinatario(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String snuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String snuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");        
       
       try{
            HashMap map = emiDocumentoAdmService.getLstDestintariotlbEmi(snuAnn,snuEmi);
            model.addAttribute("lstDestintarioDocAdmEmi",map.get("lst1"));
            model.addAttribute("lstDestintarioProveedorDocAdmEmi",map.get("lst2"));
            model.addAttribute("lstDestintarioCiudadanoDocAdmEmi",map.get("lst3"));
            model.addAttribute("lstDestintarioOtroOrigenDocAdmEmi",map.get("lst4"));
            model.addAttribute("lstLocalRemitenteEmi",referencedData.getLstLocal());
            model.addAttribute("lstPrioridadDestEmi",referencedData.getLstPrioridadDestEmi());
           mensaje = "OK";
       }catch(Exception ex){
          mensaje = ex.getMessage(); 
       }
       if (mensaje.equals("OK")) {
           return "/DocumentoAdmEmi/actualizaTablasDestinoEmiDoc";
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }       
    }  
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdTlbReferencia")
    public String goUpdTlbReferencia(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String snuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String snuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");        
        String scoDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDependencia");

        try{
           model.addAttribute("lstReferenciaDocAdmEmi",emiDocumentoAdmService.getLstDocumReferenciatblEmi(snuAnn,snuEmi)); 
           model.addAttribute("deAnnioList",referencedData.getAnnioList());
           model.addAttribute("lstTipDocReferenciaEmi",referencedData.getLstTipDocReferencia(scoDependencia));
           mensaje = "OK";
       }catch(Exception ex){
          mensaje = ex.getMessage(); 
       }
       if (mensaje.equals("OK")) {
           return "/DocumentoAdmEmi/tablaRefEmiDocAdm";
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }       
    }    
     
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaReferenciaOrigen")
    public String goBuscaReferenciaOrigen(HttpServletRequest request, Model model){
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        model.addAttribute("listaReferenOrig",referencedData.getListReferenciaOrigen(pnuAnn,usuarioConfigBean));
        return "/modalGeneral/consultaReferenciaOrigen";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestinatario")
    public String goBuscaDestinatario(HttpServletRequest request, Model model){
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        //model.addAttribute("listaDestinatario",referencedData.getListDestinatarioEmi(pnuAnn,usuarioConfigBean));
        model.addAttribute("listaDestinatario",referencedData.getListDestinatarioEmiFiltro2(pnuAnn,usuarioConfigBean));//Hermes 05/09/2018
        return "/modalGeneral/consultaDestinatarioEmi";
    }   
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaElaboradoPor")
    public String goBuscaElaboradoPor(HttpServletRequest request, Model model){
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);        
        String pcoDep = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDep");
        model.addAttribute("listaEmpleado",referencedData.getListEmpleadoElaboradoPor(pcoDep,usuarioConfigBean.getTiAcceso(),usuarioConfigBean.getCempCodemp()));
        return "/modalGeneral/consultaElaboradoPor";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDependenciaDestinatario")
    public String goBuscaDependenciaDestinatario(HttpServletRequest request, Model model){
        String pdeDepen = ServletUtility.getInstancia().loadRequestParameter(request, "pdeDepen");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        model.addAttribute("listaDependenciaDestEmi",referencedData.getListDependenciaDestinatarioEmi(usuario.getCoDep(),pdeDepen));
        return "/modalGeneral/consultaDependenciaDestEmi";
    }
    /*[HPB] 02/02/21 Orden de trabajo*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDependenciaDestinatarioOT")
    public String goBuscaDependenciaDestinatarioOT(HttpServletRequest request, Model model){
        String pdeDepen = ServletUtility.getInstancia().loadRequestParameter(request, "pdeDepen");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        model.addAttribute("listaDependenciaDestEmi",referencedData.getListDependenciaDestinatarioEmiOT(usuario.getCoDep(),pdeDepen));
        return "/modalGeneral/consultaDependenciaDestEmi";
    }
    /*[HPB] 02/02/21 Orden de trabajo*/
    /*-----------------------------------Hermes 17/07/2018-------------------------------------------*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDependenciaDestinatario2")
    public String goBuscaDependenciaDestinatario2(HttpServletRequest request, Model model){
        String pdeDepen = ServletUtility.getInstancia().loadRequestParameter(request, "pdeDepen");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        model.addAttribute("listaDependenciaDestEmi",referencedData.getListDependenciaDestinatarioEmi2(usuario.getCoDep(),pdeDepen));
        return "/modalGeneral/consultaDependenciaDestEmiDetalle";
    }
    /*--------------------------------------------------------------------------------------------------*/
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaCiudadano")
    public @ResponseBody String goBuscaCiudadano(HttpServletRequest request, Model model){
        String vRespuesta;
        String coRespuesta;
        StringBuilder retval = new StringBuilder();        
        String pnuDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDoc");
        CiudadanoBean ciudadanoBean;
        try{
            ciudadanoBean = emiDocumentoAdmService.getCiudadano(pnuDoc);
           if (ciudadanoBean != null) {
                coRespuesta = "1";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"ciudadano\":");
                retval.append("{\"nuDoc\":\"");
                retval.append(ciudadanoBean.getNuDocumento());
                retval.append("\",\"nombre\":\"");
                retval.append(ciudadanoBean.getNombre());
                //retval.append("\"");                
                retval.append("\",\"ubigeo\":\"");
                retval.append(ciudadanoBean.getUbigeo());                
                retval.append("\",\"ubdep\":\""); 
                retval.append(ciudadanoBean.getIdDepartamento());                
                retval.append("\",\"ubprv\":\""); 
                retval.append(ciudadanoBean.getIdProvincia());                
                retval.append("\",\"ubdis\":\""); 
                retval.append(ciudadanoBean.getIdDistrito());                 
                retval.append("\",\"dedomicil\":\""); 
                retval.append(ciudadanoBean.getDeDireccion());
                retval.append("\",\"deemail\":\""); 
                retval.append(ciudadanoBean.getDeCorreo());
                retval.append("\",\"detelefo\":\""); 
                retval.append(ciudadanoBean.getTelefono());
                retval.append("\"");
                
                retval.append("}");            
                retval.append("}");            
           }else{
                coRespuesta = "0";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\"}");
           }            
        }catch(Exception e){
            vRespuesta = e.getMessage();
            model.addAttribute("pMensaje", vRespuesta);
            coRespuesta = "0";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\"}");
        }       
        return retval.toString();
    }    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestOtroOrigenAgrega")
    public String goBuscaDestOtroOrigenAgrega(HttpServletRequest request, Model model){
        String pnomOtroOrigen = ServletUtility.getInstancia().loadRequestParameter(request, "pnoOtroOrigen");
        model.addAttribute("lsDestOtroOrigen",emiDocumentoAdmService.getLstOtrosOrigenesAgrega(pnomOtroOrigen));
        model.addAttribute("iniFuncionParm","1");
        return "/modalGeneral/consultaDestOtroOrigen";
    }
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestOtroOrigen")
    public String goBuscaDestOtroOrigen(HttpServletRequest request, Model model){
        String pnomOtroOrigen = ServletUtility.getInstancia().loadRequestParameter(request, "pnoOtroOrigen");
        model.addAttribute("lsDestOtroOrigen",documentoExtRecepService.getLstOtrosOrigenes(pnomOtroOrigen));
        model.addAttribute("iniFuncionParm","3");
        return "/modalGeneral/consultaDestOtroOrigen";
    }
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestCiudadano")
    public String goBuscaDestCiudadano(HttpServletRequest request, Model model){ 
        String pnoCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "pnoCiudadano");
        model.addAttribute("lsDestCiudadano",emiDocumentoAdmService.getLstCiudadano(pnoCiudadano));
        model.addAttribute("iniFuncionParm","1");
        return "/modalGeneral/consultaDestCiudadano";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestProveedorAgrega")
    public String goBuscaDestProveedorAgrega(HttpServletRequest request, Model model){
        String prazonSocial = ServletUtility.getInstancia().loadRequestParameter(request, "prazonSocial");
        model.addAttribute("lsDestProveedor",emiDocumentoAdmService.getLstProveedoresAgrega(prazonSocial));
        model.addAttribute("iniFuncionParm","1");
        return "/modalGeneral/consultaDestProveedor";
    }        
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaPersonalDestinatario")
    public String goBuscaPersonalDestinatario(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String pcoDepen = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDepen");
        List list = null;
        try{
            list = emiDocumentoAdmService.getPersonalDestinatario(pcoDepen);
        }catch(Exception ex){
             mensaje = ex.getMessage();
        }
        if (list!=null) {
            model.addAttribute("listaEmpleadoDestEmi",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaEmpleadoDestEmi";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }        
    }  
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaAccionDestinatario")
    public String goBuscaAccionDestinatario(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String pcoTipDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoTipDoc");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String pcoDepen = usuario.getCoDep();
        List list = null;
        try{
            list = emiDocumentoAdmService.getLstMotivoxTipoDocumento(pcoDepen,pcoTipDoc);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaEmpleadoLocaltblDestinatario",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goBuscaEmpleadoLocaltblDestinatario(HttpServletRequest request, Model model) throws Exception {
       String vRespuesta = "";
       String pcoDepen = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDepen");
       String pcoTipoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoTipoDoc");
       String coRespuesta = "";
       StringBuilder retval = new StringBuilder();
       EmpleadoBean empleadoBean  = null;
       try{
           empleadoBean = emiDocumentoAdmService.getEmpleadoLocaltblDestinatario(pcoDepen);
           if (empleadoBean != null) {
                coRespuesta = "1";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"coLocal\":\"");
                retval.append(empleadoBean.getCoLocal());
                retval.append("\",\"deLocal\":\"");
                retval.append(empleadoBean.getDeLocal());
                retval.append("\",\"coEmpleado\":\"");
                retval.append(empleadoBean.getCempCodemp());
                retval.append("\",\"deEmpleado\":\"");
                retval.append(empleadoBean.getCompName());
                retval.append("\",\"coTramiteFirst\":\"");
                if(pcoTipoDoc.equals("232")){
                        retval.append(Constantes.CO_TRAMITE_ATENDER);
                        retval.append("\",\"deTramiteFirst\":\"");
                        retval.append(Constantes.DE_TRAMITE_ATENDER);                	
                }else{
                        retval.append(Constantes.CO_TRAMITE_ORIGINAL);
                        retval.append("\",\"deTramiteFirst\":\"");
                        retval.append(Constantes.DE_TRAMITE_ORIGINAL);                
                }
                retval.append("\",\"coTramiteNext\":\"");
                if(pcoTipoDoc.equals("232")){
                        retval.append(Constantes.CO_TRAMITE_FINES);
                        retval.append("\",\"deTramiteNext\":\"");
                        retval.append(Constantes.DE_TRAMITE_FINES);                	
                }else{
                        retval.append(Constantes.CO_TRAMITE_COPIA);
                        retval.append("\",\"deTramiteNext\":\"");
                        retval.append(Constantes.DE_TRAMITE_COPIA);                
                }               
                retval.append("\"}");            
           }else{
                coRespuesta = "0";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"deRespuesta\":\"");
                retval.append("NO SE PUEDE REGISTRAR DESTINATARIO");                
                retval.append("\"}");
           }
       }catch(Exception e){
            vRespuesta = e.getMessage();
            coRespuesta = "0";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append(vRespuesta);                            
            retval.append("\"}");
       }
       return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDocumentotblReferencia")
    public String goBuscaDocumentotblReferencia(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String pannio = ServletUtility.getInstancia().loadRequestParameter(request, "pannio");
        String ptiDoc = ServletUtility.getInstancia().loadRequestParameter(request, "ptiDoc");
        String pnuDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pnuDoc");
        String ptiBusqueda = ServletUtility.getInstancia().loadRequestParameter(request, "ptiBusqueda");// rec doc recepcionado, emi doc emitido
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);        
        String pcoEmpEmi="";
        if(usuarioConfigBean.getTiAcceso().equals("1")){//acceso personal
            pcoEmpEmi = usuario.getCempCodemp();
        }
        String pcoDepen = usuario.getCoDep();      
        List list = null;
        try{
            if(ptiBusqueda.equals("emi")){ // doc emitido
                list = emiDocumentoAdmService.getLstDocEmitidoRef(pcoEmpEmi,pcoDepen,pannio,ptiDoc,pnuDoc);                
            }else if(ptiBusqueda.equals("rec")){ // doc recepcionado
                list = emiDocumentoAdmService.getLstDocRecepcionadoRef(pcoDepen,pannio,ptiDoc,pnuDoc,usuarioConfigBean.getInMesaPartes());
            }
        }catch(Exception ex){
             mensaje = ex.getMessage();
        }
        if (list!=null) {
            if (list.size()>=200) {
                model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
            }            
            model.addAttribute("lstDocReferenciaEmi",list);
            mensaje = "OK";
        }else {
            mensaje = "No se encuentran registros.";
        }
        
        if (mensaje.equals("OK")) {
            return "/modalGeneral/consultaDocumentoRefEmi";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaLstMotivoDocEmi")
    public @ResponseBody String goBuscaLstMotivoDocEmi(HttpServletRequest request, Model model){
        String vRespuesta;
        String coRespuesta;
        StringBuilder retval = new StringBuilder();
        String pcoTipDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoTipDoc");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String pcoDepen = usuario.getCoDep();
        List<MotivoBean> list = null;
        boolean bandera = false;
        try{
            list = emiDocumentoAdmService.getLstMotivoxTipoDocumento(pcoDepen,pcoTipDoc);
           if (list != null) {
                coRespuesta = "1";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"lsMotivo\":");
                retval.append("[");
                for (MotivoBean mot : list){
                    retval.append("{\"value\":\"");
                    retval.append(mot.getCoMot());
                    retval.append("\",\"label\":\"");
                    retval.append(mot.getDeMot());
                    retval.append("\"},");
                    bandera = true;
                }
                if(bandera){
                    retval.deleteCharAt(retval.length()-1);
                }
                retval.append("]");
                retval.append("}");            
           }else{
                coRespuesta = "0";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\"}");
           }            
        }catch(Exception e){
            vRespuesta = e.getMessage();
            model.addAttribute("pMensaje", vRespuesta);
            coRespuesta = "0";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\"}");
        }
        
        return retval.toString(); 
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAnularDocEmiAdm")
    public @ResponseBody String goAnularDocEmiAdm(DocumentoEmiBean documentoEmiBean, HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       documentoEmiBean.setCoUseMod(usuario.getCoUsuario());
       try{
          mensaje = emiDocumentoAdmService.anularDocumentoEmiAdm(documentoEmiBean,usuario);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarDestinatarioIntitucion",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goAgregarDestinatarioIntitucion(HttpServletRequest request, Model model) throws Exception {
//       String vRespuesta = "";
       String scogrupo = ServletUtility.getInstancia().loadRequestParameter(request, "pcogrupo");
       String pcoTipDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcoTipDoc");
//       String coRespuesta = "";
       String retval;

       retval = emiDocumentoAdmService.getLstDestintarioAgregaGrupo(scogrupo,pcoTipDoc);
           
       return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goCargarDocEmi")
    public @ResponseBody String goCargaDocEmi(HttpServletRequest request, Model model){
       String mensaje="NO_OK";
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "nuAnn");
       String vnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "nuEmi");
       String vnuSec = ServletUtility.getInstancia().loadRequestParameter(request, "nuSec");

       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       
       DocumentoObjBean docObjBean = new DocumentoObjBean();
       if (vnuAnn!=null && vnuEmi!=null && vnuSec!=null){
            try{
                 docObjBean.setNuAnn(vnuAnn);
                 docObjBean.setNuEmi(vnuEmi);
                 docObjBean.setTiCap(ServletUtility.getInstancia().loadRequestParameter(request, "tiCap"));
                 docObjBean.setNombreArchivo(ServletUtility.getInstancia().loadRequestParameter(request, "noDoc"));
                 docObjBean.setNumeroSecuencia(applicationProperties.getRutaTemporal()+"//"+vnuSec);
                 docObjBean.setCoUseMod(usuario.getCoUsuario());
                 mensaje = emiDocumentoAdmService.cargaDocEmi(docObjBean);
            }catch(Exception e){
                mensaje = "Error en Cargar Docuemntos";
                e.printStackTrace();
            }
       }else{
          mensaje = "Documento no encontrado";
       }
               
       
       if (mensaje!=null && mensaje.equals("OK")) {
            coRespuesta = "1";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append("Documento Cargado Correctamente");                            
            retval.append("\"}");
       } else {
            coRespuesta = "0";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append(mensaje);                            
            retval.append("\"}");
       }
       return retval.toString();
    }    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goVerificarNumDocEmi",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goVerificarNumDocEmi(DocumentoEmiBean documentoEmiBean,HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;

       try{
           mensaje = emiDocumentoAdmService.verificaNroDocumentoEmiDuplicado(documentoEmiBean);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goNuevoDocumentoEmiAtender")
    public String goNuevoDocumentoEmiAtender(DocumentoBean documentoRecepBean,HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       String nuAnn = Utility.getInstancia().dateToFormatStringYYYY(new Date());
       String fechaActual = Utility.getInstancia().dateToFormatString(new Date());
       String fechaHoraActual = Utility.getInstancia().dateToFormatString2(new Date());
       String codDependencia = usuario.getCoDep();
       DocumentoEmiBean documentoEmiBean = new DocumentoEmiBean();
       DocumentoEmiBean documentoEmiBeanTemporal = new DocumentoEmiBean();
       boolean esCargaProyecto = false;
       System.out.println("ATENDER");
       try{
           String sEsDocRec = documentoRecepBean.getEsDocRec();
           if(sEsDocRec.equals("0") || sEsDocRec.equals("9")){
               if(sEsDocRec.equals("0")){
                   mensaje = "Documento no Recepcionado..";
               }else{
                   mensaje = "Documento esta Anulado..";
               }
           }else{
                
               if(documentoRecepBean.getCoTipDocAdm()!=null && documentoRecepBean.getCoTipDocAdm().equals("332")){
                    if(documentoRecepBean.getNuEmi()!=null && documentoRecepBean.getNuAnn()!=null ){
                        esCargaProyecto = true;
                    }
               }
               
               if(esCargaProyecto){
                    System.out.println("accion=goNuevoDocumentoEmiAtender 1: "+ esCargaProyecto);
                    String esDocEmi = "5";
                       //me traigo el proyecto en documentoEmiBean                   
                    documentoEmiBean = emiDocumentoAdmService.getDocumentoEmiAdmProyectoAEmiBean(documentoRecepBean.getNuAnn(),documentoRecepBean.getNuEmi());  
                    HashMap map = emiDocumentoAdmService.getLstDestintariotlbEmiProyAEmiBean(documentoRecepBean.getNuAnn(),documentoRecepBean.getNuEmi());                
                    documentoEmiBeanTemporal = emiDocumentoAdmService.getDocumentoEmiAdmNew(esDocEmi,codDependencia);//estadoDocEmi, codigo de dependencia
                    
                    /*----------------Hermes 29/04/19------------------*/
                    List<DocumentoEmiBean> lstDocumentoEmiBean = emiDocumentoAdmService.getLstTipoDestEmi(documentoRecepBean.getNuAnn(),documentoRecepBean.getNuEmi());
                    String vDestino="";
                    for(int i=0; i<lstDocumentoEmiBean.size(); i++){
                        vDestino = vDestino + lstDocumentoEmiBean.get(i).getTiDest()+",";
                    }
                    /*----------------Hermes 29/04/19------------------*/
                    /*de_doc_sig,
                    CO_EMPLEADO co_emp_emi,
                    PK_SGD_DESCRIPCION.DE_NOM_EMP(CO_EMPLEADO) de_emp_emi,
                    PK_SGD_DESCRIPCION.ESTADOS (?,'TDTV_REMITOS') de_es_doc_emi,
                    '0' existe_doc,
                    '0' existe_anexo,
                    co_es_doc_emi*/   
                    documentoEmiBean.setDeDocSig(documentoEmiBeanTemporal.getDeDocSig());
                    //documentoEmiBean.setCoEmpEmi(documentoEmiBeanTemporal.getCoEmpEmi());
                    //documentoEmiBean.setDeEmpEmi(documentoEmiBeanTemporal.getDeEmpEmi());
                    documentoEmiBean.setDeEsDocEmi(documentoEmiBeanTemporal.getDeEsDocEmi());
                    documentoEmiBean.setExisteDoc(documentoEmiBeanTemporal.getExisteDoc());
                    documentoEmiBean.setExisteAnexo(documentoEmiBeanTemporal.getExisteAnexo());
                    //                    
                    documentoEmiBean.setEsDocEmi(esDocEmi);
                    documentoEmiBean.setCoDepEmi(codDependencia);
                    documentoEmiBean.setCoLocEmi(usuario.getCoLocal());
                    documentoEmiBean.setCoEmpRes(usuario.getCempCodemp());
                    documentoEmiBean.setDeEmpRes(usuario.getDeFullName());
                    documentoEmiBean.setFeEmiCorta(fechaActual);
                    documentoEmiBean.setTiEmi("01");
                    documentoEmiBean.setNuDiaAte("0");
                    documentoEmiBean.setNuAnn(nuAnn);
                    documentoEmiBean.setDeAsu(documentoRecepBean.getDeAsu());
                    documentoEmiBean.setNuExpediente(documentoRecepBean.getNuExpediente());
                    documentoEmiBean.setNuAnnExp(documentoRecepBean.getNuAnnExp());
                    documentoEmiBean.setNuSecExp(documentoRecepBean.getNuSecExp());
                    documentoEmiBean.setFeExpCorta(documentoRecepBean.getFeExpCorta());
                    documentoEmiBean.setCoProceso(documentoRecepBean.getCoProceso());
                    documentoEmiBean.setDeProceso(documentoRecepBean.getDeProceso());                    
                     //jazanero
                    documentoEmiBean.setNuEmiProyecto(documentoRecepBean.getNuEmi());
                    documentoEmiBean.setNuAnnProyecto(documentoRecepBean.getNuAnn());                    
                    documentoEmiBean.setNuEmi(null);
                    //jazanero
                    model.addAttribute("sEsNuevoDocAdm","1");
                    model.addAttribute("sTipoDestEmi",documentoEmiBean.getTiDest());//veremos
                    /*----------------Hermes 29/04/19------------------*/
                    model.addAttribute("sTipoDestEmiNew",vDestino);
                    /*----------------Hermes 29/04/19------------------*/
                    model.addAttribute("deAnnioList",referencedData.getAnnioList());
                    model.addAttribute("lstTipDocReferenciaEmi",referencedData.getLstTipDocReferencia(codDependencia));           
                    model.addAttribute("lstTipDocDependencia",referencedData.listTipDocXDependencia(codDependencia));
                    model.addAttribute("lstTipoDestinatarioEmi",referencedData.getTipoDestinatarioEmiList());
                    model.addAttribute("lstGrupoDestinatario",referencedData.getGrupoDestinatario(codDependencia));
                    model.addAttribute("lstLocalRemitenteEmi",referencedData.getLstLocalRemitenteEmi(documentoEmiBean.getCoDepEmi()));
                    model.addAttribute("lstDependenciaRemitenteEmi",referencedData.getLstDependenciaRemitenteEmi(codDependencia));            
                    model.addAttribute("lstPrioridadDestEmi",referencedData.getLstPrioridadDestEmi());           
                    model.addAttribute("lstReferenciaDocAdmEmi",emiDocumentoAdmService.getLstDocumReferenciaAtiendeDeriva(documentoRecepBean));
                    model.addAttribute("lstDestintarioDocAdmEmi",map.get("lst1"));
                    model.addAttribute("lstDestintarioProveedorDocAdmEmi",map.get("lst2"));
                    model.addAttribute("lstDestintarioCiudadanoDocAdmEmi",map.get("lst3"));
                    model.addAttribute("lstDestintarioOtroOrigenDocAdmEmi",map.get("lst4"));
                    model.addAttribute("pfechaHoraActual",fechaHoraActual);
                    model.addAttribute("pcodEmp",usuario.getCempCodemp());
                    model.addAttribute("pdesEmp",usuario.getDeFullName());

                    DocumentoEmiBean pdocumentoEmiBean = documentoBasicoService.getNumeroAnexoProyecto(documentoRecepBean.getNuAnn(), documentoRecepBean.getNuEmi());
                    if(pdocumentoEmiBean!=null){
                        documentoEmiBean.setNuAneProyecto(pdocumentoEmiBean.getNuAneProyecto());
                    } 

                    model.addAttribute(documentoEmiBean);
                    model.addAttribute("inCreaExpediente",commonQryService.obtenerValorParametro("IN_CREA_EXPEDIENTE"));

                    //jazanero
                    model.addAttribute("sEsTipoProyecto", "SI");
                    model.addAttribute("pNuEmiProyecto", documentoRecepBean.getNuEmi());
                    model.addAttribute("pNuAnnProyecto", documentoRecepBean.getNuAnn());   
                    //jazanero
                    
                    mensaje = "OK";   

               }else{
                System.out.println("accion=goNuevoDocumentoEmiAtender 2: "+ esCargaProyecto);
                String esDocEmi = "5";
                documentoEmiBean = emiDocumentoAdmService.getDocumentoEmiAdmNew(esDocEmi,codDependencia);//estadoDocEmi, codigo de dependencia                            

                documentoEmiBean.setEsDocEmi(esDocEmi);
                documentoEmiBean.setCoDepEmi(codDependencia);
                documentoEmiBean.setCoLocEmi(usuario.getCoLocal());
                documentoEmiBean.setCoEmpRes(usuario.getCempCodemp());
                documentoEmiBean.setDeEmpRes(usuario.getDeFullName());
                documentoEmiBean.setFeEmiCorta(fechaActual);
                documentoEmiBean.setTiEmi("01");
                documentoEmiBean.setNuDiaAte("0");
                documentoEmiBean.setNuAnn(nuAnn);
                documentoEmiBean.setDeAsu(documentoRecepBean.getDeAsu());
                documentoEmiBean.setNuExpediente(documentoRecepBean.getNuExpediente());
                documentoEmiBean.setNuAnnExp(documentoRecepBean.getNuAnnExp());
                documentoEmiBean.setNuSecExp(documentoRecepBean.getNuSecExp());
                documentoEmiBean.setFeExpCorta(documentoRecepBean.getFeExpCorta());
                documentoEmiBean.setCoProceso(documentoRecepBean.getCoProceso());
                documentoEmiBean.setDeProceso(documentoRecepBean.getDeProceso());
                /*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
                documentoEmiBean.setCoGru(documentoRecepBean.getCoGru());
                documentoEmiBean.setTiEmiExp(documentoRecepBean.getTiEmi());
                documentoEmiBean.setNuOriEmi(documentoRecepBean.getNuOriEmi());
                /*HPB 11/02/2020 - Fin - Requerimiento Remitente expediente*/
                //jazanero
//                documentoEmiBean.setNuEmiProyecto(documentoRecepBean.getNuEmi());
//                documentoEmiBean.setNuAnnProyecto(documentoRecepBean.getNuAnn());
                //jazanero
                
                
                model.addAttribute("sEsNuevoDocAdm","1");
                model.addAttribute("sTipoDestEmi","01");
                model.addAttribute("deAnnioList",referencedData.getAnnioList());
                model.addAttribute("lstTipDocReferenciaEmi",referencedData.getLstTipDocReferencia(codDependencia));           
                model.addAttribute("lstTipDocDependencia",referencedData.listTipDocXDependencia(codDependencia));
                model.addAttribute("lstTipoDestinatarioEmi",referencedData.getTipoDestinatarioEmiList());
                model.addAttribute("lstGrupoDestinatario",referencedData.getGrupoDestinatario(codDependencia));
                model.addAttribute("lstLocalRemitenteEmi",referencedData.getLstLocalRemitenteEmi(documentoEmiBean.getCoDepEmi()));
                model.addAttribute("lstDependenciaRemitenteEmi",referencedData.getLstDependenciaRemitenteEmi(codDependencia));            
                model.addAttribute("lstPrioridadDestEmi",referencedData.getLstPrioridadDestEmi());           
                model.addAttribute("lstReferenciaDocAdmEmi",emiDocumentoAdmService.getLstDocumReferenciaAtiendeDeriva(documentoRecepBean)); 
                model.addAttribute("pfechaHoraActual",fechaHoraActual);
                model.addAttribute("pcodEmp",usuario.getCempCodemp());
                model.addAttribute("pdesEmp",usuario.getDeFullName());
                
//                if(documentoRecepBean.getCoTipDocAdm()!=null && documentoRecepBean.getCoTipDocAdm().equals("332")){
//                    if(documentoRecepBean.getNuEmi()!=null && documentoRecepBean.getNuAnn()!=null ){
//                        
//                        DocumentoEmiBean pdocumentoEmiBean = documentoBasicoService.getNumeroAnexoProyecto(documentoRecepBean.getNuAnn(), documentoRecepBean.getNuEmi());
//                        if(pdocumentoEmiBean!=null){
//                            documentoEmiBean.setNuAneProyecto(pdocumentoEmiBean.getNuAneProyecto());
//                        }                        
//                    }
//                } 
                
                model.addAttribute(documentoEmiBean);
                model.addAttribute("inCreaExpediente",commonQryService.obtenerValorParametro("IN_CREA_EXPEDIENTE"));
                
                //jazanero
                model.addAttribute("sEsTipoProyecto", "NO");
//                model.addAttribute("pNuEmiProyecto", documentoRecepBean.getNuEmi());
//                model.addAttribute("pNuAnnProyecto", documentoRecepBean.getNuAnn());   
                //jazanero
                
                mensaje = "OK";    
               }
                                         
           }
       }catch(Exception ex){
          mensaje = ex.getMessage(); 
       }
       if (mensaje.equals("OK")) {
           return "/DocumentoAdmEmi/nuevoDocumAdmEmi";
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }       
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaElaboradoPorEdit")
    public String goBuscaElaboradoPorEdit(HttpServletRequest request, Model model){
        String pcoDep = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDep");
        model.addAttribute("listaEmpleado",emiDocumentoAdmService.getPersonalEditDocAdmEmision(pcoDep));
        return "/modalGeneral/consultaElaboradoPorEdit";
    }  
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaFirmadoPorEdit")
    public String goBuscaFirmadoPorEdit(HttpServletRequest request, Model model){
        String pcoDep = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDep");
        model.addAttribute("listaEmpleado",emiDocumentoAdmService.getPersonalEditDocAdmEmision(pcoDep));
        return "/modalGeneral/consultaFirmadoPorEdit";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goChangeToProyecto",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goChangeToProyecto(DocumentoEmiBean documentoEmiBean, HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       documentoEmiBean.setCoUseMod(usuario.getCoUsuario());
       documentoEmiBean.setCoEmpRes(usuario.getCempCodemp());
       try{
          mensaje = emiDocumentoAdmService.changeToProyecto(documentoEmiBean,usuario);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goChangeToDespacho",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goChangeToDespacho(DocumentoEmiBean documentoEmiBean, HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       documentoEmiBean.setCoUseMod(usuario.getCoUsuario());
       if(documentoEmiBean.getNuSecuenciaFirma()!=null && !documentoEmiBean.getNuSecuenciaFirma().equals("")){
            documentoEmiBean.setNuSecuenciaFirma(applicationProperties.getRutaTemporal()+"//"+documentoEmiBean.getNuSecuenciaFirma());
       }else{
           documentoEmiBean.setNuSecuenciaFirma(null);
       }
       try{
          mensaje = emiDocumentoAdmService.changeToDespacho(documentoEmiBean,usuario);
          //ProbarFrankSilva
          if(mensaje.equals("OK")){
              String vReturn = notiService.procesarNotificacion(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), usuario.getCoUsuario());
              if (!vReturn.equals("OK")) {
                  throw new validarDatoException(vReturn);
              }
          }
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goChangeToEmitido",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goChangeToEmitido(DocumentoEmiBean documentoEmiBean, HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       DocumentoVerBean docSession =Utilidades.getInstancia().loadDocFromSession(request);
       
       documentoEmiBean.setCoUseMod(usuario.getCoUsuario());
       documentoEmiBean.setNuCorDoc("1");
       documentoEmiBean.setNuDocEmi(docSession.getNumeroDoc());
       documentoEmiBean.setNuSecuenciaFirma(applicationProperties.getRutaTemporal()+"//"+documentoEmiBean.getNuSecuenciaFirma());
       try{
          mensaje = emiDocumentoAdmService.changeToEmitido(documentoEmiBean,docSession.getNoDocumento(),usuario,applicationProperties.getNroRucInstitu());

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
            
            //add YUAL
            // model.addAttribute("pnuAnn", documentoEmiBean.getNuAnn());
          //   model.addAttribute("pnuEmi", documentoEmiBean.getNuDocEmi());
          //   model.addAttribute("MensajeriaList",commonQryService.getLsDepenciaMensjeria());
         
          //   return "/modalGeneral/consultaEnvioNotificacion";
            
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

    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goChangeToEmitidoAlta",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goChangeToEmitidoAlta(DocumentoEmiBean documentoEmiBean, HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       
       documentoEmiBean.setCoUseMod(usuario.getCoUsuario());
       documentoEmiBean.setNuCorDoc("1");
       try{
          mensaje = emiDocumentoAdmService.changeToEmitidoAlta(documentoEmiBean);

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
    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goEliminarDocEmiAdm")
    public @ResponseBody String goEliminarDocEmiAdm(DocumentoEmiBean documentoEmiBean, HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       documentoEmiBean.setCoUseMod(usuario.getCoUsuario());
       try{
          mensaje = emiDocumentoAdmService.delDocumentoEmiAdm(documentoEmiBean,usuario);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goObtenerExpedienteBean",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goObtenerExpedienteBean(HttpServletRequest request, Model model){
        String vRespuesta;
        String coRespuesta;
        StringBuilder retval = new StringBuilder();
        String pnuAnnExp = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnnExp");
        String pnuSecExp = ServletUtility.getInstancia().loadRequestParameter(request, "pnuSecExp");
        ExpedienteBean expedienteBean;
        try{
           expedienteBean = emiDocumentoAdmService.getExpDocumentoEmitido(pnuAnnExp, pnuSecExp);
           if (expedienteBean != null) {
                coRespuesta = "1";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"nuSecExp\":\"");
                retval.append(expedienteBean.getNuSecExp());
                retval.append("\",\"nuExpediente\":\"");
                retval.append(expedienteBean.getNuExpediente());
                retval.append("\",\"nuAnnExp\":\"");
                retval.append(expedienteBean.getNuAnnExp());
                retval.append("\",\"feExpCorta\":\"");
                retval.append(expedienteBean.getFeExpCorta());
                retval.append("\",\"feExp\":\"");
                retval.append(expedienteBean.getFeExp());
                retval.append("\",\"coProceso\":\"");
                retval.append(expedienteBean.getCoProceso());          
                retval.append("\",\"deProceso\":\"");
                retval.append(expedienteBean.getDeProceso());                
                retval.append("\"}");            
           }else{
                coRespuesta = "0";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"deRespuesta\":\"");
                retval.append("No se puede obtener el expediente");                
                retval.append("\"}");
           }
       }catch(Exception e){
            vRespuesta = e.getMessage();
            coRespuesta = "0";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append(vRespuesta);                            
            retval.append("\"}");
       }
       return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDependenciaEmite")
    public String goBuscaDependenciaEmite(HttpServletRequest request, Model model){
        String pdeDepEmite = ServletUtility.getInstancia().loadRequestParameter(request, "pdeDepEmite");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String vResult="";
        String coRespuesta;
        StringBuilder retval = new StringBuilder();
        HashMap map;
        
        try{
            map = emiDocumentoAdmService.getBuscaDependenciaEmite(usuarioConfigBean.getCoDep(),pdeDepEmite);
            String vReturn = (String)map.get("vReturn");
            if(vReturn.equals("1")){
                model.addAttribute("listaDependenciaDestEmi",map.get("listaDestinatario"));
                vResult = "/modalGeneral/consultaDependenciaEmi";
            }else if(vReturn.equals("0")){
                DependenciaBean dependenciaBean = (DependenciaBean)map.get("objDestinatario");
                coRespuesta = "1";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"coDependencia\":\"");
                retval.append(dependenciaBean.getCoDependencia());                
                retval.append("\",\"deDependencia\":\"");
                retval.append(dependenciaBean.getDeDependencia());                
                retval.append("\"}"); 
                model.addAttribute("pMensaje", retval.toString());
                vResult = "respuesta";
            }else{
                coRespuesta = "0";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"deRespuesta\":\"");
                retval.append("Error: No se puede obtener Dependencia.");                
                retval.append("\"}"); 
                model.addAttribute("pMensaje", retval.toString());
                vResult = "respuesta";
            }            
        }catch(Exception ex){
            ex.printStackTrace();
        }        
        return vResult;
    } 
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDocumentoEnReferencia")
    public String goBuscaDocumentoEnReferencia(BuscarDocumentoEmiBean buscarDocumentoEmiBean,HttpServletRequest request, Model model){
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        buscarDocumentoEmiBean.setsCoDependencia(usuario.getCoDep());
        buscarDocumentoEmiBean.setsCoEmpleado(usuario.getCempCodemp());
        buscarDocumentoEmiBean.setsTiAcceso(usuarioConfigBean.getTiAcceso());
        
        String mensaje ="NO_OK";
        HashMap mp = null;
        List list = null;
        /* HPB 27/05/2020 - Inicio - Requerimiento Paginación emitidos */
        List<Integer> lsCantidadPg = new ArrayList<Integer>();
        List listCantidad = null;
        String vPagina = "1";
        int paginas = 0;
        int paginasx = 0;
        vPagina = ServletUtility.getInstancia().loadRequestParameter(request, "vPagina");
        /* HPB 27/05/2020 - Fin - Requerimiento Paginación emitidos */
        try{
            //mp = emiDocumentoAdmService.getDocumentosEnReferencia(buscarDocumentoEmiBean);            
            /* HPB 27/05/2020 - Inicio - Requerimiento Paginación emitidos */
            mp = emiDocumentoAdmService.getDocumentosEnReferenciaSize(buscarDocumentoEmiBean);
            String cantPorPagina = commonQryService.obtenerValorParametro("CANT_POR_PAGINA");
            listCantidad = (List) mp.get("emiDocumAdmList");
            int cantidad = Integer.parseInt(cantPorPagina);
            paginas = listCantidad.size() / cantidad;
            paginasx = listCantidad.size() % cantidad;            
            if(paginasx > 0)
                paginas = paginas + 1;  
            mp = emiDocumentoAdmService.getDocumentosEnReferencia(buscarDocumentoEmiBean, vPagina, cantidad);        
            //mp = emiDocumentoAdmService.getDocumentosEnReferencia(buscarDocumentoRecepBean, vPagina, cantidad);          
            for (int i=1; i<=paginas; i++){
                lsCantidadPg.add(i);
            }
            /* HPB 27/05/2020 - Fin - Requerimiento Paginación emitidos */              
        }catch(Exception ex){
            mensaje = ex.getMessage();
        } 
        mensaje = String.valueOf(mp.get("msjResult"));
        if(mensaje.equals("1")){
            list = (List) mp.get("emiDocumAdmList");
            if(list!=null){
                if (list.size()>=100){
                    //model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
                }
                /* HPB 27/05/2020 - Inicio - Requerimiento Paginación emitidos */
                model.addAttribute("vPagina", vPagina);
                model.addAttribute("vTotalPaginas", paginas);
                model.addAttribute("lsCantidadPg", lsCantidadPg);
                model.addAttribute("cantRegistros", listCantidad.size());
                /* HPB 27/05/2020 - Fin - Requerimiento Paginación emitidos */                 
                model.addAttribute("emiDocumAdmList",list);
                mensaje = "OK";                
            }
        }
        if (mensaje.equals("OK")) {
            return "/DocumentoAdmEmi/tblDocAdmEmision";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }      
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goCambiaDepEmi")
    private @ResponseBody String goCambiaDepEmi(HttpServletRequest request, Model model) throws Exception {
        StringBuilder retval = new StringBuilder();
        String pcoDepEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDepEmi");
        DependenciaBean depEmi = emiDocumentoAdmService.cambiaDepEmi(pcoDepEmi);
        
        if (depEmi!=null){
                retval.append("{\"retval\":\"");
                retval.append("OK");
                retval.append("\",\"coEmpEmi\":\"");
                retval.append(depEmi.getCoEmpleado());
                retval.append("\",\"deEmpEmi\":\"");
                retval.append(depEmi.getDeDependencia());
                retval.append("\",\"deSigla\":\"");
                retval.append(depEmi.getDeSigla());
                retval.append("\"}");                        
        }else{
                retval.append("{\"retval\":");
                retval.append("false");
                retval.append("}");
        }
        
        return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaEmpVoBoDocAdm")
    public String goBuscaEmpVoBoDocAdm(HttpServletRequest request, Model model){
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String pnomEmp = ServletUtility.getInstancia().loadRequestParameter(request, "pnomEmp");
        String pcoDep = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDep");
        model.addAttribute("iniFuncionParm","5");
        model.addAttribute("listaEmpleado",commonQryService.getLsEmpDepDesEmp(pnomEmp,pcoDep));
        return "/modalGeneral/consultaElaboradoPorConsul";
    } 
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDepVoBoDocAdm")
    public String goBuscaDepVoBoDocAdm(HttpServletRequest request, Model model){
        String pdeDepen = ServletUtility.getInstancia().loadRequestParameter(request, "pdeDepen");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        model.addAttribute("iniFuncionParm","9");
        model.addAttribute("listaDestinatario",referencedData.getListDependenciaDestinatarioEmi(usuario.getCoDep(),pdeDepen));        
        return "/modalGeneral/consultaDestinatarioEmiConsul";
    }     
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaEmptblPersonalVoBo",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goBuscaEmptblPersonalVoBo(HttpServletRequest request, Model model) throws Exception {
       String vRespuesta;
       String pcoDepen = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDepen");
       String coRespuesta;
       StringBuilder retval = new StringBuilder();
       EmpleadoBean empleadoBean;
       try{
           empleadoBean = commonQryService.getEmpJefeDep(pcoDepen);
           if (empleadoBean != null && empleadoBean.getCempCodemp() != null) {
                coRespuesta = "1";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"coEmpleado\":\"");
                retval.append(empleadoBean.getCempCodemp());
                retval.append("\",\"deEmpleado\":\"");
                retval.append(empleadoBean.getNombre());
                retval.append("\"}");            
           }else{
                coRespuesta = "0";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"deRespuesta\":\"");
                retval.append("NO SE ENCUENTRA ENCARGADO DEPENDENCIA");                
                retval.append("\"}");
           }
       }catch(Exception e){
            vRespuesta = e.getMessage();
            coRespuesta = "0";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append(vRespuesta);                            
            retval.append("\"}");
       }
       return retval.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goVerificarVistoBuenoPendiente")
    public @ResponseBody String goVerificarVistoBuenoPendiente(HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
       
       try{
          mensaje = documentoVoBoService.existeVistoBuenoPendienteDocAdm(pnuAnn, pnuEmi);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdTlbVoBo")
    public String goUpdTlbVoBo(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String snuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String snuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");        
        //String scoDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDependencia");
        List<EmpleadoVoBoBean> lsEmpVobo;
                
        try{
           lsEmpVobo=commonQryService.getLsPersonalVoBo(snuAnn,snuEmi);
           model.addAttribute("lstEmpVoBoDocAdm",lsEmpVobo);
           mensaje = "OK";
       }catch(Exception ex){
          mensaje = ex.getMessage(); 
       }
       if (mensaje.equals("OK")) {
           return "/DocumentoAdmEmi/tablaPersVoBoDocAdm";
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }         
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarPersVoboGrupo",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goAgregarPersVoboGrupo(HttpServletRequest request, Model model) throws Exception {
       String scogrupo = ServletUtility.getInstancia().loadRequestParameter(request, "pcogrupo");
       String retval;

       retval = emiDocumentoAdmService.getLstPersVoBoGrupo(scogrupo);
           
       return retval.toString();
    } 
    
    /****
     * la idea es que me baje el documento a una carpeta del servidor
       luego estampe el documento con la glosa
       por ultimo suba el nuevo documento a la bd  
     * @param request
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goEstampaGlosaReporte")
    public @ResponseBody String goEstampaGlosaReporte(HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
       String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
       String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
       String ptipoDocumento = ServletUtility.getInstancia().loadRequestParameter(request, "ptipoDocumento");/*--[HPB] LOG PROVEIDO 09/07/20--*/
       String pnuCorEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuCorEmi");/*--[HPB] LOG PROVEIDO 09/07/20--*/
       String pcoDepEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDepEmi");/*--[HPB] LOG PROVEIDO 09/07/20--*/
       DocumentoObjBean docObjBean = null;
       String nombreArchivo = null;
       try{
            /*--INICIO [HPB] LOG PROVEIDO 09/07/20--*/
            String inLogDoc = commonQryService.obtenerValorParametro("IN_LOG_DOC");
            if(inLogDoc.equals("1") && ptipoDocumento.equals("232")){
                mensaje = emiDocumentoAdmService.insLogDocumentoEmiAdm(pnuAnn,pnuEmi,pnuCorEmi,pcoDepEmi,usuario);
            }
            /*--FIN [HPB] LOG PROVEIDO 09/07/20--*/
            //validamos que sea documento para glosa
            DatosPlantillaDoc datosPlantilla = documentoXmlService.datosParaPlantillaGlosa(pnuAnn,pnuEmi);
        
            //las plantillas de los formatos CARTA / OFICIO deben cambiar para evitar esta validacion
            if(datosPlantilla.isConPieDePagina() && datosPlantilla.getClave()!=null && datosPlantilla.getClave().length()>0){
                //bajada
                docObjBean = documentoObjService.leerDocumento(pnuAnn,pnuEmi,"03");

                if(docObjBean!=null){
                     //String glosa = Constantes.GLOSA;
                     String glosa = Constantes.GLOSAQR;
                     //String qrparam  = datosPlantilla.getCoTipoDoc()+"*"+datosPlantilla.getNuDocEmi()+"*"+datosPlantilla.getNuAnn()+"*"+datosPlantilla.getDeDocSig()+"*"+datosPlantilla.getClave()+"*"+datosPlantilla.getNuEmi();
                     String qrparam = datosPlantilla.getNuEmi()+"-"+datosPlantilla.getNuAnn()+"-"+datosPlantilla.getClave();
                     //coTipoDoc
                     //descarga y estampado
                     if(datosPlantilla.getPlantillaAntigua().equals("1")){
                         glosa = Constantes.GLOSA_ANTIGUA;
                     }
                      
                     glosa = glosa.replace("{LINK}", datosPlantilla.getLink());
                     glosa = glosa.replace("{CLAVE}", datosPlantilla.getClave());
                     docObjBean.setGlosaDocumento(glosa);
                     docObjBean.setPlantillaAntigua(datosPlantilla.getPlantillaAntigua());
                        docObjBean.setRutaqr(qrparam); //set qr var
                     nombreArchivo = documentoObjService.estampaGlosaEnDocumento(docObjBean);

                     if(nombreArchivo!=null){
                         //subida
                         DocumentoObjBean docObjBeanGlosa = new DocumentoObjBean();
                         docObjBeanGlosa.setNuAnn(docObjBean.getNuAnn());
                         docObjBeanGlosa.setNuEmi(docObjBean.getNuEmi());
                         docObjBeanGlosa.setNombreArchivo(docObjBean.getNombreArchivo());
                         docObjBeanGlosa.setNumeroSecuencia(applicationProperties.getRutaTemporal()+"//"+nombreArchivo);
                         docObjBeanGlosa.setCoUseMod(usuario.getCoUsuario());
                         
                         mensaje = documentoObjService.cargaDocumentoGlosa(docObjBeanGlosa);
                     }else{
                         mensaje = "NO_OK";
                     }                 

                }else{
                    mensaje = "NO_OK";
                }
            }else{
                mensaje = "OK";
            }
          
       }catch(Exception e){
           e.printStackTrace();            
           //mensaje = e.getMessage();
           mensaje = "NO_OK";
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestCiudadanoPrevio")
    public String goBuscaDestCiudadanoPrevio(HttpServletRequest request, Model model){ 
        CitizenBean citizenBean = new CitizenBean();        
        model.addAttribute("citizenBean",citizenBean); 
        return "/modalGeneral/consultaDestCiudadanoBusqPrevio";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestCiudadanoNuevo")
    public String goBuscaDestCiudadanoNuevo(HttpServletRequest request, Model model){ 
                        
        String sApePatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApePatCiudadano");
        String sApeMatCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sApeMatCiudadano");
        String sNombreCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "sNombreCiudadano");
            
        model.addAttribute("lsDestCiudadano",emiDocumentoAdmService.getLstCiudadanos(sApePatCiudadano,sApeMatCiudadano,sNombreCiudadano));
        model.addAttribute("iniFuncionParm","1");
        
        return "/modalGeneral/consultaDestCiudadano";
    }
    
    //jazanero
    //codigo para abrir popup de detalle proyecto    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goDetalleProyectoPrevio")
    public String goDetalleProyectoPrevio(HttpServletRequest request, Model model) {

        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);        
        String pnuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String pnuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");
         System.out.println("pnuEmi: "+pnuEmi);                      
        String codDependencia = usuario.getCoDep();        
        /*--Hermes 16/07/2018--*/
        //codDependencia = "10523"; //SUBSECRETARIA 
        String pcodependencia = ServletUtility.getInstancia().loadRequestParameter(request, "pcodependencia");
        String pcodempleado = ServletUtility.getInstancia().loadRequestParameter(request, "pcodempleado");
        DocumentoEmiBean documentoEmiBean;/*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
        codDependencia = pcodependencia;
        /*------------------------------*/
        String mensaje = "NO_OK";
        //necesito saber si es ins o update
        try{
            
            DocumentoProyectoBean documentoProyectoBean = emiDocumentoAdmService.getDocumentoEmiAdmProy(pnuAnn,pnuEmi);
            documentoEmiBean = emiDocumentoAdmService.getDocumentoEmiAdm(pnuAnn,pnuEmi);/*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
            if(documentoProyectoBean==null){
                System.out.println("accion=goDetalleProyectoPrevio 1");
                documentoProyectoBean = new DocumentoProyectoBean();
                //DocumentoProyectoBean documentoProyectoBean = new DocumentoProyectoBean();
                documentoProyectoBean.setNuAnn(pnuAnn);
                documentoProyectoBean.setNuEmi(pnuEmi);  
                documentoProyectoBean.setCoDepEmi(codDependencia); /*--Hermes 16/07/2018--*/  
                //documentoProyectoBean.setCoEmpEmi(pcodempleado); /*--Hermes 20/07/2018--*/ 
                
                //bean descargaMensaje
                List<TipoDestinatarioEmiBean> plstTipoDestinatarioEmi = new ArrayList<TipoDestinatarioEmiBean>();
                TipoDestinatarioEmiBean tipoDestinatarioEmiBean = new TipoDestinatarioEmiBean();
                tipoDestinatarioEmiBean.setCoDestinatario("03");tipoDestinatarioEmiBean.setDeDestinatario("CIUDADANO");
                plstTipoDestinatarioEmi.add(tipoDestinatarioEmiBean);
                TipoDestinatarioEmiBean tipoDestinatarioEmiBeano = new TipoDestinatarioEmiBean();
                tipoDestinatarioEmiBeano.setCoDestinatario("02");tipoDestinatarioEmiBeano.setDeDestinatario("PERSONA JURIDICA");
                plstTipoDestinatarioEmi.add(tipoDestinatarioEmiBeano);
                TipoDestinatarioEmiBean tipoDestinatarioEmiBeanz = new TipoDestinatarioEmiBean();
                tipoDestinatarioEmiBeanz.setCoDestinatario("04");tipoDestinatarioEmiBeanz.setDeDestinatario("OTROS");
                plstTipoDestinatarioEmi.add(tipoDestinatarioEmiBeanz);
                //model.addAttribute("plstTipoDestinatarioEmi",referencedData.getTipoDestinatarioEmiList());
                model.addAttribute("plstTipoDestinatarioEmi",plstTipoDestinatarioEmi);

                model.addAttribute("plstDependenciaRemitenteEmi",referencedData.getLstDependenciaRemitenteEmi(codDependencia)); 
                /*---------------------Hermes 16/07/2018------------------------*/
                model.addAttribute("plstDependenciaRemitenteEmi2",referencedData.getLstDependenciaRemitenteEmi2()); 
                model.addAttribute("sCodDependencia",codDependencia);

                List<DependenciaBean> lstNombre = referencedData.getLstDependenciaRemitenteEmi2(codDependencia);
                for (int i=0; i<lstNombre.size(); i++){ 
                    documentoProyectoBean.setCoEmpEmi(lstNombre.get(i).getCoEmpleado());
                    documentoProyectoBean.setDeEmpEmi(lstNombre.get(i).getDeEmpleado());
                    documentoProyectoBean.setDeDepEmi(lstNombre.get(i).getDeDependencia());
                }
                /*--------------------------------------------------------------*/
                
                List<TipoDocumentoBean> plstTipDocDependencia = new ArrayList<TipoDocumentoBean>();
                TipoDocumentoBean tipoDocumentoBean = new TipoDocumentoBean();
                tipoDocumentoBean.setCoTipDoc("246");tipoDocumentoBean.setDeTipDoc("CARTA");
                plstTipDocDependencia.add(tipoDocumentoBean);
                tipoDocumentoBean = new TipoDocumentoBean();
                tipoDocumentoBean.setCoTipDoc("243");tipoDocumentoBean.setDeTipDoc("OFICIO");
                plstTipDocDependencia.add(tipoDocumentoBean);
                /*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
                documentoProyectoBean.setNuSecExp(documentoEmiBean.getNuSecExp());
                documentoProyectoBean.setCoGru(documentoEmiBean.getCoGruExp());
                documentoProyectoBean.setNuOriEmi(documentoEmiBean.getNuOriEmi());
                documentoProyectoBean.setTiEmiExp(documentoEmiBean.getTiEmiExp());
                /*HPB 11/02/2020 - Fin - Requerimiento Remitente expediente*/

                
                //model.addAttribute("plstTipDocDependencia",referencedData.listTipDocXDependencia(codDependencia));
                model.addAttribute("plstTipDocDependencia",plstTipDocDependencia);
                model.addAttribute("psEsNuevoDocAdm","1");
                model.addAttribute("sTipoDestEmi","03");
                model.addAttribute(documentoProyectoBean);

                model.addAttribute("fechaActual", Utility.getInstancia().dateToFormatString(new Date()));
                model.addAttribute("annioActual", Utility.getInstancia().dateToFormatStringYYYY(new Date()));              
                model.addAttribute("horaActual", Utility.getInstancia().dateToFormatStringHHmm(new Date()));   

            }else{
    //            documentoProyectoBean.setNuAnn(pnuAnn);
    //            documentoProyectoBean.setNuEmi(pnuEmi);  
    //            documentoProyectoBean.setCoDepEmi(codDependencia);        
                System.out.println("accion=goDetalleProyectoPrevio 2");
                //bean descargaMensaje
                String stipoDestinatario = emiDocumentoAdmService.getTipoDestinatarioEmiProy(pnuAnn, pnuEmi);//obtener tipo de Destinatario
                if(stipoDestinatario != null){
                   HashMap map = emiDocumentoAdmService.getLstDestintariotlbEmiProy(pnuAnn,pnuEmi);
                   //model.addAttribute("lstDestintarioDocAdmEmi",map.get("lst1"));
                   model.addAttribute("lstDestintarioProveedorDocAdmEmi",map.get("lst2"));
                   model.addAttribute("lstDestintarioCiudadanoDocAdmEmi",map.get("lst3"));
                   model.addAttribute("lstDestintarioOtroOrigenDocAdmEmi",map.get("lst4"));
                }else{
                   stipoDestinatario = "03"; 
                }

                List<TipoDestinatarioEmiBean> plstTipoDestinatarioEmi = new ArrayList<TipoDestinatarioEmiBean>();
                TipoDestinatarioEmiBean tipoDestinatarioEmiBean = new TipoDestinatarioEmiBean();
                tipoDestinatarioEmiBean.setCoDestinatario("03");tipoDestinatarioEmiBean.setDeDestinatario("CIUDADANO");
                plstTipoDestinatarioEmi.add(tipoDestinatarioEmiBean);
                TipoDestinatarioEmiBean tipoDestinatarioEmiBeano = new TipoDestinatarioEmiBean();
                tipoDestinatarioEmiBeano.setCoDestinatario("02");tipoDestinatarioEmiBeano.setDeDestinatario("PERSONA JURIDICA");
                plstTipoDestinatarioEmi.add(tipoDestinatarioEmiBeano);
                TipoDestinatarioEmiBean tipoDestinatarioEmiBeanz = new TipoDestinatarioEmiBean();
                tipoDestinatarioEmiBeanz.setCoDestinatario("04");tipoDestinatarioEmiBeanz.setDeDestinatario("OTROS");
                plstTipoDestinatarioEmi.add(tipoDestinatarioEmiBeanz);
                
                List<TipoDocumentoBean> plstTipDocDependencia = new ArrayList<TipoDocumentoBean>();
                TipoDocumentoBean tipoDocumentoBean = new TipoDocumentoBean();
                tipoDocumentoBean.setCoTipDoc("246");tipoDocumentoBean.setDeTipDoc("CARTA");
                plstTipDocDependencia.add(tipoDocumentoBean);
                tipoDocumentoBean = new TipoDocumentoBean();
                tipoDocumentoBean.setCoTipDoc("243");tipoDocumentoBean.setDeTipDoc("OFICIO");
                plstTipDocDependencia.add(tipoDocumentoBean);

                /*---------------------Hermes 18/07/2018------------------------*/
                //documentoProyectoBean.setCoEmpEmi(pcodempleado); /*--Hermes 20/07/2018--*/ 
                List<DependenciaBean> lstNombre = referencedData.getLstDependenciaRemitenteEmi(documentoProyectoBean.getCoDepEmi());
                for (int i=0; i<lstNombre.size(); i++){                                       
                    documentoProyectoBean.setDeDepEmi(lstNombre.get(i).getDeDependencia());
                }
                /*--------------------------------------------------------------*/

                /*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
                documentoProyectoBean.setNuSecExp(documentoEmiBean.getNuSecExp());
                documentoProyectoBean.setCoGru(documentoEmiBean.getCoGruExp());
                documentoProyectoBean.setNuOriEmi(documentoEmiBean.getNuOriEmi());
                documentoProyectoBean.setTiEmiExp(documentoEmiBean.getTiEmiExp());
                /*HPB 11/02/2020 - Fin - Requerimiento Remitente expediente*/                
                model.addAttribute("plstTipDocDependencia",plstTipDocDependencia);
                model.addAttribute("plstTipoDestinatarioEmi",plstTipoDestinatarioEmi);
                model.addAttribute("plstDependenciaRemitenteEmi",referencedData.getLstDependenciaRemitenteEmi(codDependencia)); 
                
                model.addAttribute("sTipoDestEmi",stipoDestinatario);
                model.addAttribute("psEsNuevoDocAdm","0");

                model.addAttribute("pfechaHoraActual",Utility.getInstancia().dateToFormatString(new Date()));
                model.addAttribute("pcodEmp",usuario.getCempCodemp());
                model.addAttribute("pdesEmp",usuario.getDeFullName());           
                model.addAttribute(documentoProyectoBean);
                model.addAttribute("pusuario",usuario.getCoUsuario());

            }
            
            mensaje = "OK";
        }catch(Exception ex){
           mensaje = ex.getMessage(); 
        }
       if (mensaje.equals("OK")) {
           return "/DocumentoAdmEmi/detalleProyecto";
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }  
        
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaProyectoFirmadoPorEdit")
    public String goBuscaProyectoFirmadoPorEdit(HttpServletRequest request, Model model){
        String pcoDep = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDep");
        model.addAttribute("listaEmpleado",emiDocumentoAdmService.getPersonalEditDocAdmEmision(pcoDep));
        return "/modalGeneral/consultaProyectoFirmadoPorEdit";
    }
        
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestProveedorAgregaProy")
    public String goBuscaDestProveedorAgregaProy(HttpServletRequest request, Model model){
        String prazonSocial = ServletUtility.getInstancia().loadRequestParameter(request, "prazonSocial");
        model.addAttribute("lsDestProveedor",emiDocumentoAdmService.getLstProveedoresAgrega(prazonSocial));
        model.addAttribute("iniFuncionParm","3");
        return "/modalGeneral/consultaDestProveedor";
    }
        
    @RequestMapping(method = RequestMethod.POST, params = "accion=goGrabaDocumentoEmiProy",produces = "text/plain; charset=utf-8")
    public @ResponseBody String goGrabaDocumentoEmiProy(@RequestBody TrxDocumentoEmiBean trxDocumentoEmiBean,HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String coRespuesta;
        String deRespuesta;
        String sCrearExpediente = ServletUtility.getInstancia().loadRequestParameter(request,"pcrearExpediente");//0 no crea, 1 crea
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        trxDocumentoEmiBean.setCoUserMod(usuario.getCoUsuario());
        trxDocumentoEmiBean.setCempCodEmp(usuario.getCempCodemp());
        
        try{
            mensaje = emiDocumentoAdmService.grabaDocumentoEmiAdmProy(trxDocumentoEmiBean,sCrearExpediente,usuario);
            //ProbarFrankSilva
//            if (mensaje.equals("OK")) {
//                String vReturn = notiService.procesarNotificacion(trxDocumentoEmiBean.getNuAnn(), trxDocumentoEmiBean.getNuEmi(), usuario.getCoUsuario());
//                if (!vReturn.equals("OK")) {
//                    throw new validarDatoException(vReturn);
//                }
//            }
        }catch(validarDatoException e){
           mensaje=(e.valorMsg);
        }catch (Exception e) { 
           e.printStackTrace(); 
        } 
        
        if (mensaje.equals("OK")) {
            coRespuesta = "1";
        }else if(mensaje.equals("NO_OK")){
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
        retval.append("\",\"nuEmi\":\"");
        retval.append(trxDocumentoEmiBean.getNuEmi());  
        retval.append("\",\"nuCorEmi\":\"");
        retval.append(trxDocumentoEmiBean.getNuCorEmi());
        retval.append("\",\"nuDocEmi\":\"");
        retval.append(trxDocumentoEmiBean.getNuDocEmi());        
        retval.append("\",\"nuAnnExp\":\"");
        retval.append(trxDocumentoEmiBean.getNuAnnExp());
        retval.append("\",\"nuSecExp\":\"");
        retval.append(trxDocumentoEmiBean.getNuSecExp());
        retval.append("\",\"feExp\":\"");
        retval.append(trxDocumentoEmiBean.getFeExp());                 
        retval.append("\",\"nuExpediente\":\"");
        retval.append(trxDocumentoEmiBean.getNuExpediente());                                  
        retval.append("\",\"nuDetExp\":\"");
        retval.append("1");        
        retval.append("\"}");

        return retval.toString();        
    } 
    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdTlbsDestinatarioProy")
    public String goUpdTlbsDestinatarioProy(HttpServletRequest request, Model model){
        String mensaje = "NO_OK";
        String snuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        String snuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");        
       
       try{
            HashMap map = emiDocumentoAdmService.getLstDestintariotlbEmiProy(snuAnn,snuEmi);
            //model.addAttribute("lstDestintarioDocAdmEmi",map.get("lst1"));
            model.addAttribute("lstDestintarioProveedorDocAdmEmi",map.get("lst2"));
            model.addAttribute("lstDestintarioCiudadanoDocAdmEmi",map.get("lst3"));
            model.addAttribute("lstDestintarioOtroOrigenDocAdmEmi",map.get("lst4"));
            model.addAttribute("lstLocalRemitenteEmi",referencedData.getLstLocal());
            model.addAttribute("lstPrioridadDestEmi",referencedData.getLstPrioridadDestEmi());
           mensaje = "OK";
       }catch(Exception ex){
          mensaje = ex.getMessage(); 
       }
       if (mensaje.equals("OK")) {
           return "/DocumentoAdmEmi/actualizaTablasDestinoEmiDocProy";
       } else {
           model.addAttribute("pMensaje", mensaje);
           return "respuesta";
       }       
    } 
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestOtroOrigenAgregaProy")
    public String goBuscaDestOtroOrigenAgregaProy(HttpServletRequest request, Model model){
        String pnomOtroOrigen = ServletUtility.getInstancia().loadRequestParameter(request, "pnoOtroOrigen");
        model.addAttribute("lsDestOtroOrigen",emiDocumentoAdmService.getLstOtrosOrigenesAgrega(pnomOtroOrigen));
        model.addAttribute("iniFuncionParm","4");
        return "/modalGeneral/consultaDestOtroOrigen";
    }
    
    //jazanero
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestCiudadanoPrevioProy")
    public String goBuscaDestCiudadanoPrevioProy(HttpServletRequest request, Model model){ 
        CitizenBean citizenBean = new CitizenBean();        
        model.addAttribute("citizenBean",citizenBean); 
        return "/modalGeneral/consultaDestCiudadanoBusqPrevioProy";
    }
    /*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaProveedor")
    public @ResponseBody String goBuscaProveedor(HttpServletRequest request, Model model){
        ProveedorBean proveedorBean;
        String coRespuesta="0";
        String pnuRuc = ServletUtility.getInstancia().loadRequestParameter(request, "pnuRuc");
        StringBuilder retval = new StringBuilder();       
        try {
            proveedorBean = emiDocumentoAdmService.getProveedor(pnuRuc);
            retval.append("{\"coRespuesta\":\"");
            if(proveedorBean!=null){
                coRespuesta="1";
                retval.append(coRespuesta);
                retval.append("\",");                
                retval.append("\"proveedorBean\":{");
                retval.append("\"nuRuc\":\"");
                retval.append(proveedorBean.getNuRuc());                     
                retval.append("\",\"deRuc\":\"");
                retval.append(proveedorBean.getDescripcion());
                retval.append("\",\"idDepartamento\":\"");
                retval.append(proveedorBean.getIdDepartamento()==null?"":proveedorBean.getIdDepartamento()); 
                retval.append("\",\"idProvincia\":\"");
                retval.append(proveedorBean.getIdProvincia()==null?"":proveedorBean.getIdProvincia()); 
                retval.append("\",\"idDistrito\":\"");
                retval.append(proveedorBean.getIdDistrito()==null?"":proveedorBean.getIdDistrito()); 
                retval.append("\",\"cubiCoddep\":\"");
                retval.append(proveedorBean.getCubiCoddep()==null?"":proveedorBean.getCubiCoddep()); 
                retval.append("\",\"cubiCodpro\":\"");
                retval.append(proveedorBean.getCubiCodpro()==null?"":proveedorBean.getCubiCodpro()); 
                retval.append("\",\"cubiCoddis\":\"");
                retval.append(proveedorBean.getCubiCoddis()==null?"":proveedorBean.getCubiCoddis());                 
                retval.append("\",\"noDep\":\"");
                retval.append(proveedorBean.getNoDep()==null?"":proveedorBean.getNoDep()); 
                retval.append("\",\"noPrv\":\"");
                retval.append(proveedorBean.getNoPrv()==null?"":proveedorBean.getNoPrv()); 
                retval.append("\",\"noDis\":\"");
                retval.append(proveedorBean.getNoDis()==null?"":proveedorBean.getNoDis());                 
                retval.append("\",\"deDireccion\":\"");
                retval.append(proveedorBean.getDeDireccion()==null?"":proveedorBean.getDeDireccion()); 
                retval.append("\",\"deCorreo\":\"");
                retval.append(proveedorBean.getDeCorreo()==null?"":proveedorBean.getDeCorreo()); 
                retval.append("\",\"telefono\":\"");
                retval.append(proveedorBean.getTelefono()==null?"":proveedorBean.getTelefono()); 
                retval.append("\"}");                  
            }else{
                retval.append(coRespuesta);
                retval.append("\"");                
            }
            retval.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("retval: "+retval.toString());
        return retval.toString();
    } 
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDestOtroOrigenAgregaExpediente")
    public @ResponseBody String goBuscaDestOtroOrigenAgregaExpediente(HttpServletRequest request, Model model){
        DestinatarioOtroOrigenBean destinatarioOtroOrigenBean;
        String coRespuesta="0";
        String pnuCodigo = ServletUtility.getInstancia().loadRequestParameter(request, "pnuCodigo");
        StringBuilder retval = new StringBuilder();       
        try {
            destinatarioOtroOrigenBean = emiDocumentoAdmService.getDestinatarioOtroOrigen(pnuCodigo);
            retval.append("{\"coRespuesta\":\"");
            if(destinatarioOtroOrigenBean!=null){
                coRespuesta="1";
                retval.append(coRespuesta);
                retval.append("\",");                
                retval.append("\"destinatarioOtroOrigenBean\":{");
                retval.append("\"descripcion\":\"");
                retval.append(destinatarioOtroOrigenBean.getDescripcion());                     
                retval.append("\",\"tipoDocIdentidad\":\"");
                retval.append(destinatarioOtroOrigenBean.getTipoDocIdentidad()==null?"":destinatarioOtroOrigenBean.getTipoDocIdentidad()); 
                retval.append("\",\"nroDocIdentidad\":\"");
                retval.append(destinatarioOtroOrigenBean.getNroDocIdentidad()==null?"":destinatarioOtroOrigenBean.getNroDocIdentidad()); 
                retval.append("\",\"coOtroOrigen\":\"");
                retval.append(destinatarioOtroOrigenBean.getCoOtroOrigen()==null?"":destinatarioOtroOrigenBean.getCoOtroOrigen()); 
                retval.append("\",\"idDepartamento\":\"");
                retval.append(destinatarioOtroOrigenBean.getIdDepartamento()==null?"":destinatarioOtroOrigenBean.getIdDepartamento()); 
                retval.append("\",\"idProvincia\":\"");
                retval.append(destinatarioOtroOrigenBean.getIdProvincia()==null?"":destinatarioOtroOrigenBean.getIdProvincia()); 
                retval.append("\",\"idDistrito\":\"");
                retval.append(destinatarioOtroOrigenBean.getIdDistrito()==null?"":destinatarioOtroOrigenBean.getIdDistrito()); 
                retval.append("\",\"deDireccion\":\"");
                retval.append(destinatarioOtroOrigenBean.getDeDireccion()==null?"":destinatarioOtroOrigenBean.getDeDireccion()); 
                retval.append("\",\"deCorreo\":\"");
                retval.append(destinatarioOtroOrigenBean.getDeCorreo()==null?"":destinatarioOtroOrigenBean.getDeCorreo()); 
                retval.append("\",\"telefono\":\"");
                retval.append(destinatarioOtroOrigenBean.getTelefono()==null?"":destinatarioOtroOrigenBean.getTelefono());  
                retval.append("\",\"ubDep\":\"");
                retval.append(destinatarioOtroOrigenBean.getUbDep()==null?"":destinatarioOtroOrigenBean.getUbDep()); 
                retval.append("\",\"ubPro\":\"");
                retval.append(destinatarioOtroOrigenBean.getUbPro()==null?"":destinatarioOtroOrigenBean.getUbPro()); 
                retval.append("\",\"ubDis\":\"");
                retval.append(destinatarioOtroOrigenBean.getUbDis()==null?"":destinatarioOtroOrigenBean.getUbDis());                  
                retval.append("\",\"ubigeo\":\"");
                retval.append(destinatarioOtroOrigenBean.getUbigeo()==null?"":destinatarioOtroOrigenBean.getUbigeo()); 
                retval.append("\",\"deEmail\":\"");
                retval.append(destinatarioOtroOrigenBean.getDeEmail()==null?"":destinatarioOtroOrigenBean.getDeEmail()); 
                retval.append("\",\"deTelefo\":\"");
                retval.append(destinatarioOtroOrigenBean.getDeTelefo()==null?"":destinatarioOtroOrigenBean.getDeTelefo()); 
                retval.append("\",\"deDirOtroOri\":\"");
                retval.append(destinatarioOtroOrigenBean.getDeDirOtroOri()==null?"":destinatarioOtroOrigenBean.getDeDirOtroOri());                 
                retval.append("\",\"noDep\":\"");
                retval.append(destinatarioOtroOrigenBean.getNoDep()==null?"":destinatarioOtroOrigenBean.getNoDep()); 
                retval.append("\",\"noPrv\":\"");
                retval.append(destinatarioOtroOrigenBean.getNoPrv()==null?"":destinatarioOtroOrigenBean.getNoPrv()); 
                retval.append("\",\"noDis\":\"");
                retval.append(destinatarioOtroOrigenBean.getNoDis()==null?"":destinatarioOtroOrigenBean.getNoDis());                 
                retval.append("\"}");                  
            }else{
                retval.append(coRespuesta);
                retval.append("\"");                
            }
            retval.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("retval: "+retval.toString());
        return retval.toString();
    }     
    /*HPB 11/02/2020 - Fin - Requerimiento Remitente expediente*/
    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goVerificaTipoTramitePorDocumento")
    public @ResponseBody String goVerificaTipoTramitePorDocumento(HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       String pCoDepen = ServletUtility.getInstancia().loadRequestParameter(request, "pcodepen");
       String pCoTipoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "pcotipodoc");

       try{
          mensaje = documentoBasicoService.getVerificaTipoTramitePorDocumento(pCoDepen, pCoTipoDoc);
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
    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
}
