const { Given } = require('cucumber');
const ctx = require('./ctx');

Given('el espacio físico división con {int} {int} {string} {string}', 
(anio, numero, orientacion, turno) => {
    ctx.url = 'http://backend:8080/divisiones';
    
    ctx.payload = { 
        anio: parseInt(anio), 
        numDivision: parseInt(numero), 
        orientacion, 
        turno 
    };
});