package price;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import price.vo.NewData;
import price.vo.PriceData;
import utils.errors.ValidationError;
import utils.validator.Validator;

import java.util.Date;

@Entity("prices")
public class Price implements Comparable<Price> {
    @Id
    private ObjectId id;

    private double price;

    private String priceState;

    private Date createdAt = new Date();

    private String articleId;

    private Price() {

    }

    /**
     * Crea un nuevo precio
     */
        public static Price newPrice(NewData data) throws ValidationError {
        Validator.validate(data);

        Price price = new Price();

        price.price = data.price;
        price.priceState = "Habilitado";
        price.articleId = data.articleId;
        price.createdAt = data.createdAt;
        return price;
    }

    /**
     * Obtiene una representación interna de los valores.
     * Preserva la inmutabilidad de la entidad.
     */
    public PriceData value() {
        PriceData data = new PriceData();
        data.id = this.id.toHexString();

        data.price = this.price;
        data.priceState = this.priceState;
        data.articleId = this.articleId;
        data.createdAt = this.createdAt;
        return data;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPriceState() {
        return priceState;
    }

    public void setPriceState(String priceState) {
        this.priceState = priceState;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void deshabilitar() {
        this.priceState = "Deshabilitado";
    }

    @Override
    public int compareTo(Price p){
        return p.getCreatedAt().compareTo(getCreatedAt());
    }
}
