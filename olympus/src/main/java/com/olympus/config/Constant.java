package com.olympus.config;

public class Constant {

    /**
     * Serve side jwt secret key
     */
    public static final String JWT_SECRET = "olympus";
    public static final String MailSenderAddress = "noreply.socialnetwork.89@gmail.com";
    public static final String MailSenderPassword = "ewsj augt exxh vkgx ";
    public static final String HTTP_STATUS_CODE_200 = "200";
    public static final String HTTP_STATUS_CODE_201 = "201";

    /**
     * Expiration time : 3 days
     */
    public static final long JWT_EXPIRATION = 1000 * 60 * 60 * 24 * 3;
    public static final String[] WHITE_LIST = {"/test", "/login", "/", "/index", "/home/", "/api-docs/**","/ws/**",
            "/**/swagger-ui/**", "/v3/api-docs", "/swagger-ui/**", "/swagger-ui.html",
            "/v1/account/forgot-password", "/v1/account/reset-password", "/v1/account/login",
            "/v1/auth", "/v1/account/register", "/v1/account/validate-reset-password"};

    public static final String MSG_SUCCESS_ACCOUNT_REGISTER = "Register new user successfully";
    public static final String MSG_SUCCESS_ACCOUNT_OTP_SENT = "OTP sent successfully";
    public static final String MSG_SUCCESS_ACCOUNT_PWD_RESET_TOKEN_SENT = "Token sent successfully";
    public static final String MSG_SUCCESS_ACCOUNT_PWD_RESET_TOKEN_VALIDATE = "Token is valid";
    public static final String MSG_SUCCESS_ACCOUNT_PWD_RESET = "Reset password successfully";
    public static final String MSG_SUCCESS_POST_COMMENT_CREATE = "Create new comment successfully";
    public static final String MSG_SUCCESS_POST_COMMENT_UPDATE = "Update comment successfully";
    public static final String MSG_SUCCESS_POST_COMMENT_DELETE = "Delete comment successfully";
    public static final String MSG_SUCCESS_POST_GET_BY_USER = "Get posts successfully";
    public static final String MSG_SUCCESS_POST_GET_NEWSFEED = "Get newsfeed successfully";
    public static final String MSG_SUCCESS_POST_CREATE = "Create new post successfully";
    public static final String MSG_SUCCESS_POST_UPDATE = "Update post successfully";
    public static final String MSG_SUCCESS_POST_DELETE = "Delete post successfully";
    public static final String MSG_SUCCESS_PROFILE_GET = "Get profile successfully";
    public static final String MSG_SUCCESS_FRIEND_REQUEST_RECEIVER = "Get list request received successfully";
    public static final String MSG_SUCCESS_FRIEND_REQUEST_SENT = "Get list request sent successfully";
    public static final String MSG_SUCCESS_FRIEND_REQUEST_CREATE = "Sent Friend request successfully";
    public static final String MSG_SUCCESS_FRIEND_REQUEST_DELETE = "Cancel friend request successfully";
    public static final String MSG_SUCCESS_FRIEND_REQUEST_CONFIRM = "Accept friend request successfully";
    public static final String MSG_SUCCESS_FRIENDSHIP_DELETE = "You two are no longer friends";
    public static final String MSG_SUCCESS_FRIENDSHIP_GET_LIST = "Get list friends successfully";
    public static final String MSG_SUCCESS_LIKE_UNLIKE = "Like or Unlike successfully";
    public static final String MSG_SUCCESS_USER_UPDATE = "Update user successfully";
    public static final String MSG_SUCCESS_AUTH = "User is authenticated successfully";
    public static final String MSG_SUCCESS_REPORT_CREATE = "Create user's report successfully";
    public static final String ERR_FRIEND_REQUEST_REQUEST_EXIST = "Friend request is already sent";
    public static final String ERR_FRIEND_REQUEST_FRIENDSHIP_EXIST = "You two are already friends";
    public static final String ERR_FRIEND_REQUEST_FRIENDSHIP_NOT_EXIST = "You two are not friends";
    public static final String ERR_FRIEND_REQUEST_REQUEST_DUPLICATE_SENDER_RECEIVER = "Sender and Receiver are the same user";
    public static final String ERR_FRIENDSHIP_DUPLICATE_SENDER_RECEIVER = "Sender and Receiver are the same user";
    public static final String ERR_FRIEND_REQUEST_REQUEST_NOT_VALID_CANCELER = "Not valid sender either receiver";
    public static final String ERR_FRIEND_REQUEST_REQUEST_NOT_VALID_ACCEPTER = "Not valid receiver";
    public static final String ERR_CONFLICT_PATH_VARIABLE_REQUEST_BODY = "Path variable and body ids conflict";
    public static final String ERR_CONFLICT_PATH_POST_ID_USER_ID = "User ID in path variable does not match ID in post's user";
    public static final String ERR_IMAGE_INVALID = "Not valid images";

    public static final String MSG_OK = "ok";
    public static final String MSG_SUCCESS = "success";
    public static final String MSG_ERROR = "error";
}
