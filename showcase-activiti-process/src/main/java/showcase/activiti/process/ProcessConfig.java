package showcase.activiti.process;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import showcase.activiti.adapters.AdaptersConfig;
import showcase.activiti.engine.EngineConfig;

@Configuration
@Import({EngineConfig.class, AdaptersConfig.class})
@ComponentScan(basePackageClasses = ProcessConfig.class)
public class ProcessConfig {

}
