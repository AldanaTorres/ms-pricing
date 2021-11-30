
import application.RabbitController;
import application.RestController;
import spark.Spark;
import utils.db.MongoStore;
import utils.errors.ErrorHandler;
import utils.server.CorsFilter;
import utils.server.Environment;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static void main(String[] args) {
        new Server().start();
    }

    void start() {
        MongoStore.init();

        Spark.exception(Exception.class, (ex, req, res) -> ErrorHandler.handleInternal(ex, req, res));
        Spark.port(Environment.getEnv().serverPort);
        Spark.staticFiles.location(Environment.getEnv().staticLocation);
        CorsFilter.apply();

        Spark.post("/v1/pricing", (req, res) -> RestController.addPrice(req, res));
        Spark.post("/v1/discount", (req, res) -> RestController.addDiscount(req, res));
        Spark.get("/v1/pricing/:articleId", (req, res) -> RestController.getPrice(req, res));
        Spark.get("/v1/pricing/:articleId/historic", (req, res) -> RestController.getHistoricPrices(req, res));
        Spark.get("/v1/discount/:articleId", (req, res) -> RestController.getDiscounts(req, res));

        Logger.getLogger("Validator").log(Level.INFO,
        "Pricing Service escuchando en el puerto : " + Environment.getEnv().serverPort);

        RabbitController.init();
    }

}