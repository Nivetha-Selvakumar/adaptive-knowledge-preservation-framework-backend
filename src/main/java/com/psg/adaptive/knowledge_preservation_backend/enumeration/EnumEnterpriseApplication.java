package com.psg.adaptive.knowledge_preservation_backend.enumeration;

public enum EnumEnterpriseApplication {

    GITHUB("GitHub", "GITHUB"),
    JIRA("Jira", "JIRA"),
    CONFLUENCE("Confluence", "CONFLUENCE"),
    GOOGLE_DRIVE("Google Drive", "GOOGLE_DRIVE"),
    OUTLOOK("Outlook", "OUTLOOK"),
    MICROSOFT_TEAMS("Microsoft Teams", "MICROSOFT_TEAMS"),
    GITLAB("GitLab", "GITLAB"),
    ONEDRIVE("OneDrive", "ONEDRIVE"),
    SLACK("Slack", "SLACK"),
    BITBUCKET("Bitbucket", "BITBUCKET");

    private final String displayName;
    private final String code;

    EnumEnterpriseApplication(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static EnumEnterpriseApplication fromCode(String code) {
        for (EnumEnterpriseApplication application : values()) {
            if (application.code.equalsIgnoreCase(code)) {
                return application;
            }
        }
        throw new IllegalArgumentException("Invalid Enterprise Application: " + code);
    }
}