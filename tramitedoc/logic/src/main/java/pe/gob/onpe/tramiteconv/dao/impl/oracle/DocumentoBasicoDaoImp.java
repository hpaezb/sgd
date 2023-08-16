/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.tramiteconv.dao.impl.oracle;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.DestinoResBen;
import pe.gob.onpe.tramitedoc.bean.DocumentoAnexoBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoBean;
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
    
    private static Logger logger=Logger.getLogger(DocumentoBasicoDaoImp.class);
    
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
//                    "from tdvv_remitos_adm " +
//                    "where nu_ann = ?\n" +
//                    "and nu_emi = ?");
        sql.append("SELECT \n"
                + "a.nu_ann, \n"
                + "a.nu_emi, \n"
                + "a.nu_cor_emi, \n"
                + "a.co_loc_emi,\n"
                + "PK_SGD_DESCRIPCION.de_local (a.co_loc_emi) de_loc_emi, \n"
                + "a.co_dep_emi,\n"
                + "PK_SGD_DESCRIPCION.de_dependencia (a.co_dep_emi) de_dep_emi,\n"
                + "a.ti_emi, \n"
                + "PK_SGD_DESCRIPCION.ti_destino (a.ti_emi) de_tip_emi,\n"
                + "a.co_emp_emi, \n"
                + "PK_SGD_DESCRIPCION.de_nom_emp (co_emp_emi) de_emp_emi,\n"
                + "a.co_emp_res, \n"
                + "PK_SGD_DESCRIPCION.de_nom_emp (a.co_emp_res) de_emp_res,\n"
                + "a.nu_dni_emi, \n"
                + "a.nu_ruc_emi, \n"
                + "a.co_otr_ori_emi,\n"
                + "PK_SGD_DESCRIPCION.ti_emi_emp (a.nu_ann, a.nu_emi) de_ori_emi,\n"
                + "DECODE\n"
                + "  (a.ti_emi,\n"
                + "  '01', PK_SGD_DESCRIPCION.de_dependencia (a.co_dep_emi)\n"
                + "  || ' - '\n"
                + "  || PK_SGD_DESCRIPCION.de_nom_emp (a.co_emp_emi),\n"
                + "  '02', PK_SGD_DESCRIPCION.de_proveedor (a.nu_ruc_emi),\n"
                + "  '03', PK_SGD_DESCRIPCION.ani_simil (a.nu_dni_emi),\n"
                + "  '04', PK_SGD_DESCRIPCION.otro_origen (a.co_otr_ori_emi),\n"
                + "  '05', PK_SGD_DESCRIPCION.de_nom_emp (a.co_emp_emi)\n"
                + "  ) de_ori_emi_mp,\n"
                + " NVL2(A.fe_emi, TO_CHAR(A.fe_emi, 'DD/MM/YYYY  hh24:mi:ss'), ' ') fe_emi, \n"
                + "TO_CHAR (a.fe_emi, 'MM') de_mes, \n"
                + "a.co_tip_doc_adm,\n"
                + "PK_SGD_DESCRIPCION.de_documento (a.co_tip_doc_adm) de_ti_doc,\n"
                + "DECODE (a.ti_emi,\n"
                + "  '01', a.nu_doc_emi || '-' || a.nu_ann || '/' || a.de_doc_sig,\n"
                + "  '05', a.nu_doc_emi || '-' || a.nu_ann || '/' || a.de_doc_sig,\n"
                + "  a.de_doc_sig\n"
                + "  ) nu_doc,\n"
                + "a.co_gru, a.es_doc_emi,\n"
                + "PK_SGD_DESCRIPCION.estados (a.es_doc_emi,'TDTV_REMITOS') de_es_doc_emi,\n"
                + "PK_SGD_DESCRIPCION.estados_mp (a.es_doc_emi,'TDTV_REMITOS') de_es_doc_emi_mp,\n"
                + "a.nu_dia_ate, \n"
                + "a.de_asu, \n"
                + "UPPER (a.de_asu) de_asu_m, \n"
                + "a.de_ane,\n"
                + "a.co_tip_doc_pro,\n"
                + "PK_SGD_DESCRIPCION.de_documento (a.co_tip_doc_pro) de_tip_doc_pro,\n"
                + "DECODE (a.nu_candes,\n"
                + "  1, PK_SGD_DESCRIPCION.ti_des (a.nu_ann, a.nu_emi),\n"
                + "  PK_SGD_DESCRIPCION.ti_des_v (a.nu_ann, a.nu_emi)\n"
                + "  ) ti_des,\n"
                + "DECODE (a.nu_candes,\n"
                + "  1, PK_SGD_DESCRIPCION.ti_co_dep_des (a.nu_ann, a.nu_emi),\n"
                + "  PK_SGD_DESCRIPCION.ti_co_dep_des_v (a.nu_ann, a.nu_emi)\n"
                + "  ) co_dep_des_t,\n"
                + "DECODE (a.nu_candes,\n"
                + "  1, PK_SGD_DESCRIPCION.ti_des_emp (a.nu_ann, a.nu_emi),\n"
                + "  PK_SGD_DESCRIPCION.ti_des_emp_v (a.nu_ann, a.nu_emi)\n"
                + "  ) de_emp_pro,\n"
                + "a.ti_cap,\n"
                + "a.co_use_cre,\n"
                + "a.fe_use_cre, \n"
                + "a.co_use_mod, \n"
                + "a.fe_use_mod, \n"
                + "a.nu_ann_exp,\n"
                + "a.nu_sec_exp, \n"
                + "a.nu_det_exp,\n"
                + "PK_SGD_DESCRIPCION.DE_NU_EXPEDIENTE(a.nu_ann_exp,a.nu_sec_exp) NU_EXPEDIENTE,\n"
                /*-- [HPB] Inicio 27/02/23 CLS-087-2022 --*/
                + "PK_SGD_DESCRIPCION.FE_EXPEDIENTE(a.nu_ann_exp,a.nu_sec_exp) FE_EXP,\n"
                /*-- [HPB] Fin 27/02/23 CLS-087-2022 --*/
                + "DECODE (a.nu_sec_exp, NULL, NULL, 'E') in_expe, \n"
                + "nu_folios, \n"
                /*--21/08/19 HPB Devolucion Doc a Oficina--*/
                + "a.doc_estado_msj,"
                + "PK_SGD_DESCRIPCION.estados(a.doc_estado_msj, 'ENVIO_DOCUMENTO_MSJ') de_doc_estado_msj,"
                + "NVL2(A.fe_emi, TO_CHAR(a.fe_dev_ofi, 'DD/MM/YYYY  HH24:MI'), ' ') fe_dev_ofi,"
                + "a.obs_dev,"
                + "a.obs_mpv,"/*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
                /*-- [HPB] Inicio 24/02/23 CLS-087-2022 --*/
                + "a.cdir_remite de_direccion, a.cexp_correoe de_correo, a.ctelefono telefono,"
                /*-- [HPB] Fin 24/02/23 CLS-087-2022 --*/
                + "(SELECT COD_USER FROM SEG_USUARIOS1 WHERE CEMP_CODEMP=a.co_emp_dev) co_emp_dev,"
//                /*--21/08/19 HPB Devolucion Doc a Oficina--*/
//                + "SE_MESA_PARTES\n"
//                + " FROM tdtv_remitos a, tdtv_estacion_doc b\n"
//                + " WHERE a.nu_ann=b.nu_ann and a.nu_emi= b.nu_emi and a.es_eli = '0' AND a.nu_ann=? AND a.nu_emi=?");
                + "SE_MESA_PARTES\n"
                + "FROM tdtv_remitos a\n"
                + "WHERE a.es_eli = '0' AND a.nu_ann=? AND a.nu_emi=?");
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
                    "PK_SGD_DESCRIPCION.ti_destino(b.ti_des) de_tip_des,\n" +
                    "PK_SGD_DESCRIPCION.ti_des_ori (b.nu_ann, b.nu_emi, b.nu_des) de_ori_des,\n" +
                    "PK_SGD_DESCRIPCION.DE_NOM_EMP(co_emp_rec) de_emp_rec,\n" +
                    "PK_SGD_DESCRIPCION.de_prioridad(b.co_pri) de_pri,\n" +
                    "PK_SGD_DESCRIPCION.motivo (b.co_mot) de_mot,\n" +
                    "b.de_pro de_indica,\n" +
                    "b.co_emp_des,\n" +/*[HPB-02/10/20] Inicio - Plazo de Atencion-*/
                    "b.es_doc_rec,\n" +/*[HPB-02/10/20] Inicio - Plazo de Atencion-*/
                    /*-- [HPB] Inicio 27/03/23 CLS-087-2022 --*/
                    "PK_SGD_DESCRIPCION.FE_EXP_OBS(b.nu_ann, b.nu_emi) fe_env_cor_obs,\n" +
                    /*-- [HPB] Fin 27/03/23 CLS-087-2022 --*/
                    "PK_SGD_DESCRIPCION.estados (b.es_doc_rec,'TDTV_DESTINOS') de_es_doc_des,\n" +
                    "to_char(b.fe_rec_doc,'dd/mm/yyyy HH24:MI') fe_rec_doc, \n" +
                    "to_char(b.fe_arc_doc,'dd/mm/yyyy') fe_arc_doc, \n" +
                    "to_char(b.fe_ate_doc,'dd/mm/yyyy') fe_ate_doc, \n" +
                    "to_char(b.fe_der_doc,'dd/mm/yyyy') fe_der_doc, \n" +
                    "de_avance \n" +
                    "FROM  tdtv_destinos b\n" +
                    "where nu_ann = ?\n" +
                    "and nu_emi = ?\n" +
                    "and nu_des = ?");
        
   
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
                    "PK_SGD_DESCRIPCION.ti_destino(b.ti_des) de_tip_des,\n" +
                    "PK_SGD_DESCRIPCION.ti_des_ori (b.nu_ann, b.nu_emi, b.nu_des) de_ori_des,\n" +
                    "PK_SGD_DESCRIPCION.DE_NOM_EMP(co_emp_rec) de_emp_rec,\n" +
                    "PK_SGD_DESCRIPCION.de_prioridad(b.co_pri) de_pri,\n" +
                    "PK_SGD_DESCRIPCION.motivo (b.co_mot) de_mot,\n" +
                    "b.de_pro de_indica,\n" +
                    "b.co_emp_des,\n" +/*[HPB-02/10/20] Inicio - Plazo de Atencion-*/
                    "b.es_doc_rec,\n" +/*[HPB-02/10/20] Inicio - Plazo de Atencion-*/
                    "PK_SGD_DESCRIPCION.estados (b.es_doc_rec,'TDTV_DESTINOS') de_es_doc_des,\n" +
                    /*-- [HPB] Inicio 27/03/23 CLS-087-2022 --*/
                    "PK_SGD_DESCRIPCION.FE_EXP_OBS(b.nu_ann, b.nu_emi) fe_env_cor_obs,\n" +
                    /*-- [HPB] Fin 27/03/23 CLS-087-2022 --*/
                    "to_char(b.fe_rec_doc,'dd/mm/yyyy HH24:MI') fe_rec_doc, \n" +
                    "to_char(b.fe_arc_doc,'dd/mm/yyyy') fe_arc_doc, \n" +
                    "to_char(b.fe_ate_doc,'dd/mm/yyyy') fe_ate_doc, \n" +
                    "to_char(b.fe_der_doc,'dd/mm/yyyy') fe_der_doc, \n" +
                    "de_avance \n" +
                    "FROM  tdtv_destinos b\n" +
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
                    "nvl(req_firma,'0') req_firma, \n" +
                    "nvl(es_proyecto,'0') es_proyecto, \n" +
                    //"(case when (select instr(UPPER(de_det), UPPER('.pdf')) from dual) >0 then 'SI' else 'NO' end) ext \n" +
                    "nvl(ti_public,'0') es_publico \n" +
                    ",nvl(in_ane_sub,'0') in_ane_sub \n" +/*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR*/
                    "from tdtv_anexos \n" +
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
        
        StringBuilder sql = new StringBuilder();
        sql.append("select \n" +
                    "A.nu_ann,\n" +
                    "A.nu_emi,\n" +
                    "A.nu_des,\n" +
                    "B.nu_ane,\n" +
                    "NVL(B.de_det,'-1') de_det,\n" +
                    "NVL(B.de_rut_ori,'') de_rut_ori,\n" +
                    "B.co_use_cre,\n" +
                    "B.fe_use_cre,\n" +
                    "B.co_use_mod,\n" +
                    "B.fe_use_mod,\n" +
                    "nvl(B.req_firma,'0') req_firma,\n" +
                    "DECODE(A.TI_DES,\n" +
                    "'01', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES),\n" +
                    "'02', PK_SGD_DESCRIPCION.DE_PROVEEDOR (A.NU_RUC_DES),\n" +
                    "'03', PK_SGD_DESCRIPCION.ANI_SIMIL (A.NU_DNI_DES),\n" +
                    "'04', PK_SGD_DESCRIPCION.OTRO_ORIGEN (A.CO_OTR_ORI_DES),\n" +
                    "'05', PK_SGD_DESCRIPCION.DE_DEPENDENCIA(A.CO_DEP_DES) ||'-'||PK_SGD_DESCRIPCION.DE_NOM_EMP (A.CO_EMP_DES)) deDestino,\n" +
                    "(SELECT DE_EST FROM TDTR_ESTADOS WHERE CO_EST=A.ES_DOC_REC AND DE_TAB='TD_MENSAJERIA') esDoc, \n" +
                    "(SELECT NVL(TI_ENV_MSJ,'0') FROM TDTV_REMITOS WHERE NU_ANN=A.NU_ANN AND NU_EMI=A.NU_EMI) tiEnv \n" +
                    //"FROM tdtv_destinos A LEFT JOIN tdtv_anexos B \n" +
                    "FROM tdtv_destinos A LEFT JOIN TDTV_ANEXOS_MSJ B \n" + //Hermes 21/01/2019 - Requerimiento Mensajeria: Acta 0005-2019
                    "ON A.nu_ann=B.nu_ann AND A.nu_emi=B.nu_emi AND A.nu_des=B.nu_des \n" +
                    "where A.nu_ann = ? \n" +             
                    "and A.nu_emi = ? \n"+
                    //"and B.es_estado = '1' \n"+
                    "order by B.nu_ane ");
   
        
        List<DocumentoAnexoBean> list = new ArrayList<DocumentoAnexoBean>();
        logger.info("getAnexosMsjList: "+sql);
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
    public RemitosResBean getRemitoProyecto(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n"
                + "a.nu_ann, \n"
                + "a.nu_emi, \n"
                + "a.nu_cor_emi, \n"
                
                + "a.co_tip_doc_adm \n"
                
                + "FROM tdtv_remitos a\n"
                + "WHERE a.es_eli = '0' AND a.nu_ann=? AND nu_emi=?");

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
    public DocumentoEmiBean getNumeroAnexoProyecto(String pnuAnn, String pnuEmi) {
        StringBuilder sql = new StringBuilder();
        
        sql.append(" select MAX(ca.NU_ANE) NU_ANE_PROYECTO  from TDTV_ANEXOS ca where ca.NU_EMI=? and ca.NU_ANN=? and ca.ES_PROYECTO='1'   ");                

        DocumentoEmiBean documentoEmiBean = new DocumentoEmiBean();

        try {
            documentoEmiBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(DocumentoEmiBean.class),new Object[]{ pnuEmi,pnuAnn} );
        } catch (EmptyResultDataAccessException e) {
            documentoEmiBean = null;
        } catch (Exception e) {
            documentoEmiBean = null;
            e.printStackTrace();
        }
        return(documentoEmiBean);
    }

    @Override
    public RemitosResBean getRemitoResumenDevolucion(String pnuAnn, String pnuEmi, String pkNuSec) {
        StringBuilder sql = new StringBuilder();
        logger.info("pnuAnn:  "+ pnuAnn +" pnuEmi: "+pnuEmi+ " pkNuSec: "+pkNuSec);
        sql.append("SELECT \n"
                + "a.nu_ann, \n"
                + "a.nu_emi, \n"
                + "a.nu_cor_emi, \n"
                + "a.co_loc_emi,\n"
                + "PK_SGD_DESCRIPCION.de_local (a.co_loc_emi) de_loc_emi, \n"
                + "a.co_dep_emi,\n"
                + "PK_SGD_DESCRIPCION.de_dependencia (a.co_dep_emi) de_dep_emi,\n"
                + "a.ti_emi, \n"
                + "PK_SGD_DESCRIPCION.ti_destino (a.ti_emi) de_tip_emi,\n"
                + "a.co_emp_emi, \n"
                + "PK_SGD_DESCRIPCION.de_nom_emp (co_emp_emi) de_emp_emi,\n"
                + "a.co_emp_res, \n"
                + "PK_SGD_DESCRIPCION.de_nom_emp (a.co_emp_res) de_emp_res,\n"
                + "a.nu_dni_emi, \n"
                + "a.nu_ruc_emi, \n"
                + "a.co_otr_ori_emi,\n"
                + "PK_SGD_DESCRIPCION.ti_emi_emp (a.nu_ann, a.nu_emi) de_ori_emi,\n"
                + "DECODE\n"
                + "  (a.ti_emi,\n"
                + "  '01', PK_SGD_DESCRIPCION.de_dependencia (a.co_dep_emi)\n"
                + "  || ' - '\n"
                + "  || PK_SGD_DESCRIPCION.de_nom_emp (a.co_emp_emi),\n"
                + "  '02', PK_SGD_DESCRIPCION.de_proveedor (a.nu_ruc_emi),\n"
                + "  '03', PK_SGD_DESCRIPCION.ani_simil (a.nu_dni_emi),\n"
                + "  '04', PK_SGD_DESCRIPCION.otro_origen (a.co_otr_ori_emi),\n"
                + "  '05', PK_SGD_DESCRIPCION.de_nom_emp (a.co_emp_emi)\n"
                + "  ) de_ori_emi_mp,\n"
                + " NVL2(A.fe_emi, TO_CHAR(A.fe_emi, 'DD/MM/YYYY  hh24:mi:ss'), ' ') fe_emi, \n"
                + "TO_CHAR (a.fe_emi, 'MM') de_mes, \n"
                + "a.co_tip_doc_adm,\n"
                + "PK_SGD_DESCRIPCION.de_documento (a.co_tip_doc_adm) de_ti_doc,\n"
                + "DECODE (a.ti_emi,\n"
                + "  '01', a.nu_doc_emi || '-' || a.nu_ann || '/' || a.de_doc_sig,\n"
                + "  '05', a.nu_doc_emi || '-' || a.nu_ann || '/' || a.de_doc_sig,\n"
                + "  a.de_doc_sig\n"
                + "  ) nu_doc,\n"
                + "a.co_gru, a.es_doc_emi,\n"
                + "PK_SGD_DESCRIPCION.estados (a.es_doc_emi,'TDTV_REMITOS') de_es_doc_emi,\n"
                + "PK_SGD_DESCRIPCION.estados_mp (a.es_doc_emi,'TDTV_REMITOS') de_es_doc_emi_mp,\n"
                + "a.nu_dia_ate, \n"
                + "a.de_asu, \n"
                + "UPPER (a.de_asu) de_asu_m, \n"
                + "a.de_ane,\n"
                + "a.co_tip_doc_pro,\n"
                + "PK_SGD_DESCRIPCION.de_documento (a.co_tip_doc_pro) de_tip_doc_pro,\n"
                + "DECODE (a.nu_candes,\n"
                + "  1, PK_SGD_DESCRIPCION.ti_des (a.nu_ann, a.nu_emi),\n"
                + "  PK_SGD_DESCRIPCION.ti_des_v (a.nu_ann, a.nu_emi)\n"
                + "  ) ti_des,\n"
                + "DECODE (a.nu_candes,\n"
                + "  1, PK_SGD_DESCRIPCION.ti_co_dep_des (a.nu_ann, a.nu_emi),\n"
                + "  PK_SGD_DESCRIPCION.ti_co_dep_des_v (a.nu_ann, a.nu_emi)\n"
                + "  ) co_dep_des_t,\n"
                + "DECODE (a.nu_candes,\n"
                + "  1, PK_SGD_DESCRIPCION.ti_des_emp (a.nu_ann, a.nu_emi),\n"
                + "  PK_SGD_DESCRIPCION.ti_des_emp_v (a.nu_ann, a.nu_emi)\n"
                + "  ) de_emp_pro,\n"
                + "a.ti_cap,\n"
                + "a.co_use_cre,\n"
                + "a.fe_use_cre, \n"
                + "a.co_use_mod, \n"
                + "a.fe_use_mod, \n"
                + "a.nu_ann_exp,\n"
                + "a.nu_sec_exp, \n"
                + "a.nu_det_exp,\n"
                + "PK_SGD_DESCRIPCION.DE_NU_EXPEDIENTE(a.nu_ann_exp,a.nu_sec_exp) NU_EXPEDIENTE,\n"
                + "DECODE (a.nu_sec_exp, NULL, NULL, 'E') in_expe, \n"
                + "nu_folios, \n"
                + "a.doc_estado_msj, "
                + "PK_SGD_DESCRIPCION.estados(a.doc_estado_msj, 'ENVIO_DOCUMENTO_MSJ') de_doc_estado_msj, "                
                + "TO_CHAR((SELECT B.FE_USE_CRE FROM TDTV_ESTACION_DOC  B  WHERE B.NU_ANN = ? AND B.NU_EMI= ? AND B.NU_SEC= ? ), 'DD/MM/YYYY  HH24:MI') fe_dev_ofi, "
                + "(SELECT B.OBS_DEV FROM TDTV_ESTACION_DOC  B  WHERE B.NU_ANN = ? AND B.NU_EMI= ? AND B.NU_SEC= ? ) obs_dev, "
                + "(SELECT B.CO_USE_CRE FROM TDTV_ESTACION_DOC  B  WHERE B.NU_ANN = ? AND B.NU_EMI= ? AND B.NU_SEC= ? ) co_emp_dev, "
                + "SE_MESA_PARTES \n"
                + "FROM tdtv_remitos a, TDTV_ESTACION_DOC B \n"
                + "WHERE A.NU_ANN = B.NU_ANN AND A.NU_EMI= B.NU_EMI AND a.es_eli = '0' AND a.nu_ann= ? AND A.nu_emi= ? AND B.NU_SEC= ? ");
        
        RemitosResBean remitoDocBean = new RemitosResBean();

        try {
            remitoDocBean = this.jdbcTemplate.queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(RemitosResBean.class),new Object[]{ 
                pnuAnn,pnuEmi, pkNuSec,
                pnuAnn,pnuEmi, pkNuSec,
                pnuAnn,pnuEmi, pkNuSec,
                pnuAnn,pnuEmi, pkNuSec} );
        } catch (EmptyResultDataAccessException e) {
            remitoDocBean = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(remitoDocBean);        
    }        
    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
    @Override
    public String getVerificaDocParaCerraPlazoAtencion(String pcoEmp, String pnuAnn, String pnuEmi) throws Exception{
        try{
            if(pnuAnn!=null){
                SimpleJdbcCall simpleJdbcCall = 
                new SimpleJdbcCall(this.dataSource).withCatalogName("PK_SGD_TRAMITE").withFunctionName("FU_DOC_PLA_ATE")
                    .withoutProcedureColumnMetaDataAccess()
                    .useInParameterNames("p_vCO_EMP_EMI", "p_vNU_ANN", "p_vNU_EMI")
                    .declareParameters(
                        new SqlOutParameter("RESULT", Types.VARCHAR),
                        new SqlParameter("p_vCO_EMP_EMI", Types.VARCHAR),
                        new SqlParameter("p_vNU_ANN", Types.VARCHAR),
                        new SqlParameter("p_vNU_EMI", Types.VARCHAR));
                
                SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("p_vCO_EMP_EMI", pcoEmp)
                    .addValue("p_vNU_ANN", pnuAnn)
                    .addValue("p_vNU_EMI", pnuEmi);
                    
                Map out;
                String mensaje= null;
                try {
                     out = simpleJdbcCall.execute(in);
                     mensaje = (String)out.get("RESULT");
                } catch (Exception e) {
                     e.printStackTrace();
                     throw e;
                }
                return mensaje;
            } else {
                return "Sin retorno valor autorizado";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }     
    }

    @Override
    public String getVerificaTipoTramitePorDocumento(String pCoDepen, String pCoTipoDoc) {
        String vResult= "NO_OK";
        try {
            vResult = this.jdbcTemplate.queryForObject("SELECT count(*) AS existe\n" +
                                                        "  FROM tdtx_moti_docu_depe\n" +
                                                        " WHERE co_dep = ?\n" +
                                                        "   AND co_tip_doc = ?\n" +
                                                        "   AND CO_MOT = '4'", 
                    String.class, new Object[]{pCoDepen,pCoTipoDoc});
        } catch (Exception e) {
            e.printStackTrace();
            vResult=e.getMessage().substring(0, 20);
        }        
        return vResult;    
    }
    /*[HPB-02/10/20] Inicio - Plazo de Atencion*/
}
