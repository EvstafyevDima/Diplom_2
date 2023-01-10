package ru.yandex.api.diplom2.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.api.diplom2.model.User;

import static io.restassured.RestAssured.given;

public class CreateUserClient extends StellarBurgersRestClient {

    private static final String User_PATH = "https://stellarburgers.nomoreparties.site/api/auth/";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(User_PATH + "register")
                .then()
                .log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(User_PATH + "login")
                .then()
                .log().all();
    }

    @Step("Изменение пользователя")
    public ValidatableResponse changeUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(User_PATH + "user")
                .then()
                .log().all();
    }

    @Step("Изменение пользователя")
    public ValidatableResponse changeUser(User user, String token) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", token)
                .body(user)
                .when()
                .patch(User_PATH + "user")
                .then()
                .log().all();
    }

    @Step("удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", token)
                .when()
                .delete(User_PATH + "user")
                .then()
                .log().all();
    }
}
