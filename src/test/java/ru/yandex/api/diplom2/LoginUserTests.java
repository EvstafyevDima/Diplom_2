package ru.yandex.api.diplom2;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.diplom2.model.User;
import ru.yandex.api.diplom2.client.model.CreateUserClient;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTests {
    CreateUserClient createUserClient;
    User user;
    String token;

    @Before
    public void setUp() {
        createUserClient = new CreateUserClient();
        user = User.getRandom();

        ValidatableResponse response = createUserClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat().statusCode(200)
                .body("success", is(true));
    }

    @After
    public void tearDown() {
        createUserClient.deleteUser(token).assertThat().statusCode(202)
                .body("success", is(true))
                .body("message", is("User successfully removed"));
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void loginUserSuccess() {
        createUserClient.loginUser(user).assertThat().statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Авторизация пользователя с некорректным паролем")
    public void loginUserPasswordIncorrectFail() {
        user.setPassword("477474747477477474747747474");
        createUserClient.loginUser(user).assertThat().statusCode(401)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с некорректным email")
    public void loginUserEmailIncorrectFail() {
        user.setEmail("drema.xxxx@mail.ru");
        createUserClient.loginUser(user).assertThat().statusCode(401)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }
}