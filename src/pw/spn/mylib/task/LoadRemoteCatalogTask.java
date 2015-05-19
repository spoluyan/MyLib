package pw.spn.mylib.task;

import javafx.application.Platform;
import javafx.concurrent.Task;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.util.UIUtil;

public class LoadRemoteCatalogTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        CatalogService.getInstance().getRemoteLibrary();
        Platform.runLater(() -> UIUtil.enableSearch());
        return null;
    }

}
