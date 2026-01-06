// Authentication utilities
function saveToken(token) {
    localStorage.setItem('token', token);
}

function getToken() {
    return localStorage.getItem('token');
}

function clearToken() {
    localStorage.removeItem('token');
}

function isAuthenticated() {
    return !!getToken();
}

function redirectToDashboard() {
    window.location.href = 'dashboard.html';
}

function redirectToLogin() {
    window.location.href = 'index.html';
}

// Check authentication on protected pages
function requireAuth() {
    if (!isAuthenticated()) {
        redirectToLogin();
        return false;
    }
    return true;
}

// Logout function
function logout() {
    clearToken();
    // Prevent back button from accessing dashboard
    window.location.replace('index.html');
}

// Handle login form
async function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;
    const errorDiv = document.getElementById('loginError');
    const submitBtn = event.target.querySelector('button[type="submit"]');

    try {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Logging in...';
        errorDiv.classList.add('d-none');

        const response = await api.login(username, password);
        saveToken(response.token);
        redirectToDashboard();
    } catch (error) {
        errorDiv.textContent = error.message || 'Invalid credentials';
        errorDiv.classList.remove('d-none');
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Login';
    }
}

// Handle register form
async function handleRegister(event) {
    event.preventDefault();

    const username = document.getElementById('registerUsername').value;
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;
    const errorDiv = document.getElementById('registerError');
    const submitBtn = event.target.querySelector('button[type="submit"]');

    try {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Registering...';
        errorDiv.classList.add('d-none');

        const response = await api.register(username, email, password);
        saveToken(response.token);
        redirectToDashboard();
    } catch (error) {
        errorDiv.textContent = error.message || 'Registration failed';
        errorDiv.classList.remove('d-none');
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Register';
    }
}
