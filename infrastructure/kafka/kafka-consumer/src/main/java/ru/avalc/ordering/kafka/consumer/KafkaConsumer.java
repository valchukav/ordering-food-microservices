package ru.avalc.ordering.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

/**
 * @author Alexei Valchuk, 11.09.2023, email: a.valchukav@gmail.com
 */

public interface KafkaConsumer<T extends SpecificRecordBase> {

    void receive(List<T> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);
}
