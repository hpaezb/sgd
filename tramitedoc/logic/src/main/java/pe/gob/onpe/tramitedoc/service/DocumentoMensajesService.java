/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.service;

import java.util.List;
import java.util.Map;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoCargaMsjBean;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoRecepConsulBean;
import pe.gob.onpe.tramitedoc.bean.DescargaMensajeBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoFileBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoVerBean;
import pe.gob.onpe.tramitedoc.bean.ElementoMensajeroBean;
import pe.gob.onpe.tramitedoc.bean.MensajesConsulBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;


/**
 *
 * @author oti3
 */
public interface DocumentoMensajesService {
    List<ElementoMensajeroBean> getListResponsableMensajeria(String tipo);
    List<SiElementoBean> getListTipoElementoMensajeria(String pctabCodtab);
    List<MensajesConsulBean> getBuscaDocumentosCarga(BuscarDocumentoCargaMsjBean buscarDocumentoCargaMsjBean); 
    MensajesConsulBean getBuscaDocumentosMsj(String nu_ann,String nu_emi,String nu_des,String nu_msj);
    String updMensajeriaDocumento(DescargaMensajeBean descargaMensaje);
    String delMensajeriaDocumento(DescargaMensajeBean descargaMensaje);
    String deleteMsj(DescargaMensajeBean descargaMensaje);
    String insArchivoAnexoDes(String coUsu,String pnuAnn,String pnuEmi,String pnuDes,DocumentoFileBean pfileAnexo);
    ReporteBean getGenerarReporte(BuscarDocumentoCargaMsjBean buscarDocumentoRecepConsulBean,Map parametros);
    DocumentoVerBean getNombreDocMsj(String pnuAnn, String pnuEmi,String pnuAnexo);
    DocumentoObjBean getNombreArchivoMsj(String pnuAnn, String pnuEmi, String pnuAnexo);
    String revMensajeriaDocumento(DescargaMensajeBean descargaMensaje);
    String selectCalFechaPlazo(DescargaMensajeBean descargaMensaje);
    
    //jazanero
    List<MensajesConsulBean> getBuscaDocumentosMsj(String codigos);
    String updMensajeriaDocumentoMasivo(DescargaMensajeBean descargaMensaje);
    
    //Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
    List<DocumentoAnexoBean>  getAnexosListMsj(String pnuAnn, String pnuEmi, String pnuDes);
    String insArchivoAnexoMsj(String coUsu,String pnuAnn,String pnuEmi,String pnuDes,DocumentoFileBean pfileAnexo, String pinUpd);
    DocumentoVerBean getNombreDocMsjAdicional(String pnuAnn, String pnuEmi,String pnuAnexo, String pnuAne);
    DocumentoObjBean getNombreArchivoMsjAdicional(String pnuAnn, String pnuEmi, String pnuAnexo);
    String deleteMsjAdicional(DescargaMensajeBean descargaMensaje);
    String updMensajeriaDocumentoAdicional(DescargaMensajeBean descargaMensaje, String coUsu);
    List<DocumentoAnexoBean>  getAnexosListMsj2(String pnuAnn, String pnuEmi);
    List<SiElementoBean> getListTipoDocAdicionalMsj(String pctabCodtab);
    //Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
}
