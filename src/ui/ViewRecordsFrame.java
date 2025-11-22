package ui;

import models.Attendance;
import models.Subject;
import dao.AttendanceDAO;
import dao.SubjectDAO;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class ViewRecordsFrame extends JFrame {
    private JTable recordsTable;
    private DefaultTableModel tableModel;
    private JButton refreshBtn, exportBtn, filterBtn, deleteBtn, modifyBtn, clearFiltersBtn;
    private JComboBox<String> filterCombo;
    private JComboBox<Subject> subjectFilterCombo;
    private JTextField searchField;
    private JSpinner dateFromSpinner, dateToSpinner;
    
    public ViewRecordsFrame() {
        setTitle("View Attendance Records");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        applyStyles();
        loadAttendanceData();
        loadSubjects();
        
        setVisible(true);
    }
    
    private void initComponents() {
        
        String[] columns = {"Record ID", "Student PID", "Student Name", "Subject", "Status", "Date", "Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        recordsTable = new JTable(tableModel);
        recordsTable.setRowHeight(30);
        recordsTable.setAutoCreateRowSorter(true); 
        
        // Filter components
        filterCombo = new JComboBox<>(new String[]{"All", "Present", "Absent"});
        subjectFilterCombo = new JComboBox<>();
        subjectFilterCombo.addItem(new Subject(-1, "All Subjects")); 
        
        searchField = new JTextField(15);
        
        // Date spinners
        SpinnerDateModel dateModelFrom = new SpinnerDateModel();
        dateFromSpinner = new JSpinner(dateModelFrom);
        JSpinner.DateEditor dateEditorFrom = new JSpinner.DateEditor(dateFromSpinner, "yyyy-MM-dd");
        dateFromSpinner.setEditor(dateEditorFrom);
        
        SpinnerDateModel dateModelTo = new SpinnerDateModel();
        dateToSpinner = new JSpinner(dateModelTo);
        JSpinner.DateEditor dateEditorTo = new JSpinner.DateEditor(dateToSpinner, "yyyy-MM-dd");
        dateToSpinner.setEditor(dateEditorTo);
        
      
        Calendar calendar = Calendar.getInstance();
        dateToSpinner.setValue(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        dateFromSpinner.setValue(calendar.getTime());
        
        // Buttons
        refreshBtn = new JButton("Refresh");
        exportBtn = new JButton("Export to CSV");
        filterBtn = new JButton("Apply Filters");
        deleteBtn = new JButton("Delete Selected");
        modifyBtn = new JButton("Modify Status");
        clearFiltersBtn = new JButton("Clear Filters");
        
        setupEventListeners();
    }
    
    @SuppressWarnings("unused")
	private void setupEventListeners() {
        refreshBtn.addActionListener(e -> loadAttendanceData());
        exportBtn.addActionListener(e -> exportToCSV());
        filterBtn.addActionListener(e -> applyFilter());
        deleteBtn.addActionListener(e -> deleteSelectedRecord());
        modifyBtn.addActionListener(e -> modifySelectedRecord());
        clearFiltersBtn.addActionListener(e -> clearFilters());
        
        // Search as you type
        searchField.addActionListener(e -> applyFilter());
        
        // Double-click to modify
        recordsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    modifySelectedRecord();
                }
            }
        });
    }
    
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        StyleUtils.stylePanel(mainPanel);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Attendance Records");
        titleLabel.setFont(StyleUtils.TITLE_FONT);
        titleLabel.setForeground(new Color(65, 105, 225));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Filter panel - First row
        JPanel filterPanelRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanelRow1.setBackground(StyleUtils.BACKGROUND_COLOR);
        
        filterPanelRow1.add(new JLabel("Search:"));
        filterPanelRow1.add(searchField);
        filterPanelRow1.add(new JLabel("Status:"));
        filterPanelRow1.add(filterCombo);
        filterPanelRow1.add(new JLabel("Subject:"));
        filterPanelRow1.add(subjectFilterCombo);
        
        // Filter panel - Second row
        JPanel filterPanelRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanelRow2.setBackground(StyleUtils.BACKGROUND_COLOR);
        
        filterPanelRow2.add(new JLabel("Date From:"));
        filterPanelRow2.add(dateFromSpinner);
        filterPanelRow2.add(new JLabel("Date To:"));
        filterPanelRow2.add(dateToSpinner);
        filterPanelRow2.add(filterBtn);
        filterPanelRow2.add(clearFiltersBtn);
        filterPanelRow2.add(refreshBtn);
        filterPanelRow2.add(exportBtn);
        
        // Combine filter rows
        JPanel filterPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        filterPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        filterPanel.add(filterPanelRow1);
        filterPanel.add(filterPanelRow2);
        
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(recordsTable);
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        actionPanel.add(modifyBtn);
        actionPanel.add(deleteBtn);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void applyStyles() {
        StyleUtils.styleTextField(searchField);
        StyleUtils.styleComboBox(filterCombo);
        StyleUtils.styleComboBox(subjectFilterCombo);
        StyleUtils.styleButton(refreshBtn);
        StyleUtils.styleButton(exportBtn);
        StyleUtils.styleButton(filterBtn);
        StyleUtils.styleButton(modifyBtn);
        StyleUtils.styleButton(clearFiltersBtn);
        
        // Style date spinners
        dateFromSpinner.setFont(StyleUtils.NORMAL_FONT);
        dateToSpinner.setFont(StyleUtils.NORMAL_FONT);
        
        // Style delete button differently
        deleteBtn.setFont(StyleUtils.BUTTON_FONT);
        deleteBtn.setBackground(new Color(231, 76, 60)); // Red color for delete
        deleteBtn.setForeground(new Color(65, 105, 225));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(192, 57, 43), 1),
            new javax.swing.border.EmptyBorder(10, 20, 10, 20)
        ));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        deleteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteBtn.setBackground(new Color(231, 76, 60));
            }
        });
        
        // Style table
        recordsTable.setFont(StyleUtils.NORMAL_FONT);
        recordsTable.getTableHeader().setFont(StyleUtils.BUTTON_FONT);
        recordsTable.getTableHeader().setBackground(StyleUtils.PRIMARY_COLOR);
        recordsTable.getTableHeader().setForeground(new Color(65, 105, 225));
    }
    
    private void loadSubjects() {
        try {
            List<Subject> subjects = SubjectDAO.getAllSubjects();
            for (Subject subject : subjects) {
                subjectFilterCombo.addItem(subject);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading subjects: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadAttendanceData() {
        try {
            List<Attendance> attendanceList = AttendanceDAO.getAllAttendance();
            populateTable(attendanceList);
            updateStats(attendanceList);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading attendance records: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void populateTable(List<Attendance> attendanceList) {
        tableModel.setRowCount(0);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        for (Attendance attendance : attendanceList) {
            String date = dateFormat.format(attendance.getStime());
            String time = timeFormat.format(attendance.getStime());
            
            // Use actual names from the database
            String studentName = attendance.getStudentName() != null ? 
                attendance.getStudentName() : "Student " + attendance.getPid();
            
            String subjectName = attendance.getSubjectName() != null ? 
                attendance.getSubjectName() : "Subject " + attendance.getSuid();
            
            tableModel.addRow(new Object[]{
                attendance.getAid(),
                attendance.getPid(),
                studentName, // Actual student name
                subjectName, // Actual subject name
                attendance.getStatusWithIcon(),
                date,
                time
            });
        }
    }
    
    private void updateStats(List<Attendance> attendanceList) {
        int totalRecords = attendanceList.size();
        int presentCount = 0;
        
        for (Attendance attendance : attendanceList) {
            if (attendance.getStatus() == 1) {
                presentCount++;
            }
        }
        
        double presentPercentage = totalRecords > 0 ? (presentCount * 100.0) / totalRecords : 0;
        
        // Update status bar or show in dialog
        setTitle("View Attendance Records - Total: " + totalRecords + 
                " | Present: " + presentCount + " (" + String.format("%.1f", presentPercentage) + "%)");
    }
    
    private void applyFilter() {
        String searchText = searchField.getText().toLowerCase();
        String statusFilter = (String) filterCombo.getSelectedItem();
        Subject selectedSubject = (Subject) subjectFilterCombo.getSelectedItem();
        Date dateFrom = (Date) dateFromSpinner.getValue();
        Date dateTo = (Date) dateToSpinner.getValue();
        
        // Validate date range
        if (dateFrom.after(dateTo)) {
            JOptionPane.showMessageDialog(this, 
                "Date From cannot be after Date To", 
                "Invalid Date Range", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            List<Attendance> allRecords = AttendanceDAO.getAllAttendance();
            List<Attendance> filteredRecords = allRecords.stream()
                .filter(record -> {
                    // Search filter
                    boolean matchesSearch = searchText.isEmpty() ||
                        record.getPid().toLowerCase().contains(searchText) ||
                        (record.getStudentName() != null && 
                         record.getStudentName().toLowerCase().contains(searchText)) ||
                        (record.getSubjectName() != null && 
                         record.getSubjectName().toLowerCase().contains(searchText));
                    
                    // Status filter
                    boolean matchesStatus = statusFilter.equals("All") ||
                        (statusFilter.equals("Present") && record.getStatus() == 1) ||
                        (statusFilter.equals("Absent") && record.getStatus() == 0);
                    
                    // Subject filter
                    boolean matchesSubject = selectedSubject.getSuid() == -1 || // "All Subjects"
                        record.getSuid() == selectedSubject.getSuid();
                    
                    // Date filter
                    java.sql.Date recordDate = new java.sql.Date(record.getStime().getTime());
                    java.sql.Date fromDate = new java.sql.Date(dateFrom.getTime());
                    java.sql.Date toDate = new java.sql.Date(dateTo.getTime());
                    
                    boolean matchesDate = 
                        (recordDate.equals(fromDate) || recordDate.after(fromDate)) &&
                        (recordDate.equals(toDate) || recordDate.before(toDate));
                    
                    return matchesSearch && matchesStatus && matchesSubject && matchesDate;
                })
                .toList();
            
            populateTable(filteredRecords);
            updateStats(filteredRecords);
            
            // Show filter summary
            if (filteredRecords.size() != allRecords.size()) {
                setTitle("View Attendance Records - Filtered: " + filteredRecords.size() + 
                        " of " + allRecords.size() + " records");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error applying filter: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFilters() {
        searchField.setText("");
        filterCombo.setSelectedIndex(0);
        subjectFilterCombo.setSelectedIndex(0);
        
        // Reset dates to default (last 30 days)
        Calendar calendar = Calendar.getInstance();
        dateToSpinner.setValue(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        dateFromSpinner.setValue(calendar.getTime());
        
        loadAttendanceData(); // Reload all data
    }
    
    private void deleteSelectedRecord() {
        int selectedRow = recordsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a record to delete", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row index to model row index (in case of sorting)
        int modelRow = recordsTable.convertRowIndexToModel(selectedRow);
        
        int recordId = (Integer) tableModel.getValueAt(modelRow, 0);
        String studentName = (String) tableModel.getValueAt(modelRow, 2);
        String subjectName = (String) tableModel.getValueAt(modelRow, 3);
        String status = (String) tableModel.getValueAt(modelRow, 4);
        String date = (String) tableModel.getValueAt(modelRow, 5);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this attendance record?\n\n" +
            "Student: " + studentName + "\n" +
            "Subject: " + subjectName + "\n" +
            "Status: " + status + "\n" +
            "Date: " + date,
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (AttendanceDAO.deleteAttendance(recordId)) {
                    JOptionPane.showMessageDialog(this, 
                        "Attendance record deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAttendanceData(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to delete attendance record", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting record: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void modifySelectedRecord() {
        int selectedRow = recordsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a record to modify", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row index to model row index (in case of sorting)
        int modelRow = recordsTable.convertRowIndexToModel(selectedRow);
        
        int recordId = (Integer) tableModel.getValueAt(modelRow, 0);
        String studentName = (String) tableModel.getValueAt(modelRow, 2);
        String subjectName = (String) tableModel.getValueAt(modelRow, 3);
        String currentStatus = (String) tableModel.getValueAt(modelRow, 4);
        String date = (String) tableModel.getValueAt(modelRow, 5);
        
        // Determine current status
        boolean isCurrentlyPresent = currentStatus.contains("✅");
        
        // Create dialog for status modification
        Object[] options = {"Present", "Absent", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
            "Modify attendance status for:\n\n" +
            "Student: " + studentName + "\n" +
            "Subject: " + subjectName + "\n" +
            "Date: " + date + "\n\n" +
            "Current Status: " + (isCurrentlyPresent ? "Present" : "Absent") + "\n" +
            "Select new status:",
            "Modify Attendance Status",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[isCurrentlyPresent ? 0 : 1]);
        
        if (choice == 0 || choice == 1) { // Present or Absent selected
            int newStatus = (choice == 0) ? 1 : 0; // 1 for Present, 0 for Absent
            
            // Don't update if status hasn't changed
            if ((isCurrentlyPresent && newStatus == 1) || (!isCurrentlyPresent && newStatus == 0)) {
                JOptionPane.showMessageDialog(this, 
                    "Status is already set to " + (newStatus == 1 ? "Present" : "Absent"),
                    "No Change", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            try {
                if (AttendanceDAO.updateAttendance(recordId, newStatus)) {
                    JOptionPane.showMessageDialog(this, 
                        "Attendance status updated successfully!\n\n" +
                        "Student: " + studentName + "\n" +
                        "New Status: " + (newStatus == 1 ? "Present" : "Absent"),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAttendanceData(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to update attendance status", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error updating record: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
       
    }
    
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Records to CSV");
        fileChooser.setSelectedFile(new java.io.File("attendance_records.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
                // Write CSV header
                writer.println("Record ID,Student PID,Student Name,Subject,Status,Date,Time");
                
                // Write data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    writer.printf("%s,%s,%s,%s,%s,%s,%s%n",
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2),
                        tableModel.getValueAt(i, 3),
                        tableModel.getValueAt(i, 4).toString().replace("✅ ", "").replace("❌ ", ""),
                        tableModel.getValueAt(i, 5),
                        tableModel.getValueAt(i, 6)
                    );
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Records exported successfully to: " + file.getAbsolutePath());
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error exporting to CSV: " + e.getMessage());
            }
        }
    }
}