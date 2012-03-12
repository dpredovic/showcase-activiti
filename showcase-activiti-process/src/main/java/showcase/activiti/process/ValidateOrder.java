package showcase.activiti.process;

import org.springframework.stereotype.Component;
import showcase.activiti.persistence.Order;

@Component
public class ValidateOrder {

    public boolean isValid(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return false;
        }
        return true;
    }

}
