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

    /**
     * Implements logic to send calculated boni to the data-collector
     * @param execution current camunda execution context
     * @throws Exception
     */
    public void execute(DelegateExecution execution) throws Exception {

        LOG.debug("Send request to Data collector.");
        Set<String> variableNames = execution.getVariableNames();
        LOG.debug("Variables: {}", variableNames);
        String salesmanName = (String) execution.getVariable("NameField");
        LOG.debug("Salesman chosen: {}", salesmanName);
        String combinedResultJson = execution.getVariableTyped("combinedResult").getValue().toString();

        ObjectMapper om = new ObjectMapper();
        CombinedResult combinedResult = om.readValue(combinedResultJson, CombinedResult.class);
        ClientInfoDTO payload = new ClientInfoDTO();
        payload.setSalesmanName(salesmanName);
        payload.setSalesInfos(combinedResult.getValues());
        callEndpoint(salesmanName, payload);
    }

    /**
     * Calls data-collector API endpoint to write calculated boni back to OrangeHRM
     * @param name specific Salesman name
     * @param payload Includes ClientInfoDTO and calculated boni
     * @return HTTP raw response as string
     * @throws IOException
     */
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
