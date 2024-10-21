package com.example.conductor;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;

public class ProcessItemWorker implements Worker {

    private final String taskDefName;

    public ProcessItemWorker(String taskDefName) {
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
        try {
            // Simulate item processing
            System.out.println("Processing item: " + item);
            // Implement actual processing logic here

            // Simulate success or failure
            boolean isSuccess = Math.random() > 0.3; // 70% chance of success

            if (isSuccess) {
                result.getOutputData().put("status", "SUCCESS");
                result.getOutputData().put("data", "Processed: " + item);
                result.setStatus(TaskResult.Status.COMPLETED);
                System.out.println("Item processed successfully: " + item);
            } else {
                throw new Exception("Processing failed for item: " + item);
            }
        } catch (Exception e) {
            result.setStatus(TaskResult.Status.FAILED);
            result.log(e.getMessage());
            System.err.println(e.getMessage());
        }
        return result;
    }
}
