package showcase.activiti.engine.spel;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.el.ExpressionManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ClassUtils;

public class SpelExpressionManager extends ExpressionManager {

    ExpressionParser parser = new SpelExpressionParser();
    ParserContext parserContext = new TemplateParserContext();
    BeanResolver beanResolver;
    Map<String, org.springframework.expression.Expression> expressionCache = new ConcurrentHashMap<String, org.springframework.expression.Expression>();

    @Autowired
    private ApplicationContext applicationContext;

    private Class<? extends SpelExpression> expressionClass = SpelExpression.class;
    private Constructor<? extends SpelExpression> expressionConstructor;

    @Override
    public Expression createExpression(String expression) {
        return BeanUtils.instantiateClass(expressionConstructor, expression, this);
    }

    @PostConstruct
    public void init() throws Exception {
        beanResolver = new BeanFactoryResolver(applicationContext);
        expressionConstructor = ClassUtils.getConstructorIfAvailable(expressionClass, String.class, SpelExpressionManager.class);
    }

    public void setExpressionClass(Class<? extends SpelExpression> expressionClass) {
        this.expressionClass = expressionClass;
    }
}
