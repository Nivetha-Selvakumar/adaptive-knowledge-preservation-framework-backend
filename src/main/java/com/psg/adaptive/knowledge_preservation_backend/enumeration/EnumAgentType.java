package com.psg.adaptive.knowledge_preservation_backend.enumeration;

public enum EnumAgentType {

    REPOSITORY("Repository Agent", "REPOSITORY"),
    JIRA("Jira Agent", "JIRA"),
    CONFLUENCE("Confluence Agent", "CONFLUENCE"),
    GOOGLE_DRIVE("Google Drive Agent", "GOOGLE_DRIVE"),
    OUTLOOK("Outlook Agent", "OUTLOOK"),
    MICROSOFT_TEAMS("Microsoft Teams Agent", "MICROSOFT_TEAMS"),
    GITLAB("GitLab Agent", "GITLAB"),
    ONEDRIVE("OneDrive Agent", "ONEDRIVE");

    private final String displayName;
    private final String code;

    EnumAgentType(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static EnumAgentType fromCode(String code) {

        for (EnumAgentType type : values()) {

            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }

        }

        throw new IllegalArgumentException(
                "Invalid Agent Type: " + code
        );

    }

}