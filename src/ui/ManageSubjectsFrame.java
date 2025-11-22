package ui;

import dao.SubjectDAO;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("serial")
public class ManageSubjectsFrame extends JFrame {
    private JTable subjectsTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, deleteBtn, refreshBtn;
    
    public ManageSubjectsFrame() {
        setTitle("Manage Subjects");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        applyStyles();
        loadSubjectsData();
        
        setVisible(true);
    }
    
    private void initComponents() {
        String[] columns = {"ID", "Subject Name"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only for now
            }
        };
        
        subjectsTable = new JTable(tableModel);
        subjectsTable.setRowHeight(30);
        
        addBtn = new JButton("Add Subject");
        deleteBtn = new JButton("Delete Selected");
        refreshBtn = new JButton("Refresh");
        
        setupEventListeners();
    }
    
    @SuppressWarnings("unused")
	private void setupEventListeners() {
        addBtn.addActionListener(e -> showAddSubjectDialog());
        deleteBtn.addActionListener(e -> deleteSelectedSubject());
        refreshBtn.addActionListener(e -> loadSubjectsData());
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
        JScrollPane tableScrollPane = new JScrollPane(subjectsTable);
        
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void applyStyles() {
        StyleUtils.styleButton(addBtn);
        StyleUtils.styleButton(deleteBtn);
        StyleUtils.styleButton(refreshBtn);
        
        subjectsTable.setFont(StyleUtils.NORMAL_FONT);
        subjectsTable.getTableHeader().setFont(StyleUtils.BUTTON_FONT);
        subjectsTable.getTableHeader().setBackground(StyleUtils.PRIMARY_COLOR);
        subjectsTable.getTableHeader().setForeground(new Color(65, 105, 225));
    }
    
    private void loadSubjectsData() {
        try {
            List<models.Subject> subjects = SubjectDAO.getAllSubjects();
            tableModel.setRowCount(0);
            
            for (models.Subject subject : subjects) {
                tableModel.addRow(new Object[]{
                    subject.getSuid(),
                    subject.getName()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading subjects: " + e.getMessage());
        }
    }
    
    private void showAddSubjectDialog() {
        JTextField nameField = new JTextField();
        
        Object[] message = {
            "Subject Name:", nameField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, 
            "Add New Subject", JOptionPane.OK_CANCEL_OPTION);
            
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter subject name");
                return;
            }
            
            try {
                if (SubjectDAO.addSubject(name)) {
                    JOptionPane.showMessageDialog(this, "Subject added successfully");
                    loadSubjectsData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding subject: " + e.getMessage());
            }
        }
    }
    
    private void deleteSelectedSubject() {
        int selectedRow = subjectsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject to delete");
            return;
        }
        
        int suid = (Integer) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete subject: " + name + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (SubjectDAO.deleteSubject(suid)) {
                    JOptionPane.showMessageDialog(this, "Subject deleted successfully");
                    loadSubjectsData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting subject: " + e.getMessage());
            }
        }
    }
}