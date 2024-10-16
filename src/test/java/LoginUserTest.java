import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import original.Constants;
import original.requestbodies.RequestBodyForCreatingUser;
import original.requestbodies.RequestBodyForLoginUser;
import original.stepsfortests.CreatingUserSteps;
import original.stepsfortests.LoginUserSteps;

import static org.apache.http.HttpStatus.*;

public class LoginUserTest {

    private String accessToken;

    CreatingUserSteps creatingUserSteps = new CreatingUserSteps();
    LoginUserSteps loginUserSteps = new LoginUserSteps();

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASIC_URL;
        RequestBodyForCreatingUser requestBodyForCreatingUser = new RequestBodyForCreatingUser("Mukhammed@yandex.ru", "password", "Mukhammed");
        creatingUserSteps.createUser(requestBodyForCreatingUser);
    }

    @Test
    public void rightLoginUser() {
        RequestBodyForLoginUser requestBodyForLoginUser = new RequestBodyForLoginUser("Mukhammed@yandex.ru", "password");

        Response responseAfterLoginUser = creatingUserSteps.loginUser(requestBodyForLoginUser);
        creatingUserSteps.verifyStatus(responseAfterLoginUser, SC_OK);

        String actualJson = loginUserSteps.getFormattedResponseBody(responseAfterLoginUser);
        String expectedJson = loginUserSteps.generateExpectedJson(responseAfterLoginUser);
        creatingUserSteps.verifyResponseBody(expectedJson, actualJson);

        setAccessToken(creatingUserSteps.extractingToken(responseAfterLoginUser));
    }

    @Test
    public void tryToLoginWithWrongEmail() {
        RequestBodyForLoginUser requestBodyForLoginUser = new RequestBodyForLoginUser("WrongEmail@yandex.ru", "password");

        Response responseAfterLoginUser = creatingUserSteps.loginUser(requestBodyForLoginUser);
        creatingUserSteps.verifyStatus(responseAfterLoginUser, SC_UNAUTHORIZED);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterLoginUser);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_LOGIN_USER_WITH_BAD_REQUEST, actualJson);
    }

    @Test
    public void tryToLoginWithWrongPassword() {
        RequestBodyForLoginUser requestBodyForLoginUser = new RequestBodyForLoginUser("Mukhammed@yandex.ru", "wrongpass");

        Response responseAfterLoginUser = creatingUserSteps.loginUser(requestBodyForLoginUser);
        creatingUserSteps.verifyStatus(responseAfterLoginUser, SC_UNAUTHORIZED);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterLoginUser);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_LOGIN_USER_WITH_BAD_REQUEST, actualJson);
    }

    @After
    public void setDown() {
        if(getAccessToken() != null) {
            creatingUserSteps.deleteUser(getAccessToken());
        } else if (getAccessToken() == null) {
            RequestBodyForLoginUser requestBodyForLoginUser = new RequestBodyForLoginUser("Mukhammed@yandex.ru", "password");
            Response responseAfterLoginUser = creatingUserSteps.loginUser(requestBodyForLoginUser);

            creatingUserSteps.deleteUser(creatingUserSteps.extractingToken(responseAfterLoginUser));
        }
    }
}
