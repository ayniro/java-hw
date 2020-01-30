package ru.spbstu.telematics.korobov.lab03;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

public class SystemTest
{
    @Test
    public void systemTest() {
        OperatingSystem operatingSystem = new OperatingSystem(400);
        operatingSystem.start();
        operatingSystem.shutdown();

        Console console = operatingSystem.getConsole();
        boolean[] usersSubmittedAllTasks = console.getUsersSubmittedAllTasks();
        SystemProcess process = operatingSystem.getSystemProcess();
        User[] users = operatingSystem.getUsers();
        int finishedTasks = 0;

        for (int i = 0; i < usersSubmittedAllTasks.length; ++i) {
            Assert.assertTrue(usersSubmittedAllTasks[i]);
        }
        Assert.assertTrue(process.noNewTasks());
        for (int i = 0; i < users.length; ++i) {
            Assert.assertEquals(users[i].getInitialTasksCount(), users[i].getFinishedTasksCount());
            finishedTasks += users[i].getFinishedTasksCount();
        }
        Assert.assertEquals(finishedTasks, process.getFinishedTasks());
    }
}
