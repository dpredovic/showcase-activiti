package showcase.activiti.service.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import showcase.activiti.process.ProcessConfig;

@Configuration
@ComponentScan(basePackageClasses = ServiceCoreConfig.class)
@Import(ProcessConfig.class)
public class ServiceCoreConfig {

}
