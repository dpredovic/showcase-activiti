package showcase.activiti.service.core;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import showcase.activiti.engine.Engine;
import showcase.activiti.persistence.Order;
import showcase.activiti.persistence.OrderItem;
import showcase.activiti.service.api.OrderService;
import showcase.activiti.service.api.dto.OrderDto;
import showcase.activiti.service.api.dto.OrderItemDto;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private Engine engine;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public long placeOrder(OrderDto order) {
        Order o = mapOrder(order);
        em.persist(o);
        engine.startProcess(o);
        return o.getId();
    }

    private Order mapOrder(OrderDto order) {
        Order o = new Order();
        o.setUserId(order.getUserId());
        for (OrderItemDto item : order.getItems()) {
            OrderItem i = new OrderItem();
            i.setArticleId(item.getArticleId());
            i.setAmount(item.getAmount());
            i.setOrder(o);
            o.getItems().add(i);
        }
        return o;
    }
}
