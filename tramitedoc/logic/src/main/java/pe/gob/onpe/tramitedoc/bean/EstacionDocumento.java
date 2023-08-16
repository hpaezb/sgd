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
public class EstacionDocumento {
    private String esDocIni;
    private String esDocFin;
    private String obsDev;
    
    public EstacionDocumento() {
    }

    public EstacionDocumento(String esDocIni, String esDocFin, String obsDev) {
        this.esDocIni = esDocIni;
        this.esDocFin = esDocFin;
        this.obsDev = obsDev;
    }


    public String getEsDocIni() {
        return esDocIni;
    }

    public void setEsDocIni(String esDocIni) {
        this.esDocIni = esDocIni;
    }

    public String getEsDocFin() {
        return esDocFin;
    }

    public void setEsDocFin(String esDocFin) {
        this.esDocFin = esDocFin;
    }

    public String getObsDev() {
        return obsDev;
    }

    public void setObsDev(String obsDev) {
        this.obsDev = obsDev;
    }
    
    
}
