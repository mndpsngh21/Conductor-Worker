package com.example.conductor;



import java.util.Arrays;

import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;

public class ConductorWorkerApp {

    public static void main(String[] args) {
        // Initialize TaskClient with Conductor API endpoint
  TaskClient taskClient = new TaskClient();
        taskClient.setRootURI("http://localhost:8080/api/"); // Point this to the server API

        int threadCount =
                8; // number of threads used to execute workers.  To avoid starvation, should be
        // same or more than number of workers

        Worker worker1 = new MyCustomWorker("Sample-1");
        Worker worker2 = new MyCustomWorker("Sample-2");
        Worker worker3 = new MyCustomWorker("my_custom_task");
        Worker processItemWorker = new ProcessItemWorker("process_item");
        Worker retryProcessItemWorker = new RetryProcessItemWorker("retry_process_item");
        Worker initialize = new InitializeTask("initialize");
        Worker checkProcessingStatus = new CheckDecision("check_processing_status");
        Worker addLead = new AddLeadWorker("add_lead");
        // Create TaskRunnerConfigurer
        TaskRunnerConfigurer configurer =
                new TaskRunnerConfigurer.Builder(taskClient, Arrays.asList(checkProcessingStatus,initialize,worker1, worker2, worker3,processItemWorker, retryProcessItemWorker,addLead))
                        .withThreadCount(threadCount)
                        .build();

        // Start the polling and execution of tasks
        configurer.init();
    }
}
