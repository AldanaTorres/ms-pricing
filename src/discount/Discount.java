package discount;

import discount.vo.NewDataDiscount;
import discount.vo.DiscountData;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import utils.errors.ValidationError;
import utils.validator.Validator;

import java.util.Date;

@Entity("discounts")
public class Discount {
    @Id
    private ObjectId id;

    private String codeDiscount;

    private String discountDescription;

    private double discount;

    private Date startDate;

    private Date expirationDate;

    private String articleId;

    private Discount() {

    }

    /**
     * Crea un nuevo descuento
     */
        public static Discount newDiscount(NewDataDiscount data) throws ValidationError {
        Validator.validate(data);

        Discount discount = new Discount();

        discount.codeDiscount = data.codeDiscount;
        discount.discountDescription = data.discountDescription;
        discount.discount = data.discount;
        discount.startDate = data.startDate;
        discount.expirationDate = data.expirationDate;
        discount.articleId = data.articleId;

        return discount;
    }

    /**
     * Obtiene una representación interna de los valores.
     * Preserva la inmutabilidad de la entidad.
     */
    public DiscountData value() {
        DiscountData data = new DiscountData();
        data.id = this.id.toHexString();

        data.discount = this.discount;
        data.discountDescription = this.discountDescription;
        data.codeDiscount = this.codeDiscount;
        data.articleId = this.articleId;
        data.expirationDate = this.expirationDate;
        data.startDate = this.startDate;
        return data;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCodeDiscount() {
        return codeDiscount;
    }

    public void setCodeDiscount(String codeDiscount) {
        this.codeDiscount = codeDiscount;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }

    public void setDiscountDescription(String discountDescription) {
        this.discountDescription = discountDescription;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
