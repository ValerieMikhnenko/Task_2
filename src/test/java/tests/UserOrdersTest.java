
package tests;

import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.OrderRequest;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserOrdersTest {

    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();

    private String accessToken;

    @Step("Создать пользователя и получить токен")
    private void registerUser() {
        User user = new User(
                "orders_" + UUID.randomUUID() + "@example.com",
                "Password123",
                "OrdersUser"
        );

        Response response = userClient.register(user);

        assertEquals(200, response.statusCode());

        accessToken = response.jsonPath().getString("accessToken");
        assertNotNull(accessToken);
    }

    @Test
    @Step("Получение заказов авторизованного пользователя")
    void getOrdersWithAuthReturnsSuccess() {
        registerUser();

        Response response = orderClient.getUserOrders(accessToken);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertNotNull(response.jsonPath().get("orders"));
    }

    @Test
    @Step("Получение заказов без авторизации")
    void getOrdersWithoutAuthReturnsError() {
        Response response = orderClient.getUserOrdersWithoutAuth();

        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
    }

    @AfterEach
    @Step("Удалить тестового пользователя")
    void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
            accessToken = null;
        }
    }
}
