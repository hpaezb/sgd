package pe.gob.onpe.tramitedoc.service;

import java.util.List;
import java.util.Map;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiPersConsulBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;

/**
 *
 * @author oti18
 */
public interface ReporteAcervoDocPerService {
    List<DocumentoEmiPersConsulBean> getListaReporteAcervo(DocumentoEmiPersConsulBean buscarDocPer);
    ReporteBean getGenerarReporteAcervoDocPer(DocumentoEmiPersConsulBean buscarDocPer,Map parametros);
    List<DocumentoEmiPersConsulBean> getDocsPersAcervoDocumentario(DocumentoEmiPersConsulBean buscarDocPer);
    List<EmpleadoBean> getPersonalEditAcervoDoc(String pcoDepEmi);
}
