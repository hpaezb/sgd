/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;
/**
 *
 * @author oti3
 */
public class DescargaMensajeBean {
    String nu_msj;
    String fec_enviomsj;
    String nu_ann;
    String nu_emi;
    String nu_des;
    String ob_msj;
    String mo_msj_dev;
    String fe_ent_msj;
    String ho_ent_msj;
    String fe_dev_msj;
    String ho_dev_msj;
    String es_pen_msj;
    String co_EstadoDoc;
    String fe_pla_dev;
    String di_pla_dev;
    String es_pen_dev;
    String coMotivo;
    String de_tipo_msj;
    String pe_env_msj;
    byte[] archivo;
    
    String pe_env_msj_d;//PE_ENV_MSJ_D
    
    private String codigos;
    //CommonsMultipartFile archivo;
    String fe_pla_msj;
    String nu_ane;

    String tip_doc_msj;
    String coUseMod;/*[HPB-21/06/21] Campos Auditoria-*/
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

    public String getFe_ent_msj() {
        return fe_ent_msj;
    }

    public void setFe_ent_msj(String fe_ent_msj) {
        this.fe_ent_msj = fe_ent_msj;
    }

    public String getHo_ent_msj() {
        return ho_ent_msj;
    }

    public void setHo_ent_msj(String ho_ent_msj) {
        this.ho_ent_msj = ho_ent_msj;
    }

    public String getFe_dev_msj() {
        return fe_dev_msj;
    }

    public void setFe_dev_msj(String fe_dev_msj) {
        this.fe_dev_msj = fe_dev_msj;
    }

    public String getHo_dev_msj() {
        return ho_dev_msj;
    }

    public void setHo_dev_msj(String ho_dev_msj) {
        this.ho_dev_msj = ho_dev_msj;
    }

    public String getEs_pen_msj() {
        return es_pen_msj;
    }

    public void setEs_pen_msj(String es_pen_msj) {
        this.es_pen_msj = es_pen_msj;
    }

    public String getCo_EstadoDoc() {
        return co_EstadoDoc;
    }

    public void setCo_EstadoDoc(String co_EstadoDoc) {
        this.co_EstadoDoc = co_EstadoDoc;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String getFe_pla_dev() {
        return fe_pla_dev;
    }

    public void setFe_pla_dev(String fe_pla_dev) {
        this.fe_pla_dev = fe_pla_dev;
    }

    public String getDi_pla_dev() {
        return di_pla_dev;
    }

    public void setDi_pla_dev(String di_pla_dev) {
        this.di_pla_dev = di_pla_dev;
    }



    public String getEs_pen_dev() {
        return es_pen_dev;
    }

    public void setEs_pen_dev(String es_pen_dev) {
        this.es_pen_dev = es_pen_dev;
    }

    public String getCoMotivo() {
        return coMotivo;
    }

    public void setCoMotivo(String coMotivo) {
        this.coMotivo = coMotivo;
    }

    public String getDe_tipo_msj() {
        return de_tipo_msj;
    }

    public void setDe_tipo_msj(String de_tipo_msj) {
        this.de_tipo_msj = de_tipo_msj;
    }

    public String getPe_env_msj() {
        return pe_env_msj;
    }

    public void setPe_env_msj(String pe_env_msj) {
        this.pe_env_msj = pe_env_msj;
    }

    @Override
    public String toString(){
        return "nu_msj->"+nu_msj +"\n;"+
        "fec_enviomsj->"+fec_enviomsj+"\n;"+
        "nu_ann->"+nu_ann+"\n;"+
        "nu_emi->"+nu_emi+"\n;"+
        "nu_des->"+nu_des+"\n;"+
        "nu_ane->"+nu_ane+"\n;"+
        "ob_msj->"+ob_msj+"\n;"+
        "tip_doc_msj->"+tip_doc_msj+"\n;"+
        "mo_msj_dev->"+mo_msj_dev+"\n;"+
        "fe_ent_msj->"+fe_ent_msj+"\n;"+
        "ho_ent_msj->"+ho_ent_msj+"\n;"+
        "fe_dev_msj->"+fe_dev_msj+"\n;"+
        "ho_dev_msj->"+ho_dev_msj+"\n;"+
        "es_pen_msj->"+es_pen_msj+"\n;"+
        "co_EstadoDoc->"+co_EstadoDoc+"\n;"+
        "fe_pla_dev->"+fe_pla_dev+"\n;"+
        "di_pla_dev->"+di_pla_dev+"\n;"+
        "es_pen_dev->"+es_pen_dev+"\n;"+
        "coMotivo->"+coMotivo+"\n;"+
        "de_tipo_msj->"+de_tipo_msj+"\n;"+
        "pe_env_msj->"+pe_env_msj+"\n;"+
        "pe_env_msj_d->"+pe_env_msj_d+"\n;"+
        "codigos->"+codigos;
    }

    /**
     * @return the codigos
     */
    public String getCodigos() {
        return codigos;
    }

    /**
     * @param codigos the codigos to set
     */
    public void setCodigos(String codigos) {
        this.codigos = codigos;
    }

    public String getFe_pla_msj() {
        return fe_pla_msj;
    }

    public void setFe_pla_msj(String fe_pla_msj) {
        this.fe_pla_msj = fe_pla_msj;
    }

    public String getPe_env_msj_d() {
        return pe_env_msj_d;
    }

    public void setPe_env_msj_d(String pe_env_msj_d) {
        this.pe_env_msj_d = pe_env_msj_d;
    }

    public String getNu_ane() {
        return nu_ane;
    }

    public void setNu_ane(String nu_ane) {
        this.nu_ane = nu_ane;
    }

    public String getTip_doc_msj() {
        return tip_doc_msj;
    }

    public void setTip_doc_msj(String tip_doc_msj) {
        this.tip_doc_msj = tip_doc_msj;
    }

    public String getCoUseMod() {
        return coUseMod;
    }

    public void setCoUseMod(String coUseMod) {
        this.coUseMod = coUseMod;
    }



    
}
