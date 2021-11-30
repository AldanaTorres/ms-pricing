package price;

import discount.Discount;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import price.vo.PriceDiscountData;

import java.util.Date;

public class PriceDiscount {

    @Id
    private ObjectId id;

    private double price;

    private String priceState;

    private Date createdAt;

    private String articleId;

   private double finalPrice;

    public PriceDiscount(Price p,Discount d) {
        this.id = p.getId();
        this.price = p.getPrice();
        this.priceState = p.getPriceState();
        this.createdAt = p.getCreatedAt();
        this.articleId = p.getArticleId();
        this.finalPrice = calculateFinalPrice(p, d);
    }

    private double calculateFinalPrice(Price p, Discount d) {
        if(d == null){
            return p.getPrice();
        }else {
            return p.getPrice() * ((100 - d.getDiscount())/100);
        }
    }

    /**
     * Obtiene una representación interna de los valores.
     * Preserva la inmutabilidad de la entidad.
     */
    public PriceDiscountData value() {
        PriceDiscountData data = new PriceDiscountData();
        data.id = this.id.toHexString();

        data.price = this.price;
        data.finalPrice = this.finalPrice;
        data.priceState = this.priceState;
        data.articleId = this.articleId;
        data.createdAt = this.createdAt;
        return data;
    }

}
