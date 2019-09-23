import java.io.*;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceClient implements JavaDelegate {

    private static Logger LOG = LoggerFactory.getLogger("ServiceClient");
    public void execute(DelegateExecution execution) throws Exception {

        LOG.info("Send request to Data collector.");

        Set<String> variableNames = execution.getVariableNames();
        LOG.info("Variables: {}", variableNames);
        String salesmanName = (String) execution.getVariable("NameField");
        LOG.info("Salesman chosen: {}", salesmanName);
        String response = callEndpoint(salesmanName);

        ClientInfoDTO clientInfo = mapClientInfoDTOResponse(response);
        ObjectValue typedSalesInfoListObject =
            Variables.objectValue(clientInfo.getSalesInfo()).serializationDataFormat("application/json").create();

        execution.setVariable("clientCollection", typedSalesInfoListObject);
        System.out.println(clientInfo);
    }

    /**
     * Creates an Salesman object from an JSON String
     * @param response JSON body from HTTP response
     * @return Salesman Object
     * @throws IOException
     */
    static Salesman mapSalesmanResponse(String response) throws IOException {
        ObjectMapper oMapper = new ObjectMapper();
        return oMapper.readValue(response, Salesman.class);
    }

    /**
     * Creates an ClientInfoDTO object from an JSON String
     * @param response JSON body from HTTP response
     * @return ClientInfoDTO Object
     * @throws IOException
     */
    static ClientInfoDTO mapClientInfoDTOResponse(String response) throws IOException {
        ObjectMapper oMapper = new ObjectMapper();
        return oMapper.readValue(response, ClientInfoDTO.class);
    }

    /**
     * Calls data-collector API endpoint to retrieve information for a specific Salesman
     * @param name Specific salesman name
     * @return JSON String
     * @throws IOException
     */
    public static String callEndpoint(String name) throws IOException {
        HttpRequestFactory requestFactory
                = new NetHttpTransport().createRequestFactory();
        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl("http://data-collector:9050/salesman/"+name.trim().replace(" ", "%20")+"/bonusInfo"));
        String rawResponse = request.execute().parseAsString();
        return rawResponse;
    }
}

