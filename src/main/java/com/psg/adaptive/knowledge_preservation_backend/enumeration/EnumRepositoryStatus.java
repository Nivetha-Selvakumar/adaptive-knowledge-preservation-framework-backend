package com.psg.adaptive.knowledge_preservation_backend.enumeration;

public enum EnumRepositoryStatus {

    CREATED("Created", "CREATED"),
    ACTIVE("Active", "ACTIVE"),
    SYNCING("Syncing", "SYNCING"),
    FAILED("Failed", "FAILED"),
    DISABLED("Disabled", "DISABLED");

    private final String displayName;
    private final String code;

    EnumRepositoryStatus(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static EnumRepositoryStatus fromCode(String code) {

        for (EnumRepositoryStatus status : values()) {

            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }

        }

        throw new IllegalArgumentException(
                "Invalid Repository Status: " + code
        );

    }

}