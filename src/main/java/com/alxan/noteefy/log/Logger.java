package com.alxan.noteefy.log;


public interface Logger {
    void error(String log);

    void trace(String log);

    void warn(String log);

    void debug(String log);

    void info(String log);
}
