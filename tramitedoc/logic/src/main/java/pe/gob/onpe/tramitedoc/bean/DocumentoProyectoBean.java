/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author oti8
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoProyectoBean {
    private String coDepEmi;
    private String coEmpEmi;
    private String coEmpRes;
    private String coExp;
    private String coGru;
    private String coLocEmi;
    private String coOtrOriEmi;
    private String coTipDocAdm;
    private String deAne;
    private String deAsu;
    private String deAsuM;
    private String deDepEmi;
    private String deDesEst;
    private String deEmiRef;
    private String deEmpEmi;
    private String deEmpPro;
    private String deEmpRes;
    private String deEsDocEmi;
    private String deLocEmi;
    private String deMes;
    private String deOriEmi;
    private String deTipDocAdm;
    private String deTipEmi;
    private String esDocEmi;
    private String feEmi;
    private String feEmiCorta;
    private String inExpe;
    private String inOficio;
    private String nuAnn;
    private String nuAnnExp;
    private String nuCorDoc;
    private String nuCorEmi;
    private String nuDetExp;
    private String nuDiaAte;
    private String nuDniEmi;
    private String nuDoc;
    private String nuDocEmi;
    private String nuEmi;
    private String nuExpediente;
    private String nuRucEmi;
    private String nuSecExp;
    private String tiCap;
    private String tiEmi;
    private String tiEmiRef;
    private String tiEmpPro;
    private String existeDoc;
    private String existeAnexo;
    private String deDocSig;
    private String feExp;
    private String feExpCorta;
    private String coProceso;
    private String deProceso;
    private String coUseMod;
    private String coUseCre;
    private String nuSecuenciaFirma;
    private String msjResult;
    private String coPrioridad;
    private String inFirmaAnexo;
    private String docEstadoMsj;
    private String docObser;
    private String tiDest;
    private String tiEnvMsj;
    private String recursoenvio;
    private String deObsDoc;
    
    private String nEnviaCorreo;
    private String nuEmiProyecto;
    private String nuAnnProyecto;
    
    private String nuAneProyecto;
    /*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
    private String tiEmiExp;
    private String nuOriEmi;
    /*HPB 11/02/2020 - Fin - Requerimiento Remitente expediente*/
    /**
     * @return the coDepEmi
     */
    public String getCoDepEmi() {
        return coDepEmi;
    }

    /**
     * @param coDepEmi the coDepEmi to set
     */
    public void setCoDepEmi(String coDepEmi) {
        this.coDepEmi = coDepEmi;
    }

    /**
     * @return the coEmpEmi
     */
    public String getCoEmpEmi() {
        return coEmpEmi;
    }

    /**
     * @param coEmpEmi the coEmpEmi to set
     */
    public void setCoEmpEmi(String coEmpEmi) {
        this.coEmpEmi = coEmpEmi;
    }

    /**
     * @return the coEmpRes
     */
    public String getCoEmpRes() {
        return coEmpRes;
    }

    /**
     * @param coEmpRes the coEmpRes to set
     */
    public void setCoEmpRes(String coEmpRes) {
        this.coEmpRes = coEmpRes;
    }

    /**
     * @return the coExp
     */
    public String getCoExp() {
        return coExp;
    }

    /**
     * @param coExp the coExp to set
     */
    public void setCoExp(String coExp) {
        this.coExp = coExp;
    }

    /**
     * @return the coGru
     */
    public String getCoGru() {
        return coGru;
    }

    /**
     * @param coGru the coGru to set
     */
    public void setCoGru(String coGru) {
        this.coGru = coGru;
    }

    /**
     * @return the coLocEmi
     */
    public String getCoLocEmi() {
        return coLocEmi;
    }

    /**
     * @param coLocEmi the coLocEmi to set
     */
    public void setCoLocEmi(String coLocEmi) {
        this.coLocEmi = coLocEmi;
    }

    /**
     * @return the coOtrOriEmi
     */
    public String getCoOtrOriEmi() {
        return coOtrOriEmi;
    }

    /**
     * @param coOtrOriEmi the coOtrOriEmi to set
     */
    public void setCoOtrOriEmi(String coOtrOriEmi) {
        this.coOtrOriEmi = coOtrOriEmi;
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

    /**
     * @return the deAne
     */
    public String getDeAne() {
        return deAne;
    }

    /**
     * @param deAne the deAne to set
     */
    public void setDeAne(String deAne) {
        this.deAne = deAne;
    }

    /**
     * @return the deAsu
     */
    public String getDeAsu() {
        return deAsu;
    }

    /**
     * @param deAsu the deAsu to set
     */
    public void setDeAsu(String deAsu) {
        this.deAsu = deAsu;
    }

    /**
     * @return the deAsuM
     */
    public String getDeAsuM() {
        return deAsuM;
    }

    /**
     * @param deAsuM the deAsuM to set
     */
    public void setDeAsuM(String deAsuM) {
        this.deAsuM = deAsuM;
    }

    /**
     * @return the deDepEmi
     */
    public String getDeDepEmi() {
        return deDepEmi;
    }

    /**
     * @param deDepEmi the deDepEmi to set
     */
    public void setDeDepEmi(String deDepEmi) {
        this.deDepEmi = deDepEmi;
    }

    /**
     * @return the deDesEst
     */
    public String getDeDesEst() {
        return deDesEst;
    }

    /**
     * @param deDesEst the deDesEst to set
     */
    public void setDeDesEst(String deDesEst) {
        this.deDesEst = deDesEst;
    }

    /**
     * @return the deEmiRef
     */
    public String getDeEmiRef() {
        return deEmiRef;
    }

    /**
     * @param deEmiRef the deEmiRef to set
     */
    public void setDeEmiRef(String deEmiRef) {
        this.deEmiRef = deEmiRef;
    }

    /**
     * @return the deEmpEmi
     */
    public String getDeEmpEmi() {
        return deEmpEmi;
    }

    /**
     * @param deEmpEmi the deEmpEmi to set
     */
    public void setDeEmpEmi(String deEmpEmi) {
        this.deEmpEmi = deEmpEmi;
    }

    /**
     * @return the deEmpPro
     */
    public String getDeEmpPro() {
        return deEmpPro;
    }

    /**
     * @param deEmpPro the deEmpPro to set
     */
    public void setDeEmpPro(String deEmpPro) {
        this.deEmpPro = deEmpPro;
    }

    /**
     * @return the deEmpRes
     */
    public String getDeEmpRes() {
        return deEmpRes;
    }

    /**
     * @param deEmpRes the deEmpRes to set
     */
    public void setDeEmpRes(String deEmpRes) {
        this.deEmpRes = deEmpRes;
    }

    /**
     * @return the deEsDocEmi
     */
    public String getDeEsDocEmi() {
        return deEsDocEmi;
    }

    /**
     * @param deEsDocEmi the deEsDocEmi to set
     */
    public void setDeEsDocEmi(String deEsDocEmi) {
        this.deEsDocEmi = deEsDocEmi;
    }

    /**
     * @return the deLocEmi
     */
    public String getDeLocEmi() {
        return deLocEmi;
    }

    /**
     * @param deLocEmi the deLocEmi to set
     */
    public void setDeLocEmi(String deLocEmi) {
        this.deLocEmi = deLocEmi;
    }

    /**
     * @return the deMes
     */
    public String getDeMes() {
        return deMes;
    }

    /**
     * @param deMes the deMes to set
     */
    public void setDeMes(String deMes) {
        this.deMes = deMes;
    }

    /**
     * @return the deOriEmi
     */
    public String getDeOriEmi() {
        return deOriEmi;
    }

    /**
     * @param deOriEmi the deOriEmi to set
     */
    public void setDeOriEmi(String deOriEmi) {
        this.deOriEmi = deOriEmi;
    }

    /**
     * @return the deTipDocAdm
     */
    public String getDeTipDocAdm() {
        return deTipDocAdm;
    }

    /**
     * @param deTipDocAdm the deTipDocAdm to set
     */
    public void setDeTipDocAdm(String deTipDocAdm) {
        this.deTipDocAdm = deTipDocAdm;
    }

    /**
     * @return the deTipEmi
     */
    public String getDeTipEmi() {
        return deTipEmi;
    }

    /**
     * @param deTipEmi the deTipEmi to set
     */
    public void setDeTipEmi(String deTipEmi) {
        this.deTipEmi = deTipEmi;
    }

    /**
     * @return the esDocEmi
     */
    public String getEsDocEmi() {
        return esDocEmi;
    }

    /**
     * @param esDocEmi the esDocEmi to set
     */
    public void setEsDocEmi(String esDocEmi) {
        this.esDocEmi = esDocEmi;
    }

    /**
     * @return the feEmi
     */
    public String getFeEmi() {
        return feEmi;
    }

    /**
     * @param feEmi the feEmi to set
     */
    public void setFeEmi(String feEmi) {
        this.feEmi = feEmi;
    }

    /**
     * @return the feEmiCorta
     */
    public String getFeEmiCorta() {
        return feEmiCorta;
    }

    /**
     * @param feEmiCorta the feEmiCorta to set
     */
    public void setFeEmiCorta(String feEmiCorta) {
        this.feEmiCorta = feEmiCorta;
    }

    /**
     * @return the inExpe
     */
    public String getInExpe() {
        return inExpe;
    }

    /**
     * @param inExpe the inExpe to set
     */
    public void setInExpe(String inExpe) {
        this.inExpe = inExpe;
    }

    /**
     * @return the inOficio
     */
    public String getInOficio() {
        return inOficio;
    }

    /**
     * @param inOficio the inOficio to set
     */
    public void setInOficio(String inOficio) {
        this.inOficio = inOficio;
    }

    /**
     * @return the nuAnn
     */
    public String getNuAnn() {
        return nuAnn;
    }

    /**
     * @param nuAnn the nuAnn to set
     */
    public void setNuAnn(String nuAnn) {
        this.nuAnn = nuAnn;
    }

    /**
     * @return the nuAnnExp
     */
    public String getNuAnnExp() {
        return nuAnnExp;
    }

    /**
     * @param nuAnnExp the nuAnnExp to set
     */
    public void setNuAnnExp(String nuAnnExp) {
        this.nuAnnExp = nuAnnExp;
    }

    /**
     * @return the nuCorDoc
     */
    public String getNuCorDoc() {
        return nuCorDoc;
    }

    /**
     * @param nuCorDoc the nuCorDoc to set
     */
    public void setNuCorDoc(String nuCorDoc) {
        this.nuCorDoc = nuCorDoc;
    }

    /**
     * @return the nuCorEmi
     */
    public String getNuCorEmi() {
        return nuCorEmi;
    }

    /**
     * @param nuCorEmi the nuCorEmi to set
     */
    public void setNuCorEmi(String nuCorEmi) {
        this.nuCorEmi = nuCorEmi;
    }

    /**
     * @return the nuDetExp
     */
    public String getNuDetExp() {
        return nuDetExp;
    }

    /**
     * @param nuDetExp the nuDetExp to set
     */
    public void setNuDetExp(String nuDetExp) {
        this.nuDetExp = nuDetExp;
    }

    /**
     * @return the nuDiaAte
     */
    public String getNuDiaAte() {
        return nuDiaAte;
    }

    /**
     * @param nuDiaAte the nuDiaAte to set
     */
    public void setNuDiaAte(String nuDiaAte) {
        this.nuDiaAte = nuDiaAte;
    }

    /**
     * @return the nuDniEmi
     */
    public String getNuDniEmi() {
        return nuDniEmi;
    }

    /**
     * @param nuDniEmi the nuDniEmi to set
     */
    public void setNuDniEmi(String nuDniEmi) {
        this.nuDniEmi = nuDniEmi;
    }

    /**
     * @return the nuDoc
     */
    public String getNuDoc() {
        return nuDoc;
    }

    /**
     * @param nuDoc the nuDoc to set
     */
    public void setNuDoc(String nuDoc) {
        this.nuDoc = nuDoc;
    }

    /**
     * @return the nuDocEmi
     */
    public String getNuDocEmi() {
        return nuDocEmi;
    }

    /**
     * @param nuDocEmi the nuDocEmi to set
     */
    public void setNuDocEmi(String nuDocEmi) {
        this.nuDocEmi = nuDocEmi;
    }

    /**
     * @return the nuEmi
     */
    public String getNuEmi() {
        return nuEmi;
    }

    /**
     * @param nuEmi the nuEmi to set
     */
    public void setNuEmi(String nuEmi) {
        this.nuEmi = nuEmi;
    }

    /**
     * @return the nuExpediente
     */
    public String getNuExpediente() {
        return nuExpediente;
    }

    /**
     * @param nuExpediente the nuExpediente to set
     */
    public void setNuExpediente(String nuExpediente) {
        this.nuExpediente = nuExpediente;
    }

    /**
     * @return the nuRucEmi
     */
    public String getNuRucEmi() {
        return nuRucEmi;
    }

    /**
     * @param nuRucEmi the nuRucEmi to set
     */
    public void setNuRucEmi(String nuRucEmi) {
        this.nuRucEmi = nuRucEmi;
    }

    /**
     * @return the nuSecExp
     */
    public String getNuSecExp() {
        return nuSecExp;
    }

    /**
     * @param nuSecExp the nuSecExp to set
     */
    public void setNuSecExp(String nuSecExp) {
        this.nuSecExp = nuSecExp;
    }

    /**
     * @return the tiCap
     */
    public String getTiCap() {
        return tiCap;
    }

    /**
     * @param tiCap the tiCap to set
     */
    public void setTiCap(String tiCap) {
        this.tiCap = tiCap;
    }

    /**
     * @return the tiEmi
     */
    public String getTiEmi() {
        return tiEmi;
    }

    /**
     * @param tiEmi the tiEmi to set
     */
    public void setTiEmi(String tiEmi) {
        this.tiEmi = tiEmi;
    }

    /**
     * @return the tiEmiRef
     */
    public String getTiEmiRef() {
        return tiEmiRef;
    }

    /**
     * @param tiEmiRef the tiEmiRef to set
     */
    public void setTiEmiRef(String tiEmiRef) {
        this.tiEmiRef = tiEmiRef;
    }

    /**
     * @return the tiEmpPro
     */
    public String getTiEmpPro() {
        return tiEmpPro;
    }

    /**
     * @param tiEmpPro the tiEmpPro to set
     */
    public void setTiEmpPro(String tiEmpPro) {
        this.tiEmpPro = tiEmpPro;
    }

    /**
     * @return the existeDoc
     */
    public String getExisteDoc() {
        return existeDoc;
    }

    /**
     * @param existeDoc the existeDoc to set
     */
    public void setExisteDoc(String existeDoc) {
        this.existeDoc = existeDoc;
    }

    /**
     * @return the existeAnexo
     */
    public String getExisteAnexo() {
        return existeAnexo;
    }

    /**
     * @param existeAnexo the existeAnexo to set
     */
    public void setExisteAnexo(String existeAnexo) {
        this.existeAnexo = existeAnexo;
    }

    /**
     * @return the deDocSig
     */
    public String getDeDocSig() {
        return deDocSig;
    }

    /**
     * @param deDocSig the deDocSig to set
     */
    public void setDeDocSig(String deDocSig) {
        this.deDocSig = deDocSig;
    }

    /**
     * @return the feExp
     */
    public String getFeExp() {
        return feExp;
    }

    /**
     * @param feExp the feExp to set
     */
    public void setFeExp(String feExp) {
        this.feExp = feExp;
    }

    /**
     * @return the feExpCorta
     */
    public String getFeExpCorta() {
        return feExpCorta;
    }

    /**
     * @param feExpCorta the feExpCorta to set
     */
    public void setFeExpCorta(String feExpCorta) {
        this.feExpCorta = feExpCorta;
    }

    /**
     * @return the coProceso
     */
    public String getCoProceso() {
        return coProceso;
    }

    /**
     * @param coProceso the coProceso to set
     */
    public void setCoProceso(String coProceso) {
        this.coProceso = coProceso;
    }

    /**
     * @return the deProceso
     */
    public String getDeProceso() {
        return deProceso;
    }

    /**
     * @param deProceso the deProceso to set
     */
    public void setDeProceso(String deProceso) {
        this.deProceso = deProceso;
    }

    /**
     * @return the coUseMod
     */
    public String getCoUseMod() {
        return coUseMod;
    }

    /**
     * @param coUseMod the coUseMod to set
     */
    public void setCoUseMod(String coUseMod) {
        this.coUseMod = coUseMod;
    }

    /**
     * @return the coUseCre
     */
    public String getCoUseCre() {
        return coUseCre;
    }

    /**
     * @param coUseCre the coUseCre to set
     */
    public void setCoUseCre(String coUseCre) {
        this.coUseCre = coUseCre;
    }

    /**
     * @return the nuSecuenciaFirma
     */
    public String getNuSecuenciaFirma() {
        return nuSecuenciaFirma;
    }

    /**
     * @param nuSecuenciaFirma the nuSecuenciaFirma to set
     */
    public void setNuSecuenciaFirma(String nuSecuenciaFirma) {
        this.nuSecuenciaFirma = nuSecuenciaFirma;
    }

    /**
     * @return the msjResult
     */
    public String getMsjResult() {
        return msjResult;
    }

    /**
     * @param msjResult the msjResult to set
     */
    public void setMsjResult(String msjResult) {
        this.msjResult = msjResult;
    }

    /**
     * @return the coPrioridad
     */
    public String getCoPrioridad() {
        return coPrioridad;
    }

    /**
     * @param coPrioridad the coPrioridad to set
     */
    public void setCoPrioridad(String coPrioridad) {
        this.coPrioridad = coPrioridad;
    }

    /**
     * @return the inFirmaAnexo
     */
    public String getInFirmaAnexo() {
        return inFirmaAnexo;
    }

    /**
     * @param inFirmaAnexo the inFirmaAnexo to set
     */
    public void setInFirmaAnexo(String inFirmaAnexo) {
        this.inFirmaAnexo = inFirmaAnexo;
    }

    /**
     * @return the docEstadoMsj
     */
    public String getDocEstadoMsj() {
        return docEstadoMsj;
    }

    /**
     * @param docEstadoMsj the docEstadoMsj to set
     */
    public void setDocEstadoMsj(String docEstadoMsj) {
        this.docEstadoMsj = docEstadoMsj;
    }

    /**
     * @return the docObser
     */
    public String getDocObser() {
        return docObser;
    }

    /**
     * @param docObser the docObser to set
     */
    public void setDocObser(String docObser) {
        this.docObser = docObser;
    }

    /**
     * @return the tiDest
     */
    public String getTiDest() {
        return tiDest;
    }

    /**
     * @param tiDest the tiDest to set
     */
    public void setTiDest(String tiDest) {
        this.tiDest = tiDest;
    }

    /**
     * @return the tiEnvMsj
     */
    public String getTiEnvMsj() {
        return tiEnvMsj;
    }

    /**
     * @param tiEnvMsj the tiEnvMsj to set
     */
    public void setTiEnvMsj(String tiEnvMsj) {
        this.tiEnvMsj = tiEnvMsj;
    }

    /**
     * @return the recursoenvio
     */
    public String getRecursoenvio() {
        return recursoenvio;
    }

    /**
     * @param recursoenvio the recursoenvio to set
     */
    public void setRecursoenvio(String recursoenvio) {
        this.recursoenvio = recursoenvio;
    }

    /**
     * @return the deObsDoc
     */
    public String getDeObsDoc() {
        return deObsDoc;
    }

    /**
     * @param deObsDoc the deObsDoc to set
     */
    public void setDeObsDoc(String deObsDoc) {
        this.deObsDoc = deObsDoc;
    }

    /**
     * @return the nEnviaCorreo
     */
    public String getnEnviaCorreo() {
        return nEnviaCorreo;
    }

    /**
     * @param nEnviaCorreo the nEnviaCorreo to set
     */
    public void setnEnviaCorreo(String nEnviaCorreo) {
        this.nEnviaCorreo = nEnviaCorreo;
    }

    /**
     * @return the nuEmiProyecto
     */
    public String getNuEmiProyecto() {
        return nuEmiProyecto;
    }

    /**
     * @param nuEmiProyecto the nuEmiProyecto to set
     */
    public void setNuEmiProyecto(String nuEmiProyecto) {
        this.nuEmiProyecto = nuEmiProyecto;
    }

    /**
     * @return the nuAnnProyecto
     */
    public String getNuAnnProyecto() {
        return nuAnnProyecto;
    }

    /**
     * @param nuAnnProyecto the nuAnnProyecto to set
     */
    public void setNuAnnProyecto(String nuAnnProyecto) {
        this.nuAnnProyecto = nuAnnProyecto;
    }

    /**
     * @return the nuAneProyecto
     */
    public String getNuAneProyecto() {
        return nuAneProyecto;
    }

    /**
     * @param nuAneProyecto the nuAneProyecto to set
     */
    public void setNuAneProyecto(String nuAneProyecto) {
        this.nuAneProyecto = nuAneProyecto;
    }

    public String getTiEmiExp() {
        return tiEmiExp;
    }

    public void setTiEmiExp(String tiEmiExp) {
        this.tiEmiExp = tiEmiExp;
    }

    public String getNuOriEmi() {
        return nuOriEmi;
    }

    public void setNuOriEmi(String nuOriEmi) {
        this.nuOriEmi = nuOriEmi;
    }
    
    
}
