package tests;

import client.OrderClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientsTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    void getIngredientsReturnsList() {
        Response response = orderClient.getIngredients();

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertFalse(response.jsonPath().getList("data").isEmpty());
    }
}
