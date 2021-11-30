package price.vo;

import com.google.gson.annotations.SerializedName;
import utils.gson.Builder;
import utils.validator.MaxLen;
import utils.validator.MinLen;
import utils.validator.MinValue;
import utils.validator.Required;
import utils.gson.JsonSerializable;

import java.util.Date;

public class NewData implements JsonSerializable {

    @SerializedName("_id")
    public String id;

    @MinValue(0)
    @Required()
    @SerializedName("price")
    public double price;

    @SerializedName("articleId")
    @MinLen(10)
    @MaxLen(40)
    public String articleId;

    @SerializedName("createdAt")
    public Date createdAt = new Date();

    public static NewData fromJson(String json) {
        return Builder.gson().fromJson(json, NewData.class);
    }

    @Override
    public String toJson() {
        return Builder.gson().toJson(this);
    }
}
