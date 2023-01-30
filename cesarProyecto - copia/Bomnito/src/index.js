const { app } = require('./config/express');

const main = ()=>{
    try{
    app.listen(app.get('port'))
    console.log("imblue http://localhost:3000")
    }catch(err){
        console.log(err)
    }
}
main();