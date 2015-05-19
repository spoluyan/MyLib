package pw.spn.mylib.ui.component.book;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import org.apache.commons.io.IOUtils;

import pw.spn.mylib.util.UIUtil;

public class LoadCoverTask extends Task<Void> {
    private static final Map<String, Image> imagesCache = new HashMap<>();

    private String id;
    private String url;

    public LoadCoverTask(String id, String url) {
        this.id = id;
        this.url = url;
    }

    @Override
    protected Void call() throws Exception {
        Image image = loadImage();
        Platform.runLater(() -> UIUtil.updateCover(id, image));
        return null;
    }

    private Image loadImage() throws IOException {
        Image image = loadFromCache();
        if (image == null) {
            image = loadFromURL();
            imagesCache.put(id, image);
        }
        return image;
    }

    private Image loadFromCache() {
        return imagesCache.get(id);
    }

    private Image loadFromURL() throws IOException {
        byte[] imageData = IOUtils.toByteArray(URI.create(url));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
        Image image = new Image(inputStream);
        IOUtils.closeQuietly(inputStream);
        return image;
    }

}
