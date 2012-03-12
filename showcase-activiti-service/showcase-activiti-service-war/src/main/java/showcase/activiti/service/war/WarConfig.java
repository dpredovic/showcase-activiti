package showcase.activiti.service.war;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import showcase.activiti.service.core.ServiceCoreConfig;

@Configuration
@Import(ServiceCoreConfig.class)
@ImportResource("/WEB-INF/cxfContext.xml")
@PropertySource("classpath:showcase-activiti.properties")
public class WarConfig {

}
