var saveAPI = function(apiData) {
    var log = new Log();
    var apiProvider = require("/core/greg/greg.js").getAPIProviderObj();
    var user = require("/core/users/users.js").getUser();
    apiData.provider = user.username;
    apiData.request = request;
    try {
        var success = apiProvider.addAPI(apiData);
        if (log.isDebugEnabled()) {
            log.debug("addAPI : " + apiData.apiName + "-" + apiData.version);
        }
        if (success) {
            return {
                error:false
            };
        } else {
            return {
                error:true
            };
        }
    } catch (e) {
        log.error(e.message);
        return {
            error:e

        };
    }
};

var isContextExist = function(context) {
    try {
        var contextExist = apiProvider.isContextExist(context);
        if (log.isDebugEnabled()) {
            log.debug("isContext exist for : " + context + " : " + contextExist);
        }
        return {
            error:false,
            contextExist:contextExist
        };
    } catch (e) {
        log.error(e.message);
        return {
            error:e

        };
    }

};
