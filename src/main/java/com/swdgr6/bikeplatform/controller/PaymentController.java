package com.swdgr6.bikeplatform.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.swdgr6.bikeplatform.model.payload.dto.PaymentResDTO;
import com.swdgr6.bikeplatform.model.payload.dto.PaypalDTO;
import com.swdgr6.bikeplatform.model.payload.responeModel.ResponseObject;
import com.swdgr6.bikeplatform.service.UserService;
import com.swdgr6.bikeplatform.service.impl.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;


@RestController
@RequestMapping("/no-auth/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    private final UserService userService;
    @GetMapping("/vn-pay")
    public ResponseObject<PaymentResDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }
    @GetMapping("/vn-pay-callback")
    public ResponseObject<PaymentResDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return new ResponseObject<>(HttpStatus.OK, "Success", new PaymentResDTO.VNPayResponse("00", "Success", "https://www.google.com.vn/"));
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
        }
    }

    @PostMapping("/paypal")
    public ResponseEntity<?> payment(@RequestHeader("Authorization") String jwt, @RequestBody PaypalDTO pay, HttpServletResponse response) {
        try {
            System.out.println("request jwt: " + jwt);
            Payment payment = paymentService.createPayment(pay.getPrice(), pay.getContent());
            // Store the JWT token associated with the payment ID
            String paymentId = payment.getId();
            userService.storeJwtToken(paymentId, jwt);
            String approvalUrl = payment.getLinks().stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst()
                    .map(Links::getHref)
                    .orElse(null);

            if (approvalUrl != null) {
                return ResponseEntity.ok(approvalUrl);
            } else {
                return ResponseEntity.ok(payment);
            }
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public RedirectView cancelPay() {
        return new RedirectView("http://localhost:8080/payment?success=false");
    }

    @GetMapping("/paypal-success")
    public ResponseEntity<?> successPay(@RequestParam("paymentId") String paymentId,
                                        @RequestParam("PayerID") String payerId,
                                        HttpServletRequest request) {
        try {
            // Retrieve the JWT token associated with the payment ID
            String jwt = userService.retrieveJwtToken(paymentId);
            System.out.println("success jwt: " + jwt);
            Payment payment = paymentService.executePayment(paymentId, payerId);
            // Update the user's balance
            BigDecimal amount = new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal());
            return ResponseEntity.ok("Payment executed successfully.");
        } catch (PayPalRESTException e) {
            return ResponseEntity.ok("Payment executed failed for some reason.");
        }
    }
}
