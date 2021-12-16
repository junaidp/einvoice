package com.einvoive.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Locations {
    A_GEOTECH_GENERAL("A GEOTECH GENERAL", "011"),
    A_GEOPHYSICS("A GEOPHYSICS", "018"),
    H_GEO_CONSULTING("H GEO CONSULTING", "017"),
    M_MINERAL_DRILLING("M MINERAL DRILLING", "032"),
    B_HARADH("B HARADH", "046"),
    B_MATERIAL_ENGG("B MATERIAL ENGG.", "047"),
    B_ABQAIQ("B ABQAIQ", "049"),
    B_DAMMAM("B DAMMAM", "051"),
    B_JUBAIL("B JUBAIL", "052"),
    B_RIYADH("B RIYADH", "053"),
    C_JEDDAH("C JEDDAH", "054"),
    C_YANBU("C YANBU", "056"),
    C_RABIGH("C RABIGH", "057"),
    C_TABUK("C TABUK", "058"),
    C_JIZAN("C JIZAN", "059"),
    E_NDT("E NDT", "071"),
    D_CHEMICAL_TEST_ECP("D CHEMICAL TEST  ECP", "081"),
    D_CHEMICAL_TEST_WP("D CHEMICAL TEST  WP", "082"),
    D_CALIBRATION_ECP("D CALIBRATION ECP", "083"),
    D_CALIBRATION_WP("D CALIBRATION WP", "084"),
    D_CALIBRATION_RYD("D CALIBRATION - RYD", "085"),
    F_CONSTRUCTION_SUPPO("F CONSTRUCTION SUPPO", "024"),
    F_FUGRO_GROUP("F FUGRO GROUP", "021"),
    J_SURVEY_SERVCS_95("J SURVEY SERVCS # 95", "095"),
    G_ADMINISTRATION("G ADMINISTRATION", "099");

//    private static final Map<String, Locations> BY_CODE = new HashMap<>();
    private final String name;
    private final String code;

//    static {
//        for (Locations e : values()) {
//            BY_CODE.put(e.code, e);
//        }
//    }

    Locations(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<Locations> getLocationsByValue(String value) {
        return Arrays.stream(Locations.values())
                .filter(locations -> locations.name.equals(value))
                .findFirst();
    }

    public String getCode() {
        return code;
    }

//    public static Locations valueOfCode(String code) {
//        return BY_CODE.get(code);
//    }

}
