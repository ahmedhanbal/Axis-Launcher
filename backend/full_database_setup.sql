-- Database Creation
CREATE DATABASE IF NOT EXISTS axis_launcher;
USE axis_launcher;

-- Users Table
-- Stores user authentication details
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    profile_image LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User Settings Table
-- Stores all customizable launcher preferences
CREATE TABLE IF NOT EXISTS user_settings (
    user_id INT PRIMARY KEY,
    -- Grid Settings
    grid_rows INT DEFAULT 5,
    grid_columns INT DEFAULT 4,
    
    -- Visual Settings
    icon_size INT DEFAULT 80,
    show_labels BOOLEAN DEFAULT TRUE,
    drawer_mode VARCHAR(20) DEFAULT 'DRAWER', -- 'DRAWER' or 'CLASSIC'
    
    -- Theme & Appearance (New fields based on App)
    theme VARCHAR(50) DEFAULT 'Light Theme',
    wallpaper VARCHAR(100) DEFAULT 'Default',
    icon_pack VARCHAR(50) DEFAULT 'classic',
    
    -- Privacy & Security (New fields based on App)
    app_lock BOOLEAN DEFAULT FALSE,
    hide_apps BOOLEAN DEFAULT FALSE,
    
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- User Favorites Table
-- Stores the list of favorite apps (pinned to home screen)
CREATE TABLE IF NOT EXISTS user_favorites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    package_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    -- Prevent duplicate favorites for the same user
    UNIQUE KEY unique_fav (user_id, package_name)
);

-- Optional: Hidden Apps Table
-- If you want to sync specifically WHICH apps are hidden
CREATE TABLE IF NOT EXISTS user_hidden_apps (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    package_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_hidden (user_id, package_name)
);

-- Optional: Locked Apps Table
-- If you want to sync specifically WHICH apps are locked
CREATE TABLE IF NOT EXISTS user_locked_apps (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    package_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_locked (user_id, package_name)
);
