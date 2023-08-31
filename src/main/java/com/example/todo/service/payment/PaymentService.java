package com.example.todo.service.payment;

import com.example.todo.domain.entity.PaymentEntity;
import com.example.todo.domain.entity.TeamActiveSubscriptionEntity;
import com.example.todo.domain.entity.TeamSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.PaymentRepository;
import com.example.todo.domain.repository.TeamActiveSubscriptionRepository;
import com.example.todo.domain.repository.TeamSubscriptionRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.PaymentInfoDto;
import com.example.todo.dto.PaymentResponseDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final TeamSubscriptionRepository teamSubscriptionRepository;
    private final UserRepository userRepository;
    private final TeamActiveSubscriptionRepository teamActiveSubscriptionRepository;
    private final IamportClient iamportClient;

    public PaymentService(PaymentRepository paymentRepository, TeamSubscriptionRepository teamSubscriptionRepository, UserRepository userRepository, TeamActiveSubscriptionRepository teamActiveSubscriptionRepository, IamportClient iamportClient) {
        this.paymentRepository = paymentRepository;
        this.teamSubscriptionRepository = teamSubscriptionRepository;
        this.userRepository = userRepository;
        this.teamActiveSubscriptionRepository = teamActiveSubscriptionRepository;
        this.iamportClient = iamportClient;
    }



    @Transactional
    public PaymentResponseDto processPayment(PaymentInfoDto paymentInfo, Authentication authentication){
        TeamSubscriptionEntity teamSubscription = teamSubscriptionRepository.findByMerchantUid(paymentInfo.getMerchantUid())
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM_SUBSCRIPTION));

        teamSubscription.changeSubscriptionStatus(SubscriptionStatus.ACTIVE);

        User user = userRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));

        PaymentEntity payment = PaymentEntity.builder()
                .impUid(paymentInfo.getImpUid())
                .amount(paymentInfo.getPaidAmount())
                .paymentDate(paymentInfo.convertFromUnixTimestamp(paymentInfo.getPaidAt()))
                .merchantUid(paymentInfo.getMerchantUid())
                .teamSubscription(teamSubscription)
                .payMethod(paymentInfo.getPayMethod())
                .payStatus(paymentInfo.getPayStatus())
                .user(user)
                .build();

        teamSubscriptionRepository.save(teamSubscription);


        //team_active_subscription에 활성화 중인 subscription 저장
        TeamActiveSubscriptionEntity teamActiveSubscription = TeamActiveSubscriptionEntity.builder()
                .team(teamSubscription.getTeam())
                .teamSubscription(teamSubscription)
                .build();

        teamActiveSubscriptionRepository.save(teamActiveSubscription);


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

        TeamSubscriptionEntity teamSubscription = teamSubscriptionRepository.findByMerchantUid(iamportResponse.getResponse().getMerchantUid())
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM_SUBSCRIPTION));

        //실제 결제 금액과 db의 구독권 금액 비교
        if (!amount.equals(teamSubscription.getSubscriptionPrice())){
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
        IamportResponse<Payment> result = iamportClient.cancelPaymentByImpUid(cancelData); //이미 취소된 거래는 response가 null이다.

        if (result == null){
            throw new TodoAppException(ErrorCode.ALREADY_CANCELED);
        }
    }
}
