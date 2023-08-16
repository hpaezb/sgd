/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.dao;
import java.io.InputStream;
import pe.gob.onpe.tramitedoc.bean.MensajesConsulBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;
import pe.gob.onpe.tramitedoc.bean.EstadoDocumentoBean;
import pe.gob.onpe.tramitedoc.bean.ElementoMensajeroBean;
import java.util.List;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoCargaMsjBean;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DescargaMensajeBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoDesBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;

/**
 *
 * @author MVALDERA
 */
public interface MensajesDao {
        List<EstadoDocumentoBean> listEstadosMsj(String nomTabla);
        List<EstadoDocumentoBean> listEstadosCarga(String nomTabla);
        List<SiElementoBean> getLsSiElementoBean(String pctabCodtab);
        List<ElementoMensajeroBean> getListMensajero(String tipo);
        List<MensajesConsulBean> getBuscaDocumentosCarga(BuscarDocumentoCargaMsjBean buscarDocumentoCargaMsjBean);
        MensajesConsulBean getBuscaDocumentosMsj(String nu_ann,String nu_emi,String nu_des,String nu_msj);
        String updMensajeriaDocumento(DescargaMensajeBean descargaMensaje);
        String delMensajeriaDocumento(DescargaMensajeBean descargaMensaje);
        String deleteMsj(DescargaMensajeBean descargaMensaje);
        String insArchivoAnexoDes(DocumentoAnexoDesBean docAnexo, InputStream archivoAnexo ,int size);
        String getUltimoAnexo(String pnuAnn,String pnuEmi);
        DocumentoObjBean getNombreArchivoMsj(String pnuAnn, String pnuEmi, String pnuAnexo);
        String revMensajeriaDocumento(DescargaMensajeBean descargaMensaje);
        List<DependenciaBean> getLsOficina();
        String selectCalFechaPlazo(DescargaMensajeBean descargaMensaje);
        List<SiElementoBean> getLsMotivo(String pctabCodtab);
        
        //jazanero
        List<MensajesConsulBean> getBuscaDocumentosMsj(String codigos);
        String updMensajeriaDocumentoMasivo(DescargaMensajeBean descargaMensaje);
        
        //Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
        List<DocumentoAnexoBean>  getAnexosListMsj(String pnuAnn, String pnuEmi, String pnuDes);  
        String insArchivoAnexoMsj(DocumentoAnexoDesBean docAnexo, InputStream archivoAnexo ,int size, String inUpd);
        String getUltimoAnexoAdicional(String pnuAnn,String pnuEmi);
        DocumentoObjBean getNombreArchivoMsjAdicional(String pnuAnn, String pnuEmi, String pnuAnexo);
        String deleteMsjAdicional(DescargaMensajeBean descargaMensaje);
        String updMensajeriaDocumentoAdicional(DescargaMensajeBean descargaMensaje, String coUsu);
        List<DocumentoAnexoBean>  getAnexosListMsj2(String pnuAnn, String pnuEmi); 
        List<SiElementoBean> getListTipoDocAdicionalMsj(String pctabCodtab);
        //Hermes 22/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
        String updMensajeriaDocumentoAntesEliminar(DescargaMensajeBean descargaMensaje);/*[HPB-21/06/21] Campos Auditoria-*/
}
