
//  Example stubs for storexml operations.  This function is not intended
//  to be called, but rather as a source for copy-and-paste development.

//  Note that this stub has been generated for use in E4X environments.
//  All endpoints have been converted to the "localhost" domain.

function stubs() {
        // remove operation
    try {
        /* anyType */ var removeReturn = storexml.remove(/* string */ param_name);
    } catch (e) {
        // fault handling
    }

    // retrieve operation
    try {
        /* anyType */ var retrieveReturn = storexml.retrieve(/* string */ param_name);
    } catch (e) {
        // fault handling
    }

    // store operation
    try {
        /* anyType */ var storeReturn = storexml.store(/* string */ param_name, /* anyType */ param_value);
    } catch (e) {
        // fault handling
    }

}
stubs.visible = false;

var storexml = new WebService("SOAP12Endpoint");

storexml.remove =
    function remove(/* string */ _name)
    {
        var isAsync, request, response, resultValue;
        this._options = new Array();
        isAsync = (this.remove.callback != null && typeof(this.remove.callback) == 'function');
        request = this.remove_payload(/* string */ _name);

        if (isAsync) {
            try {
                this._call(
                    "remove",
                    "http://www.w3.org/ns/wsdl/in-out",
                    request,
                    function(thisRequest, callbacks) {
                        if (thisRequest.error != null) {
                            callbacks[1](thisRequest.error);
                        } else {
                            response = new XML(thisRequest.responseText);
                            if (response == null) {
                                resultValue = null;
                            } else {
                                resultValue = /* xs:anyType */ WebService.utils._convertJSType(response["return"], true);
                            }
                            callbacks[0](resultValue);
                        }
                    },
                    new Array(this.remove.callback, this.remove.onError)
                );
            } catch (e) {
                var error;
                if (WebServiceError.prototype.isPrototypeOf(e)) {
                    error = e;
                } else if (e.name != null) {
                    // Mozilla
                    error = new WebServiceError(e.name, e.message + " (" + e.fileName + "#" + e.lineNumber + ")");
                } else if (e.description != null) {
                    // IE
                    error = new WebServiceError(e.description, e.number, e.number);
                } else {
                    error = new WebServiceError(e, "Internal Error");
                }
                this.remove.onError(error);
            }
        } else {
            try {
                                response = this._call("remove", "http://www.w3.org/ns/wsdl/in-out", request);
                                resultValue = /* xs:anyType */ WebService.utils._convertJSType(response["return"], true);
                                return resultValue;
            } catch (e) {
                if (typeof(e) == "string") throw(e);
                if (e.message) throw(e.message);
                throw (e.reason);
            }
        }
        return null; // Suppress warnings when there is no return.
    }
storexml.remove_payload =
    function (/* string */ _name) {
        
        return '<p:remove xmlns:p="http://services.mashup.wso2.org/storexml?xsd">' +
                (_name == null ? '' : '<name>' + this._encodeXML(_name) + '</name>') +
            '</p:remove>' ;
    }
storexml.remove_payload.visible = false;
storexml.remove.callback = null;

storexml.retrieve =
    function retrieve(/* string */ _name)
    {
        var isAsync, request, response, resultValue;
        this._options = new Array();
        isAsync = (this.retrieve.callback != null && typeof(this.retrieve.callback) == 'function');
        request = this.retrieve_payload(/* string */ _name);

        if (isAsync) {
            try {
                this._call(
                    "retrieve",
                    "http://www.w3.org/ns/wsdl/in-out",
                    request,
                    function(thisRequest, callbacks) {
                        if (thisRequest.error != null) {
                            callbacks[1](thisRequest.error);
                        } else {
                            response = new XML(thisRequest.responseText);
                            if (response == null) {
                                resultValue = null;
                            } else {
                                resultValue = /* xs:anyType */ WebService.utils._convertJSType(response["return"], true);
                            }
                            callbacks[0](resultValue);
                        }
                    },
                    new Array(this.retrieve.callback, this.retrieve.onError)
                );
            } catch (e) {
                var error;
                if (WebServiceError.prototype.isPrototypeOf(e)) {
                    error = e;
                } else if (e.name != null) {
                    // Mozilla
                    error = new WebServiceError(e.name, e.message + " (" + e.fileName + "#" + e.lineNumber + ")");
                } else if (e.description != null) {
                    // IE
                    error = new WebServiceError(e.description, e.number, e.number);
                } else {
                    error = new WebServiceError(e, "Internal Error");
                }
                this.retrieve.onError(error);
            }
        } else {
            try {
                                response = this._call("retrieve", "http://www.w3.org/ns/wsdl/in-out", request);
                                resultValue = /* xs:anyType */ WebService.utils._convertJSType(response["return"], true);
                                return resultValue;
            } catch (e) {
                if (typeof(e) == "string") throw(e);
                if (e.message) throw(e.message);
                throw (e.reason);
            }
        }
        return null; // Suppress warnings when there is no return.
    }
storexml.retrieve_payload =
    function (/* string */ _name) {
        
        return '<p:retrieve xmlns:p="http://services.mashup.wso2.org/storexml?xsd">' +
                (_name == null ? '' : '<name>' + this._encodeXML(_name) + '</name>') +
            '</p:retrieve>' ;
    }
storexml.retrieve_payload.visible = false;
storexml.retrieve.callback = null;

storexml.store =
    function store(/* string */ _name, /* anyType */ _value)
    {
        var isAsync, request, response, resultValue;
        this._options = new Array();
        isAsync = (this.store.callback != null && typeof(this.store.callback) == 'function');
        request = this.store_payload(/* string */ _name, /* anyType */ _value);

        if (isAsync) {
            try {
                this._call(
                    "store",
                    "http://www.w3.org/ns/wsdl/in-out",
                    request,
                    function(thisRequest, callbacks) {
                        if (thisRequest.error != null) {
                            callbacks[1](thisRequest.error);
                        } else {
                            response = new XML(thisRequest.responseText);
                            if (response == null) {
                                resultValue = null;
                            } else {
                                resultValue = /* xs:anyType */ WebService.utils._convertJSType(response["return"], true);
                            }
                            callbacks[0](resultValue);
                        }
                    },
                    new Array(this.store.callback, this.store.onError)
                );
            } catch (e) {
                var error;
                if (WebServiceError.prototype.isPrototypeOf(e)) {
                    error = e;
                } else if (e.name != null) {
                    // Mozilla
                    error = new WebServiceError(e.name, e.message + " (" + e.fileName + "#" + e.lineNumber + ")");
                } else if (e.description != null) {
                    // IE
                    error = new WebServiceError(e.description, e.number, e.number);
                } else {
                    error = new WebServiceError(e, "Internal Error");
                }
                this.store.onError(error);
            }
        } else {
            try {
                                response = this._call("store", "http://www.w3.org/ns/wsdl/in-out", request);
                                resultValue = /* xs:anyType */ WebService.utils._convertJSType(response["return"], true);
                                return resultValue;
            } catch (e) {
                if (typeof(e) == "string") throw(e);
                if (e.message) throw(e.message);
                throw (e.reason);
            }
        }
        return null; // Suppress warnings when there is no return.
    }
storexml.store_payload =
    function (/* string */ _name, /* anyType */ _value) {
        
        return '<p:store xmlns:p="http://services.mashup.wso2.org/storexml?xsd">' +
                (_name == null ? '' : '<name>' + this._encodeXML(_name) + '</name>') +
                WebService.utils._serializeAnytype('value', _value, '', false) +
            '</p:store>' ;
    }
storexml.store_payload.visible = false;
storexml.store.callback = null;



// WebService object.
function WebService(endpointName)
{
    this.readyState = 0;
    this.onreadystatechange = null;
    this.scriptInjectionCallback = null;
    this.proxyAddress = null;

    //public accessors for manually intervening in setting the address (e.g. supporting tcpmon)
    this.getAddress = function (endpointName)
    {
        return this._endpointDetails[endpointName].address;
    }

    this.setAddress = function (endpointName, address)
    {
        this._endpointDetails[endpointName].address = address;
    }

    // private helper functions
    this._getWSRequest = function()
    {
        var wsrequest;
        try {
            wsrequest = new WSRequest();
            // try to set the proxyAddress based on the context of the stub - browser or Mashup Server
            try {
                wsrequest.proxyEngagedCallback = this.scriptInjectionCallback;
                wsrequest.proxyAddress = this.proxyAddress;
            } catch (e) {
                try {
                    wsrequest.proxyEngagedCallback = this.scriptInjectionCallback;
                    wsrequest.proxyAddress = this.proxyAddress;
                } catch (e) { }
            }
        } catch(e) {
            try {
                wsrequest = new ActiveXObject("WSRequest");
            } catch(e) {
                try {
                    wsrequest = new SOAPHttpRequest();
                    
                } catch (e) {
                    throw new WebServiceError("WSRequest object not defined.", "WebService._getWSRequest() cannot instantiate WSRequest object.");
                }
            }
        }
        return wsrequest;
    }

    this._endpointDetails =
        {
            "SOAP12Endpoint": {
                "type" : "SOAP12",
                "address" : "http://localhost:9763/services/admin/storexml.SOAP12Endpoint/",
                "action" : {
                    "remove" : "urn:remove",
                    "store" : "urn:store",
                    "retrieve" : "urn:retrieve"
                },
                "soapaction" : {
                    "remove" : "urn:remove",
                    "store" : "urn:store",
                    "retrieve" : "urn:retrieve"
                },
                "httplocation" : {
                    "remove" : "remove",
                    "store" : "store",
                    "retrieve" : "retrieve"
                }
            },
            "SecureSOAP12Endpoint": {
                "type" : "SOAP12",
                "address" : "https://localhost:9443/services/admin/storexml.SecureSOAP12Endpoint/",
                "action" : {
                    "remove" : "urn:remove",
                    "store" : "urn:store",
                    "retrieve" : "urn:retrieve"
                },
                "soapaction" : {
                    "remove" : "urn:remove",
                    "store" : "urn:store",
                    "retrieve" : "urn:retrieve"
                },
                "httplocation" : {
                    "remove" : "remove",
                    "store" : "store",
                    "retrieve" : "retrieve"
                }
            },
            "SOAP11Endpoint": {
                "type" : "SOAP11",
                "address" : "http://localhost:9763/services/admin/storexml.SOAP11Endpoint/",
                "action" : {
                    "remove" : "urn:remove",
                    "store" : "urn:store",
                    "retrieve" : "urn:retrieve"
                },
                "soapaction" : {
                    "remove" : "urn:remove",
                    "store" : "urn:store",
                    "retrieve" : "urn:retrieve"
                },
                "httplocation" : {
                    "remove" : "remove",
                    "store" : "store",
                    "retrieve" : "retrieve"
                }
            },
            "SecureSOAP11Endpoint": {
                "type" : "SOAP11",
                "address" : "https://localhost:9443/services/admin/storexml.SecureSOAP11Endpoint/",
                "action" : {
                    "remove" : "urn:remove",
                    "store" : "urn:store",
                    "retrieve" : "urn:retrieve"
                },
                "soapaction" : {
                    "remove" : "urn:remove",
                    "store" : "urn:store",
                    "retrieve" : "urn:retrieve"
                },
                "httplocation" : {
                    "remove" : "remove",
                    "store" : "store",
                    "retrieve" : "retrieve"
                }
            },
            "HTTPEndpoint": {
                "type" : "HTTP",
                "address" : "http://localhost:9763/services/admin/storexml.HTTPEndpoint/",
                "httplocation" : {
                    "remove" : "remove",
                    "store" : "store",
                    "retrieve" : "retrieve"
                },
                "httpignoreUncited" : {
                        "remove" : "false",
                        "store" : "false",
                        "retrieve" : "false"
                },
                "httpmethod" : {
                        "remove" : "POST",
                        "store" : "POST",
                        "retrieve" : "GET"
                },
                "fitsInURLParams" : {
                        "remove" : true,
                        "store" : false,
                        "retrieve" : true
                }
            },
            "SecureHTTPEndpoint": {
                "type" : "HTTP",
                "address" : "https://localhost:9443/services/admin/storexml.SecureHTTPEndpoint/",
                "httplocation" : {
                    "remove" : "remove",
                    "store" : "store",
                    "retrieve" : "retrieve"
                },
                "httpignoreUncited" : {
                        "remove" : "false",
                        "store" : "false",
                        "retrieve" : "false"
                },
                "httpmethod" : {
                        "remove" : "POST",
                        "store" : "POST",
                        "retrieve" : "GET"
                },
                "fitsInURLParams" : {
                        "remove" : true,
                        "store" : false,
                        "retrieve" : true
                }
            }
    };
    this.endpoint = endpointName;

    this.username = null;
    this.password = null;

    this._encodeXML = function (value) {
        var str = value.toString();
        str = str.replace(/&/g, "&amp;");
        str = str.replace(/</g, "&lt;");
        return(str);
    };

    this._setOptions = function (details, opName) {
        var options = new Array();

        if (details.type == 'SOAP12') options.useSOAP = 1.2;
        else if (details.type == 'SOAP11') options.useSOAP = 1.1;
        else if (details.type == 'HTTP') options.useSOAP = false;

        if (options.useSOAP != false) {
            if (details.action != null) {
                options.useWSA = true;
                options.action = details.action[opName];
            } else if (details.soapaction != null) {
                options.useWSA = false;
                options.action = details.soapaction[opName];
            } else {
                options.useWSA = false;
                options.action = undefined;
            }
        }

        if (details["httpmethod"] != null) {
            options.HTTPMethod = details.httpmethod[opName];
        } else {
            options.HTTPMethod = null;
        }

        if (details["httpinputSerialization"] != null) {
            options.HTTPInputSerialization = details.httpinputSerialization[opName];
        } else {
            options.HTTPInputSerialization= null;
        }

        if (details["httplocation"] != null) {
            options.HTTPLocation = details.httplocation[opName];
        } else {
            options.HTTPLocation = null;
        }

        if (details["httpignoreUncited"] != null) {
            options.HTTPLocationIgnoreUncited = details.httpignoreUncited[opName];
        } else {
            options.HTTPLocationIgnoreUncited = null;
        }

        if (details["httpqueryParameterSeparator"] != null) {
            options.HTTPQueryParameterSeparator = details.httpqueryParameterSeparator[opName];
        } else {
            options.HTTPQueryParameterSeparator = null;
        }

        if (details["policies"]) {
            var policies = details["policies"][opName];
            for(i=0; i<policies.length; i++) {
                if(policies[i] == "UTOverTransport") {
                    options.useWSS = true;
                    break;
                }
            }
        }

        return options;
    };

    this._call = function (opName, pattern, reqContent, callback, userdata)
    {
        var details = this._endpointDetails[this.endpoint];
        this._options = this._setOptions(details, opName);

        var isAsync = (typeof(callback) == 'function');

        var thisRequest = this._getWSRequest();
        thisRequest.pattern = pattern;
        if (isAsync) {
            thisRequest._userdata = userdata;
            thisRequest.onreadystatechange =
                function() {
                    if (thisRequest.readyState == 4) {
                        callback(thisRequest, userdata);
                    }
                }
        }

        if (this.username == null)
            thisRequest.open(this._options, details.address, isAsync);
        else
            thisRequest.open(this._options, details.address, isAsync, this.username, this.password);

        thisRequest.send(reqContent);
        if (isAsync) {
            return "";
        } else {
            try {
                var resultContent = thisRequest.responseText;
                if (resultContent == "") {
                    throw new WebServiceError("No response", "WebService._call() did not recieve a response to a synchronous request.");
                }
                var resultXML = new XML(thisRequest.responseText);
            } catch (e) {
                throw new WebServiceError(e);
            }
            return resultXML;
        }
    };
}
WebService.visible = false;

WebService.utils = {
    toXSdate : function (thisDate) {
        var year = thisDate.getUTCFullYear();
        var month = thisDate.getUTCMonth() + 1;
        var day = thisDate.getUTCDate();

        return year + "-" +
            (month < 10 ? "0" : "") + month + "-" +
            (day < 10 ? "0" : "") + day + "Z";
    },

    toXStime : function (thisDate) {
        var hours = thisDate.getUTCHours();
        var minutes = thisDate.getUTCMinutes();
        var seconds = thisDate.getUTCSeconds();
        var milliseconds = thisDate.getUTCMilliseconds();

        return (hours < 10 ? "0" : "") + hours + ":" +
            (minutes < 10 ? "0" : "") + minutes + ":" +
            (seconds < 10 ? "0" : "") + seconds +
            (milliseconds == 0 ? "" : (milliseconds/1000).toString().substring(1)) + "Z";
    },

    toXSdateTime : function (thisDate) {
        var year = thisDate.getUTCFullYear();
        var month = thisDate.getUTCMonth() + 1;
        var day = thisDate.getUTCDate();
        var hours = thisDate.getUTCHours();
        var minutes = thisDate.getUTCMinutes();
        var seconds = thisDate.getUTCSeconds();
        var milliseconds = thisDate.getUTCMilliseconds();

        return year + "-" +
            (month < 10 ? "0" : "") + month + "-" +
            (day < 10 ? "0" : "") + day + "T" +
            (hours < 10 ? "0" : "") + hours + ":" +
            (minutes < 10 ? "0" : "") + minutes + ":" +
            (seconds < 10 ? "0" : "") + seconds +
            (milliseconds == 0 ? "" : (milliseconds/1000).toString().substring(1)) + "Z";
    },

    parseXSdateTime : function (dateTime) {
        var buffer = dateTime.toString();
        var p = 0; // pointer to current parse location in buffer.

        var era, year, month, day, hour, minute, second, millisecond;

        // parse date, if there is one.
        if (buffer.substr(p,1) == '-')
        {
            era = -1;
            p++;
        } else {
            era = 1;
        }

        if (buffer.charAt(p+2) != ':')
        {
            year = era * buffer.substr(p,4);
            p += 5;
            month = buffer.substr(p,2);
            p += 3;
            day = buffer.substr(p,2);
            p += 3;
        } else {
            year = 1970;
            month = 1;
            day = 1;
        }

        // parse time, if there is one
        if (buffer.charAt(p) != '+' && buffer.charAt(p) != '-')
        {
            hour = buffer.substr(p,2);
            p += 3;
            minute = buffer.substr(p,2);
            p += 3;
            second = buffer.substr(p,2);
            p += 2;
            if (buffer.charAt(p) == '.')
            {
                millisecond = parseFloat(buffer.substring(p))*1000;
                // Note that JS fractional seconds are significant to 3 places - xs:time is significant to more -
                // though implementations are only required to carry 3 places.
                p++;
                while (buffer.charCodeAt(p) >= 48 && buffer.charCodeAt(p) <= 57) p++;
            } else {
                millisecond = 0;
            }
        } else {
            hour = 0;
            minute = 0;
            second = 0;
            millisecond = 0;
        }

        var tzhour = 0;
        var tzminute = 0;
        // parse time zone
        if (buffer.charAt(p) != 'Z' && buffer.charAt(p) != '') {
            var sign = (buffer.charAt(p) == '-' ? -1 : +1);
            p++;
            tzhour = sign * buffer.substr(p,2);
            p += 3;
            tzminute = sign * buffer.substr(p,2);
        }

        var thisDate = new Date();
        thisDate.setUTCFullYear(year);
        thisDate.setUTCMonth(month-1);
        thisDate.setUTCDate(day);
        thisDate.setUTCHours(hour);
        thisDate.setUTCMinutes(minute);
        thisDate.setUTCSeconds(second);
        thisDate.setUTCMilliseconds(millisecond);
        thisDate.setUTCHours(thisDate.getUTCHours() - tzhour);
        thisDate.setUTCMinutes(thisDate.getUTCMinutes() - tzminute);
        return thisDate;
    },

    _nextPrefixNumber : 0,

    _QNameNamespaceDecl : function (qn) {
        if (qn.uri == null) return "";
        var prefix = qn.localName.substring(0, qn.localName.indexOf(":"));
        if (prefix == "") {
            prefix = "n" + ++this._nextPrefixNumber;
        }
        return ' xmlns:' + prefix + '="' + qn.uri + '"';
    },

    _QNameValue : function(qn) {
        if (qn.uri == null) return qn.localName;
        var prefix, localName;
        if (qn.localName.indexOf(":") >= 0) {
            prefix = qn.localName.substring(0, qn.localName.indexOf(":"));
            localName = qn.localName.substring(qn.localName.indexOf(":")+1);
        } else {
            prefix = "n" + this._nextPrefixNumber;
            localName = qn.localName;
        }
        return prefix + ":" + localName;
    },

    scheme : function (url) {
        var s = url.substring(0, url.indexOf(':'));
        return s;
    },

    domain : function (url) {
        var d = url.substring(url.indexOf('://') + 3, url.indexOf('/',url.indexOf('://')+3));
        return d;
    },

    domainPort : function (url) {
        var d = this.domain(url);
        if (d.indexOf(":") >= 0)
        d = d.substring(d.indexOf(':') +1);
        return d;
    },

    domainNoPort : function (url) {
        var d = this.domain(url);
        if (d.indexOf(":") >= 0)
        d = d.substring(0, d.indexOf(':'));
        return d;
    },

    _serializeAnytype : function (name, value, namespace, optional) {
        // dynamically serialize an anyType value in xml, including setting xsi:type.
        if (optional && value == null) return "";
        var type = "xs:string";
        if (value == null) {
            value = "";
        } else if (typeof(value) == "number") {
            type = "xs:double";
        } else if (typeof(value) == "xml") {
            type = "xs:anyType";
            value = value.toXMLString();
        } else if (typeof(value) == "boolean") {
            type = "xs:boolean";
        } else if (typeof(value) == "object" && Date.prototype.isPrototypeOf(value)) {
            type = "xs:dateTime";
            value = WebService.utils.toXSdateTime(value);
        } else if (value.match(/^\s*true\s*$/g) != null) {
            type = "xs:boolean";
        } else if (value.match(/^\s*false\s*$/g) != null) {
            type = "xs:boolean";
        } else if (!isNaN(Date.parse(value))) {
            type = "xs:dateTime";
            value = WebService.utils.toXSdateTime(new Date(Date.parse(value)));
        } else if (value.match(/^\s*\-?\d*\-\d\d\-\d\dZ?\s*$/g) != null) {
            type = "xs:date";
        } else if (value.match(/^\s*\-?\d*\-\d\d\-\d\d[\+\-]\d\d:\d\d\s*$/g) != null) {
            type = "xs:date";
        } else if (value.match(/^\s*\d\d:\d\d:\d\d\.?\d*Z?\s*$/g) != null) {
            type = "xs:time";
        } else if (value.match(/^\s*\d\d:\d\d:\d\d\.?\d*[\+\-]\d\d:\d\d\s*$/g) != null) {
            type = "xs:time";
        } else if (value.match(/^\s*\-?\d*\-\d\d\-\d\dT\d\d:\d\d:\d\d\.?\d*Z?\s*$/g) != null) {
            type = "xs:dateTime";
        } else if (value.match(/^\s*\-?\d*\-\d\d\-\d\dT\d\d:\d\d:\d\d\.?\d*[\+\-]\d\d:\d\d\s*$/g) != null) {
            type = "xs:dateTime";
        } else if (value.match(/^\s*\d\d*\.?\d*\s*$/g) != null) {
            type = "xs:double";
        } else if (value.match(/^\s*\d*\.?\d\d*\s*$/g) != null) {
            type = "xs:double";
        } else if (value.match(/^\s*\</g) != null) {
    
            try {
                value = new XML(value).toXMLString();
                type = "xs:anyType";
            } catch (e) {}
        
        }
        if (type == "xs:string") {
            value = storexml._encodeXML(value);
        }
        var starttag =   "<" + name +
                     (namespace == "" ? "" : " xmlns='" + namespace + "'") +
                     " xsi:type='" + type + "'" +
                     " xmlns:xs='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
                     ">";
        var endtag = "</" + name + ">";
        return starttag + value + endtag;
    },

    
    // library function for dynamically converting an element with js:type annotation to a Javascript type.
    _convertJSType : function (element, isWrapped) {
        if (element == null) return "";
        var extractedValue = element.*.toString();
        var resultValue, i;
        var js = new Namespace("http://www.wso2.org/ns/jstype");
        var type = element.@js::type;
        if (type == null) {
            type = "#raw";
        } else {
            type = type.toString();
        }
        switch (type) {
            case "string":
                return extractedValue;
                break;
            case "number":
                return parseFloat(extractedValue);
                break;
            case "boolean":
                return extractedValue == "true" || extractedValue == "1";
                break;
            case "date":
                return WebService.utils.parseXSdateTime(extractedValue);
                break;
            case "array":
                resultValue = new Array();
                for (i=0; i<element.*.length(); i++) {
                    resultValue = resultValue.concat(WebService.utils._convertJSType(element[i]));
                }
                return(resultValue);
                break;
            case "object":
                resultValue = new Object();
                for (i=0; i<element.*.length(); i++) {
                    resultValue[element[i].name()] = WebService.utils._convertJSType(element[i]);
                }
                return(resultValue);
                break;
            case "xmlList":
                return element.*;
                break;
            case "xml":
                return element.*[0];
                break;
            case "#raw":
            default:
                if (isWrapped == true)
                    return element.*;
                else return element;
                break;
        }
    }

};



    
    
    
    
    
        
        
        
        
        
            
            
            
                
                
                    
                    
                
                
                
                
                    
                
            
        
    


