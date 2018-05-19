package io.github.jordieh.boosters.common;

import io.github.jordieh.boosters.BoosterPlugin;

import java.util.logging.Level;

// FIXME ?
public final class Logger {

    private static final java.util.logging.Logger logger = BoosterPlugin.getInstance().getLogger();

    public static void info(String s) {
        logger.info(s);
    }

    public static void warn(String s, Throwable throwable) {
        logger.log(Level.WARNING, s, throwable);
    }

}
