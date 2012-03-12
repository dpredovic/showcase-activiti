package showcase.activiti.service.api.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    private Long userId;

    private List<OrderItemDto> items = new ArrayList<OrderItemDto>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}
