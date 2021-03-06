/*
*  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.identity.entitlement.dto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This encapsulates the <code>AttributeTreeNodeDTO</code> element of different categories
 */
public class PolicyEditorAttributeDTO {

    /**
     *  Array of attribute value tree which shows in the XACML policy editor UI
     */
    private AttributeTreeNodeDTO[] nodeDTOs = new AttributeTreeNodeDTO[]{};

    /**
     * supported target function with functionId and functionUri
     */
    private String[] supportedTargetFunctions = new String[]{};

    /**
     * supported rule function with functionId and functionUri
     */
    private String[] supportedRuleFunctions = new String[]{};

    /**
     * supported categories with categoryId and categoryUri
     */
    private String[] supportedCategories = new String[]{};

    /**
     * attributeId with data types. This helps to build basic policy editor
     */
    private String[] attributeIdDataTypes = new String[]{};

    public AttributeTreeNodeDTO[] getNodeDTOs() {
        return Arrays.copyOf(nodeDTOs, nodeDTOs.length);
    }

    public void addNodeDTO(AttributeTreeNodeDTO node){
        Set<AttributeTreeNodeDTO> valueNodes =
                new HashSet<AttributeTreeNodeDTO>(Arrays.asList(this.nodeDTOs));
        valueNodes.add(node);
        this.nodeDTOs = valueNodes.toArray(new AttributeTreeNodeDTO[valueNodes.size()]);
    }

    public String[] getSupportedRuleFunctions() {
        return Arrays.copyOf(supportedRuleFunctions, supportedRuleFunctions.length);
    }

    public void addSupportedRuleFunctions(List<String> ruleFunctions) {
        this.supportedRuleFunctions = ruleFunctions.toArray(new String[ruleFunctions.size()]);
    }

    public void addAttributeIdDataTypes(List<String> attributeIdDataTypes) {
        this.attributeIdDataTypes = attributeIdDataTypes.toArray(new String[attributeIdDataTypes.size()]);
    }

    public String[] getAttributeIdDataTypes() {
        return Arrays.copyOf(attributeIdDataTypes, attributeIdDataTypes.length);
    }

    public String[] getSupportedTargetFunctions() {
        return Arrays.copyOf(supportedTargetFunctions, supportedTargetFunctions.length);
    }
    
    public void addSupportedTargetFunctions(List<String> targetFunctions) {
        this.supportedTargetFunctions = targetFunctions.toArray(new String[targetFunctions.size()]);
    }

    public String[] getSupportedCategories() {
        return Arrays.copyOf(supportedCategories, supportedCategories.length);
    }

    public void addSupportedCategories(List<String>  supportedCategories) {
        this.supportedCategories = supportedCategories.toArray(new String[supportedCategories.size()]);
    }

}
