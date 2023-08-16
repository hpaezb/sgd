/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.autentica.model.Usuario;
import pe.gob.onpe.tramitedoc.bean.AccionLog;
import pe.gob.onpe.tramitedoc.bean.BuscarAccionLog;
import pe.gob.onpe.tramitedoc.bean.SiElementoBean;

/**
 *
 * @author hpaez
 */
public interface ActionLogDao {
    String insActionLog(AccionLog accionLog,Usuario usuario) throws Exception;
    String getNextAction();
    AccionLog getIndicadorMenu(String coOpcion);
    SiElementoBean getConfigLog (String coOpcion, String coEstadoDoc) throws Exception;
    List<AccionLog> getLstDocumentosLog(BuscarAccionLog buscarAccionLog) throws Exception;
    List<AccionLog> getListaReporteBusqueda(BuscarAccionLog buscarAccionLog); 
    public int getCantidadDocEncontrados(String tipoDoc, String numeroDoc);
}
