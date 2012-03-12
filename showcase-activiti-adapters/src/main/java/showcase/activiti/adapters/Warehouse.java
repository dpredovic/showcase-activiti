package showcase.activiti.adapters;

public interface Warehouse {

    String reserveIfAvailable(long articleId, int amount, long orderId);

    void prepareForDistribution(String reservationId);

    void cancelReservation(String reservationId);

    void cancelAll(long orderId);

}
