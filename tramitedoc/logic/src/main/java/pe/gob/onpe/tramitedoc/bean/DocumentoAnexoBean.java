/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

import java.util.Date;

/**
 *
 * @author wcutipa
 */
public class DocumentoAnexoBean {

    private String nuAnn; 
    private String nuEmi; 
    private String nuAne;
    private String nuDes;
    private String deDet;
    private String deRutOri;
    private String coUseCre;
    private Date feUseCre;
    private String coUseMod;
    private Date feUseMod;
    private String tiOpe;
    private String reqFirma;    
    private String deDestino;
    private String esDoc;
    private String tiEnv;
    private String inAneSub; /*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR*/
    private String esPublico;
    //JAZANERO
    private String esProyecto;
    //JAZANERO

    /*----Hermes 22/01/2019----*/
    private String obMsj;
    private String tipDocMsj;
    private String deDocMsj;
    /*----Hermes 22/01/2019----*/
    /*[HPB] Inicio 18/08/23 OS-0000786-2023 Mejoras*/
    private String difHora;
    private int inDifHora;
    /*[HPB] Fin 18/08/23 OS-0000786-2023 Mejoras*/
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

    public String getNuAne() {
        return nuAne;
    }

    public void setNuAne(String nuAne) {
        this.nuAne = nuAne;
    }

    public String getNuDes() {
        return nuDes;
    }

    public void setNuDes(String nuDes) {
        this.nuDes = nuDes;
    }

    public String getDeDet() {
        return deDet;
    }

    public void setDeDet(String deDet) {
        this.deDet = deDet;
    }

    public String getDeRutOri() {
        return deRutOri;
    }

    public void setDeRutOri(String deRutOri) {
        this.deRutOri = deRutOri;
    }

    public String getCoUseCre() {
        return coUseCre;
    }

    public void setCoUseCre(String coUseCre) {
        this.coUseCre = coUseCre;
    }

    public Date getFeUseCre() {
        return feUseCre;
    }

    public void setFeUseCre(Date feUseCre) {
        this.feUseCre = feUseCre;
    }

    public String getCoUseMod() {
        return coUseMod;
    }

    public void setCoUseMod(String coUseMod) {
        this.coUseMod = coUseMod;
    }

    public Date getFeUseMod() {
        return feUseMod;
    }

    public void setFeUseMod(Date feUseMod) {
        this.feUseMod = feUseMod;
    }

    public String getTiOpe() {
        return tiOpe;
    }

    public void setTiOpe(String tiOpe) {
        this.tiOpe = tiOpe;
    }

    public String getReqFirma() {
        return reqFirma;
    }

    public void setReqFirma(String reqFirma) {
        this.reqFirma = reqFirma;
    }

    public String getDeDestino() {
        return deDestino;
    }

    public void setDeDestino(String deDestino) {
        this.deDestino = deDestino;
    }

    public String getEsDoc() {
        return esDoc;
    }

    public void setEsDoc(String esDoc) {
        this.esDoc = esDoc;
    }

    public String getTiEnv() {
        return tiEnv;
    }

    public void setTiEnv(String tiEnv) {
        this.tiEnv = tiEnv;
    }

    /**
     * @return the esProyecto
     */
    public String getEsProyecto() {
        return esProyecto;
    }

    /**
     * @param esProyecto the esProyecto to set
     */
    public void setEsProyecto(String esProyecto) {
        this.esProyecto = esProyecto;
    }

    public String getObMsj() {
        return obMsj;
    }

    public void setObMsj(String obMsj) {
        this.obMsj = obMsj;
    }

    public String getTipDocMsj() {
        return tipDocMsj;
    }

    public void setTipDocMsj(String tipDocMsj) {
        this.tipDocMsj = tipDocMsj;
    }

    public String getDeDocMsj() {
        return deDocMsj;
    }

    public void setDeDocMsj(String deDocMsj) {
        this.deDocMsj = deDocMsj;
    }

    public String getEsPublico() {
        return esPublico;
    }

    public void setEsPublico(String esPublico) {
        this.esPublico = esPublico;
    }

    public String getInAneSub() {
        return inAneSub;
    }

    public void setInAneSub(String inAneSub) {
        this.inAneSub = inAneSub;
    }
    /*[HPB] Inicio 18/08/23 OS-0000786-2023 Mejoras*/
    public String getDifHora() {
        return difHora;
    }

    public void setDifHora(String difHora) {
        this.difHora = difHora;
    }

    public int getInDifHora() {
        return inDifHora;
    }

    public void setInDifHora(int inDifHora) {
        this.inDifHora = inDifHora;
    }
    /*[HPB] Fin 18/08/23 OS-0000786-2023 Mejoras*/
}
