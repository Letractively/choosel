package org.thechiselgroup.chooselexample.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.chooselexample.client.services.CSVFileService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class CSVFileServiceImpl extends RemoteServiceServlet implements
        CSVFileService {

    @Override
    public Set<Resource> getCSVResources(String filePath, String fileName)
            throws Exception {

        BufferedReader in = new BufferedReader(new FileReader(filePath
                + fileName));
        int count = 1;

        try {
            String[] fieldNames = in.readLine().trim().split(",");
            Set<Resource> resources = new HashSet<Resource>();

            String line = in.readLine();
            while (line != null) {
                String[] fieldValues = line.trim().split(",");

                // TODO maybe should be if else
                assert (fieldNames.length == fieldValues.length);

                Resource resource = new Resource("csv:" + count++);
                for (int i = 0; i < fieldValues.length; i++) {
                    
                    resource.putValue(fieldNames[i].trim(), fieldValues[i].trim());
                }

                resources.add(resource);
                line = in.readLine();
            }

            return resources;
        } finally {
            in.close();
        }
    }

}
