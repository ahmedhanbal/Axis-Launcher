<?php
require 'db_connect.php';

header("Content-Type: application/json");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Update settings
    $user_id = $_POST['user_id'];
    $grid_rows = $_POST['grid_rows'];
    $grid_columns = $_POST['grid_columns'];
    $icon_size = $_POST['icon_size'];
    $show_labels = $_POST['show_labels']; // 0 or 1
    $drawer_mode = $_POST['drawer_mode'];

    if (empty($user_id)) {
        echo json_encode(["status" => "error", "message" => "User ID required"]);
        exit();
    }

    $query = "UPDATE user_settings SET grid_rows=?, grid_columns=?, icon_size=?, show_labels=?, drawer_mode=? WHERE user_id=?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("iiiisi", $grid_rows, $grid_columns, $icon_size, $show_labels, $drawer_mode, $user_id);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Settings updated"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Update failed: " . $stmt->error]);
    }

} elseif ($_SERVER['REQUEST_METHOD'] == 'GET') {
    // Fetch settings
    $user_id = $_GET['user_id'];

    if (empty($user_id)) {
        echo json_encode(["status" => "error", "message" => "User ID required"]);
        exit();
    }

    $query = "SELECT * FROM user_settings WHERE user_id = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $settings = $result->fetch_assoc();
        echo json_encode(["status" => "success", "settings" => $settings]);
    } else {
        echo json_encode(["status" => "error", "message" => "Settings not found"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}
?>