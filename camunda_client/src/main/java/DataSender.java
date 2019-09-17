import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.util.Set;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSender implements JavaDelegate {

    private static Logger LOG = LoggerFactory.getLogger("ServiceClient");

    public void execute(DelegateExecution execution) throws Exception {

        LOG.info("Send request to Data collector.");

        Set<String> variableNames = execution.getVariableNames();
        LOG.info("Variables: {}", variableNames);
        String salesmanName = (String) execution.getVariable("NameField");
        LOG.info("Salesman chosen: {}", salesmanName);
        String combinedResultJson = execution.getVariableTyped("combinedResult").getValue().toString();
        System.out.println("CombinedResultJson: " + combinedResultJson);
        ObjectMapper om = new ObjectMapper();
        CombinedResult combinedResult = om.readValue(combinedResultJson, CombinedResult.class);
        ClientInfoDTO payload = new ClientInfoDTO();
        payload.setSalesmanName(salesmanName);
        payload.setSalesInfos(combinedResult.getValues());
        callEndpoint(salesmanName, payload);
    }

    private static String callEndpoint(String name, ClientInfoDTO payload) throws IOException {
        HttpRequestFactory requestFactory
            = new NetHttpTransport().createRequestFactory();
        JsonHttpContent jsonHttpContent = new JsonHttpContent(new JacksonFactory(), payload);
        HttpRequest request = requestFactory.buildPutRequest(
            new GenericUrl("http://data-collector:9050/salesman/" + name.trim().replace(" ", "%20") + "/bonusInfo"),
            jsonHttpContent);

        HttpResponse response = request.execute();
        if (response.isSuccessStatusCode()) {
            return response.parseAsString();
        } else {
            throw new HttpResponseException(response);
        }
    }
}
