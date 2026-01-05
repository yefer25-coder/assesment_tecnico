package com.app.projects.domain.model;

import java.util.UUID;

public class Project {
    private UUID id;
    private UUID ownerId;
    private String name;
    private ProjectStatus status;
    private boolean deleted;

    public enum ProjectStatus {
        DRAFT, ACTIVE
    }

    public Project() {
    }

    public Project(UUID id, UUID ownerId, String name, ProjectStatus status, boolean deleted) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.status = status;
        this.deleted = deleted;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID ownerId;
        private String name;
        private ProjectStatus status;
        private boolean deleted;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder ownerId(UUID ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder status(ProjectStatus status) {
            this.status = status;
            return this;
        }

        public Builder deleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Project build() {
            return new Project(id, ownerId, name, status, deleted);
        }
    }
}
