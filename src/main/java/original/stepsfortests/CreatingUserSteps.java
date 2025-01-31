package original.stepsfortests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import original.Constants;
import original.requestbodies.RequestBodyForCreatingOrUpdatingUserData;
import original.requestbodies.RequestBodyForLoginUser;
import original.responsebodies.RightResponseBodyAfterCreatingUser;
import original.responsebodies.RightResponseBodyForBadRequest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class CreatingUserSteps {

    @Step("Создаем пользователя")
    public Response createUser(RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(requestBodyForCreatingOrUpdatingUserData)
                        .when()
                        .post(Constants.ENDPOINT_FOR_CREATING_USER);
        return response;
    }

    @Step("Проверяем статус ответа")
    public void verifyStatus(Response response, Integer expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Step("Форматируем тело ответа в форматированный JSON")
    public String getFormattedResponseBody(Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RightResponseBodyAfterCreatingUser rightResponseBodyAfterCreatingUser =
                response.body().as(RightResponseBodyAfterCreatingUser.class);
        return gson.toJson(rightResponseBodyAfterCreatingUser);
    }

    @Step("Формируем ожидаемый JSON")
    public String generateExpectedJson(Response response) {
        String accessToken = response.then().extract().body().path("accessToken").toString();
        String refreshToken = response.then().extract().body().path("refreshToken").toString();
        String email = response.then().extract().body().path("user.email").toString();
        String name = response.then().extract().body().path("user.name").toString();
        return "{\n" +
                "  \"success\": true,\n" +
                "  \"user\": {\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"name\": \"" + name + "\"\n" +
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
    public Response loginUser(RequestBodyForLoginUser requestBodyForLoginUser) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(requestBodyForLoginUser)
                        .when()
                        .post(Constants.ENDPOINT_FOR_LOGIN_USER);
        return response;
    }

    @Step("Извлекаем accessToken из response")
    public String extractingToken(Response response) {
        String accessToken = response.then().extract().body().path("accessToken").toString();
        return accessToken.substring(7);
    }

    @Step("Удаляем пользователя по accessToken")
    public void deleteUser(String accessToken) {
        given()
                .auth().oauth2(accessToken)
                .when()
                .delete(Constants.ENDPOINT_FOR_DELETING_OR_UPDATING_USER_DATA);
    }

    @Step("Форматируем тело ответа в форматированный JSON (with bad request)")
    public String getFormattedErrorResponseBody(Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RightResponseBodyForBadRequest errorResponse =
                response.body().as(RightResponseBodyForBadRequest.class);
        return gson.toJson(errorResponse);
    }
}
