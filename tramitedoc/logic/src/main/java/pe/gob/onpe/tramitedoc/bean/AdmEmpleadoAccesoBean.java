/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author GLuque
 */
public class AdmEmpleadoAccesoBean {
    private String coEmpleado;
    private String coUsuario;
    private String deUsuario;
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    private String coPerfil;
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/

    public String getCoUsuario() {
        return coUsuario;
    }

    public void setCoUsuario(String coUsuario) {
        this.coUsuario = coUsuario;
    }

    public String getDeUsuario() {
        return deUsuario;
    }

    public void setDeUsuario(String deUsuario) {
        this.deUsuario = deUsuario;
    }

    public String getCoEmpleado() {
        return coEmpleado;
    }

    public void setCoEmpleado(String coEmpleado) {
        this.coEmpleado = coEmpleado;
    }
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    public String getCoPerfil() {
        return coPerfil;
    }

    public void setCoPerfil(String coPerfil) {
        this.coPerfil = coPerfil;
    }
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
}
