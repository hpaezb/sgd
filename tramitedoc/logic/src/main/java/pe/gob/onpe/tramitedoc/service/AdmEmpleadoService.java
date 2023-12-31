/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramitedoc.service;

import java.util.List;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.tramitedoc.bean.AdmEmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.AdmEmpleadoCargoBean;
import pe.gob.onpe.tramitedoc.bean.AdmEmpleadoCategoriaBean;
import pe.gob.onpe.tramitedoc.bean.AdmEmpleadoAccesoBean;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
/*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
import pe.gob.onpe.tramitedoc.bean.EmpleadoDependenciaBean;
import pe.gob.onpe.tramitedoc.bean.LocalBean;
/*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
/**
 *
 * @author GLuque
 */
public interface AdmEmpleadoService {
    
    /**
     * Obtiene un empleado
     * @param codEmp
     * @return
     * @throws Exception 
     */
    AdmEmpleadoBean getAdmEmpleado(String codEmp) throws Exception;
    
    /**
     * Actualiza datos personales de un empleado
     * @param empleado
     * @param acceso
     * @return
     * @throws Exception 
     */
    int updAdmEmpleado(AdmEmpleadoBean empleado, AdmEmpleadoAccesoBean acceso) throws Exception;
    
    /**
     * Guardar nuevo empleado
     * @param empleado
     * @param acceso
     * @param usuario
     * @return
     * @throws Exception 
     */
    String nuevoAdmEmpleado(AdmEmpleadoBean empleado, AdmEmpleadoAccesoBean acceso, Usuario usuario) throws Exception;
    
    /**
     * Obtiene una persona de la tabla maestra "idtanirs" mediante su DNI
     * @param dni
     * @return
     * @throws Exception 
     */
    AdmEmpleadoBean getPersonaDesdeDni(String dni) throws Exception;
    
    /**
     * Obtiene un determinado empleado mediante su DNI
     * @param dni
     * @return
     * @throws Exception 
     */
    public AdmEmpleadoBean getAdmEmpleadoDesdeDni(String dni) throws Exception;
    
    
    /**
     * Realiza búsqueda de empleados con los siguientes criterios
     * @param dni
     * @param apPaterno
     * @param apMaterno
     * @param nombres
     * @return
     * @throws Exception 
     */
    List<AdmEmpleadoBean> getBsqAdmEmpleado(String dni, String apPaterno, String apMaterno, String nombres) throws Exception;
    
    /**
     * Obtiene una lista de dependencia,
     * realizando una búsqueda de acuerdo al parámetro "CRITERIO"
     * @param criterio
     * @return
     * @throws Exception 
     */
    List<DependenciaBean> getBsqDependencia(String criterio)throws Exception;
    
    /**
     * Obtiene lista de cargos de los empleados
     * @return
     * @throws Exception 
     */
    List<AdmEmpleadoCargoBean> getLsCargo() throws Exception;
    
    
    /**
     * Obtiene Objeto Acceso del Empleado
     * @param coEmpleado
     * @return
     * @throws Exception 
     */
    AdmEmpleadoAccesoBean getAcceso(String coEmpleado) throws Exception;
    
    
    /**
     * Obtiene lista de Obj Acceso que coincidan con el NombreUsuario el cual se 
     * le asignará al empleado
     * @param acceso
     * @return
     * @throws Exception 
     */
    List<AdmEmpleadoAccesoBean> getLsAcceso(AdmEmpleadoAccesoBean acceso) throws Exception;
    
    
    /**
     * Elimina acceso al sistema para un determinado empleado
     * @param acceso
     * @return
     * @throws Exception 
     */
    boolean deleteAcceso(AdmEmpleadoAccesoBean acceso) throws Exception;
    
    
    /**
     * Guarda nuevo Usuario con Acceso al sistema
     * @param acceso
     * @return
     * @throws Exception 
     */
    String saveNuevoAcceso(AdmEmpleadoAccesoBean acceso) throws Exception;
    
    
    /**
     * Restablece el acceso al sistema de un empleado
     * @param coUse     
     * @param acceso
     * @param empleado
     * @return
     * @throws Exception 
     */
    boolean restablecerAcceso(String coUse, AdmEmpleadoAccesoBean acceso, AdmEmpleadoBean empleado) throws Exception;
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    String getPerfilUsuario(String coUser) throws Exception;
    String getPerfilUsuarioCemp(String coEmp) throws Exception;
    String insPerfilUsuario(String coUser, String coPerfil, String coEmp, Usuario usuario) throws Exception;
    public String insDependenciaAdicional(EmpleadoDependenciaBean depAdic, String coEmp, String coUsuario)throws Exception;
    String getVerificaConfEmpc(String pCoDepen, String coEmp);
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
    /*-- [HPB] Inicio 23/02/23 CLS-087-2022 --*/
    /**
     * Obtiene una lista de local,
     * realizando una búsqueda de acuerdo al parámetro "CRITERIO"
     * @param criterio
     * @return
     * @throws Exception 
     */
    List<LocalBean> getBsqLocal(String criterio)throws Exception;
    /*-- [HPB] Fin 23/02/23 CLS-087-2022 --*/ 
    /* [HPB] Inicio 23/11/23 OS-0001287-2023 Dar de baja a empleado en grupos y comisiones. Advertencia si es jefe */
    List<DependenciaBean> getBsqEncargadoDependencia(String coEmp)throws Exception;
    String getValidaEncargadoDependencia(String coEmp) throws Exception;
    /* [HPB] Fin 23/11/23 OS-0001287-2023 Dar de baja a empleado en grupos y comisiones. Advertencia si es jefe */    
}
