package com.neero.ticketing_service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TicketNumberGenerator {

    @Value("${app.ticketing.ticket.number-prefix:TKT}")
    private String prefix;

    private final AtomicLong counter = new AtomicLong(0);
    private final Random random = new Random();

    public String generate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);

        long count = counter.incrementAndGet();
        int randomNum = random.nextInt(1000);

        return String.format("%s-%s-%04d-%03d", prefix, timestamp, count % 10000, randomNum);
    }

    public String generateWithCategory(String category) {
        String categoryCode = category.substring(0, Math.min(category.length(), 3)).toUpperCase();
        return categoryCode + "-" + generate();
    }
}
