package ru.yandex.api.diplom2;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.diplom2.client.IngredientsClient;
import ru.yandex.api.diplom2.client.OrdersClient;
import ru.yandex.api.diplom2.model.Ingredient;
import ru.yandex.api.diplom2.model.IngredientsRequest;
import ru.yandex.api.diplom2.model.IngredientsResponse;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrdersWithoutAuthTest {
    OrdersClient ordersClient;
    IngredientsClient ingredientsClient;
    IngredientsRequest ingredientsRequest;
    Response response;

    @Before
    public void setUp() {

        ordersClient = new OrdersClient();
        ingredientsClient = new IngredientsClient();

        response = ingredientsClient.getIngredients();
        response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthSuccess() {
        Ingredient[] getIngredients = response.body().as(IngredientsResponse.class).getData();

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(getIngredients[0]._id);
        ingredients.add(getIngredients[1]._id);

        ingredientsRequest = new IngredientsRequest(ingredients);

        ordersClient.createOrders(ingredientsRequest).assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }
}
