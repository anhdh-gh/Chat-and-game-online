package chat.view;

import chat.controller.Controller;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class View extends JFrame {   
    
    protected static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    
    protected Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }
    
    public void renderView() {} // Hàm này sẽ render lại toàn bộ dữ liệu trên view
    
    public void showError(String text) {
        JOptionPane.showMessageDialog(controller.getCurrentView(), text, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccess(String text) {
        JOptionPane.showMessageDialog(controller.getCurrentView(), text, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public int showConfirm(String text) {
        // 0 yes, 1 no, 2 cancel
        return JOptionPane.showConfirmDialog(controller.getCurrentView(), text);
    }
    
    public File[] showSelectMultipleFiles(String approveButtonText) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        
        int res = fileChooser.showDialog(this, approveButtonText);
        if(res == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            return files;
        }

        return new File[0];
    }
    
    public String showSelectDirectory(String approveButtonText) {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int res = folderChooser.showDialog(this, approveButtonText);
        if(res == JFileChooser.APPROVE_OPTION) {
            return folderChooser.getSelectedFile().toString();
        }

        return null;   
    }
    
    public void deleteView() {
        this.dispose();
    }
    
    public void displayView() {
        this.setVisible(true);
    }

    protected boolean isValidateEmali(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}