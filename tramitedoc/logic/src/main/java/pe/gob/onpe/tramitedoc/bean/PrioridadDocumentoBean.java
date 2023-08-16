/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author ECueva
 */
public class PrioridadDocumentoBean {
    private String coPri;
    private String dePri;
    /* [INICIO] - [VMBP - 14/08/2019] - agregar prioridad*/
    private String esMensActivo;
    /* [FIN] - [VMBP - 14/08/2019] - agregar prioridad*/
    public PrioridadDocumentoBean() {
    }

    public String getCoPri() {
        return coPri;
    }

    public void setCoPri(String coPri) {
        this.coPri = coPri;
    }

    public String getDePri() {
        return dePri;
    }

    public void setDePri(String dePri) {
        this.dePri = dePri;
    }

    /* [INICIO] - [VMBP - 14/08/2019] - agregar prioridad*/
    public String getEsMensActivo() {
        return esMensActivo;
    }

    public void setEsMensActivo(String esMensActivo) {
        this.esMensActivo = esMensActivo;
    }
    /* [FIN] - [VMBP - 14/08/2019] - agregar prioridad*/
}
