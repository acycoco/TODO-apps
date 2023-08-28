package com.example.todo.api.Payment;

import com.example.todo.domain.Response;
import com.example.todo.dto.PaymentInfoDto;
import com.example.todo.dto.PaymentResponseDto;
import com.example.todo.exception.TodoAppException;
import com.example.todo.service.payment.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/team/{teamId}/subscription/{teamSubscriptionId}/payment")
@RequiredArgsConstructor
public class PaymentApiController {
    private final PaymentService paymentService;

    @PostMapping("/process")
    public Response<PaymentResponseDto> process(
            @RequestBody PaymentInfoDto paymentInfoDto, Authentication authentication
    ){
        return  Response.success(paymentService.processPayment(paymentInfoDto, authentication));
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
        } catch (TodoAppException ex) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "결제 검증 실패: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }




}
