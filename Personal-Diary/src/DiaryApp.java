import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiaryApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new DiaryFrame("My Personal Diary");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(720, 650);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

class DiaryFrame extends JFrame {
    private JTextArea diaryTextArea;
    private List<String> diaryEntries;
    private int currentEntryIndex;
    private boolean isDarkMode;
    private JPanel panel;
    private Font customFont;

    private JButton searchButton;
    private JTextField searchField;

    public DiaryFrame(String diaryApp) {
        super(diaryApp);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        customFont = new Font("Times New Roman", Font.BOLD, 20);

        diaryTextArea = new JTextArea();
        diaryTextArea.setLineWrap(true);
        diaryTextArea.setWrapStyleWord(true);
        diaryTextArea.setFont(customFont);

        JScrollPane scrollPane = new JScrollPane(diaryTextArea);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton saveButton = new JButton("Save Entry");
        saveButton.addActionListener(new SaveButtonListener());

        JButton editButton = new JButton("Edit Entry");
        editButton.addActionListener(new EditButtonListener());

        JButton deleteButton = new JButton("Delete Entry");
        deleteButton.addActionListener(new DeleteButtonListener());

        JButton prevButton = new JButton("Previous Entry");
        prevButton.addActionListener(new PrevButtonListener());

        JButton nextButton = new JButton("Next Entry");
        nextButton.addActionListener(new NextButtonListener());

        JButton darkModeButton = new JButton("Dark Mode");
        darkModeButton.addActionListener(new DarkModeButtonListener());

        JButton emojiButton = new JButton("✔");

        JButton tagButton = new JButton("Add Tag");
        tagButton.addActionListener(new AddTagButtonListener());

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchButtonListener());
        buttonPanel.add(searchField);

        buttonPanel.add(saveButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(darkModeButton);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(emojiButton);
        buttonPanel.add(tagButton);

        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);


        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        diaryEntries = new ArrayList<>();
        currentEntryIndex = -1;
        isDarkMode = false;

        this.add(panel);
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveDiaryEntry();
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            editDiaryEntry();
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            deleteDiaryEntry();
        }
    }

    private class PrevButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            navigateToPreviousEntry();
        }
    }

    private class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            navigateToNextEntry();
        }
    }

    private class DarkModeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            toggleDarkMode();
        }
    }

    private class EmoticonButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            insertEmoji("😊");
        }
    }

    private class AddTagButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            addTag();
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            searchEntries(searchField.getText());
        }
    }

    private void searchEntries(String searchText){
        diaryTextArea.setSelectionStart(0);
        diaryTextArea.setSelectionEnd(0);

        String text = diaryTextArea.getText();
        int index = text.indexOf(searchText);

        if (index >= 0){
            diaryTextArea.setSelectionStart(index);
            diaryTextArea.setSelectionEnd(index + searchText.length());
        } else {
            JOptionPane.showMessageDialog(this, "Text not found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveDiaryEntry() {
        String entry = diaryTextArea.getText();
        if (!entry.trim().isEmpty()) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            diaryEntries.add("Date/Time: " + timestamp + "\n" + entry);
            currentEntryIndex = diaryEntries.size() - 1;
            diaryTextArea.setText("");
            JOptionPane.showMessageDialog(this, "Entry saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please enter an entry before saving.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editDiaryEntry() {
        String entry = diaryTextArea.getText();
        if (!entry.trim().isEmpty() && currentEntryIndex >= 0 && currentEntryIndex < diaryEntries.size()) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String editedEntry = "Date/Time: " + timestamp + "\n" + entry;
            diaryEntries.set(currentEntryIndex, editedEntry);
            diaryTextArea.setText(editedEntry); // Update the displayed text
            JOptionPane.showMessageDialog(this, "Entry edited successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an entry to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteDiaryEntry() {
        if (currentEntryIndex >= 0 && currentEntryIndex < diaryEntries.size()) {
            diaryEntries.remove(currentEntryIndex);
            if (diaryEntries.isEmpty()) {
                diaryTextArea.setText("");
            } else {
                if (currentEntryIndex >= diaryEntries.size()) {
                    currentEntryIndex = diaryEntries.size() - 1;
                }
                diaryTextArea.setText(diaryEntries.get(currentEntryIndex));
            }
            JOptionPane.showMessageDialog(this, "Entry deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an entry to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void navigateToPreviousEntry() {
        if (currentEntryIndex > 0) {
            currentEntryIndex--;
            diaryTextArea.setText(diaryEntries.get(currentEntryIndex));
        }
    }

    private void navigateToNextEntry() {
        if (currentEntryIndex < diaryEntries.size() - 1) {
            currentEntryIndex++;
            diaryTextArea.setText(diaryEntries.get(currentEntryIndex));
        }
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;

        if (isDarkMode) {
            // Set dark mode colors
            panel.setBackground(Color.BLACK);
            diaryTextArea.setBackground(Color.DARK_GRAY);
            diaryTextArea.setForeground(Color.WHITE);
            // Set other components' colors accordingly
            // You can also change button colors, text colors, etc.
        } else {
            // Revert to light mode colors
            panel.setBackground(Color.WHITE);
            diaryTextArea.setBackground(Color.WHITE);
            diaryTextArea.setForeground(Color.BLACK);
            // Revert other components' colors
        }
    }

    private void insertEmoji(String emoji) {
        int caretPosition = diaryTextArea.getCaretPosition();
        diaryTextArea.insert(emoji, caretPosition);

    }

    private void addTag() {
        String tag = JOptionPane.showInputDialog("Enter a tag:");
        if (tag != null && !tag.trim().isEmpty()) {
            diaryTextArea.insert("[" + tag + "]", diaryTextArea.getCaretPosition());
        }
    }

    // Inner class to represent a diary entry with a tag and content
    private class DiaryEntry {
        private String tag;
        private String content;

        public DiaryEntry(String tag, String content) {
            this.tag = tag;
            this.content = content;
        }

        public String getTag() {
            return tag;
        }

        public String getContent() {
            return content;
        }
    }
}
