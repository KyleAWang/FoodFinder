package com.wang.kyle.foodfinder.module;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kyle on 5/5/2016.
 */
public class PlaceDetail {
    private List<Address> address_components;
    private String formatted_address;
    private String formatted_phone_number;
    private Geometry geometry;
    private String icon;
    private String id;
    private String international_phone_number;
    private String name;
    private String place_id;
    private String scope;
    private AltIds alt_ids;
    private String rating;
    private String reference;
    private List<Review> reviews;
    private String[] types;
    private String url;
    private String vicinity;
    private Photos[] photos;
    private String website;

    public Photos[] getPhotos() {
        return photos;
    }

    public void setPhotos(Photos[] photos) {
        this.photos = photos;
    }

    public class Geometry implements Serializable {
        private static final long serialVersionUID = -7716498122875620949L;
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }


    public class Photos {
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

    public List<Address> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(List<Address> address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
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

    public String getInternational_phone_number() {
        return international_phone_number;
    }

    public void setInternational_phone_number(String international_phone_number) {
        this.international_phone_number = international_phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    private class AltIds {
        private String place_id;
        private String scope;

    }



    public class Location{
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }

    public  class Address{
        private  String long_name;
        private String short_name;
        private String[] types;

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public String[] getTypes() {
            return types;
        }

        public void setTypes(String[] types) {
            this.types = types;
        }

        public String getLong_name() {

            return long_name;
        }

        public void setLong_name(String long_name) {
            this.long_name = long_name;
        }
    }
}
