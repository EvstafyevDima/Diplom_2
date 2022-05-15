package ru.yandex.api.diplom2;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.Matchers.is;


public class СreateUserTest {

    CreateUserClient createUserClient;
    User user;
    String token;


    @Before
    public void setUp() {
        createUserClient = new CreateUserClient();
    }

    @After
    public void tearDown() {
        if (token == null) {
            return;
        }
        createUserClient.deleteUser(token);
    }

    @Test
    @DisplayName("Создание пользователя")
    public void createUserTest() {
        user = User.getRandom();

        ValidatableResponse response = createUserClient.createUser(user);
        token = response.extract().path("accessToken");

        response.assertThat().statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithEmptyEmail() {
        user = new User("", "awdawdad", "adaddad");
        createUserClient.createUser(user).assertThat().statusCode(403)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithEmptyPassword() {
        user = new User("Dream.xxx@mail.ru", "", "eee");
        createUserClient.createUser(user).assertThat().statusCode(403)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithEmptyUsername() {
        user = new User("Dream.xxx@mail.ru", "eee", "");
        createUserClient.createUser(user).assertThat().statusCode(403)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("создание пользователя, который уже зарегистрирован")
    public void userAlreadyRegistered() {
        user = User.getRandom();
        createUserClient.createUser(user);
        ValidatableResponse response = createUserClient.createUser(user);

        response.assertThat().statusCode(403)
                .body("success", is(false))
                .body("message", is("User already exists"));

    }

}