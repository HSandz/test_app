-- Insert roles
INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('ADMIN');

-- Insert users (password is 'password' encoded with BCrypt)
INSERT INTO users (username, password, email, enabled) VALUES 
('user', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user@example.com', true);
INSERT INTO users (username, password, email, enabled) VALUES 
('admin', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'admin@example.com', true);

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- user gets USER role
INSERT INTO user_roles (user_id, role_id) VALUES (2, 1); -- admin gets USER role
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- admin gets ADMIN role

-- Insert sample courses
INSERT INTO courses (title, description, instructor, duration, price) VALUES 
('Introduction to Spring Boot', 'Learn the basics of Spring Boot framework', 'John Doe', 40, 299.99);
INSERT INTO courses (title, description, instructor, duration, price) VALUES 
('Advanced Java Programming', 'Deep dive into advanced Java concepts', 'Jane Smith', 60, 499.99);
INSERT INTO courses (title, description, instructor, duration, price) VALUES 
('Web Development with React', 'Build modern web applications with React', 'Bob Johnson', 45, 399.99);
INSERT INTO courses (title, description, instructor, duration, price) VALUES 
('Database Design and SQL', 'Learn database design principles and SQL', 'Alice Brown', 35, 249.99);