package showcase.activiti.engine;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import showcase.activiti.persistence.Order;

@Component
public class EngineImpl implements Engine {

    @Autowired
    private ProcessEngine processEngine;

    @Override
    public String startProcess(Order order) {
        Map<String, Object> vars = new HashMap<String, Object>(1);
        vars.put("order", order);

        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("order", vars);
        String processInstanceId = processInstance.getProcessInstanceId();

        order.setProcessInstanceId(processInstanceId);

        return processInstanceId;
    }

}
