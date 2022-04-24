package com.batch.btcBatch.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BtcDto {
    private String unix_timestamp;
    private String datetime;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume_btc;
    private String volume_currency;
    private String weighted_price;

}
