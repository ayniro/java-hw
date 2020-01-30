package ru.spbstu.telematics.korobov.lab03;

public class OperatingSystem {
    private final Console console;
    private final SystemProcess systemProcess;
    private int numberOfUsers;
    private User[] users;

    OperatingSystem(int users) {
        console = new Console(users);
        systemProcess = new SystemProcess(console);
        this.numberOfUsers = users;
        this.users = new User[numberOfUsers];
    }

    public void start() {
        for (int i = 0; i < numberOfUsers; ++i) {
            users[i] = new User(console, i);
            Thread t = new Thread(users[i], "User" + i);
            t.start();
        }
        Thread systemProcessThread = new Thread(systemProcess, "System thread");
        systemProcessThread.run();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public User[] getUsers() {
        return users;
    }

    public Console getConsole() {
        return console;
    }

    public SystemProcess getSystemProcess() {
        return systemProcess;
    }
}
