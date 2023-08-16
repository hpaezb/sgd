function fn_inicializaReporteEmiDocAcervoDocPersonal(){
    var noForm='#buscarConsulDocPersEmiBean';
    jQuery(noForm).find('#esFiltroFecha').val("3");//annio mes
    jQuery(noForm).find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 3});    
    jQuery(noForm).find('#nuDoc').change(function() {
        $(this).val(replicate($(this).val(), 6));
    });
    pnumFilaSelect=0;
    changeTipoBusqEmiDocPersReporteAcervo("0",noForm);      
}
        
function changeTipoBusqEmiDocPersReporteAcervo(tipo,noForm) {
    jQuery(noForm).find('#tipoBusqueda').val(tipo);
    submitAjaxFormDocEmiPersReporteAcervo(tipo,noForm);
    mostrarOcultarDivBusqFiltro2();
}

function fn_preChangeTipoBusqEmiDocPersReporteAcervo(tipo){
    var noForm='#buscarConsulDocPersEmiBean';
    changeTipoBusqEmiDocPersReporteAcervo(tipo,noForm);
}

function submitAjaxFormDocEmiPersReporteAcervo(tipo,noForm) {
    var validaFiltro = fu_validarBusqFormEmiDocPersonalConsul(tipo,noForm);
    if (validaFiltro === "1") {
        ajaxCall("/srReporteAcervoDocPersonal.do?accion=goInicio", $(noForm).serialize(), function(data) {
            refreshScript("divTblConsulDocumentoEmitido", data);
        }, 'text', false, false, "POST");
    }
    return false;
}

function fn_buscaFirmadoPorAcervoDoc() {
    //var codDependencia = jQuery('#buscarDocumentoEmiConsulBean').find('#coDependencia').val();
    var coDepOrigen = jQuery('#buscarConsulDocPersEmiBean').find('#coDepEmite').val();
    ajaxCall("/srReporteAcervoDocPersonal.do?accion=goBuscaFirmadoPorEditAcervo&pcoDep=" + coDepOrigen, '', function(data) {
        fn_rptaBuscaFirmadoPorAcervo(data);
    },
            'text', false, false, "POST");
}

function fn_rptaBuscaFirmadoPorAcervo(XML_AJAX) {
    if (XML_AJAX !== null) {
        $("body").append(XML_AJAX);
    }
}

function fu_generarReporteAcervoDocPerXLS(){
    fu_generarReporteAcervoDocPersonal('XLS');
}

function fu_generarReporteAcervoDocPerPDF(){
    fu_generarReporteAcervoDocPersonal('PDF')
}

function fu_generarReporteAcervoDocPersonal(pformatoReporte){
    var noForm='#buscarConsulDocPersEmiBean';
    var validaFiltro = fu_validarBusqFormEmiDocPersonalConsul("0",noForm);
    if (validaFiltro === "1") {
        ajaxCall("/srReporteAcervoDocPersonal.do?accion=goExportarReporteAcervoDocPers&pformatRepor="+pformatoReporte, $(noForm).serialize(), function(data) {
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


function fn_rptaBuscaRemitenteFiltroAcervoDocPersonal(XML_AJAX){
   if(XML_AJAX !== null){
       $("body").append(XML_AJAX);
   }    
}

function fu_setDatoFirmadoPorEditAcervo(cod, desc) {
    jQuery('#buscarConsulDocPersEmiBean').find('#txtFirmadoPor').val(desc);
    jQuery('#buscarConsulDocPersEmiBean').find('#coEmpFirmo').val(cod);
    fu_changeRemitenteEmiBean();
    removeDomId('windowConsultaElaboradoPor');
}

function fn_buscaRemitenteFiltroAcervoDocPersonal(){
    var p = new Array();
    p[0] = "accion=goBuscaRemitenteAcervo";	    
    p[1] = "pnuAnn=" + jQuery('#coAnnio').val();	    
    ajaxCall("/srReporteAcervoDocPersonal.do",p.join("&"),function(data){
           fn_rptaBuscaRemitenteFiltroAcervoDocPersonal(data); 
        },
    'text', false, false, "POST");       
}

function fu_iniTblDocPersEmiAcervo(){
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
            {"sType": "fecha"},                        
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
                if (index === 5 || index === 8 || index === 9 || index === 10) {
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
            var pnuAnn=$(this).children('td')[0].innerHTML;
            var pnuEmi=$(this).children('td')[1].innerHTML;
            if (!!pnuAnn&&!!pnuEmi) {
                jQuery('#txtpnuAnn').val(pnuAnn);
                jQuery('#txtpnuEmi').val(pnuEmi);
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

function fu_cleanEmiDocPersAcervoDoc(tipo){
    var noForm='#buscarConsulDocPersEmiBean';
    if(tipo==="1"){
        jQuery(noForm).find("#nuCorEmi").val("");
        jQuery(noForm).find("#nuDoc").val("");
        jQuery(noForm).find("#deAsu").val("");
        jQuery(noForm).find("#esIncluyeFiltro1").prop('checked',false);
        jQuery(noForm).find("#esIncluyeFiltro1").attr('checked',false);        
        jQuery(noForm).find("#feEmiIni").val("");
        jQuery(noForm).find("#feEmiFin").val("");
    }else{
        jQuery(noForm).find("#estadoDoc option[value=]").prop("selected", "selected");
        jQuery(noForm).find("#tipoDoc option[value=]").prop("selected", "selected");
        //jQuery("#sPrioridadDoc option[value=]").prop("selected", "selected");
        jQuery(noForm).find("#coDepRef").val("");
        jQuery(noForm).find("#txtRefOrigen").val(" [TODOS]");
        jQuery(noForm).find("#coDepDestino").val("");
        jQuery(noForm).find("#txtDependenciaDes").val(" [TODOS]");        
        jQuery(noForm).find("#esFiltroFecha").val("3");
        jQuery(noForm).find("#txtRemitente").val(" [TODOS]"); 
        jQuery(noForm).find("#txtFirmadoPor").val(" [TODOS]");  
        jQuery(noForm).find("#coEmpFirmo").val("");
        jQuery(noForm).find("#coDepRemite").val("");
        jQuery(noForm).find("#nuAnn").val(jQuery("#txtAnnioActual").val());        
        jQuery("#fechaFiltro").showDatePicker({defaultOpcionSelected: 3});
    }    
}