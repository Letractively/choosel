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
package org.thechiselgroup.choosel.workbench.client.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.thechiselgroup.choosel.core.client.util.URLFetchService;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;

import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Retrieves the content of URLs using a invisible Flash widget.
 */
public class FlashURLFetchService extends SWFWidget implements URLFetchService {

    private static enum Status {
        INITIALIZING, NOT_INITIALIZED, READY
    }

    public static final String SWF_FILE = GWT.getModuleBaseURL()
            + "swf/FlexProxy.swf";

    private static Map<String, FlashURLFetchService> widgets = CollectionFactory
            .createStringMap();

    static {
        try {
            exportStaticMethods();
        } catch (Exception ex) {
            Log.error(ex.getMessage(), ex);
        }
    }

    public static void _callback(String swfID, String content, String url,
            String error) {
        widgets.get(swfID).callback(content, url, error);
    }

    public static void _onLoad(String swfID) {
        try {
            widgets.get(swfID).onWidgetReady();
        } catch (Exception ex) {
            Log.error(ex.getMessage(), ex);
        }
    }

    private static native void _requestURL(String swfID, String url) /*-{
        $doc.getElementById(swfID).call(url, "_flexproxy_callback");
    }-*/;

    private static native void exportStaticMethods() /*-{
        $wnd._flexproxy_loaded=$entry(
        @org.thechiselgroup.choosel.workbench.client.util.FlashURLFetchService::_onLoad(Ljava/lang/String;));
        $wnd._flexproxy_callback=$entry(
        @org.thechiselgroup.choosel.workbench.client.util.FlashURLFetchService::_callback(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
    }-*/;

    private Map<String, List<AsyncCallback<String>>> requests = CollectionFactory
            .createStringMap();

    private Status status = Status.NOT_INITIALIZED;

    public static final String FLASH_VAR_SWFID = "swfid";

    public FlashURLFetchService() {
        super(SWF_FILE, 1, 1);

        addAttribute("wmode", "transparent");

        // TODO extract superclass
        // hack around IE / FF differences with Flash embedding
        addFlashVar(FLASH_VAR_SWFID, getSwfId());
    }

    private void addUrlCallback(String url, AsyncCallback<String> callback) {
        if (!requests.containsKey(url)) {
            requests.put(url, new ArrayList<AsyncCallback<String>>());
        }

        requests.get(url).add(callback);
    }

    private void callback(String content, String url, String error) {
        assert requests.containsKey(url) : "no callback for URL " + url
                + " registered";
        List<AsyncCallback<String>> callbacks = requests.remove(url);

        if (error != null) {
            for (AsyncCallback<String> callback : callbacks) {
                callback.onFailure(new IOException("Could not retrieve URL "
                        + url + " (" + error + ")"));
            }
        } else {
            for (AsyncCallback<String> callback : callbacks) {
                callback.onSuccess(content);
            }
        }
    }

    @Override
    public void fetchURL(String url, AsyncCallback<String> callback) {
        addUrlCallback(url, callback);

        switch (status) {
        case NOT_INITIALIZED: {
            init();
        }
            break;
        case INITIALIZING: {
        }
            break;
        case READY: {
            if (requests.get(url).size() == 1) {
                requestURL(url);
            }
        }
            break;
        }
    }

    private void init() {
        RootPanel.get().add(this);
        status = Status.INITIALIZING;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        FlashURLFetchService.widgets.put(getSwfId(), this);
    }

    @Override
    protected void onUnload() {
        FlashURLFetchService.widgets.remove(getSwfId());
        super.onUnload();
    }

    public void onWidgetReady() {
        status = Status.READY;

        for (String url : requests.keySet()) {
            requestURL(url);
        }
    }

    private void requestURL(String url) {
        _requestURL(getSwfId(), url);
    }
}