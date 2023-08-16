/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;
import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
/**
 *
 * @author WCONDORI
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoRecepMensajeriaBean {
     private String nuExpediente;
    private String nuEmi;
    private String nuDoc;
    private String nuAnn;
    private String tiEmi;
    private String deTiEmi;
    private String deTipDocAdm;
    private String deOriEmiMp;
    private String deEsDocEmiMp;
    private String deEmpDes;
    private String deAsu;
    private String coEsDocEmiMp;
    private String coDepEmi;
    private String coLocEmi;
    private String coEmpEmi;
    private String deEmpEmi;
    private String nuCorrExp;
    private String feExp;
    private String feExpCorta;
    private String coTipDocAdm;
    private String nuFolios;
    private String nuCorDoc;
    private String nuCorEmi;
    private String coEmpRes;
    private String deDocSig;
    private String deDocSigG;
    private String deEmpRes;
    private String nuAnnExp;
    private String nuSecExp;
    private String nuDni;
    private String deNuDni;
    private String nuRuc;
    private String deNuRuc;
    private String coOtros;
    private String deNomOtros;
    private String deDocOtros;
    private String nuDocOtros;
    private String feVence;
    private String coProceso;
    private String feEmiCorta;
    private String existeDoc;
    private String inNumeroMp;
    private String coUseMod;
    private String nuDiaAte;
    private String deLocEmi;
    private String deDependencia;

    private String totalDestino;
    private String totalEnviado;
    private String totalPendiente;
    private String coTipMensajero;
    
    private String numsj;
    private String feregmsj;
    private String cousecre;
    private String deambito;
    private String detipmsj;
    private String reenvmsj;
    private String detipenv;
    private String nusermsj;
    private String ansermsj;
    private String fecenviomsj;
    private String hoenvmsj;
    private String feplamsj;
    private String hoplamsj;
    private String numDes;
    private String codigo;
    private String docEstadoMsj;
    private String fecRecepmp;
    private String fecEnviomsj;
    private String deAmbito;
    private String deTipEnv;
    private String nuSerMsj;
    private String fePlaMsj;
    private String diasVencimiento;
    private String diasEntrega;
    private String diasDevoluvion;
    private String fechaenvioamensajeria;
    private String calculaPenalizacion;
    
    //Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
    private String obMsj;
    private String tiZona;
    private String feEntMsj;
    private String feDevMsj;
    private String moMsjDev;
    private String tieneanexocargo;
    //Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
    
    private String direccion;
    private String ubigeo;
    private String cargo;
   
    /*------Hermes 20/07/2018--------*/
    private String coUbiDptoDestinoMsj;
    private String coUbiProvDestinoMsj;
    private String coUbiDistDestinoMsj;
    private String coServicio;
    private String fePlaMsjD;
    /*---------------Fin--------------*/
    private String newCodigo;
    
    public String getCalculaPenalizacion() {
        return calculaPenalizacion;
    }

    public void setCalculaPenalizacion(String calculaPenalizacion) {
        this.calculaPenalizacion = calculaPenalizacion;
    }
    private ArrayList<DestinatarioDocumentoEmiBean> lstDestinatario;
    
    public ArrayList<DestinatarioDocumentoEmiBean> getLstDestinatario() {
        return lstDestinatario;
    }

    public void setLstDestinatario(ArrayList<DestinatarioDocumentoEmiBean> lstDestinatario) {
        this.lstDestinatario = lstDestinatario;
    }
    
    
    public String getDiasEntrega() {
        return diasEntrega;
    }

    public String getFechaenvioamensajeria() {
        return fechaenvioamensajeria;
    }

    public void setFechaenvioamensajeria(String fechaenvioamensajeria) {
        this.fechaenvioamensajeria = fechaenvioamensajeria;
    }

    public void setDiasEntrega(String diasEntrega) {
        this.diasEntrega = diasEntrega;
    }

    public String getDiasDevoluvion() {
        return diasDevoluvion;
    }

    public void setDiasDevoluvion(String diasDevoluvion) {
        this.diasDevoluvion = diasDevoluvion;
    }
    
    
    
    public String getFecEnviomsj() {
        return fecEnviomsj;
    }

    public void setFecEnviomsj(String fecEnviomsj) {
        this.fecEnviomsj = fecEnviomsj;
    }
    
    public String getDeTipEnv() {
        return deTipEnv;
    }

    public void setDeTipEnv(String deTipEnv) {
        this.deTipEnv = deTipEnv;
    }

    public String getFePlaMsj() {
        return fePlaMsj;
    }

    public void setFePlaMsj(String fePlaMsj) {
        this.fePlaMsj = fePlaMsj;
    }

    public String getDiasVencimiento() {
        return diasVencimiento;
    }

    public void setDiasVencimiento(String diasVencimiento) {
        this.diasVencimiento = diasVencimiento;
    }
    
    
            
    
    public String getFecRecepmp() {
        return fecRecepmp;
    }

    public void setFecRecepmp(String fecRecepmp) {
        this.fecRecepmp = fecRecepmp;
    }
    
    public String getDocEstadoMsj() {
        return docEstadoMsj;
    }

    public void setDocEstadoMsj(String docEstadoMsj) {
        this.docEstadoMsj = docEstadoMsj;
    }

     
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNumDes() {
        return numDes;
    }

    public void setNumDes(String numDes) {
        this.numDes = numDes;
    }
    public String getNumsj() {
        return numsj;
    }

    public void setNumsj(String numsj) {
        this.numsj = numsj;
    }

    public String getFeregmsj() {
        return feregmsj;
    }

    public void setFeregmsj(String feregmsj) {
        this.feregmsj = feregmsj;
    }

    public String getCousecre() {
        return cousecre;
    }

    public void setCousecre(String cousecre) {
        this.cousecre = cousecre;
    }

    public String getDeambito() {
        return deambito;
    }

    public void setDeambito(String deambito) {
        this.deambito = deambito;
    }

    public String getDetipmsj() {
        return detipmsj;
    }

    public void setDetipmsj(String detipmsj) {
        this.detipmsj = detipmsj;
    }

    public String getReenvmsj() {
        return reenvmsj;
    }

    public void setReenvmsj(String reenvmsj) {
        this.reenvmsj = reenvmsj;
    }

    public String getDetipenv() {
        return detipenv;
    }

    public void setDetipenv(String detipenv) {
        this.detipenv = detipenv;
    }

    public String getNusermsj() {
        return nusermsj;
    }

    public void setNusermsj(String nusermsj) {
        this.nusermsj = nusermsj;
    }

    public String getAnsermsj() {
        return ansermsj;
    }

    public void setAnsermsj(String ansermsj) {
        this.ansermsj = ansermsj;
    }

    public String getFecenviomsj() {
        return fecenviomsj;
    }

    public void setFecenviomsj(String fecenviomsj) {
        this.fecenviomsj = fecenviomsj;
    }

    public String getHoenvmsj() {
        return hoenvmsj;
    }

    public void setHoenvmsj(String hoenvmsj) {
        this.hoenvmsj = hoenvmsj;
    }

    public String getFeplamsj() {
        return feplamsj;
    }

    public void setFeplamsj(String feplamsj) {
        this.feplamsj = feplamsj;
    }

    public String getHoplamsj() {
        return hoplamsj;
    }

    public void setHoplamsj(String hoplamsj) {
        this.hoplamsj = hoplamsj;
    }

            
            
    public DocumentoRecepMensajeriaBean() {
    }
    public String getCoTipMensajero() {
        return coTipMensajero;
    }

    public void setCoTipMensajero(String coTipMensajero) {
        this.coTipMensajero = coTipMensajero;
    }
    public String getTotalPendiente() {
        return totalPendiente;
    }

    public void setTotalPendiente(String totalPendiente) {
        this.totalPendiente = totalPendiente;
    }
    public String getTotalEnviado() {
        return totalEnviado;
    }

    public void setTotalEnviado(String totalEnviado) {
        this.totalEnviado = totalEnviado;
    }
    
    public String getTotalDestino() {
        return totalDestino;
    }

    public void setTotalDestino(String totalDestino) {
        this.totalDestino = totalDestino;
    }
    
    public String getDeDependencia() {
        return deDependencia;
    }

    public void setDeDependencia(String deDependencia) {
        this.deDependencia = deDependencia;
    }

    public String getDeLocEmi() {
        return deLocEmi;
    }

    public void setDeLocEmi(String deLocEmi) {
        this.deLocEmi = deLocEmi;
    }

    public String getNuDiaAte() {
        return nuDiaAte;
    }

    public void setNuDiaAte(String nuDiaAte) {
        this.nuDiaAte = nuDiaAte;
    }

    public String getExisteDoc() {
        return existeDoc;
    }

    public void setExisteDoc(String existeDoc) {
        this.existeDoc = existeDoc;
    }

    public String getFeEmiCorta() {
        return feEmiCorta;
    }

    public void setFeEmiCorta(String feEmiCorta) {
        this.feEmiCorta = feEmiCorta;
    }

    public String getFeVence() {
        return feVence;
    }

    public void setFeVence(String feVence) {
        this.feVence = feVence;
    }

    public String getCoProceso() {
        return coProceso;
    }

    public void setCoProceso(String coProceso) {
        this.coProceso = coProceso;
    }

    public String getNuAnnExp() {
        return nuAnnExp;
    }

    public void setNuAnnExp(String nuAnnExp) {
        this.nuAnnExp = nuAnnExp;
    }

    public String getNuDni() {
        return nuDni;
    }

    public void setNuDni(String nuDni) {
        this.nuDni = nuDni;
    }

    public String getDeNuDni() {
        return deNuDni;
    }

    public void setDeNuDni(String deNuDni) {
        this.deNuDni = deNuDni;
    }

    public String getNuRuc() {
        return nuRuc;
    }

    public void setNuRuc(String nuRuc) {
        this.nuRuc = nuRuc;
    }

    public String getDeNuRuc() {
        return deNuRuc;
    }

    public void setDeNuRuc(String deNuRuc) {
        this.deNuRuc = deNuRuc;
    }

    public String getCoOtros() {
        return coOtros;
    }

    public void setCoOtros(String coOtros) {
        this.coOtros = coOtros;
    }

    public String getDeNomOtros() {
        return deNomOtros;
    }

    public void setDeNomOtros(String deNomOtros) {
        this.deNomOtros = deNomOtros;
    }

    public String getDeDocOtros() {
        return deDocOtros;
    }

    public void setDeDocOtros(String deDocOtros) {
        this.deDocOtros = deDocOtros;
    }

    public String getNuDocOtros() {
        return nuDocOtros;
    }

    public void setNuDocOtros(String nuDocOtros) {
        this.nuDocOtros = nuDocOtros;
    }

    public String getNuSecExp() {
        return nuSecExp;
    }

    public void setNuSecExp(String nuSecExp) {
        this.nuSecExp = nuSecExp;
    }

    public String getDeEmpRes() {
        return deEmpRes;
    }

    public void setDeEmpRes(String deEmpRes) {
        this.deEmpRes = deEmpRes;
    }

    public String getDeDocSigG() {
        return deDocSigG;
    }

    public void setDeDocSigG(String deDocSigG) {
        this.deDocSigG = deDocSigG;
    }

    public String getDeDocSig() {
        return deDocSig;
    }

    public void setDeDocSig(String deDocSig) {
        this.deDocSig = deDocSig;
    }

    public String getCoEmpRes() {
        return coEmpRes;
    }

    public void setCoEmpRes(String coEmpRes) {
        this.coEmpRes = coEmpRes;
    }

    public String getNuCorEmi() {
        return nuCorEmi;
    }

    public void setNuCorEmi(String nuCorEmi) {
        this.nuCorEmi = nuCorEmi;
    }

    public String getNuCorDoc() {
        return nuCorDoc;
    }

    public void setNuCorDoc(String nuCorDoc) {
        this.nuCorDoc = nuCorDoc;
    }

    public String getCoUseMod() {
        return coUseMod;
    }

    public void setCoUseMod(String coUseMod) {
        this.coUseMod = coUseMod;
    }

    public String getNuFolios() {
        return nuFolios;
    }

    public void setNuFolios(String nuFolios) {
        this.nuFolios = nuFolios;
    }

    public String getCoTipDocAdm() {
        return coTipDocAdm;
    }

    public void setCoTipDocAdm(String coTipDocAdm) {
        this.coTipDocAdm = coTipDocAdm;
    }

    public String getNuCorrExp() {
        return nuCorrExp;
    }

    public void setNuCorrExp(String nuCorrExp) {
        this.nuCorrExp = nuCorrExp;
    }

    public String getFeExp() {
        return feExp;
    }

    public void setFeExp(String feExp) {
        this.feExp = feExp;
    }

    public String getFeExpCorta() {
        return feExpCorta;
    }

    public void setFeExpCorta(String feExpCorta) {
        this.feExpCorta = feExpCorta;
    }

    public String getTiEmi() {
        return tiEmi;
    }

    public void setTiEmi(String tiEmi) {
        this.tiEmi = tiEmi;
    }

    public String getDeTiEmi() {
        return deTiEmi;
    }

    public void setDeTiEmi(String deTiEmi) {
        this.deTiEmi = deTiEmi;
    }

    public String getCoDepEmi() {
        return coDepEmi;
    }

    public void setCoDepEmi(String coDepEmi) {
        this.coDepEmi = coDepEmi;
    }

    public String getCoLocEmi() {
        return coLocEmi;
    }

    public void setCoLocEmi(String coLocEmi) {
        this.coLocEmi = coLocEmi;
    }

    public String getCoEmpEmi() {
        return coEmpEmi;
    }

    public void setCoEmpEmi(String coEmpEmi) {
        this.coEmpEmi = coEmpEmi;
    }

    public String getDeEmpEmi() {
        return deEmpEmi;
    }

    public void setDeEmpEmi(String deEmpEmi) {
        this.deEmpEmi = deEmpEmi;
    }

    public String getCoEsDocEmiMp() {
        return coEsDocEmiMp;
    }

    public void setCoEsDocEmiMp(String coEsDocEmiMp) {
        this.coEsDocEmiMp = coEsDocEmiMp;
    }

    public String getNuExpediente() {
        return nuExpediente;
    }

    public void setNuExpediente(String nuExpediente) {
        this.nuExpediente = nuExpediente;
    }

    public String getNuEmi() {
        return nuEmi;
    }

    public void setNuEmi(String nuEmi) {
        this.nuEmi = nuEmi;
    }

    public String getNuDoc() {
        return nuDoc;
    }

    public void setNuDoc(String nuDoc) {
        this.nuDoc = nuDoc;
    }

    public String getNuAnn() {
        return nuAnn;
    }

    public void setNuAnn(String nuAnn) {
        this.nuAnn = nuAnn;
    }

    public String getDeTipDocAdm() {
        return deTipDocAdm;
    }

    public void setDeTipDocAdm(String deTipDocAdm) {
        this.deTipDocAdm = deTipDocAdm;
    }

    public String getDeOriEmiMp() {
        return deOriEmiMp;
    }

    public void setDeOriEmiMp(String deOriEmiMp) {
        this.deOriEmiMp = deOriEmiMp;
    }

    public String getDeEsDocEmiMp() {
        return deEsDocEmiMp;
    }

    public void setDeEsDocEmiMp(String deEsDocEmiMp) {
        this.deEsDocEmiMp = deEsDocEmiMp;
    }

    public String getDeEmpDes() {
        return deEmpDes;
    }

    public void setDeEmpDes(String deEmpDes) {
        this.deEmpDes = deEmpDes;
    }

    public String getDeAsu() {
        return deAsu;
    }

    public void setDeAsu(String deAsu) {
        this.deAsu = deAsu;
    }

    public String getInNumeroMp() {
        return inNumeroMp;
    }

    public void setInNumeroMp(String inNumeroMp) {
        this.inNumeroMp = inNumeroMp;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the ubigeo
     */
    public String getUbigeo() {
        return ubigeo;
    }

    /**
     * @param ubigeo the ubigeo to set
     */
    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    /**
     * @return the cargo
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * @param cargo the cargo to set
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCoUbiDptoDestinoMsj() {
        return coUbiDptoDestinoMsj;
    }

    public void setCoUbiDptoDestinoMsj(String coUbiDptoDestinoMsj) {
        this.coUbiDptoDestinoMsj = coUbiDptoDestinoMsj;
    }

    public String getCoUbiProvDestinoMsj() {
        return coUbiProvDestinoMsj;
    }

    public void setCoUbiProvDestinoMsj(String coUbiProvDestinoMsj) {
        this.coUbiProvDestinoMsj = coUbiProvDestinoMsj;
    }

    public String getCoUbiDistDestinoMsj() {
        return coUbiDistDestinoMsj;
    }

    public void setCoUbiDistDestinoMsj(String coUbiDistDestinoMsj) {
        this.coUbiDistDestinoMsj = coUbiDistDestinoMsj;
    }

    public String getCoServicio() {
        return coServicio;
    }

    public void setCoServicio(String coServicio) {
        this.coServicio = coServicio;
    }

    public String getNewCodigo() {
        return newCodigo;
    }

    public void setNewCodigo(String newCodigo) {
        this.newCodigo = newCodigo;
    }

    public String getFePlaMsjD() {
        return fePlaMsjD;
    }

    public void setFePlaMsjD(String fePlaMsjD) {
        this.fePlaMsjD = fePlaMsjD;
    }

    public String getObMsj() {
        return obMsj;
    }

    public void setObMsj(String obMsj) {
        this.obMsj = obMsj;
    }

    public String getTiZona() {
        return tiZona;
    }

    public void setTiZona(String tiZona) {
        this.tiZona = tiZona;
    }

    public String getFeEntMsj() {
        return feEntMsj;
    }

    public void setFeEntMsj(String feEntMsj) {
        this.feEntMsj = feEntMsj;
    }

    public String getFeDevMsj() {
        return feDevMsj;
    }

    public void setFeDevMsj(String feDevMsj) {
        this.feDevMsj = feDevMsj;
    }

    public String getMoMsjDev() {
        return moMsjDev;
    }

    public void setMoMsjDev(String moMsjDev) {
        this.moMsjDev = moMsjDev;
    }

    public String getTieneanexocargo() {
        return tieneanexocargo;
    }

    public void setTieneanexocargo(String tieneanexocargo) {
        this.tieneanexocargo = tieneanexocargo;
    }

    public String getDeAmbito() {
        return deAmbito;
    }

    public void setDeAmbito(String deAmbito) {
        this.deAmbito = deAmbito;
    }

    public String getNuSerMsj() {
        return nuSerMsj;
    }

    public void setNuSerMsj(String nuSerMsj) {
        this.nuSerMsj = nuSerMsj;
    }
    
    
}
