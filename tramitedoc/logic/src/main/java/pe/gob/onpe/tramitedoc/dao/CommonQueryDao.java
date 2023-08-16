/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.CargoFuncionBean;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBean;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBeanNew;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioOtroOrigenBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoVoBoBean;
import pe.gob.onpe.tramitedoc.bean.ProveedorBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;

/*interoperabilidad*/
import pe.gob.onpe.tramitedoc.bean.DatosInterBean;
/*interoperabilidad*/
/**
 *
 * @author ECueva
 */
public interface CommonQueryDao {
    DependenciaBean getDependenciaxCoDependencia(String coDepen);
    DependenciaBean getDependenciaBasico(String coDepen);
    List<RemitenteBean> getListRemitente(String coDepen);
    String verificarExpedienteDuplicado(String pnuAnnExp, String pcoDepExp, String pcoGru, String pnuCorrExp);
    String verficarNumeroDuplicadoEmiDocExtRecep(String pnuAnn,String pcoDepEmi,String pnuCorEmi);
    List<ProveedorBean> getLstProveedores(String prazonSocial);
    List<DestinatarioOtroOrigenBean> getLstOtrosOrigenes(String pdescripcion);
    CiudadanoBean getCiudadano(String pnuDoc);
    ProveedorBean getProveedor(String pnuRuc);
    List<EmpleadoBean> getLsEmpleado(String coDep);
    List<CargoFuncionBean> getLsEmpleo(); 
    List<DependenciaBean> getLsDepencia();
    String[] getNroCorrLocalDependencia();
    List<SiElementoBean> getLsDepenciaMensjeria();
    List<EmpleadoBean> getLsEmpleadoIntitu(String deEmp);
    String[] getCodigoMotivoDocRec(String nuAnn,String nuEmi,String nuDes);
    EmpleadoBean getEmpJefeDep(String coDep);
    List<EmpleadoBean> getLsEmpDepDesEmp(String deEmp,String coDep);
    List<EmpleadoVoBoBean> getLsPersonalVoBo(String nuAnn,String nuEmi);
    String getInFirmaDoc(String pcoDep,String pcoTipoDoc);
    List<CiudadanoBean> getLstCiudadanos(String sDescCiudadano);   
    String obtenerValorParametro(String nombreParametro);
    
    List<CiudadanoBean> getLstCiudadanos(String sApePatCiudadano, String sApeMatCiudadano, String sNombreCiudadano);
    /*interoperabilidad*/
    DatosInterBean DatosInter(String nuAnn,String nuEmi);
    /*interoperabilidad*/   
    /* HPB - [INICIO - 13/01/2020] - Integrar PIDE */   
    CiudadanoBeanNew getLstCiudadanoStored(String pnuDoc, String pcoTipoDoc);
    CiudadanoBean getLstCiudadanoAni(String pnuDoc, String pcoTipoDoc);
    ProveedorBean getLstProveedor(String pnuRuc);
    String getDescripcionUbigeo(String pcoDep,String pcoPrv,String pcoDis, String pcoCar) throws Exception;
    ProveedorBean getProveedorExpediente(String pnuRuc);
    DestinatarioOtroOrigenBean getDestinatarioOtroOrigenExpediente(String pnuCodigo);
    /* HPB - [FIN - 13/01/2020] - Integrar PIDE */ 
    String obtenerIndicadorEntidadPide(String nroRuc);/*[HPB] 06/11/20 Modificaciones en el envío de documentos. Listado de entidades que interoperan*/
}
