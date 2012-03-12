package showcase.activiti.engine;

import showcase.activiti.persistence.Order;

public interface Engine {

    String startProcess(Order order);

}
