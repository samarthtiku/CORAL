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
    private JPanel containerPanel; // This will hold the card layout
    private JToolBar toolBar; // Shared toolbar for navigation
    private Map<String, EcoProfile> profiles = new HashMap<>();

    public Coral() {
        // Initialize the frame
        frame = new JFrame("Coral Mobile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640); // my guess of mobile screen size ratio
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        createToolBar();
        createPanels();

        // Layout the frame
        frame.setLayout(new BorderLayout());
        frame.add(toolBar, BorderLayout.NORTH);
        frame.add(containerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

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

    private void createPanels() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel mainLabel = new JLabel("Welcome to Coral 1", SwingConstants.CENTER);
        mainPanel.add(mainLabel, BorderLayout.CENTER);

        // Second panel, "CORAL 2"
        JPanel secondPanel = new JPanel(new BorderLayout());
        JLabel secondPanelLabel = new JLabel("Welcome to CORAL 2", SwingConstants.CENTER);
        secondPanel.add(secondPanelLabel, BorderLayout.CENTER);

        containerPanel.add(mainPanel, "Coral 1");
        containerPanel.add(secondPanel, "Coral 2");
    }

    public EcoProfile initiateEcoJourney(String name, String email, String ecoPassphrase) {
        EcoProfile profile = new EcoProfile(name, email, ecoPassphrase);
        profiles.put(email, profile);
        return profile;
    }


    public EcoProfile validateEcoExplorer(String email, String ecoPassphrase) {
        return profiles.getOrDefault(email, null);
    }

    public double assessCarbonImpact(String actionType) {
        // logic for carbon impact based on action type
        switch (actionType.toLowerCase()) {
            case "driving":
                return 2.3;
            case "flying":
                return 150;
            default:
                return 0.5;
        }
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Coral::new);
    }
}
