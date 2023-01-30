const jwt = require('jsonwebtoken');
require('dotenv').config();

const generateToken =(payload) => {
    return jwt.sign(payload, process.env.SECRET);
}

const auth = async(req, res, next) => {
    try{
        const token = req.headers;
        if(!token) throw Error ('invalidid')
        const decodeToken = jwt.verify(token, process.env.SECRET);
        req.token = decodeToken;
        next();
    }catch(error){
        res.status(400).json({message: 'unauth'});
    }
    
}

const checkroles =(roles) => {
    return async (req, res, next) => {
        try{
            const token = req.token;
            if(!token) throw Error ('invalidid')
            if(!roles.some((role)=> role === token.role)) throw Error ('invalidid')
            next();
        }catch(error){
            res.status(400).json({message: 'unauth'});
        }
    }
}

module.exports = {
    generateToken,
    auth,
    checkroles
};