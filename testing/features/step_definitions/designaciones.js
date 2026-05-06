const { Given } = require('cucumber');
const request = require('sync-request');
const ctx = require('./ctx');

function normalizar(str) {
    return (str || "").replace(/"/g, "").trim().toUpperCase()
        .normalize("NFD").replace(/[\u0300-\u036f]/g, "")
        .replace(/Ñ/g, "N");
}

Given(/^la persona con (\d+) (.+) y (.+)$/, function (dni, nombre, apellido) {
    ctx.url = 'http://backend:8080/designaciones';
    
    let res = request('GET', `http://backend:8080/personas`, {});
    let personas = JSON.parse(res.body).data;
    let personaEncontrada = personas.find(p => p.dni == dni);
    
    if (personaEncontrada) {
        ctx.payload = { 
            persona: { 
                dni: personaEncontrada.dni 
            } 
        };
    } else {
        throw new Error(`La persona con DNI ${dni} no existe.`);
    }
});

Given(/^que se asigna al cargo\s+con tipo de designación (.+) y (.+)$/, function (tipo, nombreCargo) {
    this.tempTipo = tipo.replace(/"/g, "").trim().replace(/ /g, '_');
    this.tempNombreCargo = nombreCargo.replace(/"/g, "").trim();
});

Given(/^si es espacio curricular asignada a la división\s*(.*)$/, function (resto) {
    const texto = resto.replace(/"/g, "").trim();
    
    const match = texto.match(/(\d+)\s+(\d+)\s+(.+)/);
    
    if (match) {
        let res = request('GET', `http://backend:8080/cargos`, {});
        let cargos = JSON.parse(res.body).data;

        let cargoEncontrado = cargos.find(c => {
            let matchBasico = c.nombre === this.tempNombreCargo && c.tipoDesignacion === this.tempTipo;
            return matchBasico && 
                   c.division.anio == parseInt(match[1]) && 
                   c.division.numDivision == parseInt(match[2]) && 
                   normalizar(c.division.turno) === normalizar(match[3]);
        });

        ctx.payload.cargo = { id: cargoEncontrado ? cargoEncontrado.id : -1 };
    } else {
        let res = request('GET', `http://backend:8080/cargos`, {});
        let cargos = JSON.parse(res.body).data;
        
        let cargoEncontrado = cargos.find(c => 
            c.nombre === this.tempNombreCargo && c.tipoDesignacion === this.tempTipo
        );
        
        ctx.payload.cargo = { id: cargoEncontrado ? cargoEncontrado.id : -1 };
    }
});

Given(/^se designa por el período (\S+)\s*(\S*)$/, function (desde, hasta) {
    ctx.payload.situacionRevista = "TITULAR"; 

    ctx.payload.fechaDesde = desde; 
    ctx.payload.fechaHasta = hasta === "" ? null : hasta;
});
