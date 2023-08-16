/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.CitizenBean;

/**
 *
 * @author RBerrocal
 */
public interface CiudadanoDao {
    public List<CitizenBean> getCiudadanoList();
    public List<CitizenBean> getCiudadanoBusqList(String busCiudDocumento, 
                                                  String busCiudApPaterno, 
                                                  String busCiudApMaterno, 
                                                  String busCiudNombres );
    public String insCiudadano(CitizenBean ciudadano, String codUsuario);
    public CitizenBean getCiudadano(String codCiudadano);
    public String updCiudadano(CitizenBean ciudadano, String codUsuario);
}
