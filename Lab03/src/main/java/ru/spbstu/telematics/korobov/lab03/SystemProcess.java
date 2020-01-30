package ru.spbstu.telematics.korobov.lab03;

import java.util.ArrayDeque;
import java.util.Queue;

public class SystemProcess implements Runnable {
    private final Console console;
    private final Queue<UserTask> taskQueue;
    private final Queue<UserTask> finishedTasksQueue;
    private boolean noNewTasks;
    private int finishedTasks;

    SystemProcess(Console console) {
        this.console = console;
        taskQueue = new ArrayDeque<UserTask>();
        finishedTasksQueue = new ArrayDeque<UserTask>();
        finishedTasks = 0;
    }

    @Override
    public void run() {
        while (!noNewTasks || taskQueue.size() > 0 || finishedTasksQueue.size() > 0) {
            if (taskQueue.size() > 0) {
                try {
                    doTasks();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (taskQueue.size() == 0 || finishedTasksQueue.size() > 0) {
                console.getAcquiredBy(this);
            }
        }
    }

    public void getTask(UserTask task) {
        taskQueue.add(task);
    }

    public boolean noNewTasks() {
        return noNewTasks;
    }

    public int getFinishedTasks() {
        return finishedTasks;
    }

    public void setNoNewTasks(boolean noNewTasks) {
        this.noNewTasks = noNewTasks;
    }

    public void outputResults() throws InterruptedException {
        int outputLimit = 5;
        while (!finishedTasksQueue.isEmpty() && outputLimit > 0) {
            UserTask task = finishedTasksQueue.poll();
            if (task != null) {
                Thread.sleep(task.getOutputTimeMs());
                System.out.println("Task #" + task.getId() + " from User" + task.getUserId() + ": finished output!");
                console.addCompletedUserTask(task.getUserId());
                finishedTasks++;
                outputLimit--;
            }
        }
    }

    private void doTasks() throws InterruptedException {
        int executionLimit = 10;
        while (!taskQueue.isEmpty() && executionLimit > 0) {
            UserTask task = taskQueue.poll();
            if (task != null) {
                Thread.sleep(task.getCompletionTimeMs());
                finishedTasksQueue.add(task);
                System.out.println("Task #" + task.getId() + " from User" + task.getUserId() + ": finished execution!");
                executionLimit--;
            }
        }
    }
}
