package showcase.activiti.engine;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.VariableScope;
import showcase.activiti.engine.spel.SpelExpression;
import showcase.activiti.engine.spel.SpelExpressionManager;
import showcase.activiti.persistence.Order;
import showcase.activiti.persistence.OrderItem;

public class OrderExpression extends SpelExpression {

    public OrderExpression(String expressionText, SpelExpressionManager expressionManager) {
        super(expressionText, expressionManager);
    }

    @Override
    protected Map<String, Object> additionalVariables(VariableScope variableScope) {
        Map<String, Object> vars = new HashMap<String, Object>();

        Order order = (Order) variableScope.getVariable("order");
        if (order != null) {
            vars.put("ORDER_ID", order.getId());
            vars.put("USER_ID", order.getUserId());
        }
        OrderItem orderItem = (OrderItem) variableScope.getVariable("orderItem");
        if (orderItem != null) {
            vars.put("ARTICLE_ID", orderItem.getArticleId());
            vars.put("AMOUNT", orderItem.getAmount());
        }

        return vars;
    }
}
