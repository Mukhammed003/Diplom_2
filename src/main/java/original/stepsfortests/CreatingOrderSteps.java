package original.stepsfortests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import original.Constants;
import original.requestbodies.RequestBodyForCreatingOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreatingOrderSteps {

    @Step("Создаем заказ с авторизованным пользователем")
    public Response createOrderWithAuthorization(RequestBodyForCreatingOrder requestBodyForCreatingOrder, String accessToken) {
        Response response =
                given()
                        .auth().oauth2(accessToken)
                        .header("Content-type", "application/json")
                        .body(requestBodyForCreatingOrder)
                        .when()
                        .post(Constants.ENDPOINT_FOR_CREATING_ORDER_OR_RECEIVING_ORDERS_OF_SPECIFIC_USER);
        return response;
    }

    @Step("Создаем заказ без авторизаций пользователя")
    public Response createOrderWithoutAuthorization(RequestBodyForCreatingOrder requestBodyForCreatingOrder) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(requestBodyForCreatingOrder)
                        .when()
                        .post(Constants.ENDPOINT_FOR_CREATING_ORDER_OR_RECEIVING_ORDERS_OF_SPECIFIC_USER);
        return response;
    }

    @Step("Проверяем, что структура ответа правильная и содержит нужны поля")
    public void checkStructureOfResponseForCreatingOrder(Response response) {
        response.then()
                .assertThat()
                .body("success", notNullValue())
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }
}
