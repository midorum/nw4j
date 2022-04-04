package midorum.nw4j.linux.x11;

import midorum.nw4j.NwWindow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class X11SystemTestDev {

    @Test
    public void listWindows() throws Exception {
        try (X11System system = new X11System()) {
            system.listWindows().forEach(w -> {
                System.out.println(w.getProcessId());
                System.out.println(w.getTitle());
                System.out.println(w.getClassName());
                System.out.println(w.getWindowRectangle());
                System.out.println();
            });
        }
    }

    @Test
    public void findWindow() throws Exception {
        try (X11System system = new X11System()) {
            system.listWindows().stream()
                    .filter(w -> w.getTitle().filter(s -> s.contains("win32api")).isPresent())//"nw4j"
                    .findFirst()
                    .ifPresent(w -> {
                        System.out.println(w.getProcessId());
                        System.out.println(w.getTitle());
                        System.out.println(w.getClassName());
                        System.out.println(w.getWindowRectangle());
                    });
        }
    }

    @Test
    public void findAndCloseWindow() throws Exception {
        try (X11System system = new X11System()) {
            system.listWindows().stream()
                    .filter(w -> w.getTitle().filter(s -> s.contains("nw4j")).isPresent())
                    .findFirst()
                    .ifPresent(NwWindow::close);
        }
    }

    @Test
    public void getFocusedWindow() throws Exception {
        try (X11System system = new X11System()) {
            final Optional<NwWindow> focusedWindow = system.getFocusedWindow();
            Assertions.assertTrue(focusedWindow.isPresent());
            focusedWindow.ifPresent(w -> System.out.println("focusedWindow: " + w.getTitle()));
        }
    }

}