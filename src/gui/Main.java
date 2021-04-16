package gui;

import javax.swing.*;

public class Main {
    private JButton btIniciar;
    private JButton btPausar;
    private JButton brReiniciar;
    private JPanel panelMain;

    public static void main(String[] args) {
        JFrame frame = new JFrame("CronoG");
        frame.setContentPane(new Main().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
