import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import original.Constants;
import original.requestbodies.RequestBodyForCreatingUser;
import original.requestbodies.RequestBodyForLoginUser;
import original.stepsfortests.CreatingUserSteps;

import static org.apache.http.HttpStatus.*;

public class CreatingUserTest {

    private Boolean isNeedToDeleteUser;

    CreatingUserSteps creatingUserSteps = new CreatingUserSteps();

    public Boolean getIsNeedToDeleteUser() {
        return isNeedToDeleteUser;
    }

    public void setIsNeedToDeleteUser(Boolean needToDeleteUser) {
        isNeedToDeleteUser = needToDeleteUser;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASIC_URL;
    }

    @Test
    public void rightCreatingUser() {
        setIsNeedToDeleteUser(true);
        RequestBodyForCreatingUser requestBodyForCreatingUser = new RequestBodyForCreatingUser("Mukhammed@yandex.ru", "password", "Mukhammed");
        Response responseAfterCreatingUser = creatingUserSteps.createUser(requestBodyForCreatingUser);
        creatingUserSteps.verifyStatus(responseAfterCreatingUser, SC_OK);

        String actualJson = creatingUserSteps.getFormattedResponseBody(responseAfterCreatingUser);
        String expectedJson = creatingUserSteps.generateExpectedJson(responseAfterCreatingUser);
        creatingUserSteps.verifyResponseBody(expectedJson, actualJson);
    }

    @Test
    public void tryToCreateTwoIdenticalUsers() {
        setIsNeedToDeleteUser(true);
        RequestBodyForCreatingUser requestBodyForCreatingUser = new RequestBodyForCreatingUser("Mukhammed@yandex.ru", "password", "Mukhammed");
        creatingUserSteps.createUser(requestBodyForCreatingUser);

        Response secondResponseAfterCreatingUser = creatingUserSteps.createUser(requestBodyForCreatingUser);
        creatingUserSteps.verifyStatus(secondResponseAfterCreatingUser, SC_FORBIDDEN);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(secondResponseAfterCreatingUser);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_IDENTICAL_USERS, actualJson);
    }

    @Test
    public void tryToCreateUserWithoutEmail() {
        setIsNeedToDeleteUser(false);
        RequestBodyForCreatingUser requestBodyForCreatingUser = new RequestBodyForCreatingUser(null, "password", "Mukhammed");
        Response responseAfterCreatingUserWithBadRequest = creatingUserSteps.createUser(requestBodyForCreatingUser);

        creatingUserSteps.verifyStatus(responseAfterCreatingUserWithBadRequest, SC_FORBIDDEN);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterCreatingUserWithBadRequest);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_USER_WITH_BAD_REQUEST, actualJson);
    }

    @Test
    public void tryToCreateUserWithoutPassword() {
        setIsNeedToDeleteUser(false);
        RequestBodyForCreatingUser requestBodyForCreatingUser = new RequestBodyForCreatingUser("Mukhammed@yandex.ru", null, "Mukhammed");
        Response responseAfterCreatingUserWithBadRequest = creatingUserSteps.createUser(requestBodyForCreatingUser);

        creatingUserSteps.verifyStatus(responseAfterCreatingUserWithBadRequest, SC_FORBIDDEN);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterCreatingUserWithBadRequest);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_USER_WITH_BAD_REQUEST, actualJson);
    }

    @Test
    public void tryToCreateUserWithoutName() {
        setIsNeedToDeleteUser(false);
        RequestBodyForCreatingUser requestBodyForCreatingUser = new RequestBodyForCreatingUser("Mukhammed@yandex.ru", "password", null);
        Response responseAfterCreatingUserWithBadRequest = creatingUserSteps.createUser(requestBodyForCreatingUser);

        creatingUserSteps.verifyStatus(responseAfterCreatingUserWithBadRequest, SC_FORBIDDEN);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterCreatingUserWithBadRequest);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_USER_WITH_BAD_REQUEST, actualJson);
    }

    @After
    public void setDown() {
        if(getIsNeedToDeleteUser().equals(true)) {
            RequestBodyForLoginUser requestBodyForLoginUser = new RequestBodyForLoginUser("Mukhammed@yandex.ru", "password");

            String accessToken = creatingUserSteps.loginUser(requestBodyForLoginUser);
            creatingUserSteps.deleteUser(accessToken);
        }
        else {
            System.out.println("Error");
        }
    }
}
