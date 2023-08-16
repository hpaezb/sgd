package pe.gob.onpe.tramitedoc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.service.AvisoBandejaEntradaService;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import pe.gob.onpe.tramitedoc.bean.EtiquetaBandejaEnBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;

@Controller(value = "mainPanelController")
public class MainPanelController {

    @Autowired
    private AvisoBandejaEntradaService avisoBandejaEntradaService;
/*
    @Autowired @Qualifier(value = "applicationProps")
    private Properties properties;
*/

    @RequestMapping("/{version}/mainpanel.do")
    public String showMainPanel(Model model,HttpServletRequest request){
//    	Usuario usuario = Utilidades.getInstancia().loadUserFromSession(request);
        UsuarioConfigBean usuario = Utilidades.getInstancia().loadUserConfigFromSession(request);
        model.addAttribute("siglasDep",usuario.getDeSiglasDep());
        model.addAttribute("bandejaEntradaList",avisoBandejaEntradaService.getBandejaEntrada(usuario.getCoDep(),usuario.getCempCodemp(),usuario.getTiAcceso()));
        String coDep=usuario.getCoDep();
        String coEmp=usuario.getCempCodemp();
        List<EtiquetaBandejaEnBean> etiquetasList= avisoBandejaEntradaService.getListEtiquetaBandejaEntrada(coDep,coEmp);
        model.addAttribute("etiquetasList",etiquetasList);
        model.addAttribute("fechaActual",Utility.getInstancia().dateToFormatString(new Date()));
        String nameImgPortada=usuario.getNameImgPortadaSgd();
        if(!(nameImgPortada!=null&&nameImgPortada.trim().length()>0)){
            nameImgPortada="default.jpg";
        }
        model.addAttribute("imgId",nameImgPortada);
        return "mainpanel";
    }

}
