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

import static org.apache.http.HttpStatus.*;

public class CreatingOrderTest {

    private String accessToken;

    CreatingUserSteps creatingUserSteps = new CreatingUserSteps();
    CreatingOrderSteps creatingOrderSteps = new CreatingOrderSteps();

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
    public void tryToCreateOrderWithAuthorizationWithRightIngredients() {
        RequestBodyForCreatingOrder requestBodyForCreatingOrder = new RequestBodyForCreatingOrder(Constants.BODY_WITH_INGREDIENTS);
        Response responseAfterCreatingOrder = creatingOrderSteps.createOrderWithAuthorization(requestBodyForCreatingOrder, getAccessToken());

        creatingUserSteps.verifyStatus(responseAfterCreatingOrder, SC_OK);

        creatingOrderSteps.checkStructureOfResponseForCreatingOrder(responseAfterCreatingOrder);
    }

    @Test
    public void tryToCreateOrderWithAuthorizationWithoutIngredients() {
        RequestBodyForCreatingOrder requestBodyForCreatingOrder = new RequestBodyForCreatingOrder(Constants.BODY_WITHOUT_INGREDIENTS);
        Response responseAfterCreatingOrder = creatingOrderSteps.createOrderWithAuthorization(requestBodyForCreatingOrder, getAccessToken());

        creatingUserSteps.verifyStatus(responseAfterCreatingOrder, SC_BAD_REQUEST);

        String actualJson = creatingUserSteps.getFormattedErrorResponseBody(responseAfterCreatingOrder);
        creatingUserSteps.verifyResponseBody(Constants.EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_ORDER_WITHOUT_INGREDIENTS, actualJson);
    }

    @Test
    public void tryToCreateOrderWithAuthorizationWithWrongHashOfIngredient() {
        RequestBodyForCreatingOrder requestBodyForCreatingOrder = new RequestBodyForCreatingOrder(Constants.BODY_WITH_WRONG_HASH_INGREDIENTS);
        Response responseAfterCreatingOrder = creatingOrderSteps.createOrderWithAuthorization(requestBodyForCreatingOrder, getAccessToken());

        creatingUserSteps.verifyStatus(responseAfterCreatingOrder, SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void tryToCreateOrderWithoutAuthorizationWithRightIngredients() {
        RequestBodyForCreatingOrder requestBodyForCreatingOrder = new RequestBodyForCreatingOrder(Constants.BODY_WITH_INGREDIENTS);
        Response responseAfterCreatingOrder = creatingOrderSteps.createOrderWithoutAuthorization(requestBodyForCreatingOrder);

        /* В требованиях написано:
        "Когда пользователь нажимает «Оформить заказ», неавторизованный
        пользователь переадресовывается на маршрут /login ."
        Ожидаю переадресовку...*/
        creatingUserSteps.verifyStatus(responseAfterCreatingOrder, SC_TEMPORARY_REDIRECT);
    }

    @Test
    public void tryToCreateOrderWithoutAuthorizationWithoutIngredients() {
        RequestBodyForCreatingOrder requestBodyForCreatingOrder = new RequestBodyForCreatingOrder(Constants.BODY_WITHOUT_INGREDIENTS);
        Response responseAfterCreatingOrder = creatingOrderSteps.createOrderWithoutAuthorization(requestBodyForCreatingOrder);

        /* В требованиях написано:
        "Когда пользователь нажимает «Оформить заказ», неавторизованный
        пользователь переадресовывается на маршрут /login ."
        Ожидаю переадресовку...*/
        creatingUserSteps.verifyStatus(responseAfterCreatingOrder, SC_TEMPORARY_REDIRECT);
    }

    @Test
    public void tryToCreateOrderWithoutAuthorizationWithWrongHashOfIngredient() {
        RequestBodyForCreatingOrder requestBodyForCreatingOrder = new RequestBodyForCreatingOrder(Constants.BODY_WITH_WRONG_HASH_INGREDIENTS);
        Response responseAfterCreatingOrder = creatingOrderSteps.createOrderWithoutAuthorization(requestBodyForCreatingOrder);

        /* В требованиях написано:
        "Когда пользователь нажимает «Оформить заказ», неавторизованный
        пользователь переадресовывается на маршрут /login ."
        Ожидаю переадресовку...*/
        creatingUserSteps.verifyStatus(responseAfterCreatingOrder, SC_TEMPORARY_REDIRECT);
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
