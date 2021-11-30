package application;

import discount.Discount;
import discount.DiscountRepository;
import discount.DiscountsValids;
import discount.vo.NewDataDiscount;
import price.PriceDiscount;
import price.PricesHistoric;
import price.vo.NewData;
import price.Price;
import price.PriceRepository;
import security.TokenService;
import spark.Request;
import spark.Response;
import utils.errors.ErrorHandler;
import utils.errors.SimpleError;
import utils.errors.ValidationError;

import java.util.Date;
import java.util.List;

public class RestController {

    /**
     * @api {post} /v1/pricing/ Crear un precio para un artículo
     * @apiName Crear Precio
     * @apiGroup Precios
     *
     * @apiUse AuthHeader
     *
     * @apiExample {json} Body
     *   {
     *       "price": "{precio del artículo}",
     *       "articleId": "{id del artículo}",
     *   }
     *
     * @apiSuccessExample {json} Respuesta
     *   HTTP/1.1 200 OK
     *   {
     *       "_id": "{id de precio}"
     *       "createdAt": "{fecha de creación}",
     *       "price": "{precio del artículo}",
     *       "priceState": "{estado actual del precio}",
     *       "articleId": {id del artículo},
     *   }
     *
     * @apiUse Errors
     */
    public static String addPrice(Request req, Response res) {
        try {
            TokenService.validateAdmin(req.headers("Authorization"));

            Price price = Price.newPrice(NewData.fromJson(req.body()));
            List<Price> pricesHab = PriceRepository.getInstance().getAllByArticle(price.getArticleId());
            deshabilitarAnteriores(pricesHab);

            PriceRepository.getInstance().save(price);
            RabbitController.sendNewPrice(price);
            RabbitController.sendArticleValidation(price.getId(),price.getArticleId());

            return price.value().toJson();
        } catch (SimpleError | ValidationError e) {
            return ErrorHandler.handleError(res, e);
        }
    }

    private static void deshabilitarAnteriores(List<Price> pricesHab) {
        for (Price p : pricesHab) {
            p.deshabilitar();
            PriceRepository.getInstance().save(p);
        }
    }

    /**
     *
     * @api {get} /v1/pricing/:articleId Traer el precio de un artículo
     * @apiName Buscar precio de un artículo
     * @apiGroup Precios
     *
     *
     *
     * @apiSuccessExample {json} Respuesta
     *   HTTP/1.1 200 OK
     *   {
     *       "_id": "{id de precio}"
     *       "createdAt": "{fecha de creación}",
     *       "price": "{precio del artículo}",
     *       "priceState": "{estado actual del precio}",
     *       "articleId": {id del artículo},
     *   }
     *
     * @apiUse Errors
     */
    public static String getPrice(Request req, Response res) {
        Price price = PriceRepository.getInstance().findByCriteria(req.params(":articleId"));
        Discount discount = DiscountRepository.getInstance().findByCriteria(new Date(), new Date(),req.params(":articleId"),req.queryParams("codeDiscount"));
        PriceDiscount result = new PriceDiscount(price, discount);
        return result.value().toJson();
    }

    /**
     *
     * @api {get} /v1/pricing/:articleId/historic Traer el historico de precios de un artículo
     * @apiName Buscar precios de un artículo
     * @apiGroup Precios
     *
     *
     *
     * @apiSuccessExample {json} Respuesta
     *   HTTP/1.1 200 OK
     *   {
     *      "articleId": "{id del artículo relacionado}"
     *      "prices" : [
     *          {
     *              "_id" : "{id del precio}",
     *              "createdAt": "{fecha de creación}",
     *              "price”: "{precio del artículo}",
     *              "priceState": "{estado actual del precio}"
     *          },
     *          ...
     *                 ]
     *  }
     *
     * @apiUse Errors
     */
    public static String getHistoricPrices(Request req, Response res) {
        List<Price> prices = PriceRepository.getInstance().getAll(req.params(":articleId"));
        PricesHistoric result = new PricesHistoric(req.params(":articleId"), prices);
        return result.value().toJson();
    }

    /**
     * @api {post} /v1/discount/ Crear un descuento para un artículo
     * @apiName Crear Descuento
     * @apiGroup Precios
     *
     * @apiUse AuthHeader
     *
     * @apiExample {json} Body
     *   {
     *      "discountDescription": "{descripción del descuento}",
     *      "discount": "{porcentaje de descuento a aplicar}",
     *      "startDate": "{fecha de inicio del descuento}",
     *      "expirationDate": "{fecha de expiración}"
     *   }
     *
     * @apiSuccessExample {json} Respuesta
     *   HTTP/1.1 200 OK
     *   {
     *      "_id" : "{id del descuento}",
     *      "codeDiscount" : "{código del descuento}",
     *      "discountDescription": "{descripción del descuento}",
     *      "discount": "{porcentaje de descuento a aplicar}",
     *      "startDate": "{fecha de inicio del descuento}",
     *      "expirationDate": "{fecha de expiración}",
     *      "articleId": "{id del artículo relacionado}"
     *  }
     *
     *
     * @apiUse Errors
     */
    public static String addDiscount(Request req, Response res) {
        try {
            TokenService.validateAdmin(req.headers("Authorization"));

            Discount discount = Discount.newDiscount(NewDataDiscount.fromJson(req.body()));
            DiscountRepository.getInstance().save(discount);
            RabbitController.sendNewDiscount(discount);

            return discount.value().toJson();
        } catch (SimpleError | ValidationError e) {
            return ErrorHandler.handleError(res, e);
        }
    }

    /**
     *
     * @api {get} /v1/discount/:articleId Traer los descuentos de un artículo
     * @apiName Buscar descuentos de un artículo
     * @apiGroup Precios
     *
     *
     *
     * @apiSuccessExample {json} Respuesta
     *   HTTP/1.1 200 OK
     *   {
     *      "articleId": "{id del artículo relacionado}"
     *      "discounts" : [
     *          {
     *             "_id" : "{id del descuento}",
     *             "codeDiscount" : "{código del descuento}",
     *             "discountDescription": "{descripción del descuento}",
     *             "discount": "{porcentaje de descuento a aplicar}",
     *             "startDate": "{fecha de inicio del descuento}",
     *             "expirationDate": "{fecha de expiración}"
     *          },
     *          ...
     *                 ]
     *  }
     *
     * @apiUse Errors
     */
    public static String getDiscounts(Request req, Response res) {
        List<Discount> discounts = DiscountRepository.getInstance().getDiscountsValid(new Date(), req.params(":articleId"));
        DiscountsValids result = new DiscountsValids(req.params(":articleId"), discounts);
        return result.value().toJson();
    }

}
