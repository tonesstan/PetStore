package ApiTest;

import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Main {
    public static void main(String[] args) {
        System.out.println(ZonedDateTime.now().toLocalDateTime().truncatedTo(SECONDS));
    }
}