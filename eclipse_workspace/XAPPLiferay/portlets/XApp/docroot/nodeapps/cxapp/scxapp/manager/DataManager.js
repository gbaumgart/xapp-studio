define([
    "dojo/has"
], function (has)
{
    return dojo.declare("scxapp.manager.DataManager", null,
        {
            ctx:null,
            currentQueueIndex:0,
            queue:[],
            dataQueue:null,
            isLocked:false,
            dataCache:null,
            useMemoryCache:false,
            useHTML5LocalStoreage:false,
            deviceStorage:null,
            transportType:0,
            _patchQueueObject:function(queuedObject,url,pageData)
            {
                var attach = true;
                if(xapp.utils.isTwitterServiceUrl(url)){
                    attach=false;
                    queuedObject.extern = true
                }

                if (pageData && attach)
                {
                    var newUrl = this.attachPageContextToUrl(url, pageData);
                    if (newUrl) {
                        queuedObject.url = newUrl
                    }
                }
                if(attach){
                    queuedObject.url = this.attachApplicationInfoToUrl(queuedObject.url);
                }
            },
            loadData:function(url,pageData,cb,owner,options)
            {
                //console.error("load data " + url);
                var transportType = this.transportType;
                if(options && options.transportType)
                {
                    transportType=options.transportType;
                }
                switch(transportType)
                {
                    case xapp.types.TransportType.REST:
                    {
                        return this.loadJSONData(url, pageData, cb, owner,options);
                    }
                    case xapp.types.TransportType.REST_PROXY:
                    {
                        return this.loadJSONData(url, pageData, cb, owner,options);
                    }
                    case xapp.types.TransportType.JSONP:
                    {
                        return this.loadJSONP(url,pageData,cb,owner,options);
                    }
                }
                return null;
            },
            getLocalStorage:function()
            {
                return win.global.localStorage || null;
            },
            loadFromStorage:function(name)
            {
                if (this.getLocalStorage()) {
                    return this.getLocalStorage().getItem(name);
                } else {
                    return "";
                }
            },
            saveToStorage:function(name, val)
            {
                if (this.getLocalStorage()) {
                    this.getLocalStorage().setItem(name, val);
                }
            },
            attachApplicationInfoToUrl:function (url) {
                var newUrl = url;

                if(newUrl.indexOf("uuid")==-1)
                {
                    //result+="&uuid="+splitted[0];
                    newUrl += "&uuid=" + this.ctx.getSession().getUUID();
                }

                if(newUrl.indexOf("appIdentifier")==-1)
                {
                    newUrl += "&appIdentifier=" + this.ctx.getSession().getAppId();
                }
                return newUrl;
            },
            attachCIContextToUrl:function (url, ci)
            {
                var newUrl = url;
                var dataSource = xapp.utils.getStringValue(ci.dataSource);
                if (dataSource) {
                    newUrl += "&dataSource=" + dataSource;
                } else {
                    console.error("---------HAVE NO DATA CI");
                }
                return newUrl;
            },
            attachPageContextToUrl:function (url, pageData) {
                var newUrl = url;
                var contentSourceCI = xapp.utils.getInputCIByName(pageData, xapp.types.CIName.ContentSource);
                if (contentSourceCI) {
                    var dataSource = xapp.utils.getStringValue(contentSourceCI.dataSource);
                    if (dataSource) {
                        newUrl += "&dataSource=" + dataSource;
                    }
                } else {
                    console.error("---------HAVE NO DATACI");
                }
                var pageUID = xapp.utils.getIntegerValue(pageData.uid);
                if (pageUID) {
                    newUrl += "&pageUid=" + pageUID;
                }
                return newUrl;
            },
            loadJSONDataWithTypeAndDataCI:function (url,ci,callback, dst)
            {
                var queuedObject = {
                    pageData:ci,
                    owner:dst,
                    url:url,
                    status:0,
                    callback:callback,
                    queueIndex:this.currentQueueIndex
                };
                /*
                if (dsUrl) {
                    var splitted = dsUrl.split("/");
                    if (splitted && splitted.length >= 4) {
                        url += "&dataSource=" + splitted[2];
                    }
                    //queuedObject.url = url;
                    var newUrl = this.attachCIContextToUrl(url, pageData);
                    if (newUrl) {
                        queuedObject.url = newUrl;
                    }
                }
                */
                var newUrl = this.attachCIContextToUrl(url, ci);
                if (newUrl) {
                    queuedObject.url = newUrl;
                }
                queuedObject.url = this.attachApplicationInfoToUrl(queuedObject.url);
                this.dataQueue.push(queuedObject);
                this.processNextJSON();
                this.currentQueueIndex++;
                return queuedObject.url;
            },
            loadJSONDataWithDSUrlEx:function (url, dsUID, pageData, callback, dst)
            {
                /*
                var queuedObject =
                {
                    pageData:pageData,
                    owner:dst,
                    url:url,
                    status:0,
                    callback:callback,
                    queueIndex:this.currentQueueIndex,
                    clear:true
                };
                queuedObject.url = url += "&dataSource=" + dsUID;
                queuedObject.url = this.attachApplicationInfoToUrl(queuedObject.url);
                */
                url +="&dataSource=" + dsUID;
                url = this.attachApplicationInfoToUrl(url);
                return this.loadData(url,pageData,callback,dst);
                /*this.dataQueue.push(queuedObject);
                this.processNextJSON();
                this.currentQueueIndex++;
                return queuedObject.url;
                */
            },
            loadJSONDataWithDSUrl:function (url, dsUrl, pageData, callback, dst)
            {
                var queuedObject = {
                    pageData:pageData,
                    owner:dst,
                    url:url,
                    status:0,
                    callback:callback,
                    queueIndex:this.currentQueueIndex,
                    clear:true
                };
                if (dsUrl) {
                    var splitted = dsUrl.split("/");
                    if (splitted && splitted.length >= 4) {
                        url += "&dataSource=" + splitted[2];
                    }
                    //queuedObject.url = url;
                    var newUrl = this.attachPageContextToUrl(url, pageData);
                    if (newUrl) {
                        queuedObject.url = newUrl;
                    }
                }
                queuedObject.url = this.attachApplicationInfoToUrl(queuedObject.url);
                //var cachedData = this.dataCache.get(queuedObject.url);
                //cachedData=null;
                /*
                if (cachedData) {
                    console.error("found cached data");
                    //queuedObject.callback(queuedObject.owner,cachedData);
                    //return;
                }
                */
                /*
                 else if(localStorage){
                 cachedData = localStorage.getItem(queuedObject.url);
                 if(cachedData)
                 {
                 console.error("found cached data in HTML5 storage");
                 queuedObject.callback(queuedObject.owner, JSON.parse(cachedData));
                 }


                 }*/
                this.dataQueue.push(queuedObject);
                this.processNextJSON();
                this.currentQueueIndex++;
                return queuedObject.url;
            },
            loadJSONData:function (url, pageData, callback, dst,options)
            {
                var queuedObject = {
                    pageData:pageData,
                    owner:dst,
                    url:url,
                    status:0,
                    extern:false,
                    callback:callback,
                    queueIndex:this.currentQueueIndex,
                    clear:true
                };

                var attach = true;
                if(options && options.attachAppId!=null)
                {
                    attach=options.attachAppId;
                }
                /*
                if(xapp.utils.isTwitterServiceUrl(url)){
                    attach=false;
                    queuedObject.extern = true
                }
                */

                if (pageData && attach)
                {
                    var newUrl = this.attachPageContextToUrl(url, pageData);
                    if (newUrl) {
                        queuedObject.url = newUrl
                    }
                }
                //console.error("lo : " + queuedObject.url + " attach " + attach);
                if(attach){
                    queuedObject.url = this.attachApplicationInfoToUrl(queuedObject.url);
                }

                //var cachedData = this.dataCache.get(queuedObject.url);
                //cachedData=null;
                /*
                 if(cachedData)
                 {
                 console.error("found cached data");
                 queuedObject.callback(queuedObject.owner,cachedData);
                 return;
                 }else if(localStorage){
                 cachedData = localStorage.getItem(queuedObject.url);
                 if(cachedData)
                 {
                 console.error("found cached data in HTML5 storage");
                 queuedObject.callback(queuedObject.owner, JSON.parse(cachedData));
                 }


                 }
                 */
                this.dataQueue.push(queuedObject);
                this.processNextJSON();
                this.currentQueueIndex++;
                return queuedObject.url;
            },
            dataRecievedJSONP:function (result)
            {
                var resO = result;
                try {
                    resO = dojo.fromJson(result);
                } catch (e) {
                    resO = result;
                }
                var job = this.getCurrentJob();
                if (job) {
                    if (job.callback && job.owner)
                    {
                        /*
                        }
                        if (resO.items){
                            job.callback(job.owner,resO.items);
                        }
                        else{
                          */
                            job.callback(job.owner,resO);
                        //}
                    }
                    var idx = this.dataQueue.indexOf(job);
                    if (idx != -1) this.dataQueue.splice(idx, 1);
                }
                this.isLocked = false;
                this.processNext();
            },
            getCurrentJob:function () {
                for (var i = 0; i < this.dataQueue.length; i++) {
                    if (this.dataQueue[i].status == 1)
                        return this.dataQueue[i];
                }
                return null;
            },
            getNextJob:function () {
                for (var i = 0; i < this.dataQueue.length; i++) {
                    if (this.dataQueue[i].status == 0)
                        return this.dataQueue[i];
                }
                return null;
            },
            cacheResponse:function (url, data) {
                if(this.dataCache)
                {
                    this.dataCache.put(url, data);
                    // Put the object into storage
                    if (localStorage) {
                        localStorage.setItem(url, JSON.stringify(data));
                    }
                }
            },
            dataRecievedJSON:function (result) {
                var thiz = this;
                var job = this.getCurrentJob();
                if (job) {
                    if (job.callback && job.owner) {
                        job.callback(job.owner, result);
                        thiz.cacheResponse(job.url, result);
                    }
                    var idx = this.dataQueue.indexOf(job);
                    if (idx != -1) this.dataQueue.splice(idx, 1);
                }
                this.isLocked = false;
                this.processNextJSON();
            },
            onError:function(){
              //console.error("error");
              ctx.getUrlHandler().onDataError();
              ctx.getViewManager().onDataError();
            },
            processNextJSON:function () {

                var thiz=this;
                if(this.isLocked){
                    return;
                }
                var job = this.getNextJob();
                if (job)
                {
                    if(job.extern){
                        this.processNext();
                        return;
                    }
                    if (!this.isLocked)
                        job.status = 1;
                    var xhrArgs = {
                        url:job.url,
                        handleAs:"json",
                        load:dojo.hitch(this, "dataRecievedJSON"),
                        error:function(){
                            thiz.onError();
                        }
                    };
                    dojo.xhrGet(xhrArgs);

                }
            },
            getJobByUrl:function(url)
            {
                for (var i = 0; i < this.dataQueue.length; i++)
                {
                    if (this.dataQueue[i].url == url)
                        return this.dataQueue[i];
                }
                return null;

            },
            processNext:function () {
                var thiz=this;
                var job = this.getNextJob();
                if (job)
                {
                    var callbackName = "ctx.getSession().dataManager.dataRecievedJSONP";
                    var attachPrefix = "?";
                    if (job.url.indexOf("?") != -1) {
                        attachPrefix = "&";
                    }
                    //var url = job.url + attachPrefix + "jsonpCallback=" + callbackName;
                    if (!this.isLocked)
                        job.status = 1;

                    var def  = dojo.io.script.get({
                        url:job.url,
                        async:true,
                        handleAs:"json",
                        callbackParamName: "callback"
                    });
                    def.addCallback(dojo.hitch(this, this.dataRecievedJSONP));
                }
            },
            loadJSONP:function (url,pageData,callback,dst)
            {

               var queuedObject = {
                    url:url,
                    status:0,
                    owner:dst,
                    callback:callback,
                    queueIndex:this.currentQueueIndex
                };
                this._patchQueueObject(queuedObject,url,pageData);
                this.dataQueue.push(queuedObject);
                this.processNext();
                this.currentQueueIndex++;
                return url;
            },
            constructor:function () {
                this.dataQueue = new Array();
                console.error('helo');
            }

        });
});