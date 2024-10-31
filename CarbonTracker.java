interface CarbonPulseTracker {
    void beginEcoMonitoring();
    void concludeEcoMonitoring();
    void recordDeliberateEcoAction(EcoAction action);
}