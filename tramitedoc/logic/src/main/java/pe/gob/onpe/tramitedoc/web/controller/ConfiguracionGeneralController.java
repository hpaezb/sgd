/**
 *
 */
package pe.gob.onpe.tramitedoc.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.autentica.service.UsuarioService;
import pe.gob.onpe.libreria.exception.validarDatoException;
import pe.gob.onpe.libreria.json.JSONArray;
import pe.gob.onpe.libreria.json.JSONObject;
import pe.gob.onpe.libreria.util.ServletUtility;
import pe.gob.onpe.tramitedoc.bean.CitizenBean;
import pe.gob.onpe.tramitedoc.bean.TupaBean;
import pe.gob.onpe.tramitedoc.bean.AdmEmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.AdmEmpleadoGlobalBean;
import pe.gob.onpe.tramitedoc.bean.AdmEmpleadoAccesoBean;
import pe.gob.onpe.tramitedoc.bean.BuscarDocDependencia;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoDependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoFileBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoDependenciaBean;
import pe.gob.onpe.tramitedoc.bean.GrupoDestinoBean;
import pe.gob.onpe.tramitedoc.bean.GrupoDestinoDetalleBean;
import pe.gob.onpe.tramitedoc.bean.LocalDepBean;
import pe.gob.onpe.tramitedoc.bean.MotivoBean;
import pe.gob.onpe.tramitedoc.bean.MotivoDocumentoDependenciaBean;
import pe.gob.onpe.tramitedoc.bean.OtroOrigenBean;
import pe.gob.onpe.tramitedoc.bean.ProveedorBean;
import pe.gob.onpe.tramitedoc.bean.RequisitoBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;
import pe.gob.onpe.tramitedoc.bean.TrxDependenciaBean;
import pe.gob.onpe.tramitedoc.bean.UbigeoBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.CiudadanoService;
import pe.gob.onpe.tramitedoc.service.TupaService;
import pe.gob.onpe.tramitedoc.service.AdmEmpleadoService;
import pe.gob.onpe.tramitedoc.service.CommonQryService;
import pe.gob.onpe.tramitedoc.service.DependenciaService;
import pe.gob.onpe.tramitedoc.service.DocDependenciaService;
import pe.gob.onpe.tramitedoc.service.DocumentoExtRecepService;
import pe.gob.onpe.tramitedoc.service.GrupoDestinoService;
import pe.gob.onpe.tramitedoc.service.ImagenPortadaService;
import pe.gob.onpe.tramitedoc.service.OtroOrigenService;
import pe.gob.onpe.tramitedoc.service.ProveedorService;
import pe.gob.onpe.tramitedoc.service.ReferencedData;
import pe.gob.onpe.tramitedoc.service.RequisitoService;
import pe.gob.onpe.tramitedoc.service.UsuarioConfigService;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;

/**
 * @author ecueva
 *
 */
@Controller
@RequestMapping("/{version}/srTablaConfiguracion.do")
public class ConfiguracionGeneralController {

    @Autowired
    private DependenciaService dependenciaService;
    @Autowired
    private DocDependenciaService docDependenciaService;
    @Autowired
    private GrupoDestinoService grupoDestinoService;
    @Autowired
    private UsuarioConfigService usuarioConfigService;
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private OtroOrigenService otroOrigenService;
    @Autowired
    private ReferencedData referencedData;   
    @Autowired
    private CommonQryService commonQryService;         
    @Autowired
    private AdmEmpleadoService admEmpleadoService;

    //Services anadido por rberrocal
    @Autowired
    private TupaService tupaService;
    
    @Autowired
    private CiudadanoService ciudadanoService; 
    @Autowired
    private ApplicationProperties applicationProperties;    
    @Autowired
    private ImagenPortadaService imagenPortadaService;
    
    @Autowired
    private RequisitoService requisitoService;
    
    @Autowired
    private UsuarioService usuarioService;    
    
    @Autowired
    private DocumentoExtRecepService documentoExtRecepService;
    /*    @Autowired
     private ReferencedData referencedData;*/
//    @RequestMapping(method = RequestMethod.GET, params = "accion=goDocDependencia")
//    public String goDocDependencia(HttpServletRequest request, Model model) {
//        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
//        String sCodDepencia = usuarioConfigBean.getCoDep();
//        if (true/*usuarioConfigBean.getInEsAdmin().equals("1")*/) {
//            model.addAttribute("dependenciaList", dependenciaService.getAllDependencia(true, sCodDepencia));
//        } else {
//            model.addAttribute("dependenciaList", dependenciaService.getAllDependencia(false, sCodDepencia));
//        }
//        BuscarDocDependencia buscarDocDependencia = new BuscarDocDependencia(sCodDepencia, "0");
//        model.addAttribute(buscarDocDependencia);
//        return "/configGeneral/documentoDependencia";
//    }

    @RequestMapping(method = RequestMethod.GET, params = "accion=goDocsDependencia")
    public String goDocsDependencia(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String sCodDepencia = usuarioConfigBean.getCoDep();
        if ("1".equals(usuarioConfigBean.getInEsAdmin())) {
            model.addAttribute("dependenciaList", dependenciaService.getAllDependencia(true, sCodDepencia));
        } else {
            model.addAttribute("dependenciaList", dependenciaService.getAllDependencia(false, sCodDepencia));
        }
        BuscarDocDependencia buscarDocDependencia = new BuscarDocDependencia(sCodDepencia, "0");
        model.addAttribute(buscarDocDependencia);
        return "/configGeneral/docsDependencia";
    }

//    @RequestMapping(method = RequestMethod.POST, params = "accion=goDocuDependencia")
//    public String goDocuDependencia(HttpServletRequest request, Model model) {
//        String mensaje = "NO_OK";
//        List list = null;
//        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDependencia");
//        String nroPaginaActual = ServletUtility.getInstancia().loadRequestParameter(request, "txtNumeroPaginaActual");
//        Paginacion oPaginacion = new Paginacion();
//        if (!nroPaginaActual.equals("")) {
//            String operacionPaginacion = ServletUtility.getInstancia().loadRequestParameter(request, "hdnOperacionPaginacion");
//            String nroTotalPaginas = ServletUtility.getInstancia().loadRequestParameter(request, "hdnNumeroTotalDePaginas");
//            oPaginacion.actualizarPaginaActual(nroPaginaActual, operacionPaginacion, nroTotalPaginas);
//        }
//        model.addAttribute("paginacion", oPaginacion);
//        try {
//            list = docDependenciaService.getAllDocXDependencia(sCodDependencia, oPaginacion);
//        } catch (Exception ex) {
//            mensaje = ex.getMessage();
//        }
//
//        if (list != null) {
//            if (list.size() <= 50) {
//                model.addAttribute("docDependenciaList", list);
//                mensaje = "OK";
//            } else {
//                mensaje = "MUCHOS_REGISTROS";
//            }
//        } else {
//            mensaje = "No se encuentran registros.";
//        }
//        if (mensaje.equals("OK")) {
//            return "/configGeneral/tablaDocDependencia";
//        } else {
//            model.addAttribute("pMensaje", mensaje);
//            return "respuesta";
//        }
//    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdDocuDependencia")
    public String goUpdDocuDependencia(DocumentoDependenciaBean documentoDependenciaBean, HttpServletRequest request, BindingResult result, Model model) {
        String mensaje = "NO_OK";
        String sAccion = ServletUtility.getInstancia().loadRequestParameter(request, "vAccion");
        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDependencia");
        List list = null;
        if (sAccion.equals("1")) {
            String sCodTipoDoc = ServletUtility.getInstancia().loadRequestParameter(request, "vCodTipoDoc");
            try {
                DocumentoDependenciaBean documentoDependenciaBeanAux = docDependenciaService.getDocDependencia(sCodDependencia, sCodTipoDoc);
                list = docDependenciaService.getAllDocXDependencia(sCodDependencia);
                model.addAttribute("lstDocDependUpdDet", list);
                model.addAttribute("documentoDependenciaBean", documentoDependenciaBeanAux);
                mensaje = "OK";
            } catch (Exception ex) {
                mensaje = ex.getMessage();
            }
        } else if (sAccion.equals("2")) {//Grabar
            try {
                //mensaje = docDependenciaService.updDocXDependencia(documentoDependenciaBean);
            } catch (Exception ex) {
                mensaje = ex.getMessage();
            }
        }

        if (mensaje.equals("OK")) {
            if (sAccion.equals("1")) {
                return "/configGeneral/documentoDependenciaUpd";
            } else if (sAccion.equals("2")) {
                return "respuesta";
            } else {
                return "respuesta";
            }
        } else {
            return "respuesta";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaDocsDependencia")
    public String goListaDocsDependencia(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDependencia");
        try {
            List<DocumentoDependenciaBean> docDepList = docDependenciaService.getDocDependenciaList(sCodDependencia);
            model.addAttribute("docDependenciaList", docDepList);
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/docsDependenciaListado";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goCargarDocumentoDep")
    public String goCargarDocumentoDep(HttpServletRequest request, Model model) {
        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDependencia");
        List<DocumentoDependenciaBean> docDepFaltantesList = docDependenciaService.getDocDependenciaFaltantesList(sCodDependencia);
        model.addAttribute("docDepFaltantesList", docDepFaltantesList);
        return "/modalGeneral/consultaDocumentoDependencia";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarDocsDependencia")
    public String goAgregarDocsDependencia(@RequestBody DocumentoDependenciaBean docDep, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        try {
            String respuesta = docDependenciaService.insDocDependencia(docDep, usuario.getCoUsuario());
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goEliDocDependencia")
    public String goEliDocDependencia(@RequestBody DocumentoDependenciaBean docDep, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String respuesta = docDependenciaService.eliDocDependencia(docDep);
            mensaje = respuesta;
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (mensaje.equals("OK")) {
            model.addAttribute("pMensaje", "Datos eliminados.");
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdDocsDependencia")
    public String goUpdDocsDependencia(@RequestBody DocumentoDependenciaBean docDep, HttpServletRequest request, Model model) {
        String codDocReempl = ServletUtility.getInstancia().loadRequestParameter(request, "codDocReempl");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String mensaje = "NO_OK";
        try {
            String respuesta = docDependenciaService.updDocDependencia(docDep, codDocReempl, usuario.getCoUsuario());
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

    @RequestMapping(method = RequestMethod.GET, params = "accion=goMotDependencia")
    public String goMotDependencia(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String sCodDepencia = usuarioConfigBean.getCoDep();
        if ("1".equals(usuarioConfigBean.getInEsAdmin())) {
            model.addAttribute("dependenciaList", dependenciaService.getAllDependencia(true, sCodDepencia));
        } else {
            model.addAttribute("dependenciaList", dependenciaService.getAllDependencia(false, sCodDepencia));
        }
        BuscarDocDependencia buscarDocDependencia = new BuscarDocDependencia(sCodDepencia, "0");
        model.addAttribute(buscarDocDependencia);
        return "/configGeneral/motDependencia";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaDocMotDependencia")
    public String goListaDocMotDependencia(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDependencia");
        try {
            List<DocumentoDependenciaBean> docDepList = docDependenciaService.getDocDependenciaList(sCodDependencia);
            model.addAttribute("docDependenciaList", docDepList);
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/motDependenciaDocListado";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaMotDependencia")
    public String goListaMotDependencia(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDependencia");
        String sCodDoc = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDoc");
        try {
            List<MotivoBean> motList = docDependenciaService.getMotDependenciaList(sCodDependencia, sCodDoc);
            model.addAttribute("motList", motList);
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/motDependenciaMotListado";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goCargarMotDep")
    public String goCargarMotDep(HttpServletRequest request, Model model) {
        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDependencia");
        String sCodDoc = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDoc");
        List<MotivoBean> motFaltantesList = docDependenciaService.getMotFaltantesList(sCodDependencia, sCodDoc);
        model.addAttribute("motFaltantesList", motFaltantesList);
        return "/modalGeneral/consultaMotivoDependencia";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarMotiDocDependencia")
    public String goAgregarMotiDocDependencia(@RequestBody MotivoDocumentoDependenciaBean motDocDep, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {

            String respuesta = docDependenciaService.insMotDocDependencia(motDocDep);
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goCambiarMotiDocDependencia")
    public String goCambiarMotiDocDependencia(@RequestBody MotivoDocumentoDependenciaBean motDocDep, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        String codMotReempl = ServletUtility.getInstancia().loadRequestParameter(request, "codMotReempl");
        try {
            String respuesta = docDependenciaService.updMotDocDependencia(motDocDep, codMotReempl);
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goEliMotDocDependencia")
    public String goEliMotDocDependencia(@RequestBody MotivoDocumentoDependenciaBean motDocDep, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String respuesta = docDependenciaService.eliMotDocDependencia(motDocDep);
            mensaje = respuesta;
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (mensaje.equals("OK")) {
            model.addAttribute("pMensaje", "Datos eliminados.");
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }

    @RequestMapping(method = RequestMethod.GET, params = "accion=goGruposDestinos")
    public String goGruposDestinos(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String sCodDepencia = usuarioConfigBean.getCoDep();
        String sDeDependencia= usuarioConfigBean.getDeDep();
        model.addAttribute("coDep",sCodDepencia);
        model.addAttribute("deDep",sDeDependencia);
        
//        if ("1".equals(usuarioConfigBean.getInEsAdmin())) {
//            model.addAttribute("dependenciaList", dependenciaService.getAllDependencia(true, sCodDepencia));
//        } else {
//            model.addAttribute("dependenciaList", dependenciaService.getAllDependencia(false, sCodDepencia));
//        }
//        BuscarDocDependencia buscarDocDependencia = new BuscarDocDependencia(sCodDepencia, "0");
//        model.addAttribute(buscarDocDependencia);
        return "/configGeneral/grupoDestino";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaGruposDestinos")
    public String goListaGruposDestinos(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDependencia");
        try {
            List<GrupoDestinoBean> grupoDestinoList = grupoDestinoService.getGruposDestinosList(sCodDependencia);
            model.addAttribute("grupoDestinoList", grupoDestinoList);
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/grupoDestinoListado";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaGrupoDestinoDet")
    public String goListaGrupoDestinoDet(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        String sCodGrupoDestino = ServletUtility.getInstancia().loadRequestParameter(request, "vCodGrupoDestino");
        try {
            List<GrupoDestinoDetalleBean> grupoDestinoDetList = grupoDestinoService.getGrupoDestinoDetalleList(sCodGrupoDestino);
            model.addAttribute("grupoDestinoDetList", grupoDestinoDetList);
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/grupoDestinoListadoDetalle";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goCargarDependencias")
    public String goCargarDependencias(HttpServletRequest request, Model model) {
        String codDepen = ServletUtility.getInstancia().loadRequestParameter(request, "codDepen");
        List<DependenciaBean> dependenciasList = grupoDestinoService.getDependenciasList(codDepen);
        model.addAttribute("dependenciasList", dependenciasList);

        return "/modalGeneral/consultaDependencias";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarDependenciaDestino")
    public String goAgregarDependenciaDestino(@RequestBody GrupoDestinoDetalleBean destDet, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
            String respuesta = grupoDestinoService.insDependenciaDestino(destDet, usuario.getCoUsuario());
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goCargarEmpleados")
    public String goCargarEmpleados(HttpServletRequest request, Model model) {
        String codDepen = ServletUtility.getInstancia().loadRequestParameter(request, "codDepen");
        String codGrupoDest = ServletUtility.getInstancia().loadRequestParameter(request, "codGrupoDest");
        //List<EmpleadoBean> empleadosList = grupoDestinoService.getEmpleadosDestList(codDepen, codGrupoDest);
        List<EmpleadoBean> empleadosList = grupoDestinoService.getEmpleadosDependenciaList(codDepen);
        model.addAttribute("empleadosList", empleadosList);
        return "/modalGeneral/consultaEmpleadosDest";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarEmpleadoDestino")
    public String goAgregarEmpleadoDestino(@RequestBody GrupoDestinoDetalleBean destDet, HttpServletRequest request, Model model) {
        String codEmpActual = ServletUtility.getInstancia().loadRequestParameter(request, "codEmpActual");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String mensaje = "NO_OK";
        try {
            String respuesta = grupoDestinoService.updDependenciaDestino(destDet, codEmpActual, usuario.getCoUsuario());
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goEliDetalleGrupo")
    public String goEliDetalleGrupo(@RequestBody GrupoDestinoDetalleBean destDet, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String respuesta = grupoDestinoService.eliDetalleGrupoDest(destDet);
            mensaje = respuesta;
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (mensaje.equals("OK")) {
            model.addAttribute("pMensaje", "Datos eliminados.");
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goNuevoGrupo")
    public String goNuevoGrupo(@RequestBody GrupoDestinoBean gruDest, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String codCurrentUser=usuario.getCoUsuario();
        try {
            mensaje = grupoDestinoService.insNuevoGrupoDest(gruDest,codCurrentUser);
        } catch (validarDatoException e) {
            mensaje = e.valorMsg;
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
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdateGrupo")
    public String goUpdateGrupo(@RequestBody GrupoDestinoBean gruDest, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String codCurrentUser=usuario.getCoUsuario();
        try {
            mensaje = grupoDestinoService.updGrupoDest(gruDest,codCurrentUser);
        } catch (validarDatoException e) {
            mensaje = (e.valorMsg);
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goEliGrupoDestino")
    public String goEliGrupoDestino(@RequestBody GrupoDestinoBean gruDest, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        try {
            String respuesta = grupoDestinoService.eliGrupoDestino(gruDest, usuario.getCoUsuario());
            mensaje = respuesta;
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (mensaje.equals("OK")) {
            model.addAttribute("pMensaje", "Datos eliminados.");
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goConfigPersonal")
    public String goConfigPersonal(HttpServletRequest request, Model model) {
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuarioConf = usuarioConfigService.getConfig(null, usuario.getCempCodemp(), usuario.getCoDep());
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioConf", usuarioConf);
        return "/configGeneral/configPersonal";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdConfigPersonal")
    public String goUpdConfigPersonal(@RequestBody UsuarioConfigBean usuarioConf, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String respuesta = usuarioConfigService.updUsuarioConfing(usuarioConf);
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goRegProveedores")
    public String goRegProveedores(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String url = "";
        if (usuarioConfigBean != null) {
            url = "/configGeneral/registroProveedores";
        } else {
            url = "login";
        }
        return url;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRegTupa")
    public String goRegTupa(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String url;
        if (usuarioConfigBean != null) {
            url = "/configGeneral/registroTupa";
        } else {
            url = "login";
        }
        return url;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRegCiudadanos")
    public String goRegCiudadanos(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String url;
        if (usuarioConfigBean != null) {
            url = "/configGeneral/registroCiudadano";
        } else {
            url = "login";
        }
        return url;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaRegProveedores")
    public String goListaRegProveedores(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            List<ProveedorBean> proveedoresList = proveedorService.getProveedoresList();
            model.addAttribute("proveedoresList", proveedoresList);
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/registroProveedoresListado";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaRegTupa")
    public String goListaRegTupa(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            List<TupaBean> TupaList = tupaService.getTupaList();
            model.addAttribute("tupaList", TupaList);
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/registroTupaListado";
    }    

    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaBusqProveedores")
    public String goListaBusqProveedores(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String busNroRuc = ServletUtility.getInstancia().loadRequestParameter(request, "busNroRuc");
            String busRazonSocial = ServletUtility.getInstancia().loadRequestParameter(request, "busRazonSocial");
            List<ProveedorBean> proveedoresList = proveedorService.getProveedoresBusqList(busNroRuc, busRazonSocial);
            
            if (proveedoresList!=null) {
                if (proveedoresList.size() >= applicationProperties.getTopRegistrosConsultas() ) { //Se debe cambiar a 100 segun lo especificado
                    model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
                }

                model.addAttribute("proveedoresList", proveedoresList);
                mensaje = "OK";
            }
            
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/registroProveedoresListado";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaBusqTupa")
    public String goListaBusqTupa(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String busTupaCorto         = ServletUtility.getInstancia().loadRequestParameter(request, "busTupaCorto");
            String busTupaDescripcion   = ServletUtility.getInstancia().loadRequestParameter(request, "busTupaDescripcion");
            
            List<TupaBean> tupaList = tupaService.getTupaBusqList(busTupaCorto, busTupaDescripcion);
            
            if (tupaList!=null) {
                if (tupaList.size()>= applicationProperties.getTopRegistrosConsultas()) {
                    model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
                }

                model.addAttribute("tupaList", tupaList);
                mensaje = "OK";
            }
            
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/registroTupaListado";
    }    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaBusqCiudadano")
    public String goListaBusqCiudadano(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String busCiudDocumento   = ServletUtility.getInstancia().loadRequestParameter(request, "busCiudDocumento");
            String busCiudApPaterno   = ServletUtility.getInstancia().loadRequestParameter(request, "busCiudApPaterno");
            String busCiudApMaterno   = ServletUtility.getInstancia().loadRequestParameter(request, "busCiudApMaterno");
            String busCiudNombres     = ServletUtility.getInstancia().loadRequestParameter(request, "busCiudNombres");
            
            List<CitizenBean> ciudadanoList = ciudadanoService.getCiudadanoBusqList(busCiudDocumento, busCiudApPaterno, busCiudApMaterno, busCiudNombres);
            
            if (ciudadanoList!=null) {
                if (ciudadanoList.size()>= applicationProperties.getTopRegistrosConsultas()) {
                    model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
                }

                model.addAttribute("ciudadanoList", ciudadanoList);
                mensaje = "OK";
            }
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/registroCiudadanoListado";
    }    

    @RequestMapping(method = RequestMethod.POST, params = "accion=goNuevoProveedor")
    public String goNuevoProveedor(HttpServletRequest request, Model model) {
        String codProveedor = ServletUtility.getInstancia().loadRequestParameter(request, "vcodProveedor");
        ProveedorBean proveedor = null;
        
        if (codProveedor != "") {
            proveedor = proveedorService.getProveedor(codProveedor);
        }
        
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String url;
        if (usuarioConfigBean != null) {
            url = "/configGeneral/registroProveedoresNuevo";
        } else {
            url = "login";
        }
        /*--HPB 13/01/20 Integrar PIDE--*/
        String vIndicadorPide = commonQryService.obtenerValorParametro("IN_PIDE");
        model.addAttribute("vIndicadorPide", vIndicadorPide);
        /*--HPB 13/01/20 Integrar PIDE--*/         
        model.addAttribute("proveedor", proveedor);
        return url;
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goNuevoTupa")
    public String goNuevoTupa(HttpServletRequest request, Model model) {
        String codTupa = ServletUtility.getInstancia().loadRequestParameter(request, "vcodTupa");
        TupaBean tupa = null;
        if (codTupa != "") {
            tupa = tupaService.getTupa(codTupa);
        }

        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String url = "";
        if (usuarioConfigBean != null) {
            url = "/configGeneral/registroTupaNuevo";
        } else {
            url = "login";
        }
        model.addAttribute("tupa", tupa);
        return url;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goNuevoCiudadano")
    public String goNuevoCiudadano(HttpServletRequest request, Model model) {
        String codCiudadano = ServletUtility.getInstancia().loadRequestParameter(request, "vcodCiudadano");
        CitizenBean ciudadano = null;
        if (!"".equals(codCiudadano)) {
            ciudadano = ciudadanoService.getCiudadano(codCiudadano);
        }

        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String url = "";
        if (usuarioConfigBean != null) {
            url = "/configGeneral/registroCiudadanoNuevo";
        } else {
            url = "login";
        }
        model.addAttribute("ciudadano", ciudadano);
        return url;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goCargarUbigeo")
    public String goCargarUbigeo(HttpServletRequest request, Model model) {
        /*
         * revisar este metodo.
         */
        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDependencia");
        String sCodDoc = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDoc");
        List<MotivoBean> motFaltantesList = docDependenciaService.getMotFaltantesList(sCodDependencia, sCodDoc);
        model.addAttribute("motFaltantesList", motFaltantesList);
        return "/modalGeneral/consultaUbigeoProveedor";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goCargarUbigeoEmi")
    public String goCargarUbigeoEmi(HttpServletRequest request, Model model) {
        /*
         * revisar este metodo.
         */
        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDependencia");
        String sCodDoc = ServletUtility.getInstancia().loadRequestParameter(request, "vCodDoc");
        List<MotivoBean> motFaltantesList = docDependenciaService.getMotFaltantesList(sCodDependencia, sCodDoc);
        model.addAttribute("motFaltantesList", motFaltantesList);
        return "/modalGeneral/consultaUbigeoProveedorEmi";
    }
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaBusqUbigeoEmi")
    public String goListaBusqUbigeoEmi(@RequestBody UbigeoBean ubigeo, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        List<UbigeoBean> ubigeoList = proveedorService.getUbigeoBusqList(ubigeo);
        model.addAttribute("ubigeoList", ubigeoList);
        return "/modalGeneral/consultaUbigeoProveedorEmiDet";
    }
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaBusqUbigeo")
    public String goListaBusqUbigeo(@RequestBody UbigeoBean ubigeo, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        List<UbigeoBean> ubigeoList = proveedorService.getUbigeoBusqList(ubigeo);
        model.addAttribute("ubigeoList", ubigeoList);
        return "/modalGeneral/consultaUbigeoProveedorDet";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarProveedor")
    public String goAgregarProveedor(@RequestBody ProveedorBean proveedor, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String respuesta = proveedorService.insProveedor(proveedor);
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarTupa")
    public String goAgregarTupa(@RequestBody TupaBean tupa, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String codusuario = usuario.getCoUsuario();
        
        try {
            String respuesta = tupaService.insTupa(tupa,codusuario);
            mensaje = respuesta;
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (mensaje.equals("OK")) {            
            referencedData.emptyListTupaExpediente(); // Limpiamos la lista de Tupa de memoria.            
            model.addAttribute("pMensaje", "Datos guardados.");
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarCiudadano")
    public String goAgregarCiudadano(@RequestBody CitizenBean ciudadano, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String codUsuario = usuario.getCoUsuario();

        try {
            String respuesta = ciudadanoService.insCiudadano(ciudadano, codUsuario);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goOtrosOrigenes")
    public String goOtrosOrigenes(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String url = "";
        if (usuarioConfigBean != null) {
            url = "/configGeneral/otrosOrigenes";
        } else {
            url = "login";
        }
        return url;
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goNuevoOrigen")
    public String goNuevoOrigen(HttpServletRequest request, Model model) {
        String codOrigen = ServletUtility.getInstancia().loadRequestParameter(request, "vcodOrigen");
        OtroOrigenBean origenEditar = null;
        if (codOrigen != "") {
            origenEditar = otroOrigenService.getOtroOrigen(codOrigen);
        }
        List<SiElementoBean> tipoDocList = otroOrigenService.getTipoDocumentoList();
        model.addAttribute("tipoDocList", tipoDocList);
        model.addAttribute("origenEditar", origenEditar);
        return "/configGeneral/otrosOrigenesNuevo";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaOtrosOrigenes")
    public String goListaOtrosOrigenes(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            List<OtroOrigenBean> otroOrigenList = otroOrigenService.getOtrosOrigenesList();
            List<SiElementoBean> tipoDocList = otroOrigenService.getTipoDocumentoList();
            model.addAttribute("otroOrigenList", otroOrigenList);
            model.addAttribute("tipoDocList", tipoDocList);
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/otrosOrigenesListado";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaBusqOtrosOrigenes")
    public String goListaBusqOtrosOrigenes(@RequestBody OtroOrigenBean otroOrigen, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            List<OtroOrigenBean> otroOrigenList = otroOrigenService.getOtroOrigenBusqList(otroOrigen);
            List<SiElementoBean> tipoDocList = otroOrigenService.getTipoDocumentoList();
            
            if (otroOrigenList!=null) {
                if (otroOrigenList.size() >= applicationProperties.getTopRegistrosConsultas()) { 
                    model.addAttribute("msjEmision","OPTIMICE SU CONSULTA");
                }

                model.addAttribute("otroOrigenList", otroOrigenList);
                model.addAttribute("tipoDocList", tipoDocList);
                mensaje = "OK";
            }
            
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/otrosOrigenesListado";
    }

    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarOtroOrigen")
    public String goAgregarOtroOrigen(@RequestBody OtroOrigenBean otroOrigen, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String respuesta = otroOrigenService.insOtroOrigen(otroOrigen);
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdOtroOrigen")
    public String goUpdOtroOrigen(@RequestBody OtroOrigenBean otroOrigen, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String respuesta = otroOrigenService.updOtroOrigen(otroOrigen);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdTupa")
    public String goUpdTupa(@RequestBody TupaBean tupa, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String codusuario = usuario.getCoUsuario();
        
        try {
            String respuesta = tupaService.updTupa(tupa, codusuario);
            mensaje = respuesta;
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (mensaje.equals("OK")) {
            referencedData.emptyListTupaExpediente(); // Limpiamos la lista de Tupa de memoria.
            model.addAttribute("pMensaje", "Datos guardados.");
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdCiudadano")
    public String goUpdCiudadano(@RequestBody CitizenBean ciudadano, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String codusuario = usuario.getCoUsuario();

        try {
            String respuesta = ciudadanoService.updCiudadano(ciudadano, codusuario);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdProveedor")
    public String goUpdProveedor(@RequestBody ProveedorBean proveedor, HttpServletRequest request, Model model) {
        String mensaje      = "NO_OK";
        Usuario usuario     = Utilidades.getInstancia().loadUserFromSession(request);
        String codProveedor = usuario.getCoUsuario();

        try {
            String respuesta = proveedorService.updProveedor(proveedor, codProveedor);
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goSelDependencia")
    public String goSelDependencia(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String sCodDepencia = usuarioConfigBean.getCoDep();
//        String codDepen = ServletUtility.getInstancia().loadRequestParameter(request, "codDepen");
//        List<DependenciaBean> dependenciasList = grupoDestinoService.getDependenciasList(codDepen);
//        model.addAttribute("dependenciasList", dependenciasList);
        if ("1".equals(usuarioConfigBean.getInEsAdmin())) {
            model.addAttribute("dependenciasList", dependenciaService.getAllDependencia(true, sCodDepencia));
        } else {
            model.addAttribute("dependenciasList", dependenciaService.getAllDependencia(false, sCodDepencia));
        }
//        BuscarDocDependencia buscarDocDependencia = new BuscarDocDependencia(sCodDepencia, "0");
//        model.addAttribute(buscarDocDependencia);

        return "/modalGeneral/consultaSelDependencia";
    }
    

    @RequestMapping(method = RequestMethod.GET, params = "accion=goAdminUO")
    public String goAdminUO(HttpServletRequest request, Model model) {
        String url = "/configGeneral/adminDependencia";
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        
        if ("1".equals(usuarioConfigBean.getInEsAdmin())) {
            model.addAttribute("dependenciaList", dependenciaService.getDependenciaHijo("1000"));
        } else {
            url = "";
        }
        
        return url;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAdminUO")
    public String goAdminUO(String param,HttpServletRequest request, Model model) {
        String url = "/configGeneral/adminDependenciaLista";
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        
        if ("1".equals(usuarioConfigBean.getInEsAdmin())) {
            model.addAttribute("dependenciaList", dependenciaService.getDependenciaHijo("1000"));
        } else {
            url = "";
        }
        
        return url;
    }    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaAdminUO")
    public String goBuscaAdminUO(HttpServletRequest request, Model model) {
        String url = "/configGeneral/adminDependenciaLista";
        String busDep = ServletUtility.getInstancia().loadRequestParameter(request, "busDep");
        String busTipo = ServletUtility.getInstancia().loadRequestParameter(request, "busTipo");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        model.addAttribute("dependenciaList", dependenciaService.getBuscaDependencia(busDep,busTipo));
        return url;
    }
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goEditAdminUO")
    public String goEditAdminUO(HttpServletRequest request, Model model) {
        String url = "/configGeneral/adminDependenciaEdit";
        String coDep = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDep");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        try {
            //if(coDep.trim().length()>1){
                //obtener datos de dependencia.
                model.addAttribute("dependenciaBean", dependenciaService.getDependencia(coDep));
                model.addAttribute("coLocal",dependenciaService.getCoLocal(coDep));
                model.addAttribute("lsTipoEncargatura", referencedData.grpElementoList("CO_TIPO_ENC"));
                model.addAttribute("lsLocal", referencedData.getLstLocal());
                model.addAttribute("lstEmpleadosDepenUUOO", dependenciaService.getLsEmpDepen(coDep));
                //model.addAttribute("accionBd", "UPD");
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goEditAdminUO")//ecueva
    public @ResponseBody String processFormEdit(@RequestBody TrxDependenciaBean trxDep,HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        String coRespuesta;
        String deRespuesta;
        String coDep = ServletUtility.getInstancia().loadRequestParameter(request, "pcoDep");
        String coLocal = ServletUtility.getInstancia().loadRequestParameter(request, "coLocal");
        String pTipoAnt = ServletUtility.getInstancia().loadRequestParameter(request, "pTipoAnt"); // tipo (Institucion || Comite especial)
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coEmp=usuario.getCempCodemp();
        trxDep.setCoDep(coDep);
        trxDep.setCoUseMod(coEmp);
        LocalDepBean localDep=new LocalDepBean();
        localDep.setCoLoc(coLocal); 
        localDep.setCoUseMod(coEmp);

        try{
            mensaje = dependenciaService.updDependenciaBean(trxDep,coDep,localDep,pTipoAnt);
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
        retval.append("\",\"coDep\":\"");
        retval.append(trxDep.getCoDep());  
        retval.append("\"}");

        return retval.toString();
    }    
    
    @RequestMapping(method = RequestMethod.GET, params = "accion=goNewAdminUO")
    public String goNewAdminUO(HttpServletRequest request, Model model) {
        String url = "/configGeneral/adminDependenciaEdit";
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        DependenciaBean dependenciaBean = new DependenciaBean();
        dependenciaBean.setInBaja("0");
        try {
            model.addAttribute("dependenciaBean", dependenciaBean);
            model.addAttribute("lsTipoEncargatura", referencedData.grpElementoList("CO_TIPO_ENC"));
            model.addAttribute("lsLocal", referencedData.getLstLocal());
            //model.addAttribute("accionBd", "INS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goNewAdminUO")
    public @ResponseBody String processFormNew(@RequestBody TrxDependenciaBean trxDep,HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        String coRespuesta;
        String deRespuesta;
        String coLocal = ServletUtility.getInstancia().loadRequestParameter(request, "coLocal");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        trxDep.setCoUseMod(usuario.getCempCodemp());
        LocalDepBean localDep=new LocalDepBean();
        localDep.setCoLoc(coLocal);

        try{
            mensaje = dependenciaService.insDependenciaBean(trxDep,localDep);
        }catch(validarDatoException e){
           mensaje=(e.valorMsg);
        }catch (Exception e) { 
           e.printStackTrace(); 
        } 
        String action="";        
        if (mensaje.equals("OK")) {
            action="srTablaConfiguracion.do?accion=goEditAdminUO";            
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
        retval.append("\",\"action\":\"");
        retval.append(action);          
        retval.append("\",\"coDep\":\"");
        retval.append(trxDep.getCoDep());  
        retval.append("\"}");

        return retval.toString();    
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaEmpEncargado")
    public String goListaEmpEncargado(HttpServletRequest request, Model model){
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        model.addAttribute("iniFuncionParm","3");
        model.addAttribute("listaEmpleado",commonQryService.getLsEmpleado(""));
        return "/modalGeneral/consultaElaboradoPorConsul";
    }    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaEmpleo")
    public String goListaEmpleo(HttpServletRequest request, Model model){
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        /**
         * IDCODIGO: ID DEL ELEMENTO CODIGO DE CARGO HTML
         * IDDESCRIPCION: ID DEL ELEMENTO DESCRIPCION DE CARGO EN HTML
         */
        String idCodigo = ServletUtility.getInstancia().loadRequestParameter(request, "idCodigo");
        String idDescripcion = ServletUtility.getInstancia().loadRequestParameter(request, "idDescripcion");
        model.addAttribute("idCodigo",idCodigo);
        model.addAttribute("idDescripcion",idDescripcion);
        
        model.addAttribute("iniFuncionParm","1");
        model.addAttribute("listaCargo",commonQryService.getLsEmpleo());
        return "/modalGeneral/consultaCargoFuncionEmp";
    } 
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDepPadre")
    public String goBuscaDepPadre(HttpServletRequest request, Model model){
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        model.addAttribute("iniFuncionParm","8");
        model.addAttribute("listaDestinatario",commonQryService.getLsDepencia());
        return "/modalGeneral/consultaDestinatarioEmiConsul";
    }     
    
//    @RequestMapping(method = RequestMethod.POST, params = "accion=goGrabaDepedenciaBean")
//    public @ResponseBody String goGrabaDepedenciaBean(DependenciaBean dep,HttpServletRequest request, Model model){
//        String mensaje = "NO_OK";
//        String coRespuesta;
//        String deRespuesta;
//        String accionBd = ServletUtility.getInstancia().loadRequestParameter(request, "accionBd");
//        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
//        dep.setIdAct(usuario.getCempCodemp());
//
//        try{
//            mensaje = dependenciaService.grabaDependenciaBean(dep,accionBd);
//        }catch (Exception e) { 
//           e.printStackTrace(); 
//        } 
//        
//        if (mensaje.equals("OK")) {
//            coRespuesta = "1";
//        }else{
//            coRespuesta = "0";
//        }  
//        deRespuesta = mensaje;
//        
//        StringBuilder retval = new StringBuilder();
//        retval.append("{\"coRespuesta\":\"");
//        retval.append(coRespuesta);
//        retval.append("\",\"deRespuesta\":\"");
//        retval.append(deRespuesta);
//        retval.append("\",\"coDep\":\"");
//        retval.append(dep.getCoDependencia());  
//        retval.append("\"}");
//
//        return retval.toString();        
//    }  
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaEmpUUOO")
    public String goBuscaEmpUUOO(HttpServletRequest request, Model model){
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String pnomEmp = ServletUtility.getInstancia().loadRequestParameter(request, "pnomEmp");
        model.addAttribute("iniFuncionParm","4");
        model.addAttribute("listaEmpleado",commonQryService.getLsEmpleadoIntitu(pnomEmp));
        return "/modalGeneral/consultaElaboradoPorConsul";
    }
    
    /**
     * Muestra vista inicial para administrar usuarios
     * @param request
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.GET, params = "accion=goAdmEmpleado")
    public String goAdmEmpleado(HttpServletRequest request, Model model){
        try{
            UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "/configGeneral/admEmpleado";
    }
    
    /**
     * Realiza busqueda de empleados segun criterios del formulario
     * @param empleado
     * @param request
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBsqAdmEmpleado")
    public String goBsqAdmEmpleado(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String dni = ServletUtility.getInstancia().loadRequestParameter(request, "txtDni");
        String apPaterno = ServletUtility.getInstancia().loadRequestParameter(request, "txtApPaterno");
        String apMaterno = ServletUtility.getInstancia().loadRequestParameter(request, "txtApMaterno");
        String nombres = ServletUtility.getInstancia().loadRequestParameter(request, "txtNombres");
        try{
            model.addAttribute("listaEmpleado",admEmpleadoService.getBsqAdmEmpleado(dni, apPaterno.toUpperCase(), apMaterno.toUpperCase(),nombres.toUpperCase()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return "/configGeneral/admEmpleadoList";
    } 
    
    /**
     * Obtiene un empleado para editar datos
     * @param request
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.GET, params = "accion=goEditAdmEmpleado")
    public String goEditAdmEmpleado(HttpServletRequest request, Model model) {
        String codEmp = ServletUtility.getInstancia().loadRequestParameter(request, "codEmp");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        try {
            String vParametro = usuarioService.obtenerValorParam("LOGIN_LDAP");//Hermes 07/10/19 LDAP
            AdmEmpleadoBean empleado = admEmpleadoService.getAdmEmpleado(codEmp);
            model.addAttribute("titulo","Edición de Empleado");
            model.addAttribute("empleado",empleado);
            model.addAttribute("vParametro", vParametro);
            AdmEmpleadoAccesoBean acceso = admEmpleadoService.getAcceso(codEmp);
            model.addAttribute("acceso",acceso);
            /* [HPB] Inicio 29/09/22 OS-0000768-2022 */
            String vPerfil = null;
            if(acceso.getCoUsuario()!= null && acceso.getCoUsuario().length()>0)
                vPerfil = admEmpleadoService.getPerfilUsuarioCemp(codEmp);
            model.addAttribute("vPerfil",vPerfil);
            model.addAttribute("lstPerfil", referencedData.grpElementoListPerfil("PERFIL_SGD"));
            /* [HPB] Fin 29/09/22 OS-0000768-2022 */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/configGeneral/admEmpleadoEdit";
    }
    
    /**
     * permite guardar nuevo empleado y actualizar datos
     * @param empleado
     * @param request
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdAdmEmpleado")
    public @ResponseBody String goUpdAdmEmpleado(@RequestBody AdmEmpleadoGlobalBean global, HttpServletRequest request, Model model) {
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        JSONObject obj;
        try {
            AdmEmpleadoBean empleado = global.getEmpleado();
            AdmEmpleadoAccesoBean acceso = global.getAcceso();
            /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
            UsuarioConfigBean usuConfig = null;
            String encontrado = "0";
            /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
            //verifica disponibilidad de USERNAME
            acceso.setCoUsuario(acceso.getCoUsuario().toUpperCase().trim());
            List<AdmEmpleadoAccesoBean> list = new ArrayList<AdmEmpleadoAccesoBean>();
            if(acceso.getCoUsuario().trim().length()>0){// Verificar si existe el usuario a registrar
                list = admEmpleadoService.getLsAcceso(acceso);
            }
            if(list.isEmpty()){
                if(empleado.getCoEmpleado().length()==0){ //NUEVO EMPLEADO
                    admEmpleadoService.nuevoAdmEmpleado(empleado,acceso,usuario);
                    AdmEmpleadoBean newEmpleado = admEmpleadoService.getAdmEmpleado(empleado.getCoEmpleado());
                    obj = new JSONObject(newEmpleado);
                    obj.put("exito",true);
                    obj.put("mensaje","Nuevo Empleado registrado.");
                } else { // ACTUALIZAR EMPLEADO
                    int tmp = admEmpleadoService.updAdmEmpleado(empleado,acceso);
                    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
                    if(acceso.getCoUsuario().length()>0){
                        usuConfig = new UsuarioConfigBean();
                        usuConfig.setCempCodemp(empleado.getCoEmpleado());
                        usuConfig.setCoDep(empleado.getCoDependencia());
                        usuConfig.setTiAcceso("1");
                        usuConfig.setTiConsulta("1");
                        usuConfig.setInFirma("0");
                        usuConfig.setInCargaDocMesaPartes("0");
                        usuConfig.setInTipoDoc("0");
                        usuConfig.setInObsDocumento("0");
                        usuConfig.setInReviDocumento("0");
                        encontrado = admEmpleadoService.getVerificaConfEmpc(empleado.getCoDependencia(), empleado.getCoEmpleado());
                        if(encontrado.equals("OK"))
                            usuarioConfigService.updUsuarioConfing(usuConfig);                     
                        else
                            usuarioConfigService.insUsuarioConfing(usuConfig);                            
                    }
                    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
                    if(tmp==1){
                        AdmEmpleadoBean updEmpleado = admEmpleadoService.getAdmEmpleado(empleado.getCoEmpleado());
                        obj = new JSONObject(updEmpleado);
                        obj.put("exito", true);
                        obj.put("mensaje", "Datos actualizados.");
                    } else {
                        obj = new JSONObject();
                        obj.put("exito", false);
                        obj.put("mensaje", "Los datos proporcionados no son validos.");
                    }
                }
                /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
                AdmEmpleadoAccesoBean accesoNew = admEmpleadoService.getAcceso(empleado.getCoEmpleado());
                if(accesoNew!=null){
                    System.out.println("acceso.getCoPerfil()--> "+acceso.getCoPerfil());
                    System.out.println("accesoNew.getCoUsuario()--> "+ accesoNew.getCoUsuario());
                    System.out.println("empleado.getCoEmpleado()--> "+ empleado.getCoEmpleado());
                    admEmpleadoService.insPerfilUsuario(accesoNew.getCoUsuario(), 
                            acceso.getCoPerfil(), empleado.getCoEmpleado(), usuario);
                }
                /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
            }else{
                obj = new JSONObject();
                obj.put("exito", true);
                obj.put("mensaje", "Nombre de usuario no disponible.");
                Collection list2 = list;
                obj.put("list",new JSONArray(list2));
            }
        } catch (Exception e) {
            obj = new JSONObject();
            obj.put("exito",false);
            obj.put("mensaje",e.toString());
        }
        return obj.toString();
    }
    
    /**
     * Muestra formulario para nuevo Empleado
     * @param request
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.GET, params = "accion=goNewAdmEmpleado")
    public String goNewAdmEmpleado(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        try {
            String vParametro = usuarioService.obtenerValorParam("LOGIN_LDAP");//Hermes 07/10/19 LDAP
            model.addAttribute("vParametro", vParametro);//Hermes 07/10/19 LDAP
            model.addAttribute("titulo","Nuevo Empleado");
            model.addAttribute("empleado",null);
            model.addAttribute("acceso",null);
            model.addAttribute("accesoList",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/configGeneral/admEmpleadoEdit";
    }
    
    /**
     * Obtiene datos de una persona por medio de su DNI
     * @param request
     * @param model
     * @return JSON
     */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goGetPersonaDesdeDni")
    public @ResponseBody String goGetPersonaDesdeDni(HttpServletRequest request, Model model){
        String dni = ServletUtility.getInstancia().loadRequestParameter(request, "dni");
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String respuesta = null;
        JSONObject obj;
        try {
            AdmEmpleadoBean empleado = admEmpleadoService.getAdmEmpleadoDesdeDni(dni);
            if(empleado==null){ // no existe, cargar datos
                AdmEmpleadoBean persona = admEmpleadoService.getPersonaDesdeDni(dni);
                if(persona!=null){
                    obj = new JSONObject(persona);
                    obj.put("exito",true);
                    obj.put("si_existe",false);
                    obj.put("disponible",true);
                    respuesta = obj.toString();
                } else {
                    obj = new JSONObject();
                    obj.put("exito",true);
                    obj.put("si_existe",true);
                    obj.put("disponible",false);
                    obj.put("mensaje","No se encontró ninguna persona con el DNI brindado");
                    respuesta = obj.toString();
                }
            } else {
                obj = new JSONObject();
                obj.put("exito",true);
                obj.put("si_existe",true);
                obj.put("disponible",false);
                obj.put("mensaje","El DNI brindado ya está registrado");
                respuesta = obj.toString();
            }
        } catch (Exception e) {
            obj = new JSONObject();
            obj.put("exito",false);
            obj.put("mensaje",e.toString());
            respuesta = obj.toString();
            e.printStackTrace();
        }
        return respuesta;
    }
    
    /**
     * Muestra formulario para elegir Dependencia del empleado
     * @param request
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAdmEmpleadoDependencia")
    public String goAdmEmpleadoDependencia(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String criterio = ServletUtility.getInstancia().loadRequestParameter(request, "criterio");
        try {
            model.addAttribute("dependenciaList",admEmpleadoService.getBsqDependencia(criterio.toUpperCase()));
            model.addAttribute("criterio",criterio);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/configGeneral/admEmpleadoDependencia";
    }
    
    /**
     * Elimina acceso del empleado
     * @param acceso
     * @param request
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAdmEmpleadoEliminarAcceso")
    public @ResponseBody String goAdmEmpleadoEliminarAcceso(@RequestBody AdmEmpleadoAccesoBean acceso, HttpServletRequest request, Model model) {
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        JSONObject obj = new JSONObject();
        try {
            boolean re = admEmpleadoService.deleteAcceso(acceso);
            obj.put("exito",true);
            if(re){
                obj.put("mensaje","Los datos de acceso del empleado fueron eliminados.");
            } else {
                obj.put("mensaje","Ha ocurrido un error inesperado");
            }
        } catch (Exception e) {
            obj.put("exito",false);
            obj.put("mensaje",e.toString());
        }
        return obj.toString();
    }
    
    
    /**
     * Restablece la contrasena de Acceso del Usuario
     * @param acceso
     * @param request
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAdmEmpleadoRestablecerAcceso")
    public @ResponseBody String goAdmEmpleadoRestablecerAcceso(@RequestBody AdmEmpleadoAccesoBean acceso, HttpServletRequest request, Model model) {
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        JSONObject obj = new JSONObject();
        try {
            if(acceso.getCoEmpleado()!= null && acceso.getCoEmpleado().length()>0){
                AdmEmpleadoBean empleado = admEmpleadoService.getAdmEmpleado(acceso.getCoEmpleado());
                admEmpleadoService.restablecerAcceso(usuario.getCoUsuario(), acceso, empleado);
                obj.put("exito",true);
                obj.put("mensaje","La contraseña ha sido restablecida.");
            } else {
                throw new Exception("El empleado no existe");
            }
        } catch (Exception e) {
            obj.put("exito",false);
            obj.put("mensaje",e.toString());
        }
        return obj.toString();
    }
    
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAdmEmpleadoVerificarUsername")
    public @ResponseBody String goAdmEmpleadoVerificarUsername(@RequestBody AdmEmpleadoAccesoBean acceso, HttpServletRequest request, Model model) {
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        JSONObject obj = new JSONObject();
        try {
            List<AdmEmpleadoAccesoBean> list = new ArrayList<AdmEmpleadoAccesoBean>();
            acceso.setCoUsuario(acceso.getCoUsuario().toUpperCase().trim());
            list = admEmpleadoService.getLsAcceso(acceso);
            Collection collection = list;
            obj.put("exito",true);
            JSONArray listObj = new JSONArray(collection);
            obj.put("list",listObj);
        } catch (Exception e) {
            obj.put("exito",false);
            obj.put("mensaje",e.toString());
        }
        return obj.toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goEmptyListMaestro")
    public @ResponseBody String goEmptyListMaestro(HttpServletRequest request, Model model){
       String mensaje;
       StringBuilder retval = new StringBuilder();
       String coRespuesta;
       String vRespuesta;
       try{
          Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request); 
          referencedData.emptyListMaestra();
          mensaje="OK";
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goChangeImgPortada")
    public String goChangeImgPortada(HttpServletRequest request, Model model) {
        return "/configGeneral/imgPortada/imgPortada";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUploadImg")
    public @ResponseBody String goUploadImg(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = null;
        DocumentoFileBean fileMeta = null;
        /*String pNuAnn = null;
        String pNuEmi = null;

        pNuAnn = ServletUtility.getInstancia().loadRequestParameter(request, "pnuAnn");
        pNuEmi = ServletUtility.getInstancia().loadRequestParameter(request, "pnuEmi");*/
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String coUsu=usuario.getCoUsuario();
        
        String vreturn = "NO_OK";
        
        while (itr.hasNext()) {

            mpf = request.getFile(itr.next());
            
            //2.3 create new fileMeta
            fileMeta = new DocumentoFileBean();
            fileMeta.setNombreArchivo(mpf.getOriginalFilename());
            fileMeta.setTamanoArchivo(mpf.getSize() / 1024 + " Kb");
            fileMeta.setTipoArchivo(mpf.getContentType());

            try {
                fileMeta.setArchivoBytes(mpf.getBytes());
                vreturn=imagenPortadaService.saveImgPortada(coUsu, fileMeta);
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
     
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goRegRequisito")
    public String goRegRequisito(HttpServletRequest request, Model model) {
        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String url;
        if (usuarioConfigBean != null) {
            url = "/configGeneral/registroRequisito";
        } else {
            url = "login";
        }
        return url;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaBusqRequisito")
    public String goListaBusqRequisito(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        String indicador = "";
        try {
            String descripcion = ServletUtility.getInstancia().loadRequestParameter(request, "busRequisitoDescripcion");
            String estado = ServletUtility.getInstancia().loadRequestParameter(request, "estado");
            indicador = ServletUtility.getInstancia().loadRequestParameter(request, "indicador");
            RequisitoBean requisito = new RequisitoBean();
            requisito.setDescripcion(descripcion);
            requisito.setEsEstado(estado);
            if (indicador != null && !indicador.equals("0")) {
                requisito.setIndicador(indicador);
            }
            List<RequisitoBean> requisitoList = requisitoService.getRequisitoBusqList(requisito);
            
            if (requisitoList != null) {
                if (requisitoList.size()>= applicationProperties.getTopRegistrosConsultas()) {
                    model.addAttribute("msjEmision", "OPTIMICE SU CONSULTA");
                }

                model.addAttribute("requisitoList", requisitoList);
                mensaje = "OK";
            }
            
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (indicador.equals("0")) {
            return "/configGeneral/registroRequisitoListado";
        } else {
            return "/modalGeneral/consultaTupaRequisito";
        }        
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goNuevoRequisito")
    public String goNuevoRequisito(HttpServletRequest request, Model model) {
        String codRequisito = ServletUtility.getInstancia().loadRequestParameter(request, "vcodRequisito");
        RequisitoBean requisito = null;
        if (codRequisito != "") {
            requisito = requisitoService.getRequisito(codRequisito);
        }

        UsuarioConfigBean usuarioConfigBean = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String url = "";
        if (usuarioConfigBean != null) {
            url = "/configGeneral/registroRequisitoNuevo";
        } else {
            url = "login";
        }
        model.addAttribute("requisito", requisito);
        return url;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarRequisito")
    public String goAgregarRequisito(@RequestBody RequisitoBean requisito, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String codusuario = usuario.getCoUsuario();
        
        try {
            String respuesta = requisitoService.insRequisito(requisito,codusuario);
            mensaje = respuesta;
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (mensaje.equals("OK")) {            
            // referencedData.emptyListRequisitoExpediente(); // Limpiamos la lista de Requisito de memoria.            
            model.addAttribute("pMensaje", "Datos guardados.");
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goUpdRequisito")
    public String goUpdRequisito(@RequestBody RequisitoBean requisito, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        String codusuario = usuario.getCoUsuario();
        
        try {
            String respuesta = requisitoService.updRequisito(requisito, codusuario);
            mensaje = respuesta;
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (mensaje.equals("OK")) {
            // referencedData.emptyListRequisitoExpediente(); // Limpiamos la lista de Requisito de memoria.
            model.addAttribute("pMensaje", "Datos guardados.");
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }

    // Tupa x Requisitos
    @RequestMapping(method = RequestMethod.POST, params = "accion=goListaTupaRequisito")
    public String goListaTupaRequisito(HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String codProceso = ServletUtility.getInstancia().loadRequestParameter(request, "codProceso");
            RequisitoBean requisito = new RequisitoBean();
            requisito.setCodProceso(codProceso);
            requisito.setEsEstado("1"); // Habilitados
            List<RequisitoBean> requisitoList = requisitoService.getTupaRequisitoBusqList(requisito);
            
            if (requisitoList != null) {
                model.addAttribute("requisitoList", requisitoList);
                mensaje = "OK";
            }
            
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        return "/configGeneral/registroTupaRequisitos";       
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAgregarTupaRequisito")
    public String goAgregarTupaRequisito(@RequestBody RequisitoBean requisito, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
            requisito.setNuCorrelativo("1");
            requisito.setIndicador("0"); // No Obligatorio
            requisito.setEsEstado("1");
            String respuesta = requisitoService.insTupaRequisito(requisito, usuario.getCoUsuario());
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

    @RequestMapping(method = RequestMethod.POST, params = "accion=goCambiarTupaRequisito")
    public String goCambiarTupaRequisito(@RequestBody RequisitoBean requisito, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
            String respuesta = requisitoService.updTupaRequisito(requisito, usuario.getCoUsuario());
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
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goEliTupaRequisito")
    public String goEliTupaRequisito(@RequestBody RequisitoBean requisito, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        try {
            String respuesta = requisitoService.eliTupaRequisito(requisito);
            mensaje = respuesta;
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }
        if (mensaje.equals("OK")) {
            model.addAttribute("pMensaje", "Datos eliminados.");
            return "respuesta";
        } else {
            model.addAttribute("pMensaje", mensaje);
            return "respuesta";
        }
    }
    /*--HPB 13/01/20 Integrar PIDE--*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goGetProveedorDesdeRuc")
    public @ResponseBody String goGetProveedorDesdeRuc(HttpServletRequest request, Model model){
        String nuRuc = ServletUtility.getInstancia().loadRequestParameter(request, "ruc");
        String respuesta = null;
        JSONObject obj;
        try {
            ProveedorBean proveedor = proveedorService.getProveedor(nuRuc);
            if(proveedor==null){ // no existe, cargar datos
                ProveedorBean persona = commonQryService.getLstProveedor(nuRuc);
                if(persona!=null){
                    obj = new JSONObject(persona);
                    obj.put("exito",true);
                    obj.put("si_existe",false);
                    obj.put("disponible",true);
                    respuesta = obj.toString();
                } else {
                    obj = new JSONObject();
                    obj.put("exito",true);
                    obj.put("si_existe",true);
                    obj.put("disponible",false);
                    obj.put("mensaje","No se encontró ninguna persona jurídica con el RUC brindado");
                    respuesta = obj.toString();
                }
            } else {
                obj = new JSONObject();
                obj.put("exito",true);
                obj.put("si_existe",true);
                obj.put("disponible",false);
                obj.put("mensaje","El RUC brindado ya está registrado");
                respuesta = obj.toString();
            }
        } catch (Exception e) {
            obj = new JSONObject();
            obj.put("exito",false);
            obj.put("mensaje",e.toString());
            respuesta = obj.toString();
            e.printStackTrace();
        }
        return respuesta;
    }    
    /*--HPB 13/01/20 Integrar PIDE--*/  
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    @RequestMapping(method = RequestMethod.POST, params = "accion=goCargarAccesoNuevo")
    public String goCargarAccesoNuevo(HttpServletRequest request, Model model) {
        String sCodDependencia = ServletUtility.getInstancia().loadRequestParameter(request, "vCoDependencia");
        String sUserName = ServletUtility.getInstancia().loadRequestParameter(request, "vUserName");

        model.addAttribute("lstDestintarioDocAdmEmi",null);
        model.addAttribute("lstDependenciaAccesos", usuarioConfigService.getListDependenciaAccesos(sCodDependencia, sUserName));
        return "/modalGeneral/consultaAccesoAdicional";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goBuscaDependenciaAccesoAdicional")
    public String goBuscaDependenciaAccesoAdicional(HttpServletRequest request, Model model){
        String pdeDepen = ServletUtility.getInstancia().loadRequestParameter(request, "pdeDepen");
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        model.addAttribute("listaDependenciaDestEmi",referencedData.getListDependenciaDestinatarioEmi(usuario.getCoDep(),pdeDepen));
        return "/modalGeneral/consultaDependenciaAccesoAdicional";
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goNuevoDepAdic")
    public String goNuevoDepAdic(@RequestBody EmpleadoDependenciaBean depAdic, HttpServletRequest request, Model model) {
        String mensaje = "NO_OK";
        Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);

        String coUsuarioEmpleado=depAdic.getCoUsuario();
        String codigoEmpleado = depAdic.getCempCodemp();
        String coLocal = usuario.getCoLocal();
        depAdic.setCoLocal(coLocal);

        try {
            mensaje = admEmpleadoService.insDependenciaAdicional(depAdic, codigoEmpleado, coUsuarioEmpleado);
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
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
    /*-- [HPB] Inicio 23/02/23 CLS-087-2022 --*/
     /**
     * Muestra formulario para elegir local del empleado
     * @param request
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.POST, params = "accion=goAdmEmpleadoLocal")
    public String goAdmEmpleadoLocal(HttpServletRequest request, Model model) {
        String criterio = ServletUtility.getInstancia().loadRequestParameter(request, "criterio");
        try {
            model.addAttribute("localList",admEmpleadoService.getBsqLocal(criterio.toUpperCase()));
            model.addAttribute("criterio",criterio);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/configGeneral/admEmpleadoLocal";
    }
    /*-- [HPB] Fin 23/02/23 CLS-087-2022 --*/    
}