/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author wcutipa
 */
public class DocRespuestaBean {
    private String retval; 
    private String nuAnn; 
    private String nuEmi; 
    private String nuAne; 
    private String noDoc; 
    private String noFirma; 
    private String noUrl; 

    private String tieneWord;
    private String usuario;
    private String tipoDocumentoPrincipal;

    public String getRetval() {
        return retval;
    }

    public void setRetval(String retval) {
        this.retval = retval;
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

    public String getNoDoc() {
        return noDoc;
    }

    public void setNoDoc(String noDoc) {
        this.noDoc = noDoc;
    }

    public String getNoFirma() {
        return noFirma;
    }

    public void setNoFirma(String noFirma) {
        this.noFirma = noFirma;
    }

    public String getNoUrl() {
        return noUrl;
    }

    public void setNoUrl(String noUrl) {
        this.noUrl = noUrl;
    }

    public String getNuAne() {
        return nuAne;
    }

    public void setNuAne(String nuAne) {
        this.nuAne = nuAne;
    }

    /**
     * @return the tieneWord
     */
    public String getTieneWord() {
        return tieneWord;
    }

    /**
     * @param tieneWord the tieneWord to set
     */
    public void setTieneWord(String tieneWord) {
        this.tieneWord = tieneWord;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the tipoDocumentoPrincipal
     */
    public String getTipoDocumentoPrincipal() {
        return tipoDocumentoPrincipal;
    }

    /**
     * @param tipoDocumentoPrincipal the tipoDocumentoPrincipal to set
     */
    public void setTipoDocumentoPrincipal(String tipoDocumentoPrincipal) {
        this.tipoDocumentoPrincipal = tipoDocumentoPrincipal;
    }
    
    
    
}
