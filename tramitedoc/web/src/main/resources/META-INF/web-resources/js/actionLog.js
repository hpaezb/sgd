function fn_inicializaActionDocLog(){   
    jQuery('#buscarAccionLog').find('#esFiltroFecha').val("2");//solo año
    jQuery('#buscarAccionLog').find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 5});
//    jQuery('#buscarAccionLog').find('#nuDocumentoAccion').change(function() {
//        $(this).val(replicate($(this).val(), 6));
//    });    
    //changeTipoBusqActionDocLog("0");
    $('input').keypress(function(e) {
        if (e.which == 13) {
            return false;
        }
    });     
}

function changeTipoBusqActionDocLog(tipo) {
    jQuery('#sTipoBusqueda').val(tipo);
    var validaFiltroDoc = fu_validaFiltroActionLogDoc();
    if (validaFiltroDoc === "1") {
            submitAjaxFormActionDocLog(tipo);
    }
}

function fu_validaFiltroActionLogDoc(){
    var valRetorno = "1";
    var tipoDoc = $('#tipoDocumentoAccion').val();
    var numeroDoc = $('#nuDocumentoAccion').val();    
    
    if((typeof(tipoDoc)==="undefined" || tipoDoc===null || tipoDoc==="")){
        alert_Warning("Buscar: ","Seleccionar tipo de documento como filtro de búsqueda.");
        valRetorno = "0";
    }  
    
    if((typeof(numeroDoc)==="undefined" || numeroDoc===null || numeroDoc==="")){
        alert_Warning("Buscar: ","Ingresar número de documento como filtro de búsqueda.");
        valRetorno = "0";
    }   
    
    if(valRetorno==="1" && !fu_cantDocLog(tipoDoc, numeroDoc)){ 
        alert_Warning("Buscar: ","Se encontraron varios registros. Adicionar más datos en el número de documento para mostrar el resultado.");
        valRetorno = "0";
    }
    
    return valRetorno;
}

function fu_cantDocLog(tipoDoc, numeroDoc) {
    var valRetorno = true;    
    var p={coFiltro:tipoDoc+','+numeroDoc};
    
    ajaxCall("/srAction.do?accion=goValidaCantDocLog", p, function (data) {
        if (data.coRespuesta === "1") {
            valRetorno = false;
        }else{
            valRetorno = true;
        }
    }, 'json', true, false, "POST");

    return valRetorno;   
}

function submitAjaxFormActionDocLog(tipo) {
    var validaFiltro = "1";//fu_validaFiltroActionDocLog(tipo);
    if (validaFiltro === "1") {
        ajaxCall("/srAction.do?accion=goInicio", $('#buscarAccionLog').serialize(), function(data) {
            refreshScript("divListaAcciones", data);
        }, 'text', false, false, "POST");
    }
    return false;
}

function fu_eventoTablaActionDocLog() {
    var oTable;
     oTable = $('#myTableFixed');  
    
    function showdivToolTip(elemento, text){
        $('#divflotante').html(text);

        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotante").css({left: x, top: y});		
        document.getElementById('divflotante').style.display = 'block';

        return;
    }    
    
    $("#myTableFixed tbody tr").click(function(e) {
        if ($(this).hasClass('row_selected')) {
            //$(this).removeClass('row_selected');
            //jQuery('#txtpnuAnn').val("");
        }else {
            //oTable.$('tr.row_selected').removeClass('row_selected');
            $('tr.row_selected').removeClass('row_selected');
            $(this).addClass('row_selected');
            if(typeof($(this).children('td')[1]) !== "undefined"){
                jQuery('#txtpnuAnn').val($(this).children('td')[1].innerHTML);
                jQuery('#txtpnuEmi').val($(this).children('td')[2].innerHTML);
                jQuery('#txtCoEstadoDoc').val($(this).children('td')[12].innerHTML);//Hermes - 28/05/2019
                pnumFilaSelect = $(this).index();
            }                        
        }
    });  
    
    if(jQuery('#myTableFixed >tbody >tr').length > 0){
        try{
            if(jQuery('#myTableFixed >tbody >tr').eq(pnumFilaSelect).length === 1){
                jQuery('#myTableFixed >tbody >tr').eq(pnumFilaSelect).trigger("click");
                jQuery('#myTableFixed >tbody >tr').eq(pnumFilaSelect).focus();
            }else{
                pnumFilaSelect=0;
            }
        }catch(ex){
            pnumFilaSelect=0;
        }
    }
    jQuery('#myTableFixed >tbody >tr').keydown(function(evento){
       if(evento.which===38){//up
            if (jQuery("#myTableFixed >tbody >tr").eq(pnumFilaSelect).prev('tr').length===0){
                pnumFilaSelect=$("#myTableFixed >tbody >tr").length;
            }
            pnumFilaSelect--;
            jQuery("#myTableFixed >tbody >tr").eq(pnumFilaSelect).trigger("click");
            jQuery("#myTableFixed >tbody >tr").eq(pnumFilaSelect).focus();
       }else if(evento.which===40){//down
            if ($("#myTableFixed >tbody >tr").eq(pnumFilaSelect).next('tr').length===0){
                pnumFilaSelect=-1;
            }           
            pnumFilaSelect++;
            jQuery("#myTableFixed >tbody >tr").eq(pnumFilaSelect).trigger("click");               
            jQuery("#myTableFixed >tbody >tr").eq(pnumFilaSelect).focus();           
       }
       evento.preventDefault();
    });    
}

function fu_cleanActionDocLog(tipo){
    if (tipo==="1") {
        jQuery("#tipoDocumentoAccion option[value=]").prop("selected", "selected");
        jQuery("#nuDocumentoAccion").val("");
        jQuery("#fechaFiltro").showDatePicker({defaultOpcionSelected: 5});
    }
}

function fu_validaFiltroActionDocLog(tipo) {
    var valRetorno = "1";
    jQuery('#sFeEmiIni').val(jQuery('#sCoAnnio').parent('td').find('#fechaFiltro').attr('fini'));
    jQuery('#sFeEmiFin').val(jQuery('#sCoAnnio').parent('td').find('#fechaFiltro').attr('ffin'));    
    
    
    //var pEsIncluyeFiltro = $('#buscarDocumentoEmiBean').find('#esIncluyeFiltro1').is(':checked');
    var vFechaActual = jQuery('#txtFechaActual').val();
    if(tipo==="0"){
        valRetorno = fu_validaFiltroActionDocLogFiltrar(vFechaActual);     
    }else if(tipo==="1"){      
//      valRetorno = fu_validarBusquedaXReferencia(tipo);  
//      if(valRetorno==="1"){
//        valRetorno = fu_validaFiltroEmiDocAdmBuscar();  
//        if(valRetorno==="1"){
//            if(pEsIncluyeFiltro){
//               valRetorno = fu_validaFiltroEmiDocAdmFiltrar(vFechaActual); 
//            }else{
//               valRetorno = setAnnioNoIncludeFiltroEmi();
//            }
//        }
//      }
    }    
    return valRetorno;
}

function fu_validaFiltroActionDocLogFiltrar(vFechaActual){
    var valRetorno = "1";
    if (valRetorno==="1") {
        fu_obtenerEsFiltroFechaActionDocLog('buscarAccionLog');
        var pEsFiltroFecha = jQuery('#buscarAccionLog').find("#esFiltroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
        if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
           if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
                var pAnnio = jQuery('#buscarAccionLog').find('#sCoAnnio').val();
                if(pAnnio!==null && pAnnio!=="null" && typeof(pAnnio)!=="undefined" && pAnnio!==""){
                    var vValidaNumero = fu_validaNumero(pAnnio);
                    if (vValidaNumero !== "OK") {
                       bootbox.alert("Año debe ser solo números.");
                        valRetorno = "0";
                    }                
                }               
           }   
            
            var vFeInicio = jQuery("#sFeEmiIni").val();
            var vFeFinal = jQuery("#sFeEmiFin").val();
            if(valRetorno==="1" /*&& pEsFiltroFecha==="1"*/){
              var pAnnioBusq = verificarAñoBusquedaFiltro(vFeInicio,vFeFinal);
                if(pAnnioBusq!==null && pAnnioBusq!=="null" && typeof(pAnnioBusq)!=="undefined" && pAnnioBusq!==""){
                    var vValidaNumero = fu_validaNumero(pAnnioBusq);
                    if (vValidaNumero !== "OK") {
                       bootbox.alert("Año debe ser solo números.");
                        valRetorno = "0";
                    }else if(vValidaNumero ==="OK"){
                        jQuery('#buscarAccionLog').find('#sCoAnnio').val(pAnnioBusq);                          
                    }                
                }               
            }           
                
           if(pEsFiltroFecha==="1" /*|| pEsFiltroFecha==="3"*/){
               //VALIDA FECHAS
            
               if (valRetorno==="1") {
                    if (vFeInicio===""){
                       bootbox.alert('Debe ingresar Fecha Del');
                        valRetorno="0";
                    } else {
                        valRetorno=fu_validaFechaConsulta(vFeInicio,vFechaActual);
                        if (valRetorno!=="1") {
                           bootbox.alert("Error en Fecha Del : "+ valRetorno);
                            valRetorno="0";
                        }
                    }
               }

                if (valRetorno==="1") {
                    //VALIDA FECHAS
                    if (vFeFinal===""){
                       bootbox.alert('Debe ingresar Fecha Al');
                        valRetorno="0";
                    } else {
                        if(pEsFiltroFecha==="3"){
                            vFechaActual = obtenerFechaUltimoDiaMes(vFechaActual);
                        }
                        valRetorno=fu_validaFechaConsulta(vFeFinal,vFechaActual);
                        if (valRetorno!=="1") {
                           bootbox.alert("Error en Fecha Al : "+ valRetorno);
                            valRetorno="0";
                        }
                    }
                }
                //se verifica que fechas DEL sea mayor o igual que fecha AL
                if (valRetorno==="1") {
                    var vCantidadDias =  getNumeroDeDiasDiferencia(vFeInicio,vFeFinal);
                    if (vCantidadDias < 0){
                      bootbox.alert("La Fecha Del debe ser mayor o igual a Fecha Al");
                       valRetorno="0";
                    }   
                }
            }
        }
    }
    return valRetorno;    
}

function fu_obtenerEsFiltroFechaActionDocLog(nameForm){
    var opt = jQuery('#'+nameForm).find('#sCoAnnio').parent('td').find('#fechaFiltro').attr('optselected');
    if(opt==="1"||opt==="2"||opt==="4"||opt==="8"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("1"); 
    }else if(opt==="5"||opt==="6"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("2"); 
    }else if(opt==="3"||opt==="7"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("3"); 
    }
}

function fu_generarReporteActionLogPDF(){
   fu_generarReporteActionLog('PDF');  
}

function fu_generarReporteActionLogXLS(){
   fu_generarReporteActionLog('XLS'); 
}

function fu_generarReporteActionLog(pformatoReporte){
    var validaFiltroDoc = fu_validaFiltroActionLogDoc();
    if (validaFiltroDoc === "1") {
        var validaFiltro = "1";//fu_validaFiltroActionDocLog("0");
        if (validaFiltro === "1" || validaFiltro === "2") {
            ajaxCall("/srAction.do?accion=goExportarArchivoLista&pformatRepor="+pformatoReporte, $('#buscarAccionLog').serialize(), function(data) {
                if (data!==null) {
                    if(data.coRespuesta==="0"){
                        if(!!data.noUrl&&!!data.noDoc){
                            var param={urlDoc:data.noUrl,rutaDoc:data.noDoc};
                            runOnDesktop(accionOnDesktopTramiteDoc.abrirDocumento,param, function(data){
                                var result = data;
                            });
                        }
                    }else{
                       bootbox.alert(data.deRespuesta);
                    }
                }else{
                   bootbox.alert("La respuesta del servidor es nula.");
                }
            }, 'json', false, true, "POST");
        }        
    }    

    return false;
}