package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;

@Data
public class DashboardSummaryResponseDto {
    private String connectedApps;

    private String totalHistoryItems;

    private String pendingApps;

    private String totalRepositories;

    public DashboardSummaryResponseDto() {

    }

    public DashboardSummaryResponseDto(
            String connectedApps,
            String totalHistoryItems,
            String pendingApps,
            String totalRepositories
    ) {
        this.connectedApps = connectedApps;
        this.totalHistoryItems = totalHistoryItems;
        this.pendingApps = pendingApps;
        this.totalRepositories = totalRepositories;
    }

    public String getConnectedApps() {
        return connectedApps;
    }

    public void setConnectedApps(String connectedApps) {
        this.connectedApps = connectedApps;
    }

    public String getTotalHistoryItems() {
        return totalHistoryItems;
    }

    public void setTotalHistoryItems(String totalHistoryItems) {
        this.totalHistoryItems = totalHistoryItems;
    }

    public String getPendingApps() {
        return pendingApps;
    }

    public void setPendingApps(String pendingApps) {
        this.pendingApps = pendingApps;
    }

    public String getTotalRepositories() {
        return totalRepositories;
    }

    public void setTotalRepositories(String totalRepositories) {
        this.totalRepositories = totalRepositories;
    }
}
