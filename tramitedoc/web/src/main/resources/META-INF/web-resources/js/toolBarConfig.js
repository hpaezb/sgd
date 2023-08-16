function fn_cargaToolBarEmi(){
   var pcoEstado = jQuery('#documentoEmiBean').find('#esDocEmi').val();
   var pcoTipoDoc=jQuery('#documentoEmiBean').find('#coTipDocAdm').val();
   var pcoFormato="PDF";
    
    if(pcoEstado!==null){
        if(pcoEstado!=="5" && pcoEstado!=="7" && pcoEstado!=="9" ){
            pcoEstado="0";
        }
    }else{
        pcoEstado="0";
    }
        
    if(pcoTipoDoc!==null && (pcoTipoDoc==="232" || pcoTipoDoc==="304" || pcoTipoDoc==="340")){/*[HPB] 02/02/21 Orden de trabajo*/
        pcoTipoDoc="0";
    }else{
        pcoTipoDoc="1";
    }
        
    
   var p = new Array();    
   p[0] = "accion=goToolEmisionAdm";	    
   p[1] = "pcoEstado=" + pcoEstado;   
   p[2] = "pcoTipoDoc=" + pcoTipoDoc;   
   p[3] = "pcoFormato=" + pcoFormato;   
   
    ajaxCall("/srToolBar.do",p.join("&"),function(data){                
        if(data!==null){
            refreshScript("divToolBarEmiAdm", data);            
        }
         
    },'text', false, true, "GET");         
    
}

function fn_cargaToolBarEmiPersonal(){
   var pcoEstado = jQuery('#documentoPersonalEmiBean').find('#esDocEmi').val();
   var pcoTipoDoc=jQuery('#documentoPersonalEmiBean').find('#coTipDocAdm').val();
   var pcoFormato="PDF";
    
    if(pcoEstado!==null){
        if(pcoEstado!=="5" && pcoEstado!=="7" && pcoEstado!=="9" ){
            pcoEstado="0";
        }
    }else{
        pcoEstado="0";
    }
        
    if(pcoTipoDoc!==null && (pcoTipoDoc==="232" || pcoTipoDoc==="304" )){
        pcoTipoDoc="0";
    }else{
        pcoTipoDoc="1";
    }
        
    
   var p = new Array();    
   p[0] = "accion=goToolEmisionPersonal";	    
   p[1] = "pcoEstado=" + pcoEstado;   
   p[2] = "pcoTipoDoc=" + pcoTipoDoc;   
   p[3] = "pcoFormato=" + pcoFormato;   
   
    ajaxCall("/srToolBar.do",p.join("&"),function(data){                
        if(data!==null){
            refreshScript("divToolBarEmiAdm", data);            
        }
    },'text', false, true, "GET");         
    
}

function fn_cargaToolBarRec(){
   var pcoEstado = jQuery('#documentoRecepBean').find('#esDocRec').val();
   var pcoDepDes = jQuery('#documentoRecepBean').find('#coDepDes').val();
    
    if(pcoEstado==null){
            pcoEstado="0";
     }
        
   var p = new Array();    
   p[0] = "accion=goToolRecepcionAdm";	    
   p[1] = "pcoEstado=" + pcoEstado;   
   p[2] = "pcoDep=" + pcoDepDes;   
   
    ajaxCall("/srToolBar.do",p.join("&"),function(data){                
        if(data!==null){
            refreshScript("divToolBarRecAdm", data);            
        }
    },'text', true, true, "GET");         
    
}

function fn_cargaToolBarEmiAlta(){
   var pcoEstado = jQuery('#documentoEmiBean').find('#esDocEmi').val();
   var pcoTipoDoc=jQuery('#documentoEmiBean').find('#coTipDocAdm').val();
   var pcoFormato="PDF";
    
    if(pcoEstado!==null){
        if(pcoEstado!=="5" && pcoEstado!=="7" && pcoEstado!=="9" ){
            pcoEstado="0";
        }
    }else{
        pcoEstado="0";
    }
        
    if(pcoTipoDoc!==null && (pcoTipoDoc==="232" || pcoTipoDoc==="304" )){
        pcoTipoDoc="0";
    }else{
        pcoTipoDoc="1";
    }
    
   var p = new Array();    
   p[0] = "accion=goToolEmisionAlta";	    
   p[1] = "pcoEstado=" + pcoEstado;   
   p[2] = "pcoTipoDoc=" + pcoTipoDoc;   
   p[3] = "pcoFormato=" + pcoFormato;   
   
    ajaxCall("/srToolBar.do",p.join("&"),function(data){                
        if(data!==null){
            refreshScript("divToolBarEmiAdm", data);            
        }
    },'text', false, true, "GET");         
    
}

function fn_cargaToolBarDocExtRecep(){
   var noForm='documentoExtRecepBean';
   var pcoEstado = jQuery('#'+noForm).find('#coEsDocEmiMp').val();
   /* [HPB] Inicio 26/09/22 OS-0000768-2022 */
   var pcoOrigen = jQuery('#'+noForm).find('#coOriDoc').val();
   /* [HPB] Fin 26/09/22 OS-0000768-2022 */
   if(!!pcoEstado){
       if(pcoEstado!=='5'&&pcoEstado!=='9'&&pcoEstado!=='7'&&pcoEstado!=='A'){/*[HPB-22/07/21] Inicio - Adicionar estado POR SUBSANAR */
        pcoEstado="0";           
       }
   }else{
       pcoEstado="0";           
   } 
   var p = new Array();    
   p[0] = "accion=goToolEmisionDocExt";	    
   p[1] = "pcoEstado=" + pcoEstado;   
   /* [HPB] Inicio 26/09/22 OS-0000768-2022 */
   p[2] = "pcoOrigen=" + pcoOrigen;
   /* [HPB] Fin 26/09/22 OS-0000768-2022 */
    ajaxCall("/srToolBar.do",p.join("&"),function(data){                
        if(data!==null){
            refreshScript("divToolBarEmiDocExt", data);            
        }
    },'text', false, true, "GET");            
}

function fn_cargaToolBarDocVoBo(noForm){
   var pcoEstado = jQuery(noForm).find('#esDocEmi').val();
   var pcoTipoDoc=jQuery(noForm).find('#coTipDocAdm').val();
   var pcoFormato="PDF";
    
    if(pcoEstado!==null){
        if(pcoEstado!=="5" && pcoEstado!=="7" && pcoEstado!=="2" && pcoEstado!=="0"){
            pcoEstado="1";
        }
    }else{
        pcoEstado="1";
    }
        
    if(pcoTipoDoc!==null && (pcoTipoDoc==="232" || pcoTipoDoc==="304" )){
        pcoTipoDoc="0";
    }else{
        pcoTipoDoc="1";
    }
    
   var p = new Array();    
   p[0] = "accion=goToolDocVoBo";	    
   p[1] = "pcoEstado=" + pcoEstado;   
   p[2] = "pcoTipoDoc=" + pcoTipoDoc;   
   p[3] = "pcoFormato=" + pcoFormato;   
    ajaxCall("/srToolBar.do",p.join("&"),function(data){                
        if(data!==null){
            refreshScript("divToolBarDocVobo", data);            
        }
    },'text', false, true, "GET");         
    
}