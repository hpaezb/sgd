function fn_iniCargoGeneradoInterno(){
    var noForm='buscarCargoEntregaBean';
    jQuery('#'+noForm).find('#esFiltroFecha').val("1");//hoy
    jQuery('#'+noForm).find('#auxCoLocDes').val(jQuery('#'+noForm).find('#coLocDes').val());
    jQuery('#'+noForm).find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 1});        
    pnumFilaSelect=0;
    submitAjaxFormBusCarGeneradoInterno(noForm);  
}

function fn_submitAjaxFormBusCarGeneradoInterno(){
    var noForm='buscarCargoEntregaBean';
    return submitAjaxFormBusCarGeneradoInterno(noForm);
}


function submitAjaxFormBusCarGeneradoInterno(noForm){
    var validaFiltro = fu_validaFormBusCargoGenInterno(noForm);
    if (validaFiltro === "1") {
        ajaxCall("/srCargoEntregaInterno.do?accion=goInicioCargo", $('#'+noForm).serialize(), function(data) {
            refreshScript("divTablaCargosEntregaInterno", data);
        }, 'text', false, false, "POST");
    }
    return false;    
}

function fu_validaFormBusCargoGenInterno(noForm) {
    var valRetorno = "0";
    jQuery('#'+noForm).find('#feGuiaIni').val(jQuery('#'+noForm).find('#fechaFiltro').attr('fini'));
    jQuery('#'+noForm).find('#feGuiaFin').val(jQuery('#'+noForm).find('#fechaFiltro').attr('ffin'));    
    
    
    var vFechaActual = jQuery('#txtFechaActual').val();
    valRetorno = fu_validaFechasFormBusCargoEntregaInterno(vFechaActual,noForm);  

    return valRetorno;
}

function fu_validaFechasFormBusCargoEntregaInterno(vFechaActual,noForm){
    var valRetorno='1';
    fu_obtenerEsFiltroFechaCargoEntregaInterno(noForm);
    var pEsFiltroFecha = jQuery('#'+noForm).find("#esFiltroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
    if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
       if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
            var pAnnio = jQuery('#'+noForm).find('#nuAnnGuia').val();
            if(!!pAnnio){
                var vValidaNumero = fu_validaNumero(pAnnio);
                if (vValidaNumero !== "OK") {
                   bootbox.alert("Año debe ser solo números.");
                    valRetorno = "0";
                }                
            }               
       }   

        var vFeInicio = jQuery('#'+noForm).find("#feGuiaIni").val();
        var vFeFinal = jQuery('#'+noForm).find("#feGuiaFin").val();
        if(valRetorno==="1"){
          var pAnnioBusq = verificarAñoBusquedaFiltro(vFeInicio,vFeFinal);
            if(!!pAnnioBusq){
                var vValidaNumero = fu_validaNumero(pAnnioBusq);
                if (vValidaNumero !== "OK") {
                   bootbox.alert("Año debe ser solo números.");
                    valRetorno = "0";
                }else if(vValidaNumero ==="OK"){
                    jQuery('#'+noForm).find('#nuAnnGuia').val(pAnnioBusq);                          
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
                        var msg_fec = 'Error en Fecha Desde ';
                        msg_fec = msg_fec + vFeInicio;
                        msg_fec = msg_fec + ' Hasta ' + vFeFinal + ' : ';
                        bootbox.alert(msg_fec + valRetorno);
                        //bootbox.alert("Error en Fecha Del : "+ valRetorno);
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
    return valRetorno;    
}

function fu_obtenerEsFiltroFechaCargoEntregaInterno(nameForm){
    var opt = jQuery('#'+nameForm).find('#fechaFiltro').attr('optselected');
    if(opt==="1"||opt==="2"||opt==="4"||opt==="8"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("1"); 
    }else if(opt==="5"||opt==="6"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("2"); 
    }else if(opt==="3"||opt==="7"){
       jQuery('#'+nameForm).find("#esFiltroFecha").val("3"); 
    }
}

function fu_obtenerFechaCalendarDocPendEntregaInterno(nameForm){
    var opt = jQuery('#'+nameForm).find('#fechaCalendar').attr('optselected');
    if(opt==="1"||opt==="2"||opt==="4"||opt==="8"){
       jQuery('#'+nameForm).find("#filtroFecha").val("1"); 
    }else if(opt==="5"||opt==="6"){
       jQuery('#'+nameForm).find("#filtroFecha").val("2"); 
    }else if(opt==="3"||opt==="7"){
       jQuery('#'+nameForm).find("#filtroFecha").val("3"); 
    }
}

function fu_cleanFiltroCarGeneradoInterno(){
    var noForm='#buscarCargoEntregaBean';
    jQuery(noForm).find('#estadoGuiaMp option[value=0]').prop("selected", "selected");
    jQuery(noForm).find("#coLocDes option[value=001]").prop("selected", "selected");    
    jQuery(noForm).find("#coDepDes").val("");
    jQuery(noForm).find("#txtDependencia").val(" [TODOS]");
    jQuery(noForm).find("#esFiltroFecha").val("1");//hoy
    jQuery(noForm).find("#nuAnnGuia").val(jQuery("#txtAnnioActual").val());
    jQuery(noForm).find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 1});    
}

function fu_iniTblCargoEntregaInternoInt(){
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
            {"bSortable": true},
            {"sType": "fecha"},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true}
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
                if (index === 5){
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

            if (typeof($(this).children('td')[0]) !== "undefined") {
                jQuery('#txtpnuAnnGuia').val($(this).children('td')[0].innerHTML);
                jQuery('#txtpnuGuia').val($(this).children('td')[1].innerHTML);
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
}

function fn_goNuevoCargoEntregaInterno(){
    var noForm='buscarCargoEntregaBean';
    var p = new Array();    
    p[0] = "accion=goInicioNewDocPendienteEntregaInterno";	    
    p[1] = "coLoc=" + jQuery('#'+noForm).find("#auxCoLocDes").val();   
    p[2] = "coDep=" + jQuery('#'+noForm).find("#coDepOri").val();   
    ajaxCall("/srCargoEntregaInterno.do",p.join("&"),function(data){                
        if(data!==null){
            refreshScript("divDocsPedienteEntregaInterno", data);
            jQuery('#divDocsPedienteEntregaInterno').toggle();
            jQuery('#divTablaCargosEntregaInterno').html("");
            jQuery('#divBuscarCargoEntregaInterno').toggle();
        }
    },'text', false, true, "GET");    
}

function regresarLsCargosGeneradosInterno(){
    fn_submitAjaxFormBusCarGeneradoInterno();
    jQuery('#divBuscarDocPendEntregaInterno').toggle();
    jQuery('#divDocsPedienteEntregaInterno').toggle();
    jQuery('#divTablaDocPendEntregaInterno').html("");
    jQuery('#divBuscarCargoEntregaInterno').toggle();
}

function fn_iniBusDocPendEntregaInterno(){
    var noForm='buscarDocPendienteEntregaBean';
    jQuery('#'+noForm).find('#filtroFecha').val("1");//hoy
    jQuery('#'+noForm).find("#fechaCalendar").showDatePicker({defaultOpcionSelected: 1});        
    pnumFilaSelect2=0;
    changeTipoBusqDocPendEntrega(noForm,'0');      
}

function changeTipoBusqDocPendEntregaInterno(noForm,tipo) {
    jQuery('#'+noForm).find('#tipoBusqueda').val(tipo);
    submitAjaxFormBusqDocPendEntregaInterno(noForm,tipo);
    mostrarOcultarDivBusqFiltro2();
}

function fn_PrebuscarDocPendEntregaInterno(tipo){
    var noForm='buscarDocPendienteEntregaBean';
    changeTipoBusqDocPendEntregaInterno(noForm,tipo);
}

function regresarLsDocPendEntregaOfNewCargoEntregaInterno(){
    var noForm='#buscarDocPendienteEntregaBean';
    fn_PrebuscarDocPendEntregaInterno(jQuery(noForm).find('#tipoBusqueda').val());
    jQuery('#divNewCargoEntregaInterno').toggle();
    jQuery('#divTablaDocPendEntregaInterno').html("");
    jQuery('#divNewCargoEntregaInterno').html("");
    jQuery('#divDocsPedienteEntregaInterno').toggle();
}

function submitAjaxFormBusqDocPendEntregaInterno(noForm,tipo) {
    var validaFiltro = fu_validaFormBusqDocPendEntregaInterno(noForm,tipo);
    if (validaFiltro === "1") {
        ajaxCall("/srCargoEntregaInterno.do?accion=goInicioNewDocPendienteEntregaInterno", $('#'+noForm).serialize(), function(data) {
            refreshScript("divTablaDocPendEntregaInterno", data);
        }, 'text', false, false, "POST");
    }
    return false;
}

function fu_validaFormBusqDocPendEntregaInterno(noForm,tipo) {
    var valRetorno = "1";
    jQuery('#'+noForm).find('#feEmiIni').val(jQuery('#'+noForm).find('#fechaCalendar').attr('fini'));
    jQuery('#'+noForm).find('#feEmiFin').val(jQuery('#'+noForm).find('#fechaCalendar').attr('ffin'));    
    
    
    var pEsIncluyeFiltro = $('#'+noForm).find('#esIncluyeFiltro1').is(':checked');
    var vFechaActual = jQuery('#txtFechaActual').val();
    if(tipo==="0"){
        valRetorno = fu_validaFechasFormBusDocPendEntregaInterno(vFechaActual,noForm);     
    }else if(tipo==="1"){
        valRetorno = fu_validaFormBusConfBusDocPendEntregaInterno(noForm);  
        if(valRetorno==="1"){
            if(pEsIncluyeFiltro){
               valRetorno = fu_validaFechasFormBusDocPendEntregaInterno(vFechaActual,noForm); 
            }else{
               valRetorno = setAnnioNoIncludeFiltroDocPendEntregaInterno(noForm);
            }
        }
    }    
    return valRetorno;
}

function setAnnioNoIncludeFiltroDocPendEntregaInterno(noForm){
    var valRetorno = "1";
    fu_obtenerFechaCalendarDocPendEntregaInterno(noForm);
    var pEsFiltroFecha = jQuery('#'+noForm).find("#filtroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
    if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
       if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
            var pAnnio = jQuery('#'+noForm).find('#nuAnn').val();
            if(pAnnio!==null && pAnnio!=="null" && typeof(pAnnio)!=="undefined" && pAnnio!==""){
                var vValidaNumero = fu_validaNumero(pAnnio);
                if (vValidaNumero !== "OK") {
                   bootbox.alert("Año debe ser solo números.");
                    valRetorno = "0";
                }                
            }               
       }     

        var vFeInicio = jQuery('#'+noForm).find('#feEmiIni').val();
        var vFeFinal = jQuery('#'+noForm).find('#feEmiFin').val();

        if(valRetorno==="1" /*&& pEsFiltroFecha==="1"*/){
          var pAnnioBusq = obtenerAnioBusqueda(vFeInicio,vFeFinal);
            if(pAnnioBusq!==null && pAnnioBusq!=="null" && typeof(pAnnioBusq)!=="undefined" && pAnnioBusq!==""){
                var vValidaNumero = fu_validaNumero(pAnnioBusq);
                if (vValidaNumero !== "OK") {
                   bootbox.alert("Año debe ser solo números.");
                    valRetorno = "0";
                }else if(vValidaNumero ==="OK"){
                    jQuery('#'+noForm).find('#nuAnn').val(pAnnioBusq);                          
                }                
            }               
        }
    }
    return valRetorno;       
}

function fu_validaFormBusConfBusDocPendEntregaInterno(noForm) {
    var valRetorno = "1";
    
    upperCaseFormConfigBusDocPendEntregaInterno(noForm);
    
    //var vnuCorEmi = allTrim(jQuery('#'+noForm).find('#nuCorEmi').val());
    var vnuExpediente = allTrim(jQuery('#'+noForm).find('#nuExpediente').val());
    var vdeAsu = allTrim(jQuery('#'+noForm).find('#deAsu').val());
    
    if(!(!!vnuExpediente||!!vdeAsu)){
       alert_Warning("Buscar: ","Ingresar Algún parámetro de Búsqueda");
       valRetorno = "0";
    }
    
//    if(valRetorno==="1"){
//        if (!!vnuCorEmi) {
//            var vValidaNumero = fu_validaNumero(vnuCorEmi);
//            if (vValidaNumero !== "OK") {
//                alert_Warning("Buscar: ","N° de Registro debe ser solo números.");
//                valRetorno = "0";
//            }
//        }
//    }
    return valRetorno;
}

function fu_validaFechasFormBusDocPendEntregaInterno(vFechaActual,noForm){
    var valRetorno='1';
    fu_obtenerFechaCalendarDocPendEntrega(noForm);
    var pEsFiltroFecha = jQuery('#'+noForm).find("#filtroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
    if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
       if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
            var pAnnio = jQuery('#'+noForm).find('#nuAnn').val();
            if(!!pAnnio){
                var vValidaNumero = fu_validaNumero(pAnnio);
                if (vValidaNumero !== "OK") {
                   bootbox.alert("Año debe ser solo números.");
                    valRetorno = "0";
                }                
            }               
       }   

        var vFeInicio = jQuery('#'+noForm).find("#feEmiIni").val();
        var vFeFinal = jQuery('#'+noForm).find("#feEmiFin").val();
        if(valRetorno==="1"){
          var pAnnioBusq = verificarAñoBusquedaFiltro(vFeInicio,vFeFinal);
            if(!!pAnnioBusq){
                var vValidaNumero = fu_validaNumero(pAnnioBusq);
                if (vValidaNumero !== "OK") {
                   bootbox.alert("Año debe ser solo números.");
                    valRetorno = "0";
                }else if(vValidaNumero ==="OK"){
                    jQuery('#'+noForm).find('#nuAnn').val(pAnnioBusq);                          
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
                        var msg_fec = 'Error en Fecha Desde ';
                        msg_fec = msg_fec + vFeInicio;
                        msg_fec = msg_fec + ' Hasta ' + vFeFinal + ' : ';
                        bootbox.alert(msg_fec + valRetorno);                        
                        // bootbox.alert("Error en Fecha Del : "+ valRetorno);
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
    return valRetorno;    
}

function upperCaseFormConfigBusDocPendEntregaInterno(noForm){
    //jQuery('#'+noForm).find('#nuCorEmi').val(fu_getValorUpperCase(jQuery('#'+noForm).find('#nuCorEmi').val()));
    jQuery('#'+noForm).find('#nuExpediente').val(fu_getValorUpperCase(jQuery('#'+noForm).find('#nuExpediente').val()));
    jQuery('#'+noForm).find('#deAsu').val(fu_getValorUpperCase(jQuery('#'+noForm).find('#deAsu').val()));
}

function fu_iniTblDocPendEntregaInterno(){
    var nomTabla='#tblDocPendEntregaInterno';
    var indexFilaClick = -1;
    $(nomTabla+" tbody tr").click(function() {
        if (indexFilaClick !== -1) {
            if ($(nomTabla+" tbody tr:eq(" + indexFilaClick + ")").hasClass('row_selected')) {
                $(nomTabla+" tbody tr:eq(" + indexFilaClick + ")").removeClass('row_selected');
            }
        }
        if ($(this).hasClass('row_selected')) {
            $(this).removeClass('row_selected');
        }else {
            $(this).addClass('row_selected');
            indexFilaClick = $(this).index();
        }
    });
    $(nomTabla+" tbody td").hover(
            function() {
                $(this).attr('id', 'divtitlemostrar');
                var index = $(this).index();
                if (index === 4 || index === 8 || index === 9) {
                    showdivToolTip($('#divtitlemostrar').position(), $(this).html());
                }
            },
            function() {
                $('#divtitlemostrar').removeAttr('id');
                $('#divflotante').hide();
            }
    );
    function showdivToolTip(elemento, text){
        $('#divflotante').html(text);
        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotante").css({left: x, top: y});

        document.getElementById('divflotante').style.display = 'block';

        return;
    }        
}

function fu_cleanBusDecPendEntregaInterno(tipo){
    var noForm='#buscarDocPendienteEntregaBean';
    if (tipo==="1") {
        //jQuery(noForm).find('#nuCorEmi').val("");
        jQuery(noForm).find('#nuExpediente').val("");
        jQuery(noForm).find('#deAsu').val("");
        jQuery(noForm).find("#esIncluyeFiltro1").prop('checked',false);
        jQuery(noForm).find("#esIncluyeFiltro1").attr('checked',false);        
        jQuery(noForm).find("#feEmiIni").val("");
        jQuery(noForm).find("#feEmiFin").val("");
    } else if(tipo==="0"){
        jQuery(noForm).find("#coTipDocAdm option[value=]").prop("selected", "selected");
        jQuery(noForm).find("#coLocDes option[value=001]").prop("selected", "selected");
        jQuery(noForm).find('#coDepDes').val("");
        jQuery(noForm).find('#txtDependenciaDes').val(" [TODOS]");
        jQuery(noForm).find("#filtroFecha").val("1");//hoy
        jQuery(noForm).find("#nuAnn").val(jQuery("#txtAnnioActual").val());
        jQuery(noForm).find("#fechaCalendar").showDatePicker({defaultOpcionSelected: 1});
    }    
}

function fn_changeChkIncluirDocPedEntregaInterno(chk){
    var nomTabla='#tblDocPendEntregaInterno';
    if(chk.checked){
        $(nomTabla + " tbody tr").each(function(index, row) {
            $(row).find('#chkTblIncluir').attr('checked',true);
            $(row).find('#chkTblIncluir').prop('checked',true);
        });        
    }else{
        $(nomTabla + " tbody tr").each(function(index, row) {
            $(row).find('#chkTblIncluir').attr('checked',false);
            $(row).find('#chkTblIncluir').prop('checked',false);
        });         
    }
}

function fn_submitAjaxGenerarCargoInterno(){
    var json=fn_preObtenerJsontblDocPendEntregaInterno();
    var vResult=fn_verificarLsDocPendEntregaSelecInterno(new Function('return ' + json)());
    if(vResult==='1'){
        //var noForm='#buscarDocPendienteEntregaBean';
        //var pcoDepDes="null";
        //var coDepDes=jQuery(noForm).find('#coDepDes').val();
        //var deDepDes=jQuery(noForm).find('#txtDependenciaDes').val();
        //if(jQuery(noForm).find('#tipoBusqueda').val()==="0"&&!!coDepDes){
            //pcoDepDes=coDepDes;
        //}
        ajaxCallSendJson("/srCargoEntregaInterno.do?accion=goGeneraCargoEntregaInterno"/*&pcoDepDes="+pcoDepDes+"&pdeDepDes="+deDepDes*/, json, function(data) {
            fn_preRptaGenerarCargoInterno(data);
        },'text', false, false, "POST");        
    }else{
        alert_Info("Cargo :", "Seleccione filas.");
    }
}

function fn_preRptaGenerarCargoInterno(XML_AJAX){
    if (XML_AJAX !== null) {
        var obj = null;
        try{obj = jQuery.parseJSON(XML_AJAX);}catch(e){}        
        if(obj){
            if(obj.coRespuesta==="0"){
                alert_Info("Cargo :", "Seleccionar registros de una misma dependencia.");
            }
        }else{
            fn_rptaGenerarCargoInterno(XML_AJAX);
        }         
    }
}

function fn_rptaGenerarCargoInterno(data){
    if(!!data){
        refreshScript("divNewCargoEntregaInterno", data);
        jQuery('#divNewCargoEntregaInterno').toggle();
        jQuery('#divTablaDocPendEntregaInterno').html("");
        jQuery('#divDocsPedienteEntregaInterno').toggle();        
    }
}

function fn_verificarLsDocPendEntregaSelecInterno(LsDoc){
    var vReturn='0';
    if(!!LsDoc){
        if(LsDoc.length>0){
            vReturn='1';
        }
    }
    return vReturn;
}

function fn_preObtenerJsontblDocPendEntregaInterno(){
    //Observaciones RBN
    //El armado de Json se maneja posicion por posicion, por tanto hay que tener mucho cuidado
    //al momento de incluir nuevos campos, se sugiere ponerlos al final, para evitar mover los
    //campos ya dados y evitar errores
    //OJO nuCorEmi ya se encontraba comentado
    var nomTabla='#tblDocPendEntregaInterno';
    var nomChk='#chkTblIncluir';
    var arrColEnviar = new Array();
    arrColEnviar[0] = "nuAnn=1";
    arrColEnviar[1] = "nuEmi=2";
    arrColEnviar[2] = "nuDes=3";
    //arrColEnviar[3] = "nuCorEmi=4";
    arrColEnviar[3] = "deOriEmi=5";
    arrColEnviar[4] = "feEmiCorta=6";
    arrColEnviar[5] = "deTipDocAdm=7";
    arrColEnviar[6] = "nuDoc=8";
    arrColEnviar[7] = "deDepDes=9";
    return fn_obtenerJsontblDocPendEntregaInterno(nomTabla,arrColEnviar,nomChk);
}

function fn_obtenerJsontblDocPendEntregaInterno(nomTabla,colMostrar,nomChk){
    var otArr = [];
    $(nomTabla + " tbody tr").each(function(index, row) {
        var itArr = [];
        var isRowCheck=$(row).find(nomChk).is(':checked');
        if(isRowCheck){
            var x = $(this).children();
            x.each(function(index) {
             for (var i = 0; i < colMostrar.length; i++) {
                var auxArrColMostrar = colMostrar[i].split("=");
                if (auxArrColMostrar[1] * 1 === index + 1) {
                    var campoBean = auxArrColMostrar[0];
                    var valCampoBean = $(this).text();
                    itArr.push('"' + campoBean + '":' + JSON.stringify(valCampoBean));
                }
             }   
            });
            otArr.push('{' + itArr.join(',') + '}');
        }
    });
    return '[' + otArr.join(",") + ']';    
}

function fn_iniCargoEntregaInterno(){
    var noForm='#guiaMesaPartesBean';
    jQuery(noForm).find("#feGuiMp").change(function() {
       var vResult=fn_changeFeGuiMpCargoEntregaInterno(noForm);
       if(vResult==="1"){
        jQuery(noForm).find('#envGuiaMp').val("1");           
       }
    });
    jQuery(noForm).find("#deObs").change(function() {
        jQuery(noForm).find('#envGuiaMp').val("1");
    });
    var pnuAnnGuia=jQuery(noForm).find("#nuAnn").val();
    var pnuGuia=jQuery(noForm).find("#nuGuia").val();
    if(!(!!pnuAnnGuia)||!(!!pnuGuia)){
        jQuery(noForm).find('#envGuiaMp').val("1");        
    }
}

function fu_iniTblGuiaMesaPartesInterno(){
    var nomTabla='#tblDetGuiaMPInterno';
    $(nomTabla+" tbody td").hover(
            function() {
                $(this).attr('id', 'divtitlemostrar');
                var index = $(this).index();
                if (index === 7 || index === 8) {
                    showdivToolTip($('#divtitlemostrar').position(), $(this).html());
                }
            },
            function() {
                $('#divtitlemostrar').removeAttr('id');
                $('#divflotante').hide();
            }
    );
    function showdivToolTip(elemento, text){
        $('#divflotante').html(text);
        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotante").css({left: x, top: y});

        document.getElementById('divflotante').style.display = 'block';

        return;
    }        
}

function fn_changeFeGuiMpCargoEntregaInterno(noForm){
    var valRetorno="0";
    var pFeGuia=jQuery(noForm).find("#feGuiMp").val();
    if(!!pFeGuia){
        if(moment(pFeGuia, "DD/MM/YYYY HH:mm").isValid()){
            var fecha=moment(pFeGuia, ["DD","DD/MM","DD/MM/YY","DD/MM/YY HH:mm","DD/MM/YYYY","DD/MM/YYYY HH:mm"]); 
            if(fecha.isValid()){
                if(fecha.hour()===0){
                    fecha.hour(moment().hour());
                }
                if(fecha.minute()===0){
                    fecha.minute(moment().minute());
                }
                jQuery(noForm).find("#feGuiMp").val(moment(fecha,"DD/MM/YYYY HH:mm").format("DD/MM/YYYY HH:mm"));
                valRetorno="1";
            }            
        }else{
             bootbox.alert("<h5>Fecha Inválida.</h5>", function() {
                 bootbox.hideAll();
                 jQuery(noForm).find('#feGuiMp').focus();
             });
        }
    }else{
        bootbox.alert("<h5>Indicar fecha.</h5>", function() {
            bootbox.hideAll();
            jQuery(noForm).find('#feGuiMp').focus();
        });        
    }
    return valRetorno;
}

function fn_goGrabarCargoEntregaInterno(){
    var noForm='#guiaMesaPartesBean';
    if(fn_verificarFormCargoEntregaInterno(noForm)==="1"){
        var json=fn_buildSendJsontoServerGuiaMpInterno(noForm);
        //verificar si necesita grabar el documento.
        var rpta = fu_verificarChangeCargoEntregaInterno(noForm,json);
        var nrpta = rpta.substr(0,1);
        if (nrpta === "1") {    
            ajaxCallSendJson("/srCargoEntregaInterno.do?accion=goGrabarGuiaMpInterno", json, function(data) {
                fn_rptaGrabarCargoEntregaInterno(data,noForm);
            },'json', false, false, "POST");           
        }else{
            alert_Info("", rpta);
        } 
    }
}

function fn_verificarFormCargoEntregaInterno(noForm){
//    var tblDetGuia='tblDetGuiaMPInterno';
    var pcoLocOri=allTrim(jQuery(noForm).find('#coLocOri').val());
    var pcoDepOri=allTrim(jQuery(noForm).find('#coDepOri').val());
    var pcoLocDes=allTrim(jQuery(noForm).find('#coLocDes').val());
    var pnuAnn=allTrim(jQuery(noForm).find('#nuAnn').val());
//    var pnuGuia=allTrim(jQuery(noForm).find('#nuGuia').val());
    var pfeGuiMp=allTrim(jQuery(noForm).find('#feGuiMp').val());
    var pdeObs=allTrim(jQuery(noForm).find('#deObs').val());
    var maxLengthDeObs=jQuery(noForm).find('#deObs').attr('maxlength');
    
    var valRetorno = "0";
    var vValidaNumero = "";
    
    if(!!pcoLocOri&&pcoLocOri.length>1){
        valRetorno="1";
        jQuery(noForm).find('#coLocOri').val(pcoLocOri);
    }else{
        bootbox.alert("<h5>Indicar Local Origen.</h5>", function() {
            bootbox.hideAll();
            //jQuery(noForm).find('#coLocOri').focus();
        });        
    }
    
    if(valRetorno==="1"){
       if(!!pcoDepOri&&pcoDepOri.length>1){
           jQuery(noForm).find('#coDepOri').val(pcoDepOri);
       }else{
           valRetorno="0";
           bootbox.alert("<h5>Indicar Dependencia Origen.</h5>", function() {
               bootbox.hideAll();
               //jQuery(noForm).find('#coDepOri').focus();
           });            
       } 
    }
    
    if(valRetorno==="1"){
       if(!!pcoLocDes&&pcoLocDes.length>1){
           jQuery(noForm).find('#coLocDes').val(pcoLocDes);
       }else{
           valRetorno="0";
           bootbox.alert("<h5>Indicar Local Destino.</h5>", function() {
               bootbox.hideAll();
               //jQuery(noForm).find('#coLocDes').focus();
           });            
       }         
    }
    
    if(valRetorno==="1"){
       if(!!pnuAnn&&pnuAnn.length>1){
           valRetorno = "0";           
           vValidaNumero = fu_validaNumero(pnuAnn);
            if (vValidaNumero === "OK"){
                valRetorno = "1";           
                jQuery(noForm).find('#nuAnn').val(pnuAnn);                    
            }else{
                bootbox.alert("<h5>Número Año debe ser solo números.</h5>", function() {
                    bootbox.hideAll();
                });            
            }  
       }else{
           valRetorno="0";
           bootbox.alert("<h5>Indicar Año.</h5>", function() {
               bootbox.hideAll();
               //jQuery(noForm).find('#coLocDes').focus();
           });            
       }         
    }
    
//    if(valRetorno==="1"){
//        if(!!pnuAnn&&!!pnuGuia){
//          //implementar  
//        }else{
//            
//        }
//    }
    
   if(valRetorno==="1"){
       if(!!pfeGuiMp){
           valRetorno="0";
            if(moment(pfeGuiMp, "DD/MM/YYYY HH:mm").isValid()){
                var fecha=moment(pfeGuiMp, ["DD","DD/MM","DD/MM/YY","DD/MM/YY HH:mm","DD/MM/YYYY","DD/MM/YYYY HH:mm"]); 
                if(fecha.isValid()){
                    if(fecha.hour()===0){
                        fecha.hour(moment().hour());
                    }
                    if(fecha.minute()===0){
                        fecha.minute(moment().minute());
                    }
                    jQuery(noForm).find("#feGuiMp").val(moment(fecha,"DD/MM/YYYY HH:mm").format("DD/MM/YYYY HH:mm"));
                    valRetorno="1";
                }                
            }else{
                valRetorno="0";
                 bootbox.alert("<h5>Fecha Inválida.</h5>", function() {
                     bootbox.hideAll();
                     jQuery(noForm).find('#feGuiMp').focus();
                 });
            }       
       }else{
           valRetorno="0";
            bootbox.alert("<h5>Indicar fecha.</h5>", function() {
                bootbox.hideAll();
                jQuery(noForm).find('#feGuiMp').focus();
            });
       }
   }
   
   if(valRetorno==="1"){
       if(!!pdeObs){
        if(!!maxLengthDeObs){
            var nrolinesDeAsu = (pdeObs.match(/\n/g) || []).length;
            if(pdeObs.length+nrolinesDeAsu > maxLengthDeObs){
                valRetorno = "0";
                bootbox.alert("<h5>La Observación Excede el límite de "+maxLengthDeObs+" caracteres.</h5>", function() {
                    bootbox.hideAll();
                    jQuery(noForm).find('#deObs').focus();
                });
            }
        }           
       }
   }   
   return valRetorno;    
}

function fu_verificarChangeCargoEntregaInterno(noForm,cadenaJson) {//si es "1" necesita grabar el documento.
    var pnuAnn=jQuery(noForm).find('#nuAnn').val();
    var pnuGuia=jQuery(noForm).find('#nuGuia').val();
    var rpta = fn_validarEnvioGrabaCargoEntregaInterno(new Function('return ' + cadenaJson)());
    var nrpta = rpta.substr(0, 1);
    if (nrpta === "1"||!(!!pnuGuia&&!!pnuAnn)){
        return "1";
    }else{
        return rpta.substr(1);
    }
}

function fn_validarEnvioGrabaCargoEntregaInterno(objTrxDocumentoEmiBean) {
    var vReturn = "0EL DOCUMENTO ES EL MISMO.";
    if (objTrxDocumentoEmiBean !== null && typeof(objTrxDocumentoEmiBean) !== "undefined") {
        if (typeof(objTrxDocumentoEmiBean.guia) !== "undefined") {
            vReturn = "1A GRABAR";
        } else if (typeof(objTrxDocumentoEmiBean.lsDetGuia) !== "undefined") {
            if (objTrxDocumentoEmiBean.lsDetGuia.length > 0) {
                vReturn = "1A GRABAR";
            }
        }
    }
    return vReturn;
}

function fn_rptaGrabarCargoEntregaInterno(Obj,noForm){
    if(!!Obj){
        var vRespuesta=Obj.coRespuesta;
        if(!!vRespuesta){
            if(vRespuesta==="1"){
                var accion=Obj.accionBd;
                if(accion==="INS"){
                    var guia=Obj.guiaBean;
                    if(!!guia){
                        jQuery(noForm).find('#nuGuia').val(guia.nuGuia);
                        jQuery(noForm).find('#nuCorGui').val(guia.nuCorGuia);
                        jQuery(noForm).find('#nuAnn').val(guia.nuAnn);
                        var lsDetGuia=Obj.lstDetGuia;
                        if(!!lsDetGuia){
                            fn_actualizarTblDetalleGuiaInterno(lsDetGuia);
                        }
                        jQuery(noForm).find('#envGuiaMp').val("0");
                        alert_Sucess("Éxito!", "Documento grabado correctamente.");           
                    }
                }else if(accion==="UPD"){
                    jQuery(noForm).find('#envGuiaMp').val("0");
                    alert_Sucess("Éxito!", "Documento grabado correctamente.");           
                }
            }else{
                alert_Danger("Cargo Entrega: ",Obj.deRespuesta);
            }
        }
    }
}

function fn_actualizarTblDetalleGuiaInterno(lsDetGuia){
    var noTblDetGuia='#tblDetGuiaMPInterno';
    $(noTblDetGuia+" tbody tr").each(function(index, row) {
        $.each(lsDetGuia, function(i, obj) {
           var pnuAnn=$(row).find('td:eq(0)').html();
           var pnuEmi=$(row).find('td:eq(1)').html();
           var pnuDes=$(row).find('td:eq(2)').html();          
           if(pnuAnn===obj.nuAnn&&pnuEmi===obj.nuEmi
                   &&pnuDes===obj.nuDes){
               $(row).find('td:eq(3)').html(obj.nuCorr);
           }
        });
    });
}

function fn_buildSendJsontoServerGuiaMpInterno(noForm) {
    var result = "{";
    result = result + '"nuAnnGuia":"' + jQuery(noForm).find('#nuAnn').val() + '",';
    result = result + '"nuGuia":"' + jQuery(noForm).find('#nuGuia').val() + '",';
    var valEnvio = jQuery(noForm).find('#envGuiaMp').val();
    if (valEnvio === "1") {
        result = result + '"guia":' + JSON.stringify(getJsonFormGeneralCargoEntregaBeanInterno(noForm,getArrCampoBeanGuiaMpInterno())) + ',';
    }
    result = result + '"lsDetGuia":[' + fn_tblDetCargoEmihtml2jsonInterno('#tblDetGuiaMPInterno',0,fn_getArrCampoBeanDetGuiaMpInterno(),4) + ']';
    return result + "}";
}

function fn_getArrCampoBeanDetGuiaMpInterno() {
    var arrColEnviar = new Array();
    arrColEnviar[0] = "nuAnn=1";
    arrColEnviar[1] = "nuEmi=2";
    arrColEnviar[2] = "nuDes=3";
    arrColEnviar[3] = "nuCor=4";
    return arrColEnviar;
}

function getArrCampoBeanGuiaMpInterno(){
    var arrCampoBean = new Array();
    arrCampoBean[0] = "nuAnn";
    arrCampoBean[1] = "nuGuia";
    arrCampoBean[2] = "coLocOri";
    arrCampoBean[3] = "coDepOri";
    arrCampoBean[4] = "coLocDes";
    arrCampoBean[5] = "coDepDes";
    arrCampoBean[6] = "feGuiMp";
    arrCampoBean[7] = "deObs";
    return arrCampoBean;
}

/**
 * 
 * @param {type} idTable nombre de la tabla
 * @param {type} iniFila a partir de que fila se empieza a contar
 * @param {type} colEnviar columnas a enviar al server con sus respectivos campos
 * @param {type} colNroCorr columna de numero correlativo de acuerdo a esto se envia las filas al server.
 * @returns {String} return json.
 */
function fn_tblDetCargoEmihtml2jsonInterno(idTable, iniFila, colEnviar,colNroCorr) {
    //var json = '[';
    var otArr = [];
    var count = 0;
    $(idTable + ' tr').each(function(i) {
        if (count >= iniFila) {
            var x = $(this).children();
            var itArr = [];
            if (!(!!($(this).find("td").eq(colNroCorr - 1).text()))) {
                x.each(function(index) {
                    for (var i = 0; i < colEnviar.length; i++) {
                        var auxArrColMostrar = colEnviar[i].split("=");
                        if (auxArrColMostrar[1] * 1 === index + 1) {
                            var campoBean = auxArrColMostrar[0];
                            var valCampoBean = "";
                            if (typeof($(this).find('input[type=text]').val()) !== "undefined") {
                                valCampoBean = $(this).find('input[type=text]').val();
                            } else if (typeof($(this).find('textarea').val()) !== "undefined") {
                                valCampoBean = $(this).find('textarea').val();
                            } else if (typeof($(this).find('select').val()) !== "undefined") {
                                valCampoBean = $(this).find('select').val();
                            } else if (typeof($(this).find('input[type=radio]').val()) !== "undefined") {
                                valCampoBean = $(this).find('input[type=radio]:checked').val();
                            } else {
                                valCampoBean = $(this).text();
                            }
                            itArr.push('"' + campoBean + '":' + JSON.stringify(valCampoBean));
                        }
                    }
                });
                otArr.push('{' + itArr.join(',') + '}');
            }
        }
        count++;
    });
    //json += otArr.join(",") + ']';
    return otArr.join(",");
}

function getJsonFormGeneralCargoEntregaBeanInterno(noForm,arrCampoBean) {
    var o = {};
    var a = $(noForm).serializeArray();
    $.each(a, function() {
        for (var i = 0; i < arrCampoBean.length; i++) {
            if (this.name === arrCampoBean[i]) {
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            }
        }
    });
    return o;
}

function fn_goEditarCargoEntregaInterno(){
    var pnuAnnGuia=jQuery('#txtpnuAnnGuia').val();
    var pnuGuia=jQuery('#txtpnuGuia').val();
    if(!!pnuAnnGuia&&pnuGuia){
        var p = new Array();    
        p[0] = "accion=goEditCargoEntregaInterno";	    
        p[1] = "pnuAnnGuia=" + pnuAnnGuia;   
        p[2] = "pnuGuia=" + pnuGuia;           
        ajaxCall("/srCargoEntregaInterno.do?accion=goEditCargoEntregaInterno", p.join("&"), function(data) {
            fn_rptaEditarCargoEntregaInterno(data);
        }, 'text', false, false, "POST");        
    }
}

function fn_rptaEditarCargoEntregaInterno(data){
    if(!!data){
        refreshScript("divNewCargoEntregaInterno", data);
        jQuery('#divNewCargoEntregaInterno').toggle();
        jQuery('#divTablaCargosEntregaInterno').html("");
        jQuery('#divBuscarCargoEntregaInterno').toggle();        
    }
}

function fn_backCargoEntregaInterno(){
    var noForm='#guiaMesaPartesBean';
    var whoCalled=jQuery(noForm).find('#whoCalled').val();
    if(!!whoCalled){
        if(whoCalled==="1"){//editar cargo
            regresarLsCargosGeneradosOfNewCargoEntregaInterno();
        }else if(whoCalled==="0"){//nuevo cargo
            regresarLsDocPendEntregaOfNewCargoEntregaInterno();
        }
    }
}

function regresarLsCargosGeneradosOfNewCargoEntregaInterno(){
    fn_submitAjaxFormBusCarGeneradoInterno();
    jQuery('#divNewCargoEntregaInterno').toggle();
    jQuery('#divTablaCargosEntregaInterno').html("");
    jQuery('#divNewCargoEntregaInterno').html("");
    jQuery('#divBuscarCargoEntregaInterno').toggle();
}

function fn_anularCargoEntregaMpInterno(){
    var noForm='#guiaMesaPartesBean';
    var pnuAnnGuia=jQuery(noForm).find('#nuAnn').val();
    var pnuGuia=jQuery(noForm).find('#nuGuia').val();
    if (!!pnuAnnGuia&&!!pnuGuia) {
        var estadoGuia=jQuery(noForm).find('#estadoGuia').val();
        if(!!estadoGuia&&estadoGuia==="0"){
            bootbox.dialog({
                message: " <h5>¿ Esta Seguro de Anular Cargo ?</h5>",
                buttons: {
                    SI: {
                        label: "SI",
                        className: "btn-primary",
                        callback: function() {
                            var p = new Array();
                            p[0] = "accion=goAnularGuiaMpInterno";
                            p[1] = "nuAnn=" + pnuAnnGuia;    
                            p[2] = "nuGuia=" + pnuGuia;   
                            ajaxCall("/srCargoEntregaInterno.do", p.join("&"), function(data) {
                                fn_rptAnularCargoEntregaMpInterno(data);
                            }, 'json', false, false, "POST"); 
                        }                        
                    },
                    NO: {
                        label: "NO",
                        className: "btn-default"
                    }
                }
            });   
        }else{
          alert_Warning("Cargo Entrega :", "No se puede anular el cargo porque el destino ya recibió el documento.");  
        }        
    }else {
        alert_Info("Cargo Entrega :", "Necesita grabar los cambios.");
    }
}

function fn_rptAnularCargoEntregaMpInterno(data){
    if(!!data){
       if(data.coRespuesta==="1"){
           fn_backCargoEntregaInterno();
       }else{
          alert_Warning("Cargo Entrega :", data.deRespuesta);   
       } 
    }
}

function fn_imprimirCargoEntregaInterno(){
    var noForm='#guiaMesaPartesBean';
    var pnuAnnGuia=jQuery(noForm).find('#nuAnn').val();
    var pnuGuia=jQuery(noForm).find('#nuGuia').val();
    if (!!pnuAnnGuia&&!!pnuGuia) {
        var p = new Array();
        p[0] = "accion=goExportarArchivoListaInterno";
        p[1] = "pnuAnnGuia=" + pnuAnnGuia;    
        p[2] = "pnuGuia=" + pnuGuia;   
        ajaxCall("/srCargoEntregaInterno.do", p.join("&"), function(data) {          
            if (data!==null) {
                if(data.coRespuesta==="0"){
                    if(!!data.noUrl&&!!data.noDoc){
//                        fn_generaDocApplet_personal(data.noUrl,data.noDoc,function(data){
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
    } else {
        alert_Info("Cargo Entrega :", "Necesita grabar los cambios.");
    }
}

function fn_iniConsDestDocPendEntrega(){
        var tableaux = $('#tlbDestinoEmi');
        tableaux.find('tr').each(function(index, row) {
            if(index == 0){
                $(this).addClass('row_selected');                        
                return false;
            }
        });
        var searchOnTable = function(evento) {
                var table = $('#tlbDestinoEmi');
                var value = this.value;
                //alert(evento.which);
                var isFirst = false;
                var indexSelect = -1;
                table.find('tr').each(function(index, row) {
                        if ( $(this).hasClass('row_selected') ) {
                            $(this).removeClass('row_selected');
                        }
                        //var allCells = $(row).find('td');
                        var allCells = $(row).find('td');
                        if(allCells.length > 0) {
                                var found = false;
                                allCells.each(function(index, td) {
                                        var regExp = new RegExp(value, 'i');
                                        if(regExp.test($(td).text())) {
                                                found = true;
                                                return false;
                                        }
                                });
                                if (found == true) {$(row).show(); if(!isFirst){$(this).addClass('row_selected'); isFirst = true; indexSelect = index;}}
                                else {$(row).hide();}
                        }
                });
                if(evento.which == 13){
                    if(isFirst){
                        var pdesDest= $("#tlbDestinoEmi tbody tr:eq("+indexSelect+")").find("td:eq(1)").html();
                        var pcodDest= $("#tlbDestinoEmi tbody tr:eq("+indexSelect+")").find("td:eq(2)").html();
                        fu_setDatoDestinatarioDocPendEntrega(pcodDest,pdesDest);
                    }
                }
        };        
        $('#txtConsultaFind').keyup(searchOnTable);
        $("#tlbDestinoEmi tbody tr").click(function(e) {
            var pdesDest= $(this).find("td:eq(1)").html();
            var pcodDest= $(this).find("td:eq(2)").html();
            fu_setDatoDestinatarioDocPendEntrega(pcodDest,pdesDest);            
        });    
}

function fu_setDatoDestinatarioDocPendEntrega(cod, desc){
    var noForm='#buscarDocPendienteEntregaBean';
    jQuery(noForm).find('#txtDependenciaDes').val(desc);
    jQuery(noForm).find('#coDepDes').val(cod);
    removeDomId('windowConsultaDestinoEmi');
}

function fu_buscaDependenciaDestinoDocPendEntregaInterno(){
    var noForm='#buscarDocPendienteEntregaBean';
    var p = new Array();
    p[0] = "accion=goBuscaDependenciaInterno";
    p[1] = "pcoLocDes=" + jQuery(noForm).find('#coLocDes').val();
    ajaxCall("/srCargoEntregaInterno.do", p.join("&"), function(data) {
        fn_rptaBuscaReferenciaOrigen(data);
    },'text', false, false, "POST");    
}

function fn_changeLocalFiltroDocPendEnrtegaInterno(){
    var noForm='#buscarDocPendienteEntregaBean';
    jQuery(noForm).find('#coDepDes').val("");
    jQuery(noForm).find('#txtDependenciaDes').val(" [TODOS]");
}

function fn_buscaDepDestCargoEntregaInterno(){
    var noForm='#buscarCargoEntregaBean';
    var p = new Array();
    p[0] = "accion=goBuscaDependenciaCargoEntregaInterno";
    p[1] = "pcoLocDes=" + jQuery(noForm).find('#coLocDes').val();
    ajaxCall("/srCargoEntregaInterno.do", p.join("&"), function(data) {
        fn_rptaBuscaReferenciaOrigen(data);
    },'text', false, false, "POST");      
}

function fn_changeLocalFiltroCargoGeneradosInterno(){
    var noForm='#buscarCargoEntregaBean';
    jQuery(noForm).find('#coDepDes').val("");
    jQuery(noForm).find('#txtDependencia').val(" [TODOS]");    
}

function fn_iniConsDestCargosGenerados(){
        var tableaux = $('#tlbDestinoEmi');
        tableaux.find('tr').each(function(index, row) {
            if(index == 0){
                $(this).addClass('row_selected');                        
                return false;
            }
        });
        var searchOnTable = function(evento) {
                var table = $('#tlbDestinoEmi');
                var value = this.value;
                //alert(evento.which);
                var isFirst = false;
                var indexSelect = -1;
                table.find('tr').each(function(index, row) {
                        if ( $(this).hasClass('row_selected') ) {
                            $(this).removeClass('row_selected');
                        }
                        //var allCells = $(row).find('td');
                        var allCells = $(row).find('td');
                        if(allCells.length > 0) {
                                var found = false;
                                allCells.each(function(index, td) {
                                        var regExp = new RegExp(value, 'i');
                                        if(regExp.test($(td).text())) {
                                                found = true;
                                                return false;
                                        }
                                });
                                if (found == true) {$(row).show(); if(!isFirst){$(this).addClass('row_selected'); isFirst = true; indexSelect = index;}}
                                else {$(row).hide();}
                        }
                });
                if(evento.which == 13){
                    if(isFirst){
                        var pdesDest= $("#tlbDestinoEmi tbody tr:eq("+indexSelect+")").find("td:eq(1)").html();
                        var pcodDest= $("#tlbDestinoEmi tbody tr:eq("+indexSelect+")").find("td:eq(2)").html();
                        fu_setDatoDestinatarioCargosGenerados(pcodDest,pdesDest);
                    }
                }
        };        
        $('#txtConsultaFind').keyup(searchOnTable);
        $("#tlbDestinoEmi tbody tr").click(function(e) {
            var pdesDest= $(this).find("td:eq(1)").html();
            var pcodDest= $(this).find("td:eq(2)").html();
            fu_setDatoDestinatarioCargosGenerados(pcodDest,pdesDest);            
        });    
}

function fu_setDatoDestinatarioCargosGenerados(cod, desc){
    var noForm='#buscarCargoEntregaBean';
    jQuery(noForm).find('#txtDependencia').val(desc);
    jQuery(noForm).find('#coDepDes').val(cod);
    removeDomId('windowConsultaDestinoEmi');
}

function fu_iniTblDetGuiaMpEditInterno(){
    var nomTabla='#tblDetGuiaMPInterno';
    $(nomTabla+" tbody td").hover(
            function() {
                $(this).attr('id', 'divtitlemostrar');
                var index = $(this).index();
                if (index === 8 || index === 9) {
                    showdivToolTip($('#divtitlemostrar').position(), $(this).html());
                }
            },
            function() {
                $('#divtitlemostrar').removeAttr('id');
                $('#divflotante').hide();
            }
    );
    function showdivToolTip(elemento, text){
        $('#divflotante').html(text);
        var x = elemento.left;
        var y = elemento.top + 24;
        $("#divflotante").css({left: x, top: y});

        document.getElementById('divflotante').style.display = 'block';

        return;
    }
}    