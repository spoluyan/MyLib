package pw.spn.mylib;

import freemarker.template.Configuration;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.domain.BookStatus;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.service.ParserService;
import pw.spn.mylib.service.SearchService;
import pw.spn.mylib.web.JSONTransformer;
import pw.spn.mylib.web.UILoader;
import spark.ModelAndView;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.setPort;
import static spark.SparkBase.staticFileLocation;
import static spark.SparkBase.stop;
import spark.template.freemarker.FreeMarkerEngine;

public final class MyLib {

    private static final Logger LOG = LoggerFactory.getLogger(MyLib.class);

    private FreeMarkerEngine tplEngine;
    private final JSONTransformer responseTransformer = new JSONTransformer();
    private final File catalog = new File(Config.DB_DIR + Config.DB_FILE);

    private final CatalogService catalogService = new CatalogService(catalog);
    private final ParserService parserService = new ParserService();
    private final SearchService searchService = new SearchService();

    private Set<Book> library;
    private Set<Book>  remoteLibrary;

    private void start() {
        LOG.info("Initialization started.");
        loadLocalCatalog();
        configureTemplateEngine();
        mapRoutes();
        loadRemoteCatalog();
        LOG.info("Initialization finished.");
    }

    private void loadLocalCatalog() {
        LOG.info("Loading local catalog.");
        library = catalogService.load();
    }

    private void configureTemplateEngine() {
        LOG.info("Creating template engine configuration.");
        final Configuration config = new Configuration();
        config.setClassForTemplateLoading(UILoader.class, Config.PATH_PREFIX);
        tplEngine = new FreeMarkerEngine(config);
        staticFileLocation(Config.STATIC_FILE_LOCATION);
    }

    private void mapRoutes() {
        LOG.info("Mapping routes.");
        setPort(Config.PORT);

        //index.html
        get(Config.ROUTE_INDEX, (rq, rs) -> new ModelAndView(Config.PORT_TPL_MAP, Config.PAGE_NAME), tplEngine);

        //get local catalog
        get(Config.ROUTE_CATALOG, Config.JSON, (rq, rs) -> {
            return library;
        }, responseTransformer);

        //search books
        get(Config.ROUTE_SEARCH_BOOKS, Config.JSON, (rq, rs) -> {
            String query = null;
            try {
                query = URLDecoder.decode(rq.params(Config.PARAM_SEARCH), Config.ENCODING).toLowerCase();
            } catch (UnsupportedEncodingException ex) {
                LOG.error("Unsupported encoding {}", Config.ENCODING);
            }
            return searchService.searchBooks(query, remoteLibrary, library);
        }, responseTransformer);

        //update book status
        post(Config.ROUTE_UPDATE, (rq, rs) -> {
            long id = Long.parseLong(rq.params(Config.PARAM_ID));
            BookStatus status = BookStatus.valueOf(rq.params(Config.PARAM_STATUS));
            library = catalogService.update(library, id, status, remoteLibrary);
            return StringUtils.EMPTY;
        });

        //submit cover task
        post(Config.ROUTE_COVER, (rq, rs) -> {
            long id = Long.parseLong(rq.params(Config.PARAM_ID));
            String image = parserService.loadImage(id);
            library = catalogService.update(library, id, image);
            return StringUtils.EMPTY;
        });
        
        get(Config.ROUTE_STOP, (rq, rs) -> {
            stop();
            return StringUtils.EMPTY;
        });
    }

    private void loadRemoteCatalog() {
        LOG.info("Loading remote catalog.");
        remoteLibrary = parserService.loadRemoteCatalog();
    }

    public static void main(String... args) {
        new MyLib().start();
    }
}
