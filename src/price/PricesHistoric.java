package price;

import org.mongodb.morphia.annotations.Id;
import price.vo.PricesHistoricData;

import java.util.List;

public class PricesHistoric {

    @Id
    private String articleId;

    private List<Price> prices;


    public PricesHistoric(String articleId, List<Price> prices) {
        this.articleId = articleId;
        this.prices = prices;
    }

    /**
     * Obtiene una representación interna de los valores.
     * Preserva la inmutabilidad de la entidad.
     */
    public PricesHistoricData value() {
        PricesHistoricData data = new PricesHistoricData();
        data.articleId = this.articleId;

        data.prices = this.prices;
        return data;
    }

}
