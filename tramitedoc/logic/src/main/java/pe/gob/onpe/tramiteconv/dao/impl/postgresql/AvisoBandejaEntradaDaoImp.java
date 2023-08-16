/**
 * 
 */
package pe.gob.onpe.tramiteconv.dao.impl.postgresql;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import pe.gob.onpe.tramitedoc.dao.AvisoBandejaEntradaDao;
import pe.gob.onpe.tramitedoc.dao.SimpleJdbcDaoBase;
import org.springframework.stereotype.Repository;
import pe.gob.onpe.tramitedoc.bean.AvisoBandejaEntradaBean;
import pe.gob.onpe.tramitedoc.bean.EtiquetaBandejaEnBean;

/**
 * @author ecueva
 *
 */
@Repository("avisoBandejaEntradaDao")
public class AvisoBandejaEntradaDaoImp  extends SimpleJdbcDaoBase implements AvisoBandejaEntradaDao {
	
    @Override
    public List<AvisoBandejaEntradaBean> getBandejaEntradaAccesoTotal(String coDependencia,String coEmpleado){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 orden,'06' Ti_Pen,\n" +
                    "      'PARA DESPACHO' de_pen, 'Documentos Administrativos listos para la Firma' de_resumen,\n" +
                    "      SUM(nu_can) Nu_Can , :pCoDep co_dep \n" +
                    "FROM (SELECT count(1) Nu_Can     \n" +
                    "       FROM IDOSGD.Tdtv_Remitos r\n" +
                    "      WHERE r.Es_Doc_Emi IN ('7') \n" +
                    "     AND r.Ti_Emi = '01' \n" +                
                    "        AND r.Co_Dep_Emi = :pCoDep \n" +
                    "      GROUP BY r.Co_Dep_Emi \n" +
                    "      union select 0 nu_can) Q1\n" +
                    "UNION\n" +
                    "SELECT 2 ORDEN,'VB' TI_PEN,\n" +
                    "   'PARA VISTO BUENO' DE_PEN,'Documentos Administrativos listos para Visto Bueno' DE_RESUMEN,\n" +
                    "   COUNT(1) NU_CAN,:pCoDep CO_DEP\n" +
                    "   FROM IDOSGD.TDTV_REMITOS R, IDOSGD.TDTV_PERSONAL_VB VB\n" +
                    "   WHERE R.NU_ANN=VB.NU_ANN\n" +
                    "        AND R.NU_EMI=VB.NU_EMI\n" +
                    "        AND R.ES_DOC_EMI='7'\n" +
//                    "        AND R.CO_DEP_EMI=:pCoDep\n" +
                    "        AND VB.CO_DEP=:pCoDep\n" +
                    "        AND VB.CO_EMP_VB=:pCoEmp\n" +                                
                    "        AND VB.IN_VB <> '1'\n" +
                    "        GROUP BY VB.CO_DEP\n" +
                    "UNION\n" +
                    "SELECT 3 orden,'05' Ti_Pen,\n" +
                    "       'EN PROYECTO' De_Pen,'Documentos Administrativos en proceso de elaboración' de_resumen, \n" +
                    "       sum(Nu_Can) Nu_Can , :pCoDep co_dep \n" +
                    "  from \n" +
                    "  (select \n" +
                    "        COUNT(1) Nu_Can \n" +
                    "    FROM IDOSGD.Tdtv_Remitos r \n" +
                    "   WHERE r.Es_Doc_Emi IN ('5') \n" +
                    "     AND r.Ti_Emi = '01' \n" +
                    "     AND r.Co_Dep_Emi = :pCoDep \n" +
                    "   GROUP BY r.Co_Dep_Emi \n" +
                    "    union select 0 nu_can\n" +
                    "    ) Q2\n" +
                    "UNION\n" +
                    "   SELECT  4 orden ,  '07' ti_pen, 'MUY URGENTES' de_pen, 'Documentos pendientes de recibir MUY URGENTES' de_resumen ,\n" +
                    "            COUNT (1) nu_can, :pCoDep co_dep \n" +
                    "       FROM IDOSGD.tdtv_remitos r, IDOSGD.tdtv_destinos t\n" +
                    "      WHERE r.nu_ann = t.nu_ann\n" +
                    "        AND r.nu_emi = t.nu_emi\n" +
                    "        AND r.es_doc_emi NOT IN ('5', '9', '7') \n" +
                    "        AND t.es_doc_rec = '0'\n" +
                    "        AND t.es_eli = '0'\n" +
                    "        AND t.co_pri = '3'\n" +
                    "        AND r.in_oficio = '0'\n" +
                    "        AND t.co_dep_des = :pCoDep \n" +
                    "   GROUP BY t.co_dep_des\n" +
                    "UNION\n" +
                    "SELECT 5 orden ,  '04' ti_pen, 'URGENTES' de_pen, 'Documentos pendientes de recibir URGENTES' de_resumen ,\n" +
                    "       COUNT (1) nu_can , :pCoDep co_dep \n" +
                    "       FROM IDOSGD.tdtv_remitos r, IDOSGD.tdtv_destinos t\n" +
                    "      WHERE r.nu_ann = t.nu_ann\n" +
                    "        AND r.nu_emi = t.nu_emi\n" +
                    "        AND r.es_doc_emi NOT IN ('5', '9', '7') \n" +
                    "        AND t.es_doc_rec = '0'\n" +
                    "        AND t.es_eli = '0'\n" +
                    "        AND t.co_pri = '2'\n" +
                    "        AND r.in_oficio = '0'\n" +
                    "        AND t.co_dep_des = :pCoDep \n" +
                    "   GROUP BY t.co_dep_des\n" +
                    "UNION\n" +
                    "SELECT 6 orden,'01' Ti_Pen, \n" +
                    "       'NO LEIDOS' De_Pen, 'Documentos pendientes de recibir' de_resumen, \n" +
                    "       SUM(nu_can) Nu_Can , :pCoDep co_dep \n" +
                    "FROM (SELECT  COUNT(1) Nu_Can \n" +
                    "      FROM IDOSGD.Tdtv_Remitos  r, \n" +
                    "           IDOSGD.Tdtv_Destinos t \n" +
                    "     WHERE r.Nu_Ann = t.Nu_Ann \n" +
                    "       AND r.Nu_Emi = t.Nu_Emi \n" +
                    "       AND r.Es_Doc_Emi NOT IN ('5', '7', '9')\n" +
                    "       AND t.Es_Doc_Rec = '0'\n" +
                    "       AND t.Es_Eli = '0'\n" +
                    "       AND t.Co_Dep_Des = :pCoDep \n" +
                    "     GROUP BY t.Co_Dep_Des\n" +
                    "     union select 0 nu_can) Q3\n" +
                    "ORDER BY 1");

        List<AvisoBandejaEntradaBean> list = new ArrayList<AvisoBandejaEntradaBean>();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        objectParam.put("pCoDep", coDependencia);
        objectParam.put("pCoEmp", coEmpleado);
        
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(AvisoBandejaEntradaBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }    

    @Override
    public List<AvisoBandejaEntradaBean> getBandejaEntradaAccesoPersonal(String coDependencia,String coEmpleado){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 orden,'06' Ti_Pen,\n" +
                    "      'PARA DESPACHO' De_Pen, 'Documentos Administrativos listos para la Firma' de_resumen,\n" +
                    "      SUM(nu_can) Nu_Can , :pCoDep co_dep \n" +
                    "FROM (SELECT count(1) Nu_Can     \n" +
                    "       FROM IDOSGD.Tdtv_Remitos r\n" +
                    "      WHERE r.Es_Doc_Emi IN ('7') \n" +
                    "     AND r.Ti_Emi = '01' \n" +                
                    "        AND r.Co_Dep_Emi = :pCoDep \n" +
                    "        AND r.Co_Emp_Res = :pCoEmp \n" +
                    "      GROUP BY r.Co_Dep_Emi,\n" +
                    "               r.Co_Emp_Res\n" +
                    "      union select 0 nu_can) Q1" +
                    " UNION\n" +
                    "SELECT 2 ORDEN,'VB' TI_PEN,\n" +
                    "   'PARA VISTO BUENO' DE_PEN,'Documentos Administrativos listos para Visto Bueno' DE_RESUMEN,\n" +
                    "   COUNT(1) NU_CAN,:pCoDep CO_DEP\n" +
                    "   FROM IDOSGD.TDTV_REMITOS R,IDOSGD.TDTV_PERSONAL_VB VB\n" +
                    "   WHERE R.NU_ANN=VB.NU_ANN\n" +
                    "        AND R.NU_EMI=VB.NU_EMI\n" +
                    "        AND R.ES_DOC_EMI='7'\n" +
//                    "        AND R.CO_DEP_EMI=:pCoDep\n" +
                    "        AND VB.CO_DEP=:pCoDep\n" +
                    "        AND VB.CO_EMP_VB=:pCoEmp\n" +                                
                    "        AND VB.IN_VB <> '1'\n" +
                    "        GROUP BY VB.CO_DEP\n" +
                    " UNION\n" +                
                    " SELECT 3 orden,'05' Ti_Pen,\n" +
                    "       'EN PROYECTO' De_Pen,'Documentos Administrativos en proceso de elaboración' de_resumen, \n" +
                    "       sum(Nu_Can) Nu_Can , :pCoDep co_dep \n" +
                    "  from \n" +
                    "  (select \n" +
                    "        COUNT(1) Nu_Can \n" +
                    "    FROM IDOSGD.Tdtv_Remitos r \n" +
                    "   WHERE r.Es_Doc_Emi IN ('5') \n" +
                    "     AND r.Ti_Emi = '01' \n" +
                    "     AND r.Co_Dep_Emi = :pCoDep \n" +
                    "     AND r.Co_Emp_Res = :pCoEmp \n" +
                    "   GROUP BY r.Co_Dep_Emi, \n" +
                    "            r.Co_Emp_Res  \n" +
                    "    union select 0 nu_can \n" +
                    "    ) Q2" +
                    " UNION\n" +
                    "SELECT 4 orden,'07' Ti_Pen,\n" +
                    "       'MUY URGENTES' De_Pen,'Documentos pendientes de recibir MUY URGENTES' de_resumen,\n" +
                    "       COUNT(1) Nu_Can, :pCoDep co_dep \n" +
                    "  FROM IDOSGD.Tdtv_Remitos  r,\n" +
                    "       IDOSGD.Tdtv_Destinos t\n" +
                    " WHERE r.Nu_Ann = t.Nu_Ann\n" +
                    "   AND r.Nu_Emi = t.Nu_Emi\n" +
                    "   AND r.Es_Doc_Emi NOT IN ('5', '7', '9') --en proyecto, anulados\n" +
                    "   AND t.Co_Dep_Des = :pCoDep \n" +
                    "   AND t.Co_Emp_Des = :pCoEmp \n" +
                    "   AND t.Es_Doc_Rec = '0'\n" +
                    "   AND t.Es_Eli = '0'\n" +
                    "   AND t.Co_Pri = '3'\n" +
                    "   AND r.in_oficio = '0'\n" +
                    " GROUP BY t.Co_Dep_Des,\n" +
                    "          t.Co_Emp_Des\n" +
                    " UNION\n" +
                    "SELECT 5 orden,'04' Ti_Pen,\n" +
                    "       'URGENTES' De_Pen, 'Documentos pendientes de recibir URGENTES' de_resumen,\n" +
                    "       COUNT(1) Nu_Can, :pCoDep co_dep \n" +
                    "  FROM IDOSGD.Tdtv_Remitos  r,\n" +
                    "       IDOSGD.Tdtv_Destinos t\n" +
                    " WHERE r.Nu_Ann = t.Nu_Ann\n" +
                    "   AND r.Nu_Emi = t.Nu_Emi\n" +
                    "   AND r.Es_Doc_Emi NOT IN ('5', '7', '9') --en proyecto, anulados\n" +
                    "   AND t.Co_Dep_Des = :pCoDep \n" +
                    "   AND t.Co_Emp_Des = :pCoEmp \n" +
                    "   AND t.Es_Doc_Rec = '0'\n" +
                    "   AND t.Es_Eli = '0'\n" +
                    "   AND t.Co_Pri = '2'\n" +
                    "   AND r.in_oficio = '0'\n" +
                    " GROUP BY t.Co_Dep_Des,\n" +
                    "          t.Co_Emp_Des  \n" +
                    " UNION\n" +
                    "SELECT 6 orden,'01' Ti_Pen, \n" +
                    "       'NO LEIDOS' De_Pen, 'Documentos pendientes de recibir' de_resumen, \n" +
                    "       SUM(nu_can) Nu_Can , :pCoDep co_dep \n" +
                    "FROM (SELECT  COUNT(1) Nu_Can \n" +
                    "      FROM IDOSGD.Tdtv_Remitos  r, \n" +
                    "           IDOSGD.Tdtv_Destinos t \n" +
                    "     WHERE r.Nu_Ann = t.Nu_Ann \n" +
                    "       AND r.Nu_Emi = t.Nu_Emi \n" +
                    "       AND r.Es_Doc_Emi NOT IN ('5', '7', '9')\n" +
                    "       AND t.Co_Dep_Des = :pCoDep \n" +
                    "       AND t.Co_Emp_Des = :pCoEmp \n" +
                    "       AND t.Es_Doc_Rec = '0'\n" +
                    "       AND t.Es_Eli = '0'\n" +
                    "     GROUP BY t.Co_Dep_Des,\n" +
                    "              t.Co_Emp_Des \n" +
                    "     union select 0 nu_can) Q3" +
                    " order by 1");

        List<AvisoBandejaEntradaBean> list = new ArrayList<AvisoBandejaEntradaBean>();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        objectParam.put("pCoDep", coDependencia);
        objectParam.put("pCoEmp", coEmpleado);
        
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(AvisoBandejaEntradaBean.class));
            
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<AvisoBandejaEntradaBean> getBandejaEntradaAccesoFuncionario(String coDependencia,String coEmpleado){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 orden,'06' Ti_Pen,\n" +
                    "	       'PARA DESPACHO' De_Pen, 'Documentos Administrativos listos para la Firma' de_resumen ,\n" +
                    "	       COUNT(1) Nu_Can, :pCoDep co_dep\n" +
                    "	  FROM IDOSGD.Tdtv_Remitos r\n" +
                    "	 WHERE r.Es_Doc_Emi IN ('7') --Listos para despacho\n" +
                    "     AND r.Ti_Emi = '01' \n" +
                    "	   and r.Co_Dep_Emi = :pCoDep \n" +
                    "	   AND (r.Co_Emp_Res = :pCoEmp OR r.co_emp_emi = :pCoEmp)\n" +
                    "	 GROUP BY r.Co_Dep_Emi\n" +
                    "	 UNION\n" +
                    "SELECT 2 ORDEN,'VB' TI_PEN,\n" +
                    "   'PARA VISTO BUENO' DE_PEN,'Documentos Administrativos listos para Visto Bueno' DE_RESUMEN,\n" +
                    "   COUNT(1) NU_CAN,:pCoDep CO_DEP\n" +
                    "   FROM IDOSGD.TDTV_REMITOS R,IDOSGD.TDTV_PERSONAL_VB VB\n" +
                    "   WHERE R.NU_ANN=VB.NU_ANN\n" +
                    "        AND R.NU_EMI=VB.NU_EMI\n" +
                    "        AND R.ES_DOC_EMI='7'\n" +
//                    "        AND R.CO_DEP_EMI=:pCoDep\n" +
                    "        AND VB.CO_DEP=:pCoDep\n" +
                    "        AND VB.CO_EMP_VB=:pCoEmp\n" +                                
                    "        AND VB.IN_VB <> '1'\n" +
                    "        GROUP BY VB.CO_DEP\n" +
                    "UNION\n" +               
                    "	SELECT 3 orden,'05' Ti_Pen,\n" +
                    "	       'EN PROYECTO' De_Pen, 'Documentos Administrativos en proceso de elaboración' de_resumen,\n" +
                    "	       COUNT(1) Nu_Can, :pCoDep co_dep\n" +
                    "	  FROM IDOSGD.Tdtv_Remitos r\n" +
                    "	 WHERE r.Es_Doc_Emi IN ('5') --En proyecto\n" +
                    "	   AND r.Ti_Emi = '01'\n" +
                    "	   and r.Co_Dep_Emi = :pCoDep \n" +
                    "	   AND (r.Co_Emp_Res = :pCoEmp OR r.co_emp_emi = :pCoEmp)\n" +
                    "	 GROUP BY r.Co_Dep_Emi			 \n" +
                    "	 UNION\n" +
                    "	SELECT 4 orden,'07' Ti_Pen,\n" +
                    "	       'MUY URGENTES' De_Pen, 'Documentos pendientes de recibir MUY URGENTES' de_resumen,\n" +
                    "	       COUNT(1) Nu_Can, :pCoDep co_dep\n" +
                    "	  FROM IDOSGD.Tdtv_Remitos  r,\n" +
                    "			       IDOSGD.Tdtv_Destinos t\n" +
                    "	 WHERE r.Nu_Ann = t.Nu_Ann\n" +
                    "	   AND r.Nu_Emi = t.Nu_Emi\n" +
                    "	   AND r.Es_Doc_Emi NOT IN ('5', '7', '9') --en proyecto, anulados\n" +
                    "	   AND t.Es_Doc_Rec = '0'\n" +
                    "	   AND t.Es_Eli = '0'\n" +
                    "	   AND t.Co_Pri = '3'\n" +
                    "	   and t.Co_Dep_Des = :pCoDep \n" +
                    "	   AND t.Co_Emp_Des = :pCoEmp\n" +
                    "	 GROUP BY t.Co_Dep_Des,\n" +
                    "	          t.Co_Emp_Des			\n" +
                    "	 UNION\n" +
                    "	SELECT 5 orden,'04' Ti_Pen,\n" +
                    "	       'URGENTES' De_Pen, 'Documentos pendientes de recibir URGENTES' de_resumen,\n" +
                    "	       COUNT(1) Nu_Can, :pCoDep co_dep\n" +
                    "	  FROM IDOSGD.Tdtv_Remitos  r,\n" +
                    "	       IDOSGD.Tdtv_Destinos t\n" +
                    "	 WHERE r.Nu_Ann = t.Nu_Ann\n" +
                    "	   AND r.Nu_Emi = t.Nu_Emi\n" +
                    "	   AND r.Es_Doc_Emi NOT IN ('5', '7', '9') --en proyecto, anulados\n" +
                    "	   AND t.Es_Doc_Rec = '0'\n" +
                    "	   AND t.Es_Eli = '0'\n" +
                    "	   AND t.Co_Pri = '2'\n" +
                    "	   and t.Co_Dep_Des = :pCoDep\n" +
                    "	   AND t.Co_Emp_Des = :pCoEmp\n" +
                    "	 GROUP BY t.Co_Dep_Des,\n" +
                    "	          t.Co_Emp_Des	\n" +
                    "	 UNION\n" +
                    "	SELECT 6 orden,'01' Ti_Pen,\n" +
                    "	       'NO LEIDOS' De_Pen, 'Documentos pendientes de recibir' de_resumen,\n" +
                    "	       COUNT(1) Nu_Can, :pCoDep co_dep\n" +
                    "	  FROM IDOSGD.Tdtv_Remitos  r,\n" +
                    "	       IDOSGD.Tdtv_Destinos t\n" +
                    "	 WHERE r.Nu_Ann = t.Nu_Ann\n" +
                    "	   AND r.Nu_Emi = t.Nu_Emi\n" +
                    "	   AND r.Es_Doc_Emi NOT IN ('5', '7', '9') --en proyecto, anulados\n" +
                    "	   AND t.Es_Doc_Rec = '0'\n" +
                    "	   AND t.Es_Eli = '0'\n" +
                    "	   and t.Co_Dep_Des = :pCoDep \n" +
                    "	   AND t.Co_Emp_Des = :pCoEmp \n" +
                    "	 GROUP BY t.Co_Dep_Des,\n" +
                    "	          t.Co_Emp_Des\n" +
                    " ORDER BY 1");

        List<AvisoBandejaEntradaBean> list = new ArrayList<AvisoBandejaEntradaBean>();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        objectParam.put("pCoDep", coDependencia);
        objectParam.put("pCoEmp", coEmpleado);
        
        
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(AvisoBandejaEntradaBean.class));
            
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }     

    @Override
    public List<EtiquetaBandejaEnBean> getListEtiquetaBandejaEntrada(String coDep, String coEmp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select e.co_est,e.de_est descripcion,count(*) numero_documentos,d.co_dep_des,d.co_emp_rec ");
        sql.append("from IDOSGD.tdtv_bandeja_rec d,IDOSGD.tdtr_estados e ");
        sql.append("where d.co_dep_des = :pCoDep ");
        sql.append("and d.co_emp_rec=:pCoEmp ");
        sql.append("and d.co_etiqueta_rec<>'0' ");
        sql.append("and d.es_doc_emi not in ('5','7','9') ");
        sql.append("and e.co_est=d.co_etiqueta_rec ");
        sql.append("and e.de_tab='CO_ETIQUETA_REC' ");
        sql.append("group by e.co_est,e.de_est,d.co_dep_des,d.co_emp_rec ");
        sql.append("order by e.co_est ");
        List<EtiquetaBandejaEnBean> list = new ArrayList<EtiquetaBandejaEnBean>();
        Map<String, Object> objectParam = new HashMap<String, Object>();
        objectParam.put("pCoDep", coDep);
        objectParam.put("pCoEmp", coEmp);
        try {
            list = this.namedParameterJdbcTemplate.query(sql.toString(), objectParam, BeanPropertyRowMapper.newInstance(EtiquetaBandejaEnBean.class));
        }catch (EmptyResultDataAccessException e) {
            list = null;
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
}
