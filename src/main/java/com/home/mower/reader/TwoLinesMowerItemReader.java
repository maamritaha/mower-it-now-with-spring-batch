package com.home.mower.reader;

import com.home.mower.model.Mower;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.lang.Nullable;

@Slf4j
public class TwoLinesMowerItemReader implements ItemReader<Mower>, ItemStream {
    private FlatFileItemReader<FieldSet> delegate;
    private boolean isFirstLine = true;
    private ExecutionContext executionContext;

    @Nullable
    @Override
    public Mower read() throws Exception {
        Mower mower = null;

        for (FieldSet line; (line = this.delegate.read()) != null; ) {
            if (isFirstLine) {
                log.info("reading map : " + line.readString(0));
                this.executionContext.putInt("width", Integer.valueOf(line.readString(0).split(" ")[0]));
                this.executionContext.putInt("height", Integer.valueOf(line.readString(0).split(" ")[1]));
                isFirstLine = false;
                continue;
            }
            if (NumberUtils.isParsable(line.readString(0).substring(0, 1))) {
                String[] parts = line.readString(0).split(" ");
                mower = new Mower(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), parts[2].charAt(0), null);
            } else {
                mower.setCommand(line.readString(0));
                return mower;
            }
        }
        return null;
    }

    public void setDelegate(FlatFileItemReader<FieldSet> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() throws ItemStreamException {
        this.delegate.close();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.delegate.open(executionContext);
        this.executionContext = executionContext;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        this.delegate.update(executionContext);
        this.executionContext = executionContext;
    }

}
