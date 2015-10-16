
//module.exports = global.require;
/**
 * Module dependencies.
 */
/*
dojoConfig = {"host-node":1,baseUrl:"",packages:[{name: 'scxapp', location: 'scxapp'}]};
var dojo = require(__dirname + "/../../lib/dojo/dojo",function(dojo)
{
    console.error('asdfasdfsdf');
});
*/

//var d = dojo();
/*
setTimeout(function(){
    //console.error(define);
    var dm = require(__dirname + '/scxapp/manager/DataManager');
    var c= new dm();
    define([ 'dojo/has','scxapp/manager/DataManager' ], function (has,DataManager)
    {
        console.error();
    });
},2000);
*/


//module.exports = global.require;
var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , http = require('http')
  , path = require('path');

var app = express();

app.configure(function(){
  app.set('port', process.env.PORT || 3000);
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.favicon());
  app.use(express.logger('dev'));
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
  app.use(express.static(path.join(__dirname, 'public')));
});

app.configure('development', function(){
  app.use(express.errorHandler());
});

app.get('/', routes.index);
app.get('/users', user.list);

function provides(type) {
    return function(req, res, next){
        if (req.accepts(type)) return next();
        next('route');
    }
}
app.get('/cxapp', provides('json'),function(req, res)
{
    console.error('dojo : ' + dojo);
   // console.error('define : ' + define);
   // console.error('has : ' + dojo.has);
    var records = {
        test:1,
        teset:req.query["id"]
    };
    res.send(records);

});

http.createServer(app).listen(app.get('port'), function(){
  console.log("Express server listening on port " + app.get('port'));
});
//baseUrl:__dirname + "/../../lib/",
//load:"app/run"
//main:"app.js",
//,packages:[{name: 'cxappmaker', location: 'cxappmaker'}]
/*
dojoConfig = {"host-node":1,baseUrl:""};
require(__dirname + "/test/dojo/dojo",function(dojo)
{
    console.error('asdfasdfsdf');
});
module.exports = global.require;
*/

