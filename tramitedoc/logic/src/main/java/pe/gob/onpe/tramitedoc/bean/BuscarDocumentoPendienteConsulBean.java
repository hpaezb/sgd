package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author hpaez
 */
public class BuscarDocumentoPendienteConsulBean extends ReporteBean{
    
    private String sCoAnnio;
    private String sEstadoDoc;
    private String sCoDependencia;
    private String sCoEmpleado;    
    private String sPrioridadDoc;
    private String sTipoDoc;
    private String sRemitente;
    private String sDestinatario;
    private String sExpediente;
    private String sTiAcceso;
    private String esFiltroFecha;
    private String sFeEmiIni;
    private String sFeEmiFin;    
    private String idEtiqueta;    
    
    /***************************************/
    private String sTipoBusqueda;//0 Filtrar, 1 Buscar
    /***************************************/
    //busqueda desde tabla hacia BD
//    private String sFechaEmisionIni;
//    private String sFechaEmisionFin;
//    private String sUoremitente;
    private String sNroEmision;
    private String sDeTipoDocAdm;
    private String sNroDocumento;
//    private String sUoDestinatario;
    private String sBuscAsunto;
    private String sBuscNroExpediente;
    private String sBuscDestinatario;
    private String sNumDocRef;
    private boolean esIncluyeFiltro;
    private String sCoAnnioBus;
    private String coTema;
    
    private String coTipoPersona;
    private String busNumDni;
    private String busDescDni;
    private String busNumRuc;
    private String busDescRuc;
    private String busCoOtros;
    private String busNomOtros;
    private String busResultado;

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

    public String getsCoDependencia() {
        return sCoDependencia;
    }

    public void setsCoDependencia(String sCoDependencia) {
        this.sCoDependencia = sCoDependencia;
    }

    public String getsCoEmpleado() {
        return sCoEmpleado;
    }

    public void setsCoEmpleado(String sCoEmpleado) {
        this.sCoEmpleado = sCoEmpleado;
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

    public String getsRemitente() {
        return sRemitente;
    }

    public void setsRemitente(String sRemitente) {
        this.sRemitente = sRemitente;
    }

    public String getsDestinatario() {
        return sDestinatario;
    }

    public void setsDestinatario(String sDestinatario) {
        this.sDestinatario = sDestinatario;
    }

    public String getsExpediente() {
        return sExpediente;
    }

    public void setsExpediente(String sExpediente) {
        this.sExpediente = sExpediente;
    }

    public String getsTiAcceso() {
        return sTiAcceso;
    }

    public void setsTiAcceso(String sTiAcceso) {
        this.sTiAcceso = sTiAcceso;
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

    public String getIdEtiqueta() {
        return idEtiqueta;
    }

    public void setIdEtiqueta(String idEtiqueta) {
        this.idEtiqueta = idEtiqueta;
    }

    public String getsTipoBusqueda() {
        return sTipoBusqueda;
    }

    public void setsTipoBusqueda(String sTipoBusqueda) {
        this.sTipoBusqueda = sTipoBusqueda;
    }

    public String getsNroEmision() {
        return sNroEmision;
    }

    public void setsNroEmision(String sNroEmision) {
        this.sNroEmision = sNroEmision;
    }

    public String getsDeTipoDocAdm() {
        return sDeTipoDocAdm;
    }

    public void setsDeTipoDocAdm(String sDeTipoDocAdm) {
        this.sDeTipoDocAdm = sDeTipoDocAdm;
    }

    public String getsNroDocumento() {
        return sNroDocumento;
    }

    public void setsNroDocumento(String sNroDocumento) {
        this.sNroDocumento = sNroDocumento;
    }

    public String getsBuscAsunto() {
        return sBuscAsunto;
    }

    public void setsBuscAsunto(String sBuscAsunto) {
        this.sBuscAsunto = sBuscAsunto;
    }

    public String getsBuscNroExpediente() {
        return sBuscNroExpediente;
    }

    public void setsBuscNroExpediente(String sBuscNroExpediente) {
        this.sBuscNroExpediente = sBuscNroExpediente;
    }

    public String getsBuscDestinatario() {
        return sBuscDestinatario;
    }

    public void setsBuscDestinatario(String sBuscDestinatario) {
        this.sBuscDestinatario = sBuscDestinatario;
    }

    public String getsNumDocRef() {
        return sNumDocRef;
    }

    public void setsNumDocRef(String sNumDocRef) {
        this.sNumDocRef = sNumDocRef;
    }

    public boolean isEsIncluyeFiltro() {
        return esIncluyeFiltro;
    }

    public void setEsIncluyeFiltro(boolean esIncluyeFiltro) {
        this.esIncluyeFiltro = esIncluyeFiltro;
    }

    public String getsCoAnnioBus() {
        return sCoAnnioBus;
    }

    public void setsCoAnnioBus(String sCoAnnioBus) {
        this.sCoAnnioBus = sCoAnnioBus;
    }

    public String getCoTema() {
        return coTema;
    }

    public void setCoTema(String coTema) {
        this.coTema = coTema;
    }

    public String getCoTipoPersona() {
        return coTipoPersona;
    }

    public void setCoTipoPersona(String coTipoPersona) {
        this.coTipoPersona = coTipoPersona;
    }

    public String getBusNumDni() {
        return busNumDni;
    }

    public void setBusNumDni(String busNumDni) {
        this.busNumDni = busNumDni;
    }

    public String getBusDescDni() {
        return busDescDni;
    }

    public void setBusDescDni(String busDescDni) {
        this.busDescDni = busDescDni;
    }

    public String getBusNumRuc() {
        return busNumRuc;
    }

    public void setBusNumRuc(String busNumRuc) {
        this.busNumRuc = busNumRuc;
    }

    public String getBusDescRuc() {
        return busDescRuc;
    }

    public void setBusDescRuc(String busDescRuc) {
        this.busDescRuc = busDescRuc;
    }

    public String getBusCoOtros() {
        return busCoOtros;
    }

    public void setBusCoOtros(String busCoOtros) {
        this.busCoOtros = busCoOtros;
    }

    public String getBusNomOtros() {
        return busNomOtros;
    }

    public void setBusNomOtros(String busNomOtros) {
        this.busNomOtros = busNomOtros;
    }

    public String getBusResultado() {
        return busResultado;
    }

    public void setBusResultado(String busResultado) {
        this.busResultado = busResultado;
    }    
}
