/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramitedoc.service;

import java.util.List;
import pe.gob.onpe.tramitedoc.bean.OtroOrigenBean;
import pe.gob.onpe.tramitedoc.bean.ProveedorBean;
import pe.gob.onpe.tramitedoc.bean.UbigeoBean;


/**
 *
 * @author NGilt
 */
public interface ProveedorService {

    public List<ProveedorBean> getProveedoresList();

    public List<ProveedorBean> getProveedoresBusqList(String busNroRuc,String busRazonSocial);

    public List<UbigeoBean> getUbigeoBusqList(UbigeoBean ubigeo);

    public String insProveedor(ProveedorBean proveedor);

    public ProveedorBean getProveedor(String codProveedor);
    
    public String updProveedor(ProveedorBean proveedor, String codProveedor);    
    
}
