import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServiceClientTest {
    @org.junit.jupiter.api.Test
    void mapResponse() throws IOException {
        String response = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Smith\",\n" +
                "  \"employeeId\": \"7\",\n" +
                "  \"jobTitle\": \"Senior Salesman\"\n" +
                "}";
        Salesman salesman = ServiceClient.mapResponse(response);
        assertEquals("John", salesman.getFirstName());
        assertEquals("Smith", salesman.getLastName());
        assertEquals("Senior Salesman", salesman.getJobTitle());
    }

    @org.junit.jupiter.api.Test
    void callEndpoint() throws IOException {
        ServiceClient.callEndpoint("John Smith");
    }
}