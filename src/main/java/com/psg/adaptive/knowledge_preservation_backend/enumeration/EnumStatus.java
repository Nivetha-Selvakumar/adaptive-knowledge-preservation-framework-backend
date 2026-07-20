package com.psg.adaptive.knowledge_preservation_backend.enumeration;

public enum EnumStatus {

    ACTIVE("Active", "ACTIVE"),
    INACTIVE("Inactive", "INACTIVE"),
    DELETE("Delete","DELETE");

    private final String displayName;
    private final String code;

    EnumStatus(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static EnumStatus fromCode(String code) {
        for (EnumStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + code);
    }
}
