function fn_iniMensajes(){
    jQuery('#buscarDocumentoCargaMsjBean').find('#esFiltroFecha').val("4");//ultimos 30 dias
    jQuery('#buscarDocumentoCargaMsjBean').find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 4});    
   
    pnumFilaSelect=0;
    changeBusqMensajes("1");
    
}
function changeBusqMensajes(tipo) {
    
    jQuery('#buscarDocumentoCargaMsjBean').find('#tipoBusqueda').val(tipo);
    submitAjaxFormBusDocMensajes(tipo);
    
}

function submitAjaxFormBusDocMensajes(tipo) {
    
    var validaFiltro = fu_validaFormBusqMensajes(tipo);
    
    if (validaFiltro === "1") {
        ajaxCall("/srGestionMensajes.do?accion=goInicio", $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
            refreshScript("divTablaMensajes", data);
        }, 'text', false, false, "POST");
    }
    return false;
}


function fu_validaFormBusqMensajes(tipo) {
    var valRetorno = "1";
    $('#buscarDocumentoCargaMsjBean').find('#feEmiIni').val($('#buscarDocumentoCargaMsjBean').find('#coAnnio').parent('td').find('#fechaFiltro').attr('fini'));
    $('#buscarDocumentoCargaMsjBean').find('#feEmiFin').val($('#buscarDocumentoCargaMsjBean').find('#coAnnio').parent('td').find('#fechaFiltro').attr('ffin'));    
    
    
    var pEsIncluyeFiltro = $('#buscarDocumentoCargaMsjBean').find('#esIncluyeFiltro').is(':checked');
    var vFechaActual = $('#txtFechaActual').val();
    if(tipo==="1"){
         
        valRetorno = fu_validaFechasFormBusqMensaje(vFechaActual);  
    }else if(tipo==="0"){
        //valRetorno = fu_validaBusDocExtRecep();
        if(valRetorno==="1"){
            if(pEsIncluyeFiltro===false){
               valRetorno = fu_validaFechasFormBusqMensaje(vFechaActual); 
            }else{
               valRetorno = setAnnioNoIncludeFiltroDocExtRecep();
            }
        }
    }    
    
    return valRetorno;
}


function fu_validaFechasFormBusqMensaje(vFechaActual){
    var valRetorno = "1";
    
    if (valRetorno==="1") {
        
        fu_obtenerEsFiltroFechaMensaje('buscarDocumentoCargaMsjBean');
        var pEsFiltroFecha = jQuery('#buscarDocumentoCargaMsjBean').find("#esFiltroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
        if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
           if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
                var pAnnio = jQuery('#buscarDocumentoCargaMsjBean').find('#coAnnio').val();
                if(pAnnio!==null && pAnnio!=="null" && typeof(pAnnio)!=="undefined" && pAnnio!==""){
                    var vValidaNumero = fu_validaNumero(pAnnio);
                    if (vValidaNumero !== "OK") {
                       bootbox.alert("Año debe ser solo números.");
                        valRetorno = "0";
                    }                
                }               
           }   
            
            var vFeInicio = jQuery('#buscarDocumentoCargaMsjBean').find("#feEmiIni").val();
            var vFeFinal = jQuery('#buscarDocumentoCargaMsjBean').find("#feEmiFin").val();
            if(valRetorno==="1"){
              var pAnnioBusq = verificarAñoBusquedaFiltro(vFeInicio,vFeFinal);
                if(pAnnioBusq!==null && pAnnioBusq!=="null" && typeof(pAnnioBusq)!=="undefined" && pAnnioBusq!==""){
                    var vValidaNumero = fu_validaNumero(pAnnioBusq);
                    if (vValidaNumero !== "OK") {
                       bootbox.alert("Año debe ser solo números.");
                        valRetorno = "0";
                    }else if(vValidaNumero ==="OK"){
                        jQuery('#buscarDocumentoCargaMsjBean').find('#coAnnio').val(pAnnioBusq);                          
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

function fu_obtenerEsFiltroFechaMensaje(nameForm){
    var opt = jQuery('#'+nameForm).find('#coAnnio').parent('td').find('#fechaFiltro').attr('optselected');
    
    if(opt==="1"||opt==="2"||opt==="4"||opt==="8"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("1"); 
    }else if(opt==="5"||opt==="6"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("2"); 
    }else if(opt==="3"||opt==="7"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("3"); 
    }
}


function fn_cargarResponsable() 
{
    var p = new Array();
    p[0] = "accion=goListaResponsable";
    p[1] = "vCodResposable=" + jQuery('#coTipoMsj').val();
    ajaxCall("/srGestionMensajes.do", p.join("&"), function(data) {
    refreshScript("divResponsableMensajeria", data);
    }, 'text', false, false, "POST");
}

function fn_verMotivo() 
{
   /* var p = new Array();
    p[0] = "accion=goListaResponsable";
    p[1] = "vCodResposable=" + jQuery('#coTipoMsj').val();
    ajaxCall("/srGestionMensajes.do", p.join("&"), function(data) {
    refreshScript("divResponsableMensajeria", data);
    }, 'text', false, false, "POST");*/
    var estado=jQuery('#co_EstadoDoc').val();
    

    if(estado === "4")
    {
         $('#divMotivo').show(); 
          document.getElementById("LblFechaEnv").innerHTML = "Fecha Primera Visita:" ;
          document.getElementById("LblFechaDev").innerHTML = "Fecha de Devolución:" ;
          
   }else
   {
      $('#divMotivo').hide();        
       document.getElementById("LblFechaEnv").innerHTML = "Fecha de Entrega:" ; 
       document.getElementById("LblFechaDev").innerHTML = "Fecha de Devolución:" ; 
       
  }
  
}



function fn_buildSendJsontoServerDocMensaj(noForm) {
    var result = "{";
    result += '"deambito":"' + $('#'+noForm).find("#deambito").val() + '",';
    result += '"detipmsj":"' + $('#'+noForm).find("#detipmsj").val() + '",';
    result += '"reenvmsj":"' + $('#'+noForm).find("#slcResponsableMensajeria").val() + '",';
    result += '"detipenv":"' + $('#'+noForm).find("#detipenv").val() + '",';
    result += '"nusermsj":"' + $('#'+noForm).find("#nusermsj").val() + '",';
    result += '"ansermsj":"' + $('#'+noForm).find("#ansermsj").val() + '",';
    result += '"fecenviomsj":"' + $('#'+noForm).find("#fecenviomsj").val() + ' ' + $('#'+noForm).find("#hoenvmsj").val() + ':00",';
    result += '"hoenvmsj":"' + $('#'+noForm).find("#fecenviomsj").val() + ' ' + $('#'+noForm).find("#hoenvmsj").val() + ':00",'; 
    result += '"feplamsj":"' + $('#'+noForm).find("#feplamsj").val() + ' ' + $('#'+noForm).find("#hoplamsj").val() + ':00",';
    result += '"hoplamsj":"' + $('#'+noForm).find("#feplamsj").val() + ' ' + $('#'+noForm).find("#hoplamsj").val() + ':00",';    
    result += '"numsj":"' + $('#'+noForm).find("#numsj").val() + '",'; 
    result += '"codigo":"' + $('#'+noForm).find("#codigo").val() + '"'; 
    return result + "}";
} 

function fu_cleanBusqMensajes(tipo) {
    var noForm='#buscarDocumentoCargaMsjBean';
    if (tipo==="0") {
        $(noForm).find("#busNuSerMsj").val("");
        $(noForm).find("#busAnSerMsj").val("");
        $(noForm).find("#busNuMsj").val("");

    } else if(tipo==="1"){
        $(noForm).find("#esIncluyeFiltro").prop('checked',false);
        $(noForm).find("#esIncluyeFiltro").attr('checked',false);
        $(noForm).find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 4});    
        $(noForm).find("#esFiltroFecha").val("4");//ultimos 30 dias
        $(noForm).find("#coAnnio").val(jQuery("#txtAnnioActual").val());
        $(noForm).find("#coEstadoDoc").val("2");        
        $(noForm).find("#coTipoEnvMsj").val(".: TODOS :.");
        $(noForm).find("#coTipoMsj").val(".: TODOS :.");
        $(noForm).find("#coAmbitoMsj").val(".: TODOS :.");
        $(noForm).find("#tipoZona").val("");
        $(noForm).find("#busNumExpediente").val("");
        $(noForm).find("#busDesti").val("");
        $(noForm).find("#busTipoDoc").val("");
        
        $(noForm).find("#coOficina").val("");
        
        $(noForm).find("#busNuSerMsj").val("");
        $(noForm).find("#busAnSerMsj").val("");
        $(noForm).find("#busNuMsj").val("");
        $(noForm).find("#busNuDoc").val("");
    }
    
    changeBusqMensajes('0');
}

function fn_optionSelecction_mensajes(){
    
    console.log("fn_optionSelecction1");
    var codigo="";
    var validar="";
    $('.esOptionCheck input').each(function() {  
        if($(this).is(':checked')){
        codigo+=$(this).attr("nuAnn")+$(this).attr("nuEmi")+$(this).attr("nuDes")+$(this).attr("nuMsj")+",";
        validar+=$(this).attr("docEstadoMsj");
        }
    });
    $('#txtSelecctionOption').val(codigo);
    $('#txtSelecctionOptionValidar').val(validar);
    
    console.log("->"+ $('#txtSelecctionOption').val());
    console.log("->"+ $('#txtSelecctionOptionValidar').val());
    
}

function validardescargaMasiva(valor){
    return true;
}

function fn_DescargarMensajes(val){
    var pnuAnn = allTrim($('#txtpnuAnn').val());    
    var pnuEmi = allTrim($('#txtpnuEmi').val());
    var validar=$('#txtSelecctionOptionValidar').val(); 
    
    if(validar!=""){
        if(validar.indexOf("0")>-1){
            bootbox.alert('Verificar por favor.!</br>El documento no puede descargarse por que no está en estado ENVIADO.');
        }
        else {
             fn_goDescargarMensajeria(val,pnuAnn,pnuEmi);
        }
    }
}

function fn_goDescargarMensajeria(val,pnuAnn,pnuEmi){
    if($('#divTablaMensajes').length===1){
        if(!!pnuAnn&&!!pnuEmi){
            var p = new Array();
            p[0] = "pnuAnn=" + pnuAnn;
            p[1] = "pnuEmi=" + pnuEmi;
            p[2] = "codigos=" + $('#txtSelecctionOption').val();
            console.log('p[2]->'+p[2]);
            console.log('codigos->'+$('#txtSelecctionOption').val());
            ajaxCall("/srGestionMensajes.do?accion=goDescargarMensajePrevio", p.join("&"), function(data) {
                $('#divBuscarMensajes').hide();
                //$('#divDescargaMsj').show();
                $("#divDescargaMsj").css("display", "block");
                refreshScript("divDescargaMsj", data);
                $("#divDescargaMsj").css("display", "block");
                //$('#divTablaDocExtRecep').html("");
                //fn_cargaToolBarDocExtRecep();
            }, 'text', false, false, "POST");
            
            //ejecutaOpcionModal(val, '/srGestionMensajes.do?accion=goDescargarMensajePrevio&amp;codigos='+$('#txtSelecctionOption').val(), 'POST');
            
        }
    }
}

//ejecutaOpcionModal(this, '/srGestionMensajes.do?accion=goDescargarMensaje&amp;nu_ann=${docMensaje.nu_ann}&amp;nu_emi=${docMensaje.nu_emi}&amp;nu_des=${docMensaje.nu_des}&amp;nu_msj=${docMensaje.nu_msj}&amp;fec_enviomsj=${docMensaje.fec_enviomsj}&amp;fec_plazo=${docMensaje.fe_pla_msj}', 'POST');"

function fu_eventoTablaMensajesMasivos(){
    //TABLA
    var oTable;
    
    oTable = $('#myTableDetalleDescarga').dataTable({
        "bPaginate": false,
        "bFilter": false,
        "bSort": true,
        "bInfo": true,
        "bAutoWidth": true,
        "bDestroy": true,
        "sScrollY": "150px",
        "bScrollCollapse": false,
        "oLanguage": {
            "sZeroRecords": "No se encuentran registros.",
            "sInfo": "Registros: _TOTAL_ ",
            "sInfoEmpty": ""
        }
    });
    
    function showdivToolTip(elemento, text)
    {
        $('#divflotantemas').html(text);

        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotantemas").css({left: x, top: y});

        //document.getElementById('divflotante').style.top = elemento.top + 24;
        //document.getElementById('divflotante').style.left = elemento.left;		
        document.getElementById('divflotantemas').style.display = 'block';

        return;
    }
    
    $("#myTableDetalleDescarga tbody td").hover(
            function() {
                $(this).attr('id', 'divtitlemostrar');
                //console.log($(this).index());
                var index = $(this).index();
                if (index >= 1 && index <= 6) {
                    showdivToolTip($('#divtitlemostrar').position(), $(this).html());
                }
            },
            function() {
                $('#divtitlemostrar').removeAttr('id');
                $('#divflotantemas').hide();
            }
    );
}

function fu_eventoTablaMensajes() {
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
        "aoColumnDefs": [
            {
                "sWidth": "0%",
                "aTargets": [ 0 ]
            },
            {
                "sWidth": "0%",
                "aTargets": [ 1 ]
            },{
                "sWidth": "1%",
                "aTargets": [ 2 ]
            },
            {               
                "sWidth": "0%",
                "aTargets": [ 3 ]
            },
            {               
                "sWidth": "2%",
                "aTargets": [ 4 ]
            },
            {               
                "sWidth": "8%",
                "aTargets": [ 5 ]
            },
            {               
                "sWidth": "8%",
                "aTargets": [ 6 ]
            },
            {               
                "sWidth": "7%",
                "aTargets": [ 7 ]
            },
            {               
                "sWidth": "9%",
                "aTargets": [ 8 ]
            },
            {               
                "sWidth": "9%",
                "aTargets": [ 9]
            },
            {               
                "sWidth": "5%",
                "aTargets": [ 10 ]
            },
            {               
                "sWidth": "5%",
                "aTargets": [ 11 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 12]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 13 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 24 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 25 ]
            },
                        {               
                "sWidth": "1%",
                "aTargets": [ 26 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 27 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 28 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 29 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 30 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 31 ]
            }
        ]    
    
    });
    
    jQuery.fn.dataTableExt.oSort['fecha-asc'] = function(a, b) {
        var ukDatea = a.split('/');
        var ukDateb = b.split('/');

        var x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
        var y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;

        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    };

    jQuery.fn.dataTableExt.oSort['fecha-desc'] = function(a, b) {
        var ukDatea = a.split('/');
        var ukDateb = b.split('/');

        var x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
        var y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;

        return ((x < y) ? 1 : ((x > y) ? -1 : 0));
    };
    //$("#myTableFixed thead tr").addClass("ui-datatable-fixed-scrollable-header ui-state-default")
    //$("#myTableFixed tbody tr").addClass("bx_sb ui-datatable-fixed-scrollable-body ui-datatable-fixed-data");
    function showdivToolTip(elemento, text)
    {
        $('#divflotante').html(text);

        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotante").css({left: x, top: y});

        //document.getElementById('divflotante').style.top = elemento.top + 24;
        //document.getElementById('divflotante').style.left = elemento.left;		
        document.getElementById('divflotante').style.display = 'block';

        return;
    }
    $("#myTableFixed tbody td").hover(
            function() {
                $(this).attr('id', 'divtitlemostrar');
                //console.log($(this).index());
                var index = $(this).index();
                if (index >= 3 && index <= 20) {
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
    $("#myTableFixed tbody tr").dblclick(function() {
        //jQuery('#txtTextIndexSelect').val("-1"); 
    });
    if(jQuery('#myTableFixed >tbody >tr').length > 0){
        //$('#myTableFixed >tbody >tr').eq(0).addClass('row_selected');
        //var pauxNumFilaSelect = typeof(pnumFilaSelect)!=="undefined"? pnumFilaSelect:0;
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

function fu_eventoTablaMensajesMasivos(){
    //TABLA
    var oTable;
    
    oTable = $('#myTableDetalleDescarga').dataTable({
        "bPaginate": false,
        "bFilter": false,
        "bSort": true,
        "bInfo": true,
        "bAutoWidth": true,
        "bDestroy": true,
        "sScrollY": "150px",
        "bScrollCollapse": false,
        "oLanguage": {
            "sZeroRecords": "No se encuentran registros.",
            "sInfo": "Registros: _TOTAL_ ",
            "sInfoEmpty": ""
        }
    });
    
    function showdivToolTip(elemento, text)
    {
        $('#divflotantemas').html(text);

        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotantemas").css({left: x, top: y});

        //document.getElementById('divflotante').style.top = elemento.top + 24;
        //document.getElementById('divflotante').style.left = elemento.left;		
        document.getElementById('divflotantemas').style.display = 'block';

        return;
    }
    
    $("#myTableDetalleDescarga tbody td").hover(
            function() {
                $(this).attr('id', 'divtitlemostrar');
                //console.log($(this).index());
                var index = $(this).index();
                if (index >= 1 && index <= 6) {
                    showdivToolTip($('#divtitlemostrar').position(), $(this).html());
                }
            },
            function() {
                $('#divtitlemostrar').removeAttr('id');
                $('#divflotantemas').hide();
            }
    );
}

function fu_eventoTablaMensajes2() {
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
        "aoColumnDefs": [
            {
                "sWidth": "0%",
                "aTargets": [ 0 ]
            },
            {
                "sWidth": "0%",
                "aTargets": [ 1 ]
            },{
                "sWidth": "1%",
                "aTargets": [ 2 ]
            },
            {               
                "sWidth": "0%",
                "aTargets": [ 3 ]
            },
            {               
                "sWidth": "2%",
                "aTargets": [ 4 ]
            },
            {               
                "sWidth": "6%",
                "aTargets": [ 5 ]
            },
            {               
                "sWidth": "7%",
                "aTargets": [ 6 ]
            },
            {               
                "sWidth": "6%",
                "aTargets": [ 7 ]
            },
            {               
                "sWidth": "7%",
                "aTargets": [ 8 ]
            },
            {               
                "sWidth": "7%",
                "aTargets": [ 9]
            },
            {               
                "sWidth": "6%",
                "aTargets": [ 10 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 11 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 12]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 13 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 14 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 16 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 17 ]
            },            
            {               
                "sWidth": "4%",
                "aTargets": [ 18 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 19 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 20 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 21 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 24 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 25 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 26 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 27 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 28 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 29 ]
            },
            {               
                "sWidth": "1%",
                "aTargets": [ 30 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 31 ]
            },
            {               
                "sWidth": "4%",
                "aTargets": [ 33 ]
            }
        ]    
    
    });
    
    jQuery.fn.dataTableExt.oSort['fecha-asc'] = function(a, b) {
        var ukDatea = a.split('/');
        var ukDateb = b.split('/');

        var x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
        var y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;

        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    };

    jQuery.fn.dataTableExt.oSort['fecha-desc'] = function(a, b) {
        var ukDatea = a.split('/');
        var ukDateb = b.split('/');

        var x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
        var y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;

        return ((x < y) ? 1 : ((x > y) ? -1 : 0));
    };
    //$("#myTableFixed thead tr").addClass("ui-datatable-fixed-scrollable-header ui-state-default")
    //$("#myTableFixed tbody tr").addClass("bx_sb ui-datatable-fixed-scrollable-body ui-datatable-fixed-data");
    function showdivToolTip(elemento, text)
    {
        $('#divflotante').html(text);

        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotante").css({left: x, top: y});

        //document.getElementById('divflotante').style.top = elemento.top + 24;
        //document.getElementById('divflotante').style.left = elemento.left;		
        document.getElementById('divflotante').style.display = 'block';

        return;
    }
    $("#myTableFixed tbody td").hover(
            function() {
                $(this).attr('id', 'divtitlemostrar');
                //console.log($(this).index());
                var index = $(this).index();
                if (index >= 3 && index <= 20) {
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
                pnumFilaSelect = $(this).index();
                jQuery('#txtCoEstadoDoc').val($(this).children('td')[32].innerHTML);//Hermes - 28/05/2019
            }
            //verrrrrrrrrrrrrrrrrrrrrr bloque
            /*
            if (typeof($(this).children('td')[12]) !== "undefined") {
                jQuery('#txtpnuAnn').val($(this).children('td')[1].innerHTML);
                jQuery('#txtpnuEmi').val($(this).children('td')[2].innerHTML);
                jQuery('#txtpnuDes').val($(this).children('td')[5].innerHTML);
                pnumFilaSelect = $(this).index();
            }*/
            //editarDocumentoRecep($(this).children('td')[12].innerHTML,$(this).children('td')[13].innerHTML,$(this).children('td')[15].innerHTML);
            /* comentado xq los botones de ver documento y ver anexos, ya se encuentran el la tabla
             var sData = $(this).find('td');
             passArrayToHtmlTabla("txtTextIndexSelect",sData);*/

        }

    });
    $("#myTableFixed tbody tr").dblclick(function() {
        //jQuery('#txtTextIndexSelect').val("-1"); 
    });
    if(jQuery('#myTableFixed >tbody >tr').length > 0){
        //$('#myTableFixed >tbody >tr').eq(0).addClass('row_selected');
        //var pauxNumFilaSelect = typeof(pnumFilaSelect)!=="undefined"? pnumFilaSelect:0;
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

function fn_grabarDescargaMsj() {
    
    console.log("fn_grabarDescargaMsj");
    
    var myForm='descargaMensajeBean';
    if(fn_valFormDescargaMsj(myForm)){
        if ($('#txtNombreAnexo').val() === '' && $("#txtAnexarCargo").val()==="NO"){
            bootbox.dialog({
                message: " <h5>Advertencia: No ha adjuntado el cargo del documento. ¿Desea grabar la descarga del documento?</h5>",
                buttons: {
                    SI: {
                        label: "SI",
                        className: "btn-primary",
                        callback: function() {
                            var jsonBody =
                                    {
                                        "nu_msj": $('#'+myForm).find("#txtNu_Msj").val(), 
                                        "nu_des": $('#'+myForm).find("#txtNu_Des").val(),
                                        "co_EstadoDoc": $('#'+myForm).find("#co_EstadoDoc").val(), 
                                        "nu_ann": $('#'+myForm).find("#txtNu_Ann").val(),
                                        "nu_emi": $('#'+myForm).find("#txtNu_Emi").val(), 
                                        "ob_msj": allTrim($('#'+myForm).find("#txtObservaciones").val()),
                                        "mo_msj_dev": $('#'+myForm).find("#coMotivo").val(), 
                                        "es_pen_msj": allTrim($('#'+myForm).find("#txtes_pen_msj").val()),
                                        "es_pen_dev": allTrim($('#'+myForm).find("#txtes_pen_dev").val()),
                                        "fe_ent_msj": $('#'+myForm).find("#txtFechaEnt").val()+ ' ' + $('#'+myForm).find("#txtHoraEnt").val() + ':00',
                                        "ho_ent_msj":$('#'+myForm).find("#txtFechaEnt").val()+ ' ' + $('#'+myForm).find("#txtHoraEnt").val() + ':00',
                                        "fe_dev_msj": $('#'+myForm).find("#txtFechaDev").val(),
                                        "ho_dev_msj":$('#'+myForm).find("#txtFechaDev").val()+ ' ' + $('#'+myForm).find("#txtHoraDev").val() + ':00',
                                        "fe_pla_dev":$('#'+myForm).find("#txtFe_Pla_Dev").val(),
                                        "fe_pla_msj":$('#'+myForm).find("#txtFe_Pla").val(),
                                        "di_pla_dev":$('#'+myForm).find("#txtDia_Pla_Dev").val(),
                                        "pe_env_msj_d":$('#'+myForm).find("#txtPeEnvMsj").val(),
                                        "pe_env_msj":$('#'+myForm).find("#txtPeEnvMsj").val()
                                    };

                            var jsonString = JSON.stringify(jsonBody);
                            var url = "/srGestionMensajes.do?accion=goUpdDescargaMsj";

                            ajaxCallSendJson(url, jsonString, function(data) {
                                if (data === "Datos guardados.") {
                                    alert_Sucess("MENSAJE", data);                                    
                                        ajaxCall("/srGestionMensajes.do?accion=goInicio", $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
                                                    $('#divBuscarMensajes').show();
                                                    $('#divDescargaMsj').hide();
                                                    refreshScript("divTablaMensajes", data);
                                                    }, 'text', false, false, "POST");                                              
                                    jQuery('#divDescargaMsj').html(""); 
                                    removeDomId('divOrigenMain');
                                } else {
                                    alert_Danger("ERROR:", data);
                                }
                            }, false, false, false, "POST");
                        }                        
                    },
                    NO: {
                        label: "NO",
                        className: "btn-default"
                    }
                }
            });                 
        }else{
            var jsonBody =
                    {
                        "nu_msj": $('#'+myForm).find("#txtNu_Msj").val(), 
                        "nu_des": $('#'+myForm).find("#txtNu_Des").val(),
                        "co_EstadoDoc": $('#'+myForm).find("#co_EstadoDoc").val(), 
                        "nu_ann": $('#'+myForm).find("#txtNu_Ann").val(),
                        "nu_emi": $('#'+myForm).find("#txtNu_Emi").val(), 
                        "ob_msj": allTrim($('#'+myForm).find("#txtObservaciones").val()),
                        "mo_msj_dev": $('#'+myForm).find("#coMotivo").val(), 
                        "es_pen_msj": allTrim($('#'+myForm).find("#txtes_pen_msj").val()),
                        "es_pen_dev": allTrim($('#'+myForm).find("#txtes_pen_dev").val()),
                        "fe_ent_msj": $('#'+myForm).find("#txtFechaEnt").val()+ ' ' + $('#'+myForm).find("#txtHoraEnt").val() + ':00',
                        "ho_ent_msj":$('#'+myForm).find("#txtFechaEnt").val()+ ' ' + $('#'+myForm).find("#txtHoraEnt").val() + ':00',
                        "fe_dev_msj": $('#'+myForm).find("#txtFechaDev").val(),
                        "ho_dev_msj":$('#'+myForm).find("#txtFechaDev").val()+ ' ' + $('#'+myForm).find("#txtHoraDev").val() + ':00',
                        "fe_pla_dev":$('#'+myForm).find("#txtFe_Pla_Dev").val(),
                        "fe_pla_msj":$('#'+myForm).find("#txtFe_Pla").val(),
                        "di_pla_dev":$('#'+myForm).find("#txtDia_Pla_Dev").val(),
                        "pe_env_msj_d":$('#'+myForm).find("#txtPeEnvMsj").val(),
                        "pe_env_msj":$('#'+myForm).find("#txtPeEnvMsj").val()
                    };

            var jsonString = JSON.stringify(jsonBody);
            var url = "/srGestionMensajes.do?accion=goUpdDescargaMsj";

            ajaxCallSendJson(url, jsonString, function(data) {
                if (data === "Datos guardados.") {
                    alert_Sucess("MENSAJE", data);                                    
                        ajaxCall("/srGestionMensajes.do?accion=goInicio", $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
                                    $('#divBuscarMensajes').show();
                                    $('#divDescargaMsj').hide();
                                    refreshScript("divTablaMensajes", data);
                                    }, 'text', false, false, "POST");                                              
                    jQuery('#divDescargaMsj').html(""); 
                    removeDomId('divOrigenMain');
                } else {
                    alert_Danger("ERROR:", data);
                }
            }, false, false, false, "POST");            
        }        
    }
    return;
}


function fn_grabarDescargaMsjMasivo() {
    
    console.log("fn_grabarDescargaMsjMasivo");
    
    var myForm='descargaMensajeBean';
    if(fn_valFormDescargaMsjMasivo(myForm)){
        
        console.log("Entre");
        console.log("->"+$('#'+myForm).find("#txtCodigos").val());
        
        var jsonBody =
                {
                    "nu_msj": $('#'+myForm).find("#txtNu_Msj").val(), 
                    "nu_des": $('#'+myForm).find("#txtNu_Des").val(),
                    "co_EstadoDoc": $('#'+myForm).find("#co_EstadoDoc").val(), 
                    "nu_ann": $('#'+myForm).find("#txtNu_Ann").val(),
                    "nu_emi": $('#'+myForm).find("#txtNu_Emi").val(), 
                    "ob_msj": allTrim($('#'+myForm).find("#txtObservaciones").val()),
                    "mo_msj_dev": $('#'+myForm).find("#coMotivo").val(), 
                    "es_pen_msj": allTrim($('#'+myForm).find("#txtes_pen_msj").val()),
                    "es_pen_dev": allTrim($('#'+myForm).find("#txtes_pen_dev").val()),
                    "fe_ent_msj": $('#'+myForm).find("#txtFechaEnt").val()+ ' ' + $('#'+myForm).find("#txtHoraEnt").val() + ':00',
                    "ho_ent_msj":$('#'+myForm).find("#txtFechaEnt").val()+ ' ' + $('#'+myForm).find("#txtHoraEnt").val() + ':00',
                    "fe_dev_msj": $('#'+myForm).find("#txtFechaDev").val(),
                    "ho_dev_msj":$('#'+myForm).find("#txtFechaDev").val()+ ' ' + $('#'+myForm).find("#txtHoraDev").val() + ':00',
                    "fe_pla_dev":$('#'+myForm).find("#txtFe_Pla_Dev").val(),
                    "pe_env_msj":$('#'+myForm).find("#txtPeEnvMsj").val(),
                    "codigos":$('#'+myForm).find("#txtCodigos").val()
                };
                
        var jsonString = JSON.stringify(jsonBody);

        var url = "/srGestionMensajes.do?accion=goUpdDescargaMsjMasivo";
        ajaxCallSendJson(url, jsonString, function(data) {
            if (data === "Datos guardados.") {
                alert_Sucess("MENSAJE", data);
                
        
            
                    ajaxCall("/srGestionMensajes.do?accion=goInicio", $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
                                $('#divBuscarMensajes').show();
                                $('#divDescargaMsj').hide();
                                refreshScript("divTablaMensajes", data);
                                }, 'text', false, false, "POST");
                
                              
                jQuery('#divDescargaMsj').html(""); 
                removeDomId('divOrigenMain');
            } else {
                alert_Danger("ERROR:", data);
            }
        }, false, false, false, "POST");
        
        
        
        
        
        /*$('#RutaDocs').html("<strong>" + dirPri + "</strong>");
        setTimeout("fu_activaDependencia_dir(" + codDep + "," + codDep + ");", 500);*/
    }
    return;
}

function fn_grabarDescarga() {
    var archivo=document.getElementById("files").files;//$("#fileupload").files();
    
    //var archivo = $("input[type=file name='archivo']").val().split('\\').pop();
    /*var nu_msj = $("#txtNu_Msj").val();
    var nu_des = $("#txtNu_Des").val();
    var esDoc = $("#coEstadoDoc").val();
    var nu_ann=$("#txtNu_Ann").val();
    var nu_emi=$("#txtNu_Emi").val();
    var Observaciones=allTrim($("#txtObservaciones").val());*/

    var myForm='descargaMensajeBean';
   /* var piePag = $("#txtPiePag").val();
    var rbAcceso = $("input[name='rbAcceso']:checked").val();
    var rbCarDoc = $("input[name='rbCarDoc']:checked").val();
    var rbFirDoc = $("input[name='rbFirDoc']:checked").val();
    var rbDocDef = $("input[name='rbDocDef']:checked").val();*/
    //myForm={nu_ann:nu_ann,nu_emi:nu_emi,nu_msj:nu_msj, nu_des:nu_des, esDoc:esDoc, Observaciones:Observaciones};
    
           /* var jsonBody =
                {
                    "nu_msj": $('#'+myForm).find("#txtNu_Msj").val(), "nu_des": $('#'+myForm).find("#txtNu_Des").val(),
                    "co_EstadoDoc": $('#'+myForm).find("#co_EstadoDoc").val(), "nu_ann": $('#'+myForm).find("#txtNu_Ann").val(),
                    "nu_emi": $('#'+myForm).find("#txtNu_Emi").val(), "ob_msj": allTrim($('#'+myForm).find("#txtObservaciones").val()),
                    "mo_msj_dev": $('#'+myForm).find("#coMotivo").val(), "es_pen_msj": allTrim($('#'+myForm).find("#txtes_pen_msj").val()),
                    "fe_ent_msj": $('#'+myForm).find("#txtFecha").val(),"archivo": archivo
                };*/
                
    if(fn_valFormDescargaMsj(myForm)){
        /*jQuery('#fileupload').attr("multiple", "");         
        jQuery('#fileupload').click(); */
       /* var jsonBody =
                {
                    "nu_msj": $('#'+myForm).find("#txtNu_Msj").val(), "nu_des": $('#'+myForm).find("#txtNu_Des").val(),
                    "co_EstadoDoc": $('#'+myForm).find("#co_EstadoDoc").val(), "nu_ann": $('#'+myForm).find("#txtNu_Ann").val(),
                    "nu_emi": $('#'+myForm).find("#txtNu_Emi").val(), "ob_msj": allTrim($('#'+myForm).find("#txtObservaciones").val()),
                    "mo_msj_dev": $('#'+myForm).find("#coMotivo").val(), "es_pen_msj": allTrim($('#'+myForm).find("#txtes_pen_msj").val()),
                    "fe_ent_msj": $('#'+myForm).find("#txtFecha").val()
                };
        var jsonString = JSON.stringify(jsonBody);*/
        var url = "/srGestionMensajes.do?accion=goUpload";
       /* ajaxCallSendJson(url, jsonString, function(data) {
            if (data === "Datos guardados.") {
                alert_Sucess("MENSAJE", data);
                
        
            
                    ajaxCall("/srGestionMensajes.do?accion=goInicio", $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
                                $('#divBuscarMensajes').show();
                                $('#divDescargaMsj').hide();
                                refreshScript("divTablaMensajes", data);
                                }, 'text', false, false, "POST");
                
                              
                jQuery('#divDescargaMsj').html(""); 
                removeDomId('divOrigenMain');
            } else {
                alert_Danger("ERROR:", data);
            }
        }, 'text', false, false, "POST");*/
        
          var oMyForm = new FormData(myForm);
          /*oMyForm.append("NuEmi", $('#'+myForm).find("#txtNu_Emi").val());
          oMyForm.append("NuDes", $('#'+myForm).find("#txtNu_Des").val());
          oMyForm.append("NuAnn", $('#'+myForm).find("#txtNu_Ann").val());
          oMyForm.append("file", files.files[0]);*/

          /*  $.ajax({
              url:url,// 'http://localhost:8080/spring-mvc-file-upload/rest/cont/upload',
              data: oMyForm,
              dataType: 'text',
              processData: false,
              contentType: false,
              type: 'POST',
              success: function(data){
                if (data === "Datos guardados.") {
                                alert_Sucess("MENSAJE", data);



                                    ajaxCall("/srGestionMensajes.do?accion=goInicio", $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
                                                $('#divBuscarMensajes').show();
                                                $('#divDescargaMsj').hide();
                                                refreshScript("divTablaMensajes", data);
                                                }, 'text', false, false, "POST");


                                jQuery('#divDescargaMsj').html(""); 
                                removeDomId('divOrigenMain');
                            } else {
                                alert_Danger("ERROR:", data);
                            }
              }
            });*/
        
        ajaxCall(url, oMyForm, function(data) {
            if (data === "Datos guardados.") {
                alert_Sucess("MENSAJE", data);
                
        
            
                    ajaxCall("/srGestionMensajes.do?accion=goInicio", $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
                                $('#divBuscarMensajes').show();
                                $('#divDescargaMsj').hide();
                                refreshScript("divTablaMensajes", data);
                                }, 'text', false, false, "POST");
                
                              
                jQuery('#divDescargaMsj').html(""); 
                removeDomId('divOrigenMain');
            } else {
                alert_Danger("ERROR:", data);
            }
        }, 'json', false, false, "POST");
        
        
        /*$('#RutaDocs').html("<strong>" + dirPri + "</strong>");
        setTimeout("fu_activaDependencia_dir(" + codDep + "," + codDep + ");", 500);*/
    }
    return;
}

function fn_grabarDescarga2() {
    var rutactx = pRutaContexto + "/" + pAppVersion;
   /* var url = "";
    var data=document.getElementById("files");//$("#fileupload").files();
    url = url.concat(rutactx, "/srGestionMensajes.do?accion=goUpload", "&pkEmi=");
    data.url = url;
    alert_Danger(""+data.url);
    data.submit();
    $("#descargaMensajeBean").submit();
    jQuery('#files').click();*/
    
    
    //var url = "/srGestionMensajes.do?accion=goUpload";
   // var myForm='descargaMensajeBean';
   // var data = new FormData(myForm); //Creamos los datos a enviar con el formulario
  $.ajax({
        url: rutactx+"/srGestionMensajes.do?accion=goUpload", //URL destino
        data: $('#descargaMensajeBean').serialize(),
        processData: false, //Evitamos que JQuery procese los datos, daría error
        contentType: false, //No especificamos ningún tipo de dato
        type: 'POST',
        success: function (resultado) {
            alert(resultado);
        }
    });
    
           /* ajaxCall("/srGestionMensajes.do?accion=goUpload", $('#descargaMensajeBean').serialize(), function(data) {
            if (data === "Datos guardados.") {
                alert_Sucess("MENSAJE", data);
                
        
            
                    ajaxCall("/srGestionMensajes.do?accion=goInicio", $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
                                $('#divBuscarMensajes').show();
                                $('#divDescargaMsj').hide();
                                refreshScript("divTablaMensajes", data);
                                }, 'text', false, false, "POST");
                
                              
                jQuery('#divDescargaMsj').html(""); 
                removeDomId('divOrigenMain');
            } else {
                alert_Danger("ERROR:", data);
            }
        }, 'multipart/form-data', false, false, "POST");*/
    

}

function fn_valFormDescargaMsj(objForm){
    var vReturn=1;
//    var Observaciones=$('#'+objForm).find("#txtObservaciones").val();
    var FechaEnt=$('#'+objForm).find("#txtFechaEnt").val();
    var HoraEnt=$('#'+objForm).find("#txtHoraEnt").val();
    var FechaDev=$('#'+objForm).find("#txtFechaDev").val();
    var HoraDev=$('#'+objForm).find("#txtHoraDev").val();
    var archivo=$('#'+objForm).find("#txtNombreAnexo").val();
    
//    if (!!Observaciones===false)
//    {
//        vReturn=0;
//        alert_Info("Aviso", "Debe Ingresar por lo menos una observación");
//        $('#'+objForm).find('#txtObservaciones').focus();
//    }
    if (!!FechaEnt===false)
    {
        vReturn=0;
        alert_Info("Aviso", "Debe Ingresar Fecha");
        $('#'+objForm).find('#FechaEnt').focus();
    }
    
    if(!!FechaEnt){
            if(moment(fecha, "DD/MM/YYYY").isValid()){// HH:mm
  
                var fecha=moment(fecha, ["DD","DD/MM","DD/MM/YY","DD/MM/YY HH:mm","DD/MM/YYYY","DD/MM/YYYY HH:mm"]); 
                if(!fecha.isValid()){
                vReturn=0;
                 bootbox.alert("<h5>Fecha Inválida.</h5>", function() {
                     bootbox.hideAll();
                     $('#'+objForm).find('#txtFechaEnt').focus();
                 });
                                         
                }                
            }     
       
       
    }
    
    if (!!FechaDev===false)
    {
        vReturn=0;
        alert_Info("Aviso", "Debe Ingresar Fecha");
        $('#'+objForm).find('#txtFecha').focus();
    }
    
    if(!!FechaDev){
            if(moment(fecha, "DD/MM/YYYY").isValid()){// HH:mm
  
                var fecha=moment(fecha, ["DD","DD/MM","DD/MM/YY","DD/MM/YY HH:mm","DD/MM/YYYY","DD/MM/YYYY HH:mm"]); 
                if(!fecha.isValid()){
                vReturn=0;
                 bootbox.alert("<h5>Fecha Inválida.</h5>", function() {
                     bootbox.hideAll();
                     $('#'+objForm).find('#txtFechaDev').focus();
                 });
                                         
                }                
            }     
       
       
    }
    
   if (!!HoraEnt===false) {
        vReturn=0;
        alert_Info("Aviso", "Debe Ingresar Hora");
        $('#'+objForm).find('#txtHoraEnt').focus(); 
   }
   else {
        
        if (HoraEnt.substr(2,1)==":") {
            
            for (i=0; i<5; i++) {

                    if ((HoraEnt.substr(i,1)<"0" || HoraEnt.substr(i,1)>"9") && i!==2) {
                        vReturn = 0; 
                        alert_Info("Aviso", "Debe Ingresar Hora Válida");
                        $('#'+objForm).find('#txtHoraEnt').focus();
                    }
                
                
            }
            
            var h = HoraEnt.substr(0,2);
            var m = HoraEnt.substr(3,2);
            var s = HoraEnt.substr(6,2);
            
            if (h>23 || m>59 || s>59) {
                vReturn = 0; 
                alert_Info("Aviso", "Debe Ingresar Hora Válida");
                $('#'+objForm).find('#txtHoraEnt').focus();
            }
        } else {
            vReturn = 0; 
            alert_Info("Aviso", "Debe Verificar Formato de Hora");
            $('#'+objForm).find('#txtHoraEnt').focus();
        }
    }
   
   if (!!HoraDev===false) {
        vReturn=0;
        alert_Info("Aviso", "Debe Ingresar Hora");
        $('#'+objForm).find('#txtHoraDev').focus(); 
   }
   else {
        
        if (HoraDev.substr(2,1)==":") {
            
            for (i=0; i<5; i++) {

                    if ((HoraDev.substr(i,1)<"0" || HoraDev.substr(i,1)>"9") && i!==2) {
                        vReturn = 0; 
                        alert_Info("Aviso", "Debe Ingresar Hora Válida");
                        $('#'+objForm).find('#txtHoraDev').focus();
                    }
                
                
            }
            
            var h = HoraDev.substr(0,2);
            var m = HoraDev.substr(3,2);
            var s = HoraDev.substr(6,2);
            
            if (h>23 || m>59 || s>59) {
                vReturn = 0; 
                alert_Info("Aviso", "Debe Ingresar Hora Válida");
                $('#'+objForm).find('#txtHoraDev').focus();
            }
        } else {
            vReturn = 0; 
            alert_Info("Aviso", "Debe Verificar Formato de Hora");
            $('#'+objForm).find('#txtHoraDev').focus();
        }
    }
      
   
   
//    if ($('#txtNombreAnexo').val() === '' && $("#txtAnexarCargo").val()==="NO"){
//        vReturn=0;
//        alert_Info("Aviso", "Debe seleccionar un archivo");
//        $('#'+objForm).find('#btn-ReemplazarDoc').focus();
//    }
   
   
   
    return vReturn;
}


function fn_valFormDescargaMsjMasivo(objForm){
    var vReturn=1;
    var FechaEnt=$('#'+objForm).find("#txtFechaEnt").val();
    var HoraEnt=$('#'+objForm).find("#txtHoraEnt").val();
    var FechaDev=$('#'+objForm).find("#txtFechaDev").val();
    var HoraDev=$('#'+objForm).find("#txtHoraDev").val();
    

    if (!!FechaEnt===false)
    {
        vReturn=0;
        alert_Info("Aviso", "Debe Ingresar Fecha");
        $('#'+objForm).find('#FechaEnt').focus();
    }
    
    if(!!FechaEnt){
            if(moment(fecha, "DD/MM/YYYY").isValid()){// HH:mm
  
                var fecha=moment(fecha, ["DD","DD/MM","DD/MM/YY","DD/MM/YY HH:mm","DD/MM/YYYY","DD/MM/YYYY HH:mm"]); 
                if(!fecha.isValid()){
                vReturn=0;
                 bootbox.alert("<h5>Fecha Inválida.</h5>", function() {
                     bootbox.hideAll();
                     $('#'+objForm).find('#txtFechaEnt').focus();
                 });
                                         
                }                
            }     
       
       
    }
    
    if (!!FechaDev===false)
    {
        vReturn=0;
        alert_Info("Aviso", "Debe Ingresar Fecha");
        $('#'+objForm).find('#txtFecha').focus();
    }
    
    if(!!FechaDev){
            if(moment(fecha, "DD/MM/YYYY").isValid()){// HH:mm
  
                var fecha=moment(fecha, ["DD","DD/MM","DD/MM/YY","DD/MM/YY HH:mm","DD/MM/YYYY","DD/MM/YYYY HH:mm"]); 
                if(!fecha.isValid()){
                vReturn=0;
                 bootbox.alert("<h5>Fecha Inválida.</h5>", function() {
                     bootbox.hideAll();
                     $('#'+objForm).find('#txtFechaDev').focus();
                 });
                                         
                }                
            }     
       
       
    }
    
   if (!!HoraEnt===false) {
        vReturn=0;
        alert_Info("Aviso", "Debe Ingresar Hora");
        $('#'+objForm).find('#txtHoraEnt').focus(); 
   }
   else {
        
        if (HoraEnt.substr(2,1)==":") {
            
            for (i=0; i<5; i++) {

                    if ((HoraEnt.substr(i,1)<"0" || HoraEnt.substr(i,1)>"9") && i!==2) {
                        vReturn = 0; 
                        alert_Info("Aviso", "Debe Ingresar Hora Válida");
                        $('#'+objForm).find('#txtHoraEnt').focus();
                    }
                
                
            }
            
            var h = HoraEnt.substr(0,2);
            var m = HoraEnt.substr(3,2);
            var s = HoraEnt.substr(6,2);
            
            if (h>23 || m>59 || s>59) {
                vReturn = 0; 
                alert_Info("Aviso", "Debe Ingresar Hora Válida");
                $('#'+objForm).find('#txtHoraEnt').focus();
            }
        } else {
            vReturn = 0; 
            alert_Info("Aviso", "Debe Verificar Formato de Hora");
            $('#'+objForm).find('#txtHoraEnt').focus();
        }
    }
   
   if (!!HoraDev===false) {
        vReturn=0;
        alert_Info("Aviso", "Debe Ingresar Hora");
        $('#'+objForm).find('#txtHoraDev').focus(); 
   }
   else {
        
        if (HoraDev.substr(2,1)==":") {
            
            for (i=0; i<5; i++) {

                    if ((HoraDev.substr(i,1)<"0" || HoraDev.substr(i,1)>"9") && i!==2) {
                        vReturn = 0; 
                        alert_Info("Aviso", "Debe Ingresar Hora Válida");
                        $('#'+objForm).find('#txtHoraDev').focus();
                    }
                
                
            }
            
            var h = HoraDev.substr(0,2);
            var m = HoraDev.substr(3,2);
            var s = HoraDev.substr(6,2);
            
            if (h>23 || m>59 || s>59) {
                vReturn = 0; 
                alert_Info("Aviso", "Debe Ingresar Hora Válida");
                $('#'+objForm).find('#txtHoraDev').focus();
            }
        } else {
            vReturn = 0; 
            alert_Info("Aviso", "Debe Verificar Formato de Hora");
            $('#'+objForm).find('#txtHoraDev').focus();
        }
    }
     
    return vReturn;
}

function fn_eliminarMsj(vnu_ann,vnu_emi,vnu_des,vnu_msj) {
   // var pesDocEmi = jQuery('#buscarDocumentoCargaMsjBean').find('#esDocEmi').val();
    if (vnu_ann !== null && vnu_emi !== null) {
//        if (confirm('¿ Esta Seguro de Eliminar ?')) {
//            $('#documentoEmiBean').find('select').removeProp('disabled');
//            ajaxCall("/srDocumentoAdmEmision.do?accion=goEliminarDocEmiAdm", $('#documentoEmiBean').serialize(), function(data) {
//                fn_rptEliminarDocEmiAdm(data);
//            }, 'json', false, false, "POST");
//        }
        bootbox.dialog({
            message: " <h5>¿Esta seguro de retornar este registro?</h5>",
            buttons: {
                SI: {
                    label: "SI",
                    className: "btn-primary",
                    callback: function() {
                       // $('#documentoEmiBean').find('select').removeProp('disabled');
                        ajaxCallSendJson("/srGestionMensajes.do?accion=goEliminaMensaje&nu_ann="+vnu_ann+"&nu_emi="+vnu_emi+"&nu_des="+vnu_des+"&nu_msj="+vnu_msj, "", function(data) {
                           if (data === "Datos guardados.") {
                                alert_Sucess("MENSAJE", "Registro Retornado");                                          
            
                                ajaxCall("/srGestionMensajes.do?accion=goInicio", $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
                                        $('#divBuscarMensajes').show();
                                        $('#divDescargaMsj').hide();
                                        refreshScript("divTablaMensajes", data);
                                        }, 'text', false, false, "POST");
                
                              
                                    jQuery('#divDescargaMsj').html(""); 
                                    removeDomId('divOrigenMain');
                            } else {
                                alert_Danger("ERROR:", data);
                            }
                        }, 'text', false, false, "POST");
                    }                        
                },
                NO: {
                    label: "NO",
                    className: "btn-default"
                }
            }
        });           
    }
    return false;
}

function change(obj) {


    var selectBox = obj;
    var selected = selectBox.options[selectBox.selectedIndex].value;
    var textarea = document.getElementById("coMotivo");

    if(selected === '1'){
        textarea.style.display = "block";
    }
    else{
        textarea.style.display = "none";
    }
}


function fu_generarReporteMsjConsulPDF(){
   fu_generarReporteMsjConsul('PDF');  
}

function fu_generarReporteMsjConsulXLS(){
   fu_generarReporteMsjConsul('XLS');  
}

function fu_generarReporteMsjConsul(pformatoReporte){
    var validaFiltro = fu_validaFiltroRecepDocAdmConsul("0");
    if (validaFiltro === "1" || validaFiltro === "2") {
        //ajaxCall("/srConsultaRecepcionDoc.do?accion=goRutaReporte&pformatRepor="+pformatoReporte, $('#buscarDocumentoRecepConsulBean').serialize(), function(data) {
        ajaxCall("/srGestionMensajes.do?accion=goExportarArchivoLista&pformatRepor="+pformatoReporte, $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
            if (data!==null) {
                if(data.coRespuesta==="0"){
                    if(!!data.noUrl&&!!data.noDoc){
//                        fn_generaDocApplet_personal(data.noUrl,data.noDoc,function (data){
//                            var result = data;
//                            if (result!=="OK"){
//                               bootbox.alert(result);
//                            }                            
//                        });
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

function fn_anexoDocumento(tamanioMaxAnexos)
{
    var reemplazarDoc = true;
    jQuery('#progress .progress-bar').css('width', '0%');
    jQuery('#fileupload').removeAttr("multiple");
    fn_fileUploadMsj(reemplazarDoc, tamanioMaxAnexos);/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
}


function fn_fileUploadMsj(reemplazarDoc, tamanioMaxAnexos) {
    $("#progress").show();
    //var pFileSizeMax = 10000000;HPB 17/02/2020 - Requerimiento capacidad archivo configurable
//    var rutactx = $("#rutactx").attr("value");
    var rutactx = pRutaContexto + "/" + pAppVersion;
    var numFiles = 0;
    var contAux = 0;
    jQuery('#fileupload').fileupload({
        dataType: 'text',
        add: function(e, data) {
            var fileSizeMax = tamanioMaxAnexos;/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
            var fileSize = data.files[0].size;
            var fileName = cleanString(data.files[0].name);
            if (fileSize <= fileSizeMax)
            {
                numFiles++;
                if (reemplazarDoc === true) {
                    var nuAnn = $("#txtNu_Ann").attr("value");
                    var nuEmi = $("#txtNu_Emi").attr("value");
                    var nuDes = $("#txtNu_Des").attr("value");
                    var url = "";
                    url = url.concat(rutactx, "/srGestionMensajes.do?accion=goUpload", "&NuAnn=", nuAnn,"&NuEmi=", nuEmi, "&NuDes=", nuDes);
                    data.url = url;
                }
                data.submit();
            } else
            {
                fileSize = Math.round(fileSize / 1024) + " Kb";
                var row = "";
                row = row.concat('Error! el tamaño <strong>', fileSize, '</strong> del archivo <strong>', fileName, '</strong> supera el limite permitido.');
                jQuery("#divErrorLista").append('<li>' + row + '</li>');
                jQuery("#divError").show();
                jQuery("#divError").attr('display', 'block');
                return;
            }
        },
        done: function(e, data) {
            if (data.textStatus === "success")
            {
                contAux++;
                if (contAux === numFiles) {
                    if (reemplazarDoc) {
                        var jsonRes = jQuery.parseJSON(data.result);
                        var nombreDoc = jsonRes[0].name;
                        var nuAne = $("#oNuAne").attr("value");
                        $("#Anexo" + nuAne + " input:eq(0)").attr("value", nombreDoc);
                        $("#Anexo" + nuAne + " input:eq(1)").attr("value", nombreDoc);
                        reemplazarDoc = false;
                    } else {
                        actualizarListadoAnexos();
                    }
                    alert_Sucess("MENSAJE", "archivo adjuntado");
                    $("#txtNombreAnexo").attr("value",nombreDoc);
                    $("#txtAnexarCargo").attr("value","SI");
                }
            } else {
                alert_Danger("ERROR", "Error al cargar el archivo");
            }
        },
        progressall: function(e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            jQuery('#progress .progress-bar').css('width', progress + '%');
            var progressText = "";
            if (progress < 30) {
                progressText = progress + '%';
            } else {
                progressText = 'Cargando ' + progress + '%';
            }
            jQuery('#progress span').html(progressText);
        }
    });
    jQuery('#fileupload').click();
}

function fn_anexoDocumentoArc(pAnn,pEmi,pDes, tamanioMaxAnexos)
{
    var reemplazarDoc = true;
    jQuery('#progress .progress-bar').css('width', '0%');
    jQuery('#fileupload').removeAttr("multiple");
    fn_fileUploadMsjArc(reemplazarDoc,pAnn,pEmi,pDes, tamanioMaxAnexos);/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
}

function fn_fileUploadMsjArc(reemplazarDoc,pAnn,pEmi,pDes, tamanioMaxAnexos) {
    $("#progress").show();
    //var pFileSizeMax = 10000000;HPB 17/02/2020 - Requerimiento capacidad archivo configurable
//    var rutactx = $("#rutactx").attr("value");
    var rutactx = pRutaContexto + "/" + pAppVersion;
    var numFiles = 0;
    var contAux = 0;
    jQuery('#fileupload').fileupload({
        dataType: 'text',
        add: function(e, data) {
            var fileSizeMax = tamanioMaxAnexos;/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
            var fileSize = data.files[0].size;
            var fileName = cleanString(data.files[0].name);
            if (fileSize <= fileSizeMax)
            {
                numFiles++;
                if (reemplazarDoc === true) {
                    var nuAnn=pAnn;// $("#txtNu_Ann").attr("value");
                    var nuEmi=pEmi ;//$("#txtNu_Emi").attr("value");
                    var nuDes=pDes ;//$("#txtNu_Des").attr("value");
                    var url = "";
                    url = url.concat(rutactx, "/srGestionMensajes.do?accion=goUpload", "&NuAnn=", nuAnn,"&NuEmi=", nuEmi, "&NuDes=", nuDes);
                    data.url = url;
                }
                data.submit();
                
            } else
            {
                fileSize = Math.round(fileSize / 1024) + " Kb";
                var row = "";
                row = row.concat('Error! el tamaño <strong>', fileSize, '</strong> del archivo <strong>', fileName, '</strong> supera el limite permitido.');
                jQuery("#divErrorLista").append('<li>' + row + '</li>');
                jQuery("#divError").show();
                jQuery("#divError").attr('display', 'block');
                return;
            }
        },
        done: function(e, data) {
            if (data.textStatus === "success")
            {
                contAux++;
                if (contAux === numFiles) {
                    if (reemplazarDoc) {
                        var jsonRes = jQuery.parseJSON(data.result);
                        var nombreDoc = jsonRes[0].name;
                        var nuAne = $("#oNuAne").attr("value");
                        $("#Anexo" + nuAne + " input:eq(0)").attr("value", nombreDoc);
                        $("#Anexo" + nuAne + " input:eq(1)").attr("value", nombreDoc);
                        reemplazarDoc = false;
                    } else {
                        actualizarListadoAnexos();
                    }
                    alert_Sucess("MENSAJE", "archivo adjuntado");
                    fn_verListadoMsjAnexos(pAnn,pEmi);
                    $("#txtNombreAnexo").attr("value",nombreDoc);
                    $("#txtUpdate").attr("value","1");
                }
            } else {
                alert_Danger("ERROR", "Error al cargar el archivo");
            }
        },
        progressall: function(e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            jQuery('#progress .progress-bar').css('width', progress + '%');
            var progressText = "";
            if (progress < 30) {
                progressText = progress + '%';
            } else {
                progressText = 'Cargando ' + progress + '%';
            }
            jQuery('#progress span').html(progressText);
        }
    });
    jQuery('#fileupload').click();
    
}


function fn_verAnexoMsj(pnuAnn, pnuEmi, pnuDes) {
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goDocRutaAnexo";
        p[1] = "nuAnn=" + pnuAnn;
        p[2] = "nuEmi=" + pnuEmi;
        p[3] = "nuDes=" + pnuDes;
        ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
            var result;
            eval("var docs=" + data);
            if (typeof (docs) != "undefined" && typeof (docs.nuAnn) != 'undefined' && docs.nuAnn != "") {
                //result = fn_abrirDocApplet(docs.noUrl,docs.noDoc);
                var param = {urlDoc: docs.noUrl, rutaDoc: docs.noDoc};
                runApplet(appletsTramiteDoc.abrirDocumento, param, function(data) {
                    result = data;
                });
            }

        }, 'text', false, false, "POST");

    }
    return false;
}

function fn_eliminarArchivoMsj(pNuAnn,pNuEmi,pNuDes, pNuAne)
{

    var p = new Array();
    var accion;

    accion = "accion=goDelete";
 
    p[0] = accion;
    p[1] = "nu_ann=" + pNuAnn;
    p[2] = "nu_emi=" + pNuEmi;
    p[3] = "nu_des=" + pNuDes;
    p[4] = "nu_ane=" + pNuAne;//Hermes 31/01/2019 - Requerimiento Acta 005-20019 
    ajaxCall("/srGestionMensajes.do", p.join("&"), function(data) {
        if (data !== null)
        {
            alert_Sucess("MENSAJE", "archivo eliminado");
            fn_verListadoMsjAnexos(pNuAnn,pNuEmi);
        }
    }, 'text', false, false, "POST");
    return false;
}
/*[HPB] Inicio 18/08/23 OS-0000786-2023 Mejoras*/
function fn_eliminarAdjuntarDoc(pNuAnn,pNuEmi,pNuDes, pNuAne){
    var p = new Array();
    var accion;
    accion = "accion=goDelete";
 
    p[0] = accion;
    p[1] = "nu_ann=" + pNuAnn;
    p[2] = "nu_emi=" + pNuEmi;
    p[3] = "nu_des=" + pNuDes;
    p[4] = "nu_ane=" + pNuAne; 
    
    ajaxCall("/srGestionMensajes.do", p.join("&"), function(data) {
        if (data !== null){
            alert_Sucess("MENSAJE", "archivo eliminado");
            fn_verListadoAdjuntarDocAnexos(pNuAnn,pNuEmi);
        }
    }, 'text', false, false, "POST");
    return false;
}

function fn_verListadoAdjuntarDocAnexos(pann,pemi) {
    var p = new Array();
    var accion;
    accion = "accion=goListadoAdjuntarDocAnexos";
 
    p[0] = accion;
    p[1] = "nuAnn=" + pann;
    p[2] = "nuEmi=" + pemi;
    ajaxCall("/srDocObjeto.do", p.join("&"), function(data) {
        if (data !== null){
            refreshScript("divListadoMsjAnexos", data);
        }
    }, 'text', false, false, "POST");
    return false;
}
/*[HPB] Fin 18/08/23 OS-0000786-2023 Mejoras*/
function calPenEnv()
{
    console.log("calPenEnv");
    
    var TipoMsj=$("#txtPeEnvMsj").val();
    var vBloDias=getNumeroDeDiasDiferencia($("#txtFe_Act").val(),$("#txtFechaEnt").val());
    var vDias=getNumeroDeDiasDiferencia($("#txtFe_Pla").val(),$("#txtFechaEnt").val());
    
    if (vDifDias>0)
    {
        $("#txtFechaDev").val($("#txtFechaEnt").val());
        

    }
   
   $("#txtFechaDev").datepicker("change", { minDate: vBloDias }); 
    
   var fila1 = document.getElementById("rowPen1"); 
   var fila2 = document.getElementById("rowPen2"); 
   
   if (TipoMsj !=="0")
   {
    
  
    fila1.style.display = ""; //ocultar fila 
    fila2.style.display = "";
    
    var vDifDias=getNumeroDeDiasDiferencia($("#txtFechaDev").val(),$("#txtFechaEnt").val());
 
    if (vDias>0)
      {
       $('#txtes_pen').text('SI');
      obj = document.getElementById('txtes_pen');
      obj.style.backgroundColor = (obj.style.backgroundColor = '#ac2925') ? 'none' : '#ac2925';
       $('#txtes_pen_msj').val('S');
      }
   else
      {
       $('#txtes_pen').text('NO');
       obj = document.getElementById('txtes_pen');
      obj.style.backgroundColor = (obj.style.backgroundColor = '#468847') ? 'none' : '#468847';
       $('#txtes_pen_msj').val('N');
      }
    calPenDev();
   }
  else
  {
      fila1.style.display = "none"; //ocultar fila 
      fila2.style.display = "none";
  }
    
    
}

function calPenDev()
{ 
   var TipoMsj=$("#txtPeEnvMsj").val();
    
   var fila1 = document.getElementById("rowPen1"); 
   var fila2 = document.getElementById("rowPen2"); 
   
   if (TipoMsj !=="0")
   {
    
  
    fila1.style.display = ""; //ocultar fila 
    fila2.style.display = "";
    
    fu_calFechaPlazo();
    
    var vDias=getNumeroDeDiasDiferencia($("#txtFe_Pla_Dev").val(),$("#txtFechaDev").val());
     if (vDias>0 && $("#txtFechaDev").val()!==$("#txtFechaEnt").val())
       {
        $('#txtes_dev').text('SI');
       obj = document.getElementById('txtes_dev');
       obj.style.backgroundColor = (obj.style.backgroundColor = '#ac2925') ? 'none' : '#ac2925';
        $('#txtes_pen_dev').val('S');
       }
    else
       {
        $('#txtes_dev').text('NO');
        obj = document.getElementById('txtes_dev');
        obj.style.backgroundColor = (obj.style.backgroundColor = '#468847') ? 'none' : '#468847';
        $('#txtes_pen_dev').val('N');
       }       
            
        
     //var vPla =$("#txtDia_Pla_Dev").val();
       // alert_Danger("dias=>>>",vDias);


   }
   else
   {
      fila1.style.display = "none"; //ocultar fila 
      fila2.style.display = "none";
   }
    
    

           
}

function fn_verDocMsj(pnuAnn, pnuEmi, pnuDes) {
    
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goObtieneDocumentoMsj";
        p[1] = "nuAnn=" + pnuAnn;
        p[2] = "nuEmi=" + pnuEmi;
        p[3] = "nuDes=" + pnuDes;
       
        ajaxCall("/srGestionMensajes.do", p.join("&"), function(data) {
            var result;
            eval("var docs=" + data);
            if (typeof (docs) != "undefined" && typeof (docs.nuAnn) != 'undefined' && docs.nuAnn != "") {
                //result = fn_abrirDocApplet(docs.noUrl,docs.noDoc);
                    var param = {urlDoc: docs.noUrl, rutaDoc: docs.noDoc};
//                runApplet(appletsTramiteDoc.abrirDocumento, param, function(data) {
//                    result = data;
//                });                    
                    runOnDesktop(accionOnDesktopTramiteDoc.abrirDocumento,param, function(data){
                        var result = data;
                    });
            }

        }, 'text', false, false, "POST");

    }
    return false;
}

function fn_revertirMsj(vnu_ann,vnu_emi,vnu_des,vnu_msj) {
   // var pesDocEmi = jQuery('#buscarDocumentoCargaMsjBean').find('#esDocEmi').val();
    if (vnu_ann !== null && vnu_emi !== null) {
//        if (confirm('¿ Esta Seguro de Eliminar ?')) {
//            $('#documentoEmiBean').find('select').removeProp('disabled');
//            ajaxCall("/srDocumentoAdmEmision.do?accion=goEliminarDocEmiAdm", $('#documentoEmiBean').serialize(), function(data) {
//                fn_rptEliminarDocEmiAdm(data);
//            }, 'json', false, false, "POST");
//        }
        bootbox.dialog({
            message: " <h5>¿Esta seguro de revertir este registro?</h5>",
            buttons: {
                SI: {
                    label: "SI",
                    className: "btn-primary",
                    callback: function() {
                       // $('#documentoEmiBean').find('select').removeProp('disabled');
                        ajaxCallSendJson("/srGestionMensajes.do?accion=goRevertirMensaje&nu_ann="+vnu_ann+"&nu_emi="+vnu_emi+"&nu_des="+vnu_des+"&nu_msj="+vnu_msj, "", function(data) {
                           if (data === "Datos guardados.") {
                                alert_Sucess("MENSAJE", "Registro Revertido");                                          
            
                                ajaxCall("/srGestionMensajes.do?accion=goInicio", $('#buscarDocumentoCargaMsjBean').serialize(), function(data) {
                                        $('#divBuscarMensajes').show();
                                        $('#divDescargaMsj').hide();
                                        refreshScript("divTablaMensajes", data);
                                        }, 'text', false, false, "POST");
                
                              
                                    jQuery('#divDescargaMsj').html(""); 
                                    removeDomId('divOrigenMain');
                            } else {
                                alert_Danger("ERROR:", data);
                            }
                        }, 'text', false, false, "POST");
                    }                        
                },
                NO: {
                    label: "NO",
                    className: "btn-default"
                }
            }
        });           
    }
    return false;
}

function fu_calFechaPlazo(){
   
    var p = new Array();

    p[0] = "pfechaent=" + $("#txtFechaEnt").val();
    p[1] = "pdipladev=" + $("#txtDia_Pla_Dev").val();

    ajaxCall("/srGestionMensajes.do?accion=goCalFechaPlazo",  p.join("&"), function(data) {
                if(data!==null){ 
                    if(data.coRespuesta==="1"){ 

                            $("#txtFe_Pla_Dev").val(data.deFechaDev);  

                    }else{
                        alert_Danger("Calcular Fecha Plazo: ",data.deRespuesta);
                    }
                }
        },'json', false, false, "POST");                  


}

//Hermes 22/01/2019 - Requerimiento Acta 005-20019 
function fn_verDocMsjAdicional(pnuAnn, pnuEmi, pnuDes, pNuAne) {    
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goObtieneDocumentoMsjAdicional";
        p[1] = "nuAnn=" + pnuAnn;
        p[2] = "nuEmi=" + pnuEmi;
        p[3] = "nuDes=" + pnuDes;
        p[4] = "nuAne=" + pNuAne;
       
        ajaxCall("/srGestionMensajes.do", p.join("&"), function(data) {
            var result;
            eval("var docs=" + data);
            if (typeof (docs) != "undefined" && typeof (docs.nuAnn) != 'undefined' && docs.nuAnn != "") {
                    var param = {urlDoc: docs.noUrl, rutaDoc: docs.noDoc};
                    runOnDesktop(accionOnDesktopTramiteDoc.abrirDocumento,param, function(data){
                        var result = data;
                    });
            }
        }, 'text', false, false, "POST");
    }
    return false;
}

function fn_grabarDescargaMsjAdicional(form) {    
    console.log("fn_grabarDescargaMsjAdicional");    
    var myForm = form;
    var indicador = $('#txtUpdate').val();
    var indicadorNew = $('#txtIndicador').val();
    console.log(myForm);  
    
    if(indicador==='1'){
        if(indicadorNew==='1'){
            var jsonBody = {
                "nu_ann": $('#'+myForm).find("#txtNu_Ann").val(),
                "nu_emi": $('#'+myForm).find("#txtNu_Emi").val(),             
                "nu_des": $('#'+myForm).find("#txtNu_Des").val(),
                "nu_ane": $('#'+myForm).find("#txtNu_Ane").val(),
                "ob_msj": allTrim($('#'+myForm).find("#txtObservaciones").val()),
                "tip_doc_msj": $('#'+myForm).find("#coTipoDocMsj").val()
            };

            var jsonString = JSON.stringify(jsonBody);        
            var url = "/srGestionMensajes.do?accion=goUpdDescargaMsjAdicional";

            ajaxCallSendJson(url, jsonString, function(data) {
                if (data === "Datos guardados.") {                
                    alert_Sucess("MENSAJE", data);
                    $("#btn-grabar").prop('disabled', true);
                    $("#btn-grabar-accion").prop('disabled', true);
                } else {
                    alert_Danger("ERROR:", data);
                }
            }, false, false, false, "POST");                    
        }else{
            var row = "";
            row = row.concat('Favor de solucionar lo que indica en el mensaje.');
            alert_Danger("ERROR:", row);
        }        
    }else{
        bootbox.dialog({
            message: " <h5>No se realizaron cambios.\n" +
                    "¿ Desea salir ?</h5>",
            buttons: {
                SI: {
                    label: "SI",
                    className: "btn-default",
                    callback: function() {
                        removeDomId('divOrigenMain');
                        removeDomId('windowConsultaArchivar');
                    }                        
                },
                NO: {
                    label: "NO",
                    className: "btn-primary"
                }
            }
        });          
    }
    return;
}

function fn_anexoDocumentoMsjAdicional(tamanioMaxAnexos){
    var reemplazarDoc = true;
    jQuery('#progress .progress-bar').css('width', '0%');
    jQuery('#fileupload').removeAttr("multiple");
    fn_fileUploadMsjAdicional(reemplazarDoc, tamanioMaxAnexos);/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
}

function fn_fileUploadMsjAdicional(reemplazarDoc, tamanioMaxAnexos) {
    $("#progress").show();
    //var pFileSizeMax = 9500000;//9MB HPB 17/02/2020 - Requerimiento capacidad archivo configurable
    var rutactx = pRutaContexto + "/" + pAppVersion;
    var numFiles = 0;
    var contAux = 0;
    var row = "";
    
    jQuery('#fileupload').fileupload({
        dataType: 'text',
        add: function(e, data) {
            var fileSizeMax = tamanioMaxAnexos;/*HPB 17/02/2020 - Requerimiento capacidad archivo configurable*/
            var fileSize = data.files[0].size;
            var fileName = data.files[0].name;
            /*Inicio-------------Valida PDF----------------------*/
            //extensiones_permitidas = new Array(".gif", ".jpg", ".doc", ".pdf");
            var extensiones_permitidas = new Array(".pdf");
            var extension = (fileName.substring(fileName.lastIndexOf("."))).toLowerCase();            
            var permitida = false; 
            
            for (var i = 0; i < extensiones_permitidas.length; i++) { 
               if (extensiones_permitidas[i] == extension) { 
                permitida = true; 
                break; 
               } 
            } 
            if (!permitida) {                 
                row = row.concat('Comprueba la extensión de los archivos a subir. \nSólo se pueden subir archivos con extensiones:', extensiones_permitidas.join(),'');
                jQuery("#divErrorLista").html("");
                jQuery("#divErrorLista").append('<li>' + row + '</li>');
                jQuery("#divError").show();
                jQuery("#divError").attr('display', 'block');
                $('#txtIndicador').val("0");
                return;                
            }          
            /*Fin-------------Valida PDF----------------------*/
            if (fileSize <= fileSizeMax){
                jQuery("#divErrorLista").html("");
                jQuery("#divError").hide();
                jQuery("#divError").attr('display', 'none');                
                numFiles++;
                if (reemplazarDoc === true) {
                    var nuAnn = $("#txtNu_Ann").attr("value");
                    var nuEmi = $("#txtNu_Emi").attr("value");
                    var nuDes = $("#txtNu_Des").attr("value");                    
                    var inUpd = $("#txtUpdate").attr("value");                    
                    var url = "";
                    url = url.concat(rutactx, "/srGestionMensajes.do?accion=goUploadMsjAdicional", "&NuAnn=", nuAnn,"&NuEmi=", nuEmi, "&NuDes=", nuDes, "&InUpd=", inUpd);
                    data.url = url;
                }
                data.submit();
                $('#txtIndicador').val("1");
            } else{
                fileSize = Math.round(fileSize / 1024) + " Kb";
                row = row.concat('Error! el tamaño <strong>', fileSize, '</strong> del archivo <strong>', fileName, '</strong> supera el limite permitido.');
                jQuery("#divErrorLista").html("");
                jQuery("#divErrorLista").append('<li>' + row + '</li>');
                jQuery("#divError").show();
                jQuery("#divError").attr('display', 'block');
                $('#txtIndicador').val("0");
                return;
            }                
        },
        done: function(e, data) {
            if (data.textStatus === "success")
            {
                contAux++;
                if (contAux === numFiles) {
                    if (reemplazarDoc) {
                        var jsonRes = jQuery.parseJSON(data.result);
                        var nombreDoc = jsonRes[0].name;
                        var numeroAnexo = jsonRes[0].nuAne;
                        var nuAne = $("#oNuAne").attr("value");
                        $("#Anexo" + nuAne + " input:eq(0)").attr("value", nombreDoc);
                        $("#Anexo" + nuAne + " input:eq(1)").attr("value", nombreDoc);
                        reemplazarDoc = false;
                    } else {
                        actualizarListadoAnexos();
                    }
                    alert_Sucess("MENSAJE", "archivo adjuntado");
                    $("#txtNombreAnexo").attr("value",nombreDoc);
                    $("#txtUpdate").attr("value","1");
                    $("#txtNu_Ane").attr("value",numeroAnexo);
                }
            } else {
                alert_Danger("ERROR", "Error al cargar el archivo");
            }
        },
        progressall: function(e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            jQuery('#progress .progress-bar').css('width', progress + '%');
            var progressText = "";
            if (progress < 30) {
                progressText = progress + '%';
            } else {
                progressText = 'Cargando ' + progress + '%';
            }
            jQuery('#progress span').html(progressText);
        }
    });
    jQuery('#fileupload').click();
}

function fu_eventoTablaMensajesNew() {
    var oTable;
    oTable = $('#myTableFixed').DataTable( {
        "bPaginate": false,
        "sScrollY": "470px",
        "searching": false
    } );
    
    jQuery.fn.dataTableExt.oSort['fecha-asc'] = function(a, b) {
        var ukDatea = a.split('/');
        var ukDateb = b.split('/');

        var x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
        var y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;

        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    };

    jQuery.fn.dataTableExt.oSort['fecha-desc'] = function(a, b) {
        var ukDatea = a.split('/');
        var ukDateb = b.split('/');

        var x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
        var y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;

        return ((x < y) ? 1 : ((x > y) ? -1 : 0));
    };
    //$("#myTableFixed thead tr").addClass("ui-datatable-fixed-scrollable-header ui-state-default")
    //$("#myTableFixed tbody tr").addClass("bx_sb ui-datatable-fixed-scrollable-body ui-datatable-fixed-data");
    function showdivToolTip(elemento, text)
    {
        $('#divflotante').html(text);

        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotante").css({left: x, top: y});

        //document.getElementById('divflotante').style.top = elemento.top + 24;
        //document.getElementById('divflotante').style.left = elemento.left;		
        document.getElementById('divflotante').style.display = 'block';

        return;
    }
    $("#myTableFixed tbody td").hover(
            function() {
                $(this).attr('id', 'divtitlemostrar');
                //console.log($(this).index());
                var index = $(this).index();
                if (index >= 5 && index <= 9) {
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
    $("#myTableFixed tbody tr").dblclick(function() {
        //jQuery('#txtTextIndexSelect').val("-1"); 
    });
    if(jQuery('#myTableFixed >tbody >tr').length > 0){
        //$('#myTableFixed >tbody >tr').eq(0).addClass('row_selected');
        //var pauxNumFilaSelect = typeof(pnumFilaSelect)!=="undefined"? pnumFilaSelect:0;
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

function fn_goLstDocAdicionalesMsj(pnuAnn, pnuEmi, pnuDes) {        
    if (!!pnuAnn) {
        var p = new Array();
        p[0] = "accion=goLstDocAdicionalesMsj";
        p[1] = "nuAnn=" + pnuAnn;
        p[2] = "nuEmi=" + pnuEmi;
        p[3] = "nuDes=" + pnuDes;
     
        ajaxCall("/srGestionMensajes.do", p.join("&"), function(data) {
            if (data !== null)
            {
                $("body").append(data);
            }
        }, 'text', false, false, "POST");

    }
    return false;    
}
//Hermes 22/01/2019 - Requerimiento Acta 005-20019 
function pintarDiasTranscurridos() {
    var col = 26;
    var col2 = 25;
    var col3 = 13;
    $("#myTableFixed tbody tr").each(function() {
        var diasTranscurridos = $(this).find("td:eq(" + col + ")").text();
        var diasEntrega = $(this).find("td:eq(" + col2 + ")").text();
        var tipoMensajero = $(this).find("td:eq(" + col3 + ")").text();
        if(tipoMensajero =="COURRIER"){
            if(parseInt(diasTranscurridos)>parseInt(diasEntrega)){
                var codigo = 2;
                codigo = parseInt(codigo);
                var color = coloresDiasTranscurridos[codigo].color;
                var clase = coloresDiasTranscurridos[codigo].cssClass;
                
                $(this).find("td:eq(" + col + ")").attr("class", clase);
            } else {
                //seguramente no hay filas en la tabla no hay nada que pintar
            } 
        }
    });
}

var coloresDiasTranscurridos = {
    0: {"text": "Normal", "color": "#009BE6", "descrip": "Documentos sin días límites de atención.", "cssClass": "estadoVenNormal"},
    1: {"text": "Proximo a vencer", "color": "#E97200", "descrip": "Con vencimiento menor o igual a 2 días.", "cssClass": "estadoVenProximoVencer"},
    2: {"color": "#222222", "descrip": "Con la fecha de vencimiento anterior a la fecha actual.", "cssClass": "estadoVenVencido"},
    3: {"text": "Vence hoy", "color": "#D00000", "descrip": "Fecha de vencimiento igual a la fecha actual.", "cssClass": "estadoVenVenceHoy"},
    4: {"text": "Por vencer", "color": "#D9D900", "descrip": "Tiempo de vecimiento mayor a 2 días.", "cssClass": "estadoVenPorVencer"},
    5: {"text": "Atendido", "color": "#009F01", "descrip": "Documentos con la fecha de atención o archivamiento.", "cssClass": "estadoVenAtendido"}
};