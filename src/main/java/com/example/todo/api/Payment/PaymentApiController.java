package com.example.todo.api.Payment;

import com.example.todo.domain.Response;
import com.example.todo.dto.payment.PaymentInfoDto;
import com.example.todo.dto.payment.PaymentResponseDto;
import com.example.todo.exception.TodoAppException;
import com.example.todo.service.payment.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users-subscription/{usersSubscriptionId}/payment")
@RequiredArgsConstructor
public class PaymentApiController {
    private final PaymentService paymentService;

    @PostMapping("/complete")
    public Response<PaymentResponseDto> process(
            @RequestBody PaymentInfoDto paymentInfoDto, Authentication authentication
    ){
        Long userId = Long.parseLong(authentication.getName());
        return  Response.success(paymentService.completePayment(paymentInfoDto, userId));
    }

    @PostMapping("/verifyIamport")
    public ResponseEntity<Map<String, String>> verifyIamport(
            @RequestBody PaymentInfoDto paymentInfoDto
    ) throws IamportResponseException, IOException {
        try {
            paymentService.verifyIamport(paymentInfoDto.getImpUid(), paymentInfoDto.getPaidAmount());
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "결제 검증 완료");
            return ResponseEntity.ok(response);
        } catch (TodoAppException e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "결제 검증 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<Map<String, String>> cancel(
            @RequestBody Map<String, String> map
    ) throws IamportResponseException, IOException {
        try {

            paymentService.cancelPayment(map.get("impUid"), BigDecimal.valueOf(Integer.parseInt(map.get("amount"))));
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "결제 환불 완료");
            return ResponseEntity.ok(response);
        } catch (TodoAppException e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "결제 환불 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public Response<PaymentResponseDto> read(
            @PathVariable("usersSubscriptionId") Long usersSubscriptionId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(
                paymentService.readPayment(userId, usersSubscriptionId)
        );
    }



}
