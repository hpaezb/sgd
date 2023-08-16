/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.DocumentoDatoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBlock;

/**
 *
 * @author WCutipa
 */
public interface DocumentoObjDao {
    
    DocumentoDatoBean getDatosDoc(String pnuAnn, String pnuEmi);
    DocumentoObjBean getNombreArchivo(String pnuAnn, String pnuEmi, String ptiCap);
    DocumentoObjBean getNombreArchivoAnexo(String pnuAnn, String pnuEmi, String pnuAnexo);
    DocumentoObjBean leerDocumento(String pnuAnn, String pnuEmi, String ptiCap);
    DocumentoObjBean leerDocumentoAnexo(String pnuAnn, String pnuEmi, String pnuAne);
    String getInFirmaDoc(String pcoDep,String pcoTipoDoc);
    String insRemitosBlock(DocumentoObjBlock docBlock);
    String updRemitosBlock(DocumentoObjBlock docBlock);
    DocumentoObjBlock getDatosDocBlock(String pnuAnn,String pnuEmi);
    DocumentoDatoBean CargarCabeceraReporte(String pnuAnn, String pnuEmi);
    List<DocumentoDatoBean> CargarSubReporte1(String pnuAnn, String pnuEmi);
    List<DocumentoDatoBean> CargarSubReporte2(String pnuAnn, String pnuEmi);
    //jazanero
    DocumentoObjBean leerDocumento(String pnuAnn, String pnuEmi, String ptiCap, String pAbreWord);
    List<DocumentoObjBean> leerDocumentoAnexo(String pnuAnn, String pnuEmi);
    
    DocumentoObjBean leerDocumentoAnexoMsj(String pnuAnn, String pnuEmi, String pnuAne);/*Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019*/
}
