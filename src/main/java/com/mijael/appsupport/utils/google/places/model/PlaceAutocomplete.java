package com.mijael.appsupport.utils.google.places.model;

import java.util.List;

public class PlaceAutocomplete {

    private String description;
    private String id;
    private String place_id;
    private String reference;
    private StructuredFormatting structured_formatting;
    private List<Terms> terms;
    private List<String> types;


    public class StructuredFormatting{
        private String main_text;
        private String secondary_text;

        public String getMain_text() {
            return main_text;
        }

        public String getSecondary_text() {
            return secondary_text;
        }

        @Override
        public String toString() {
            return "StructuredFormatting{" +
                    "main_text='" + main_text + '\'' +
                    ", secondary_text='" + secondary_text + '\'' +
                    '}';
        }
    }

    public class Terms{
        private String offset;
        private String value;

        public String getOffset() {
            return offset;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Terms{" +
                    "offset='" + offset + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getPlaceId() {
        return place_id;
    }

    public String getReference() {
        return reference;
    }

    public StructuredFormatting getStructured_formatting() {
        return structured_formatting;
    }

    public List<Terms> getTerms() {
        return terms;
    }

    public List<String> getTypes() {
        return types;
    }

    @Override
    public String toString() {
        return "PlaceAutocomplete{" +
                "description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", place_id='" + place_id + '\'' +
                ", reference='" + reference + '\'' +
                ", structured_formatting=" + structured_formatting +
                ", terms=" + terms +
                ", types=" + types +
                '}';
    }

}
