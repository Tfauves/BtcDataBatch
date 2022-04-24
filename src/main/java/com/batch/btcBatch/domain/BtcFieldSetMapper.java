package com.batch.btcBatch.domain;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class BtcFieldSetMapper implements FieldSetMapper<BtcDto> {
    @Override
    public BtcDto mapFieldSet(FieldSet fieldSet) throws BindException {
        return new BtcDto(fieldSet.readString("unix_timestamp"),
                fieldSet.readString("datetime"),
                fieldSet.readString("open"),
                fieldSet.readString("high"),
                fieldSet.readString("low"),
                fieldSet.readString("close"),
                fieldSet.readString("volume_btc"),
                fieldSet.readString("volume_currency"),
                fieldSet.readString("weighted_price"));
    }
}
