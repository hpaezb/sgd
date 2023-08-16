/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author ecueva
 */
public class DocumentoEmiPersConsulBean extends ReporteBean{
    private String nuAnn;
    private String nuEmi;
    private String tiEmi;
    private String estadoDoc;
    private String tipoDoc;
    private String feEmi;
    private String feEmiCorta;
    private String nuCorEmi;
    private String nuDoc;
    private String deAsu;
    private String coDepDestino;
    private String deDepDestino;
    private String coEmpDestino;
    private String deEmpDestino;
    private String coExpediente;
    private String deEmpElaboro;
    private String deDepRef;
    private String coDepRef;
    private String existeDoc;
    private String existeAnexo;
    private String deDepEmi;
    private String deLocEmi;
    private String deEmpFirmo;
    private String nuDiaAte;
    private String deEsDocEmi;
    private String nuExpediente;
    
    
    private String coEmpleado;            
    //Filtro
    private String feEmiIni;
    private String feEmiFin;
    private String esFiltroFecha;
    /***************************************/
    private String tipoBusqueda;//0 Filtrar, 1 Buscar
    /***************************************/    
    //Buscar
    private boolean esIncluyeFiltro;
    
    /*-----Hermes 05/09/2018-----*/
    private String coDepRemite;
    private String coAnnio;
    private String coEmpFirmo;
    private String coDepEmite;
    /*---------------------------*/
    
    private String esDocEmi;//Hermes Log 28/05/2019
    
    public DocumentoEmiPersConsulBean() {
    }

    public String getDeDepEmi() {
        return deDepEmi;
    }

    public void setDeDepEmi(String deDepEmi) {
        this.deDepEmi = deDepEmi;
    }

    public String getNuExpediente() {
        return nuExpediente;
    }

    public void setNuExpediente(String nuExpediente) {
        this.nuExpediente = nuExpediente;
    }
    
    public String getDeLocEmi() {
        return deLocEmi;
    }

    public void setDeLocEmi(String deLocEmi) {
        this.deLocEmi = deLocEmi;
    }

    public String getDeEmpFirmo() {
        return deEmpFirmo;
    }

    public void setDeEmpFirmo(String deEmpFirmo) {
        this.deEmpFirmo = deEmpFirmo;
    }

    public String getNuDiaAte() {
        return nuDiaAte;
    }

    public void setNuDiaAte(String nuDiaAte) {
        this.nuDiaAte = nuDiaAte;
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

    public String getCoDepRef() {
        return coDepRef;
    }

    public void setCoDepRef(String coDepRef) {
        this.coDepRef = coDepRef;
    }

    public String getDeDepRef() {
        return deDepRef;
    }

    public void setDeDepRef(String deDepRef) {
        this.deDepRef = deDepRef;
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

    public String getTiEmi() {
        return tiEmi;
    }

    public void setTiEmi(String tiEmi) {
        this.tiEmi = tiEmi;
    }

    public String getEstadoDoc() {
        return estadoDoc;
    }

    public void setEstadoDoc(String estadoDoc) {
        this.estadoDoc = estadoDoc;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
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

    public String getNuCorEmi() {
        return nuCorEmi;
    }

    public void setNuCorEmi(String nuCorEmi) {
        this.nuCorEmi = nuCorEmi;
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

    public String getCoDepDestino() {
        return coDepDestino;
    }

    public void setCoDepDestino(String coDepDestino) {
        this.coDepDestino = coDepDestino;
    }

    public String getDeDepDestino() {
        return deDepDestino;
    }

    public void setDeDepDestino(String deDepDestino) {
        this.deDepDestino = deDepDestino;
    }

    public String getCoEmpDestino() {
        return coEmpDestino;
    }

    public void setCoEmpDestino(String coEmpDestino) {
        this.coEmpDestino = coEmpDestino;
    }

    public String getDeEmpDestino() {
        return deEmpDestino;
    }

    public void setDeEmpDestino(String deEmpDestino) {
        this.deEmpDestino = deEmpDestino;
    }

    public String getCoExpediente() {
        return coExpediente;
    }

    public void setCoExpediente(String coExpediente) {
        this.coExpediente = coExpediente;
    }

    public String getDeEmpElaboro() {
        return deEmpElaboro;
    }

    public void setDeEmpElaboro(String deEmpElaboro) {
        this.deEmpElaboro = deEmpElaboro;
    }

    public String getCoEmpleado() {
        return coEmpleado;
    }

    public void setCoEmpleado(String coEmpleado) {
        this.coEmpleado = coEmpleado;
    }

    public String getFeEmiIni() {
        return feEmiIni;
    }

    public void setFeEmiIni(String feEmiIni) {
        this.feEmiIni = feEmiIni;
    }

    public String getFeEmiFin() {
        return feEmiFin;
    }

    public void setFeEmiFin(String feEmiFin) {
        this.feEmiFin = feEmiFin;
    }

    public String getEsFiltroFecha() {
        return esFiltroFecha;
    }

    public void setEsFiltroFecha(String esFiltroFecha) {
        this.esFiltroFecha = esFiltroFecha;
    }

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public boolean isEsIncluyeFiltro() {
        return esIncluyeFiltro;
    }

    public void setEsIncluyeFiltro(boolean esIncluyeFiltro) {
        this.esIncluyeFiltro = esIncluyeFiltro;
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

    public String getCoDepRemite() {
        return coDepRemite;
    }

    public void setCoDepRemite(String coDepRemite) {
        this.coDepRemite = coDepRemite;
    }

    public String getCoAnnio() {
        return coAnnio;
    }

    public void setCoAnnio(String coAnnio) {
        this.coAnnio = coAnnio;
    }

    public String getCoEmpFirmo() {
        return coEmpFirmo;
    }

    public void setCoEmpFirmo(String coEmpFirmo) {
        this.coEmpFirmo = coEmpFirmo;
    }

    public String getCoDepEmite() {
        return coDepEmite;
    }

    public void setCoDepEmite(String coDepEmite) {
        this.coDepEmite = coDepEmite;
    }

    public String getEsDocEmi() {
        return esDocEmi;
    }

    public void setEsDocEmi(String esDocEmi) {
        this.esDocEmi = esDocEmi;
    }
    
    
}
