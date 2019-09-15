import java.io.*;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
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
//        Salesman salesman = mapSalesmanResponse(response);
//        System.out.println(salesman);
        ClientInfoDTO clientInfo = mapClientInfoDTOResponse(response);
        System.out.println(clientInfo);
    }

    static Salesman mapSalesmanResponse(String response) throws IOException {
        ObjectMapper oMapper = new ObjectMapper();
        return oMapper.readValue(response, Salesman.class);
    }

    static ClientInfoDTO mapClientInfoDTOResponse(String response) throws IOException {
        ObjectMapper oMapper = new ObjectMapper();
        return oMapper.readValue(response, ClientInfoDTO.class);
    }

    public static String callEndpoint(String name) throws IOException {
        HttpRequestFactory requestFactory
                = new NetHttpTransport().createRequestFactory();
        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl("http://192.168.2.169:9050/salesman/"+name.trim().replace(" ", "%20")+"/bonusInfo"));
        String rawResponse = request.execute().parseAsString();
        return rawResponse;
    }
}

