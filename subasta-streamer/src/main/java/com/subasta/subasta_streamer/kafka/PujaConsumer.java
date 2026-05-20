package com.subasta.subasta_streamer.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subasta.subasta_streamer.event.PujaEvent;
import com.subasta.subasta_streamer.websocket.PujaWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PujaConsumer {
    
    private final PujaWebSocketHandler webSocketHandler;
    private final ObjectMapper kafkaObjectMapper;

    @KafkaListener(topics = "puja-events", groupId = "streamer-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumir(PujaEvent event) {
        log.info("Evento recibido - producto: {}, monto: {}", event.productoId(), event.monto());
        try {
            String json = kafkaObjectMapper.writeValueAsString(event);
            webSocketHandler.broadcast(json);
        } catch (Exception e) {
            log.error("Error al procesar evento: {}", e.getMessage());
        }
    }

}
