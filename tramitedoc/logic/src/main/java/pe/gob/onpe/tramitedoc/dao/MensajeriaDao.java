/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.dao;

import java.util.List; 
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoRecepMensajeriaBean; 
import pe.gob.onpe.tramitedoc.bean.DestiDocumentoEnvMensajeriaBean; 
import pe.gob.onpe.tramitedoc.bean.DestinoResBen;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoRecepMensajeriaBean; 
import pe.gob.onpe.tramitedoc.bean.EstacionDocumento;
import pe.gob.onpe.tramitedoc.bean.TipoElementoMensajeriaBean;

/**
 *
 * @author WCONDORI
 */
public interface MensajeriaDao {
    List<DocumentoRecepMensajeriaBean> getDocumentoRecepMensajeria(BuscarDocumentoRecepMensajeriaBean buscarDocumentoRecepMensajeriaBean);
    List<DestiDocumentoEnvMensajeriaBean> getLstDestinoEnvMensajeria(String pnuAnnpnuEmi);
    List<TipoElementoMensajeriaBean> getlistTipoElementoMensajeria(String tipo);
    List<TipoElementoMensajeriaBean> getListResponsableMensajeria(String tipo,String Ambito);
    String insMensajeriaDocumento(DocumentoRecepMensajeriaBean documentoMensajeria);
    String updMensajeriaDocumentoRecibir(String codigo, String usuario);/*[HPB-21/06/21]Fin Campos Auditoria-*/
    String updMensajeriaDocumentoDevolver(String codigo, String pEmiInsEstacionDoc, DocumentoEmiBean documentoEmiBean);/*--23/08/19 HPB Devolucion Doc a Oficina--*/ 
    String selectCalcularFechaPlazo(DocumentoRecepMensajeriaBean documentoMensajeria);
    List<DocumentoRecepMensajeriaBean> getLstDetalleMensajeria(DestinoResBen oDestinoResBen); 
    List<DestiDocumentoEnvMensajeriaBean> getEditLstDetalleMensajeria(String  nroMensajeria) ;
    
    List<DestiDocumentoEnvMensajeriaBean> getLstDestinoEnvMensajeria2(String pnuAnnpnuEmi, DocumentoRecepMensajeriaBean documentoRecepMensajeriaBean);
    String insMensajeriaDocumento2(DocumentoRecepMensajeriaBean documentoMensajeria, List<DestiDocumentoEnvMensajeriaBean> lstDestinos);
    
    String insEstacionDocumento(DocumentoEmiBean documentoEmiBean, EstacionDocumento estacionDocumento); /*--23/08/19 HPB Devolucion Doc a Oficina--*/ 
    List<DocumentoRecepMensajeriaBean> getLstDetalleMensajeriaVirtual(DestinoResBen oDestinoResBen);
}
