package com.app.projects.infraestructure.rest.dto;

import com.app.projects.domain.model.Task;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private UUID id;
    private UUID projectId;
    private String title;
    private boolean completed;
    private boolean deleted;

    public static TaskResponse fromDomain(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .projectId(task.getProjectId())
                .title(task.getTitle())
                .completed(task.isCompleted())
                .deleted(task.isDeleted())
                .build();
    }
}
