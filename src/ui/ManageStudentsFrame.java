package ui;

import models.Student;
import dao.StudentDAO;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("serial")
public class ManageStudentsFrame extends JFrame {
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, deleteBtn, refreshBtn;
    
    public ManageStudentsFrame() {
        setTitle("Manage Students");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        applyStyles();
        loadStudentsData();
        
        setVisible(true);
    }
    
    private void initComponents() {
        String[] columns = {"PID", "Name", "Password"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Allow editing
            }
        };
        
        studentsTable = new JTable(tableModel);
        studentsTable.setRowHeight(30);
        
        addBtn = new JButton("Add Student");
        deleteBtn = new JButton("Delete Selected");
        refreshBtn = new JButton("Refresh");
        
        setupEventListeners();
    }
    
    @SuppressWarnings("unused")
	private void setupEventListeners() {
        addBtn.addActionListener(e -> showAddStudentDialog());
        deleteBtn.addActionListener(e -> deleteSelectedStudent());
        refreshBtn.addActionListener(e -> loadStudentsData());
    }
    
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        StyleUtils.stylePanel(mainPanel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(studentsTable);
        
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void applyStyles() {
        StyleUtils.styleButton(addBtn);
        StyleUtils.styleButton(deleteBtn);
        StyleUtils.styleButton(refreshBtn);
        
        studentsTable.setFont(StyleUtils.NORMAL_FONT);
        studentsTable.getTableHeader().setFont(StyleUtils.BUTTON_FONT);
        studentsTable.getTableHeader().setBackground(StyleUtils.PRIMARY_COLOR);
        studentsTable.getTableHeader().setForeground(new Color(65, 105, 225));
    }
    
    private void loadStudentsData() {
        try {
            List<Student> students = StudentDAO.getAllStudents();
            tableModel.setRowCount(0);
            
            for (Student student : students) {
                tableModel.addRow(new Object[]{
                    student.getPid(),
                    student.getName(),
                    student.getPassword()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
        }
    }
    
    private void showAddStudentDialog() {
        JTextField pidField = new JTextField();
        JTextField nameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        Object[] message = {
            "Student PID:", pidField,
            "Name:", nameField,
            "Password:", passwordField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, 
            "Add New Student", JOptionPane.OK_CANCEL_OPTION);
            
        if (option == JOptionPane.OK_OPTION) {
            String pid = pidField.getText().trim();
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (pid.isEmpty() || name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            
            try {
                Student student = new Student(pid, name, password);
                if (StudentDAO.addStudent(student)) {
                    JOptionPane.showMessageDialog(this, "Student added successfully");
                    loadStudentsData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding student: " + e.getMessage());
            }
        }
    }
    
    private void deleteSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete");
            return;
        }
        
        String pid = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete student: " + name + " (" + pid + ")?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (StudentDAO.deleteStudent(pid)) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully");
                    loadStudentsData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting student: " + e.getMessage());
            }
        }
    }
}