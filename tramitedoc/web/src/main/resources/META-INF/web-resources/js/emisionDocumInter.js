function fn_buscaReferenciaOrigenInter() {
    var p = new Array();
    p[0] = "accion=goBuscaReferenciaOrigen";
    p[1] = "pnuAnn=" + jQuery('#sCoAnnio').val();
    ajaxCall("/srDocumentoEmisionInteroperabilidad.do", p.join("&"), function(data) {
        fn_rptaBuscaReferenciaOrigenInter(data);
    },
            'text', false, false, "POST");
}

function fn_rptaBuscaReferenciaOrigenInter(XML_AJAX) {
    if (XML_AJAX !== null) {
        $("body").append(XML_AJAX);
    }
}

function fu_setDatoReferenOrigenInter(cod, desc) {
    jQuery('#txtRefOrigen').val(desc);
    jQuery('#sRefOrigen').val(cod);
    removeDomId('windowConsultaRefOri');
}

function fn_buscaDestinatarioEmiInter() {
    var p = new Array();
    p[0] = "accion=goBuscaDestinatario";
    p[1] = "pnuAnn=" + jQuery('#sCoAnnio').val();
    ajaxCall("/srDocumentoEmisionInteroperabilidad.do", p.join("&"), function(data) {
        fn_rptaBuscaDestinatarioEmiInter(data);
    },
            'text', false, false, "POST");
}

function fn_rptaBuscaDestinatarioEmiInter(XML_AJAX) {
    if (XML_AJAX !== null) {
        $("body").append(XML_AJAX);
    }
}

function fu_setDatoDestinatarioEmiInter(cod, desc) {
    jQuery('#txtDestinatario').val(desc);
    jQuery('#sDestinatario').val(cod);
    removeDomId('windowConsultaDestinoEmi');
}

function fn_buscaElaboradoPorInter() {
    var codDependencia = jQuery('#sCoDependencia').val();
    ajaxCall("/srDocumentoEmisionInteroperabilidad.do?accion=goBuscaElaboradoPor&pcoDep=" + codDependencia, '', function(data) {
        fn_rptaBuscaElaboradoPorInter(data);
    },
            'text', false, false, "POST");
}

function fn_rptaBuscaElaboradoPorInter(XML_AJAX) {
    if (XML_AJAX !== null) {
        $("body").append(XML_AJAX);
    }
}

function fu_setDatoElaboradoPorInter(cod, desc) {
    jQuery('#txtElaboradoPor').val(desc);
    jQuery('#sElaboradoPor').val(cod);
    removeDomId('windowConsultaElaboradoPor');
}

function fn_inicializaDocInterEmi(sCoAnnio){
    //countPressBtnChange=0;
    jQuery('#buscarDocumentoEmiBean').find('#esFiltroFecha').val("2");//solo año
    //jQuery("#fechaFiltro").html("Año: "+sCoAnnio);
    /*jQuery("#fechaFiltro").showDatePicker({
        showDia: false,
        selectTodosMeses:true,
        pressAceptarEvent: function(data) {
            if(data.rbOp==="0"){
                jQuery('#buscarDocumentoEmiBean').find('#sCoAnnio').val(data.anio);
                if(data.mes==="" && data.anio!==""){
                   jQuery("#fechaFiltro").html("Año: "+data.anio);
                   jQuery('#buscarDocumentoEmiBean').find('#esFiltroFecha').val("2");//solo año
                }else{
                   jQuery("#fechaFiltro").html("Año: "+data.anio+"  Mes: "+monthYearArray[data.mes * 1]);  
                   jQuery('#buscarDocumentoEmiBean').find('#esFiltroFecha').val("3");//año y mes
                }
            }else if(data.rbOp==="1"){
                jQuery('#buscarDocumentoEmiBean').find('#esFiltroFecha').val("1");//rango fecha
                jQuery("#fechaFiltro").html("Del: "+data.fIni+"  Al: "+data.fFin); 
            }
        }
    });*/
    jQuery('#buscarDocumentoEmiBean').find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 5});        
    jQuery('#buscarDocumentoEmiBean').find('#sNumDocRef').change(function() {
        $(this).val(replicate($(this).val(), 6));
    });
    jQuery('#buscarDocumentoEmiBean').find('#sNumDoc').change(function() {
        $(this).val(replicate($(this).val(), 6));
    });    
    jQuery('#buscarDocumentoEmiBean').find('#sCoAnnioBus').find('option[value=""]').remove();
    pnumFilaSelect=0;
    changeTipoBusqEmiDocuInter("0");
}


function fu_eventoTablaEmiDocInter() {
    
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
            {"bSortable": true},
            {"sType": "fecha"},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": true},
            {"bSortable": false},
            {"bSortable": false},
            {"bSortable": false},
            {"bSortable": false},
            {"bSortable": false},
            {"bSortable": false},
            {"bSortable": false}
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
                if (index === 3 || index === 7 || index === 8 || index === 9) {
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
            if (typeof($(this).children('td')[13]) !== "undefined") {
                jQuery('#txtpnuAnn').val($(this).children('td')[13].innerHTML);
                jQuery('#txtpnuEmi').val($(this).children('td')[14].innerHTML);
                jQuery('#txtpexisteDoc').val($(this).children('td')[17].innerHTML);
                jQuery('#txtpexisteAnexo').val($(this).children('td')[18].innerHTML);
                pnumFilaSelect = $(this).index();
            }
            //editarDocumentoRecep($(this).children('td')[12].innerHTML,$(this).children('td')[13].innerHTML,$(this).children('td')[15].innerHTML);
            /* comentado xq los botones de ver documento y ver anexos, ya se encuentran el la tabla
             var sData = $(this).find('td');
             passArrayToHtmlTabla("txtTextIndexSelect",sData);*/


        }
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

function changeTipoBusqEmiDocuInter2() {
    changeTipoBusqEmiDocuInter('1');
}

function changeTipoBusqEmiDocuInter(tipo) {
    jQuery('#sTipoBusqueda').val(tipo);
    submitAjaxFormEmiDocInter(tipo);
    mostrarOcultarDivBusqFiltro2();
}

function submitAjaxFormEmiDocInter(tipo) {
    var validaFiltro = fu_validaFiltroEmiDocInter(tipo);
    if (validaFiltro === "1") {
        ajaxCall("/srDocumentoEmisionInteroperabilidad.do?accion=goInicio", $('#buscarDocumentoEmiBean').serialize(), function(data) {
            refreshScript("divTablaEmiDocumenAdm", data);
        }, 'text', false, false, "POST");
    }else if(validaFiltro === "2"){//buscar referencia
        ajaxCall("/srDocumentoEmisionInteroperabilidad.do?accion=goBuscaDocumentoEnReferencia", $('#buscarDocumentoEmiBean').serialize(), function(data) {
            fu_rptaBuscaDocumentoEnReferenciaInter(data);
        }, 'text', false, false, "POST");        
    }
    return false;
}

function fu_rptaBuscaDocumentoEnReferenciaInter(data){
    if(data!==null){
        if(data==="0"){
          alert_Info("Buscar Referencia", "No Existe Documento.");
        }else{
          refreshScript("divTablaEmiDocumenAdm", data);  
        }
    }
}

function fu_validaFiltroEmiDocInter(tipo) {
    var valRetorno = "1";
    jQuery('#sFeEmiIni').val(jQuery('#sCoAnnio').parent('td').find('#fechaFiltro').attr('fini'));
    jQuery('#sFeEmiFin').val(jQuery('#sCoAnnio').parent('td').find('#fechaFiltro').attr('ffin'));    
    
    
    //var pEsIncluyeFiltro = jQuery("esIncluyeFiltro1").is(':checked');
    var pEsIncluyeFiltro = $('#buscarDocumentoEmiBean').find('#esIncluyeFiltro1').is(':checked');
    //var pEsFiltroFecha = jQuery("#esFiltroFecha").val();
    var vFechaActual = jQuery('#txtFechaActual').val();
    if(tipo==="0"){
        valRetorno = fu_validaFiltroEmiDocInterFiltrar(vFechaActual);     
    }else if(tipo==="1"){
      //verificar si se ingreso datos en los campos de busqueda de referencia
      valRetorno = fu_validarBusquedaXReferenciaInter(tipo);  
      if(valRetorno==="1"){
        valRetorno = fu_validaFiltroEmiDocInterBuscar();  
        if(valRetorno==="1"){
            if(pEsIncluyeFiltro){
               valRetorno = fu_validaFiltroEmiDocInterFiltrar(vFechaActual); 
            }else{
               valRetorno = setAnnioNoIncludeFiltroEmiInter();
            }
        }
      }
    }    
    return valRetorno;
}

function fu_validarBusquedaXReferenciaInter(tipo){
    var valRetorno="1";//no buscar por referencia
    if(tipo === "1"){
        var vBuscDestinatario = allTrim(jQuery('#sBuscDestinatario').val());
        var vDeTipoDocInter = allTrim(jQuery('#sDeTipoDocInter').val());
        var vCoAnnioBus = allTrim(jQuery('#sCoAnnioBus').val());
        var vNumDocRef = allTrim(jQuery('#sNumDocRef').val());

        if((typeof(vBuscDestinatario)!=="undefined" && vBuscDestinatario!==null && vBuscDestinatario!=="") &&
           (typeof(vDeTipoDocInter)!=="undefined" && vDeTipoDocInter!==null && vDeTipoDocInter!=="") &&
           (typeof(vCoAnnioBus)!=="undefined" && vCoAnnioBus!==null && vCoAnnioBus!=="") &&
           (typeof(vNumDocRef)!=="undefined" && vNumDocRef!==null && vNumDocRef!=="")){
           valRetorno = "2";//buscar por referencia
        }
        
    if(valRetorno==="2"){
        if (vNumDocRef !== "" && vNumDocRef !== null) {
            var vValidaNumero = fu_validaNumero(vNumDocRef);
            if (vValidaNumero !== "OK") {
               bootbox.alert("N° de Documento en referencia debe ser solo numeros.");
                valRetorno = "0";
            }
        }
    }        
    }
    return valRetorno;
}

function fu_validaFiltroEmiDocInterFiltrar(vFechaActual){
    var valRetorno = "1";
    if (valRetorno==="1") {
        fu_obtenerEsFiltroFecha('buscarDocumentoEmiBean');
        var pEsFiltroFecha = jQuery('#buscarDocumentoEmiBean').find("#esFiltroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
        if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
           if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
                var pAnnio = jQuery('#buscarDocumentoEmiBean').find('#sCoAnnio').val();
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
                        jQuery('#buscarDocumentoEmiBean').find('#sCoAnnio').val(pAnnioBusq);                          
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

function fu_validaFiltroEmiDocInterBuscar() {
    var valRetorno = "1";
    
    upperCaseBuscarDocumentoEmiBeanInter();
    
    var vNroEmision = jQuery('#sNumCorEmision').val();
    var vNroDocumento = jQuery('#sNumDoc').val();
    var vNroExpediente = jQuery('#sBuscNroExpediente').val();
    var vAsunto = jQuery('#sDeAsuM').val();
    
    if((typeof(vNroEmision)==="undefined" || vNroEmision===null || vNroEmision==="") &&
       (typeof(vNroDocumento)==="undefined" || vNroDocumento===null || vNroDocumento==="") &&
       (typeof(vNroExpediente)==="undefined" || vNroExpediente===null || vNroExpediente==="") &&
       (typeof(vAsunto)==="undefined" || vAsunto===null || vAsunto==="")){
       alert_Warning("Buscar: ","Ingresar Algún parámetro de Búsqueda");
       valRetorno = "0";
    }
    
    if(valRetorno==="1"){
        if (vNroEmision !== "" && vNroEmision !== null) {
            var vValidaNumero = fu_validaNumero(vNroEmision);
            if (vValidaNumero !== "OK") {
                //alert("N° de Emisión debe ser solo numeros.");
                alert_Warning("Buscar: ","N° de Emisión debe ser solo numeros.");
                valRetorno = "0";
            }
        }
    }
    return valRetorno;
}

function fu_cleanEmiDocInter(tipo) {
    if (tipo==="1") {
        jQuery("#sNumDoc").val("");
        jQuery("#sNumDocRef").val("");
        jQuery("#sBuscNroExpediente").val("");
        jQuery("#sDeAsuM").val("");
        jQuery("#sDeTipoDocInter option[value=]").prop("selected", "selected");
//        jQuery("#sBuscEstado").val("");
        jQuery("#sFeEmiIni").val("");
        jQuery("#sFeEmiFin").val("");
//        jQuery("#sDeEmiReferencia").val("");
        jQuery("#txtDepEmiteBus").val(" [TODOS]");
        jQuery("#sBuscDestinatario").val("");
//        jQuery("#sBuscElaboraradoPor").val("");
        jQuery("#sNumCorEmision").val("");
        jQuery("#esIncluyeFiltro1").prop('checked',false);
        jQuery("#esIncluyeFiltro1").attr('checked',false);
        jQuery("#sCoAnnioBus").find('option:first').prop("selected", "selected");
    } else if(tipo==="0"){
        jQuery("#sEstadoDoc option[value=5]").prop("selected", "selected");
        jQuery("#sPrioridadDoc option[value=]").prop("selected", "selected");
        jQuery("#coTema option[value=]").prop("selected", "selected");
        jQuery("#sTipoDoc option[value=]").prop("selected", "selected");
        jQuery("#sRefOrigen").val("");
        jQuery("#txtRefOrigen").val("[TODOS]");
        jQuery("#sDestinatario").val("");
        jQuery("#txtDestinatario").val("[TODOS]");
        jQuery("#sElaboradoPor").val("");
        jQuery("#txtElaboradoPor").val("[TODOS]");
        jQuery("#sExpediente option[value=]").prop("selected", "selected");
        jQuery("#esFiltroFecha").val("2");//solo año
        //jQuery("#fechaFiltro").html("Año: "+jQuery("#txtAnnioActual").val());
        jQuery("#sCoAnnio").val(jQuery("#txtAnnioActual").val());
        jQuery("#fechaFiltro").showDatePicker({defaultOpcionSelected: 5});
    }
}

function fu_goNuevoEmisionDocInter() {
    var validaFiltro = "";
//var tipo = dojo.byId("sTipoControl").value;
//jQuery('#sTipoBusqueda').val("1");
    validaFiltro = "1";/*fu_validaFiltroEmiDocInter(tipo);*/
    if (validaFiltro === "1") {
        ajaxCall("/srDocumentoEmisionInteroperabilidad.do?accion=goNuevoDocumentoEmi", '', function(data) {
            jQuery('#divEmiDocumentoAdmin').hide();
            jQuery('#divNewEmiDocumAdmin').show();
            refreshScript("divNewEmiDocumAdmin", data);
            alert_Danger("","dasdsadasd")
            fn_cargaToolBarEmiInter();
        }, 'text', false, false, "POST");
    }
    return false;
}

function fn_grabarDocumentoEmiAdmInter() {
    var validaFiltro = fu_verificarCamposDocEmiAdm('0', '');
    if (validaFiltro === "1") {
        var pesDocEmi = jQuery('#documentoEmiBean').find('#esDocEmi').val();
        var rpta = fu_verificarDestinatarioInter("1");
        var nrpta = rpta.substr(0, 1);
        var vResult = "0";
        if (pesDocEmi === "7") {
            if (nrpta === "1") {
                vResult = "1";
            }
        } else {
            if (nrpta === "1" || nrpta === "E") {
                vResult = "1";
            }
        }
        if (vResult === "1") {
            rpta = fu_verificarReferencia();
            nrpta = rpta.substr(0, 1);            
            if(nrpta === "1"){
                rpta = fu_verificarEmpVoBo();//add vobo
                nrpta = rpta.substr(0, 1);            
                if(nrpta === "1"){
                    //verificar si necesita grabar el documento.
                    rpta = fu_verificarChangeDocumentoEmiAdm();
                    nrpta = rpta.substr(0, 1);
                    if (nrpta === "1") {
                        fn_goGrabarDocumentoEmiAdmInter();//grabar Documento
                    } else {
                        alert_Info("Emisión :", rpta);
                    }                    
                }else{
                    alert_Info("Emisión :", rpta);
                }
            }else{
                alert_Info("Emisión :", rpta);
            }
        } else {
            alert_Info("Emisión :", rpta);
        }
    }
    return false;
}

function fu_verificarDestinatarioInter(pverificarRef) {
    var vResult = " destinatario";
    var vtabla = ["tblDestEmiDocAdmInter= destinatario InterInstitucion"];
    //var ltabla = vtabla.length;
    /*if (pverificarRef === "1") {
        vtabla.push("tblRefEmiDocAdm= Referencia");
    }*/
    var countEmpty = 0;
    for (var i = 0; i < vtabla.length; i++) {
        var vauxTabla = vtabla[i].split("=");
        var rpta = fn_verificarExisteDestinatario(vauxTabla[0]);
        var nrpta = rpta.substr(0, 1);
        if (nrpta === "0") {
            vResult = rpta.substr(1) + vauxTabla[1];
            break;
        } else if (nrpta === "2" /*&& vauxTabla[0]!=="tblRefEmiDocAdm"*/) {
            countEmpty++;
            //if (countEmpty === ltabla) {
            if (countEmpty === vtabla.length) {
                vResult = rpta.substr(1) + vResult;
                break;
            }
        } else {
            vResult = rpta.substr(0, 1);
        }
    }
    return vResult;
}

function fn_tblDestEmiDocInterToJson() {
    var json = '[';
    var itArr = [];
    var tbl1 = fn_tblDestInterEmiDocAdmToJson();
    tbl1 !== "" ? itArr.push(tbl1) : "";
    json += itArr.join(",") + ']';
    return json;
}
function fn_tblDestInterEmiDocAdmToJson() {
  
    var arrColMostrar = new Array();
    arrColMostrar[0] = "accionBD=8";
    arrColMostrar[1] = "cidCat=1";
    arrColMostrar[2] = "nuRuc=2";
    arrColMostrar[3] = "deDepDes=3";
    arrColMostrar[4] = "deNomDes=4";
    arrColMostrar[5] = "deCarDest=5";
    arrColMostrar[6] = "nuFol=6";
    arrColMostrar[7] = "coTipoDestino=10";
    arrColMostrar[8] = "nuDes=12";
    return fn_tblDestEmihtml2Interjson('tblDestEmiDocAdmInter', 1, arrColMostrar, 11, "1", 8, "BD");
}

function fn_tblDestEmihtml2Interjson(idTable, iniFila, colMostrar, colEstadoActivo, estadoActivo, colAccionBD, accionBD) {
    //var json = '[';
    var otArr = [];
    var count = 0;      
    $('#' + idTable + ' tr').each(function(i) {
        if (count >= iniFila) {
            var x = $(this).children();
            var itArr = [];
            
            if ($(this).find("td").eq(colEstadoActivo - 1).text() === estadoActivo && $(this).find("td").eq(colAccionBD - 1).text() !== accionBD) {
                x.each(function(index) {                   
                    for (var i = 0; i < colMostrar.length; i++) {
                        var auxArrColMostrar = colMostrar[i].split("=");
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
                            } else if (typeof($(this).find('input[type=checkbox]').val()) !== "undefined") {
                                valCampoBean = $(this).find('input[type=checkbox]').is(':checked')?'1':'0';
                            } else {
                                valCampoBean = $(this).text();
                            }
                            if (typeof($(this).find('span').html()) !== "undefined") {
                                valCampoBean = $(this).find('span').html();
                            }
                            if (typeof($(this).find('input[id=txtCodigoUbigeoOtro]').val()) !== "undefined") {
                                valCampoBean = $(this).find('input[id=txtCodigoUbigeoOtro]').val();                                  
                            }
                            if (typeof($(this).find('input[id=idCategoria]').val()) !== "undefined") {
                                valCampoBean = $(this).find('input[id=idCategoria]').val();                                  
                            }
                            if (typeof($(this).find('input[id=idEntidad]').val()) !== "undefined") {
                                valCampoBean = $(this).find('input[id=idEntidad]').val();                                  
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


function fn_buildSendJsontoServerDocuEmiInter() {
    var result = "{";
    result = result + '"nuAnn":"' + $("#nuAnn").val() + '",';
    result = result + '"nuEmi":"' + $("#nuEmi").val() + '",';
    //result = result + '"documentoEmiBean":' + JSON.stringify($('#documentoEmiBean').serializeFormJSON()) + ',';
    var valEnvio = jQuery('#envDocumentoEmiBean').val();
    if (valEnvio === "1") {
        result = result + '"documentoEmiBean":' + JSON.stringify(getJsonFormDocumentoEmiBean()) + ',';
    }
    valEnvio = jQuery('#envExpedienteEmiBean').val();
    if (valEnvio === "1") {
        result = result + '"expedienteEmiBean":' + JSON.stringify(getJsonFormExpedienteEmiBean()) + ',';
    }
    valEnvio = jQuery('#envRemitenteEmiBean').val();
    if (valEnvio === "1") {
        result = result + '"remitenteEmiBean":' + JSON.stringify(getJsonFormRemitenteEmiBean()) + ',';
    }
    result = result + '"lstReferencia":' + fn_tblRefEmiDocAdmToJson() + ',';
    result = result + '"lstDestinatario":' + sortDelFirst(fn_tblDestEmiDocInterToJson()) + ',';
    result = result + '"lstEmpVoBo":' + sortDelFirst(fn_tblEmpVoBoDocAdmToJson());//add vobo
    return result + "}";
}

function fn_goGrabarDocumentoEmiAdmInter() {
    $('#documentoEmiBean').find('select').removeProp('disabled');
    var cadenaJson = fn_buildSendJsontoServerDocuEmiInter();
    //console.log(cadenaJson);
    var pcrearExpediente = "0";
    var pesnuevoDocEmiAdm = jQuery("#txtEsNuevoDocAdm").val();
    if (pesnuevoDocEmiAdm === "1" && jQuery("#nuAnnExp").val() === "" && jQuery("#nuSecExp").val() === "") {
        /*bootbox.dialog({
            message: " <h5>¿ Desea crear expediente para este documento ?</h5>",
            buttons: {
                SI: {
                    label: "SI",
                    className: "btn-primary",
                    callback: function() {
                        pcrearExpediente = "1";
                        fn_submitGrabarDocumentoEmiAdm(cadenaJson,pcrearExpediente); 
                    }                        
                },
                NO: {
                    label: "NO",
                    className: "btn-default",
                    callback: function() {
                        fn_submitGrabarDocumentoEmiAdm(cadenaJson,pcrearExpediente); 
                    }                      
                }
            }
        });   */
        var inCreaExpediente = jQuery("#inCreaExpediente").val();
        if(inCreaExpediente==='SI'){
            pcrearExpediente = "1";
            fn_submitGrabarDocumentoEmiAdmInter(cadenaJson,pcrearExpediente); 
    }else{
            pcrearExpediente = "0";
       fn_submitGrabarDocumentoEmiAdmInter(cadenaJson,pcrearExpediente); 
    }
    }else{
       fn_submitGrabarDocumentoEmiAdmInter(cadenaJson,pcrearExpediente); 
}
}

function fn_submitGrabarDocumentoEmiAdmInter(cadenaJson,pcrearExpediente){
    jQuery('#documentoEmiBean').find("#nuSecuenciaFirma").val("");
    ajaxCallSendJson("/srDocumentoEmisionInteroperabilidad.do?accion=goGrabaDocumentoEmi&pcrearExpediente=" + pcrearExpediente, cadenaJson, function(data) {
        fn_rptaGrabaDocumEmiAdmin(data, pcrearExpediente);
    },
    'json', false, false, "POST");    
}

function fn_changeToProyectoDocEmiAdmInter() {
    ajaxCall("/srDocumentoEmisionInteroperabilidad.do?accion=goChangeToProyecto", $('#documentoEmiBean').serialize(), function(data) {
        fn_rptaChangeToProyectoDocEmiAdm(data);
    },
            'json', false, false, "POST");
}

function editarDocumentoEmiClickInter(pnuAnn,pnuEmi,pexisteDoc,pexisteAnexo) {
    if (pnuAnn !== "" && pnuEmi !== "") {
        var p = new Array();
        p[0] = "pnuAnn=" + pnuAnn;
        p[1] = "pnuEmi=" + pnuEmi;
        p[2] = "pexisteDoc=" + pexisteDoc;
        p[3] = "pexisteAnexo=" + pexisteAnexo;
        ajaxCall("/srDocumentoEmisionInteroperabilidad.do?accion=goEditDocumentoEmi", p.join("&"), function(data) {
            refreshScript("divNewEmiDocumAdmin", data);
            jQuery('#divEmiDocumentoAdmin').hide();
            jQuery('#divNewEmiDocumAdmin').show();
            jQuery('#divTablaEmiDocumenAdm').html("");
            fn_cargaToolBarEmiIner();
        }, 'text', false, false, "POST");
    } else {
       bootbox.alert("Seleccione una fila de la lista");
    }
}

function editarDocumentoEmiInter() {
    var pnuAnn = jQuery('#txtpnuAnn').val();
    if (pnuAnn !== "") {
        var p = new Array();
        p[0] = "pnuAnn=" + pnuAnn;
        p[1] = "pnuEmi=" + jQuery('#txtpnuEmi').val();
        p[2] = "pnuDes=" + jQuery('#txtpnuDes').val();
        p[3] = "pexisteDoc=" + jQuery('#txtpexisteDoc').val();
        p[4] = "pexisteAnexo=" + jQuery('#txtpexisteAnexo').val();
        ajaxCall("/srDocumentoEmisionInteroperabilidad.do?accion=goEditDocumentoEmi", p.join("&"), function(data) {
            refreshScript("divNewEmiDocumAdmin", data);
            jQuery('#divEmiDocumentoAdmin').hide();
            jQuery('#divNewEmiDocumAdmin').show();
            jQuery('#divTablaEmiDocumenAdm').html("");
            fn_cargaToolBarEmiInter();
        }, 'text', false, false, "POST");
    } else {
        alert_Info("Emisión :", "Seleccione una fila de la lista");
    }
}

//function changeEstadoDocEmiAdm(component){
//   if($("#"+component).hasClass('open')){
//     $("#"+component).removeClass('open');     
//   }else{
//     $("#"+component).addClass('open');     
//   }
//}

function fn_eventEstDocInter(sTipoDestEmi, sEstadoDocInter) {
//   $("#ullsEstDocEmiAdm").hover(
//       function(){
//        return;
//       },
//       function(){
//        changeEstadoDocEmiAdm('estDocEmiAdm');
//       }
//   );
    $("[bt='true']").showBigTextBox({
        pressAceptarEvent: function(data) {
            if (data.hasChangedText) {
                changeAccionBDDocEmi(data.currentObject);
            }
        },maxNumCar:600
    });
    $("#txtIndicaciones").showBigTextBox({maxNumCar:600});
    jQuery('#documentoEmiBean').find('#nuDiaAte').change(function() {
        fu_changeDocumentoEmiBean();
    });
    jQuery('#documentoEmiBean').find('#deDocSig').change(function() {
        fu_changeDocumentoEmiBean();
    });
    jQuery('#documentoEmiBean').find('#coDepEmi').change(function() {
        fu_changeRemitenteEmiBean();
    });
    jQuery('#documentoEmiBean').find('#coLocEmi').change(function() {
        fu_changeRemitenteEmiBean();
    });
//    jQuery('#documentoEmiBean').find('#coTipDocInter').change(function() {
//        fu_changeDocumentoEmiBean();
//    });
    jQuery('#documentoEmiBean').find('#deAsu').change(function() {
        fu_changeDocumentoEmiBean();
    });
    jQuery('#documentoEmiBean').find('#nuDocEmi').change(function() {
	var noForm='#documentoEmiBean';
        var pnuAnn = jQuery(noForm).find('#nuAnn').val();
        var pnuEmi = jQuery(noForm).find('#nuEmi').val();
        var ptiEmi = jQuery(noForm).find('#tiEmi').val();
        var pcoTipDocInter = jQuery(noForm).find('#coTipDocInter').val();
        var pcoDepEmi = jQuery(noForm).find('#coDepEmi').val();
        var pnuDocEmiAnn = jQuery(noForm).find('#txtnuDocEmiAn').val();
        var pnuDocEmi = allTrim(jQuery(noForm).find('#nuDocEmi').val());
        if (!!pnuDocEmi&&!!pnuAnn&&!!pnuEmi){
            var vValidaNumero = fu_validaNumero(pnuDocEmi);
            if (vValidaNumero==="OK") {
                pnuDocEmi = replicate(pnuDocEmi, 6);
                if (pnuDocEmiAnn !== pnuDocEmi || (typeof(pnuDocEmiAnn) !== "undefined" && pnuDocEmiAnn === "")) {
                    fn_jsonVerificarNumeracionDocEmi(pnuDocEmiAnn, pnuAnn, pnuEmi, ptiEmi, pcoTipDocInter, pcoDepEmi, pnuDocEmi);
                } else {
                    jQuery(noForm).find('#nuDocEmi').val(pnuDocEmiAnn);
                    jQuery(noForm).find('#nuCorDoc').val("1");
                }
            }else{
                bootbox.alert("<h5>Nro. Documento debe ser solo números.</h5>", function() {
                    bootbox.hideAll();
                    jQuery(noForm).find('#nuDocEmi').focus();        
                });   
            }            
        }
        fu_changeDocumentoEmiBean();
    });
    if (jQuery("#txtEsNuevoDocInter").val() === '1') {
        jQuery("#coTipDocInter").focus();
    }
    fn_changeTipoDestinatarioDocuInter(sTipoDestEmi, sEstadoDocInter);
    fu_cargaEdicionDocAdm("00", sEstadoDocInter);
//    $('#containerTblVoBo').resizable({
//        handles: 's',
//        minHeight: 100,
//        maxHeight: 200
//    });
}

function fn_eventDestInterEmiDoc() {
    fn_eventTblSeleccionGuardaIndex("tblDestEmiDocAdmInter", "txtIndexFilaDestInterEmiDoc");
}

function fn_changeTipoDestinatarioDocuEmiInter(sTipoDestEmi){
    fn_changeTipoDestinatarioDocuInter(sTipoDestEmi,jQuery('#documentoEmiBean').find('#esDocEmi').val());
}

function fn_changeTipoDestinatarioDocuInter(sTipoDestEmi, sEstadoDocAdm) {

    $("#divtablaDestEmiDocAdmInter").show();
    
}


//function fn_removeReferenciaEmi(index){
//    if(index !== -1){
//        $("#tblRefEmiDocInter tbody tr:eq("+index+")").remove();
//    }    
//}

function fn_searchDocReferentblInter(cell) {
    var p = new Array();
    p[0] = "accion=goBuscaDocumentotblReferencia";
    (($(cell).parent()).parent()).children().each(function(index) {
        switch (index)
        {
            case 0:
                p[1] = "pannio=" + $(this).find('select').val();
                break;
            case 1:
                p[2] = "ptiDoc=" + $(this).find('select').val();
                break;
            case 2:
                p[3] = "ptiBusqueda=" + $(this).find('input[type=radio]:checked').val();
                break;
            case 3:
                p[4] = "pnuDoc=" + $(this).find('input[type=text]').val();
                break;
            default:
                break;
        }
    });
    ajaxCall("/srDocumentoEmisionInteroperabilidad.do", p.join("&"), function(data) {
        fn_rptaBuscaDocumentoReferencia(data);
        jQuery('#txtTblRefEmiFilaWhereButton').val((($(cell).parent()).parent()).index());
        jQuery('#txtTblRefEmiColWhereButton').val(($(cell).parent()).index());
    },
            'text', false, false, "POST");
}



function fn_buscaDependenciaDestEmitblInter(cell) {
    var p = new Array();
    p[0] = "accion=goBuscaDependenciaDestinatario";
    p[1] = "pdeDepen=" + fu_getValorUpperCase(($(cell).parent()).find('input[type=text]').val());
    ajaxCall("/srDocumentoEmisionInteroperabilidad.do", p.join("&"), function(data) {
        fn_rptaBuscaDependenciaDestinatarioInter(data);
        jQuery('#txtTblDestEmiFilaWhereButton').val((($(cell).parent()).parent()).index());
        jQuery('#txtTblDestEmiColWhereButton').val(($(cell).parent()).index());
    },
            'text', false, false, "POST");
}

function fn_changeReferenciaCorrectaInter(pnroFila) {
    var ultimDiv = $("#tblRefEmiDocInter tbody tr:eq(" + pnroFila + ") td div.ui-state-error-text").last();
    ultimDiv.removeClass('ui-state-error-text');
    var ultimDivSpan = ultimDiv.find('span');
    ultimDivSpan.removeClass('ui-icon-alert');
    ultimDivSpan.addClass('ui-icon-check');
    ultimDivSpan.attr("title", "OK");
    ultimDiv.addClass('ui-state-highlight-text');
    $("#tblRefEmiDocInter tbody tr:eq(" + pnroFila + ") td").last().html("1");
}

function fn_changeReferenciaIncorrecta(pnroFila) {
    var ultimDiv = $("#tblRefEmiDocInter tbody tr:eq(" + pnroFila + ") td div.ui-state-highlight-text").last();
    ultimDiv.removeClass('ui-state-highlight-text');
    var ultimDivSpan = ultimDiv.find('span');
    ultimDivSpan.removeClass('ui-icon-check');
    ultimDivSpan.addClass('ui-icon-alert');
    ultimDivSpan.attr("title", "ERROR");
    ultimDiv.addClass('ui-state-error-text');
    $("#tblRefEmiDocInter tbody tr:eq(" + pnroFila + ") td").last().html("0");
}

function fn_addDestintarioEmiInter() {
    fn_addDestintarioEmiDoc('tblDestEmiDocAdmInter');
}
function fn_removeDestintarioEmiInter() {
    fn_removeDestintarioEmiDoc("7", "tblDestEmiDocAdmInter", 'txtIndexFilaDestInterEmiDoc');
}

function fn_buscaCategoriaDestEmitblInter(cell) {
    var p = new Array();
    p[0] = "accion=goBusquedaCategoriaDestinatario";  
    ajaxCall("/srDocumentoEmisionInteroperabilidad.do", p.join("&"), function(data) {
        fn_rptaBuscaDependenciaDestinatario(data);
        $('#txtTblDestEmiFilaWhereButton').val((($(cell).parent()).parent()).index());
        $('#txtTblDestEmiColWhereButton').val(($(cell).parent()).index());      
    }, 'text', false, false, "POST");
    return false;
}

function fn_buscaEntidadDestEmitblInter(cell) {
    /*var p = new Array();
    var pfila = jQuery('#txtTblDestEmiFilaWhereButton').val();
    var pcol = jQuery('#txtTblDestEmiColWhereButton').val();
    var dato=jQuery("#tblDestEmiDocAdmInter tbody tr:eq(" + pfila + ") td:eq(" + pcol + ")").find('input[id=idCategoria]').val();
    
    p[0] = "accion=goBuscaEntidadDestinatario";
    p[1] = "idCategoria=" + dato;
        
    ajaxCall("/srDocumentoAdmEmision.do", p.join("&"), function(data) {
        fn_rptaBuscaDependenciaDestinatario(data);
        jQuery('#txtTblDestEmiFilaWhereButton').val((($(cell).parent()).parent()).index());
        jQuery('#txtTblDestEmiColWhereButton').val(($(cell).parent()).index());
    },
            'text', false, false, "POST");*/
    jQuery('#txtTblRefEmiFilaWhereButton').val((($(cell).parent()).parent()).index());
    jQuery('#txtTblRefEmiColWhereButton').val(($(cell).parent()).index());
    var p = new Array();
    

    var pfila = jQuery('#txtTblDestEmiFilaWhereButton').val();
    //var pcol = jQuery('#txtTblDestEmiColWhereButton').val();
    var dato=jQuery("#tblDestEmiDocAdmInter tbody tr:eq(" + pfila + ") td:eq(0)").find('input[id=idCategoria]').val();
    
    p[0] = "accion=goBusquedaEntidadDestinatario";  
    p[1] = "idCategoria=" + dato;
 
    if (dato.trim().length>0)
    {
        ajaxCall("/srDocumentoEmisionInteroperabilidad.do", p.join("&"), function(data) {
        fn_rptaBuscaDependenciaDestinatario(data);
        $('#txtTblDestEmiFilaWhereButton').val((($(cell).parent()).parent()).index());
        $('#txtTblDestEmiColWhereButton').val(($(cell).parent()).index());      
        }, 'text', false, false, "POST"); 
    }

    
    return false;
}

function fu_setDatoEntiDestEmi(cod, desc) {
    destinatarioDuplicado = true;
    var pfila = jQuery('#txtTblDestEmiFilaWhereButton').val();
    var pcol = jQuery('#txtTblDestEmiColWhereButton').val();
    var idTabla = "tblDestEmiDocAdmInter";

    $("#"+idTabla+" tbody tr:eq(" + pfila + ") td:eq(" + pcol + ")").find('input[id=txtEntidad]').val(desc);
    $("#"+idTabla+" tbody tr:eq(" + pfila + ") td:eq(" + pcol + ")").find('input[id=idEntidad]').val(cod);
    fu_validaInter();
    removeDomId('windowConsultaEntiDestEmi');
 }   
function fu_setDatoCateDestEmi(cod, desc) {
    destinatarioDuplicado = true;
    var pfila = jQuery('#txtTblDestEmiFilaWhereButton').val();
    var pcol = jQuery('#txtTblDestEmiColWhereButton').val();
    var idTabla = "tblDestEmiDocAdmInter";

    $("#"+idTabla+" tbody tr:eq(" + pfila + ") td:eq(" + pcol + ")").find('input[id=txtCategoria]').val(desc);
    $("#"+idTabla+" tbody tr:eq(" + pfila + ") td:eq(" + pcol + ")").find('input[id=idCategoria]').val(cod);
    $("#"+idTabla+" tbody tr:eq(" + pfila + ") td:eq(1)").find('input[id=txtEntidad]').val("");
    $("#"+idTabla+" tbody tr:eq(" + pfila + ") td:eq(1)").find('input[id=idEntidad]').val("");
    fu_validaInter();
    removeDomId('windowConsultaCateDestEmi');
//    var pcoTipoDoc = jQuery('#documentoEmiBean').find('#coTipDocAdm').length === 0 ? 
//                            jQuery('#documentoPersonalEmiBean').find('#coTipDocAdm').length === 0 ? 
//                                "": 
//                                jQuery('#documentoPersonalEmiBean').find('#coTipDocAdm').val() :
//                            jQuery('#documentoEmiBean').find('#coTipDocAdm').val();
//    var p = new Array();
//    p[0] = "accion=goBuscaEmpleadoLocaltblDestinatario";
//    p[1] = "pcoDepen=" + cod;
//    p[2] = "pcoTipoDoc=" + pcoTipoDoc;
//    ajaxCall("/srDocumentoAdmEmision.do", p.join("&"), function(data) {
//        if (data !== null && allTrim(data.coRespuesta) === "1") {
//            var arrCampo = new Array();
//            arrCampo[0] = (pcol * 1 + 1) + "=" + cod;//codDependencia
//            arrCampo[1] = "5=" + data.coEmpleado;//codEmpleado
//            var bResult = fn_validaDestinatarioIntituDuplicado(idTabla, arrCampo, true, pfila*1);
//            var esPrimero = $("#" + idTabla + " tbody tr").siblings().not('.oculto').length;
//            var bDestinOk=true;
//            p = new Array();
//            $("#" + idTabla + " tbody tr:eq(" + pfila + ") td").each(function(index) {
//                switch (index)
//                {
//                    case 2:
//                        $(this).find('input[type=text]').val(data.deLocal);
//                        p[0] = allTrim(desc);
//                        p[1] = allTrim(data.deLocal);
//                        break;
//                    case 3:
//                        $(this).text(data.coLocal);
//                        break;
//                    case 4:
//                        var deEmp=data.deEmpleado;
//                        if(deEmp!==null && deEmp!=="null" && allTrim(deEmp)!==""){
//                            $(this).find('input[type=text]').val(deEmp);
//                            p[2] = allTrim(deEmp);
//                        }else{
//                            bDestinOk=false;
//                        }
////                        $(this).find('input[type=text]').val(data.deEmpleado);
////                        p[2] = allTrim(data.deEmpleado);
//                        break;
//                    case 5:
//                        var coEmp=data.coEmpleado;
//                        if(coEmp!==null && coEmp!=="null" && allTrim(coEmp)!==""){
//                            $(this).text(coEmp);
//                        }
//                        else{
//                            bDestinOk=false;
//                        }                          
////                        $(this).text(data.coEmpleado);
//                        break;
//                    case 6:
//                        if (esPrimero === 1) {
//                            $(this).find('input[type=text]').val(data.deTramiteFirst);
//                            p[3] = allTrim(data.deTramiteFirst);
//                        } else {
//                            $(this).find('input[type=text]').val(data.deTramiteNext);
//                            p[3] = allTrim(data.deTramiteNext);
//                        }
//                        break;
//                    case 7:
//                        if (esPrimero === 1) {
//                            $(this).text(data.coTramiteFirst);
//                        } else {
//                            $(this).text(data.coTramiteNext);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            });
//            $("#" + idTabla + " tbody tr:eq(" + pfila + ") td:eq(12)").text(p.join("$#"));
//            if ($("#" + idTabla + " tbody tr:eq(" + pfila + ") td:eq(11)").text() === "BD") {
//                $("#" + idTabla + " tbody tr:eq(" + pfila + ") td:eq(11)").text("UPD");
//            }
//            if (bResult) {
//                destinatarioDuplicado=false;
//                if(bDestinOk){
//                    fn_changeDestinatarioCorrecto(pfila);
////                    jQuery("#"+idTabla).parents(':eq(4)').find('td').first().find('button').first().focus();
////                }else{
////                    $("#"+idTabla+" tbody tr:eq("+pfila+") td:visible").first().find('input[type=text]').first().focus();
//                }
////            }else{
////                $("#"+idTabla+" tbody tr:eq("+pfila+") td:visible").first().find('input[type=text]').first().focus();
//            }
////                $("#"+idTabla+" tbody tr:eq("+pfila+") td:eq("+pcol+")").find('input[type=text]').val(desc);
////                $("#"+idTabla+" tbody tr:eq("+pfila+") td:eq("+(pcol * 1 + 1)+")").text(cod);                     
//        } else {
//           bootbox.alert(data.deRespuesta);
//        }
//    },
//            'json', false, false, "POST");
}
function fu_validaInter(cell)
{
    jQuery('#txtTblRefEmiFilaWhereButton').val((($(cell).parent()).parent()).index());
    jQuery('#txtTblRefEmiColWhereButton').val(($(cell).parent()).index());
        
    var pfila = jQuery('#txtTblDestEmiFilaWhereButton').val();
    var idCategoria=jQuery("#tblDestEmiDocAdmInter tbody tr:eq(" + pfila + ") td:eq(0)").find('input[id=idCategoria]').val();
    var idEntidad=jQuery("#tblDestEmiDocAdmInter tbody tr:eq(" + pfila + ") td:eq(1)").find('input[id=idEntidad]').val();
    var Entidad=jQuery("#tblDestEmiDocAdmInter tbody tr:eq(" + pfila + ") td:eq(1)").find('input[id=txtEntidad]').val();
    
    var vDependencia=jQuery("#tblDestEmiDocAdmInter tbody tr:eq(" + pfila + ") td:eq(2)").find('input[id=txtDependencia]').val();
    var vDestinatario=jQuery("#tblDestEmiDocAdmInter tbody tr:eq(" + pfila + ") td:eq(3)").find('input[id=txtDestinatario]').val();
    var vCargo=jQuery("#tblDestEmiDocAdmInter tbody tr:eq(" + pfila + ") td:eq(4)").find('input[id=txtCargo]').val();
    var vFolio=jQuery("#tblDestEmiDocAdmInter tbody tr:eq(" + pfila + ") td:eq(5)").find('input[id=txtFolio]').val();
    
    if(!!vFolio){
        var vValidaNumero = fu_validaNumero(vFolio);
        if (vValidaNumero !== "OK") {
          // bootbox.alert("Año debe ser solo números.");
            vFolio = "0";
        }                
    } 

    if (idCategoria.trim().length>0 && idEntidad.trim().length>0 && vDependencia.trim().length>0 && vDestinatario.trim().length>0 && vCargo.trim().length>0 && vFolio>0 )
    { 
        var p = new Array();
        p[0] = "accion=goAgregarProveedorEmi";
        p[1] = "ruc="+idEntidad ;
        p[2] = "descripcion="+Entidad ;
        
        ajaxCall("/srDocumentoEmisionInteroperabilidad.do", p.join("&"), function(data) {
            
            if (data !== null && allTrim(data.coRespuesta) === "1"){//data.equals("Datos guardados.")) {
              fn_changeDestinatarioCorrectoOtro("tblDestEmiDocAdmInter",pfila);  
            }
         
        },'json', false, false, "POST");
    } else
    {
        fn_changeDestinatarioIncorrectoOtro("tblDestEmiDocAdmInter",pfila);
    }
   
}