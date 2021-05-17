package gui;

import timers.PomodoroTimer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private JButton btStart;
    private JButton btPause;
    private JButton brRestart;
    private JPanel panelMain;
    private JLabel txtTime;
    private JButton btPlusMinute;
    private JLabel txtSessionAmount;

    private boolean shouldStop;
    private int seconds;
    private int minutes;
    private int hours;

    //for Pomodoro timer
    PomodoroTimer pomodoroTimer;
    ScheduledExecutorService scheduler;

    public static void main(String[] args) {
        JFrame frame = new JFrame("CronoG");
        frame.setContentPane(new Main().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Main() {
        txtTime.setForeground(Color.red);
        btStart.setFocusable(false);
        btPause.setFocusable(false);
        brRestart.setFocusable(false);

        setupAsPomodoroTimer();
        pomodoroTimer = new PomodoroTimer(30,5);
    }

    private void setupAsPomodoroTimer() {

        btStart.addActionListener(actionEvent -> {
            btStart.setEnabled(false);
            btPause.setEnabled(true);
            pomodoroTimer.start();
            this.runPomodoroTimer();
        });
        btPause.addActionListener(actionEvent -> {
            pomodoroTimer.pause();
            btStart.setEnabled(true);
            btPause.setEnabled(false);
        });
        brRestart.addActionListener(actionEvent -> {
            pomodoroTimer.restart();
            scheduler.shutdown();
            btStart.setEnabled(true);
            btPause.setEnabled(false);
            txtTime.setText("00:" + (this.minutes < 10 ? "0" + this.minutes : this.minutes) + ":00");
        });
        btPlusMinute.addActionListener(actionEvent -> {
            pomodoroTimer.addMinuteToTimer();
        });
    }

    private void runPomodoroTimer() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
            () -> {
                if (pomodoroTimer.shouldStop()) {
                    pomodoroTimer.setShouldStop(false);
                    scheduler.shutdown();
                    return;
                }

                pomodoroTimer.timerIteration();

                txtSessionAmount.setText("Total de sessões: " + pomodoroTimer.getSessionAmount());

                if(pomodoroTimer.isOnABreak() && txtTime.getForeground() == Color.red){
                    txtTime.setForeground(Color.blue);
                } else if (!pomodoroTimer.isOnABreak() && txtTime.getForeground() == Color.blue) {
                    txtTime.setForeground(Color.red);
                }
                //show seconds update on UI
                txtTime.setText((((pomodoroTimer.getHours() < 10) ? ("0" + pomodoroTimer.getHours()) : pomodoroTimer.getHours()) + ":")
                        + (((pomodoroTimer.getMinutes() < 10) ? ("0" + pomodoroTimer.getMinutes()) : pomodoroTimer.getMinutes()) + ":")
                        + ((pomodoroTimer.getSeconds() < 10) ? ("0" + pomodoroTimer.getSeconds()) : pomodoroTimer.getSeconds()));
            }, 1, 1, TimeUnit.SECONDS);
    }


    private void setupAsStopwatch() {
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
