/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.DestinoResBen;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.RemitosResBean;

/**
 *
 * @author wcutipa
 */
public interface DocumentoBasicoDao {
    RemitosResBean getRemitoResumen(String pnuAnn, String pnuEmi);
    DestinoResBen  getDestinoResumen(String pnuAnn, String pnuEmi, String pnuDes);  
    List<DestinoResBen>  getDestinoResumenList(String pnuAnn, String pnuEmi);  
    List<DocumentoAnexoBean>  getAnexosList(String pnuAnn, String pnuEmi);  
    List<DocumentoAnexoBean>  getAnexosMsjList(String pnuAnn, String pnuEmi); 
    
    //jazanero
    RemitosResBean getRemitoProyecto(String pnuAnn, String pnuEmi);
    DocumentoEmiBean getNumeroAnexoProyecto(String pnuAnn, String pnuEmi);
    //jazanero
    RemitosResBean getRemitoResumenDevolucion(String pnuAnn, String pnuEmi, String pkNuSec);
    String getVerificaDocParaCerraPlazoAtencion(String pcoEmp, String pnuAnn, String pnuEmi) throws Exception;/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    String getVerificaTipoTramitePorDocumento(String pCoDepen, String pCoTipoDoc);/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
}
