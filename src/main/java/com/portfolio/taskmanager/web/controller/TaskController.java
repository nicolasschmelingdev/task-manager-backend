package com.portfolio.taskmanager.web.controller;

import com.portfolio.taskmanager.application.service.TaskService;
import com.portfolio.taskmanager.domain.model.Task;
import com.portfolio.taskmanager.web.dto.PageResponse;
import com.portfolio.taskmanager.web.dto.TaskCreateRequest;
import com.portfolio.taskmanager.web.dto.TaskResponse;
import com.portfolio.taskmanager.web.dto.TaskUpdateRequest;
import com.portfolio.taskmanager.web.mapper.TaskWebMapper;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PageResponse<TaskResponse>> list(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "search", required = false) String search,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        Page<Task> page = service.search(status, search, pageable);
        PageResponse<TaskResponse> body = new PageResponse<>(
                page.map(TaskWebMapper::toResponse).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSize(),
                page.getNumber());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> get(@PathVariable("id") UUID id) {
        Task task = service.get(id);
        return ResponseEntity.ok(TaskWebMapper.toResponse(task));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskCreateRequest request,
                                               UriComponentsBuilder uriBuilder) {
        Task created = service.create(TaskWebMapper.toDomain(request), null);
        URI location = uriBuilder.path("/api/tasks/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(TaskWebMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable("id") UUID id,
                                               @Valid @RequestBody TaskUpdateRequest request) {
        Task updated = service.update(id, TaskWebMapper.toDomain(request), null);
        return ResponseEntity.ok(TaskWebMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }
}
