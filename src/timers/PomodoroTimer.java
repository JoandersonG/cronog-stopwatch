package timers;

import java.awt.*;

public class PomodoroTimer {
    private boolean shouldStop;
    private int seconds;
    private int minutes;
    private int hours;
    private final int minutesWorking;
    private final int minutesResting;
    private boolean onABreak = false;
    private int sessionAmount = 0;

    public PomodoroTimer(int minutesWorking, int minutesResting) {
        this.minutesWorking = minutesWorking;
        this.minutesResting = minutesResting;
        this.minutes = minutesWorking;
    }

    public void start() {
        shouldStop = false;
    }

    public void pause() {
        shouldStop = true;
    }

    public void restart() {
        this.minutes = minutesWorking;
        this.seconds = 0;
    }

    public void startBreakTimer() {
        this.minutes = minutesResting;
        this.seconds = 0;
    }

    //Method that execute one iteration out of every second iterations that the timer executes
    public void timerIteration() {
        if(this.seconds == 0) {
            this.minutes--;
            this.seconds = 59;
        } else {
            this.seconds--;
        }
        if (this.minutes == -1) {
            if (onABreak) {
                //finished entire Pomodoro session
                sessionAmount++;
                try {
                    displayNotification("De volta ao trabalho");
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                this.minutes = this.minutesWorking;
                this.seconds = 0;
                onABreak = false;
            } else {//starting a Pomodoro break
                if (SystemTray.isSupported()) {
                    try {
                        displayNotification("Hora de descansar");
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("System tray not supported: impossible to send notifications");
                }
                startBreakTimer();
                onABreak = true;
            }
        }
    }

    private static void displayNotification(String message) throws AWTException {

        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "CronoG notification");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("CronoG notification");
        tray.add(trayIcon);

        trayIcon.displayMessage("CronoG", message, TrayIcon.MessageType.NONE);
    }

    public void addMinuteToTimer() {
        this.minutes++;
    }

    public boolean isOnABreak() {
        return onABreak;
    }

    public int getSessionAmount() {
        return sessionAmount;
    }

    public boolean shouldStop() {
        return shouldStop;
    }

    public void setShouldStop(boolean b) {
        shouldStop = b;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }
}
