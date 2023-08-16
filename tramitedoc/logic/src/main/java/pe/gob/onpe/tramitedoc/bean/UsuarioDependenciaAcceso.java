/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author consultor_ogti_80
 */
public class UsuarioDependenciaAcceso {
    private String coUsuario;
    private String cempCodemp;
    private String coDependencia;
    private String deDependencia;
    private String tiAcceso;
    private String tiAccesoMp;
    private String inConsulta;
    private String inConsultaMp;
    private String inMesaPartes;
    /*-- [HPB] Inicio 16/01/23 CLS-087-2022 --*/
    private String inTrabajador;
    /*-- [HPB] Fin 16/01/23 CLS-087-2022 --*/
    public String getCoUsuario() {
        return coUsuario;
    }

    public void setCoUsuario(String coUsuario) {
        this.coUsuario = coUsuario;
    }

    public String getCempCodemp() {
        return cempCodemp;
    }

    public void setCempCodemp(String cempCodemp) {
        this.cempCodemp = cempCodemp;
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

    public String getTiAcceso() {
        return tiAcceso;
    }

    public void setTiAcceso(String tiAcceso) {
        this.tiAcceso = tiAcceso;
    }

    public String getTiAccesoMp() {
        return tiAccesoMp;
    }

    public void setTiAccesoMp(String tiAccesoMp) {
        this.tiAccesoMp = tiAccesoMp;
    }

    public String getInConsulta() {
        return inConsulta;
    }

    public void setInConsulta(String inConsulta) {
        this.inConsulta = inConsulta;
    }

    public String getInConsultaMp() {
        return inConsultaMp;
    }

    public void setInConsultaMp(String inConsultaMp) {
        this.inConsultaMp = inConsultaMp;
    }

    public String getInMesaPartes() {
        return inMesaPartes;
    }

    public void setInMesaPartes(String inMesaPartes) {
        this.inMesaPartes = inMesaPartes;
    }
    /*-- [HPB] Inicio 16/01/23 CLS-087-2022 --*/
    public String getInTrabajador() {
        return inTrabajador;
    }

    public void setInTrabajador(String inTrabajador) {
        this.inTrabajador = inTrabajador;
    }
    /*-- [HPB] Fin 16/01/23 CLS-087-2022 --*/
}
