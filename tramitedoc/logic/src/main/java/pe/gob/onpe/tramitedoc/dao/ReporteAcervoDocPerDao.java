package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiPersConsulBean;
import pe.gob.onpe.tramitedoc.bean.EmpleadoBean;

public interface ReporteAcervoDocPerDao {
    List<DocumentoEmiPersConsulBean> getDocsPersAcervoDocumentario(DocumentoEmiPersConsulBean buscarDocPer);
    List<EmpleadoBean> getPersonalEditAcervoDoc(String pcoDepEmi);
    List<DocumentoEmiPersConsulBean> getListaReporteAcervoDocPers(DocumentoEmiPersConsulBean buscarDocPer);
}
