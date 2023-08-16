package pe.gob.onpe.tramitedoc.dao;

import java.util.List;
import pe.gob.onpe.libreria.util.Mensaje;
import pe.gob.onpe.tramitedoc.bean.UsuarioConfigBean;
import pe.gob.onpe.tramitedoc.bean.UsuarioDepAcceso;
/*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
import pe.gob.onpe.tramitedoc.bean.UsuarioDependenciaAcceso;
/*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
public interface UsuarioConfigDao {
    public UsuarioConfigBean getConfig(Mensaje msg, String cempCodemp, String coDep);
    public String updUsuarioConfing(UsuarioConfigBean usuarioConf);
    public String insUsuarioConfingBasico(UsuarioConfigBean usuarioConf);
    public List<UsuarioDepAcceso> getListDepAccesos(String cempCodemp, String coUsuario);
    public String getCoDepUsuario(String pcoEmp);
    public String getTiEncargadoDep(String pcoEmp, String pcoDep);
    public List<UsuarioDepAcceso> getListDepTotal(String cempCodemp);
    /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
    public List<UsuarioDependenciaAcceso> getListDependenciaAccesos(String cempCodemp, String coUsuario);
    /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/
}
