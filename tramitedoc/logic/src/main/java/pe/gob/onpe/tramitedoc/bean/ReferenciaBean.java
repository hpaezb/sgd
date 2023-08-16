/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author ECueva
 */
public class ReferenciaBean {
    private String nuAnn;
    private String nuEmi;
    private String nuDes;
    private String nuCorEmi;
    private String coRef;
    private String nuAnnRef;
    private String nuEmiRef;
    private String nuDesRef;
    private String coUseCre;
    private String feUseCre;
    private String liNuDoc;
    private String liTipDoc;
    private String deTipoDoc;
    private String deDepEmi;
    private String deDepDes;
    private String feEmi;
    private String feEmiCorta;
    private String deEsDocEmi;
    private String tipDocRef;// emitido emi, recepcionado rec.
    private String coTipDocAdm;
    private String accionBd;
    private String nuAnnExp;
    private String nuSecExp;
    private String inPlaAte;/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    private String inResPlaAte;/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    //private String deDocSig;
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    private String tiEmi;
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
    public ReferenciaBean() {
    }

    /*public String getDeDocSig() {
        return deDocSig;
    }

    public void setDeDocSig(String deDocSig) {
        this.deDocSig = deDocSig;
    }*/

    public String getCoTipDocAdm() {
        return coTipDocAdm;
    }

    public String getAccionBd() {
        return accionBd;
    }

    public void setAccionBd(String accionBd) {
        this.accionBd = accionBd;
    }

    public void setCoTipDocAdm(String coTipDocAdm) {
        this.coTipDocAdm = coTipDocAdm;
    }    
    public String getTipDocRef() {
        return tipDocRef;
    }

    public void setTipDocRef(String tipDocRef) {
        this.tipDocRef = tipDocRef;
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

    public String getLiNuDoc() {
        return liNuDoc;
    }

    public void setLiNuDoc(String liNuDoc) {
        this.liNuDoc = liNuDoc;
    }

    public String getLiTipDoc() {
        return liTipDoc;
    }

    public void setLiTipDoc(String liTipDoc) {
        this.liTipDoc = liTipDoc;
    }

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

    public String getNuDes() {
        return nuDes;
    }

    public void setNuDes(String nuDes) {
        this.nuDes = nuDes;
    }

    public String getCoRef() {
        return coRef;
    }

    public void setCoRef(String coRef) {
        this.coRef = coRef;
    }

    public String getNuAnnRef() {
        return nuAnnRef;
    }

    public void setNuAnnRef(String nuAnnRef) {
        this.nuAnnRef = nuAnnRef;
    }

    public String getNuEmiRef() {
        return nuEmiRef;
    }

    public void setNuEmiRef(String nuEmiRef) {
        this.nuEmiRef = nuEmiRef;
    }

    public String getNuDesRef() {
        return nuDesRef;
    }

    public void setNuDesRef(String nuDesRef) {
        this.nuDesRef = nuDesRef;
    }

    public String getCoUseCre() {
        return coUseCre;
    }

    public void setCoUseCre(String coUseCre) {
        this.coUseCre = coUseCre;
    }

    public String getFeUseCre() {
        return feUseCre;
    }

    public void setFeUseCre(String feUseCre) {
        this.feUseCre = feUseCre;
    }

    public String getDeDepEmi() {
        return deDepEmi;
    }

    public void setDeDepEmi(String deDepEmi) {
        this.deDepEmi = deDepEmi;
    }

    public String getDeDepDes() {
        return deDepDes;
    }

    public void setDeDepDes(String deDepDes) {
        this.deDepDes = deDepDes;
    }

    public String getDeTipoDoc() {
        return deTipoDoc;
    }

    public void setDeTipoDoc(String deTipoDoc) {
        this.deTipoDoc = deTipoDoc;
    }

    public String getNuCorEmi() {
        return nuCorEmi;
    }

    public void setNuCorEmi(String nuCorEmi) {
        this.nuCorEmi = nuCorEmi;
    }

    public String getDeEsDocEmi() {
        return deEsDocEmi;
    }

    public void setDeEsDocEmi(String deEsDocEmi) {
        this.deEsDocEmi = deEsDocEmi;
    }

    public String getNuAnnExp() {
        return nuAnnExp;
    }

    public void setNuAnnExp(String nuAnnExp) {
        this.nuAnnExp = nuAnnExp;
    }

    public String getNuSecExp() {
        return nuSecExp;
    }

    public void setNuSecExp(String nuSecExp) {
        this.nuSecExp = nuSecExp;
    }

    public String getInPlaAte() {
        return inPlaAte;
    }

    public void setInPlaAte(String inPlaAte) {
        this.inPlaAte = inPlaAte;
    }

    public String getInResPlaAte() {
        return inResPlaAte;
    }

    public void setInResPlaAte(String inResPlaAte) {
        this.inResPlaAte = inResPlaAte;
    } 
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    public String getTiEmi() {
        return tiEmi;
    }

    public void setTiEmi(String tiEmi) {
        this.tiEmi = tiEmi;
    }
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
}
