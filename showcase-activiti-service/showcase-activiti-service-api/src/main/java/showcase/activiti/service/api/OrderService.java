package showcase.activiti.service.api;

import showcase.activiti.service.api.dto.OrderDto;

public interface OrderService {

    long placeOrder(OrderDto order);

}
