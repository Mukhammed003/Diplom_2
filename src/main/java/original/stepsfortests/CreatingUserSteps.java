package original.stepsfortests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import original.Constants;
import original.requestbodies.RequestBodyForCreatingUser;
import original.requestbodies.RequestBodyForLoginUser;
import original.responsebodies.RightResponseBodyAfterCreatingUser;
import original.responsebodies.RightResponseBodyAfterCreatingUserWithBadRequest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class CreatingUserSteps {

    @Step("Создание пользователя")
    public Response createUser(RequestBodyForCreatingUser requestBodyForCreatingUser) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(requestBodyForCreatingUser)
                        .when()
                        .post(Constants.ENDPOINT_FOR_CREATING_USER);
        return response;
    }

    @Step("Проверка статуса ответа")
    public void verifyStatus(Response response, Integer expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Step("Форматирование тела ответа в форматированный JSON")
    public String getFormattedResponseBody(Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RightResponseBodyAfterCreatingUser rightResponseBodyAfterCreatingUser =
                response.body().as(RightResponseBodyAfterCreatingUser.class);
        return gson.toJson(rightResponseBodyAfterCreatingUser);
    }

    @Step("Формируем ожидаемый JSON с токенами")
    public String generateExpectedJson(Response response) {
        String accessToken = response.then().extract().body().path("accessToken").toString();
        String refreshToken = response.then().extract().body().path("refreshToken").toString();
        return "{\n" +
                "  \"success\": true,\n" +
                "  \"user\": {\n" +
                "    \"email\": \"mukhammed@yandex.ru\",\n" +
                "    \"name\": \"Mukhammed\"\n" +
                "  },\n" +
                "  \"accessToken\": \"" + accessToken + "\",\n" +
                "  \"refreshToken\": \"" + refreshToken + "\"\n" +
                "}";
    }

    @Step("Проверка, что тело ответа совпадает с ожидаемым")
    public void verifyResponseBody(String expectedJson, String actualJson) {
        assertEquals(expectedJson, actualJson);
    }

    @Step("Авторизуемся под пользователем")
    public String loginUser(RequestBodyForLoginUser requestBodyForLoginUser) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(requestBodyForLoginUser)
                        .when()
                        .post(Constants.ENDPOINT_FOR_LOGIN_USER);
        String accessToken = response.then().extract().body().path("accessToken").toString();
        return accessToken.substring(7);
    }

    @Step("Удаление пользователя по accessToken")
    public void deleteUser(String accessToken) {
        given()
                .auth().oauth2(accessToken)
                .when()
                .delete(Constants.ENDPOINT_FOR_DELETING_USER);
    }

    @Step("Форматирование тела ответа в форматированный JSON (with bad request)")
    public String getFormattedErrorResponseBody(Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RightResponseBodyAfterCreatingUserWithBadRequest errorResponse =
                response.body().as(RightResponseBodyAfterCreatingUserWithBadRequest.class);
        return gson.toJson(errorResponse);
    }
}
