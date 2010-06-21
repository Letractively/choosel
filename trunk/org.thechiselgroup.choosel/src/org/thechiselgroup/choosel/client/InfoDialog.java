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
package org.thechiselgroup.choosel.client;

import org.thechiselgroup.choosel.client.command.AsyncCommandExecutor;
import org.thechiselgroup.choosel.client.feedback.FeedbackDialog;
import org.thechiselgroup.choosel.client.feedback.FeedbackServiceAsync;

import com.google.inject.Inject;

public class InfoDialog extends FeedbackDialog {

    private static final String MESSAGE = "<h1>Bio-Mixer</h1>"
            + "<i>\"To understand is hard. Once one understands, "
            + "action is easy.\"</i>"
            + "<p>(C) Copyright 2009, 2010 "
            + "The CHISEL Group, University of Victoria<br/>"
            + "(<a href=\"http://www.thechiselgroup.org/\" "
            + "target=\"_blank\">www.thechiselgroup.org</a>)<p>"
            + "<p>Available at: <a href=\"http://bio-mixer.appspot.com\" "
            + "target=\"_blank\">bio-mixer.appspot.com</a></p>"
            + "<p>User group: <a href=\"http://groups.google.com/group/bio-mixer\" "
            + "target=\"_blank\">groups.google.com/group/bio-mixer</a></p>"
            + "For more information, visit <a href=\"http://code.google.com/p/bio-mixer/\" "
            + "target=\"_blank\">code.google.com/p/bio-mixer/</a></p>"
            + "<br/><b>We appreciate your ideas and comments:</b>";

    @Inject
    public InfoDialog(AsyncCommandExecutor executor,
            FeedbackServiceAsync feedbackService) {
        super("About Bio-Mixer", MESSAGE, null, executor, feedbackService);
    }

    @Override
    public String getOkayButtonLabel() {
        return "Send feedback";
    }

}