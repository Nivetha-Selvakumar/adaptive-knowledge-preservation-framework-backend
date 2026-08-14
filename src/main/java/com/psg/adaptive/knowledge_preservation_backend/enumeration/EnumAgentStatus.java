package com.psg.adaptive.knowledge_preservation_backend.enumeration;

public enum EnumAgentStatus {

    CREATED("Created", "CREATED"),
    ACTIVE("Active", "ACTIVE"),
    SYNCING("Syncing", "SYNCING"),
    IDLE("Idle", "IDLE"),
    FAILED("Failed", "FAILED"),
    STOPPED("Stopped", "STOPPED");

    private final String displayName;
    private final String code;

    EnumAgentStatus(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static EnumAgentStatus fromCode(String code) {

        for (EnumAgentStatus status : values()) {

            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }

        }

        throw new IllegalArgumentException(
                "Invalid Agent Status: " + code
        );

    }

}