import javax.swing.*;
public class DigitWindow{
    public static DigitPanel panel;
    public static void createWindow(){
        JFrame frame = new JFrame("Game");
        frame.setSize(560,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new DigitPanel();
        frame.add(panel);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }
}