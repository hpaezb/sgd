/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.CargoEntregaBean;
import pe.gob.onpe.tramitedoc.bean.DetGuiaMesaPartesBean;
import pe.gob.onpe.tramitedoc.bean.DocPedienteEntregaBean;
import pe.gob.onpe.tramitedoc.bean.GuiaMesaPartesBean;

/**
 *
 * @author consultor_ogti_80
 */
public interface CargoEntregaInternoDao {
    List<CargoEntregaBean> getCargosEntregaInterno(CargoEntregaBean cargo);
    List<DocPedienteEntregaBean> getDocsPendienteEntregaInterno(DocPedienteEntregaBean busqDoc);
    String isDocPendienteEnGuiaMpInterno(String pnuAnn,String pnuEmi,String pnuDes);
    DocPedienteEntregaBean getDocPendienteEntregaInterno(String pnuAnn,String pnuEmi,String pnuDes);
    String insGuiaMpInterno(GuiaMesaPartesBean guia);
    String insDetGuiaMpInterno(DetGuiaMesaPartesBean detGuia);
    String getNroGuiaCabeceraInterno(String pnuAnnGuia);
    String getNroCorrelativoGuiaCabeceraInterno(String pnuAnnGuia,String pcoDepOri);
    String getNroCorrelativoDetGuiaCabeceraInterno(String pnuAnnGuia, String pnuGuia);
    String updGuiaMpInterno(GuiaMesaPartesBean guia);
    GuiaMesaPartesBean getGuiaMpInterno(String pnuAnnGuia, String pnuGuia);
    List<DocPedienteEntregaBean> getDetalleGuiaMpInterno(String pnuAnnGuia,String pnuGuia);
    String updEstadoGuiaMpInterno(String estado, String pcoUseMod, String pnuAnnGuia, String pnuGuia);
    DocPedienteEntregaBean getDependenciaDestinoDocExtRecInterno(String pnuAnn, String pnuEmi, String pnuDes);
    List<CargoEntregaBean> getListaReporteBusquedaInterno(CargoEntregaBean cargo);    
}
