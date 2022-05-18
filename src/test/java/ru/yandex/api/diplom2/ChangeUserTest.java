package ru.yandex.api.diplom2;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.diplom2.model.User;
import ru.yandex.api.diplom2.client.model.CreateUserClient;

import static org.hamcrest.Matchers.is;

public class ChangeUserTest {
    CreateUserClient сreateUserClient;
    User user;
    String token;
    Faker faker;

    @Before
    public void setUp() {
        сreateUserClient = new CreateUserClient();
        user = User.getRandom();
        faker = new Faker();

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
    }

    @Test
    @DisplayName("Изменение имени пользователя")
    public void changeNameUser() {
        String name = faker.name().name();
        user.setName(name);
        сreateUserClient.changeUser(user, token).assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("user.name", is(name));
    }

    @Test
    @DisplayName("изменение почты пользователя")
    public void changeEmailUser() {
        String email = faker.internet().emailAddress();
        user.setEmail(email);
        сreateUserClient.changeUser(user, token).assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", is(email));
    }

    @Test
    @DisplayName("Изменение пароля пользователя")
    public void changePasswordUser() {
        String password = faker.lorem().characters(10, true);
        user.setEmail(password);
        сreateUserClient.changeUser(user, token).assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changeUserWithoutAuth() {
        сreateUserClient.changeUser(user).assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
