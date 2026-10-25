
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

public class OrderCreateTest {

    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();

    private String accessToken;

    @Step("Получить два действительных ID ингредиентов")
    private List<String> getValidIngredientIds() {
        Response response = orderClient.getIngredients();

        assertEquals(200, response.statusCode());

        String firstId = response.jsonPath().getString("data[0]._id");
        String secondId = response.jsonPath().getString("data[1]._id");

        assertNotNull(firstId);
        assertNotNull(secondId);

        return List.of(firstId, secondId);
    }

    @Step("Создать пользователя для оформления заказа")
    private void registerUser() {
        User user = new User(
                "order_" + UUID.randomUUID() + "@example.com",
                "Password123",
                "OrderUser"
        );

        Response response = userClient.register(user);

        assertEquals(200, response.statusCode());

        accessToken = response.jsonPath().getString("accessToken");
        assertNotNull(accessToken);
    }

    @Test
    @Step("Создание заказа с авторизацией и ингредиентами")
    void createOrderWithAuthAndIngredientsReturnsSuccess() {
        registerUser();

        OrderRequest request = new OrderRequest(getValidIngredientIds());

        Response response = orderClient.createOrder(accessToken, request);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertNotNull(response.jsonPath().get("order.number"));
    }

    @Test
    @Step("Создание заказа без авторизации")
    void createOrderWithoutAuthReturnsSuccess() {
        OrderRequest request = new OrderRequest(getValidIngredientIds());

        Response response = orderClient.createOrderWithoutAuth(request);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @Step("Создание заказа без ингредиентов")
    void createOrderWithoutIngredientsReturnsError() {
        OrderRequest request = new OrderRequest(List.of());

        Response response = orderClient.createOrderWithoutAuth(request);

        assertEquals(400, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
    }

    @Test
    @Step("Создание заказа с неверным хешем ингредиента")
    void createOrderWithInvalidIngredientHashReturnsError() {
        OrderRequest request =
                new OrderRequest(List.of("invalid_ingredient_hash"));

        Response response = orderClient.createOrderWithoutAuth(request);

        assertEquals(500, response.statusCode());
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

