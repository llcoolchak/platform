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

package org.wso2.carbon.identity.oauth2.authz.handlers;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.authz.OAuthAuthzReqMessageContext;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AuthorizeReqDTO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AuthorizeRespDTO;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;

public class CodeResponseTypeHandler extends AbstractAuthorizationHandler {

    private static Log log = LogFactory.getLog(CodeResponseTypeHandler.class);

    public CodeResponseTypeHandler()
            throws IdentityOAuth2Exception {
        super();
    }

    @Override
    public OAuth2AuthorizeRespDTO issue(OAuthAuthzReqMessageContext oauthAuthzMsgCtx)
            throws IdentityOAuth2Exception {
        OAuth2AuthorizeRespDTO respDTO = new OAuth2AuthorizeRespDTO();
        String authorizationCode;

        OAuth2AuthorizeReqDTO authorizationReqDTO = oauthAuthzMsgCtx.getAuthorizationReqDTO();

        try {
            authorizationCode = oauthIssuerImpl.authorizationCode();
        } catch (OAuthSystemException e) {
            throw new IdentityOAuth2Exception(e.getMessage(), e);
        }

        tokenMgtDAO.storeAuthorizationCode(authorizationCode,
                authorizationReqDTO.getConsumerKey(),
                OAuth2Util.buildScopeString(oauthAuthzMsgCtx.getApprovedScope()),
                authorizationReqDTO.getUsername());

        if (log.isDebugEnabled()) {
            log.debug("Issued Authorization Code to user : " +
                    authorizationReqDTO.getUsername() + ". Using the redirect url : " +
                    authorizationReqDTO.getCallbackUrl());
        }
        respDTO.setCallbackURI(authorizationReqDTO.getCallbackUrl());
        respDTO.setAuthorized(true);
        respDTO.setAuthorizationCode(authorizationCode);
        return respDTO;
    }


}