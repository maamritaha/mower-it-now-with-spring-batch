package com.home.mower.writer;

import com.home.mower.model.Mower;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

@Slf4j
public class MowerWriter extends FlatFileItemWriter<Mower> {

    public MowerWriter(FileSystemResource resource) {
        setResource(resource);
        setLineAggregator(createMowerLineAggregator());
    }

    private DelimitedLineAggregator<Mower> createMowerLineAggregator() {
        DelimitedLineAggregator<Mower> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(" ");
        lineAggregator.setFieldExtractor(mower -> new Object[]{mower.getPositionX(), mower.getPositionY(), mower.getOrientation()});
        return lineAggregator;
    }

}

