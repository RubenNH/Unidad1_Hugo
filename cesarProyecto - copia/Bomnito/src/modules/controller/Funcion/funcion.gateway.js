const { query } = require('../../../utils/mysql'); 

const findAll = async() =>{
    const sql = `select * 
    from paises`;
    return await query(sql, []);
};

module.exports = {
    findAll
};