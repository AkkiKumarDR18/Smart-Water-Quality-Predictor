import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WaterQualityGUI extends JFrame {

    // Define UI components
    private JTextField phField;
    private JTextField hardnessField;
    private JTextField solidsField;
    private JTextField chloraminesField;
    private JTextField sulfateField;
    private JTextField conductivityField;
    private JTextField organicCarbonField;
    private JTextField trihalomethanesField;
    private JTextField turbidityField;
    private JLabel resultLabel;
    private JButton predictButton;
    private JButton resetButton;
    private Image backgroundImage;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/water_quality_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345678Ak#";

    public WaterQualityGUI() {
        // Loading the background image
        backgroundImage = new ImageIcon("background1.jpg").getImage();

        // Initializing components
        phField = new JTextField(10);
        hardnessField = new JTextField(10);
        solidsField = new JTextField(10);
        chloraminesField = new JTextField(10);
        sulfateField = new JTextField(10);
        conductivityField = new JTextField(10);
        organicCarbonField = new JTextField(10);
        trihalomethanesField = new JTextField(10);
        turbidityField = new JTextField(10);
        resultLabel = new JLabel("Potability:");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));  // Increase font size
        predictButton = new JButton("Predict");
        resetButton = new JButton("Reset");

        // Create a panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setOpaque(true); // Set panel to be opaque

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Adding all labels and text fields to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("pH [6.5-8.5]:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(phField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Hardness [50-300 mg/L]:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(hardnessField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Solids [0-500 mg/L]:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(solidsField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Chloramines [0-4 mg/L]:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(chloraminesField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Sulfate [0-250 mg/L]:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(sulfateField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Conductivity [0-2000 µS/cm]:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(conductivityField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("Organic Carbon [0-5 mg/L]:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(organicCarbonField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(new JLabel("Trihalomethanes [0-0.1 mg/L]:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(trihalomethanesField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 8;
        inputPanel.add(new JLabel("Turbidity [0-5 NTU]:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(turbidityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(predictButton, gbc);
        gbc.gridy = 10;
        inputPanel.add(resetButton, gbc);
        gbc.gridy = 11;
        inputPanel.add(resultLabel, gbc);

        // Create a panel to hold the input panel
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
        containerPanel.setOpaque(true); // Set container panel to be opaque
        containerPanel.setBackground(Color.WHITE); // Solid background color for the container panel
        containerPanel.add(inputPanel, BorderLayout.CENTER);

        // Adding the panels to the frame
        setTitle("Water Quality Predictor");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the background panel first
        setContentPane(new BackgroundPanel());
        add(containerPanel, BorderLayout.CENTER); // Add the container panel on top of the background panel

        // Adding button action listeners
predictButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Get input values
            String ph = phField.getText();
            String hardness = hardnessField.getText();
            String solids = solidsField.getText();
            String chloramines = chloraminesField.getText();
            String sulfate = sulfateField.getText();
            String conductivity = conductivityField.getText();
            String organicCarbon = organicCarbonField.getText();
            String trihalomethanes = trihalomethanesField.getText();
            String turbidity = turbidityField.getText();

            // Insert data into database (without potability at this stage)
            insertDataIntoDatabase(ph, hardness, solids, chloramines, sulfate, conductivity, organicCarbon, trihalomethanes, turbidity);

            // Constructing the command to execute the Python script
            String command = String.format("python3 predict.py %s %s %s %s %s %s %s %s %s",
                    ph, hardness, solids, chloramines, sulfate, conductivity, organicCarbon, trihalomethanes, turbidity);

            // Using ProcessBuilder instead of Runtime.exec
            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();

            // Check if extreme values
            boolean isExtreme = checkExtremeValues(Float.parseFloat(ph), Float.parseFloat(hardness), Float.parseFloat(solids),
                    Float.parseFloat(chloramines), Float.parseFloat(sulfate), Float.parseFloat(conductivity),
                    Float.parseFloat(organicCarbon), Float.parseFloat(trihalomethanes), Float.parseFloat(turbidity));

            // Determine potability based on the result from Python script
            String potability = "1".equals(result) ? "Potable" : "No";

            // Update database with the potability status
            updatePotabilityInDatabase(potability);

            // Display the result
            resultLabel.setText("Potability: " + (potability.equals("Potable") ? "water can be used for drinking" : "water cannot be used for drinking"));

            // Show a message if the prediction is potable but extreme
            if ("1".equals(result) && isExtreme) {
                showMessage("The dataset used in machine learning is not trained for extreme values. " +
                                "Please note the following values are outside WHO and Other Studies recommended range:\n" +
                                getExtremeValuesExplanation(ph, hardness, solids, chloramines, sulfate, conductivity, organicCarbon, trihalomethanes, turbidity),
                        "Prediction Explanation");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            resultLabel.setText("Error: " + ex.getMessage());
        }
    }
});

        // Reset button action listener
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear all text fields
                phField.setText("");
                hardnessField.setText("");
                solidsField.setText("");
                chloraminesField.setText("");
                sulfateField.setText("");
                conductivityField.setText("");
                organicCarbonField.setText("");
                trihalomethanesField.setText("");
                turbidityField.setText("");
                resultLabel.setText("Potability:");
            }
        });
    }

    // Method to insert data into the database
    // Method to insert data into the database, including the potability result
private void insertDataIntoDatabase(String ph, String hardness, String solids, String chloramines, String sulfate,
                                    String conductivity, String organicCarbon, String trihalomethanes, String turbidity) throws SQLException {
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String query = "INSERT INTO water_quality_data (ph, hardness, solids, chloramines, sulfate, conductivity, organic_carbon, trihalomethanes, turbidity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ph);
            statement.setString(2, hardness);
            statement.setString(3, solids);
            statement.setString(4, chloramines);
            statement.setString(5, sulfate);
            statement.setString(6, conductivity);
            statement.setString(7, organicCarbon);
            statement.setString(8, trihalomethanes);
            statement.setString(9, turbidity);
            statement.executeUpdate();
        }
    }
}

private void updatePotabilityInDatabase(String potability) throws SQLException {
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String query = "UPDATE water_quality_data SET result = ? ORDER BY id DESC LIMIT 1"; // Assumes you have an id or unique identifier for each entry.
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, potability);
            statement.executeUpdate();
        }
    }
}

    // Method to check if the values are extreme
    private boolean checkExtremeValues(float ph, float hardness, float solids, float chloramines, float sulfate,
                                       float conductivity, float organicCarbon, float trihalomethanes, float turbidity) {
        return (ph < 6.5 || ph > 8.5 || hardness < 50 || hardness > 300 || solids < 0 || solids > 500 ||
                chloramines < 0 || chloramines > 4 || sulfate < 0 || sulfate > 250 || conductivity < 0 ||
                conductivity > 2000 || organicCarbon < 0 || organicCarbon > 5 || trihalomethanes < 0 ||
                trihalomethanes > 0.1 || turbidity < 0 || turbidity > 5);
    }

    // Method to get the explanation for extreme values
    private String getExtremeValuesExplanation(String ph, String hardness, String solids, String chloramines,
                                                String sulfate, String conductivity, String organicCarbon,
                                                String trihalomethanes, String turbidity) {
        StringBuilder explanation = new StringBuilder("The following values are outside WHO and Other Studies recommended ranges:\n");
        if (Float.parseFloat(ph) < 6.5 || Float.parseFloat(ph) > 8.5) explanation.append("pH: ").append(ph).append("\n");
        if (Float.parseFloat(hardness) < 50 || Float.parseFloat(hardness) > 300) explanation.append("Hardness: ").append(hardness).append("\n");
        if (Float.parseFloat(solids) < 0 || Float.parseFloat(solids) > 500) explanation.append("Solids: ").append(solids).append("\n");
        if (Float.parseFloat(chloramines) < 0 || Float.parseFloat(chloramines) > 4) explanation.append("Chloramines: ").append(chloramines).append("\n");
        if (Float.parseFloat(sulfate) < 0 || Float.parseFloat(sulfate) > 250) explanation.append("Sulfate: ").append(sulfate).append("\n");
        if (Float.parseFloat(conductivity) < 0 || Float.parseFloat(conductivity) > 2000) explanation.append("Conductivity: ").append(conductivity).append("\n");
        if (Float.parseFloat(organicCarbon) < 0 || Float.parseFloat(organicCarbon) > 5) explanation.append("Organic Carbon: ").append(organicCarbon).append("\n");
        if (Float.parseFloat(trihalomethanes) < 0 || Float.parseFloat(trihalomethanes) > 0.1) explanation.append("Trihalomethanes: ").append(trihalomethanes).append("\n");
        if (Float.parseFloat(turbidity) < 0 || Float.parseFloat(turbidity) > 5) explanation.append("Turbidity: ").append(turbidity).append("\n");
        return explanation.toString();
    }

    // Method to show messages in a dialog
    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Main method to run the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WaterQualityGUI().setVisible(true));
    }

    // Inner class for background panel
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
