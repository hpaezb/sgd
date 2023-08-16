/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author hpaez
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoDevolucionMensajeriaBean {
    private String nuAnn;    
    private String nuEmi;   
    private String nuSec;   
    private String nuDocEmi;
    private String deDocSig;
    private String deTipDocAdm;
    private String nuDoc;
    private String deDependencia;
    private String deEmpDes;
    private String deAsu;
    private String obsDev;
    private String direccion;
    private String ubigeo;
    private String dePri;
    private String feEmiCorta;
    private String feEnvMes;
    private String feDevOfi;
    private String coEmpDev;
    private String esDocFin;
    private String deDocFin;
    private String esDocEmi;
    private String deDocEmi; 
    private String inExisteDoc;
    private String inExisteAnexo;

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

    public String getNuDocEmi() {
        return nuDocEmi;
    }

    public void setNuDocEmi(String nuDocEmi) {
        this.nuDocEmi = nuDocEmi;
    }

    public String getDeDocSig() {
        return deDocSig;
    }

    public void setDeDocSig(String deDocSig) {
        this.deDocSig = deDocSig;
    }

    public String getDeTipDocAdm() {
        return deTipDocAdm;
    }

    public void setDeTipDocAdm(String deTipDocAdm) {
        this.deTipDocAdm = deTipDocAdm;
    }

    public String getNuDoc() {
        return nuDoc;
    }

    public void setNuDoc(String nuDoc) {
        this.nuDoc = nuDoc;
    }

    public String getDeDependencia() {
        return deDependencia;
    }

    public void setDeDependencia(String deDependencia) {
        this.deDependencia = deDependencia;
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

    public String getObsDev() {
        return obsDev;
    }

    public void setObsDev(String obsDev) {
        this.obsDev = obsDev;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getDePri() {
        return dePri;
    }

    public void setDePri(String dePri) {
        this.dePri = dePri;
    }

    public String getFeEmiCorta() {
        return feEmiCorta;
    }

    public void setFeEmiCorta(String feEmiCorta) {
        this.feEmiCorta = feEmiCorta;
    }

    public String getFeDevOfi() {
        return feDevOfi;
    }

    public void setFeDevOfi(String feDevOfi) {
        this.feDevOfi = feDevOfi;
    }

    public String getCoEmpDev() {
        return coEmpDev;
    }

    public void setCoEmpDev(String coEmpDev) {
        this.coEmpDev = coEmpDev;
    }

    public String getEsDocFin() {
        return esDocFin;
    }

    public void setEsDocFin(String esDocFin) {
        this.esDocFin = esDocFin;
    }

    public String getDeDocFin() {
        return deDocFin;
    }

    public void setDeDocFin(String deDocFin) {
        this.deDocFin = deDocFin;
    }

    public String getEsDocEmi() {
        return esDocEmi;
    }

    public void setEsDocEmi(String esDocEmi) {
        this.esDocEmi = esDocEmi;
    }

    public String getDeDocEmi() {
        return deDocEmi;
    }

    public void setDeDocEmi(String deDocEmi) {
        this.deDocEmi = deDocEmi;
    }

    public String getInExisteDoc() {
        return inExisteDoc;
    }

    public void setInExisteDoc(String inExisteDoc) {
        this.inExisteDoc = inExisteDoc;
    }

    public String getInExisteAnexo() {
        return inExisteAnexo;
    }

    public void setInExisteAnexo(String inExisteAnexo) {
        this.inExisteAnexo = inExisteAnexo;
    }

    public String getFeEnvMes() {
        return feEnvMes;
    }

    public void setFeEnvMes(String feEnvMes) {
        this.feEnvMes = feEnvMes;
    }

    public String getNuSec() {
        return nuSec;
    }

    public void setNuSec(String nuSec) {
        this.nuSec = nuSec;
    }

    
    
}
