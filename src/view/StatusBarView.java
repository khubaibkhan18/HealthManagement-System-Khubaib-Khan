package view;

import model.User;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusBarView extends JPanel {
    private JLabel userLabel;
    private JLabel timeLabel;
    private JLabel statusLabel;
    
    public StatusBarView(User currentUser) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());
        
        // Left: User info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        userLabel = new JLabel();
        updateUserInfo(currentUser);
        leftPanel.add(userLabel);
        
        // Center: Status message
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        statusLabel = new JLabel("System Ready");
        statusLabel.setForeground(Color.BLUE);
        centerPanel.add(statusLabel);
        
        // Right: Date/time
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 2));
        timeLabel = new JLabel();
        updateTime();
        rightPanel.add(timeLabel);
        
        // Add panels
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        
        // Timer to update time every minute
        Timer timer = new Timer(60000, e -> updateTime());
        timer.start();
    }
    
    private void updateUserInfo(User user) {
        String roleColor = getRoleColor(user.getRole());
        userLabel.setText("<html><b>User:</b> <font color='" + roleColor + "'>" + 
                         user.getName() + " (" + user.getRole().toUpperCase() + ")</font></html>");
    }
    
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        timeLabel.setText(sdf.format(new Date()));
    }
    
    private String getRoleColor(String role) {
        switch (role.toLowerCase()) {
            case "admin": return "#FF0000"; // Red
            case "gp": return "#0000FF"; // Blue
            case "specialist": return "#008000"; // Green
            case "nurse": return "#800080"; // Purple
            case "staff": return "#FF8C00"; // Orange
            case "patient": return "#808080"; // Gray
            default: return "#000000"; // Black
        }
    }
    
    public void setStatus(String message, String type) {
        switch (type.toLowerCase()) {
            case "success":
                statusLabel.setForeground(new Color(34, 139, 34));
                statusLabel.setText("✓ " + message);
                break;
            case "error":
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("✗ " + message);
                break;
            case "warning":
                statusLabel.setForeground(Color.ORANGE);
                statusLabel.setText("⚠ " + message);
                break;
            default:
                statusLabel.setForeground(Color.BLUE);
                statusLabel.setText(message);
        }
    }
    
    public void clearStatus() {
        statusLabel.setForeground(Color.BLUE);
        statusLabel.setText("System Ready");
    }
}