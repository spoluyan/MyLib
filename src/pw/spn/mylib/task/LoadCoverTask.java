package pw.spn.mylib.task;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

import org.apache.commons.io.IOUtils;

public class LoadCoverTask extends Task<Image> {
    private static final Map<String, Image> imagesCache = new HashMap<>();

    private String id;
    private String url;

    public LoadCoverTask(String id, String url) {
        this.id = id;
        this.url = url;
    }

    @Override
    protected Image call() throws Exception {
        Image image = loadImage();
        return image;
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
