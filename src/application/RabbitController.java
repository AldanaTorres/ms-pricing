package application;

import com.google.gson.annotations.SerializedName;
import discount.Discount;
import org.bson.types.ObjectId;
import price.Price;
import price.PriceRepository;
import security.TokenService;
import utils.errors.ValidationError;
import utils.gson.Builder;
import utils.gson.JsonSerializable;
import utils.rabbit.*;
import utils.validator.Required;
import utils.validator.Validator;

import java.util.Date;

public class RabbitController {

    public static void init() {
        FanoutConsumer fanoutConsumer = new FanoutConsumer("auth");
        fanoutConsumer.addProcessor("logout", e -> processLogout(e));
        fanoutConsumer.start();

        DirectConsumer directConsumer = new DirectConsumer("pricing", "pricing");
        directConsumer.addProcessor("article-exist", e -> processArticleExist(e));
        directConsumer.start();

    }

    /**
     * @api {fanout} auth/logout Logout
     *
     * @apiGroup RabbitMQ GET
     *
     * @apiDescription Escucha de mensajes logout desde auth. Invalida sesiones en cache.
     *
     * @apiExample {json} Mensaje
     *   {
     *     "type": "logout",
     *     "message" : "tokenId"
     *   }
     */
    static void processLogout(RabbitEvent event) {
        TokenService.invalidate(event.message.toString());
    }


    /**
     *
     * @api {direct} pricing/new-price New price
     *
     * @apiGroup RabbitMQ POST
     *
     * @apiDescription Enviá mensajes new-price desde pricing.
     *
     * @apiSuccessExample {json} Mensaje
     *     {
     *     "type": "new-price",
     *     "message" : {
     *         "price": "{price}",
     *         "articleId": "{articleId}",
     *         "createdAt: "{createdAt}"
     *        }
     *     }
     *
     */
    public static void sendNewPrice(Price event) {
        RabbitEvent eventToSend = new RabbitEvent();
        eventToSend.type = "new-price";
        eventToSend.exchange = "price";
        eventToSend.queue = "price";

        eventToSend.message = new EventNewPrice(event.getPrice(),
                event.getArticleId());

        DirectPublisher.publish("price", "price", eventToSend);
    }


    /**
     *
     * @api {direct} discount/new-discount New Discount
     *
     * @apiGroup RabbitMQ POST
     *
     * @apiDescription Enviá mensajes new-discount desde pricing.
     *
     * @apiSuccessExample {json} Mensaje
     *     {
     *     "type": "new-discount",
     *     "message" : {
     *         "discount": "{discount}",
     *         "articleId": "{articleId}",
     *         "startDate: "{startDate}",
     *         "expirationDate": "{expirationDate}"
     *        }
     *     }
     *
     */
    public static void sendNewDiscount(Discount event) {
        RabbitEvent eventToSend = new RabbitEvent();
        eventToSend.type = "new-discount";
        eventToSend.exchange = "discount";
        eventToSend.queue = "discount";

        eventToSend.message = new EventNewDiscount(event.getCodeDiscount(), event.getDiscount(), event.getDiscountDescription(),
                event.getArticleId(), event.getExpirationDate(), event.getStartDate());

        DirectPublisher.publish("discount", "discount", eventToSend);
    }

    /**
     * @api {direct} pricing/article-exist Validación de Artículos
     * @apiGroup RabbitMQ GET
     *
     * @apiDescription Escucha de mensajes article-exist desde cart. Valida artículos
     *
     * @apiSuccessExample {json} Mensaje
     *     {
     *        "type": "article-exist",
     *        "message": {
     *             "referenceId": "{cartId}",
     *             "articleId": "{articleId}",
     *             "valid": true|false
     *        }
     *     }
     */
    public static void processArticleExist(RabbitEvent event){
        EventArticleExist exist = EventArticleExist.fromJson(event.message.toString());
        System.out.println("RabbitMQ Consume article-exist for article : " + exist.articleId);

        PriceRepository.getInstance().findByArticle(exist.articleId, exist.valid);
    }


    /**
     * @api {direct} catalog/article-exist Comprobar Articulo
     * @apiGroup RabbitMQ POST
     *
     * @apiDescription Pricing enviá un mensaje a Catalog para comprobar la validez de un articulo.
     *
     * @apiExample {json} Mensaje
     *     {
     *        "type": "article-exist",
     *        "queue": "pricing",
     *        "exchange": "pricing",
     *         "message": {
     *             "referenceId": "{priceId}",
     *             "articleId": "{articleId}"
     *        }
     *     }
     */
    /**
     * Enviá una petición a catalog para validar si un articulo puede incluirse en un precio.
     */
    public static void sendArticleValidation(ObjectId priceId, String articleId){
        RabbitEvent eventToSend = new RabbitEvent();
        eventToSend.type = "article-exist";
        eventToSend.exchange = "pricing";
        eventToSend.queue = "pricing";
        eventToSend.message = new EventValidation(priceId,articleId);

        DirectPublisher.publish("catalog", "catalog", eventToSend);


    }


    static class EventValidation implements JsonSerializable{
        @Required
        @SerializedName("priceId")
        public String priceId;
        @Required
        @SerializedName("articleId")
        public String articleId;

        public EventValidation(ObjectId priceId, String articleId) {
            this.priceId = priceId.toHexString();
            this.articleId = articleId;
        }

        @Override
        public String toJson() {
            return Builder.gson().toJson(this);
        }
    }

    static class EventNewPrice implements JsonSerializable {
        @Required
        @SerializedName("price")
        public double price;
        @Required
        @SerializedName("articleId")
        public String articleId;

        public EventNewPrice(double price, String articleId) {
            this.price = price;
            this.articleId = articleId;
        }

        @Override
        public String toJson() {
            return Builder.gson().toJson(this);
        }
    }

    static class EventNewDiscount implements JsonSerializable {
        @Required
        @SerializedName("discount")
        public double discount;
        @Required
        @SerializedName("codeDiscount")
        public String codeDiscount;
        @Required
        @SerializedName("discountDescription")
        public String discountDescription;
        @Required
        @SerializedName("articleId")
        public String articleId;
        @Required
        @SerializedName("startDate")
        public Date startDate;
        @Required
        @SerializedName("expirationDate")
        public Date expirationDate;

        public EventNewDiscount(String codeDiscount, double discount, String discountDescription, String articleId, Date expirationDate, Date startDate) {
            this.articleId = articleId;
            this.codeDiscount = codeDiscount;
            this.discount = discount;
            this.discountDescription = discountDescription;
            this.expirationDate = expirationDate;
            this.startDate = startDate;
        }

        @Override
        public String toJson() {
            return Builder.gson().toJson(this);
        }
    }

        static class EventArticleExist implements JsonSerializable {
            @Required
            @SerializedName("articleId")
            public String articleId;
            @Required
            @SerializedName("referenceId")
            public String referenceId;
            @SerializedName("valid")
            public boolean valid;

            public static EventArticleExist fromJson(String json) {
                return Builder.gson().fromJson(json, EventArticleExist.class);
            }

            @Override
            public String toJson() {
                return Builder.gson().toJson(this);
            }
        }

    }
