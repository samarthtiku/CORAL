import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

class EcoProfile {
    String name;
    String email;
    String ecoPassphrase;
    List<EcoAction> actions;

    public EcoProfile(String name, String email, String ecoPassphrase) {
        this.name = name;
        this.email = email;
        this.ecoPassphrase = ecoPassphrase;
        this.actions = new ArrayList<>();
    }

    public void addAction(EcoAction action) {
        actions.add(action);
    }

    public double calculateTotalCarbonImpact() {
        return actions.stream().mapToDouble(action -> action.carbonImpact).sum();
    }
}

class EcoAction {
    String actionType;
    double carbonImpact;

    public EcoAction(String actionType, double carbonImpact) {
        this.actionType = actionType;
        this.carbonImpact = carbonImpact;
    }
}

class EcoInsight {
    String guidance;
    boolean wasImpactful;

    public EcoInsight(String guidance) {
        this.guidance = guidance;
        this.wasImpactful = false;
    }
}

public class Coral {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel containerPanel;
    private Map<String, EcoProfile> profiles = new HashMap<>();

    public Coral() {
        frame = new JFrame("CORAL: Carbon Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 800);
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        createToolBar();
        createPanels();

        frame.setLayout(new BorderLayout());
        frame.add(containerPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Initialize a sample profile for testing
        EcoProfile profile = new EcoProfile("John Doe", "john@example.com", "eco123");
        profiles.put("john@example.com", profile);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JLabel titleLabel = new JLabel("CORAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        toolBar.add(titleLabel);

        frame.add(toolBar, BorderLayout.NORTH);
    }

    private void createPanels() {
        // Dashboard Panel
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel dashboardLabel = new JLabel("Dashboard", SwingConstants.CENTER);
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton calculateImpactButton = new JButton("Calculate Carbon Impact");
        JButton addActionButton = new JButton("Add Action");
        JButton insightsButton = new JButton("Show Insights");

        // Add button actions
        calculateImpactButton.addActionListener(e -> calculateCarbonImpact());
        addActionButton.addActionListener(e -> addAction());
        insightsButton.addActionListener(e -> showInsights());

        buttonPanel.add(calculateImpactButton);
        buttonPanel.add(addActionButton);
        buttonPanel.add(insightsButton);

        dashboardPanel.add(dashboardLabel, BorderLayout.NORTH);
        dashboardPanel.add(buttonPanel, BorderLayout.CENTER);

        containerPanel.add(dashboardPanel, "Dashboard");
    }

    private void calculateCarbonImpact() {
        String email = JOptionPane.showInputDialog("Enter your email:");
        EcoProfile profile = profiles.get(email);

        if (profile != null) {
            double totalImpact = profile.calculateTotalCarbonImpact();
            JOptionPane.showMessageDialog(frame, "Total Carbon Impact: " + totalImpact + " units.");
        } else {
            JOptionPane.showMessageDialog(frame, "Profile not found.");
        }
    }

    private void addAction() {
        String email = JOptionPane.showInputDialog("Enter your email:");
        EcoProfile profile = profiles.get(email);

        if (profile != null) {
            String actionType = JOptionPane.showInputDialog("Enter action type (e.g., Driving, Recycling):");
            String impactInput = JOptionPane.showInputDialog("Enter carbon impact (e.g., 2.3):");
            try {
                double carbonImpact = Double.parseDouble(impactInput);
                profile.addAction(new EcoAction(actionType, carbonImpact));
                JOptionPane.showMessageDialog(frame, "Action added successfully.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid carbon impact value.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Profile not found.");
        }
    }

    private void showInsights() {
        String email = JOptionPane.showInputDialog("Enter your email:");
        EcoProfile profile = profiles.get(email);

        if (profile != null) {
            double totalImpact = profile.calculateTotalCarbonImpact();
            String insight = totalImpact > 1000
                    ? "Consider reducing long-distance travel."
                    : "Great job! Keep focusing on low-impact activities.";
            JOptionPane.showMessageDialog(frame, "Insight: " + insight);
        } else {
            JOptionPane.showMessageDialog(frame, "Profile not found.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Coral::new);
    }
}
