import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInfoDTO {
    private String clientName;
    private List<SalesInfoDTO> salesInfo;

    public String getClientName() {
        return clientName;
    }

    public List<SalesInfoDTO> getSalesInfo() {
        return salesInfo;
    }

    @Override
    public String toString() {
        return "ClientInfoDTO{" +
            "clientName='" + clientName + '\'' +
            ", salesInfo=" + salesInfo +
            '}';
    }
}
