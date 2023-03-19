package com.acme.util;

public class IdGenerator {

    private static final Snowflake SNOWFLAKE = new Snowflake();

    public static long randomId() {
        return SNOWFLAKE.nextId();
    }

    private IdGenerator() {}
}
