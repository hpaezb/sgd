package pe.gob.onpe.tramitedoc.service;

import java.util.HashMap;
import java.util.List;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoRecepBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaBean;
import pe.gob.onpe.tramitedoc.util.Paginacion;

public interface RecepDocumentoAdmService {

//	List<DocumentoBean> getDocumentosRecepAdm(
//			BuscarDocumentoRecepBean buscarDocumentoRecep);
        List<DocumentoBean> getDocumentosRecepAdm(BuscarDocumentoRecepBean buscarDocumentoRecep, Paginacion paginacion);
        
	List<DocumentoBean> getDocumentosBuscaRecepAdm(
			BuscarDocumentoRecepBean buscarDocumentoRecep);        
        DocumentoBean getDocumentoRecepAdm(String pnuAnn,String pnuEmi,String pnuDes);
        BuscarDocumentoRecepBean estadoRecepcionDocumento(BuscarDocumentoRecepBean buscarDocumentoRecepBean,String accion);
        ExpedienteBean getExpDocumentoRecepAdm(String pnuAnnExp,String pnuSecExp);
        List<ReferenciaBean> getDocumentosRefRecepAdm(String pnuAnn,String pnuEmi);
        String updDocumentoRecepAdm(DocumentoBean documentoBean,String accion,String sEsDocAnu, Usuario usuario) throws Exception;
        HashMap getDocumentosEnReferencia(BuscarDocumentoRecepBean buscarDocumentoRecepBean, String pnuPagina, int pnuRegistros);
        String getVerificaAtendido(String pnuAnn, String pnuEmi, String pnuDes);
        List<ReferenciaBean> getConsultaAtendido(String pnuAnn, String pnuEmi, String pnuDes);
        //servicio rest notificaciones movil
        String updDocumentoRecepAdm(DocumentoBean doc, String accion, Usuario usu) throws Exception;
        String changeToAnulado(String nuAnn, String nuEmi, String nuDes, String coUseMod) throws Exception;
        //servicio rest notificaciones movil
        
        List<DocumentoBean> getDocumentosBuscaRecepAdm2(BuscarDocumentoRecepBean buscarDocumentoRecep, String pnuPagina, int pnuRegistros); 
        /*HPB 20/02/2020 - Inicio - Requerimiento Paginación recepcionados*/
        List<DocumentoBean> getDocumentosBuscaRecepAdm2Size(BuscarDocumentoRecepBean buscarDocumentoRecep); 
        HashMap getDocumentosEnReferenciaSize(BuscarDocumentoRecepBean buscarDocumentoRecepBean);
        /*HPB 20/02/2020 - Fin - Requerimiento Paginación recepcionados*/
        String actualizarEstadoPlazoAtencion(DocumentoBean documentoBean, String inButtonRed); /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
}