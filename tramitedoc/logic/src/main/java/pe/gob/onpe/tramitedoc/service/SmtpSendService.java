package pe.gob.onpe.tramitedoc.service;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.web.util.SmtpObject;

public interface SmtpSendService {
	public boolean envioCorreoUsuario(String titulo, String mensaje, String paraEmail, SmtpObject smtpObject, List<DocumentoObjBean> lstdocObjBean);	
	public boolean envioCorreoUsuario(String titulo, String mensaje, String paraEmail, SmtpObject smtpObject);
}
