package ru.spbstu.telematics.korobov.lab03;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Console {
    private final Semaphore binarySemaphore;
    private final Queue<UserTask> taskQueue;
    private int activeUsers;
    private int[] usersFinishedTasksCounter;
    private boolean[] usersSubmittedAllTasks;

    public Console(int activeUsers) {
        binarySemaphore = new Semaphore(1);
        taskQueue = new ArrayDeque<UserTask>();
        this.activeUsers = activeUsers;
        usersFinishedTasksCounter = new int[activeUsers];
        usersSubmittedAllTasks = new boolean[activeUsers];
    }

    public void getAcquiredBy(SystemProcess process) {
        try {
            binarySemaphore.acquire();

            while (!taskQueue.isEmpty()) {
                process.getTask(taskQueue.poll());
            }
            process.outputResults();

            boolean noNewTasks = true;
            for (int i = 0; i < activeUsers; ++i) {
                if (!usersSubmittedAllTasks[i]) {
                    noNewTasks = false;
                    break;
                }
            }
            process.setNoNewTasks(noNewTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            binarySemaphore.release();
        }
    }

    public void getAcquiredBy(User user) {
        try {
            binarySemaphore.acquire();

            usersSubmittedAllTasks[user.getId()] = user.submitTask();
            user.addFinishedTasks(usersFinishedTasksCounter[user.getId()]);
            usersFinishedTasksCounter[user.getId()] = 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            binarySemaphore.release();
        }
    }

    public void addTaskToQueue(UserTask task) {
        taskQueue.add(task);
    }

    public void addCompletedUserTask(int userId) {
        usersFinishedTasksCounter[userId]++;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public boolean[] getUsersSubmittedAllTasks() {
        return usersSubmittedAllTasks;
    }
}
