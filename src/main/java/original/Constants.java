package original;

public class Constants {
    public final static String BASIC_URL = "https://stellarburgers.nomoreparties.site";
    public final static String ENDPOINT_FOR_CREATING_USER = "/api/auth/register";
    public final static String ENDPOINT_FOR_LOGIN_USER = "/api/auth/login";
    public final static String ENDPOINT_FOR_DELETING_USER = "/api/auth/user";

    public final static String EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_IDENTICAL_USERS = "{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"User already exists\"\n" +
            "}";

    public final static String EXAMPLE_OF_RIGHT_RESPONSE_BODY_AFTER_CREATING_USER_WITH_BAD_REQUEST = "{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"Email, password and name are required fields\"\n" +
            "}";
}
