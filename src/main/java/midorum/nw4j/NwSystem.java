package midorum.nw4j;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface NwSystem extends AutoCloseable {

    List<NwWindow> listWindows();

    /**
     * Window with witch user currently working (window that has user focus).
     * @return optional window
     */
    Optional<NwWindow> getFocusedWindow();

    List<NwProcess> listProcesses();

    List<NwThread> listThreads();

    default Map<Integer, List<NwThread>> getThreadsGroupedByProcessId() {
        return listThreads().stream()
                .collect(Collectors.groupingBy(NwThread::getOwnerProcessId));
    }

    int getNumberOfProcessors();

    long getSystemTime();

    Rectangle getScreenResolution();

}

