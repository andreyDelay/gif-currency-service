package com.andrey.gifcurrencyservice.feign;

import com.andrey.gifcurrencyservice.config.FeignConfig;
import com.andrey.gifcurrencyservice.model.ApiRates;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(value = "currency-rate-service", url = "${feign-client.currency-api.url}", configuration = FeignConfig.class)
public interface CurrencyFeignClientAPI {

	@Cacheable(value = "latestRates")
	@GetMapping("/{latest}")
	Optional<ApiRates> getRates(@PathVariable String latest,
	                            @RequestParam("app_id") String appId, @RequestParam("base") String base);

	@Cacheable(value = "ratesForDate")
	@GetMapping("/{historical}/{specifiedDate}")
	Optional<ApiRates> getRatesForSpecifiedData(@PathVariable String historical, @PathVariable String specifiedDate,
	                                            @RequestParam("app_id") String appId,
												@RequestParam("base") String base);
}
