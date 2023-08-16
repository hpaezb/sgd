package pe.gob.onpe.tramitedoc.web.util;

public class SmtpObject {

	private String accountMail;
	private String accountPass;
	private String personalName;
	private String host;
	private String port;
	private String auth;
	private String enable;
	private String filePathAdjunto;
	private String fileNameAdjunto;
        private byte[] documento;
	
	public String getAccountMail() {
		return accountMail;
	}
	
	public void setAccountMail(String accountMail) {
		this.accountMail = accountMail;
	}
	
	public String getAccountPass() {
		return accountPass;
	}
	
	public void setAccountPass(String accountPass) {
		this.accountPass = accountPass;
	}
	
	public String getPersonalName() {
		return personalName;
	}
	
	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getAuth() {
		return auth;
	}
	
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	public String getEnable() {
		return enable;
	}
	
	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getFilePathAdjunto() {
		return filePathAdjunto;
	}

	public void setFilePathAdjunto(String filePathAdjunto) {
		this.filePathAdjunto = filePathAdjunto;
	}

	public String getFileNameAdjunto() {
		return fileNameAdjunto;
	}

	public void setFileNameAdjunto(String fileNameAdjunto) {
		this.fileNameAdjunto = fileNameAdjunto;
	}

    /**
     * @return the documento
     */
    public byte[] getDocumento() {
        return documento;
    }

    /**
     * @param documento the documento to set
     */
    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }
	
}