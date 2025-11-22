package ui;

import models.Teacher;
import utils.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class TeacherDashboard extends JFrame {
    private Teacher teacher;
    private JButton markAttendanceBtn, manageStudentsBtn, manageSubjectsBtn, viewRecordsBtn;
    
    public TeacherDashboard(Teacher teacher) {
        this.teacher = teacher;
        
        setTitle("Attendance System - Teacher Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        applyStyles();
        
        setVisible(true);
    }
    
    private void initComponents() {
        markAttendanceBtn = new JButton("Mark Attendance");
        manageStudentsBtn = new JButton("Manage Students");
        manageSubjectsBtn = new JButton("Manage Subjects");
        viewRecordsBtn = new JButton("View Records");
        
        // Add action listeners
        markAttendanceBtn.addActionListener(new ButtonClickListener());
        manageStudentsBtn.addActionListener(new ButtonClickListener());
        manageSubjectsBtn.addActionListener(new ButtonClickListener());
        viewRecordsBtn.addActionListener(new ButtonClickListener());
    }
    
    private void layoutComponents() {
        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        StyleUtils.stylePanel(mainPanel);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + teacher.getName());
        welcomeLabel.setFont(StyleUtils.HEADER_FONT);
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel pidLabel = new JLabel("PID: " + teacher.getPid());
        pidLabel.setFont(StyleUtils.NORMAL_FONT);
        pidLabel.setForeground(Color.WHITE);
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(pidLabel, BorderLayout.EAST);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        buttonPanel.add(markAttendanceBtn);
        buttonPanel.add(manageStudentsBtn);
        buttonPanel.add(manageSubjectsBtn);
        buttonPanel.add(viewRecordsBtn);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void applyStyles() {
        // Style buttons directly
        StyleUtils.styleButton(markAttendanceBtn);
        StyleUtils.styleButton(manageStudentsBtn);
        StyleUtils.styleButton(manageSubjectsBtn);
        StyleUtils.styleButton(viewRecordsBtn);
    }
    
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String command = source.getText();
            
            switch (command) {
            case "Mark Attendance":
                new MarkAttendanceFrame(teacher);
                break;
            case "Manage Students":
                new ManageStudentsFrame();
                break;
            case "Manage Subjects":
                new ManageSubjectsFrame();
                break;
            case "View Records":
                new ViewRecordsFrame(); // Updated this line
                break;
        }
        }
    }
}