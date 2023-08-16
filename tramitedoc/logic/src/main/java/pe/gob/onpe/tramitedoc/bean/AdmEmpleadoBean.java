/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramitedoc.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author GLuque
 */
public class AdmEmpleadoBean {
    private String coEmpleado;
    private String apPaterno;
    private String apMaterno;
    private String nombres;
    private String dni;
    private String fechaNacimiento;
    private String sexo;
    private String email;
    private String estado;
    private String coDependencia;
    private String deDependencia;
    private String coCargo;
    private String deCargo;
    /* [HPB] Inicio 23/02/23 CLS-087-2022 */
    private String coLocal;
    private String deLocal;
    /* [HPB] Fin 23/02/23 CLS-087-2022 */
    public String getCoEmpleado() {
        return coEmpleado;
    }

    public void setCoEmpleado(String coEmpleado) {
        this.coEmpleado = coEmpleado;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Convierte fecha de formato: 
     * "yyyy-MM-dd HH:mm:ss" a
     * "dd/MM/yyyy"
     * @return String date
     */
    public String getFechaNacimiento() {
        if(this.fechaNacimiento==null){
            return "";
        }else{
            try{
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//1962-07-21 00:00:00
                Date date = dateFormat.parse(this.fechaNacimiento);
                dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                return dateFormat.format(date);
            }catch(ParseException e){
                return "";
            }
        }
    }
    
    /**
     * Retorna la fecha en el siguiente formato
     * @return String date
     */
    public String getFechaNacimiento2() {
        return this.fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getCoDependencia() {
        return coDependencia;
    }

    public void setCoDependencia(String coDependencia) {
        this.coDependencia = coDependencia;
    }

    public String getDeDependencia() {
        return deDependencia;
    }

    public void setDeDependencia(String deDependencia) {
        this.deDependencia = deDependencia;
    }

    public String getCoCargo() {
        return coCargo;
    }

    public void setCoCargo(String coCargo) {
        this.coCargo = coCargo;
    }

    public String getDeCargo() {
        return deCargo;
    }

    public void setDeCargo(String deCargo) {
        this.deCargo = deCargo;
    }
    /* [HPB] Inicio 23/02/23 CLS-087-2022 */
    public String getCoLocal() {
        return coLocal;
    }

    public void setCoLocal(String coLocal) {
        this.coLocal = coLocal;
    }
   
    public String getDeLocal() {
        return deLocal;
    }

    public void setDeLocal(String deLocal) {
        this.deLocal = deLocal;
    }
    /* [HPB] Fin 23/02/23 CLS-087-2022 **/   
}
