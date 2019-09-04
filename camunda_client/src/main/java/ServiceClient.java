import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceClient implements JavaDelegate {

    private static Logger LOG = LoggerFactory.getLogger("ServiceClient");
    public void execute(DelegateExecution execution) throws Exception {

        LOG.info("Send request to Data collector.");

//        HttpURLConnection connection = null;
//        String targetURL = ""; // TODO: 2019-09-02 add correct url
//        try {
//            //Create connection
//            URL url = new URL(targetURL);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type",
//                "application/x-www-form-urlencoded");
//            connection.setRequestProperty("Content-Language", "en-US");
//            connection.setUseCaches(false);
//            connection.setDoOutput(true);
//
//            //Send request
//            DataOutputStream wr = new DataOutputStream (
//                connection.getOutputStream());
//            wr.close();
//
//            //Get Response
//            InputStream is = connection.getInputStream();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
//            String line;
//            while ((line = rd.readLine()) != null) {
//                response.append(line);
//                response.append('\r');
//            }
//            rd.close();
//            System.out.println(response.toString());
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
    }
}
