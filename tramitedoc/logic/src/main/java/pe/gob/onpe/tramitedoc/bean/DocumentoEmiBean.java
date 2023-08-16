/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author ecueva
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoEmiBean {
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

    private String nuDes;
    String coTipoDocMsj;//Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
    /*--21/08/19 HPB Devolucion Doc a Oficina--*/
    private String coEmpDev;
    private String feDevOfi;
    private String obsDev;
    private String codigosDestinos;
    private String feEnvOfi;
    /*--21/08/19 HPB Devolucion Doc a Oficina--*/
    
    private String obsMpv;/*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
    /*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
    private String tiEmiExp;
    private String nuOriEmi;
    private String coGruExp;
    /*HPB 11/02/2020 - Fin - Requerimiento Remitente expediente*/
    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    private String fePlaAte;
    private String coEstPla;
    private String inPlaAte;
    private String feAteDocEmi;
    private String existeDocPorRecibir;
    /*[HPB-02/10/20] Fin - Plazo de Atencion*/
    private String coClaveAcceso; /*ADD YPA CODIGO DE VERIFICAIÖN DOCUMENTAL*/
     private String coDepMsj; /*ADD YPA CODIGO DE VERIFICAIÖN DOCUMENTAL*/
    /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente--*/
    //private String nuExtension;
    /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente--*/
    public String getRecursoenvio() {
        return recursoenvio;
    }

    public void setRecursoenvio(String recursoenvio) {
        this.recursoenvio = recursoenvio;
    }
    
    public String getCoDepEmi() {
        return coDepEmi;
    }

    public void setCoDepEmi(String coDepEmi) {
        this.coDepEmi = coDepEmi;
    }

    public String getCoEmpEmi() {
        return coEmpEmi;
    }

    

    public void setCoEmpEmi(String coEmpEmi) {
        this.coEmpEmi = coEmpEmi;
    }

    public String getCoEmpRes() {
        return coEmpRes;
    }

    public void setCoEmpRes(String coEmpRes) {
        this.coEmpRes = coEmpRes;
    }

    public String getCoExp() {
        return coExp;
    }

    public void setCoExp(String coExp) {
        this.coExp = coExp;
    }

    public String getCoGru() {
        return coGru;
    }

    public void setCoGru(String coGru) {
        this.coGru = coGru;
    }

    public String getCoLocEmi() {
        return coLocEmi;
    }

    public void setCoLocEmi(String coLocEmi) {
        this.coLocEmi = coLocEmi;
    }

    public String getCoOtrOriEmi() {
        return coOtrOriEmi;
    }

    public void setCoOtrOriEmi(String coOtrOriEmi) {
        this.coOtrOriEmi = coOtrOriEmi;
    }

    public String getCoTipDocAdm() {
        return coTipDocAdm;
    }

    public void setCoTipDocAdm(String coTipDocAdm) {
        this.coTipDocAdm = coTipDocAdm;
    }

    public String getDeAne() {
        return deAne;
    }

    public void setDeAne(String deAne) {
        this.deAne = deAne;
    }

    public String getDeAsu() {
        return deAsu;
    }

    public void setDeAsu(String deAsu) {
        this.deAsu = deAsu;
    }

    public String getDeAsuM() {
        return deAsuM;
    }

    public void setDeAsuM(String deAsuM) {
        this.deAsuM = deAsuM;
    }

    public String getDeDepEmi() {
        return deDepEmi;
    }

    public void setDeDepEmi(String deDepEmi) {
        this.deDepEmi = deDepEmi;
    }

    public String getDeDesEst() {
        return deDesEst;
    }

    public void setDeDesEst(String deDesEst) {
        this.deDesEst = deDesEst;
    }

    public String getDeEmiRef() {
        return deEmiRef;
    }

    public void setDeEmiRef(String deEmiRef) {
        this.deEmiRef = deEmiRef;
    }

    public String getDeEmpEmi() {
        return deEmpEmi;
    }

    public void setDeEmpEmi(String deEmpEmi) {
        this.deEmpEmi = deEmpEmi;
    }

    public String getDeEmpPro() {
        return deEmpPro;
    }

    public void setDeEmpPro(String deEmpPro) {
        this.deEmpPro = deEmpPro;
    }

    public String getDeEmpRes() {
        return deEmpRes;
    }

    public void setDeEmpRes(String deEmpRes) {
        this.deEmpRes = deEmpRes;
    }

    public String getDeEsDocEmi() {
        return deEsDocEmi;
    }

    public void setDeEsDocEmi(String deEsDocEmi) {
        this.deEsDocEmi = deEsDocEmi;
    }

    public String getDeLocEmi() {
        return deLocEmi;
    }

    public void setDeLocEmi(String deLocEmi) {
        this.deLocEmi = deLocEmi;
    }

    public String getDeMes() {
        return deMes;
    }

    public void setDeMes(String deMes) {
        this.deMes = deMes;
    }

    public String getDeOriEmi() {
        return deOriEmi;
    }

    public void setDeOriEmi(String deOriEmi) {
        this.deOriEmi = deOriEmi;
    }

    public String getDeTipDocAdm() {
        return deTipDocAdm;
    }

    public void setDeTipDocAdm(String deTipDocAdm) {
        this.deTipDocAdm = deTipDocAdm;
    }

    public String getDeTipEmi() {
        return deTipEmi;
    }

    public void setDeTipEmi(String deTipEmi) {
        this.deTipEmi = deTipEmi;
    }

    public String getEsDocEmi() {
        return esDocEmi;
    }

    public void setEsDocEmi(String esDocEmi) {
        this.esDocEmi = esDocEmi;
    }

    public String getFeEmi() {
        return feEmi;
    }

    public void setFeEmi(String feEmi) {
        this.feEmi = feEmi;
    }

    public String getFeEmiCorta() {
        return feEmiCorta;
    }

    public void setFeEmiCorta(String feEmiCorta) {
        this.feEmiCorta = feEmiCorta;
    }

    public String getInExpe() {
        return inExpe;
    }

    public void setInExpe(String inExpe) {
        this.inExpe = inExpe;
    }

    public String getInOficio() {
        return inOficio;
    }

    public void setInOficio(String inOficio) {
        this.inOficio = inOficio;
    }

    public String getNuAnn() {
        return nuAnn;
    }

    public void setNuAnn(String nuAnn) {
        this.nuAnn = nuAnn;
    }

    public String getNuAnnExp() {
        return nuAnnExp;
    }

    public void setNuAnnExp(String nuAnnExp) {
        this.nuAnnExp = nuAnnExp;
    }

    public String getNuCorDoc() {
        return nuCorDoc;
    }

    public void setNuCorDoc(String nuCorDoc) {
        this.nuCorDoc = nuCorDoc;
    }

    public String getNuCorEmi() {
        return nuCorEmi;
    }

    public void setNuCorEmi(String nuCorEmi) {
        this.nuCorEmi = nuCorEmi;
    }

    public String getNuDetExp() {
        return nuDetExp;
    }

    public void setNuDetExp(String nuDetExp) {
        this.nuDetExp = nuDetExp;
    }

    public String getNuDiaAte() {
        return nuDiaAte;
    }

    public void setNuDiaAte(String nuDiaAte) {
        this.nuDiaAte = nuDiaAte;
    }

    public String getNuDniEmi() {
        return nuDniEmi;
    }

    public void setNuDniEmi(String nuDniEmi) {
        this.nuDniEmi = nuDniEmi;
    }

    public String getNuDoc() {
        return nuDoc;
    }

    public void setNuDoc(String nuDoc) {
        this.nuDoc = nuDoc;
    }

    public String getNuDocEmi() {
        return nuDocEmi;
    }

    public void setNuDocEmi(String nuDocEmi) {
        this.nuDocEmi = nuDocEmi;
    }

    public String getNuEmi() {
        return nuEmi;
    }

    public void setNuEmi(String nuEmi) {
        this.nuEmi = nuEmi;
    }

    public String getNuExpediente() {
        return nuExpediente;
    }

    public void setNuExpediente(String nuExpediente) {
        this.nuExpediente = nuExpediente;
    }

    public String getNuRucEmi() {
        return nuRucEmi;
    }

    public void setNuRucEmi(String nuRucEmi) {
        this.nuRucEmi = nuRucEmi;
    }

    public String getNuSecExp() {
        return nuSecExp;
    }

    public void setNuSecExp(String nuSecExp) {
        this.nuSecExp = nuSecExp;
    }

    public String getTiCap() {
        return tiCap;
    }

    public void setTiCap(String tiCap) {
        this.tiCap = tiCap;
    }

    public String getTiEmi() {
        return tiEmi;
    }

    public void setTiEmi(String tiEmi) {
        this.tiEmi = tiEmi;
    }

    public String getTiEmiRef() {
        return tiEmiRef;
    }

    public void setTiEmiRef(String tiEmiRef) {
        this.tiEmiRef = tiEmiRef;
    }

    public String getTiEmpPro() {
        return tiEmpPro;
    }

    public void setTiEmpPro(String tiEmpPro) {
        this.tiEmpPro = tiEmpPro;
    }

    public String getExisteDoc() {
        return existeDoc;
    }

    public void setExisteDoc(String existeDoc) {
        this.existeDoc = existeDoc;
    }

    public String getExisteAnexo() {
        return existeAnexo;
    }

    public void setExisteAnexo(String existeAnexo) {
        this.existeAnexo = existeAnexo;
    }

    public String getDeDocSig() {
        return deDocSig;
    }

    public void setDeDocSig(String deDocSig) {
        this.deDocSig = deDocSig;
    }

    public String getFeExp() {
        return feExp;
    }

    public void setFeExp(String feExp) {
        this.feExp = feExp;
    }

    public String getFeExpCorta() {
        return feExpCorta;
    }

    public void setFeExpCorta(String feExpCorta) {
        this.feExpCorta = feExpCorta;
    }

    public String getCoProceso() {
        return coProceso;
    }

    public void setCoProceso(String coProceso) {
        this.coProceso = coProceso;
    }

    public String getDeProceso() {
        return deProceso;
    }

    public void setDeProceso(String deProceso) {
        this.deProceso = deProceso;
    }

    public String getCoUseMod() {
        return coUseMod;
    }

    public void setCoUseMod(String coUseMod) {
        this.coUseMod = coUseMod;
    }

    public String getCoUseCre() {
        return coUseCre;
    }

    public void setCoUseCre(String coUseCre) {
        this.coUseCre = coUseCre;
    }

    public String getNuSecuenciaFirma() {
        return nuSecuenciaFirma;
    }

    public void setNuSecuenciaFirma(String nuSecuenciaFirma) {
        this.nuSecuenciaFirma = nuSecuenciaFirma;
    }

    public String getMsjResult() {
        return msjResult;
    }

    public void setMsjResult(String msjResult) {
        this.msjResult = msjResult;
    }

    public String getCoPrioridad() {
        return coPrioridad;
    }

    public void setCoPrioridad(String coPrioridad) {
        this.coPrioridad = coPrioridad;
    }

    public String getInFirmaAnexo() {
        return inFirmaAnexo;
    }

    public void setInFirmaAnexo(String inFirmaAnexo) {
        this.inFirmaAnexo = inFirmaAnexo;
    }

    public String getDocEstadoMsj() {
        return docEstadoMsj;
    }

    public void setDocEstadoMsj(String docEstadoMsj) {
        this.docEstadoMsj = docEstadoMsj;
    }

    public String getDocObser() {
        return docObser;
    }

    public void setDocObser(String docObser) {
        this.docObser = docObser;
    }

    public String getTiDest() {
        return tiDest;
    }

    public void setTiDest(String tiDest) {
        this.tiDest = tiDest;
    }

    public String getTiEnvMsj() {
        return tiEnvMsj;
    }

    public void setTiEnvMsj(String tiEnvMsj) {
        this.tiEnvMsj = tiEnvMsj;
    }

    public String getDeObsDoc() {
        return deObsDoc;
    }

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

    public String getNuDes() {
        return nuDes;
    }

    public void setNuDes(String nuDes) {
        this.nuDes = nuDes;
    }

    public String getCoTipoDocMsj() {
        return coTipoDocMsj;
    }

    public void setCoTipoDocMsj(String coTipoDocMsj) {
        this.coTipoDocMsj = coTipoDocMsj;
    }

    public String getCoEmpDev() {
        return coEmpDev;
    }

    public void setCoEmpDev(String coEmpDev) {
        this.coEmpDev = coEmpDev;
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

    public String getCodigosDestinos() {
        return codigosDestinos;
    }

    public void setCodigosDestinos(String codigosDestinos) {
        this.codigosDestinos = codigosDestinos;
    }

    public String getFeEnvOfi() {
        return feEnvOfi;
    }

    public void setFeEnvOfi(String feEnvOfi) {
        this.feEnvOfi = feEnvOfi;
    }

    public String getObsMpv() {
        return obsMpv;
    }

    public void setObsMpv(String obsMpv) {
        this.obsMpv = obsMpv;
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

    public String getCoGruExp() {
        return coGruExp;
    }

    public void setCoGruExp(String coGruExp) {
        this.coGruExp = coGruExp;
    }

    public String getFePlaAte() {
        return fePlaAte;
    }

    public void setFePlaAte(String fePlaAte) {
        this.fePlaAte = fePlaAte;
    }

    public String getCoEstPla() {
        return coEstPla;
    }

    public void setCoEstPla(String coEstPla) {
        this.coEstPla = coEstPla;
    }

    public String getInPlaAte() {
        return inPlaAte;
    }

    public void setInPlaAte(String inPlaAte) {
        this.inPlaAte = inPlaAte;
    }

    public String getFeAteDocEmi() {
        return feAteDocEmi;
    }

    public void setFeAteDocEmi(String feAteDocEmi) {
        this.feAteDocEmi = feAteDocEmi;
    }

    public String getExisteDocPorRecibir() {
        return existeDocPorRecibir;
    }

    public void setExisteDocPorRecibir(String existeDocPorRecibir) {
        this.existeDocPorRecibir = existeDocPorRecibir;
    }

    /**
     * @return the coClaveAcceso
     */
    public String getCoClaveAcceso() {
        return coClaveAcceso;
    }

    /**
     * @param coClaveAcceso the coClaveAcceso to set
     */
    public void setCoClaveAcceso(String coClaveAcceso) {
        this.coClaveAcceso = coClaveAcceso;
    }

    /**
     * @return the coDepMsj
     */
    public String getCoDepMsj() {
        return coDepMsj;
    }

    /**
     * @param coDepMsj the coDepMsj to set
     */
    public void setCoDepMsj(String coDepMsj) {
        this.coDepMsj = coDepMsj;
    }
    /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente--*/
//    public String getNuExtension() {
//        return nuExtension;
//    }
//
//    public void setNuExtension(String nuExtension) {
//        this.nuExtension = nuExtension;
//    }
    /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente--*/
    
}
