/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.wso2.carbon.identity.entitlement.proxy;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheManager;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.caching.core.identity.IdentityCacheEntry;
import org.wso2.carbon.caching.core.identity.IdentityCacheKey;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

class PEPProxyCache {

    private SimpleCache simpleCache;
    private Cache carbonCache;
    private int invalidationInterval = 0;

    PEPProxyCache(String enableCaching, int invalidationInterval, int maxEntries){
        if(enableCaching.equalsIgnoreCase("simple")){
            simpleCache = new SimpleCache(maxEntries);
            this.invalidationInterval = invalidationInterval;
        }else if(enableCaching.equalsIgnoreCase("carbon")){
            carbonCache = getCommonCache(ProxyConstants.DECISION_CACHE);
        }
    }

    private class SimpleCache<K, V> extends LinkedHashMap<K, V> {

        private static final long serialVersionUID = 1L;

        private int maxEntries;

        public SimpleCache(int maxEntries) {
            // removeEldestEntry() is called after a put(). To allow maxEntries in
            // cache, capacity should be maxEntries + 1 (for the entry which will be
            // removed). Load factor is taken as 1 because size is fixed. This is
            // less space efficient when very less entries are present, but there
            // will be no effect on time complexity for get(). The third parameter
            // in the base class constructor says that this map is
            // insertion-order oriented.
            super(maxEntries + 1, 1, false);
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(Entry<K, V> eldest) {
            // After size exceeds max entries, this statement returns true and the
            // oldest value will be removed. Behaves like a queue, the first
            // inserted value will go away.
            return size() > maxEntries;
        }


    }

    /**
         * Encapsulate the XACML Decision with XACML response and time stamp
         */
    private class EntitlementDecision {

       /**
             * XACML response
             */
        private String response;

       /**
             * time stamp
             */
        private long cachedTime;

        EntitlementDecision(String response,long cachedTime){
            this.response = response;
            this.cachedTime = cachedTime;
        }

        public String getResponse() {
            return response;
        }

        public long getCachedTime() {
            return cachedTime;
        }
    }

  /**
     * Return an instance of a named cache that is common to all tenants.
     *
     * @param name the name of the cache.
     * @return the named cache instance.
     */
    private Cache getCommonCache(String name) {
        // We create a single cache for all tenants. It is not a good choice to create per-tenant
        // caches in this case. We qualify tenants by adding the tenant identifier in the cache key.
        PrivilegedCarbonContext currentContext = PrivilegedCarbonContext.getCurrentContext();
        currentContext.startTenantFlow();
        try {
            currentContext.setTenantId(MultitenantConstants.SUPER_TENANT_ID);
            return CacheManager.getInstance().getCache(name);
        } finally {
            currentContext.endTenantFlow();
        }
    }

    void put(String key,String entry){
        if(simpleCache != null){
            EntitlementDecision entitlementDecision = new EntitlementDecision(entry,Calendar.getInstance().getTimeInMillis());
            simpleCache.put(key,entitlementDecision);
        }else if(carbonCache != null){
            int tenantId = CarbonContext.getCurrentContext().getTenantId();
            IdentityCacheKey identityKey = new IdentityCacheKey(tenantId,key);
            IdentityCacheEntry identityEntry = new IdentityCacheEntry(entry);
            carbonCache.put(identityKey,identityEntry);
        }
    }

    String get(String key){
        if(simpleCache != null){
            EntitlementDecision entitlementDecision = (EntitlementDecision)simpleCache.get(key);
            if(entitlementDecision != null &&
               (entitlementDecision.getCachedTime() + (long) invalidationInterval >
                Calendar.getInstance().getTimeInMillis())){
                return entitlementDecision.getResponse();
            }
        }else if(carbonCache != null){
            int tenantId = CarbonContext.getCurrentContext().getTenantId();
            IdentityCacheKey identityKey = new IdentityCacheKey(tenantId,key);
            IdentityCacheEntry identityCacheEntry = (IdentityCacheEntry)carbonCache.get(identityKey);
            if(identityCacheEntry != null){
                return identityCacheEntry.getCacheEntry();
            }else{
                return null;
            }
        }
        return null;
    }

    void clear(){
        if(simpleCache != null){
            simpleCache =  new SimpleCache(simpleCache.maxEntries);
        }else if(carbonCache != null){
            carbonCache.clear();
        }
    }

}
