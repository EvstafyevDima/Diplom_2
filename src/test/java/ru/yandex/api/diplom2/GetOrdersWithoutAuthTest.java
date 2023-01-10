package ru.yandex.api.diplom2;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.diplom2.client.OrdersClient;

import static org.hamcrest.Matchers.is;

public class GetOrdersWithoutAuthTest {
    OrdersClient ordersClient;

    @Before
    public void setUp() {

        ordersClient = new OrdersClient();
    }

    @Test
    @DisplayName("Получить заказ без авторизации")
    public void getOrderWithoutAuthFail() {

        ordersClient.getOrders().assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
