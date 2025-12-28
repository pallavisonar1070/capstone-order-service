package com.concepts.core.services;

import com.concepts.core.models.Order;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private int counter = 0;

    public void processPayment(Order order) {
        sleep(2000);
        counter++;

        // Fail every 2nd request
        if (counter % 2 == 0) {
            throw new RuntimeException("Simulated payment failure");
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
