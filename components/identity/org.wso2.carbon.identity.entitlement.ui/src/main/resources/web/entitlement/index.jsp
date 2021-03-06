<!--
 ~ Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 ~
 ~ WSO2 Inc. licenses this file to you under the Apache License,
 ~ Version 2.0 (the "License"); you may not use this file except
 ~ in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~    http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing,
 ~ software distributed under the License is distributed on an
 ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ~ KIND, either express or implied.  See the License for the
 ~ specific language governing permissions and limitations
 ~ under the License.
 -->
<%@page import="org.wso2.carbon.ui.util.CharacterEncoder"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.identity.entitlement.ui.client.EntitlementPolicyAdminServiceClient" %>
<%@ page import="java.util.ResourceBundle"%><jsp:include page="../dialog/display_messages.jsp"/>
<%@ page import="java.lang.Exception" %>
<%@ page import="org.wso2.carbon.identity.entitlement.stub.dto.PaginatedPolicySetDTO" %>
<%@ page import="org.wso2.carbon.identity.entitlement.stub.dto.PolicyDTO" %>
<%@ page import="org.wso2.carbon.identity.entitlement.stub.dto.AttributeTreeNodeDTO" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.wso2.carbon.identity.entitlement.stub.dto.PolicyEditorAttributeDTO" %>
<%@ page import="org.wso2.carbon.identity.entitlement.stub.dto.AttributeTreeNodeDTO" %>
<%@ page import="org.wso2.carbon.identity.entitlement.ui.EntitlementPolicyConstants" %>
<%@ page import="org.wso2.carbon.identity.entitlement.ui.PolicyEditorConstants" %>
<jsp:useBean id="entitlementPolicyBean" type="org.wso2.carbon.identity.entitlement.ui.EntitlementPolicyBean"
             class="org.wso2.carbon.identity.entitlement.ui.EntitlementPolicyBean" scope="session"/>
<jsp:setProperty name="entitlementPolicyBean" property="*" />

<%
    entitlementPolicyBean.cleanEntitlementPolicyBean();
    String serverURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
    ConfigurationContext configContext =
            (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
    String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
    String forwardTo = null;
    PaginatedPolicySetDTO paginatedPolicySetDTO = null;
    String globalPolicyCombiningAlgorithm = null;
    String [] policyCombiningAlgorithms = new String[]{PolicyEditorConstants.CombiningAlog.DENY_OVERRIDE_ID,
                            PolicyEditorConstants.CombiningAlog.PERMIT_OVERRIDE_ID,
                            PolicyEditorConstants.CombiningAlog.FIRST_APPLICABLE_ID,
                            PolicyEditorConstants.CombiningAlog.PERMIT_UNLESS_DENY_ID,
                            PolicyEditorConstants.CombiningAlog.DENY_UNLESS_PERMIT_ID,
                            PolicyEditorConstants.CombiningAlog.ORDER_PERMIT_OVERRIDE_ID,
                            PolicyEditorConstants.CombiningAlog.ORDER_DENY_OVERRIDE_ID,
                            PolicyEditorConstants.CombiningAlog.ONLY_ONE_APPLICABLE_ID};

    PolicyDTO[] policies = null;
    String[] policyTypes = new String[] {"Policy", "PolicySet", "Active" , "Promoted"};
    String BUNDLE = "org.wso2.carbon.identity.entitlement.ui.i18n.Resources";
	ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE, request.getLocale());
    globalPolicyCombiningAlgorithm = request.getParameter("globalAlgorithmName");
    session.removeAttribute("publishAllPolicies");
    session.removeAttribute("selectedPolicies");
    session.removeAttribute(EntitlementPolicyConstants.ENTITLEMENT_PUBLISHER_MODULE);

    int numberOfPages = 0;
    String pageNumber = request.getParameter("pageNumber");
    if (pageNumber == null) {
        pageNumber = "0";
    }
    int pageNumberInt = 0;
    try {
        pageNumberInt = Integer.parseInt(pageNumber);
    } catch (NumberFormatException ignored) {
    }

    String policyTypeFilter = request.getParameter("policyTypeFilter");
    if (policyTypeFilter == null || "".equals(policyTypeFilter)) {
        policyTypeFilter = "ALL";
    }
    String policySearchString = request.getParameter("policySearchString");
    if (policySearchString == null) {
        policySearchString = "";
    } else {
        policySearchString = policySearchString.trim();
    }

    String paginationValue = "policyTypeFilter=" + policyTypeFilter +
                             "&policySearchString=" + policySearchString;

    try {
        EntitlementPolicyAdminServiceClient client = new EntitlementPolicyAdminServiceClient(cookie, serverURL, configContext);
        paginatedPolicySetDTO = client.getAllPolicies(policyTypeFilter, policySearchString, pageNumberInt);
        if(globalPolicyCombiningAlgorithm != null && globalPolicyCombiningAlgorithm.trim().length() > 0){
            client.setGlobalPolicyAlgorithm(globalPolicyCombiningAlgorithm);
        } else {
            globalPolicyCombiningAlgorithm = client.getGlobalPolicyAlgorithm();              
        }

        PolicyEditorAttributeDTO[] policyAttributeDTOs =  client.getPolicyAttributeValues();
        if(policyAttributeDTOs != null){

            Map<String, String> targetFunctionMap = new HashMap<String, String>();
            Map<String, String> ruleFunctionMap = new HashMap<String, String>();
            Map<String, String> categoryMap = new HashMap<String, String>();
            Map<String, String> attributeIdMap = new HashMap<String, String>();
            Map<String, String> attributeIdDataTypeMap = new HashMap<String, String>();

            for(PolicyEditorAttributeDTO policyAttributeDTO : policyAttributeDTOs){

                AttributeTreeNodeDTO[] nodeDTOs =  policyAttributeDTO.getNodeDTOs();
                if(nodeDTOs != null){
                    for(AttributeTreeNodeDTO nodeDTO : nodeDTOs){
                        entitlementPolicyBean.putAttributeValueNodeMap(nodeDTO.getCategoryId(), nodeDTO);
                        entitlementPolicyBean.addDefaultAttributeId(nodeDTO.getCategoryId(),
                                                                    nodeDTO.getDefaultAttributeId());
                        entitlementPolicyBean.addDefaultDataType(nodeDTO.getCategoryId(),
                                                                    nodeDTO.getDefaultAttributeDataType());
                        // This basic editor is always assume there is category called Subject
                        if("Subject".equals(nodeDTO.getCategoryId())){
                            String[] attributeIdArray = nodeDTO.getSupportedAttributeIds();
                            if(attributeIdArray != null && attributeIdArray.length > 1){
                                for(int i = 0; i < attributeIdArray.length-1; i = i+2){
                                    attributeIdMap.put(attributeIdArray[i], attributeIdArray[i+1]);
                                }
                            }
                        }
                    }
                }
                
                String[] targetFunctionArray = policyAttributeDTO.getSupportedTargetFunctions();
                if(targetFunctionArray != null && targetFunctionArray.length > 1){
                    for(int i = 0; i < targetFunctionArray.length-1; i = i+2){
                        targetFunctionMap.put(targetFunctionArray[i], targetFunctionArray[i+1]);
                    }
                }

                String[] ruleFunctionArray = policyAttributeDTO.getSupportedRuleFunctions();
                if(ruleFunctionArray != null && ruleFunctionArray.length > 1){
                    for(int i = 0; i < ruleFunctionArray.length-1; i = i+2){
                        ruleFunctionMap.put(ruleFunctionArray[i], ruleFunctionArray[i+1]);
                    }
                }

                String[] categoryArray = policyAttributeDTO.getSupportedCategories();
                if(categoryArray != null && categoryArray.length > 1){
                    for(int i = 0; i < categoryArray.length -1; i = i+2){
                        categoryMap.put(categoryArray[i], categoryArray[i+1]);
                    }
                }

                String[] attributeIdDataTypeArray = policyAttributeDTO.getSupportedCategories();
                if(attributeIdDataTypeArray != null && attributeIdDataTypeArray.length > 1){
                    for(int i = 0; i < attributeIdDataTypeArray.length -1; i = i+2){
                        attributeIdDataTypeMap.put(attributeIdDataTypeArray[i], attributeIdDataTypeArray[i+1]);
                    }
                }
            }

            entitlementPolicyBean.setTargetFunctionMap(targetFunctionMap);
            entitlementPolicyBean.setRuleFunctionMap(ruleFunctionMap);
            entitlementPolicyBean.setCategoryMap(categoryMap);
            entitlementPolicyBean.setAttributeIdMap(attributeIdMap);
            entitlementPolicyBean.setAttributeIdDataTypeMap(attributeIdDataTypeMap);
        }
        policies = paginatedPolicySetDTO.getPolicySet();
        numberOfPages = paginatedPolicySetDTO.getNumberOfPages();

    } catch (Exception e) {
    	String message = resourceBundle.getString("error.while.loading.policy")+ " " + e.getMessage();
        CarbonUIMessage.sendCarbonUIMessage(message, CarbonUIMessage.ERROR, request, e);
        forwardTo = "../admin/error.jsp";
%>

<script type="text/javascript">
    function forward() {
        location.href = "<%=forwardTo%>";
    }
</script>

<script type="text/javascript">
    forward();
</script>
<%
    }
%>

<fmt:bundle basename="org.wso2.carbon.identity.entitlement.ui.i18n.Resources">
    <carbon:breadcrumb
            label="ent.policies"
            resourceBundle="org.wso2.carbon.identity.entitlement.ui.i18n.Resources"
            topPage="true"
            request="<%=request%>"/>

<script type="text/javascript" src="../carbon/admin/js/breadcrumbs.js"></script>
<script type="text/javascript" src="../carbon/admin/js/cookies.js"></script>
<script type="text/javascript" src="../carbon/admin/js/main.js"></script>
<script type="text/javascript">

    var allPolicesSelected = false;        

    function removePolicies() {


        location.href = "remove-policy.jsp";

    }

    function edit(policy) {
        location.href = "edit-policy.jsp?policyid=" + policy;
    }

    function enable(policy) {
        location.href = "enable-disable-policy.jsp?policyid=" + policy +"&action=enable";
    }
    
    function disable(policy) {
        location.href = "enable-disable-policy.jsp?policyid=" + policy +"&action=disable";
    }

    function promote(policy) {
        location.href = "enable-disable-policy.jsp?policyid=" + policy +"&action=promote";
    }

    function dePromote(policy) {
        location.href = "enable-disable-policy.jsp?policyid=" + policy +"&action=depromote";
    }

    function syncPromote(policy) {
        location.href = "enable-disable-policy.jsp?policyid=" + policy +"&action=sync";
    }

    function setPolicyCombineAlgorithm() {
        var comboBox = document.getElementById("globalAlgorithmName");
        var globalAlgorithmName = comboBox[comboBox.selectedIndex].value;
        location.href = 'index.jsp?globalAlgorithmName=' + globalAlgorithmName;
    }

    function deleteServices() {
        var selected = false;
        if (document.policyForm.policies[0] != null) { // there is more than 1 policy
            for (var j = 0; j < document.policyForm.policies.length; j++) {
                selected = document.policyForm.policies[j].checked;
                if (selected) break;
            }
        } else if (document.policyForm.policies != null) { // only 1 policy
            selected = document.policyForm.policies.checked;
        }
        if (!selected) {
            CARBON.showInfoDialog('<fmt:message key="select.policies.to.be.deleted"/>');
            return;
        }
        if (allPolicesSelected) {
            CARBON.showConfirmationDialog("<fmt:message key="delete.all.policies.prompt"/>",function() {
                document.policyForm.action = "remove-policy.jsp";
                document.policyForm.submit();
            });
        } else {
            CARBON.showConfirmationDialog("<fmt:message key="delete.services.on.page.prompt"/>",function() {
                document.policyForm.action = "remove-policy.jsp";
                document.policyForm.submit();
            });
        }
    }

    function publishPolicies(){

        var selected = false;
        if (document.policyForm.policies[0] != null) { // there is more than 1 policy
            for (var j = 0; j < document.policyForm.policies.length; j++) {
                selected = document.policyForm.policies[j].checked;
                if (selected) break;
            }
        } else if (document.policyForm.policies != null) { // only 1 policy
            selected = document.policyForm.policies.checked;
        }
        if (!selected) {
            CARBON.showInfoDialog('<fmt:message key="select.policies.to.be.published"/>');
            return;
        }
        if (allPolicesSelected) {
            CARBON.showConfirmationDialog("<fmt:message key="publish.all.policies.prompt"/>",function() {
                document.policyForm.action = "policy-publish.jsp";
                document.policyForm.submit();
            });
        } else {
            CARBON.showConfirmationDialog("<fmt:message key="publish.services.on.page.prompt"/>",function() {
                document.policyForm.action = "policy-publish.jsp";
                document.policyForm.submit();
            });
        }
    }

    function publishAllPolicies() {
        CARBON.showConfirmationDialog("<fmt:message key="publish.all.policies.prompt"/>",function() {
            location.href = "policy-publish.jsp?publishAllPolicies=true";
        });                
    }

      function publishPolicy(policy) {
        location.href = "policy-publish.jsp?policyid=" + policy;
    }

    function selectAllInThisPage(isSelected) {
        allPolicesSelected = false;
        if (document.policyForm.policies != null &&
            document.policyForm.policies[0] != null) { // there is more than 1 service
            if (isSelected) {
                for (var j = 0; j < document.policyForm.policies.length; j++) {
                    document.policyForm.policies[j].checked = true;
                }
            } else {
                for (j = 0; j < document.policyForm.policies.length; j++) {
                    document.policyForm.policies[j].checked = false;
                }
            }
        } else if (document.policyForm.policies != null) { // only 1 service
            document.policyForm.policies.checked = isSelected;
        }
        return false;
    }

    function selectAllInAllPages() {
        selectAllInThisPage(true);
        allPolicesSelected = true;
        return false;
    }

    function resetVars() {
        allPolicesSelected = false;

        var isSelected = false;
        if (document.policyForm.policies[0] != null) { // there is more than 1 service
            for (var j = 0; j < document.policyForm.policies.length; j++) {
                if (document.policyForm.policies[j].checked) {
                    isSelected = true;
                }
            }                           
        } else if (document.policyForm.policies != null) { // only 1 service
            if (document.policyForm.policies.checked) {
                isSelected = true;
            }
        }
        return false;
    }

    function searchServices() {

        jQuery('#searchTable > tbody:last').append('<tr><td><input type="hidden" name="policyOrder" id="policyOrder" value="' + orderRuleElement() +'"/></td></tr>') ;
        document.searchForm.submit();
    }

    function orderPolicies(){
//        location.href = 'update-policy-order.jsp?policyOrder=' + orderRuleElement() ;
        jQuery('#searchTable > tbody:last').append('<tr><td><input type="hidden" name="policyOrder" id="policyOrder" value="' + orderRuleElement() +'"/></td></tr>') ;
        document.searchForm.action="update-policy-order.jsp";
        document.searchForm.submit();

    }

    function getSelectedPolicyType() {
        var comboBox = document.getElementById("policyTypeFilter");
        var policyTypeFilter = comboBox[comboBox.selectedIndex].value;
        location.href = 'index.jsp?policyTypeFilter=' + policyTypeFilter ;
    }

    function orderRuleElement(){
        var ruleElementOrder = new Array();
        var tmp = jQuery("#dataTable tbody tr input.chkBox");
        for (var i = 0 ; i < tmp.length; i++){
            ruleElementOrder.push(tmp[i].value);
        }
        return ruleElementOrder;
    }

    jQuery(document).ready(
        function(){
            jQuery('#orderPolicyBtn').hide();
        }
    );

    function updownthis(thislink,updown,pageNumber,numberOfPages){
        if(!jQuery('#orderPolicyBtn').is(":visible")){
            jQuery('#orderPolicyBtn').show();
        }
//        var needPagination = false;   TODO : sort policies with pagination
//        var itemsPerPage = 15;
//        var itemNumber = clickedRow.rowIndex;
//        if(numberOfPages > 1){
//            if(pageNumber > 0){
//                if(itemNumber == 1){ //go up
//                    jQuery.ajax({
//                      url: "sort.jsp",
//                      data:"pageNumber="+pageNumber+"&direction=up",
//                      success: function(){
//                        //redrect to the right paginated page
//                      }
//                    });
//                }
//            }
//            if(itemNumber == itemsPerPage && (pageNumber+1) < numberOfPages){ //go down
//                jQuery.ajax({
//                      url: "sort.jsp",
//                      data:"pageNumber="+pageNumber+"&direction=down",
//                      success: function(){
//                        //redrect to the right paginated page
//                      }
//                    });
//            }
//            return;
//        }
        var sampleTable = document.getElementById('dataTable');
        var clickedRow = thislink.parentNode.parentNode;
        var addition = -1;
        if(updown == "down"){
            addition = 1;
        }
        var otherRow = sampleTable.rows[clickedRow.rowIndex + addition];
        var numrows = jQuery("#dataTable tbody tr").length;
        if(numrows <= 1){
            return;
        }
        if(clickedRow.rowIndex == 1 && updown == "up"){
            return;
        } else if(clickedRow.rowIndex == numrows && updown == "down"){
            return;
        }
        var rowdata_clicked = new Array();
        for(var i=0;i<clickedRow.cells.length;i++){
            rowdata_clicked.push(clickedRow.cells[i].innerHTML);
            clickedRow.cells[i].innerHTML = otherRow.cells[i].innerHTML;
        }
        for(i=0;i<otherRow.cells.length;i++){
            otherRow.cells[i].innerHTML =rowdata_clicked[i];
        }
    }    
</script>

<div id="middle">
    <h2><fmt:message key='policy.administration'/></h2>
    <div id="workArea">

    <table style="border:none; margin-bottom:10px">
        <tr>
            <td>
                <div style="height:30px;">
                    <a href="javascript:document.location.href='basic-policy-editor.jsp'" class="icon-link"
                       style="background-image:url(../admin/images/add.gif);"><fmt:message key='add.new.ent.policy'/></a>
                </div>
            </td>
            <td>
                <div style="height:30px;">
                    <a href="javascript:document.location.href='create-policy-set.jsp'" class="icon-link"
                       style="background-image:url(../admin/images/add.gif);"><fmt:message key='add.new.policy.set'/></a>
                </div>
            </td>            
            <td>
                <div style="height:30px;">
                    <a href="javascript:document.location.href='import-policy.jsp'" class="icon-link"
                       style="background-image:url(images/import.gif);"><fmt:message key='import.new.ent.policy'/></a>
                </div>
            </td>
        </tr>
    </table>

    <table class="styledLeft" style="margin-top:10px;margin-bottom:10px;">
        <tr>
            <td>
            <table  style="border:0; !important" >
                <tr>
                <td style="border:0; !important"><fmt:message key="policy.combining.algorithm"/></td>
                <td style="border:0; !important">
                  <select id="globalAlgorithmName" name="globalAlgorithmName" class="text-box-big">
                <%
                  if (policyCombiningAlgorithms != null && policyCombiningAlgorithms.length > 0) {
                      for (String algorithmName : policyCombiningAlgorithms) {
                          if(algorithmName.equals(globalPolicyCombiningAlgorithm)){
                %>
                      <option value="<%=algorithmName%>" selected="selected"><%=globalPolicyCombiningAlgorithm%></option>
                <%
                            } else {
                %>
                      <option value="<%=algorithmName%>"><%=algorithmName%></option>
                <%
                            }
                        }
                    }
                %>
                  </select>
                </td>
                <td style="border:0; !important">
                    <input type="button" class="button"  tabindex="4" value="Update"
                           onclick="setPolicyCombineAlgorithm();"/>
                </td>
                </tr>
            </table>
            </td>
        </tr>
    </table>

    <form action="index.jsp" name="searchForm" method="post">
        <table id="searchTable" name="searchTable" class="styledLeft" style="border:0;
                                                !important margin-top:10px;margin-bottom:10px;">
            <tr>
            <td>
                <table style="border:0; !important">
                    <tbody>
                    <tr style="border:0; !important">
                        <td style="border:0; !important">
                            <nobr>
                                <fmt:message key="policy.type"/>
                                <select name="policyTypeFilter" id="policyTypeFilter"  onchange="getSelectedPolicyType();">
                                    <%
                                        if (policyTypeFilter.equals("ALL")) {
                                    %>
                                    <option value="ALL" selected="selected"><fmt:message key="all"/></option>
                                    <%
                                    } else {
                                    %>
                                    <option value="ALL"><fmt:message key="all"/></option>
                                    <%
                                        }
                                        for (String policyType : policyTypes) {
                                            if (policyTypeFilter.equals(policyType)) {
                                    %>
                                    <option value="<%= policyType%>" selected="selected"><%= policyType%>
                                    </option>
                                    <%
                                    } else {
                                    %>
                                    <option value="<%= policyType%>"><%= policyType%>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                                &nbsp;&nbsp;&nbsp;
                                <fmt:message key="search.policy"/>
                                <input type="text" name="policySearchString"
                                       value="<%= policySearchString != null? policySearchString :""%>"/>&nbsp;
                            </nobr>
                        </td>
                        <td style="border:0; !important">
                             <a class="icon-link" href="#" style="background-image: url(images/search.gif);"
                                   onclick="searchServices(); return false;"
                                   alt="<fmt:message key="search"/>"></a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
            </tr>
        </table>
    </form>

    <table style="margin-top:10px;margin-bottom:10px">
        <tbody>
        <tr>
            <td>
                <a style="cursor: pointer;" onclick="selectAllInThisPage(true);return false;" href="#"><fmt:message key="selectAllInPage"/></a>
               &nbsp; | &nbsp;</td><td><a style="cursor: pointer;" onclick="selectAllInThisPage(false);return false;" href="#"><fmt:message key="selectNone"/></a>
            </td>
            <td width="20%">&nbsp;</td>
            <td>
                <a onclick="deleteServices();return false;"  href="#"  class="icon-link"
                   style="background-image: url(images/delete.gif);" ><fmt:message key="delete"/></a>
            </td>
            <td>
                <a onclick="publishPolicies();return false;"  href="#" class="icon-link"
                   style="background-image: url(images/publish.gif);" ><fmt:message key="publish.selected"/></a>
            </td>
            <td>
                <a onclick="publishAllPolicies();return false;"  class="icon-link" href="#"
                   style="background-image: url(images/publish-all.gif);" ><fmt:message key="publish.all.policies"/></a>
            </td>
            <td width="20%">&nbsp;</td>            
            <td style="border:0; !important">
                <input type="button" class="button"  tabindex="4" value="ReOrder Policies"
                       onclick="orderPolicies();" id="orderPolicyBtn"/>
            </td>
        </tr>
        </tbody>
    </table>

    <form action="" name="policyForm" method="post">
        <table style="width: 100%" id="dataTable" class="styledLeft">
            <thead>
            <tr>
                <th colspan="5"><fmt:message key='available.ent.policies'/></th>
            </tr>
            </thead>
            <tbody>
            <%
            if (policies != null) {
                for (int i = 0; i < policies.length; i++) {
                    if(policies[i] != null){
                    boolean edit = policies[i].getPolicyEditable();
                    boolean delete = policies[i].getPolicyCanDelete();
            %>
            <tr>
                <td style="width:100px;">
                    <a class="icon-link" onclick="updownthis(this,'up',<%=request.getParameter("pageNumber")%>,<%=numberOfPages%>)" style="background-image:url(../admin/images/up-arrow.gif)"></a>
                    <a class="icon-link" onclick="updownthis(this,'down',<%=request.getParameter("pageNumber")%>,<%=numberOfPages%>)" style="background-image:url(../admin/images/down-arrow.gif)"></a>
                    <input type="hidden" value="<%=CharacterEncoder.getSafeText(policies[i].getPolicyId())%>"/>                    
                </td>

                <td width="10px" style="text-align:center; !important">
                    <input type="checkbox" name="policies"
                           value="<%=policies[i].getPolicyId()%>"
                           onclick="resetVars()" class="chkBox" <% if(!delete){%>disabled="disabled"<% } %>/>
                </td>

                <td>
                    <a <% if(edit) { %>href="policy-view.jsp?policyid=<%=policies[i].getPolicyId()%>" <% } %>><%=policies[i].getPolicyId()%></a>
                </td>

                <td width="20px" style="text-align:left;">
                    <%
                        if(policies[i].getPolicyType() == null || "".equals(policies[i].getPolicyType())){
                            policies[i].setPolicyType("Policy");
                        }
                    %>
                    <nobr>
                    <img src="images/<%= policies[i].getPolicyType()%>-type.gif"
                         title="<%= policies[i].getPolicyType()%>"
                         alt="<%= policies[i].getPolicyType()%>"/>
                        <%= policies[i].getPolicyType()%>
                    </nobr>
                </td>
                
                <td width="40%">
                    <a title="<fmt:message key='edit.policy'/>"
                    <% if(edit){%> onclick="edit('<%=policies[i].getPolicyId()%>');return false;" <%}%>
                    href="#" style="background-image: url(images/edit.gif);" class="icon-link">
                    <fmt:message key='edit'/></a>
                    <% if (Boolean.toString(policies[i].getActive()).equals("true")) { %>
                    <a title="<fmt:message key='disable.policy'/>"
                    <% if(edit){%> onclick="disable('<%=policies[i].getPolicyId()%>');return false;" <%}%>
                    href="#" style="background-image: url(images/disable.gif);" class="icon-link">
                    <fmt:message key='disable.policy'/></a>
                    <% }else { %>
                    <a title="<fmt:message key='enable.policy'/>"
                    <% if(edit){%> onclick="enable('<%=policies[i].getPolicyId()%>');return false;"  <%}%>
                    href="#" style="background-image: url(images/enable.gif);" class="icon-link">
                    <fmt:message key='enable.policy'/></a>
                    <%} %>
                    <% if (policies[i].getPolicyLifeCycle() == 1) { %>
                    <a title="<fmt:message key='not.promote.policy'/>"
                    <% if(edit){%> onclick="dePromote('<%=policies[i].getPolicyId()%>');return false;" <%}%>
                    href="#" style="background-image: url(images/delete.gif);" class="icon-link">
                    <fmt:message key='not.promote.policy'/></a>
                    <% } else if(policies[i].getPolicyLifeCycle() == 2) { %>
                    <a title="<fmt:message key='sync.policy'/>"
                    <% if(edit){%> onclick="promote('<%=policies[i].getPolicyId()%>');return false;"  <%}%>
                    href="#" style="background-image: url(images/sync.png);" class="icon-link">
                    <fmt:message key='sync.policy'/></a>
                    <%} else { %>
                    <a title="<fmt:message key='promote.policy'/>"
                    <% if(edit){%> onclick="promote('<%=policies[i].getPolicyId()%>');return false;"  <%}%>
                    href="#" style="background-image: url(images/publish-pdp.gif);" class="icon-link">
                    <fmt:message key='promote.policy'/></a>
                    <%} %>
                </td>
            </tr>
            <%} }
            } else { %>
            <tr>
                <td colspan="2"><fmt:message key='no.policies.defined'/></td>
            </tr>
            <%}%>
            </tbody>
        </table>
    </form>
    <carbon:paginator pageNumber="<%=pageNumberInt%>" numberOfPages="<%=numberOfPages%>"
                      page="index.jsp" pageNumberParameterName="pageNumber" parameters="<%=paginationValue%>"
                      resourceBundle="org.wso2.carbon.identity.entitlement.ui.i18n.Resources"
                      prevKey="prev" nextKey="next"/>
        </div>
    </div>
</fmt:bundle>
