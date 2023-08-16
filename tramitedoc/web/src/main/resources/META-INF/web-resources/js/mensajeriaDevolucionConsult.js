function fn_iniDocMensajeriaDevolucion(){
    $('#buscarDocumentoDevolucionMensajeriaBean').find('#esFiltroFecha').val("2");//año
    $('#buscarDocumentoDevolucionMensajeriaBean').find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 5});
    
    $('#buscarDocumentoDevolucionMensajeriaBean').find('#esFiltroFechaEnvMsj').val("3");//hoy
    $('#buscarDocumentoDevolucionMensajeriaBean').find("#fechaFiltroEnvMsj").showDatePicker({defaultOpcionSelected: 4});
    $('#buscarDocumentoDevolucionMensajeriaBean').find('#esFiltroFechaDevOfi').val("3");//hoy
    $('#buscarDocumentoDevolucionMensajeriaBean').find("#fechaFiltroDevolucion").showDatePicker({defaultOpcionSelected: 4});
    pnumFilaSelect=0;
    changeTipoBusqMensajeriaDevolucion("0");
}

function changeTipoBusqMensajeriaDevolucion(tipo) {
    $('#buscarDocumentoDevolucionMensajeriaBean').find('#tipoBusqueda').val(tipo);
    submitAjaxFormBusDocMensajeriaDevolucion(tipo);
    mostrarOcultarDivBusqFiltro2();
}

function submitAjaxFormBusDocMensajeriaDevolucion(tipo) {
    var validaFiltro = fu_validaFormBusDocMensajeriaDevolucion(tipo);
    if (validaFiltro === "1") {
        ajaxCall("/srMensajeriaDevolucion.do?accion=goInicio", $('#buscarDocumentoDevolucionMensajeriaBean').serialize(), function(data) {
            refreshScript("divTablaDocMensajeriaDevolucion", data);
        }, 'text', false, false, "POST");
    }
    return false;
}

function fu_validaFormBusDocMensajeriaDevolucion(tipo) {
    var valRetorno = "1";
    $('#buscarDocumentoDevolucionMensajeriaBean').find('#feEmiIni').val($('#buscarDocumentoDevolucionMensajeriaBean').find('#coAnnio').parent('td').find('#fechaFiltro').attr('fini'));
    $('#buscarDocumentoDevolucionMensajeriaBean').find('#feEmiFin').val($('#buscarDocumentoDevolucionMensajeriaBean').find('#coAnnio').parent('td').find('#fechaFiltro').attr('ffin'));    
    
    $('#buscarDocumentoDevolucionMensajeriaBean').find('#feEmiIniEnvMSJ').val($('#buscarDocumentoDevolucionMensajeriaBean').find('#coAnnioEnvio').parent('td').find('#fechaFiltroEnvMsj').attr('fini'));
    $('#buscarDocumentoDevolucionMensajeriaBean').find('#feEmiFinEnvMSJ').val($('#buscarDocumentoDevolucionMensajeriaBean').find('#coAnnioEnvio').parent('td').find('#fechaFiltroEnvMsj').attr('ffin')); 
    
    $('#buscarDocumentoDevolucionMensajeriaBean').find('#feEmiIniDevOfi').val($('#buscarDocumentoDevolucionMensajeriaBean').find('#coAnnioDev').parent('td').find('#fechaFiltroDevolucion').attr('fini'));
    $('#buscarDocumentoDevolucionMensajeriaBean').find('#feEmiFinDevOfi').val($('#buscarDocumentoDevolucionMensajeriaBean').find('#coAnnioDev').parent('td').find('#fechaFiltroDevolucion').attr('ffin')); 

    var pEsIncluyeFiltro = $('#buscarDocumentoDevolucionMensajeriaBean').find('#esIncluyeFiltro1').is(':checked');
    var vFechaActual = $('#txtFechaActual').val();
    if(tipo==="0"){
        valRetorno = fu_validaFechasFormBusDocMensajeriaDevolucion(vFechaActual);  
    }else if(tipo==="1"){
        valRetorno = fu_validaBusDocMensajeriaDevolucion();
        if(valRetorno==="1"){
            if(pEsIncluyeFiltro){
               valRetorno = fu_validaFechasFormBusDocMensajeriaDevolucion(vFechaActual); 
            }else{
               valRetorno = setAnnioNoIncludeFiltroDocMensajeriaDevolucion();
            }
        }
    }    
    return valRetorno;
}

function fu_validaFechasFormBusDocMensajeriaDevolucion(vFechaActual){
    var valRetorno = "1";
    if (valRetorno==="1") {
        fu_obtenerEsFiltroFechaMensajeriaDevolucion('buscarDocumentoDevolucionMensajeriaBean');
        var pEsFiltroFecha = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find("#esFiltroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
        if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
           if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
                var pAnnio = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#coAnnio').val();
                if(pAnnio!==null && pAnnio!=="null" && typeof(pAnnio)!=="undefined" && pAnnio!==""){
                    var vValidaNumero = fu_validaNumero(pAnnio);
                    if (vValidaNumero !== "OK") {
                       bootbox.alert("Año debe ser solo números.");
                        valRetorno = "0";
                    }                
                }               
           }   
            
            var vFeInicio = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find("#feEmiIni").val();
            var vFeFinal = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find("#feEmiFin").val();
            if(valRetorno==="1"){
              var pAnnioBusq = verificarAñoBusquedaFiltro(vFeInicio,vFeFinal);
                if(pAnnioBusq!==null && pAnnioBusq!=="null" && typeof(pAnnioBusq)!=="undefined" && pAnnioBusq!==""){
                    var vValidaNumero = fu_validaNumero(pAnnioBusq);
                    if (vValidaNumero !== "OK") {
                       bootbox.alert("Año debe ser solo números.");
                        valRetorno = "0";
                    }else if(vValidaNumero ==="OK"){
                        jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#coAnnio').val(pAnnioBusq);                          
                    }                
                }               
            }           
                
           if(pEsFiltroFecha==="1"){
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

function fu_obtenerEsFiltroFechaMensajeriaDevolucion(nameForm){
    var opt = jQuery('#'+nameForm).find('#coAnnio').parent('td').find('#fechaFiltro').attr('optselected');
    if(opt==="1"||opt==="2"||opt==="4"||opt==="8"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("1"); 
    }else if(opt==="5"||opt==="6"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("2"); 
    }else if(opt==="3"||opt==="7"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("3"); 
    }
}

function fu_validaBusDocMensajeriaDevolucion() {
    var valRetorno = "1";
    
    upperCaseBuscarDocumentoMensajeriaDevolucion();
    
    var vNroDocumento = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#busNumDoc').val();
    var vNroExpediente = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#busNumExpediente').val();
    var vAsunto = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#busAsunto').val();
    var vBusquedaTipoPersona = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#busResultado').val();
    
    if((typeof(vNroDocumento)==="undefined" || vNroDocumento===null || vNroDocumento==="") &&
       (typeof(vNroExpediente)==="undefined" || vNroExpediente===null || vNroExpediente==="") &&
       (typeof(vAsunto)==="undefined" || vAsunto===null || vAsunto==="") &&
       (typeof(vBusquedaTipoPersona)==="undefined" || vBusquedaTipoPersona===null || vBusquedaTipoPersona===""))
    {
       alert_Warning("Buscar: ","Ingresar Algún parámetro de Búsqueda");
       valRetorno = "0";
    }
    return valRetorno;
}

function upperCaseBuscarDocumentoMensajeriaDevolucion(){
    jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#busNumDoc').val(fu_getValorUpperCase(jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#busNumDoc').val()));
    //jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#busNumExpediente').val(fu_getValorUpperCase(jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#busNumExpediente').val()));
    jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#busAsunto').val(fu_getValorUpperCase(jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#busAsunto').val()));
}

function setAnnioNoIncludeFiltroDocMensajeriaDevolucion(){
    var valRetorno = "1";
    fu_obtenerEsFiltroFechaMP('buscarDocumentoDevolucionMensajeriaBean');
    var pEsFiltroFecha = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find("#esFiltroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
    if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
       if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
            var pAnnio = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#coAnnio').val();
            if(pAnnio!==null && pAnnio!=="null" && typeof(pAnnio)!=="undefined" && pAnnio!==""){
                var vValidaNumero = fu_validaNumero(pAnnio);
                if (vValidaNumero !== "OK") {
                   bootbox.alert("Año debe ser solo números.");
                    valRetorno = "0";
                }                
            }               
       }     

        var vFeInicio = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find("#feEmiIni").val();
        var vFeFinal = jQuery('#buscarDocumentoDevolucionMensajeriaBean').find("#feEmiFin").val();

        if(valRetorno==="1"){
          var pAnnioBusq = obtenerAnioBusqueda(vFeInicio,vFeFinal);
            if(pAnnioBusq!==null && pAnnioBusq!=="null" && typeof(pAnnioBusq)!=="undefined" && pAnnioBusq!==""){
                var vValidaNumero = fu_validaNumero(pAnnioBusq);
                if (vValidaNumero !== "OK") {
                   bootbox.alert("Año debe ser solo números.");
                    valRetorno = "0";
                }else if(vValidaNumero ==="OK"){
                    jQuery('#buscarDocumentoDevolucionMensajeriaBean').find('#coAnnio').val(pAnnioBusq);                          
                }                
            }               
        }
    }
    return valRetorno;       
}

function fu_iniTblDocMensajeriaDevolucion(){
    var oTable;
    oTable = $('#myTableFixed').dataTable({
        "bPaginate": false,
        /*"bLengthChange": false,*/
        "bFilter": false,
        "bSort": true,
        "bInfo": true,
        "bAutoWidth": true,
        "bDestroy": true,
        "sScrollY": "470px",
        "bScrollCollapse": false,
        "oLanguage": {
            "sZeroRecords": "No se encuentran registros.",
            "sInfo": "Registros: _TOTAL_ ",
            "sInfoEmpty": ""
        },        
        "aoColumns": [
            {"bSortable": false},
            {"bSortable": false},
            {"bSortable": false},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},             
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},           
            {"bSortable": true},           
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},            
            {"bSortable": true},            
            {"bSortable": true}
        ],
        "aoColumnDefs": [
            {
                "sWidth": "4%",
                "aTargets": [ 2 ]
            },
            {
                "sWidth": "4%",
                "aTargets": [ 3 ]
            },{
                "sWidth": "4%",
                "aTargets": [ 4 ]
            },
            {               
                "sWidth": "10%",
                "aTargets": [ 5 ]
            },
            {               
                "sWidth": "10%",
                "aTargets": [ 6 ]
            },
            {               
                "sWidth": "10%",
                "aTargets": [ 7 ]
            },
            {               
                "sWidth": "10%",
                "aTargets": [ 8 ]
            },
            {               
                "sWidth": "10%",
                "aTargets": [ 9 ]
            },
            {               
                "sWidth": "10%",
                "aTargets": [ 10 ]
            },            
            {               
                "sWidth": "6%",
                "aTargets": [ 11 ]
            },
            {               
                "sWidth": "6%",
                "aTargets": [ 12 ]
            },
            {               
                "sWidth": "7%",
                "aTargets": [ 13 ]
            },
            {               
                "sWidth": "7%",
                "aTargets": [ 14 ]
            },
            {               
                "sWidth": "7%",
                "aTargets": [ 15 ]
            },
            {               
                "sWidth": "5%",
                "aTargets": [ 16 ]
            },
            {               
                "sWidth": "5%",
                "aTargets": [ 17 ]
            }            
        ]
    }); 
    $.fn.dataTableExt.oSort['fecha-asc'] = function(a, b) {
        var ukDatea = a.split('/');
        var ukDateb = b.split('/');

        var x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
        var y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;

        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    };

    $.fn.dataTableExt.oSort['fecha-desc'] = function(a, b) {
        var ukDatea = a.split('/');
        var ukDateb = b.split('/');

        var x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
        var y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;

        return ((x < y) ? 1 : ((x > y) ? -1 : 0));
    };
    function showdivToolTip(elemento, text)
    {
        $('#divflotante').html(text);

        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotante").css({left: x, top: y});

        document.getElementById('divflotante').style.display = 'block';
        return;
    }
    $("#myTableFixed tbody td").hover(
            function() {
                $(this).attr('id', 'divtitlemostrar');
                var index = $(this).index();
                if ( index === 10 || index === 11  ) {
                    showdivToolTip($('#divtitlemostrar').position(), $(this).html());
                }
            },
            function() {
                $('#divtitlemostrar').removeAttr('id');
                $('#divflotante').hide();
            }
    );
    $("#myTableFixed tbody tr").click(function(e) {
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
            if (typeof($(this).children('td')[0]) !== "undefined") {
                $('#txtpnuAnn').val($(this).children('td')[0].innerHTML);
                $('#txtpnuEmi').val($(this).children('td')[1].innerHTML);
                $('#txtpnuSec').val($(this).children('td')[2].innerHTML);
                jQuery('#txtCoEstadoDoc').val($(this).children('td')[17].innerHTML);//Hermes - 28/05/2019
                pnumFilaSelect = $(this).index();
            }
            //editarDocumentoRecep($(this).children('td')[12].innerHTML,$(this).children('td')[13].innerHTML,$(this).children('td')[15].innerHTML);
            /* comentado xq los botones de ver documento y ver anexos, ya se encuentran el la tabla
             var sData = $(this).find('td');
             passArrayToHtmlTabla("txtTextIndexSelect",sData);*/


        }
    });
    if($('#myTableFixed >tbody >tr').length > 0){
        //$('#myTableFixed >tbody >tr').eq(0).addClass('row_selected');
        //var pauxNumFilaSelect = typeof(pnumFilaSelect)!=="undefined"? pnumFilaSelect:0;
        try{
            if($('#myTableFixed >tbody >tr').eq(pnumFilaSelect).length === 1){
                $('#myTableFixed >tbody >tr').eq(pnumFilaSelect).trigger("click");
                $('#myTableFixed >tbody >tr').eq(pnumFilaSelect).focus();
            }else{
                pnumFilaSelect=0;
            }
        }catch(ex){
            pnumFilaSelect=0;
        }
    }
    $('#myTableFixed >tbody >tr').keydown(function(evento){
       if(evento.which===38){//up
            if (jQuery("#myTableFixed >tbody >tr").eq(pnumFilaSelect).prev('tr').length===0){
                pnumFilaSelect=$("#myTableFixed >tbody >tr").length;
            }
            pnumFilaSelect--;
            $("#myTableFixed >tbody >tr").eq(pnumFilaSelect).trigger("click");
            $("#myTableFixed >tbody >tr").eq(pnumFilaSelect).focus();
       }else if(evento.which===40){//down
            if ($("#myTableFixed >tbody >tr").eq(pnumFilaSelect).next('tr').length===0){
                pnumFilaSelect=-1;
            }           
            pnumFilaSelect++;
            $("#myTableFixed >tbody >tr").eq(pnumFilaSelect).trigger("click");               
            $("#myTableFixed >tbody >tr").eq(pnumFilaSelect).focus();           
       }
       evento.preventDefault();
    });    
}

function fu_cleanBusDocMensajeriaDevolucion(tipo) {
    var noForm='#buscarDocumentoDevolucionMensajeriaBean';
    if (tipo==="1") {        
        $(noForm).find("#esIncluyeFiltro1").prop('checked',false);
        $(noForm).find("#esIncluyeFiltro1").attr('checked',false);
    } else if(tipo==="0"){
        $(noForm).find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 1});    
        $(noForm).find("#esFiltroFecha").val("1");//hoy
        $(noForm).find("#coAnnio").val(jQuery("#txtAnnioActual").val());
        $(noForm).find("#coEstadoDoc option[value=6]").prop("selected", "selected");        
        $(noForm).find("#coTipoDoc option[value=]").prop("selected", "selected");
        $(noForm).find("#coTipoEnvMsj option[value=]").prop("selected", "selected");
        $(noForm).find("#busNumDoc").val("");
        $(noForm).find("#busAsunto").val("");
        $(noForm).find("#busObsDev").val("");        
        $(noForm).find("#busDesti").val("");
        //$(noForm).find("#coTipoEnvMsj").val(".: TODOS :.");
        $(noForm).find("#coDependenciaBusca").val("");
    }
}

function fu_generarReporteMensajeriaDevolucionXLS(){
    fu_generarReporteMsjDevolucion('XLS');
}

function fu_generarReporteMsjDevolucion(pformatoReporte){    
    var validaFiltro = fu_validaFormBusDocMensajeriaDevolucion("0");

    if (validaFiltro === "1") {        
        ajaxCall("/srMensajeriaDevolucion.do?accion=goExportarArchivoLista&pformatRepor="+pformatoReporte, $('#buscarDocumentoDevolucionMensajeriaBean').serialize(), function(data) {
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
    return false;
}