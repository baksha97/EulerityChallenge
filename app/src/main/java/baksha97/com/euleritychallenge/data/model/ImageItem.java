package baksha97.com.euleritychallenge.data.model;

import com.google.gson.Gson;

public class ImageItem {
    private String updated;

    private String created;

    private String url;

    public ImageItem(String created, String updated, String url) {
        this.created = created;
        this.updated = updated;
        this.url = url;
    }

    public String getUpdated() {
        return updated;
    }

    public String getCreated() {
        return created;
    }

    public String getUrl() {
        return url;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "ImageItem.class - [updated = " + updated + ", created = " + created + ", url = " + url + "]";
    }
}

