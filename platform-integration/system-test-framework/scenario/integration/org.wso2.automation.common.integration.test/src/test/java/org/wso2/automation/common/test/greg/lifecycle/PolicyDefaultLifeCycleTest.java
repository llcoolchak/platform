/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.automation.common.test.greg.lifecycle;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.automation.common.test.greg.lifecycle.utils.Utils;
import org.wso2.carbon.admin.service.ActivitySearchAdminService;
import org.wso2.carbon.admin.service.LifeCycleAdminService;
import org.wso2.carbon.governance.custom.lifecycles.checklist.stub.CustomLifecyclesChecklistAdminServiceExceptionException;
import org.wso2.carbon.governance.custom.lifecycles.checklist.stub.beans.xsd.LifecycleBean;
import org.wso2.carbon.registry.activities.stub.RegistryExceptionException;
import org.wso2.carbon.registry.activities.stub.beans.xsd.ActivityBean;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.platform.test.core.ProductConstant;
import org.wso2.platform.test.core.utils.UserInfo;
import org.wso2.platform.test.core.utils.UserListCsvReader;
import org.wso2.platform.test.core.utils.environmentutils.EnvironmentBuilder;
import org.wso2.platform.test.core.utils.environmentutils.EnvironmentVariables;
import org.wso2.platform.test.core.utils.gregutils.RegistryProvider;

import java.rmi.RemoteException;
import java.util.Calendar;

public class PolicyDefaultLifeCycleTest {
    private String sessionCookie;

        private WSRegistryServiceClient registry;
        private LifeCycleAdminService lifeCycleAdminService;
        private ActivitySearchAdminService activitySearch;
        private UserInfo userInfo;

        private final String ASPECT_NAME = "ServiceLifeCycle";
        private final String ACTION_PROMOTE = "Promote";
        //    private final String ACTION_DEMOTE = "Demote";
        private String policyPathDev;


        @BeforeClass
        public void init() throws Exception {
            final int userId = 3;
            EnvironmentBuilder builder = new EnvironmentBuilder().greg(userId);
            EnvironmentVariables gregServer = builder.build().getGreg();
            userInfo = UserListCsvReader.getUserInfo(userId);
            sessionCookie = gregServer.getSessionCookie();
            lifeCycleAdminService = new LifeCycleAdminService(gregServer.getBackEndUrl());
            activitySearch = new ActivitySearchAdminService(gregServer.getBackEndUrl());
            registry = new RegistryProvider().getRegistry(userId, ProductConstant.GREG_SERVER_NAME);
            Registry governance = new RegistryProvider().getGovernance(registry, userId);

            String policyName = "LifeCycleTestPolicy.xml";
            policyPathDev = "/_system/governance" + Utils.addPolicy(policyName, governance);
            Thread.sleep(1000);

        }

        @Test(priority = 1, description = "Add lifecycle to a Policy")
        public void addLifecycle()
                throws RegistryException, CustomLifecyclesChecklistAdminServiceExceptionException,
                       RemoteException, InterruptedException, RegistryExceptionException {
            lifeCycleAdminService.addAspect(sessionCookie, policyPathDev, ASPECT_NAME);
            Thread.sleep(500);
            LifecycleBean lifeCycle = lifeCycleAdminService.getLifecycleBean(sessionCookie, policyPathDev);
            Resource service = registry.get(policyPathDev);
            Assert.assertNotNull(service, "Service Not found on registry path " + policyPathDev);
            Assert.assertEquals(service.getPath(), policyPathDev, "Service path changed after adding life cycle. " + policyPathDev);

            Assert.assertEquals(Utils.getLifeCycleState(lifeCycle), "Development", "LifeCycle State Mismatched");

            //Activity search
            Thread.sleep(1000 * 10);
            ActivityBean activityObj = activitySearch.getActivities(sessionCookie, userInfo.getUserName()
                    , policyPathDev, Utils.formatDate(Calendar.getInstance().getTime())
                    , "", ActivitySearchAdminService.FILTER_ASSOCIATE_ASPECT, 1);
            Assert.assertNotNull(activityObj, "Activity object null for Associate Aspect");
            Assert.assertNotNull(activityObj.getActivity(), "Activity list object null for Associate Aspect");
            Assert.assertTrue((activityObj.getActivity().length > 0), "Activity list object null");
            String activity = activityObj.getActivity()[0];
            Assert.assertTrue(activity.contains(userInfo.getUserName()), "User name not found on activity last activity. " + activity);
            Assert.assertTrue(activity.contains("associated the aspect ServiceLifeCycle"),
                              "associated the aspect ServiceLifeCycle not contain in last activity. " + activity);
            Assert.assertTrue(activity.contains("0m ago"), "current time not found on activity. " + activity);


        }

        @Test(priority = 2, dependsOnMethods = {"addLifecycle"}, description = "Promote to Testing")
        public void promoteToTesting()
                throws CustomLifecyclesChecklistAdminServiceExceptionException, RemoteException,
                       InterruptedException, RegistryException, RegistryExceptionException {

            lifeCycleAdminService.invokeAspect(sessionCookie, policyPathDev, ASPECT_NAME,
                                               ACTION_PROMOTE, null);
            Thread.sleep(2000);

            LifecycleBean lifeCycle = lifeCycleAdminService.getLifecycleBean(sessionCookie, policyPathDev);
            Resource service = registry.get(policyPathDev);
            Assert.assertNotNull(service, "Service Not found on registry path " + policyPathDev);

            Assert.assertEquals(Utils.getLifeCycleState(lifeCycle), "Testing", "LifeCycle State Mismatched");
            Assert.assertEquals(registry.get(policyPathDev).getPath(), policyPathDev,
                                "Resource not exist on trunk. Preserve original not working fine");

            //activity search for trunk
            Thread.sleep(1000 * 10);
            ActivityBean activityObjTrunk = activitySearch.getActivities(sessionCookie, userInfo.getUserName()
                    , policyPathDev, Utils.formatDate(Calendar.getInstance().getTime())
                    , "", ActivitySearchAdminService.FILTER_RESOURCE_UPDATE, 1);
            Assert.assertNotNull(activityObjTrunk, "Activity object null in trunk");
            Assert.assertNotNull(activityObjTrunk.getActivity(), "Activity list object null");
            Assert.assertTrue((activityObjTrunk.getActivity().length > 0), "Activity list object null");
            String activity = activityObjTrunk.getActivity()[0];
            Assert.assertTrue(activity.contains(userInfo.getUserName()), "Activity not found. User name not found on last activity. " + activity);
            Assert.assertTrue(activity.contains("has updated the resource"),
                              "Activity not found. has updated not contain in last activity. " + activity);
            Assert.assertTrue(activity.contains("0m ago"), "Activity not found. current time not found on last activity. " + activity);

        }

        @Test(priority = 2, dependsOnMethods = {"promoteToTesting"}, description = "Promote to Testing")
        public void promoteToProduction()
                throws CustomLifecyclesChecklistAdminServiceExceptionException, RemoteException,
                       InterruptedException, RegistryException, RegistryExceptionException {

            lifeCycleAdminService.invokeAspect(sessionCookie, policyPathDev, ASPECT_NAME,
                                               ACTION_PROMOTE, null);
            Thread.sleep(2000);

            Resource service = registry.get(policyPathDev);
            Assert.assertNotNull(service, "Service Not found on registry path " + policyPathDev);

            Assert.assertEquals(registry.get(policyPathDev).getPath(), policyPathDev,
                                "Resource not exist on trunk. Preserve original not working fine");

            //activity search for trunk
            Thread.sleep(1000 * 10);
            ActivityBean activityObjTrunk = activitySearch.getActivities(sessionCookie, userInfo.getUserName()
                    , policyPathDev, Utils.formatDate(Calendar.getInstance().getTime())
                    , "", ActivitySearchAdminService.FILTER_RESOURCE_UPDATE, 1);
            Assert.assertNotNull(activityObjTrunk, "Activity object null in trunk");
            Assert.assertNotNull(activityObjTrunk.getActivity(), "Activity list object null");
            Assert.assertTrue((activityObjTrunk.getActivity().length > 0), "Activity list object null");
            String activity = activityObjTrunk.getActivity()[0];
            Assert.assertTrue(activity.contains(userInfo.getUserName()), "Activity not found. User name not found on last activity. " + activity);
            Assert.assertTrue(activity.contains("has updated the resource"),
                              "Activity not found. has updated not contain in last activity. " + activity);
            Assert.assertTrue(activity.contains("0m ago"), "Activity not found. current time not found on last activity. " + activity);

        }

        @AfterClass
        public void cleanUp() throws RegistryException {
            if (policyPathDev != null) {
                registry.delete(policyPathDev);
            }
            registry = null;
            activitySearch = null;
            lifeCycleAdminService = null;
        }
}
