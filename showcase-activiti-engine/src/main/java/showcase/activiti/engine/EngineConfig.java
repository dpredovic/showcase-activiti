package showcase.activiti.engine;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;
import showcase.activiti.engine.spel.SpelExpressionManager;
import showcase.activiti.persistence.config.PersistenceUnitConfig;

@Configuration
@ComponentScan(basePackageClasses = EngineConfig.class)
@Import(PersistenceUnitConfig.class)
public class EngineConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "close")
    public ProcessEngine processEngine() {
        return processEngineConfiguration().buildProcessEngine();
    }

    @Bean
    public ProcessEngineConfigurationImpl processEngineConfiguration() {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setExpressionManager(expressionManager());

        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setTransactionManager(transactionManager);

        processEngineConfiguration.setJpaEntityManagerFactory(entityManagerFactory);
        processEngineConfiguration.setJpaHandleTransaction(true);
        processEngineConfiguration.setJpaCloseEntityManager(true);

        if (env.acceptsProfiles("junit", "standalone")) {
            processEngineConfiguration.setDatabaseSchemaUpdate("true");
        }
        return processEngineConfiguration;
    }

    @Bean
    public ExpressionManager expressionManager() {
        SpelExpressionManager spelExpressionManager = new SpelExpressionManager();
        spelExpressionManager.setExpressionClass(OrderExpression.class);
        return spelExpressionManager;
    }

}
