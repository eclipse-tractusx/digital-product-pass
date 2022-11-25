package tools;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class threadTools {

    public static void runTask(Runnable runnable){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(runnable);
    }
    public static Thread runThread(Runnable runnable){
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }
}
