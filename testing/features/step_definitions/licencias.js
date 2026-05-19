const { Given, When, Then } = require('cucumber');
const assert = require('assert');
const request = require('sync-request');
const ctx = require('./ctx');

// Función auxiliar para normalizar textos (evita problemas con tildes y eñes)
function normalizar(str) {
    return (str || "").replace(/"/g, "").trim().toUpperCase()
        .normalize("NFD").replace(/[\u0300-\u036f]/g, "")
        .replace(/Ñ/g, "N");
}

// =============================================================================
// 1. STEPS PARA EL ESQUEMA DE ARTÍCULOS DE LICENCIA
// =============================================================================

Given(/^el docente con DNI (\d+), nombre "([^"]*)" y apellido "([^"]*)"$/, function (dni, nombre, apellido) {
    ctx.url = 'http://backend:8080/licencias';
    
    // Buscamos si la persona existe en el sistema
    let res = request('GET', `http://backend:8080/personas`, {});
    let personas = JSON.parse(res.body).data;
    let personaEncontrada = personas.find(p => p.dni == dni);
    
    if (personaEncontrada) {
        ctx.payload = { 
            persona: { dni: personaEncontrada.dni } 
        };
    } else {
        // Si el test simula un DNI inexistente (ej: Raúl Gutiérrez 99999999)
        ctx.payload = { 
            persona: { dni: parseInt(dni) } 
        };
    }
});

When(/^solicita una licencia artículo "([^"]*)" con descripción "([^"]*)" para el período "([^"]*)" "([^"]*)"$/, function (articulo, descripcion, desde, hasta) {
    // Armamos el cuerpo de la licencia según el diagrama UML
    ctx.payload.articuloLicencia = { articulo: articulo, descripcion: descripcion };
    ctx.payload.pedidoDesde = desde;
    ctx.payload.pedidoHasta = hasta;

    // Ejecutamos la petición POST de inmediato
    const res = request('POST', ctx.url, {
        json: ctx.payload,
        failOnStatusCode: false 
    });

    ctx.response = JSON.parse(res.body);
});

Then(/^debería obtener la siguiente resultado de (\d+) y "([^"]*)"$/, function (status, respuestaEsperada) {
    // Validamos el código de estado HTTP y el mensaje de respuesta de la licencia
    assert.strictEqual(ctx.response.status, parseInt(status));
    assert.strictEqual(ctx.response.message.trim(), respuestaEsperada.trim());
});

// =============================================================================
// 2. STEPS PARA ESCENARIOS DE SUPLENCIAS Y REEMPLAZOS
// =============================================================================

Given(/^que existe la persona$/, function (dataTable) {
    const row = dataTable.hashes()[0];
    ctx.suplenteDni = parseInt(row.DNI);
});

Given(/^que existen las siguientes instancias de designación asignada$/, function (dataTable) {
    const row = dataTable.hashes()[0];
    this.tempTipoCargo = row.TipoDesignacion.toUpperCase();
    this.tempNombreCargo = row.NombreTipoDesignacion;
});

Given(/^que la instancia de designación está asignada a la persona con licencia "([^"]*)" comprendida en el período desde "([^"]*)" hasta "([^"]*)"$/, function (articulo, desde, hasta, dataTable) {
    const row = dataTable.hashes()[0];
    this.titularDni = parseInt(row.DNI);
    // Guardamos las fechas de la licencia del titular para las comprobaciones de suplencia
    this.licenciaDesde = desde;
    this.licenciaHasta = hasta;
});

When(/^se solicita el servicio de designación de la persona al cargo en el período comprendido desde "([^"]*)" hasta "([^"]*)"$/, function (desde, hasta) {
    ctx.url = 'http://backend:8080/designaciones';
    
    // Armamos la carga útil para la nueva designación suplente
    ctx.payload = {
        persona: { dni: ctx.suplenteDni },
        cargo: { nombre: this.tempNombreCargo, tipoDesignacion: this.tempTipoCargo },
        fechaInicio: desde,
        fechaFin: hasta
    };

    const res = request('POST', ctx.url, {
        json: ctx.payload,
        failOnStatusCode: false
    });

    ctx.response = JSON.parse(res.body);
});

Then(/^se recupera el mensaje$/, function (docString) {
    const esperado = JSON.parse(docString);
    
    // El profesor configuró un formato JSON específico para las respuestas de reemplazos (StatusCode / StatusText)
    // Mapeamos el ctx.response estándar a los campos que exige la aserción de Cucumber
    const actualStatusCode = ctx.response.status;
    const actualStatusText = ctx.response.message;

    assert.strictEqual(actualStatusCode, esperado.StatusCode);
    assert.strictEqual(actualStatusText.trim(), esperado.StatusText.trim());
});