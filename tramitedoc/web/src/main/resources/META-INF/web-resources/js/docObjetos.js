var paramConfAnexosDetalle = {
    "bPaginate": false,
    "bFilter": false,
    "bSort": true,
    "bInfo": true,
    "bAutoWidth": false,
    "sScrollY": "140px",
    "bScrollCollapse": false,
    "oLanguage": {
        "sZeroRecords": "No se encuentran registros.",
        "sInfo": "Registros: _TOTAL_ ",
        "sInfoEmpty": ""
    },
    "aoColumns": [
        {"bSortable": true},
        {"bSortable": true},
        {"bSortable": false}
    ]
};

/*Hermes 30/01/2019 - Requerimiento Mensajeria: Acta 0005-2019    */
var paramConfAnexosDetalleAdicional = {
    "bPaginate": false,
    "bFilter": false,
    "bSort": true,
    "bInfo": true,
    "bAutoWidth": false,
    //"sScrollY": "100px",
    "bScrollCollapse": false,
    "oLanguage": {
        "sZeroRecords": "No tiene cargo adjuntado.",
        "sInfo": "Registros: _TOTAL_ ",
        "sInfoEmpty": ""
    },
    "aoColumns": [
        {"bSortable": true},
        {"bSortable": true},
        {"bSortable": true},
        {"bSortable": true},        
        {"bSortable": false}
    ]
};
/*Hermes 30/01/2019 - Requerimiento Mensajeria: Acta 0005-2019    */

function fn_verDocumentoLista(pOpcion) {
    var pnuAnn = jQuery('#txtpnuAnn').val();
    var pnuEmi = jQuery('#txtpnuEmi').val();
    var ptiOpe = "0";
    //var pOpcion = allTrim($("#txtCodOpcion").val());//Hermes 28/05/2019
    if (pnuAnn) {
        fn_verDocumentosObj(pnuAnn, pnuEmi, ptiOpe, pOpcion);
    } else {
        //alert("Seleccione una fila de la lista");
        alert_Info("Emisión :", "Seleccione una fila de la lista");
    }
}

function fn_firmarDocApplet(urlDoc, rutaDoc, tipoFirma) {
    var retval = "";
    var appletObj = jQuery('#firmarDocumento');
    try {
        retval = appletObj[0].ejecutaFirma(urlDoc, rutaDoc, tipoFirma);
    } catch (ex) {
       bootbox.alert("Fallo en abrir el documento");
       return;
    }
    return retval;
}

function fn_abrirDocApplet(purlDoc, prutaDoc) {
    var retval = "";
    var appletObj = jQuery('#firmarDocumento');
    var urlDoc = purlDoc;
    var rutaDoc = prutaDoc;
    try {
        retval = appletObj[0].abrirDocumento(urlDoc, rutaDoc);
    } catch (ex) {
       bootbox.alert("Fallo en abrir el documento");
       return;
    }
    return retval;
}

function fn_abrirDocFromPCApplet(rutaDoc) {
    var retval = "";
    var appletObj = jQuery('#firmarDocumento');
    try {
        retval = appletObj[0].abrirDocumentoPC(rutaDoc);
    } catch (ex) {
       bootbox.alert("Fallo en abrir el documento");
       return;
    }
    return retval;
}


function fn_generaDocApplet_personal(urlDoc, rutaDoc, callback) {   
    var retval = "";
    var param = {rutaDoc: rutaDoc, verBloqueo: false};
    //runApplet(appletsTramiteDoc.verificaSiExisteDoc, param, function(data) {   
    runOnDesktop(accionOnDesktopTramiteDoc.verificaSiExisteDoc, param, function(data){
        retval = data;
        if (retval === "SI") {
           bootbox.alert("El documento ya fue creado, se procedera a visualizar.",function(){
                var param = {urlDoc: urlDoc, rutaDoc: rutaDoc, remplazaArchivo: false};
                //runApplet(appletsTramiteDoc.generaDocumento, param, function(data) {
                runOnDesktop(accionOnDesktopTramiteDoc.verificaSiExisteDoc, param, function(data){
                    retval = data;
                    callback(retval);
                });
           });       
        }else{
            var param = {urlDoc: urlDoc, rutaDoc: rutaDoc, remplazaArchivo: false};                
                //runApplet(appletsTramiteDoc.generaDocumento, param, function(data) {
		runOnDesktop(accionOnDesktopTramiteDoc.verificaSiExisteDoc, param, function(data){                
                    retval = data;
                    callback(retval);
                });
        }
    });

}

function fn_generaDocApplet(urlDoc, rutaDoc, callback) {
    /*var retval = "";
     var appletObj = jQuery('#firmarDocumento');
     try{
     retval=appletObj[0].verificaSiExisteDoc(rutaDoc,false);
     if (retval=="SI"){
    bootbox.alert("El documento ya fue creado, se procedera a visualizar.");
     }
     retval=appletObj[0].generaDocumento(urlDoc,rutaDoc,false);
     
     }catch(ex){
    bootbox.alert("Fallo en Generar el documento");
     }
     return retval;*/

    var retval = "";
    var param = {rutaDoc: rutaDoc, verBloqueo: false};
    runApplet(appletsTramiteDoc.verificaSiExisteDoc, param, function(data) {
        retval = data;
        if (retval === "SI") {
           bootbox.alert("El documento ya fue creado, se procedera a visualizar.",function(){
                var param = {urlDoc: urlDoc, rutaDoc: rutaDoc, remplazaArchivo: false};
                runApplet(appletsTramiteDoc.generaDocumento, param, function(data) {
                    retval = data;
                    callback(retval);
                });
           });       
        }else{
            var param = {urlDoc: urlDoc, rutaDoc: rutaDoc, remplazaArchivo: false};
                runApplet(appletsTramiteDoc.generaDocumento, param, function(data) {
                    retval = data;
                    callback(retval);
                });
        }
    });

}

function fn_verificaSiExisteDocApplet(rutaDoc, remplazaArchivo) {
    
    console.log("========================================");
    console.log("NUCLEO DURO");
    console.log("========================================");
    console.log("fn_verificaSiExisteDocApplet->"+rutaDoc+", "+remplazaArchivo);
    
    var retval = "";
    var appletObj = jQuery('#firmarDocumento');
        
    try {
        //verificaSiExisteDoc
        var pTieneWord = jQuery('#txtTieneWord').val();
        if (typeof (pTieneWord) !== "undefined" && pTieneWord !== "" && pTieneWord==="SI") { 
            remplazaArchivo = true;  
        }else{
            remplazaArchivo = false;        
        }         
        
        console.log(remplazaArchivo);        
        retval = appletObj[0].verificaSiExisteDoc(rutaDoc, remplazaArchivo);
    } catch (ex) {
       bootbox.alert("Fallo en verificar si existe el documento");
       return;
    }
    
    console.log("========================================");
    console.log("FIN NUCLEO DURO");
    console.log("========================================");
    
    return retval;
}

function fn_generaDocumentoApplet(urlDoc, rutaDoc, remplazaArchivo) {
    console.log("fn_generaDocumentoApplet");
    console.log("urlDoc->"+urlDoc);
    console.log("rutaDoc->"+rutaDoc);
    console.log("remplazaArchivo->"+remplazaArchivo);
    
    var retval = "";
    var appletObj = jQuery('#firmarDocumento');
    try {
        if (!!!remplazaArchivo) {
            remplazaArchivo = false;
        }
        retval = appletObj[0].generaDocumento(urlDoc, rutaDoc, remplazaArchivo);
    } catch (ex) {
       bootbox.alert("Fallo en generar el documento");
       return;
    }
    console.log("retval->"+retval);
    return retval;
}

function fn_cargarDocumentoApplet(urlDoc, rutaDoc) {
    var retval = "";
    var appletObj = jQuery('#firmarDocumento');
    try {
        retval = appletObj[0].cargarDocumento(urlDoc, rutaDoc);
    } catch (ex) {
       bootbox.alert("Fallo en cargar el documento");
       return;
    }
    return retval;
}


function fn_abrirRutaDocsApplet() {
    var retval = "NO";
    var appletObj = jQuery('#firmarDocumento');
    try {
        retval = appletObj[0].abrirRutaDocs();
    } catch (ex) {
        retval = "NO";
    }
    return retval;
}


function fn_cargaDocApplet(urlDoc, rutaDoc, callback) {
    console.log("function fn_cargaDocApplet-->");
//        var retval = "";
//        var appletObj = jQuery('#firmarDocumento');
//        try{
//            retval=appletObj[0].verificaSiExisteDoc(rutaDoc,false);
//            if (retval==="SI"){
//                retval=appletObj[0].cargarDocumento(urlDoc,rutaDoc);
//            }else{
//               bootbox.alert("El documento NO existe en: "+rutaDoc);
//            }
//        }catch(ex){
//            bootbox.alert("Fallo en cargar el documento");
//        }
//        return retval;


    var retval = "";
    //var param = {rutaDoc: rutaDoc, verBloqueo: false};
    var param = {rutaDoc: rutaDoc, verBloqueo: false, reemplazaArchivo: false};
    runApplet(appletsTramiteDoc.verificaSiExisteDoc, param, function(data) {
        retval = data;
        console.log("retval--->"+retval);
        if (retval === "SI") {
            var param = {urlDoc: urlDoc, rutaDoc: rutaDoc};
            console.log("--->appletsTramiteDoc.cargarDocumento");
            runApplet(appletsTramiteDoc.cargarDocumento, param, function(data) {
                callback(data);
            });
        } else {
            console.log("--->bootbox.alert");
            bootbox.alert("El documento NO existe en: " + rutaDoc,function(){
                callback(retval);
           });
           
        }
    });


}


function fn_verDocumentosObj(pnuAnn, pnuEmi, ptiOpe, pOpcion, pnuDes) {
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goDocRutaAbrir";
        //p[0] = "accion=goDocRutaAbrirEmiReporte";
        p[1] = "nuAnn=" + pnuAnn;
        p[2] = "nuEmi=" + pnuEmi;
        p[3] = "tiOpe=" + ptiOpe;
        /*--Ver_doc - Hermes - Log 28/05/19--*/
        p[4] = "coAccion=" + "002";         
        if (typeof (pOpcion) !== "undefined" && pOpcion !== ""){
            if ((typeof (pOpcion) !== "undefined" && pOpcion !== "") && (typeof (pnuDes) !== "undefined" && pnuDes !== "")) {
                p[5] = "nuDes=" + pnuDes; 
                p[6] = "coEstado=" + allTrim($("#txtCoEstadoDoc").val());
                p[7] = "coOpcion=" + pOpcion;
            }else{
                pOpcion = allTrim($("#txtCodOpcion").val());
                pnuDes = jQuery('#txtpnuDes').val();
                if (typeof (pOpcion) !== "M020102") {
                    pnuDes = "1";            
                }            
                p[5] = "nuDes=" + pnuDes; 
                p[6] = "coEstado=" + allTrim($("#txtCoEstadoDoc").val());
                p[7] = "coOpcion=" + pOpcion;            
            }            
        }else{
            if($("#txtCodOpcion").val()!== "" && typeof ($("#txtCodOpcion").val()) !== "undefined"){
                pnuDes = jQuery('#txtpnuDes').val();
                if ($("#txtCodOpcion").val() !== "M020102") {
                    pnuDes = "1";            
                }
                p[5] = "nuDes=" + pnuDes; 
                p[6] = "coEstado=" + allTrim($("#txtCoEstadoDoc").val());
                p[7] = "coOpcion=" + $("#txtCodOpcion").val();
            }
        }
        /*--Ver_doc - Hermes - Log 28/05/19--*/              
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            var result;
            eval("var docs=" + data);
            //var docs = JSON.parse(data);
            if (typeof (docs) !== "undefined" && typeof (docs.nuAnn) !== 'undefined' && docs.nuAnn !== "") {
                if (docs.retval === "OK") {
                    //result = fn_abrirDocApplet(docs.noUrl,docs.noDoc);
                    var param = {urlDoc: docs.noUrl, rutaDoc: docs.noDoc};
                    //runApplet(appletsTramiteDoc.abrirDocumento, param, function(data) {
                    runOnDesktop(accionOnDesktopTramiteDoc.abrirDocumento, param, function(data) {
                        result = data;
                    });
                } else {
                    //Error en Documento
                    alert_Danger("!Repositorio : ", docs.retval);
                }
            }

        }, 'text', false, false, "POST");
    }
    return false;
}

function fn_selAccion() 
{

    var accion=jQuery('#ddlAccion').val();   
   

    if(accion === "0")
    {
        // [INICIO] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
        /*$('#divUrgencia').show(); */
        // [FIN] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
        $('#divMensajeria').show(); 
    }
    /*if(accion === "1")
    {
        $('#divUrgencia').hide(); 
        $('#divMensajeria').hide(); 
    }*/
    if(accion === "1")
    {
        // [INICIO] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
        /*$('#divUrgencia').show(); */
        // [FIN] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
        $('#divMensajeria').hide(); 
    }
  
}


function fn_grabarEnvioNotificacionObj(pnuAnn,pnuEmi,pexisteDoc,pexisteAnexo,tipoBandeja) {
    console.log("fn_grabarEnvioNotificacionObj");
    
    //var nCodAccion = allTrim($("#ddlAccion").val());
    var nCodAccion = $('input[name="envio"]:checked').val();/*[HPB] 06/11/20 Fin-Modificaciones en el envío de documentos. Listado de entidades que interoperan*/
    // [INICIO] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
    /*var nCodUrgencia = allTrim($("#ddlUrgencia").val());*/
    // [FIN] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
    var nCodDepMsj=allTrim($("#sMensajeria").val());

    var p = new Array();
    //p[0] = "accion=goDocRutaAbrir";
    p[0] = "accion=goUpdEnvioNotificacion";
    p[1] = "nuAnn=" + pnuAnn;
    p[2] = "nuEmi=" + pnuEmi;
    p[3] = "nCodAccion=" + nCodAccion;
    // [INICIO] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
    /*p[4] = "nCodUrgencia=" + nCodUrgencia;*/
    p[4] = "nCodDepMsj=" + nCodDepMsj;
    // [FIN] [VMBP 16/08/2019 - Se borra la opción "urgencia". Req Mensajería]
    /*HPB 27/05/2020 - Inicio - Requerimiento Paginación emitidos*/
    var paginaRecepcionSelect = jQuery('#txtPaginaEmisionSalta').val();
    if(typeof(paginaRecepcionSelect)==="undefined" || paginaRecepcionSelect===null || paginaRecepcionSelect===""){
        paginaRecepcionSelect = 1;
    }
    /*HPB 27/05/2020 - Fin - Requerimiento Paginación emitidos*/  
    //ajaxCallSendJson(url, jsonString, function(data) {
    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {

        if (data.substring(0,1) === "1") {
            alert_Sucess("MENSAJE", "Se envió el documento a la mensajería correctamente");
            if(tipoBandeja==="EDIT"){
                editarDocumentoEmiClick(pnuAnn, pnuEmi,pexisteDoc,pexisteAnexo);           
            }
            if(tipoBandeja==="NEW"){
                EnvioOkMensajeria();
            }
            else {
                  ajaxCall("/srDocumentoAdmEmision.do?accion=goInicio&vPagina="+paginaRecepcionSelect, $('#buscarDocumentoEmiBean').serialize(), function(data) {
                        refreshScript("divTablaEmiDocumenAdm", data);
                    }, 'text', false, false, "POST");
           
           }
            removeDomId('windowConsultaAnexo');



        } else {
            alert_Danger("ERROR:", data.substring(1,data.length));
        }
    }, 'text', false, false, "POST");    
      

                            
    return;
}

//jazanero
function fn_grabarEnvioNotificacionObjCorreo(pnuAnn,pnuEmi,pexisteDoc,pexisteAnexo,tipoBandeja,enviaCorreo,correo) {
    console.log("fn_grabarEnvioNotificacionObjCorreo");
        
    var p = new Array();
    //p[0] = "accion=goDocRutaAbrir";
    p[0] = "accion=goUpdEnvioNotificacion";
    p[1] = "nuAnn=" + pnuAnn;
    p[2] = "nuEmi=" + pnuEmi;
    p[3] = "nCodAccion=1";
    p[4] = "nCodUrgencia=1";
    p[5] = "nCodDepMsj=" ;
    p[6] = "nEnviaCorreo=" + enviaCorreo ;
    p[7] = "nCorreo=" + correo ;

    console.log("array->"+p);
    
    //ajaxCallSendJson(url, jsonString, function(data) {
    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {

        if (data.substring(0,1) === "1") {
            alert_Sucess("MENSAJE", "Se envió el documento a la mensajería correctamente");
            if(tipoBandeja==="EDIT"){
                editarDocumentoEmiClick(pnuAnn, pnuEmi,pexisteDoc,pexisteAnexo);           
            }
            if(tipoBandeja==="NEW"){
                EnvioOkMensajeria();
            }
            else {
                  ajaxCall("/srDocumentoAdmEmision.do?accion=goInicio", $('#buscarDocumentoEmiBean').serialize(), function(data) {
                        refreshScript("divTablaEmiDocumenAdm", data);
                    }, 'text', false, false, "POST");
           
           }
            removeDomId('windowConsultaAnexo');



        } else {
            alert_Danger("ERROR:", data.substring(1,data.length));
        }
    }, 'text', false, false, "POST");    
      

                            
    return;
}

function fn_grabarArchivarDocumento(pnuAnn,pnuEmi) {

    
    var myForm='documentoEmiArBean';
    if(fn_valFormArchivarDoc(myForm)){
        var jsonBody =
                {
                    "nuAnn": pnuAnn, "nuEmi": pnuEmi,"nuDes": $('#'+myForm).find("#txtNu_Des").val(), "existeAnexo": $('#'+myForm).find("#txtUpdate").val(),//Hermes 30/01/2019 - Requerimiento Acta 005-20019 
                    "feEmi": $('#'+myForm).find("#itxtFecha").val(), "docObser": $('#'+myForm).find("#itxtObservaciones").val()
                };
        var jsonString = JSON.stringify(jsonBody);
        var url = "/srDocObjeto.do?accion=goUpdArchivarDoc";
        /*HPB 27/05/2020 - Inicio - Requerimiento Paginación emitidos*/
        var paginaRecepcionSelect = jQuery('#txtPaginaEmisionSalta').val();
        if(typeof(paginaRecepcionSelect)==="undefined" || paginaRecepcionSelect===null || paginaRecepcionSelect===""){
            paginaRecepcionSelect = 1;
        }
        /*HPB 27/05/2020 - Fin - Requerimiento Paginación emitidos*/         
        ajaxCallSendJson(url, jsonString, function(data) {
            if (data === "Datos guardados.") {
                alert_Sucess("MENSAJE", data);                           
          
                ajaxCall("/srDocumentoAdmEmision.do?accion=goInicio&vPagina="+paginaRecepcionSelect, $('#buscarDocumentoEmiBean').serialize(), function(data) {
                            $('#divNewEmiDocumAdmin').hide();
                            $('#divEmiDocumentoAdmin').show();
                            refreshScript("divTablaEmiDocumenAdm", data);
                        }, 'text', false, false, "POST");
                        
                        
                jQuery('#divNewEmiDocumAdmin').html("");        
                removeDomId('windowConsultaArchivar');

            } else {
                alert_Danger("ERROR:", data);
            }
        }, 'text', false, false, "POST");
        /*$('#RutaDocs').html("<strong>" + dirPri + "</strong>");
        setTimeout("fu_activaDependencia_dir(" + codDep + "," + codDep + ");", 500);*/
    }
    return;
}

function fn_valFormArchivarDoc(objForm){
    var vReturn=1;
    var Observaciones=$('#'+objForm).find("#itxtObservaciones").val();
    var Fecha=$('#'+objForm).find("#itxtFecha").val();

    
    if ( document.getElementById( "vacio" )) {
        var vacio=document.getElementById("vacio").value;
        
        if(vacio==="-1")
        {
           vReturn=0;
           alert_Info("Aviso", "Verificar los archivos adjuntos"); 
        }
    }

    
    if (!!Observaciones===false)
    {
        vReturn=0;
        alert_Info("Aviso", "Debe Ingresar por lo menos una observación");
        $('#'+objForm).find('#itxtObservaciones').focus();
    }
    if (!!Fecha===false)
    {
        vReturn=0;
        alert_Info("Aviso", "Debe Ingresar Fecha");
        $('#'+objForm).find('#itxtFecha').focus();
    }
    
    if(!!Fecha){
            if(moment(fecha, "DD/MM/YYYY").isValid()){// HH:mm
  
                var fecha=moment(fecha, ["DD","DD/MM","DD/MM/YY","DD/MM/YY HH:mm","DD/MM/YYYY","DD/MM/YYYY HH:mm"]); 
                if(!fecha.isValid()){
                vReturn=0;
                 bootbox.alert("<h5>Fecha Inválida.</h5>", function() {
                     bootbox.hideAll();
                     $('#'+objForm).find('#itxtFecha').focus();
                 });
                                         
                }                
            }     
       
       
    }
    
 
   
   
   
    return vReturn;
}


function fn_verDocumentosFromPC2(pnuAnn, pnuEmi, ptiOpe) {
    if (!!pnuAnn) {
        var p = new Array();
        //p[0] = "accion=goDocRutaAbrir";
        p[0] = "accion=goDocRutaAbrirEmiReporte";
        p[1] = "nuAnn=" + pnuAnn;
        p[2] = "nuEmi=" + pnuEmi;
        p[3] = "tiOpe=" + ptiOpe;
        
       
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            var result;
            eval("var docs=" + data);
            if (typeof (docs) != "undefined" && typeof (docs.nuAnn) != 'undefined' && docs.nuAnn != "") {
                if (docs.retval == "OK") {
                    result = fn_abrirDocFromPCApplet(docs.noDoc);
                } else {
                    //Error en Documento
                    alert_Danger("!Repositorio : ", docs.retval);
                }
            }

        }, 'text', false, false, "POST");

    }
    return false;
}

function fn_verDocumentosFromPC(pnuAnn, pnuEmi, ptiOpe) {
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goRutaGeneraDocx";
        p[1] = "nuAnn=" + pnuAnn;
        p[2] = "nuEmi=" + pnuEmi;
        p[3] = "tiOpe=1";
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            var result;
            eval("var docs=" + data);
            if (typeof (docs) !== "undefined" && typeof (docs.nuAnn) !== 'undefined' && docs.nuAnn !== "") {
                //result = fn_abrirDocFromPCApplet(docs.noDoc);
				/*if (docs.retval === "OK") {
	                var param = {rutaDoc: docs.noDoc};
	                runApplet(appletsTramiteDoc.abrirDocumentoPC, param, function(data) {
	                    result = data;
	                    if (result != "OK") {
	                        alert_Danger("!Abrir Docx : ", result);
	                    }
	                });				
                    }else{
                        alert_Danger("!Abrir Docx : ", docs.retval);				
                    }*/
                if (docs.retval === "OK") {
//                    var param = {rutaDoc: docs.noDoc};
//                    runOnDesktop(accionOnDesktopTramiteDoc.abrirDocumentoPC, param, function(data) {
//                        result = data;
//                        if (result !== "OK") {
//                            alert_Danger("!Abrir Docx : ", result);
//                        }
//                    });
                    fn_generaDocDesktop(docs.noUrl, docs.noDoc, docs.nuAnn, docs.nuEmi, docs.tieneWord, function(data) {
                        result = data;
                        if (result !== "OK") {
                           bootbox.alert(result);
                        }
                    });
                                        
                }else{
                    alert_Danger("!Abrir Docx : ", docs.retval);
                }
            }
        }, 'text', false, false, "POST");

    }
    return false;
}

function fn_enviarNotificacionObj(pnuAnn, pnuEmi,ptiEnv,pexisteDoc,pexisteAnexo,tipoBandeja) {
    console.log("fn_enviarNotificacionObj");
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goDocEnviarNotificacion";
       // p[1] = "pkEmi=" + pnuAnn + pnuEmi + pnuDes;
       p[1]="pnuAnn="+pnuAnn;
       p[2]="pnuEmi="+pnuEmi;
       p[3]="ptiEnvMsj="+ptiEnv;
       p[4]="pexisteDoc="+pexisteDoc;
       p[5]="pexisteAnexo="+pexisteAnexo;
       p[6]="tipoBandeja="+tipoBandeja;
       
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            if (data !== null)
            {
                $("body").append(data);
            }
        }, 'text', false, false, "POST");

    }
    return false;
}

function fn_ArchivarDocumentosMsj(pnuAnn, pnuEmi) {

    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goArDocMsjEnviados";
       // p[1] = "pkEmi=" + pnuAnn + pnuEmi + pnuDes;
       p[1]="pnuAnn="+pnuAnn;
       p[2]="pnuEmi="+pnuEmi;
     
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            if (data !== null)
            {
                $("body").append(data);
            }
        }, 'text', false, false, "POST");

    }
    return false;
}

function fn_verAnexosObj(pnuAnn, pnuEmi, pnuDes) {
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goDocAnexo";
        p[1] = "pkEmi=" + pnuAnn + pnuEmi + pnuDes;
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            if (data !== null)
            {
                $("body").append(data);
            }
        }, 'text', false, false, "POST");

    }
    return false;
}

function fn_cargarDocumentosAnexosObj(pnuAnn, pnuEmi) {
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goCargarDocumentosAnexos";
        p[1] = "pkEmi=" + pnuAnn + pnuEmi + "N";
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            if (data !== null)
            {
                $("body").append(data);
            }
        }, 'text', false, false, "POST");

    }
    return false;
}


function fn_verSeguimientoObj(pnuAnn, pnuEmi, pnuDes) {     
    var result;
    if (!!pnuAnn) {
        //alert("entro="+pnuAnn+" pnuEmi="+pnuEmi+" pnuDes="+pnuDes);
         var p = new Array();
                p[0] = "accion=goDocSeguimiento";
                p[1] = "pkEmi=" + pnuAnn + pnuEmi + pnuDes;
                //validando si existe seguimiento
                ajaxCall("/srDocObjeto.do", "accion=goDocSeguimientoRoot&"+p[1], function(data) {
                       if (data == null || data==""){
                           alert_Info("Seguimiento :", "No se puede dar seguimiento a documentos sin destinatario o anulados.");
                           return;
                       }else{ 
                           //si es ok 
                           //alert("ok="+p.join("&"));
                           ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
                               if (data !== null){
                                   $("body").append(data);
                               }
                           }, 'text', false, false, "POST");
                       }
                }, 'text', false, true, "GET");         
                
        
   }
   /*
   var q = new Array();
        q[1] = "pkEmi=" + pnuAnn + pnuEmi + pnuDes;
        //alert("accion=goDocAutorizado&"+q[1]);
        ajaxCall("/srDocObjeto.do", "accion=goDocAutorizado&"+q[1], function(data) {
        result=data; 
        }, 'text', false, true, "GET");
     */   
   
    return false;
}

function fn_anexosJson(
        pnuAnn, pnuEmi) {
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goDocAnexoRoot";
        p[1] = "nuAnn=" + pnuAnn;
        p[2] = "nuEmi=" + pnuEmi;
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
           bootbox.alert(data);
        }, 'text', false, false, "POST");

    }
    return false;
}

function fn_verDetalleDocAnexo(vpkEmiDoc) {
    var p = new Array();
    p[0] = "accion=goDetalleDocAnexo";
    p[1] = "pkEmi=" + vpkEmiDoc;
    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
        if (data !== null)
        {
            refreshScript("divDetalleDocAnexo", data);
        }
    }, 'text', false, false, "POST");
    return false;
}
function fn_verDetalleSeguimiento(vpkEmiDoc, vpkNuSec) {
    var p = new Array();
    p[0] = "accion=goDetalleSeguimiento";
    p[1] = "pkEmi=" + vpkEmiDoc;   
    p[2] = "pkExp=" + jQuery('#frm_docOrigenBean_pkExp').val(); 
    if (vpkNuSec == null || vpkNuSec==""){
        p[3] = "pkNuSec=0";
    }else{
        p[3] = "pkNuSec=" + vpkNuSec;/*12/09/2019 HPB Devolucion*/
    }
    p[4] = "pkCoEmpEmi=" + jQuery('#pCoEmpEmi1').val();
    /* [HPB] Inicio 24/04/23 CLS-087-2022 Extension expediente */
    //p[5] = "pkExpPadre=" + jQuery('#pNuEmiPadre').val(); 
    /* [HPB] Fin 24/04/23 CLS-087-2022 Extension expediente */
    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
        if (data !== null)
        {
            refreshScript("divDetalleDocSeguimiento", data);
        }
    }, 'text', false, false, "POST");
    return false;
}

function fn_verDocAnexo(pnuAnn, pnuEmi, pnuAne) {
    console.log("fn_verDocAnexo");
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goDocRutaAnexo";
        p[1] = "nuAnn=" + pnuAnn;
        p[2] = "nuEmi=" + pnuEmi;
        p[3] = "nuAne=" + pnuAne;
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            var result;
            eval("var docs=" + data);
            if (typeof (docs) != "undefined" && typeof (docs.nuAnn) != 'undefined' && docs.nuAnn != "") {
                //result = fn_abrirDocApplet(docs.noUrl,docs.noDoc);
                console.log("urlDoc:"+docs.noUrl);
                console.log("rutaDoc:"+docs.noDoc);
                var param = {urlDoc: docs.noUrl, rutaDoc: docs.noDoc};
               /*runApplet(appletsTramiteDoc.abrirDocumento, param, function(data) {
                    result = data;
                });*/
                runOnDesktop(accionOnDesktopTramiteDoc.abrirDocumento,param, function(data) {
                    result = data;
                });
            }

        }, 'text', false, false, "POST");

    }
    return false;
}

function fn_generaDocx_old() {
    var pnuAnn = jQuery('#documentoEmiBean').find('#nuAnn').val();
    var pnuEmi = jQuery('#documentoEmiBean').find('#nuEmi').val();

    var rpta = fu_verificarDestinatario("1");
    var nrpta = rpta.substr(0, 1);
    if (nrpta === "1") {
        rpta = fu_verificarReferencia();
        nrpta = rpta.substr(0, 1);
        if (nrpta === "1") {
            rpta = fu_verificarChangeDocumentoEmiAdm();
            nrpta = rpta.substr(0, 1);
            if (nrpta === "1") {
                alert_Warning("Emisión :", "Necesita grabar los cambios");
            } else {
                if (!!pnuAnn) {
                    var p = new Array();
                    p[0] = "accion=goRutaGeneraDocx";
                    p[1] = "nuAnn=" + pnuAnn;
                    p[2] = "nuEmi=" + pnuEmi;
                    p[3] = "tiOpe=1";
                    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
                        var result;
                        eval("var docs=" + data);
                        if (typeof (docs) !== "undefined" && typeof (docs.retval) !== 'undefined' && docs.retval !== "") {
                            if (docs.retval === "OK") {
                                //fn_generaDocApplet(docs.noUrl, docs.noDoc, function(data) {
                     
                                fn_generaDocDesktop(docs.noUrl, docs.noDoc, docs.nuAnn, docs.nuEmi, docs.tieneWord, function(data) {
                                    result = data;
                                    if (result !== "OK") {
                                       bootbox.alert(result);
                                    }
                                });
                            } else {
                                alert_Danger("Generar Docx: ", docs.retval);
                            }
                        }
                    }, 'text', false, false, "POST");
                }
            }
        } else {
            alert_Info("Emisión :", rpta);
        }
    } else {
        alert_Info("Emisión :", rpta);
    }

    return false;
}


function fn_generaDocx() {
    var pnuAnn = jQuery('#documentoEmiBean').find('#nuAnn').val();
    var pnuEmi = jQuery('#documentoEmiBean').find('#nuEmi').val();
               
    console.log("fn_generaDocx()");
    var rpta = fu_verificarDestinatario("1");
    var nrpta = rpta.substr(0, 1);
    console.log("fu_verificarDestinatario nrpta->"+nrpta);
    if (nrpta === "1") {
        rpta = fu_verificarReferencia();
        nrpta = rpta.substr(0, 1);
        console.log("fu_verificarReferencia nrpta->"+nrpta);
        if (nrpta === "1") {
            rpta = fu_verificarChangeDocumentoEmiAdm();
            nrpta = rpta.substr(0, 1);
            console.log("fu_verificarChangeDocumentoEmiAdm nrpta->"+nrpta);
            if (nrpta === "1") {
                console.log("alert_Warning1");
                alert_Warning("Emisión :", "Necesita grabar los cambios");
            } else {
                if (!!pnuAnn) {
                    var p = new Array();
                    p[0] = "accion=goRutaGeneraDocx";
                    p[1] = "nuAnn=" + pnuAnn;
                    p[2] = "nuEmi=" + pnuEmi;
                    p[3] = "tiOpe=1";
                    console.log("/srDocObjeto.do?accion=goRutaGeneraDocx");
                    console.log("nuEmi->"+pnuEmi);
                    console.log("tiOpe=1");
                    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
                        var result;
                        eval("var docs=" + data);
                        console.log("data->"+data);
                        if (typeof (docs) !== "undefined" && typeof (docs.retval) !== 'undefined' && docs.retval !== "") {
                            console.log("docs.retval->"+docs.retval);
                            if (docs.retval === "OK") {
                                console.log("fn_generaDocApplet()");
                                console.log("docs.noUrl->"+docs.noUrl);
                                console.log("docs.noDoc->"+docs.noDoc);
                                console.log("docs.tienedocx->"+docs.tieneWord);
                                //fn_generaDocApplet(docs.noUrl, docs.noDoc, docs.nuAnn, docs.nuEmi, docs.tieneWord, function(data) {
                                fn_generaDocDesktop2(docs.noUrl, docs.noDoc, docs.nuAnn, docs.nuEmi, docs.tieneWord, function(data) {
                                    result = data;
                                    console.log("result->"+result);
                                    if (result !== "OK") {
                                       bootbox.alert(result);
                                    }
                                });
                            } else {
                                console.log("alert_Danger2");
                                alert_Danger("Generar Docx: ", docs.retval);
                            }
                        }
                    }, 'text', false, false, "POST");
                }
            }
        } else {
            console.log("alert_Info1");
            alert_Info("Emisión :", rpta);
        }
    } else {
        console.log("alert_Info2");
        alert_Info("Emisión :", rpta);
    }

    return false;
}


function fn_generaDocApplet(urlDoc, rutaDoc, nuAnn, nuEmi, tieneWord, callback) {
    console.log("acc: fn_generaDocApplet(urlDoc, rutaDoc, nuAnn, nuEmi, tieneWord, callback)");
    var retval = "";
    
    //logica para verificar si existe archivo word
    //fin logica de word
    
    jQuery('#txtCargaArepositorio').val("SI");
    jQuery("#btncolorazul").css("display","none");
    jQuery("#btncolorverde").css("display","block");
    
    //jazanero
    var pnuEmiProyecto = jQuery('#documentoEmiBean').find('#nuEmiProyecto').val();
    var pnuAnnProyecto = jQuery('#documentoEmiBean').find('#nuAnnProyecto').val();
    var pnuAneProyecto = jQuery('#documentoEmiBean').find('#nuAneProyecto').val();
    var psEsTipoProyecto = jQuery('#txtsEsTipoProyecto').val();  
    //jazanero
    console.log("pnuAneProyecto->"+pnuAneProyecto);
    console.log("pnuEmiProyecto->"+pnuEmiProyecto);
    console.log("pnuAnnProyecto->"+pnuAnnProyecto);
    console.log("psEsTipoProyecto->"+psEsTipoProyecto);
    
    if (typeof (tieneWord) !== "undefined" && tieneWord !== "" && tieneWord==="SI") {
        console.log("1");
        jQuery('#txtTieneWord').val("SI");        
    }else{
        console.log("2");
        jQuery('#txtTieneWord').val("NO");        
    }  
    
    console.log("VERIFICA QUE EN LA CARPETA DE LA PC EXISTA EL DOCUMENTO");
    console.log("========================================================");
    console.log("rutaDoc->"+rutaDoc);
    console.log("verBloqueo->false");
    console.log("appletsTramiteDoc.verificaSiExisteDoc");  
    var param = {rutaDoc: rutaDoc, verBloqueo: false, reemplazaArchivo: false};
    runApplet(appletsTramiteDoc.verificaSiExisteDoc, param, function(data) {
        retval = data;
        console.log("retval->"+retval);
        if (retval === "SI") {
           bootbox.alert("El documento ya fue creado, se procedera a visualizar.",function(){
               console.log("El documento ya fue creado, se procedera a visualizar. --> PRESIONO SI") ;
               console.log("appletsTramiteDoc.generaDocumento") ;
               console.log("urlDoc->"+urlDoc);
               console.log("rutaDoc->"+rutaDoc);
               console.log("remplazaArchivo->false");
               var param = {urlDoc: urlDoc, rutaDoc: rutaDoc, remplazaArchivo: false};
                runApplet(appletsTramiteDoc.generaDocumento, param, function(data) {
                    retval = data;
                    callback(retval);
                });
           });       
        }else{
            
            //console.log("Colocar logica para que indiar que habra desde Respositorio");
            //INICIO
            if (typeof (tieneWord) !== "undefined" && tieneWord !== "" && tieneWord==="SI") {
                console.log("Existe un backup en BD, procedo a abrirlo");
                //LOGICA PARA ABRIR DESDE BD
                    var pAbreWord = "&pAbreWord="+tieneWord;
                    var p = new Array();
                    p[0] = "accion=goDocRutaAbrirEmiReporte";        
                    p[1] = "nuAnn=" + nuAnn;
                    p[2] = "nuEmi=" + nuEmi;
                    p[3] = "tiOpe=" + "5";
                    p[4] = "pAbreWord=" + tieneWord;
                    console.log("pnuEmi->"+nuEmi);
                    console.log("ptiOpe->5");                    
                    console.log("/srDocObjeto.do?accion=goDocRutaAbrirEmiReporte");
                    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
                        var result;
                        eval("var docs=" + data);
                        console.log("data->"+data);
                        if (typeof(docs) !== "undefined" && typeof(docs.nuAnn) !== 'undefined' && docs.nuAnn !== "") {
                            console.log("docs.retval->"+docs.retval);                
                            if (docs.retval === "OK") {
                                console.log("urlDoc->"+docs.noUrl+pAbreWord);
                                console.log("rutaDoc->"+docs.noDoc);
                                //result = fn_abrirDocApplet(docs.noUrl, docs.noDoc);
                                var param={urlDoc:docs.noUrl+pAbreWord,rutaDoc:docs.noDoc};
                                runApplet(appletsTramiteDoc.abrirDocumento,param,function(data){
                                    //result=data;
                                    retval = data;
                                    callback(retval);
                                });
                            } else {
                                console.log("alert_Danger1");
                                //Error en Documento
                                alert_Danger("!Repositorio : ", docs.retval);
                            }
                        }

                    }, 'text', false, false, "POST");
                
                //FIN LOGICA
            }else if (!!pnuEmiProyecto && !!pnuAnnProyecto && typeof (psEsTipoProyecto) !== "undefined" && psEsTipoProyecto !== "" && psEsTipoProyecto==="SI") {
                console.log("Abre proyecto desde ANEXO principal");
                console.log("procedo a abrirlo");
                
                //LOGICA PARA ABRIR DESDE BD
                    var p = new Array();
                    p[0] = "accion=goDocRutaAnexo";
                    p[1] = "nuAnn=" + pnuAnnProyecto;
                    p[2] = "nuEmi=" + pnuEmiProyecto;
                    p[3] = "nuAne=" + pnuAneProyecto;
                    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
                            var result;
                            eval("var docs=" + data);
                            if (typeof (docs) != "undefined" && typeof (docs.nuAnn) != 'undefined' && docs.nuAnn != "") {
                                    //result = fn_abrirDocApplet(docs.noUrl,docs.noDoc);
                                    console.log("urlDoc:"+docs.noUrl);
                                    console.log("rutaDoc:"+docs.noDoc);
                                    console.log("rutaDoc:"+rutaDoc);                                    
                                    var param = {urlDoc: docs.noUrl, rutaDoc: rutaDoc};
                                    runApplet(appletsTramiteDoc.abrirDocumento, param, function(data) {
                                            result = data;
                                    });
                            }

                    }, 'text', false, false, "POST");
                //FIN LOGICA
                
            }else {
                console.log("Abro appletsTramiteDoc.generaDocumento");
                console.log("urlDoc->"+urlDoc);
                console.log("rutaDoc->"+rutaDoc);
                console.log("remplazaArchivo->false");
                var param = {urlDoc: urlDoc, rutaDoc: rutaDoc, remplazaArchivo: false};
                    runApplet(appletsTramiteDoc.generaDocumento, param, function(data) {
                        retval = data;
                        callback(retval);
                    });
            }
            
            //FIN
            
        }
    });

}

function fn_generaDocDesktop2(urlDoc, rutaDoc, nuAnn, nuEmi, tieneWord, callback) {
    var retval = "";

    jQuery('#txtCargaArepositorio').val("SI");
    jQuery("#btncolorazul").css("display","none");
    jQuery("#btncolorverde").css("display","block");
        
    var pnuEmiProyecto = jQuery('#documentoEmiBean').find('#nuEmiProyecto').val();
    var pnuAnnProyecto = jQuery('#documentoEmiBean').find('#nuAnnProyecto').val();
    var pnuAneProyecto = jQuery('#documentoEmiBean').find('#nuAneProyecto').val();
    var psEsTipoProyecto = jQuery('#txtsEsTipoProyecto').val();  
        
    if (typeof (tieneWord) !== "undefined" && tieneWord !== "" && tieneWord==="SI") {
        jQuery('#txtTieneWord').val("SI");        
    }else{
        jQuery('#txtTieneWord').val("NO");        
    }  
    
    var param = {rutaDoc: rutaDoc, verBloqueo: false, reemplazaArchivo: false};
    //runApplet(appletsTramiteDoc.verificaSiExisteDoc, param, function(data) {
    runOnDesktop(accionOnDesktopTramiteDoc.verificaSiExisteDoc, param, function(data){
        retval = data;
        if (retval === "SI") {
           bootbox.alert("El documento ya fue creado, se procedera a visualizar.",function(){
               var param = {urlDoc: urlDoc, rutaDoc: rutaDoc, remplazaArchivo: false};
//                runApplet(appletsTramiteDoc.generaDocumento, param, function(data) {
//                    retval = data;
//                    callback(retval);
//                });
                fn_genDocDesktop(param, function(data){
                                                callback(data);
                });
           });       
        }else{

            if (typeof (tieneWord) !== "undefined" && tieneWord !== "" && tieneWord==="SI") {
                //LOGICA PARA ABRIR DESDE BD
                    var pAbreWord = "&pAbreWord="+tieneWord;
                    var p = new Array();
                    p[0] = "accion=goDocRutaAbrirEmiReporte";        
                    p[1] = "nuAnn=" + nuAnn;
                    p[2] = "nuEmi=" + nuEmi;
                    p[3] = "tiOpe=" + "5";
                    p[4] = "pAbreWord=" + tieneWord;

                    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
                        var result;
                        eval("var docs=" + data);
                        if (typeof(docs) !== "undefined" && typeof(docs.nuAnn) !== 'undefined' && docs.nuAnn !== "") {
                            if (docs.retval === "OK") {
                                //result = fn_abrirDocApplet(docs.noUrl, docs.noDoc);
//                                var param={urlDoc:docs.noUrl+pAbreWord,rutaDoc:docs.noDoc};
//                                runApplet(appletsTramiteDoc.abrirDocumento,param,function(data){
//                                    //result=data;
//                                    retval = data;
//                                    callback(retval);
//                                });
                                var param={urlDoc:docs.noUrl+pAbreWord,rutaDoc:docs.noDoc, remplazaArchivo: true};
                                runOnDesktop(accionOnDesktopTramiteDoc.abrirDocumento,param, function(data){
                                    retval = data;
                                });
                                
                            } else {
                                alert_Danger("!Repositorio : ", docs.retval);
                            }
                        }

                    }, 'text', false, false, "POST");
                
                //FIN LOGICA
            }else if (!!pnuEmiProyecto && !!pnuAnnProyecto && typeof (psEsTipoProyecto) !== "undefined" && psEsTipoProyecto !== "" && psEsTipoProyecto==="SI") {
                //LOGICA PARA ABRIR DESDE BD
                    var p = new Array();
                    p[0] = "accion=goDocRutaAnexo";
                    p[1] = "nuAnn=" + pnuAnnProyecto;
                    p[2] = "nuEmi=" + pnuEmiProyecto;
                    p[3] = "nuAne=" + pnuAneProyecto;
                    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
                            var result;
                            eval("var docs=" + data);
                            if (typeof (docs) != "undefined" && typeof (docs.nuAnn) != 'undefined' && docs.nuAnn != "") {
                                    //result = fn_abrirDocApplet(docs.noUrl,docs.noDoc);
                                    var param = {urlDoc: docs.noUrl, rutaDoc: rutaDoc};
//                                    runApplet(appletsTramiteDoc.abrirDocumento, param, function(data) {
//                                            result = data;
//                                    });
                                runOnDesktop(accionOnDesktopTramiteDoc.abrirDocumento,param, function(data){
                                    retval = data;
                                });
                            }

                    }, 'text', false, false, "POST");
                //FIN LOGICA
                
            }else {
                var param = {urlDoc: urlDoc, rutaDoc: rutaDoc, remplazaArchivo: false};
//                    runApplet(appletsTramiteDoc.generaDocumento, param, function(data) {
//                        retval = data;
//                        callback(retval);
//                    });
                fn_genDocDesktop(param, function(data){
                                                callback(data);
                                            });
            }
            
            //FIN
            
        }
    });

}

function fn_generaDocxProyecto(){
    console.log("Abro fn_generaDocxProyecto");

    var p = new Array();
    p[0] = "accion=goRutaGeneraDocx";
    p[1] = "nuAnn=" + $("#nuAnn").val();
    p[2] = "nuEmi=" + $("#nuEmi").val();
    p[3] = "tiOpe=1";
    p[4] = "plantilla=SI";
    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
        var result;
        eval("var docs=" + data);
        if (typeof (docs) !== "undefined" && typeof (docs.retval) !== 'undefined' && docs.retval !== "") {
            if (docs.retval === "OK") {
                console.log("urlDoc:"+docs.noUrl);
                console.log("rutaDoc:"+docs.noDoc);
                var str = docs.noDoc;
                var res = $("#nuAnn").val()+ "|ANEXO_" +str.substring(5, str.length);
                //console.log("rutaDoc:"+res);
                console.log("urlDoc->"+"documento?accion=creaDocx&nuAnn="+ $("#nuAnn").val()+"&nuEmi="+$("#nuEmi").val()+"&tiCap=03&proyecto=1");
                console.log("rutaDoc->"+res);
                console.log("remplazaArchivo->false");
//                var param = {urlDoc: "documento?accion=creaDocx&nuAnn="+ $("#nuAnn").val()+"&nuEmi="+$("#nuEmi").val()+"&tiCap=03&proyecto=1", rutaDoc: res, remplazaArchivo: false};
//                runApplet(appletsTramiteDoc.generaDocumento, param, function(data) {
//                    retval = data;
//                    //callback(retval);
//                });
                fn_generaDocDesktop("documento?accion=creaDocx&nuAnn="+ $("#nuAnn").val()+"&nuEmi="+$("#nuEmi").val()+"&tiCap=03&proyecto=1", res, $("#nuAnn").val(), $("#nuEmi").val(), docs.tieneWord, function(data) {
                //fn_generaDocDesktop("documento?accion=creaDocx&nuAnn="+ $("#nuAnn").val()+"&nuEmi="+$("#nuEmi").val()+"&tiCap=03&proyecto=1", res, function(data) {
                    result = data;
                    console.log("resultado principal->"+data);
                    console.log("resultado result->"+result);

                    if (result !== "OK") {
                       bootbox.alert(result);
                    }
                });

            } else {
                alert_Danger("Generar Docx: ", docs.retval);
            }
        }
    }, 'text', false, false, "POST");
}

function fn_generaDocxPersonal() {
    console.log("acc: function fn_generaDocxPersonal");
    
    var pnuAnn = jQuery('#documentoPersonalEmiBean').find('#nuAnn').val();
    var pnuEmi = jQuery('#documentoPersonalEmiBean').find('#nuEmi').val();

    var rpta = fu_verificarDestinatario("1");
    var nrpta = rpta.substr(0, 1);
    if (nrpta === "1") {
        rpta = fu_verificarReferencia();
        nrpta = rpta.substr(0, 1);
        if (nrpta === "1") {
            rpta = fu_verificarChangeDocumentoEmiPersonal();
            nrpta = rpta.substr(0, 1);
            if (nrpta === "1") {
                alert_Warning("Emisión :", "Necesita grabar los cambios");
            } else {
                if (!!pnuAnn) {
                    var p = new Array();
                    p[0] = "accion=goRutaGeneraDocx";
                    p[1] = "nuAnn=" + pnuAnn;
                    p[2] = "nuEmi=" + pnuEmi;
                    p[3] = "tiOpe=1";
                    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
                        var result;
                        eval("var docs=" + data);
                        if (typeof (docs) != "undefined" && typeof (docs.retval) != 'undefined' && docs.retval != "") {
                            if (docs.retval === "OK") {
//                             result = fn_generaDocApplet(docs.noUrl,docs.noDoc);
//                            if (result!="OK"){
//                               bootbox.alert(result);
//                            }
                                console.log("acc: docs.noUrl->"+docs.noUrl);
                                console.log("acc: docs.noDoc->"+docs.noDoc);
                                
                                //fn_generaDocApplet(docs.noUrl, docs.noDoc, function(data) {
                                fn_generaDocDesktop(docs.noUrl, docs.noDoc, function(data) {
                                    result = data;
                                    console.log("resultado principal->"+data);
                                    console.log("resultado result->"+result);
                                    
                                    if (result !== "OK") {
                                       bootbox.alert(result);
                                    }
                                });


                            } else {
                                alert_Danger("Generar Docx: ", docs.retval);
                            }
                        }
                    }, 'text', false, false, "POST");
                }
            }
        } else {
            alert_Info("Emisión :", rpta);
        }
    } else {
        alert_Info("Emisión :", rpta);
    }
    return false;
}

function fn_cargaObjDoc(pnuAnn, pnuEmi, ptiOpe) {
    var p = new Array();
    p[0] = "accion=goRutaCargaDoc";
    p[1] = "nuAnn=" + pnuAnn;
    p[2] = "nuEmi=" + pnuEmi;
    p[3] = "tiOpe=" + ptiOpe;
    var result = "ERROR";
    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
        eval("var docs=" + data);
        if (typeof (docs) !== "undefined" && typeof (docs.nuAnn) !== 'undefined' && docs.nuAnn !== "") {
            //fn_cargaDocApplet(docs.noUrl, docs.noDoc, function(data) {
            fn_cargaDocDesktop(docs.noUrl, docs.noDoc, function(data) {
                result = data;
            });
        }
    }, 'text', false, false, "POST");

    return result;
}

function fn_verificaSiExisteDoc(rutaDoc) {
    /*
     //FUNCION REEMPPLAZADA 
     var retval = "NO";
     var appletObj = jQuery('#firmarDocumento');
     try{
     retval=appletObj[0].verificaSiExisteDoc(rutaDoc,false);
     }catch(ex){
     retval = "NO";
     }
     return retval;
     */
}
function fu_callEventoTablaAnexosdetalle() {
    var idTabla = "tblAnexosDetalle";
    fu_eventoGridTabla(idTabla, paramConfAnexosDetalle);
}

function fn_verRequisitoObj(frm_docOrigenBean_pkExp) {
    if(!!frm_docOrigenBean_pkExp&&allTrim(frm_docOrigenBean_pkExp).length>6){    
        var pnuAnnExp=frm_docOrigenBean_pkExp.substr(0,4);
        var pnuSecExp=frm_docOrigenBean_pkExp.substr(4);
        if (!!pnuAnnExp&&!!pnuSecExp) {
            var p = new Array();
            p[0] = "accion=goRequisitosExpDocExtSeg";
            p[1] = "pnuAnnExp=" + pnuAnnExp;
            p[2] = "pnuSecExp=" + pnuSecExp;
            ajaxCall("/srMesaPartes.do", p.join("&"), function(data) {
                if (data !== null)
                {
                    $("body").append(data);
                }
            }, 'text', false, false, "POST");

        }        
    }
    return false;
}

/*-----Inicio Hermes Enlace de expedientes 17/09/2018-----*/
function fn_verSeguimientoObjEE(pnuAnn, pnuEmi, pnuDes, pCondicion) {     
    
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goDocSeguimientoEE";
        p[1] = "pkEmi=" + pnuAnn + pnuEmi + pnuDes;
        p[2] = "pCondicion=" + pCondicion;
        //validando si existe seguimiento
        ajaxCall("/srDocObjeto.do", "accion=goDocSeguimientoRootEE&"+p[1], function(data) {
            if (data == null || data==""){
                alert_Info("Seguimiento :", "No se puede dar seguimiento a documentos sin destinatario o anulados.");
                return;
            }else{
                //si es ok 
                ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
                    if (data !== null){
                        $("body").append(data);
                    }
                }, 'text', false, false, "POST");
            }
        }, 'text', false, true, "GET");
     
    }
    return false;
}

function fn_verDetalleSeguimientoEE(vpkEmiDoc) {
    var p = new Array();
    p[0] = "accion=goDetalleSeguimientoEE";
    p[1] = "pkEmi=" + vpkEmiDoc;
    p[2] = "pkExp=" + jQuery('#frm_docOrigenBean_pkExp').val();    
    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
        if (data !== null)
        {
            refreshScript("divDetalleDocSeguimientoEE", data);
        }
    }, 'text', false, false, "POST");
    return false;
}
/*-----Fin Hermes Enlace de expedientes 17/09/2018-----*/

/*interoperabilidad segdi mvaldera*/
function fn_grabarEnvioMesaVirtual(pnuAnn,pnuEmi,pexisteDoc,pexisteAnexo,tipoBandeja) {
    var nDeDepDes = allTrim($("#txtDeDepDes").val());
    var nDeNomDes = allTrim($("#txtDeNomDes").val());
    var nDeCarDes=allTrim($("#txtDeCarDes").val());
    var vValidar=fu_validarMesaVirtual();
    
    if (vValidar===1)
    {
        var p = new Array();
        //p[0] = "accion=goDocRutaAbrir";
        p[0] = "accion=goUpdEnvioMesaVirtual";
        p[1] = "nuAnn=" + pnuAnn;
        p[2] = "nuEmi=" + pnuEmi;
        p[3] = "nDeDepDes=" + nDeDepDes;
        p[4] = "nDeNomDes=" + nDeNomDes;
        p[5] = "nDeCarDes=" + nDeCarDes;
        /*HPB 27/05/2020 - Inicio - Requerimiento Paginación emitidos*/
        var paginaRecepcionSelect = jQuery('#txtPaginaEmisionSalta').val();
        if(typeof(paginaRecepcionSelect)==="undefined" || paginaRecepcionSelect===null || paginaRecepcionSelect===""){
            paginaRecepcionSelect = 1;
        }
        /*HPB 27/05/2020 - Fin - Requerimiento Paginación emitidos*/  
        //ajaxCallSendJson(url, jsonString, function(data) {
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {

            if (data.substring(0,1) === "1") {
                alert_Sucess("MENSAJE", "Se envió el documento a la mensajería correctamente");
                if(tipoBandeja==="EDIT"){
                    editarDocumentoEmiClick(pnuAnn, pnuEmi,pexisteDoc,pexisteAnexo);           
                }
                if(tipoBandeja==="NEW"){
                    EnvioOkMensajeria();
                }
                else {
                      ajaxCall("/srDocumentoAdmEmision.do?accion=goInicio&vPagina="+paginaRecepcionSelect, $('#buscarDocumentoEmiBean').serialize(), function(data) {
                            refreshScript("divTablaEmiDocumenAdm", data);
                        }, 'text', false, false, "POST");
               }
                removeDomId('windowConsultaAnexo');
            } else {
                alert_Danger("ERROR:", data.substring(1,data.length));
            }
        }, 'text', false, false, "POST");    
    }                            
    return;
}

function fu_validarMesaVirtual(){
    var valRetorno=1;//no buscar por referencia    
    var nDeDepDes = allTrim($("#txtDeDepDes").val());
    var nDeNomDes = allTrim($("#txtDeNomDes").val());
    var nDeCarDes=allTrim($("#txtDeCarDes").val());
    
    if(!(typeof(nDeDepDes)!=="undefined" && nDeDepDes!==null && nDeDepDes!==""))
    {
        valRetorno = 0;
        alert_Info("Aviso", "Debe Ingresar por lo menos una dependencia u oficina");
    }
    /*HPB 27/03/2020 - Inicio - Requerimiento Validacion Dependencia Destinatario*/
    var depenDestinoLenght = nDeDepDes.length;
    if(!$.trim(nDeDepDes)){
        valRetorno = 0;
        alert_Info("Aviso", "Debe Ingresar por lo menos una dependencia u oficina");
    }      
    if(depenDestinoLenght > 100)
    {
        valRetorno = 0;
        alert_Info("Aviso", "La dependencia u oficina excede el límite de 100 caracteres permitidos.");
    } 
    var cadena ="^[0-9a-zA-Z ]+$";
    var re = new RegExp(cadena);
    if (!nDeDepDes.match(re)) {
        valRetorno = 0;
        alert_Info("Aviso", "No se permiten caracteres especiales ni tildes.");
    }     
    /*HPB 27/03/2020 - Fin - Requerimiento Validacion Dependencia Destinatario*/
    if(!(typeof(nDeNomDes)!=="undefined" && nDeNomDes!==null && nDeNomDes!==""))
    {
        valRetorno = 0;
        alert_Info("Aviso", "Debe Ingresar por lo menos un Destinatario o Persona");
    }
    
    if(!(typeof(nDeCarDes)!=="undefined" && nDeCarDes!==null && nDeCarDes!==""))
    {
        valRetorno = 0;
        alert_Info("Aviso", "Debe Ingresar por lo menos un cargo del destinatario");
    }
    return valRetorno;
}

function fn_activarInter(siInter,tipoEnv){
    console.log("tipo"+siInter);
    console.log("tipo"+tipoEnv);
    if (tipoEnv==="-1" || tipoEnv==="3" ){/*--28/08/19 HPB Devolucion Doc a Oficina--*/
    //if (tipoEnv==="-1"){
        if (siInter==="S"){
             jQuery('#viewFisico').hide();
             jQuery('#viewInter').show();

           /*COMENTADO YPA SIS
            *   var pDesDes=$("#txtDeDepDes").val().trim();
             if (pDesDes!="" && pDesDes!=null){
                document.getElementById("txtDeDepDes").readOnly = true; 
             }*/
             
             var pNomDes=$("#txtDeNomDes").val();
             if (pNomDes!="" && pNomDes!=null){
                 document.getElementById("txtDeNomDes").readOnly = true; 
             }

             var pCarDes=$("#txtDeCarDes").val();
             if (pCarDes!="" && pCarDes!=null){
                 document.getElementById("txtDeCarDes").readOnly = true; 
             }
             jQuery('#viewChkInter').show();
             document.getElementById("chkviewInter").checked = true;             
             jQuery('#viewFisicoF').hide();
             jQuery('#viewInterF').show();
         }else{
             jQuery('#viewFisico').show();
             jQuery('#viewInter').hide();
             jQuery('#viewChkInter').hide();
             jQuery('#viewFisicoF').show();
             jQuery('#viewInterF').hide();
         }   
    }else{
        if (tipoEnv==="2"){
            jQuery('#viewFisico').hide();
            jQuery('#viewInter').show();             
            //document.getElementById("txtDeDepDes").readOnly = true;
            document.getElementById("txtDeNomDes").readOnly = true;
            document.getElementById("txtDeCarDes").readOnly = true;
            jQuery('#viewFisicoF').hide();
            jQuery('#viewInterF').show();
         }else{
             jQuery('#viewFisico').show();
             jQuery('#viewInter').hide();
             jQuery('#viewChkInter').hide();
             jQuery('#viewFisicoF').show();
             jQuery('#viewInterF').hide();
         }
    }
}

function fn_changeChkVerInter(chk){
    if(chk.checked){    
        jQuery('#viewFisico').hide();
        jQuery('#viewInter').show();             
        jQuery('#viewFisicoF').hide();
        jQuery('#viewInterF').show();        
    }else{
        jQuery('#viewFisico').show();
        jQuery('#viewInter').hide();        
        jQuery('#viewFisicoF').show();
        jQuery('#viewInterF').hide();
    }    
}
/*interoperabilidad segdi mvaldera*/

/*Hermes 28/01/2019 - Requerimiento Mensajeria: Acta 0005-2019    */

function fn_verSeguimientoObjDevolucion(pnuAnn, pnuEmi, pnuDes, pnuSec) {     
    var result;
    if (!!pnuAnn) {
        //alert("entro="+pnuAnn+" pnuEmi="+pnuEmi+" pnuDes="+pnuDes);
         var p = new Array();
                p[0] = "accion=goDocSeguimiento";
                p[1] = "pkEmi=" + pnuAnn + pnuEmi + pnuDes;
                p[2] = "pkNuSecDevolucion=" + pnuSec;/*12/09/2019 HPB Devolucion*/
                //validando si existe seguimiento
                ajaxCall("/srDocObjeto.do", "accion=goDocSeguimientoRoot&"+p[1], function(data) {
                       if (data == null || data==""){
                           alert_Info("Seguimiento :", "No se puede dar seguimiento a documentos sin destinatario o anulados.");
                           return;
                       }else{ 
                           //si es ok 
                           //alert("ok="+p.join("&"));
                           ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
                               if (data !== null){
                                   $("body").append(data);
                               }
                           }, 'text', false, false, "POST");
                       }
                }, 'text', false, true, "GET");         
                
        
   }
   /*
   var q = new Array();
        q[1] = "pkEmi=" + pnuAnn + pnuEmi + pnuDes;
        //alert("accion=goDocAutorizado&"+q[1]);
        ajaxCall("/srDocObjeto.do", "accion=goDocAutorizado&"+q[1], function(data) {
        result=data; 
        }, 'text', false, true, "GET");
     */   
   
    return false;
}

function fn_AdjuntarDocumentosMsj(pnuAnn, pnuEmi, pexisteAnexo) {

    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goAdjuntarDocMsjEnviados";
        p[1]="pnuAnn="+pnuAnn;
        p[2]="pnuEmi="+pnuEmi;
        p[3]="pexisteAnexo="+pexisteAnexo;
     
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            if (data !== null)
            {
                $("body").append(data);
            }
        }, 'text', false, false, "POST");

    }
    return false;
}

function fu_callEventoTablaAnexosdetalleAdicional() {
    pnumFilaSelect=0;
    //var idTabla = "tblAnexosDetalle1";
    //fu_eventoGridTabla(idTabla, paramConfAnexosDetalleAdicional);
    
    var oTable;
    oTable = $('#tblAnexosDetalle1').DataTable( {
            "bPaginate": false,
            "bFilter": false,
            "bSort": true,
            "bInfo": true,
            "bAutoWidth": true,
            "bDestroy": true,
            "sScrollY": "100px",
            "bScrollCollapse": false,
            "oLanguage": {
                "sZeroRecords": "No tiene cargo adjuntado.",
                "sInfo": "Registros: _TOTAL_ ",
                "sInfoEmpty": ""
            },
            "aoColumns": [
                {"bSortable": true},
                {"bSortable": true},
                {"bSortable": true},
                {"bSortable": true}, 
                {"bSortable": true},
                {"bSortable": false}
            ]    
    } ); 
    function showdivToolTipNew(elemento, text){
        $('#divflotante').html(text);

        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotante").css({left: x, top: y});

        //document.getElementById('divflotante').style.top = elemento.top + 24;
        //document.getElementById('divflotante').style.left = elemento.left;		
        document.getElementById('divflotante').style.display = 'block';

        return;
    }
    
//    $("#tblAnexosDetalle1 tbody td").hover(
//            function() {
//                $(this).attr('id', 'divtitlemostrar');
//                //console.log($(this).index());
//                var index = $(this).index();
//                if (index >= 0 && index <= 3) {
//                    showdivToolTipNew($('#divtitlemostrar').position(), $(this).html());
//                }
//            },
//            function() {
//                $('#divtitlemostrar').removeAttr('id');
//                $('#divflotante').hide();
//            }
//    );

    $("#tblAnexosDetalle1 tbody tr").click(function(e) {
        if ($(this).hasClass('row_selected')) {
            //$(this).removeClass('row_selected');
            //jQuery('#txtpnuAnn').val("");
        }
        else {
            oTable.$('tr.row_selected').removeClass('row_selected');
            $(this).addClass('row_selected');

            /*obtiene datos*/
            //alert($(this).children('td')[12].innerHTML);
            //jQuery('#txtTextIndexSelect').val("0");
            if (typeof($(this).children('td')[12]) !== "undefined") {
                jQuery('#txtpnuAnn').val($(this).children('td')[1].innerHTML);
                jQuery('#txtpnuEmi').val($(this).children('td')[2].innerHTML);
                jQuery('#txtpnuDes').val($(this).children('td')[5].innerHTML);
                pnumFilaSelect = $(this).index();
            }
            //editarDocumentoRecep($(this).children('td')[12].innerHTML,$(this).children('td')[13].innerHTML,$(this).children('td')[15].innerHTML);
            /* comentado xq los botones de ver documento y ver anexos, ya se encuentran el la tabla
             var sData = $(this).find('td');
             passArrayToHtmlTabla("txtTextIndexSelect",sData);*/


        }
    });
    $("#tblAnexosDetalle1 tbody tr").dblclick(function() {
        //jQuery('#txtTextIndexSelect').val("-1"); 
    });
    if(jQuery('#tblAnexosDetalle1 >tbody >tr').length > 0){
        //$('#myTableFixed >tbody >tr').eq(0).addClass('row_selected');
        //var pauxNumFilaSelect = typeof(pnumFilaSelect)!=="undefined"? pnumFilaSelect:0;
        try{
            if(jQuery('#tblAnexosDetalle1 >tbody >tr').eq(pnumFilaSelect).length === 1){
                jQuery('#tblAnexosDetalle1 >tbody >tr').eq(pnumFilaSelect).trigger("click");
                jQuery('#tblAnexosDetalle1 >tbody >tr').eq(pnumFilaSelect).focus();
            }else{
                pnumFilaSelect=0;
            }
        }catch(ex){
            pnumFilaSelect=0;
        }
    }
    jQuery('#tblAnexosDetalle1 >tbody >tr').keydown(function(evento){
       if(evento.which===38){//up
            if (jQuery("#tblAnexosDetalle1 >tbody >tr").eq(pnumFilaSelect).prev('tr').length===0){
                pnumFilaSelect=$("#tblAnexosDetalle1 >tbody >tr").length;
            }
            pnumFilaSelect--;
            jQuery("#tblAnexosDetalle1 >tbody >tr").eq(pnumFilaSelect).trigger("click");
            jQuery("#tblAnexosDetalle1 >tbody >tr").eq(pnumFilaSelect).focus();
       }else if(evento.which===40){//down
            if ($("#tblAnexosDetalle1 >tbody >tr").eq(pnumFilaSelect).next('tr').length===0){
                pnumFilaSelect=-1;
            }           
            pnumFilaSelect++;
            jQuery("#tblAnexosDetalle1 >tbody >tr").eq(pnumFilaSelect).trigger("click");               
            jQuery("#tblAnexosDetalle1 >tbody >tr").eq(pnumFilaSelect).focus();           
       }
       evento.preventDefault();
    });     
}

function fu_callEventoTablaAnexosdetalleAdicionalMensajeria() {
    pnumFilaSelect=0;    
    var oTable;
    oTable = $('#tblAnexosDetalleNew').DataTable( {
            "bPaginate": false,
            "bFilter": false,
            "bSort": true,
            "bInfo": true,
            "bAutoWidth": true,
            "bDestroy": true,
            "sScrollY": "100px",
            "bScrollCollapse": false,
            "oLanguage": {
                "sZeroRecords": "No tiene cargo adjuntado.",
                "sInfo": "Registros: _TOTAL_ ",
                "sInfoEmpty": ""
            },
            "aoColumns": [
                {"bSortable": true},
                {"bSortable": true},
                {"bSortable": true},
                {"bSortable": true}, 
                {"bSortable": true},
                {"bSortable": false}
            ]    
    } ); 
    function showdivToolTipNew(elemento, text){
        $('#divflotante').html(text);

        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotante").css({left: x, top: y});	
        document.getElementById('divflotante').style.display = 'block';

        return;
    }
    
//    $("#tblAnexosDetalleNew tbody td").hover(
//            function() {
//                $(this).attr('id', 'divtitlemostrar');
//                //console.log($(this).index());
//                var index = $(this).index();
//                if (index >= 0 && index <= 3) {
//                    showdivToolTipNew($('#divtitlemostrar').position(), $(this).html());
//                }
//            },
//            function() {
//                $('#divtitlemostrar').removeAttr('id');
//                $('#divflotante').hide();
//            }
//    );

    $("#tblAnexosDetalleNew tbody tr").click(function(e) {
        if ($(this).hasClass('row_selected')) {
            //$(this).removeClass('row_selected');
            //jQuery('#txtpnuAnn').val("");
        }
        else {
            oTable.$('tr.row_selected').removeClass('row_selected');
            $(this).addClass('row_selected');

            /*obtiene datos*/
            //alert($(this).children('td')[12].innerHTML);
            //jQuery('#txtTextIndexSelect').val("0");
            if (typeof($(this).children('td')[12]) !== "undefined") {
                jQuery('#txtpnuAnn').val($(this).children('td')[1].innerHTML);
                jQuery('#txtpnuEmi').val($(this).children('td')[2].innerHTML);
                jQuery('#txtpnuDes').val($(this).children('td')[5].innerHTML);
                pnumFilaSelect = $(this).index();
            }
        }
    });
    $("#tblAnexosDetalleNew tbody tr").dblclick(function() {
        //jQuery('#txtTextIndexSelect').val("-1"); 
    });
    if(jQuery('#tblAnexosDetalleNew >tbody >tr').length > 0){
        //$('#myTableFixed >tbody >tr').eq(0).addClass('row_selected');
        //var pauxNumFilaSelect = typeof(pnumFilaSelect)!=="undefined"? pnumFilaSelect:0;
        try{
            if(jQuery('#tblAnexosDetalleNew >tbody >tr').eq(pnumFilaSelect).length === 1){
                jQuery('#tblAnexosDetalleNew >tbody >tr').eq(pnumFilaSelect).trigger("click");
                jQuery('#tblAnexosDetalleNew >tbody >tr').eq(pnumFilaSelect).focus();
            }else{
                pnumFilaSelect=0;
            }
        }catch(ex){
            pnumFilaSelect=0;
        }
    }
    jQuery('#tblAnexosDetalleNew >tbody >tr').keydown(function(evento){
       if(evento.which===38){//up
            if (jQuery("#tblAnexosDetalleNew >tbody >tr").eq(pnumFilaSelect).prev('tr').length===0){
                pnumFilaSelect=$("#tblAnexosDetalleNew >tbody >tr").length;
            }
            pnumFilaSelect--;
            jQuery("#tblAnexosDetalleNew >tbody >tr").eq(pnumFilaSelect).trigger("click");
            jQuery("#tblAnexosDetalleNew >tbody >tr").eq(pnumFilaSelect).focus();
       }else if(evento.which===40){//down
            if ($("#tblAnexosDetalleNew >tbody >tr").eq(pnumFilaSelect).next('tr').length===0){
                pnumFilaSelect=-1;
            }           
            pnumFilaSelect++;
            jQuery("#tblAnexosDetalleNew >tbody >tr").eq(pnumFilaSelect).trigger("click");               
            jQuery("#tblAnexosDetalleNew >tbody >tr").eq(pnumFilaSelect).focus();           
       }
       evento.preventDefault();
    });     
}
/*Hermes 28/01/2019 - Requerimiento Mensajeria: Acta 0005-2019*/
/*--HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS--*/
function fn_ArchivarDocumentosObsMesaVirtual(pnuAnn, pnuEmi) {

    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goArcDocObsMesaPartesVirtual";
        p[1] = "pnuAnn="+pnuAnn;
        p[2] = "pnuEmi="+pnuEmi;
     
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            if (data !== null)
            {
                $("body").append(data);
            }
        }, 'text', false, false, "POST");
    }
    return false;
}
/*--HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS--*/
/*[HPB] 06/11/20 Inicio-Modificaciones en el envío de documentos. Listado de entidades que interoperan*/
function fn_activarInterMod(siInter,tipoEnv){
    console.log("tipo"+siInter);
    console.log("tipo"+tipoEnv);
    if (tipoEnv==="-1" || tipoEnv==="3" ){/*--28/08/19 HPB Devolucion Doc a Oficina--*/
        if (siInter==="S"){            
             jQuery('#viewFisico').hide();
             jQuery('#viewInter').show();
             jQuery('#viewInterDatos').show();

          /* COMENTADO YPA SIS  
           *  var pDesDes=$("#txtDeDepDes").val().trim();
             if (pDesDes!="" && pDesDes!=null){
                document.getElementById("txtDeDepDes").readOnly = true; 
             }*/
             
             var pNomDes=$("#txtDeNomDes").val();
             if (pNomDes!="" && pNomDes!=null){
                 document.getElementById("txtDeNomDes").readOnly = true; 
             }

             var pCarDes=$("#txtDeCarDes").val();
             if (pCarDes!="" && pCarDes!=null){
                 document.getElementById("txtDeCarDes").readOnly = true; 
             }            
             jQuery('#viewFisicoF').hide();
             jQuery('#viewInterF').show();
             $("#envio3").prop("checked", true);
         }else{
             jQuery('#viewFisico').show();
             jQuery('#viewInter').hide();
             jQuery('#viewInterDatos').hide();
             jQuery('#viewFisicoF').show();
             jQuery('#viewInterF').hide();
         }   
    }else{
        if (tipoEnv==="2"){
            jQuery('#viewFisico').hide();
            jQuery('#viewInter').show(); 
            jQuery('#viewInterDatos').show();
           // document.getElementById("txtDeDepDes").readOnly = true;
            document.getElementById("txtDeNomDes").readOnly = true;
            document.getElementById("txtDeCarDes").readOnly = true;
            $('input[name=envio]').attr("disabled",true);
            jQuery('#viewFisicoF').hide();
            jQuery('#viewInterF').show();
            $("#envio3").prop("checked", true);
         }else{
            if (siInter==="S"){
                jQuery('#viewFisico').hide();
                jQuery('#viewInter').show(); 
                jQuery('#viewInterDatos').show();
               // document.getElementById("txtDeDepDes").readOnly = true;
                document.getElementById("txtDeNomDes").readOnly = true;
                document.getElementById("txtDeCarDes").readOnly = true;
                //$('input[name=envio]').attr("disabled",true);
                jQuery('#viewFisicoF').hide();
                jQuery('#viewInterF').show();
                $("#envio3").prop("checked", true);
            }else{             
                jQuery('#viewFisico').show();
                jQuery('#viewInter').hide();
                jQuery('#viewInterDatos').hide();
                jQuery('#viewFisicoF').show();
                jQuery('#viewInterF').hide();
                //$('input[name=envio]').attr("disabled",true);
            }
         }
    }
}
function fn_changeChkVerInterMP(chk){  
    switch (chk) {
        case '0':
            jQuery('#viewFisico').show();
            jQuery('#viewFisicoF').show();
            $("#envio1").prop("checked", true);
             $('#divMensajeria').show(); //ADD YPA SIS
            break;
        case '1':
            jQuery('#viewFisico').show();
            jQuery('#viewFisicoF').show();
            $("#envio2").prop("checked", true);
            $('#divMensajeria').hide(); //ADD YPA SIS
            break;
    }
}

function fn_changeChkVerInterMV(chk){  
    switch (chk) {
        case '0':
            jQuery('#viewInter').show();
            jQuery('#viewInterDatos').hide(); 
            jQuery('#viewFisicoF').show();
            jQuery('#viewInterF').hide();
            $("#envio1").prop("checked", true);
            $('#divMensajeria').show(); //ADD YPA SIS
            break;
        case '1':
            jQuery('#viewInter').show();
            jQuery('#viewInterDatos').hide(); 
            jQuery('#viewFisicoF').show();
            jQuery('#viewInterF').hide();
            $("#envio2").prop("checked", true);
            $('#divMensajeria').hide(); //ADD YPA SIS
            break;
        case '2':
            jQuery('#viewInter').show(); 
            jQuery('#viewInterDatos').show();  
            jQuery('#viewFisicoF').hide();
            jQuery('#viewInterF').show(); 
            $("#envio3").prop("checked", true);
            $('#divMensajeria').hide(); //ADD YPA SIS
            break;
    }
}

function fn_changeChkVerInterDatos(chk){
    if(chk.checked){    
        jQuery('#viewFisico').hide();
        jQuery('#viewInter').show();
        jQuery('#viewInterDatos').show();             
        jQuery('#viewFisicoF').hide();
        jQuery('#viewInterF').show();
        $("#envio3").prop("checked", true);
    }else{
        jQuery('#viewFisico').show();
        jQuery('#viewInterDatos').hide();   
        jQuery('#viewInter').hide();
        jQuery('#viewFisicoF').show();
        jQuery('#viewInterF').hide();
        $("#envio1").prop("checked", true);
        $('#divMensajeria').show(); //ADD YPA SIS
    }    
}
/*[HPB] 06/11/20 Fin-Modificaciones en el envío de documentos. Listado de entidades que interoperan*/
/*-- [HPB] Inicio 26/09/22 OS-0000768-2022 --*/
function fn_zipearAnexos(pnuAnn, pnuEmi, ptipoDoc, pnuDoc){
    var url = "/srDocObjeto.do/downloadZip/";
    var urlFullPath = pRutaContexto + "/" + pAppVersion + url;
    var ajax = new XMLHttpRequest();
    var momentoActual = new Date();
    var fechaActual =  momentoActual.getFullYear()+ "" +(momentoActual.getMonth() +1)+ "" +momentoActual.getDate();
    var horaActual = momentoActual.getHours()+ "" + momentoActual.getMinutes()+ "" +momentoActual.getSeconds(); 
    var nombreZip;

    pnuDoc = pnuDoc.replace('/','-');
    nombreZip = ptipoDoc+" "+pnuDoc+"-anexos-"+fechaActual+"-"+horaActual;   
    ajax.open("Post", urlFullPath, true);
    ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ajax.responseType = "blob";
    ajax.onreadystatechange = function () {
        if (this.readyState === 4) {
            var blob = new Blob([this.response], { type: "application/octet-stream" });
            var fileName = nombreZip+".zip";
            saveAs(blob, fileName);
            alert_Sucess("MENSAJE", "Se descargo el archivo ZIP correctamente");
        }
    };
    ajax.send("pnuAnn="+pnuAnn+"&pnuEmi="+pnuEmi+"");
}
/*-- [HPB] Fin 26/09/22 OS-0000768-2022 --*/