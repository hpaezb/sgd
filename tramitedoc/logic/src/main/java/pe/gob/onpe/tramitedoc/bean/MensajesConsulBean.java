/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

/**
 *
 * @author MVALDERA
 */
public class MensajesConsulBean {
    String nu_msj;
    String fec_enviomsj;
    String nu_ann;
    String nu_emi;
    String nu_des;
    String de_tip_doc;
    String co_local;
    String de_local;
    String co_dependencia;
    String de_dependencia;    
    String destinatario;
    String co_tramite;
    String co_prioridad;
    String de_indicaciones;
    String co_tipo_destino;
    String direccion;
    String departamento;
    String de_tip_msj;
    String de_tip_env;
    String re_env_msj;
    String nu_servicio;
    String fe_ent_msj;
    String fe_dev_msj;
    String est_msj;
    String cod_est_msj;
    String de_ambito;
    String tiZona;
    String fe_pla_msj;
    String es_pen_msj;
    String ob_msj;
    String mo_msj_dev;
    String ho_ent_msj;
    String ho_dev_msj;
    String de_asu;
    String dia_pla;
    String dia_tra;
    String dia_pen;
    String dia_ven;
    String dia_ent;
    String dia_dev;
    String tieneanexocargo;
    String es_doc_emi;
    String fe_emi;
    String fec_recepmp;
    String dias_pla_devo;
    String es_pen_dev;
    String fe_pla_dev;
    String fe_env_mes;
    String pe_env_msj;//PE_ENV_MSJ
    String pe_env_msj_d;//PE_ENV_MSJ_D
    String dias_pla_devo_d;
    
    String coTipoDocMsj;//Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
    String dias_pla_entr_d;
    private String tipodocumento;
    private String numerodocumento;
    
    private String busNumExpediente;
    
    public String getTieneanexocargo() {
        return tieneanexocargo;
    }

    public void setTieneanexocargo(String tieneanexocargo) {
        this.tieneanexocargo = tieneanexocargo;
    }
    
    public String getNu_msj() {
        return nu_msj;
    }

    public void setNu_msj(String nu_msj) {
        this.nu_msj = nu_msj;
    }

    public String getFec_enviomsj() {
        return fec_enviomsj;
    }

    public void setFec_enviomsj(String fec_enviomsj) {
        this.fec_enviomsj = fec_enviomsj;
    }

    public String getNu_ann() {
        return nu_ann;
    }

    public void setNu_ann(String nu_ann) {
        this.nu_ann = nu_ann;
    }

    public String getNu_emi() {
        return nu_emi;
    }

    public void setNu_emi(String nu_emi) {
        this.nu_emi = nu_emi;
    }

    public String getNu_des() {
        return nu_des;
    }

    public void setNu_des(String nu_des) {
        this.nu_des = nu_des;
    }

    public String getDe_tip_doc() {
        return de_tip_doc;
    }

    public void setDe_tip_doc(String de_tip_doc) {
        this.de_tip_doc = de_tip_doc;
    }

    public String getCo_local() {
        return co_local;
    }

    public void setCo_local(String co_local) {
        this.co_local = co_local;
    }

    public String getDe_local() {
        return de_local;
    }

    public void setDe_local(String de_local) {
        this.de_local = de_local;
    }

    public String getCo_dependencia() {
        return co_dependencia;
    }

    public void setCo_dependencia(String co_dependencia) {
        this.co_dependencia = co_dependencia;
    }

    public String getDe_dependencia() {
        return de_dependencia;
    }

    public void setDe_dependencia(String de_dependencia) {
        this.de_dependencia = de_dependencia;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getCo_tramite() {
        return co_tramite;
    }

    public void setCo_tramite(String co_tramite) {
        this.co_tramite = co_tramite;
    }

    public String getCo_prioridad() {
        return co_prioridad;
    }

    public void setCo_prioridad(String co_prioridad) {
        this.co_prioridad = co_prioridad;
    }

    public String getDe_indicaciones() {
        return de_indicaciones;
    }

    public void setDe_indicaciones(String de_indicaciones) {
        this.de_indicaciones = de_indicaciones;
    }

    public String getCo_tipo_destino() {
        return co_tipo_destino;
    }

    public void setCo_tipo_destino(String co_tipo_destino) {
        this.co_tipo_destino = co_tipo_destino;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDe_tip_msj() {
        return de_tip_msj;
    }

    public void setDe_tip_msj(String de_tip_msj) {
        this.de_tip_msj = de_tip_msj;
    }

    public String getDe_tip_env() {
        return de_tip_env;
    }

    public void setDe_tip_env(String de_tip_env) {
        this.de_tip_env = de_tip_env;
    }

    public String getRe_env_msj() {
        return re_env_msj;
    }

    public void setRe_env_msj(String re_env_msj) {
        this.re_env_msj = re_env_msj;
    }

    public String getNu_servicio() {
        return nu_servicio;
    }

    public void setNu_servicio(String nu_servicio) {
        this.nu_servicio = nu_servicio;
    }

    public String getFe_ent_msj() {
        return fe_ent_msj;
    }

    public void setFe_ent_msj(String fe_ent_msj) {
        this.fe_ent_msj = fe_ent_msj;
    }

    public String getFe_dev_msj() {
        return fe_dev_msj;
    }

    public void setFe_dev_msj(String fe_dev_msj) {
        this.fe_dev_msj = fe_dev_msj;
    }
    
    public String getEst_msj() {
        return est_msj;
    }

    public void setEst_msj(String est_msj) {
        this.est_msj = est_msj;
    }

    public String getCod_est_msj() {
        return cod_est_msj;
    }

    public void setCod_est_msj(String cod_est_msj) {
        this.cod_est_msj = cod_est_msj;
    }

    public String getDe_ambito() {
        return de_ambito;
    }

    public void setDe_ambito(String de_ambito) {
        this.de_ambito = de_ambito;
    }

    public String getFe_pla_msj() {
        return fe_pla_msj;
    }

    public void setFe_pla_msj(String fe_pla_msj) {
        this.fe_pla_msj = fe_pla_msj;
    }

    public String getEs_pen_msj() {
        return es_pen_msj;
    }

    public void setEs_pen_msj(String es_pen_msj) {
        this.es_pen_msj = es_pen_msj;
    }

    public String getOb_msj() {
        return ob_msj;
    }

    public void setOb_msj(String ob_msj) {
        this.ob_msj = ob_msj;
    }

    public String getMo_msj_dev() {
        return mo_msj_dev;
    }

    public void setMo_msj_dev(String mo_msj_dev) {
        this.mo_msj_dev = mo_msj_dev;
    }

    public String getHo_ent_msj() {
        return ho_ent_msj;
    }

    public void setHo_ent_msj(String ho_ent_msj) {
        this.ho_ent_msj = ho_ent_msj;
    }

    public String getHo_dev_msj() {
        return ho_dev_msj;
    }

    public void setHo_dev_msj(String ho_dev_msj) {
        this.ho_dev_msj = ho_dev_msj;
    }
    
    public String getDe_asu() {
        return de_asu;
    }

    public void setDe_asu(String de_asu) {
        this.de_asu = de_asu;
    }

    public String getDia_pla() {
        return dia_pla;
    }

    public void setDia_pla(String dia_pla) {
        this.dia_pla = dia_pla;
    }

    public String getDia_tra() {
        return dia_tra;
    }

    public void setDia_tra(String dia_tra) {
        this.dia_tra = dia_tra;
    }

    public String getDia_pen() {
        return dia_pen;
    }

    public void setDia_pen(String dia_pen) {
        this.dia_pen = dia_pen;
    }

    public String getDia_ven() {
        return dia_ven;
    }

    public void setDia_ven(String dia_ven) {
        this.dia_ven = dia_ven;
    }

    public String getDia_ent() {
        return dia_ent;
    }

    public void setDia_ent(String dia_ent) {
        this.dia_ent = dia_ent;
    }

    public String getDia_dev() {
        return dia_dev;
    }

    public void setDia_dev(String dia_dev) {
        this.dia_dev = dia_dev;
    }



    public String getEs_doc_emi() {
        return es_doc_emi;
    }

    public void setEs_doc_emi(String es_doc_emi) {
        this.es_doc_emi = es_doc_emi;
    }

    public String getFe_emi() {
        return fe_emi;
    }

    public void setFe_emi(String fe_emi) {
        this.fe_emi = fe_emi;
    }

    public String getFec_recepmp() {
        return fec_recepmp;
    }

    public void setFec_recepmp(String fec_recepmp) {
        this.fec_recepmp = fec_recepmp;
    }

    public String getDias_pla_devo() {
        return dias_pla_devo;
    }

    public void setDias_pla_devo(String dias_pla_devo) {
        this.dias_pla_devo = dias_pla_devo;
    }

    public String getEs_pen_dev() {
        return es_pen_dev;
    }

    public void setEs_pen_dev(String es_pen_dev) {
        this.es_pen_dev = es_pen_dev;
    }

    public String getFe_pla_dev() {
        return fe_pla_dev;
    }

    public void setFe_pla_dev(String fe_pla_dev) {
        this.fe_pla_dev = fe_pla_dev;
    }

    public String getFe_env_mes() {
        return fe_env_mes;
    }

    public void setFe_env_mes(String fe_env_mes) {
        this.fe_env_mes = fe_env_mes;
    }

    public String getPe_env_msj() {
        return pe_env_msj;
    }

    public void setPe_env_msj(String pe_env_msj) {
        this.pe_env_msj = pe_env_msj;
    }

    /**
     * @return the tipodocumento
     */
    public String getTipodocumento() {
        return tipodocumento;
    }

    /**
     * @param tipodocumento the tipodocumento to set
     */
    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    /**
     * @return the numerodocumento
     */
    public String getNumerodocumento() {
        return numerodocumento;
    }

    /**
     * @param numerodocumento the numerodocumento to set
     */
    public void setNumerodocumento(String numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public String getTiZona() {
        return tiZona;
    }

    public void setTiZona(String tiZona) {
        this.tiZona = tiZona;
    }

    public String getPe_env_msj_d() {
        return pe_env_msj_d;
    }

    public void setPe_env_msj_d(String pe_env_msj_d) {
        this.pe_env_msj_d = pe_env_msj_d;
    }

    public String getDias_pla_devo_d() {
        return dias_pla_devo_d;
    }

    public void setDias_pla_devo_d(String dias_pla_devo_d) {
        this.dias_pla_devo_d = dias_pla_devo_d;
    }

    public String getCoTipoDocMsj() {
        return coTipoDocMsj;
    }

    public void setCoTipoDocMsj(String coTipoDocMsj) {
        this.coTipoDocMsj = coTipoDocMsj;
    }

    public String getDias_pla_entr_d() {
        return dias_pla_entr_d;
    }

    public void setDias_pla_entr_d(String dias_pla_entr_d) {
        this.dias_pla_entr_d = dias_pla_entr_d;
    }

    public String getBusNumExpediente() {
        return busNumExpediente;
    }

    public void setBusNumExpediente(String busNumExpediente) {
        this.busNumExpediente = busNumExpediente;
    }

    
    
    
}
