package com.moodtracker.client;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.framework.ServiceReference;
import com.moodtracker.services.MoodTrackerService;

@Component
public class MoodTrackerClient {

    private MoodTrackerService moodTrackerService;

    @Reference
    public void gotService(ServiceReference<MoodTrackerService> reference) {
        // Bind the MoodTrackerService
        moodTrackerService = (MoodTrackerService) reference.getBundle().getBundleContext().getService(reference);
        System.out.println("Bind Service: " + moodTrackerService);
    }

    public void lostService(ServiceReference<MoodTrackerService> reference) {
        // Unbind the MoodTrackerService
        moodTrackerService = null;
        System.out.println("Unbind Service");
    }

    public void execute() {
        if (moodTrackerService != null) {
            // Example usage of the MoodTrackerService
            System.out.println("Using MoodTrackerService...");
        } else {
            System.out.println("MoodTrackerService is not available.");
        }
    }
}