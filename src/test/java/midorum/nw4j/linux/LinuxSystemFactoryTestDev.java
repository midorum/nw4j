package midorum.nw4j.linux;

import midorum.nw4j.NwSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LinuxSystemFactoryTestDev {

    @Test
    public void getSystem() throws Exception {
        try (NwSystem system = LinuxSystemFactory.getSystem()) {
            Assertions.assertNotNull(system);
        }
    }

}