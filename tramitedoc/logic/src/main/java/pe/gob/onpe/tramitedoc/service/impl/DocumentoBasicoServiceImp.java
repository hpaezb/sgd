/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.onpe.tramitedoc.bean.DestinoResBen;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.ReferenciaBean;
import pe.gob.onpe.tramitedoc.bean.RemitosResBean;
import pe.gob.onpe.tramitedoc.dao.DocumentoBasicoDao;
import pe.gob.onpe.tramitedoc.dao.EmiDocumentoAdmDao;
import pe.gob.onpe.tramitedoc.service.DocumentoBasicoService;

/**
 *
 * @author wcutipa
 */

@Service("documentoBasicoService")
public class DocumentoBasicoServiceImp implements DocumentoBasicoService{

    @Autowired
    private DocumentoBasicoDao documentoBasicoDao;
    /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente--*/
    //@Autowired
    //private EmiDocumentoAdmDao emiDocumentoAdmDao;
    /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente--*/
    @Override
    public RemitosResBean getRemitoResumen(String pnuAnn, String pnuEmi){
        RemitosResBean docRemitoBean = null;
        docRemitoBean = documentoBasicoDao.getRemitoResumen(pnuAnn, pnuEmi);
        return (docRemitoBean);
    }
    
    @Override
    public DestinoResBen  getDestinoDocumento(String pnuAnn, String pnuEmi, String pnuDes){
        DestinoResBen docDestinoBean = null;
        docDestinoBean = documentoBasicoDao.getDestinoResumen(pnuAnn, pnuEmi, pnuDes);
        return (docDestinoBean);
    }

    @Override
    public DestinoResBen  getDestinoResumen(String pnuAnn, String pnuEmi, String pnuDes){
        DestinoResBen docDestinoBean = null;

       if (pnuDes!= null && !pnuDes.equals("N")){
            docDestinoBean = documentoBasicoDao.getDestinoResumen(pnuAnn, pnuEmi, pnuDes);
       }else{
            List<DestinoResBen> list = null;
            list = documentoBasicoDao.getDestinoResumenList(pnuAnn, pnuEmi);
            docDestinoBean = new DestinoResBen();
            for(int i=0; i<list.size(); i++) {
                docDestinoBean = list.get(i);
            }            
       }
        return (docDestinoBean);
    }
    
    
    @Override
    public List<DestinoResBen>  getDestinoResumenList(String pnuAnn, String pnuEmi){
        List<DestinoResBen> list = null;
        list = documentoBasicoDao.getDestinoResumenList(pnuAnn, pnuEmi);
        return (list);
        
    }
    
    @Override
    public List<DocumentoAnexoBean>  getAnexosList(String pnuAnn, String pnuEmi){
        List<DocumentoAnexoBean> list = null;
        list = documentoBasicoDao.getAnexosList(pnuAnn, pnuEmi);
        return (list);
    }

    @Override
    public List<DocumentoAnexoBean> getAnexosMsjList(String pnuAnn, String pnuEmi) {
        List<DocumentoAnexoBean> list = null;
        list = documentoBasicoDao.getAnexosMsjList(pnuAnn, pnuEmi);
        return (list);
    }

    @Override
    public RemitosResBean getRemitoProyecto(String pnuAnn, String pnuEmi) {
        RemitosResBean docRemitoBean = null;
        docRemitoBean = documentoBasicoDao.getRemitoProyecto(pnuAnn, pnuEmi);
        return (docRemitoBean);
    }

    @Override
    public DocumentoEmiBean getNumeroAnexoProyecto(String pnuAnn, String pnuEmi) {
        DocumentoEmiBean documentoEmiBean = null;
        documentoEmiBean = documentoBasicoDao.getNumeroAnexoProyecto(pnuAnn, pnuEmi);
        return (documentoEmiBean);
    }

    @Override
    public RemitosResBean getRemitoResumenDevolucion(String pnuAnn, String pnuEmi, String pkNuSec) {
        RemitosResBean docRemitoBean = null;
        docRemitoBean = documentoBasicoDao.getRemitoResumenDevolucion(pnuAnn, pnuEmi, pkNuSec);
        return (docRemitoBean);        
    }
    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    @Override
    public String getVerificaDocParaCerraPlazoAtencion(String pcoEmp, String pnuAnn, String pnuEmi) {
        String retval = null;
            try {
                retval = documentoBasicoDao.getVerificaDocParaCerraPlazoAtencion(pcoEmp, pnuAnn, pnuEmi);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        return retval;
    }

    @Override
    public String getVerificaTipoTramitePorDocumento(String pCoDepen, String pCoTipoDoc) {
        String vResult="NO_OK";
        try {
            vResult=documentoBasicoDao.getVerificaTipoTramitePorDocumento(pCoDepen, pCoTipoDoc);
            if(vResult.equals("1")){
                vResult="OK";
            }else{
                vResult="NO_OK";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vResult;
    }
    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    /*-- [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente--*/
//    @Override
//    public DocumentoEmiBean getExtensionExpediente(String pnuAnn, String pnuEmi, String pnuDes) {
//        DocumentoEmiBean documentoEmiBean = null;
//        try {
//            documentoEmiBean = emiDocumentoAdmDao.getExtensionExpediente(pnuAnn, pnuEmi, pnuDes);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return documentoEmiBean;
//    }
//
//    @Override
//    public List<ReferenciaBean> getDocSegParaExtExp(String pnuAnn, String pnuEmi, String pNuAnnRef, String pnuEmiRef) {
//        List<ReferenciaBean> list = null;
//        try {
//            list = emiDocumentoAdmDao.getDocSegParaExtExp(pnuAnn, pnuEmi, pNuAnnRef, pnuEmiRef);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return list;
//    }
    /*-- [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente--*/
}
