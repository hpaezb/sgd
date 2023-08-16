package pe.gob.onpe.tramitedoc.service.impl;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.libreria.exception.validarDatoException;
import pe.gob.onpe.tramitedoc.bean.AudiEstadosMovDocBean;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBean;
import pe.gob.onpe.tramitedoc.bean.CiudadanoBeanNew;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioOtroOrigenBean;
import pe.gob.onpe.tramitedoc.bean.DestinoBean;
import pe.gob.onpe.tramitedoc.bean.DetalleEnvioDeCorreoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoProyectoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoVoBoBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteBean;
import pe.gob.onpe.tramitedoc.bean.MotivoBean;
import pe.gob.onpe.tramitedoc.bean.ProveedorBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaEmiDocBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaRemitoBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteEmiBean;
import pe.gob.onpe.tramitedoc.bean.TblRemitosBean;
import pe.gob.onpe.tramitedoc.bean.TrxDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.dao.CommonQueryDao;
import pe.gob.onpe.tramitedoc.dao.DocumentoTriggerDao;
import pe.gob.onpe.tramitedoc.dao.DocumentoVoBoDao;
import pe.gob.onpe.tramitedoc.dao.EmiDocumentoAdmDao;
import pe.gob.onpe.tramitedoc.dao.MaestrosDao;
import pe.gob.onpe.tramitedoc.service.ActualizaResumenService;
import pe.gob.onpe.tramitedoc.service.AuditoriaMovimientoDocService;
import pe.gob.onpe.tramitedoc.service.EmiDocumentoAdmService;
import pe.gob.onpe.tramitedoc.service.NotificacionService;
import pe.gob.onpe.tramitedoc.service.ValidarFirmaService;
import pe.gob.onpe.tramitedoc.util.ArchivoTemporal;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/*interoperabilidad*/
import pe.gob.onpe.tramitedoc.bean.DatosInterBean;
/*interoperabilidad*/

@Service("emiDocumentoAdmService")
public class EmiDocumentoAdmServiceImp implements EmiDocumentoAdmService {

    private static Logger logger=Logger.getLogger("SGDEmiDocumentoAdmService");

    @Autowired
    private EmiDocumentoAdmDao emiDocumentoAdmDao;
    
    @Autowired
    private DocumentoTriggerDao documentoTriggerDao;

    @Autowired
    private MaestrosDao maestrosDao;
    
    @Autowired
    private CommonQueryDao commonQueryDao;
    
    @Autowired
    private ActualizaResumenService actualizaResumenService;

    @Autowired
    private ValidarFirmaService validarFirmaService;
    
    @Autowired
    AuditoriaMovimientoDocService audiMovDoc;  
    
    @Autowired
    private DocumentoVoBoDao docVoboDao;    
    
//    @Autowired
//    private NotificacionService notiService;    
    @Override
    public List<DocumentoEmiBean> getDocumentosBuscaEmiAdm(BuscarDocumentoEmiBean buscarDocumentoEmiBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List<DocumentoEmiBean> list = null;
        try {
            list = emiDocumentoAdmDao.getDocumentosBuscaEmiAdm(buscarDocumentoEmiBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public DocumentoEmiBean getDocumentoEmitido(String pnuAnn, String pnuEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        DocumentoEmiBean documentoEmiBean = null;
        try {
            documentoEmiBean = emiDocumentoAdmDao.getDocumentoEmitido(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return documentoEmiBean;
    }

    public DocumentoEmiBean getDocumentoEmiAdm(String pnuAnn, String pnuEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        DocumentoEmiBean documentoEmiBean = null;
        try {
            documentoEmiBean = emiDocumentoAdmDao.getDocumentoEmiAdm(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return documentoEmiBean;
    }

    public ExpedienteBean getExpDocumentoEmitido(String pnuAnnExp, String pnuSecExp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ExpedienteBean expedienteBean = null;
        try {
            expedienteBean = emiDocumentoAdmDao.getExpDocumentoEmitido(pnuAnnExp, pnuSecExp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return expedienteBean;
    }

    public EmpleadoBean getEmpleadoLocaltblDestinatario(String pcoDependencia) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        EmpleadoBean empleadoBean = null;
        try {
            empleadoBean = emiDocumentoAdmDao.getEmpleadoLocaltblDestinatario(pcoDependencia);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return empleadoBean;
    }

    public List<EmpleadoBean> getPersonalDestinatario(String pcoDepen) {
        List<EmpleadoBean> list = null;
        try {
            list = emiDocumentoAdmDao.getPersonalDestinatario(pcoDepen);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public List<MotivoBean> getLstMotivoxTipoDocumento(String pcoDepen, String pcoTipDoc) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List<MotivoBean> list = null;
        try {
            list = emiDocumentoAdmDao.getLstMotivoxTipoDocumento(pcoDepen, pcoTipDoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DocumentoBean> getLstDocEmitidoRef(String pcoEmpEmi, String pcoDepen, String pannio, String ptiDoc, String pnuDoc) {
        List<DocumentoBean> list = null;
        try {
            list = emiDocumentoAdmDao.getLstDocEmitidoRef(pcoEmpEmi, pcoDepen, pannio, ptiDoc, pnuDoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DocumentoBean> getLstDocRecepcionadoRef(String pcoDepen, String pannio, String ptiDoc, String pnuDoc,String inMesaPartes) {
        List<DocumentoBean> list = null;
        try {
            if(inMesaPartes!=null&&inMesaPartes.equals("1")){
                list = emiDocumentoAdmDao.getLstDocRecepcionadoRefMp(pcoDepen, pannio, ptiDoc, pnuDoc);                
            }else{
                list = emiDocumentoAdmDao.getLstDocRecepcionadoRef(pcoDepen, pannio, ptiDoc, pnuDoc);                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    //@Override
    public List<DestinatarioDocumentoEmiBean> _getLstDestintariotlbEmi(String pnuAnn, String pnuEmi) {
        List<DestinatarioDocumentoEmiBean> list = null;
        try {
            list = emiDocumentoAdmDao.getLstDestintariotlbEmi(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public HashMap getLstDestintariotlbEmi(String pnuAnn, String pnuEmi) {
        List<DestinatarioDocumentoEmiBean> list = null;
        HashMap map = null;
        try {
            list = emiDocumentoAdmDao.getLstDestintariotlbEmi(pnuAnn, pnuEmi);
            map = getLstDestinatario(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    private HashMap getLstDestinatario(List<DestinatarioDocumentoEmiBean> list) {
        HashMap map = new HashMap();
        List lst1 = new ArrayList();
        List lst2 = new ArrayList();
        List lst3 = new ArrayList();
        List lst4 = new ArrayList();
        for (DestinatarioDocumentoEmiBean dest : list) {
            if (dest != null) {
                String coTipoDest = dest.getCoTipoDestino();
                if (coTipoDest.equals("01")) {
                    lst1.add(dest);
                } else if (coTipoDest.equals("02")) {
                    lst2.add(dest);
                } else if (coTipoDest.equals("03")) {
                    lst3.add(dest);
                } else if (coTipoDest.equals("04")) {
                    lst4.add(dest);
                }
            }
        }
        map.put("lst1", lst1);
        map.put("lst2", lst2);
        map.put("lst3", lst3);
        map.put("lst4", lst4);
        return map;
    }

    @Override
    public List<ReferenciaBean> getLstDocumReferenciatblEmi(String pnuAnn, String pnuEmi) {
        List<ReferenciaBean> list = null;
        try {
            list = emiDocumentoAdmDao.getLstDocumReferenciatblEmi(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public String getTipoDestinatarioEmi(String pnuAnn, String pnuEmi) {
        String retval = null;
        try {
            retval = emiDocumentoAdmDao.getTipoDestinatarioEmi(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retval;
    }

    @Override
    public List<DestinatarioOtroOrigenBean> getLstOtrosOrigenesAgrega(String pdescripcion) {
        List<DestinatarioOtroOrigenBean> list = null;
        try {
            list = commonQueryDao.getLstOtrosOrigenes(pdescripcion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
 @Override
    public List<CiudadanoBean> getLstCiudadano(String nombres)
    {
        List<CiudadanoBean> ciudadanoBean = null;
        try {
            ciudadanoBean = commonQueryDao.getLstCiudadanos(nombres);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ciudadanoBean;
    }
//    @Override
//    @Transactional (propagation = Propagation.REQUIRED,rollbackFor=Exception.class) 
//    public synchronized String grabaDocumentoEmi(TrxDocumentoEmiBean trxDocumentoEmiBean,String pcrearExpediente) throws Exception{
//       String vReturn = "NO_OK";
//        try{
//            String pnuAnn = trxDocumentoEmiBean.getNuAnn();
//            String pnuEmi = trxDocumentoEmiBean.getNuEmi();
//            String pcoUserMod = trxDocumentoEmiBean.getCoUserMod();
//            String pcempCodEmp = trxDocumentoEmiBean.getCempCodEmp();
//            DocumentoEmiBean documentoEmiBean = trxDocumentoEmiBean.getDocumentoEmiBean();
//            ExpedienteBean expedienteEmiBean = trxDocumentoEmiBean.getExpedienteEmiBean();
//            RemitenteEmiBean remitenteEmiBean = trxDocumentoEmiBean.getRemitenteEmiBean();
//            ArrayList<ReferenciaEmiDocBean> lstReferencia = trxDocumentoEmiBean.getLstReferencia();
//            ArrayList<DestinatarioDocumentoEmiBean> lstDestinatario = trxDocumentoEmiBean.getLstDestinatario();            
//            if(pnuAnn != null && pnuEmi != null && !pnuAnn.equals("") && !pnuEmi.equals("")){//UPD
//                if(documentoEmiBean != null || expedienteEmiBean != null || remitenteEmiBean != null){
//                    vReturn = emiDocumentoAdmDao.updDocumentoEmiBean(pnuAnn,pnuEmi,documentoEmiBean, expedienteEmiBean, remitenteEmiBean,pcoUserMod);
//                    if ("NO_OK".equals(vReturn)) {
//                        throw new validarDatoException("Error al Actualizar Documento");
//                    }  
//                }
//                for (ReferenciaEmiDocBean ref : lstReferencia) {
//                    String sAccionBD = ref.getAccionBD();
//                    if("INS".equals(sAccionBD)){
//                        vReturn = emiDocumentoAdmDao.insReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error Agregando Referencia");
//                        } 
//                    }else if("UPD".equals(sAccionBD)){
//                        vReturn = emiDocumentoAdmDao.updReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error Actualizando Referencia");
//                        }                         
//                    }else if("DEL".equals(sAccionBD)){
//                        vReturn = emiDocumentoAdmDao.delReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error Borrando Referencia");
//                        }                         
//                    }
//                }
//                for (DestinatarioDocumentoEmiBean dest : lstDestinatario) {
//                    String sAccionBD = dest.getAccionBD();
//                    if("INS".equals(sAccionBD)){
//                        vReturn = emiDocumentoAdmDao.insDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error Agregando Destinatario");
//                        }  
//                    }else if("UPD".equals(sAccionBD)){
//                        vReturn = emiDocumentoAdmDao.updDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error Actualizando Destinatario");
//                        }                         
//                    }else if("DEL".equals(sAccionBD)){
//                        vReturn = emiDocumentoAdmDao.delDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error Borrando Destinatario");
//                        }                        
//                    }
//                }
//            }else{//INSERTAR
//                if(documentoEmiBean != null){
//                    if(pcrearExpediente.equals("1")){
//                        ExpedienteBean expedienteBean = new ExpedienteBean();
//                        expedienteBean.setUsCreaAudi(pcempCodEmp);
//                        expedienteBean.setFeExp(documentoEmiBean.getFeEmiCorta());
//                        expedienteBean.setCoDepExp(documentoEmiBean.getCoDepEmi());
//                        expedienteBean.setNuAnnExp(documentoEmiBean.getFeEmiCorta().trim().substring(6,10));
//                        vReturn = emiDocumentoAdmDao.insExpedienteBean(expedienteBean);
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error al Grabar Expediente");
//                        }
//                        trxDocumentoEmiBean.setNuAnnExp(expedienteBean.getNuAnnExp());
//                        trxDocumentoEmiBean.setFeExp(expedienteBean.getFeExp());
//                        trxDocumentoEmiBean.setNuExpediente(expedienteBean.getNuExpediente());
//                        trxDocumentoEmiBean.setNuSecExp(expedienteBean.getNuSecExp());
//                        documentoEmiBean.setNuAnnExp(expedienteBean.getNuAnnExp());
//                        documentoEmiBean.setNuSecExp(expedienteBean.getNuSecExp());
//                    }
//                    documentoEmiBean.setCoUseMod(pcoUserMod);
//                    vReturn = emiDocumentoAdmDao.insDocumentoEmiBean(documentoEmiBean, expedienteEmiBean, remitenteEmiBean);
//                    if ("NO_OK".equals(vReturn)) {
//                        throw new validarDatoException("Error al Grabar Documento");
//                    }
//                    trxDocumentoEmiBean.setNuEmi(documentoEmiBean.getNuEmi());
//                    trxDocumentoEmiBean.setNuCorEmi(documentoEmiBean.getNuCorEmi());
//                    pnuEmi = documentoEmiBean.getNuEmi();
//                }
//                for (ReferenciaEmiDocBean ref : lstReferencia) {
//                    vReturn = emiDocumentoAdmDao.insReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
//                    if ("NO_OK".equals(vReturn)) {
//                        throw new validarDatoException("Error Agregando Referencia");
//                    } 
//                }
//                for (DestinatarioDocumentoEmiBean dest : lstDestinatario) {
//                    vReturn = emiDocumentoAdmDao.insDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
//                    if ("NO_OK".equals(vReturn)) {
//                        throw new validarDatoException("Error Agregando Destinatario");
//                    }  
//                }                
//            }
//        }catch (validarDatoException e) { 
//            throw e; 
//         } 
//         catch (Exception e) { 
//            e.printStackTrace(); 
//            throw new validarDatoException("ERROR EN TRANSACCION"); 
//         }
//       return vReturn;
//    }
    @Override
    public CiudadanoBean getCiudadano(String pnuDoc) {
        CiudadanoBean ciudadanoBean = null;
        CiudadanoBeanNew ciudadanoBeanNew = null;/* HPB - [13/01/2020] - Integrar PIDE */
        try {
            String vIndicadorPide = commonQueryDao.obtenerValorParametro("IN_PIDE"); /* HPB - [13/01/2020] - Integrar PIDE */
            ciudadanoBean = commonQueryDao.getCiudadano(pnuDoc);
            if(ciudadanoBean==null){
                if(vIndicadorPide.equals("1")){
                    ciudadanoBeanNew = commonQueryDao.getLstCiudadanoStored(pnuDoc, "11");
                    if(ciudadanoBeanNew!=null){
                        ciudadanoBean = commonQueryDao.getLstCiudadanoAni(pnuDoc, "11");
                    }else{
                        ciudadanoBean = null;
                    }             
                }else{
                    ciudadanoBean = null;
                }            
            }            
            /* HPB - [FIN - 13/01/2020] - Integrar PIDE */
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ciudadanoBean;
    }

    @Override
    public List<ProveedorBean> getLstProveedoresAgrega(String pRazonSocial) {
        List<ProveedorBean> list = null;
        try {
            list = commonQueryDao.getLstProveedores(pRazonSocial);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public String getJsonAfterGrabaDocumentoEmi(String snuAnn, String snuEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String retval = null;
        List<DestinatarioDocumentoEmiBean> list1;
        List<ReferenciaBean> list2;
        try {
            list1 = emiDocumentoAdmDao.getLstDestintariotlbEmi(snuAnn, snuEmi);
            list2 = emiDocumentoAdmDao.getLstDocumReferenciatblEmi(snuAnn, snuEmi);
            Writer strWriter = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(strWriter, getJsonTblDocEmiAdm(list1, list2));
            retval = strWriter.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retval;
    }

    @Override
    public String _getJsonAfterGrabaDocumentoEmi(String snuAnn, String snuEmi) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String retval = null;
        List<DestinatarioDocumentoEmiBean> list1;
        List<ReferenciaBean> list2;
        try {
            list1 = emiDocumentoAdmDao.getLstDestintariotlbEmi(snuAnn, snuEmi);
            list2 = emiDocumentoAdmDao.getLstDocumReferenciatblEmi(snuAnn, snuEmi);
            retval = _getJsonTblDocEmiAdm(list1, list2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retval;
    }

    private String _getJsonTblDocEmiAdm(List<DestinatarioDocumentoEmiBean> lDest, List<ReferenciaBean> lRef) {
        String userDataJSON = null;
        StringBuilder retval = new StringBuilder();       

        try {
            HashMap map = getLstDestinatario(lDest);
            Writer strWriter = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(strWriter, lRef);
            userDataJSON = strWriter.toString();

            String coRespuesta = "1";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"lsReferencia\":");
            retval.append(userDataJSON);
            retval.append(",\"lsDestInstitu\":");
            strWriter = new StringWriter();
            mapper = new ObjectMapper();
            mapper.writeValue(strWriter, (List<DestinatarioDocumentoEmiBean>) map.get("lst1"));
            userDataJSON = strWriter.toString();
            retval.append(userDataJSON);

            retval.append(",\"lsDestProveedor\":");
            strWriter = new StringWriter();
            mapper = new ObjectMapper();
            mapper.writeValue(strWriter, (List<DestinatarioDocumentoEmiBean>) map.get("lst2"));
            userDataJSON = strWriter.toString();
            retval.append(userDataJSON);

            retval.append(",\"lsDestCiudadano\":");
            strWriter = new StringWriter();
            mapper = new ObjectMapper();
            mapper.writeValue(strWriter, (List<DestinatarioDocumentoEmiBean>) map.get("lst3"));
            userDataJSON = strWriter.toString();
            retval.append(userDataJSON);

            retval.append(",\"lsDestOtros\":");
            strWriter = new StringWriter();
            mapper = new ObjectMapper();
            mapper.writeValue(strWriter, (List<DestinatarioDocumentoEmiBean>) map.get("lst4"));
            userDataJSON = strWriter.toString();
            retval.append(userDataJSON);
            retval.append("}");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return retval.toString();
    }

    private StringBuilder getJsonTblDocEmiAdm(List<DestinatarioDocumentoEmiBean> lDest, List<ReferenciaBean> lRef) {
        boolean bandera = false;
        StringBuilder retval = new StringBuilder();
        HashMap map = getLstDestinatario(lDest);
        String coRespuesta = "1";
        retval.append("{\"coRespuesta\":\"");
        retval.append(coRespuesta);
        retval.append("\",\"lsDestInstitu\":");
        retval.append("[");
        for (DestinatarioDocumentoEmiBean dest : (List<DestinatarioDocumentoEmiBean>) map.get("lst1")) {
            retval.append("{\"deDependencia\":\"");
            retval.append(dest.getDeDependencia());
            retval.append("\",\"coDependencia\":\"");
            retval.append(dest.getCoDependencia());
            retval.append("\",\"coLocal\":\"");
            retval.append(dest.getCoLocal());
            retval.append("\",\"deLocal\":\"");
            retval.append(dest.getDeLocal());
            retval.append("\",\"coEmpleado\":\"");
            retval.append(dest.getCoEmpleado());
            retval.append("\",\"deEmpleado\":\"");
            retval.append(dest.getDeEmpleado());
            retval.append("\",\"coTramite\":\"");
            retval.append(dest.getCoTramite());
            retval.append("\",\"deTramite\":\"");
            retval.append(dest.getDeTramite());
            retval.append("\",\"deIndicaciones\":\"");
            retval.append(dest.getDeIndicaciones());
            retval.append("\",\"coPrioridad\":\"");
            retval.append(dest.getCoPrioridad());
            retval.append("\",\"nuDes\":\"");
            retval.append(dest.getNuDes());
            retval.append("\"},");
            bandera = true;
        }
        if (bandera) {
            retval.deleteCharAt(retval.length() - 1);
            bandera = false;
        }
        retval.append("]");

        retval.append(",\"lsDestProveedor\":");
        retval.append("[");
        for (DestinatarioDocumentoEmiBean dest : (List<DestinatarioDocumentoEmiBean>) map.get("lst2")) {
            retval.append("{\"nuRuc\":\"");
            retval.append(dest.getNuRuc());
            retval.append("\",\"deProveedor\":\"");
            retval.append(dest.getDeProveedor());
            retval.append("\",\"nuDes\":\"");
            retval.append(dest.getNuDes());
            retval.append("\"},");
            bandera = true;
        }
        if (bandera) {
            retval.deleteCharAt(retval.length() - 1);
            bandera = false;
        }
        retval.append("]");

        retval.append(",\"lsDestCiudadano\":");
        retval.append("[");
        for (DestinatarioDocumentoEmiBean dest : (List<DestinatarioDocumentoEmiBean>) map.get("lst3")) {
            retval.append("{\"nuDni\":\"");
            retval.append(dest.getNuDni());
            retval.append("\",\"deCiudadano\":\"");
            retval.append(dest.getDeCiudadano());
            retval.append("\",\"coLocal\":\"");
            retval.append(dest.getCoLocal());
            retval.append("\",\"nuDes\":\"");
            retval.append(dest.getNuDes());
            retval.append("\"},");
            bandera = true;
        }
        if (bandera) {
            retval.deleteCharAt(retval.length() - 1);
            bandera = false;
        }
        retval.append("]");

        retval.append(",\"lsDestOtros\":");
        retval.append("[");
        for (DestinatarioDocumentoEmiBean dest : (List<DestinatarioDocumentoEmiBean>) map.get("lst4")) {
            retval.append("{\"coOtroOrigen\":\"");
            retval.append(dest.getCoOtroOrigen());
            retval.append("\",\"deNombreOtroOrigen\":\"");
            retval.append(dest.getDeNombreOtroOrigen());
            retval.append("\",\"deTipoDocOtroOrigen\":\"");
            retval.append(dest.getDeTipoDocOtroOrigen());
            retval.append("\",\"nuDocOtroOrigen\":\"");
            retval.append(dest.getNuDocOtroOrigen());
            retval.append("\",\"nuDes\":\"");
            retval.append(dest.getNuDes());
            retval.append("\"},");
            bandera = true;
        }
        if (bandera) {
            retval.deleteCharAt(retval.length() - 1);
            bandera = false;
        }
        retval.append("]");

        retval.append(",\"lsReferencia\":");
        retval.append("[");
        for (ReferenciaBean ref : lRef) {
            retval.append("{\"coRef\":\"");
            retval.append(ref.getCoRef());
            retval.append("\",\"nuAnnRef\":\"");
            retval.append(ref.getNuAnnRef());
            retval.append("\",\"nuEmiRef\":\"");
            retval.append(ref.getNuEmiRef());
            retval.append("\",\"nuDesRef\":\"");
            retval.append(ref.getNuDesRef());
            retval.append("\",\"liNuDoc\":\"");
            retval.append(ref.getLiNuDoc());
            retval.append("\",\"liTipDoc\":\"");
            retval.append(ref.getLiTipDoc());
            retval.append("\",\"feEmiCorta\":\"");
            retval.append(ref.getFeEmiCorta());
            retval.append("\",\"tipDocRef\":\"");
            retval.append(ref.getTipDocRef());
            retval.append("\",\"coTipDocAdm\":\"");
            retval.append(ref.getCoTipDocAdm());
            retval.append("\",\"nuDes\":\"");
            retval.append(ref.getNuDes());
            retval.append("\"},");
            bandera = true;
        }
        if (bandera) {
            retval.deleteCharAt(retval.length() - 1);
        }
        retval.append("]");
        retval.append("}");

        return retval;
    }

    @Override
    public DocumentoEmiBean getDocumentoEmiAdmNew(String sEstadoDocEmi, String codDependencia) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        DocumentoEmiBean documentoEmiBean = null;
        try {
            documentoEmiBean = emiDocumentoAdmDao.getDocumentoEmiAdmNew(sEstadoDocEmi, codDependencia);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return documentoEmiBean;
    }

//    @Override
//    public ExpedienteBean grabarExpedienteBean(String pcodUserMod,String feEmi,String coDepEmi,String deDocSig){
//        ExpedienteBean expedienteBean = new ExpedienteBean();
//        try{
//            expedienteBean.setUsCreaAudi(pcodUserMod);
//            expedienteBean.setFeExp(feEmi);
//            expedienteBean.setCoDepExp(coDepEmi);
//            expedienteBean.setNuExpediente(deDocSig);
//            expedienteBean.setNuAnnExp(feEmi.trim().substring(6,10));
//            if(!emiDocumentoAdmDao.insExpedienteBean(expedienteBean).equals("OK")){
//               expedienteBean = null; 
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return expedienteBean;
//    }
    @Override
    public String anularDocumentoEmiAdm(DocumentoEmiBean documentoEmiBean,Usuario usuario) {
        String vReturn = "NO_OK";
        String pcoUsedMod = documentoEmiBean.getCoUseMod();
        try {
            documentoEmiBean = emiDocumentoAdmDao.getEstadoDocumento(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi());
            String pesDocEmi = documentoEmiBean.getEsDocEmi();
            if (pesDocEmi != null && (pesDocEmi.equals("5") || pesDocEmi.equals("7"))) {
                documentoEmiBean.setCoUseMod(pcoUsedMod);
                vReturn = emiDocumentoAdmDao.verificarDocumentoLeido(documentoEmiBean.getNuAnn(),documentoEmiBean.getNuEmi());
                if (vReturn != null && vReturn.equals("0")) {
                    //verificar si el documento tiene vistos buenos o esta observado
                    vReturn=docVoboDao.existeVistoBuenoDocAdm(documentoEmiBean.getNuAnn(),documentoEmiBean.getNuEmi());
                    if(vReturn.equals("0")){
                        documentoEmiBean.setEsDocEmi("9");//anulado
                        vReturn = emiDocumentoAdmDao.anularDocumentoEmiAdm(documentoEmiBean);
                        if(vReturn.equals("OK")){
                            vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), pesDocEmi);
                            if(!vReturn.equals("OK")){
                                vReturn = "Error insertando Auditoria.";
                            }                                
                        }else{
                            vReturn = "Error anulando documento.";
                        }                        
                    }else{
                        vReturn = "Doc. con Visto Bueno u Observado.";
                    } 
                } else {
                    vReturn = "Documento ya fue leido por los Destinatarios";
                }
            } else {
                vReturn = "Documento ya fue leido por los Destinatarios";
            }
        } catch (Exception e) {
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    public String getLstDestintarioAgregaGrupo(String pcoGrupo,String pcoTipDoc) {
        StringBuilder retval = new StringBuilder();
//        String vRespuesta;
        String coRespuesta;
        List<DestinatarioDocumentoEmiBean> list = null;

        try {
            list = emiDocumentoAdmDao.getLstDestinatarioGrupo(pcoGrupo,pcoTipDoc);
            if (list != null) {
                Writer strWriter = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();

                coRespuesta = "1";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"lsDestInstitu\":");
                strWriter = new StringWriter();
                mapper = new ObjectMapper();
                mapper.writeValue(strWriter, list);
                retval.append(strWriter.toString());
                retval.append("}");
            }
        } catch (Exception e) {
            coRespuesta = "0";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append(e.getMessage());
            retval.append("\"}");
        }

        return retval.toString();
    }

    @Override
    public String cargaDocEmi(DocumentoObjBean docObjBean) {
        String vReturn = "NO_OK";
        
        if(docObjBean!=null){
            try {
                docObjBean.setTipoDoc(docObjBean.getNombreArchivo().substring(docObjBean.getNombreArchivo().lastIndexOf('.') + 1));
                docObjBean.setNombreArchivo(docObjBean.getNombreArchivo().substring(docObjBean.getNombreArchivo().lastIndexOf('|') + 1));
                
                //Validamos el PDF
                //validarFirmaService.getValidaCertificadoEmp(docObjBean.getNumeroSecuencia(), "01341466");
                
                byte[] archivoByte = ArchivoTemporal.leerArchivo(docObjBean.getNumeroSecuencia());
                docObjBean.setDocumento(archivoByte);
                docObjBean.setNuTamano((int) Math.round(((double) archivoByte.length) / 1024));
                    
                DocumentoEmiBean documentoEmiBean = emiDocumentoAdmDao.getEstadoDocumento(docObjBean.getNuAnn(), docObjBean.getNuEmi());
                if(documentoEmiBean!=null && (documentoEmiBean.getEsDocEmi().equals("5")||documentoEmiBean.getEsDocEmi().equals("7"))){
                    vReturn = emiDocumentoAdmDao.updDocumentoObj(docObjBean);
                }else{
                    vReturn = "El Documento a cambiado de estado.";
                }
            } catch (Exception ex) {
                StringBuilder mensaje = new StringBuilder();
                vReturn = "Error en Leer Documento de Repositorio";
                mensaje.append(docObjBean.getNumeroSecuencia()+":");
                mensaje.append(docObjBean.getNuAnn()+"."+docObjBean.getNuEmi());
                logger.error(mensaje,ex);            
            }
        }else{
            vReturn = "Error en Carga de Documento";
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("cargaDocEmi:Objeto docObjBean NULO");
            logger.error(mensaje);            
        }
        return vReturn;
    }

    @Override
    public String verificaNroDocumentoEmiDuplicado(DocumentoEmiBean documentoEmiBean) {
        String vReturn = "NO_OK";
//        String pesDocEmi = documentoEmiBean.getEsDocEmi();
//        if(pesDocEmi!=null && (pesDocEmi.equals("5") || pesDocEmi.equals("7") || pesDocEmi.equals("0"))){
        try {
            vReturn = emiDocumentoAdmDao.getCanDocumentoEmiDuplicados(documentoEmiBean);
            if (vReturn.equals("0")) {
                vReturn = "OK";
            } else {
                vReturn = "Número de documento duplicado....!";
            }
        } catch (Exception e) {
            vReturn = e.getMessage();
        }
//        }else{
//            vReturn = "Documento ya fue leido por los Destinatarios";
//        }
        return vReturn;
    }

    @Override
    public List<ReferenciaBean> getLstDocumReferenciaAtiendeDeriva(DocumentoBean documentoRecepBean) {
        List<ReferenciaBean> list = new ArrayList<ReferenciaBean>();
        ReferenciaBean referenciaBean = new ReferenciaBean();
        try {
            referenciaBean.setNuAnnRef(documentoRecepBean.getNuAnn());
            referenciaBean.setCoTipDocAdm(documentoRecepBean.getCoTipDocAdm());
            referenciaBean.setTipDocRef("rec");
            referenciaBean.setLiNuDoc(documentoRecepBean.getNuDoc());
            referenciaBean.setFeEmiCorta(documentoRecepBean.getFeEmiCorta2());
            referenciaBean.setNuEmiRef(documentoRecepBean.getNuEmi());
            referenciaBean.setNuDesRef(documentoRecepBean.getNuDes());
            referenciaBean.setAccionBd("INS");
            referenciaBean.setCoRef("");
            list.add(referenciaBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<EmpleadoBean> getPersonalEditDocAdmEmision(String pcoDepEmi) {
        List<EmpleadoBean> list = null;
        try {
            list = emiDocumentoAdmDao.getPersonalEditDocAdmEmision(pcoDepEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public String changeToProyecto(DocumentoEmiBean documentoEmiBean,Usuario usuario) {
        String vReturn = "NO_OK";
        String pesDocEmi;
        String pcoUseMod = documentoEmiBean.getCoUseMod();
        String pcoEmpRes = documentoEmiBean.getCoEmpRes();
        try {
            documentoEmiBean = emiDocumentoAdmDao.getEstadoDocumento(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi());
            if(documentoEmiBean!=null && (documentoEmiBean.getEsDocEmi().equals("7")||documentoEmiBean.getEsDocEmi().equals("0"))){
                pesDocEmi = documentoEmiBean.getEsDocEmi();
                vReturn = emiDocumentoAdmDao.verificarDocumentoLeido(documentoEmiBean.getNuAnn(),documentoEmiBean.getNuEmi());
                if (vReturn != null && vReturn.equals("0")) {
                    if(pesDocEmi.equals("0") && !documentoEmiBean.getCoEmpEmi().equals(pcoEmpRes)){
                        vReturn = "El Documento solo puede ser cambiado por el Funcionario que lo Firmó.";
                    }else{
                        boolean ischangeToProyecto=false;
                        if(pesDocEmi.equals("0")){
                            ischangeToProyecto=true;
                        }else{
                            //verificar si el documento tiene vistos buenos o esta observado
                            vReturn=docVoboDao.existeVistoBuenoDocAdm(documentoEmiBean.getNuAnn(),documentoEmiBean.getNuEmi());
                            if(vReturn.equals("0")){
                                ischangeToProyecto=true;
                            }else{
                                if(documentoEmiBean.getCoEmpEmi().equals(usuario.getCempCodemp())||documentoEmiBean.getCoEmpRes().equals(usuario.getCempCodemp())){
                                    ischangeToProyecto=true;
                                }else{
                                    vReturn = "Doc. con Visto Bueno u Observado.";
                                }
                            }                            
                        }    
                        if(ischangeToProyecto){
                            documentoEmiBean.setEsDocEmi("5");
                            documentoEmiBean.setCoUseMod(pcoUseMod);
                            
                            vReturn = emiDocumentoAdmDao.updEstadoDocumentoEmiAdm(documentoEmiBean);  
                            
                            //aqui
                            
                            if(vReturn.equals("OK")){
                               vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), pesDocEmi);
                               if(!vReturn.equals("OK")){
                                   vReturn = "Error insertando Auditoria.";
                               }
                            }else{
                                vReturn = "Error al Cambiar a Proyecto el Documento.";
                            }                               
                        }
                    }
                } else {
                    vReturn = "Documento ya fue leido por los Destinatarios.";
                }
            }else{
                vReturn = "El Documento a cambiado de estado.";
            }
        } catch (Exception e) {
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String changeToDespacho(DocumentoEmiBean documentoEmiBean, Usuario usuario) throws Exception {
        String vReturn = "NO_OK";
        String pesDocEmi = documentoEmiBean.getEsDocEmi();
        String coUseMod=documentoEmiBean.getCoUseMod();
        String ticap=documentoEmiBean.getTiCap();
        String nuSecFirma=documentoEmiBean.getNuSecuenciaFirma();        
        if (pesDocEmi != null && !pesDocEmi.equals("")) {
            try {
                documentoEmiBean=emiDocumentoAdmDao.getEstadoDocumento(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi());
                if(documentoEmiBean!=null&&documentoEmiBean.getEsDocEmi()!=null&&documentoEmiBean.getEsDocEmi().equals("5")){
                    pesDocEmi = documentoEmiBean.getEsDocEmi();
                    documentoEmiBean.setCoUseMod(coUseMod);
                    if (nuSecFirma != null) {
                        DocumentoObjBean docObjBean = new DocumentoObjBean();

                        docObjBean.setNuAnn(documentoEmiBean.getNuAnn());
                        docObjBean.setNuEmi(documentoEmiBean.getNuEmi());
                        docObjBean.setTiCap(ticap);
                        docObjBean.setNombreArchivo(nuSecFirma);
                        docObjBean.setNumeroSecuencia(nuSecFirma);
                        docObjBean.setCoUseMod(documentoEmiBean.getCoUseMod());
                        vReturn = cargaDocEmi(docObjBean);
                        if (!vReturn.equals("OK")) {
                            throw new validarDatoException("Error al Cargar Documento");
                        }
                    }
                    documentoEmiBean.setEsDocEmi("7");
                    vReturn = emiDocumentoAdmDao.updChangeToDespacho(documentoEmiBean);
                    if (!vReturn.equals("OK")) {
                        throw new validarDatoException(vReturn);
                    }

                    vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), pesDocEmi);
                    if(!vReturn.equals("OK")){
                        vReturn = "Error insertando Auditoria.";
                        throw new validarDatoException(vReturn);
                    }                
                    //ProbarFSilva
//                    vReturn=notiService.procesarNotificacion(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), coUseMod);
//                    if(!vReturn.equals("OK")){
//                        throw new validarDatoException(vReturn);
//                    }
                    
                }else{
                    vReturn="El Documento a cambiado de estado.";
                }
            } catch (validarDatoException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw new validarDatoException("ERROR EN TRANSACCION PARA DESPACHO");
            }
        } else {
            vReturn = "Documento no esta en Despacho.";
        }
        return vReturn;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String changeToEmitido(DocumentoEmiBean documentoEmiBean, String prutaDoc, Usuario usuario,String nroRucInstitucion) throws Exception {
        String vReturn = "NO_OK";
        String pesDocEmi = documentoEmiBean.getEsDocEmi();
        String coUseMod=documentoEmiBean.getCoUseMod();        
        String pnuDocEmi = documentoEmiBean.getNuDocEmi();
        String ticap=documentoEmiBean.getTiCap();
        String nuSecFirma=documentoEmiBean.getNuSecuenciaFirma();                
        if (pesDocEmi != null && !pesDocEmi.equals("")) {
            try {
                documentoEmiBean=emiDocumentoAdmDao.getEstadoDocumento(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi());                
                if(documentoEmiBean!=null&&documentoEmiBean.getEsDocEmi()!=null&&documentoEmiBean.getEsDocEmi().equals("7")){
                    if(!documentoEmiBean.getCoEmpEmi().equals(usuario.getCempCodemp())){
                        //vReturn = "El Documento solo puede ser emitido por: Firmado Por";
                        throw new validarDatoException("El Documento solo puede ser emitido por: <strong>Firmado Por</strong>");
                    } else{
                        pesDocEmi = documentoEmiBean.getEsDocEmi();
                        documentoEmiBean.setCoUseMod(coUseMod);
                        documentoEmiBean.setNuDocEmi(pnuDocEmi);
                        DocumentoObjBean docObjBean = new DocumentoObjBean();

                        docObjBean.setNuAnn(documentoEmiBean.getNuAnn());
                        docObjBean.setNuEmi(documentoEmiBean.getNuEmi());
                        docObjBean.setTiCap(ticap);
                        docObjBean.setNombreArchivo(prutaDoc);
                        docObjBean.setNumeroSecuencia(nuSecFirma);
                        docObjBean.setCoUseMod(documentoEmiBean.getCoUseMod());

                        String esOblFirma = commonQueryDao.getInFirmaDoc(documentoEmiBean.getCoDepEmi(),documentoEmiBean.getCoTipDocAdm());                        
                        vReturn = validarFirmaService.getValidaCertificadoEmp(docObjBean.getNumeroSecuencia(),usuario.getNuDni(),nroRucInstitucion);
                        if (vReturn.equals("NO_OK")) {
                            throw new validarDatoException("Firma no pertenece a Usuario");
                        }else if(vReturn.equals("")&&esOblFirma.equals("F")){
                            throw new validarDatoException("Necesita Firmar el Documento.");
                        }                    
                        
                        vReturn = cargaDocEmi(docObjBean);
                        if (!vReturn.equals("OK")) {
                            throw new validarDatoException("Error al Cargar Documento");
                        }
                        documentoEmiBean.setEsDocEmi("0");
                        documentoEmiBean.setNuCorDoc("1");
                        vReturn = emiDocumentoAdmDao.updEstadoDocumentoEmitido(documentoEmiBean);
                        if (!vReturn.equals("OK")) {
                            throw new validarDatoException(vReturn);
                        }
                        TblRemitosBean tblRemitosBean = emiDocumentoAdmDao.getDatosDocumento(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi());
                        if (!tblRemitosBean.getMsgResult().equals("OK")) {
                            throw new validarDatoException(vReturn);
                        }
                        vReturn = documentoTriggerDao.actualizarEstadoAfterEmitirDocumento(tblRemitosBean.getNuAnn(), tblRemitosBean.getNuEmi(), tblRemitosBean.getCoDepEmi(), tblRemitosBean.getFeEmi());
                        if (!vReturn.equals("OK")) {
                            throw new validarDatoException(vReturn);
                        }
                        vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), pesDocEmi);
                        if(!vReturn.equals("OK")){
                            vReturn = "Error insertando Auditoria.";
                            throw new validarDatoException(vReturn);
                        }                    
                        //verificar si el documento tiene vistos buenos pendeintes o esta observado
                        vReturn=docVoboDao.existeVistoBuenoPendienteDocAdm(tblRemitosBean.getNuAnn(), tblRemitosBean.getNuEmi());
                        if(vReturn.equals("0")){
                            vReturn="OK";
                        }else{
                            throw new validarDatoException("Doc. con Visto Bueno Pendiente u Observado.");
                        }
                    }
                }else{
                }
            } catch (validarDatoException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw new validarDatoException("ERROR EN TRANSACCION DE EMISION");
            }
        } else {
            vReturn = "Documento no esta Listo.";
        }
        return vReturn;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String changeToEmitidoAlta(DocumentoEmiBean documentoEmiBean) throws Exception {
        String vReturn = "NO_OK";
        String pesDocEmi = documentoEmiBean.getEsDocEmi();
        if (pesDocEmi != null && !pesDocEmi.equals("")) {
            try {

                //Verificamos si el Documento esta en PDF
                String vpfd=getVerificaPdfDoc(documentoEmiBean.getNuAnn(),documentoEmiBean.getNuEmi(),documentoEmiBean.getTiCap());
                
                if (!vpfd.equals("OK")){
                    throw new validarDatoException("El Documento tiene que estar en PDF");
                }
                
                documentoEmiBean.setEsDocEmi("0");
                documentoEmiBean.setNuCorDoc("1");
                vReturn = emiDocumentoAdmDao.updEstadoDocumentoEmitido(documentoEmiBean);
                if (!vReturn.equals("OK")) {
                    throw new validarDatoException(vReturn);
                }
                TblRemitosBean tblRemitosBean = emiDocumentoAdmDao.getDatosDocumento(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi());
                if (!tblRemitosBean.getMsgResult().equals("OK")) {
                    throw new validarDatoException(vReturn);
                }
                vReturn = documentoTriggerDao.actualizarEstadoAfterEmitirDocumento(tblRemitosBean.getNuAnn(), tblRemitosBean.getNuEmi(), tblRemitosBean.getCoDepEmi(), tblRemitosBean.getFeEmi());
                if (!vReturn.equals("OK")) {
                    throw new validarDatoException(vReturn);
                }                        
            } catch (validarDatoException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw new validarDatoException("ERROR EN TRANSACCION DE EMISION");
            }
        } else {
            vReturn = "Documento no esta Listo.";
        }
        return vReturn;
    }
    
    @Override
    public String changeToEnvioNotificacion(DocumentoEmiBean documentoEmiBean,Usuario usuario) {
        String vReturn = "NO_OK";
        
        //String pnuEmi = documentoEmiBean.getNuEmi();
        //String pnuAnn = documentoEmiBean.getNuAnn();
        //String nCodAccion = documentoEmiBean.getEsDocEmi();
        String nCodAccion = documentoEmiBean.getTiDest();
        //String nCodUrgencia = documentoEmiBean.getCoPrioridad();
       
        try {
            
            // if(nCodAccion.equals("0") || nCodAccion.equals("1"))
             //{
//              System.out.println("envio:"+nCodAccion);
              
                documentoEmiBean.setEsDocEmi("0");
                int cCant=emiDocumentoAdmDao.getCantidadAnexo(documentoEmiBean);
//                System.out.println(""+cCant);
                if (cCant==0)
                {
                    vReturn = emiDocumentoAdmDao.updEstadoDocumentoEnvioNotificacion(documentoEmiBean);  
                    if(vReturn.equals("OK")){
                       vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), "0");
                       if(!vReturn.equals("OK")){
                           vReturn = "Error insertando Auditoria.";
                       }
                    }else{
                        vReturn = "Error al Enviar el documento ea la mensajería.:"+vReturn;
                    } 
                }else{
                        vReturn ="NO_OK";// "Error ya tiene algunos archivos adjuntos:"+vReturn;
                    }        
                        
                              
                      
              //}    
            // else
            // {
            // vReturn="OK";
            // }
               
          
        } catch (Exception e) {
            vReturn = e.getMessage();
        }
        return vReturn;
    }

    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String grabaDocumentoEmiAdm(TrxDocumentoEmiBean trxDocumentoEmiBean, String pcrearExpediente,Usuario usuario) throws Exception {
        String vReturn = "NO_OK";
        try {
            String pnuAnn = trxDocumentoEmiBean.getNuAnn();
            String pnuEmi = trxDocumentoEmiBean.getNuEmi();
            String pcoUserMod = trxDocumentoEmiBean.getCoUserMod();
            String pcempCodEmp = trxDocumentoEmiBean.getCempCodEmp();
            DocumentoEmiBean documentoEmiBean = trxDocumentoEmiBean.getDocumentoEmiBean();
            ExpedienteBean expedienteEmiBean = trxDocumentoEmiBean.getExpedienteEmiBean();
            RemitenteEmiBean remitenteEmiBean = trxDocumentoEmiBean.getRemitenteEmiBean();
            ArrayList<ReferenciaEmiDocBean> lstReferencia = trxDocumentoEmiBean.getLstReferencia();
            ArrayList<DestinatarioDocumentoEmiBean> lstDestinatario = trxDocumentoEmiBean.getLstDestinatario();
            ArrayList<EmpleadoVoBoBean> lstEmpVoBo = trxDocumentoEmiBean.getLstEmpVoBo();
            DocumentoEmiBean documentoEmiBeanBD=null;
            
            //jazanero
            //documentoEmiBean.setNuAnnProyecto(trxDocumentoEmiBean.getNuAnnProyecto());
            //documentoEmiBean.setNuEmiProyecto(trxDocumentoEmiBean.getNuEmiProyecto());
            //jazanero
            
            if (pnuAnn != null && pnuEmi != null && !pnuAnn.equals("") && !pnuEmi.equals("")) {//UPD
                documentoEmiBeanBD = emiDocumentoAdmDao.getEstadoDocumento(pnuAnn, pnuEmi);
                if(documentoEmiBeanBD!=null&&documentoEmiBeanBD.getEsDocEmi()!=null&&(documentoEmiBeanBD.getEsDocEmi().equals("5")||documentoEmiBeanBD.getEsDocEmi().equals("7"))){
                    if (documentoEmiBean != null || expedienteEmiBean != null || remitenteEmiBean != null) {
                        if(documentoEmiBean!=null&&!documentoEmiBean.getCoTipDocAdm().equals(documentoEmiBeanBD.getCoTipDocAdm())){
                            vReturn=this.getInNumeraDocAdm(documentoEmiBean.getCoTipDocAdm());
                            if("NO_OK".equals(vReturn)){
                                throw new validarDatoException("Error al Obtener Indicador de Numeracion");
                            }                            
                            if(vReturn.equals("1")){
                                vReturn=this.getNumeroDocSiguienteAdm(documentoEmiBean.getNuAnn(), documentoEmiBean.getCoDepEmi(), documentoEmiBean.getCoTipDocAdm());
                                if("NO_OK".equals(vReturn)){
                                    throw new validarDatoException("Error al Obtener Numero de Documento.");
                                }
                                documentoEmiBean.setNuDocEmi(vReturn);
                                trxDocumentoEmiBean.setNuDocEmi(vReturn);
                                documentoEmiBean.setNuCorDoc("1");
                            }                            
                        }
                        vReturn = emiDocumentoAdmDao.updDocumentoEmiAdmBean(pnuAnn, pnuEmi, documentoEmiBean, expedienteEmiBean, remitenteEmiBean, pcoUserMod);
                        if ("NO_OK".equals(vReturn)) {
                            throw new validarDatoException("Error al Actualizar Documento");
                        }
                        if (documentoEmiBean != null){
                            trxDocumentoEmiBean.setNuCorEmi(documentoEmiBean.getNuCorEmi());
                            //trxDocumentoEmiBean.setNuCorDoc(documentoEmiBean.getNuCorDoc());
                        }                       
                        
                    }              
                    for (ReferenciaEmiDocBean ref : lstReferencia) {
                        String sAccionBD = ref.getAccionBD();
                        System.out.println("ACCION--> " +sAccionBD);/*[HPB-21/06/21] Campos Auditoria-*/
                        if ("INS".equals(sAccionBD)) {
                            ref.setCoUseCre(pcoUserMod);
                            ref.setCoUseMod(pcoUserMod);/*[HPB-21/06/21] Campos Auditoria-*/
                            vReturn = emiDocumentoAdmDao.insReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error Agregando Referencia");
                            }
                        } else if ("UPD".equals(sAccionBD)) {
                            ref.setCoUseMod(pcoUserMod);/*[HPB-21/06/21] Campos Auditoria-*/
                            vReturn = emiDocumentoAdmDao.updReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error Actualizando Referencia");
                            }
                        } else if ("DEL".equals(sAccionBD)) {
                            ref.setCoUseMod(pcoUserMod);/*[HPB-21/06/21] Campos Auditoria-*/
                            vReturn = emiDocumentoAdmDao.updReferenciaDocumentoEmiAntesEliminar(pnuAnn, pnuEmi, ref);/*[HPB-21/06/21] Campos Auditoria-*/
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error Borrando Referencia");
                            }else{
                                vReturn = emiDocumentoAdmDao.delReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
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
                            vReturn = emiDocumentoAdmDao.insDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error Agregando Destinatario");
                            }
                            /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente--*/
//                            if(documentoEmiBeanBD.getCoTipDocAdm().equals("234")){
//                                expedienteEmiBean = new ExpedienteBean();
//                                expedienteEmiBean.setCoDepExp(documentoEmiBeanBD.getCoDepEmi());
//                                vReturn = emiDocumentoAdmDao.insExtExpedienteBean(pnuAnn, pnuEmi, dest, expedienteEmiBean);
//                                if ("NO_OK".equals(vReturn)) {
//                                    throw new validarDatoException("Error Agregando Extension de Expediente");
//                                }
//                            }
                            /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente--*/
                        } else if ("UPD".equals(sAccionBD)) {
                            vReturn = emiDocumentoAdmDao.updDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error Actualizando Destinatario");
                            }
                            /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente--*/
//                            if(documentoEmiBeanBD.getCoTipDocAdm().equals("234")){
//                                vReturn = emiDocumentoAdmDao.updExtExpedienteBean(pnuAnn, pnuEmi, dest);
//                                if ("NO_OK".equals(vReturn)) {
//                                    throw new validarDatoException("Error Actualizando Extension de Expediente");
//                                }
//                            }                              
                            /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente--*/
                        } else if ("DEL".equals(sAccionBD)) {
                            vReturn = emiDocumentoAdmDao.delDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error Borrando Destinatario");
                            }
                            /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente--*/
//                            if(documentoEmiBeanBD.getCoTipDocAdm().equals("234")){
//                                vReturn = emiDocumentoAdmDao.delExtExpedienteBean(pnuAnn, pnuEmi, dest);
//                                if ("NO_OK".equals(vReturn)) {
//                                    throw new validarDatoException("Error Borrando Extension de Expediente");
//                                }
//                            }                            
                            /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente--*/
                        }
                    }
                                                            
                    for(EmpleadoVoBoBean emp : lstEmpVoBo){
                        String sAccionBD = emp.getAccionBD();
                        if("DEL".equals(sAccionBD)){
                            vReturn = emiDocumentoAdmDao.delPersonalVoBo(pnuAnn, pnuEmi, emp.getCoDependencia(), emp.getCoEmpleado());
                            if (!"OK".equals(vReturn)) {
                                throw new validarDatoException("Error Borrando Personal VB.");
                            }                        
                        }else if("INS".equals(sAccionBD)){
                            vReturn = emiDocumentoAdmDao.insPersonalVoBo(pnuAnn, pnuEmi, emp.getCoDependencia(), emp.getCoEmpleado(), pcoUserMod);
                            if(!"OK".equals(vReturn)){
                                throw new validarDatoException("Error Agregando Personal VB.");
                            }                        
                        }
                    } 
                    
                    DocumentoEmiBean documentoEmiBeanTemp = emiDocumentoAdmDao.getDocumentoEmiAdm(pnuAnn, pnuEmi);
                    
                    if(documentoEmiBeanTemp != null){
                        /**insertar codigo jazanero**/
                        boolean flag = false;
                        if(documentoEmiBeanTemp.getCoTipDocAdm().equals("243") || documentoEmiBeanTemp.getCoTipDocAdm().equals("246")
                                || documentoEmiBeanTemp.getCoTipDocAdm().equals("247")|| documentoEmiBeanTemp.getCoTipDocAdm().equals("012")){//Hermes 11/01/2019 Directiva 2019                    

                            List<DestinatarioDocumentoEmiBean> lstDestinatarioTemp = emiDocumentoAdmDao.getLstDestintariotlbEmi(pnuAnn, pnuEmi);

                            if(lstDestinatarioTemp!=null && lstDestinatarioTemp.size()>0){
                                for (DestinatarioDocumentoEmiBean dest : lstDestinatarioTemp) {
                                    if(dest.getCoTipoDestino()!=null && (!dest.getCoTipoDestino().equals("01"))){
                                            flag = true;
                                            break;
                                    }                        
                                }                                    
                            }
                        }

                        //comentado YPA SIS
                        /*
                        vReturn = emiDocumentoAdmDao.updClaveDocumentoEmi(pnuAnn, pnuEmi, (flag? Utilidades.getCadenaAlfanumAleatoria(7):null));
                        if ("NO_OK".equals(vReturn)) {
                            throw new validarDatoException("Error Agregando Clave");
                        }*/
                    }
                    
                    
                    
                }else{
                    throw new validarDatoException("El Documento a cambiado de estado.");                    
                }
            } else {//INSERTAR
                if (documentoEmiBean != null) {
                    if (pcrearExpediente.equals("1")) {
                        System.out.println("pcrearExpediente 1--> "+pcrearExpediente);
                        ExpedienteBean expedienteBean = new ExpedienteBean();
                        expedienteBean.setUsCreaAudi(pcempCodEmp);
                        expedienteBean.setFeExp(documentoEmiBean.getFeEmiCorta());
                        expedienteBean.setCoDepExp(documentoEmiBean.getCoDepEmi());
                        expedienteBean.setNuAnnExp(documentoEmiBean.getFeEmiCorta().trim().substring(6, 10));
                        /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente --*/
//                        expedienteEmiBean = new ExpedienteBean();
//                        expedienteEmiBean.setCoDepExp(expedienteBean.getCoDepExp());
//                        expedienteEmiBean.setFeExp(documentoEmiBean.getFeEmiCorta());
//                        expedienteEmiBean.setNuAnnExp(expedienteBean.getNuAnnExp());
                        /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente --*/
                        vReturn = emiDocumentoAdmDao.insExpedienteBean(expedienteBean);
                        if ("NO_OK".equals(vReturn)) {
                            throw new validarDatoException("Error al Grabar Expediente");
                        }
                        trxDocumentoEmiBean.setNuAnnExp(expedienteBean.getNuAnnExp());
                        trxDocumentoEmiBean.setFeExp(expedienteBean.getFeExp());
                        trxDocumentoEmiBean.setNuExpediente(expedienteBean.getNuExpediente());
                        trxDocumentoEmiBean.setNuSecExp(expedienteBean.getNuSecExp());
                        documentoEmiBean.setNuAnnExp(expedienteBean.getNuAnnExp());
                        documentoEmiBean.setNuSecExp(expedienteBean.getNuSecExp());
                        /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente --*/
//                        expedienteEmiBean.setNuExpediente(trxDocumentoEmiBean.getNuExpediente());
//                        expedienteEmiBean.setNuSecExp(trxDocumentoEmiBean.getNuSecExp());
                        /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente --*/
                    }
                    System.out.println("pcrearExpediente 2--> "+pcrearExpediente);
                    documentoEmiBean.setCoUseMod(pcoUserMod);
                    /*numeracion de documento*/
                    documentoEmiBean.setNuDocEmi(null);
                    vReturn=this.getInNumeraDocAdm(documentoEmiBean.getCoTipDocAdm());
                    if("NO_OK".equals(vReturn)){
                        throw new validarDatoException("Error al Obtener Indicador de Numeracion");
                    }
                    if(vReturn.equals("1")){
                        vReturn=this.getNumeroDocSiguienteAdm(documentoEmiBean.getNuAnn(), documentoEmiBean.getCoDepEmi(), documentoEmiBean.getCoTipDocAdm());
                        if("NO_OK".equals(vReturn)){
                            throw new validarDatoException("Error al Obtener Numero de Documento.");
                        }
                        documentoEmiBean.setNuDocEmi(vReturn);
                        trxDocumentoEmiBean.setNuDocEmi(vReturn);
                        documentoEmiBean.setNuCorDoc("1");
                    }else{
                        vReturn=emiDocumentoAdmDao.getNroCorrelativoDocumento(documentoEmiBean);
                        if("NO_OK".equals(vReturn)){
                            throw new validarDatoException("Error al Obtener Numero Correlativo de Documento.");
                        }
                        documentoEmiBean.setNuCorDoc(vReturn);
                    }
                    /*numeracion de documento*/
                    vReturn = emiDocumentoAdmDao.insDocumentoEmiBean(documentoEmiBean, expedienteEmiBean, remitenteEmiBean);
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error al Grabar Documento");
                    }
                    trxDocumentoEmiBean.setNuEmi(documentoEmiBean.getNuEmi());
                    trxDocumentoEmiBean.setNuCorEmi(documentoEmiBean.getNuCorEmi());
                    //trxDocumentoEmiBean.setNuCorDoc(documentoEmiBean.getNuCorDoc());
                    pnuEmi = documentoEmiBean.getNuEmi();
                    vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), "NC");
                    if(!vReturn.equals("OK")){
                        vReturn = "Error insertando Auditoria.";
                        throw new validarDatoException(vReturn);
                    }

                    /**insertar codigo jazanero**/
                    /*comentado YPA SIS*/
                   /* boolean flag = false;
                    if(documentoEmiBean.getCoTipDocAdm().equals("243") || documentoEmiBean.getCoTipDocAdm().equals("246")
                            || documentoEmiBean.getCoTipDocAdm().equals("247")|| documentoEmiBean.getCoTipDocAdm().equals("012")){//Hermes 11/01/2019 Directiva 2019                        
                        if(lstDestinatario!=null && lstDestinatario.size()>0){
                            for (DestinatarioDocumentoEmiBean dest : lstDestinatario) {
                                if(dest.getCoTipoDestino()!=null && (!dest.getCoTipoDestino().equals("01"))){
                                        flag = true;
                                        break;
                                }                        
                            }
                        }
                    } 
                    
                    vReturn = emiDocumentoAdmDao.updClaveDocumentoEmi(pnuAnn, pnuEmi, (flag? Utilidades.getCadenaAlfanumAleatoria(7):null));
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error Agregando Clave");
                    }*/
                            
                }
                for (ReferenciaEmiDocBean ref : lstReferencia) {
                    ref.setCoUseCre(pcoUserMod);
                    vReturn = emiDocumentoAdmDao.insReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error Agregando Referencia");
                    }
                }
                for (DestinatarioDocumentoEmiBean dest : lstDestinatario) {
                    System.out.println("lstDestinatario 1--> "+lstDestinatario.size());
                    System.out.println("lstDestinatario 2-->"+dest.getCoDependencia());
                    dest.setCoUseCre(pcoUserMod);
                    vReturn = emiDocumentoAdmDao.insDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error Agregando Destinatario");
                    }
                    /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente--*/
//                    if(documentoEmiBean != null && documentoEmiBean.getCoTipDocAdm().equals("234")){
//                        System.out.println("EXTENSION EXPEDIENTE");
//                        vReturn = emiDocumentoAdmDao.insExtExpedienteBean(pnuAnn, pnuEmi, dest, expedienteEmiBean);
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error Agregando Extension de Expediente");
//                        }
//                    }
                    /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente--*/
                }
                for(EmpleadoVoBoBean emp : lstEmpVoBo){
                    vReturn = emiDocumentoAdmDao.insPersonalVoBo(pnuAnn, pnuEmi, emp.getCoDependencia(), emp.getCoEmpleado(), pcoUserMod);
                    if(!"OK".equals(vReturn)){
                        throw new validarDatoException("Error Agregando Personal VB.");
                    }
                }             
            }
            //verificar si el documento tiene vistos buenos o esta observado
            if(!(documentoEmiBeanBD!=null&&(documentoEmiBeanBD.getCoEmpEmi().equals(usuario.getCempCodemp())||documentoEmiBeanBD.getCoEmpRes().equals(usuario.getCempCodemp())))){
                vReturn=docVoboDao.existeVistoBuenoDocAdm(pnuAnn, pnuEmi);
                if(vReturn.equals("0")){
                    vReturn="OK";
                }else{
                    throw new validarDatoException("Doc. con Visto Bueno u Observado.");                
                }                  
            }
            /*actualizar resumenes*/
            if (lstDestinatario.size() >= 1) {
                //llamar a service de de actualizar resumen destino
                vReturn=actualizaResumenService.updRemitosResumenDes(pnuAnn, pnuEmi);
                if ("NO_OK".equals(vReturn)) {
                    throw new validarDatoException("Error Actualizando Remito resumen destinatario");
                }
            }
            if (lstReferencia.size() >= 1) {
                //llamar a service de de actualizar resumen referencia
                vReturn=actualizaResumenService.updRemitosResumenRef(pnuAnn, pnuEmi);
                if ("NO_OK".equals(vReturn)) {
                    throw new validarDatoException("Error Actualizando Remito resumen Referencia");
                }                
            }

//            vReturn=notiService.procesarNotificacion(pnuAnn, pnuEmi, pcoUserMod);
//            if(!vReturn.equals("OK")){
//                throw new validarDatoException(vReturn);
//            }
            
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION");
        }
        return vReturn;
    }

    public List<DestinoBean> getDestinosListCodDepTipoDes(String nu_ann, String nu_emi) {
        List<DestinoBean> list = null;
        try {
            list = emiDocumentoAdmDao.getListaDestinosCodDepTipoDes(nu_ann, nu_emi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<ReferenciaRemitoBean> getReferenciaRemitoList(String nu_ann, String nu_emi) {
        List<ReferenciaRemitoBean> list = null;
        try {
            list = emiDocumentoAdmDao.getListaReferenciaRemitos(nu_ann, nu_emi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<DestinoBean> getDestinosListCodPri(String nu_ann, String nu_emi) {
        List<DestinoBean> list = null;
        try {
            list = emiDocumentoAdmDao.getListaDestinosCodPri(nu_ann, nu_emi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String delDocumentoEmiAdm(DocumentoEmiBean documentoEmiBean,Usuario usuario) throws Exception {
        String vReturn = "NO_OK";

        try {
            String pesDocEmi = documentoEmiBean.getEsDocEmi();
            if (pesDocEmi != null && pesDocEmi.equals("9")) {
                vReturn = emiDocumentoAdmDao.getNroCorrelativoDocumentoDel(documentoEmiBean);
                String snuCordoc = vReturn;
                if ("NO_OK".equals(vReturn)) {
                    throw new validarDatoException("Error al obtener nro correlativo");
                }
                /*[HPB-21/06/21] Campos Auditoria-*/
                documentoEmiBean.setCoUseMod(usuario.getCoUsuario());
                vReturn = emiDocumentoAdmDao.updReferenciaDocumentoEmiAntesEliminarTodo(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), documentoEmiBean.getCoUseMod());
                if ("NO_OK".equals(vReturn)) {
                    throw new validarDatoException("Error Borrando Referencia");
                }else{
                    vReturn = emiDocumentoAdmDao.delAllReferenciaDocumentoEmi(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi());
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error al eliminar referencias");
                    }   
                }                
                vReturn = emiDocumentoAdmDao.updDestinoDocumentoEmiAntesEliminarTodo(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), documentoEmiBean.getCoUseMod());
                if ("NO_OK".equals(vReturn)) {
                    throw new validarDatoException("Error al actualizar Destinatarios antes de eliminar");
                }else{                
                    vReturn = emiDocumentoAdmDao.delAllDestinatarioDocumentoEmi(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi());
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error al eliminar Destinatarios");
                    }
                }
                /*[HPB-21/06/21] Campos Auditoria-*/
                documentoEmiBean.setNuCorDoc(snuCordoc);
                documentoEmiBean.setNuDocEmi(null);
                documentoEmiBean.setNuAnnExp(null);
                documentoEmiBean.setNuSecExp(null);
                documentoEmiBean.setNuDetExp(null);
                vReturn = emiDocumentoAdmDao.delDocumentoEmiAdm(documentoEmiBean);
                if (!"OK".equals(vReturn)) {
                    throw new validarDatoException("Error al actualizar documento");
                }
                vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), pesDocEmi);
                if(!vReturn.equals("OK")){
                    vReturn = "Error insertando Auditoria.";
                    throw new validarDatoException(vReturn);
                }                 
            } else {
                vReturn = "Documento NO puede ser eliminado!";
            }
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION ELIMINAR DOCUMENTO EMISION");
        }
        return vReturn;
    }

    @Override
    public String getNumeroDocSiguienteAdm(String pnuAnn, String pcoDepEmi, String pcoDoc) {
        String vReturn = "NO_OK";
        try {
            vReturn = emiDocumentoAdmDao.getNumeroDocSiguienteAdm(pnuAnn, pcoDepEmi, pcoDoc);
        } catch (Exception e) {
            vReturn = "NO_OK";
        }

        return vReturn;
    }

    @Override
    public String getVerificaPdfDoc(String pnuAnn, String pnuEmi, String ptiCap) {
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
    public String getFormatoDoc(String pnuAnn, String pnuEmi, String ptiCap) {
        String vReturn = "ERR";
        try {
            DocumentoObjBean docObjBean = emiDocumentoAdmDao.getPropiedadesArchivo(pnuAnn, pnuEmi, ptiCap);
            if (docObjBean != null && docObjBean.getTipoDoc() != null) {
                vReturn = docObjBean.getTipoDoc();
            } else {
                vReturn = "ERR";
            }

        } catch (Exception e) {
            vReturn = "ERR";
        }

        return vReturn;
    }

    @Override
    public HashMap getBuscaDependenciaEmite(String pcoDepen, String pdeDepEmite) {
        String vReturn = "1";//muestra lista
        HashMap map = new HashMap();
        List list;
        try {
            list = emiDocumentoAdmDao.getListDestinatarioEmi(pcoDepen, pdeDepEmite);
            if (list != null) {
                if (list.isEmpty()) {
                    list = emiDocumentoAdmDao.getListDestinatarioEmi(pcoDepen, null);
                } else if (list.size() == 1) {
                    vReturn = "0";//solo objeto dependencia
                    map.put("objDestinatario", list.get(0));
                }
            }
            map.put("listaDestinatario", list);
        } catch (Exception e) {
            vReturn = e.getMessage();
        }
        map.put("vReturn", vReturn);
        return map;
    }
    
    @Override
    public HashMap getDocumentosEnReferencia(BuscarDocumentoEmiBean buscarDocumentoEmiBean, String pnuPagina, int pnuRegistros){
        List<DocumentoEmiBean> list = null;
        DocumentoEmiBean documentoEmiBean = null;
        HashMap mp = new HashMap();
        try{
            documentoEmiBean = emiDocumentoAdmDao.existeDocumentoReferenciado(buscarDocumentoEmiBean);
            if(documentoEmiBean!=null && documentoEmiBean.getMsjResult()!=null && documentoEmiBean.getMsjResult().equals("OK")){
                documentoEmiBean.setCoDepEmi(buscarDocumentoEmiBean.getsCoDependencia());
                documentoEmiBean.setCoEmpRes(buscarDocumentoEmiBean.getsCoEmpleado());                
                list = emiDocumentoAdmDao.getDocumentosReferenciadoBusq(documentoEmiBean,buscarDocumentoEmiBean.getsTiAcceso(), pnuPagina, pnuRegistros);/* HPB 27/05/2020 - Requerimiento Paginación emitidos (modificacion)*/                
                mp.put("emiDocumAdmList",list);
                mp.put("msjResult", "1");//Existe Documento
            }else{
                mp.put("msjResult", "0");//NO existe Documento
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return mp;           
    }
    
    @Override
    public DependenciaBean cambiaDepEmi(String pcoDep){
        // Capturando datos de la dependencia
        DependenciaBean depEmi = null; 

        try {
            depEmi = maestrosDao.getDatosDependencia(pcoDep); 

        } catch (Exception ex) {
            ex.printStackTrace();
            depEmi = null;
        }
        
        return depEmi;
    }
    
    @Override
    public String[] getNotificaciones(List<EmpleadoVoBoBean> lsEmpleadoVb, String reqFirmaAnexos){
        String[] aMensaje=null;
        String sMensaje=null;
        if(lsEmpleadoVb!=null){
            for (EmpleadoVoBoBean empVobo : lsEmpleadoVb) {
                if(empVobo!=null&&empVobo.getInVobo()!=null&&(empVobo.getInVobo().equals("0")||empVobo.getInVobo().equals("2"))){
                    sMensaje="Documento con visto bueno faltante!";
                    break;
                }
            }
        }
        if(reqFirmaAnexos!=null&&reqFirmaAnexos.equals("1")){
            if(sMensaje!=null){
                sMensaje+=";Documento con Anexos Pendientes de Firma!";
            }else{
                sMensaje="Documento con Anexos Pendientes de Firma!";
            }
        }
        if(sMensaje!=null){
            aMensaje=sMensaje.split(";");
        }
        return aMensaje;
    }
    
    private String getInNumeraDocAdm(String tipoDoc){
        String vReturn = "NO_OK";        
        
        try {
            vReturn = emiDocumentoAdmDao.getInNumeraDocAdm(tipoDoc);
        } catch (Exception e) {
            vReturn = "NO_OK";
        }        
        return vReturn;
    }
    
    @Override
    public String getInFirmaDocAdm(String tipoDoc){
        String vReturn = "NO_OK";        
        
        try {
            vReturn = emiDocumentoAdmDao.getInFirmaDocAdm(tipoDoc);
        } catch (Exception e) {
            vReturn = "NO_OK";
        }        
        return vReturn;
    }
    
    @Override
    public String getLstPersVoBoGrupo(String pcoGrupo) {
        StringBuilder retval = new StringBuilder();
        String coRespuesta;
        List<DestinatarioDocumentoEmiBean> list = null;

        try {
            list = emiDocumentoAdmDao.getLstPersVoBoGrupo(pcoGrupo);
            if (list != null) {
                Writer strWriter = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();

                coRespuesta = "1";
                retval.append("{\"coRespuesta\":\"");
                retval.append(coRespuesta);
                retval.append("\",\"lsDestInstitu\":");
                strWriter = new StringWriter();
                mapper = new ObjectMapper();
                mapper.writeValue(strWriter, list);
                retval.append(strWriter.toString());
                retval.append("}");
            }
        } catch (Exception e) {
            coRespuesta = "0";
            retval.append("{\"coRespuesta\":\"");
            retval.append(coRespuesta);
            retval.append("\",\"deRespuesta\":\"");
            retval.append(e.getMessage());
            retval.append("\"}");
        }

        return retval.toString();
    }    

    @Override
    public String updArchivarDocumento(DocumentoEmiBean documentoEmiBean, Usuario usuario) {
           String vReturn = "ERR";
        try{
           vReturn =   emiDocumentoAdmDao.updArchivarDocumento(documentoEmiBean,usuario);
        }catch(Exception e){
            vReturn = "ERR";
        }
        
        return vReturn;
    }

    @Override
    public DetalleEnvioDeCorreoBean getDetalleEnvioCorreo(String pnuAnn, String pnuEmi) {
        DetalleEnvioDeCorreoBean detalleEnvioDeCorreoBean = null; 

        try {
            detalleEnvioDeCorreoBean = emiDocumentoAdmDao.getDetalleEnvioCorreo(pnuAnn, pnuEmi); 

        } catch (Exception ex) {
            ex.printStackTrace();
            detalleEnvioDeCorreoBean = null;
        }
        
        return detalleEnvioDeCorreoBean;
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

    @Override
    public String grabaDocumentoEmiAdmProy(TrxDocumentoEmiBean trxDocumentoEmiBean, String pcrearExpediente, Usuario usuario) throws Exception {
        
        String vReturn = "NO_OK";
        try {
            String pnuAnn = trxDocumentoEmiBean.getNuAnn();
            String pnuEmi = trxDocumentoEmiBean.getNuEmi();
            String pcoUserMod = trxDocumentoEmiBean.getCoUserMod();
            String pcempCodEmp = trxDocumentoEmiBean.getCempCodEmp();
            DocumentoEmiBean documentoEmiBean = trxDocumentoEmiBean.getDocumentoEmiBean();
            ExpedienteBean expedienteEmiBean = trxDocumentoEmiBean.getExpedienteEmiBean();
            RemitenteEmiBean remitenteEmiBean = trxDocumentoEmiBean.getRemitenteEmiBean();
            ArrayList<ReferenciaEmiDocBean> lstReferencia = trxDocumentoEmiBean.getLstReferencia();
            ArrayList<DestinatarioDocumentoEmiBean> lstDestinatario = trxDocumentoEmiBean.getLstDestinatario();
            ArrayList<EmpleadoVoBoBean> lstEmpVoBo = trxDocumentoEmiBean.getLstEmpVoBo();
            DocumentoEmiBean documentoEmiBeanBD=null;
            
            
            
            
//            if (pnuAnn != null && pnuEmi != null && !pnuAnn.equals("") && !pnuEmi.equals("")) {//UPD
//                documentoEmiBeanBD = emiDocumentoAdmDao.getEstadoDocumento(pnuAnn, pnuEmi);
//                if(documentoEmiBeanBD!=null&&documentoEmiBeanBD.getEsDocEmi()!=null&&(documentoEmiBeanBD.getEsDocEmi().equals("5")||documentoEmiBeanBD.getEsDocEmi().equals("7"))){
//                    if (documentoEmiBean != null || expedienteEmiBean != null || remitenteEmiBean != null) {
//                        if(documentoEmiBean!=null&&!documentoEmiBean.getCoTipDocAdm().equals(documentoEmiBeanBD.getCoTipDocAdm())){
//                            vReturn=this.getInNumeraDocAdm(documentoEmiBean.getCoTipDocAdm());
//                            if("NO_OK".equals(vReturn)){
//                                throw new validarDatoException("Error al Obtener Indicador de Numeracion");
//                            }                            
//                            if(vReturn.equals("1")){
//                                vReturn=this.getNumeroDocSiguienteAdm(documentoEmiBean.getNuAnn(), documentoEmiBean.getCoDepEmi(), documentoEmiBean.getCoTipDocAdm());
//                                if("NO_OK".equals(vReturn)){
//                                    throw new validarDatoException("Error al Obtener Numero de Documento.");
//                                }
//                                documentoEmiBean.setNuDocEmi(vReturn);
//                                trxDocumentoEmiBean.setNuDocEmi(vReturn);
//                                documentoEmiBean.setNuCorDoc("1");
//                            }                            
//                        }
//                        vReturn = emiDocumentoAdmDao.updDocumentoEmiAdmBean(pnuAnn, pnuEmi, documentoEmiBean, expedienteEmiBean, remitenteEmiBean, pcoUserMod);
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error al Actualizar Documento");
//                        }
//                        if (documentoEmiBean != null){
//                            trxDocumentoEmiBean.setNuCorEmi(documentoEmiBean.getNuCorEmi());
//                            //trxDocumentoEmiBean.setNuCorDoc(documentoEmiBean.getNuCorDoc());
//                        }                       
//                        
//                    }
//                    for (ReferenciaEmiDocBean ref : lstReferencia) {
//                        String sAccionBD = ref.getAccionBD();
//                        if ("INS".equals(sAccionBD)) {
//                            ref.setCoUseCre(pcoUserMod);
//                            vReturn = emiDocumentoAdmDao.insReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
//                            if ("NO_OK".equals(vReturn)) {
//                                throw new validarDatoException("Error Agregando Referencia");
//                            }
//                        } else if ("UPD".equals(sAccionBD)) {
//                            vReturn = emiDocumentoAdmDao.updReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
//                            if ("NO_OK".equals(vReturn)) {
//                                throw new validarDatoException("Error Actualizando Referencia");
//                            }
//                        } else if ("DEL".equals(sAccionBD)) {
//                            vReturn = emiDocumentoAdmDao.delReferenciaDocumentoEmi(pnuAnn, pnuEmi, ref);
//                            if ("NO_OK".equals(vReturn)) {
//                                throw new validarDatoException("Error Borrando Referencia");
//                            }
//                        }
//                    }
//                    for (DestinatarioDocumentoEmiBean dest : lstDestinatario) {
//                        String sAccionBD = dest.getAccionBD();
//                        dest.setCoUseCre(pcoUserMod);
//                        if ("INS".equals(sAccionBD)) {
//                            vReturn = emiDocumentoAdmDao.insDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
//                            if ("NO_OK".equals(vReturn)) {
//                                throw new validarDatoException("Error Agregando Destinatario");
//                            }
//                        } else if ("UPD".equals(sAccionBD)) {
//                            vReturn = emiDocumentoAdmDao.updDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
//                            if ("NO_OK".equals(vReturn)) {
//                                throw new validarDatoException("Error Actualizando Destinatario");
//                            }
//                        } else if ("DEL".equals(sAccionBD)) {
//                            vReturn = emiDocumentoAdmDao.delDestinatarioDocumentoEmi(pnuAnn, pnuEmi, dest);
//                            if ("NO_OK".equals(vReturn)) {
//                                throw new validarDatoException("Error Borrando Destinatario");
//                            }
//                        }
//                    }
//                                                            
//                    for(EmpleadoVoBoBean emp : lstEmpVoBo){
//                        String sAccionBD = emp.getAccionBD();
//                        if("DEL".equals(sAccionBD)){
//                            vReturn = emiDocumentoAdmDao.delPersonalVoBo(pnuAnn, pnuEmi, emp.getCoDependencia(), emp.getCoEmpleado());
//                            if (!"OK".equals(vReturn)) {
//                                throw new validarDatoException("Error Borrando Personal VB.");
//                            }                        
//                        }else if("INS".equals(sAccionBD)){
//                            vReturn = emiDocumentoAdmDao.insPersonalVoBo(pnuAnn, pnuEmi, emp.getCoDependencia(), emp.getCoEmpleado(), pcoUserMod);
//                            if(!"OK".equals(vReturn)){
//                                throw new validarDatoException("Error Agregando Personal VB.");
//                            }                        
//                        }
//                    } 
//                    
//                    DocumentoEmiBean documentoEmiBeanTemp = emiDocumentoAdmDao.getDocumentoEmiAdm(pnuAnn, pnuEmi);
//                    
//                    if(documentoEmiBeanTemp != null){
//                        /**insertar codigo jazanero**/
//                        boolean flag = false;
//                        if(documentoEmiBeanTemp.getCoTipDocAdm().equals("243") || documentoEmiBeanTemp.getCoTipDocAdm().equals("246")){                                
//
//                            List<DestinatarioDocumentoEmiBean> lstDestinatarioTemp = emiDocumentoAdmDao.getLstDestintariotlbEmi(pnuAnn, pnuEmi);
//
//                            if(lstDestinatarioTemp!=null && lstDestinatarioTemp.size()>0){
//                                for (DestinatarioDocumentoEmiBean dest : lstDestinatarioTemp) {
//                                    if(dest.getCoTipoDestino()!=null && (!dest.getCoTipoDestino().equals("01"))){
//                                            flag = true;
//                                            break;
//                                    }                        
//                                }                                    
//                            }
//                        }
//
//                        vReturn = emiDocumentoAdmDao.updClaveDocumentoEmi(pnuAnn, pnuEmi, (flag? Utilidades.getCadenaAlfanumAleatoria(7):null));
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error Agregando Clave");
//                        }
//                    }
//                    
//                    
//                    
//                }else{
//                    throw new validarDatoException("El Documento a cambiado de estado.");                    
//                }
//            } else {//INSERTAR
                if (documentoEmiBean != null) {
                    documentoEmiBean.setNuAnn(pnuAnn);
                    documentoEmiBean.setNuEmi(pnuEmi);
                    documentoEmiBean.setCoUseMod(pcoUserMod);
                    
                    DependenciaBean depEmi = cambiaDepEmi(documentoEmiBean.getCoDepEmi());
//                    if ("NO_OK".equals(vReturn)) {
//                        throw new validarDatoException("Error al Grabar Documento");
//                    }
                    
                    if(depEmi!=null){
                        documentoEmiBean.setDeDocSig(depEmi.getDeSigla());
                    }                    
                                        
                    /*numeracion de documento*/
                    vReturn = emiDocumentoAdmDao.insDocumentoEmiBeanProyecto(documentoEmiBean, expedienteEmiBean, remitenteEmiBean);
                    if ("NO_OK".equals(vReturn)) {
                        throw new validarDatoException("Error al Grabar Documento");
                    }
                    
//                    for(DestinatarioDocumentoEmiBean dest : lstDestinatario) {
//                        dest.setCoUseCre(pcoUserMod);
//                        vReturn = emiDocumentoAdmDao.insDestinatarioDocumentoEmiProyecto(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), dest);
//                        if ("NO_OK".equals(vReturn)) {
//                            throw new validarDatoException("Error Agregando Destinatario");
//                        }
//                    }
                    
                    for (DestinatarioDocumentoEmiBean dest : lstDestinatario) {
                        String sAccionBD = dest.getAccionBD();
                        dest.setCoUseCre(pcoUserMod);
                        if ("INS".equals(sAccionBD)) {
                            vReturn = emiDocumentoAdmDao.insDestinatarioDocumentoEmiProyecto(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), dest);
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error Agregando Destinatario");
                            }
                        } else if ("UPD".equals(sAccionBD)) {
                            vReturn = emiDocumentoAdmDao.updDestinatarioDocumentoEmiProyecto(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), dest);
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error Actualizando Destinatario");
                            }
                        } else if ("DEL".equals(sAccionBD)) {
                            vReturn = emiDocumentoAdmDao.delDestinatarioDocumentoEmiProyecto(documentoEmiBean.getNuAnn(), documentoEmiBean.getNuEmi(), dest);
                            if ("NO_OK".equals(vReturn)) {
                                throw new validarDatoException("Error Borrando Destinatario");
                            }
                        }
                    }
                                                                  
                }
                            
            //}
            
        } catch (validarDatoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION");
        }
        return vReturn;
    }

    @Override
    public HashMap getLstDestintariotlbEmiProy(String pnuAnn, String pnuEmi) {
        List<DestinatarioDocumentoEmiBean> list = null;
        HashMap map = null;
        try {
            list = emiDocumentoAdmDao.getLstDestintariotlbEmiProyecto(pnuAnn, pnuEmi);
            map = getLstDestinatario(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public DocumentoProyectoBean getDocumentoEmiAdmProy(String pnuAnn, String pnuEmi) {
        DocumentoProyectoBean documentoEmiBean = null;
        try {
            documentoEmiBean = emiDocumentoAdmDao.getDocumentoEmiAdmProyecto(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return documentoEmiBean;
    }

    @Override
    public String getTipoDestinatarioEmiProy(String pnuAnn, String pnuEmi) {
        String retval = null;
        try {
            retval = emiDocumentoAdmDao.getTipoDestinatarioEmiProyecto(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retval;
    }

    @Override
    public DocumentoEmiBean getDocumentoEmiAdmProyectoAEmiBean(String pnuAnn, String pnuEmi) {
        DocumentoEmiBean documentoEmiBean = null;
        try {
            documentoEmiBean = emiDocumentoAdmDao.getDocumentoEmiAdmProyectoAEmiBean(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return documentoEmiBean;
    }

    @Override
    public HashMap getLstDestintariotlbEmiProyAEmiBean(String pnuAnn, String pnuEmi) {
        List<DestinatarioDocumentoEmiBean> list = null;
        HashMap map = null;
        try {
            list = emiDocumentoAdmDao.getLstDestintariotlbEmiProyecto(pnuAnn, pnuEmi);
            
            if(list!=null && list.size()>0){
                for(DestinatarioDocumentoEmiBean dt:list){
                    dt.setAccionBD("INS");
                }
            }
            
            map = getLstDestinatario(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public List<DocumentoEmiBean> getDocumentosBuscaEmiAdmFiltroReg(BuscarDocumentoEmiBean buscarDocumentoEmiBean, String pnuPagina, int pnuRegistros) {
        List<DocumentoEmiBean> list = null;
        try {
            list = emiDocumentoAdmDao.getDocumentosBuscaEmiAdmFiltroReg(buscarDocumentoEmiBean, pnuPagina, pnuRegistros);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return list;        
    }

    @Override
    public String changeToEnvioMesaVirtual(DatosInterBean datosInter, Usuario usuario) {
        String vReturn = "NO_OK";

       
        try {


                    vReturn = emiDocumentoAdmDao.insMesaVitual(datosInter);  
                    if(vReturn.equals("OK")){
                       vReturn=audiMovDoc.audiEstadoDocumentoRemito(usuario, datosInter.getNuAnn(), datosInter.getNuEmi(), "0");
                       if(!vReturn.equals("OK")){
                           vReturn = "Error insertando Auditoria.";
                       }
                    }else{
                        vReturn = "Error al Enviar el documento a la mensajería.:"+vReturn;
                    } 
               
          
        } catch (Exception e) {
            vReturn = e.getMessage();
        }
        return vReturn;
    }      

    @Override
    public List<DocumentoEmiBean> getLstTipoDestEmi(String pnuAnn,String pnuEmi) {
        List<DocumentoEmiBean> list = null;
        try { 
            list = emiDocumentoAdmDao.getLstTipoDestEmi(pnuAnn, pnuEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }
    /*HPB 11/02/2020 - Inicio - Requerimiento Remitente expediente*/
    @Override
    public ProveedorBean getProveedor(String pnuRuc) {
        ProveedorBean proveedorBean = null;
        try {
            proveedorBean = commonQueryDao.getProveedorExpediente(pnuRuc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return proveedorBean;
    }

    @Override
    public DestinatarioOtroOrigenBean getDestinatarioOtroOrigen(String pnuCodigo) {
        DestinatarioOtroOrigenBean destinatarioOtroOrigenBean = null;
        try {
            destinatarioOtroOrigenBean = commonQueryDao.getDestinatarioOtroOrigenExpediente(pnuCodigo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return destinatarioOtroOrigenBean;        
    }
    /*HPB 11/02/2020 - Fin - Requerimiento Remitente expediente*/    

    @Override
    public List<DocumentoEmiBean> getDocumentosBuscaEmiAdmFiltroRegSize(BuscarDocumentoEmiBean buscarDocumentoEmiBean) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List<DocumentoEmiBean> list = null;
        try {
            list = emiDocumentoAdmDao.getDocumentosBuscaEmiAdmFiltroRegSize(buscarDocumentoEmiBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;         
    }
    /* HPB 27/05/2020 - Inicio - Requerimiento Paginación emitidos */
    @Override
    public HashMap getDocumentosEnReferenciaSize(BuscarDocumentoEmiBean buscarDocumentoEmiBean) {
        List<DocumentoEmiBean> list = null;
        DocumentoEmiBean documentoEmiBean = null;
        HashMap mp = new HashMap();
        try{
            documentoEmiBean = emiDocumentoAdmDao.existeDocumentoReferenciado(buscarDocumentoEmiBean);
            if(documentoEmiBean!=null && documentoEmiBean.getMsjResult()!=null && documentoEmiBean.getMsjResult().equals("OK")){
                documentoEmiBean.setCoDepEmi(buscarDocumentoEmiBean.getsCoDependencia());
                documentoEmiBean.setCoEmpRes(buscarDocumentoEmiBean.getsCoEmpleado());                
                list = emiDocumentoAdmDao.getDocumentosReferenciadoBusqSize(documentoEmiBean,buscarDocumentoEmiBean.getsTiAcceso());                
                mp.put("emiDocumAdmList",list);
                mp.put("msjResult", "1");//Existe Documento
            }else{
                mp.put("msjResult", "0");//NO existe Documento
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return mp; 
    }
    /* HPB 27/05/2020 - Fin - Requerimiento Paginación emitidos */
    /*-- INICIO [HPB] LOG PROVEIDO 09/07/20--*/
    @Override
    public String insLogDocumentoEmiAdm(String pnuAnn, String pnuEmi, String pnuCorEmi, String pcoDepEmi, Usuario currentUser) throws Exception {
        String vReturn = "NO_OK";
        AudiEstadosMovDocBean audi=new AudiEstadosMovDocBean();
        try {
            audi.setNuAnn(pnuAnn);
            audi.setNuEmi(pnuEmi);
            audi.setDeUser(currentUser.getCoUsuario());
            audi.setDeIpPc(currentUser.getIpPC());
            audi.setDeNamePc(currentUser.getNombrePC());
            audi.setDeUserPc(currentUser.getUsuPc());

            vReturn = emiDocumentoAdmDao.insLogDocumentoEmiAdm(audi, pnuCorEmi, pcoDepEmi);
        } catch (Exception e) {
        }
        
        return vReturn;
    }
    /*-- FIN [HPB] LOG PROVEIDO 09/07/20--*/
}
