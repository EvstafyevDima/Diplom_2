package ru.yandex.api.diplom2;


import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@Builder
public class User {

    public String email;
    public String password;
    public String name;

    @Step("Создание рандомного пользователя")
    public static User getRandom() {
        Faker faker = new Faker();
        final String email = faker.name().firstName() + "@gmail.su";
        final String password = RandomStringUtils.randomAlphabetic(8);
        final String name = faker.name().firstName();
        User user = new User(email, password, name);
        return user;
    }

}
