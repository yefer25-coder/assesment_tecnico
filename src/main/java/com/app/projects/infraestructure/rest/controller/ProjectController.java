package com.app.projects.infraestructure.rest.controller;

import com.app.projects.domain.model.Project;
import com.app.projects.domain.port.in.ActivateProjectUseCase;
import com.app.projects.domain.port.in.CreateProjectUseCase;
import com.app.projects.domain.port.in.GetProjectsUseCase;
import com.app.projects.domain.port.out.CurrentUserPort;
import com.app.projects.domain.port.out.ProjectRepositoryPort;
import com.app.projects.infraestructure.rest.dto.CreateProjectRequest;
import com.app.projects.infraestructure.rest.dto.ProjectResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final ActivateProjectUseCase activateProjectUseCase;
    private final GetProjectsUseCase getProjectsUseCase;
    private final CurrentUserPort currentUserPort;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        UUID ownerId = currentUserPort.getCurrentUserId();
        Project project = createProjectUseCase.createProject(ownerId, request.getName());
        return ResponseEntity.ok(ProjectResponse.fromDomain(project));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects() {
        UUID ownerId = currentUserPort.getCurrentUserId();
        List<Project> projects = getProjectsUseCase.getProjects(ownerId);
        return ResponseEntity.ok(projects.stream()
                .map(ProjectResponse::fromDomain)
                .toList());
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateProject(@PathVariable UUID id) {
        activateProjectUseCase.activateProject(id);
        return ResponseEntity.ok().build();
    }

    public static class CreateProjectRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
