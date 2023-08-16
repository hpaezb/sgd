function fn_inicializaConsulDocPendientes(sCoAnnio,fechaActual){    
    jQuery('#buscarDocumentoPendienteConsulBean').find('#esFiltroFecha').val("2");//rango fecha
    jQuery('#buscarDocumentoPendienteConsulBean').find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 1});/*[HPB] 06/11/20 Modificaciones en filtro de busqueda*/    

    jQuery('#buscarDocumentoPendienteConsulBean').find('#sNroDocumento').change(function() {
        $(this).val(replicate($(this).val(), 6));
    }); 
    
    jQuery('#buscarDocumentoPendienteConsulBean').find('#busCoAnnio').find('option[value=""]').remove();
    pnumFilaSelect=0;
    changeTipoBusqEmiDocuPendienteConsul("0");  
}

function changeTipoBusqEmiDocuPendienteConsul() {
    jQuery('#buscarDocumentoPendienteConsulBean').find('#sTipoBusqueda').val("0");
    submitAjaxFormEmiDocPendienteConsul("0");
    mostrarOcultarDivBusqFiltro2();
}

function submitAjaxFormEmiDocPendienteConsul(tipo) {
    var validaFiltro = fu_validaFiltroDocPendienteConsul(tipo);
    if (validaFiltro === "1") {
        ajaxCall("/srConsultaDocPendientes.do?accion=goInicio", $('#buscarDocumentoPendienteConsulBean').serialize(), function(data) {
            refreshScript("divTablaConsultaDocPendiente", data);
        }, 'text', false, false, "POST");
    }else if(validaFiltro === "2"){//buscar referencia
        ajaxCall("/srConsultaDocPendientes.do?accion=goBuscaDocumentoEnReferencia", $('#buscarDocumentoPendienteConsulBean').serialize(), function(data) {
            fu_rptaBuscaDocumentoEnReferenciaConsul(data);
        }, 'text', false, false, "POST");        
    }
    return false;
}

function fu_iniTblDocPendientesConsul(){
    var oTable;
    oTable = $('#myTableFixed').dataTable({
        "bPaginate": false,
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
            {"bSortable": true}
        ],
        "aoColumnDefs": [
            {
                "sWidth": "4%",
                "aTargets": [ 3 ]
            },
            {
                "sWidth": "8%",
                "aTargets": [ 4 ]
            },{
                "sWidth": "10%",
                "aTargets": [ 5 ]
            },
            {               
                "sWidth": "16%",
                "aTargets": [ 6 ]
            },
            {               
                "sWidth": "10%",
                "aTargets": [ 7 ]
            },
            {               
                "sWidth": "20%",
                "aTargets": [ 8 ]
            },
            {               
                "sWidth": "10%",
                "aTargets": [ 9 ]
            },
            {               
                "sWidth": "14%",
                "aTargets": [ 10 ]
            },
            {               
                "sWidth": "10%",
                "aTargets": [ 11 ]
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
    
    function showdivToolTip(elemento, text){
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
//                if ((index >= 3 && index <= 11 )|| index === 12 || index === 13  ) {
//                    showdivToolTip($('#divtitlemostrar').position(), $(this).html());
//                }
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
                $('#txtpnuAnn').val($(this).children('td')[0].innerHTML);
                $('#txtpnuEmi').val($(this).children('td')[1].innerHTML);
                $('#txtpnuDes').val($(this).children('td')[2].innerHTML);
                jQuery('#txtCoEstadoDoc').val($(this).children('td')[12].innerHTML);
                pnumFilaSelect = $(this).index();
            }
        }
    });
    
    if($('#myTableFixed >tbody >tr').length > 0){
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

function fu_cleanDocPendientesConsul(tipo){
    if(tipo==="0"){
        jQuery("#coTipoPersona option[value=]").prop("selected", "selected");
        jQuery("#sEstadoDoc option[value=]").prop("selected", "selected");
        jQuery("#sTipoDoc option[value=]").prop("selected", "selected");
        jQuery("#sDestinatario").val("");
        jQuery("#sNroDocumento").val("");
        jQuery("#sBuscNroExpediente").val("");
        jQuery("#sBuscAsunto").val("");
        jQuery("#fechaFiltro").showDatePicker({defaultOpcionSelected: 5});   
    }
}

function fu_validaFiltroDocPendienteConsul(tipo){
    var valRetorno="1";
    
    jQuery('#sFeEmiIni').val(jQuery('#sCoAnnio').parent('td').find('#fechaFiltro').attr('fini'));
    jQuery('#sFeEmiFin').val(jQuery('#sCoAnnio').parent('td').find('#fechaFiltro').attr('ffin'));    
        
    var pEsIncluyeFiltro = $('#buscarDocumentoPendienteConsulBean').find('#esIncluyeFiltro1').is(':checked');
    
    var vFechaActual = jQuery('#txtFechaActual').val();
    if(tipo==="0"){
        valRetorno = fu_validaFiltroDocPendientesFiltrar(vFechaActual);     
    }else if(tipo==="1"){
        if(valRetorno==="1"){
            if(pEsIncluyeFiltro){
               valRetorno = fu_validaFiltroDocPendientesFiltrar(vFechaActual); 
            }else{
               valRetorno = setAnnioNoIncludeFiltroPendientes();
            }
        }       
    }       
    return valRetorno;
}

function fu_validaFiltroDocPendientesFiltrar(vFechaActual){
    var valRetorno = "1";
    if (valRetorno==="1") {
        fu_obtenerEsFiltroFecha('buscarDocumentoPendienteConsulBean');
        var pEsFiltroFecha = jQuery('#buscarDocumentoPendienteConsulBean').find("#esFiltroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
        if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
           if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
                var pAnnio = jQuery('#buscarDocumentoPendienteConsulBean').find('#sCoAnnio').val();
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
                        jQuery('#buscarDocumentoPendienteConsulBean').find('#sCoAnnio').val(pAnnioBusq);                          
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

function setAnnioNoIncludeFiltroPendientes(){
    var valRetorno = "1";
    fu_obtenerEsFiltroFecha('buscarDocumentoPendienteConsulBean');
    var pEsFiltroFecha = jQuery('#buscarDocumentoPendienteConsulBean').find("#esFiltroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
    if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
       if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
            var pAnnio = jQuery('#buscarDocumentoPendienteConsulBean').find('#sCoAnnio').val();
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
          var pAnnioBusq = obtenerAnioBusqueda(vFeInicio,vFeFinal);
            if(pAnnioBusq!==null && pAnnioBusq!=="null" && typeof(pAnnioBusq)!=="undefined" && pAnnioBusq!==""){
                var vValidaNumero = fu_validaNumero(pAnnioBusq);
                if (vValidaNumero !== "OK") {
                   bootbox.alert("Año debe ser solo números.");
                    valRetorno = "0";
                }else if(vValidaNumero ==="OK"){
                    jQuery('#buscarDocumentoPendienteConsulBean').find('#sCoAnnio').val(pAnnioBusq);                          
                }                
            }               
        }
    }
    return valRetorno;       
}

function buscarProveedorEditDocPendientesConsul(prazonSocial){
    
    var snoRazonSocial = allTrim(fu_getValorUpperCase(prazonSocial));
    snoRazonSocial=fn_getCleanTextExpReg(snoRazonSocial);
    snoRazonSocial=snoRazonSocial.trim();
    
    if(allTrim(snoRazonSocial).length >= 0 && allTrim(snoRazonSocial).length <= 1){  //El texto es entre 1 y 3 caracteres
            bootbox.alert("<h5>Debe ingresar como mínimo 2 caracteres.</h5>", function() {
            bootbox.hideAll();
        });  
        return;
    }         
    if (!!snoRazonSocial) {
        var rptValida = validaCaracteres(snoRazonSocial, "2");
        if (rptValida === "OK") {
            var cadena ="^[A-Z0-9ÑÁÉÍÓÚ ]+$";
            var re = new RegExp(cadena);
            if(snoRazonSocial.match(re)){      
                var p = new Array();
                p[0] = "accion=goBuscaProveedorBus";
                p[1] = "prazonSocial=" + snoRazonSocial;
                ajaxCall("/srConsultaDocPendientes.do", p.join("&"), function(data) {
                    fn_rptaBuscarProveedorEditDocExtRecep(data);
                },'text', false, false, "POST");
            }else{
                bootbox.alert("<h5>Los caracteres ingresados deben contener solo números y letras.</h5>");                  
            }                     
        } else {
            bootbox.alert("<h5>"+rptValida+".</h5>");            
        }
    } else {
        bootbox.alert("<h5>Debe ingresar datos en Nombre Persona Jurídica.</h5>");                    
    }
    return false;
}

function onclickBuscarProveedorDocPendientesConsul(){
    var noForm='buscarDocumentoPendienteConsulBean';
    var prazonSocial=allTrim(jQuery('#'+noForm).find('#busDescRuc').val());
    var pnuRucAux=allTrim(jQuery('#'+noForm).find('#nuRucAux').val());
    
    if(!!pnuRucAux&&pnuRucAux.length===11){
        buscarProveedorDocPendientesConsul(pnuRucAux,noForm);
    }else if(!!pnuRucAux && pnuRucAux.length > 0){
        buscarProveedorDocPendientesConsul(pnuRucAux,noForm);
    }else{
        buscarProveedorEditDocPendientesConsul(prazonSocial);
    }
}

function buscarProveedorDocPendientesConsul(){
    var noForm='buscarDocumentoPendienteConsulBean';
    var prazonSocial=allTrim(jQuery('#'+noForm).find('#busDescRuc').val());
    var pnuRucAux=allTrim(jQuery('#'+noForm).find('#nuRucAux').val());
    
    if(!!pnuRucAux&&pnuRucAux.length===11){
        buscarProveedorEditDocRecepAdmJson(pnuRucAux,noForm);
    }
}

function fn_changeTipoRemiDocPendientesConsult(cmbTipoRemite,esIni){
    var noForm='buscarDocumentoPendienteConsulBean';
    jQuery('#'+noForm).find("#busResultado").val("");
    var pcoTipoRemi=$(cmbTipoRemite).val();
    
    if(!!pcoTipoRemi){
        if(esIni==="0"){
            jQuery('#'+noForm).find('#nuCorDoc').val("");
            jQuery('#envRemitenteEmiBean').val("1");
            jQuery('#envDocumentoEmiBean').val("1");
        }
        $("#divRemPersonaJuri_").hide();
        $("#divRemCiudadano_").hide();
        $("#divRemOtros_").hide();        
        if(pcoTipoRemi==='02'){//juridica
            $("#divRemPersonaJuri_").show();
             jQuery('#divRemCiudadano_').find('input').val("");
             jQuery('#divRemOtros_').find('input').val("");            
            jQuery('#'+noForm).find('#nuRucAux').focus();
        }else if(pcoTipoRemi==='03'){//ciudadano
            $("#divRemCiudadano_").show();
             jQuery('#divRemPersonaJuri_').find('input').val("");
             jQuery('#divRemOtros_').find('input').val("");            
             jQuery('#'+noForm).find('#nuDniAux').focus();
        }else if(pcoTipoRemi==='04'){//otros
            $("#divRemOtros_").show();
             jQuery('#divRemPersonaJuri_').find('input').val("");
             jQuery('#divRemCiudadano_').find('input').val("");            
             jQuery('#'+noForm).find('#divRemOtros_').focus();
        }
    }
}

function fn_iniConsProveedorEditDocPendienteConsul(){
    jQuery('#busResultado').val(""); 
    jQuery('#busNumRuc').val(""); 
    jQuery('#nuRucAux').val(""); 
    
    var nomTbl='#tblDestinatario';
    var indexAux=-1;
    var tableaux = $(nomTbl);
    tableaux.find('tr').each(function(index, row) {
        if(index == 0){
            $(this).addClass('row_selected');                        
            return false;
        }
    });
    var searchOnTable = function(evento) {
            var table = $(nomTbl);
            var value = this.value;
            var isFirst = false;
            var indexSelect = -1;
            table.find('tr').each(function(index, row) {
                    if ( $(this).hasClass('row_selected') ) {
                        $(this).removeClass('row_selected');
                    }
                    var allCells = $(row).find('td:eq(0)');
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
                    var prazonSocial= $(nomTbl+" tbody tr:eq("+indexSelect+")").find("td:eq(0)").html();
                    var pnuRuc= $(nomTbl+" tbody tr:eq("+indexSelect+")").find("td:eq(1)").html();
                    fn_setProveedorEditPenDocConsul(prazonSocial,pnuRuc);
                }
            }else if(evento.which==38||evento.which==40){
                indexAux=-1;
                $(nomTbl+' >tbody >tr:visible').first().focus(); 
            }
    };
    $('#txtConsultaFind').keyup(searchOnTable);
    $(nomTbl+" tbody tr").click(function(e) {
        var prazonSocial= $(this).find("td:eq(0)").html();
        var pnuRuc= $(this).find("td:eq(1)").html();
        fn_setProveedorEditPenDocConsul(prazonSocial,pnuRuc);
    });
    $(nomTbl+' >tbody >tr:visible').keydown(function(evento){
        if(evento.which==38){//up
            if ($(nomTbl+" tbody tr:visible").eq(indexAux).prev('tr').length == 0) {
                indexAux=$(nomTbl+" tbody tr:visible").length;
            }
            $(nomTbl+" tbody tr:visible").eq(indexAux).removeClass('row_selected');
            indexAux--;
            $(nomTbl+" tbody tr:visible").eq(indexAux).addClass('row_selected');
            $(nomTbl+" tbody tr:visible").eq(indexAux).focus();
        }else if(evento.which==40){//down
            if ($(nomTbl+" tbody tr:visible").eq(indexAux).next('tr').length == 0){
                indexAux=-1;
            }
            $(nomTbl+" tbody tr:visible").eq(indexAux).removeClass('row_selected');
            indexAux++;
            $(nomTbl+" tbody tr:visible").eq(indexAux).addClass('row_selected');                
            $(nomTbl+" tbody tr:visible").eq(indexAux).focus();
        }else if(evento.which==13){
            var prazonSocial= $(this).find("td:eq(0)").html();
            var pnuRuc= $(this).find("td:eq(1)").html();
            fn_setProveedorEditPenDocConsul(prazonSocial,pnuRuc);      
        }
        evento.preventDefault();
    });    
}

function fn_setProveedorEditPenDocConsul(prazonSocial,pnuRuc){
    prazonSocial = prazonSocial.replace(/&amp;/g, "&");
    var noForm='buscarDocumentoPendienteConsulBean';
    jQuery('#'+noForm).find('#busDescRuc').val(prazonSocial);
    jQuery('#'+noForm).find('#busNumRuc').val(pnuRuc);
    jQuery('#'+noForm).find('#nuRucAux').val(pnuRuc);
    removeDomId('windowConsultaProveedor');
    jQuery('#'+noForm).find('#coTipoPersona').focus();
    jQuery('#envRemitenteEmiBean').val("1");    
    jQuery('#busResultado').val("1");
        
    return false;
}

function onclickBuscarCiudadanoPenDocConsul(){
    var noForm='buscarDocumentoPendienteConsulBean';
    var pDesCiudadano=allTrim(jQuery('#'+noForm).find('#busDescDni').val());
    var pnuDniAux=allTrim(jQuery('#'+noForm).find('#nuDniAux').val());
    
    if(!!pnuDniAux && pnuDniAux.length > 0){
        fn_getCiudadanoPenDocConsul();
    }else{
        buscarCiudadanoEditDocPenConsul(pDesCiudadano);
    }
}

function fn_getCiudadanoPenDocConsul(){
    var noForm='buscarDocumentoPendienteConsulBean';
    var pnuDniAux=allTrim(jQuery('#'+noForm).find("#nuDniAux").val());
    var vResult=fn_validarNuDniRemiDocRecAdm(noForm,pnuDniAux);
    
    if(vResult){
        var p = new Array();
        p[0] = "accion=goBuscaCiudadano";
        p[1] = "pnuDoc=" + pnuDniAux;
        ajaxCall("/srMesaPartes.do", p.join("&"), function(data) {
            fn_rptaGetCiudadanoRemDocRecAdm(data,noForm);
        },'json', false, false, "POST");   
    }    
    
}

function buscarCiudadanoEditDocPenConsul(pDesCiudadano){
    
    var sDescCiudadano = allTrim(fu_getValorUpperCase(pDesCiudadano));
    sDescCiudadano=fn_getCleanTextExpReg(sDescCiudadano);
    sDescCiudadano=sDescCiudadano.trim();
    
    if(allTrim(sDescCiudadano).length >= 0 && allTrim(sDescCiudadano).length <= 1){  //El texto es entre 1 y 3 caracteres
            bootbox.alert("<h5>Debe ingresar como mínimo 2 caracteres.</h5>", function() {
            bootbox.hideAll();
        });  
        return;
    } 
    
    if (!!sDescCiudadano) {
        var rptValida = validaCaracteres(sDescCiudadano, "2");
        if (rptValida === "OK") {
            var cadena ="^[A-Z0-9ÑÁÉÍÓÚ ]+$";
            var re = new RegExp(cadena);
            if(sDescCiudadano.match(re)){      
                var p = new Array();
                p[0] = "accion=goBuscaCiudadano";
                p[1] = "sDescCiudadano=" + sDescCiudadano;
                ajaxCall("/srConsultaDocPendientes.do", p.join("&"), function(data) {
                    fn_rptaBuscarCiudadanoEditDocRecepAdm(data);
                },'text', false, false, "POST");
            }else{
                bootbox.alert("<h5>Los caracteres ingresados deben contener solo números y letras.</h5>");                  
            }                     
        } else {
            bootbox.alert("<h5>"+rptValida+".</h5>");            
        }
    } else {
        bootbox.alert("<h5>Debe ingresar datos en Nombre del ciudadano.</h5>");                    
    }
    return false;
}

function fn_iniConsCiudadanoPenDocConsul(){
    jQuery('#busResultado').val(""); 
    jQuery('#busNumDni').val(""); 
    jQuery('#nuDniAux').val(""); 
    
    var nomTbl='#tblDestinatario';
    var indexAux=-1;
    var tableaux = $(nomTbl);
    tableaux.find('tr').each(function(index, row) {
        if(index == 0){
            $(this).addClass('row_selected');                        
            return false;
        }
    });
    var searchOnTable = function(evento) {
            var table = $(nomTbl);
            var value = this.value;
            //alert(evento.which);
            var isFirst = false;
            var indexSelect = -1;
            table.find('tr').each(function(index, row) {
                if ( $(this).hasClass('row_selected') ) {
                    $(this).removeClass('row_selected');
                }
                var allCells = $(row).find('td:eq(0)');
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
                    var pDesCiudadano= $(nomTbl+" tbody tr:eq("+indexSelect+")").find("td:eq(0)").html();
                    var pnuDni= $(nomTbl+" tbody tr:eq("+indexSelect+")").find("td:eq(1)").html();
                    fn_setCiudadanoEditPenDocConsul(pDesCiudadano,pnuDni);
                }
            }else if(evento.which==38||evento.which==40){
                indexAux=-1;
                $(nomTbl+' >tbody >tr:visible').first().focus(); 
            }
    };
    $('#txtConsultaFind').keyup(searchOnTable);
    $(nomTbl+" tbody tr").click(function(e) {
        var pDesCiudadano= $(this).find("td:eq(0)").html();
        var pnuDni= $(this).find("td:eq(1)").html();
        fn_setCiudadanoEditPenDocConsul(pDesCiudadano,pnuDni);
    });
    $(nomTbl+' >tbody >tr:visible').keydown(function(evento){
        if(evento.which==38){//up
            if ($(nomTbl+" tbody tr:visible").eq(indexAux).prev('tr').length == 0) {
                indexAux=$(nomTbl+" tbody tr:visible").length;
            }
            $(nomTbl+" tbody tr:visible").eq(indexAux).removeClass('row_selected');
            indexAux--;
            $(nomTbl+" tbody tr:visible").eq(indexAux).addClass('row_selected');
            $(nomTbl+" tbody tr:visible").eq(indexAux).focus();
        }else if(evento.which==40){//down
            if ($(nomTbl+" tbody tr:visible").eq(indexAux).next('tr').length == 0){
                indexAux=-1;
            }
            $(nomTbl+" tbody tr:visible").eq(indexAux).removeClass('row_selected');
            indexAux++;
            $(nomTbl+" tbody tr:visible").eq(indexAux).addClass('row_selected');                
            $(nomTbl+" tbody tr:visible").eq(indexAux).focus();
        }else if(evento.which==13){
            var pDesCiudadano= $(this).find("td:eq(0)").html();
            var pnuDni= $(this).find("td:eq(1)").html();
            fn_setCiudadanoEditPenDocConsul(pDesCiudadano,pnuDni);      
        }
        evento.preventDefault();
    });    
}

function fn_setCiudadanoEditPenDocConsul(pDesCiudadano,pnuDni){
    pDesCiudadano = pDesCiudadano.replace(/&amp;/g, "&");
    var noForm='buscarDocumentoPendienteConsulBean';
    jQuery('#'+noForm).find('#busDescDni').val(pDesCiudadano);
    jQuery('#'+noForm).find('#busNumDni').val(pnuDni);
    jQuery('#'+noForm).find('#nuDniAux').val(pnuDni);
    removeDomId('windowConsultaOtroOri');
    jQuery('#'+noForm).find('#coTipoPersona').focus();
    jQuery('#envRemitenteEmiBean').val("1");
    jQuery('#busResultado').val("1");
        
    return false;
}

function fn_getOtroOrigenPenDocConsul(){    
    var noForm='buscarDocumentoPendienteConsulBean';
    var pdesOtroOri=allTrim(jQuery('#'+noForm).find("#busNomOtros").val()).toUpperCase();
    var vResult=validaCaracteres(pdesOtroOri, "2");
    if(pdesOtroOri.length >= 0 && pdesOtroOri.length <= 1){  //El texto es entre 0 y 1 caracteres
        bootbox.alert("<h5>Debe ingresar como mínimo 2 caracteres.</h5>", function() {
            bootbox.hideAll();        
        });
        vResult="NO_OK";
    }
    if(vResult == "OK"){
        var p = new Array();
        p[0] = "accion=goBuscaOtroOrigen";
        p[1] = "pdesOtroOri=" + pdesOtroOri;
        ajaxCall("/srConsultaDocPendientes.do", p.join("&"), function(data) {
            fn_rptaBuscarRemitenteOtroDocRecAdm(data);
        },'text', false, false, "POST"); 
    }
    return false;
}

function fn_iniConsOtroOrigenPenDocConsul(){
    jQuery('#busResultado').val(""); 
    jQuery("#busCoOtros").val("");    
    
    var nomTbl='#tblDestinatario';
    var indexAux=-1;
    var tableaux = $(nomTbl);
    tableaux.find('tr').each(function(index, row) {
        if(index == 0){
            $(this).addClass('row_selected');                        
            return false;
        }
    });
    var searchOnTable = function(evento) {
        var table = $(nomTbl);
        var value = this.value;
        var isFirst = false;
        var indexSelect = -1;
        table.find('tr').each(function(index, row) {
            if ( $(this).hasClass('row_selected') ) {
                $(this).removeClass('row_selected');
            }
            var allCells = $(row).find('td:eq(0)');
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
                var pdesDest= $(nomTbl+" tbody tr:eq("+indexSelect+")").find("td:eq(0)").html();
                /*var ptipDocInden= $(nomTbl+" tbody tr:eq("+indexSelect+")").find("td:eq(1)").html();
                var pnroDocInden= $(nomTbl+" tbody tr:eq("+indexSelect+")").find("td:eq(2)").html();*/
                var pcodDest= $(nomTbl+" tbody tr:eq("+indexSelect+")").find("td:eq(3)").html();
                console.log("First");
                fn_setOtroOrigenEditPenDocConsul(pdesDest,pcodDest);
            }
        }else if(evento.which==38||evento.which==40){
            indexAux=-1;
            $(nomTbl+' >tbody >tr:visible').first().focus(); 
        }
    };        
    $('#txtConsultaFind').keyup(searchOnTable);
    $(nomTbl+" tbody tr").click(function(e) {
        var pdesDest= $(this).find("td:eq(0)").html();
        /*var ptipDocInden= $(this).find("td:eq(1)").html();
        var pnroDocInden= $(this).find("td:eq(2)").html();*/
        var pcodDest= $(this).find("td:eq(3)").html();        
        fn_setOtroOrigenEditPenDocConsul(pdesDest,pcodDest);
    }); 
    $(nomTbl+' >tbody >tr:visible').keydown(function(evento){
        if(evento.which==38){//up
            if ($(nomTbl+" tbody tr:visible").eq(indexAux).prev('tr').length == 0) {
                indexAux=$(nomTbl+" tbody tr:visible").length;
            }
            $(nomTbl+" tbody tr:visible").eq(indexAux).removeClass('row_selected');
            indexAux--;
            $(nomTbl+" tbody tr:visible").eq(indexAux).addClass('row_selected');
            $(nomTbl+" tbody tr:visible").eq(indexAux).focus();
        }else if(evento.which==40){//down
            if ($(nomTbl+" tbody tr:visible").eq(indexAux).next('tr').length == 0){
                indexAux=-1;
            }
            $(nomTbl+" tbody tr:visible").eq(indexAux).removeClass('row_selected');
            indexAux++;
            $(nomTbl+" tbody tr:visible").eq(indexAux).addClass('row_selected');                
            $(nomTbl+" tbody tr:visible").eq(indexAux).focus();
        }else if(evento.which==13){
            var pdesDest= $(this).find("td:eq(0)").html();
            /*var ptipDocInden= $(this).find("td:eq(1)").html();
            var pnroDocInden= $(this).find("td:eq(2)").html();*/
            var pcodDest= $(this).find("td:eq(3)").html();
            console.log("Third");
            fn_setOtroOrigenEditPenDocConsul(pdesDest,pcodDest);    
        }
        //console.log(indexAux);
        evento.preventDefault();
    });    
}

function fn_setOtroOrigenEditPenDocConsul(desDest, codDest){
    desDest = desDest.replace(/&amp;/g, "&");
    var noForm='buscarDocumentoPendienteConsulBean';
    jQuery('#'+noForm).find('#busNomOtros').val(desDest);
    jQuery('#'+noForm).find('#busCoOtros').val(codDest);
    removeDomId('windowConsultaOtroOri');
    jQuery('#'+noForm).find('#coTipoPersona').focus();
    jQuery('#envRemitenteEmiBean').val("1");    
    jQuery('#busResultado').val("1");
    
    return false;    
}

function fu_generarReporteConsultaDocPendientesXLS(){
    fu_generarReporteDocPendientesConsul('XLS');
}

function fu_generarReporteDocPendientesConsul(pformatoReporte){    
    var validaFiltro = fu_validaFiltroDocPendienteConsul("0");

    if (validaFiltro === "1") {        
        ajaxCall("/srConsultaDocPendientes.do?accion=goExportarArchivoLista&pformatRepor="+pformatoReporte, $('#buscarDocumentoPendienteConsulBean').serialize(), function(data) {
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