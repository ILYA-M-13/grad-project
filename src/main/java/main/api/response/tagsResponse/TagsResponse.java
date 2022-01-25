package main.api.response.tagsResponse;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class TagsResponse {
    private String name;
    private double weight;

    public TagsResponse(String name, double weight) {
        this.name = name;
        this.weight = roundNumber(weight);
    }
    private double roundNumber(double number){
        BigDecimal bd = new BigDecimal(number);
        return bd.setScale(2, RoundingMode.DOWN).doubleValue();
    }
}

