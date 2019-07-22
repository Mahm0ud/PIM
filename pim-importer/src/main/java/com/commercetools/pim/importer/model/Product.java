package com.commercetools.pim.importer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    private String UUID;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("provider")
    private String provider;
    @JsonProperty("available")
    private boolean available;
    @JsonProperty("MeasurementUnits")
    private String measurementUnits;

    public Product() {
    }

    public Product(String UUID, String name, String description, String provider, boolean available, String measurementUnits) {
        this.UUID = UUID;
        this.name = name;
        this.description = description;
        this.provider = provider;
        this.available = available;
        this.measurementUnits = measurementUnits;
    }

    public String getUUID() {
        return UUID;
    }

    @JsonProperty("UUID")
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getMeasurementUnits() {
        return measurementUnits;
    }

    public void setMeasurementUnits(String measurementUnits) {
        this.measurementUnits = measurementUnits;
    }

    @Override
    public String toString() {
        return "Product{" +
                "UUID='" + UUID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", provider='" + provider + '\'' +
                ", available=" + available +
                ", measurementUnits='" + measurementUnits + '\'' +
                '}';
    }
}
