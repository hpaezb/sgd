package pe.gob.onpe.tramitedoc.service;

import java.util.List;
import java.util.Map;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoDevolucionMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoDevolucionMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;

/**
 *
 * @author hpaez
 */
public interface DocumentoDevolucionMensajeriaService {
    List<DocumentoDevolucionMensajeriaBean> getDocumentoDevolucionMensajeria(BuscarDocumentoDevolucionMensajeriaBean buscarDocumentoDevolucionMensajeriaBean);
    ReporteBean getGenerarReporte(BuscarDocumentoDevolucionMensajeriaBean buscarDocumentoDevolucionMensajeriaBean,Map parametros);
}
