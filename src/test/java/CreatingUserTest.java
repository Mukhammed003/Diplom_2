import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import original.Constants;
import original.requestbodies.RequestBodyForCreatingOrUpdatingUserData;
import original.stepsfortests.CreatingUserSteps;

import static org.apache.http.HttpStatus.*;

public class CreatingUserTest {

    private String accessToken;

    CreatingUserSteps creatingUserSteps = new CreatingUserSteps();

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASIC_URL;
    }

    @Test
    public void rightCreatingUser() {
        RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData = new RequestBodyForCreatingOrUpdatingUserData("Mukhammed@yandex.ru", "password", "Mukhammed");
        Response responseAfterCreatingUser = creatingUserSteps.createUser(requestBodyForCreatingOrUpdatingUserData);
        creatingUserSteps.verifyStatus(responseAfterCreatingUser, SC_OK);

        setAccessToken(creatingUserSteps.extractingToken(responseAfterCreatingUser));

        String actualJson = creatingUserSteps.getFormattedResponseBody(responseAfterCreatingUser);
        String expectedJson = creatingUserSteps.generateExpectedJson(responseAfterCreatingUser);
        creatingUserSteps.verifyResponseBody(expectedJson, actualJson);
    }

    @Test
    public void tryToCreateTwoIdenticalUsers() {
        RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData = new RequestBodyForCreatingOrUpdatingUserData("Mukhammed@yandex.ru", "password", "Mukhammed");
        Response firstResponseAfterCreatingUser = creatingUserSteps.createUser(requestBodyForCreatingOrUpdatingUserData);

        setAccessToken(creatingUserSteps.extractingToken(firstResponseAfterCreatingUser));

        Response secondResponseAfterCreatingUser = creatingUserSteps.createUser(requestBodyForCreatingOrUpdatingUserData);
        creatingUserSteps.verifyStatus(secondResponseAfterCreatingUser, SC_FORBIDDEN);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(secondResponseAfterCreatingUser);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_IDENTICAL_USERS, actualJson);
    }

    @Test
    public void tryToCreateUserWithoutEmail() {
        RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData = new RequestBodyForCreatingOrUpdatingUserData(null, "password", "Mukhammed");
        Response responseAfterCreatingUserWithBadRequest = creatingUserSteps.createUser(requestBodyForCreatingOrUpdatingUserData);

        creatingUserSteps.verifyStatus(responseAfterCreatingUserWithBadRequest, SC_FORBIDDEN);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterCreatingUserWithBadRequest);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_USER_WITH_BAD_REQUEST, actualJson);
    }

    @Test
    public void tryToCreateUserWithoutPassword() {
        RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData = new RequestBodyForCreatingOrUpdatingUserData("Mukhammed@yandex.ru", null, "Mukhammed");
        Response responseAfterCreatingUserWithBadRequest = creatingUserSteps.createUser(requestBodyForCreatingOrUpdatingUserData);

        creatingUserSteps.verifyStatus(responseAfterCreatingUserWithBadRequest, SC_FORBIDDEN);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterCreatingUserWithBadRequest);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_USER_WITH_BAD_REQUEST, actualJson);
    }

    @Test
    public void tryToCreateUserWithoutName() {
        RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData = new RequestBodyForCreatingOrUpdatingUserData("Mukhammed@yandex.ru", "password", null);
        Response responseAfterCreatingUserWithBadRequest = creatingUserSteps.createUser(requestBodyForCreatingOrUpdatingUserData);

        creatingUserSteps.verifyStatus(responseAfterCreatingUserWithBadRequest, SC_FORBIDDEN);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterCreatingUserWithBadRequest);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_USER_WITH_BAD_REQUEST, actualJson);
    }

    @After
    public void setDown() {
        if(getAccessToken() != null) {
            creatingUserSteps.deleteUser(accessToken);
        }
        else {
            System.out.println("Error");
        }
    }
}
