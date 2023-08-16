/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoRecepConsulBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoRecepConsulBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaConsulBean;

/**
 *
 * @author NGilt
 */
public interface ConsultaRecepDocDao {
    List<DocumentoRecepConsulBean> getDocumentosBuscaRecepAdm(BuscarDocumentoRecepConsulBean buscarDocumentoRecepConsulBean);
    DocumentoRecepConsulBean getDocumentoRecepAdm(String pnuAnn,String pnuEmi,String pnuDes);
    List<ReferenciaConsulBean> getDocumentosRefRecepAdm(String pnuAnn,String pnuEmi);
    DocumentoRecepConsulBean existeDocumentoReferenciado(BuscarDocumentoRecepConsulBean buscarDocumentoRecepConsulBean);
    List<DocumentoRecepConsulBean> getDocumentosReferenciadoBusq(DocumentoRecepConsulBean documentoRecepConsulBean,String ptipoAcceso);
    //List<DocumentoRecepConsulBean> getDocumentosReferenciadoBusq(DocumentoRecepConsulBean documentoRecepConsulBean,String sTipoAcceso, String pnuPagina, int pnuRegistros); // gct 30/07/2020
    String getRutaReporte(BuscarDocumentoRecepConsulBean buscarDocumentoRecepConsulBean);
    List<DocumentoRecepConsulBean> getListaReporteBusqueda(BuscarDocumentoRecepConsulBean buscarDocumentoExtConsulBean);
    /* GCT 31/07/2020 - Inicio - Requerimiento Paginación consulta recepcion */
    //public List<DocumentoRecepConsulBean> getDocumentosBuscaEmiAdmFiltro(BuscarDocumentoRecepConsulBean buscarDocumentoRecepConsulBean, String pnuPagina, int pnuRegistros);    //Hermes 05/09/2018
    public List<DocumentoRecepConsulBean> getDocumentosBuscaRecepAdmFiltro(BuscarDocumentoRecepConsulBean buscarDocumentoRecepConsulBean, String pnuPagina, int pnuRegistros);    //gct 31/07/2020
    public List<DocumentoRecepConsulBean> getDocumentosBuscaRecepAdmFiltroSize(BuscarDocumentoRecepConsulBean buscarDocumentoRecepConsulBean); //gct 31/07/2020
    //List<DocumentoRecepConsulBean> getDocumentosReferenciadoBusqSize(BuscarDocumentoRecepConsulBean buscarDocumentoRecepConsulBean,String sTipoAcceso);
    /* GCT 31/07/2020 - Fin - Requerimiento Paginación consulta recepcion */    
}
