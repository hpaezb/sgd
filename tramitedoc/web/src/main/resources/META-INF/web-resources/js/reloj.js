//Fecha y hora del sistema
let htmlHora = document.getElementById("txtFechaHoraActual").value;

//Fecha y hora de la PC
let current = new Date();
let cDate = (current.getMonth() + 1) + '/' + current.getDate() + '/'+current.getFullYear();
let cTime = current.getHours() + ":" + current.getMinutes() + ":" + current.getSeconds();
let dateTime = cDate + ' ' + cTime;

function diff_minutes(dt2, dt1) {
    var diff =(dt2.getTime() - dt1.getTime()) / 1000;
    diff /= 60;
    return Math.abs(Math.round(diff));
}

let dt1 = new Date(dateTime);
let dt2 = new Date(htmlHora);
let diferenciaHora = diff_minutes(dt1, dt2);

if (diferenciaHora > 3){
    bootbox.alert("La hora del sistema no coincide con la hora actual.");
}