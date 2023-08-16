package pe.gob.onpe.tramitedoc.service.impl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.service.SmtpSendService;
import pe.gob.onpe.tramitedoc.web.controller.DocumentoObjetoController;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;
import pe.gob.onpe.tramitedoc.web.util.SmtpObject;

@Service("smtpSendService")
public class SmtpSendServiceImp implements SmtpSendService{

    @Autowired
    ApplicationProperties applicationProperties;

    
    public static synchronized boolean Mail(String titulo, String mensaje, String paraEmail, SmtpObject smtpObject, List<DocumentoObjBean> lstdocObjBean) 
    {
        

        System.out.println("Entro: Mail2");
        System.out.println("paraEmail: "+paraEmail);
    	
    	boolean envio = false;
                 
    	final String accountMail = null; //smtpObject.getAccountMail();
        final String accountPass = null; //smtpObject.getAccountPass(); 
        
        final String personalName = smtpObject.getPersonalName();
        
        try {

            //Propiedades de la conexion
            Properties propiedades = new Properties();
            Session session;
            propiedades.setProperty("mail.smtp.host", smtpObject.getHost());
            propiedades.setProperty("mail.smtp.port", smtpObject.getPort());                        
            
            //propiedades.setProperty("mail.smtp.ssl.enable", smtpObject.getEnable());
            //propiedades.setProperty("mail.smtp.starttls.enable", smtpObject.getEnable());

            //Preparamos la Sesion autenticando al usuario
            if(false){
            	propiedades.setProperty("mail.smtp.auth", smtpObject.getAuth());
                session = Session.getInstance(propiedades, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(accountMail, accountPass);
                    }
                });
            }else{
            	System.out.println("0"); 
            	session = Session.getInstance(propiedades,null);
            }
            

            //Preparamos el Mensaje
            //ORIGEN
            InternetAddress remitente = new InternetAddress(smtpObject.getAccountMail(),personalName);
						
            MimeMessage message = new MimeMessage(session);
            message.setFrom(remitente);
                        
            
          //Preparamos el Mensaje            
            MimeBodyPart  texto = new MimeBodyPart();
            texto.setContent(mensaje, "text/html; charset=utf-8");
            
            //CONTENIDO ADJUNTO PDF
            MimeBodyPart adjunto = null;
            if(smtpObject.getDocumento()!=null){
                DataSource dataSource = new ByteArrayDataSource(smtpObject.getDocumento(), "application/pdf");
                adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(dataSource));
                adjunto.setFileName(smtpObject.getFileNameAdjunto());
                
//                adjunto.setDataHandler(new DataHandler(new FileDataSource(smtpObject.getFilePathAdjunto())));
//                adjunto.setFileName(smtpObject.getFileNameAdjunto());
                
            }
            
            //MULTIPARTE PARA AGREGAR AL CORREO DATOS
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);				
            if(smtpObject.getDocumento() != null) multiParte.addBodyPart(adjunto);


            //CONTENIDO ADICIONAL ADJUNTOS
            BodyPart adjuntoAdicional =null;
            if(lstdocObjBean.size()>0){				
                    for(DocumentoObjBean  adj:lstdocObjBean){						
                            adjuntoAdicional = new MimeBodyPart();//   http:\mailing.culturainforma.pe\rbc_prod\fotos\bmu591811436.pdf   ++adj.getRepositorio()					
//                            URL url = new URL(adj.getRepositorio()+ adj.getNombreRealArchivo());
                            //System.out.println("adj.getRepositorio()+ adj.getNombreRealArchivo(): "+adj.getRepositorio()+ adj.getNombreRealArchivo());					
//                            URLDataSource ds = new URLDataSource(url); 					
//                            adjuntoAdicional.setDataHandler(new DataHandler(ds));//   "d:/futbol.gif"
//                            adjuntoAdicional.setFileName(adj.getNombreArchivo()+adj.getExtensionArchivo() );//"futbol.gif"

                            adjuntoAdicional.setDataHandler(new DataHandler(new FileDataSource(adj.getRutabase())));
                            adjuntoAdicional.setFileName(adj.getNombreArchivo());

                            multiParte.addBodyPart(adjuntoAdicional);
                    }				
            }            
            
//            MimeMultipart multiParte = new MimeMultipart();
//            multiParte.addBodyPart(texto);
//            multiParte.addBodyPart(adjunto);
            
            System.out.println("00");
            System.out.println("00"+smtpObject.getFilePathAdjunto());
            System.out.println("00"+smtpObject.getFileNameAdjunto());
            
            message.setSender(new InternetAddress(smtpObject.getAccountMail()));
            message.setSubject(titulo);        
			
            message.setReplyTo(InternetAddress.parse(smtpObject.getAccountMail()));
            System.out.println("02");
            
            if (paraEmail.indexOf(',') >= 0) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(paraEmail));
            } else {
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(paraEmail));
            }
            
            message.setRecipient(Message.RecipientType.BCC, new InternetAddress("oti10@presidencia.gob.pe"));
            message.setContent(multiParte);
            
            System.out.println("1");  
            //ENVIAR
            if(false){
            	System.out.println("2"); 
            	Transport transport = session.getTransport("smtp");
                message.saveChanges();
                Transport.send(message);
                transport.close();                
                System.out.println("Correo enviado satisfactoriamente.");
            }else{
            	System.out.println("5");
            	message.saveChanges();
            	System.out.println("3"); 
            	Transport.send(message);
            	System.out.println("Correo enviado satisfactoriamente.");            	
            }
            
            System.out.println("--> CORREO: Enviado de "+smtpObject.getAccountMail()+ " a "+paraEmail);  
            envio = true;
        
        } catch (MessagingException e) {
        	envio = false;
        	Logger.getLogger(SmtpSendServiceImp.class.getName()).log(Level.SEVERE, null, e);
			e.getMessage();
		} catch (UnsupportedEncodingException e) {
			envio = false;
			Logger.getLogger(SmtpSendServiceImp.class.getName()).log(Level.SEVERE, null, e);
			e.getMessage();
		} catch (Exception e) {			
			Logger.getLogger(SmtpSendServiceImp.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}
         finally {
            
        }
        
        return envio;    	
    }
    

    @Override
    public boolean envioCorreoUsuario(String titulo, String mensaje, String paraEmail, SmtpObject smtpObject, List<DocumentoObjBean> lstdocObjBean) {
        return SmtpSendServiceImp.Mail(titulo, mensaje, paraEmail, smtpObject, lstdocObjBean);
    }

    public boolean envioCorreoUsuario(String titulo, String mensaje, String paraEmail, SmtpObject smtpObject, String none) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean envioCorreoUsuario(String titulo, String mensaje, String paraEmail, SmtpObject smtpObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	
}