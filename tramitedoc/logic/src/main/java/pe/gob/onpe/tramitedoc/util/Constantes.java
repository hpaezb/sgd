package pe.gob.onpe.tramitedoc.util;

/**
 * Created by IntelliJ IDEA.
 * User: crosales
 * Date: 09/02/12
 * Time: 08:08 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Constantes {
    public static String USER_COOKIE_ID = "Usuario";
    public static String USER_ACCESS_COOKIE_ID = "Acceso";
    public static String SESSION_SATTUS = "State";
    public static int TI_IDENTIFICACION = 0;

    public static String PAGE_NUMBER = "pg";
    public static int ROWS_X_PAGE = 4;

    public static String ETAPA_CREACION = "0";
    public static String ETAPA_PDF_CARGADO = "2";
    public static String ETAPA_PARA_CONTROL_CALIDAD="3";
    public static String ETAPA_ANULADO="9";
    public static String ETAPA_PUBLICADO="1";
    public static String ETAPA_RECHAZADO="4";


    public static String PDF_RESOLUCION = "R";
    public static String PDF_ANEXO = "A";
    public static String PDF_ANTECEDENTE = "T";
    
    //para el motor de base de datos
    int DB_MYSQL = 1;
    int DB_ORACLE = 2;
    int DB_POSTGRES = 3;
    int DB_MSSQL = 4;    
    
    int NROREGISTROSXPAGINA = 10;    
    
    //Para la paginacion
    String PAGINACION_ATRAS = "P_ATRAS";
    String PAGINACION_SIGUIENTE = "P_SIGUIENTE";
    String PAGINACION_ULTIMO = "P_ULTIMO";
    String PAGINACION_PRIMERO = "P_PRIMERO";
    String PAGINACION_ACTUALIZAR = "P_ACTUALIZAR";
    String PAGINACION_BUSCAR = "P_BUSCAR";   
    
    public static final String CO_TRAMITE_ORIGINAL    = "0";
    public static final String DE_TRAMITE_ORIGINAL    = "ORIGINAL";
    public static final String CO_TRAMITE_COPIA    = "1";
    public static final String DE_TRAMITE_COPIA    = "COPIA";        
    public static final String CO_TRAMITE_ATENDER    = "4";
    public static final String DE_TRAMITE_ATENDER    = "ATENDER";
    public static final String CO_TRAMITE_FINES    = "F";
    public static final String DE_TRAMITE_FINES    = "PARA CONOCIMIENTO Y FINES";   
    
    //public static final String GLOSA = "<p style='text-align: justify; font-size:9; font-style:italic; background-color: white;'>G:: Esta es una copia auténtica imprimible de un documento electrónico archivado en el Despacho Presidencial, aplicando lo dispuesto por el Art. 25 de D.S. 070-2013-PCM y la Tercera Disposición Complementaria Final del D.S. 026- 2016-PCM. Su autenticidad e integridad pueden ser contrastadas a través de la siguiente dirección web: <b>“{LINK}”</b> e ingresando la siguiente clave: <b>{CLAVE}</b></p>";
    public static final String GLOSA = "<p style='padding-left: 110px; text-align: justify; font-size:9; font-style:italic; background-color: white;'>G:: Esta es una copia auténtica imprimible de un documento electrónico archivado en el Despacho Presidencial, aplicando lo dispuesto por el Art. 25 de D.S. 070-2013-PCM y la Tercera Disposición Complementaria Final del D.S. 026- 2016-PCM. Su autenticidad e integridad pueden ser contrastadas a través de la siguiente dirección web: <b>{LINK}</b> e ingresando la siguiente clave: <b>{CLAVE}</b></p>";
    //public static final String GLOSAQR = "<table><tr><td width='70%'><p style='text-align: justify; font-size:7; font-style:italic;'>Esta es una copia auténtica imprimible de un documento electrónico archivado en el Despacho Presidencial, aplicando lo dispuesto por el Art. 25 de D.S. 070-2013-PCM y la Tercera Disposición Complementaria Final del D.S. 026- 2016-PCM. Su autenticidad e integridad pueden ser contrastadas a través de la siguiente dirección web: <b>“{LINK}”</b> e ingresando la siguiente clave: <b>{CLAVE}</b></p></td><td width='30%'><img src='data:image/png;base64,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' style='height:90px; width:90px;' /></td></tr></table>";
    public static final String GLOSAQR = "<p style='padding-left: 110px; text-align: justify; font-size:9; font-style:italic; background-color: white;'>Esta es una copia auténtica imprimible de un documento electrónico archivado en el Despacho Presidencial, aplicando lo dispuesto por el Art. 25 de D.S. 070-2013-PCM y la Tercera Disposición Complementaria Final del D.S. 026- 2016-PCM. Su autenticidad e integridad pueden ser contrastadas a través de la siguiente dirección web: <b>{LINK}</b> e ingresando la siguiente clave: <b>{CLAVE}</b></p>";
    public static final String GLOSA_ANTIGUA = "<p style='text-align: justify; font-size:4; font-style:italic; background-color: white;'>Esta es una copia auténtica imprimible de un documento electrónico archivado en el Despacho Presidencial, aplicando lo dispuesto por el Art. 25 de D.S. 070-2013-PCM y la Tercera Disposición Complementaria Final del D.S. 026- 2016-PCM. Su autenticidad e integridad pueden ser contrastadas a través de la siguiente dirección web:</p><p style='text-align: justify; font-size:4; font-style:italic; background-color: white;'><b>{LINK}</b> e ingresando la siguiente clave: <b>{CLAVE}</b></p>";
    
    public static final String CARTA = "246";
    public static final String OFICIO = "243";
    public static final String PROYECTO_DOCUMENTO = "332";
    public static final String HOJA_DE_ENVIO = "304";
    public static final String PROVEIDO = "232";
    public static final String ORDEN_TRABAJO = "340";/*[HPB] 02/02/21 Orden de trabajo*/
    
    public static final String CIUDADANO = "03";
    
}
