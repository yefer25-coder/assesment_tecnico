package com.app.projects.infraestructure.rest.dto;

import com.app.projects.domain.model.Project;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private UUID id;
    private UUID ownerId;
    private String name;
    private String status;
    private boolean deleted;

    public static ProjectResponse fromDomain(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .ownerId(project.getOwnerId())
                .name(project.getName())
                .status(project.getStatus().name())
                .deleted(project.isDeleted())
                .build();
    }
}
