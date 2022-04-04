package midorum.nw4j;

import com.sun.jna.Platform;
import midorum.nw4j.exception.NwRuntimeException;
import midorum.nw4j.exception.UnsupportedPlatformException;
import midorum.nw4j.linux.LinuxSystemFactory;
import midorum.nw4j.win32.Win32System;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class NwPlatform {

    private Supplier<NwSystem> system = this::obtainAndCacheSystem;

    private synchronized NwSystem obtainAndCacheSystem() {
        class Factory implements Supplier<NwSystem> {
            private final NwSystem system;

            Factory() {
                this.system = switch (Platform.getOSType()) {
                    case Platform.LINUX -> LinuxSystemFactory.getSystem();
                    case Platform.WINDOWS -> new Win32System();
                    default -> throw new UnsupportedPlatformException("Platform does not supported");
                };
            }

            @Override
            public NwSystem get() {
                return this.system;
            }
        }

        if (!(system instanceof Factory)) {
            system = new Factory();
        }

        return system.get();
    }

    public void useSystem(final Consumer<NwSystem> consumer) {
        final NwSystem system = this.system.get();
        NwRuntimeException error = null;
        try {
            consumer.accept(system);
        } catch (Throwable t) {
            error = new NwRuntimeException(t.getMessage(), t);
            throw error;
        } finally {
            try {
                system.close();
            } catch (Exception e) {
                if (error == null) {
                    error = new NwRuntimeException(e.getMessage(), e);
                } else {
                    error.addSuppressed(e);
                }
            }
            if (error != null) throw error;
        }
    }

    public String getArchModel() {
        return System.getProperty("sun.arch.data.model");
    }

    public String getOsName() {
        return System.getProperty("os.name");
    }

}
