package com.ewallet.ewallet.data.mapper;

import com.ewallet.ewallet.data.dto.response.PaymentRequestDto;
import com.ewallet.ewallet.models.PaymentRequest;
import com.ewallet.ewallet.payment.request.PaymentRequestData;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentRequestMapper {

    @Mapping(source = "partnerAvatar", target = "partner.avatar")
    @Mapping(source = "partnerPartnerType", target = "partner.partnerType")
    @Mapping(source = "partnerEmail", target = "partner.email")
    @Mapping(source = "partnerName", target = "partner.name")
    @Mapping(source = "partnerId", target = "partner.id")
    PaymentRequest toEntity(PaymentRequestDto paymentRequestDto);

    @Mapping(source = "partner.avatar", target = "partnerAvatar")
    @Mapping(source = "partner.partnerType", target = "partnerPartnerType")
    @Mapping(source = "partner.email", target = "partnerEmail")
    @Mapping(source = "partner.name", target = "partnerName")
    @Mapping(source = "partner.id", target = "partnerId")
    PaymentRequestDto toDto(PaymentRequest paymentRequest);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PaymentRequest partialUpdate(
            PaymentRequestDto paymentRequestDto,
            @MappingTarget PaymentRequest paymentRequest);

    @Mapping(target = "successUrl", source = "success")
    PaymentRequest toEntity(PaymentRequestData paymentRequestData);
}