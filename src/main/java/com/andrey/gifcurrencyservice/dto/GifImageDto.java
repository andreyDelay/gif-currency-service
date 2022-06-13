package com.andrey.gifcurrencyservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GifImageDto {
    private String gifFormatURL;
    private String mp4FormatURL;
    private String webpFormatURL;
}
