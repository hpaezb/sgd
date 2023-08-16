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
import pe.gob.onpe.libreria.util.Utility;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiPersConsulBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.dao.ConsultaEmiDocPersDao;
import pe.gob.onpe.tramitedoc.dao.ReporteAcervoDocPerDao;
import pe.gob.onpe.tramitedoc.service.ReporteAcervoDocPerService;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.Utilidades;

@Service("reporteAcervoDocPerService")
public class ReporteAcervoDocPerServiceImp implements ReporteAcervoDocPerService{

    @Autowired
    ApplicationProperties applicationProperties;
    
    @Autowired
    private ConsultaEmiDocPersDao consultaEmiDocPersDao;  
    
    @Autowired
    private ReporteAcervoDocPerDao reporteAcervoDocPerDao;
    
    @Override
    public ReporteBean getGenerarReporteAcervoDocPer(DocumentoEmiPersConsulBean buscarDocPer, Map parametros) {
        List lista = null;
        ReporteBean objReporte = new ReporteBean();
        String eserror="1";//si
        String coRespuesta="";
        String deRespuesta="";
        String prutaReporte = "";
        String deNoDoc = "";
        String nombreArchivo = "";
        String coReporte = "";

        
        try {
            String extensionArch;
            int tipoArchivo = 0;
            byte[] archivo;
            
            String fecha = Utility.getInstancia().getStringFechaYYYYMMDDHHmm(new Date());
            try {
                lista = getListaReporteAcervo(buscarDocPer);
                eserror = "0";
            } catch (Exception e) {
                eserror = "1";
                deRespuesta = e.getMessage();
            }
            if (lista.size()==0) {                 
                 coRespuesta = "1";
                 deRespuesta="No existe información para generar reporte.";
                 throw new Exception(deRespuesta);
            }
            
            if(buscarDocPer.getFormatoReporte().equalsIgnoreCase("PDF")){
                coReporte = "TDR34";                 
                extensionArch=".pdf";
                tipoArchivo = 2;
            }else{
                coReporte = "TDR34_XLS";
                extensionArch=".xls";
                tipoArchivo = 1;
            }
            String rutaJasper =  buscarDocPer.getRutaReporteJasper() + "/" + coReporte + ".jasper";
            
            buscarDocPer.setRutaReporteJasper(rutaJasper);
            
            nombreArchivo = Utilidades.generateRandomNumber(10);
            prutaReporte = "EMISIONPERS_"+nombreArchivo + extensionArch;
            deNoDoc = "temp|EMISIONPERS_"+fecha+extensionArch;
            
            Utilidades util = new Utilidades();
            archivo = util.GenerarReporteLista(buscarDocPer.getRutaReporteJasper(), lista, coReporte, parametros, tipoArchivo);
            
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
            Logger.getLogger(ConsultaEmiDocPerServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror="1";
            deRespuesta=ex.getMessage();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConsultaEmiDocPerServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror="1";
            deRespuesta=ex.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(ConsultaEmiDocPerServiceImp.class.getName()).log(Level.SEVERE, null, ex);
            eserror="1";
            deRespuesta=ex.getMessage();
        } catch (Exception e) {
            Logger.getLogger(ConsultaEmiDocPerServiceImp.class.getName()).log(Level.SEVERE, null, e);
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
    public List<DocumentoEmiPersConsulBean> getListaReporteAcervo(DocumentoEmiPersConsulBean buscarDocPer) {
        return reporteAcervoDocPerDao.getListaReporteAcervoDocPers(buscarDocPer);
    }

    @Override
    public List<DocumentoEmiPersConsulBean> getDocsPersAcervoDocumentario(DocumentoEmiPersConsulBean buscarDocPer) {
        List<DocumentoEmiPersConsulBean> list = null;
        try {
            list = reporteAcervoDocPerDao.getDocsPersAcervoDocumentario(buscarDocPer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;          
    }

    @Override
    public List<EmpleadoBean> getPersonalEditAcervoDoc(String pcoDepEmi) {
        List<EmpleadoBean> list = null;
        try {
            list = reporteAcervoDocPerDao.getPersonalEditAcervoDoc(pcoDepEmi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;        
    }
    
}
