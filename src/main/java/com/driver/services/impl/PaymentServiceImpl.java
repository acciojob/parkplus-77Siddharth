package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;


    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation foundReservation = reservationRepository2.findById(reservationId).get();
        int bill  = foundReservation.getSpot().getPricePerHour() * foundReservation.getNumberOfHours();
        PaymentMode payment_mode = null;
        if (bill<amountSent)
            throw new Exception("Insufficient Amount");
//        "cash", "card", or "upi"
        if (mode == "cash")
            payment_mode = PaymentMode.CASH;
        else if (mode == "card")
            payment_mode = PaymentMode.CARD;
        else if (mode == "upi")
            payment_mode = PaymentMode.UPI;
        else
            throw new Exception("Payment mode not detected");
        Payment newPayment = new Payment(true,payment_mode);
        newPayment.setReservation(foundReservation);
        paymentRepository2.save(newPayment);
        return newPayment;
    }
}
