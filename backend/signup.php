<?php
require 'db_connect.php';

header("Content-Type: application/json");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username = $_POST['username'];
    $email = $_POST['email'];
    $password = $_POST['password'];

    if (empty($username) || empty($email) || empty($password)) {
        echo json_encode(["status" => "error", "message" => "All fields are required"]);
        exit();
    }

    // Check if user exists
    $checkQuery = "SELECT id FROM users WHERE email = ? OR username = ?";
    $stmt = $conn->prepare($checkQuery);
    $stmt->bind_param("ss", $email, $username);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        echo json_encode(["status" => "error", "message" => "User already exists"]);
        exit();
    }

    $passwordHash = password_hash($password, PASSWORD_DEFAULT);
    $profileImage = isset($_POST['profile_image']) ? $_POST['profile_image'] : null;

    $insertQuery = "INSERT INTO users (username, email, password_hash, profile_image) VALUES (?, ?, ?, ?)";
    $stmt = $conn->prepare($insertQuery);
    $stmt->bind_param("ssss", $username, $email, $passwordHash, $profileImage);

    if ($stmt->execute()) {
        $userId = $stmt->insert_id;
        // Initialize default settings
        $settingsQuery = "INSERT INTO user_settings (user_id) VALUES (?)";
        $stmtSettings = $conn->prepare($settingsQuery);
        $stmtSettings->bind_param("i", $userId);
        $stmtSettings->execute();

        echo json_encode(["status" => "success", "message" => "User registered successfully", "user_id" => $userId]);
    } else {
        echo json_encode(["status" => "error", "message" => "Registration failed: " . $stmt->error]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}
?>