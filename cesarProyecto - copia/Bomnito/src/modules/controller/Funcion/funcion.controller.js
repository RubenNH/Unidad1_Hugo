const { Response, Router } = require('express');
const { validateError } = require('../../../utils/functions');
const { findAll} = require('./funcion.gateway');

const getAll = async(req, res=Response) =>{
    try{
        const funcion = await findAll();
        res.status(200).json(funcion);
    }catch(error){
        console.log(error);
        const massage = validateError(error);
        res.status(400).json(massage);
    }
}


const funcionRouter = Router();
//funcionRouter.get('/', [ auth, checkroles(['admin']) ], getAll);

funcionRouter.get('/', getAll);

module.exports = {
    funcionRouter
}