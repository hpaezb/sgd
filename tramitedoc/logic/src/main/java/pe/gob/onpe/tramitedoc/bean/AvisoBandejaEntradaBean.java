/**
 * 
 */
package pe.gob.onpe.tramitedoc.bean;

/**
 * @author ecueva
 *
 */
public class AvisoBandejaEntradaBean {

	private String tiPen;
	private String dePen;
	private String deResumen;
	private String coDep;
	private String nuCan;        
	private String coBandeja;
	
	public String getTiPen() {
		return tiPen;
	}
	public void setTiPen(String tiPen) {
		this.tiPen = tiPen;
	}
	public String getDePen() {
		return dePen;
	}
	public void setDePen(String dePen) {
		this.dePen = dePen;
	}
	public String getCoDep() {
		return coDep;
	}
	public void setCoDep(String coDep) {
		this.coDep = coDep;
	}
	public String getNuCan() {
		return nuCan;
	}
	public void setNuCan(String nuCan) {
		this.nuCan = nuCan;
	}

    public String getDeResumen() {
        return deResumen;
    }

    public void setDeResumen(String deResumen) {
        this.deResumen = deResumen;
    }

    /**
     * @return the coBandeja
     */
    public String getCoBandeja() {
        return coBandeja;
    }

    /**
     * @param coBandeja the coBandeja to set
     */
    public void setCoBandeja(String coBandeja) {
        this.coBandeja = coBandeja;
    }
    
}
