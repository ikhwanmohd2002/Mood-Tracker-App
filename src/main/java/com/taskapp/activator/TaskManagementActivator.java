package com.taskapp.activator;

import com.taskapp.implementations.TaskManagementServiceImpl;
import com.taskapp.services.TaskManagementService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class TaskManagementActivator implements BundleActivator {
    @Override
    public void start(BundleContext context) {
        TaskManagementService taskService = new TaskManagementServiceImpl();
        taskService.addTask("Complete OSGi project");
        taskService.viewTasks();
    }

    @Override
    public void stop(BundleContext context) {
        System.out.println("Stopping Task Management App...");
    }
}
