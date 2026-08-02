package com.psg.adaptive.knowledge_preservation_backend.enumeration;

public enum EnumSyncStatus {
    SUCCESS("Success", "SUCCESS"),
    FAILED("Failed", "FAILED"),
    IN_PROGRESS("In Progress","IN_PROGRESS");

    private final String displayName;
    private final String code;

    EnumSyncStatus(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static EnumSyncStatus fromCode(String code) {
        for (EnumSyncStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Sync status: " + code);
    }
}
