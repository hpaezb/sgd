function fn_inicializaRecepDocAdm(sCoAnnio){
    jQuery('#buscarDocumentoRecepBean').find('#esFiltroFecha').val("2");//solo año
    //jQuery("#fechaFiltro").html("Año: "+sCoAnnio);
    /*jQuery("#fechaFiltro").showDatePicker({
        showDia: false,
        selectTodosMeses:true,
        pressAceptarEvent: function(data) {
            if(data.rbOp==="0"){
                jQuery('#buscarDocumentoRecepBean').find('#sCoAnnio').val(data.anio);
                if(data.mes==="" && data.anio!==""){
                   jQuery("#fechaFiltro").html("Año: "+data.anio);
                   jQuery('#buscarDocumentoRecepBean').find('#esFiltroFecha').val("2");//solo año
                }else{
                   jQuery("#fechaFiltro").html("Año: "+data.anio+"  Mes: "+monthYearArray[data.mes * 1]);  
                   jQuery('#buscarDocumentoRecepBean').find('#esFiltroFecha').val("3");//año y mes
                }
            }else if(data.rbOp==="1"){
                jQuery('#buscarDocumentoRecepBean').find('#esFiltroFecha').val("1");//rango fecha
                jQuery("#fechaFiltro").html("Del: "+data.fIni+"  Al: "+data.fFin); 
            }
        }
    });*/
    jQuery('#buscarDocumentoRecepBean').find("#fechaFiltro").showDatePicker({defaultOpcionSelected: 5});
    jQuery('#buscarDocumentoRecepBean').find('#sNumDocRef').change(function() {
        $(this).val(replicate($(this).val(), 6));
    });
    jQuery('#buscarDocumentoRecepBean').find('#sNroDocumento').change(function() {
        $(this).val(replicate($(this).val(), 6));
    });
    jQuery('#buscarDocumentoRecepBean').find('#sCoAnnioBus').find('option[value=""]').remove();
    pnumFilaSelect=0;
    changeTipoBusqRecepDocuAdm("0");
}


function preloadMostrarVentana(a,pcoDep,tipoBandeja){
   console.log("preloadMostrarVentana("+a+","+pcoDep+","+tipoBandeja+")");
   if(a === "01" || a === "02" || a === "04"|| a === "07"){
   //if(a === "01" || a === "04"|| a === "07"){   
    var p = new Array();
    p[0] = "accion=goInicio";	
    /*if(a==="02"){
    p[1] = "estadoDoc=1"; /*RECIBIDO   
    }
    else
    {*/
    p[1] = "estadoDoc="+ a;	     
    //}
    p[2] = "coDep="+ pcoDep;
    p[3]="coBandeja="+tipoBandeja;
	ajaxCall("/srDocumentoAdmRecepcion.do",p.join("&"),refreshAppBody, 'text', false, false, "GET");      
       
        
        hideMenuSio();
   }else if(a === "05" || a === "06"){
        //var coDep = coDepUsuario;
        var p = new Array();
        p[0] = "accion=goInicio";
        if(a === "05"){
            p[1] = "estadoDoc=5";
        }else{
            p[1] = "estadoDoc=7";
        }
        p[2] = "coDep="+ pcoDep;	

        ajaxCall("/srDocumentoAdmEmision.do",p.join("&"),refreshAppBody, 'text', false, false, "GET");       
        hideMenuSio();       
   }else if(a==="VB"){
        var p = new Array();
        p[0] = "accion=goInicio";
        p[1] = "coDep="+ pcoDep;
        p[2] = "coEsDoc=0";
        ajaxCall("/srDocumentoVoBo.do",p.join("&"),refreshAppBody, 'text', false, false, "GET");       
        hideMenuSio();         
   }
   /*28/08/19 HPB Devolucion Doc a Oficina*/
   else if(a === "08"){
        var p = new Array();
        p[0] = "accion=goInicio";
        if(a === "08"){
            p[1] = "estadoDoc=0";
        }else{
            p[1] = "estadoDoc=7";
        }
        p[2] = "coDep="+ pcoDep;
        p[3] = "tiEnvMsj=3";

        ajaxCall("/srDocumentoAdmEmision.do",p.join("&"),refreshAppBody, 'text', false, false, "GET");       
        hideMenuSio();       
   } 
   /*28/08/19 HPB Devolucion Doc a Oficina*/
   /*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
   else if(a === "09"){
        var p = new Array();
        p[0] = "accion=goInicio";
        if(a === "09"){
            p[1] = "estadoDoc=0";
        }else{
            p[1] = "DocEstadoMsj=7";
        }
        p[2] = "coDep="+ pcoDep;
        p[3]="coBandeja="+a;

        ajaxCall("/srDocumentoAdmEmision.do",p.join("&"),refreshAppBody, 'text', false, false, "GET");       
        hideMenuSio();       
   }    
   /*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
   else if(a === "10"){
        var p = new Array();
        p[0] = "accion=goInicio";
        p[1] = "estadoDoc=B";
        p[2] = "coDep="+ pcoDep;
        p[3]="coBandeja="+a;

        ajaxCall("/srMesaPartes.do",p.join("&"),refreshAppBody, 'text', false, false, "GET");       
        hideMenuSio();       
   }
   return false;
}

function preloadMostrarVentanaOld(a,pcoDep){
   
   if(a === "01" || a === "04"|| a === "07"){   
    var p = new Array();
    p[0] = "accion=goInicio";	
    p[1] = "estadoDoc="+ a;  
    p[2] = "coDep="+ pcoDep;	   
	ajaxCall("/srDocumentoAdmRecepcion.do",p.join("&"),refreshAppBody, 'text', false, false, "GET");       
        hideMenuSio();
   }else if(a === "05" || a === "06"){
        //var coDep = coDepUsuario;
        var p = new Array();
        p[0] = "accion=goInicio";
        if(a === "05"){
            p[1] = "estadoDoc=5";
        }else{
            p[1] = "estadoDoc=7";
        }
        p[2] = "coDep="+ pcoDep;	

        ajaxCall("/srDocumentoAdmEmision.do",p.join("&"),refreshAppBody, 'text', false, false, "GET");       
        hideMenuSio();       
   }else if(a==="VB"){
        var p = new Array();
        p[0] = "accion=goInicio";
        p[1] = "coDep="+ pcoDep;
        p[2] = "coEsDoc=0";
        ajaxCall("/srDocumentoVoBo.do",p.join("&"),refreshAppBody, 'text', false, false, "GET");       
        hideMenuSio();         
   } 
   return false;
}

function submitAjaxFormRecepDocAdm(tipo){
    /*HPB 20/02/2020 - Inicio - Requerimiento Paginación recepcionados*/
    var paginaRecepcionSelect = jQuery('#txtPaginaRecepcion').val();
    if(typeof(paginaRecepcionSelect)==="undefined" || paginaRecepcionSelect===null || paginaRecepcionSelect===""){
        paginaRecepcionSelect = 1;
    }
    /*HPB 20/02/2020 - Fin - Requerimiento Paginación recepcionados*/
    var validaFiltro=fu_validaFiltroRecepDocAdm(tipo);
    if (validaFiltro==="1") {
        ajaxCall("/srDocumentoAdmRecepcion.do?accion=goInicio&vPagina="+paginaRecepcionSelect,$('#buscarDocumentoRecepBean').serialize(),function(data){
                refreshScript("divTablaRecepDocumenAdm", data);
        }, 'text', false, false, "POST");
    }else if(validaFiltro === "2"){//buscar referencia
        ajaxCall("/srDocumentoAdmRecepcion.do?accion=goBuscaDocumentoEnReferencia&vPagina="+paginaRecepcionSelect, $('#buscarDocumentoRecepBean').serialize(), function(data) {
            fu_rptaBuscaDocumentoEnReferenciaRecep(data);
        }, 'text', false, false, "POST");        
    }  
    return false;
}

function fu_rptaBuscaDocumentoEnReferenciaRecep(data){
    if(data!==null){
        if(data==="0"){
          alert_Info("Buscar Referencia", "No Existe Documento.");
        }else{
          refreshScript("divTablaRecepDocumenAdm", data);  
        }
    }
}

function fu_validaFiltroRecepDocAdm(tipo){
    var valRetorno="1";
    /*
    if(tipo === '1'){
        var vFechaActual=jQuery('#txtFechaActual').val();//dojo.byId("txtFechaActual").value;
        var vNroEmision=jQuery('#sNroEmision').val();
        var vFeFinal=jQuery('#sFechaEmisionFin').val();
        var vFeInicio=jQuery('#sFechaEmisionIni').val();

        valRetorno=fu_validaFiltroRecepDocAdm2(vFechaActual,vNroEmision,vFeInicio,vFeFinal);    
    }
    */
   
    jQuery('#sFeEmiIni').val(jQuery('#sCoAnnio').parent('td').find('#fechaFiltro').attr('fini'));
    jQuery('#sFeEmiFin').val(jQuery('#sCoAnnio').parent('td').find('#fechaFiltro').attr('ffin'));    
    
    
    //var pEsIncluyeFiltro = jQuery("esIncluyeFiltro1").is(':checked');
    var pEsIncluyeFiltro = $('#buscarDocumentoRecepBean').find('#esIncluyeFiltro1').is(':checked');
    //var pEsFiltroFecha = jQuery("#esFiltroFecha").val();
    var vFechaActual = jQuery('#txtFechaActual').val();
    if(tipo==="0"){
        valRetorno = fu_validaFiltroRecepDocAdmFiltrar(vFechaActual);     
    }else if(tipo==="1"){
      //verificar si se ingreso datos en los campos de busqueda de referencia
      valRetorno = fu_validarBusquedaXReferenciaRecep(tipo);  
      if(valRetorno==="1"){
        valRetorno = fu_validaFiltroRecepDocAdmBuscar();
        if(valRetorno==="1"){
            if(pEsIncluyeFiltro){
               valRetorno = fu_validaFiltroRecepDocAdmFiltrar(vFechaActual); 
            }else{
               valRetorno = setAnnioNoIncludeFiltroRecep();
            }
        }
      }        
    }    
    return valRetorno;
}

function fu_validarBusquedaXReferenciaRecep(tipo){
    var valRetorno="1";//no buscar por referencia
    if(tipo === "1"){
        var vBuscDestinatario = allTrim(jQuery('#sBuscDestinatario').val());
        var vDeTipoDocAdm = allTrim(jQuery('#sDeTipoDocAdm').val());
        var vCoAnnioBus = allTrim(jQuery('#sCoAnnioBus').val());
        var vNumDocRef = allTrim(jQuery('#sNumDocRef').val());

        if((typeof(vBuscDestinatario)!=="undefined" && vBuscDestinatario!==null && vBuscDestinatario!=="") &&
           (typeof(vDeTipoDocAdm)!=="undefined" && vDeTipoDocAdm!==null && vDeTipoDocAdm!=="") &&
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

function fu_validaFiltroRecepDocAdmBuscar() {
    var valRetorno = "1";
    
    upperCaseBuscarRecepDocAdmBean();
    
    var vNroDocumento = jQuery('#sNroDocumento').val();
    var vNroExpediente = jQuery('#sBuscNroExpediente').val();
    var vAsunto = jQuery('#sBuscAsunto').val();
    
    if((typeof(vNroDocumento)==="undefined" || vNroDocumento===null || vNroDocumento==="") &&
       (typeof(vNroExpediente)==="undefined" || vNroExpediente===null || vNroExpediente==="") &&
       (typeof(vAsunto)==="undefined" || vAsunto===null || vAsunto==="")){
       //alert("Ingresar Algún parámetro de Búsqueda");
       alert_Warning("Buscar: ","Ingresar Algún parámetro de Búsqueda.");
       valRetorno = "0";
    }
    
    return valRetorno;
}

function fu_validaFiltroRecepDocAdmFiltrar(vFechaActual){
    var valRetorno = "1";
    if (valRetorno==="1") {
        fu_obtenerEsFiltroFecha('buscarDocumentoRecepBean');
        var pEsFiltroFecha = jQuery('#buscarDocumentoRecepBean').find("#esFiltroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
        if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
           if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
                var pAnnio = jQuery('#buscarDocumentoRecepBean').find('#sCoAnnio').val();
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
                        jQuery('#buscarDocumentoRecepBean').find('#sCoAnnio').val(pAnnioBusq);                          
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

function fu_validaFiltroRecepDocAdm2(vFechaActual,vNroEmision,vFeInicio,vFeFinal){
var valRetorno="1";
var vValidaNumero="";

    if (vNroEmision!="" && vNroEmision!=null) {
        vValidaNumero=fu_validaNumero(vNroEmision);
        if (vValidaNumero!="OK") {
           bootbox.alert("N° de Emisión debe ser solo numeros.");
            valRetorno="0";
        }
    }
    if (valRetorno=="1") {
        //VALIDA FECHAS
        if (vFeInicio==""){
            //alert('Debe ingresar Fecha Del');
            //valRetorno="0";
            valRetorno="1";
        } else {
            valRetorno=fu_validaFechaConsulta(vFeInicio,vFechaActual);
            if (valRetorno!="1") {
               bootbox.alert("Error en Fecha Del : "+ valRetorno);
                valRetorno="0";
            }
        }
    }
    if (valRetorno=="1") {
        //VALIDA FECHAS
        if (vFeFinal==""){
            //alert('Debe ingresar Fecha Al');
            //valRetorno="0";
            valRetorno="1";
        } else {
            valRetorno=fu_validaFechaConsulta(vFeFinal,vFechaActual);
            if (valRetorno!="1") {
               bootbox.alert("Error en Fecha Al : "+ valRetorno);
                valRetorno="0";
            }
        }
    }
    //se verifica que diferencia de fechas sea maximo de 5 dias
    /*if (valRetorno=="1") {
        vCantidadDias =  getNumeroDeDiasDiferencia(vFeInicio,vFeFinal);
        if (vCantidadDias > 5 || vCantidadDias < 0){
           bootbox.alert("Diferencia entre fechas debe ser maximo de 5 dias");
            valRetorno="0";
        }
    }*/
    return valRetorno;    
}

function changeTipoBusqRecepDocuAdm(tipo){
    /*if(tipo === "1"){
        if(jQuery('#txtMuestraDivSearchField').val() === '0'){
            jQuery('#txtMuestraDivSearchField').val('1');
            jQuery('#divSearchField').show();
            jQuery('#sFechaEmision').focus();
        }else{
            jQuery('#divSearchField').hide();
            jQuery('#txtMuestraDivSearchField').val('0');
            jQuery('#sTipoBusqueda').val(tipo);
            submitAjaxFormRecepDocAdm();
        }    
    }else{
            jQuery('#divSearchField').hide();
            jQuery('#txtMuestraDivSearchField').val('0');
            jQuery('#sTipoBusqueda').val(tipo);
            submitAjaxFormRecepDocAdm();    
    }*/
    jQuery('#sTipoBusqueda').val(tipo);
    submitAjaxFormRecepDocAdm(tipo);
    mostrarOcultarDivBusqFiltro2();
    }

function fn_changeTipoRemiDocRecepAdm(cmbTipoRemite,esIni){
    var noForm='buscarDocumentoRecepBean';
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

function fn_getCiudadanoRemDocRecAdm()
{
    var noForm='buscarDocumentoRecepBean';
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

function fn_validarNuDniRemiDocRecAdm(noForm,pnuDniAux){
    var valRetorno = true;
    if(!!pnuDniAux){
        var lnuDniAux=pnuDniAux.length;
        var vValidaNumero = fu_validaNumero(pnuDniAux);
        if (vValidaNumero !== "OK") {
            valRetorno = false;
            bootbox.alert("<h5>Número DNI debe ser solo números.</h5>", function() {
                bootbox.hideAll();
                jQuery('#'+noForm).find('#nuDniAux').focus();
            }); 
        }
        if(valRetorno){
            if(lnuDniAux!==8){
                valRetorno = false;
                bootbox.alert("<h5>Número DNI Inválido.</h5>", function() {
                    bootbox.hideAll();
                    jQuery('#'+noForm).find('#nuDniAux').focus();
                });             
            }
        }
    }else{
        valRetorno = false;
        bootbox.alert("<h5>Digite Número DNI.</h5>", function() {
            bootbox.hideAll();
            jQuery('#'+noForm).find('#nuDniAux').focus();
        });        
    }
    return valRetorno;
}

function fn_rptaGetCiudadanoRemDocRecAdm(data,noForm){
    if(data!==null){
        if(data.coRespuesta==="1"){
            var obj = data.ciudadanoBean;
            if(!!obj){
            jQuery('#'+noForm).find("#busNumDni").val(obj.nuDoc);
            jQuery('#'+noForm).find("#nuDniAux").val(obj.nuDoc);
            jQuery('#'+noForm).find("#busDescDni").val(obj.nombre);
            jQuery('#'+noForm).find("#coTipoPersona").focus();
            jQuery('#envRemitenteEmiBean').val("1");
            
            jQuery('#'+noForm).find("#busResultado").val("1");
            
        }else{
                bootbox.alert("<h5>No se encuentra ciudadano en la base de datos de la Institución.</h5>", function() {
                    bootbox.hideAll();
                    jQuery('#'+noForm).find('#nuDniAux').focus();
                    jQuery('#'+noForm).find("#busNumDni").val('');
                    jQuery('#'+noForm).find("#busDescDni").val('');
                    
                    jQuery('#'+noForm).find("#busResultado").val("");
                });                 
            }
        }else{
            bootbox.alert("<h5>No se encuentra ciudadano en la base de datos de la Institución.</h5>", function() {
                //fn_CleanDatosRemitenteDocExtRec('#'+noForm,'#divRemCiudadano');
                jQuery('#'+noForm).find("#busNumDni").val('');
                jQuery('#'+noForm).find("#busDescDni").val('');
                bootbox.hideAll();
                jQuery('#'+noForm).find('#nuDniAux').focus();
                
                jQuery('#'+noForm).find("#busResultado").val("");
            });            
        }
    }
}

function onclickBuscarCiudadanoRecAdm(){
    var noForm='buscarDocumentoRecepBean';
    var pDesCiudadano=allTrim(jQuery('#'+noForm).find('#busDescDni').val());
    var pnuDniAux=allTrim(jQuery('#'+noForm).find('#nuDniAux').val());
    
    if(!!pnuDniAux && pnuDniAux.length > 0){
        fn_getCiudadanoRemDocRecAdm();
    }else{
        buscarCiudadanoEditDocRecepAdm(pDesCiudadano);
    }
}

function buscarCiudadanoEditDocRecepAdm(pDesCiudadano){
    
    var sDescCiudadano = allTrim(fu_getValorUpperCase(pDesCiudadano));
    //snoRazonSocial=fn_getCleanTextLenGreathree(fn_getCleanTextExpReg(snoRazonSocial));
    sDescCiudadano=fn_getCleanTextExpReg(sDescCiudadano);
    sDescCiudadano=sDescCiudadano.trim();
    
    if(allTrim(sDescCiudadano).length >= 0 && allTrim(sDescCiudadano).length <= 1){  //El texto es entre 1 y 3 caracteres
            bootbox.alert("<h5>Debe ingresar como mínimo 2 caracteres.</h5>", function() {
            bootbox.hideAll();
            //($(cell).parent()).find('input[type=text]').focus();
        });  
        return;
    }         
    if (!!sDescCiudadano) {
        var rptValida = validaCaracteres(sDescCiudadano, "2");
        if (rptValida === "OK") {
            // Expresion regular
            var cadena ="^[A-Z0-9ÑÁÉÍÓÚ ]+$";
            var re = new RegExp(cadena);
            if(sDescCiudadano.match(re)){      
                var p = new Array();
                p[0] = "accion=goBuscaCiudadano";
                p[1] = "sDescCiudadano=" + sDescCiudadano;
                ajaxCall("/srDocumentoAdmRecepcion.do", p.join("&"), function(data) {
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

function fn_rptaBuscarCiudadanoEditDocRecepAdm(XML_AJAX) {
    if (XML_AJAX !== null) {
        $("body").append(XML_AJAX);
    }
}

function fn_iniConsCiudadanoEditRecDocAdm(){
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
                    //var allCells = $(row).find('td');
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
                    fn_setCiudadanoEditRecDocAdm(pDesCiudadano,pnuDni);
                }
            }else if(evento.which==38||evento.which==40){
                //$('#txtConsultaFind').blur();
                indexAux=-1;
                $(nomTbl+' >tbody >tr:visible').first().focus(); 
            }
    };
    $('#txtConsultaFind').keyup(searchOnTable);
    $(nomTbl+" tbody tr").click(function(e) {
        var pDesCiudadano= $(this).find("td:eq(0)").html();
        var pnuDni= $(this).find("td:eq(1)").html();
        fn_setCiudadanoEditRecDocAdm(pDesCiudadano,pnuDni);
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
            fn_setCiudadanoEditRecDocAdm(pDesCiudadano,pnuDni);      
        }
        //console.log(indexAux);
        evento.preventDefault();
    });    
}

function fn_setCiudadanoEditRecDocAdm(pDesCiudadano,pnuDni){
    pDesCiudadano = pDesCiudadano.replace(/&amp;/g, "&");
    var noForm='buscarDocumentoRecepBean';
    jQuery('#'+noForm).find('#busDescDni').val(pDesCiudadano);
    jQuery('#'+noForm).find('#busNumDni').val(pnuDni);
    jQuery('#'+noForm).find('#nuDniAux').val(pnuDni);
    removeDomId('windowConsultaOtroOri');
    jQuery('#'+noForm).find('#coTipoPersona').focus();
    jQuery('#envRemitenteEmiBean').val("1");
    jQuery('#busResultado').val("1");
        
    return false;
}

function buscarProveedorDocRecepAdm(){
    var noForm='buscarDocumentoRecepBean';
    var prazonSocial=allTrim(jQuery('#'+noForm).find('#busDescRuc').val());
    var pnuRucAux=allTrim(jQuery('#'+noForm).find('#nuRucAux').val());
    
    if(!!pnuRucAux&&pnuRucAux.length===11){
        buscarProveedorEditDocRecepAdmJson(pnuRucAux,noForm);
    }
}
function buscarProveedorEditDocRecepAdmJson(pnuRuc,noForm){
    if(validarBuscarProveedorEditDocAdm(pnuRuc)){
        var p = new Array();
        p[0] = "accion=goBuscaProveedorJson";
        p[1] = "pnuRuc=" + pnuRuc;
        ajaxCall("/srMesaPartes.do", p.join("&"), function(data) {
            if (!!data){
                if(data.coRespuesta==="1"){
                    var obj = data.proveedorBean;
                    if(!!obj){
                    jQuery('#'+noForm).find('#busDescRuc').val(obj.deRuc);
                    jQuery('#'+noForm).find('#busNumRuc').val(obj.nuRuc);
                    jQuery('#'+noForm).find('#nuRucAux').val(obj.nuRuc);
                    jQuery('#'+noForm).find('#coTipoPersona').focus();
                    jQuery('#envRemitenteEmiBean').val("1");
                    
                    jQuery('#'+noForm).find('#busResultado').val("1");
                    }else{
                        bootbox.alert("<h5>Número de RUC no registrado.</h5>", function() {
                            bootbox.hideAll();
                            jQuery('#'+noForm).find('#nuRucAux').focus();
                            
                            jQuery('#'+noForm).find('#busResultado').val('');
                            jQuery('#'+noForm).find('#busDescRuc').val('');
                        });                         
                    }
                }else{
                    bootbox.alert("<h5>Número de RUC no registrado.</h5>", function() {
                        //Ver el limpiado de datos
                        fn_CleanDatosRemitenteDocRecAdm('#'+noForm,'#divRemPersonaJuri');
                        
                        jQuery('#'+noForm).find('#busResultado').val("");
                        jQuery('#'+noForm).find('#busDescRuc').val('');
                        
                        bootbox.hideAll();
                        jQuery('#'+noForm).find('#nuRucAux').focus();
                    });
                }
            }
        }, 'json', false, true, "POST");        
    }
}

function onclickBuscarProveedorDocRecepAdm(){
    var noForm='buscarDocumentoRecepBean';
    var prazonSocial=allTrim(jQuery('#'+noForm).find('#busDescRuc').val());
    var pnuRucAux=allTrim(jQuery('#'+noForm).find('#nuRucAux').val());
    
    if(!!pnuRucAux&&pnuRucAux.length===11){
        buscarProveedorDocRecepAdm(pnuRucAux,noForm);
    }else if(!!pnuRucAux && pnuRucAux.length > 0){
        buscarProveedorDocRecepAdm(pnuRucAux,noForm);
    }else{
        buscarProveedorEditDocRecepAdm(prazonSocial);
    }
}

function buscarProveedorEditDocRecepAdm(prazonSocial){
    
    var snoRazonSocial = allTrim(fu_getValorUpperCase(prazonSocial));
    //snoRazonSocial=fn_getCleanTextLenGreathree(fn_getCleanTextExpReg(snoRazonSocial));
    snoRazonSocial=fn_getCleanTextExpReg(snoRazonSocial);
    snoRazonSocial=snoRazonSocial.trim();
    
    if(allTrim(snoRazonSocial).length >= 0 && allTrim(snoRazonSocial).length <= 1){  //El texto es entre 1 y 3 caracteres
            bootbox.alert("<h5>Debe ingresar como mínimo 2 caracteres.</h5>", function() {
            bootbox.hideAll();
            //($(cell).parent()).find('input[type=text]').focus();
        });  
        return;
    }         
    if (!!snoRazonSocial) {
        var rptValida = validaCaracteres(snoRazonSocial, "2");
        if (rptValida === "OK") {
            // Expresion regular
            var cadena ="^[A-Z0-9ÑÁÉÍÓÚ ]+$";
            var re = new RegExp(cadena);
            if(snoRazonSocial.match(re)){      
                var p = new Array();
                p[0] = "accion=goBuscaProveedorBus";
                p[1] = "prazonSocial=" + snoRazonSocial;
                ajaxCall("/srDocumentoAdmRecepcion.do", p.join("&"), function(data) {
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



function validarBuscarProveedorEditDocAdm(pnuRuc){
    var valRetorno = true;
    var vValidaNumero = fu_validaNumero(pnuRuc);
    if (vValidaNumero !== "OK") {
       bootbox.alert("N° de Ruc debe ser solo números.");
        valRetorno = false;
    }
    return valRetorno;
}

function fn_CleanDatosRemitenteDocRecAdm(noForm,idDiv){
    $(noForm).find(idDiv).find('input').val('');
}

function fu_cleanRecepDocAdm(tipo){
    if(tipo==="1"){
        jQuery("#sNroDocumento").val("");
        jQuery("#sBuscNroExpediente").val("");
        jQuery("#sBuscAsunto").val("");
        jQuery("#sDeTipoDocAdm").val("");
//        jQuery("#sBuscEstado").val("");
//        jQuery("#sFechaEmisionIni").val("");
//        jQuery("#sFechaEmisionFin").val("");
//        jQuery("#sUoremitente").val("");
//        jQuery("#sUoDestinatario").val("");
        jQuery("#sFeEmiIni").val("");
        jQuery("#sFeEmiFin").val("");
        jQuery("#sNroEmision").val("");
        jQuery("#sBuscDestinatario").val("");
        jQuery("#txtDepEmiteBus").val(" [TODOS]");
        jQuery("#sNumDocRef").val("");
        jQuery("#esIncluyeFiltro1").prop('checked',false);
        jQuery("#esIncluyeFiltro1").attr('checked',false); 
        jQuery("#txtPaginaRecepcion").val("");/* HPB 20/02/2020 - Requerimiento Paginación recepcionados */
//        mostrarOcultarDivBusqFiltro('2');    
    }else{
        jQuery("#sEstadoDoc option[value=0]").prop("selected", "selected");
        jQuery("#sPrioridadDoc option[value=]").prop("selected", "selected");
        jQuery("#sTipoDoc option[value=]").prop("selected", "selected");
        jQuery("#coTema option[value=]").prop("selected", "selected");
        jQuery("#sRemitente").val("");
        jQuery("#txtRemitente").val("[TODOS]");
        jQuery("#sDestinatario").val("");
        jQuery("#txtDestinatario").val("[TODOS]");
        jQuery("#sExpediente option[value=]").prop("selected", "selected");
        jQuery("#esFiltroFecha").val("2");//solo año
        //jQuery("#fechaFiltro").html("Año: "+jQuery("#txtAnnioActual").val());
        jQuery("#sCoAnnio").val(jQuery("#txtAnnioActual").val());          
        jQuery("#fechaFiltro").showDatePicker({defaultOpcionSelected: 5});        
        jQuery("#idEtiqueta option[value=]").prop("selected", "selected");
        jQuery("#txtPaginaRecepcion").val("");/* HPB 20/02/2020 - Requerimiento Paginación recepcionados */
    }
    
}

function changeTipoBusqRecepDocuAdm2(){
  changeTipoBusqRecepDocuAdm('1');
}

function passArrayToHtmlTabla(varData,sData){
    //var p = new Array();
    var values = '';
    $.each(sData,function (index,item){
       //p[index] = item.innerHTML;
       values = values + item.innerHTML + '&&';
    });
    jQuery('#'+varData).val(values);
}

function editarDocumentoRecepClick(pnuAnn,pnuEmi,pnuDes,pexisteDoc,pexisteAnexo){
      if(!!pnuAnn&&!!pnuEmi){  
        var p = new Array();
        p[0] = "pnuAnn=" + pnuAnn;	
        p[1] = "pnuEmi=" + pnuEmi;        
        p[2] = "pnuDes=" + pnuDes;
        ajaxCall("/srDocumentoAdmRecepcion.do?accion=goEditarDocumento",p.join("&"),function(data){
                refreshScript("divWorkPlaceRecepDocumAdmin", data);
                jQuery('#divRecepDocumentoAdmin').hide();
                jQuery('#divWorkPlaceRecepDocumAdmin').show();
                fn_cargaToolBarRec();
                var sEstadoDocAdm = jQuery('#documentoRecepBean').find('#esDocRec').val();
                fu_cargaEdicionDocAdm("01",sEstadoDocAdm);
                jQuery('#txtpnuAnn').val(pnuAnn);  
        }, 'text', false, false, "POST");              
    }else{
       bootbox.alert("Registro No existe.");
    }
}
/*[HPB-02/10/20] Inicio - Plazo de Atencion-**/
function editarDocumentoRecepClickPlazoAtencion(pnuAnn,pnuEmi,pnuDes){
      if(!!pnuAnn&&!!pnuEmi){  
        var p = new Array();
        p[0] = "pnuAnn=" + pnuAnn;	
        p[1] = "pnuEmi=" + pnuEmi;        
        p[2] = "pnuDes=" + pnuDes;
        ajaxCall("/srDocumentoAdmRecepcion.do?accion=goEditarDocumento",p.join("&"),function(data){
                refreshScript("divNewEmiDocumAdmin", data);
                jQuery('#divEmiDocumentoAdmin').hide();
                removeDomId('windowConsultaSeguimiento');
                jQuery('#divNewEmiDocumAdmin').show();
                fn_cargaToolBarRec();
                var sEstadoDocAdm = $('#esDocRec').val();
                fu_cargaEdicionDocAdm("01",sEstadoDocAdm);
                jQuery('#txtpnuAnn').val(pnuAnn);  
        }, 'text', false, false, "POST");              
    }else{
       bootbox.alert("Registro No existe.");
    }
}
/*[HPB-02/10/20] Inicio - Plazo de Atencion-**/
function editarDocumentoRecep(){
    var pnuAnn = jQuery('#txtpnuAnn').val();
    var pnuEmi = jQuery('#txtpnuEmi').val();
      if(!!pnuAnn&&!!pnuEmi){  
        var p = new Array();
        p[0] = "pnuAnn=" + pnuAnn;	
        p[1] = "pnuEmi=" + pnuEmi;        
        p[2] = "pnuDes=" + jQuery('#txtpnuDes').val();
        ajaxCall("/srDocumentoAdmRecepcion.do?accion=goEditarDocumento",p.join("&"),function(data){
                refreshScript("divWorkPlaceRecepDocumAdmin", data);
                jQuery('#divRecepDocumentoAdmin').hide();
                jQuery('#divWorkPlaceRecepDocumAdmin').show();
                fn_cargaToolBarRec();
                var sEstadoDocAdm = jQuery('#documentoRecepBean').find('#esDocRec').val();
                fu_cargaEdicionDocAdm("01",sEstadoDocAdm);
        }, 'text', false, false, "POST");              
    }else{
        alert_Info("Recepción :", "Seleccione una fila de la lista");
    }
}

function regresarRecepDocumAdmAnimado(){
    animacionventanaRecepDoc();
    setTimeout(function(){regresarRecepDocumAdm();}, 300);
}
function regresarRecepDocumAdm(){
    jQuery('#divRecepDocumentoAdmin').toggle();                                
    jQuery('#divWorkPlaceRecepDocumAdmin').toggle(); 
    submitAjaxFormRecepDocAdm(jQuery('#buscarDocumentoRecepBean').find('#sTipoBusqueda').val());
    jQuery('#divWorkPlaceRecepDocumAdmin').html("");          
    //mostrarOcultarDivBusqFiltro2();
}

function fu_rptaRegresarRecepDocAdm(data){
    if(data!==null){
        if(data==="0"){
          alert_Info("Buscar Referencia", "No Existe Documento.");
        }else{
          refreshScript("divTablaRecepDocumenAdm", data);  
        }
    }
}

function fn_verDocumento(pnuAnn,pnuEmi, pOpcion, pnuDes){
    fn_verDocumentosObj(pnuAnn,pnuEmi,"0", pOpcion, pnuDes);
}

function fn_verAnexo(pnuAnn,pnuEmi,pnuDes){
    fn_verAnexosObj(pnuAnn,pnuEmi,pnuDes);
}

function fn_EnviarNotificacion(pnuAnn,pnuEmi,ptiEnv,pexisteDoc,pexisteAnexo,tipoBandeja,docEstadoMsj)
{
    /*interoperabilidad segdi mvaldera*/
     fn_enviarNotificacionObj(pnuAnn,pnuEmi,ptiEnv,pexisteDoc,pexisteAnexo,tipoBandeja,docEstadoMsj);
     /*interoperabilidad segdi mvaldera*/
}

function fn_verSeguimiento() {
    var pnuAnn = jQuery('#txtpnuAnn').val();
    var pnuEmi = jQuery('#txtpnuEmi').val();
    var pnuDes = jQuery('#txtpnuDes').val();
    if (pnuAnn) {
        fn_verSeguimientoObj(pnuAnn, pnuEmi, pnuDes);
    } else {
        //alert("Seleccione una fila de la lista");
        alert_Info("Recepción :", "Seleccione una fila de la lista.");
    }
}

function fn_verDocumentoRec() {
    var pnuAnn = jQuery('#txtpnuAnn').val();
    var pnuEmi = jQuery('#txtpnuEmi').val();
    var ptiOpe = "0";
    if (pnuAnn) {
        fn_verDocumentosObj(pnuAnn, pnuEmi, ptiOpe);
    } else {
        //alert("Seleccione una fila de la lista");
        alert_Info("Recepción :", "Seleccione una fila de la lista.");
    }
}

function fn_verAnexoRec() {
    var pnuAnn = jQuery('#txtpnuAnn').val();
    var pnuEmi = jQuery('#txtpnuEmi').val();
    var pnuDes = jQuery('#txtpnuDes').val();
    if (pnuAnn) {
        fn_verAnexosObj(pnuAnn, pnuEmi, pnuDes);
    } else {
        //alert("Seleccione una fila de la lista");
        alert_Info("Recepción :", "Seleccione una fila de la lista.");
    }
}

function fn_verSeguimientoRecEdit() {
    var pnuAnn = jQuery('#documentoRecepBean').find('#nuAnn').val();
    var pnuEmi = jQuery('#documentoRecepBean').find('#nuEmi').val();
    var pnuDes = jQuery('#documentoRecepBean').find('#nuDes').val();

    if (pnuAnn) {
        fn_verSeguimientoObj(pnuAnn, pnuEmi, pnuDes);
    } else {
        //alert("Seleccione una fila de la lista");
        alert_Info("Recepción :", "Seleccione una fila de la lista.");
    }
}

function fn_verDocumentoRecEdit() {
    var pnuAnn = jQuery('#documentoRecepBean').find('#nuAnn').val();
    var pnuEmi = jQuery('#documentoRecepBean').find('#nuEmi').val();
    var pnuDes = jQuery('#documentoRecepBean').find('#nuDes').val();
    var ptiOpe = "0";
    var pOpcion = allTrim($("#txtCodOpcion").val());//Hermes 28/05/2019
    if (pnuAnn) {
        fn_verDocumentosObj(pnuAnn, pnuEmi, ptiOpe, pOpcion);
    } else {
        //alert("Seleccione una fila de la lista");
        alert_Info("Recepción :", "Seleccione una fila de la lista.");
    }
}

function fn_verAnexoRecEdit() {
    var pnuAnn = jQuery('#documentoRecepBean').find('#nuAnn').val();
    var pnuEmi = jQuery('#documentoRecepBean').find('#nuEmi').val();
    var pnuDes = jQuery('#documentoRecepBean').find('#nuDes').val();
    if (pnuAnn) {
        fn_verAnexosObj(pnuAnn, pnuEmi, pnuDes);
    } else {
        //alert("Seleccione una fila de la lista");
        alert_Info("Recepción :", "Seleccione una fila de la lista.");
    }
}


function fn_rptaBuscaRemitente(XML_AJAX){
   if(XML_AJAX !== null){
       $("body").append(XML_AJAX);
   }    
}

function fn_buscaRemitente(){
    var p = new Array();
    p[0] = "accion=goBuscaRemitente";	    
    p[1] = "pnuAnn=" + jQuery('#sCoAnnio').val();	    
    ajaxCall("/srDocumentoAdmRecepcion.do",p.join("&"),function(data){
           fn_rptaBuscaRemitente(data); 
        },
    'text', false, false, "POST");       
}

function fn_rptaBuscaDestinatario(XML_AJAX){
   if(XML_AJAX !== null){
       $("body").append(XML_AJAX);
   }    
}

function fn_buscaEmpleado(){
    ajaxCall("/srDocumentoAdmRecepcion.do?accion=goBuscaEmpleado",'',function(data){
           fn_rptaBuscaEmpleado(data); 
        },
    'text', false, false, "POST");       
}

function fn_rptaBuscaEmpleado(XML_AJAX){
   if(XML_AJAX !== null){
       $("body").append(XML_AJAX);
   }    
}

function fu_setDatoEmpleado(cod,desc){
    jQuery('#deEmpRec').val(desc);
    jQuery('#coEmpRec').val(cod);
    removeDomId('windowConsultaEmpl');
}

function fn_buscaDestinatario(){
    var p = new Array();    
    p[0] = "accion=goBuscaDestinatario";	    
    p[1] = "pnuAnn=" + jQuery('#sCoAnnio').val();    
    ajaxCall("/srDocumentoAdmRecepcion.do",p.join("&"),function(data){
           fn_rptaBuscaDestinatario(data); 
        },
    'text', false, false, "POST");       
}

function fu_setDatoDestinatario(cod,desc){
    jQuery('#txtDestinatario').val(desc);
    jQuery('#sDestinatario').val(cod);
    removeDomId('windowConsultaDest');
}

function fu_setDatoRemitente(cod,desc){
    jQuery('#txtRemitente').val(desc);
    jQuery('#sRemitente').val(cod);
    removeDomId('windowConsultaRem');
}

function mostrarOcultarDivBusqFiltro(divMostrar){
    if(divMostrar === "1"){
        if(jQuery('#divConfigFiltro').is (':visible')){
           jQuery('#divConfigFiltro').hide();
           jQuery('#spanDivFiltro').removeClass("glyphicon-collapse-up");
           jQuery('#spanDivFiltro').addClass("glyphicon-collapse-down");
        }else{
           jQuery('#divConfigFiltro').show();  
           jQuery('#spanDivFiltro').removeClass("glyphicon-collapse-down");
           jQuery('#spanDivFiltro').addClass("glyphicon-collapse-up");              
        }
    }else if(divMostrar === "2"){
        if(jQuery('#divConfigBusqueda').is (':visible')){
           jQuery('#divConfigBusqueda').hide(); 
//           jQuery('#spanDivBusqueda').removeClass("ui-icon-circle-arrow-n");
//           jQuery('#spanDivBusqueda').addClass("ui-icon-circle-arrow-s");           
           jQuery('#spanDivBusqueda').removeClass("glyphicon-collapse-up");
           jQuery('#spanDivBusqueda').addClass("glyphicon-collapse-down");
        }else{
           jQuery('#divConfigBusqueda').show();  
           jQuery('#spanDivBusqueda').removeClass("glyphicon-collapse-down");
           jQuery('#spanDivBusqueda').addClass("glyphicon-collapse-up");              
        }        
    }
}

function mostrarOcultarDivBusqFiltro2(){
    //if(divMostrar === "0"){
        if(jQuery('#divConfigFiltro').is (':visible')){
           jQuery('#divConfigFiltro').hide();
           jQuery('#spanDivFiltro').removeClass("ui-icon-circle-arrow-n");
           jQuery('#spanDivFiltro').addClass("ui-icon-circle-arrow-s");
        }/*else{
           jQuery('#divConfigFiltro').show();  
           jQuery('#spanDivFiltro').removeClass("ui-icon-circle-arrow-s");
           jQuery('#spanDivFiltro').addClass("ui-icon-circle-arrow-n");           
        }*/
    //}else if(divMostrar === "1"){
        if(jQuery('#divConfigBusqueda').is (':visible')){
           jQuery('#divConfigBusqueda').hide(); 
           jQuery('#spanDivBusqueda').removeClass("ui-icon-circle-arrow-n");
           jQuery('#spanDivBusqueda').addClass("ui-icon-circle-arrow-s");           
        }/*else{
           jQuery('#divConfigBusqueda').show();  
           jQuery('#spanDivBusqueda').removeClass("ui-icon-circle-arrow-s");
           jQuery('#spanDivBusqueda').addClass("ui-icon-circle-arrow-n");              
        }    */    
    //}    
        }

function fu_grabarRecepDocumentoAdm(){
    var vAccion = "0";
    var pesDocRec = jQuery('#documentoRecepBean').find('#esDocRec').val();
    var vResult = fu_validaFechaRecepDocAdm();
    if(vResult){
        vResult=fu_validaFechaAteArchDoc();
        if(vResult){    
            if(pesDocRec === "0"){
                vAccion = "1";
                bootbox.dialog({
                    message: " <h5>Recibir Documento ?</h5>",
                    buttons: {
                        SI: {
                            label: "SI",
                            className: "btn-primary",
                            callback: function() {
                                fu_recibirDocumentoAdm(vAccion);  
                            }                        
                        },
                        NO: {
                            label: "NO",
                            className: "btn-default"
                        }
                    }
                });                
            }else{
                fu_recibirDocumentoAdm(vAccion);
            }
        }
    }else{
        jQuery('#documentoRecepBean').find('#feRecDoc').focus();
    }    
}

function fu_recibirDocumentoAdm(vAccion){
    var validaFiltro="";
    //var tipo = dojo.byId("sTipoControl").value;
    //jQuery('#sTipoBusqueda').val("1");
    validaFiltro=fu_verificarCamposDocRecepcion(vAccion);
    if(validaFiltro==="1"){
        if (jQuery('#envDocumentoRecepBean').val()==="1" || vAccion==='1' || vAccion==="2") {
            ajaxCall("/srDocumentoAdmRecepcion.do?accion=goUpdDocumento&vAccion="+vAccion,$('#documentoRecepBean').serialize(),function(data){
                    fu_rptaRecibirDocumentoAdm(data,vAccion);
            }, 'json', false, false, "POST");        
        }else{
          alert_Info("Recepcion: ","El Documento es el mismo.");  
        }
    }
    return false;    
}

function fu_rptaRecibirDocumentoAdm(data,accion){
    if(data!==null){
        if(data.coRespuesta === "1"){
            var pEsDocRec = data.esDocRec;
            ((jQuery('#documentoRecepBean').find('#esDocRec')).parent()).find('button').text(data.deEsDocRec);
            jQuery('#documentoRecepBean').find('#esDocRec').val(pEsDocRec);
            if(accion==="2"){
              regresarRecepDocumAdm();  
            }else if(accion==="1" || accion==="0"){
              fn_cargaToolBarRec();  
              alert_Sucess("Recepción: ","Transacción completada con Exito.");  
              jQuery('#documentoRecepBean').find('#nuCorDes').val(data.nuCorDes);  
              fu_inicializarRecepDocumAdm(pEsDocRec,'1');
            }else{
              fn_cargaToolBarRec();    
              alert_Sucess("Recepción: ","Transacción completada con Exito.");    
              fu_inicializarRecepDocumAdm(pEsDocRec,'1');  
            }
            jQuery('#envDocumentoRecepBean').val("0");
        }else{
            if(accion==="2"){
                alert_Info("Recepcion: ",data.deRespuesta); 
            }else{
                alert_Danger("Recepcion: ",data.deRespuesta); 
            }
        }
    }
}


function fu_eventoTablaRecepDocAdm(){
var oTable;
    oTable = $('#myTableFixed').dataTable( {
            "bPaginate": false,
            "bFilter": false,
            "bSort": true,
            "bInfo": false,
            "bAutoWidth": true,
            "bDestroy": true,           
            "sScrollY": "470px",
            "bScrollCollapse": false,           
            "oLanguage": {
                "sZeroRecords": "No se encuentran registros.",
                //"sInfo": "Registros: _TOTAL_ ",
                "sInfoEmpty": ""
            },             
            "aoColumns": [
                                {"bSortable": false},
                                {"bSortable": false},
                                {"bSortable": false},
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
    } );         
    
        jQuery.fn.dataTableExt.oSort['fecha-asc']  = function(a,b) {
            var ukDatea = a.split('/');
            var ukDateb = b.split('/');

            var x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
            var y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;

            return ((x < y) ? -1 : ((x > y) ?  1 : 0));
        };
        
        jQuery.fn.dataTableExt.oSort['fecha-desc'] = function(a,b) {
            var ukDatea = a.split('/');
            var ukDateb = b.split('/');

            var x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
            var y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;

            return ((x < y) ? 1 : ((x > y) ?  -1 : 0));
        };      
    //$("#myTableFixed thead tr").addClass("ui-datatable-fixed-scrollable-header ui-state-default")
    //$("#myTableFixed tbody tr").addClass("bx_sb ui-datatable-fixed-scrollable-body ui-datatable-fixed-data");
    function showdivToolTip(elemento,text)
    {
        $('#divflotante').html(text);

        var x=elemento.left;
        var y=elemento.top + 24;
        $("#divflotante").css({left:x,top:y});

        //document.getElementById('divflotante').style.top = elemento.top + 24;
        //document.getElementById('divflotante').style.left = elemento.left;		
        document.getElementById('divflotante').style.display='block';

        return;
    }
    $("#myTableFixed tbody td").hover(
        function(){    
            $(this).attr('id','divtitlemostrar');
            var index = $(this).index();
            if(index === 8 || index === 11 || index === 12){
                showdivToolTip($('#divtitlemostrar').position(),$(this).html());
            }
        },  
        function(){
            $('#divtitlemostrar').removeAttr('id');
            $('#divflotante').hide();
        }			
    );
    $("#myTableFixed tbody tr").click( function( e ) {
            if ( $(this).hasClass('row_selected') ) {
                    //$(this).removeClass('row_selected');
                    //jQuery('#txtpnuAnn').val("");
            }
            else {
                    oTable.$('tr.row_selected').removeClass('row_selected');
                    $(this).addClass('row_selected');

                    /*obtiene datos*/
                    //alert($(this).children('td')[12].innerHTML);
                    //jQuery('#txtTextIndexSelect').val("0");
                    if(typeof($(this).children('td')[1]) !== "undefined"){
                        jQuery('#txtpnuAnn').val($(this).children('td')[1].innerHTML);
                        jQuery('#txtpnuEmi').val($(this).children('td')[2].innerHTML);
                        jQuery('#txtptiCap').val($(this).children('td')[3].innerHTML);
                        jQuery('#txtpnuDes').val($(this).children('td')[4].innerHTML);
                        jQuery('#txtCoEstadoDoc').val($(this).children('td')[16].innerHTML);//Hermes - 28/05/2019
                        pnumFilaSelect = $(this).index();
                    }
                    //editarDocumentoRecep($(this).children('td')[12].innerHTML,$(this).children('td')[13].innerHTML,$(this).children('td')[15].innerHTML);
                    /* comentado xq los botones de ver documento y ver anexos, ya se encuentran el la tabla
                    var sData = $(this).find('td');
                    passArrayToHtmlTabla("txtTextIndexSelect",sData);*/


            }
    });
    $("#myTableFixed tbody tr").dblclick(function () {
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

//function fu_preEjecutaOpcion(idOpc,url){
   //ejecutaOpcion(idOpc, url);
   //changeTipoBusqRecepDocuAdm("0");
//}

function fu_anularDocumentoRecepcion(){
    var vEsDocRec=jQuery('#documentoRecepBean').find('#esDocRec').val();
    if(vEsDocRec!=="" && vEsDocRec!=="0"){
//        if (confirm('¿ Seguro de anular la Recepción ?')){
//              fu_recibirDocumentoAdm("2");  
                bootbox.dialog({
                    message: " <h5>¿ Seguro de anular la Recepción ?</h5>",
                    buttons: {
                        SI: {
                            label: "SI",
                            className: "btn-primary",
                            callback: function() {
                                fu_recibirDocumentoAdm("2");  
                            }                        
                        },
                        NO: {
                            label: "NO",
                            className: "btn-default"
                        }
                    }
                });    
//        }
    }
    return;
}

/**
 * funcion que habilita o deshabilita las propiedades del Documento 
 * de acuerdo a su estado.
 * @param {type} tModulo --> emision 00, recepcion 01
 * @param {type} estadoDocAdm --> estado del documento
 * @returns void
 */
function fu_cargaEdicionDocAdm(tModulo,estadoDocAdm){
    console.log("modulo :"+ tModulo +" estado :" + estadoDocAdm);
    var vDocEstMsj = $('#docEstadoMsj').val();/*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
    if(tModulo === "00"){//Emision
        jQuery('#divEmitirDoc').hide();
        var btnEmitirDoc = jQuery('#divEmitirDoc').find('button').get(0);
       if(typeof btnEmitirDoc != "undefined")
       {
        btnEmitirDoc.removeAttribute('onclick');
        }
        var btnAux = jQuery('#estDocEmiAdm').find('button').get(0);
        if(typeof btnAux != "undefined")
       {
        btnAux.removeAttribute('onclick');
        }
        jQuery('#btnAnulaDocEmi').hide();
        jQuery('#estDocEmiAdm').removeClass('btn-group');
        jQuery('#estDocEmiAdm').find('button').last().hide();
        var objSpan = jQuery('#estDocEmiAdm').find('button').first().find('span');
        if(objSpan.hasClass('glyphicon glyphicon-ok')){
            objSpan.removeClass('glyphicon glyphicon-ok');
        }
        if(estadoDocAdm === "5" || estadoDocAdm === "7"){//en proyecto
          jQuery('#coDepEmi').removeProp('disabled');
          jQuery('#coDepEmi').removeAttr('disabled');
          jQuery('#coLocEmi').removeProp('disabled');
          jQuery('#coLocEmi').removeAttr('disabled');
          jQuery('#deEmpEmi').removeProp('readonly');
          jQuery('#deEmpEmi').removeAttr('readonly');
          jQuery('#deEmpRes').removeProp('readonly');
          jQuery('#deEmpRes').removeAttr('readonly');
          jQuery('#coTipDocAdm').removeProp('disabled');
          jQuery('#coTipDocAdm').removeAttr('disabled');
          jQuery('#inPlaAte').removeProp('disabled');/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
          jQuery('#inPlaAte').removeAttr('disabled');/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
          jQuery('#fePlaAte').removeProp('disabled');/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
          jQuery('#fePlaAte').removeAttr('disabled');/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
          jQuery('#deAsu').removeProp('readonly');
          jQuery('#deAsu').removeAttr('readonly');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').first().removeProp('readonly');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').first().removeAttr('readonly');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').last().removeProp('readonly');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').last().removeAttr('readonly');
          if(estadoDocAdm === "5"){
              jQuery('#nuDocEmi').prop('readonly','true');
              jQuery('#nuDocEmi').attr('readonly','true');
              jQuery('#ullsEstDocEmiAdm').html('');
              jQuery('#ullsEstDocEmiAdm').append('<li><a href="#" onclick="fu_changeEstadoDocEmiAdm(\'7\');">PARA DESPACHO</a></li>');
              jQuery('#estDocEmiAdm').find('button').first().text('EN PROYECTO');
              
                jQuery("#tblPersVoBoDocAdm").find("input,textarea").removeProp("readonly");
                jQuery("#tblPersVoBoDocAdm").find("input,textarea").removeAttr("readonly");
                jQuery("#tblPersVoBoDocAdm").find("select,input[type=radio]").removeProp("disabled");
                jQuery("#tblPersVoBoDocAdm").find("select,input[type=radio]").removeAttr("disabled");
                jQuery("#tblPersVoBoDocAdm tbody tr").each(function(index,row){
                  if(index > 0){
                     $(row).find("button").first().show();
                  }
                });
                jQuery('#tblRefEmiDocAdm').parents(':eq(4)').find('td').first().show();
          }else{
              //jQuery('#nuDocEmi').removeProp('disabled');
              //btnAux.setAttribute('onclick','fu_changeEstadoDocEmiAdm(\'0\');');
              //showBtnEnviarDocAdm();
              jQuery('#nuDocEmi').removeProp('readonly');
              jQuery('#nuDocEmi').removeAttr('readonly');
              jQuery('#ullsEstDocEmiAdm').html('');
              jQuery('#ullsEstDocEmiAdm').append('<li><a href="#" onclick="fu_changeEstadoDocEmiAdm(\'5\');">EN PROYECTO</a></li>');
              jQuery('#estDocEmiAdm').find('button').first().html("<span/>  PARA DESPACHO");              
              //(jQuery('#estDocEmiAdm').find('button').first().find('span')).addClass('glyphicon glyphicon-ok');
          }
          if(!jQuery('#estDocEmiAdm').hasClass('btn-group')){
             jQuery('#estDocEmiAdm').addClass('btn-group');          
          }
          jQuery('#estDocEmiAdm').find('button').last().show();
//          jQuery('#feEmiCorta').prop('readonly','true');
          jQuery('#nuDiaAte').removeProp('readonly');
          jQuery('#nuDiaAte').removeAttr('readonly');
          
          jQuery("#tblRefEmiDocAdm").find("input,textarea").removeProp("readonly");
          jQuery("#tblRefEmiDocAdm").find("input,textarea").removeAttr("readonly");
          jQuery("#tblRefEmiDocAdm").find("select,input[type=radio]").removeProp("disabled");
          jQuery("#tblRefEmiDocAdm").find("select,input[type=radio]").removeAttr("disabled");
          jQuery("#tblRefEmiDocAdm tbody tr").each(function(index,row){
            if(index > 0){
               $(row).find("button").first().show();
            }
          });
          jQuery('#btnGrabaDocEmi').show();
          jQuery('#btnAnulaDocEmi').show();
          
          jQuery('#tblRefEmiDocAdm').parents(':eq(4)').find('td').first().show();
          if(jQuery("#sTipoDestinatario").val()==="01"){
            jQuery("#divMuestraOpcInstitu").show();
          }
          jQuery("#tblDestEmiDocAdm").find("select,button").removeProp("disabled");
          jQuery("#tblDestEmiDocAdm").find("select,button").removeAttr("disabled");
          jQuery("#tblDestEmiDocAdm").find("input,textarea").removeProp("readonly");          
          jQuery("#tblDestEmiDocAdm").find("input,textarea").removeAttr("readonly");          
          
          jQuery("#tblDestEmiDocAdmOtro").find("select,button").first().removeProp("disabled");
          jQuery("#tblDestEmiDocAdmOtro").find("input,textarea").first().removeProp("readonly");     
          
          jQuery("#tblDestEmiDocAdmCiudadano").find("select,button").first().removeProp("disabled");
          jQuery("#tblDestEmiDocAdmCiudadano").find("input,textarea").first().removeProp("readonly");     
          
          jQuery("#tblDestEmiDocAdmPersJuri").find("select,button").first().removeProp("disabled");
          jQuery("#tblDestEmiDocAdmPersJuri").find("input,textarea").first().removeProp("readonly");    
          
          jQuery("#tblDestEmiDocAdmOtro").find("select,button").first().removeAttr("disabled");
          jQuery("#tblDestEmiDocAdmOtro").find("input,textarea").first().removeAttr("readonly");     
          
          jQuery("#tblDestEmiDocAdmCiudadano").find("select,button").first().removeAttr("disabled");
          jQuery("#tblDestEmiDocAdmCiudadano").find("input,textarea").first().removeAttr("readonly");     
          
          jQuery("#tblDestEmiDocAdmPersJuri").find("select,button").first().removeAttr("disabled");
          jQuery("#tblDestEmiDocAdmPersJuri").find("input,textarea").first().removeAttr("readonly");           
          
          jQuery('#tblDestEmiDocAdm').parents(':eq(4)').find('td').first().show();
          jQuery('#tblDestEmiDocAdmOtro').parents(':eq(4)').find('td').first().show();
          jQuery('#tblDestEmiDocAdmCiudadano').parents(':eq(4)').find('td').first().show();
          jQuery('#tblDestEmiDocAdmPersJuri').parents(':eq(4)').find('td').first().show();
          
          (jQuery('#deEmpRes').parent()).find('button').show();
          if(!jQuery('#deEmpRes').hasClass('inp-xs-grup')){
            jQuery('#deEmpRes').addClass('inp-xs-grup');          
          }
         
          (jQuery('#deEmpEmi').parent()).find('button').show();
          if(!jQuery('#deEmpEmi').hasClass('inp-xs-grup')){
            jQuery('#deEmpEmi').addClass('inp-xs-grup');          
          }
            /*[HPB] 02/02/21 Orden de trabajo*/
            var coTipoDocumento = jQuery('#documentoEmiBean').find('#coTipDocAdm').val();
            var btnTablaDestEmiDocAdmInstitu = jQuery('#divtablaDestEmiDocAdmIntitu').find('button').get(2);
            var inpTablaDestEmiDocAdmInstitu = jQuery('#divtablaDestEmiDocAdmIntitu').find('input').get(4);
            if(coTipoDocumento==="340"){
                console.log('ORDEN DE TRABAJO');
                $("#accordion *").prop('disabled',true);
                jQuery('#nuDiaAte').prop('readonly','true');
                jQuery('#nuDiaAte').attr('readonly','true'); 
                $("#deObsDoc").prop('disabled', true);

                $('#sTipoDestinatario')
                .empty()
                .append('<option selected="selected" value="01">INSTITUCION</option>');
      
                btnTablaDestEmiDocAdmInstitu.setAttribute('onclick','fn_buscaDependenciaDestEmitblOT(this);');
                inpTablaDestEmiDocAdmInstitu.setAttribute('onkeyup','fu_cambioTxtEmiDoc(this);fu_FiltrarTecladoCadenaFunParamOT(event, false, public_apenom,this);');
                $("#divtablaDestEmiDocAdmIntitu").show(); 
            }
            /*[HPB] 02/02/21 Orden de trabajo*/
            /* [HPB] Inicio 11/12/23 OS-0001287-2023 Implementar SubTipo para documentos tipo PAPELETA */
            /*
            if(coTipoDocumento==="319"){
                $("#coSubTipDocAdm").removeAttr('disabled');
            }else{
                $("#coSubTipDocAdm").attr('disabled', 'disabled');
            }
            */
            /* [HPB] Fin 11/12/23 OS-0001287-2023 Implementar SubTipo para documentos tipo PAPELETA */
        }else{
          jQuery('#coDepEmi').prop('disabled','true');
          jQuery('#coLocEmi').prop('disabled','true');
          jQuery('#deEmpEmi').prop('readonly','true');
          jQuery('#deEmpRes').prop('readonly','true');
          jQuery('#coTipDocAdm').prop('disabled','true');
          jQuery('#fePlaAte').prop('disabled', 'true');/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
          jQuery('#inPlaAte').prop('disabled', 'true');/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
          jQuery('#deAsu').prop('readonly','true');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').first().prop('readonly','true');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').last().prop('readonly','true');
          
          jQuery('#coDepEmi').attr('disabled','true');
          jQuery('#coLocEmi').attr('disabled','true');
          jQuery('#deEmpEmi').attr('readonly','true');
          jQuery('#deEmpRes').attr('readonly','true');
          jQuery('#coTipDocAdm').attr('disabled','true');
          jQuery('#fePlaAte').attr('disabled','true');/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
          jQuery('#inPlaAte').attr('disabled','true');/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
          jQuery('#deAsu').attr('readonly','true');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').first().attr('readonly','true');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').last().attr('readonly','true');          
          
          if(estadoDocAdm === "0"){
              jQuery('#btnAnulaDocEmi').show();
              jQuery('#ullsEstDocEmiAdm').html('');
              jQuery('#ullsEstDocEmiAdm').append('<li><a href="#" onclick="fu_changeEstadoDocEmiAdm(\'5\');">EN PROYECTO</a></li>');
              if(!jQuery('#estDocEmiAdm').hasClass('btn-group')){
                jQuery('#estDocEmiAdm').addClass('btn-group');          
              }
              if(vDocEstMsj === "7"){/*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/
                 jQuery('#estDocEmiAdm').find('button').last().hide(); 
              }else{ /*HPB 05/12/2019 - Requerimiento MPV-OBSERVADOS*/             
                jQuery('#estDocEmiAdm').find('button').last().show();
              }
              jQuery('#estDocEmiAdm').find('button').first().text('EMITIDO');
          }
//          jQuery('#feEmiCorta').prop('readonly','true');
          jQuery('#nuDiaAte').prop('readonly','true');
          jQuery('#nuDiaAte').attr('readonly','true');
          
          jQuery("#tblPersVoBoDocAdm").find("input,textarea").prop('readonly','true');
          jQuery("#tblPersVoBoDocAdm").find("select,input[type=radio]").prop('disabled','true');
          
          jQuery("#tblPersVoBoDocAdm").find("input,textarea").attr('readonly','true');
          jQuery("#tblPersVoBoDocAdm").find("select,input[type=radio]").attr('disabled','true');          
          jQuery("#tblPersVoBoDocAdm tbody tr").each(function(index,row){
            if(index > 0){
               $(row).find("button").first().hide();
            }
          });
          jQuery('#tblPersVoBoDocAdm').parents(':eq(4)').find('td').first().hide();
          
          jQuery("#tblRefEmiDocAdm").find("input,textarea").prop('readonly','true');
          jQuery("#tblRefEmiDocAdm").find("select,input[type=radio]").prop('disabled','true');
          
          jQuery("#tblRefEmiDocAdm").find("input,textarea").attr('readonly','true');
          jQuery("#tblRefEmiDocAdm").find("select,input[type=radio]").attr('disabled','true');          
          jQuery("#tblRefEmiDocAdm tbody tr").each(function(index,row){
            if(index > 0){
               $(row).find("button").first().hide();
            }
          });
          jQuery('#btnGrabaDocEmi').hide();
          
          jQuery('#tblRefEmiDocAdm').parents(':eq(4)').find('td').first().hide();
          jQuery("#divMuestraOpcInstitu").hide();
          
          jQuery("#tblDestEmiDocAdm").find("select,button").prop('disabled','true');
          jQuery("#tblDestEmiDocAdm").find("input,textarea").prop('readonly','true');             
          
          jQuery("#tblDestEmiDocAdmOtro").find("select,button").prop('disabled','true');
          jQuery("#tblDestEmiDocAdmOtro").find("input,textarea").prop('readonly','true');             
          
          jQuery("#tblDestEmiDocAdmCiudadano").find("select,button").prop('disabled','true');
          jQuery("#tblDestEmiDocAdmCiudadano").find("input,textarea").prop('readonly','true');  
          
          jQuery("#tblDestEmiDocAdmPersJuri").find("select,button").prop('disabled','true');
          jQuery("#tblDestEmiDocAdmPersJuri").find("input,textarea").prop('readonly','true');      
          
          jQuery("#tblDestEmiDocAdm").find("select,button").attr('disabled','true');
          jQuery("#tblDestEmiDocAdm").find("input,textarea").attr('readonly','true');             
          
          jQuery("#tblDestEmiDocAdmOtro").find("select,button").attr('disabled','true');
          jQuery("#tblDestEmiDocAdmOtro").find("input,textarea").attr('readonly','true');             
          
          jQuery("#tblDestEmiDocAdmCiudadano").find("select,button").attr('disabled','true');
          jQuery("#tblDestEmiDocAdmCiudadano").find("input,textarea").attr('readonly','true');  
          
          jQuery("#tblDestEmiDocAdmPersJuri").find("select,button").attr('disabled','true');
          jQuery("#tblDestEmiDocAdmPersJuri").find("input,textarea").attr('readonly','true');             
          
          jQuery('#tblDestEmiDocAdm').parents(':eq(4)').find('td').first().hide();
          jQuery('#tblDestEmiDocAdmOtro').parents(':eq(4)').find('td').first().hide();
          jQuery('#tblDestEmiDocAdmCiudadano').parents(':eq(4)').find('td').first().hide();
          jQuery('#tblDestEmiDocAdmPersJuri').parents(':eq(4)').find('td').first().hide();
          
          (jQuery('#deEmpRes').parent()).find('button').hide();
          if(jQuery('#deEmpRes').hasClass('inp-xs-grup')){
            jQuery('#deEmpRes').removeClass('inp-xs-grup');          
          }
         
          (jQuery('#deEmpEmi').parent()).find('button').hide();
          if(jQuery('#deEmpEmi').hasClass('inp-xs-grup')){
            jQuery('#deEmpEmi').removeClass('inp-xs-grup');          
          }            
        }
    }else if(tModulo === "01"){//Recepcion
        var arrBtn = jQuery("#divBotonesAccion").find('button');
//        var pexisteDoc = jQuery("#documentoRecepBean").find('#existeDoc').val();
//        var pexisteAnexo = jQuery("#documentoRecepBean").find('#existeAnexo').val();        
        for (var i=0;i<arrBtn.length;i++){
            if(i===0){
                jQuery(arrBtn.get(i)).prop('title','Grabar Cambios del Documento');
                jQuery(arrBtn.get(i)).find('span').removeProp("class");
                jQuery(arrBtn.get(i)).find('span').removeAttr("class");
                jQuery(arrBtn.get(i)).find('span').addClass("glyphicon glyphicon-floppy-disk");
                jQuery(arrBtn.get(i)).find('span').attr("class","glyphicon glyphicon-floppy-disk");
                jQuery(arrBtn.get(i)).html(jQuery(arrBtn.get(i)).find('span'));
                jQuery(arrBtn.get(i)).append(' Grabar');
            }
//            if(i===1){
//                jQuery(arrBtn.get(i)).prop('disabled','true');
//            }
//            if(i===2 && pexisteDoc==="0"){
//                jQuery(arrBtn.get(i)).prop('disabled','true');
//            }
//            if(i===3 && pexisteAnexo==="0"){
//                jQuery(arrBtn.get(i)).prop('disabled','true');
//            }
//            if(i===5){
//                jQuery(arrBtn.get(i)).removeProp('disabled');
//            }
        }
        jQuery("#documentoRecepBean").find('#feRecDoc').removeProp('readonly');
        jQuery("#documentoRecepBean").find('#feRecDoc').removeAttr('readonly');
        if(estadoDocAdm==="0"){//no leido
          for (var i=0;i<arrBtn.length;i++){
            if(i===0){
              jQuery(arrBtn.get(i)).prop('title','Recibir Documento');
              jQuery(arrBtn.get(i)).find('span').removeProp("class");
              jQuery(arrBtn.get(i)).find('span').removeAttr("class");
              jQuery(arrBtn.get(i)).find('span').addClass("glyphicon glyphicon-import");
              jQuery(arrBtn.get(i)).find('span').attr("class","glyphicon glyphicon-import");
              jQuery(arrBtn.get(i)).html(jQuery(arrBtn.get(i)).find('span'));
              jQuery(arrBtn.get(i)).append(' Recibir');                
            }
//            if(i===5){
//                jQuery(arrBtn.get(i)).prop('disabled','true');
//            }              
          }
        jQuery("#documentoRecepBean").find('#feAteDoc').removeProp('readonly');
        jQuery("#documentoRecepBean").find('#feArcDoc').removeProp('readonly');     
        
        jQuery("#documentoRecepBean").find('#feAteDoc').removeAttr('readonly');
        jQuery("#documentoRecepBean").find('#feArcDoc').removeAttr('readonly');            
        }else /*if(estadoDocAdm==="1")*/{//recibido
          for (var i=0;i<arrBtn.length;i++){
              if(i===1){
                jQuery(arrBtn.get(i)).removeProp('disabled');  
                jQuery(arrBtn.get(i)).removeAttr('disabled');  
              }
          }
          (jQuery('#deEmpRec').parent()).find('button').hide();
          if(jQuery('#deEmpRec').hasClass('inp-xs-grup')){
            jQuery('#deEmpRec').removeClass('inp-xs-grup');          
          }
          jQuery('#feRecDoc').prop('readonly','true');
          jQuery('#feRecDoc').attr('readonly','true');
          if(estadoDocAdm==="2"){
              jQuery("#documentoRecepBean").find('#feAteDoc').prop('readonly','true');
              jQuery("#documentoRecepBean").find('#feAteDoc').attr('readonly','true');
          }
          if(estadoDocAdm==="3"){
              jQuery("#documentoRecepBean").find('#feArcDoc').prop('readonly','true');
              jQuery("#documentoRecepBean").find('#feArcDoc').attr('readonly','true');
              var pFeAteDoc = allTrim(jQuery("#documentoRecepBean").find('#feAteDoc').val());
              if(pFeAteDoc!==""){
                 jQuery("#documentoRecepBean").find('#feAteDoc').prop('readonly','true'); 
                 jQuery("#documentoRecepBean").find('#feAteDoc').attr('readonly','true'); 
              }
          }
        }
    }else if(tModulo === "02"){//Emision Personal
        jQuery('#divEmitirDocPersonal').hide();
        var btnEmitirDoc = jQuery('#divEmitirDocPersonal').find('button').get(0);
        btnEmitirDoc.removeAttribute('onclick');        
        var btnAux = jQuery('#estDocEmiAdm').find('button').get(0);
        btnAux.removeAttribute('onclick');
//        jQuery('#btnAnulaDocEmi').hide();
        jQuery('#estDocEmiAdm').removeClass('btn-group');
        jQuery('#estDocEmiAdm').find('button').last().hide();
        var objSpan = jQuery('#estDocEmiAdm').find('button').first().find('span');
        if(objSpan.hasClass('glyphicon glyphicon-ok')){
            objSpan.removeClass('glyphicon glyphicon-ok');
        }
        if(estadoDocAdm === "5"){//en proyecto
          jQuery('#coTipDocAdm').removeProp('disabled');
          jQuery('#deAsu').removeProp('readonly');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').first().removeProp('readonly');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').last().removeProp('readonly');
         jQuery('#coTipDocAdm').removeAttr('disabled');
          jQuery('#deAsu').removeAttr('readonly');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').first().removeAttr('readonly');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').last().removeAttr('readonly');          
//          if(estadoDocAdm === "5"){
              //jQuery('#nuDocEmi').prop('readonly','true');
              jQuery('#ullsEstDocEmiAdm').html('');
//              jQuery('#ullsEstDocEmiAdm').append('<li><a href="#" onclick="fu_changeEstadoDocEmiAdm(\'7\');">PARA DESPACHO</a></li>');
//              jQuery('#estDocEmiAdm').find('button').first().text('EN PROYECTO');
//          }else{
              //jQuery('#nuDocEmi').removeProp('disabled');
              //btnAux.setAttribute('onclick','fu_changeEstadoDocPersonalEmi(\'0\');');
              //showBtnEnviarDocPersonal();
              jQuery('#nuDocEmi').removeProp('readonly');
              jQuery('#nuDocEmi').removeAttr('readonly');
//              jQuery('#ullsEstDocEmiAdm').html('');
//              jQuery('#ullsEstDocEmiAdm').append('<li><a href="#" onclick="fu_changeEstadoDocEmiAdm(\'5\');">EN PROYECTO</a></li>');
              jQuery('#estDocEmiAdm').find('button').first().html("<span/>  EN PROYECTO");              
              //(jQuery('#estDocEmiAdm').find('button').first().find('span')).addClass('glyphicon glyphicon-ok');
//          }
//          if(!jQuery('#estDocEmiAdm').hasClass('btn-group')){
//             jQuery('#estDocEmiAdm').addClass('btn-group');          
//          }
//          jQuery('#estDocEmiAdm').find('button').last().show();
//          jQuery('#feEmiCorta').prop('readonly','true');
          jQuery('#nuDiaAte').removeProp('readonly');
          jQuery('#nuDiaAte').removeAttr('readonly');
          
          jQuery("#tblRefEmiDocAdm").find("input,textarea").removeProp("readonly");
          jQuery("#tblRefEmiDocAdm").find("select,input[type=radio]").removeProp("disabled");
          
          jQuery("#tblRefEmiDocAdm").find("input,textarea").removeAttr("readonly");
          jQuery("#tblRefEmiDocAdm").find("select,input[type=radio]").removeAttr("disabled");          
          jQuery("#tblRefEmiDocAdm tbody tr").each(function(index,row){
            if(index > 0){
               $(row).find("button").first().show();
            }
          });
//          jQuery('#btnGrabaDocEmi').show();
//          jQuery('#btnAnulaDocEmi').show();
          
          jQuery('#tblRefEmiDocAdm').parents(':eq(4)').find('td').first().show();
//          jQuery("#divMuestraOpcInstitu").show();
          jQuery("#tblDestEmiDocAdm").find("select,button").removeProp("disabled");
          jQuery("#tblDestEmiDocAdm").find("input,textarea").removeProp("readonly");          
          
          jQuery("#tblDestEmiDocAdm").find("select,button").removeAttr("disabled");
          jQuery("#tblDestEmiDocAdm").find("input,textarea").removeAttr("readonly");                    
//          
//          jQuery("#tblDestEmiDocAdmOtro").find("select,button").first().removeProp("disabled");
//          jQuery("#tblDestEmiDocAdmOtro").find("input,textarea").first().removeProp("readonly");     
//          
//          jQuery("#tblDestEmiDocAdmCiudadano").find("select,button").first().removeProp("disabled");
//          jQuery("#tblDestEmiDocAdmCiudadano").find("input,textarea").first().removeProp("readonly");     
//          
//          jQuery("#tblDestEmiDocAdmPersJuri").find("select,button").first().removeProp("disabled");
//          jQuery("#tblDestEmiDocAdmPersJuri").find("input,textarea").first().removeProp("readonly");    
          
          jQuery('#tblDestEmiDocAdm').parents(':eq(4)').find('td').first().show();
//          jQuery('#tblDestEmiDocAdmOtro').parents(':eq(4)').find('td').first().show();
//          jQuery('#tblDestEmiDocAdmCiudadano').parents(':eq(4)').find('td').first().show();
//          jQuery('#tblDestEmiDocAdmPersJuri').parents(':eq(4)').find('td').first().show();
//          
//          (jQuery('#deEmpRes').parent()).find('button').show();
//          if(!jQuery('#deEmpRes').hasClass('inp-xs-grup')){
//            jQuery('#deEmpRes').addClass('inp-xs-grup');          
//          }
         
//          (jQuery('#deEmpEmi').parent()).find('button').show();
//          if(!jQuery('#deEmpEmi').hasClass('inp-xs-grup')){
//            jQuery('#deEmpEmi').addClass('inp-xs-grup');          
//          }
        }else{
//          jQuery('#coDepEmi').prop('disabled','true');
//          jQuery('#coLocEmi').prop('disabled','true');
          jQuery('#coTipDocAdm').prop('disabled','true');
          jQuery('#deAsu').prop('readonly','true');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').first().prop('readonly','true');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').last().prop('readonly','true');
          
          jQuery('#coTipDocAdm').attr('disabled','true');
          jQuery('#deAsu').attr('readonly','true');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').first().attr('readonly','true');
          (jQuery('#nuDocEmi').parent()).find('input[type=text]').last().attr('readonly','true');          
          
          if(estadoDocAdm === "0"){
//              jQuery('#btnAnulaDocEmi').show();
              jQuery('#ullsEstDocEmiAdm').html('');
              jQuery('#ullsEstDocEmiAdm').append('<li><a href="#" onclick="fu_changeEstadoDocPersonalEmi(\'5\');">EN PROYECTO</a></li>');
              if(!jQuery('#estDocEmiAdm').hasClass('btn-group')){
                jQuery('#estDocEmiAdm').addClass('btn-group');          
              }
              jQuery('#estDocEmiAdm').find('button').last().show();
              jQuery('#estDocEmiAdm').find('button').first().text('EMITIDO');
          }
//          jQuery('#feEmiCorta').prop('readonly','true');
          jQuery('#nuDiaAte').prop('readonly','true');
          jQuery('#nuDiaAte').attr('readonly','true');
          
          jQuery("#tblRefEmiDocAdm").find("input,textarea").prop('readonly','true');
          jQuery("#tblRefEmiDocAdm").find("select,input[type=radio]").prop('disabled','true');
          
          jQuery("#tblRefEmiDocAdm").find("input,textarea").attr('readonly','true');
          jQuery("#tblRefEmiDocAdm").find("select,input[type=radio]").attr('disabled','true');          
          jQuery("#tblRefEmiDocAdm tbody tr").each(function(index,row){
            if(index > 0){
               $(row).find("button").first().hide();
            }
          });
//          jQuery('#btnGrabaDocEmi').hide();
          
          jQuery('#tblRefEmiDocAdm').parents(':eq(4)').find('td').first().hide();
//          jQuery("#divMuestraOpcInstitu").hide();
          
          jQuery("#tblDestEmiDocAdm").find("select,button").prop('disabled','true');
          jQuery("#tblDestEmiDocAdm").find("input,textarea").prop('readonly','true');             

          jQuery("#tblDestEmiDocAdm").find("select,button").attr('disabled','true');
          jQuery("#tblDestEmiDocAdm").find("input,textarea").attr('readonly','true');                       
//          jQuery("#tblDestEmiDocAdmOtro").find("select,button").prop('disabled','true');
//          jQuery("#tblDestEmiDocAdmOtro").find("input,textarea").prop('readonly','true');             
//          
//          jQuery("#tblDestEmiDocAdmCiudadano").find("select,button").prop('disabled','true');
//          jQuery("#tblDestEmiDocAdmCiudadano").find("input,textarea").prop('readonly','true');  
//          
//          jQuery("#tblDestEmiDocAdmPersJuri").find("select,button").prop('disabled','true');
//          jQuery("#tblDestEmiDocAdmPersJuri").find("input,textarea").prop('readonly','true');            
          
          jQuery('#tblDestEmiDocAdm').parents(':eq(4)').find('td').first().hide();
//          jQuery('#tblDestEmiDocAdmOtro').parents(':eq(4)').find('td').first().hide();
//          jQuery('#tblDestEmiDocAdmCiudadano').parents(':eq(4)').find('td').first().hide();
//          jQuery('#tblDestEmiDocAdmPersJuri').parents(':eq(4)').find('td').first().hide();
          
//          (jQuery('#deEmpRes').parent()).find('button').hide();
//          if(jQuery('#deEmpRes').hasClass('inp-xs-grup')){
//            jQuery('#deEmpRes').removeClass('inp-xs-grup');          
//          }
         
//          (jQuery('#deEmpEmi').parent()).find('button').hide();
//          if(jQuery('#deEmpEmi').hasClass('inp-xs-grup')){
//            jQuery('#deEmpEmi').removeClass('inp-xs-grup');          
//          }            
        }        
    }
}

function fu_atenderDerivarDocumento(){
  var esDocAdmRecepcion = jQuery('#documentoRecepBean').find('#esDocRec').val(); 
  if(esDocAdmRecepcion==="0" || esDocAdmRecepcion==="9"){
     bootbox.alert("No se puede atender Documento.");
  }else{
    var p = new Array();
    p[0] = "pnuAnn=" + jQuery('#documentoRecepBean').find('#nuAnn').val();
    p[1] = "pnuEmi=" + jQuery('#documentoRecepBean').find('#nuEmi').val();
    p[2] = "pnuDes=" + jQuery('#documentoRecepBean').find('#nuDes').val();
    ajaxCall("/srDocumentoAdmRecepcion.do?accion=goVerAtendido",p.join("&"),function(data){
        try{
        var docs = JSON.parse(data);
        if(typeof(docs)!=="undefined" && typeof(docs.retval)!=='undefined' && docs.retval!==""){
            if(docs.retval ==="OK"){
              animacionventanaRecepDoc();
              fu_atenderDerivarDocumento_OK();
              //alert("");
//              animacionventanaRecepDoc();
//              setTimeout(function(){fu_atenderDerivarDocumento_OK();}, 300);
              
            }else{
              fu_consultaAtendido();  
            }
        }
        }catch(ex){
           bootbox.alert("Fallo en abrir el documento.");
        }        
    }, 'text', false, false, "POST");              
  }
}

function animacionventanaRecepDoc(){
    var selector=$("#divWorkPlaceRecepDocumAdmin").find(".ui-panel");
    var auxClase = selector.attr("class");
    auxClase = auxClase + " aniEnviar";
    selector.attr("class", auxClase);
    //Espero que la animacion termine
//    $("#divWorkPlaceRecepDocumAdmin").find(".ui-panel").one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e) {
//        fu_atenderDerivarDocumento_OK();
//    });
    
}
function fu_consultaAtendido(){
    var p = new Array();
    p[0] = "pnuAnn=" + jQuery('#documentoRecepBean').find('#nuAnn').val();
    p[1] = "pnuEmi=" + jQuery('#documentoRecepBean').find('#nuEmi').val();
    p[2] = "pnuDes=" + jQuery('#documentoRecepBean').find('#nuDes').val();
    ajaxCall("/srDocumentoAdmRecepcion.do?accion=goConsultaAtendido",p.join("&"),function(data){
        if(data !== null)
        {
            $("body").append(data);
        }        
    }, 'text', false, false, "POST");              
}


function fu_continuarCreacionDocumento(){
    removeDomId('windowAtendido');
    animacionventanaRecepDoc();
    fu_atenderDerivarDocumento_OK();
}

function fu_atenderDerivarDocumento_OK(){
  var esDocAdmRecepcion = jQuery('#documentoRecepBean').find('#esDocRec').val(); 
  if(esDocAdmRecepcion==="0" || esDocAdmRecepcion==="9"){
     bootbox.alert("No se puede atender Documento.");
  }else{
      
    ajaxCall("/srDocumentoAdmEmision.do?accion=goNuevoDocumentoEmiAtender",$('#documentoRecepBean').serialize(),function(data){
            refreshScript("divWorkPlaceRecepDocumAdmin", data);
            fn_cargaToolBarEmi();
    }, 'text', false, false, "POST");            
  }
}


function fu_inicializarRecepDocumAdm(sEstadoDocAdm,pcargado){
    if(pcargado==="0"){
//        jQuery('#divDatepickRecepDoc').datetimepicker({
//          language: 'en',
//          pick12HourFormat: true
//        });

        jQuery('#documentoRecepBean').find('#dePro').showBigTextBox();
//        jQuery('#divDatepickAteDoc').datetimepicker({
//          language: 'en',
//          pick12HourFormat: true
//        });    
//        jQuery('#divDatepickArchDoc').datetimepicker({
//          language: 'en',
//          pick12HourFormat: true
//        });
        jQuery('#documentoRecepBean').find('#deEmpRec').change(function(){
            jQuery('#envDocumentoRecepBean').val("1");
        });
        jQuery('#documentoRecepBean').find('#feRecDoc').change(function(){
            jQuery('#envDocumentoRecepBean').val("1");
        });   
        jQuery('#documentoRecepBean').find('#deAne').change(function(){
            jQuery('#envDocumentoRecepBean').val("1");
        });     
        jQuery('#documentoRecepBean').find('#feAteDoc').change(function(){
            jQuery('#envDocumentoRecepBean').val("1");
        });
        jQuery('#documentoRecepBean').find('#feArcDoc').change(function(){
            jQuery('#envDocumentoRecepBean').val("1");
        });        
    }
    fu_cargaEdicionDocAdm("01",sEstadoDocAdm);
}

function fu_validaFechaRecDoc(){
    var vReturn="0";
    var vFeRecDoc = jQuery('#documentoRecepBean').find('#feRecDoc').val();
    var lFeRecDoc = vFeRecDoc.length;
    if(lFeRecDoc>=10){
        var fechaValida = valFecha(vFeRecDoc.substr(0,10));
        if(fechaValida===true){
            var vresult=fu_validaComparaFechaRecDoc(moment(vFeRecDoc,"DD/MM/YYYY HH:mm"),moment(jQuery('#documentoRecepBean').find('#feEmiCorta').val(),"DD/MM/YYYY HH:mm"));
            if(vresult!== "1"){
              bootbox.alert(vresult);
               vReturn="0";
            }else{
                vReturn="1";
            }
        }else {
           bootbox.alert('Fecha Recepción no es válida.');
            vReturn="0";
        }
    }else{
     bootbox.alert('Fecha Recepción no es válida.');  
      vReturn="0";
    }
    return vReturn;
}

function fu_validaFechaRecepDocAdm(){
    var vResult=false;
    var noForm='documentoRecepBean';
    var vfechaRecDoc=allTrim(jQuery('#'+noForm).find('#feRecDoc').val());
    if(!!vfechaRecDoc){
        if(moment(vfechaRecDoc, "DD/MM/YYYY HH:mm").isValid()){
//            jQuery('#'+noForm).find("#feRecDoc").val(moment(vfechaRecDoc, ["DD","DD/MM","DD/MM/YY","DD/MM/YYYY"])
//                    .hour(moment().hour()).minute(moment().minute())
//                    .format("DD/MM/YYYY HH:mm"));   
            var fecha=moment(vfechaRecDoc, ["DD","DD/MM","DD/MM/YY","DD/MM/YY HH:mm","DD/MM/YYYY","DD/MM/YYYY HH:mm"]); 
            if(fecha.isValid()){
                if(fecha.hour()===0){
                    fecha.hour(moment().hour());
                }
                if(fecha.minute()===0){
                    fecha.minute(moment().minute());
                }
                jQuery('#'+noForm).find("#feRecDoc").val(moment(fecha,"DD/MM/YYYY HH:mm").format("DD/MM/YYYY HH:mm"));
            }            
            if(fu_validaFechaRecDoc()==="1"){
                vResult=true;                
            }else{
                vResult=false;
            }
        }else{
           bootbox.alert('Fecha Recepción no es válida.');            
            vResult=false;
        }         
    }else{
       bootbox.alert('Ingrese fecha Recepción.');
        vResult=false;
    }
    return vResult;      
}

function fu_validaComparaFechaRecDoc(dfeRec,dfeEmi){
    var vRetorna="1";
    if(dfeRec.isBefore(dfeEmi)){
        vRetorna="Fecha recepción debe ser mayor a la Emisión.";
    }
    return vRetorna;
}

function fu_preValidaFechaAteDoc(){
    return fn_validaFechaAteDocAdm(true);
}

function fn_validaFechaAteDocAdm(callJsp){
    var vResult=false;
    var noForm='documentoRecepBean';
    var vfechaAteDoc=allTrim(jQuery('#'+noForm).find('#feAteDoc').val());
    if(!!vfechaAteDoc){
        if(moment(vfechaAteDoc, "DD/MM/YYYY HH:mm").isValid()){
//            jQuery('#'+noForm).find("#feAteDoc").val(moment(vfechaAteDoc, ["DD","DD/MM","DD/MM/YY","DD/MM/YYYY"])
//                    .hour(moment().hour()).minute(moment().minute())
//                    .format("DD/MM/YYYY HH:mm"));
//            jQuery('#envDocumentoRecepBean').val("1");
            var fecha=moment(vfechaAteDoc, ["DD","DD/MM","DD/MM/YY","DD/MM/YY HH:mm","DD/MM/YYYY","DD/MM/YYYY HH:mm"]); 
            if(fecha.isValid()){
                if(fecha.hour()===0){
                    fecha.hour(moment().hour());
                }
                if(fecha.minute()===0){
                    fecha.minute(moment().minute());
                }
                jQuery('#'+noForm).find("#feAteDoc").val(moment(fecha,"DD/MM/YYYY HH:mm").format("DD/MM/YYYY HH:mm"));
                jQuery('#envDocumentoRecepBean').val("1");
            } 
            
            if(fu_validaFechaAteDoc(callJsp)==="1"){
                vResult=true;                
            }else{
                vResult=false;
            }            
        }else{
           bootbox.alert('Fecha Atención no es válida.');
            vResult=false;
        }         
    }else{
        vResult=true;
    }
    return vResult;    
}

function fu_validaFechaAteDoc(callJsp){
    var vResult = fu_validaFechaRecepDocAdm();
    if(vResult){
        var vReturn="0";
        var vFeAteDoc = jQuery('#documentoRecepBean').find('#feAteDoc').val();
        var lFeAteDoc = vFeAteDoc.length;
        if(lFeAteDoc>=10){
            var fechaValida = valFecha(vFeAteDoc.substr(0,10));
            if(fechaValida){
                var vresult=fu_validaComparaFechaAteDoc(moment(vFeAteDoc,"DD/MM/YYYY HH:mm"),moment(jQuery('#documentoRecepBean').find('#feRecDoc').val(),"DD/MM/YYYY HH:mm"));
                if(vresult!== "1"){
                  bootbox.alert(vresult);
                   vReturn="0";
                }else{
                    vReturn="1";
                }
            }else {
               bootbox.alert('Fecha Atención no es válida.');
                vReturn="0";
            }
        }
        if(callJsp){
            jQuery('#documentoRecepBean').find('#feArcDoc').focus();
        }
        return vReturn;
    }else{
        jQuery('#documentoRecepBean').find('#feRecDoc').focus();
        return '0';
    }
}

function fu_validaComparaFechaAteDoc(dfeAte,dfeRec){
    var vRetorna="1";
    if(dfeAte.isBefore(dfeRec)){
        vRetorna="Fecha Atención debe ser mayor a la Recepción.";
    }
    return vRetorna;    
}

function fu_preValidaFechaArchDoc(){
    return fn_validaFechaArchDocAdm(true);
}

function fn_validaFechaArchDocAdm(callJsp){
    var vResult=false;
    var noForm='documentoRecepBean';
    var vfechaArchDoc=allTrim(jQuery('#'+noForm).find('#feArcDoc').val());
    if(!!vfechaArchDoc){
        if(moment(vfechaArchDoc, "DD/MM/YYYY HH:mm").isValid()){
//            jQuery('#'+noForm).find("#feArcDoc").val(moment(vfechaArchDoc, ["DD","DD/MM","DD/MM/YY","DD/MM/YYYY"])
//                    .hour(moment().hour()).minute(moment().minute())
//                    .format("DD/MM/YYYY HH:mm"));
//            jQuery('#envDocumentoRecepBean').val("1");
            var fecha=moment(vfechaArchDoc, ["DD","DD/MM","DD/MM/YY","DD/MM/YY HH:mm","DD/MM/YYYY","DD/MM/YYYY HH:mm"]); 
            if(fecha.isValid()){
                if(fecha.hour()===0){
                    fecha.hour(moment().hour());
                }
                if(fecha.minute()===0){
                    fecha.minute(moment().minute());
                }
                jQuery('#'+noForm).find("#feArcDoc").val(moment(fecha,"DD/MM/YYYY HH:mm").format("DD/MM/YYYY HH:mm"));
                jQuery('#envDocumentoRecepBean').val("1");
            } 
            
            if(fu_validaFechaArchDoc(callJsp)==="1"){
                vResult=true;                
            }else{
                vResult=false;
            }            
        }else{
           bootbox.alert('Fecha Archivamiento no es válida.');            
            vResult=false;
        }         
    }else{
        vResult=true;
    }
    return vResult;    
}

function fu_validaFechaArchDoc(callJsp){
    var vResult = fu_validaFechaRecepDocAdm();
    if(vResult){
        var vReturn="0";
        var vFeArchDoc = jQuery('#documentoRecepBean').find('#feArcDoc').val();
        var lFeArchDoc = vFeArchDoc.length;
        if(lFeArchDoc>=10){
            var fechaValida = valFecha(vFeArchDoc.substr(0,10));
            if(fechaValida){
                var vresult=fu_validaComparaFechaArchDoc(moment(vFeArchDoc,"DD/MM/YYYY HH:mm"),moment(jQuery('#documentoRecepBean').find('#feRecDoc').val(),"DD/MM/YYYY HH:mm"));
                if(vresult!== "1"){
                  bootbox.alert(vresult);
                   vReturn="0";
                }else{
                    vReturn="1";
                }
            }else {
               bootbox.alert('Fecha Archivamiento no es válida.');
                vReturn="0";
            }
        }
        if(callJsp){
           jQuery('#documentoRecepBean').find('#deAne').focus(); 
        }
        return vReturn;
    }else{
        jQuery('#documentoRecepBean').find('#feRecDoc').focus();
        return '0';
    }
}

function fu_validaComparaFechaArchDoc(dfeArc,dfeRec){
    var vRetorna="1";
    if(dfeArc.isBefore(dfeRec)){
        vRetorna="Fecha Archivamiento debe ser mayor a la Recepción.";
    }
    return vRetorna;     
}

function fu_validaFechaAteArchDoc(){
    var vRetorna=false;
    var vFechaAte=allTrim(jQuery('#documentoRecepBean').find('#feAteDoc').val());
    var vFechaArch=allTrim(jQuery('#documentoRecepBean').find('#feArcDoc').val());       
    if(!!vFechaAte || !!vFechaArch){
        if(!!vFechaAte){
            vRetorna=fn_validaFechaAteDocAdm(false);
            if(vRetorna && !!vFechaArch){
                vRetorna=fn_validaFechaArchDocAdm(false);
            }
        }else{
            vRetorna=fn_validaFechaArchDocAdm(false);
        }
    }else{
        vRetorna=true;
    }
    return vRetorna;
}


function fu_atenderDerivarDocumentoPersonal(){
  var esDocAdmRecepcion = jQuery('#documentoRecepBean').find('#esDocRec').val();
  if(esDocAdmRecepcion==="0" || esDocAdmRecepcion==="9"){
     bootbox.alert("No se puede atender Documento.");
  }else{
    ajaxCall("/srDocumentoEmisionPersonal.do?accion=goNuevoDocumentoEmiAtender",$('#documentoRecepBean').serialize(),function(data){
            refreshScript("divWorkPlaceRecepDocumAdmin", data);
            fn_cargaToolBarEmiPersonal();
    }, 'text', false, false, "POST");            
  }    
}

function upperCaseBuscarRecepDocAdmBean(){
    jQuery('#buscarDocumentoRecepBean').find('#sNroDocumento').val(fu_getValorUpperCase(jQuery('#buscarDocumentoRecepBean').find('#sNroDocumento').val()));
    jQuery('#buscarDocumentoRecepBean').find('#sBuscNroExpediente').val(fu_getValorUpperCase(jQuery('#buscarDocumentoRecepBean').find('#sBuscNroExpediente').val()));
    jQuery('#buscarDocumentoRecepBean').find('#sBuscAsunto').val(fu_getValorUpperCase(jQuery('#buscarDocumentoRecepBean').find('#sBuscAsunto').val()));
}

function setAnnioNoIncludeFiltroRecep(){
    var valRetorno = "1";
    fu_obtenerEsFiltroFecha('buscarDocumentoRecepBean');
    var pEsFiltroFecha = jQuery('#buscarDocumentoRecepBean').find("#esFiltroFecha").val();//"2" solo año ,"3" año mes,"1" rango fechas
    if(pEsFiltroFecha==="1" || pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
       if(pEsFiltroFecha==="2" || pEsFiltroFecha==="3"){
            var pAnnio = jQuery('#buscarDocumentoRecepBean').find('#sCoAnnio').val();
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
                    jQuery('#buscarDocumentoRecepBean').find('#sCoAnnio').val(pAnnioBusq);                          
                }                
            }               
        }
    }
    return valRetorno;       
}

function fu_verificarCamposDocRecepcion(vAccion){
    var valRetorno="1";
    if(vAccion==="1"||vAccion==="0"||vAccion==="2"){
        var pdeObservaciones = jQuery('#documentoRecepBean').find('#deAne').val();
        var maxLengthDeObs = jQuery('#documentoRecepBean').find('#deAne').attr('maxlength');
        if(!!pdeObservaciones && !!maxLengthDeObs){
            var nrolinesDeAsu = (pdeObservaciones.match(/\n/g) || []).length;
            if(pdeObservaciones.length+nrolinesDeAsu > maxLengthDeObs){
                valRetorno = "0";
                bootbox.alert("<h5>La Observación Excede el límite de "+maxLengthDeObs+" caracteres.</h5>", function() {
                    jQuery('#documentoRecepBean').find("#deAne").focus();
                });
                jQuery('#documentoRecepBean').find("#deAne").focus();
            }            
        }
    }
    return valRetorno;
}

function fu_atenderDerivarMesaParteDocExterno(){
  var noForm='documentoRecepBean';
  var esDocAdmRecepcion = jQuery('#'+noForm).find('#esDocRec').val(); 
  if(esDocAdmRecepcion==="0" || esDocAdmRecepcion==="9"){
     bootbox.alert("No se puede atender Documento.");
  }else{
    ajaxCall("/srMesaPartes.do?accion=goNewDocumentoExternoAtender",$('#'+noForm).serialize(),function(data){
            refreshScript("divWorkPlaceRecepDocumAdmin", data);
            jQuery('#envExpedienteEmiBean').val("1");
            jQuery('#envDocumentoEmiBean').val("1");
            jQuery('#envRemitenteEmiBean').val("1");            
            fn_cargaToolBarDocExtRecep();
    }, 'text', false, false, "POST");            
  }     
}
function fn_seleccionarEtiqueta(id, elem) {
    var texto = jQuery(elem).text();
    var ico = jQuery(elem).find("span").attr("class");
    jQuery("#btn-etiquetaRecep span:eq(0)").attr("class", ico);
    jQuery("#btn-etiquetaRecep span:eq(1)").text(" " + texto + " ");
    jQuery("#coEtiquetaRec").val(id);
    jQuery('#envDocumentoRecepBean').val("1");//valida el guardar
    if (id === 0) {
        jQuery("#btn-etiquetaRecep").attr("class", "btn btn-default btn dropdown-toggle");
    } else {
        jQuery("#btn-etiquetaRecep").attr("class", "btn btn-warning btn dropdown-toggle");
    }
}
function cambiarEtiqueta(id) {
    var numEtiquetas = $("#divEtiqueta ul li").length;
    if (id >= numEtiquetas || id < 0) {
        return;//pila reventada
    }
    var txt = $("#divEtiqueta ul li:eq(" + id + ")").text();
    var ico = $("#divEtiqueta ul li:eq(" + id + ") a span").attr("class");
    $("#btn-etiquetaRecep span:eq(0)").attr("class", ico);
    $("#btn-etiquetaRecep span:eq(1)").text(" " + txt + " ");
    $("#coEtiquetaRec").val(id);
    jQuery('#envDocumentoRecepBean').val("1");//valida el guardar

    if (parseInt(id) === 0) {
        jQuery("#btn-etiquetaRecep").attr("class", "btn btn-default btn dropdown-toggle");
    } else {
        jQuery("#btn-etiquetaRecep").attr("class", "btn btn-warning btn dropdown-toggle");
    }
}
function fn_seleccionarRecepcionTipoDoc(id, elem) {
    var texto = jQuery(elem).text();
    var ico = jQuery(elem).find("span").attr("class");
    jQuery("#btn-TipoDocRecep span:eq(0)").attr("class", ico);
    jQuery("#btn-TipoDocRecep span:eq(1)").text(" " + texto + " ");
    jQuery("#tiFisicoRec").val(id);
    jQuery('#envDocumentoRecepBean').val("1");//valida el guardar
}
function cambiarRecepcionTipoDoc(id) {
    var num = $("#divTipoDocRecep ul li").length;
    if (id >= num || id < 0) {
        return;//pila reventada
    }
    var txt = $("#divTipoDocRecep ul li:eq(" + id + ")").text();
    var ico = $("#divTipoDocRecep ul li:eq(" + id + ") a span").attr("class");
    $("#btn-TipoDocRecep span:eq(0)").attr("class", ico);
    $("#btn-TipoDocRecep span:eq(1)").text(" " + txt + " ");
    $("#tiFisicoRec").val(id);
    jQuery('#envDocumentoRecepBean').val("1");//valida el guardar
}
function cargarEtiquetasEstadoDocumento() {
    var p = new Array();
    p[0] = "accion=goListaEtiquetas";
    ajaxCall("/srEtiquetaDoc.do", p.join("&"), function(data) {
        var aux = jQuery.parseJSON(data);
        for (var i = 0; i < aux.length; i++) {
            var cod = aux[i].coEst;
            var txt = aux[i].deEst;
            var icono=getIcoEtiqueta(i);
            if (!!!icono) {
                icono = "glyphicon glyphicon-tag";
            }
            var fila = "<li><a href='#' id='" + cod + "' onClick='fn_seleccionarEtiqueta(" + cod + ", this);' ><span class='" + icono + "'><jsp:text/></span> " + txt + " </a></li>";
            jQuery("#divEtiqueta ul").append(fila);
        }
        var idEtiqueta = $("#coEtiquetaRec").val();
        cambiarEtiqueta(idEtiqueta);
        var idEstadoDoc = $("#tiFisicoRec").val();
        if (idEstadoDoc === "1") {
            cambiarRecepcionTipoDoc(idEstadoDoc);
            jQuery('#envDocumentoRecepBean').val("0");//para que no afecte los cambios al guardar
        }
    }, 'text', false, false, "POST");
}
function mostrarListaEtiquetas(idEtiqueta,coDep){
    var p = new Array();
    p[0] = "accion=goInicio";
    p[1] = "idEtiqueta=" + idEtiqueta;
    p[2] = "coDep=" + coDep;
    p[3] = "coBandeja=" + "E"; /*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR */
    ajaxCall("/srDocumentoAdmRecepcion.do", p.join("&"), refreshAppBody, 'text', false, false, "GET");
    hideMenuSio();
}

function fn_iniConsProveedorEditRecDocAdm(){
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
            //alert(evento.which);
            var isFirst = false;
            var indexSelect = -1;
            table.find('tr').each(function(index, row) {
                    if ( $(this).hasClass('row_selected') ) {
                        $(this).removeClass('row_selected');
                    }
                    //var allCells = $(row).find('td');
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
                    fn_setProveedorEditRecDocAdm(prazonSocial,pnuRuc);
                }
            }else if(evento.which==38||evento.which==40){
                //$('#txtConsultaFind').blur();
                indexAux=-1;
                $(nomTbl+' >tbody >tr:visible').first().focus(); 
            }
    };
    $('#txtConsultaFind').keyup(searchOnTable);
    $(nomTbl+" tbody tr").click(function(e) {
        var prazonSocial= $(this).find("td:eq(0)").html();
        var pnuRuc= $(this).find("td:eq(1)").html();
        fn_setProveedorEditRecDocAdm(prazonSocial,pnuRuc);
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
            fn_setProveedorEditRecDocAdm(prazonSocial,pnuRuc);      
        }
        //console.log(indexAux);
        evento.preventDefault();
    });    
}

function fn_setProveedorEditRecDocAdm(prazonSocial,pnuRuc){
    prazonSocial = prazonSocial.replace(/&amp;/g, "&");
    var noForm='buscarDocumentoRecepBean';
    jQuery('#'+noForm).find('#busDescRuc').val(prazonSocial);
    jQuery('#'+noForm).find('#busNumRuc').val(pnuRuc);
    jQuery('#'+noForm).find('#nuRucAux').val(pnuRuc);
    removeDomId('windowConsultaProveedor');
    jQuery('#'+noForm).find('#coTipoPersona').focus();
    jQuery('#envRemitenteEmiBean').val("1");
    
    jQuery('#busResultado').val("1");
        
    return false;
}

function fn_getOtroOrigenRemDocRecAdm()
{    
    var noForm='buscarDocumentoRecepBean';
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
        ajaxCall("/srDocumentoAdmRecepcion.do", p.join("&"), function(data) {
            fn_rptaBuscarRemitenteOtroDocRecAdm(data);
        },'text', false, false, "POST"); 
    }
    return false;
}

function fn_rptaBuscarRemitenteOtroDocRecAdm(XML_AJAX){
    if (XML_AJAX !== null) {
        $("body").append(XML_AJAX);
    }    
}

function fn_iniConsOtroOrigenEmisorAdm(){
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
            //alert(evento.which);
            var isFirst = false;
            var indexSelect = -1;
            table.find('tr').each(function(index, row) {
                    if ( $(this).hasClass('row_selected') ) {
                        $(this).removeClass('row_selected');
                    }
                    //var allCells = $(row).find('td');
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
                    fn_setOtroOrigenEditDocRecAdm(pdesDest,pcodDest);
                }
            }else if(evento.which==38||evento.which==40){
                //$('#txtConsultaFind').blur();
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
        fn_setOtroOrigenEditDocRecAdm(pdesDest,pcodDest);
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
            fn_setOtroOrigenEditDocExtRecBus(pdesDest,pcodDest);    
        }
        //console.log(indexAux);
        evento.preventDefault();
    });    
}

function fn_setOtroOrigenEditDocRecAdm(desDest, codDest){
    desDest = desDest.replace(/&amp;/g, "&");
    var noForm='buscarDocumentoRecepBean';
    jQuery('#'+noForm).find('#busNomOtros').val(desDest);
    jQuery('#'+noForm).find('#busCoOtros').val(codDest);
    removeDomId('windowConsultaOtroOri');
    jQuery('#'+noForm).find('#coTipoPersona').focus();
    jQuery('#envRemitenteEmiBean').val("1");
    
    jQuery('#busResultado').val("1");
    return false;    
}
/*HPB 20/02/2020 - Inicio - Requerimiento Paginación recepcionados*/
function fn_paginationChangeTipoBusqRecepDocuAdmNext(){
    var paginaInicial= $('select[name=cantPaginasRecep]').val();
    var paginaNext = parseInt(paginaInicial) + 1;
    var ultimaPagina = $('select option:last').val();
    
    if(paginaNext <= parseInt(ultimaPagina)){
        $('select[name=cantPaginasRecep]').val(paginaNext);
    }
    fn_changePaginaRecepDocuAdm()
}

function fn_paginationChangeTipoBusqRecepDocuAdmNextLast(){
    var ultimaPagina = $('select option:last').val();    
    $('select[name=cantPaginasRecep]').val(ultimaPagina);
    fn_changePaginaRecepDocuAdm()
}

function fn_paginationChangeTipoBusqRecepDocuAdmPrevius(){
    var paginaInicial= $('select[name=cantPaginasRecep]').val();
    var paginaNext = parseInt(paginaInicial) - 1;
    
    if(parseInt(paginaNext)!=0){
        $('select[name=cantPaginasRecep]').val(paginaNext);
    }
    fn_changePaginaRecepDocuAdm()
}

function fn_paginationChangeTipoBusqRecepDocuAdmPreviusFirts(){
    var PrimeraPagina = $('select option:first').val();
    $('select[name=cantPaginasRecep]').val(PrimeraPagina);
    fn_changePaginaRecepDocuAdm()
}

function fn_changePaginaRecepDocuAdm(){   
  jQuery('#txtPaginaRecepcion').val($('select[name=cantPaginasRecep]').val());
  var tipo = jQuery('#sTipoBusqueda').val();
  changeTipoBusqRecepDocuAdm(tipo);
}
/*HPB 20/02/2020 - Fin - Requerimiento Paginación recepcionados*/
/*[HPB-02/10/20] Inicio - Plazo de Atencion*/
function fnVerLeyendaVencimientoPlazoAtencionRecep() {
    var divLeyenda;
    $("#btnShowLeyendaVenPlazoAtencionRecep")
            .mouseenter(function() {
                var pos = $(this).position();
                var zindex = $(this).zIndex();
                divLeyenda = $("#divLeyendaVenPlazoAtencionRecep").clone().insertAfter(this).css({
                    position: "absolute",
                    top: (pos.top + 37) + "px",
                    left: (pos.left + 50) + "px",
                    "z-index": zindex + 1
                });

                divLeyenda.show();
            })
            .mouseleave(function() {
                if (!!divLeyenda) {
                    divLeyenda.remove();
                }
            });
}

function fn_construyeTablaVencimientoDetalleCuadradosEmiPlazoAtencionRecep() {
    len = function(obj) {
        var L = 0;
        $.each(obj, function(i, elem) {
            L++;
        });
        return L;
    }
    var orden = [0, 1, 2, 3, 4, 5];
    var orden = [0, 4, 1, 2, 3, 5];
    for (var j = 0; j < len(coloresVencimientoPlazoAtencion); j++)
    {
        var label = coloresVencimientoPlazoAtencion[orden[j]].text;
        var color = coloresVencimientoPlazoAtencion[orden[j]].color;
        var descrip = coloresVencimientoPlazoAtencion[orden[j]].descrip;
        var clase = coloresVencimientoPlazoAtencion[orden[j]].cssClass;
        var filaAux = Array();
        filaAux.push("<tr><td><div class='cuadradoVencimiento " + clase + "'>" + label + "</div></td>");
        filaAux.push("<td>" + descrip + "</td>");
        filaAux.push("</tr>");
        var fila = "";
        for (var i = 0; i < filaAux.length; i++) {
            fila = fila + filaAux[i];
        }
        $("#tablaDetVencimientoCuadradosPlazoAtencionRecep").append(fila);
        fila = [];
    }
}
function pintarEstadosPlazoAtencionRecep() {
    var col = 17;
    var col2 = 13;
    $("#myTableFixed tbody tr").each(function() {
        var codVencimiento = $(this).find("td:eq(" + col + ")").text();
        if (!!codVencimiento) {
            codVencimiento = parseInt(codVencimiento);
            var text = coloresVencimientoPlazoAtencion[codVencimiento].text;
            var color = coloresVencimientoPlazoAtencion[codVencimiento].color;
            var clase = coloresVencimientoPlazoAtencion[codVencimiento].cssClass;
            //$(this).find("td:eq(" + col + ")").html(text);
            //$(this).find("td:eq(" + col + ")").css({"background-color": color, "color": "white"});
            $(this).find("td:eq(" + col2 + ")").attr("class", clase);
            //var auxCol = col2 - 1;
            //$(this).find("td:eq(" + auxCol + ")").attr("class", clase);
        } else {
            //seguramente no hay filas en la tabla no hay nada que pintar
        }
    });
}
/*[HPB-02/10/20] Fin - Plazo de Atencion*/