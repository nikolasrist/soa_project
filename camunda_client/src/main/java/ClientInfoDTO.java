import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInfoDTO {
    private String salesmanName;
    private List<SalesInfoDTO> salesInfos;

    public String getClientName() {
        return salesmanName;
    }

    public List<SalesInfoDTO> getSalesInfo() {
        return salesInfos;
    }

    @Override
    public String toString() {
        return "ClientInfoDTO{" +
            "clientName='" + salesmanName + '\'' +
            ", salesInfo=" + salesInfos +
            '}';
    }
}
