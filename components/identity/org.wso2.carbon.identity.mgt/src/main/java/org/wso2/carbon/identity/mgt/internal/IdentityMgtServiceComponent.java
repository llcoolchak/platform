/*
 * Copyright (c)  WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.mgt.internal;

import org.apache.axis2.engine.AxisObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.caching.core.CacheInvalidator;
import org.wso2.carbon.identity.mgt.IdentityMgtProcessor;
import org.wso2.carbon.identity.mgt.IdentityMgtConfig;
import org.wso2.carbon.identity.mgt.IdentityMgtEventListener;
import org.wso2.carbon.identity.mgt.IdentityMgtException;
import org.wso2.carbon.identity.mgt.constants.IdentityMgtConstants;
import org.wso2.carbon.identity.mgt.dto.ChallengeQuestionDTO;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.api.ClaimManager;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.api.ClaimMapping;
import org.wso2.carbon.user.core.listener.UserOperationEventListener;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 
 * @scr.component name="org.wso2.carbon.identity.mgt.internal.IdentityMgtServiceComponent"
 * immediate="true"
 * @scr.reference name="registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService" cardinality="1..1"
 * policy="dynamic" bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService"cardinality="1..1"
 * policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 * @scr.reference name="cache.invalidation.service"
 *                interface="org.wso2.carbon.caching.core.CacheInvalidator"
 *                cardinality="0..1" policy="dynamic"
 *                bind="setCacheInvalidator"
 *                unbind="removeCacheInvalidator"
 */

public class IdentityMgtServiceComponent {

    private static Log log = LogFactory.getLog(IdentityMgtServiceComponent.class);

    private static RealmService realmService;

    private static RegistryService registryService;

    private static IdentityMgtProcessor recoveryProcessor;

    private static ConfigurationContextService configurationContextService;

    private static CacheInvalidator cacheInvalidator;

    private ServiceRegistration serviceRegistration = null;
    
    private static IdentityMgtEventListener listener = null;

    protected void activate(ComponentContext context) {

        // Publish the OSGi service
        Properties props = new Properties();
        props.put(CarbonConstants.AXIS2_CONFIG_SERVICE, AxisObserver.class.getName());
        context.getBundleContext().registerService(AxisObserver.class.getName(),
                                                new IdentityMgtDeploymentInterceptor(), props);
        init();
        listener = new IdentityMgtEventListener();
        serviceRegistration =
                context.getBundleContext().registerService(UserOperationEventListener.class.getName(),
                        listener, null);

        log.debug("Identity Management bundle is activated");
    }


    protected void deactivate(ComponentContext context) {
        log.debug("Identity Management bundle is de-activated");
    }

    protected void setRegistryService(RegistryService registryService) {
        log.debug("Setting the Registry Service");
        IdentityMgtServiceComponent.registryService = registryService;
    }

    protected void unsetRegistryService(RegistryService registryService) {
        log.debug("UnSetting the Registry Service");
        IdentityMgtServiceComponent.registryService = null;
    }

    protected void setRealmService(RealmService realmService) {
        log.debug("Setting the Realm Service");
        IdentityMgtServiceComponent.realmService = realmService;
    }

    protected void unsetRealmService(RealmService realmService) {
        log.debug("UnSetting the Realm Service");
        IdentityMgtServiceComponent.realmService = null;
    }

    protected void setConfigurationContextService(ConfigurationContextService configurationContextService) {
        log.debug("Setting theConfigurationContext Service");
        IdentityMgtServiceComponent.configurationContextService = configurationContextService;

    }

    protected void unsetConfigurationContextService(ConfigurationContextService configurationContextService) {
        log.debug("UnSetting the  ConfigurationContext Service");
        IdentityMgtServiceComponent.configurationContextService = null;
    }

    public static RealmService getRealmService() {
        return realmService;
    }

    public static RegistryService getRegistryService() {
        return registryService;
    }

    public static IdentityMgtProcessor getRecoveryProcessor() {
        return recoveryProcessor;
    }

    public static ConfigurationContextService getConfigurationContextService() {
        return configurationContextService;
    }

    protected void setCacheInvalidator(CacheInvalidator invalidator) {
        cacheInvalidator = invalidator;
    }

    protected void removeCacheInvalidator(CacheInvalidator invalidator) {
        cacheInvalidator = null;
    }

    public static CacheInvalidator getCacheInvalidator() {
    	return cacheInvalidator;
    }
    
    private static void init(){

        Registry registry;
        recoveryProcessor = new IdentityMgtProcessor();
        IdentityMgtConfig.getInstance(realmService.getBootstrapRealmConfiguration());
        try {
            registry = IdentityMgtServiceComponent.getRegistryService().getConfigSystemRegistry();
            if(!registry.resourceExists(IdentityMgtConstants.IDENTITY_MANAGEMENT_PATH)){
                Collection questionCollection = registry.newCollection();
                registry.put(IdentityMgtConstants.IDENTITY_MANAGEMENT_PATH, questionCollection);
                loadDefaultChallenges();
            }
        } catch (RegistryException e) {
            log.error("Error while creating registry collection for org.wso2.carbon.identity.mgt component");
        }                  
    }


    private static void  loadDefaultChallenges(){

        List<ChallengeQuestionDTO> questionSetDTOs = new ArrayList<ChallengeQuestionDTO>();

        for(String challenge : IdentityMgtConstants.SECRET_QUESTIONS_SET01){
            ChallengeQuestionDTO dto = new ChallengeQuestionDTO();
            dto.setQuestion(challenge);
            dto.setPromoteQuestion(true);
            dto.setQuestionSetId(IdentityMgtConstants.DEFAULT_CHALLENGE_QUESTION_URI01);
            questionSetDTOs.add(dto);
        }

        for(String challenge : IdentityMgtConstants.SECRET_QUESTIONS_SET02){
            ChallengeQuestionDTO dto = new ChallengeQuestionDTO();
            dto.setQuestion(challenge);
            dto.setPromoteQuestion(true);
            dto.setQuestionSetId(IdentityMgtConstants.DEFAULT_CHALLENGE_QUESTION_URI02);
            questionSetDTOs.add(dto);
        }

        try {
            recoveryProcessor.getQuestionProcessor().setChallengeQuestions(questionSetDTOs.
                                    toArray(new ChallengeQuestionDTO[questionSetDTOs.size()]));
        } catch (IdentityMgtException e) {
            log.error("Error while promoting default challenge questions", e);
        }
    }
//
//    private static void processLockUsers() {
//
//        try{
//            UserStoreManager manager = realmService.getBootstrapRealm().getUserStoreManager();
//            String[] users = manager.getUserList(UserCoreConstants.ClaimTypeURIs.ACCOUNT_STATUS,
//                                                            UserCoreConstants.USER_LOCKED, null);
//
//            for(String user : users){
//                String userName = MultitenantUtils.getTenantAwareUsername(user);
//                String tenantDomain = MultitenantUtils.getTenantDomain(user);
//                Utils.lockUserAccount(userName, Utils.getTenantId(tenantDomain));
//            }
//        } catch (Exception e) {
//            log.error("Error while locking user account of locked users", e);
//        }
//    }
}
