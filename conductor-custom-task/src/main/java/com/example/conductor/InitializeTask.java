package com.example.conductor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;

public class InitializeTask implements Worker {

    private final String taskDefName;

    public InitializeTask(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }

    @Override
    public TaskResult execute(Task task) {
        TaskResult result = new TaskResult(task);
        List<String> items = (List<String>) task.getInputData().get("items");
        System.out.println("Intializing items: " + items);
        try {
          Map<String, Object> output = task.getOutputData();
        output.put("items", items);
        output.put("currentIndex", 0); 
        result.setStatus(TaskResult.Status.COMPLETED);
        } catch (Exception e) {
            result.setStatus(TaskResult.Status.FAILED);
            result.log(e.getMessage());
            System.err.println(e.getMessage());
        }
        return result;
    }
}
