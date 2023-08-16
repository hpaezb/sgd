package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoPendienteConsulBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;

/**
 *
 * @author hpaez
 */
public interface ConsultaPendienteDocDao {
    List<DocumentoBean> getDocumentosBuscaPendientes(BuscarDocumentoPendienteConsulBean buscarDocumentoPendienteConsulBean); 
}
