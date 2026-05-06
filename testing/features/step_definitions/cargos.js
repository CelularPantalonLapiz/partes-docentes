const { Given } = require('cucumber');
const request = require('sync-request');
const ctx = require('./ctx'); 

function normalizar(str) {
    return (str || "").replace(/"/g, "").trim().toUpperCase()
        .normalize("NFD").replace(/[\u0300-\u036f]/g, "")
        .replace(/Ñ/g, "N");
}

Given(/^el cargo institucional cuyo (.+) que da título al mismo$/, function (nombre) {
    ctx.url = 'http://backend:8080/cargos';
    ctx.payload = { nombre: nombre.replace(/"/g, "").trim() };
});

Given(/^que es del (.+)$/, function (tipoDesignacion) {
    ctx.payload.tipoDesignacion = tipoDesignacion.replace(/"/g, "").trim().replace(/ /g, '_');
});

Given(/^que tiene una (\d+) con la vigencia (\S+)\s*(\S*)$/, function (carga, desde, hasta) {
    ctx.payload.cargaHoraria = parseInt(carga);
    ctx.payload.fechaDesde = desde;
    ctx.payload.fechaHasta = hasta === "" ? null : hasta;
});

Given(/^que si el tipo es espacio curricular, opcionalmente se asigna a la división(.*)$/, function (resto) {
    const textoLimpio = resto.replace(/"/g, "").trim();
    const match = textoLimpio.match(/(\d+)\s+(\d+)\s+(.+)/);
    
    if (match) {
        let divRes = request('GET', `http://backend:8080/divisiones`, {});
        let divisiones = JSON.parse(divRes.body).data;
        
        let div = divisiones.find(d => 
            d.anio === parseInt(match[1]) &&
            d.numDivision === parseInt(match[2]) &&
            normalizar(d.turno) === normalizar(match[3])
        );
        ctx.payload.division = div ? { id: div.id } : { id: -1 };
    } else {
        ctx.payload.division = null;
    }
});
