package com.dreambuilders.springbatch.config;

import com.dreambuilders.springbatch.model.COVIDReport;
import lombok.Value;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<COVIDReport> itemReader,
                   ItemProcessor<COVIDReport,COVIDReport> itemProcessor,
                   ItemWriter<COVIDReport> itemWriter){
        Step step = stepBuilderFactory.get("COVID-PAYLOAD")
                .<COVIDReport, COVIDReport>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
        return jobBuilderFactory.get("MY-PAYLOAD")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public ItemReader<COVIDReport> itemReader() {
        COVIDReport covidReport = new COVIDReport();
        covidReport.setCountry("IND");
        covidReport.setDate(LocalDateTime.now());
        covidReport.setNewConfirmed(11L);
        covidReport.setNewDeath(5L);
        covidReport.setTotalConfirmed(50L);
        covidReport.setNewRecovered(10L);
        ItemReader<COVIDReport> itemReaderCovid = () -> {return covidReport;};
        return itemReaderCovid;
    }
}
