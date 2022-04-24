package com.batch.btcBatch.jobconfig;

import com.batch.btcBatch.domain.BtcDto;
import com.batch.btcBatch.domain.BtcFieldSetMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
public class JobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;
//will read the data from the flat file. We are using a FlatFileItemReaderBuilder to create a FlatFileItemReader of type<>
    @Bean
    public FlatFileItemReader<BtcDto> btcDtoItemReader() {
        FlatFileItemReader<BtcDto> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource("/data/btcdata.csv"));

        DefaultLineMapper<BtcDto> dayLineMapper = new DefaultLineMapper<>();
//This builds a delimited tokenizer.
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//will show the order of fields in the file.
        tokenizer.setNames("unix_timestamp", "datetime", "open", "high", "low", "close", "volume_btc", "volume_currency", "weighted_price");
//is an interface to map lines from file to domain object.
        dayLineMapper.setLineTokenizer(tokenizer);
//will map the data from fieldset to an object.
        dayLineMapper.setFieldSetMapper(new BtcFieldSetMapper());
        dayLineMapper.afterPropertiesSet();
        reader.setLineMapper(dayLineMapper);
        return reader;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public JdbcBatchItemWriter<BtcDto> btcDtoItemWriter() {
        JdbcBatchItemWriter<BtcDto> itemWriter = new JdbcBatchItemWriter<>();

        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO btcdata  VALUES (:unix_timestamp, :datetime, :open, :high, :low, :close, :volume_btc, :volume_currency, :weighted_price)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<BtcDto, BtcDto>chunk(10)
                .reader(btcDtoItemReader())
                .writer(btcDtoItemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .build();
    }




}
