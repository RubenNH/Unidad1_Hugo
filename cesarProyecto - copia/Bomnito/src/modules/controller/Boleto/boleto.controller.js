const { Response, Router } = require('express');
const { validateError } = require('../../../utils/functions');
const { findAll } = require('./boleto.gateway');

const getAll = async(req, res=Response) =>{
    try{
        const { id } = req.params;
        const funcion = await findAll(id);
        res.status(200).json(funcion);
    }catch(error){
        console.log(error);
        const massage = validateError(error);
        res.status(400).json(massage);
    }
}

const boletoRouter = Router();
//boletoRouter.get('/', [ auth, checkroles(['admin']) ], getAll);

boletoRouter.get('/:id', getAll);

module.exports = {
    boletoRouter
}