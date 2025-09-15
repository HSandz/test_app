// API Configuration
const API_BASE_URL = 'http://localhost:8080/api/tasks';

// DOM Elements
const taskForm = document.getElementById('task-form');
const tasksContainer = document.getElementById('tasks-container');
const searchInput = document.getElementById('search-input');
const searchBtn = document.getElementById('search-btn');
const showAllBtn = document.getElementById('show-all-btn');
const showPendingBtn = document.getElementById('show-pending-btn');
const showCompletedBtn = document.getElementById('show-completed-btn');
const editModal = document.getElementById('edit-modal');
const editTaskForm = document.getElementById('edit-task-form');
const closeModalBtn = document.querySelector('.close');

// State
let currentTasks = [];

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    loadTasks();
    setupEventListeners();
});

// Setup event listeners
function setupEventListeners() {
    taskForm.addEventListener('submit', handleAddTask);
    searchBtn.addEventListener('click', handleSearch);
    showAllBtn.addEventListener('click', () => loadTasks());
    showPendingBtn.addEventListener('click', () => loadTasksByStatus(false));
    showCompletedBtn.addEventListener('click', () => loadTasksByStatus(true));
    editTaskForm.addEventListener('submit', handleEditTask);
    closeModalBtn.addEventListener('click', closeEditModal);
    
    // Close modal when clicking outside
    editModal.addEventListener('click', function(e) {
        if (e.target === editModal) {
            closeEditModal();
        }
    });
    
    // Search on Enter key
    searchInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            handleSearch();
        }
    });
}

// API Functions
async function fetchTasks() {
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching tasks:', error);
        showMessage('Failed to load tasks. Make sure the backend is running.', 'error');
        return [];
    }
}

async function fetchTasksByStatus(completed) {
    try {
        const response = await fetch(`${API_BASE_URL}/status/${completed}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching tasks by status:', error);
        showMessage('Failed to load tasks by status.', 'error');
        return [];
    }
}

async function searchTasks(keyword) {
    try {
        const response = await fetch(`${API_BASE_URL}/search?keyword=${encodeURIComponent(keyword)}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error searching tasks:', error);
        showMessage('Failed to search tasks.', 'error');
        return [];
    }
}

async function createTask(task) {
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(task)
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error creating task:', error);
        showMessage('Failed to create task.', 'error');
        return null;
    }
}

async function updateTask(id, task) {
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(task)
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error updating task:', error);
        showMessage('Failed to update task.', 'error');
        return null;
    }
}

async function deleteTask(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return true;
    } catch (error) {
        console.error('Error deleting task:', error);
        showMessage('Failed to delete task.', 'error');
        return false;
    }
}

// UI Functions
async function loadTasks() {
    showLoading();
    const tasks = await fetchTasks();
    currentTasks = tasks;
    renderTasks(tasks);
}

async function loadTasksByStatus(completed) {
    showLoading();
    const tasks = await fetchTasksByStatus(completed);
    currentTasks = tasks;
    renderTasks(tasks);
}

function showLoading() {
    tasksContainer.innerHTML = '<div class="loading">Loading tasks...</div>';
}

function renderTasks(tasks) {
    if (tasks.length === 0) {
        tasksContainer.innerHTML = `
            <div class="empty-state">
                <h3>No tasks found</h3>
                <p>Start by adding a new task above.</p>
            </div>
        `;
        return;
    }

    const tasksHTML = tasks.map(task => `
        <div class="task-item ${task.completed ? 'completed' : ''}">
            <div class="task-header">
                <div>
                    <div class="task-title ${task.completed ? 'completed' : ''}">${escapeHtml(task.title)}</div>
                    <span class="task-status ${task.completed ? 'status-completed' : 'status-pending'}">
                        ${task.completed ? 'Completed' : 'Pending'}
                    </span>
                </div>
            </div>
            ${task.description ? `<div class="task-description">${escapeHtml(task.description)}</div>` : ''}
            <div class="task-actions">
                <button class="btn btn-secondary" onclick="openEditModal(${task.id})">Edit</button>
                <button class="btn ${task.completed ? 'btn-secondary' : 'btn-success'}" 
                        onclick="toggleTaskStatus(${task.id}, ${!task.completed})">
                    ${task.completed ? 'Mark Pending' : 'Mark Complete'}
                </button>
                <button class="btn btn-danger" onclick="confirmDeleteTask(${task.id})">Delete</button>
            </div>
        </div>
    `).join('');

    tasksContainer.innerHTML = tasksHTML;
}

// Event Handlers
async function handleAddTask(e) {
    e.preventDefault();
    
    const formData = new FormData(taskForm);
    const task = {
        title: formData.get('title').trim(),
        description: formData.get('description').trim(),
        completed: false
    };
    
    if (!task.title) {
        showMessage('Task title is required.', 'error');
        return;
    }
    
    const createdTask = await createTask(task);
    if (createdTask) {
        showMessage('Task created successfully!', 'success');
        taskForm.reset();
        loadTasks();
    }
}

async function handleSearch() {
    const keyword = searchInput.value.trim();
    if (!keyword) {
        loadTasks();
        return;
    }
    
    showLoading();
    const tasks = await searchTasks(keyword);
    currentTasks = tasks;
    renderTasks(tasks);
}

async function handleEditTask(e) {
    e.preventDefault();
    
    const taskId = document.getElementById('edit-task-id').value;
    const formData = new FormData(editTaskForm);
    
    const task = {
        title: formData.get('title').trim(),
        description: formData.get('description').trim(),
        completed: document.getElementById('edit-task-completed').checked
    };
    
    if (!task.title) {
        showMessage('Task title is required.', 'error');
        return;
    }
    
    const updatedTask = await updateTask(taskId, task);
    if (updatedTask) {
        showMessage('Task updated successfully!', 'success');
        closeEditModal();
        loadTasks();
    }
}

// Task Actions
async function toggleTaskStatus(id, completed) {
    const task = currentTasks.find(t => t.id === id);
    if (!task) return;
    
    const updatedTask = {
        ...task,
        completed: completed
    };
    
    const result = await updateTask(id, updatedTask);
    if (result) {
        showMessage(`Task marked as ${completed ? 'completed' : 'pending'}!`, 'success');
        loadTasks();
    }
}

async function confirmDeleteTask(id) {
    if (confirm('Are you sure you want to delete this task?')) {
        const success = await deleteTask(id);
        if (success) {
            showMessage('Task deleted successfully!', 'success');
            loadTasks();
        }
    }
}

// Modal Functions
function openEditModal(id) {
    const task = currentTasks.find(t => t.id === id);
    if (!task) return;
    
    document.getElementById('edit-task-id').value = task.id;
    document.getElementById('edit-task-title').value = task.title;
    document.getElementById('edit-task-description').value = task.description || '';
    document.getElementById('edit-task-completed').checked = task.completed;
    
    editModal.style.display = 'block';
}

function closeEditModal() {
    editModal.style.display = 'none';
    editTaskForm.reset();
}

// Utility Functions
function escapeHtml(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

function showMessage(message, type) {
    // Remove existing messages
    const existingMessages = document.querySelectorAll('.message');
    existingMessages.forEach(msg => msg.remove());
    
    // Create new message
    const messageElement = document.createElement('div');
    messageElement.className = `message ${type}`;
    messageElement.textContent = message;
    
    // Insert at the top of the main content
    const main = document.querySelector('main');
    main.insertBefore(messageElement, main.firstChild);
    
    // Auto-remove after 5 seconds
    setTimeout(() => {
        if (messageElement.parentNode) {
            messageElement.remove();
        }
    }, 5000);
}