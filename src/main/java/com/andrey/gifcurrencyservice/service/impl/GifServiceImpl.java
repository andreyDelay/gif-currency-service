package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.feign.GifFeignClientAPI;
import com.andrey.gifcurrencyservice.service.GifService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GifServiceImpl implements GifService {

	private final GifFeignClientAPI gifFeignClientAPI;


}
