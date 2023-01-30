const { salaRouter } = require('./Sala/sala.controller')
const { boletoRouter } = require('./Boleto/boleto.controller')
const { funcionRouter } = require('./Funcion/funcion.controller')

module.exports = {
    salaRouter,
    boletoRouter,
    funcionRouter
}