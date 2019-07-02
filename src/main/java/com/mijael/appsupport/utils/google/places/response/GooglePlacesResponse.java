package com.mijael.appsupport.utils.google.places.response;

import androidx.annotation.NonNull;

import com.mijael.appsupport.utils.google.places.model.PlaceAutocomplete;

import java.util.List;

public class GooglePlacesResponse {

    private final List<PlaceAutocomplete> a;

    public GooglePlacesResponse(@NonNull List<PlaceAutocomplete> var1) {
        this.a = var1;

    }

    @NonNull
    public List<PlaceAutocomplete> getAutocompletePlaces() {
        return a;
    }


    @NonNull
    public static GooglePlacesResponse newInstance(@NonNull List<PlaceAutocomplete> var0) {
        return new GooglePlacesResponse(var0);
    }
}
