/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.postgresql;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.DestinoResBen;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.RemitosResBean;
import pe.gob.onpe.tramitedoc.dao.DocumentoBasicoDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;

/**
 *
 * @author wcutipa
 */
@Repository("documentoBasicoDao")
public class DocumentoBasicoDaoImp extends SimpleJdbcDaoBase implements DocumentoBasicoDao{
    @Override
    public RemitosResBean getRemitoResumen(String pnuAnn, String pnuEmi){
        StringBuilder sql = new StringBuilder();
//        sql.append("select \n" +
//                    "nu_ann, \n" +
//                    "nu_emi, \n" +
//                    "ti_emi,\n" +
//                    "de_tip_emi de_ti_emi,\n" +
//                    "ti_cap, \n" +
//                    "de_dep_emi,\n" +
//                    "de_emp_emi, \n" +
//                    "de_emp_res, \n" +
//                    "de_ori_emi,\n" +
//                    "to_char(fe_emi,'dd/mm/yyyy') fe_emi,\n" +
//                    "de_tip_doc_adm de_ti_doc,\n" +
//                    "nu_doc,\n" +
//                    "de_asu,\n" +
//                    "de_es_doc_emi\n" +
//                    "from IDOSGD.tdvv_remitos_adm " +
//                    "where nu_ann = ?\n" +
//                    "and nu_emi = ?");
        
        sql.append("SELECT \n" +
                    "a.nu_ann, \n" +
                    "a.nu_emi,\n" +
                    "a.ti_emi, \n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_ti_destino (a.ti_emi) de_ti_emi,\n" +
                    "a.ti_cap,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_de_dependencia (a.co_dep_emi) de_dep_emi,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_de_nom_emp (a.co_emp_emi) de_emp_emi,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_de_nom_emp (a.co_emp_res) de_emp_res,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_ti_emi_emp (a.nu_ann, a.nu_emi) de_ori_emi,\n" +
                    "to_char(a.fe_emi,'dd/mm/yyyy') fe_emi,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_de_documento (a.co_tip_doc_adm) de_ti_doc,\n" +
                    "CASE WHEN a.ti_emi='01' THEN     a.nu_doc_emi || '-' || a.nu_ann || '/' || a.de_doc_sig\n" +
                    "                    WHEN a.ti_emi='05' THEN a.nu_doc_emi || '-' || a.nu_ann || '/' || a.de_doc_sig\n" +
                    "                    ELSE a.de_doc_sig\n" +
                    "                    END  AS nu_doc, \n" +
                    "a.de_asu,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_estados (a.es_doc_emi,\n" +
                    "                                     'TDTV_REMITOS'\n" +
                    "                                    ) de_es_doc_emi\n" +
                    "\n" +
                    "from IDOSGD.tdtv_remitos a \n" +
                    "where nu_ann = ?\n" +
                    "and nu_emi = ?");
   
        RemitosResBean remitoDocBean = new RemitosResBean();

        try {
            remitoDocBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(RemitosResBean.class),new Object[]{ pnuAnn,pnuEmi} );
        } catch (EmptyResultDataAccessException e) {
            remitoDocBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(remitoDocBean);
    
    
    }
    
    @Override
    public DestinoResBen  getDestinoResumen(String pnuAnn, String pnuEmi, String pnuDes){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n" +
                    "b.de_ane, \n" +
                    "b.nu_ann, \n" +
                    "b.nu_emi, \n" +
                    "b.nu_des,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_ti_destino(b.ti_des) de_tip_des,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_ti_des_ori (b.nu_ann, b.nu_emi, b.nu_des) de_ori_des,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(co_emp_rec) de_emp_rec,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_de_prioridad(b.co_pri) de_pri,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_motivo (b.co_mot) de_mot,\n" +
                    "b.de_pro de_indica,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_estados (b.es_doc_rec,'TDTV_DESTINOS') de_es_doc_des,\n" +
                    "to_char(b.fe_rec_doc,'dd/mm/yyyy HH24:MI') fe_rec_doc, \n" +
                    "to_char(b.fe_arc_doc,'dd/mm/yyyy') fe_arc_doc, \n" +
                    "to_char(b.fe_ate_doc,'dd/mm/yyyy') fe_ate_doc, \n" +
                    "to_char(b.fe_der_doc,'dd/mm/yyyy') fe_der_doc\n" +
                    "FROM  IDOSGD.tdtv_destinos b\n" +
                    "where nu_ann = ?\n" +
                    "and nu_emi = ?\n" +
//                    "and nu_des = CAST(? AS BIGINT)");        
                    "and nu_des = REGEXP_REPLACE(COALESCE(?, '0'), '[^0-9]*' ,'0')::integer");
        
   
        DestinoResBen destinoDocBean = new DestinoResBen();

        try {
            destinoDocBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DestinoResBen.class),new Object[]{ pnuAnn,pnuEmi,pnuDes} );
        } catch (EmptyResultDataAccessException e) {
            destinoDocBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(destinoDocBean);
    }
    
    @Override
    public List<DestinoResBen>  getDestinoResumenList(String pnuAnn, String pnuEmi){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n" +
                    "b.nu_ann, \n" +
                    "b.nu_emi, \n" +
                    "b.nu_des,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_ti_destino(b.ti_des) de_tip_des,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_ti_des_ori (b.nu_ann, b.nu_emi, b.nu_des) de_ori_des,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_DE_NOM_EMP(co_emp_rec) de_emp_rec,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_de_prioridad(b.co_pri) de_pri,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_motivo (b.co_mot) de_mot,\n" +
                    "b.de_pro de_indica,\n" +
                    "IDOSGD.PK_SGD_DESCRIPCION_estados (b.es_doc_rec,'TDTV_DESTINOS') de_es_doc_des,\n" +
                    "to_char(b.fe_rec_doc,'dd/mm/yyyy HH24:MI') fe_rec_doc, \n" +
                    "to_char(b.fe_arc_doc,'dd/mm/yyyy') fe_arc_doc, \n" +
                    "to_char(b.fe_ate_doc,'dd/mm/yyyy') fe_ate_doc, \n" +
                    "to_char(b.fe_der_doc,'dd/mm/yyyy') fe_der_doc\n" +
                    "FROM  IDOSGD.tdtv_destinos b\n" +
                    "where nu_ann = ?\n" +
                    "and nu_emi = ? ");
   
        
        List<DestinoResBen> list = new ArrayList<DestinoResBen>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DestinoResBen.class), new Object[]{pnuAnn,pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(list);
    
    }

    @Override
    public List<DocumentoAnexoBean>  getAnexosList(String pnuAnn, String pnuEmi){
        StringBuilder sql = new StringBuilder();
        sql.append("select  \n" +
                    "nu_ann,\n" +
                    "nu_emi,\n" +
                    "nu_ane,\n" +
                    "de_det,\n" +
                    "de_rut_ori,\n" +
                    "co_use_cre,\n" +
                    "fe_use_cre,\n" +
                    "co_use_mod,\n" +
                    "fe_use_mod,\n" +
                    "COALESCE(req_firma,'0') req_firma\n" +
                    "from IDOSGD.tdtv_anexos \n" +
                    "where nu_ann = ? \n" +
                    "and nu_emi = ? \n"+
                    "order by nu_ane ");
   
        
        List<DocumentoAnexoBean> list = new ArrayList<DocumentoAnexoBean>();

        try {
            list = this.jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoAnexoBean.class), new Object[]{pnuAnn,pnuEmi});
        } catch (EmptyResultDataAccessException e) {
            list = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(list);
    
    }

    @Override
    public List<DocumentoAnexoBean> getAnexosMsjList(String pnuAnn, String pnuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RemitosResBean getRemitoProyecto(String pnuAnn, String pnuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DocumentoEmiBean getNumeroAnexoProyecto(String pnuAnn, String pnuEmi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RemitosResBean getRemitoResumenDevolucion(String pnuAnn, String pnuEmi, String pkNuSec) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getVerificaDocParaCerraPlazoAtencion(String pcoEmp, String pnuAnn, String pnuEmi) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getVerificaTipoTramitePorDocumento(String pCoDepen, String pCoTipoDoc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
