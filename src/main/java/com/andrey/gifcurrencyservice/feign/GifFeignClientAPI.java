package com.andrey.gifcurrencyservice.feign;

import com.andrey.gifcurrencyservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@FeignClient(value = "gif-service", configuration = FeignConfig.class)
public interface GifFeignClientAPI {

}
