package gui;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private JButton btStartPause;
    private JButton btPause;
    private JButton brRestart;
    private JPanel panelMain;
    private JLabel txtTime;
    private JLabel txtMinutes;
    private JLabel txtHours;

    public static void main(String[] args) {
        JFrame frame = new JFrame("CronoG");
        frame.setContentPane(new Main().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Main() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        Instant start = Instant.now();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                () -> {
                    Instant end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    String hours = String.valueOf(ChronoUnit.HOURS.between(start,end));
                    txtHours.setText(hours +":");
                    txtTime.setText(timeFormat.format(timeElapsed.toMillis()));
                }, 1, 1, TimeUnit.SECONDS);

    }
}
