package com.nirumand.workers;

import com.nirumand.service.ZeebeClientService;
import io.quarkus.runtime.Startup;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;

@Startup
@ApplicationScoped
public class ShipmentWithInsuranceWorker implements JobHandler {
    private static final Logger LOG = Logger.getLogger(ShipmentWithInsuranceWorker.class);
    private static final String JOB_NAME = "ship-with-insurance";

    @Inject
    ZeebeClientService clientService;

    private JobWorker subscription;

    @PostConstruct
    public void subscribe() {
        subscription = clientService.getClient().newWorker().jobType(JOB_NAME).handler(this).timeout(Duration.ofMinutes(1)).open();
    }

    @PreDestroy
    public void closeSubscription() {
        subscription.close();
    }

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        LOG.infof("Handling ship-insurance for version workflow Version: %s", job.getWorkflowDefinitionVersion());
        client.newCompleteCommand(job.getKey()).send().join();
        LOG.info("ship-insurance shipped");
    }
}
