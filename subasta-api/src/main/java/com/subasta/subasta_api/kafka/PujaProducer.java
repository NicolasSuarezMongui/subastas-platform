package com.subasta.subasta_api.kafka;

import com.subasta.subasta_api.event.PujaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PujaProducer {
    private static final String TOPIC = "puja-events";

    private final KafkaTemplate<String, PujaEvent> kafkaTemplate;

    public void publicar(PujaEvent event) {
        kafkaTemplate.send(TOPIC, event.productoId().toString(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Error publicando puja en Kafka: {}", ex.getMessage());
                } else {
                    log.info("Puja publicada en Kafka - topic: {}, offset: {}",
                        TOPIC, result.getRecordMetadata().offset()
                    );
                }
            });
    }
}
