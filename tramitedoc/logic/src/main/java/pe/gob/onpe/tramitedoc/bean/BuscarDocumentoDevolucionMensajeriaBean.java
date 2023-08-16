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
public class BuscarDocumentoDevolucionMensajeriaBean {
    private String coGrupo;
    private String coDependencia;
    private String coEmpleado;    
    private String tiAcceso; 
    private boolean esIncluyeFiltro;
    private String coAnnio;
    private String coAnnioEnvio;    
    private String coAnnioDev;    
    private String coEstadoDoc;
    private String coTipoDoc;
    private String coTipoEmisor;
    private String coRemitente;
    private String coDepDestino;
    private String esFiltroFecha;
    private String feEmiIni;
    private String feEmiFin;
    private String coLocal;
    private String inCambioEst;
    private String coLocEmi;
    private String coDepEmi;
    private String coDepOriRec;//codigo de dependencia Origen de recepcion de documentos externos.
    private String inMesaPartes;

    /***************************************/
    private String tipoBusqueda;//0 Filtrar, 1 Buscar
    /***************************************/    
    
    //busqueda desde tabla hacia BD
    private String busNumDoc;
    private String busAsunto;
    private String busNumExpediente;

    private String FormatoReporte;
    private String RutaReporteJasper;
    private String coDependenciaBusca;
    private String esFiltroFechaEnvMsj;
    private String esFiltroFechaDevOfi;
    private String feEmiIniEnvMSJ;
    private String feEmiFinEnvMSJ;

    private String feEmiIniDevOfi;
    private String feEmiFinDevOfi;
    
    private String busObsDev;
    private String busDesti;
    
    private String numGuia;
    private String coTipoEnvMsj;
    private String coTipoMsj;  

    public BuscarDocumentoDevolucionMensajeriaBean() {
        this.tipoBusqueda = "0";
        this.esFiltroFecha = "0";
        this.esIncluyeFiltro = false;           
    }

    
    public String getCoGrupo() {
        return coGrupo;
    }

    public void setCoGrupo(String coGrupo) {
        this.coGrupo = coGrupo;
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

    public boolean isEsIncluyeFiltro() {
        return esIncluyeFiltro;
    }

    public void setEsIncluyeFiltro(boolean esIncluyeFiltro) {
        this.esIncluyeFiltro = esIncluyeFiltro;
    }

    public String getCoAnnio() {
        return coAnnio;
    }

    public void setCoAnnio(String coAnnio) {
        this.coAnnio = coAnnio;
    }

    public String getCoAnnioEnvio() {
        return coAnnioEnvio;
    }

    public void setCoAnnioEnvio(String coAnnioEnvio) {
        this.coAnnioEnvio = coAnnioEnvio;
    }

    public String getCoEstadoDoc() {
        return coEstadoDoc;
    }

    public void setCoEstadoDoc(String coEstadoDoc) {
        this.coEstadoDoc = coEstadoDoc;
    }

    public String getCoTipoDoc() {
        return coTipoDoc;
    }

    public void setCoTipoDoc(String coTipoDoc) {
        this.coTipoDoc = coTipoDoc;
    }

    public String getCoTipoEmisor() {
        return coTipoEmisor;
    }

    public void setCoTipoEmisor(String coTipoEmisor) {
        this.coTipoEmisor = coTipoEmisor;
    }

    public String getCoRemitente() {
        return coRemitente;
    }

    public void setCoRemitente(String coRemitente) {
        this.coRemitente = coRemitente;
    }

    public String getCoDepDestino() {
        return coDepDestino;
    }

    public void setCoDepDestino(String coDepDestino) {
        this.coDepDestino = coDepDestino;
    }

    public String getEsFiltroFecha() {
        return esFiltroFecha;
    }

    public void setEsFiltroFecha(String esFiltroFecha) {
        this.esFiltroFecha = esFiltroFecha;
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

    public String getCoLocal() {
        return coLocal;
    }

    public void setCoLocal(String coLocal) {
        this.coLocal = coLocal;
    }

    public String getInCambioEst() {
        return inCambioEst;
    }

    public void setInCambioEst(String inCambioEst) {
        this.inCambioEst = inCambioEst;
    }

    public String getCoLocEmi() {
        return coLocEmi;
    }

    public void setCoLocEmi(String coLocEmi) {
        this.coLocEmi = coLocEmi;
    }

    public String getCoDepEmi() {
        return coDepEmi;
    }

    public void setCoDepEmi(String coDepEmi) {
        this.coDepEmi = coDepEmi;
    }

    public String getCoDepOriRec() {
        return coDepOriRec;
    }

    public void setCoDepOriRec(String coDepOriRec) {
        this.coDepOriRec = coDepOriRec;
    }

    public String getInMesaPartes() {
        return inMesaPartes;
    }

    public void setInMesaPartes(String inMesaPartes) {
        this.inMesaPartes = inMesaPartes;
    }

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public String getBusNumDoc() {
        return busNumDoc;
    }

    public void setBusNumDoc(String busNumDoc) {
        this.busNumDoc = busNumDoc;
    }

    public String getBusAsunto() {
        return busAsunto;
    }

    public void setBusAsunto(String busAsunto) {
        this.busAsunto = busAsunto;
    }

    public String getBusNumExpediente() {
        return busNumExpediente;
    }

    public void setBusNumExpediente(String busNumExpediente) {
        this.busNumExpediente = busNumExpediente;
    }

    public String getFormatoReporte() {
        return FormatoReporte;
    }

    public void setFormatoReporte(String FormatoReporte) {
        this.FormatoReporte = FormatoReporte;
    }

    public String getRutaReporteJasper() {
        return RutaReporteJasper;
    }

    public void setRutaReporteJasper(String RutaReporteJasper) {
        this.RutaReporteJasper = RutaReporteJasper;
    }

    public String getCoDependenciaBusca() {
        return coDependenciaBusca;
    }

    public void setCoDependenciaBusca(String coDependenciaBusca) {
        this.coDependenciaBusca = coDependenciaBusca;
    }

    public String getEsFiltroFechaEnvMsj() {
        return esFiltroFechaEnvMsj;
    }

    public void setEsFiltroFechaEnvMsj(String esFiltroFechaEnvMsj) {
        this.esFiltroFechaEnvMsj = esFiltroFechaEnvMsj;
    }

    public String getFeEmiIniEnvMSJ() {
        return feEmiIniEnvMSJ;
    }

    public void setFeEmiIniEnvMSJ(String feEmiIniEnvMSJ) {
        this.feEmiIniEnvMSJ = feEmiIniEnvMSJ;
    }

    public String getFeEmiFinEnvMSJ() {
        return feEmiFinEnvMSJ;
    }

    public void setFeEmiFinEnvMSJ(String feEmiFinEnvMSJ) {
        this.feEmiFinEnvMSJ = feEmiFinEnvMSJ;
    }

    public String getNumGuia() {
        return numGuia;
    }

    public void setNumGuia(String numGuia) {
        this.numGuia = numGuia;
    }

    public String getCoTipoEnvMsj() {
        return coTipoEnvMsj;
    }

    public void setCoTipoEnvMsj(String coTipoEnvMsj) {
        this.coTipoEnvMsj = coTipoEnvMsj;
    }

    public String getCoTipoMsj() {
        return coTipoMsj;
    }

    public void setCoTipoMsj(String coTipoMsj) {
        this.coTipoMsj = coTipoMsj;
    }

    public String getBusObsDev() {
        return busObsDev;
    }

    public void setBusObsDev(String busObsDev) {
        this.busObsDev = busObsDev;
    }

    public String getBusDesti() {
        return busDesti;
    }

    public void setBusDesti(String busDesti) {
        this.busDesti = busDesti;
    }

    public String getCoAnnioDev() {
        return coAnnioDev;
    }

    public void setCoAnnioDev(String coAnnioDev) {
        this.coAnnioDev = coAnnioDev;
    }

    public String getFeEmiIniDevOfi() {
        return feEmiIniDevOfi;
    }

    public void setFeEmiIniDevOfi(String feEmiIniDevOfi) {
        this.feEmiIniDevOfi = feEmiIniDevOfi;
    }

    public String getFeEmiFinDevOfi() {
        return feEmiFinDevOfi;
    }

    public void setFeEmiFinDevOfi(String feEmiFinDevOfi) {
        this.feEmiFinDevOfi = feEmiFinDevOfi;
    }

    public String getEsFiltroFechaDevOfi() {
        return esFiltroFechaDevOfi;
    }

    public void setEsFiltroFechaDevOfi(String esFiltroFechaDevOfi) {
        this.esFiltroFechaDevOfi = esFiltroFechaDevOfi;
    }
    
    
    
}
