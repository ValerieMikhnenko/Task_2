package tests;

import client.UserClient;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserLoginTest {

    private final UserClient userClient = new UserClient();

    @Test
    void loginWithValidCredentialsReturnsSuccess() {
        User user = new User(
                "login_" + UUID.randomUUID() + "@example.com",
                "Password123",
                "TestUser"
        );

        Response registrationResponse = userClient.register(user);

        assertEquals(200, registrationResponse.statusCode());

        String accessToken = registrationResponse.jsonPath()
                .getString("accessToken");

        Response loginResponse = userClient.login(user);

        assertEquals(200, loginResponse.statusCode());
        assertTrue(loginResponse.jsonPath().getBoolean("success"));
        assertEquals(user.getEmail(),
                loginResponse.jsonPath().getString("user.email"));

        userClient.delete(accessToken);
    }

    @Test
    void loginWithInvalidCredentialsReturnsError() {
        User user = new User(
                "missing_" + UUID.randomUUID() + "@example.com",
                "WrongPassword123",
                "TestUser"
        );

        Response response = userClient.login(user);

        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
    }
}