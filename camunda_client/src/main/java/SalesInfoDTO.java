import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesInfoDTO {
    private String productName;
    private String clientName;
    private int clientRanking;
    private double quantity;

    public String getProductName() {
        return productName;
    }

    public String getClientName() {
        return clientName;
    }

    public int getClientRanking() {
        return clientRanking;
    }

    public double getQuantity() {
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
