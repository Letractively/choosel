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
package org.thechiselgroup.choosel.workbench.client.importer;

import java.io.Serializable;
import java.util.Date;

import org.thechiselgroup.choosel.core.client.label.IncrementingSuffixLabelFactory;
import org.thechiselgroup.choosel.core.client.label.LabelProvider;
import org.thechiselgroup.choosel.core.client.resources.DataType;
import org.thechiselgroup.choosel.core.client.resources.DefaultResourceSet;
import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetUtils;

import com.google.gwt.i18n.client.DateTimeFormat;

/*
 * TODO revisit error handling 
 * is this really what the user wants? --> probably not, because then
 * numbers cannot be used like numbers columns (warning message would be
 * better, and the ability to change this after the import would be the
 * best choice) --> for now, we could display error messages (e.g.
 * column x could not be parsed as a number, line y, data value)
 */
public class Importer {

    private static final String LOCATION_DETECTION_REGEX = "^[-+]?[0-9]*\\.?[0-9]+\\/[-+]?[0-9]*\\.?[0-9]+";

    private static final String DATE_DETECTION_REGEX = "^(0[1-9]|[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012]|[1-9])/\\d{4}";

    public static final String NUMBER_DETECTION_REGEX = "^[-+]?[0-9]*\\.?[0-9]+";

    private LabelProvider uriHeaderProvider;

    public Importer() {
        uriHeaderProvider = new IncrementingSuffixLabelFactory("import");
    }

    // TODO test
    // TODO pass set of parsers... --> separate step in which parsers are
    // determined
    public ResourceSet createResources(StringTable table) throws ParseException {
        assert table != null;

        String uriType = uriHeaderProvider.nextLabel();

        ResourceSet resources = new DefaultResourceSet();
        resources.setLabel("import"); // TODO changeable, inc number

        if (table.getRowCount() == 0) {
            return resources;
        }

        // use first row to determine types
        DataType[] columnTypes = new DataType[table.getColumnCount()];
        for (int column = 0; column < table.getColumnCount(); column++) {
            String stringValue = table.getValue(0, column);

            if (stringValue.matches(NUMBER_DETECTION_REGEX)) {
                columnTypes[column] = DataType.NUMBER;
            } else if (stringValue.matches(DATE_DETECTION_REGEX)) {
                columnTypes[column] = DataType.DATE;
            } else if (stringValue.matches(LOCATION_DETECTION_REGEX)) {
                columnTypes[column] = DataType.LOCATION;
            } else {
                columnTypes[column] = DataType.TEXT;
            }
        }

        for (int row = 0; row < table.getRowCount(); row++) {
            String uri = uriType + ":" + row;
            Resource resource = new Resource(uri);

            for (int column = 0; column < table.getColumnCount(); column++) {
                String stringValue = table.getValue(row, column);

                /*
                 * TODO should not be parsed at this point - change once setting
                 * property types possible
                 */
                Serializable value;

                switch (columnTypes[column]) {
                case NUMBER:
                    if (!stringValue.matches(NUMBER_DETECTION_REGEX)) {
                        throw new ParseException("Invalid number", stringValue,
                                row + 1);
                    }
                    value = new Double(stringValue);
                    break;
                case DATE:
                    if (!stringValue.matches(DATE_DETECTION_REGEX)) {
                        throw new ParseException("Invalid date", stringValue,
                                row + 1);
                    }
                    value = parseDate(stringValue);
                    break;
                case LOCATION:
                    if (!stringValue.matches(LOCATION_DETECTION_REGEX)) {
                        throw new ParseException("Invalid location",
                                stringValue, row + 1);
                    }

                    Resource locationResource = new Resource();
                    String[] split = stringValue.split("\\/");
                    locationResource.putValue(ResourceSetUtils.LATITUDE,
                            Double.parseDouble(split[0]));
                    locationResource.putValue(ResourceSetUtils.LONGITUDE,
                            Double.parseDouble(split[1]));
                    value = locationResource;
                    break;
                default:
                    value = stringValue;
                    break;
                }

                resource.putValue(table.getColumnName(column), value);
            }

            resources.add(resource);
        }

        return resources;
    }

    protected Date parseDate(String stringValue) {
        return DateTimeFormat.getFormat("dd/MM/yyyy").parse(stringValue);
    }

}
