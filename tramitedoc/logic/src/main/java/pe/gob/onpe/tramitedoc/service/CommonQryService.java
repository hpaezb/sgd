/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramitedoc.service;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.CargoFuncionBean;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoVoBoBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;
/*interoperabilidad*/
import pe.gob.onpe.tramitedoc.bean.DatosInterBean;
import pe.gob.onpe.tramitedoc.bean.ProveedorBean;
/*interoperabilidad*/
/**
 *
 * @author ECueva
 */
public interface CommonQryService {
    DependenciaBean getDependenciaxCoDependencia(String coDepen); 
    List<RemitenteBean> getListRemitente(String coDepen);   
    String isExpedienteDuplicado(String pnuAnnExp, String pcoDepExp, String pcoGru, String pnuCorrExp);
    List<EmpleadoBean> getLsEmpleado(String coDepen);
    List<CargoFuncionBean> getLsEmpleo();
    List<DependenciaBean> getLsDepencia();
    List<SiElementoBean> getLsDepenciaMensjeria();
    List<EmpleadoBean> getLsEmpleadoIntitu(String deEmp);
    EmpleadoBean getEmpJefeDep(String coDep);
    List<EmpleadoBean> getLsEmpDepDesEmp(String deEmp,String coDep);
    List<EmpleadoVoBoBean> getLsPersonalVoBo(String nuAnn,String nuEmi);
    public String obtenerValorParametro(String nombreParametro);
      
    /*interoperabilidad*/
    DatosInterBean DatosInter(String nuAnn,String nuEmi);
    /*interoperabilidad*/ 
    ProveedorBean getLstProveedor(String pnuRuc);
    public String obtenerIndicadorEntidadPide(String nroRuc);/*[HPB] 06/11/20 Modificaciones en el envío de documentos. Listado de entidades que interoperan*/
}
