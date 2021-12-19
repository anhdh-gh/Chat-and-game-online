package chat.view;

import chat.controller.Controller;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        JOptionPane.showMessageDialog(controller.getCurrentWiew(), text, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccess(String text) {
        JOptionPane.showMessageDialog(controller.getCurrentWiew(), text, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public int showConfirm(String text) {
        // 0 yes, 1 no, 2 cancel
        return JOptionPane.showConfirmDialog(controller.getCurrentWiew(), text);
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