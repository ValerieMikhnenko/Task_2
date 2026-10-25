
package tests;

import client.UserClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import model.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserCreateTest {

    private final UserClient userClient = new UserClient();
    private String accessToken;

    private User createUniqueUser() {
        return new User(
                "test_" + UUID.randomUUID() + "@example.com",
                "Password123",
                "TestUser"
        );
    }

    @Test
    void createUniqueUserReturnsSuccess() {
        User user = createUniqueUser();

        Response response = userClient.register(user);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertEquals(user.getEmail(),
                response.jsonPath().getString("user.email"));
        assertEquals(user.getName(),
                response.jsonPath().getString("user.name"));

        accessToken = response.jsonPath().getString("accessToken");
        assertNotNull(accessToken);
    }


    @Test
    void createAlreadyRegisteredUserReturnsError() {
        User user = createUniqueUser();

        Response firstResponse = userClient.register(user);
        assertEquals(200, firstResponse.statusCode());
        assertTrue(firstResponse.jsonPath().getBoolean("success"));

        accessToken = firstResponse.jsonPath().getString("accessToken");

        Response secondResponse = userClient.register(user);

        assertEquals(403, secondResponse.statusCode());
        assertFalse(secondResponse.jsonPath().getBoolean("success"));
        assertEquals("User already exists",
                secondResponse.jsonPath().getString("message"));
    }

    @Test
    void createUserWithoutEmailReturnsError() {
        User user = new User(null, "Password123", "TestUser");

        Response response = userClient.register(user);

        assertEquals(403, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals("Email, password and name are required fields",
                response.jsonPath().getString("message"));
    }


    @AfterEach
    void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}
