package showcase.activiti.adapters;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component("warehouse")
public class DummyWarehouseImpl implements Warehouse {

    private AtomicLong counter = new AtomicLong(0);

    @Override
    public String reserveIfAvailable(long articleId, int amount, long orderId) {
        return String.valueOf(counter.incrementAndGet());
    }

    @Override
    public void prepareForDistribution(String reservationId) {
    }

    @Override
    public void cancelReservation(String reservationId) {
    }

    @Override
    public void cancelAll(long orderId) {
    }
}
