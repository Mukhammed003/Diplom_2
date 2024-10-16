import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import original.Constants;
import original.requestbodies.RequestBodyForCreatingOrUpdatingUserData;
import original.stepsfortests.CreatingUserSteps;
import original.stepsfortests.UpdatingUserDataSteps;

import static org.apache.http.HttpStatus.*;

public class UpdatingUserDataTest {

    private String accessToken;

    CreatingUserSteps creatingUserSteps = new CreatingUserSteps();
    UpdatingUserDataSteps updatingUserDataSteps = new UpdatingUserDataSteps();

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASIC_URL;
        RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData = new RequestBodyForCreatingOrUpdatingUserData("Mukhammed@yandex.ru", "password", "Mukhammed");
        Response responseAfterCreatingUser = creatingUserSteps.createUser(requestBodyForCreatingOrUpdatingUserData);
        setAccessToken(creatingUserSteps.extractingToken(responseAfterCreatingUser));
    }

    @Test
    public void tryToUpdateUserDataWithAuthorization() {
        RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData = new RequestBodyForCreatingOrUpdatingUserData("Mukhamed@yandex.ru", "pasword", "Mukhamed");
        Response responseAfterUpdatingUserData = updatingUserDataSteps.updateUserDataWithAuthorization(requestBodyForCreatingOrUpdatingUserData, getAccessToken());

        creatingUserSteps.verifyStatus(responseAfterUpdatingUserData, SC_OK);

        String actualJson = updatingUserDataSteps.getFormattedResponseBody(responseAfterUpdatingUserData);
        String expectedJson = updatingUserDataSteps.generateExpectedJson(responseAfterUpdatingUserData);
        creatingUserSteps.verifyResponseBody(expectedJson, actualJson);
    }

    @Test
    public void tryToUpdateUserDataWithoutAuthorization() {
        RequestBodyForCreatingOrUpdatingUserData requestBodyForCreatingOrUpdatingUserData = new RequestBodyForCreatingOrUpdatingUserData("Mukhamed@yandex.ru", "pasword", "Mukhamed");
        Response responseAfterUpdatingUserData = updatingUserDataSteps.updateUserDataWithoutAuthorization(requestBodyForCreatingOrUpdatingUserData);

        creatingUserSteps.verifyStatus(responseAfterUpdatingUserData, SC_UNAUTHORIZED);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterUpdatingUserData);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_UPDATING_USER_DATA_WITHOUT_AUTHORIZATION, actualJson);
    }

    @After
    public void setDown() {
        if(getAccessToken() != null) {
            creatingUserSteps.deleteUser(getAccessToken());
        } else {
            System.out.println("Error");
        }
    }
}
