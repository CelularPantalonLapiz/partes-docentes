/*
const { When, Then } = require('cucumber');
const assert = require('assert'); 

const ctx = require('./ctx'); 

When('se presiona el botón de guardar', async () => {
    const response = await fetch(ctx.url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(ctx.payload)
    });

    ctx.response = await response.json(); 
    
    if (ctx.response?.data?.id) {
        ctx.createdIds.push({
            url: ctx.url,
            id: ctx.response.data.id
        });
    }
});


Then('se espera el siguiente {int} con la {string}', (status, mensajeEsperado) => {
    assert.strictEqual(ctx.response.status, status);
    assert.strictEqual(ctx.response.message, mensajeEsperado);
});
*/
// features/step_definitions/common.js
// features/step_definitions/common.js
const { When, Then } = require('cucumber');
const assert = require('assert');
const request = require('sync-request'); 
const ctx = require('./ctx');

// Maneja "botón de guardar" y "botón guardar"
When(/^se presiona el botón (?:de )?guardar$/, function () {
    const res = request('POST', ctx.url, {
        json: ctx.payload,
        failOnStatusCode: false 
    });

    ctx.response = JSON.parse(res.body);
    
    // Si el backend devolvió un ID (éxito), lo guardamos para limpieza
    if (ctx.response?.data?.id) {
        ctx.createdIds.push({
            url: ctx.url,
            id: ctx.response.data.id
        });
    }
});

// Maneja "con la" y "y", y mensajes con o sin comillas
Then(/^se espera el siguiente (\d+) (?:con la |y )(.*)$/, function (status, mensajeEsperado) {
    // 1. Validar Status
    assert.strictEqual(ctx.response.status, parseInt(status));

    // 2. Validar Mensaje
    // Quitamos comillas si las tuviera (de la fase 4) y espacios extra
    const msgLimpio = mensajeEsperado.replace(/\"/g, "").trim();
    assert.strictEqual(ctx.response.message, msgLimpio);
});