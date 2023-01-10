package ru.yandex.api.diplom2;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.diplom2.client.IngredientsClient;
import ru.yandex.api.diplom2.client.OrdersClient;
import ru.yandex.api.diplom2.model.Ingredient;
import ru.yandex.api.diplom2.model.IngredientsRequest;
import ru.yandex.api.diplom2.model.IngredientsResponse;
import ru.yandex.api.diplom2.model.User;
import ru.yandex.api.diplom2.client.model.CreateUserClient;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {
    OrdersClient ordersClient;
    CreateUserClient сreateUserClient;
    IngredientsClient ingredientsClient;
    User user;
    IngredientsRequest ingredientsRequest;
    String token;

    @Before
    public void setUp() {
        ordersClient = new OrdersClient();
        сreateUserClient = new CreateUserClient();
        ingredientsClient = new IngredientsClient();

        user = User.getRandom();

        ValidatableResponse responseUser = сreateUserClient.createUser(user);
        token = responseUser.extract().path("accessToken");
        responseUser.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @After
    public void tearDown() {
        сreateUserClient.deleteUser(token).assertThat()
                .statusCode(202)
                .body("success", is(true))
                .body("message", is("User successfully removed"));
    }

    @Test
    @DisplayName("Получить заказы пользователя")
    public void getOrderWithAuthSuccess() {
        Response responseIngredients = ingredientsClient.getIngredients();
        responseIngredients.then().assertThat().statusCode(200);
        Ingredient[] getIngredients = responseIngredients.body().as(IngredientsResponse.class).getData();

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(getIngredients[0]._id);
        ingredients.add(getIngredients[1]._id);
        ingredientsRequest = new IngredientsRequest(ingredients);

        ordersClient.createOrders(ingredientsRequest, token).assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());

        ordersClient.getOrders(token).assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue());
    }
}

