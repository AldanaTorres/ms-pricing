package discount.vo;

import com.google.gson.annotations.SerializedName;
import discount.Discount;
import price.Price;
import utils.gson.Builder;
import utils.gson.JsonSerializable;
import utils.validator.Required;

import java.util.List;

public class DiscountsValidsData implements JsonSerializable {
    @SerializedName("articleId")
    public String articleId;

    @SerializedName("discounts")
    @Required()
    public List<Discount> discounts;

    @Override
    public String toJson() {
        return Builder.gson().toJson(this);
    }
}
