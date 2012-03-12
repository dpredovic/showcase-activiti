package showcase.activiti.process;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import showcase.activiti.engine.Engine;
import showcase.activiti.engine.test.SpringActivitiTest;
import showcase.activiti.persistence.Order;
import showcase.activiti.persistence.OrderItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration(classes = ProcessConfig.class, loader = AnnotationConfigContextLoader.class)
public class OrderProcessTest extends SpringActivitiTest {

    @Autowired
    private Engine engine;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment(resources = "bpmn/order.bpmn20.xml")
    @Test
    @Transactional
    public void goodRun() {
        Order order = new Order();
        order.setUserId(1234L);
        {
            OrderItem item = new OrderItem();
            item.setArticleId(234L);
            item.setAmount(1);
            item.setOrder(order);
            order.getItems().add(item);
        }
        {
            OrderItem item = new OrderItem();
            item.setArticleId(345L);
            item.setAmount(2);
            item.setOrder(order);
            order.getItems().add(item);
        }

        entityManager.persist(order);

        String processInstanceId = engine.startProcess(order);

        assertProcessEnded(processInstanceId);
        assertHistoricEndActivity(processInstanceId, "end");
    }

    @Deployment(resources = "bpmn/order.bpmn20.xml")
    @Test
    @Transactional
    public void invalidOrder() {
        Order order = new Order();
        order.setUserId(1234L);

        entityManager.persist(order);

        String processInstanceId = engine.startProcess(order);

        assertProcessEnded(processInstanceId);
        assertHistoricEndActivity(processInstanceId, "error");
    }

}
