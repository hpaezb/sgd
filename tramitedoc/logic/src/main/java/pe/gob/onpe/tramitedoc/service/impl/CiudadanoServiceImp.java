/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.onpe.tramitedoc.bean.CitizenBean;
import pe.gob.onpe.tramitedoc.dao.CiudadanoDao;
import pe.gob.onpe.tramitedoc.service.CiudadanoService;

/**
 *
 * @author RBerrocal
 */
@Service("ciudadanoService")
public class CiudadanoServiceImp implements CiudadanoService{
    
    @Autowired
    private CiudadanoDao ciudadanoDao; 

    public List<CitizenBean> getCiudadanoList(){
        List<CitizenBean> list = null;
        try {
            list = ciudadanoDao.getCiudadanoList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<CitizenBean> getCiudadanoBusqList(String busCiudDocumento, String busCiudApPaterno, String busCiudApMaterno, String busCiudNombres) {
        List<CitizenBean> list = null;
        try {
            list = ciudadanoDao.getCiudadanoBusqList(busCiudDocumento, busCiudApPaterno, busCiudApMaterno, busCiudNombres);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    public String insCiudadano(CitizenBean ciudadano, String codUsuario) {
        String vReturn = "NO_OK";
        try {
            //Se hace el cambio para que se guarde los datos relevantes en mayuscula
            ciudadano.setDeApp(ciudadano.getDeApp().toUpperCase().trim());
            ciudadano.setDeApm(ciudadano.getDeApm().toUpperCase().trim());
            ciudadano.setDeNom(ciudadano.getDeNom().toUpperCase().trim());
            vReturn = ciudadanoDao.insCiudadano(ciudadano, codUsuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }

    public CitizenBean getCiudadano(String codCiudadano) {
        CitizenBean ciudadano = null;
        try {
            
            ciudadano = ciudadanoDao.getCiudadano(codCiudadano);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ciudadano;
    }

    public String updCiudadano(CitizenBean ciudadano, String codUsuario) {
        String vReturn = "NO_OK";
        try {
            //Se hace el cambio para que se guarde los datos relevantes en mayuscula
            ciudadano.setDeApp(ciudadano.getDeApp().toUpperCase().trim());
            ciudadano.setDeApm(ciudadano.getDeApm().toUpperCase().trim());
            ciudadano.setDeNom(ciudadano.getDeNom().toUpperCase().trim());
            
            vReturn = ciudadanoDao.updCiudadano(ciudadano, codUsuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vReturn;
    }
}
