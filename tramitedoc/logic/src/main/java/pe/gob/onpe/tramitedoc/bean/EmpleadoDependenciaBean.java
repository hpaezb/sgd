package pe.gob.onpe.tramitedoc.bean;

import java.util.ArrayList;
/**
 *
 * @author consultor_ogti_80
 */
public class EmpleadoDependenciaBean {
    private String cempApepat;
    private String cempApemat;
    private String cempDenom;
    private String cempCodemp;
    private String coUsuario;
    private String coLocal;
    private String coDependencia;
    private ArrayList<DependenciaBean> empleadoDependenciaDetalle;  

    public String getCempApepat() {
        return cempApepat;
    }

    public void setCempApepat(String cempApepat) {
        this.cempApepat = cempApepat;
    }

    public String getCempApemat() {
        return cempApemat;
    }

    public void setCempApemat(String cempApemat) {
        this.cempApemat = cempApemat;
    }

    public String getCempDenom() {
        return cempDenom;
    }

    public void setCempDenom(String cempDenom) {
        this.cempDenom = cempDenom;
    }

    public String getCempCodemp() {
        return cempCodemp;
    }

    public void setCempCodemp(String cempCodemp) {
        this.cempCodemp = cempCodemp;
    }

    public String getCoUsuario() {
        return coUsuario;
    }

    public void setCoUsuario(String coUsuario) {
        this.coUsuario = coUsuario;
    }

    public String getCoLocal() {
        return coLocal;
    }

    public void setCoLocal(String coLocal) {
        this.coLocal = coLocal;
    }

    public String getCoDependencia() {
        return coDependencia;
    }

    public void setCoDependencia(String coDependencia) {
        this.coDependencia = coDependencia;
    }

    public ArrayList<DependenciaBean> getEmpleadoDependenciaDetalle() {
        return empleadoDependenciaDetalle;
    }

    public void setEmpleadoDependenciaDetalle(ArrayList<DependenciaBean> empleadoDependenciaDetalle) {
        this.empleadoDependenciaDetalle = empleadoDependenciaDetalle;
    }
    
    
}
