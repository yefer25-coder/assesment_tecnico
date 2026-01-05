package com.app.projects.domain.model;

import java.util.UUID;

public class Task {
    private UUID id;
    private UUID projectId;
    private String title;
    private boolean completed;
    private boolean deleted;

    public Task() {
    }

    public Task(UUID id, UUID projectId, String title, boolean completed, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.completed = completed;
        this.deleted = deleted;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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
        private UUID projectId;
        private String title;
        private boolean completed;
        private boolean deleted;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder projectId(UUID projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder completed(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Builder deleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Task build() {
            return new Task(id, projectId, title, completed, deleted);
        }
    }
}
