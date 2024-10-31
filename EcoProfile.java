class EcoProfile {
    String name;
    String email;
    String ecoPassphrase;
    double carbonFootprint;

    // Constructor
    EcoProfile(String name, String email, String ecoPassphrase) {
        this.name = name;
        this.email = email;
        this.ecoPassphrase = ecoPassphrase;
        this.carbonFootprint = 0.0;
    }
}