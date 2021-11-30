package discount;

import discount.vo.DiscountsValidsData;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

public class DiscountsValids {

    @Id
    private String articleId;

    private List<Discount> discounts;


    public DiscountsValids(String articleId, List<Discount> discounts) {
        this.articleId = articleId;
        this.discounts = discounts;
    }

    /**
     * Obtiene una representación interna de los valores.
     * Preserva la inmutabilidad de la entidad.
     */
    public DiscountsValidsData value() {
        DiscountsValidsData data = new DiscountsValidsData();
        data.articleId = this.articleId;

        data.discounts = this.discounts;
        return data;
    }

}
