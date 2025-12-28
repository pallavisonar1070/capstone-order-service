package com.concepts.core.services;

import com.concepts.core.models.Order;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public void processPayment(Order order){
        try{
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            if(Math.random() < 0.3 ){
                throw new RuntimeException("Payment Failed");
            }
        }
    }
}
