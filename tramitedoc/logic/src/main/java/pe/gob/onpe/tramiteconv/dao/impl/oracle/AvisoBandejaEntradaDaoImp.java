/**
 * 
 */
package pe.gob.onpe.tramiteconv.dao.impl.oracle;

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
                    "      SUM(nu_can) Nu_Can , :pCoDep co_dep , '' co_bandeja \n" +
                    "FROM (SELECT count(1) Nu_Can     \n" +
                    "       FROM Tdtv_Remitos r\n" +
                    "      WHERE r.Es_Doc_Emi IN ('7') \n" +
                    "     AND r.Ti_Emi = '01' \n" +                
                    "        AND r.Co_Dep_Emi = :pCoDep \n" +
                    "      GROUP BY r.Co_Dep_Emi \n" +
                    "      union select 0 nu_can from dual)\n" +
                    "UNION\n" +
                    "SELECT 2 ORDEN,'VB' TI_PEN,\n" +
                    "   'PARA VISTO BUENO' DE_PEN,'Documentos Administrativos listos para Visto Bueno' DE_RESUMEN,\n" +
                    "   COUNT(1) NU_CAN,:pCoDep CO_DEP , '' co_bandeja \n" +
                    "   FROM TDTV_REMITOS R,TDTV_PERSONAL_VB VB\n" +
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
                    "       sum(Nu_Can) Nu_Can , :pCoDep co_dep , '' co_bandeja  \n" +
                    "  from \n" +
                    "  (select \n" +
                    "        COUNT(1) Nu_Can \n" +
                    "    FROM Tdtv_Remitos r \n" +
                    "   WHERE r.Es_Doc_Emi IN ('5') \n" +
                    "     AND r.Ti_Emi = '01' \n" +
                    "     AND r.Co_Dep_Emi = :pCoDep \n" +
                    "   GROUP BY r.Co_Dep_Emi \n" +
                    "    union select 0 nu_can from dual \n" +
                    "    )\n" +
                    "UNION\n" +
                    "   SELECT  4 orden ,  '07' ti_pen, 'MUY URGENTES (Encargado)' de_pen, 'Documentos pendientes de recibir MUY URGENTES' de_resumen ,\n" +
                    "            COUNT (1) nu_can, :pCoDep co_dep , 'O' co_bandeja  \n" +
                    "       FROM tdtv_remitos r, tdtv_destinos t\n" +
                    "      WHERE r.nu_ann = t.nu_ann\n" +
                    "        AND r.nu_emi = t.nu_emi\n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //"        AND r.es_doc_emi NOT IN ('5', '9', '7') \n" +
                    "        AND r.es_doc_emi NOT IN ('5', '9', '7', 'A', 'B') \n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    "        AND t.es_doc_rec = '0'\n" +
                    "        AND t.es_eli = '0'\n" +
                    "        AND t.co_pri = '3'\n" +
                    "        AND r.in_oficio = '0'\n" +
                    "        AND t.co_dep_des = :pCoDep \n" +
                    "       AND t.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pCoDep ) "+/*[HPB] 06/11/20 FILTRAR MUY URGENTES PARA ENCARGADO*/
                    "   GROUP BY t.co_dep_des\n" +
                    "UNION\n" +
                    "SELECT 5 orden ,  '04' ti_pen, 'URGENTES (Encargado)' de_pen, 'Documentos pendientes de recibir URGENTES' de_resumen ,\n" +
                    "       COUNT (1) nu_can , :pCoDep co_dep , 'O' co_bandeja  \n" +
                    "       FROM tdtv_remitos r, tdtv_destinos t\n" +
                    "      WHERE r.nu_ann = t.nu_ann\n" +
                    "        AND r.nu_emi = t.nu_emi\n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //"        AND r.es_doc_emi NOT IN ('5', '9', '7') \n" +
                    "        AND r.es_doc_emi NOT IN ('5', '9', '7', 'A', 'B') \n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    "        AND t.es_doc_rec = '0'\n" +
                    "        AND t.es_eli = '0'\n" +
                    "        AND t.co_pri = '2'\n" +
                    "        AND r.in_oficio = '0'\n" +
                    "        AND t.co_dep_des = :pCoDep \n" +
                    "       AND t.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pCoDep ) "+/*[HPB] 06/11/20 FILTRAR URGENTES PARA ENCARGADO*/
                    "   GROUP BY t.co_dep_des\n" +
                    /*"UNION\n" +
                    "SELECT 6 orden,'01' Ti_Pen, \n" +
                    "       'NO LEIDOS' De_Pen, 'Documentos pendientes de recibir' de_resumen, \n" +
                    "       SUM(nu_can) Nu_Can , :pCoDep co_dep , '' co_bandeja  \n" +
                    "FROM (SELECT  COUNT(1) Nu_Can \n" +
                    "      FROM Tdtv_Remitos  r, \n" +
                    "           Tdtv_Destinos t \n" +
                    "     WHERE r.Nu_Ann = t.Nu_Ann \n" +
                    "       AND r.Nu_Emi = t.Nu_Emi \n" +
                    "       AND r.Es_Doc_Emi NOT IN ('5', '7', '9')\n" +
                    "       AND t.Es_Doc_Rec = '0'\n" +
                    "       AND t.Es_Eli = '0'\n" +
                    "       AND t.Co_Dep_Des = :pCoDep \n" +
                    "     GROUP BY t.Co_Dep_Des\n" +
                    "     union select 0 nu_can from dual)\n" +
                */
                
                /*YPA ADD RECIBIDOS SIS*/
                "UNION\n" +
                    "SELECT 7 orden,'02' Ti_Pen, \n" +
                    "       'RECIBIDOS (Encargado)' De_Pen, 'Documentos recibidos' de_resumen, \n" +
                    "       SUM(nu_can) Nu_Can , :pCoDep co_dep , 'O' co_bandeja  \n" +
                    "FROM (SELECT  COUNT(1) Nu_Can \n" +
                    "      FROM Tdtv_Remitos  r, \n" +
                    "           Tdtv_Destinos t \n" +
                    "     WHERE r.Nu_Ann = t.Nu_Ann \n" +
                    "       AND r.Nu_Emi = t.Nu_Emi \n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //"       AND r.Es_Doc_Emi NOT IN ('5', '7', '9')\n" +
                    "       AND r.Es_Doc_Emi NOT IN ('5', '7', '9', 'A', 'B')\n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    "       AND t.Es_Doc_Rec = '1'\n" +
                    "       AND t.Es_Eli = '0'\n" +
                    "       AND t.Co_Dep_Des = :pCoDep \n" +
                   "       AND t.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pCoDep ) "+
                    "     GROUP BY t.Co_Dep_Des\n" +
                    "     union select 0 nu_can from dual)\n" +
                 /*FIN YPA ADD RECIBIDOS SIS*/
                "UNION\n" +
                    "SELECT 6 orden,'01' Ti_Pen, \n" +
                    "       'NO LEIDOS (Encargado)' De_Pen, 'Documentos pendientes de recibir' de_resumen, \n" +
                    "       SUM(nu_can) Nu_Can , :pCoDep co_dep , 'O' co_bandeja  \n" +
                    "FROM (SELECT  COUNT(1) Nu_Can \n" +
                    "      FROM Tdtv_Remitos  r, \n" +
                    "           Tdtv_Destinos t \n" +
                    "     WHERE r.Nu_Ann = t.Nu_Ann \n" +
                    "       AND r.Nu_Emi = t.Nu_Emi \n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //"       AND r.Es_Doc_Emi NOT IN ('5', '7', '9')\n" +
                    "       AND r.Es_Doc_Emi NOT IN ('5', '7', '9', 'A', 'B')\n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    "       AND t.Es_Doc_Rec = '0'\n" +
                    "       AND t.Es_Eli = '0'\n" +
                    "       AND t.Co_Dep_Des = :pCoDep \n" +
                    "       AND t.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pCoDep ) "+
                    "     GROUP BY t.Co_Dep_Des\n" +
                    "     union select 0 nu_can from dual)\n" +
                
                "UNION\n" +
                    "SELECT 8 orden,'01' Ti_Pen, \n" +
                    "       'NO LEIDOS (Personal)' De_Pen, 'Documentos pendientes de recibir' de_resumen, \n" +
                    "       SUM(nu_can) Nu_Can , :pCoDep co_dep , 'E' co_bandeja  \n" +
                    "FROM (SELECT  COUNT(1) Nu_Can \n" +
                    "      FROM Tdtv_Remitos  r, \n" +
                    "           Tdtv_Destinos t \n" +
                    "     WHERE r.Nu_Ann = t.Nu_Ann \n" +
                    "       AND r.Nu_Emi = t.Nu_Emi \n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //"       AND r.Es_Doc_Emi NOT IN ('5', '7', '9')\n" +
                    "       AND r.Es_Doc_Emi NOT IN ('5', '7', '9', 'A', 'B')\n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    "       AND t.Es_Doc_Rec = '0'\n" +
                    "       AND t.Es_Eli = '0'\n" +
                    "       AND t.Co_Dep_Des = :pCoDep \n" +
                    "       AND not t.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pCoDep ) "+
                    "       AND t.CO_EMP_DES in ( :pCoEmp ) "+
                    "     GROUP BY t.Co_Dep_Des\n" +
                    "     union select 0 nu_can from dual)\n" +
                
                "UNION\n" +
                    "SELECT 9 orden,'02' Ti_Pen, \n" +
                    "       'RECIBIDOS (Del Personal)' De_Pen, 'Documentos pendientes de atender' de_resumen, \n" +
                    "       SUM(nu_can) Nu_Can , :pCoDep co_dep , 'T' co_bandeja  \n" +
                    "FROM (SELECT  COUNT(1) Nu_Can \n" +
                    "      FROM Tdtv_Remitos  r, \n" +
                    "           Tdtv_Destinos t \n" +
                
                    " LEFT JOIN (SELECT  R.NU_ANN AS NU_ANN2, \n" +
                    "       R.NU_EMI AS NU_EMI2, \n" +
                    "       R.NU_ANN_REF AS NU_ANN_REF2, \n" +
                    "       R.NU_EMI_REF AS NU_EMI_REF2,  \n" +
                    "       R.NU_DES_REF  AS NU_DES_REF2 \n" +
                    " FROM IDOSGD.TDTR_REFERENCIA R \n" +
                    " LEFT JOIN IDOSGD.TDTV_REMITOS RE ON R.NU_ANN = RE.NU_ANN AND R.NU_EMI=RE.NU_EMI \n" +
                    " LEFT JOIN IDOSGD.TDTX_REMITOS_RESUMEN RS ON RE.NU_ANN = RS.NU_ANN AND RE.NU_EMI = RS.NU_EMI \n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //" WHERE RE.ES_DOC_EMI NOT IN ('5', '9', '7')) W \n" +
                    " WHERE RE.ES_DOC_EMI NOT IN ('5', '9', '7', 'A', 'B')) W \n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    " ON t.NU_ANN=W.NU_ANN_REF2 AND t.NU_EMI=W.NU_EMI_REF2 AND t.NU_DES = W.NU_DES_REF2 \n" +
                            
                    "     WHERE r.Nu_Ann = t.Nu_Ann \n" +
                    "       AND r.Nu_Emi = t.Nu_Emi \n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //"       AND r.Es_Doc_Emi NOT IN ('5', '7', '9')\n" +
                    "       AND r.Es_Doc_Emi NOT IN ('5', '7', '9', 'A', 'B')\n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    "       AND t.Es_Doc_Rec = '1'\n" +
                    "       AND t.Es_Eli = '0'\n" +
                    "       AND W.NU_ANN2 IS NULL \n" +
                    "       AND t.Co_Dep_Des = :pCoDep \n" +
                    "       AND not t.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pCoDep ) "+
                    //COMENTADO SIS   
                    //"       AND t.CO_EMP_DES in ( :pCoEmp ) "+

                
                    "     GROUP BY t.Co_Dep_Des\n" +
                    "     union select 0 nu_can from dual)\n" +
                
                "UNION\n" +
                    "SELECT 10 orden,'01' Ti_Pen, \n" +
                    "       'NO LEIDOS (Del Personal)' De_Pen, 'Documentos pendientes de recibir' de_resumen, \n" +
                    "       SUM(nu_can) Nu_Can , :pCoDep co_dep , 'P' co_bandeja  \n" +
                    "FROM (SELECT  COUNT(1) Nu_Can \n" +
                    "      FROM Tdtv_Remitos  r, \n" +
                    "           Tdtv_Destinos t \n" +
                    "     WHERE r.Nu_Ann = t.Nu_Ann \n" +
                    "       AND r.Nu_Emi = t.Nu_Emi \n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //"       AND r.Es_Doc_Emi NOT IN ('5', '7', '9')\n" +
                    "       AND r.Es_Doc_Emi NOT IN ('5', '7', '9', 'A', 'B')\n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    "       AND t.Es_Doc_Rec = '0'\n" +
                    "       AND t.Es_Eli = '0'\n" +
                    "       AND t.Co_Dep_Des = :pCoDep \n" +
                    "       AND not t.CO_EMP_DES in ( SELECT NVL(CO_EMPLEADO,'0') FROM RHTM_DEPENDENCIA WHERE CO_DEPENDENCIA= :pCoDep ) "+
                    "       AND not t.CO_EMP_DES in ( :pCoEmp ) "+
                    "     GROUP BY t.Co_Dep_Des\n" +
                    "     union select 0 nu_can from dual)\n" +                
                "UNION\n" +
                    "SELECT 11 orden,'08' Ti_Pen, 'DEVUELTOS (Mensajería)' De_Pen, 'Documentos devueltos a oficina por mensajería' de_resumen, \n" +
                    "       COUNT(1) Nu_Can , :pCoDep co_dep , '' co_bandeja  \n" +
                    "       FROM tdtv_remitos a \n" +
                    "      WHERE a.es_doc_emi in ('0') \n" +
                    "        AND a.doc_estado_msj='6' \n" +
                    "        AND a.co_dep_emi=:pCoDep \n" +
                    "       GROUP BY a.co_dep_emi \n" +
                "UNION\n" +
                    "SELECT 12 orden,'09' Ti_Pen, 'OBSERVADOS (Mesa Partes Virtual)' De_Pen, 'Documentos emitidos observados en la Mesa de Partes Virtual' de_resumen, \n" +
                    "       COUNT(1) Nu_Can , :pCoDep co_dep , '' co_bandeja  \n" +
                    "       FROM tdtv_remitos a \n" +
                    "      WHERE a.es_doc_emi in ('0') \n" +
                    "        AND a.doc_estado_msj IN ('7','8') \n" +
                    "        AND a.ti_env_msj is null \n" +
                    "        AND a.co_dep_emi=:pCoDep \n" +
                    "       GROUP BY a.co_dep_emi \n");   
                if(coDependencia.equals("10619")){/*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR*/
                  sql.append("UNION\n" +
                            "SELECT 13 orden, '10' Ti_Pen, 'PARA REVISIÓN' de_pen, 'Expedientes observados pendientes de revisión' de_resumen, SUM(nu_can) Nu_Can, '10619' co_dep, '' co_bandeja \n" +
                            "       FROM (SELECT count(1) Nu_Can  \n" +
                            "       FROM Tdtv_Remitos r \n" +
                            "      WHERE r.Es_Doc_Emi IN ('B') \n" +
                            "        AND r.Co_Dep_Emi =:pCoDep \n" +
                            "        AND R.CO_GRU = '3' \n" +
                            "        GROUP BY r.Co_Dep_Emi \n" +
                            "       union \n" +           
                            "       select 0 nu_can \n" + 
                            "       from dual) \n"); 
                }
                sql.append( "ORDER BY 1");/*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR*/
                
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
                    "       FROM Tdtv_Remitos r\n" +
                    "      WHERE r.Es_Doc_Emi IN ('7') \n" +
                    "     AND r.Ti_Emi = '01' \n" +                
                    "        AND r.Co_Dep_Emi = :pCoDep \n" +
                    "        AND r.Co_Emp_Res = :pCoEmp \n" +
                    "      GROUP BY r.Co_Dep_Emi,\n" +
                    "               r.Co_Emp_Res\n" +
                    "      union select 0 nu_can from dual)" +
                    "UNION\n" +
                    "SELECT 2 ORDEN,'VB' TI_PEN,\n" +
                    "   'PARA VISTO BUENO' DE_PEN,'Documentos Administrativos listos para Visto Bueno' DE_RESUMEN,\n" +
                    "   COUNT(1) NU_CAN,:pCoDep CO_DEP\n" +
                    "   FROM TDTV_REMITOS R,TDTV_PERSONAL_VB VB\n" +
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
                    "    FROM Tdtv_Remitos r \n" +
                    "   WHERE r.Es_Doc_Emi IN ('5') \n" +
                    "     AND r.Ti_Emi = '01' \n" +
                    "     AND r.Co_Dep_Emi = :pCoDep \n" +
                    "     AND r.Co_Emp_Res = :pCoEmp \n" +
                    "   GROUP BY r.Co_Dep_Emi, \n" +
                    "            r.Co_Emp_Res  \n" +
                    "    union select 0 nu_can from dual \n" +
                    "    )" +
                    "UNION\n" +
                    "SELECT 4 orden,'07' Ti_Pen,\n" +
                    "       'MUY URGENTES' De_Pen,'Documentos pendientes de recibir MUY URGENTES' de_resumen,\n" +
                    "       COUNT(1) Nu_Can, :pCoDep co_dep \n" +
                    "  FROM Tdtv_Remitos  r,\n" +
                    "       Tdtv_Destinos t\n" +
                    " WHERE r.Nu_Ann = t.Nu_Ann\n" +
                    "   AND r.Nu_Emi = t.Nu_Emi\n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //"   AND r.Es_Doc_Emi NOT IN ('5', '7', '9') --en proyecto, anulados\n" +
                    "   AND r.Es_Doc_Emi NOT IN ('5', '7', '9', 'A', 'B') --en proyecto, anulados\n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    "   AND t.Co_Dep_Des = :pCoDep \n" +
                    "   AND t.Co_Emp_Des = :pCoEmp \n" +
                    "   AND t.Es_Doc_Rec = '0'\n" +
                    "   AND t.Es_Eli = '0'\n" +
                    "   AND t.Co_Pri = '3'\n" +
                    "   AND r.in_oficio = '0'\n" +
                    " GROUP BY t.Co_Dep_Des,\n" +
                    "          t.Co_Emp_Des\n" +
                    "UNION\n" +
                    "SELECT 5 orden,'04' Ti_Pen,\n" +
                    "       'URGENTES' De_Pen, 'Documentos pendientes de recibir URGENTES' de_resumen,\n" +
                    "       COUNT(1) Nu_Can, :pCoDep co_dep \n" +
                    "  FROM Tdtv_Remitos  r,\n" +
                    "       Tdtv_Destinos t\n" +
                    " WHERE r.Nu_Ann = t.Nu_Ann\n" +
                    "   AND r.Nu_Emi = t.Nu_Emi\n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //"   AND r.Es_Doc_Emi NOT IN ('5', '7', '9') --en proyecto, anulados\n" +
                    "   AND r.Es_Doc_Emi NOT IN ('5', '7', '9', 'A', 'B') --en proyecto, anulados\n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    "   AND t.Co_Dep_Des = :pCoDep \n" +
                    "   AND t.Co_Emp_Des = :pCoEmp \n" +
                    "   AND t.Es_Doc_Rec = '0'\n" +
                    "   AND t.Es_Eli = '0'\n" +
                    "   AND t.Co_Pri = '2'\n" +
                    "   AND r.in_oficio = '0'\n" +
                    " GROUP BY t.Co_Dep_Des,\n" +
                    "          t.Co_Emp_Des  \n" +
                    "UNION\n" +
                    "SELECT 6 orden,'01' Ti_Pen, \n" +
                    "       'NO LEIDOS' De_Pen, 'Documentos pendientes de recibir' de_resumen, \n" +
                    "       SUM(nu_can) Nu_Can , :pCoDep co_dep \n" +
                    "FROM (SELECT  COUNT(1) Nu_Can \n" +
                    "      FROM Tdtv_Remitos  r, \n" +
                    "           Tdtv_Destinos t \n" +
                    "     WHERE r.Nu_Ann = t.Nu_Ann \n" +
                    "       AND r.Nu_Emi = t.Nu_Emi \n" +
                    /*-- [HPB] Inicio 27/09/22 OS-0000768-2022 --*/
                    //"       AND r.Es_Doc_Emi NOT IN ('5', '7', '9')\n" +
                    "       AND r.Es_Doc_Emi NOT IN ('5', '7', '9', 'A', 'B')\n" +
                    /*-- [HPB] Fin 27/09/22 OS-0000768-2022 --*/
                    "       AND t.Co_Dep_Des = :pCoDep \n" +
                    "       AND t.Co_Emp_Des = :pCoEmp \n" +
                    "       AND t.Es_Doc_Rec = '0'\n" +
                    "       AND t.Es_Eli = '0'\n" +
                    "     GROUP BY t.Co_Dep_Des,\n" +
                    "              t.Co_Emp_Des \n" +
                    "     union select 0 nu_can from dual)" +
                    "UNION\n" +
                    "SELECT 7 orden,'08' Ti_Pen, \n" +
                    "'DEVUELTOS (Mensajería)' De_Pen, 'Documentos devueltos a oficina por mensajería' de_resumen,  \n" +
                    "COUNT(1) Nu_Can , :pCoDep co_dep \n" +
                    "from tdtv_remitos a \n" +
                    "where a.es_doc_emi in ('0') \n" +
                    "and a.doc_estado_msj='6' \n" +
                    "and a.co_dep_emi=:pCoDep \n" +
                    "and a.co_emp_res = :pCoEmp \n" +
                    "GROUP BY a.co_dep_emi \n" +
                    "UNION\n" +
                        "SELECT 8 orden,'09' Ti_Pen, 'OBSERVADOS (Mesa Partes Virtual)' De_Pen, 'Documentos emitidos observados en la Mesa de Partes Virtual' de_resumen, \n" +
                        "       COUNT(1) Nu_Can , :pCoDep co_dep  \n" +
                        "       FROM tdtv_remitos a \n" +
                        "      WHERE a.es_doc_emi in ('0') \n" +
                        "        AND a.doc_estado_msj IN ('7','8') \n" +
                        "        AND a.ti_env_msj is null \n" +
                        "        AND a.co_dep_emi=:pCoDep \n" +
                        "        AND a.Co_Emp_Res = :pCoEmp \n" +//28/04/20 Bug Observados en MP-´V
                        "       GROUP BY a.co_dep_emi \n" + 
                        "               ,a.Co_Emp_Res\n");//28/04/20 Bug Observados en MP-´V
                if(coDependencia.equals("10619")){/*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR*/
                  sql.append("UNION\n" +
                            "SELECT 13 orden, '10' Ti_Pen, 'PARA REVISIÓN' de_pen, 'Expedientes observados pendientes de revisión' de_resumen, \n" +
                            "       count(1) Nu_Can, :pCoDep co_dep  \n" +
                            "       FROM Tdtv_Remitos r \n" +
                            "      WHERE r.Es_Doc_Emi IN ('B') \n" +
                            "        AND r.Co_Dep_Emi =:pCoDep \n" +
                            "        AND r.Co_Emp_Res = :pCoEmp \n" +
                            "        AND R.CO_GRU = '3' \n" +
                            "        GROUP BY r.Co_Dep_Emi \n" ); 
//                            "       union \n" +           
//                            "       select 0 nu_can \n" + 
//                            "       from dual) \n"); 
                }
                sql.append( "ORDER BY 1");                   
                sql.append(    " ");/*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR*/
                    
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
                    "	  FROM Tdtv_Remitos r\n" +
                    "	 WHERE r.Es_Doc_Emi IN ('7') --Listos para despacho\n" +
                    "     AND r.Ti_Emi = '01' \n" +
                    "	   and r.Co_Dep_Emi = :pCoDep \n" +
                    "	   AND (r.Co_Emp_Res = :pCoEmp OR r.co_emp_emi = :pCoEmp)\n" +
                    "	 GROUP BY r.Co_Dep_Emi\n" +
                    "	 UNION\n" +
                    "SELECT 2 ORDEN,'VB' TI_PEN,\n" +
                    "   'PARA VISTO BUENO' DE_PEN,'Documentos Administrativos listos para Visto Bueno' DE_RESUMEN,\n" +
                    "   COUNT(1) NU_CAN,:pCoDep CO_DEP\n" +
                    "   FROM TDTV_REMITOS R,TDTV_PERSONAL_VB VB\n" +
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
                    "	  FROM Tdtv_Remitos r\n" +
                    "	 WHERE r.Es_Doc_Emi IN ('5') --En proyecto\n" +
                    "	   AND r.Ti_Emi = '01'\n" +
                    "	   and r.Co_Dep_Emi = :pCoDep \n" +
                    "	   AND (r.Co_Emp_Res = :pCoEmp OR r.co_emp_emi = :pCoEmp)\n" +
                    "	 GROUP BY r.Co_Dep_Emi			 \n" +
                    "	 UNION\n" +
                    "	SELECT 4 orden,'07' Ti_Pen,\n" +
                    "	       'MUY URGENTES' De_Pen, 'Documentos pendientes de recibir MUY URGENTES' de_resumen,\n" +
                    "	       COUNT(1) Nu_Can, :pCoDep co_dep\n" +
                    "	  FROM Tdtv_Remitos  r,\n" +
                    "			       Tdtv_Destinos t\n" +
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
                    "	  FROM Tdtv_Remitos  r,\n" +
                    "	       Tdtv_Destinos t\n" +
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
                    "	  FROM Tdtv_Remitos  r,\n" +
                    "	       Tdtv_Destinos t\n" +
                    "	 WHERE r.Nu_Ann = t.Nu_Ann\n" +
                    "	   AND r.Nu_Emi = t.Nu_Emi\n" +
                    "	   AND r.Es_Doc_Emi NOT IN ('5', '7', '9') --en proyecto, anulados\n" +
                    "	   AND t.Es_Doc_Rec = '0'\n" +
                    "	   AND t.Es_Eli = '0'\n" +
                    "	   and t.Co_Dep_Des = :pCoDep \n" +
                    "	   AND t.Co_Emp_Des = :pCoEmp \n" +
                    "	 GROUP BY t.Co_Dep_Des,\n" +
                    "	          t.Co_Emp_Des");

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
        sql.append("from tdtv_bandeja_rec d,tdtr_estados e ");
        sql.append("where d.co_dep_des = :pCoDep ");
        sql.append("and d.co_emp_rec=:pCoEmp ");
        sql.append("and d.co_etiqueta_rec<>0 ");
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
