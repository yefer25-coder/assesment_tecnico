// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// HTTP Client with JWT token injection
class ApiClient {
    constructor() {
        this.baseUrl = API_BASE_URL;
    }

    getToken() {
        return localStorage.getItem('token');
    }

    async request(endpoint, options = {}) {
        const url = `${this.baseUrl}${endpoint}`;
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        const token = this.getToken();
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const config = {
            ...options,
            headers
        };

        try {
            const response = await fetch(url, config);

            if (!response.ok) {
                // Try to parse error response
                let errorMessage = `HTTP ${response.status}`;
                try {
                    const errorData = await response.json();
                    errorMessage = errorData.detail || errorData.message || errorData.title || errorMessage;
                } catch (e) {
                    // If JSON parsing fails, use status text
                    errorMessage = response.statusText || errorMessage;
                }
                throw new Error(errorMessage);
            }

            // Handle 204 No Content or empty responses
            if (response.status === 204 || response.headers.get('content-length') === '0') {
                return null;
            }

            // Check if response has content
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            }

            return null;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    }

    // Auth endpoints
    async register(username, email, password) {
        return this.request('/auth/register', {
            method: 'POST',
            body: JSON.stringify({ username, email, password })
        });
    }

    async login(username, password) {
        return this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        });
    }

    // Project endpoints
    async getProjects() {
        return this.request('/projects');
    }

    async createProject(name) {
        return this.request('/projects', {
            method: 'POST',
            body: JSON.stringify({ name })
        });
    }

    async activateProject(projectId) {
        return this.request(`/projects/${projectId}/activate`, {
            method: 'PATCH'
        });
    }

    // Task endpoints
    async getTasks(projectId) {
        return this.request(`/projects/${projectId}/tasks`);
    }

    async createTask(projectId, title) {
        return this.request(`/projects/${projectId}/tasks`, {
            method: 'POST',
            body: JSON.stringify({ title })
        });
    }

    async completeTask(taskId) {
        return this.request(`/tasks/${taskId}/complete`, {
            method: 'PATCH'
        });
    }
}

// Export singleton instance
const api = new ApiClient();
