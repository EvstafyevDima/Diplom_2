package ru.yandex.api.diplom2;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends StellarBurgersRestClient {
    public final String PATH = "https://stellarburgers.nomoreparties.site/api/ingredients";

    @Step("Ingredients - Получение данных об ингредиентах")
    public Response getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(PATH);
    }
}