package com.dreambuilders.springbatch.config;

import com.dreambuilders.springbatch.model.COVIDReport;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class SpringBatchConfig {

    @Autowired
    JobRepository jobRepository;

    // To run job async
    @Bean
    public JobLauncher myJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    /** Job conf - buld
     *
     * @param jobBuilderFactory
     * @param stepBuilderFactory
     * @param itemReader
     * @param itemProcessor
     * @param itemWriter
     * @return
     */
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<COVIDReport> itemReader,
                   ItemProcessor<COVIDReport,COVIDReport> itemProcessor,
                   ItemWriter<COVIDReport> itemWriter){
        Step step = stepBuilderFactory.get("COVID-STEP")
                .<COVIDReport, COVIDReport>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
        return jobBuilderFactory.get("COVID-JOB")
                .incrementer(new RunIdIncrementer())
                //.flow(step1)
                //.next(step2)
                //.end()
                .start(step)
                .build();
    }


    @Bean
    public ItemReader<COVIDReport> itemReader() {
//        COVIDReport covidReports = getCovidReport();
//        ItemReader<COVIDReport> itemReaderCovid = () -> {return covidReports;};
        FlatFileItemReader<COVIDReport> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/covid.csv"));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

//    private COVIDReport getCovidReport() {
//        List<COVIDReport> covidReports = new ArrayList<>();
//        COVIDReport covidReport = new COVIDReport();
//        covidReport.setCountry("IND");
//        covidReport.setDate(LocalDateTime.now());
//        covidReport.setNewConfirmed(11L);
//        covidReport.setNewDeath(5L);
//        covidReport.setTotalConfirmed(50L);
//        covidReport.setNewRecovered(10L);
//        covidReports.add(covidReport);
//        return covidReport;
//    }

    @Bean
    public LineMapper<COVIDReport> lineMapper() {

        DefaultLineMapper<COVIDReport> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"country", "newConfirmed", "totalConfirmed","newDeath","newRecovered","totalRecovered"});

        BeanWrapperFieldSetMapper<COVIDReport> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(COVIDReport.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }
}
