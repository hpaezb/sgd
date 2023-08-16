/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author wcutipa
 */
public class RemitosResBean {
    private String nuAnn;
    private String nuEmi; 
    private String tiEmi;
    private String deTiEmi;
    private String tiCap; 
    private String deDepEmi;
    private String deEmpEmi; 
    private String deEmpRes; 
    private String deOriEmi;
    private String feEmi;
    private String deTiDoc;
    private String nuDoc;
    private String deAsu;
    private String nuExpediente;
    private String deEsDocEmi;
    private String coTipDocAdm;
    
    /*22/08/19 HPB Devolucion Doc a Oficina*/
    private String docEstadoMsj;
    private String deDocEstadoMsj;
    private String feDevOfi;
    private String obsDev;
    private String coEmpDev;
    /*22/08/19 HPB Devolucion Doc a Oficina*/
    private String obsMpv;/*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
    private String coEmpEmi; /*[HPB-02/10/20] Inicio - Plazo de Atencion-*/
    /*-- [HPB] Inicio 24/02/23 CLS-087-2022 --*/
    private String deDireccion;
    private String deCorreo;
    private String telefono;
    private String feExp;
    /*-- [HPB] Fin 24/02/23 CLS-087-2022 --*/
    public String getNuAnn() {
        return nuAnn;
    }

    public void setNuAnn(String nuAnn) {
        this.nuAnn = nuAnn;
    }

    public String getNuEmi() {
        return nuEmi;
    }

    public void setNuEmi(String nuEmi) {
        this.nuEmi = nuEmi;
    }

    public String getTiEmi() {
        return tiEmi;
    }

    public void setTiEmi(String tiEmi) {
        this.tiEmi = tiEmi;
    }

    public String getDeTiEmi() {
        return deTiEmi;
    }

    public void setDeTiEmi(String deTiEmi) {
        this.deTiEmi = deTiEmi;
    }

    public String getTiCap() {
        return tiCap;
    }

    public void setTiCap(String tiCap) {
        this.tiCap = tiCap;
    }

    public String getDeDepEmi() {
        return deDepEmi;
    }

    public void setDeDepEmi(String deDepEmi) {
        this.deDepEmi = deDepEmi;
    }

    public String getDeEmpEmi() {
        return deEmpEmi;
    }

    public void setDeEmpEmi(String deEmpEmi) {
        this.deEmpEmi = deEmpEmi;
    }

    public String getDeOriEmi() {
        return deOriEmi;
    }

    public void setDeOriEmi(String deOriEmi) {
        this.deOriEmi = deOriEmi;
    }

    public String getFeEmi() {
        return feEmi;
    }

    public void setFeEmi(String feEmi) {
        this.feEmi = feEmi;
    }

    public String getDeTiDoc() {
        return deTiDoc;
    }

    public void setDeTiDoc(String deTiDoc) {
        this.deTiDoc = deTiDoc;
    }

    public String getNuDoc() {
        return nuDoc;
    }

    public void setNuDoc(String nuDoc) {
        this.nuDoc = nuDoc;
    }

    public String getDeAsu() {
        return deAsu;
    }

    public void setDeAsu(String deAsu) {
        this.deAsu = deAsu;
    }

    public String getNuExpediente() {
        return nuExpediente;
    }

    public void setNuExpediente(String nuExpediente) {
        this.nuExpediente = nuExpediente;
    }

    public String getDeEsDocEmi() {
        return deEsDocEmi;
    }

    public void setDeEsDocEmi(String deEsDocEmi) {
        this.deEsDocEmi = deEsDocEmi;
    }

    public String getDeEmpRes() {
        return deEmpRes;
    }

    public void setDeEmpRes(String deEmpRes) {
        this.deEmpRes = deEmpRes;
    }

    /**
     * @return the coTipDocAdm
     */
    public String getCoTipDocAdm() {
        return coTipDocAdm;
    }

    /**
     * @param coTipDocAdm the coTipDocAdm to set
     */
    public void setCoTipDocAdm(String coTipDocAdm) {
        this.coTipDocAdm = coTipDocAdm;
    }

    public String getDocEstadoMsj() {
        return docEstadoMsj;
    }

    public void setDocEstadoMsj(String docEstadoMsj) {
        this.docEstadoMsj = docEstadoMsj;
    }

    public String getDeDocEstadoMsj() {
        return deDocEstadoMsj;
    }

    public void setDeDocEstadoMsj(String deDocEstadoMsj) {
        this.deDocEstadoMsj = deDocEstadoMsj;
    }

    public String getFeDevOfi() {
        return feDevOfi;
    }

    public void setFeDevOfi(String feDevOfi) {
        this.feDevOfi = feDevOfi;
    }

    public String getObsDev() {
        return obsDev;
    }

    public void setObsDev(String obsDev) {
        this.obsDev = obsDev;
    }

    public String getCoEmpDev() {
        return coEmpDev;
    }

    public void setCoEmpDev(String coEmpDev) {
        this.coEmpDev = coEmpDev;
    }   

    public String getObsMpv() {
        return obsMpv;
    }

    public void setObsMpv(String obsMpv) {
        this.obsMpv = obsMpv;
    }

    public String getCoEmpEmi() {
        return coEmpEmi;
    }

    public void setCoEmpEmi(String coEmpEmi) {
        this.coEmpEmi = coEmpEmi;
    }
    /*-- [HPB] Inicio 24/02/23 CLS-087-2022 --*/
    public String getDeDireccion() {
        return deDireccion;
    }

    public void setDeDireccion(String deDireccion) {
        this.deDireccion = deDireccion;
    }

    public String getDeCorreo() {
        return deCorreo;
    }

    public void setDeCorreo(String deCorreo) {
        this.deCorreo = deCorreo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }     

    public String getFeExp() {
        return feExp;
    }

    public void setFeExp(String feExp) {
        this.feExp = feExp;
    }
    /*-- [HPB] Fin 24/02/23 CLS-087-2022 --*/  
}
