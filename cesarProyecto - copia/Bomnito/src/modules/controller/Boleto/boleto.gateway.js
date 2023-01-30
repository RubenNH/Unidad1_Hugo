const { query } = require('../../../utils/mysql'); 

const findAll = async(id) =>{
    const sql = `select * 
    from municipios where fkEstados = ?`;
    return await query(sql, [id]);
};

module.exports = {
    findAll
};