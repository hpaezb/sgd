package pe.gob.onpe.tramitedoc.bean;

public class BuscarDocumentoRecepBean {

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
    
    //jazanero
    private boolean esIncluyeOficina;
    private boolean esIncluyeProfesional;
    private boolean esIncluyePersonal;
    //jazanero

    private String cantidadPaginasRecep;
    private boolean esPorVencer;/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    public String getCoTema() {
        return coTema;
    }

    public void setCoTema(String coTema) {
        this.coTema = coTema;
    }
//    private String sBuscEstado;
    public String getIdEtiqueta() {
        return idEtiqueta;
    }

    public void setIdEtiqueta(String idEtiqueta) {
        this.idEtiqueta = idEtiqueta;
    }
    

    public BuscarDocumentoRecepBean() {
        this.sTipoBusqueda = "0";
    }

    public String getsCoAnnioBus() {
        return sCoAnnioBus;
    }

    public void setsCoAnnioBus(String sCoAnnioBus) {
        this.sCoAnnioBus = sCoAnnioBus;
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

    public boolean isEsIncluyeFiltro() {
        return esIncluyeFiltro;
    }

    public void setEsIncluyeFiltro(boolean esIncluyeFiltro) {
        this.esIncluyeFiltro = esIncluyeFiltro;
    }

    public String getsNumDocRef() {
        return sNumDocRef;
    }

    public void setsNumDocRef(String sNumDocRef) {
        this.sNumDocRef = sNumDocRef;
    }

    public String getsBuscDestinatario() {
        return sBuscDestinatario;
    }

    public void setsBuscDestinatario(String sBuscDestinatario) {
        this.sBuscDestinatario = sBuscDestinatario;
    }

//    public String getsFechaEmisionIni() {
//        return sFechaEmisionIni;
//    }
//
//    public void setsFechaEmisionIni(String sFechaEmisionIni) {
//        this.sFechaEmisionIni = sFechaEmisionIni;
//    }

//    public String getsFechaEmisionFin() {
//        return sFechaEmisionFin;
//    }
//
//    public void setsFechaEmisionFin(String sFechaEmisionFin) {
//        this.sFechaEmisionFin = sFechaEmisionFin;
//    }

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

//    public String getsUoDestinatario() {
//        return sUoDestinatario;
//    }
//
//    public void setsUoDestinatario(String sUoDestinatario) {
//        this.sUoDestinatario = sUoDestinatario;
//    }

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

//    public String getsBuscEstado() {
//        return sBuscEstado;
//    }
//
//    public void setsBuscEstado(String sBuscEstado) {
//        this.sBuscEstado = sBuscEstado;
//    }

    public String getsCoEmpleado() {
        return sCoEmpleado;
    }

    public String getsTiAcceso() {
        return sTiAcceso;
    }

    public void setsTiAcceso(String sTiAcceso) {
        this.sTiAcceso = sTiAcceso;
    }

    public void setsCoEmpleado(String sCoEmpleado) {
        this.sCoEmpleado = sCoEmpleado;
    }

    public String getsCoDependencia() {
        return sCoDependencia;
    }

    public void setsCoDependencia(String sCoDependencia) {
        this.sCoDependencia = sCoDependencia;
    }

    public String getsTipoBusqueda() {
        return sTipoBusqueda;
    }

    public void setsTipoBusqueda(String sTipoBusqueda) {
        this.sTipoBusqueda = sTipoBusqueda;
    }

//    public String getsUoremitente() {
//        return sUoremitente;
//    }
//
//    public void setsUoremitente(String sUoremitente) {
//        this.sUoremitente = sUoremitente;
//    }

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

    public String getsPrioridadDoc() {
        return sPrioridadDoc;
    }

    public void setsPrioridadDoc(String sPrioridadDoc) {
        this.sPrioridadDoc = sPrioridadDoc;
    }

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

    /**
     * @return the esIncluyeOficina
     */
    public boolean isEsIncluyeOficina() {
        return esIncluyeOficina;
    }

    /**
     * @param esIncluyeOficina the esIncluyeOficina to set
     */
    public void setEsIncluyeOficina(boolean esIncluyeOficina) {
        this.esIncluyeOficina = esIncluyeOficina;
    }

    /**
     * @return the esIncluyeProfesional
     */
    public boolean isEsIncluyeProfesional() {
        return esIncluyeProfesional;
    }

    /**
     * @param esIncluyeProfesional the esIncluyeProfesional to set
     */
    public void setEsIncluyeProfesional(boolean esIncluyeProfesional) {
        this.esIncluyeProfesional = esIncluyeProfesional;
    }

    /**
     * @return the esIncluyePersonal
     */
    public boolean isEsIncluyePersonal() {
        return esIncluyePersonal;
    }

    /**
     * @param esIncluyePersonal the esIncluyePersonal to set
     */
    public void setEsIncluyePersonal(boolean esIncluyePersonal) {
        this.esIncluyePersonal = esIncluyePersonal;
    }

    public String getCantidadPaginasRecep() {
        return cantidadPaginasRecep;
    }

    public void setCantidadPaginasRecep(String cantidadPaginasRecep) {
        this.cantidadPaginasRecep = cantidadPaginasRecep;
    }

    public boolean isEsPorVencer() {
        return esPorVencer;
    }

    public void setEsPorVencer(boolean esPorVencer) {
        this.esPorVencer = esPorVencer;
    }
    
    
}
