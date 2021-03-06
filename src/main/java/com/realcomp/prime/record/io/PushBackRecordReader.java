package com.realcomp.prime.record.io;

import com.realcomp.prime.conversion.ConversionException;
import com.realcomp.prime.record.Record;
import com.realcomp.prime.schema.SchemaException;
import com.realcomp.prime.validation.ValidationException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Wraps a RecordReader to provide a push-back feature to un-read Records.
 *
 */
public class PushBackRecordReader implements RecordReader{

    private RecordReader reader;
    private Queue<Record> queue;

    public PushBackRecordReader(RecordReader reader){
        queue = new LinkedList<Record>();
        this.reader = reader;
    }

    public void pushBack(Record record){
        queue.add(record);
    }

    @Override
    public long getCount(){
        return reader.getCount() - queue.size();
    }

    @Override
    public Record read() throws IOException, ValidationException, ConversionException, SchemaException{
        return queue.isEmpty() ? reader.read() : queue.remove();
    }

    @Override
    public void close() throws IOException{
        reader.close();
    }

    @Override
    public void open(IOContext context) throws IOException, SchemaException{
        reader.open(context);
    }

    @Override
    public IOContext getIOContext(){
        return reader.getIOContext();
    }

    @Override
    public void close(boolean closeIOContext) throws IOException{
        reader.close(closeIOContext);
    }

    @Override
    public Map<String, String> getDefaults(){
        return reader.getDefaults();
    }
}
