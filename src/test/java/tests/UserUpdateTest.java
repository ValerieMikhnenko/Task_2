package tests;

import client.UserClient;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserUpdateTest {

    private final UserClient userClient = new UserClient();
    private String accessToken;

    private User createUniqueUser() {
        return new User(
                "update_" + UUID.randomUUID() + "@example.com",
                "Password123",
                "TestUser"
        );
    }

    private void registerUser(User user) {
        Response response = userClient.register(user);

        assertEquals(200, response.statusCode());
        accessToken = response.jsonPath().getString("accessToken");
        assertNotNull(accessToken);
    }

    @Test
    void updateUserWithAuthReturnsUpdatedData() {
        User user = createUniqueUser();
        registerUser(user);

        User updatedUser = new User(
                "updated_" + UUID.randomUUID() + "@example.com",
                "NewPassword123",
                "UpdatedUser"
        );

        Response response = userClient.updateUser(accessToken, updatedUser);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertEquals(updatedUser.getEmail(),
                response.jsonPath().getString("user.email"));
        assertEquals(updatedUser.getName(),
                response.jsonPath().getString("user.name"));
    }

    @Test
    void updateUserWithoutAuthReturnsError() {
        User user = createUniqueUser();
        registerUser(user);

        User updatedUser = new User(
                "updated_" + UUID.randomUUID() + "@example.com",
                "NewPassword123",
                "UpdatedUser"
        );

        Response response = userClient.updateUserWithoutAuth(updatedUser);

        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
    }

    @AfterEach
    void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}
