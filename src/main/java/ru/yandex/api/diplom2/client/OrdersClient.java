package ru.yandex.api.diplom2.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.api.diplom2.model.IngredientsRequest;

import static io.restassured.RestAssured.given;

public class OrdersClient extends StellarBurgersRestClient {

    public final String Order_PATH = "https://stellarburgers.nomoreparties.site/api/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrders(IngredientsRequest ingredientsRequest, String token) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", token)
                .body(ingredientsRequest)
                .when()
                .post(Order_PATH)
                .then()
                .log().all();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrders(IngredientsRequest ingredientsRequest) {
        return given()
                .spec(getBaseSpec())
                .body(ingredientsRequest)
                .when()
                .post(Order_PATH)
                .then()
                .log().all();
    }

    @Step("Получить заказы")
    public ValidatableResponse getOrders(String token) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", token)
                .when()
                .get(Order_PATH)
                .then()
                .log().all();
    }

    @Step("Получить заказы")
    public ValidatableResponse getOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(Order_PATH)
                .then()
                .log().all();
    }
}
