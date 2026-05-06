const { Given, When, Then } = require('cucumber');
const assert = require('assert');
const ctx = require('./ctx');

Given('la persona con {string} {string} {int} {string} {string} {string} {string} {string}', 
(nombre, apellido, dni, cuil, sexo, titulo, domicilio, telefono) => {
    ctx.url = 'http://backend:8080/personas';
    ctx.payload = { 
        dni: parseInt(dni), 
        nombre, 
        apellido, 
        cuil, 
        sexo, 
        titulo, 
        domicilio, 
        telefono 
    };
});