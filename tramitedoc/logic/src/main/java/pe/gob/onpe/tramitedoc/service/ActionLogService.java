package pe.gob.onpe.tramitedoc.service;

import java.util.List;
import java.util.Map;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.tramitedoc.bean.AccionLog;
import pe.gob.onpe.tramitedoc.bean.BuscarAccionLog;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;

/**
 *
 * @author hpaez
 */
public interface ActionLogService {
    String insActionLog(AccionLog accionLog,Usuario usuario) throws Exception;
    AccionLog getIndicadorMenu(String coOpcion) throws Exception;
    SiElementoBean getConfigLog (String coOpcion, String coEstadoDoc) throws Exception;
    List<AccionLog> getLstDocumentosLog(BuscarAccionLog buscarAccionLog);
    public ReporteBean getGenerarReporte(BuscarAccionLog buscarAccionLog,Map parametros);
    public List<AccionLog> getListaReporte(BuscarAccionLog buscarAccionLog);
    public int getCantidadDocEncontrados(String tipoDoc, String numeroDoc);
}
