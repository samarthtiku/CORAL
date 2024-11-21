import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

// Defines an EcoProfile class to hold user's eco-friendly actions and their profile details
class EcoProfile {
    String name;
    String email;
    String ecoPassphrase;
    List<EcoAction> actions;

    // Constructor initializes the profile with basic information and an empty list of actions
    public EcoProfile(String name, String email, String ecoPassphrase) {
        this.name = name;
        this.email = email;
        this.ecoPassphrase = ecoPassphrase;
        this.actions = new ArrayList<>();
    }

    // Adds an eco-friendly action to the user's profile
    public void addAction(EcoAction action) {
        actions.add(action);
    }
}

// Represents an environmental action with its type and carbon impact
class EcoAction {
    String actionType;
    double carbonImpact;

    // Constructor to set the action type and its carbon impact
    public EcoAction(String actionType, double carbonImpact) {
        this.actionType = actionType;
        this.carbonImpact = carbonImpact;
    }
}

// Provides insights based on the user's eco actions
class EcoInsight {
    String guidance;
    boolean wasImpactful;

    // Initializes guidance with a default 'not impactful' status
    public EcoInsight(String guidance) {
        this.guidance = guidance;
        this.wasImpactful = false;
    }
}

// Main class to manage the GUI and user interactions
public class Coral {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel containerPanel; // Holds panels for different UI screens
    private JToolBar toolBar; // Toolbar for navigation
    private Map<String, EcoProfile> profiles = new HashMap<>();

    // Constructor to setup the main UI components
    public Coral() {
        frame = new JFrame("Coral Mobile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640); // Size simulating a mobile screen ratio
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        createToolBar();
        createPanels();

        // Setting the layout and making the frame visible
        frame.setLayout(new BorderLayout());
        frame.add(toolBar, BorderLayout.NORTH);
        frame.add(containerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Creates a toolbar with navigation buttons and a title
    private void createToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton menuButton = new JButton(new ImageIcon("menu_icon.png"));
        menuButton.setPreferredSize(new Dimension(50, 50));

        JPopupMenu menu = new JPopupMenu();
        JMenuItem switchToCoral2 = new JMenuItem("Go to Coral 2");
        switchToCoral2.addActionListener(e -> cardLayout.show(containerPanel, "Coral 2"));
        JMenuItem switchToCoral1 = new JMenuItem("Go to Coral 1");
        switchToCoral1.addActionListener(e -> cardLayout.show(containerPanel, "Coral 1"));

        menu.add(switchToCoral1);
        menu.add(switchToCoral2);

        menuButton.addActionListener(e -> menu.show(menuButton, 0, menuButton.getHeight()));
        toolBar.add(menuButton);

        JLabel titleLabel = new JLabel("Coral");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        toolBar.addSeparator(new Dimension(15, 50));
        toolBar.add(titleLabel);
    }

    // Creates the main and secondary panels for the application
    private void createPanels() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel mainLabel = new JLabel("Welcome to Coral 1", SwingConstants.CENTER);
        mainPanel.add(mainLabel, BorderLayout.CENTER);

        JPanel secondPanel = new JPanel(new BorderLayout());
        JLabel secondPanelLabel = new JLabel("Welcome to CORAL 2", SwingConstants.CENTER);
        secondPanel.add(secondPanelLabel, BorderLayout.CENTER);

        containerPanel.add(mainPanel, "Coral 1");
        containerPanel.add(secondPanel, "Coral 2");
    }

    // Initiates a new eco journey by creating an EcoProfile
    public EcoProfile initiateEcoJourney(String name, String email, String ecoPassphrase) {
        EcoProfile profile = new EcoProfile(name, email, ecoPassphrase);
        profiles.put(email, profile);
        return profile;
    }

    // Validates the user's credentials and returns the associated EcoProfile
    public EcoProfile validateEcoExplorer(String email, String ecoPassphrase) {
        return profiles.getOrDefault(email, null);
    }

    // Assesses the carbon impact of a specified action
    public double assessCarbonImpact(String actionType) {
        switch (actionType.toLowerCase()) {
            case "driving":
                return 2.3;
            case "flying":
                return 150;
            default:
                return 0.5;
        }
    }

    // Generates tailored ecological guidance based on the user's actions
    public List<EcoInsight> generateTailoredEcoGuidance(EcoProfile explorer) {
        List<EcoInsight> insights = new ArrayList<>();
        double totalImpact = explorer.actions.stream().mapToDouble(a -> a.carbonImpact).sum();
        if (totalImpact > 1000) {
            insights.add(new EcoInsight("Consider reducing long-distance travel to cut down your carbon footprint."));
        } else {
            insights.add(new EcoInsight("Great job! Keep focusing on low-impact activities."));
        }
        return insights;
    }

    // The application entry point that ensures the GUI is constructed on the EDT
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Coral::new);
    }
}