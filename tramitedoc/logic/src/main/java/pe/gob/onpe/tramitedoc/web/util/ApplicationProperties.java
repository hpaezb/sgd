package pe.gob.onpe.tramitedoc.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;
import pe.gob.onpe.libreria.util.ConstantesSec;
import pe.gob.onpe.libreria.util.Utility;


/**
 * Created by IntelliJ IDEA.
 * User: crosales
 * Date: 27/04/12
 * Time: 02:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Component("applicationProperties")
public class ApplicationProperties {

    @Autowired @Qualifier(value = "applicationProps")
    private Properties properties;

    private String coAplicativo;
    private String staticResourceServer;
    private String rutaTemporal;

    private String reportServerA;
    private String reportServerB;
    private String reportServiceA;
    private String reportServiceB;
    private String reportUserId;

    private String applicationVersion;
    private String localCacheObjectsVersion;
    private String resourcesVersion;
    private String versionJre;
    private String coMotSegui;
    private int diasAntesExpira;
    private String nroRucInstitu;
    private String rutaReportes;
    private int topRegistrosConsultas;    
    private String urlSgdRestTask;
    private String siglaInstitucion;
    private String logoReporteB64;
    
    public Properties getProperties() {
        return properties;
    }

    public String getLogoReporteB64() {
         String retval="";
        try {
            retval=(properties.getProperty("B64_LOGO_REPORTE")==null?"":properties.getProperty("B64_LOGO_REPORTE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    public String getSiglaInstitucion(){
        String retval="";
        try {
            //retval=Utility.getInstancia().descifrar(properties.getProperty("sigla_institucion"),ConstantesSec.SGD_SECRET_KEY_PROPERTIES);
            retval=properties.getProperty("sigla_institucion");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }

    public String getRutaReportes() {
        return properties.getProperty("ruta_reportes");
    }

    public String getNroRucInstitu() {
        String retval="";
        try {
            retval=Utility.getInstancia().descifrar(properties.getProperty("nro_ruc"),ConstantesSec.SGD_SECRET_KEY_PROPERTIES);
            System.out.println("retval--> "+ retval);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }    

    public int getDiasAntesExpira() {
        String retval="";
        try {
            retval=Utility.getInstancia().descifrar(properties.getProperty("dias_antes_expira"),ConstantesSec.SGD_SECRET_KEY_PROPERTIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(retval);
    }    

    public String getCoMotSegui() {
        String retval="";
        try {
            retval=Utility.getInstancia().descifrar(properties.getProperty("co_mot_segui"),ConstantesSec.SGD_SECRET_KEY_PROPERTIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }
    
    public String getCoAplicativo() {
        String retval="";
        try {
            retval=Utility.getInstancia().descifrar(properties.getProperty("co_aplicativo"),ConstantesSec.SGD_SECRET_KEY_PROPERTIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }

//    public String getStaticResourceServer() {
//        String retval="";
//        try {
//            retval=Utility.getInstancia().descifrar(properties.getProperty("sigla_institucion"),ConstantesSec.SGD_SECRET_KEY_PROPERTIES);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return properties.getProperty("static_resource_server");
//    }

    public String getRutaTemporal() {
        String retval="";
        try {
        //YUAL DESCOMENTAR EN SERVIDOR        
           retval=Utility.getInstancia().descifrar(properties.getProperty("ruta_temporal"),ConstantesSec.SGD_SECRET_KEY_PROPERTIES);
//         retval="C:\\GlassFish4.1\\glassfish\\tmppcm";
//         retval="D:\\glassfish4\\glassfish\\tmppcm";
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }

    public String getApplicationVersion() {
        return properties.getProperty("application.version");
    }

    public String getLocalCacheObjectsVersion() {
        return properties.getProperty("localCacheObjects.version");
    }

    public String getResourcesVersion() {
       return properties.getProperty("resources.version");
    }

    public String getVersionJre() {
       return properties.getProperty("version_jre");
    }

    public int getTopRegistrosConsultas() {
        String retval="";
        try {
            retval=Utility.getInstancia().descifrar(properties.getProperty("topRegistros_consultas"),ConstantesSec.SGD_SECRET_KEY_PROPERTIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(retval);
    }
    public String getFormatoSiglas() {
        String retval="";
        try {
            retval=properties.getProperty("formato_siglas");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }    
//No usados
    public String getUrlSgdRestTask() {
        return properties.getProperty("url_sgdRestTask");
    }

    public String getReportServerA() {
        return properties.getProperty("report_serverA");
    }

    public String getReportServerB() {
        return properties.getProperty("report_serverB");
    }

    public String getReportServiceA() {
        return properties.getProperty("report_serviceA");
    }

    public String getReportServiceB() {
        return properties.getProperty("report_serviceB");
    }

    public String getReportUserId() {
        return properties.getProperty("report_user_id");
    }    
    /*Hermes 11/06/20 Inicio - Agregar Captcha*/
    public String getIndicadorCaptchaExterno() {
        String retval="";
        try {
            retval=properties.getProperty("captcha_externo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }
    /*Hermes 11/06/20 Fin - Agregar Captcha*/
}
