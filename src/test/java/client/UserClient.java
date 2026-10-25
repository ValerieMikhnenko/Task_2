
package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String BASE_URL =
            "https://qa-stellarburgers.education-services.ru";

    @Step("Создать пользователя")
    public Response register(User user) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Удалить пользователя")
    public Response delete(String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }

    @Step("Авторизоваться")
    public Response login(User user) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/login");
    }

    @Step("Обновить данные пользователя")
    public Response updateUser(String accessToken, User user) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Обновить данные пользователя без авторизации")
    public Response updateUserWithoutAuth(User user) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .patch("/api/auth/user");
    }
}
