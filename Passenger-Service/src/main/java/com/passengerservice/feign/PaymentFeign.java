package com.passengerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.passengerservice.payment.ProductRequest;
import com.passengerservice.payment.StripeResponse;


@FeignClient(name = "stripe-payment",url = "http://localhost:8085")
public interface PaymentFeign {
	  @PostMapping("/payment/payment-confirmation")
	    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody ProductRequest productRequest);
}
