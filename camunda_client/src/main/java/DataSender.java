import com.fasterxml.jackson.core.JsonFactory;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.TypedValue;
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
            System.out.println("CombinedResult: " + combinedResult);
            ClientInfoDTO payload = new ClientInfoDTO();
            payload.setSalesmanName(salesmanName);
            payload.setSalesInfos(combinedResult.getValues());
            System.out.println(payload);
            System.out.println("JSON STRING: " + om.writeValueAsString(payload));
            callEndpoint(salesmanName,payload);
        }

    private static String callEndpoint(String name, ClientInfoDTO payload) throws IOException {
        HttpRequestFactory requestFactory
            = new NetHttpTransport().createRequestFactory();
        JsonHttpContent jsonHttpContent = new JsonHttpContent(new JacksonFactory(), payload);
        HttpRequest request = requestFactory.buildPutRequest(
            new GenericUrl("http://192.168.0.2:9050/salesman/"+name.trim().replace(" ", "%20")+"/bonusInfo"), jsonHttpContent);

        HttpResponse respons = request.execute();
        if(respons.isSuccessStatusCode()){
            return respons.parseAsString();
        } else {
          throw new HttpResponseException(respons);
        }
    }
}
