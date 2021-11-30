package discount.vo;

import com.google.gson.annotations.SerializedName;
import utils.gson.Builder;
import utils.gson.JsonSerializable;
import utils.validator.*;

import java.util.Date;

public class NewDataDiscount implements JsonSerializable {

    @SerializedName("_id")
    public String id;

    @MinValue(1)
    @MaxValue(99)
    @Required()
    @SerializedName("discount")
    public double discount;

    @Required()
    @SerializedName("codeDiscount")
    public String codeDiscount;


    @Required()
    @SerializedName("discountDescription")
    public String discountDescription;

    @SerializedName("articleId")
    @MinLen(10)
    @MaxLen(40)
    @Required()
    public String articleId;

    @SerializedName("startDate")
    @Required()
    public Date startDate;

    @SerializedName("expirationDate")
    @Required()
    public Date expirationDate;

    public static NewDataDiscount fromJson(String json) {
        return Builder.gson().fromJson(json, NewDataDiscount.class);
    }

    @Override
    public String toJson() {
        return Builder.gson().toJson(this);
    }
}
