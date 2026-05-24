package utils;

import com.github.javafaker.Faker;
import java.util.UUID;

public final class DynamicDataGenerator {
    private static final Faker FAKER = new Faker();
    private DynamicDataGenerator() {}

    public static String uniqueId()  { return UUID.randomUUID().toString(); }
    public static String firstName() { return FAKER.name().firstName(); }
    public static String lastName()  { return FAKER.name().lastName(); }
    public static String username()  { return "user_" + System.currentTimeMillis(); }
    public static String email()     { return "qa_" + System.currentTimeMillis() + "@test.io"; }
    public static int    age()       { return FAKER.number().numberBetween(18, 65); }
    public static long   timestamp() { return System.currentTimeMillis(); }
}