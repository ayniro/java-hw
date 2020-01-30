package ru.spbstu.telematics.korobov.lab03;

public class UserTask {
    static private int idCounter = 0;
    private int completionTimeMs = 0;
    private int submitTimeMs = 0;
    private int outputTimeMs = 0;
    private int userId;
    private int id;

    public UserTask(int completionTimeMs, int submitTimeMs, int outputTimeMs, int userId) {
        this.completionTimeMs = completionTimeMs;
        this.submitTimeMs = submitTimeMs;
        this.outputTimeMs = outputTimeMs;
        this.userId = userId;
        id = getUniqueId();
    }

    public int getCompletionTimeMs() {
        return completionTimeMs;
    }

    public int getSubmitTimeMs() {
        return submitTimeMs;
    }

    public int getOutputTimeMs() {
        return outputTimeMs;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserTask{" +
                "id=" + id +
                '}';
    }

    synchronized static private int getUniqueId() {
        idCounter++;
        return idCounter;
    }
}
