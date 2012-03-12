package showcase.activiti.service.core;

import java.util.Arrays;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import showcase.activiti.persistence.Order;
import showcase.activiti.service.api.OrderService;
import showcase.activiti.service.api.dto.OrderDto;
import showcase.activiti.service.api.dto.OrderItemDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration(classes = ServiceCoreConfig.class, loader = AnnotationConfigContextLoader.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProcessEngine processEngine;

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        processEngine.getRepositoryService().createDeployment().addClasspathResource("bpmn/order.bpmn20.xml").deploy();
    }

    @Test
    public void testService() throws Exception {
        OrderDto order = new OrderDto();
        order.setUserId(123L);

        OrderItemDto item1 = new OrderItemDto();
        item1.setArticleId(234L);
        item1.setAmount(1);

        OrderItemDto item2 = new OrderItemDto();
        item2.setArticleId(345L);
        item2.setAmount(2);

        order.setItems(Arrays.asList(item1, item2));

        long id = orderService.placeOrder(order);

        Order o = em.find(Order.class, id);
        String processInstanceId = o.getProcessInstanceId();

        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService().
                createHistoricProcessInstanceQuery().
                processInstanceId(processInstanceId).
                singleResult();

        String endActivityId = historicProcessInstance.getEndActivityId();
        Assert.assertEquals("end", endActivityId);
    }
}
