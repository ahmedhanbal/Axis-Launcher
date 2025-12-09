<?php
require 'db_connect.php';

header("Content-Type: application/json");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Update favorites (Full sync: Delete all and re-insert)
    // Expecting JSON body for this one as it's an array
    $input = json_decode(file_get_contents('php://input'), true);

    $user_id = $input['user_id'];
    $favorites = $input['favorites']; // Array of package names

    if (empty($user_id)) {
        echo json_encode(["status" => "error", "message" => "User ID required"]);
        exit();
    }

    $conn->begin_transaction();

    try {
        // Delete existing favorites
        $deleteQuery = "DELETE FROM user_favorites WHERE user_id = ?";
        $stmt = $conn->prepare($deleteQuery);
        $stmt->bind_param("i", $user_id);
        $stmt->execute();

        // Insert new favorites
        if (!empty($favorites)) {
            $insertQuery = "INSERT INTO user_favorites (user_id, package_name) VALUES (?, ?)";
            $stmt = $conn->prepare($insertQuery);
            foreach ($favorites as $pkg) {
                $stmt->bind_param("is", $user_id, $pkg);
                $stmt->execute();
            }
        }

        $conn->commit();
        echo json_encode(["status" => "success", "message" => "Favorites synced"]);
    } catch (Exception $e) {
        $conn->rollback();
        echo json_encode(["status" => "error", "message" => "Sync failed: " . $e->getMessage()]);
    }

} elseif ($_SERVER['REQUEST_METHOD'] == 'GET') {
    // Fetch favorites
    $user_id = $_GET['user_id'];

    if (empty($user_id)) {
        echo json_encode(["status" => "error", "message" => "User ID required"]);
        exit();
    }

    $query = "SELECT package_name FROM user_favorites WHERE user_id = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $favorites = [];
    while ($row = $result->fetch_assoc()) {
        $favorites[] = $row['package_name'];
    }

    echo json_encode(["status" => "success", "favorites" => $favorites]);
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}
?>