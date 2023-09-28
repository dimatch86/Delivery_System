package ru.skillbox.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.dto.StatusDto;

@UtilityClass
public class RestUtil {

    public final String BASE_CHANGE_ORDER_STATUS_URL = "http://localhost:9090/api/order/";

    public void createRequestForOrderStatusUpdating(RestTemplate restTemplate, StatusDto statusDto, long orderId, String userToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, userToken);
        HttpEntity<StatusDto> request = new HttpEntity<>(statusDto, headers);
        restTemplate.exchange(RestUtil.BASE_CHANGE_ORDER_STATUS_URL + orderId, HttpMethod.PATCH, request, StatusDto.class);
    }
}
