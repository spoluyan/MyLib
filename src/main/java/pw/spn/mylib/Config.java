package pw.spn.mylib;

import java.util.Collections;
import java.util.Map;

public interface Config {

    int PORT = 4567;
    String PORT_PROPERTY = "port";
    Map<String, Object> PORT_TPL_MAP = Collections.singletonMap(PORT_PROPERTY, String.valueOf(PORT));
    String ENCODING = "UTF-8";

    String PATH_PREFIX = "";
    String STATIC_FILE_LOCATION = "/public";
    String PAGE_NAME = "index.html";
    String JSON = "application/json";
    String DB_DIR = "/home/x/Cloud/Dropbox/";
    String DB_FILE = "mylib.ser";

    String PARAM_SEARCH = ":q";
    String PARAM_ID = ":id";
    String PARAM_STATUS = ":status";

    String ROUTE_INDEX = "/";
    String ROUTE_CATALOG = "/catalog";
    String ROUTE_SEARCH_BOOKS = "/search/" + PARAM_SEARCH;
    String ROUTE_UPDATE = "/book/" + PARAM_ID + "/" + PARAM_STATUS;
    String ROUTE_COVER = "/cover/" + PARAM_ID;
    String ROUTE_STOP = "/stop";
}
