/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramitedoc.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.onpe.libreria.exception.validarDatoException;
/*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.autentica.service.UsuarioService;
import pe.gob.onpe.libreria.util.ConstantesSec;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.AdmEmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.AdmEmpleadoCargoBean;
import pe.gob.onpe.tramitedoc.bean.AdmEmpleadoAccesoBean;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoDependenciaBean;
import pe.gob.onpe.tramitedoc.bean.LocalBean;
import pe.gob.onpe.tramitedoc.dao.AdmEmpleadoDao;
/*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
import pe.gob.onpe.tramitedoc.dao.DependenciaDao;
import pe.gob.onpe.tramitedoc.dao.MesaPartesDao;
/*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
import pe.gob.onpe.tramitedoc.service.AdmEmpleadoService;

/**
 *
 * @author GLuque
 */
@Service("admEmpleadoService")
public class AdmEmpleadoServiceImp implements AdmEmpleadoService{
    
    @Autowired
    public AdmEmpleadoDao admEmpleadoDao;
    
    @Autowired
    public UsuarioService usuarioService;    
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    @Autowired
    public DependenciaDao dependenciaDao;
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
    /* [HPB] Inicio 13/07/23 OS-0000786-2023 Mejoras */
    @Autowired
    private MesaPartesDao mesaPartesDao;
    /* [HPB] Fin 13/07/23 OS-0000786-2023 Mejoras */
    /**
     * Obtiene un determinado empleado para modificar sus datos
     * @return
     * @throws Exception 
     */
    @Override
    public AdmEmpleadoBean getAdmEmpleado(String codEmp) throws Exception {
        AdmEmpleadoBean empleado = new AdmEmpleadoBean();
        try {
            empleado = admEmpleadoDao.getAdmEmpleado(codEmp);
        } catch (Exception e) {
            throw e;
        }
        return empleado;
    }
    
    /**
     * Actualiza datos personales de un empleado
     * @param empleado
     * @param acceso
     * @return
     * @throws Exception 
     */
    @Override
    public int updAdmEmpleado(AdmEmpleadoBean empleado, AdmEmpleadoAccesoBean acceso) throws Exception{
        int vReturn=0;
        try {
            vReturn=admEmpleadoDao.updAdmEmpleado(empleado);
            if(acceso.getCoUsuario().length()>0){//Crear acceso si solo viene con USERNAME 
                String vnuDni=Utility.getInstancia().cifrar(empleado.getDni(),ConstantesSec.SGD_SECRET_KEY_PASSWORD);
                admEmpleadoDao.saveNuevoAcceso(acceso.getCoUsuario(), acceso.getCoEmpleado(), vnuDni);
            }
        } catch (Exception e) {
            throw e;
        }        
        return vReturn;
    }
    
    /**
     * Guardar nuevo empleado
     * @param empleado
     * @param acceso
     * @param usuario
     * @return
     * @throws Exception 
     */
    @Override
    public String nuevoAdmEmpleado(AdmEmpleadoBean empleado, AdmEmpleadoAccesoBean acceso, Usuario usuario) throws Exception{
        String vResult="NO_OK";
        try {
            empleado.setCoEmpleado(admEmpleadoDao.getNextCoEmpleado());
            if(empleado.getCoEmpleado()!=null&&empleado.getCoEmpleado().trim().length()>0){
                vResult=admEmpleadoDao.nuevoAdmEmpleado(empleado, usuario);
                if(vResult.equals("OK")){
                    // guardamos accesos
                    acceso.setCoEmpleado(empleado.getCoEmpleado());
                    String vnuDni=Utility.getInstancia().cifrar(empleado.getDni(),ConstantesSec.SGD_SECRET_KEY_PASSWORD);
                    admEmpleadoDao.saveNuevoAcceso(acceso.getCoUsuario(), acceso.getCoEmpleado(), vnuDni);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return vResult;
    }
    
    /**
     * Obtiene un empleado de la tabla maestra "idtanirs" mediante su DNI
     * @param dni
     * @return
     * @throws Exception 
     */
    @Override
    public AdmEmpleadoBean getPersonaDesdeDni(String dni) throws Exception{
        try {
            return admEmpleadoDao.getPersonaDesdeDni(dni);
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Obtiene un determinado empleado mediante su DNI
     * @param dni
     * @return 
     * @throws Exception 
     */
    @Override
    public AdmEmpleadoBean getAdmEmpleadoDesdeDni(String dni) throws Exception{
        try {
            return admEmpleadoDao.getAdmEmpleadoDesdeDni(dni);
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Realiza búsqueda de empleados con los criterios lineas abajo.
     * Nota: no todo los campos son requeridos.
     * @param dni
     * @param apPaterno
     * @param apMaterno
     * @param nombres
     * @return
     * @throws Exception 
     */
    @Override
    public List<AdmEmpleadoBean> getBsqAdmEmpleado(String dni, String apPaterno, String apMaterno, String nombres) throws Exception{
        try {
            return admEmpleadoDao.getBsqAdmEmpleado(dni, apPaterno, apMaterno, nombres);
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Obtiene una lista de dependencia,
     * realizando una búsqueda de acuerdo al parámetro "CRITERIO"
     * @return
     * @throws Exception 
     */
    @Override
    public List<DependenciaBean> getBsqDependencia(String criterio)throws Exception{
        try {
            return admEmpleadoDao.getBsqDependencia(criterio);
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Obtiene lista de cargos de los empleados
     * @return
     * @throws Exception 
     */
    @Override
    public List<AdmEmpleadoCargoBean> getLsCargo() throws Exception{
        try {
            return admEmpleadoDao.getLsCargo();
        } catch (Exception e) {
            throw e;
        }
    }
    
    
    
    /**
     * Obtiene Objeto Acceso del Empleado
     * @param coEmpleado
     * @return
     * @throws Exception 
     */
    @Override
    public AdmEmpleadoAccesoBean getAcceso(String coEmpleado) throws Exception{
        try {
            return admEmpleadoDao.getAcceso(coEmpleado);
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Obtiene lista de Obj Acceso que coincidan con el NombreUsuario el cual se 
     * le asignará al empleado
     * @param acceso
     * @return
     * @throws Exception 
     */
    @Override
    public List<AdmEmpleadoAccesoBean> getLsAcceso(AdmEmpleadoAccesoBean acceso) throws Exception{
        try {
            return admEmpleadoDao.getLsAcceso(acceso);
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Elimina acceso al sistema para un determinado empleado
     * @param acceso
     * @return
     * @throws Exception 
     */
    @Override
    public boolean deleteAcceso(AdmEmpleadoAccesoBean acceso) throws Exception{
        try {
            acceso = admEmpleadoDao.getAcceso(acceso.getCoEmpleado());
            if(acceso!=null){
                usuarioService.delUser(acceso.getCoUsuario());                
            }
            return true;
        } catch (Exception e) {
            throw e;
        }        
    }
    
    /**
     * Guarda nuevo Usuario con Acceso al sistema
     * @param acceso
     * @return
     * @throws Exception 
     */
    @Override
    public String saveNuevoAcceso(AdmEmpleadoAccesoBean acceso) throws Exception{
        try {
            return admEmpleadoDao.saveNuevoAcceso(acceso.getCoUsuario(), acceso.getCoEmpleado(), null);
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Restablece el acceso al sistema de un empleado
     * @param coUse     
     * @param acceso
     * @param empleado
     * @return
     * @throws Exception 
     */
    @Override
    public boolean restablecerAcceso(String coUse, AdmEmpleadoAccesoBean acceso, AdmEmpleadoBean empleado) throws Exception{
        try {
            acceso = admEmpleadoDao.getAcceso(acceso.getCoEmpleado());
            if(acceso!=null){
                usuarioService.resetPassword(acceso.getCoUsuario(), empleado.getDni(), coUse);                
            }
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String insDependenciaAdicional(EmpleadoDependenciaBean depAdic, String coEmp, String coUsuario) throws Exception {
        String vReturn = "NO_OK";
        
        try {
            String coLocal = depAdic.getCoLocal();
            ArrayList<DependenciaBean> lstDependecia = depAdic.getEmpleadoDependenciaDetalle();
            if(lstDependecia!=null&&lstDependecia.size()>0){
                for (DependenciaBean dep : lstDependecia) {                                            
                    String sAccionBD = dep.getAccionBD();
                    if(dep.getTiAcceso().equals("1"))
                        dep.setTiAcceso("0");
                    else
                        dep.setTiAcceso("1");
                    if(dep.getInConsulta().equals("1"))
                        dep.setInConsulta("0");
                    else
                        dep.setInConsulta("1");
                    if(dep.getInConsultaMp().equals("1"))
                        dep.setInConsultaMp("0");
                    else
                        dep.setInConsultaMp("1");
                    if(dep.getTiAccesoMp().equals("1"))
                        dep.setTiAccesoMp("0");
                    else
                        dep.setTiAccesoMp("1");                        
                    
                    if ("INS".equals(sAccionBD)) {
                        vReturn = dependenciaDao.insDepAdicDelEmp(coUsuario, dep.getCoDependencia());
                        if(vReturn.equals("OK")){                                
                            vReturn=dependenciaDao.insAccesosDepAdicDelEmp(coEmp, 
                                    dep.getCoDependencia(), coLocal,
                                    dep.getTiAcceso(), dep.getInConsulta(),
                                    dep.getInConsultaMp(), dep.getTiAccesoMp());
                            if(!vReturn.equals("OK")){
                                vReturn="Error al dar accesos a dependencia.";
                                throw new validarDatoException(vReturn);                               
                            }
                            /*-- [HPB] Inicio 16/01/23 CLS-087-2022 --*/
                            if(dep.getInTrabajador().equals("1")){
                                vReturn = dependenciaDao.insEmpDependencia(coEmp, dep.getCoDependencia());
                            }
                            /*-- [HPB] Fin 16/01/23 CLS-087-2022 --*/
                            /* [HPB] Inicio 13/07/23 OS-0000786-2023 Mejoras */
                            if(dep.getCoDependencia().equals("11351")){
                                vReturn = mesaPartesDao.insPermisoMP(coEmp, dep.getCoDependencia());
                                if(!vReturn.equals("OK")){
                                    vReturn="Error al dar accesos a registrar expediente.";
                                    throw new validarDatoException(vReturn);                               
                                }
                            }
                            /* [HPB] Fin 13/07/23 OS-0000786-2023 Mejoras */
                        }else{
                            vReturn="Error al registrar dependencia adicional.";
                            throw new validarDatoException(vReturn);                      
                        }
                    }else if ("UPD".equals(sAccionBD)) {
                        vReturn = dependenciaDao.updAccesosDepAdicDelEmp(coEmp, 
                                    dep.getCoDependencia(),dep.getTiAcceso(), 
                                    dep.getInConsulta(), dep.getInConsultaMp(), 
                                    dep.getTiAccesoMp());
                        if ("NO_OK".equals(vReturn)) {
                            throw new validarDatoException("Error actualizando dependencia.");
                        }
                        /*-- [HPB] Inicio 16/01/23 CLS-087-2022 --*/
                        if(depAdic.getCoDependencia().equals(dep.getCoDependencia()) && dep.getInTrabajador().equals("0")){
                            vReturn = "UPD";
                        }else{
                            if(dep.getInTrabajador().equals("0")){
                                vReturn = dependenciaDao.delEmpDependenciaLst(coEmp, dep.getCoDependencia());
                            }else{
                                vReturn = dependenciaDao.insEmpDependencia(coEmp, dep.getCoDependencia());
                            }
                        }
                        /*-- [HPB] Fin 16/01/23 CLS-087-2022 --*/ 
                        /* [HPB] Inicio 13/07/23 OS-0000786-2023 Mejoras */
                        if(dep.getCoDependencia().equals("11351")){
                            vReturn = mesaPartesDao.updPermisoMP(coEmp, dep.getCoDependencia(), dep.getTiAccesoMp());
                            if(!vReturn.equals("OK")){
                                vReturn="Error al actualizar acceso a registrar expediente.";
                                throw new validarDatoException(vReturn);                               
                            }
                        }
                        /* [HPB] Fin 13/07/23 OS-0000786-2023 Mejoras */
                    }else if ("DEL".equals(sAccionBD)) {
                        if(!depAdic.getCoDependencia().equals(dep.getCoDependencia())){
                            vReturn = dependenciaDao.delDepAdic(coUsuario, dep.getCoDependencia());
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error al borrar dependencia adicional.");
                            }
                            /*-- [HPB] Inicio 16/01/23 CLS-087-2022 --*/
                            vReturn = dependenciaDao.delEmpDependenciaLst(coEmp, dep.getCoDependencia());
                            /*-- [HPB] Fin 16/01/23 CLS-087-2022 --*/  
                            /* [HPB] Inicio 13/07/23 OS-0000786-2023 Mejoras */
                            if(dep.getCoDependencia().equals("11351")){
                                vReturn = mesaPartesDao.delPermisoMP(coEmp, dep.getCoDependencia());
                                if(!vReturn.equals("OK")){
                                    vReturn="Error al eliminar acceso a registrar expediente.";
                                    throw new validarDatoException(vReturn);                               
                                }
                            }
                            /* [HPB] Fin 13/07/23 OS-0000786-2023 Mejoras */                            
                        }else{
                            vReturn = "DEL";
                        }
                    }
                }    
            }
        } catch (validarDatoException e) {
           throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("Error en la transacción al actualizar dependencia.");
        }
        return vReturn;  
    }

    @Override
    public String getPerfilUsuario(String coUser) throws Exception {
        try {
            return admEmpleadoDao.getPerfilUsuario(coUser);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String getPerfilUsuarioCemp(String coEmp) throws Exception {
        try {
            return admEmpleadoDao.getPerfilUsuarioCemp(coEmp);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String insPerfilUsuario(String coUser, String coPerfil, String coEmp, Usuario usuario) throws Exception {
        try {
            return admEmpleadoDao.insPerfilUsuario(coUser, coPerfil, coEmp, usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String getVerificaConfEmpc(String pCoDepen, String coEmp) {
        String vResult="NO_OK";
        try {
            vResult=admEmpleadoDao.getVerificaConfEmpc(pCoDepen, coEmp);
            if(vResult.equals("1")){
                vResult="OK";
            }else{
                vResult="NO_OK";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vResult;
    }
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
    /*-- [HPB] Inicio 23/02/23 CLS-087-2022 --*/
    /**
     * Obtiene una lista de dependencia,
     * realizando una búsqueda de acuerdo al parámetro "CRITERIO"
     * @return
     * @throws Exception 
     */
    @Override
    public List<LocalBean> getBsqLocal(String criterio) throws Exception {
        try {
            return admEmpleadoDao.getBsqLocal(criterio);
        } catch (Exception e) {
            throw e;
        }
    }
    /*-- [HPB] Fin 23/02/23 CLS-087-2022 --*/    
}
