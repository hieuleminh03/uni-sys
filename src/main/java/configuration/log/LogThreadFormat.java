package configuration.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogThreadFormat extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        String threadName = event.getThreadName();

        if (threadName.contains("-")) {
            int index = threadName.lastIndexOf('-') + 1;
            if (!(threadName.length() - index < 3)) {
                threadName = threadName.substring(index);
            }
        }

        if (threadName.length() < 17) {
            int space = (17 - threadName.length()) / 2;
            return " ".repeat(space).concat(threadName).concat(" ".repeat(17 - space - threadName.length()));
        }

        return threadName.substring(0, 17);
    }
}
