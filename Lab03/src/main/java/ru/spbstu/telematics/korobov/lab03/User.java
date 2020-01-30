package ru.spbstu.telematics.korobov.lab03;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class User implements Runnable {
    private final Console console;
    private final Queue<UserTask> userTasks;
    private final int id;
    private final Random rand;
    private int initialTasksCount;
    private int finishedTasksCount;

    User(Console console, int id) {
        this.console = console;
        userTasks = new ArrayDeque<UserTask>();
        this.id = id;
        rand = new Random();
        finishedTasksCount = 0;
        populateTaskQueue();
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        while (finishedTasksCount < initialTasksCount) {
            console.getAcquiredBy(this);
        }
    }

    public boolean submitTask() throws InterruptedException {
        if (!userTasks.isEmpty()) {
            UserTask task = userTasks.poll();
            Thread.sleep(task.getSubmitTimeMs());
            console.addTaskToQueue(task);
            System.out.println("Task #" + task.getId() + " from User" + task.getUserId() + ": submitted!");
        }
        return userTasks.isEmpty();
    }

    public void addFinishedTasks(int count) {
        finishedTasksCount += count;
    }

    private void populateTaskQueue() {
        int taskTime = 1;
        initialTasksCount = rand.nextInt(4) + 1;
        for (int i = 0; i < initialTasksCount; ++i) {
            UserTask task = new UserTask(
                    rand.nextInt(5 * taskTime) + 1, rand.nextInt(25 * taskTime) + 1,
                    rand.nextInt(10 * taskTime) + 1, getId());
            userTasks.add(task);
        }
    }

    public int getInitialTasksCount() {
        return initialTasksCount;
    }

    public int getFinishedTasksCount() {
        return finishedTasksCount;
    }
}
