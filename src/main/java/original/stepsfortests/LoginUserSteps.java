package original.stepsfortests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import original.responsebodies.RightResponseBodyAfterLoginUser;

public class LoginUserSteps {

    @Step("Форматирование тела ответа в форматированный JSON")
    public String getFormattedResponseBody(Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RightResponseBodyAfterLoginUser rightResponseBodyAfterLoginUser =
                response.body().as(RightResponseBodyAfterLoginUser.class);
        return gson.toJson(rightResponseBodyAfterLoginUser);
    }

    @Step("Формируем ожидаемый JSON с токенами")
    public String generateExpectedJson(Response response) {
        String accessToken = response.then().extract().body().path("accessToken").toString();
        String refreshToken = response.then().extract().body().path("refreshToken").toString();
        String email = response.then().extract().body().path("user.email").toString();
        String name = response.then().extract().body().path("user.name").toString();
        return "{\n" +
                "  \"success\": true,\n" +
                "  \"accessToken\": \"" + accessToken + "\",\n" +
                "  \"refreshToken\": \"" + refreshToken + "\",\n" +
                "  \"user\": {\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"name\": \"" + name + "\"\n" +
                "  }\n" +
                "}";
    }
}
