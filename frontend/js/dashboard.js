// Dashboard state
let projects = [];

// Initialize dashboard
async function initDashboard() {
    requireAuth();
    await loadProjects();
}

// Load all projects
async function loadProjects() {
    const container = document.getElementById('projectsContainer');
    const loadingDiv = document.getElementById('loading');

    try {
        loadingDiv.classList.remove('d-none');
        projects = await api.getProjects();
        renderProjects();
    } catch (error) {
        showError('Failed to load projects: ' + error.message);
    } finally {
        loadingDiv.classList.add('d-none');
    }
}

// Render projects
function renderProjects() {
    const container = document.getElementById('projectsContainer');

    if (projects.length === 0) {
        container.innerHTML = `
            <div class="col-12">
                <div class="alert alert-info">
                    No projects yet. Create your first project!
                </div>
            </div>
        `;
        return;
    }

    container.innerHTML = projects.map(project => `
        <div class="col-md-6 col-lg-4 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-header bg-${project.status === 'ACTIVE' ? 'success' : 'secondary'} text-white">
                    <h5 class="mb-0">${escapeHtml(project.name)}</h5>
                    <small>Status: ${project.status}</small>
                </div>
                <div class="card-body">
                    <div id="tasks-${project.id}" class="mb-3">
                        <div class="spinner-border spinner-border-sm" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>
                    <button class="btn btn-sm btn-primary" onclick="showCreateTaskModal('${project.id}')">
                        <i class="bi bi-plus-circle"></i> Add Task
                    </button>
                    ${project.status === 'DRAFT' ? `
                        <button class="btn btn-sm btn-success" onclick="activateProject('${project.id}')">
                            <i class="bi bi-check-circle"></i> Activate
                        </button>
                    ` : ''}
                </div>
            </div>
        </div>
    `).join('');

    // Load tasks for each project
    projects.forEach(project => loadTasks(project.id));
}

// Load tasks for a project
async function loadTasks(projectId) {
    const container = document.getElementById(`tasks-${projectId}`);

    try {
        const tasks = await api.getTasks(projectId);

        if (tasks.length === 0) {
            container.innerHTML = '<p class="text-muted small mb-0">No tasks yet</p>';
            return;
        }

        container.innerHTML = `
            <ul class="list-group list-group-flush">
                ${tasks.map(task => `
                    <li class="list-group-item px-0 py-2 d-flex justify-content-between align-items-center ${task.completed ? 'task-completed' : ''}">
                        <span>
                            ${escapeHtml(task.title)}
                        </span>
                        ${!task.completed ? `
                            <button class="btn btn-sm btn-outline-success" onclick="completeTask('${task.id}', '${projectId}')">
                                <i class="bi bi-check"></i>
                            </button>
                        ` : '<i class="bi bi-check-circle-fill task-check-icon"></i>'}
                    </li>
                `).join('')}
            </ul>
        `;
    } catch (error) {
        container.innerHTML = `<p class="text-danger small">Error loading tasks</p>`;
    }
}

// Create project
async function handleCreateProject(event) {
    event.preventDefault();

    const name = document.getElementById('projectName').value;
    const modal = bootstrap.Modal.getInstance(document.getElementById('createProjectModal'));

    try {
        await api.createProject(name);
        modal.hide();
        document.getElementById('createProjectForm').reset();
        await loadProjects();
        showSuccess('Project created successfully!');
    } catch (error) {
        showError('Failed to create project: ' + error.message);
    }
}

// Show create task modal
function showCreateTaskModal(projectId) {
    document.getElementById('taskProjectId').value = projectId;
    const modal = new bootstrap.Modal(document.getElementById('createTaskModal'));
    modal.show();
}

// Create task
async function handleCreateTask(event) {
    event.preventDefault();

    const projectId = document.getElementById('taskProjectId').value;
    const title = document.getElementById('taskTitle').value;
    const modal = bootstrap.Modal.getInstance(document.getElementById('createTaskModal'));

    try {
        await api.createTask(projectId, title);
        modal.hide();
        document.getElementById('createTaskForm').reset();
        await loadTasks(projectId);
        showSuccess('Task created successfully!');
    } catch (error) {
        showError('Failed to create task: ' + error.message);
    }
}

// Activate project
async function activateProject(projectId) {
    console.log('Activating project:', projectId);

    // Store projectId for modal confirmation
    window.pendingActivationProjectId = projectId;

    // Show confirmation modal
    const modal = new bootstrap.Modal(document.getElementById('confirmActivationModal'));
    modal.show();
}

// Confirm activation (called from modal)
async function confirmActivation() {
    const projectId = window.pendingActivationProjectId;
    const modal = bootstrap.Modal.getInstance(document.getElementById('confirmActivationModal'));

    try {
        console.log('Calling API to activate project...');
        await api.activateProject(projectId);
        console.log('Project activated, reloading projects...');
        modal.hide();
        await loadProjects();
        showSuccess('Project activated successfully!');
    } catch (error) {
        console.error('Activation error:', error);
        modal.hide();
        showError('Failed to activate project: ' + error.message);
    }
}

// Complete task
async function completeTask(taskId, projectId) {
    try {
        await api.completeTask(taskId);
        await loadTasks(projectId);
        showSuccess('Task completed!');
    } catch (error) {
        showError('Failed to complete task: ' + error.message);
    }
}

// Utility functions
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function showSuccess(message) {
    showToast(message, 'success');
}

function showError(message) {
    showToast(message, 'danger');
}

function showToast(message, type) {
    const toastContainer = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast align-items-center text-white bg-${type} border-0`;
    toast.setAttribute('role', 'alert');
    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${escapeHtml(message)}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;
    toastContainer.appendChild(toast);
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
    toast.addEventListener('hidden.bs.toast', () => toast.remove());
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', initDashboard);
