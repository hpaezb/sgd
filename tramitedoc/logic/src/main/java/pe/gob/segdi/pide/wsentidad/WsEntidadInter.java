/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.segdi.pide.wsentidad;

import java.net.URL;
import javax.xml.ws.BindingProvider;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
/**
 *
 * @author mvaldera
 */
public class WsEntidadInter {
        public String getInter(String rucent,String Url) throws Exception{
            String ventidad = "N";
            try
            {
                        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        
                PcmIMgdEntidad service = new  PcmIMgdEntidad(new URL(Url));
                PcmIMgdEntidadPortType entidad = service.getPcmIMgdEntidadHttpsSoap11Endpoint();
                BindingProvider bindingProvider = (BindingProvider) entidad;
                bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Url);
                ventidad = entidad.validarEntidad(rucent);
                if (!ventidad.equals("-1"))
                {
                    ventidad="S";
                }
                else
                {
                    ventidad="N";
                }  
            }            
            catch(Exception e)
            {
                ventidad="N";
            }                    

            return ventidad;
	}
}
