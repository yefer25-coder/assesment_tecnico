package com.app.projects.infraestructure.rest.controller;

import com.app.projects.domain.port.in.CompleteTaskUseCase;
import com.app.projects.domain.port.in.CreateTaskUseCase;
import com.app.projects.domain.port.in.GetTasksUseCase;
import com.app.projects.domain.model.Task;
import com.app.projects.infraestructure.rest.dto.CreateTaskRequest;
import com.app.projects.infraestructure.rest.dto.TaskResponse;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final GetTasksUseCase getTasksUseCase;

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponse> createTask(@PathVariable UUID projectId,
            @Valid @RequestBody CreateTaskRequest request) {
        Task task = createTaskUseCase.createTask(projectId, request.getTitle());
        return ResponseEntity.ok(TaskResponse.fromDomain(task));
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable UUID projectId) {
        List<Task> tasks = getTasksUseCase.getTasks(projectId);
        return ResponseEntity.ok(tasks.stream()
                .map(TaskResponse::fromDomain)
                .toList());
    }

    @PatchMapping("/tasks/{id}/complete")
    public ResponseEntity<Void> completeTask(@PathVariable UUID id) {
        completeTaskUseCase.completeTask(id);
        return ResponseEntity.ok().build();
    }
}
