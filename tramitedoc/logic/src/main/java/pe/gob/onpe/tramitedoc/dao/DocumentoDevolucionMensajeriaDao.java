package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.BuscarDocumentoDevolucionMensajeriaBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoDevolucionMensajeriaBean;

/**
 *
 * @author hpaez
 */
public interface DocumentoDevolucionMensajeriaDao {
   List<DocumentoDevolucionMensajeriaBean> getDocumentoDevolucionMensajeria(BuscarDocumentoDevolucionMensajeriaBean buscarDocumentoDevolucionMensajeriaBean); 
}
