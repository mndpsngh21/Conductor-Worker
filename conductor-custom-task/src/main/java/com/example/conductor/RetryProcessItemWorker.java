package com.example.conductor;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;

public class RetryProcessItemWorker implements Worker {

    private final String taskDefName;

    public RetryProcessItemWorker(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }

    @Override
    public TaskResult execute(Task task) {
        TaskResult result = new TaskResult(task);
        String item = (String) task.getInputData().get("item");
        int currentRetry = (int) task.getInputData().getOrDefault("retryCount", 0);
        int maxRetries = (int) task.getInputData().getOrDefault("maxRetries", 3);

        try {
            // Simulate item processing
            System.out.println("Retrying processing item: " + item + " (Attempt " + (currentRetry + 1) + ")");
            // Implement actual processing logic here

            // Simulate success or failure
            boolean isSuccess = Math.random() > 0.3; // 70% chance of success

            if (isSuccess) {
                result.getOutputData().put("status", "SUCCESS");
                result.getOutputData().put("data", "Processed after retry: " + item);
                result.setStatus(TaskResult.Status.COMPLETED);
                System.out.println("Item processed successfully on retry: " + item);
            } else {
                if (currentRetry + 1 >= maxRetries) {
                    throw new Exception("Max retries reached for item: " + item);
                } else {
                    throw new Exception("Retrying failed for item: " + item);
                }
            }
        } catch (Exception e) {
            result.setStatus(TaskResult.Status.FAILED);
            result.log(e.getMessage());
            System.err.println(e.getMessage());
        }
        return result;
    }
}
