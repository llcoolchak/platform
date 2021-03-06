/*
 * Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.automation.utils.esb;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;
import org.wso2.carbon.automation.core.ProductConstant;

import javax.xml.namespace.QName;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StockQuoteClient {

    private static final Log log = LogFactory.getLog(StockQuoteClient.class);

    private ConfigurationContext cfgCtx;

    private ServiceClient serviceClient;
    private MultiThreadedHttpConnectionManager httpConnectionManager;
    private HttpClient httpClient;
    private List<Header> httpHeaders = new ArrayList<Header>();

    public StockQuoteClient() {
        String repositoryPath = ProductConstant.getModuleClientPath();

        File repository = new File(repositoryPath);
        log.info("Using the Axis2 repository path: " + repository.getAbsolutePath());
        httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpClient = new HttpClient(httpConnectionManager);
        try {
            cfgCtx =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(repository.getCanonicalPath(),
                                                                                         null);
            serviceClient = new ServiceClient(cfgCtx, null);
            serviceClient.getOptions().setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Constants.VALUE_TRUE);
            serviceClient.getOptions().setProperty(HTTPConstants.CACHED_HTTP_CLIENT, httpClient);
            log.info("Sample client initialized successfully...");
        } catch (Exception e) {
            log.error("Error while initializing the StockQuoteClient", e);
        }
    }

    public void setHeader(String localName, String ns, String value) throws AxisFault {
        serviceClient.addStringHeader(new QName(ns, localName), value);
    }

    public OMElement sendSimpleStockQuoteRequest(String trpUrl, String addUrl, String symbol)
            throws AxisFault {

        Options options = getOptions(trpUrl, addUrl);
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(createStandardRequest(symbol));
    }

    public OMElement sendSimpleStockQuoteRequest_REST(String trpUrl, String addUrl, String symbol)
            throws AxisFault {

        Options options = getRESTEnabledOptions(trpUrl, addUrl);
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(createStandardRequest(symbol));
    }

    public OMElement sendSimpleQuoteRequest(String trpUrl, String addUrl, String symbol)
            throws AxisFault {

        Options options = getOptions(trpUrl, addUrl, "getSimpleQuote");
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(createStandardSimpleRequest(symbol));
    }

    public OMElement sendSimpleQuoteRequest_REST(String trpUrl, String addUrl, String symbol)
            throws AxisFault {

        Options options = getRESTEnabledOptions(trpUrl, addUrl, "getSimpleQuote");
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(createStandardSimpleRequest(symbol));
    }

    public OMElement sendSimpleStockQuoteSoap11(String trpUrl, String addUrl, String symbol)
            throws AxisFault {

        Options options = getOptions(trpUrl, addUrl);
        serviceClient.setOptions(options);
        serviceClient.getOptions()
                .setSoapVersionURI(org.apache.axiom.soap.SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        return serviceClient.sendReceive(createStandardRequest(symbol));
    }


    public OMElement sendSimpleStockQuoteSoap12(String trpUrl, String addUrl, String symbol)
            throws AxisFault {

        Options options = getOptions(trpUrl, addUrl);
        serviceClient.setOptions(options);
        serviceClient.getOptions()
                .setSoapVersionURI(org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        return serviceClient.sendReceive(createStandardRequest(symbol));
    }

    public OMElement sendSimpleStockQuoteRequest(String trpUrl, String addUrl, OMElement payload)
            throws AxisFault {

        Options options = getOptions(trpUrl, addUrl);
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(payload);
    }

    public OMElement sendSimpleStockQuoteRequest_REST(String trpUrl, String addUrl,
                                                      OMElement payload)
            throws AxisFault {

        Options options = getRESTEnabledOptions(trpUrl, addUrl);
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(payload);
    }

    public OMElement sendSecuredSimpleStockQuoteRequest(String trpUrl, String addUrl,
                                                        String symbol, String policyPath)
            throws Exception {

        Options options = getOptions(trpUrl, addUrl);

        if (policyPath != null && !policyPath.equals("")) {
            serviceClient.engageModule("addressing");
            serviceClient.engageModule("rampart");
            options.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy(policyPath));
        }

        serviceClient.setOptions(options);
        return serviceClient.sendReceive(createStandardRequest(symbol));
    }

    public OMElement sendCustomQuoteRequest(String trpUrl, String addUrl, String symbol)
            throws AxisFault {

        Options options = getOptions(trpUrl, addUrl);
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(createCustomQuoteRequest(symbol));
    }

    public OMElement send(String trpUrl, String addUrl, String action, OMElement payload)
            throws AxisFault {

        Options options = getOptions(trpUrl, addUrl, action);
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(payload);
    }


    public OMElement sendCustomQuoteRequest_REST(String trpUrl, String addUrl, String symbol)
            throws AxisFault {

        Options options = getRESTEnabledOptions(trpUrl, addUrl);
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(createCustomQuoteRequest(symbol));
    }

    public OMElement sendMultipleQuoteRequest(String trpUrl, String addUrl, String symbol, int n)
            throws AxisFault {

        Options options = getOptions(trpUrl, addUrl);
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(createMultipleQuoteRequest(symbol, n));
    }

    public OMElement sendMultipleQuoteRequest_REST(String trpUrl, String addUrl, String symbol,
                                                   int n)
            throws AxisFault {

        Options options = getRESTEnabledOptions(trpUrl, addUrl);
        serviceClient.setOptions(options);
        return serviceClient.sendReceive(createMultipleQuoteRequest(symbol, n));
    }

    private Options getOptions(String trpUrl, String addUrl) throws AxisFault {
        Options originalOptions = serviceClient.getOptions();

        Options options = new Options();

        options.setSoapVersionURI(originalOptions.getSoapVersionURI());


        if (trpUrl != null && !"null".equals(trpUrl)) {
            options.setProperty(Constants.Configuration.TRANSPORT_URL, trpUrl);
        }

        if (addUrl != null && !"null".equals(addUrl)) {
            serviceClient.engageModule("addressing");
            options.setTo(new EndpointReference(addUrl));
        }

        options.setAction("urn:getQuote");
        if (httpHeaders.size() > 0) {
            options.setProperty(HTTPConstants.HTTP_HEADERS, httpHeaders);
        }
        return options;
    }

    private Options getRESTEnabledOptions(String trpUrl, String addUrl) throws AxisFault {
        Options originalOptions = serviceClient.getOptions();

        Options options = new Options();
        options.setProperty("enableREST", "true");
        options.setSoapVersionURI(originalOptions.getSoapVersionURI());


        if (trpUrl != null && !"null".equals(trpUrl)) {
            options.setProperty(Constants.Configuration.TRANSPORT_URL, trpUrl);
        }

        if (addUrl != null && !"null".equals(addUrl)) {
            serviceClient.engageModule("addressing");
            options.setTo(new EndpointReference(addUrl));
        }

        options.setAction("urn:getQuote");
        if (httpHeaders.size() > 0) {
            options.setProperty(HTTPConstants.HTTP_HEADERS, httpHeaders);
        }
        return options;
    }

    private Options getOptions(String trpUrl, String addUrl, String operation) throws AxisFault {
        Options options = new Options();

        if (trpUrl != null && !"null".equals(trpUrl)) {
            options.setProperty(Constants.Configuration.TRANSPORT_URL, trpUrl);
        }

        if (addUrl != null && !"null".equals(addUrl)) {
            serviceClient.engageModule("addressing");
            options.setTo(new EndpointReference(addUrl));
        }

        options.setAction("urn:" + operation);
        if (httpHeaders.size() > 0) {
            options.setProperty(HTTPConstants.HTTP_HEADERS, httpHeaders);
        }
        return options;
    }

    private Options getRESTEnabledOptions(String trpUrl, String addUrl, String operation)
            throws AxisFault {
        Options options = new Options();
        options.setProperty("enableREST", "true");
        if (trpUrl != null && !"null".equals(trpUrl)) {
            options.setProperty(Constants.Configuration.TRANSPORT_URL, trpUrl);
        }

        if (addUrl != null && !"null".equals(addUrl)) {
            serviceClient.engageModule("addressing");
            options.setTo(new EndpointReference(addUrl));
        }

        options.setAction("urn:" + operation);
        if (httpHeaders.size() > 0) {
            options.setProperty(HTTPConstants.HTTP_HEADERS, httpHeaders);
        }
        return options;
    }

    public void destroy() {
        try {
            serviceClient.cleanup();

            cfgCtx.cleanupContexts();
            cfgCtx.terminate();
        } catch (AxisFault axisFault) {
            log.error("Error while cleaning up the service client", axisFault);
        }
        httpConnectionManager.closeIdleConnections(0);
        httpConnectionManager.shutdown();
        serviceClient = null;
        cfgCtx = null;
    }

    public void addHttpHeader(String name, String value) {
        httpHeaders.add(new Header(name, value));
    }

    private OMElement createStandardRequest(String symbol) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.samples", "ns");
        OMElement method = fac.createOMElement("getQuote", omNs);
        OMElement value1 = fac.createOMElement("request", omNs);
        OMElement value2 = fac.createOMElement("symbol", omNs);

        value2.addChild(fac.createOMText(value1, symbol));
        value1.addChild(value2);
        method.addChild(value1);

        return method;
    }

    private OMElement createStandardSimpleRequest(String symbol) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.samples", "ns");
        OMElement method = fac.createOMElement("getSimpleQuote", omNs);
        OMElement value1 = fac.createOMElement("symbol", omNs);

        value1.addChild(fac.createOMText(method, symbol));
        method.addChild(value1);

        return method;
    }

    private OMElement createMultipleQuoteRequest(String symbol, int iterations) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.samples", "ns");
        OMElement method = fac.createOMElement("getQuote", omNs);

        for (int i = 0; i < iterations; i++) {
            OMElement value1 = fac.createOMElement("request", omNs);
            OMElement value2 = fac.createOMElement("symbol", omNs);
            value2.addChild(fac.createOMText(value1, symbol));
            value1.addChild(value2);
            method.addChild(value1);
        }
        return method;
    }

    private OMElement createCustomQuoteRequest(String symbol) {
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace ns = factory.createOMNamespace("http://services.samples", "ns");
        OMElement chkPrice = factory.createOMElement("CheckPriceRequest", ns);
        OMElement code = factory.createOMElement("Code", ns);
        chkPrice.addChild(code);
        code.setText(symbol);
        return chkPrice;
    }

    private static Policy loadPolicy(String xmlPath) throws Exception {

        StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
        return PolicyEngine.getPolicy(builder.getDocumentElement());
    }

    public ServiceClient getServiceClient() {
        return this.serviceClient;
    }

}
