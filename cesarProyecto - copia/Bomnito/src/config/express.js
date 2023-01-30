const express = require('express');
require('dotenv').config();
const cors = require('cors');

const { boletoRouter, salaRouter, funcionRouter} = require('../modules/controller/routes');

const app = express();

app.set("port",process.env.PORT || 3000);

app.use(cors({origins:"*"}));
app.use(express.json({limit:'50mb'}));

app.get('/', (req,res)=>{
    res.send("otra casa");
});


app.use('/api/pais', funcionRouter)
app.use('/api/muni', boletoRouter)
app.use('/api/edo', salaRouter)

module.exports = {app};