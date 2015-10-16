//"use strict";
var fs = require("fs");
//var argv = require('optimist').argv;
var path = require("path");
/*
var clientExtensions = {};

var projectDir = (argv.w && path.resolve(process.cwd(), argv.w)) || process.cwd();
var fsUrl = "/workspace";
var vfsUrl = "/vfs";

var port = argv.p || process.env.PORT || 3000;
var host = argv.l || process.env.IP || "0.0.0.0";
*/
var scxappConfig = {

    test:"xxxx",
    AppStoreRoot:"/Applications/MAMP/htdocs/CMAC/",
    cloud9Root:"/PMaster/cloud92/",
    cloud9Host:"192.168.1.37",
    cxapp:{
        template: "/liferay/tomcat-7.0.27/webapps/XApp-portlet/lib/cxapp_template/",
        service:"http://localhost:3000/scxapp/"
    }
};

module.exports =scxappConfig;
global.scxappConfig=scxappConfig;

