package org.wso2.carbon.automation.core.utils.frameworkutils.productsetters;

import org.wso2.carbon.automation.core.utils.environmentutils.EnvironmentBuilder;
import org.wso2.carbon.automation.core.utils.environmentutils.ProductUrlGeneratorUtil;
import org.wso2.carbon.automation.core.utils.frameworkutils.EnvironmentSetter;
import org.wso2.carbon.automation.core.utils.frameworkutils.productvariables.ProductVariables;
import org.wso2.carbon.automation.core.utils.frameworkutils.productvariables.WorkerVariables;

import java.util.Properties;

public class GregSetter extends EnvironmentSetter {
    ProductVariables productVariables = new ProductVariables();
    WorkerVariables workerVariables = new WorkerVariables();
    EnvironmentBuilder environmentBuilder = new EnvironmentBuilder();
    public String managerHostName;
    public String managerHttpPort;
    public String managerHttpsPort;
    public String managerWebContextRoot;
    public String workerHostName = null;
    public String workerHttpPort = null;
    public String workerHttpsPort = null;
    public String workerWebContextRoot = null;
    public Properties properties;

    public GregSetter() {
        this.properties = new ProductUrlGeneratorUtil().getStream();

        String hostNames;
        if (Boolean.parseBoolean(prop.getProperty("stratos.test"))) {
            hostNames = (properties.getProperty("greg.service.host.name", "governance.stratoslive.wso2.com"));
        } else {
            hostNames = (properties.getProperty("greg.host.name", "localhost"));
        }
        String httpPorts = (prop.getProperty("greg.http.port", "9763"));
        String httpsPorts = (prop.getProperty("greg.https.port", "9400"));
        String webContextRoots = (prop.getProperty("greg.webContext.root", null));

        if (hostNames.contains(",")) {
            managerHostName = hostNames.split(",")[0];
            workerHostName = hostNames.split(",")[1];
        } else {
            managerHostName = hostNames;
        }
        if (httpPorts.contains(",")) {
            managerHttpPort = httpPorts.split(",")[0];
            workerHttpPort = httpPorts.split(",")[1];
        } else {
            managerHttpPort = httpPorts;
        }
        if (httpsPorts.contains(",")) {
            managerHttpsPort = httpsPorts.split(",")[0];
            workerHttpsPort = httpsPorts.split(",")[1];
        } else {
            managerHttpsPort = httpsPorts;
        }
        if (webContextRoots != null) {
            if (webContextRoots.contains(",")) {
                managerWebContextRoot = webContextRoots.split(",")[0];
                workerWebContextRoot = webContextRoots.split(",")[1];
            }
        } else {
            managerWebContextRoot = webContextRoots;
        }
    }

//    public ProductVariables getProductVariables() {
//        String hostName = null;
//        ProductUrlGeneratorUtil productUrlGeneratorUtil = new ProductUrlGeneratorUtil();
//        prop = productUrlGeneratorUtil.getStream();
//        if (!Boolean.parseBoolean(prop.getProperty("stratos.test"))) {
//            hostName = (prop.getProperty("greg.host.name", "localhost"));
//        } else {
//            hostName = prop.getProperty("greg.service.host.name", "governance.stratoslive.wso2.com");
//        }
//        String httpPort = (prop.getProperty("greg.http.port", "9763"));
//        String httpsPort = (prop.getProperty("greg.https.port", "9400"));
//        String webContextRoot = (prop.getProperty("greg.webContext.root", null));
//        productVariables.setProductVariables(hostName, httpPort, httpsPort, webContextRoot, productUrlGeneratorUtil.getBackendUrl(httpsPort, hostName, webContextRoot));
//        return productVariables;
//    }

    public ProductVariables getProductVariables() {
        ProductUrlGeneratorUtil productUrlGeneratorUtil = new ProductUrlGeneratorUtil();
        this.properties = new ProductUrlGeneratorUtil().getStream();

        productVariables.setProductVariables
                (this.managerHostName, this.managerHttpPort, this.managerHttpsPort, this.managerWebContextRoot,
                 productUrlGeneratorUtil.getBackendUrl(managerHttpsPort, managerHostName,
                                                       managerWebContextRoot));


        return productVariables;
    }

    public WorkerVariables getWorkerVariables() {
        ProductUrlGeneratorUtil productUrlGeneratorUtil = new ProductUrlGeneratorUtil();
        this.properties = new ProductUrlGeneratorUtil().getStream();

        if (environmentBuilder.getFrameworkSettings().getEnvironmentSettings().isClusterEnable()) {

            workerVariables.setWorkerVariables(workerHostName, workerHttpPort, workerHttpsPort, workerWebContextRoot,
                                               productUrlGeneratorUtil.getBackendUrl(workerHttpsPort, workerHostName,
                                                                                     workerWebContextRoot));
        }
        return workerVariables;
    }
}
