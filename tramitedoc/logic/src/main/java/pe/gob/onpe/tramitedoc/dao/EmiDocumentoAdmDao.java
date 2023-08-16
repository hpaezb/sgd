/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.tramitedoc.bean.AudiEstadosMovDocBean;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DependenciaBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DestinoBean;
import pe.gob.onpe.tramitedoc.bean.DetalleEnvioDeCorreoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoProyectoBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteBean;
import pe.gob.onpe.tramitedoc.bean.MotivoBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaEmiDocBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaRemitoBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteEmiBean;
import pe.gob.onpe.tramitedoc.bean.TblRemitosBean;
/*interoperabilidad*/
import pe.gob.onpe.tramitedoc.bean.DatosInterBean;
/*interoperabilidad*/
/**
 *
 * @author ecueva
 */
public interface EmiDocumentoAdmDao {
//    List<DocumentoEmiBean> getDocumentosEmiAdm(BuscarDocumentoEmiBean buscarDocumentoEmiBean);
    List<DocumentoEmiBean> getDocumentosBuscaEmiAdm(BuscarDocumentoEmiBean buscarDocumentoEmi);
    DocumentoEmiBean getDocumentoEmitido(String pnuAnn, String pnuEmi);
    DocumentoEmiBean getDocumentoEmiAdm(String pnuAnn,String pnuEmi);
    ExpedienteBean getExpDocumentoEmitido(String pnuAnnExp, String pnuSecExp);
    EmpleadoBean getEmpleadoLocaltblDestinatario(String pcoDependencia);
    List<EmpleadoBean> getPersonalDestinatario(String pcoDepen);
    List<MotivoBean> getLstMotivoxTipoDocumento(String pcoDepen,String pcoTipDoc);
    List<DocumentoBean> getLstDocEmitidoRef(String pcoEmpEmi,String pcoDepen,String pannio,String ptiDoc,String pnuDoc);
    List<DocumentoBean> getLstDocRecepcionadoRef(String pcoDepen,String pannio,String ptiDoc,String pnuDoc);
    List<DestinatarioDocumentoEmiBean> getLstDestintariotlbEmi(String pnuAnn, String pnuEmi);
    List<ReferenciaBean> getLstDocumReferenciatblEmi(String pnuAnn, String pnuEmi);
    String getTipoDestinatarioEmi(String pnuAnn, String pnuEmi);
    String updDocumentoEmiBean(String nuAnn,String nuEmi,DocumentoEmiBean documentoEmiBean,ExpedienteBean expedienteBean,RemitenteEmiBean remitenteEmiBean,String pcoUserMod);
    String insDocumentoEmiBean(DocumentoEmiBean documentoEmiBean,ExpedienteBean expedienteBean,RemitenteEmiBean remitenteEmiBean);
    String insReferenciaDocumentoEmi(String nuAnn,String nuEmi,ReferenciaEmiDocBean referenciaEmiDocBean);
    String updReferenciaDocumentoEmi(String nuAnn,String nuEmi,ReferenciaEmiDocBean referenciaEmiDocBean);
    String delReferenciaDocumentoEmi(String nuAnn,String nuEmi,ReferenciaEmiDocBean referenciaEmiDocBean);
    String insDestinatarioDocumentoEmi(String nuAnn,String nuEmi,DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
    String updDestinatarioDocumentoEmi(String nuAnn,String nuEmi,DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
    String delDestinatarioDocumentoEmi(String nuAnn,String nuEmi,DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
    DocumentoEmiBean getDocumentoEmiAdmNew(String sEstadoDocEmi, String codDependencia);
    String insExpedienteBean(ExpedienteBean expedienteBean);
    String verificarDocumentoLeido(String pnuAnn, String pnuEmi);
    String anularDocumentoEmiAdm(DocumentoEmiBean documentoEmiBean);
    List<DestinatarioDocumentoEmiBean> getLstDestinatarioGrupo(String pcoGrupo,String pcoTipDoc);
    String updDocumentoObj(DocumentoObjBean docObjBean);
    String getCanDocumentoEmiDuplicados(DocumentoEmiBean documentoEmiBean);
    List<EmpleadoBean> getPersonalEditDocAdmEmision(String pcoDepEmi);
    String updEstadoDocumentoEmiAdm(DocumentoEmiBean documentoEmiBean);
    String updArchivarDocumento(DocumentoEmiBean documentoEmiBean,Usuario usuario);
    String updEstadoDocumentoEmitido(DocumentoEmiBean documentoEmiBean);
    String updDocumentoEmiAdmBean(String nuAnn, String nuEmi, DocumentoEmiBean documentoEmiBean, ExpedienteBean expedienteBean, RemitenteEmiBean remitenteEmiBean,String pcoUserMod);
    String getNumeroDocSiguienteAdm(String pnuAnn, String pcoDepEmi, String pcoDoc);
    DocumentoObjBean getPropiedadesArchivo(String pnuAnn, String pnuEmi, String ptiCap);
    String getNroCorrelativoDocumento(DocumentoEmiBean documentoEmiBean);
    String getNroCorrelativoDocumentoDel(DocumentoEmiBean documentoEmiBean);
    String delAllReferenciaDocumentoEmi(String nuAnn, String nuEmi);
    String delAllDestinatarioDocumentoEmi(String nuAnn, String nuEmi);
    String delDocumentoEmiAdm(DocumentoEmiBean documentoEmiBean);
    List<DependenciaBean> getListDestinatarioEmi(String pcoDepen, String pdeDepEmite);
    DocumentoEmiBean existeDocumentoReferenciado(BuscarDocumentoEmiBean buscarDocumentoEmiBean);
    List<DocumentoEmiBean> getDocumentosReferenciadoBusq(DocumentoEmiBean documentoEmiBean,String sTipoAcceso, String pnuPagina, int pnuRegistros);/* HPB 27/05/2020 - Requerimiento Paginación emitidos (Modificación) */
    List<DestinoBean> getListaDestinosCodDepTipoDes(String nu_ann, String nu_emi);
    List<ReferenciaRemitoBean> getListaReferenciaRemitos(String nu_ann, String nu_emi);
    List<DestinoBean> getListaDestinosCodPri(String nu_ann, String nu_emi);
    String getNumDestinos(String nu_ann, String nu_emi);
    String updRemitoResumenDestinatario(String pnuAnn, String pnuEmi, String vti_des, String vco_pri, String vnu_cant_des, String vresOriDes);
    String updRemitoResumenReferencia(String pnuAnn, String pnuEmi, String vti_ori, String vdeOriEmi);
    TblRemitosBean getDatosDocumento(String pnuAnn,String pnuEmi);
    String getCoEmplFirmoDocumento(String pnuAnn,String pnuEmi);
    DocumentoEmiBean getEstadoDocumento(String pnuAnn,String pnuEmi);
    String getNroCorrelativoEmision(DocumentoEmiBean documentoEmiBean);
    List<DestinoBean> getListaDestinosOriTipoDes(String nu_ann, String nu_emi);
    List<ReferenciaRemitoBean> getOriReferenciaLista(String nu_ann, String nu_emi);
    String updChangeToDespacho(DocumentoEmiBean documentoEmiBean);
    String updEstadoDocumentoEnvioNotificacion(DocumentoEmiBean documentoEmiBean);    
    List<DocumentoBean> getLstDocRecepcionadoRefMp(String pcoDepen, String pannio, String ptiDoc, String pnuDoc);
    DocumentoEmiBean getEstadoDocumentoAudi(String pnuAnn,String pnuEmi);
    String insPersonalVoBo(String nuAnn,String nuEmi,String coDep,String coEmp,String coUser);
    String delPersonalVoBo(String nuAnn,String nuEmi,String coDep,String coEmp);
    String getInNumeraDocAdm(String tipoDoc);
    String getInFirmaDocAdm(String tipoDoc);
    List<DestinatarioDocumentoEmiBean> getLstPersVoBoGrupo(String pcoGrupo);
    int getCantidadAnexo(DocumentoEmiBean documentoEmiBean);
    
    //jazanero
    DetalleEnvioDeCorreoBean getDetalleEnvioCorreo(String pnuAnn, String pnuEmi); 
    String updClaveDocumentoEmi(String nuAnn,String nuEmi,String referenciaEmiDocBean);
    String insDocumentoEmiBeanProyecto(DocumentoEmiBean documentoEmiBean,ExpedienteBean expedienteBean,RemitenteEmiBean remitenteEmiBean);
    String insDestinatarioDocumentoEmiProyecto(String nuAnn,String nuEmi,DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
    String updDestinatarioDocumentoEmiProyecto(String nuAnn,String nuEmi,DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
    String delDestinatarioDocumentoEmiProyecto(String nuAnn,String nuEmi,DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
    List<DestinatarioDocumentoEmiBean> getLstDestintariotlbEmiProyecto(String pnuAnn, String pnuEmi);
    DocumentoProyectoBean getDocumentoEmiAdmProyecto(String pnuAnn,String pnuEmi);
    String getTipoDestinatarioEmiProyecto(String pnuAnn, String pnuEmi);
    DocumentoEmiBean getDocumentoEmiAdmProyectoAEmiBean(String pnuAnn,String pnuEmi);
    /* HPB 27/05/2020 Inicio - Requerimiento Paginación emitidos */
    //List<DocumentoEmiBean> getDocumentosBuscaEmiAdmFiltroReg(BuscarDocumentoEmiBean buscarDocumentoEmi);
    List<DocumentoEmiBean> getDocumentosBuscaEmiAdmFiltroReg(BuscarDocumentoEmiBean buscarDocumentoEmi, String pnuPagina, int pnuRegistros);
    List<DocumentoEmiBean> getDocumentosBuscaEmiAdmFiltroRegSize(BuscarDocumentoEmiBean buscarDocumentoEmi);
    List<DocumentoEmiBean> getDocumentosReferenciadoBusqSize(DocumentoEmiBean documentoEmiBean,String sTipoAcceso);
    /* HPB 27/05/2020 Fin - Requerimiento Paginación emitidos */
    /*interoperabilidad segdi mvaldera*/    
    String insMesaVitual(DatosInterBean datosInter);
    /*interoperabilidad segdi mvaldera*/    
    List<DocumentoEmiBean> getLstTipoDestEmi(String pnuAnn,String pnuEmi);
    String insLogDocumentoEmiAdm(AudiEstadosMovDocBean usuario, String pnuCorEmi, String pcoDepEmi) throws Exception;/*--[HPB] LOG PROVEIDO 09/07/20--*/
    String updReferenciaDocumentoEmiAntesEliminar(String nuAnn,String nuEmi,ReferenciaEmiDocBean referenciaEmiDocBean);/*[HPB-21/06/21] Campos Auditoria-*/
    String updReferenciaDocumentoEmiAntesEliminarTodo(String nuAnn,String nuEmi,String usuario);/*[HPB-21/06/21] Campos Auditoria-*/
    String updDestinoDocumentoEmiAntesEliminarTodo(String nuAnn,String nuEmi,String usuario);/*[HPB-21/06/21] Campos Auditoria-*/
    /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente--*/
//    String insExtExpedienteBean(String nuAnn,String nuEmi,DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean, ExpedienteBean expedienteBean);
//    DocumentoEmiBean getExtensionExpediente(String pnuAnn,String pnuEmi, String pnuDes);
//    String delExtExpedienteBean(String nuAnn,String nuEmi,DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
//    String updExtExpedienteBean(String nuAnn,String nuEmi,DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
//    List<ReferenciaBean> getDocSegParaExtExp(String pnuAnn,String pnuEmi, String pNuAnnRef, String pnuEmiRef);
    /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente --*/
}
