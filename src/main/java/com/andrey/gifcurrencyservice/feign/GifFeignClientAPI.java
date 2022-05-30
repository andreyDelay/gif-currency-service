package com.andrey.gifcurrencyservice.feign;

import com.andrey.gifcurrencyservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(value = "gif-service", url = "${feign-client.gif-api.url}", configuration = FeignConfig.class)
public interface GifFeignClientAPI {

    @GetMapping
    Optional<String> getGif(@RequestParam("api_key") String key,
                            @RequestParam("q") String searchQueryParam,
                            @RequestParam("limit") int gifsSearchLimit);
}
