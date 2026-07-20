package com.psg.adaptive.knowledge_preservation_backend.enumeration;

public enum EnumSex {

    MALE("Male", "MALE"),
    FEMALE("Female", "FEMALE"),
    OTHER("Other", "OTHER");

    private final String displayName;
    private final String code;

    EnumSex(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static EnumSex fromCode(String code) {
        for (EnumSex sex : values()) {
            if (sex.code.equalsIgnoreCase(code)) {
                return sex;
            }
        }
        throw new IllegalArgumentException("Invalid sex: " + code);
    }
}
