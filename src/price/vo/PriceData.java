package price.vo;

import com.google.gson.annotations.SerializedName;
import utils.gson.Builder;
import utils.gson.JsonSerializable;
import utils.validator.MaxLen;
import utils.validator.MinLen;
import utils.validator.MinValue;
import utils.validator.Required;

import java.util.Date;

/**
 * Value object para price
 */
public class PriceData implements JsonSerializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("price")
    @Required()
    @MinValue(0)
    public double price;

    @SerializedName("priceState")
    @Required()
    @MinLen(1)
    @MaxLen(15)
    public String priceState;

    @SerializedName("articleId")
    @MinLen(10)
    @MaxLen(40)
    public String articleId;

    @SerializedName("createdAt")
    public Date createdAt = new Date();

    @Override
    public String toJson() {
        return Builder.gson().toJson(this);
    }
}