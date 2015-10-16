define([ 'dojo/has','scxapp/manager/DataManager' ], function (has,DataManager)
{
    //var express = require('express');
    //console.error('say hello' + dojo.config.loaderPatch.nodeRequire);
    var nrequire = dojo.config.loaderPatch.nodeRequire;
    //var e = nrequire('express');

    var path2 = nrequire('path');

    var fs = nrequire("fs");
    //console.error('dir2' + global.__dirname);

    var d = scxapp.manager.DataManager();

    var dirPrefix="../../";
    var dirPrefix2="../";
    var c=nrequire(dirPrefix + "default");
    console.dir('dir2' + scxappConfig.test);


    var express = nrequire('express')
        , routes = nrequire(dirPrefix+ 'routes')
        , user = nrequire(dirPrefix + 'routes/user')
        , http = nrequire('http')
        , path = nrequire('path');

    var app = express();

    app.configure(function(){
        app.set('port', process.env.PORT || 3000);
        app.set('views', dirPrefix2 + 'views');
        app.set('view engine', 'jade');
        app.use(express.favicon());
        app.use(express.logger('dev'));
        app.use(express.bodyParser());
        app.use(express.methodOverride());
        app.use(app.router);
        app.use(express.static(path.join(dirPrefix, 'public')));
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
        debugger;
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

});
