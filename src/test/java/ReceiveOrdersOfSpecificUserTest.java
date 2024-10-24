import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import original.Constants;
import original.requestbodies.RequestBodyForCreatingOrUpdatingUserData;
import original.requestbodies.RequestBodyForCreatingOrder;
import original.stepsfortests.CreatingOrderSteps;
import original.stepsfortests.CreatingUserSteps;
import original.stepsfortests.ReceiveOrdersOfSpecificUserSteps;

import static org.apache.http.HttpStatus.*;

public class ReceiveOrdersOfSpecificUserTest {
    private String accessToken;

    CreatingUserSteps creatingUserSteps = new CreatingUserSteps();
    CreatingOrderSteps creatingOrderSteps = new CreatingOrderSteps();
    ReceiveOrdersOfSpecificUserSteps receiveOrdersOfSpecificUserSteps = new ReceiveOrdersOfSpecificUserSteps();

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
    public void receiveOrdersFromSpecificUserWithAuthorization() {
        // Сделал пару заказов
        RequestBodyForCreatingOrder requestBodyForCreatingOrder = new RequestBodyForCreatingOrder(Constants.BODY_WITH_INGREDIENTS);
        creatingOrderSteps.createOrderWithAuthorization(requestBodyForCreatingOrder, getAccessToken());
        creatingOrderSteps.createOrderWithAuthorization(requestBodyForCreatingOrder, getAccessToken());
        creatingOrderSteps.createOrderWithAuthorization(requestBodyForCreatingOrder, getAccessToken());

        Response responseAfterReceivingOrdersOfSpecificUser = receiveOrdersOfSpecificUserSteps.receiveOrdersOfAuthorizedUser(getAccessToken());
        creatingUserSteps.verifyStatus(responseAfterReceivingOrdersOfSpecificUser, SC_OK);

        receiveOrdersOfSpecificUserSteps.checkStructureOfResponseForReceivingOrdersOfSpecificuser(responseAfterReceivingOrdersOfSpecificUser);
    }

    @Test
    public void receiveOrdersFromSpecificUserWithoutAuthorization() {
        Response responseAfterReceivingOrdersOfSpecificUser = receiveOrdersOfSpecificUserSteps.receiveOrdersOfUnauthorizedUser();
        creatingUserSteps.verifyStatus(responseAfterReceivingOrdersOfSpecificUser, SC_UNAUTHORIZED);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterReceivingOrdersOfSpecificUser);
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
