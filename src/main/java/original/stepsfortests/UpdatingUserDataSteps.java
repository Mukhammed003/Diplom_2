package original.stepsfortests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import original.Constants;
import original.requestbodies.RequestBodyForCreatingOrUpdatingUserData;
import original.responsebodies.RightResponseBodyAfterUpdatingUserData;

import static io.restassured.RestAssured.given;

public class UpdatingUserDataSteps {

    @Step("Изменяем данные пользователя с авторизацией")
    public Response updateUserDataWithAuthorization(RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData, String accessToken) {
        Response response =
                given()
                        .auth().oauth2(accessToken)
                        .header("Content-type", "application/json")
                        .body(requestBodyForCreatingOrUpdatingUserData)
                        .when()
                        .patch(Constants.ENDPOINT_FOR_DELETING_OR_UPDATING_USER_DATA);
        return response;
    }

    @Step("Изменяем данные пользователя без авторизаци")
    public Response updateUserDataWithoutAuthorization(RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(requestBodyForCreatingOrUpdatingUserData)
                        .when()
                        .patch(Constants.ENDPOINT_FOR_DELETING_OR_UPDATING_USER_DATA);
        return response;
    }

    @Step("Форматирование тела ответа в форматированный JSON")
    public String getFormattedResponseBody(Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RightResponseBodyAfterUpdatingUserData rightResponseBodyAfterUpdatingUserData =
                response.body().as(RightResponseBodyAfterUpdatingUserData.class);
        return gson.toJson(rightResponseBodyAfterUpdatingUserData);
    }

    @Step("Формируем ожидаемый JSON")
    public String generateExpectedJson(Response response) {
        String email = response.then().extract().body().path("user.email").toString();
        String name = response.then().extract().body().path("user.name").toString();
        return "{\n" +
                "  \"success\": true,\n" +
                "  \"user\": {\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"name\": \"" + name + "\"\n" +
                "  }\n" +
                "}";
    }
}
