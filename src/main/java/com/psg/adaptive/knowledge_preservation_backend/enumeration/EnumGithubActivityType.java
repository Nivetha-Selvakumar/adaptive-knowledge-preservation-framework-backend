package com.psg.adaptive.knowledge_preservation_backend.enumeration;

public enum EnumGithubActivityType {
    COMMIT("Commit", "COMMIT"),
    ISSUE("Issue", "ISSUE"),
    PULL_REQUEST("Pull Request", "PULL_REQUEST"),
    README("README", "README"),
    BRANCH("Branch", "BRANCH"),
    RELEASE("Release", "RELEASE");

    private final String displayName;
    private final String code;

    EnumGithubActivityType(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static EnumGithubActivityType fromCode(String code) {

        for (EnumGithubActivityType type : values()) {

            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }

        throw new IllegalArgumentException(
                "Invalid GitHub activity type: " + code
        );
    }
}
