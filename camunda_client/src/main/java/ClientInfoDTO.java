import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.api.client.util.Key;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInfoDTO {
    @JsonProperty("salesmanName")
    @Key
    private String salesmanName;
    @JsonProperty("salesInfos")
    @Key
    private List<SalesInfoDTO> salesInfos;

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public void setSalesInfos(List<SalesInfoDTO> salesInfos) {
        this.salesInfos = salesInfos;
    }

    public String getClientName() {
        return salesmanName;
    }

    public List<SalesInfoDTO> getSalesInfo() {
        return salesInfos;
    }
}
