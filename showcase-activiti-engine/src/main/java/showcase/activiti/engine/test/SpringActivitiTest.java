package showcase.activiti.engine.test;

import javax.sql.DataSource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.impl.test.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertEquals;

public abstract class SpringActivitiTest {

    @Autowired
    private ProcessEngine processEngine;

    @Rule
    public TestName testName = new TestName();

    @Autowired
    private DataSource dataSource;

    private String deploymentId;

    private ProcessEngineConfigurationImpl processEngineConfiguration;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected FormService formService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;

    protected long defaultIntervalMillis = 25L;
    protected long defaultMaxWaitMillis = 5000L;

    @Before
    public void setUp() throws Exception {
        initializeServices();
        deploymentId = TestHelper.annotationDeploymentSetUp(processEngine, getClass(), testName.getMethodName());
    }

    @After
    public void tearDown() throws Exception {
        TestHelper.annotationDeploymentTearDown(processEngine, deploymentId, getClass(), testName.getMethodName());
    }

    protected void assertProcessEnded(String id) {
        TestHelper.assertProcessEnded(processEngine, id);
    }

    protected void waitForJobExecutorToProcessAllJobs(long maxMillisToWait, long intervalMillis) {
        TestHelper.waitForJobExecutorToProcessAllJobs(processEngineConfiguration, maxMillisToWait, intervalMillis);
    }

    protected void waitForJobExecutorToProcessAllJobs() {
        waitForJobExecutorToProcessAllJobs(defaultMaxWaitMillis, defaultIntervalMillis);
    }

    private void initializeServices() {
        processEngineConfiguration = ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
        formService = processEngine.getFormService();
        historyService = processEngine.getHistoryService();
        identityService = processEngine.getIdentityService();
        managementService = processEngine.getManagementService();
    }

    protected void assertHistoricEndActivity(String processInstanceId, String endActivityId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().
                processInstanceId(processInstanceId).singleResult();
        assertEquals(endActivityId, historicProcessInstance.getEndActivityId());
    }

    protected boolean areJobsAvailable() {
        return TestHelper.areJobsAvailable(processEngineConfiguration);
    }

    protected void startJobExecutor() {
        JobExecutor jobExecutor = processEngineConfiguration.getJobExecutor();
        jobExecutor.start();
    }

    protected void stopJobExecutor() {
        JobExecutor jobExecutor = processEngineConfiguration.getJobExecutor();
        jobExecutor.shutdown();
    }

    protected void waitForInstanceJobs(String id, long intervalMillis) {
        do {
            try {
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                //
            }
        } while (areJobsAvailable(id));
    }

    protected void waitForInstanceJobs(String id) {
        waitForInstanceJobs(id, defaultIntervalMillis);
    }

    protected boolean areJobsAvailable(String processInstanceId) {

        return processEngineConfiguration.getManagementService()
                       .createJobQuery()
                       .processInstanceId(processInstanceId)
                       .count() != 0;
    }

    protected void truncateJobTables() {
        String[] statements = new String[]{
                "delete from ACT_RU_VARIABLE",
                "delete from ACT_RU_IDENTITYLINK",
                "delete from ACT_RU_TASK",
                "update ACT_RU_EXECUTION set PARENT_ID_=null",
                "update ACT_RU_EXECUTION set SUPER_EXEC_=null",
                "delete from ACT_RU_EXECUTION where SUPER_EXEC_ IS NOT NULL",
                "delete from ACT_RU_EXECUTION",
                //  "delete from ACT_RU_EXECUTION order by PARENT_ID_ desc",
                "update ACT_RU_JOB set EXECUTION_ID_=null",
                "delete from ACT_RU_JOB"
        };

        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.batchUpdate(statements);
    }

}
