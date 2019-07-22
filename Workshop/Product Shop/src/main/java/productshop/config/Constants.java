package productshop.config;

public final class Constants {

    public static final String BAD_REQUEST_ERROR_CODE = "400";

    public static final String BLANK_USERNAME_MESSAGE = "Please enter a name.";
    public static final String NULL_PRICE_MESSAGE = "Please enter a price.";
    public static final String MINIMUM_DECIMAL_PRICE_VALUE = "1";
    public static final String NEGATIVE_PRICE_MESSAGE = "Price must be positive.";
    public static final String EMPTY_CATEGORIES_MESSAGE = "Choose a category.";
    public static final String BLANK_PASSWORD_MESSAGE = "Please enter a password.";
    public static final String BLANK_CONFIRM_PASSWORD_MESSAGE = "Please re-enter your password.";
    public static final String INVALID_EMAIL_MESSAGE = "Please enter a valid email.";
    public static final String BLANK_EMAIL_MESSAGE = "Please enter an email.";

    public static final String PASSWORDS_DONT_MATCH_MESSAGE = "Passwords don't match.";
    public static final String WRONG_PASSWORD_MESSAGE = "Wrong password.";
    public static final String USERNAME_ALREADY_IN_USE_MESSAGE = "Username is already in use.";

    public static final String EMPTY_IMAGE_MESSAGE = "Please select an image.";
    public static final String INVALID_IMAGE_MESSAGE = "File is either too large or not in the supported formats (jpg, jpeg, png, svg)";

    public static final String CATEGORY_ALREADY_EXISTS_MESSAGE = "Category already exists.";

    public static final String IS_MODERATOR = "hasRole('MODERATOR')";
    public static final String IS_ADMIN = "hasRole('ADMIN')";

    public static final String CANNOT_CHANGE_TO_ROOT = "Cannot change role to ROOT.";
    public static final String USERNAME_NOT_FOUND = "User with such username doesn't exist.";


    private Constants() {
    }
}
