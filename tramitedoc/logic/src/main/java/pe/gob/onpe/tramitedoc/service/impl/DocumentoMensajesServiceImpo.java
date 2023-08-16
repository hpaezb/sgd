/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoCargaMsjBean;
import pe.gob.onpe.tramitedoc.bean.DescargaMensajeBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoDesBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoFileBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoVerBean;
import pe.gob.onpe.tramitedoc.bean.ElementoMensajeroBean;
import pe.gob.onpe.tramitedoc.bean.MensajesConsulBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;
import pe.gob.onpe.tramitedoc.bean.TipoElementoMensajeriaBean;
import pe.gob.onpe.tramitedoc.dao.MensajesDao;
import pe.gob.onpe.tramitedoc.dao.MensajesDao;
import pe.gob.onpe.tramitedoc.service.DocumentoMensajesService;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author oti3
 */
@Service("documentoMensajesService")
public class DocumentoMensajesServiceImpo implements DocumentoMensajesService{
    @Autowired
    private MensajesDao mensajesDao;

    @Autowired
    ApplicationProperties applicationProperties;
    
    
    @Override
    public List<ElementoMensajeroBean> getListResponsableMensajeria(String tipo) {
        List<ElementoMensajeroBean> list = null;
        try {  
            list = mensajesDao.getListMensajero(tipo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }
    
    @Override
    public List<SiElementoBean> getListTipoElementoMensajeria(String tipo){
     List<SiElementoBean> list = null;
        try {  
            list = mensajesDao.getLsSiElementoBean(tipo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public List<MensajesConsulBean> getBuscaDocumentosCarga(BuscarDocumentoCargaMsjBean buscarDocumentoCargaMsjBean) {
        List<MensajesConsulBean> list = null;
        try {
            //buscarDocumentoCargaMsjBean.setCoGrupo("1");
            /*String vResult=mesaPartesDao.getPermisoChangeEstadoMP(buscarDocumentoExtRecepBean.getCoEmpleado(),buscarDocumentoExtRecepBean.getCoDependencia());
            buscarDocumentoExtRecepBean.setInCambioEst(vResult!=null&&vResult.equals("1")?"1":"0");*/
            list = mensajesDao.getBuscaDocumentosCarga(buscarDocumentoCargaMsjBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public String updMensajeriaDocumento(DescargaMensajeBean descargaMensaje) {
         String vReturn = "ERR";
        try{           
            vReturn =   mensajesDao.updMensajeriaDocumento(descargaMensaje);
        }catch(Exception e){
            vReturn = "ERR";
        }
        
        return vReturn;
    }

    @Override
    public String insArchivoAnexoDes(String coUsu, String pnuAnn, String pnuEmi, String pnuDes, DocumentoFileBean pfileAnexo) {
        String vReturn = "NO_OK";
        String vnuAne = mensajesDao.getUltimoAnexo(pnuAnn, pnuEmi);

        DocumentoAnexoDesBean docAnexo = new DocumentoAnexoDesBean();

        docAnexo.setNuAnn(pnuAnn);
        docAnexo.setNuEmi(pnuEmi);
        docAnexo.setNuAne(vnuAne);
        docAnexo.setNuDes(pnuDes);
        docAnexo.setDeDet(pfileAnexo.getNombreArchivo());
        docAnexo.setDeRutOri(pfileAnexo.getNombreArchivo());
        docAnexo.setCoUseCre(coUsu);
        docAnexo.setCoUseMod(coUsu);
        
        validarAnexoDes(docAnexo);

        final InputStream archivoAnexo = new ByteArrayInputStream(pfileAnexo.getArchivoBytes());
        final int size = pfileAnexo.getArchivoBytes().length;
        int maxUploadSize=10000000;

        try {
            if(size<=maxUploadSize){
                vReturn = mensajesDao.insArchivoAnexoDes(docAnexo, archivoAnexo, size);
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(archivoAnexo!=null){
                    archivoAnexo.close();                                        
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }                

        return vReturn;
    }


    private void validarAnexoDes(DocumentoAnexoDesBean docAnexo){
    String pdeDet=docAnexo.getDeDet();
    if(pdeDet!=null){
        int tamCampoDeDet=200;
        pdeDet=pdeDet.trim();
        int pLenDeDet=pdeDet.length();
        if(pLenDeDet>tamCampoDeDet){
            pdeDet=pdeDet.substring(pLenDeDet-tamCampoDeDet, pLenDeDet);
        }
    }
    docAnexo.setDeDet(pdeDet);
    }

    @Override
    public String delMensajeriaDocumento(DescargaMensajeBean descargaMensaje) {
            String vReturn = "ERR";
        try{
           vReturn =   mensajesDao.delMensajeriaDocumento(descargaMensaje);
        }catch(Exception e){
            vReturn = "ERR";
        }
        
        return vReturn;
    }

    @Override
    public ReporteBean getGenerarReporte(BuscarDocumentoCargaMsjBean buscarDocumentoRecepConsulBean, Map parametros) {
        List lista = null;
        ReporteBean objReporte = new ReporteBean();
        String coRespuesta="";
        String deRespuesta="";
        String prutaReporte = "";
        String deNoDoc = "";
        String coReporte = "";
        String extensionArch;
        String nombreArchivo = "";
        int tipoArchivo=0;
        byte[] archivo;
        String eserror="1";//si
            
        try {
            String fecha = Utility.getInstancia().getStringFechaYYYYMMDDHHmm(new Date());  
            
            try{
                lista = getBuscaDocumentosCarga(buscarDocumentoRecepConsulBean);
                eserror="0";
            }catch(Exception ex){               
                eserror="1";
                deRespuesta=ex.getMessage();
            }
            if (lista.size()==0) {                 
                 coRespuesta = "1";
                 deRespuesta="No existe información para generar reporte.";
                 throw new Exception(deRespuesta);
            }
            if(buscarDocumentoRecepConsulBean.getFormatoReporte().equalsIgnoreCase("PDF")){
                coReporte = "TDR_MSJ";                
                extensionArch=".pdf";
                tipoArchivo = 2;
            }else{
                coReporte = "TDR_MSJ_XLS";
                extensionArch=".xls";
                tipoArchivo = 1;
            }
            
            String rutaJasper =  buscarDocumentoRecepConsulBean.getRutaReporteJasper() + "/" + coReporte + ".jasper";
            buscarDocumentoRecepConsulBean.setRutaReporteJasper(rutaJasper);
            
            System.out.println("Ruta");
            System.err.println(rutaJasper);
            
            nombreArchivo = Utilidades.generateRandomNumber(10);
            prutaReporte = "RPTDOCUMENTOSAL_"+nombreArchivo + extensionArch;
            deNoDoc = "temp|RPTDOCUMENTOSAL_"+fecha+extensionArch;
            
            Utilidades util = new Utilidades();
            archivo = util.GenerarReporteLista(buscarDocumentoRecepConsulBean.getRutaReporteJasper(), lista, coReporte, parametros, tipoArchivo);
            
            if (archivo==null) {
                eserror = "0";
                coRespuesta=eserror;
                throw new Exception("No generó correctamente el archivo del reporte.");
            }
            
            String rutaBase = applicationProperties.getRutaTemporal() + "/" + prutaReporte;
            
            File tmpFile = new File(rutaBase);
            FileOutputStream fos = new FileOutputStream(tmpFile);
            fos.write(archivo);
            fos.flush();
            fos.close();
            String rutaUrl = "reporte?coReporte=";
            prutaReporte = rutaUrl + prutaReporte;
            
            if(eserror.equals("0")){
                coRespuesta=eserror;
            }else{
                coRespuesta="1";
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultaEmiDocServiceImp.class.getName()).log(Level.SEVERE, null, e);
            eserror="1";
            deRespuesta=e.getMessage();
        }finally{
            objReporte.setcoRespuesta(coRespuesta);
            objReporte.setdeRespuesta(deRespuesta);
            objReporte.setnoUrl(prutaReporte);
            objReporte.setnoDoc(deNoDoc);
            return objReporte;
        }
    }

    @Override
    public MensajesConsulBean getBuscaDocumentosMsj(String nu_ann, String nu_emi, String nu_des, String nu_msj) {
        MensajesConsulBean mensajesConsulBean=new MensajesConsulBean();
                
        try {
            //buscarDocumentoCargaMsjBean.setCoGrupo("1");
            /*String vResult=mesaPartesDao.getPermisoChangeEstadoMP(buscarDocumentoExtRecepBean.getCoEmpleado(),buscarDocumentoExtRecepBean.getCoDependencia());
            buscarDocumentoExtRecepBean.setInCambioEst(vResult!=null&&vResult.equals("1")?"1":"0");*/
            mensajesConsulBean = mensajesDao.getBuscaDocumentosMsj(nu_ann,  nu_emi,  nu_des,  nu_msj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mensajesConsulBean;
    }

    @Override
    public DocumentoVerBean getNombreDocMsj(String pnuAnn, String pnuEmi, String pnuAnexo) {
        DocumentoVerBean docVerBean= new DocumentoVerBean();
        
        docVerBean.setNuAnn(pnuAnn);
        docVerBean.setNuEmi(pnuEmi);
        docVerBean.setNuAne(pnuAnexo); 
        docVerBean.setInDoc(false);
        
        DocumentoObjBean docObjBean = null;
        
        docObjBean = this.getNombreArchivoMsj(pnuAnn, pnuEmi, pnuAnexo);
        if (docObjBean!=null && docObjBean.getNuTamano()>0){
            docVerBean.setInDoc(true);
        }
        
        if (docVerBean.isInDoc()){
            //Creando la Url del Documento 
            //Cambiar para colocar la seguridad maximo 5 segundos
            docVerBean.setDeMensaje("OK");
            //String urlDoc = "documento?accion=abrirAnexo&nuAnn="+docObjBean.getNuAnn()+"&nuEmi="+docObjBean.getNuEmi()+"&nuAne="+docObjBean.getNuAne();
            String urlDoc = "documento?accion=abrirAnexoMsj&nuAnn="+docObjBean.getNuAnn()+"&nuEmi="+docObjBean.getNuEmi()+"&nuAne="+docObjBean.getNuAne();//Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019

            //Ruta del Documento
            String nombreDoc = "TEMP|"+docObjBean.getNombreArchivo();
            
            docVerBean.setUrlDocumento(urlDoc);
            docVerBean.setNoDocumento(nombreDoc);
        }
        return(docVerBean);
    }

    @Override
    public DocumentoObjBean getNombreArchivoMsj(String pnuAnn, String pnuEmi, String pnuAnexo) {
        DocumentoObjBean docObjBean = null;
        docObjBean = mensajesDao.getNombreArchivoMsj(pnuAnn, pnuEmi, pnuAnexo);
        // Verificamos la Extencion del archivo
        if (docObjBean!=null && docObjBean.getNombreArchivo().length()>0 ){
            int pos = docObjBean.getNombreArchivo().lastIndexOf("\\");
            if (pos>0){
                docObjBean.setNombreArchivo(docObjBean.getNombreArchivo().substring(pos+1)); 
            }
            
            pos = docObjBean.getNombreArchivo().lastIndexOf(".");
            if (pos>0){
                docObjBean.setTipoDoc(docObjBean.getNombreArchivo().substring(pos+1).toLowerCase()); 
            }else{
                docObjBean.setTipoDoc("archivo"); 
            }
        }
        return(docObjBean);
    }

    @Override
    public String deleteMsj(DescargaMensajeBean descargaMensaje) {
        String vReturn = "ERR";
        try{
            vReturn =mensajesDao.updMensajeriaDocumentoAntesEliminar(descargaMensaje);
            if ("NO_OK".equals(vReturn)) {
                vReturn = "Error al Actualizar el anexo en Auditoria";
            }else{   
                vReturn =mensajesDao.deleteMsj(descargaMensaje);
            }
        }catch(Exception e){
            vReturn = "ERR";
        }
        
        return vReturn;
    }

    @Override
    public String revMensajeriaDocumento(DescargaMensajeBean descargaMensaje) {
                String vReturn = "ERR";
        try{
           vReturn =   mensajesDao.revMensajeriaDocumento(descargaMensaje);
        }catch(Exception e){
            vReturn = "ERR";
        }
        
        return vReturn;
    }

    @Override
    public String selectCalFechaPlazo(DescargaMensajeBean descargaMensaje) {
        String vReturn = "ERR";
        try{
           vReturn =   mensajesDao.selectCalFechaPlazo(descargaMensaje);
        }catch(Exception e){
            vReturn = "ERR";
        }
        
        return vReturn;
    }

    @Override
    public List<MensajesConsulBean> getBuscaDocumentosMsj(String codigos) {
        List<MensajesConsulBean> list = null;
        try{
            list = mensajesDao.getBuscaDocumentosMsj(codigos);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return list;
    }

    @Override
    public String updMensajeriaDocumentoMasivo(DescargaMensajeBean descargaMensaje) {
        String vReturn = "ERR";
        List<MensajesConsulBean> list = null;
        try{
            //con los codigos busco los registros.
            list = mensajesDao.getBuscaDocumentosMsj(descargaMensaje.getCodigos());
                       
            //realizo bucle para actualizar
           if(list!=null && list.size()>0){
               for(MensajesConsulBean m: list){
                   descargaMensaje.setNu_msj(m.getNu_msj());
                   descargaMensaje.setNu_emi(m.getNu_emi());
                   descargaMensaje.setNu_des(m.getNu_des());
                   descargaMensaje.setNu_ann(m.getNu_ann());
                   System.out.println("->bean:");
                   System.out.println(descargaMensaje.toString());
                   
                   /*-----------------Hermes 15/08/2018----------------------*/
                   descargaMensaje.setFe_pla_msj(m.getFe_pla_msj());
                   descargaMensaje.setDi_pla_dev(m.getDias_pla_devo_d());
                   descargaMensaje.setPe_env_msj(m.getPe_env_msj_d());
                   /*-----------------Hermes 15/08/2018----------------------*/
                   vReturn =   mensajesDao.updMensajeriaDocumento(descargaMensaje);                   
               }           
           }            
            
           
        }catch(Exception e){
            vReturn = "ERR";
        }
        
        return vReturn;
    }
    
    //Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019    
    @Override
    public String deleteMsjAdicional(DescargaMensajeBean descargaMensaje) {
        String vReturn = "ERR";
        try{
           vReturn =mensajesDao.deleteMsjAdicional(descargaMensaje);
        }catch(Exception e){
            vReturn = "ERR";
        }
        
        return vReturn;
    }
    
    @Override
    public String updMensajeriaDocumentoAdicional(DescargaMensajeBean descargaMensaje, String coUsu) {
         String vReturn = "ERR";
        try{           
            vReturn =   mensajesDao.updMensajeriaDocumentoAdicional(descargaMensaje, coUsu);
        }catch(Exception e){
            vReturn = "ERR";
        }
        
        return vReturn;        
    }
    
    @Override
    public List<DocumentoAnexoBean> getAnexosListMsj(String pnuAnn, String pnuEmi, String pnuDes) {
        List<DocumentoAnexoBean> list = null;
        list = mensajesDao.getAnexosListMsj(pnuAnn, pnuEmi, pnuDes);
        return (list);        
    }

    @Override
    public String insArchivoAnexoMsj(String coUsu, String pnuAnn, String pnuEmi, String pnuDes, DocumentoFileBean pfileAnexo, String pinUpd) {
        String vReturn = "NO_OK";
        String vnuAne = "";
        if(pinUpd.equals("0")){
            vnuAne = mensajesDao.getUltimoAnexo(pnuAnn, pnuEmi);
        }else{
            vnuAne = mensajesDao.getUltimoAnexoAdicional(pnuAnn, pnuEmi);
        }

        DocumentoAnexoDesBean docAnexo = new DocumentoAnexoDesBean();

        docAnexo.setNuAnn(pnuAnn);
        docAnexo.setNuEmi(pnuEmi);
        docAnexo.setNuAne(vnuAne);
        docAnexo.setNuDes(pnuDes);
        docAnexo.setDeDet(pfileAnexo.getNombreArchivo());
        docAnexo.setDeRutOri(pfileAnexo.getNombreArchivo());
        docAnexo.setCoUseCre(coUsu);
        docAnexo.setCoUseMod(coUsu);
        
        validarAnexoDes(docAnexo);

        final InputStream archivoAnexo = new ByteArrayInputStream(pfileAnexo.getArchivoBytes());
        final int size = pfileAnexo.getArchivoBytes().length;
        int maxUploadSize=10000000;

        try {
            if(size<=maxUploadSize){
                vReturn = mensajesDao.insArchivoAnexoMsj(docAnexo, archivoAnexo, size, pinUpd);
                vReturn = vReturn+vnuAne;
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(archivoAnexo!=null){
                    archivoAnexo.close();                                        
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }                

        return vReturn;        
    }  

    @Override
    public DocumentoVerBean getNombreDocMsjAdicional(String pnuAnn, String pnuEmi, String pnuAnexo, String pnuAne) {
        DocumentoVerBean docVerBean= new DocumentoVerBean();
        
        docVerBean.setNuAnn(pnuAnn);
        docVerBean.setNuEmi(pnuEmi);
        docVerBean.setNuAne(pnuAne); 
        docVerBean.setInDoc(false);
        
        DocumentoObjBean docObjBean = null;
        
        docObjBean = this.getNombreArchivoMsjAdicional(pnuAnn, pnuEmi, pnuAne);
        if (docObjBean!=null && docObjBean.getNuTamano()>0){
            docVerBean.setInDoc(true);
        }
        
        if (docVerBean.isInDoc()){
            docVerBean.setDeMensaje("OK");
            String urlDoc = "documento?accion=abrirAnexoMsj&nuAnn="+docObjBean.getNuAnn()+"&nuEmi="+docObjBean.getNuEmi()+"&nuAne="+docObjBean.getNuAne();//Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
            String nombreDoc = "TEMP|"+docObjBean.getNombreArchivo();
            
            docVerBean.setUrlDocumento(urlDoc);
            docVerBean.setNoDocumento(nombreDoc);
        }
        return(docVerBean);        
    }

    @Override
    public DocumentoObjBean getNombreArchivoMsjAdicional(String pnuAnn, String pnuEmi, String pnuAnexo) {
     DocumentoObjBean docObjBean = null;
        docObjBean = mensajesDao.getNombreArchivoMsjAdicional(pnuAnn, pnuEmi, pnuAnexo);

        if (docObjBean!=null && docObjBean.getNombreArchivo().length()>0 ){
            int pos = docObjBean.getNombreArchivo().lastIndexOf("\\");
            if (pos>0){
                docObjBean.setNombreArchivo(docObjBean.getNombreArchivo().substring(pos+1)); 
            }
            
            pos = docObjBean.getNombreArchivo().lastIndexOf(".");
            if (pos>0){
                docObjBean.setTipoDoc(docObjBean.getNombreArchivo().substring(pos+1).toLowerCase()); 
            }else{
                docObjBean.setTipoDoc("archivo"); 
            }
        }
        return(docObjBean);        
    }    
    //Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019      

    @Override
    public List<DocumentoAnexoBean> getAnexosListMsj2(String pnuAnn, String pnuEmi) {
        List<DocumentoAnexoBean> list = null;
        list = mensajesDao.getAnexosListMsj2(pnuAnn, pnuEmi);
        return (list);  
    }

    @Override
    public List<SiElementoBean> getListTipoDocAdicionalMsj(String pctabCodtab) {
     List<SiElementoBean> list = null;
        try {  
            list = mensajesDao.getListTipoDocAdicionalMsj(pctabCodtab);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;        
    }
}
