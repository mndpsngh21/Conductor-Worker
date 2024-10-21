package com.example.conductor;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import com.netflix.conductor.common.metadata.tasks.TaskResult.Status;

public class MyCustomWorker implements Worker {

    private final String taskDefName;

    public MyCustomWorker(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }

    @Override
    public TaskResult execute(Task task) {
        TaskResult result = new TaskResult(task);
        try {
            // Retrieve input data
            String input = (String) task.getInputData().get("inputKey");
            System.out.println("Received input: " + input);

            // Process the input (custom logic)
            String output = processInput(input);

            // Set the output data
            result.addOutputData("outputKey", output);
            result.setStatus(Status.COMPLETED);
            System.out.println("Task completed successfully with output: " + output);
        } catch (Exception e) {
            result.setStatus(Status.FAILED);
            result.log("Task execution failed: " + e.getMessage());
            System.err.println("Task execution failed: " + e.getMessage());
        }
        return result;
    }

    private String processInput(String input) {
        
        // Your custom business logic goes here
        // For demonstration, we'll simply reverse the input string
        return new StringBuilder(input).reverse().toString();
    }
}
