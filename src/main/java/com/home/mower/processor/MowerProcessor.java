package com.home.mower.processor;

import com.home.mower.model.Mower;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;

@Slf4j
public class MowerProcessor implements ItemProcessor<Mower, Mower>, ItemStream {
    private ExecutionContext executionContext;

    @Override
    public Mower process(Mower item) throws Exception {
        log.info("processing : " + item);
        item.executeCommand(executionContext.getInt("width"), executionContext.getInt("height"));
        return item;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        ItemStream.super.open(executionContext);
        this.executionContext = executionContext;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        ItemStream.super.update(executionContext);
        this.executionContext = executionContext;
    }
}
