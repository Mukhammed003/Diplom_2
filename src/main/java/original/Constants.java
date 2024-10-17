package original;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public final static String BASIC_URL = "https://stellarburgers.nomoreparties.site";
    public final static String ENDPOINT_FOR_CREATING_USER = "/api/auth/register";
    public final static String ENDPOINT_FOR_LOGIN_USER = "/api/auth/login";
    public final static String ENDPOINT_FOR_DELETING_OR_UPDATING_USER_DATA = "/api/auth/user";
    public final static String ENDPOINT_FOR_CREATING_ORDER = "/api/orders";

    public final static String EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_IDENTICAL_USERS = "{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"User already exists\"\n" +
            "}";

    public final static String EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_USER_WITH_BAD_REQUEST = "{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"Email, password and name are required fields\"\n" +
            "}";

    public final static String EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_LOGIN_USER_WITH_BAD_REQUEST = "{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"email or password are incorrect\"\n" +
            "}";

    public final static String EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_UPDATING_USER_DATA_WITHOUT_AUTHORIZATION = "{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"You should be authorised\"\n" +
            "}";

    public final static String EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_ORDER_WITHOUT_INGREDIENTS = "{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"Ingredient ids must be provided\"\n" +
            "}";

    public final static List<String> BODY_WITH_INGREDIENTS = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa72");
    public final static List<String> BODY_WITHOUT_INGREDIENTS = Arrays.asList();
    public final static List<String> BODY_WITH_WRONG_HASH_INGREDIENTS = Arrays.asList("61c0c5a71d1f82001bdaaa6d7");
}
