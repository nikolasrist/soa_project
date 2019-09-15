import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesInfoDTO {
    private String productName;
    private String clientName;
    private String clientRanking;
    private String quantity;

    public String getProductName() {
        return productName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientRanking() {
        return clientRanking;
    }

    public String getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "SalesInfoDTO{" +
            "productName='" + productName + '\'' +
            ", clientName='" + clientName + '\'' +
            ", clientRanking='" + clientRanking + '\'' +
            ", quantity='" + quantity + '\'' +
            '}';
    }
}
