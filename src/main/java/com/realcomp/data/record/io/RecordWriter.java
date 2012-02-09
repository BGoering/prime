package com.realcomp.data.record.io;

import com.realcomp.data.conversion.ConversionException;
import com.realcomp.data.record.Record;
import com.realcomp.data.schema.FileSchema;
import com.realcomp.data.schema.SchemaException;
import com.realcomp.data.validation.Severity;
import com.realcomp.data.validation.ValidationException;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Writes records to an output stream.
 * Any Classifier(s) in a schema are ignored.
 *
 * Note:
 * Implementors should ensure that their RecordWriter has a default empty constructor.
 *
 * @author krenfro
 */
public interface RecordWriter {

    /**
     * If a Validator logs a warning at or above this threshold, then the warning is thrown as
     * a ValidationException. By default, Validators log at Severity.MEDIUM, so all
     * validation failures are simply logged.  This mechanism, along with adjustable
     * Validator severity, allow for configurations where some validations are more
     * important than others.
     */
    public static final Severity DEFAULT_VALIDATION_THREASHOLD = Severity.HIGH;
    
   /**
     * write a Record
     * @param record the Record to write; not null
     * @throws IOException
     */
    void write(Record record)
            throws IOException, ValidationException, ConversionException, SchemaException;


    /**
     * Close open resources. Should be invoked when you are done with the RecordReader.
     */
    void close();
    
    /**
     * Close open resources, optionally closing the original output stream passed to open().
     * @param closeAll 
     */
    void close(boolean closeAll);

    /**
     * Open an IOContext for writing. May be invoked multiple times with new output as needed.
     * close() is automatically invoked before each open();
     *
     * @param in OutputStream to write to. Not null
     * @throws IOException
     * @throws SchemaException
     */
    void open(IOContext context) throws IOException, SchemaException;
    
    /**
     *
     * @return number of records read; not including skipped records.
     */
    long getCount();
    
    
    /**
     * @return the IOContext being operated on; or null if not yet open
     */
    IOContext getIOContext();
    
}
