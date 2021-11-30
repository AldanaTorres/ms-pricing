package price;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import spark.utils.StringUtils;
import utils.db.MongoStore;
import utils.errors.ValidationError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PriceRepository {
    private static PriceRepository instance;

    private PriceRepository() {
    }

    public static PriceRepository getInstance() {
        if (instance == null) {
            instance = new PriceRepository();
        }
        return instance;
    }

    public Price save(Price price) {
        MongoStore.getDataStore().save(price);
        return price;
    }

    public void remove(Price price) {
        MongoStore.getDataStore().delete(price);
    }

    public Price get(String id) throws ValidationError {
        if (StringUtils.isBlank(id)) {
            throw new ValidationError().addPath("id", "Not found");
        }

        Price result = MongoStore.getDataStore().get(Price.class, new ObjectId(id));
        if (result == null) {
            throw new ValidationError().addPath("id", "Not found");
        }

        return result;
    }

    public Price findByCriteria(String criteria) {
        ArrayList<Price> result = new ArrayList<>();
        Query<Price> q = MongoStore.getDataStore().createQuery(Price.class);
        q.and(q.criteria("priceState").equal("Habilitado"), //
                q.and(q.criteria("articleId").equal(criteria)));

        Iterable<Price> resultList = q.fetch();
        for (Price p : resultList) {
            result.add(p);
        }
        return result.get(0);
    }

    public List<Price> getAll(String params) {
        ArrayList<Price> result = new ArrayList<>();
        Query<Price> q = MongoStore.getDataStore().createQuery(Price.class);
        q.and(q.criteria("articleId").equal(params));
        Iterable<Price> resultList = q.fetch();
        for (Price p : resultList) {
            result.add(p);
        }
        Collections.sort(result);
        return result;
    }

    public List<Price> getAllByArticle(String articleId) {
        ArrayList<Price> result = new ArrayList<>();
        Query<Price> q = MongoStore.getDataStore().createQuery(Price.class);
        q.and(q.criteria("priceState").equal("Habilitado"), //
                q.and(q.criteria("articleId").equal(articleId)));

        Iterable<Price> resultList = q.fetch();
        for (Price p : resultList) {
            result.add(p);
        }

        return result;
    }

    public void findByArticle(String articleId, boolean valid) {
        Query<Price> q = MongoStore.getDataStore().createQuery(Price.class);
                q.and(q.criteria("articleId").equal(articleId));

        Iterable<Price> resultList = q.fetch();

        for (Price p : resultList) {
            System.out.println(valid);
            if (!valid) {
                System.out.println("entro");
                remove(p);
            }
        }
    }
}