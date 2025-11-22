package utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StyleUtils {
    // Color scheme
    @SuppressWarnings("exports")
	public static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    @SuppressWarnings("exports")
	public static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    @SuppressWarnings("exports")
	public static final Color ACCENT_COLOR = new Color(46, 204, 113);
    @SuppressWarnings("exports")
	public static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    @SuppressWarnings("exports")
	public static final Color TEXT_COLOR = new Color(44, 62, 80);
    
    // Fonts
    @SuppressWarnings("exports")
	public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    @SuppressWarnings("exports")
	public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    @SuppressWarnings("exports")
	public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    @SuppressWarnings("exports")
	public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    public static void styleButton(@SuppressWarnings("exports") JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }
    
    public static void styleTextField(@SuppressWarnings("exports") JTextField textField) {
        textField.setFont(NORMAL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
    }
    
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(NORMAL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
    }
    
    public static void stylePanel(@SuppressWarnings("exports") JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
    }
}