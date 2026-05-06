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