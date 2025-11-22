package ui;

import models.Student;
import models.Teacher;
import dao.StudentDAO;
import dao.TeacherDAO;
import utils.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
    private JTextField pidField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JButton loginButton;
    
    public LoginFrame() {
        setTitle("Attendance Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 470);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
        layoutComponents();
        applyStyles();
        
        setVisible(true);
    }
    
    private void initComponents() {
        pidField = new JTextField(20);
        passwordField = new JPasswordField(20);
        userTypeCombo = new JComboBox<>(new String[]{"Teacher", "Student"});
        loginButton = new JButton("Login");
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }
    
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        StyleUtils.stylePanel(mainPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Attendance System", SwingConstants.CENTER);
        titleLabel.setFont(StyleUtils.TITLE_FONT);
        titleLabel.setForeground(StyleUtils.PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Please login to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(StyleUtils.NORMAL_FONT);
        subtitleLabel.setForeground(StyleUtils.TEXT_COLOR);
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);
        
        // User type
        gbc.gridwidth = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        mainPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(userTypeCombo, gbc);
        
        // PID
        gbc.gridy = 3; gbc.gridx = 0;
        mainPanel.add(new JLabel("PID:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(pidField, gbc);
        
        // Password
        gbc.gridy = 4; gbc.gridx = 0;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridy = 5; gbc.gridx = 1; gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(loginButton, gbc);
        
        add(mainPanel);
    }
    
    private void applyStyles() {
        StyleUtils.styleTextField(pidField);
        StyleUtils.styleTextField(passwordField);
        StyleUtils.styleComboBox(userTypeCombo);
        StyleUtils.styleButton(loginButton);
        
    }
    
    private void login() {
        String pid = pidField.getText().trim();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();
        
        if (pid.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both PID and password", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if ("Teacher".equals(userType)) {
                Teacher teacher = TeacherDAO.getTeacherByPid(pid);
                if (teacher != null && teacher.getPassword().equals(password)) {
                    new TeacherDashboard(teacher);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid PID or password", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Student student = StudentDAO.getStudentByPid(pid);
                if (student != null && student.getPassword().equals(password)) {
                    new StudentDashboard(student);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid PID or password", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        
        try {
           
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame();
            }
        });
    }
}