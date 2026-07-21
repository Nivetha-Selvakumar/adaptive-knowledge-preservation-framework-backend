package com.psg.adaptive.knowledge_preservation_backend.enumeration;

public enum EnumRole {

    SENIOR_DEVELOPER("Senior Developer", "SENIOR_DEVELOPER"),
    JUNIOR_DEVELOPER("Junior Developer", "JUNIOR_DEVELOPER"),
    TECH_LEAD_ARCHITECT("Tech Lead / Architect", "TECH_LEAD_ARCHITECT"),
    SYSTEM_ADMINISTRATOR("System Administrator", "SYSTEM_ADMINISTRATOR");

    private final String displayName;
    private final String code;

    EnumRole(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static EnumRole fromCode(String code) {
        for (EnumRole sex : values()) {
            if (sex.code.equalsIgnoreCase(code)) {
                return sex;
            }
        }
        throw new IllegalArgumentException("Invalid Role: " + code);
    }
}
