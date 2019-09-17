import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.api.client.util.Key;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesInfoDTO {
    @Key
    private String productName;
    @Key
    private String clientName;
    @Key
    private int clientRanking;
    @Key
    private double quantity;
    @Key
    private int bonus;

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

    public int getBonus() {
        return bonus;
    }

    @Override
    public String toString() {
        return "SalesInfoDTO{" +
            "productName='" + productName + '\'' +
            ", clientName='" + clientName + '\'' +
            ", clientRanking=" + clientRanking +
            ", quantity=" + quantity +
            ", bonus=" + bonus +
            '}';
    }
}
