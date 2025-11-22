package ui;

import models.Student;
import models.Attendance;
import dao.AttendanceDAO;
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
public class StudentDashboard extends JFrame {
    private Student student;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JLabel statsLabel, welcomeLabel;
    private JButton refreshBtn, logoutBtn, exportBtn;
    private JComboBox<String> statusFilterCombo, subjectFilterCombo;
    private JSpinner dateFromSpinner, dateToSpinner;
    private JButton applyFilterBtn, clearFilterBtn;
    
    public StudentDashboard(Student student) {
        this.student = student;
        
        setTitle("Attendance System - Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        applyStyles();
        loadAttendanceData();
        
        setVisible(true);
    }
    
    private void initComponents() {
        // Attendance table
        String[] columns = {"Date", "Subject", "Status", "Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        attendanceTable = new JTable(tableModel);
        attendanceTable.setRowHeight(30);
        attendanceTable.setAutoCreateRowSorter(true);
        
        // Stats label
        statsLabel = new JLabel("Loading attendance data...");
        statsLabel.setFont(StyleUtils.HEADER_FONT);
        
        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + student.getName());
        
        // Filter components
        statusFilterCombo = new JComboBox<>(new String[]{"All", "Present", "Absent"});
        subjectFilterCombo = new JComboBox<>(new String[]{"All Subjects"});
        
        // Date spinners
        SpinnerDateModel dateModelFrom = new SpinnerDateModel();
        dateFromSpinner = new JSpinner(dateModelFrom);
        JSpinner.DateEditor dateEditorFrom = new JSpinner.DateEditor(dateFromSpinner, "yyyy-MM-dd");
        dateFromSpinner.setEditor(dateEditorFrom);
        
        SpinnerDateModel dateModelTo = new SpinnerDateModel();
        dateToSpinner = new JSpinner(dateModelTo);
        JSpinner.DateEditor dateEditorTo = new JSpinner.DateEditor(dateToSpinner, "yyyy-MM-dd");
        dateToSpinner.setEditor(dateEditorTo);
        
        // Set default dates (last 30 days)
        Calendar calendar = Calendar.getInstance();
        dateToSpinner.setValue(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        dateFromSpinner.setValue(calendar.getTime());
        
        // Buttons
        refreshBtn = new JButton("Refresh");
        logoutBtn = new JButton("Logout");
        exportBtn = new JButton("Export to CSV");
        applyFilterBtn = new JButton("Apply Filters");
        clearFilterBtn = new JButton("Clear Filters");
        
        setupEventListeners();
    }
    
    @SuppressWarnings("unused")
	private void setupEventListeners() {
        refreshBtn.addActionListener(e -> loadAttendanceData());
        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
        exportBtn.addActionListener(e -> exportToCSV());
        applyFilterBtn.addActionListener(e -> applyFilters());
        clearFilterBtn.addActionListener(e -> clearFilters());
    }
    
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        StyleUtils.stylePanel(mainPanel);

        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        welcomeLabel.setFont(StyleUtils.HEADER_FONT);
        welcomeLabel.setForeground(Color.WHITE);

        JLabel pidLabel = new JLabel("PID: " + student.getPid());
        pidLabel.setFont(StyleUtils.NORMAL_FONT);
        pidLabel.setForeground(Color.WHITE);

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(pidLabel, BorderLayout.EAST);

       
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JPanel filterRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterRow1.setBackground(StyleUtils.BACKGROUND_COLOR);
        filterRow1.add(new JLabel("Status:"));
        filterRow1.add(statusFilterCombo);
        filterRow1.add(new JLabel("Subject:"));
        filterRow1.add(subjectFilterCombo);
        filterRow1.add(applyFilterBtn);
        filterRow1.add(clearFilterBtn);

        
        JPanel filterRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterRow2.setBackground(StyleUtils.BACKGROUND_COLOR);
        filterRow2.add(new JLabel("Date From:"));
        filterRow2.add(dateFromSpinner);
        filterRow2.add(new JLabel("Date To:"));
        filterRow2.add(dateToSpinner);
        filterRow2.add(refreshBtn);
        filterRow2.add(exportBtn);

        filterPanel.add(filterRow1);
        filterPanel.add(filterRow2);

        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        statsPanel.add(statsLabel);

        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(StyleUtils.BACKGROUND_COLOR);

        centerPanel.add(filterPanel);
        centerPanel.add(statsPanel);
        centerPanel.add(new JScrollPane(attendanceTable));

        // ===================== BOTTOM PANEL =====================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.add(logoutBtn);

        // ===================== ASSEMBLE WHOLE WINDOW =====================
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    
    private void applyStyles() {
       
        StyleUtils.styleButton(refreshBtn);
        StyleUtils.styleButton(logoutBtn);
        StyleUtils.styleButton(exportBtn);
        StyleUtils.styleButton(applyFilterBtn);
        StyleUtils.styleButton(clearFilterBtn);
        
        
        StyleUtils.styleComboBox(statusFilterCombo);
        StyleUtils.styleComboBox(subjectFilterCombo);
        
     
        dateFromSpinner.setFont(StyleUtils.NORMAL_FONT);
        dateToSpinner.setFont(StyleUtils.NORMAL_FONT);
        
       
        attendanceTable.setFont(StyleUtils.NORMAL_FONT);
        attendanceTable.getTableHeader().setFont(StyleUtils.BUTTON_FONT);
        attendanceTable.getTableHeader().setBackground(StyleUtils.PRIMARY_COLOR);
        attendanceTable.getTableHeader().setForeground(new Color(65, 105, 225));
        
        
        welcomeLabel.setFont(StyleUtils.HEADER_FONT);
        welcomeLabel.setForeground(Color.WHITE);
    }
    
    private void loadAttendanceData() {
        try {
            List<Attendance> attendanceList = AttendanceDAO.getAttendanceByStudent(student.getPid());
            populateTable(attendanceList);
            updateStats(attendanceList);
            updateSubjectFilter(attendanceList);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading attendance data: " + e.getMessage(),
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
            
            // Use actual subject name from the database
            String subjectName = attendance.getSubjectName() != null ? 
                attendance.getSubjectName() : "Subject " + attendance.getSuid();
            
            tableModel.addRow(new Object[]{
                date,
                subjectName, // Actual subject name
                attendance.getStatusWithIcon(),
                time
            });
        }
    }
    
    private void updateStats(List<Attendance> attendanceList) {
        int totalCount = attendanceList.size();
        int presentCount = 0;
        
        for (Attendance attendance : attendanceList) {
            if (attendance.getStatus() == 1) {
                presentCount++;
            }
        }
        
        if (totalCount > 0) {
            double attendanceRate = (presentCount * 100.0) / totalCount;
            statsLabel.setText(String.format(
                "Attendance Summary: %d/%d (%.1f%%) - Present: %d, Absent: %d",
                presentCount, totalCount, attendanceRate, presentCount, totalCount - presentCount
            ));
            
            // Update window title with stats
            setTitle("Student Dashboard - " + student.getName() + " - " + 
                    presentCount + "/" + totalCount + " (" + String.format("%.1f", attendanceRate) + "%)");
        } else {
            statsLabel.setText("No attendance records found.");
            setTitle("Student Dashboard - " + student.getName() + " - No Records");
        }
    }
    
    private void updateSubjectFilter(List<Attendance> attendanceList) {
        // Save current selection
        String currentSelection = (String) subjectFilterCombo.getSelectedItem();
        
        // Clear and repopulate subject filter
        subjectFilterCombo.removeAllItems();
        subjectFilterCombo.addItem("All Subjects");
        
        // Add unique subjects from attendance records
        attendanceList.stream()
            .map(attendance -> attendance.getSubjectName() != null ? 
                 attendance.getSubjectName() : "Subject " + attendance.getSuid())
            .distinct()
            .sorted()
            .forEach(subjectFilterCombo::addItem);
        
        // Restore previous selection if it still exists
        if (currentSelection != null) {
            for (int i = 0; i < subjectFilterCombo.getItemCount(); i++) {
                if (subjectFilterCombo.getItemAt(i).equals(currentSelection)) {
                    subjectFilterCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private void applyFilters() {
        String statusFilter = (String) statusFilterCombo.getSelectedItem();
        String subjectFilter = (String) subjectFilterCombo.getSelectedItem();
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
            List<Attendance> allRecords = AttendanceDAO.getAttendanceByStudent(student.getPid());
            List<Attendance> filteredRecords = allRecords.stream()
                .filter(record -> {
                    // Status filter
                    boolean matchesStatus = statusFilter.equals("All") ||
                        (statusFilter.equals("Present") && record.getStatus() == 1) ||
                        (statusFilter.equals("Absent") && record.getStatus() == 0);
                    
                    // Subject filter
                    String recordSubject = record.getSubjectName() != null ? 
                        record.getSubjectName() : "Subject " + record.getSuid();
                    boolean matchesSubject = subjectFilter.equals("All Subjects") ||
                        recordSubject.equals(subjectFilter);
                    
                    // Date filter
                    java.sql.Date recordDate = new java.sql.Date(record.getStime().getTime());
                    java.sql.Date fromDate = new java.sql.Date(dateFrom.getTime());
                    java.sql.Date toDate = new java.sql.Date(dateTo.getTime());
                    
                    boolean matchesDate = 
                        (recordDate.equals(fromDate) || recordDate.after(fromDate)) &&
                        (recordDate.equals(toDate) || recordDate.before(toDate));
                    
                    return matchesStatus && matchesSubject && matchesDate;
                })
                .toList();
            
            populateTable(filteredRecords);
            updateFilteredStats(filteredRecords, allRecords.size());
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error applying filters: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateFilteredStats(List<Attendance> filteredRecords, int totalRecords) {
        int filteredCount = filteredRecords.size();
        int presentCount = 0;
        
        for (Attendance attendance : filteredRecords) {
            if (attendance.getStatus() == 1) {
                presentCount++;
            }
        }
        
        if (filteredCount > 0) {
            double attendanceRate = (presentCount * 100.0) / filteredCount;
            statsLabel.setText(String.format(
                "Filtered: %d/%d records - Present: %d, Absent: %d (%.1f%%)",
                filteredCount, totalRecords, presentCount, filteredCount - presentCount, attendanceRate
            ));
        } else {
            statsLabel.setText("No records match the current filters.");
        }
    }
    
    private void clearFilters() {
        statusFilterCombo.setSelectedIndex(0);
        subjectFilterCombo.setSelectedIndex(0);
        
        // Reset dates to default (last 30 days)
        Calendar calendar = Calendar.getInstance();
        dateToSpinner.setValue(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        dateFromSpinner.setValue(calendar.getTime());
        
        loadAttendanceData(); // Reload all data
    }
    
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export My Attendance to CSV");
        fileChooser.setSelectedFile(new java.io.File(student.getName() + "_attendance.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
                // Write CSV header
                writer.println("Date,Subject,Status,Time,Student Name,Student PID");
                
                // Write data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String date = (String) tableModel.getValueAt(i, 0);
                    String subject = (String) tableModel.getValueAt(i, 1);
                    String status = ((String) tableModel.getValueAt(i, 2))
                        .replace("✅ ", "").replace("❌ ", "");
                    String time = (String) tableModel.getValueAt(i, 3);
                    
                    writer.printf("%s,%s,%s,%s,%s,%s%n",
                        date, subject, status, time, student.getName(), student.getPid()
                    );
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