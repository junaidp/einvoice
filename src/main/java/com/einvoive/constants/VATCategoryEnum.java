package com.einvoive.constants;

public enum VATCategoryEnum {

    S("Standard", 15, "S"),
    Z("Zero Rate ", 0, "Z"),
    E("Exempt", 0, "E"),
    O("Out of scope", 0, "O");

    private final String name;
    private final int value;
    private final String code;

    VATCategoryEnum(String name, int value, String code) {
        this.name = name;
        this.value = value;
        this.code = code;
    }
}
