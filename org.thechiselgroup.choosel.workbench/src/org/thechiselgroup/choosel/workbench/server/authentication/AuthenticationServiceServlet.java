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
package org.thechiselgroup.choosel.workbench.server.authentication;

import org.thechiselgroup.choosel.core.client.util.ServiceException;
import org.thechiselgroup.choosel.core.client.util.task.Task;
import org.thechiselgroup.choosel.workbench.client.authentication.Authentication;
import org.thechiselgroup.choosel.workbench.client.authentication.AuthenticationService;
import org.thechiselgroup.choosel.workbench.server.ChooselServiceServlet;

import com.google.appengine.api.users.UserServiceFactory;

public class AuthenticationServiceServlet extends ChooselServiceServlet
        implements AuthenticationService {

    private AuthenticationService service = null;

    @Override
    public Authentication getAuthentication(final String moduleBaseURL)
            throws ServiceException {

        return execute(new Task<Authentication>() {
            @Override
            public Authentication execute() throws ServiceException {
                return getServiceDelegate().getAuthentication(moduleBaseURL);
            }
        });
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