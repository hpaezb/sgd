package pe.gob.onpe.tramitedoc.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.libreria.exception.validarDatoException;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.AccionLog;
import pe.gob.onpe.tramitedoc.bean.BuscarAccionLog;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;
import pe.gob.onpe.tramitedoc.dao.ActionLogDao;
import pe.gob.onpe.tramitedoc.service.ActionLogService;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author hpaez
 */
@Service("actionLogService")
public class ActionLogServiceImp implements ActionLogService{

    @Autowired
    private ActionLogDao actionLogDao;
    
    @Autowired
    ApplicationProperties applicationProperties;
   
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String insActionLog(AccionLog accionLog, Usuario usuario) throws Exception {
        String vReturn = "NO_OK";
        
        try {
            accionLog.setnIdAction(actionLogDao.getNextAction());
            vReturn = actionLogDao.insActionLog(accionLog, usuario);
            if ("NO_OK".equals(vReturn)) {
                throw new validarDatoException("Error al Grabar Documento");
            }            
        } catch (Exception e) {
            e.printStackTrace();
            throw new validarDatoException("ERROR EN TRANSACCION");
        }
        return vReturn;
    }    

    @Override
    public AccionLog getIndicadorMenu(String coOpcion) throws Exception{
        AccionLog accionLog = null;
        try {
            accionLog = actionLogDao.getIndicadorMenu(coOpcion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return accionLog;
    }

    @Override
    public SiElementoBean getConfigLog(String coOpcion, String coEstadoDoc) throws Exception {
        SiElementoBean siElementoBean = null;
        try {
            siElementoBean = actionLogDao.getConfigLog(coOpcion, coEstadoDoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return siElementoBean;
    }

    @Override
    public List<AccionLog> getLstDocumentosLog(BuscarAccionLog buscarAccionLog) {
        List<AccionLog> list = null;
        try {
            list = actionLogDao.getLstDocumentosLog(buscarAccionLog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;          
    }

    @Override
    public ReporteBean getGenerarReporte(BuscarAccionLog buscarAccionLog, Map parametros) {
        ReporteBean objReporte = new ReporteBean();
        String eserror="1";//si
        String coRespuesta="";
        String deRespuesta="";
        String prutaReporte = "";
        String deNoDoc = "";
        String nombreArchivo = "";
        String coReporte = "";
        try{          
            
            String extensionArch;
            int tipoArchivo=0;
            byte[] archivo;
            
            String fecha = Utility.getInstancia().getStringFechaYYYYMMDDHHmm(new Date());  
            
            List lista = null;
            try{
                lista = getListaReporte(buscarAccionLog);
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
            if(buscarAccionLog.getFormatoReporte().equalsIgnoreCase("PDF")){
                coReporte = "TDR14";
                //pcoReporte = "TDR10_XLS";
                extensionArch=".pdf";
                tipoArchivo = 2;
            }else{
                coReporte = "TDR14_XLS";
                extensionArch=".xls";
                tipoArchivo = 1;
            }
            String rutaJasper =  buscarAccionLog.getRutaReporteJasper() + "/" + coReporte + ".jasper";
            
            buscarAccionLog.setRutaReporteJasper(rutaJasper);
            
            nombreArchivo = Utilidades.generateRandomNumber(10);
            prutaReporte = "EMISION_"+nombreArchivo + extensionArch;
            deNoDoc = "temp|EMISION_"+fecha+extensionArch;
            
            Utilidades util = new Utilidades();
            archivo = util.GenerarReporteLista(buscarAccionLog.getRutaReporteJasper(), lista, coReporte, parametros, tipoArchivo);
            
            if (archivo==null) {
                eserror="1";
                coRespuesta="1";
                deRespuesta = "No se generó correctamente el archivo.";
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
            
            if(eserror.equals("0")){
                coRespuesta=eserror;
            }else{
                coRespuesta="1";
            }
            
        }catch(JRException ex){
            Logger.getLogger(ActionLogServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror="1";
            deRespuesta=ex.getMessage();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ActionLogServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror="1";
            deRespuesta=ex.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(ActionLogServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror="1";
            deRespuesta=ex.getMessage();
        }  
        finally{
            objReporte.setcoRespuesta(coRespuesta);
            objReporte.setdeRespuesta(deRespuesta);
            objReporte.setnoUrl(prutaReporte);
            objReporte.setnoDoc(deNoDoc);
            return objReporte;
        }        
    }

    @Override
    public List<AccionLog> getListaReporte(BuscarAccionLog buscarAccionLog) {
        return actionLogDao.getListaReporteBusqueda(buscarAccionLog);
    }

    @Override
    public int getCantidadDocEncontrados(String tipoDoc, String numeroDoc) {
        return actionLogDao.getCantidadDocEncontrados(tipoDoc, numeroDoc);
    }
}
