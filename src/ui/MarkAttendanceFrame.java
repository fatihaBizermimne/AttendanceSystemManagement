package ui;

import models.Teacher;
import models.Student;
import models.Subject;
import dao.StudentDAO;
import dao.SubjectDAO;
import dao.AttendanceDAO;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings({ "unused", "serial" })
public class MarkAttendanceFrame extends JFrame {
    private Teacher teacher;
    private JComboBox<Subject> subjectCombo;
    private JTextField searchField;
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private List<Student> allStudents;
    private JButton markAttendanceBtn, exportBtn;
    private JLabel subjectLabel;
    
    public MarkAttendanceFrame(Teacher teacher) {
        this.teacher = teacher;
        
        setTitle("Mark Attendance - " + teacher.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        applyStyles();
        loadData();
        
        setVisible(true);
    }
    
    private void initComponents() {
        // Subjects combo box
        subjectCombo = new JComboBox<>();
        
        // Search field
        searchField = new JTextField(20);
        
        // Students table
        String[] columns = {"PID", "Student Name", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3; // Only status and actions columns are editable
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) return Boolean.class;
                if (column == 3) return JButton.class;
                return String.class;
            }
        };
        
        studentsTable = new JTable(tableModel);
        studentsTable.setRowHeight(35);
        studentsTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        studentsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        // Set custom renderer and editor for the Actions column
        studentsTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        studentsTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // Buttons
        markAttendanceBtn = new JButton("Save Attendance");
        exportBtn = new JButton("Export to CSV");
        
        // Subject label
        subjectLabel = new JLabel("Select Subject: ");
        
        // Load all students
        try {
            allStudents = StudentDAO.getAllStudents();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
            allStudents = new ArrayList<>();
        }
        
        setupEventListeners();
    }
    
    // Custom renderer for the button column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("View History");
            StyleUtils.styleButton(this);
            return this;
        }
    }
    
    // Custom editor for the button column
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            StyleUtils.styleButton(button);
            
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    viewAttendanceHistory(currentRow);
                }
            });
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = "View History";
            button.setText(label);
            isPushed = true;
            currentRow = row;
            return button;
        }
        
        public Object getCellEditorValue() {
            if (isPushed) {
                // Return the button text or any value you want
            }
            isPushed = false;
            return label;
        }
        
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
        
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    
    private void viewAttendanceHistory(int row) {
        String studentPid = (String) tableModel.getValueAt(row, 0);
        String studentName = (String) tableModel.getValueAt(row, 1);
        Subject selectedSubject = (Subject) subjectCombo.getSelectedItem();
        
        if (selectedSubject == null) {
            JOptionPane.showMessageDialog(this, "Please select a subject first");
            return;
        }
        
        try {
            // Get attendance history for this student and subject
            List<Object[]> history = AttendanceDAO.getAttendanceHistory(studentPid, selectedSubject.getSuid());
            
            // Create and show history dialog
            showAttendanceHistoryDialog(studentName, studentPid, selectedSubject.getName(), history);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading attendance history: " + e.getMessage());
        }
    }
    
    private void showAttendanceHistoryDialog(String studentName, String studentPid, String subjectName, List<Object[]> history) {
        JDialog historyDialog = new JDialog(this, "Attendance History", true);
        historyDialog.setSize(500, 400);
        historyDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        StyleUtils.stylePanel(mainPanel);
        
        // Header with student info
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        JLabel nameLabel = new JLabel("Student: " + studentName);
        JLabel pidLabel = new JLabel("PID: " + studentPid);
        JLabel subjectLabel = new JLabel("Subject: " + subjectName);
        
        nameLabel.setFont(StyleUtils.BUTTON_FONT);
        pidLabel.setFont(StyleUtils.NORMAL_FONT);
        subjectLabel.setFont(StyleUtils.NORMAL_FONT);
        
        headerPanel.add(nameLabel);
        headerPanel.add(pidLabel);
        headerPanel.add(subjectLabel);
        
        // History table
        String[] columns = {"Date", "Status"};
        DefaultTableModel historyModel = new DefaultTableModel(columns, 0);
        JTable historyTable = new JTable(historyModel);
        
        // Populate history table
        for (Object[] record : history) {
            String status = (Integer) record[1] == 1 ? "Present" : "Absent";
            historyModel.addRow(new Object[]{record[0], status});
        }
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        // Close button
        JButton closeBtn = new JButton("Close");
        StyleUtils.styleButton(closeBtn);
        closeBtn.addActionListener(e -> historyDialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        buttonPanel.add(closeBtn);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        historyDialog.add(mainPanel);
        historyDialog.setVisible(true);
    }
    
    private void setupEventListeners() {
        // Search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterStudents(); }
            public void removeUpdate(DocumentEvent e) { filterStudents(); }
            public void changedUpdate(DocumentEvent e) { filterStudents(); }
        });
        
        // Mark attendance button
        markAttendanceBtn.addActionListener(e -> saveAttendance());
        
        // Export button
        exportBtn.addActionListener(e -> exportToCSV());
        
        // Subject selection - update title
        subjectCombo.addActionListener(e -> {
            Subject selectedSubject = (Subject) subjectCombo.getSelectedItem();
            if (selectedSubject != null) {
                setTitle("Mark Attendance - " + teacher.getName() + " - " + selectedSubject.getName());
            }
        });
    }
    
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        StyleUtils.stylePanel(mainPanel);
        
        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        headerPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        
        headerPanel.add(subjectLabel);
        headerPanel.add(subjectCombo);
        headerPanel.add(new JLabel("Search Student:"));
        headerPanel.add(searchField);
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(studentsTable);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        buttonPanel.add(exportBtn);
        buttonPanel.add(markAttendanceBtn);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void applyStyles() {
        StyleUtils.styleComboBox(subjectCombo);
        StyleUtils.styleTextField(searchField);
        StyleUtils.styleButton(markAttendanceBtn);
        StyleUtils.styleButton(exportBtn);
        
        // Style table
        studentsTable.setFont(StyleUtils.NORMAL_FONT);
        studentsTable.getTableHeader().setFont(StyleUtils.BUTTON_FONT);
        studentsTable.getTableHeader().setBackground(StyleUtils.PRIMARY_COLOR);
        studentsTable.getTableHeader().setForeground(new Color(65, 105, 225));
        
        subjectLabel.setFont(StyleUtils.BUTTON_FONT);
        subjectLabel.setForeground(StyleUtils.PRIMARY_COLOR);
    }
    
    private void loadData() {
        // Load subjects
        try {
            List<Subject> subjects = SubjectDAO.getAllSubjects();
            subjectCombo.removeAllItems();
            for (Subject subject : subjects) {
                subjectCombo.addItem(subject);
            }
            
            if (!subjects.isEmpty()) {
                subjectCombo.setSelectedIndex(0);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading subjects: " + e.getMessage());
        }
        
        // Load students into table
        populateStudentsTable(allStudents);
    }
    
    private void populateStudentsTable(List<Student> students) {
        tableModel.setRowCount(0);
        
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getPid(),
                student.getName(), // Use actual student name
                false, // Default to absent
                "View History"
            });
        }
    }
    
    private void filterStudents() {
        String searchText = searchField.getText().toLowerCase();
        List<Student> filteredStudents = new ArrayList<>();
        
        for (Student student : allStudents) {
            if (student.getName().toLowerCase().contains(searchText) ||
                student.getPid().toLowerCase().contains(searchText)) {
                filteredStudents.add(student);
            }
        }
        
        populateStudentsTable(filteredStudents);
    }
    
    private void saveAttendance() {
        Subject selectedSubject = (Subject) subjectCombo.getSelectedItem();
        if (selectedSubject == null) {
            JOptionPane.showMessageDialog(this, "Please select a subject");
            return;
        }
        
        int successCount = 0;
        int totalCount = tableModel.getRowCount();
        
        try {
            for (int i = 0; i < totalCount; i++) {
                String studentPid = (String) tableModel.getValueAt(i, 0);
                boolean isPresent = (Boolean) tableModel.getValueAt(i, 2);
                int status = isPresent ? 1 : 0;
                
                if (AttendanceDAO.markAttendance(studentPid, selectedSubject.getSuid(), status)) {
                    successCount++;
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                "Attendance saved successfully for " + selectedSubject.getName() + "!\n" + 
                "Marked " + successCount + " out of " + totalCount + " students");
                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving attendance: " + e.getMessage());
        }
    }
    
    private void exportToCSV() {
        Subject selectedSubject = (Subject) subjectCombo.getSelectedItem();
        if (selectedSubject == null) {
            JOptionPane.showMessageDialog(this, "Please select a subject first");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Attendance to CSV");
        fileChooser.setSelectedFile(new java.io.File("attendance_" + selectedSubject.getName() + ".csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
                // Write CSV header
                writer.println("Student PID,Student Name,Subject,Status,Date");
                
                // Write data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String pid = (String) tableModel.getValueAt(i, 0);
                    String name = (String) tableModel.getValueAt(i, 1);
                    boolean status = (Boolean) tableModel.getValueAt(i, 2);
                    String statusText = status ? "Present" : "Absent";
                    
                    writer.println(pid + "," + name + "," + selectedSubject.getName() + "," + 
                                 statusText + "," + new java.util.Date());
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Attendance exported successfully to: " + file.getAbsolutePath());
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error exporting to CSV: " + e.getMessage());
            }
        }
    }
}