package price.vo;

import com.google.gson.annotations.SerializedName;
import price.Price;
import utils.gson.Builder;
import utils.gson.JsonSerializable;
import utils.validator.Required;

import java.util.List;

public class PricesHistoricData implements JsonSerializable {

    @SerializedName("articleId")
    public String articleId;

    @SerializedName("prices")
    @Required()
    public List<Price> prices;

    @Override
    public String toJson() {
        return Builder.gson().toJson(this);
    }
}
