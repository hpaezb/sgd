/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.onpe.tramitedoc.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.libreria.exception.validarDatoException;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoExtConsulBean;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBean;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBeanNew;
import pe.gob.onpe.tramitedoc.bean.DestinatarioDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioOtroOrigenBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoExtConsulBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteDocExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.MotivoBean;
import pe.gob.onpe.tramitedoc.bean.ProcesoExpBean;
import pe.gob.onpe.tramitedoc.bean.ProveedorBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaDocExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteDocExtRecepBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.RequisitoBean;
import pe.gob.onpe.tramitedoc.bean.TrxDocExternoRecepBean;
import pe.gob.onpe.tramitedoc.dao.CommonQueryDao;
import pe.gob.onpe.tramitedoc.dao.EmiDocumentoAdmDao;
import pe.gob.onpe.tramitedoc.dao.MesaPartesDao;
import pe.gob.onpe.tramitedoc.service.ActualizaResumenService;
import pe.gob.onpe.tramitedoc.service.AuditoriaMovimientoDocService;
import pe.gob.onpe.tramitedoc.service.CommonQryService;
import pe.gob.onpe.tramitedoc.service.DocumentoExtRecepService;
import pe.gob.onpe.tramitedoc.util.Constantes;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author ECueva
 */
@Service("documentoExtRecepService")
public class DocumentoExtRecepServiceImp implements DocumentoExtRecepService{

    @Autowired
    private EmiDocumentoAdmDao emiDocumentoAdmDao;
    
    @Autowired
    private MesaPartesDao mesaPartesDao;   
    
    @Autowired
    private CommonQueryDao commonQueryDao;   
    
    @Autowired
    private ActualizaResumenService actualizaResumenService;
    
    @Autowired
    AuditoriaMovimientoDocService audiMovDoc;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private CommonQryService commonQryService;  
        
    @Override
    public List<DocumentoExtRecepBean> getDocumentosExtRecep(BuscarDocumentoExtRecepBean buscarDocumentoExtRecepBean) {
        List<DocumentoExtRecepBean> list = null;
        System.out.println("getDocumentosExtRecep");
        try {
            buscarDocumentoExtRecepBean.setCoGrupo("3");
            /*String vResult=mesaPartesDao.getPermisoChangeEstadoMP(buscarDocumentoExtRecepBean.getCoEmpleado(),buscarDocumentoExtRecepBean.getCoDependencia());
            buscarDocumentoExtRecepBean.setInCambioEst(vResult!=null&&vResult.equals("1")?"1":"0");*/
            list = mesaPartesDao.getDocumentosExtRecep(buscarDocumentoExtRecepBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String grabaDocumentoExternoRecep(TrxDocExternoRecepBean trxDocExternoRecepBean, Usuario usuario) throws Exception {
      String vReturn = "NO_OK";
        try {
            String pnuAnn = trxDocExternoRecepBean.getNuAnn();
            String pnuEmi = trxDocExternoRecepBean.getNuEmi();
            String pnuAnnExp = trxDocExternoRecepBean.getNuAnnExp();
            String pnuSecExp = trxDocExternoRecepBean.getNuSecExp();
            String pcoUserMod = trxDocExternoRecepBean.getCoUserMod();
            String pcempCodEmp = trxDocExternoRecepBean.getCempCodEmp();            
            DocumentoExtRecepBean documentoExtRecepBean = trxDocExternoRecepBean.getDocumentoEmiBean();
            ExpedienteDocExtRecepBean expedienteBean = trxDocExternoRecepBean.getExpedienteEmiBean();
            RemitenteDocExtRecepBean remitenteBean = trxDocExternoRecepBean.getRemitenteEmiBean();
            ArrayList<ReferenciaDocExtRecepBean> lstReferencia = trxDocExternoRecepBean.getLstReferencia();
            ArrayList<DestinatarioDocumentoEmiBean> lstDestinatario = trxDocExternoRecepBean.getLstDestinatario();             
            if (pnuAnn != null && pnuEmi != null && !pnuAnn.equals("") && !pnuEmi.equals("")) {//UPD 
                trxDocExternoRecepBean.setAccionBD("UPD");
                String pcoDepEmi=trxDocExternoRecepBean.getCoDependencia();
                if(expedienteBean!=null){
                    expedienteBean.setUsCreaAudi(pcempCodEmp);
                    vReturn = Utility.getInstancia().getStringFechaFormat(expedienteBean.getFeVence(),"dd/MM/yyyy HH:mm:ss","dd/MM/yyyy");
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error fecha vencimiento Expediente.");
                    }                 
                    expedienteBean.setFeVence(vReturn);                    
                    vReturn = mesaPartesDao.updExpedienteBean(expedienteBean);
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error actualizando Expediente.");
                    }                                      
                }
                if (documentoExtRecepBean != null || remitenteBean != null) {
                    if(documentoExtRecepBean!=null){
                        documentoExtRecepBean.setCoDepEmi(pcoDepEmi);
                    }
                    vReturn = mesaPartesDao.updDocumentoExtBean(pnuAnn, pnuEmi, documentoExtRecepBean, expedienteBean, remitenteBean, pcoUserMod);
                    if (!"OK".equals(vReturn)) {
                        throw new validarDatoException("Error actualizando Documento.");
                    }                    
                }                
                for (ReferenciaDocExtRecepBean ref : lstReferencia) {
                    String sAccionBD = ref.getAccionBd();
                    ref.setCoUseCre(pcoUserMod);
                    ref.setCoUseMod(pcoUserMod);/*[HPB-21/06/21] Campos Auditoria-*/
                    if ("INS".equals(sAccionBD)) {
                        vReturn = mesaPartesDao.insReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
                        if ("NO_OK".equals(vReturn)) {
                            throw new validarDatoException("Error Agregando Referencia");
                        }                        
                    } else if ("UPD".equals(sAccionBD)) {
                        vReturn = mesaPartesDao.updReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
                        if ("NO_OK".equals(vReturn)) {
                            throw new validarDatoException("Error Actualizando Referencia");
                        }                           
                    } else if ("DEL".equals(sAccionBD)) {
                        vReturn = mesaPartesDao.updRefDocumentoEmiAntesEliminar(pnuAnn, pnuEmi, ref);/*[HPB-21/06/21] Campos Auditoria-*/
                        if ("NO_OK".equals(vReturn)) {
                            throw new validarDatoException("Error Borrando Referencia");
                        }else {                        
                            vReturn = mesaPartesDao.delReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref.getNuAnn(), ref.getNuEmi());
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error Borrando Referencia");
                            } 
                        }
                    }
                }
                for (DestinatarioDocumentoEmiBean dest : lstDestinatario) {
                    String sAccionBD = dest.getAccionBD();
                    dest.setCoUseCre(pcoUserMod);
                    if ("INS".equals(sAccionBD)) {
                        vReturn = mesaPartesDao.insDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
                        if ("NO_OK".equals(vReturn)) {
                            throw new validarDatoException("Error Agregando Destinatario");
                        }
                    } else if ("UPD".equals(sAccionBD)) {
                        vReturn = mesaPartesDao.updDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
                        if ("NO_OK".equals(vReturn)) {
                            throw new validarDatoException("Error Actualizando Destinatario");
                        }
                    } else if ("DEL".equals(sAccionBD)) {
                        vReturn = mesaPartesDao.delDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest.getNuDes());
                        if ("NO_OK".equals(vReturn)) {
                            throw new validarDatoException("Error Borrando Destinatario");
                        }
                    }
                }                   
            }else{//INS
                trxDocExternoRecepBean.setAccionBD("INS");
                expedienteBean.setUsCreaAudi(pcempCodEmp);
                vReturn = Utility.getInstancia().getStringFechaFormat(expedienteBean.getFeExp(),"dd/MM/yyyy HH:mm:ss","dd/MM/yyyy HH:mm:ss");
                if ("NO_OK".equals(vReturn)) {
                    throw new validarDatoException("Error fecha Expediente.");
                }                  
                expedienteBean.setFeExp(vReturn);
                vReturn = Utility.getInstancia().getStringFechaFormat(expedienteBean.getFeVence(),"dd/MM/yyyy HH:mm:ss","dd/MM/yyyy");
                if ("NO_OK".equals(vReturn)) {
                    throw new validarDatoException("Error fecha vencimiento Expediente.");
                }                 
                expedienteBean.setFeVence(vReturn);
                //Verificamos si viene con numeracion automatica de expediente
                if (expedienteBean.getNuCorrExp()==null || expedienteBean.getNuCorrExp().equals("") ){
                    vReturn = mesaPartesDao.getNumeroExpediente(expedienteBean);////
                }
                documentoExtRecepBean.setNuCorrExp(expedienteBean.getNuCorrExp());
                documentoExtRecepBean.setNuExpediente(expedienteBean.getNuExpediente());                
                //ver lo de_detalle expediente
                vReturn = mesaPartesDao.insExpedienteBean(expedienteBean);////
                if (!"OK".equals(vReturn)) {
                    throw new validarDatoException("Error al Grabar Expediente.");
                }                    
                documentoExtRecepBean.setCoUseMod(pcoUserMod);
                documentoExtRecepBean.setCoEsDocEmiMp("5");//EN REGISTRO
                vReturn = mesaPartesDao.getNroCorrelativoDocumento(documentoExtRecepBean.getNuAnn(),remitenteBean.getCoDepEmi(),remitenteBean.getTiEmi());
                if ("NO_OK".equals(vReturn)) {
                    throw new validarDatoException("Error al obtener número correlativo.");
                }
                documentoExtRecepBean.setNuCorDoc(vReturn);
                documentoExtRecepBean.setNuSecExp(expedienteBean.getNuSecExp());
                remitenteBean.setCoDep(usuario.getCoDep());
                vReturn = mesaPartesDao.insDocumentoExtBean(documentoExtRecepBean, expedienteBean, remitenteBean);////
                if (!"OK".equals(vReturn)) {
                    throw new validarDatoException("Error al Grabar Documento.");
                }
                pnuEmi = documentoExtRecepBean.getNuEmi();
                vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoExtRecepBean.getNuAnn(), pnuEmi, "NC");
                if(!vReturn.equals("OK")){
                    vReturn = "Error insertando Auditoria.";
                    throw new validarDatoException(vReturn);
                }                
                trxDocExternoRecepBean.setNuEmi(pnuEmi);
                pnuAnnExp=expedienteBean.getNuAnnExp();
                pnuSecExp=expedienteBean.getNuSecExp();
                trxDocExternoRecepBean.setNuAnnExp(pnuAnnExp);
                trxDocExternoRecepBean.setNuSecExp(pnuSecExp);
                for (ReferenciaDocExtRecepBean ref : lstReferencia) {
                    ref.setCoUseCre(pcoUserMod);
                    vReturn = mesaPartesDao.insReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error Agregando Referencia");
                    }
                }
                for (DestinatarioDocumentoEmiBean dest : lstDestinatario) {
                    dest.setCoUseCre(pcoUserMod);
                    vReturn = mesaPartesDao.insDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error Agregando Destinatario");
                    }
                }                
            }
            /*actualizar resumenes*/
            if (lstDestinatario.size() >= 1) {
                vReturn=actualizaResumenService.updRemitosResumenDes(pnuAnn, pnuEmi);
                if ("NO_OK".equals(vReturn)) {
                    throw new validarDatoException("Error Actualizando Remito resumen destinatario");
                }
            }
            if (lstReferencia.size() >= 1) {
                vReturn=actualizaResumenService.updRemitosResumenRef(pnuAnn, pnuEmi);
                if ("NO_OK".equals(vReturn)) {
                    throw new validarDatoException("Error Actualizando Remito resumen Referencia");
                }                
            }            
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION.");
        }
      return vReturn;  
    }
    
    @Override
    public String getJsonRptGrabaDocumentoExternoRecep(TrxDocExternoRecepBean trxDocExternoRecepBean){
        boolean bandera = false;
        StringBuilder retval = new StringBuilder(); 
        String paccionBD = trxDocExternoRecepBean.getAccionBD();
        try {
            DocumentoExtRecepBean documentoExtRecepBean = trxDocExternoRecepBean.getDocumentoEmiBean();
            if(paccionBD.equals("UPD")){
                if(documentoExtRecepBean!=null){
                    retval.append("\"documentoEmiBean\":{");
                    retval.append("\"nuCorDoc\":\"");
                    retval.append(documentoExtRecepBean.getNuCorDoc());                     
                    retval.append("\",\"esDocEmi\":\"");
                    retval.append(documentoExtRecepBean.getCoEsDocEmiMp());                     
                    retval.append("\"},");                
                }
            }else if(paccionBD.equals("INS")){
                retval.append("\"documentoEmiBean\":{");
                retval.append("\"nuAnn\":\"");
                retval.append(documentoExtRecepBean.getNuAnn());
                retval.append("\",\"nuEmi\":\"");
                retval.append(documentoExtRecepBean.getNuEmi());
                retval.append("\",\"nuAnnExp\":\"");
                retval.append(documentoExtRecepBean.getNuAnn());                 
                retval.append("\",\"nuSecExp\":\"");
                retval.append(documentoExtRecepBean.getNuSecExp()); 
                retval.append("\",\"nuCorDoc\":\"");
                retval.append(documentoExtRecepBean.getNuCorDoc());                     
                retval.append("\",\"esDocEmi\":\"");
                retval.append(documentoExtRecepBean.getCoEsDocEmiMp());                     
                retval.append("\",\"nuCorrExp\":\"");
                retval.append(documentoExtRecepBean.getNuCorrExp());                                     
                retval.append("\",\"nuExpediente\":\"");
                retval.append(documentoExtRecepBean.getNuExpediente());                  
                retval.append("\"},");
            }    
            retval.append("\"lstReferencia\":[");
            for (ReferenciaDocExtRecepBean ref : trxDocExternoRecepBean.getLstReferencia()) {
                    retval.append("{\"accionBD\":\"");
                    retval.append(ref.getAccionBd());
                    retval.append("\",\"nuAnn\":\"");
                    retval.append(ref.getNuAnn());
                    retval.append("\",\"nuEmi\":\"");
                    retval.append(ref.getNuEmi());
                    retval.append("\",\"coRef\":\"");
                    retval.append(ref.getCoRef());                        
                    retval.append("\"},"); 
                    bandera = true;
            }
            if (bandera) {
                retval.deleteCharAt(retval.length() - 1);
                bandera = false;
            }                
            retval.append("]");

            retval.append(",\"lstDestinatario\":[");  
            for (DestinatarioDocumentoEmiBean dest : trxDocExternoRecepBean.getLstDestinatario()) {
                    retval.append("{\"accionBD\":\"");
                    retval.append(dest.getAccionBD());
                    retval.append("\",\"coDependencia\":\"");
                    retval.append(dest.getCoDependencia());
                    retval.append("\",\"coEmpleado\":\"");
                    retval.append(dest.getCoEmpleado());
                    retval.append("\",\"nuDes\":\"");
                    retval.append(dest.getNuDes());                        
                    retval.append("\"},"); 
                    bandera = true;
            }                
            if (bandera) {
                retval.deleteCharAt(retval.length() - 1);
            }                
            retval.append("]");                
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval.toString();
    }
    
    @Override
    public String isExpedienteDuplicado(String pnuAnnExp, String pcoDepExp, String pnuCorrExp){
        String vResult="1";
        try {
            vResult = commonQueryDao.verificarExpedienteDuplicado(pnuAnnExp, pcoDepExp, "3", pnuCorrExp);
            if(vResult.equals("0")){//no Duplicado
                vResult = commonQueryDao.verficarNumeroDuplicadoEmiDocExtRecep(pnuAnnExp,pcoDepExp,pnuCorrExp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vResult;        
    }
    
    @Override
    public DocumentoExtRecepBean getDocumentoExtRecepNew(String pcoDependencia){
        DocumentoExtRecepBean docExterno=null;
        String esDocEmi="5";
        try {
            docExterno=mesaPartesDao.getDocumentoExtRecepNew(pcoDependencia);
            if(docExterno!=null){
                docExterno.setCoEsDocEmiMp(esDocEmi);
                docExterno.setNuFolios("");
                docExterno.setTiEmi("03");
                /*modificaciones para mostrar plantilla de documentos externo recepcion*/
                docExterno.setCoTipDocAdm("231");//SOLICITUD
                docExterno.setDeAsu("");//ASUNTO PLANTILLA
                /*modificaciones para mostrar plantilla de documentos externo recepcion*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docExterno;
    }
    
    @Override
    public List<MotivoBean> getLstMotivoxTipoDocumento(){
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List<MotivoBean> list = null;
        try {
            list = mesaPartesDao.getLstMotivoxTipoDocumento();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<ReferenciaDocExtRecepBean> getLstDocExtReferencia(String pnuAnn,String pcoTiDoc,String pcoDepEmi,String pnuExpediente){
        List<ReferenciaDocExtRecepBean> list = null;
        try {
            if(pnuExpediente!=null&&pnuExpediente.trim().length()>0){
                pnuExpediente=Utility.getInstancia().rellenaCerosIzquierda(pnuExpediente, 6);
            }
            list = mesaPartesDao.getLstDocExtReferencia(pnuAnn, pcoTiDoc, pcoDepEmi, pnuExpediente);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ReferenciaDocExtRecepBean> getLstDocExtReferenciaEmitido(String pnuAnn,String pcoTiDoc,String pcoDepEmi,String pnuExpediente){
        List<ReferenciaDocExtRecepBean> list = null;
        try {
            if(pnuExpediente!=null&&pnuExpediente.trim().length()>0){
                pnuExpediente=Utility.getInstancia().rellenaCerosIzquierda(pnuExpediente, 6);
            }
            list = mesaPartesDao.getLstDocExtReferenciaEmitido(pnuAnn, pcoTiDoc, pcoDepEmi, pnuExpediente);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public DocumentoExtRecepBean getDocumentoExtRec(String pnuAnn, String pnuEmi) {
        DocumentoExtRecepBean documentoExtRecepBean=null;
        try {
            documentoExtRecepBean = mesaPartesDao.getDocumentoExtRec(pnuAnn,pnuEmi);
            if(documentoExtRecepBean!=null){
                String ptiEmi=documentoExtRecepBean.getTiEmi();
                String pcoOtros=documentoExtRecepBean.getCoOtros();
                if(ptiEmi!=null&&ptiEmi.equals("04")&&pcoOtros!=null&&pcoOtros.trim().length()>0){//otros
                    Map map=mesaPartesDao.getDesFieldOtro(pcoOtros);
                    if(map!=null){
                        documentoExtRecepBean.setDeNomOtros(String.valueOf(map.get("DE_NOM_OTROS")));
                        documentoExtRecepBean.setDeDocOtros(String.valueOf(map.get("DE_DOC_OTROS")));
                        documentoExtRecepBean.setNuDocOtros(String.valueOf(map.get("NU_DOC_OTROS")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentoExtRecepBean;
    }
    
    @Override
    public List<DestinatarioDocumentoEmiBean> getLstDestinoEmiDoc(String pnuAnn, String pnuEmi){
        List<DestinatarioDocumentoEmiBean> list = null;
        try {
            list = mesaPartesDao.getLstDestinoEmiDoc(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }   
    
    @Override
    public List<ReferenciaDocExtRecepBean> getLstReferenciaDoc(String pnuAnn, String pnuEmi){
        List<ReferenciaDocExtRecepBean> list = null;
        try {
            if(pnuAnn!=null&&pnuAnn.trim().length()>0&&pnuEmi!=null&&pnuEmi.trim().length()>0){
                list = mesaPartesDao.getLstReferenciaDoc(pnuAnn, pnuEmi);                    
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<ProveedorBean> getLstProveedores(String prazonSocial){
        List<ProveedorBean> list = null;
        try {
            list = commonQueryDao.getLstProveedores(prazonSocial);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    @Override
    public String getCiudadano(String pnuDoc){
        CiudadanoBean ciudadanoBean = null;
        CiudadanoBeanNew ciudadanoBeanNew = null;/*--HPB 13/01/20 Integrar PIDE--*/
        String coRespuesta="0";
        StringBuilder retval = new StringBuilder();   
        try {
            String vIndicadorPide = commonQueryDao.obtenerValorParametro("IN_PIDE"); /*--HPB 13/01/20 Integrar PIDE--*/
            retval.append("{\"coRespuesta\":\"");
            ciudadanoBean = commonQueryDao.getCiudadano(pnuDoc);
            if(ciudadanoBean!=null){
                coRespuesta="1";
                retval.append(coRespuesta);
                retval.append("\",");                
                retval.append("\"ciudadanoBean\":{");
                retval.append("\"nuDoc\":\"");
                retval.append(ciudadanoBean.getNuDocumento());                     
                retval.append("\",\"nombre\":\"");
                retval.append(ciudadanoBean.getNombre());   
                retval.append("\",\"idDepartamento\":\"");
                retval.append(ciudadanoBean.getIdDepartamento()==null?"":ciudadanoBean.getIdDepartamento()); 
                retval.append("\",\"idProvincia\":\"");
                retval.append(ciudadanoBean.getIdProvincia()==null?"":ciudadanoBean.getIdProvincia()); 
                retval.append("\",\"idDistrito\":\"");
                retval.append(ciudadanoBean.getIdDistrito()==null?"":ciudadanoBean.getIdDistrito()); 
                retval.append("\",\"deDireccion\":\"");
                /* [HPB] Inicio 07/11/22 OS-0000768-2022 */
                //retval.append(ciudadanoBean.getDeDireccion()==null?"":ciudadanoBean.getDeDireccion()); 
                retval.append(ciudadanoBean.getDeDireccion()==null?"":ciudadanoBean.getDeDireccion().replace("\"", ""));
                /* [HPB] Inicio 07/11/22 OS-0000768-2022 */
                retval.append("\",\"deCorreo\":\"");
                retval.append(ciudadanoBean.getDeCorreo()==null?"":ciudadanoBean.getDeCorreo()); 
                retval.append("\",\"telefono\":\"");
                retval.append(ciudadanoBean.getTelefono()==null?"":ciudadanoBean.getTelefono()); 
                retval.append("\"}");                  
            }else{
                if(vIndicadorPide.equals("1")){/*--HPB 13/01/20 Integrar PIDE--*/
                    ciudadanoBeanNew = commonQueryDao.getLstCiudadanoStored(pnuDoc, "11");
                  
                    if(ciudadanoBeanNew!=null){
                        ciudadanoBean = commonQueryDao.getLstCiudadanoAni(pnuDoc, "11");
                        if(ciudadanoBean!=null){
                            coRespuesta="1";
                            retval.append(coRespuesta);
                            retval.append("\",");                
                            retval.append("\"ciudadanoBean\":{");
                            retval.append("\"nuDoc\":\"");
                            retval.append(ciudadanoBean.getNuDocumento());                     
                            retval.append("\",\"nombre\":\"");
                            retval.append(ciudadanoBean.getNombre());   
                            retval.append("\",\"idDepartamento\":\"");
                            retval.append(ciudadanoBean.getIdDepartamento()==null?"":ciudadanoBean.getIdDepartamento()); 
                            retval.append("\",\"idProvincia\":\"");
                            retval.append(ciudadanoBean.getIdProvincia()==null?"":ciudadanoBean.getIdProvincia()); 
                            retval.append("\",\"idDistrito\":\"");
                            retval.append(ciudadanoBean.getIdDistrito()==null?"":ciudadanoBean.getIdDistrito()); 
                            retval.append("\",\"deDireccion\":\"");
                            /* [HPB] Inicio 07/11/22 OS-0000768-2022 */
                            retval.append(ciudadanoBean.getDeDireccion()==null?"":ciudadanoBean.getDeDireccion().replace("\"", ""));
                            /* [HPB] Inicio 07/11/22 OS-0000768-2022 */
                            retval.append("\",\"deCorreo\":\"");
                            retval.append(ciudadanoBean.getDeCorreo()==null?"":ciudadanoBean.getDeCorreo()); 
                            retval.append("\",\"telefono\":\"");
                            retval.append(ciudadanoBean.getTelefono()==null?"":ciudadanoBean.getTelefono()); 
                            retval.append("\"}");                  
                        }else{
                            retval.append(coRespuesta);
                            retval.append("\"");                
                        } 
                    }else{
                        retval.append(coRespuesta);
                        retval.append("\"");                
                    }  
                }else{
                    retval.append(coRespuesta);
                    retval.append("\"");  
                }
            }                         
            retval.append("}");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("retval.toString(): "+retval.toString());/*--HPB 13/01/20 Integrar PIDE--*/ 
        return retval.toString();
    }
    
    @Override
    public List<DestinatarioOtroOrigenBean> getLstOtrosOrigenes(String pdescripcion){
        List<DestinatarioOtroOrigenBean> list = null;
        try {
            list = commonQueryDao.getLstOtrosOrigenes(pdescripcion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }    

    @Override
    public String getProveedor(String pnuRuc) {
        ProveedorBean proveedorBean;
        String coRespuesta="0";
        StringBuilder retval = new StringBuilder();       
        try {
            String vIndicadorPide = commonQueryDao.obtenerValorParametro("IN_PIDE"); /*--HPB 23/01/20 Integrar PIDE - RUC--*/
            if(vIndicadorPide.equals("0")){                        
                proveedorBean = commonQueryDao.getProveedor(pnuRuc);
            }else{
                proveedorBean = commonQueryDao.getLstProveedor(pnuRuc);
            }
            retval.append("{\"coRespuesta\":\"");
            if(proveedorBean!=null){
                coRespuesta="1";
                retval.append(coRespuesta);
                retval.append("\",");                
                retval.append("\"proveedorBean\":{");
                retval.append("\"nuRuc\":\"");
                retval.append(proveedorBean.getNuRuc());                     
                retval.append("\",\"deRuc\":\"");
                retval.append(proveedorBean.getDescripcion());
                retval.append("\",\"idDepartamento\":\"");
                retval.append(proveedorBean.getIdDepartamento()==null?"":proveedorBean.getIdDepartamento()); 
                retval.append("\",\"idProvincia\":\"");
                retval.append(proveedorBean.getIdProvincia()==null?"":proveedorBean.getIdProvincia()); 
                retval.append("\",\"idDistrito\":\"");
                retval.append(proveedorBean.getIdDistrito()==null?"":proveedorBean.getIdDistrito()); 
                retval.append("\",\"deDireccion\":\"");
                /* [HPB] Inicio 07/11/22 OS-0000768-2022 */
                //retval.append(proveedorBean.getDeDireccion()==null?"":proveedorBean.getDeDireccion()); 
                retval.append(proveedorBean.getDeDireccion()==null?"":proveedorBean.getDeDireccion().replace("\"", ""));
                /* [HPB] Inicio 07/11/22 OS-0000768-2022 */
                retval.append("\",\"deCorreo\":\"");
                retval.append(proveedorBean.getDeCorreo()==null?"":proveedorBean.getDeCorreo()); 
                retval.append("\",\"telefono\":\"");
                retval.append(proveedorBean.getTelefono()==null?"":proveedorBean.getTelefono()); 
                retval.append("\"}");                  
            }else{
                retval.append(coRespuesta);
                retval.append("\"");                
            }
            retval.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval.toString();
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String changeToRegistrado(DocumentoExtRecepBean documentoExtRecepBean, Usuario usuario) throws Exception {
        String vReturn = "NO_OK";
        String pnuAnn=documentoExtRecepBean.getNuAnn();
        String pnuEmi=documentoExtRecepBean.getNuEmi();
        String pcoUseMod = documentoExtRecepBean.getCoUseMod();
        /* [HPB] Inicio 07/11/22 OS-0000768-2022 */
        String pcoEmpRes = documentoExtRecepBean.getCoEmpRes();
        /* [HPB] Fin 07/11/22 OS-0000768-2022 */
        try {
            if(pnuAnn!=null&&pnuAnn.trim().length()>0&&pnuEmi!=null&&pnuEmi.trim().length()>0){
                documentoExtRecepBean=mesaPartesDao.getDocumentoExtRecBasic(pnuAnn, pnuEmi);
                if(documentoExtRecepBean==null){
                  throw new validarDatoException("Documento no Exite.");    
                }else{
                  //verificamos si el documento esta en registro
                    String esDocEmi=documentoExtRecepBean.getCoEsDocEmiMp();
                    if(esDocEmi!=null&&esDocEmi.equals("7")){//para verificar
                        //Verificamos si el Documento esta en PDF
                        String vpfd=getVerificaPdfDoc(documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi(),null);
                        if (vpfd.equals("OK")){
                            //VERIFICAR REQUISITOS COMPLETOS
                            vReturn = mesaPartesDao.getRequisitoPendiente(documentoExtRecepBean.getNuAnnExp(),documentoExtRecepBean.getNuSecExp());
                            if(vReturn.equals("OK")){
                            //VERIFICAR SI ES AUTORIZADO PARA CAMBIAR ESTADO A REGISTRADO.
                            vReturn = mesaPartesDao.getPermisoChangeEstadoMP(usuario.getCempCodemp(), usuario.getCoDep());
                            if(vReturn.equals("1")){
                                //actualizar estado de documento
                                documentoExtRecepBean.setCoEsDocEmiMp("0");
                                documentoExtRecepBean.setCoUseMod(pcoUseMod);
                                /* [HPB] Inicio 07/11/22 OS-0000768-2022 */
                                documentoExtRecepBean.setCoEmpRes(pcoEmpRes);
                                /* [HPB] Fin 07/11/22 OS-0000768-2022 */
                                vReturn=mesaPartesDao.updEstadoDocumentoExt(documentoExtRecepBean);
                                if(vReturn.equals("OK")){
                                    //ACTUALIZAR FECHA EXPEDIENTE
    //                                vReturn=mesaPartesDao.updFechaExpedienteMP(documentoExtRecepBean.getCoUseMod(),documentoExtRecepBean.getNuAnnExp(),documentoExtRecepBean.getNuSecExp());
    //                                if(vReturn.equals("OK")){
                                        vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi(), esDocEmi);
                                        if(!vReturn.equals("OK")){
                                            vReturn = "Error insertando Auditoria.";
                                            throw new validarDatoException(vReturn);
                                        }
    //                                }else{
    //                                    throw new validarDatoException("Error actualizando fecha Expediente.");
    //                                }
                                }else{
                                    throw new validarDatoException("Error cambiando a Estado Registrado.");
                                }                                
                            }else{
                                throw new validarDatoException("No habilitado para cambiar a estado <strong>\\\"Registrado\\\"</strong>.");
                            }
                        }else{
                                throw new validarDatoException("Requisitos faltantes.");
                            }
                        }else{
                            throw new validarDatoException("El Documento tiene que estar en PDF");
                        }                     
                    }else{
                        throw new validarDatoException("Operación no Permitida.");
                    }
                }                
            }else{
              throw new validarDatoException("Documento no Existe.");  
            }
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION A REGISTRADO.");
        }
        return vReturn;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String changeToEnRegistro(DocumentoExtRecepBean documentoExtRecepBean, Usuario usuario) throws Exception {
        System.out.println("changeToEnRegistro 2");
        String vReturn = "NO_OK";
        String pnuAnn=documentoExtRecepBean.getNuAnn();
        String pnuEmi=documentoExtRecepBean.getNuEmi();
        String pcoEmpRes=documentoExtRecepBean.getCoEmpRes();
        String pcoUseMod = documentoExtRecepBean.getCoUseMod();
        /* [HPB] Inicio 25/08/22 OS-0000768-2022 */
        EmpleadoBean empleadoBean = commonQueryDao.getEmpJefeDep("11351");
        /* [HPB] Fin 25/08/22 OS-0000768-2022 */
        try {
            if(pnuAnn!=null&&pnuAnn.trim().length()>0&&pnuEmi!=null&&pnuEmi.trim().length()>0){
                documentoExtRecepBean=mesaPartesDao.getDocumentoExtRecBasic(pnuAnn, pnuEmi);
                if(documentoExtRecepBean==null){
                  throw new validarDatoException("Documento no Exite.");    
                }else{
                  //verificamos si el documento esta registrado
                    String esDocEmi=documentoExtRecepBean.getCoEsDocEmiMp();
                    if(esDocEmi!=null&&(esDocEmi.equals("0")||esDocEmi.equals("7") ||esDocEmi.equals("A"))){//registrado o para verificar o por subsanar /*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR */
                        boolean isChangeEstado=true;
                        if(esDocEmi.equals("0")){//registrado
                            //verificar que el documento no fue leido
                          vReturn = emiDocumentoAdmDao.verificarDocumentoLeido(documentoExtRecepBean.getNuAnn(),documentoExtRecepBean.getNuEmi());                        
                          if (vReturn!=null&&vReturn.equals("0")) {                                
                            String pcoEmpEmi=documentoExtRecepBean.getCoEmpEmi(); 
                            /* [HPB] Inicio 25/08/22 OS-0000768-2022 */
                            String pcoEmpJefe = empleadoBean.getCempCodemp();
                            if(!(pcoEmpJefe!=null&&pcoEmpJefe.equals(pcoEmpRes))){//Se verifica si es el responsable de la UTD
                            /* [HPB] Fin 25/08/22 OS-0000768-2022 */
                            /* [HPB] Inicio 11/12/23 OS-0001287-2023 Permitir a cualquier usuario modificar de REGISTRADOS a EN REGISTROS */
                            //if(!(pcoEmpEmi!=null&&pcoEmpEmi.equals(pcoEmpRes))){
                                //isChangeEstado=false;
                                //vReturn = "El documento solo puede ser cambiado por el Responsable del Área.";
                            //}
                            /* [HPB] Fin 11/12/23 OS-0001287-2023 Permitir a cualquier usuario modificar de REGISTRADOS a EN REGISTROS */
                            /* [HPB] Inicio 25/08/22 OS-0000768-2022 */
                            }                                
                            /* [HPB] Fin 25/08/22 OS-0000768-2022 */
                          }else{
                            isChangeEstado=false;
                            vReturn = "Documento ya fue leído por los Destinatarios.";
                          }
                        }
                        if(isChangeEstado){
                            //actualizar estado de documento
                            documentoExtRecepBean.setCoEsDocEmiMp("5");
                            documentoExtRecepBean.setCoUseMod(pcoUseMod);
                            vReturn=mesaPartesDao.updEstadoDocumentoExt(documentoExtRecepBean);
                            if(vReturn.equals("OK")){
                                //ACTUALIZAR FECHA EXPEDIENTE
    //                                    vReturn=mesaPartesDao.updFechaExpedienteMP(documentoExtRecepBean.getCoUseMod(),documentoExtRecepBean.getNuAnnExp(),documentoExtRecepBean.getNuSecExp());
    //                                    if(vReturn.equals("OK")){                                    
                                    vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi(), esDocEmi);
                                    if(!vReturn.equals("OK")){
                                        vReturn = "Error insertando Auditoria.";
                                        throw new validarDatoException(vReturn);
                                    }
    //                                    }else{
    //                                        throw new validarDatoException("Error actualizando fecha Expediente.");
    //                                    }
                            }else{
                                throw new validarDatoException("Error cambiando a Estado En Registro.");
                            }                                
                        }
                    }else{
                        throw new validarDatoException("Operación no Permitida.");
                    }
                }                
            }else{
              throw new validarDatoException("Documento no Existe.");  
            }
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION A REGISTRADO.");
        }
        return vReturn;
    }    
    
    @Override
    public String getVerificaPdfDoc(String pnuAnn, String pnuEmi, String ptiCap){
        String vReturn = "NO_OK";
        try {
            DocumentoObjBean docObjBean = emiDocumentoAdmDao.getPropiedadesArchivo(pnuAnn, pnuEmi, ptiCap);
            if (docObjBean != null) {
                if (docObjBean.getTipoDoc() != null && docObjBean.getTipoDoc().equals("PDF")) {
                    vReturn = "OK";
                } else {
                    vReturn = "Para la Firmar del documento tiene que estar en PDF ";
                }
            } else {
                vReturn = "Error en verificar formato del documento";
            }

        } catch (Exception e) {
            vReturn = "Error en verificar formato del documento";
        }

        return vReturn;
    }
    
    @Override
    public String anularDocumentoExtRecep(DocumentoExtRecepBean documentoExtRecepBean, Usuario usuario){
        String vReturn = "NO_OK";
        String pcoUsedMod=documentoExtRecepBean.getCoUseMod();
        String pesDocEmi=documentoExtRecepBean.getCoEsDocEmiMp();
        String pnuAnn=documentoExtRecepBean.getNuAnn();
        String pnuEmi=documentoExtRecepBean.getNuEmi();
        try {
            if(pnuAnn!=null&&pnuAnn.trim().length()>0&&pnuEmi!=null&&pnuEmi.trim().length()>0){
                if(pesDocEmi!=null&&pesDocEmi.equals("5")){
                    documentoExtRecepBean = mesaPartesDao.getDocumentoExtRecBasic(pnuAnn, pnuEmi);    
                    if(documentoExtRecepBean==null){
                      vReturn="Documento no Exite.";    
                    }else{                    
                        pesDocEmi = documentoExtRecepBean.getCoEsDocEmiMp();
                        if (pesDocEmi!=null&&pesDocEmi.equals("5")) {
                            vReturn = emiDocumentoAdmDao.verificarDocumentoLeido(documentoExtRecepBean.getNuAnn(),documentoExtRecepBean.getNuEmi());
                            if (vReturn != null && vReturn.equals("0")) {
                                documentoExtRecepBean.setCoUseMod(pcoUsedMod);
                                documentoExtRecepBean.setCoEsDocEmiMp("9");//anulado
                                vReturn = mesaPartesDao.anularDocumentoExtRecep(documentoExtRecepBean);
                                if(vReturn.equals("OK")){
                                    vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi(), pesDocEmi);
                                    if(!vReturn.equals("OK")){
                                        vReturn = "Error insertando Auditoria.";
                                    }                                
                                }else{
                                    vReturn = "Error anulando documento.";
                                }                                
                            } else {
                                vReturn = "Documento ya fue leido por los Destinatarios";
                            }
                        } else {
                            vReturn = "Operación no Permitida.";
                        }                     
                    }
                }else{
                        vReturn = "Operación no Permitida.";
                }
            }else{
              vReturn = "Documento no Existe.";  
            }
        } catch (Exception e) {
            vReturn = e.getMessage();
        }
        return vReturn;
    }
    
    @Override
    public Map getNewDocExtRecepAtender(DocumentoBean docRecBean){
        Map map=null;
        DocumentoExtRecepBean docExterno;
        List<ReferenciaDocExtRecepBean> lsRef;
        String esDocEmi="5";
        try {
            if(docRecBean!=null){
                if(docRecBean.getNuAnn()!=null&&docRecBean.getNuAnn().trim().length()>0&&
                   docRecBean.getNuEmi()!=null&&docRecBean.getNuEmi().trim().length()>0&&
                   docRecBean.getNuDes()!=null&&docRecBean.getNuDes().trim().length()>0&&
                   docRecBean.getEsDocRec()!=null&&docRecBean.getEsDocRec().trim().length()>0&&
                   !docRecBean.getEsDocRec().equals("0")&&!docRecBean.getEsDocRec().equals("9")){
                    docExterno=mesaPartesDao.getDocumentoExtRecepNew(docRecBean.getCoDepEmi());
                    if(docExterno!=null){
                        map=new HashMap();
                        docExterno.setCoEsDocEmiMp(esDocEmi);
                        docExterno.setNuFolios("0");
                        docExterno.setTiEmi("03");
                        docExterno.setDeAsu(docRecBean.getDeAsu());
                        docExterno.setNuDiaAte("0");
                        //lista referencia
                        lsRef = new ArrayList<ReferenciaDocExtRecepBean>();
                        ReferenciaDocExtRecepBean ref=mesaPartesDao.getRefAtenderDocExtRec(docRecBean.getNuAnn(), docRecBean.getNuEmi());
                        if (ref!=null){
                            ref.setNuDes(docRecBean.getNuDes());
                        }                        
                        lsRef.add(ref);

                        map.put("docExterno", docExterno);
                        map.put("lsReferencia", lsRef);                        
                    }                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return map;        
    }
    
    /*@Override
    public String getNuDiaAteTupa(String pcoProceso){
        String[] vReturn;
        String coRespuesta="0";
        StringBuilder retval = new StringBuilder();       
        try {
            vReturn = mesaPartesDao.getNuDiaAteTupa(pcoProceso);
            retval.append("{\"coRespuesta\":\"");
            if(vReturn!=null&&vReturn[0].equals("OK")){
                coRespuesta="1";
                retval.append(coRespuesta);
                retval.append("\",");                
                retval.append("\"nuDiaAte\":\"");
                retval.append(vReturn[1].toString());                     
                retval.append("\"");
            }else{
                retval.append(coRespuesta);
                retval.append("\"");                
            }
            retval.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval.toString();
    }*/
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String changeToParaVerificar(DocumentoExtRecepBean documentoExtRecepBean, Usuario usuario) throws Exception {
        String vReturn = "NO_OK";
        String pnuAnn=documentoExtRecepBean.getNuAnn();
        String pnuEmi=documentoExtRecepBean.getNuEmi();
        String pcoUseMod = documentoExtRecepBean.getCoUseMod();
        try {
            if(pnuAnn!=null&&pnuAnn.trim().length()>0&&pnuEmi!=null&&pnuEmi.trim().length()>0){
                documentoExtRecepBean=mesaPartesDao.getDocumentoExtRecBasic(pnuAnn, pnuEmi);
                if(documentoExtRecepBean==null){
                  throw new validarDatoException("Documento no Exite.");    
                }else{
                  //verificamos si el documento esta en registro
                    String esDocEmi=documentoExtRecepBean.getCoEsDocEmiMp();
                    //if(esDocEmi!=null&&esDocEmi.equals("5")){//en registro
                    if(esDocEmi!=null&&(esDocEmi.equals("5") || esDocEmi.equals("B"))){//en registro o para revisión /*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR */
                        //Verificamos si el Documento esta en PDF
                        String vpfd=getVerificaPdfDoc(documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi(),null);
                        if (vpfd.equals("OK")){
                            //actualizar estado de documento
                            documentoExtRecepBean.setCoEsDocEmiMp("7");
                            documentoExtRecepBean.setCoUseMod(pcoUseMod);
                            vReturn=mesaPartesDao.updEstadoDocumentoExt(documentoExtRecepBean);
                            if(vReturn.equals("OK")){
                                //ACTUALIZAR FECHA EXPEDIENTE
//                                vReturn=mesaPartesDao.updFechaExpedienteMP(documentoExtRecepBean.getCoUseMod(),documentoExtRecepBean.getNuAnnExp(),documentoExtRecepBean.getNuSecExp());
//                                if(vReturn.equals("OK")){
                                    vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi(), esDocEmi);
                                    if(!vReturn.equals("OK")){
                                        vReturn = "Error insertando Auditoria.";
                                        throw new validarDatoException(vReturn);
                                    }
//                                }else{
//                                    throw new validarDatoException("Error actualizando fecha Expediente.");
//                                }
                            }else{
                                throw new validarDatoException("Error cambiando a Estado Para Verificar.");
                            }
                        }else{
                            throw new validarDatoException("El Documento tiene que estar en PDF");
                        }                     
                    }else{
                        throw new validarDatoException("Operación no Permitida.");
                    }
                }                
            }else{
              throw new validarDatoException("Documento no Existe.");  
            }
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION PARA VERIFICAR.");
        }
        return vReturn;
    }    
    
    @Override
    public String getProcesoExpediente(String pcoProceso){    
        String coRespuesta="0";
        StringBuilder retval = new StringBuilder();
        String vretval = null;
        try {
            ProcesoExpBean proceso = mesaPartesDao.getProcesoExpediente(pcoProceso);
            retval.append("{\"coRespuesta\":\"");
            if(proceso!=null){
                Writer strWriter = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(strWriter, proceso.getDeAsunto());
                vretval = strWriter.toString();                  
                coRespuesta="1";
                retval.append(coRespuesta);
                retval.append("\",");                
                retval.append("\"nuDiaAte\":\"");
                retval.append(proceso.getNuDiasPlazo());
                retval.append("\",");                
                retval.append("\"deAsunto\":");
                retval.append(vretval);
            }else{
                retval.append(coRespuesta);
                retval.append("\"");                
            }
            retval.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval.toString();
    }
    
    @Override
    public List<DestinatarioDocumentoEmiBean> getLsDestinoNewDocExtMesaPartes(){
        List<DestinatarioDocumentoEmiBean> list = new ArrayList<DestinatarioDocumentoEmiBean>();
        try {
//            DestinatarioDocumentoEmiBean destino = mesaPartesDao.getEmpleadoDestinoDocExtMp("1211");
            DestinatarioDocumentoEmiBean destino = mesaPartesDao.getEmpleadoDestinoDocExtMp(null);            
            if (destino!=null) {
                destino.setDeTramite(Constantes.DE_TRAMITE_ORIGINAL);
                destino.setCoTramite(Constantes.CO_TRAMITE_ORIGINAL);
                destino.setCoPrioridad("1");
                destino.setAccionBD("INS");
                destino.setCoTipoDestino("01");                
                list.add(destino);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;        
    }
    
    @Override
    public List<RemitenteBean> getAllDependencias(){
        List<RemitenteBean> list = null;
        try{
            list = mesaPartesDao.getAllDependencias();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<RequisitoBean> getAllRequisitoExpediente(String nuAnnExp, String nuSecExp, String codProceso){
        List<RequisitoBean> list = null;
        try {
            list = mesaPartesDao.getAllRequisitoExpediente(nuAnnExp, nuSecExp, codProceso);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String guardarReqExpDocExtRec(List<RequisitoBean> lRequisito, String nuAnnExp, String nuSecExp, String coProceso, String coUsuario) throws Exception {
        String vReturn = "NO_OK";
        try {
            for (RequisitoBean req : lRequisito) {
                vReturn = mesaPartesDao.updReqExpedienteDocExtRec(req, nuAnnExp, nuSecExp, coProceso, coUsuario);                
                if (!"OK".equals(vReturn)) {
                    throw new validarDatoException("Error actualizando Requisito.");
                }                  
            }
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION.");
        }
        return vReturn;
    }
    
    @Override
    public String getEsDocEmi(String nuAnn, String nuEmi){
        String esDocEmi = null;
        try {
            DocumentoExtRecepBean doc = mesaPartesDao.getDocumentoExtRecBasic(nuAnn, nuEmi);
            if(doc!=null&&doc.getCoEsDocEmiMp()!=null&&doc.getCoEsDocEmiMp().trim().length()>0){
                esDocEmi = doc.getCoEsDocEmiMp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return esDocEmi;
    }
    
    @Override
    public String getPkExpDocExtOrigen(String pkEmi){
        String pkExp = null;
        try {
            String pNuAnn = pkEmi.substring(0, 4);
            String pNuEmi = pkEmi.substring(4, 14);
            //comentado ya que generaba coneccion pegada en app server, y ya no respondia en server,
            //verificar alternativa para esta query de recursividad.
            pkExp = null;//mesaPartesDao.getPkExpDocExtOrigen(pNuAnn, pNuEmi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pkExp;
    }
    
    @Override
    public List<RequisitoBean> getAllRequisitoExpediente(String nuAnnExp, String nuSecExp){
        List<RequisitoBean> list = null;
        try {
            list = mesaPartesDao.getAllRequisitoExpediente(nuAnnExp, nuSecExp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }    

    @Override
    public List<DocumentoBean> getListaReporteVoucher(DocumentoBean documento) {
        return mesaPartesDao.getListaReporteBusquedaVoucher(documento);
    }

    @Override
    public ReporteBean getGenerarReporteVoucher(Map parametros, DocumentoBean documento) {
        ReporteBean objReporte = new ReporteBean();
        String eserror = "1"; // si
        String coRespuesta = "";
        String deRespuesta = "";
        String prutaReporte  =  "";
        String deNoDoc = "";
        String nombreArchivo = "";
        String coReporte = "";
        try {          
            
            String extensionArch;
            int tipoArchivo = 0;
            byte[] archivo;
            
            String fecha = Utility.getInstancia().getStringFechaYYYYMMDDHHmm(new Date());  
            
            List lista = null;
            try {
                lista = getListaReporteVoucher(documento);
                eserror = "0";
            } catch(Exception ex) {               
                eserror = "1";
                deRespuesta = ex.getMessage();
            }
            if (lista.size() == 0) {                 
                 coRespuesta = "1";
                 deRespuesta = "No existe información para generar reporte.";
                 throw new Exception(deRespuesta);
            }
            
            coReporte = "TDR15";
            extensionArch = ".pdf";
            tipoArchivo = 2; // PDF

            String rutaJasper = documento.getRutaReporteJasper() + "/" + coReporte + ".jasper";
            
            documento.setRutaReporteJasper(rutaJasper);
            
            nombreArchivo = Utilidades.generateRandomNumber(10);
            prutaReporte = "VOUCHER_" + nombreArchivo + extensionArch;
            deNoDoc = "temp|VOUCHER_" + fecha + extensionArch;
            
            Utilidades util = new Utilidades();
            archivo = util.GenerarReporteLista(documento.getRutaReporteJasper(), lista, coReporte, parametros, tipoArchivo);
            
            if (archivo == null) {
                eserror = "1";
                coRespuesta = "1";
                deRespuesta  =  "No se generó correctamente el archivo.";
                throw new Exception("No se generó correctamente el archivo.");                
            }
            
            String rutaBase = applicationProperties.getRutaTemporal() + "/" + prutaReporte;
            
            File tmpFile = new File(rutaBase);
            FileOutputStream fos = new FileOutputStream(tmpFile);
            fos.write(archivo);
            fos.flush();
            fos.close();
            String rutaUrl = "reporte?coReporte=";
            prutaReporte = rutaUrl + prutaReporte;
            
            if (eserror.equals("0")) {
                coRespuesta = eserror;
            } else {
                coRespuesta = "1";
            }
            
        } catch (JRException ex) {
            Logger.getLogger(DocumentoExtRecepServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror = "1";
            deRespuesta = ex.getMessage();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentoExtRecepServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror = "1";
            deRespuesta = ex.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(DocumentoExtRecepServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror = "1";
            deRespuesta = ex.getMessage();
        } finally {
            objReporte.setcoRespuesta(coRespuesta);
            objReporte.setdeRespuesta(deRespuesta);
            objReporte.setnoUrl(prutaReporte);
            objReporte.setnoDoc(deNoDoc);
            return objReporte;
        }
    }
    
    @Override
    public ReporteBean getGenerarReporte(BuscarDocumentoExtRecepBean buscarDocumentoExtRecepBean, Map parametros) {
        ReporteBean objReporte = new ReporteBean();
        String eserror = "1"; // si
        String coRespuesta = "";
        String deRespuesta = "";
        String prutaReporte  =  "";
        String deNoDoc = "";
        String nombreArchivo = "";
        String coReporte = "";
        try {          
            
            String extensionArch;
            int tipoArchivo = 0;
            byte[] archivo;
            
            String fecha = Utility.getInstancia().getStringFechaYYYYMMDDHHmm(new Date());  
            
            List lista = null;
            try {
                lista = getListaReporte(buscarDocumentoExtRecepBean);
                eserror = "0";
            } catch(Exception ex) {               
                eserror = "1";
                deRespuesta=ex.getMessage();
            }
            if (lista.size() == 0) {                 
                 coRespuesta = "1";
                 deRespuesta = "No existe información para generar reporte.";
                 throw new Exception(deRespuesta);
            }
            if (buscarDocumentoExtRecepBean.getFormatoReporte().equalsIgnoreCase("PDF")) {
                coReporte = "TDR32";
                extensionArch = ".pdf";
                tipoArchivo = 2;
            } else {
                coReporte = "TDR32_XLS";
                extensionArch = ".xls";
                tipoArchivo = 1;
            }
            String rutaJasper = buscarDocumentoExtRecepBean.getRutaReporteJasper() + "/" + coReporte + ".jasper";
            
            buscarDocumentoExtRecepBean.setRutaReporteJasper(rutaJasper);
            
            nombreArchivo = Utilidades.generateRandomNumber(10);
            prutaReporte = "RECEPDOCEXT_" + nombreArchivo + extensionArch;
            deNoDoc = "temp|RECEPDOCEXT_" + fecha+extensionArch;
            
            Utilidades util = new Utilidades();
            archivo = util.GenerarReporteLista(buscarDocumentoExtRecepBean.getRutaReporteJasper(), lista, coReporte, parametros, tipoArchivo);
            
            if (archivo == null) {
                eserror = "1";
                coRespuesta = "1";
                deRespuesta  =  "No se generó correctamente el archivo.";
                throw new Exception("No se generó correctamente el archivo.");
                
            }
            
            String rutaBase = applicationProperties.getRutaTemporal() + "/" + prutaReporte;
            
            File tmpFile = new File(rutaBase);
            FileOutputStream fos = new FileOutputStream(tmpFile);
            fos.write(archivo);
            fos.flush();
            fos.close();
            String rutaUrl = "reporte?coReporte=";
            prutaReporte = rutaUrl + prutaReporte;
            
            if (eserror.equals("0")) {
                coRespuesta = eserror;
            } else {
                coRespuesta = "1";
            }
            
        } catch(JRException ex) {
            Logger.getLogger(DocumentoExtRecepServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror = "1";
            deRespuesta = ex.getMessage();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentoExtRecepServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror = "1";
            deRespuesta = ex.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(DocumentoExtRecepServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror = "1";
            deRespuesta = ex.getMessage();
        } finally {
            objReporte.setcoRespuesta(coRespuesta);
            objReporte.setdeRespuesta(deRespuesta);
            objReporte.setnoUrl(prutaReporte);
            objReporte.setnoDoc(deNoDoc);
            return objReporte;
        }
    }
    
    @Override
    public List<DocumentoExtRecepBean> getListaReporte(BuscarDocumentoExtRecepBean buscarDocumentoExtRecepBean) {
        return mesaPartesDao.getListaReporteBusqueda(buscarDocumentoExtRecepBean);
    }
    
    @Override
    public List<CiudadanoBean> getLstCiudadanos(String sDescCiudadano){
        List<CiudadanoBean> list = null;
        try {
            list = commonQueryDao.getLstCiudadanos(sDescCiudadano);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<DocumentoBean> obtenerHojaDeRuta(String pnuAnn, String pnuEmi, String pTipo) {
        return mesaPartesDao.obtenerHojaDeRuta(pnuAnn, pnuEmi, pTipo);
    }
    
    @Override
    public DocumentoExtRecepBean getDocumentoExtRecep(String pnuAnn, String pnuEmi) {
        return mesaPartesDao.getDocumentoExtRecep(pnuAnn, pnuEmi);
    }
    
    @Override
    public List<DocumentoBean> obtenerSituacionExpediente(String pnuAnn, String pnuEmi) {
        return mesaPartesDao.obtenerSituacionExpediente(pnuAnn, pnuEmi);
    }

    @Override
    public List<CiudadanoBean> getLstCiudadanos(String sApePatCiudadano, String sApeMatCiudadano, String sNombreCiudadano) {
        List<CiudadanoBean> list = null;
        try {
            list = commonQueryDao.getLstCiudadanos(sApePatCiudadano, sApeMatCiudadano, sNombreCiudadano);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /*Enlace Expedientes - Hermes 17/09/2018*/
    @Override
    public List<ReferenciaDocExtRecepBean> getLstEnlaceExpedientes(String pnuAnn, String pnuEmi) {
        List<ReferenciaDocExtRecepBean> list = null;
        try {
            list = mesaPartesDao.getLstEnlaceExpedientes(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;        
    }

    @Override
    public int getExisteExpedientesEnlazados(String pkEmi) {
        return mesaPartesDao.getExisteExpedientesEnlazados(pkEmi);
    }
    /*Enlace Expedientes - Hermes 17/09/2018*/
    /*-- INICIO [HPB] VALIDA DATOS DEL DOCUMENTO EN EXPEDIENTE 14/07/20--*/
    @Override
    public String getValidaExpEquals(DocumentoExtRecepBean documentoExtRecepBean) throws Exception {
        String coRespuesta="0";
        StringBuilder retval = new StringBuilder();
        int cantidad =0;
        
        try {
            cantidad = mesaPartesDao.getValidaExpEquals(documentoExtRecepBean);
            
            retval.append("{\"coRespuesta\":\"");
            if(cantidad > 0){                 
                coRespuesta="1";
                retval.append(coRespuesta);
                retval.append("\",");                
                retval.append("\"cantidad\":\"");
                retval.append(cantidad);
                retval.append("\"");                 
            }else{
                retval.append(coRespuesta);
                retval.append("\"");                
            }
            retval.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("RETVAL: "+retval.toString());
        return retval.toString();
    }
    /*-- FIN [HPB] VALIDA DATOS DEL DOCUMENTO EN EXPEDIENTE 14/07/20--*/
    /*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String changeToParaSubsanar(DocumentoExtRecepBean documentoExtRecepBean, Usuario usuario) throws Exception {
        String vReturn = "NO_OK";
        String pnuAnn=documentoExtRecepBean.getNuAnn();
        String pnuEmi=documentoExtRecepBean.getNuEmi();
        String pcoUseMod = documentoExtRecepBean.getCoUseMod();
        try {
            if(pnuAnn!=null&&pnuAnn.trim().length()>0&&pnuEmi!=null&&pnuEmi.trim().length()>0){
                documentoExtRecepBean=mesaPartesDao.getDocumentoExtRecBasic(pnuAnn, pnuEmi);
                if(documentoExtRecepBean==null){
                  throw new validarDatoException("Documento no Existe.");    
                }else{
                  //verificamos si el documento esta en registro
                    String esDocEmi=documentoExtRecepBean.getCoEsDocEmiMp();
                    if(esDocEmi!=null&&esDocEmi.equals("5")){//en registro
                        //Verificamos si el Documento esta en PDF
                        String vpfd=getVerificaPdfDoc(documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi(),null);
                        if (vpfd.equals("OK")){
                            //verificamos si ingreso alguna observación al expediente
                            ExpedienteDocExtRecepBean expedienteDocExtRecepBean = mesaPartesDao.getObsExpediente(documentoExtRecepBean.getNuAnnExp(), documentoExtRecepBean.getNuSecExp());
                            vReturn = expedienteDocExtRecepBean.getDeDetalle();
                            if (vReturn != null && !vReturn.equals("")) {
                                //actualizar estado de documento
                                documentoExtRecepBean.setCoEsDocEmiMp("A");
                                documentoExtRecepBean.setCoUseMod(pcoUseMod);
                                vReturn=mesaPartesDao.updEstadoDocumentoExt(documentoExtRecepBean);
                                if(vReturn.equals("OK")){
                                    vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi(), esDocEmi);
                                    if(!vReturn.equals("OK")){
                                        vReturn = "Error insertando Auditoria.";
                                        throw new validarDatoException(vReturn);
                                    }else{
                                        String correoRemitente = documentoExtRecepBean.getDeCorreo(); 
                                        if(correoRemitente != null && !correoRemitente.equals("")){
                                            vReturn = mesaPartesDao.enviarNotificacion(documentoExtRecepBean);
                                        }
                                        if(!vReturn.equals("OK")){
                                            vReturn = "Hubo un error al enviar la notificación.";
                                            throw new validarDatoException(vReturn);
                                        }
                                    }
                                }else{
                                    throw new validarDatoException("Error cambiando a Estado Para Subsanar.");
                                }
                            }else{
                                throw new validarDatoException("Debe ingresar alguna observacion al expediente para continuar.");
                            }
                        }else{
                            throw new validarDatoException("El Documento tiene que estar en PDF");
                        }                     
                    }else{
                        throw new validarDatoException("Operación no Permitida.");
                    }
                }                
            }else{
              throw new validarDatoException("Documento no Existe.");  
            }
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION PARA SUBSANAR.");
        }
        return vReturn;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String updObservacionExpedienteBean(String nuAnnExp, String nuSecExp, String deObservacion, String coUsuario) throws Exception {
        String vReturn = "NO_OK";
        try {
            ExpedienteDocExtRecepBean expedienteBean = new ExpedienteDocExtRecepBean();
            expedienteBean.setNuAnnExp(nuAnnExp);
            expedienteBean.setNuSecExp(nuSecExp);
            expedienteBean.setDeDetalle(deObservacion);
            expedienteBean.setUsCreaAudi(coUsuario);
            vReturn = mesaPartesDao.updObservacionExpedienteBean(expedienteBean);
            if (!"OK".equals(vReturn)) {
                throw new validarDatoException("Error actualizando Requisito.");
            } 
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION.");
        }
        return vReturn;
    }

    @Override
    public ExpedienteDocExtRecepBean getObsExpediente(String nuAnnExp, String nuSecExp) {
        ExpedienteDocExtRecepBean expedienteDocExtRecepBean = null;
        try {
            expedienteDocExtRecepBean = mesaPartesDao.getObsExpediente(nuAnnExp, nuSecExp);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return expedienteDocExtRecepBean;
    }
    /*[HPB-22/07/21] Fin - Adicionar estado POR SUBSANAR */

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String changeToArchivar(DocumentoExtRecepBean documentoExtRecepBean, Usuario usuario) throws Exception {
        String vReturn = "NO_OK";
        String pnuAnn=documentoExtRecepBean.getNuAnn();
        String pnuEmi=documentoExtRecepBean.getNuEmi();
        String pcoUseMod = documentoExtRecepBean.getCoUseMod();
        try {
            if(pnuAnn!=null&&pnuAnn.trim().length()>0&&pnuEmi!=null&&pnuEmi.trim().length()>0){
                documentoExtRecepBean=mesaPartesDao.getDocumentoExtRecBasic(pnuAnn, pnuEmi);
                if(documentoExtRecepBean==null){
                  throw new validarDatoException("Documento no Existe.");    
                }else{
                  //verificamos si el documento esta en registro
                    String esDocEmi=documentoExtRecepBean.getCoEsDocEmiMp();
                    if(esDocEmi!=null&&esDocEmi.equals("B")){//para revisión
                        //Verificamos si el Documento esta en PDF
                        String vpfd=getVerificaPdfDoc(documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi(),null);
                        if (vpfd.equals("OK")){
                            //verificamos si ingreso alguna observación al expediente
                            ExpedienteDocExtRecepBean expedienteDocExtRecepBean = mesaPartesDao.getObsExpediente(documentoExtRecepBean.getNuAnnExp(), documentoExtRecepBean.getNuSecExp());
                            vReturn = expedienteDocExtRecepBean.getDeDetalle();
                            if (vReturn != null && !vReturn.equals("")) {
                                //actualizar estado de documento
                                documentoExtRecepBean.setCoEsDocEmiMp("4");
                                documentoExtRecepBean.setCoUseMod(pcoUseMod);
                                vReturn=mesaPartesDao.updEstadoDocumentoExt(documentoExtRecepBean);
                                if(vReturn.equals("OK")){
                                    vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoExtRecepBean.getNuAnn(), documentoExtRecepBean.getNuEmi(), esDocEmi);
                                    if(!vReturn.equals("OK")){
                                        vReturn = "Error insertando Auditoria.";
                                        throw new validarDatoException(vReturn);
                                    }else{
                                        String correoRemitente = documentoExtRecepBean.getDeCorreo(); 
                                        if(correoRemitente != null && !correoRemitente.equals("")){
                                            vReturn = mesaPartesDao.enviarNotificacionDeArchivado(documentoExtRecepBean);
                                        }
                                        if(!vReturn.equals("OK")){
                                            vReturn = "Hubo un error al enviar la notificación de archivamiento.";
                                            throw new validarDatoException(vReturn);
                                        }
                                    }
                                }else{
                                    throw new validarDatoException("Error cambiando a Estado Para Subsanar.");
                                }
                            }else{
                                throw new validarDatoException("Debe ingresar alguna observacion al expediente para continuar.");
                            }
                        }else{
                            throw new validarDatoException("El Documento tiene que estar en PDF");
                        }                     
                    }else{
                        throw new validarDatoException("Operación no Permitida.");
                    }
                }                
            }else{
              throw new validarDatoException("Documento no Existe.");  
            }
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION PARA SUBSANAR.");
        }
        return vReturn;
    }
    /* [HPB] Inicio 27/02/23 CLS-087-2022 */
    @Override
    public ReporteBean getGenerarReporteHojaRuta(BuscarDocumentoExtRecepBean buscarDocumentoExtRecepBean, Map parametros, String pNuAnn, String pNuEmi, String pTipo) {
        ReporteBean objReporte = new ReporteBean();
        String eserror = "1"; // si
        String coRespuesta = "";
        String deRespuesta = "";
        String prutaReporte  =  "";
        String deNoDoc = "";
        String nombreArchivo = "";
        String coReporte = "";
        try {          
            
            String extensionArch;
            int tipoArchivo = 0;
            byte[] archivo;
            
            String fecha = Utility.getInstancia().getStringFechaYYYYMMDDHHmm(new Date());  
            
            List lista = null;
            try {
                lista = obtenerHojaDeRuta(pNuAnn,pNuEmi, pTipo);
                eserror = "0";
            } catch(Exception ex) {               
                eserror = "1";
                deRespuesta=ex.getMessage();
            }
            if (lista.isEmpty()) {                 
                 coRespuesta = "1";
                 deRespuesta = "No existe información para generar reporte.";
                 throw new Exception(deRespuesta);
            }
            if (buscarDocumentoExtRecepBean.getFormatoReporte().equalsIgnoreCase("PDF")) {
                coReporte = "TDR35";
                extensionArch = ".pdf";
                tipoArchivo = 2;
            } else {
                coReporte = "TDR35_XLS";
                extensionArch = ".xls";
                tipoArchivo = 1;
            }
            String rutaJasper = buscarDocumentoExtRecepBean.getRutaReporteJasper() + "/" + coReporte + ".jasper";
            
            buscarDocumentoExtRecepBean.setRutaReporteJasper(rutaJasper);
            
            nombreArchivo = Utilidades.generateRandomNumber(10);
            prutaReporte = "RECEPDOCEXT_" + nombreArchivo + extensionArch;
            deNoDoc = "temp|RECEPDOCEXT_" + fecha+extensionArch;
            
            Utilidades util = new Utilidades();
            archivo = util.GenerarReporteLista(buscarDocumentoExtRecepBean.getRutaReporteJasper(), lista, coReporte, parametros, tipoArchivo);
            
            if (archivo == null) {
                eserror = "1";
                coRespuesta = "1";
                deRespuesta  =  "No se generó correctamente el archivo.";
                throw new Exception("No se generó correctamente el archivo.");               
            }
            
            String rutaBase = applicationProperties.getRutaTemporal() + "/" + prutaReporte;
            
            File tmpFile = new File(rutaBase);
            FileOutputStream fos = new FileOutputStream(tmpFile);
            fos.write(archivo);
            fos.flush();
            fos.close();
            String rutaUrl = "reporte?coReporte=";
            prutaReporte = rutaUrl + prutaReporte;
            
            if (eserror.equals("0")) {
                coRespuesta = eserror;
            } else {
                coRespuesta = "1";
            }
            
        } catch(JRException ex) {
            Logger.getLogger(DocumentoExtRecepServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror = "1";
            deRespuesta = ex.getMessage();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentoExtRecepServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror = "1";
            deRespuesta = ex.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(DocumentoExtRecepServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror = "1";
            deRespuesta = ex.getMessage();
        } finally {
            objReporte.setcoRespuesta(coRespuesta);
            objReporte.setdeRespuesta(deRespuesta);
            objReporte.setnoUrl(prutaReporte);
            objReporte.setnoDoc(deNoDoc);
            return objReporte;
        }
    }
    /* [HPB] Fin 27/02/23 CLS-087-2022 */
}
