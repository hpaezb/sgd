/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author WCutipa
 */
public class DocumentoObjBean {
    private String nuAnn;
    private String nuEmi;
    private String nuAne;
    private String tiCap;
    private String numeroSecuencia;
    private String nombreArchivo;
    private String tipoDoc;
    private String coUseMod;
    private int    nuTamano;
    private byte[] documento;
    private String deRespuesta;
    
    //jazanero
    private String wNombreArchivo;
    private int    wNuTamano;
    private String rutabase;
    private String usuario;
    
    private String glosaDocumento;
    
    //JAZANERO
    private String plantillaAntigua;
    //JAZANERO
    
    //gcanaza codigo QR
    private String rutaqr;
    
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

    public String getTiCap() {
        return tiCap;
    }

    public void setTiCap(String tiCap) {
        this.tiCap = tiCap;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public int getNuTamano() {
        return nuTamano;
    }

    public void setNuTamano(int nuTamano) {
        this.nuTamano = nuTamano;
    }

    public String getNuAne() {
        return nuAne;
    }

    public void setNuAne(String nuAne) {
        this.nuAne = nuAne;
    }

    public String getCoUseMod() {
        return coUseMod;
    }

    public void setCoUseMod(String coUseMod) {
        this.coUseMod = coUseMod;
    }

    public String getNumeroSecuencia() {
        return numeroSecuencia;
    }

    public void setNumeroSecuencia(String numeroSecuencia) {
        this.numeroSecuencia = numeroSecuencia;
    }

    public String getDeRespuesta() {
        return deRespuesta;
    }

    public void setDeRespuesta(String deRespuesta) {
        this.deRespuesta = deRespuesta;
    }

    /**
     * @return the wNombreArchivo
     */
    public String getwNombreArchivo() {
        return wNombreArchivo;
    }

    /**
     * @param wNombreArchivo the wNombreArchivo to set
     */
    public void setwNombreArchivo(String wNombreArchivo) {
        this.wNombreArchivo = wNombreArchivo;
    }

    /**
     * @return the wNuTamano
     */
    public int getwNuTamano() {
        return wNuTamano;
    }

    /**
     * @param wNuTamano the wNuTamano to set
     */
    public void setwNuTamano(int wNuTamano) {
        this.wNuTamano = wNuTamano;
    }

    /**
     * @return the rutabase
     */
    public String getRutabase() {
        return rutabase;
    }

    /**
     * @param rutabase the rutabase to set
     */
    public void setRutabase(String rutabase) {
        this.rutabase = rutabase;
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
     * @return the glosaDocumento
     */
    public String getGlosaDocumento() {
        return glosaDocumento;
    }

    /**
     * @param glosaDocumento the glosaDocumento to set
     */
    public void setGlosaDocumento(String glosaDocumento) {
        this.glosaDocumento = glosaDocumento;
    }

    /**
     * @return the plantillaAntigua
     */
    public String getPlantillaAntigua() {
        return plantillaAntigua;
    }

    /**
     * @param plantillaAntigua the plantillaAntigua to set
     */
    public void setPlantillaAntigua(String plantillaAntigua) {
        this.plantillaAntigua = plantillaAntigua;
    }
    
    public String getRutaqr() {
        return rutaqr;
    }
    
    public void setRutaqr(String rutaqr) {
        this.rutaqr = rutaqr;
    }    
        
}
