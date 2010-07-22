/*******************************************************************************
 * Copyright 2009, 2010 Lars Grammel 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.  
 *******************************************************************************/
package org.thechiselgroup.choosel.server.authentication;

import org.thechiselgroup.choosel.client.authentication.Authentication;
import org.thechiselgroup.choosel.client.authentication.AuthenticationService;
import org.thechiselgroup.choosel.client.services.ServiceException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AuthenticationServiceServlet extends RemoteServiceServlet
        implements AuthenticationService {

    private AuthenticationService service = null;

    @Override
    public Authentication getAuthentication(String moduleBaseURL)
            throws ServiceException {

        try {
            return getServiceDelegate().getAuthentication(moduleBaseURL);
        } catch (RuntimeException e) {
            Log.error("getAuthentication failed", e);
            throw new ServiceException(e);
        }
    }

    private AuthenticationService getServiceDelegate() {
        if (service == null) {
            service = new DefaultAuthenticationService(
                    UserServiceFactory.getUserService());
        }

        assert service != null;

        return service;
    }
}