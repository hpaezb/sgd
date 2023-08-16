package pe.gob.onpe.tramitedoc.service;

import java.util.List;
import java.util.Map;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoPendienteConsulBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;

/**
 *
 * @author hpaez
 */
public interface ConsultaPendienteDocService {
    List<DocumentoBean> getDocumentosBuscaPendientes(BuscarDocumentoPendienteConsulBean buscarDocumentoPendienteConsulBean); 
    ReporteBean getGenerarReporte(BuscarDocumentoPendienteConsulBean buscarDocumentoPendienteConsulBean,Map parametros);
}
