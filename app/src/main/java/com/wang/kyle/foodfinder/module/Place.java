package com.wang.kyle.foodfinder.module;

import java.io.Serializable;

/**
 * Created by Kyle on 5/4/2016.
 */
public class Place  implements Serializable{
    private static final long serialVersionUID = -2756278026637570284L;
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private OpeningHours opening_hours;
    private Photos[] photos;
    private String place_id;
    private String scope;
    private AltIds alt_ids;
    private String reference;
    private String[] types;
    private String vicinity;
    private float rating;


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(OpeningHours opening_hours) {
        this.opening_hours = opening_hours;
    }

    public Photos[] getPhotos() {
        return photos;
    }

    public void setPhotos(Photos[] photos) {
        this.photos = photos;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public AltIds getAlt_ids() {
        return alt_ids;
    }

    public void setAlt_ids(AltIds alt_ids) {
        this.alt_ids = alt_ids;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    private class AltIds implements Serializable{
        private static final long serialVersionUID = -1899294313084423864L;
        private String place_id;
        private String scope;

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }

    public class Photos implements Serializable{
        private static final long serialVersionUID = -8397475843480328722L;
        private int height;
        private String[] html_attributions;
        private String photo_reference;
        private int width;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String[] getHtml_attributions() {
            return html_attributions;
        }

        public void setHtml_attributions(String[] html_attributions) {
            this.html_attributions = html_attributions;
        }

        public String getPhoto_reference() {
            return photo_reference;
        }

        public void setPhoto_reference(String photo_reference) {
            this.photo_reference = photo_reference;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    private class OpeningHours implements Serializable{
        private static final long serialVersionUID = 4400864374171510882L;
        private boolean open_now;

        public boolean isOpen_now() {
            return open_now;
        }

        public void setOpen_now(boolean open_now) {
            this.open_now = open_now;
        }
    }

    public class Geometry implements Serializable{
        private static final long serialVersionUID = -7716498122875620949L;
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public class Location implements Serializable{
        private static final long serialVersionUID = 7897065746335704703L;
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
