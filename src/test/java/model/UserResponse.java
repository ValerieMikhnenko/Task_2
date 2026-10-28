
package model;

public class UserResponse {
    private boolean success;
    private String message;
    private UserData user;
    private String accessToken;
    private String refreshToken;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserData getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static class UserData {
        private String email;
        private String name;

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }
    }
}
