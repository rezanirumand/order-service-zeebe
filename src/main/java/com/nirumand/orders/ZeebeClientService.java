package com.nirumand.orders;

import io.zeebe.client.ZeebeClient;
import lombok.Getter;
import lombok.NonNull;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@ApplicationScoped
public class ZeebeClientService {
    private static final Logger LOG = Logger.getLogger(ZeebeClientService.class);

    @Getter
    private ZeebeClient client;

    public ZeebeClientService() {
        this.client = ZeebeClient.newClientBuilder().gatewayAddress("127.0.0.1:26500").usePlaintext().build();
    }

    public String startInstance(String correlationKey, Object variables) {
        final var wfInstance = client.newCreateInstanceCommand().bpmnProcessId("order-process").version(3).variables(variables)
                .requestTimeout(Duration.of(1, SECONDS)).send().join();

        LOG.infof("Process created with following details: processid: {} , version: {}, instanceKey: {}, workflowKey: {}",
                wfInstance.getBpmnProcessId(), wfInstance.getVersion(), wfInstance.getWorkflowInstanceKey(), wfInstance.getWorkflowKey());
        return String.valueOf(wfInstance.getWorkflowInstanceKey());
    }

    public void publishMessage(@NonNull String correlatedId, @NonNull String messageName) {
        final var result = client.newPublishMessageCommand().messageName(messageName).correlationKey(correlatedId).send().join();

        LOG.infof("Message published for order {} with id {}", correlatedId, result.getMessageKey());
    }
}
