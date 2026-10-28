package model;

public class OrderResponse {
    private boolean success;
    private OrderData order;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public OrderData getOrder() {
        return order;
    }

    public String getMessage() {
        return message;
    }

    public static class OrderData {
        private String number;

        public String getNumber() {
            return number;
        }
    }
}
