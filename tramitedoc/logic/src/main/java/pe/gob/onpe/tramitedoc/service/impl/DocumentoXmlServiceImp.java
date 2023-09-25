package pe.gob.onpe.tramitedoc.service.impl;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.onpe.tramitedoc.bean.DestinatarioDocumentoEmiBean;
import pe.gob.onpe.tramitedoc.bean.DocumentoObjBean;
import pe.gob.onpe.tramitedoc.bean.DatosPlantillaDoc;
import pe.gob.onpe.tramitedoc.bean.PlantillaDocx;
import pe.gob.onpe.tramitedoc.bean.ReferenciaBean;
import pe.gob.onpe.tramitedoc.dao.DatosPlantillaDao;
import pe.gob.onpe.tramitedoc.service.DatosPlantillaService;
import pe.gob.onpe.tramitedoc.service.DocumentoXmlService;
import pe.gob.onpe.tramitedoc.util.Constantes;
import pe.gob.onpe.tramitedoc.util.Plantillas;
import pe.gob.onpe.tramitedoc.web.util.ApplicationProperties;

@Service("documentoXmlService")
public class DocumentoXmlServiceImp implements DocumentoXmlService{

    @Autowired
    private DatosPlantillaDao datosPlantillaDao;    

    @Autowired
    private DatosPlantillaService datosPlantillaService;    
    
    @Autowired
    private ApplicationProperties applicationProperties;           

    @Override
    public DatosPlantillaDoc datosParaPlantilla(String pnuAnn, String pnuEmi){
        DatosPlantillaDoc  datosPlantilla = null;
        
        List<DestinatarioDocumentoEmiBean> listDestinos = null;        
        List<ReferenciaBean> listReferencia = null;
        
        try{
            datosPlantilla = datosPlantillaDao.getDocumentoEmitido(pnuAnn, pnuEmi);
            listDestinos     = datosPlantillaDao.getLstDestintarios(pnuAnn,pnuEmi);
            listReferencia   = datosPlantillaDao.getLstReferencia(pnuAnn,pnuEmi);
                      
            
            // Mapeando datos del documento
            if( datosPlantilla!= null && listDestinos!= null){

                datosPlantilla.setLink(datosPlantillaDao.getParametros("PAGINA_SEGUIMIENTO")); //GLOSA  
                datosPlantilla.setNombreAnio(datosPlantillaDao.getParametros("NOMBRE_ANIO"));
                //datosPlantilla.setNumeroDoc("N° "+datosPlantilla.getNuDocEmi()+"-"+datosPlantilla.getNuAnn()); // Numero de Documento
                
                /*Hermes 03/01/19 - Directiva 2019*/
                datosPlantilla.setNombreAnio1(datosPlantillaDao.getParametros("NOMBRE_ANIO_1"));
                /*Hermes 03/01/19 - Directiva 2019*/
                datosPlantilla.setSiglaDoc(generarFormatoSiglas(datosPlantilla.getDeTipoDoc(), datosPlantilla.getNuDocEmi(),datosPlantilla.getNuAnn(), applicationProperties.getSiglaInstitucion() , datosPlantilla.getDeDocSig()));
                datosPlantilla.setNumeroDoc("N° "+datosPlantilla.getNuDocEmi()+"-"+datosPlantilla.getNuAnn()+"-"+datosPlantilla.getDeDocSig()); // Numero de Documento                
                datosPlantilla.setFechaDoc(datosPlantillaDao.getDistritoLocal(datosPlantilla.getCoLocEmi())+", "+datosPlantilla.getFeEmiLargo());
                datosPlantilla.setPiePagina(datosPlantillaDao.getPiePagina(datosPlantilla.getCoEmpRes(), datosPlantilla.getCoDepEmi()));

                System.out.println("datosPlantilla.getCoTipoDoc()---> "+datosPlantilla.getCoTipoDoc());
                // Cargo de Funcionario
                if(datosPlantilla.getCoEmpEmi().equals(datosPlantilla.getCoEmpFun())){
                    if (datosPlantilla.getDeCargoFun()!=null && datosPlantilla.getDeCargoFun().length()>0 ){
                       datosPlantilla.setDeDepEmi(datosPlantilla.getDeCargoFun());
                    }
                    datosPlantilla.setDeDepEmi(datosPlantilla.getDeDepEmi()+datosPlantilla.getDeTiFun());
                    //datosPlantilla.setDeDepEmiMae(datosPlantilla.getDeDepEmiMae() +datosPlantilla.getDeTiFun());
                    datosPlantilla.setDeDepEmiMae(datosPlantilla.getDeDepEmiMae());
                    datosPlantilla.setDeCargoFunEmiMae(datosPlantilla.getDeCargoFunEmiMae()+ datosPlantilla.getDeTiFun());
                }
                
                // Verificamos Referencia
                if (listReferencia!=null){
                    ReferenciaBean datoReferencia = new ReferenciaBean();
                    String deReferencia = "";
                    for(int i=0; i<listReferencia.size(); i++) {
                        datoReferencia = listReferencia.get(i);
                        /*String vsig_inst= datoReferencia.getDeDocSig().trim().length()>0?datoReferencia.getDeDocSig().contains("/")?"-CNM-":"-CNM/":"";
                        deReferencia = deReferencia + datoReferencia.getDeTipoDoc()+" N° "+ datoReferencia.getLiNuDoc()+vsig_inst+datoReferencia.getDeDocSig()+" ("+datoReferencia.getFeEmiCorta()+")\n";*/                         
                        String siglaDoc = "";
//                        if(datoReferencia.getLiNuDoc()!=null){
//                            String[] cadenaSplit = datoReferencia.getLiNuDoc().split("-");  //si ti_emi es '01' o '05' el formato es "000003-2016-SGADYAG" en otro caso es "SGADYAG"                        
//                            if(cadenaSplit.length > 1){
//                                siglaDoc = generarFormatoSiglas(datoReferencia.getDeTipoDoc(), cadenaSplit[0], cadenaSplit[1], applicationProperties.getSiglaInstitucion() , cadenaSplit[2]);
//                            }else{
//                                siglaDoc = generarFormatoSiglas(datoReferencia.getDeTipoDoc(), "", "",applicationProperties.getSiglaInstitucion() , cadenaSplit[0]);
//                            }
//                        }
                        if(datoReferencia.getLiNuDoc()!=null){                            
                            String[] cadenaSplit = datoReferencia.getLiNuDoc().split("-");  //si ti_emi es '01' o '05' el formato es "000003-2016-SGADYAG" en otro caso es "SGADYAG"
                            /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
                            if(datoReferencia.getTiEmi().equals("01")){
                            /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/  
                            if(cadenaSplit.length > 1){
                                //jazanero
                                if(cadenaSplit.length == 2){
                                    siglaDoc = generarFormatoSiglas(datoReferencia.getDeTipoDoc(), cadenaSplit[0], cadenaSplit[1], "" , "");
                                }else{
                                    
                                    String desUnidadOrganica = cadenaSplit[2];
                                    try{
                                        if(cadenaSplit.length > 3){
                                            for (int pos = 3; pos < cadenaSplit.length; pos++) {
                                                desUnidadOrganica = desUnidadOrganica + "-" + cadenaSplit[pos];
                                            }
                                        }
                                    }catch(Exception ex){
                                        desUnidadOrganica = cadenaSplit[2];
                                    }
                                    siglaDoc = generarFormatoSiglas(datoReferencia.getDeTipoDoc(), cadenaSplit[0], cadenaSplit[1], applicationProperties.getSiglaInstitucion() , desUnidadOrganica);
                                    
                                }
                                
                                
                            }else{
                                siglaDoc = generarFormatoSiglas(datoReferencia.getDeTipoDoc(), "", "", applicationProperties.getSiglaInstitucion() , cadenaSplit[0]);
                            }
                        /*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
                        }else{
                            siglaDoc = datoReferencia.getDeTipoDoc().concat(" Nº ").concat(datoReferencia.getLiNuDoc());
                        }
                        /*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/    
                        }
                        else
                        {
                            siglaDoc = generarFormatoSiglas(datoReferencia.getDeTipoDoc(),"", "","","S/N");
                        }
                        
                        //deReferencia = deReferencia + datoReferencia.getDeTipoDoc()+" N° "+ datoReferencia.getLiNuDoc()+"/"+applicationProperties.getSiglaInstitucion()+" ("+datoReferencia.getFeEmiCorta()+")\n";
                        deReferencia = deReferencia + fixedString(siglaDoc)+" ("+datoReferencia.getFeEmiCorta()+")\n";                        
                    }   
                     datosPlantilla.setReferenciaDoc(deReferencia); //Referencia de documento
                }
                
                //jazanero
                boolean flag = false;
                if(datosPlantilla.getCoTipoDoc().equals(Constantes.OFICIO) || datosPlantilla.getCoTipoDoc().equals(Constantes.CARTA)){
                    System.out.println("A--->");
                    if(listDestinos.size()>0){
                        for(DestinatarioDocumentoEmiBean b:listDestinos){
                            if(b.getCoTipoDestino()!=null && (!b.getCoTipoDestino().equals("01"))){
                                flag = true;
                                break;
                            }
                        }                        
                    }
                }     
                datosPlantilla.setConPieDePagina(flag);
                
                flag = false;
                if(datosPlantilla.getCoTipoDoc().equals(Constantes.OFICIO) || datosPlantilla.getCoTipoDoc().equals(Constantes.CARTA)){
                    System.out.println("B--->");
                    if(listDestinos.size()>0){
                        for(DestinatarioDocumentoEmiBean b:listDestinos){
                            if(b.getCoTipoDestino()!=null && (b.getCoTipoDestino().equals(Constantes.CIUDADANO))){
                                flag = true;
                                break;
                            }
                        }                        
                    }
                }
                datosPlantilla.setConRubrica(flag);
                //jazanero
                
                // Verificamos Destinos
                DestinatarioDocumentoEmiBean datoDestino = new DestinatarioDocumentoEmiBean();
                
                if(datosPlantilla.getCoTipoDoc().equals("234") || datosPlantilla.getCoTipoDoc().equals("250") || datosPlantilla.getCoTipoDoc().equals("012")
                        || datosPlantilla.getCoTipoDoc().equals("247")){ //Tipo de Documento //Hermes 04/01/2019 Se agrego OFICIO MULTIPLE
                    System.out.println("C--->");
                    String deDestino = "";
                    String deDestino1 = "";
                    String deDestino2 = "";
                    String deDestino3 = "";
                    
                    for(int i=0; i<listDestinos.size(); i++) {
                        datoDestino = listDestinos.get(i);
                        if(i>0){
                            deDestino=deDestino+"\n\n";
                        }
                        
                        if(datosPlantilla.getCoTipoDoc().equals("005")){
                             deDestino = deDestino + datoDestino.getNombreDestinatario()+"\n" +(datoDestino.getCargo()==null?"":datoDestino.getCargo())+"\n" +(datoDestino.getEntidadPrivadaDestinatario()==null?"":datoDestino.getEntidadPrivadaDestinatario())+"\n" +datoDestino.getDireccionDestinatario();
                        }else{
                            if(datoDestino.getCoTipoDestino().equals("01")){  //Si el destino es Institucional
//                            if(datoDestino.getCoEmpleado().equals(datoDestino.getCoLocal())){
//                                if (datoDestino.getDeTramite()!=null && datoDestino.getDeTramite().length()>0){
//                                    datoDestino.setDeDependencia(datoDestino.getDeTramite());
//                                }
//                                datoDestino.setDeDependencia(datoDestino.getDeDependencia()+datoDestino.getCoTramite());
//                            }
                                //Hermes 07/01/19 - Directiva 2019
                                if(datosPlantilla.getCoTipoDoc().equals("247")|| datosPlantilla.getCoTipoDoc().equals("012")){
                                    deDestino = deDestino + datoDestino.getDeEmpleado()+"\t" +datoDestino.getDeCargoFunDestMae()+"\t" +datoDestino.getDeDepDestMae();
                                    deDestino1 = deDestino1 + datoDestino.getDeEmpleado()+"\n" +datoDestino.getDeCargoFunDestMae()+datoDestino.getCoTramite()+"\n\n";
                                    //deDestino2 = deDestino2 + datoDestino.getDeCargoFunDestMae()+"\n\n";
                                    deDestino3 = deDestino3 + datoDestino.getDeDepDestMae()+datoDestino.getCoTramite()+"\n\n";
                                }else{
                                    deDestino = deDestino + datoDestino.getDeEmpleado()+"\n" +datoDestino.getDeCargoFunDestMae()+datoDestino.getCoTramite()+"\n" +datoDestino.getDeDepDestMae()+datoDestino.getCoTramite();
                                }
                                //Hermes 07/01/19 - Directiva 2019

                            }else{ // Otros Destinos   
                                //Hermes 07/01/19 - Directiva 2019
                                if(datosPlantilla.getCoTipoDoc().equals("247")|| datosPlantilla.getCoTipoDoc().equals("012")){
                                    if(datoDestino.getCoTipoDestino().equals("03")){
                                        datoDestino.setDeCargoFunDestMae("");
                                        datoDestino.setDeDepDestMae("");
                                        datoDestino.setCargo("");
                                    }
                                    if(datoDestino.getCoTipoDestino().equals("02") || datoDestino.getCoTipoDestino().equals("04")){
                                        datoDestino.setDeEmpleado((datoDestino.getNombreDestinatario()==null?"":datoDestino.getNombreDestinatario()));
                                        datoDestino.setCargo((datoDestino.getCargo()==null?"":datoDestino.getCargo()));
                                    }
                                    deDestino1 = deDestino1 + datoDestino.getDeEmpleado()+"\n" +datoDestino.getCargo().toUpperCase()+"\n\n";
                                    deDestino2 = deDestino2 + datoDestino.getDireccionDestinatario()+"\n\n";
                                    deDestino3 = deDestino3 + datoDestino.getEntidadPrivadaDestinatario()+"\n\n";
                                }else{
                                    deDestino = deDestino + datoDestino.getDeEmpleado()+"\n";
                                }
                                //Hermes 07/01/19 - Directiva 2019
                            }
                        }      
                    }
                    //Hermes 07/01/19 - Directiva 2019
                    datosPlantilla.setDepDestino1(deDestino1);
                    datosPlantilla.setDepDestino2(deDestino2);
                    datosPlantilla.setDepDestino3(deDestino3);
                    //Hermes 07/01/19 - Directiva 2019
                    
                    datosPlantilla.setDepDestino(deDestino); // Dependencia de Destino
                    datosPlantilla.setEmpDestino(" "); // Empleado de Destino
                    datosPlantilla.setCopiaDoc(" "); // Copia de Documento

                }else{
                    System.out.println("D--->");
                    datoDestino=listDestinos.get(0);
                    if(datoDestino.getCoTipoDestino().equals("01")){  //Si el destino es Institucional
                        if(datoDestino.getCoEmpleado().equals(datoDestino.getCoLocal())){
                            if (datoDestino.getDeTramite()!=null && datoDestino.getDeTramite().length()>0){
                                datoDestino.setDeDependencia(datoDestino.getDeTramite());
                                datosPlantilla.setDeDepDestMae(datoDestino.getDeDepDestMae());
                                datosPlantilla.setDeCargoFunDestMae(datoDestino.getDeCargoFunDestMae());
                            }
                            datoDestino.setDeDependencia(datoDestino.getDeDependencia()+datoDestino.getCoTramite());
                            datosPlantilla.setDeDepDestMae(datoDestino.getDeDepDestMae()+datoDestino.getCoTramite());
                            datosPlantilla.setDeCargoFunDestMae(datoDestino.getDeCargoFunDestMae()+datoDestino.getCoTramite());
                        }else{
                           datosPlantilla.setDeDepDestMae(datoDestino.getDeDepDestMae());
                           datosPlantilla.setDeCargoFunDestMae(datoDestino.getDeCargoFunDestMae());  
                        }
                        datosPlantilla.setDepDestino(datoDestino.getDeDependencia()); // Dependencia de Destino
                        datosPlantilla.setEmpDestino(datoDestino.getDeEmpleado()); // Empleado de Destino
                        datosPlantilla.setDireccionDestinatario(" ");
                        datosPlantilla.setCargo((datoDestino.getCargo()==null?"":datoDestino.getCargo()));
                        
                    }else{
                        datosPlantilla.setDeDepDestMae((datoDestino.getEntidadPrivadaDestinatario()==null?"":datoDestino.getEntidadPrivadaDestinatario()));
                        datosPlantilla.setEmpDestino((datoDestino.getNombreDestinatario()==null?"":datoDestino.getNombreDestinatario()));
                        datosPlantilla.setDeCargoFunDestMae((datoDestino.getCargo()==null?"":datoDestino.getCargo()));
                        datosPlantilla.setDepDestino(datoDestino.getDeEmpleado()); // Dependencia de Destino
                        //datosPlantilla.setEmpDestino(" "); // Empleado de Destino
                        //datosPlantilla.setDeCargoFunDestMae(" ");
                        datosPlantilla.setDireccionDestinatario(datoDestino.getDireccionDestinatario());
                        datosPlantilla.setCargo((datoDestino.getCargo()==null?"":datoDestino.getCargo()));
                        if(datoDestino.getCoTipoDestino().equals("02") || datoDestino.getCoTipoDestino().equals("04")){
                            datosPlantilla.setEntidadPrivadaDestinatario(datoDestino.getEntidadPrivadaDestinatario());
                        }else{
                            datosPlantilla.setEntidadPrivadaDestinatario("");
                        }
                    }
                    
                    datosPlantilla.setNombreDestinatario(datoDestino.getNombreDestinatario());
                    if(listDestinos.size()>1){
                        String deDestino = "";
                        for(int i=1; i<listDestinos.size(); i++) {
                            datoDestino = listDestinos.get(i);
                            deDestino = deDestino + datoDestino.getDeDependencia()+ "\n";
                        }   
                        datosPlantilla.setCopiaDoc(deDestino); 
                    }else{
                        datosPlantilla.setCopiaDoc(" "); 
                    }
                }                
            }else{
                datosPlantilla = null;
            }            
        }catch(Exception ex){
            ex.printStackTrace();
        }        
        
        return(datosPlantilla);        
    }

    
    @Override
    public DocumentoObjBean crearDocx(String pnuAnn, String pnuEmi){
        DocumentoObjBean docObjBean = null;

        long startTime = System.currentTimeMillis();
        PlantillaDocx plantillaDocx = null;
                
        DatosPlantillaDoc  datosPlantilla=datosParaPlantilla(pnuAnn,pnuEmi);
        //Hermes 03/01/19 - Directiva 2019. Se comento para no validar plantilla. Cada organo tendra su propia plantilla
        if(!datosPlantilla.isConRubrica()){
            datosPlantilla.setCoDepEmi("00000");
        }
        plantillaDocx = Plantillas.getInstancia().getPlantilla(datosPlantilla.getCoTipoDoc(), datosPlantilla.getCoDepEmi());
        
        
        if(plantillaDocx != null){
            try
            {
                IXDocReport report = plantillaDocx.getTemplate();

                if(report!=null){
                    docObjBean = new DocumentoObjBean();
                    docObjBean.setNombreArchivo(plantillaDocx.getNomArchivo());

                    IContext context = report.createContext();

                    context.put("NOMBRE_ANIO",datosPlantilla.getNombreAnio());
                    context.put("CORRELATIVO",datosPlantilla.getNuCorEmi());
                    context.put("TIPO_DOC",datosPlantilla.getDeTipoDoc());
                    context.put("SIGLA_DOC",datosPlantilla.getSiglaDoc());
                    context.put("NUMERO_DOC",datosPlantilla.getNumeroDoc());
//                    context.put("NUMERO_DOC","Informe de Prueba");
                    //context.put("SIGLA_DOC",datosPlantilla.getDeDocSig());
                    context.put("FECHA_DOC",datosPlantilla.getFechaDoc());
                    context.put("UUOO_EMITE",datosPlantilla.getDeDepEmi());
                    context.put("EMPLEADO_EMITE",datosPlantilla.getDeEmpEmi());
                    context.put("REFERENCIA",datosPlantilla.getReferenciaDoc());
                    context.put("ASUNTO",datosPlantilla.getDeAsunto());
                    context.put("NU_DNI",datosPlantilla.getNuDniDes());
                    context.put("PIE_PAGINA",datosPlantilla.getPiePagina());
                    context.put("UUOO_DESTINO",datosPlantilla.getDepDestino());
                    context.put("EMPLEADO_DESTINO",datosPlantilla.getEmpDestino());
                    context.put("COPIA",datosPlantilla.getCopiaDoc());
                    context.put("INICIALES_EMP",datosPlantilla.getDeIniciales());
                    context.put("DEPENDENCIA_EMITE",datosPlantilla.getDeDepEmiMae());
                    context.put("DEPENDENCIA_DESTINO",datosPlantilla.getDeDepDestMae());
                    context.put("CARGO_EMP_EMITE",datosPlantilla.getDeCargoFunEmiMae());
                    context.put("CARGO_EMP_DESTINO",(datosPlantilla.getDeCargoFunDestMae()==null? "":datosPlantilla.getDeCargoFunDestMae()));
                    context.put("NOMBRE_DESTINATARIO",(datosPlantilla.getNombreDestinatario()==null? "":datosPlantilla.getNombreDestinatario()));
                    context.put("DIRECCION_DESTINATARIO",(datosPlantilla.getDireccionDestinatario()==null? "": datosPlantilla.getDireccionDestinatario()));
                    context.put("ENTIDAD_PRIVADA_DESTINATARIO",(datosPlantilla.getEntidadPrivadaDestinatario()==null?"":datosPlantilla.getEntidadPrivadaDestinatario()));
                    context.put("NRO_EXPEDIENTE",(datosPlantilla.getNroExpediente()==null?"":datosPlantilla.getNroExpediente()));
                    context.put("CARGO",(datosPlantilla.getCargo()==null?"":datosPlantilla.getCargo()));
                    
                    //Hermes 03/01/19 - Directiva 2019
                    context.put("DEPENDENCIA_PADRE", datosPlantilla.getDeDepPadre());
                    context.put("NOMBRE_ANIO_1", datosPlantilla.getNombreAnio1());
                    
                    context.put("UUOO_DESTINO1",datosPlantilla.getDepDestino1());
                    context.put("UUOO_DESTINO2",datosPlantilla.getDepDestino2());
                    context.put("UUOO_DESTINO3",datosPlantilla.getDepDestino3());
                    //Hermes 03/01/19 - Directiva 2019       
                    
                    //jazanero
                    context.put("LINK_GLOSA",(datosPlantilla.getLink()==null? "":datosPlantilla.getLink()));
                    context.put("CLAVE_GLOSA",(datosPlantilla.getClave()==null? "":datosPlantilla.getClave()));
                    //jazanero

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    report.process(context, baos ); 
                    docObjBean.setDocumento(baos.toByteArray());
                    baos.flush();
                    baos.close();
                }else{
                  docObjBean = null;  
                }

            }
            catch ( Throwable e )
            {
                e.printStackTrace();
            }        
        }else{
           //Plantilla no existe 
        }
        
        //System.out.println( "Generate with " + ( System.currentTimeMillis() - startTime ) + " ms." );
        
        return(docObjBean);
    }
    
    public DocumentoObjBean crearPdfx(String pnuAnn, String pnuEmi, String ptiCap){
        DocumentoObjBean docObjBean = null;
        long startTime = System.currentTimeMillis();
        try
        {

            InputStream in = new FileInputStream(new File("C:\\TDOCUMENTOS\\PLANTILLAS_DOCX\\MEMORANDO3.docx"));
            //InputStream in = DocxNativeLineBreakAndTabWithFreemarker.class.getResourceAsStream( "C:\\TDOCUMENTOS\\PLANTILLAS_DOCX\\MEMORANDO2.docx" );
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker );

            IContext context = report.createContext();

            context.put("TIPO_DOC", "MEMORANDO");
            context.put("NUMERO_DOC", "Nº    -2013-WCC-SG");
            context.put("ASUNTO", "Asunto de Docx");
            context.put("PIE_PAG", "Jr Washington ");

            OutputStream out = new FileOutputStream( new File( "C:\\TDOCUMENTOS\\Prueba2.docx" ) );
            report.process( context, out );            
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
        }
        return(docObjBean);
    }
    
    
    public String generarFormatoSiglas(String tipoDoc, String nuDoc, String anio, String institucion, String uuoo){
        tipoDoc = (tipoDoc==null?" ":tipoDoc);
        nuDoc = (nuDoc==null?" ":nuDoc);
        anio = (anio==null?" ":anio);
        institucion = (institucion==null?" ":institucion);
        uuoo = (uuoo==null?" ":uuoo);
     
        String cadenaSiglas = applicationProperties.getFormatoSiglas();
        cadenaSiglas=cadenaSiglas.replace("{TIPODOC}", tipoDoc);  //Reemplazamos el tipo de documento
        cadenaSiglas=cadenaSiglas.replace("{NUDOC}", nuDoc);  //Reemplazamos el tipo de documento
        cadenaSiglas=cadenaSiglas.replace("{ANIO}", anio);  //Reemplazamos el tipo de documento
        cadenaSiglas=cadenaSiglas.replace("{INS}", institucion);  //Reemplazamos el tipo de documento
        cadenaSiglas=cadenaSiglas.replace("{UUOO}", uuoo);  //Reemplazamos el tipo de documento
        cadenaSiglas=cadenaSiglas.replace("Â", "");   //Reemplazamos el caracter Â
        
        return cadenaSiglas;
    }
    
    //jazanero
    public String fixedString(String cadena){
        
        cadena = cadena.trim();
        int pos = 0;

        if(cadena!=null && cadena.length()>0){
            pos = cadena.length();
            while(pos>0){
                if(cadena.charAt(pos-1)=='-'){
                    cadena = cadena.substring(0, pos-1).trim(); 
                    pos--;
                }else{
                    break;
                }
            }
        }        
        
        return cadena;
    }

    @Override
    public DatosPlantillaDoc datosParaPlantillaGlosa(String pnuAnn, String pnuEmi) {

        DatosPlantillaDoc  datosPlantilla = null;        
        List<DestinatarioDocumentoEmiBean> listDestinos = null;    
        
        try{
            datosPlantilla = datosPlantillaDao.getDocumentoEmitido(pnuAnn, pnuEmi);
            listDestinos     = datosPlantillaDao.getLstDestintarios(pnuAnn,pnuEmi);
                                    
            
            // Mapeando datos del documento
            if( datosPlantilla!= null && listDestinos!= null){
                datosPlantilla.setLink(datosPlantillaDao.getParametros("PAGINA_SEGUIMIENTO")); //GLOSA                                    
                //jazanero
                boolean flag = false;
                /*[HPB] Inicio 30/03/23 CLS-087-2022 */
                //if(datosPlantilla.getCoTipoDoc().equals("243") || datosPlantilla.getCoTipoDoc().equals("246")
                //        || datosPlantilla.getCoTipoDoc().equals("247")|| datosPlantilla.getCoTipoDoc().equals("012")){//Hermes 11/01/2019 Directiva 2019
                if(datosPlantilla.getCoTipoDoc().equals("011") || datosPlantilla.getCoTipoDoc().equals("012") || datosPlantilla.getCoTipoDoc().equals("247")|| datosPlantilla.getCoTipoDoc().equals("233")){
                /*[HPB] Fin 30/03/23 CLS-087-2022 */    
                    if(listDestinos.size()>0){
                        for(DestinatarioDocumentoEmiBean b:listDestinos){
                            if(b.getCoTipoDestino()!=null && (!b.getCoTipoDestino().equals("01"))){
                                flag = true;
                                break;
                            }
                        }                        
                    }
                }     
                datosPlantilla.setConPieDePagina(flag);
                //jazanero
                
                //YPA SIS
                /*[HPB] Inicio 30/03/23 CLS-087-2022 */
                //datosPlantilla.setConPieDePagina(false);
                /*[HPB] Fin 30/03/23 CLS-087-2022 */
                               
            }else{
                datosPlantilla = null;
            }            
        }catch(Exception ex){
            ex.printStackTrace();
        }        
        
        return(datosPlantilla);  
    }

    @Override
    public DocumentoObjBean crearDocxProyecto(String pnuAnn, String pnuEmi) {
        DocumentoObjBean docObjBean = null;

        long startTime = System.currentTimeMillis();
        PlantillaDocx plantillaDocx = null;
                
        DatosPlantillaDoc  datosPlantilla=datosParaPlantillaProyecto(pnuAnn,pnuEmi);
        //Hermes 03/01/19 - Directiva 2019. Se comento para no validar plantilla. Cada organo tendra su propia plantilla
        if(!datosPlantilla.isConRubrica()){
            datosPlantilla.setCoDepEmi("00000");
        }
        plantillaDocx = Plantillas.getInstancia().getPlantilla(datosPlantilla.getCoTipoDoc(), datosPlantilla.getCoDepEmi());
        
        
        if(plantillaDocx != null){
            try
            {
                IXDocReport report = plantillaDocx.getTemplate();

                if(report!=null){
                    docObjBean = new DocumentoObjBean();
                    docObjBean.setNombreArchivo(plantillaDocx.getNomArchivo());

                    IContext context = report.createContext();

                    context.put("NOMBRE_ANIO",datosPlantilla.getNombreAnio()==null? "":datosPlantilla.getNombreAnio());
                    context.put("CORRELATIVO",datosPlantilla.getNuCorEmi()==null? "":datosPlantilla.getNuCorEmi());
                    context.put("TIPO_DOC",datosPlantilla.getDeTipoDoc()==null? "":datosPlantilla.getDeTipoDoc());
                    context.put("SIGLA_DOC",datosPlantilla.getSiglaDoc()==null? "":datosPlantilla.getSiglaDoc());
                    context.put("NUMERO_DOC",datosPlantilla.getNumeroDoc()==null? "":datosPlantilla.getNumeroDoc());
//                    context.put("NUMERO_DOC","Informe de Prueba");
                    //context.put("SIGLA_DOC",datosPlantilla.getDeDocSig());
                    context.put("FECHA_DOC",datosPlantilla.getFechaDoc()==null? "":datosPlantilla.getFechaDoc());
                    context.put("UUOO_EMITE",datosPlantilla.getDeDepEmi()==null? "":datosPlantilla.getDeDepEmi());
                    context.put("EMPLEADO_EMITE",datosPlantilla.getDeEmpEmi()==null? "":datosPlantilla.getDeEmpEmi());
                    context.put("REFERENCIA",datosPlantilla.getReferenciaDoc()==null? "":datosPlantilla.getReferenciaDoc());
                    context.put("ASUNTO",datosPlantilla.getDeAsunto()==null? "":datosPlantilla.getDeAsunto());
                    context.put("NU_DNI",datosPlantilla.getNuDniDes()==null? "":datosPlantilla.getNuDniDes());
                    context.put("PIE_PAGINA",datosPlantilla.getPiePagina()==null? "":datosPlantilla.getPiePagina());
                    context.put("UUOO_DESTINO",datosPlantilla.getDepDestino()==null? "":datosPlantilla.getDepDestino());
                    context.put("EMPLEADO_DESTINO",datosPlantilla.getEmpDestino()==null? "":datosPlantilla.getEmpDestino());
                    context.put("COPIA",datosPlantilla.getCopiaDoc()==null? "":datosPlantilla.getCopiaDoc());
                    context.put("INICIALES_EMP",datosPlantilla.getDeIniciales()==null? "":datosPlantilla.getDeIniciales());
                    context.put("DEPENDENCIA_EMITE",datosPlantilla.getDeDepEmiMae()==null? "":datosPlantilla.getDeDepEmiMae());
                    context.put("DEPENDENCIA_DESTINO",datosPlantilla.getDeDepDestMae()==null? "":datosPlantilla.getDeDepDestMae());
                    context.put("CARGO_EMP_EMITE",datosPlantilla.getDeCargoFunEmiMae()==null? "":datosPlantilla.getDeCargoFunEmiMae());
                    context.put("CARGO_EMP_DESTINO",(datosPlantilla.getDeCargoFunDestMae()==null? "":datosPlantilla.getDeCargoFunDestMae()));
                    context.put("NOMBRE_DESTINATARIO",(datosPlantilla.getNombreDestinatario()==null? "":datosPlantilla.getNombreDestinatario()));
                    context.put("DIRECCION_DESTINATARIO",(datosPlantilla.getDireccionDestinatario()==null? "": datosPlantilla.getDireccionDestinatario()));
                    context.put("ENTIDAD_PRIVADA_DESTINATARIO",(datosPlantilla.getEntidadPrivadaDestinatario()==null?"":datosPlantilla.getEntidadPrivadaDestinatario()));
                    context.put("NRO_EXPEDIENTE",(datosPlantilla.getNroExpediente()==null?"":datosPlantilla.getNroExpediente()));
                    context.put("CARGO",(datosPlantilla.getCargo()==null?"":datosPlantilla.getCargo()));
                                              
                    //Hermes 03/01/19 - Directiva 2019
                    context.put("DEPENDENCIA_PADRE", datosPlantilla.getDeDepPadre());
                    context.put("NOMBRE_ANIO_1", datosPlantilla.getNombreAnio1());
                    
                    context.put("UUOO_DESTINO1",datosPlantilla.getDepDestino1());
                    context.put("UUOO_DESTINO2",datosPlantilla.getDepDestino2());
                    context.put("UUOO_DESTINO3",datosPlantilla.getDepDestino3());                    
                    //Hermes 03/01/19 - Directiva 2019       
                    
                    //jazanero
                    context.put("LINK_GLOSA",(datosPlantilla.getLink()==null? "":datosPlantilla.getLink()));
                    context.put("CLAVE_GLOSA",(datosPlantilla.getClave()==null? "":datosPlantilla.getClave()));
                    //jazanero

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    report.process(context, baos ); 
                    docObjBean.setDocumento(baos.toByteArray());
                    baos.flush();
                    baos.close();
                }else{
                  docObjBean = null;  
                }

            }
            catch ( Throwable e )
            {
                e.printStackTrace();
            }        
        }else{
           //Plantilla no existe 
        }
        
        //System.out.println( "Generate with " + ( System.currentTimeMillis() - startTime ) + " ms." );
        
        return(docObjBean);
    }

    @Override
    public DatosPlantillaDoc datosParaPlantillaProyecto(String pnuAnn, String pnuEmi) {
        DatosPlantillaDoc  datosPlantilla = null;
        
        List<DestinatarioDocumentoEmiBean> listDestinos = null;        
        
        try{
            datosPlantilla = datosPlantillaDao.getDocumentoEmitidoProyecto(pnuAnn, pnuEmi);
            listDestinos = datosPlantillaDao.getLstDestintariosProyecto(pnuAnn,pnuEmi);
                                  
            
            // Mapeando datos del documento
            if( datosPlantilla!= null && listDestinos!= null){

                datosPlantilla.setNombreAnio(datosPlantillaDao.getParametros("NOMBRE_ANIO"));
                //datosPlantilla.setNumeroDoc("N° "+datosPlantilla.getNuDocEmi()+"-"+datosPlantilla.getNuAnn()); // Numero de Documento
                
                /*Hermes 03/01/19 - Directiva 2019*/
                datosPlantilla.setNombreAnio1(datosPlantillaDao.getParametros("NOMBRE_ANIO_1"));
                /*Hermes 03/01/19 - Directiva 2019*/
                
                datosPlantilla.setSiglaDoc(generarFormatoSiglas(datosPlantilla.getDeTipoDoc(), datosPlantilla.getNuDocEmi(),datosPlantilla.getNuAnn(), applicationProperties.getSiglaInstitucion() , datosPlantilla.getDeDocSig()));
                datosPlantilla.setNumeroDoc("N° "+datosPlantilla.getNuDocEmi()+"-"+datosPlantilla.getNuAnn()+"-"+datosPlantilla.getDeDocSig()); // Numero de Documento                
                datosPlantilla.setFechaDoc(datosPlantillaDao.getDistritoLocal(datosPlantilla.getCoLocEmi())+", "+datosPlantilla.getFeEmiLargo());
                datosPlantilla.setPiePagina(datosPlantillaDao.getPiePagina(datosPlantilla.getCoEmpRes(), datosPlantilla.getCoDepEmi()));

                
                // Cargo de Funcionario
                if(datosPlantilla.getCoEmpEmi().equals(datosPlantilla.getCoEmpFun())){
                    if (datosPlantilla.getDeCargoFun()!=null && datosPlantilla.getDeCargoFun().length()>0 ){
                       datosPlantilla.setDeDepEmi(datosPlantilla.getDeCargoFun());
                    }
                    datosPlantilla.setDeDepEmi(datosPlantilla.getDeDepEmi()+datosPlantilla.getDeTiFun());
                    //datosPlantilla.setDeDepEmiMae(datosPlantilla.getDeDepEmiMae() +datosPlantilla.getDeTiFun());
                    datosPlantilla.setDeDepEmiMae(datosPlantilla.getDeDepEmiMae());
                    datosPlantilla.setDeCargoFunEmiMae(datosPlantilla.getDeCargoFunEmiMae()+ datosPlantilla.getDeTiFun());
                }
                
                                
                //jazanero
                boolean flag = false;                    
                datosPlantilla.setConPieDePagina(flag);
                
                flag = false;
                if(datosPlantilla.getCoTipoDoc().equals(Constantes.OFICIO) || datosPlantilla.getCoTipoDoc().equals(Constantes.CARTA)){
                    if(listDestinos.size()>0){
                        for(DestinatarioDocumentoEmiBean b:listDestinos){
                            if(b.getCoTipoDestino()!=null && (b.getCoTipoDestino().equals(Constantes.CIUDADANO))){
                                flag = true;
                                break;
                            }
                        }                        
                    }
                }
                datosPlantilla.setConRubrica(flag);
                //jazanero
                
                // Verificamos Destinos
                DestinatarioDocumentoEmiBean datoDestino = new DestinatarioDocumentoEmiBean();
                
                if(datosPlantilla.getCoTipoDoc().equals("234") || datosPlantilla.getCoTipoDoc().equals("250") || datosPlantilla.getCoTipoDoc().equals("012") || datosPlantilla.getCoTipoDoc().equals("005")
                        || datosPlantilla.getCoTipoDoc().equals("247")){ //Tipo de Documento //Hermes 04/01/2019 Se agrego OFICIO MULTIPLE
                    String deDestino = "";
                    String deDestino1 = "";
                    String deDestino2 = "";
                    String deDestino3 = "";
                    
                    for(int i=0; i<listDestinos.size(); i++) {
                        datoDestino = listDestinos.get(i);
                        if(i>0){
                            deDestino=deDestino+"\n\n";
                        }
                        
                        if(datosPlantilla.getCoTipoDoc().equals("012")|| datosPlantilla.getCoTipoDoc().equals("005")){
                             deDestino = deDestino + datoDestino.getNombreDestinatario()+"\n" +(datoDestino.getCargo()==null?"":datoDestino.getCargo())+"\n" +(datoDestino.getEntidadPrivadaDestinatario()==null?"":datoDestino.getEntidadPrivadaDestinatario())+"\n" +datoDestino.getDireccionDestinatario();
                        }else{
                            if(datoDestino.getCoTipoDestino().equals("01")){  //Si el destino es Institucional
//                            if(datoDestino.getCoEmpleado().equals(datoDestino.getCoLocal())){
//                                if (datoDestino.getDeTramite()!=null && datoDestino.getDeTramite().length()>0){
//                                    datoDestino.setDeDependencia(datoDestino.getDeTramite());
//                                }
//                                datoDestino.setDeDependencia(datoDestino.getDeDependencia()+datoDestino.getCoTramite());
//                            }
                                //Hermes 07/01/19 - Directiva 2019
                                if(datosPlantilla.getCoTipoDoc().equals("247")|| datosPlantilla.getCoTipoDoc().equals("012")){
                                    //deDestino = deDestino + datoDestino.getDeEmpleado()+"\t" +datoDestino.getDeCargoFunDestMae()+"\t" +datoDestino.getDeDepDestMae();
                                    deDestino1 = deDestino1 + datoDestino.getDeEmpleado()+"\n" +datoDestino.getDeCargoFunDestMae()+"\n\n";
                                    //deDestino2 = deDestino2 + datoDestino.getDeCargoFunDestMae()+"\n\n";
                                    deDestino3 = deDestino3 + datoDestino.getDeDepDestMae()+"\n\n";
                                }else{
                                    deDestino = deDestino + datoDestino.getDeEmpleado()+"\n" +datoDestino.getDeCargoFunDestMae()+"\n" +datoDestino.getDeDepDestMae();
                                }                                
                                //Hermes 07/01/19 - Directiva 2019
                            }else{ // Otros Destinos     
                                //Hermes 07/01/19 - Directiva 2019
                                if(datosPlantilla.getCoTipoDoc().equals("247")|| datosPlantilla.getCoTipoDoc().equals("012")){
                                    if(datoDestino.getCoTipoDestino().equals("03")){
                                        datoDestino.setDeCargoFunDestMae("");
                                        datoDestino.setDeDepDestMae("");
                                        datoDestino.setCargo("");
                                    }
                                    if(datoDestino.getCoTipoDestino().equals("02") || datoDestino.getCoTipoDestino().equals("04")){
                                        datoDestino.setDeEmpleado((datoDestino.getNombreDestinatario()==null?"":datoDestino.getNombreDestinatario()));
                                        datoDestino.setCargo((datoDestino.getCargo()==null?"":datoDestino.getCargo()));                                        
                                    }
                                    deDestino1 = deDestino1 + datoDestino.getDeEmpleado()+"\n" +datoDestino.getCargo().toUpperCase()+"\n\n";
                                    deDestino2 = deDestino2 + datoDestino.getDireccionDestinatario()+"\n\n";
                                    deDestino3 = deDestino3 + datoDestino.getEntidadPrivadaDestinatario()+"\n\n";
                                }else{
                                    deDestino = deDestino + datoDestino.getDeEmpleado()+"\n";                                    
                                }  
                                //Hermes 07/01/19 - Directiva 2019
                            }
                        }      
                    }
                    //Hermes 07/01/19 - Directiva 2019
                    datosPlantilla.setDepDestino1(deDestino1);
                    datosPlantilla.setDepDestino2(deDestino2);
                    datosPlantilla.setDepDestino3(deDestino3);
                    //Hermes 07/01/19 - Directiva 2019
                    datosPlantilla.setDepDestino(deDestino); // Dependencia de Destino
                    datosPlantilla.setEmpDestino(" "); // Empleado de Destino
                    datosPlantilla.setCopiaDoc(" "); // Copia de Documento

                }else{
                    datoDestino=listDestinos.get(0);
                    if(datoDestino.getCoTipoDestino().equals("01")){  //Si el destino es Institucional
                        if(datoDestino.getCoEmpleado().equals(datoDestino.getCoLocal())){
                            if (datoDestino.getDeTramite()!=null && datoDestino.getDeTramite().length()>0){
                                datoDestino.setDeDependencia(datoDestino.getDeTramite());
                                datosPlantilla.setDeDepDestMae(datoDestino.getDeDepDestMae());
                                datosPlantilla.setDeCargoFunDestMae(datoDestino.getDeCargoFunDestMae());
                            }
                            datoDestino.setDeDependencia(datoDestino.getDeDependencia()+datoDestino.getCoTramite());
                            datosPlantilla.setDeDepDestMae(datoDestino.getDeDepDestMae()+datoDestino.getCoTramite());
                            datosPlantilla.setDeCargoFunDestMae(datoDestino.getDeCargoFunDestMae()+datoDestino.getCoTramite());
                        }else{
                           datosPlantilla.setDeDepDestMae(datoDestino.getDeDepDestMae());
                           datosPlantilla.setDeCargoFunDestMae(datoDestino.getDeCargoFunDestMae());  
                        }
                        datosPlantilla.setDepDestino(datoDestino.getDeDependencia()); // Dependencia de Destino
                        datosPlantilla.setEmpDestino(datoDestino.getDeEmpleado()); // Empleado de Destino
                        datosPlantilla.setDireccionDestinatario(" ");
                        datosPlantilla.setCargo((datoDestino.getCargo()==null?"":datoDestino.getCargo()));
                        
                    }else{
                        datosPlantilla.setDepDestino(datoDestino.getDeEmpleado()); // Dependencia de Destino
                        datosPlantilla.setEmpDestino(" "); // Empleado de Destino
                        datosPlantilla.setDeCargoFunDestMae(" ");
                        datosPlantilla.setDireccionDestinatario(datoDestino.getDireccionDestinatario());
                        datosPlantilla.setCargo((datoDestino.getCargo()==null?"":datoDestino.getCargo()));
                        if(datoDestino.getCoTipoDestino().equals("02") || datoDestino.getCoTipoDestino().equals("04")){
                            datosPlantilla.setEntidadPrivadaDestinatario(datoDestino.getEntidadPrivadaDestinatario());
                        }else{
                            datosPlantilla.setEntidadPrivadaDestinatario("");
                        }
                    }
                    
                    datosPlantilla.setNombreDestinatario(datoDestino.getNombreDestinatario());
                    if(listDestinos.size()>1){
                        String deDestino = "";
                        for(int i=1; i<listDestinos.size(); i++) {
                            datoDestino = listDestinos.get(i);
                            deDestino = deDestino + datoDestino.getDeDependencia()+ "\n";
                        }   
                        datosPlantilla.setCopiaDoc(deDestino); 
                    }else{
                        datosPlantilla.setCopiaDoc(" "); 
                    }
                }                
            }else{
                datosPlantilla = null;
            }            
        }catch(Exception ex){
            ex.printStackTrace();
        }        
        
        return(datosPlantilla); 
    }
}
