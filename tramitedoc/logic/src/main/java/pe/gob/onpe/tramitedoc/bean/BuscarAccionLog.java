/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author hpaez
 */
public class BuscarAccionLog extends ReporteBean{
    private String nuDocumentoAccion;
    private String tipoDocumentoAccion;
    private String sTipoBusqueda;//0 Filtrar, 1 Buscar,2 Buscar Referencia
    private String sCoAnnio;
    private String esFiltroFecha;
    private String sFeEmiIni;
    private String sFeEmiFin;
    
    private String coDependencia;
    private String coEmpleado;    
    private String tiAcceso; 
    private String coAnnio;    
    private String coDepDes;
    
    public BuscarAccionLog() {
         this.sTipoBusqueda = "0";
         this.esFiltroFecha = "0";
    }

    public BuscarAccionLog(String nuDocumentoAccion) {
        this.nuDocumentoAccion = nuDocumentoAccion;
    }

    public String getNuDocumentoAccion() {
        return nuDocumentoAccion;
    }

    public void setNuDocumentoAccion(String nuDocumentoAccion) {
        this.nuDocumentoAccion = nuDocumentoAccion;
    }

    public String getsTipoBusqueda() {
        return sTipoBusqueda;
    }

    public void setsTipoBusqueda(String sTipoBusqueda) {
        this.sTipoBusqueda = sTipoBusqueda;
    }

    public String getsCoAnnio() {
        return sCoAnnio;
    }

    public void setsCoAnnio(String sCoAnnio) {
        this.sCoAnnio = sCoAnnio;
    }

    public String getEsFiltroFecha() {
        return esFiltroFecha;
    }

    public void setEsFiltroFecha(String esFiltroFecha) {
        this.esFiltroFecha = esFiltroFecha;
    }

    public String getsFeEmiIni() {
        return sFeEmiIni;
    }

    public void setsFeEmiIni(String sFeEmiIni) {
        this.sFeEmiIni = sFeEmiIni;
    }

    public String getsFeEmiFin() {
        return sFeEmiFin;
    }

    public void setsFeEmiFin(String sFeEmiFin) {
        this.sFeEmiFin = sFeEmiFin;
    }

    public String getCoDependencia() {
        return coDependencia;
    }

    public void setCoDependencia(String coDependencia) {
        this.coDependencia = coDependencia;
    }

    public String getCoEmpleado() {
        return coEmpleado;
    }

    public void setCoEmpleado(String coEmpleado) {
        this.coEmpleado = coEmpleado;
    }

    public String getTiAcceso() {
        return tiAcceso;
    }

    public void setTiAcceso(String tiAcceso) {
        this.tiAcceso = tiAcceso;
    }

    public String getCoAnnio() {
        return coAnnio;
    }

    public void setCoAnnio(String coAnnio) {
        this.coAnnio = coAnnio;
    }

    public String getCoDepDes() {
        return coDepDes;
    }

    public void setCoDepDes(String coDepDes) {
        this.coDepDes = coDepDes;
    }

    public String getTipoDocumentoAccion() {
        return tipoDocumentoAccion;
    }

    public void setTipoDocumentoAccion(String tipoDocumentoAccion) {
        this.tipoDocumentoAccion = tipoDocumentoAccion;
    }
    
    
}
