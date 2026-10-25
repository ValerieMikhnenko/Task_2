package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.OrderRequest;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URL =
            "https://qa-stellarburgers.education-services.ru";

    @Step("Создать заказ")
    public Response createOrder(String accessToken, OrderRequest orderRequest) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(orderRequest)
                .when()
                .post("/api/orders");
    }

    @Step("Создать заказ без авторизации")
    public Response createOrderWithoutAuth(OrderRequest orderRequest) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(orderRequest)
                .when()
                .post("/api/orders");
    }

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/ingredients");
    }

    @Step("Получить заказы пользователя")
    public Response getUserOrders(String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders");
    }

    @Step("Получить заказы без авторизации")
    public Response getUserOrdersWithoutAuth() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/orders");
    }
}
