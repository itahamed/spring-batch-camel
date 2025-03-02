package com.batch.camel;

import org.apache.camel.ProducerTemplate;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CamelItemWriter<T> implements ItemWriter<T> {

    private final ProducerTemplate producerTemplate;
    private final String endpointUri;

    public CamelItemWriter(ProducerTemplate producerTemplate, 
                          @Value("${camel.endpoint.fixed-length-file}") String endpointUri) {
        this.producerTemplate = producerTemplate;
        this.endpointUri = endpointUri;
    }

    @Override
    public void write(Chunk<? extends T> items) throws Exception {
        for (T item : items) {
            producerTemplate.sendBody(endpointUri, item);
        }
    }
}