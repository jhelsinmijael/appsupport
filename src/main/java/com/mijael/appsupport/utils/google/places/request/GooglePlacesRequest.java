package com.mijael.appsupport.utils.google.places.request;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.mijael.appsupport.utils.google.places.model.TypeFilter;

public class GooglePlacesRequest {

    private String query;
    private LatLng location;
    private String country;
    //private AutocompleteSessionToken autocompleteSessionToken;
    private TypeFilter typeFilter;
    private int radius;
    private Boolean strictBounds;

    //GooglePlacesRequest(@Nullable String query, @Nullable LatLng location, @Nullable String country, @Nullable AutocompleteSessionToken autocompleteSessionToken, @Nullable TypeFilter TypeFilter, @Nullable int radius, @Nullable Boolean strictBounds) {
    //    this.query = query;
    //    this.location = location;
    //    this.country = country;
    //    //this.autocompleteSessionToken = autocompleteSessionToken;
    //    this.typeFilter = TypeFilter;
    //    this.radius = radius;
    //    this.strictBounds = strictBounds;
    //}

    GooglePlacesRequest(@Nullable String query, @Nullable LatLng location, @Nullable String country, @Nullable TypeFilter TypeFilter, @Nullable int radius, @Nullable Boolean strictBounds) {
        this.query = query;
        this.location = location;
        this.country = country;
        //this.autocompleteSessionToken = autocompleteSessionToken;
        this.typeFilter = TypeFilter;
        this.radius = radius;
        this.strictBounds = strictBounds;
    }


    @Nullable
    public String getQuery() {
        return query;
    }

    @Nullable
    public LatLng getLocation() {
        return location;
    }

    @Nullable
    public String getCountry() {
        return country;
    }

    //@Nullable
    //public AutocompleteSessionToken getAutocompleteSessionToken() {
    //    return autocompleteSessionToken;
    //}

    @Nullable
    public TypeFilter getTypeFilter() {
        return typeFilter;
    }

    @Nullable
    public int getRadius() {
        return radius;
    }

    @Nullable
    public Boolean isStrictBounds() {
        return strictBounds;
    }


    public static Builder builder(){
        return new Builder();
    }


    public static class Builder {
        private String query;
        private LatLng location;
        private String country;
        //private AutocompleteSessionToken autocompleteSessionToken;
        private TypeFilter typeFilter;
        private int radius;
        private Boolean strictBounds;

        public Builder() {
        }

        public final Builder setQuery(@Nullable String query) {
            this.query = query;
            return this;
        }

        public final Builder setCountry(@Nullable String country) {
            this.country = country;
            return this;
        }

        //public final Builder setSessionToken(@Nullable AutocompleteSessionToken autocompleteSessionToken) {
        //    //this.autocompleteSessionToken = autocompleteSessionToken;
        //    return this;
        //}

        public final Builder setTypeFilter(@Nullable TypeFilter typeFilter) {
            this.typeFilter = typeFilter;
            return this;
        }

        public final Builder setRadius(@Nullable int radius){
            this.radius = radius;
            return this;
        }

        public final Builder setLocation(@Nullable LatLng location){
            this.location = location;
            return this;
        }

        public final Builder setStrictBounds(@Nullable Boolean strictBounds){
            this.strictBounds = strictBounds;
            return this;
        }

        public final GooglePlacesRequest build() {
            return new GooglePlacesRequest(query, location, country, typeFilter, radius, strictBounds);
        }
    }

}
