-- Insert Test User (password: password123)
INSERT INTO users (id, username, email, password)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'testuser', 'test@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnutj8iAt6.VwUhs.mZ5dQIyT6HwyeCJZe');

-- Insert Test Project
INSERT INTO projects (id, owner_id, name, status, deleted)
VALUES ('b1eebc99-9c0b-4ef8-bb6d-6bb9bd380b22', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Proyecto Demo', 'ACTIVE', false);

-- Insert Test Task
INSERT INTO tasks (id, project_id, title, completed, deleted)
VALUES ('c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380b22', 'Tarea de Ejemplo', false, false);
