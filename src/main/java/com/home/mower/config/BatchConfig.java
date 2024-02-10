package com.home.mower.config;

import com.home.mower.model.Mower;
import com.home.mower.processor.MowerProcessor;
import com.home.mower.reader.TwoLinesMowerItemReader;
import com.home.mower.writer.MowerWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class BatchConfig {
    @Bean
    public TwoLinesMowerItemReader itemReader(@Value("classpath:input/map") Resource resource) throws UnexpectedInputException {
        FlatFileItemReader<FieldSet> delegate = new FlatFileItemReaderBuilder<FieldSet>().name("delegateItemReader")
                .resource(resource)
                .lineTokenizer(new DelimitedLineTokenizer())
                .fieldSetMapper(new PassThroughFieldSetMapper())
                .build();
        TwoLinesMowerItemReader reader = new TwoLinesMowerItemReader();
        reader.setDelegate(delegate);
        return reader;
    }

    @Bean("MowerProcessor")
    public ItemProcessor<Mower, Mower> itemProcessor() {
        return new MowerProcessor();
    }

    @Bean
    public ItemWriter<Mower> itemWriter(@Value("${output.file.path}") String outputPath) {
        return new MowerWriter(new FileSystemResource(outputPath));

    }

    @Bean(name = "step1")
    protected Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                         TwoLinesMowerItemReader twoLinesMowerItemReader,
                         @Qualifier("MowerProcessor") ItemProcessor<Mower, Mower> processor,
                         ItemWriter<Mower> itemWriter) {
        return new StepBuilder("step1", jobRepository)
                .<Mower, Mower>chunk(2, transactionManager)
                .reader(twoLinesMowerItemReader)
                .processor(processor)
                .writer(itemWriter)
                .build();
    }

    @Bean(name = "mowerBatchJob")
    public Job job(@Qualifier("step1") Step step1, JobRepository jobRepository) {
        return new JobBuilder("mowerBatchJob", jobRepository).start(step1).build();
    }
}
