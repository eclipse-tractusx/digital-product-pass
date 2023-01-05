package tools;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public final class systemTools {
    private systemTools() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }
    public static Long getMemoryUsage(){
        return systemTools.getTotalMemory() - systemTools.getFreeMemory();
    }
    public static Long getTotalMemory(){
        return Runtime.getRuntime().totalMemory();
    }
    public static Long getPid(){
        return ProcessHandle.current().pid();
    }
    public static Long getFreeMemory(){
        return Runtime.getRuntime().freeMemory();
    }

    public static String getCommitedMemory(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return String.format("%.2f",(double)memoryMXBean.getHeapMemoryUsage().getCommitted()/1073741824);
    }
    public static String getInitialMemory(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return String.format("%.2f",(double)memoryMXBean.getHeapMemoryUsage().getInit()/1073741824);
    }
    public static String getUsedHeapMemory(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return String.format("%.2f",(double)memoryMXBean.getHeapMemoryUsage().getUsed()/1073741824);
    }
    public static String getMaxHeapMemory(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return String.format("%.2f",(double)memoryMXBean.getHeapMemoryUsage().getMax()/1073741824);
    }

    public Long getDiskUsage(){
        String path = fileTools.getWorkdirPath();
        File diskPartition = new File(path);
        return diskPartition.getTotalSpace() - diskPartition.getFreeSpace();
    }
}
