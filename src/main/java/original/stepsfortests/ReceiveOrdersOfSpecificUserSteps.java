package original.stepsfortests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import original.Constants;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

public class ReceiveOrdersOfSpecificUserSteps {

    @Step("Получаем список заказов с авторизацией пользователя")
    public Response receiveOrdersOfAuthorizedUser(String accessToken) {
        Response response =
                given()
                        .auth().oauth2(accessToken)
                        .when()
                        .get(Constants.ENDPOINT_FOR_CREATING_ORDER_OR_RECEIVING_ORDERS_OF_SPECIFIC_USER);
        return response;
    }

    @Step("Получаем список заказов без авторизаций пользователя")
    public Response receiveOrdersOfUnauthorizedUser() {
        Response response =
                given()
                        .when()
                        .get(Constants.ENDPOINT_FOR_CREATING_ORDER_OR_RECEIVING_ORDERS_OF_SPECIFIC_USER);
        return response;
    }

    @Step("Проверяем, что структура ответа правильная")
    public void checkStructureOfResponseForReceivingOrdersOfSpecificuser(Response response) {
        response.then()
                .assertThat()
                .body("success", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue())
                .body("orders.size()", greaterThan(0));
    }
}
