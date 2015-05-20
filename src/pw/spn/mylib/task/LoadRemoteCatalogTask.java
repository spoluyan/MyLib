package pw.spn.mylib.task;

import javafx.concurrent.Task;
import pw.spn.mylib.service.CatalogService;

public class LoadRemoteCatalogTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        CatalogService.getInstance().getRemoteLibrary();
        return null;
    }

}
