package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private JButton btStart;
    private JButton btPause;
    private JButton brRestart;
    private JPanel panelMain;
    private JLabel txtTime;

    private boolean shouldStop;
    private int seconds;
    private int minutes;
    private int hours;

    public static void main(String[] args) {
        JFrame frame = new JFrame("CronoG");
        frame.setContentPane(new Main().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Main() {

        btStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                shouldStop = false;
                runStopwatch();
                btStart.setEnabled(false);
                btPause.setEnabled(true);
            }
        });
        btPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                pauseStopWatch();
                btStart.setEnabled(true);
                btPause.setEnabled(false);
            }
        });
        brRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                btStart.setEnabled(true);
                btPause.setEnabled(false);
                pauseStopWatch();
                clearTime();


            }
        });
    }

    private void clearTime() {
        txtTime.setText("00:00:00");
        this.seconds = 0;
        this.minutes = 0;
        this.hours = 0;
    }

    private void pauseStopWatch() {
        shouldStop = true;
    }

    private void runStopwatch() {
        SimpleDateFormat secondsFormat = new SimpleDateFormat("ss");
        SimpleDateFormat minutesFormat = new SimpleDateFormat("mm");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                () -> {
                    if (shouldStop) {
                        shouldStop = false;
                        scheduler.shutdown();
                        return;
                    }

                    if(this.seconds > 58) {
                        this.minutes++;
                        this.seconds = 0;
                    } else {
                        this.seconds++;
                    }
                    if (this.minutes > 58) {
                        this.hours++;
                        this.minutes = 0;
                    }

                    txtTime.setText(String.valueOf(
                            (((this.hours < 10) ? ("0" + this.hours) : this.hours) + ":")
                            +(((this.minutes < 10) ? ("0" + this.minutes) : this.minutes) + ":")
                            + ((this.seconds < 10) ? ("0" + this.seconds) : this.seconds)
                    ));


                }, 1, 1, TimeUnit.SECONDS);
    }
}
