package pe.gob.onpe.tramitedoc.service;

import java.util.List;
import java.util.Map;
import pe.gob.onpe.tramitedoc.bean.CargoEntregaBean;
import pe.gob.onpe.tramitedoc.bean.DocPedienteEntregaBean;
import pe.gob.onpe.tramitedoc.bean.GuiaMesaPartesBean;
import pe.gob.onpe.tramitedoc.bean.ReporteBean;
import pe.gob.onpe.tramitedoc.bean.TrxGeneraGuiaMpBean;

/**
 *
 * @author consultor_ogti_80
 */
public interface CargoEntregaInternoService {
    List<CargoEntregaBean> getCargosEntregaInterno(CargoEntregaBean cargo);
    List<DocPedienteEntregaBean> getDocsPendienteEntregaInterno(DocPedienteEntregaBean busqDoc);
    List<DocPedienteEntregaBean> getDocsPendienteEntregaInterno(List<DocPedienteEntregaBean> docs);
    String grabarCargoEntregaInterno(TrxGeneraGuiaMpBean trxGuia)throws Exception;
    String getJsonRptGrabarCargoEntregaInterno(TrxGeneraGuiaMpBean trxGuia);
    GuiaMesaPartesBean getGuiaMpInterno(String pnuAnnGuia, String pnuGuia);
    List<DocPedienteEntregaBean> getDetalleGuiaMpInterno(String pnuAnnGuia,String pnuGuia);
    String anularGuiaMpInterno(GuiaMesaPartesBean guia);
    String getRutaGuiaInterno(String pnuAnnGuia, String pnuGuia, String pcoUser);
    GuiaMesaPartesBean getGuiaMpInterno(List<DocPedienteEntregaBean> docs);
    public List<CargoEntregaBean> getListaReporteInterno(CargoEntregaBean cargo);
    public ReporteBean getGenerarReporteInterno(Map parametros, CargoEntregaBean cargo);    
}
