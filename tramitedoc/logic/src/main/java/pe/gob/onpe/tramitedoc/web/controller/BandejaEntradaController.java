/**
 * 
 */
package pe.gob.onpe.tramitedoc.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import pe.gob.onpe.tramitedoc.bean.EtiquetaBandejaEnBean;

import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.service.AvisoBandejaEntradaService;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;
/**
 * @author ecueva
 *
 */
@Controller
@RequestMapping("/{version}/srBandejaEntrada.do")
public class BandejaEntradaController {

    @Autowired
    private AvisoBandejaEntradaService avisoBandejaEntradaService;
    
    @RequestMapping(method = RequestMethod.POST, params = "accion=goInicio")
    public String goInicio(HttpServletRequest request, Model model){
        
        //Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuario = Utilidades.getInstancia().loadUserConfigFromSession(request);
        String coDep=usuario.getCoDep();
        String coEmp=usuario.getCempCodemp();
        List<EtiquetaBandejaEnBean> etiquetasList= avisoBandejaEntradaService.getListEtiquetaBandejaEntrada(coDep,coEmp);
        model.addAttribute("siglasDep",usuario.getDeSiglasDep());
        model.addAttribute("bandejaEntradaList",avisoBandejaEntradaService.getBandejaEntrada(usuario.getCoDep(),usuario.getCempCodemp(),usuario.getTiAcceso()));        
        model.addAttribute("etiquetasList",etiquetasList);
        return "bandejaEntradaDet";
    }
    
}
