package ru.yandex.api.diplom2;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrdersTest {
    OrdersClient OrdersClient;
    CreateUserClient сreateUserClient;
    IngredientsClient ingredientsClient;
    User user;
    IngredientsRequest ingredientsRequest;
    String token;
    private Response response;

    @Before
    public void setUp() {

        сreateUserClient = new CreateUserClient();
        OrdersClient = new OrdersClient();
        ingredientsClient = new IngredientsClient();
        user = User.getRandom();

        ValidatableResponse response = сreateUserClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @After
    public void tearDown() {
        сreateUserClient.deleteUser(token).assertThat()
                .statusCode(202)
                .body("success", is(true))
                .body("message", is("User successfully removed"));

        //TODO Удалить заказ. В документации нет
    }

    @Test
    @DisplayName("Создание заказа")
    public void createOrderWithAuth() {
        response = ingredientsClient.getIngredients();
        response.then().assertThat().statusCode(200);
        Ingredient[] getIngredients = response.body().as(IngredientsResponse.class).getData();

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(getIngredients[0]._id);
        ingredients.add(getIngredients[1]._id);
        ingredientsRequest = new IngredientsRequest(ingredients);

        OrdersClient.createOrders(ingredientsRequest, token).assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с невалидным хеш ингредиента")
    public void createOrderInvalidIngredientHashFail() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("9999999999");
        ingredientsRequest = new IngredientsRequest(ingredients);

        OrdersClient.createOrders(ingredientsRequest, token).assertThat()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createEmptyOrderFail() {
        ingredientsRequest = new IngredientsRequest(new ArrayList<>());
        OrdersClient.createOrders(ingredientsRequest, token).assertThat()
                .statusCode(400)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }
}
