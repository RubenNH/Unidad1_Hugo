const { query } = require('../../../utils/mysql'); 

const findAll = async(id) =>{
    const sql = `select * 
    from estados where fkPaises = ?`;
    return await query(sql, [id]);
};

module.exports = {
    findAll
};