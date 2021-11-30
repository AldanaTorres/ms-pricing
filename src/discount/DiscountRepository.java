package discount;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import price.Price;
import spark.utils.StringUtils;
import utils.db.MongoStore;
import utils.errors.ValidationError;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiscountRepository {
    private static DiscountRepository instance;

    private DiscountRepository() {
    }

    public static DiscountRepository getInstance() {
        if (instance == null) {
            instance = new DiscountRepository();
        }
        return instance;
    }

    public Discount save(Discount discount) {
        MongoStore.getDataStore().save(discount);
        return discount;
    }

    public Discount get(String id) throws ValidationError {
        if (StringUtils.isBlank(id)) {
            throw new ValidationError().addPath("id", "Not found");
        }

        Discount result = MongoStore.getDataStore().get(Discount.class, new ObjectId(id));
        if (result == null) {
            throw new ValidationError().addPath("id", "Not found");
        }

        return result;
    }

    public Discount findByCriteria(Date expiration, Date start, String criteria, String code) {
        ArrayList<Discount> result = new ArrayList<>();
        Query<Discount> q = MongoStore.getDataStore().createQuery(Discount.class);
        q.and(q.criteria("expirationDate").greaterThan(expiration),
                q.and(q.criteria("startDate").lessThan(start),
                       q.and(q.criteria("codeDiscount").equal(code),
                q.and(q.criteria("articleId").equal(criteria)))));

        Iterable<Discount> resultList = q.fetch();
        for (Discount d : resultList) {
            result.add(d);
        }
        if(result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    public List<Discount> getDiscountsValid(Date date, String params) {
        ArrayList<Discount> result = new ArrayList<>();
        Query<Discount> q = MongoStore.getDataStore().createQuery(Discount.class);
        q.and(q.criteria("expirationDate").greaterThan(date),
                q.and(q.criteria("startDate").lessThan(date),
                                q.and(q.criteria("articleId").equal(params))));

        Iterable<Discount> resultList = q.fetch();
        for (Discount d : resultList) {
            result.add(d);
        }
        return result;
    }
}