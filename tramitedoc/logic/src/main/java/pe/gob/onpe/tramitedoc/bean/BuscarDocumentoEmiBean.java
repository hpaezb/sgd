/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author ecueva
 */
public class BuscarDocumentoEmiBean {
    //parametros generales
    private String sCoDependencia;
    private String sCoEmpleado;    
    private String sTiAcceso;
    //
    private String sCoAnnio;
    private String sEstadoDoc;
    private String sPrioridadDoc;
    private String sTipoDoc;
    private String sExpediente;    
    private String sRefOrigen;    
    private String sDestinatario;
    private String sElaboradoPor;
    private String esFiltroFecha;
    private String sFeEmiIni;
    private String sFeEmiFin;
    /***************************************/
    private String sTipoBusqueda;//0 Filtrar, 1 Buscar,2 Buscar Referencia
    /***************************************/
    //busqueda desde tabla hacia BD
    private String sNumCorEmision;
    private String sDeEmiReferencia;
    //private String sFeEmiIni;
    //private String sFeEmiFin;
    private String sDeTipoDocAdm;
    private String sNumDoc;
    private String sNumDocRef;
    private String sDeAsuM;
    private String sBuscDestinatario;
    //private String sBuscElaboraradoPor;    
    private String sBuscNroExpediente;
    private boolean esIncluyeFiltro;
    private String sCoAnnioBus;
    private String coTema;
    //private String sBuscEstado;
    /*-------Hermes 05/09/2018 - Consulta documentos emitidos - filtro otros(ciudadano, proveedor, etc)----*/
    private String tiDes;
    /*----------------------------------------------*/
    /*--28/08/19 HPB Devolucion Doc a Oficina--*/
    private String docEstadoMsj;
    private String sTiEnvMsj;
    /*--28/08/19 HPB Devolucion Doc a Oficina--*/
    private String coBandeja; /*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS */
    private boolean esPorVencer;/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    public String getCoTema() {
        return coTema;
    }

    public void setCoTema(String coTema) {
        this.coTema = coTema;
    }

    public BuscarDocumentoEmiBean() {
        this.sTipoBusqueda = "0";
        this.esFiltroFecha = "0";
        this.esIncluyeFiltro = false;
    }

    public String getsCoAnnioBus() {
        return sCoAnnioBus;
    }

    public void setsCoAnnioBus(String sCoAnnioBus) {
        this.sCoAnnioBus = sCoAnnioBus;
    }
    
    public boolean isEsIncluyeFiltro() {
        return esIncluyeFiltro;
    }

    public void setEsIncluyeFiltro(boolean esIncluyeFiltro) {
        this.esIncluyeFiltro = esIncluyeFiltro;
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

    public String getEsFiltroFecha() {
        return esFiltroFecha;
    }

    public void setEsFiltroFecha(String esFiltroFecha) {
        this.esFiltroFecha = esFiltroFecha;
    }

    public String getsNumDocRef() {
        return sNumDocRef;
    }

    public void setsNumDocRef(String sNumDocRef) {
        this.sNumDocRef = sNumDocRef;
    }
    
//    public String getsFeEmiIni() {
//        return sFeEmiIni;
//    }

    public void setsCoDependencia(String sCoDependencia) {
        this.sCoDependencia = sCoDependencia;
    }

    public void setsCoEmpleado(String sCoEmpleado) {
        this.sCoEmpleado = sCoEmpleado;
    }

    public void setsTiAcceso(String sTiAcceso) {
        this.sTiAcceso = sTiAcceso;
    }

    public String getsCoDependencia() {
        return sCoDependencia;
    }

    public String getsCoEmpleado() {
        return sCoEmpleado;
    }

    public String getsTiAcceso() {
        return sTiAcceso;
    }

//    public void setsFeEmiIni(String sFeEmiIni) {
//        this.sFeEmiIni = sFeEmiIni;
//    }
//
//    public String getsFeEmiFin() {
//        return sFeEmiFin;
//    }
//
//    public void setsFeEmiFin(String sFeEmiFin) {
//        this.sFeEmiFin = sFeEmiFin;
//    }

    public String getsCoAnnio() {
        return sCoAnnio;
    }

    public void setsCoAnnio(String sCoAnnio) {
        this.sCoAnnio = sCoAnnio;
    }

    public String getsEstadoDoc() {
        return sEstadoDoc;
    }

    public void setsEstadoDoc(String sEstadoDoc) {
        this.sEstadoDoc = sEstadoDoc;
    }

    public String getsPrioridadDoc() {
        return sPrioridadDoc;
    }

    public void setsPrioridadDoc(String sPrioridadDoc) {
        this.sPrioridadDoc = sPrioridadDoc;
    }

    public String getsTipoDoc() {
        return sTipoDoc;
    }

    public void setsTipoDoc(String sTipoDoc) {
        this.sTipoDoc = sTipoDoc;
    }

    public String getsExpediente() {
        return sExpediente;
    }

    public void setsExpediente(String sExpediente) {
        this.sExpediente = sExpediente;
    }

    public String getsRefOrigen() {
        return sRefOrigen;
    }

    public void setsRefOrigen(String sRefOrigen) {
        this.sRefOrigen = sRefOrigen;
    }

    public String getsDestinatario() {
        return sDestinatario;
    }

    public void setsDestinatario(String sDestinatario) {
        this.sDestinatario = sDestinatario;
    }

    public String getsElaboradoPor() {
        return sElaboradoPor;
    }

    public void setsElaboradoPor(String sElaboradoPor) {
        this.sElaboradoPor = sElaboradoPor;
    }

    public String getsTipoBusqueda() {
        return sTipoBusqueda;
    }

    public void setsTipoBusqueda(String sTipoBusqueda) {
        this.sTipoBusqueda = sTipoBusqueda;
    }

    public String getsNumCorEmision() {
        return sNumCorEmision;
    }

    public void setsNumCorEmision(String sNumCorEmision) {
        this.sNumCorEmision = sNumCorEmision;
    }

    public String getsDeEmiReferencia() {
        return sDeEmiReferencia;
    }

    public void setsDeEmiReferencia(String sDeEmiReferencia) {
        this.sDeEmiReferencia = sDeEmiReferencia;
    }

    public String getsDeTipoDocAdm() {
        return sDeTipoDocAdm;
    }

    public void setsDeTipoDocAdm(String sDeTipoDocAdm) {
        this.sDeTipoDocAdm = sDeTipoDocAdm;
    }

    public String getsNumDoc() {
        return sNumDoc;
    }

    public void setsNumDoc(String sNumDoc) {
        this.sNumDoc = sNumDoc;
    }

    public String getsDeAsuM() {
        return sDeAsuM;
    }

    public void setsDeAsuM(String sDeAsuM) {
        this.sDeAsuM = sDeAsuM;
    }

    public String getsBuscDestinatario() {
        return sBuscDestinatario;
    }

    public void setsBuscDestinatario(String sBuscDestinatario) {
        this.sBuscDestinatario = sBuscDestinatario;
    }

//    public String getsBuscElaboraradoPor() {
//        return sBuscElaboraradoPor;
//    }
//
//    public void setsBuscElaboraradoPor(String sBuscElaboraradoPor) {
//        this.sBuscElaboraradoPor = sBuscElaboraradoPor;
//    }

    public String getsBuscNroExpediente() {
        return sBuscNroExpediente;
    }

    public void setsBuscNroExpediente(String sBuscNroExpediente) {
        this.sBuscNroExpediente = sBuscNroExpediente;
    }

//    public String getsBuscEstado() {
//        return sBuscEstado;
//    }
//
//    public void setsBuscEstado(String sBuscEstado) {
//        this.sBuscEstado = sBuscEstado;
//    }

    public String getTiDes() {
        return tiDes;
    }

    public void setTiDes(String tiDes) {
        this.tiDes = tiDes;
    }

    public String getDocEstadoMsj() {
        return docEstadoMsj;
    }

    public void setDocEstadoMsj(String docEstadoMsj) {
        this.docEstadoMsj = docEstadoMsj;
    }

    public String getsTiEnvMsj() {
        return sTiEnvMsj;
    }

    public void setsTiEnvMsj(String sTiEnvMsj) {
        this.sTiEnvMsj = sTiEnvMsj;
    }

    public String getCoBandeja() {
        return coBandeja;
    }

    public void setCoBandeja(String coBandeja) {
        this.coBandeja = coBandeja;
    }

    public boolean isEsPorVencer() {
        return esPorVencer;
    }

    public void setEsPorVencer(boolean esPorVencer) {
        this.esPorVencer = esPorVencer;
    }

    
    
}
