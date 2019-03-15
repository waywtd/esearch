package org.apache.flume.serializer;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sicnlee on 17/11/07 上午09:40.
 */
public class FlumeAvroSerializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlumeAvroSerializer.class);

    public static AvroFlumeEvent deSerializer(byte[] bytes) {
        try {

            ByteArrayInputStream in = new ByteArrayInputStream(bytes);

            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(in, null);

            SpecificDatumReader<AvroFlumeEvent> reader = new SpecificDatumReader<AvroFlumeEvent>(AvroFlumeEvent.class);

            return reader.read(null, decoder);

        } catch (Throwable e) {
            LOGGER.error("deSerializer Error,value=" + new String(bytes));
            LOGGER.error("deSerializer Error.",e);
            throw new RuntimeException(e.getMessage());
        }

    }

    public static byte[] serializer(AvroFlumeEvent event) {

        try {
            ByteArrayOutputStream tempOutStream = new ByteArrayOutputStream();

            final SpecificDatumWriter<AvroFlumeEvent> writer = new SpecificDatumWriter<AvroFlumeEvent>(AvroFlumeEvent.class);

            BinaryEncoder encoder = EncoderFactory.get().directBinaryEncoder(tempOutStream, null);

            writer.write(event, encoder);
            encoder.flush();

            return tempOutStream.toByteArray();
        }catch (Throwable e){
            LOGGER.error("serializer Error.",e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    private static Map<CharSequence, CharSequence> toCharSeqMap(Map<String, String> stringMap) {
        Map<CharSequence, CharSequence> charSeqMap = new HashMap<CharSequence, CharSequence>();
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            charSeqMap.put(entry.getKey(), entry.getValue());
        }
        return charSeqMap;
    }
}
