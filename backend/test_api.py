import requests
import json

BASE_URL = "http://localhost/axis_launcher/backend"

def test_signup():
    print("Testing Signup...")
    url = f"{BASE_URL}/signup.php"
    data = {
        "username": "testuser",
        "email": "test@example.com",
        "password": "password123"
    }
    response = requests.post(url, data=data)
    print(f"Status: {response.status_code}")
    print(f"Response: {response.text}")
    return response.json().get("user_id")

def test_login():
    print("\nTesting Login...")
    url = f"{BASE_URL}/login.php"
    data = {
        "email": "test@example.com",
        "password": "password123"
    }
    response = requests.post(url, data=data)
    print(f"Status: {response.status_code}")
    print(f"Response: {response.text}")
    return response.json().get("user").get("id")

def test_sync_settings(user_id):
    print("\nTesting Sync Settings...")
    url = f"{BASE_URL}/sync_settings.php"
    
    # Update
    data = {
        "user_id": user_id,
        "grid_rows": 6,
        "grid_columns": 5,
        "icon_size": 90,
        "show_labels": 0,
        "drawer_mode": "CLASSIC"
    }
    response = requests.post(url, data=data)
    print(f"Update Response: {response.text}")
    
    # Get
    response = requests.get(url, params={"user_id": user_id})
    print(f"Get Response: {response.text}")

def test_sync_favorites(user_id):
    print("\nTesting Sync Favorites...")
    url = f"{BASE_URL}/sync_favorites.php"
    
    # Update
    data = {
        "user_id": user_id,
        "favorites": ["com.android.settings", "com.android.chrome"]
    }
    response = requests.post(url, json=data)
    print(f"Update Response: {response.text}")
    
    # Get
    response = requests.get(url, params={"user_id": user_id})
    print(f"Get Response: {response.text}")

if __name__ == "__main__":
    try:
        # Note: This script assumes the PHP server is running and database is set up.
        # Since we can't run the server here, this is for the user to run.
        # However, for demonstration, I'll simulate the flow if I could.
        
        print("Starting API Tests...")
        # user_id = test_signup() # Commented out to avoid error if server not running
        # if user_id:
        #     test_login()
        #     test_sync_settings(user_id)
        #     test_sync_favorites(user_id)
        print("Please run this script against a running PHP server.")
    except Exception as e:
        print(f"Error: {e}")
