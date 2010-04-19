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
package org.thechiselgroup.choosel.client.ui.widget.timeline;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class TimeLineWidget extends Widget {

    private TimeLine timeLine;

    private TimeLineEventSource eventSource;

    private DateTimeFormat inputFormat = DateTimeFormat
	    .getFormat("MMM d yyyy HH:mm:ss z");

    // TODO http://code.google.com/p/google-web-toolkit/issues/detail?id=3415
    // wait for fix to switch to "EEE, dd MMM yyyy HH:mm:ss z"
    private DateTimeFormat outputFormat = DateTimeFormat
	    .getFormat("dd MMM yyyy HH:mm:ss z");

    public TimeLineWidget() {
	setElement(DOM.createDiv());
    }

    public void layout() {
	if (timeLine != null) {
	    timeLine.layout();
	}
    }

    @Override
    protected void onAttach() {
	super.onAttach();

	if (timeLine == null) {
	    eventSource = TimeLineEventSource.create();

	    timeLine = TimeLine.create(getElement(), eventSource, inputFormat
		    .format(new Date()));

	    timeLine.disableBubbles();
	    timeLine.registerPaintListener();
	}
    }

    public TimeLine getTimeLine() {
	return timeLine;
    }

    public final int getZoomIndex(int bandNumber) {
	return timeLine.getZoomIndex(bandNumber);
    }

    public final void setZoomIndex(int bandNumber, int zoomIndex) {
	timeLine.setZoomIndex(bandNumber, zoomIndex);
    }

    public void setCenterVisibleDate(Date date) {
	assert date != null;
	// TODO use output format once
	// http://code.google.com/p/google-web-toolkit/issues/detail?id=3415
	// is fixed.
	timeLine.setCenterVisibleDate(DateTimeFormat.getFormat(
		"EEE, dd MMM yyyy HH:mm:ss z").format(date));
    }

    public Date getCenterVisibleDate() {
	// TODO
	// http://code.google.com/p/google-web-toolkit/issues/detail?id=3415
	// wait for fix to switch to "EEE, dd MMM yyyy HH:mm:ss z"
	return outputFormat.parse(timeLine.getCenterVisibleDateAsGMTString()
		.substring(5));
    }

    public void addEvent(TimeLineEvent event) {
	eventSource.addEvent(event);
	timeLine.paint();
    }

    public void removeEvent(TimeLineEvent event) {
	eventSource.removeEvent(event);
	timeLine.paint();
    }
}
