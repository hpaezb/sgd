/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DestinatarioDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DestinoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.ExpedienteBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaEmiDocBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaRemitoBean;
import pe.gob.onpe.tramitedoc.bean.RemitenteEmiBean;
import pe.gob.onpe.tramitedoc.bean.TblRemitosBean;

/**
 *
 * @author ecueva
 */
public interface EmiDocumentoPersonalDao {
  List<DocumentoEmiBean> getDocumentosEmiAdm(BuscarDocumentoEmiBean buscarDocumentoEmiBean);  
  DocumentoEmiBean getDocumentoEmiAdm(String pnuAnn, String pnuEmi);
  List<DestinatarioDocumentoEmiBean> getLstDestintariotlbEmi(String pnuAnn, String pnuEmi);
  List<ReferenciaBean> getLstDocumReferenciatblEmi(String pnuAnn, String pnuEmi);
  DocumentoEmiBean getDocumentoEmiAdmNew(String codDependencia,String codEmpleado,String codLocal);
  /* [HPB] Inicio 31/08/23 OS-0000786-2023 Mejoras:Generar doc personal con referencia */
  //String updDocumentoEmiAdmBean(DocumentoEmiBean documentoEmiBean);
  String updDocumentoEmiAdmBean(String nuAnn, String nuEmi,DocumentoEmiBean documentoEmiBean, ExpedienteBean expedienteBean, String pcoUserMod);
  /* [HPB] Fin 31/08/23 OS-0000786-2023 Mejoras:Generar doc personal con referencia */
  String insReferenciaDocumentoEmi(String nuAnn, String nuEmi, ReferenciaEmiDocBean referenciaEmiDocBean);
  String updReferenciaDocumentoEmi(String nuAnn, String nuEmi, ReferenciaEmiDocBean referenciaEmiDocBean);
  String delReferenciaDocumentoEmi(String nuAnn, String nuEmi, ReferenciaEmiDocBean referenciaEmiDocBean);
  String insDestinatarioDocumentoEmi(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
  String updDestinatarioDocumentoEmi(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
  String delDestinatarioDocumentoEmi(String nuAnn, String nuEmi, DestinatarioDocumentoEmiBean destinatarioDocumentoEmiBean);
  String insDocumentoEmiBean(DocumentoEmiBean documentoEmiBean);
  String getCanDocumentoEmiDuplicados(DocumentoEmiBean documentoEmiBean);
  String verificarDocumentoLeido(String nuAnn,String nuEmi);
  String updEstadoDocumentoEmiAdm(DocumentoEmiBean documentoEmiBean);
  String updDocumentoObj(final DocumentoObjBean docObjBean);
  String updEstadoDocumentoEmitido(DocumentoEmiBean documentoEmiBean);
  String anularDocumento(DocumentoEmiBean documentoEmiBean);

    public String updRemitoResumenDestinatario(String vti_des, String vco_pri, String vnu_cant_des, String pnuAnn, String pnuEmi);

    public String updRemitoResumenReferencia(String vti_ori, String pnuAnn, String pnuEmi);

    public String getNumDestinos(String nu_ann, String nu_emi);

    public List<DestinoBean> getListaDestinosCodDepTipoDes(String nu_ann, String nu_emi);

    public List<ReferenciaRemitoBean> getListaReferenciaRemitos(String nu_ann, String nu_emi);

    public List<DestinoBean> getListaDestinosCodPri(String nu_ann, String nu_emi);
    String getNumeroDocSiguientePersonal(String pnuAnn, String pcoEmp, String pcoDoc);
    DocumentoEmiBean getEstadoDocumento(String pnuAnn,String pnuEmi);
    
    //jazanero
    String updClaveDocumentoEmi(String nuAnn,String nuEmi,String referenciaEmiDocBean);
    TblRemitosBean getDatosDocumentoPersonal(String pnuAnn,String pnuEmi);
}
