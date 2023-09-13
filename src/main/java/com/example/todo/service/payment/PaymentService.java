package com.example.todo.service.payment;

import com.example.todo.domain.entity.PaymentEntity;
import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.PaymentRepository;
import com.example.todo.domain.repository.UsersSubscriptionRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.payment.PaymentInfoDto;
import com.example.todo.dto.payment.PaymentResponseDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UsersSubscriptionRepository usersSubscriptionRepository;
    private final UserRepository userRepository;
    private final IamportClient iamportClient;


    @Transactional
    public PaymentResponseDto completePayment(PaymentInfoDto paymentInfo, Long userId){
        UsersSubscriptionEntity usersSubscription = usersSubscriptionRepository.findByMerchantUid(paymentInfo.getMerchantUid())
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USERS_SUBSCRIPTION));

        usersSubscription.changeSubscriptionStatus(SubscriptionStatus.ACTIVE);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));

        PaymentEntity payment = PaymentEntity.builder()
                .impUid(paymentInfo.getImpUid())
                .amount(paymentInfo.getPaidAmount())
                .merchantUid(paymentInfo.getMerchantUid())
                .payStatus(paymentInfo.getPayStatus())
                .payMethod(paymentInfo.getPayMethod())
                .paymentDate(paymentInfo.convertFromUnixTimestamp(paymentInfo.getPaidAt()))
                .user(user)
                .usersSubscription(usersSubscription)
                .build();

        usersSubscriptionRepository.save(usersSubscription);

        return PaymentResponseDto.fromEntity(paymentRepository.save(payment));
    }

    @Transactional
    //위변조 검증
    public void verifyIamport(String impUid, BigDecimal amount) throws IamportResponseException, IOException {
        //iamport 서버의 결제결과
        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);

        //iamport의 금액과 실제 결제 금액 비교
        if (!amount.equals(iamportResponse.getResponse().getAmount())) {
            throw new TodoAppException(ErrorCode.NOT_MATCH_AMOUNT);
        }

        log.info("{}  {}", impUid, iamportResponse.getResponse().getMerchantUid());

        UsersSubscriptionEntity usersSubscription = usersSubscriptionRepository.findByMerchantUid(iamportResponse.getResponse().getMerchantUid())
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USERS_SUBSCRIPTION));

        log.info("{}", usersSubscription);
        //실제 결제 금액과 db의 구독권 금액 비교
        if (!amount.equals(usersSubscription.getSubscriptionPrice())){
            throw new TodoAppException(ErrorCode.NOT_MATCH_AMOUNT);
        }

        if (!"paid".equals(iamportResponse.getResponse().getStatus())){
            throw new TodoAppException(ErrorCode.INVALID_PAYMENT_STATUS);
        }

    }

    @Transactional
    public void cancelPayment(String impUid, BigDecimal amount) throws IamportResponseException, IOException {
        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);
        if (!iamportResponse.getResponse().getAmount().equals(amount)){
            //환불 금액이 결제된 금액과 다름
            throw new TodoAppException(ErrorCode.NOT_MATCH_IAMPORT_CANCEL_AMOUNT);
        }
        CancelData cancelData = new CancelData(iamportResponse.getResponse().getImpUid(), true);
        cancelData.setChecksum(amount);
        IamportResponse<Payment> result = iamportClient.cancelPaymentByImpUid(cancelData);

        //이미 취소된 거래는 response가 null이다.
        if (result == null){
            throw new TodoAppException(ErrorCode.ALREADY_CANCELED);
        }
    }

    @Transactional
    public PaymentResponseDto readPayment(Long userId, Long usersSubscriptionId) {
        if (!userRepository.existsById(userId))
            throw new TodoAppException(ErrorCode.NOT_FOUND_USER);

        UsersSubscriptionEntity usersSubscription = usersSubscriptionRepository.findById(usersSubscriptionId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USERS_SUBSCRIPTION));

        if (!userId.equals(usersSubscription.getUsers().getId()))
            throw new TodoAppException(ErrorCode.NOT_MATCH_USERS_AND_USERS_SUBSCRIPTION);

        PaymentEntity payment = paymentRepository.findByUsersSubscription(usersSubscription)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_PAYMENT));

        return PaymentResponseDto.fromEntity(payment);
    }
}
