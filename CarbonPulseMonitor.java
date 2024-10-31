import java.util.*;


class CarbonPulseMonitor implements CarbonPulseTracker {
    private List<EcoAction> actions;

    @Override
    public void beginEcoMonitoring() {
        System.out.println("Eco Monitoring started.");
    }

    @Override
    public void concludeEcoMonitoring() {
        System.out.println("Eco Monitoring concluded.");
        // Summarize the data
    }

    @Override
    public void recordDeliberateEcoAction(EcoAction action) {
        actions.add(action);
        System.out.println("Recorded action: " + action.actionType);
    }
}
