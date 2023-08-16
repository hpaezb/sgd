package pe.gob.onpe.tramitedoc.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoDevolucionMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoDevolucionMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.dao.DocumentoDevolucionMensajeriaDao;
import pe.gob.onpe.tramitedoc.service.DocumentoDevolucionMensajeriaService;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

/**
 *
 * @author hpaez
 */
@Service("documentoDevolucionMensajeriaService")
public class DocumentoDevolucionMensajeriaServiceImpl implements DocumentoDevolucionMensajeriaService{

    @Autowired
    private DocumentoDevolucionMensajeriaDao documentoDevolucionMensajeriaDao;
    
    @Autowired
    ApplicationProperties applicationProperties;
     
    @Override
    public List<DocumentoDevolucionMensajeriaBean> getDocumentoDevolucionMensajeria(BuscarDocumentoDevolucionMensajeriaBean buscarDocumentoDevolucionMensajeriaBean) {
        List<DocumentoDevolucionMensajeriaBean> list = null;
        try {
            list = documentoDevolucionMensajeriaDao.getDocumentoDevolucionMensajeria(buscarDocumentoDevolucionMensajeriaBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;        
    }    

    @Override
    public ReporteBean getGenerarReporte(BuscarDocumentoDevolucionMensajeriaBean buscarDocumentoDevolucionMensajeriaBean, Map parametros) {
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
                lista = getDocumentoDevMensajeria(buscarDocumentoDevolucionMensajeriaBean);
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
            if(buscarDocumentoDevolucionMensajeriaBean.getFormatoReporte().equalsIgnoreCase("PDF")){
                coReporte = "TDR16";                
                extensionArch=".pdf";
                tipoArchivo = 2;
            }else{
                coReporte = "TDR16_XLS";
                extensionArch=".xls";
                tipoArchivo = 1;
            }
            
            String rutaJasper =  buscarDocumentoDevolucionMensajeriaBean.getRutaReporteJasper() + "/" + coReporte + ".jasper";
            buscarDocumentoDevolucionMensajeriaBean.setRutaReporteJasper(rutaJasper);
            
            System.out.println("Ruta");
            System.err.println(rutaJasper);
            
            nombreArchivo = Utilidades.generateRandomNumber(10);
            prutaReporte = "RPTDOCUMENTOSAL_"+nombreArchivo + extensionArch;
            deNoDoc = "temp|RPTDOCUMENTOSAL_"+fecha+extensionArch;
            
            Utilidades util = new Utilidades();
            archivo = util.GenerarReporteLista(buscarDocumentoDevolucionMensajeriaBean.getRutaReporteJasper(), lista, coReporte, parametros, tipoArchivo);
            
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

    public List<DocumentoDevolucionMensajeriaBean> getDocumentoDevMensajeria(BuscarDocumentoDevolucionMensajeriaBean buscarDocumentoDevolucionMensajeriaBean) {
        List<DocumentoDevolucionMensajeriaBean> list = null;
        try {
            buscarDocumentoDevolucionMensajeriaBean.setCoGrupo("1"); 
            list = documentoDevolucionMensajeriaDao.getDocumentoDevolucionMensajeria(buscarDocumentoDevolucionMensajeriaBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }    
}
