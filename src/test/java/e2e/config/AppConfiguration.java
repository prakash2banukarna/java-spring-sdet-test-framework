package e2e.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@ComponentScan(basePackages = "e2e")         // ← scans ALL e2e.* packages for @Component beans
@PropertySource("classpath:env-dev.properties")
public class AppConfiguration {

    // ── existing fields ──────────────────────────────────────────────────────

    @Value("${userName}")
    private String dbUserName;

    @Value("${operation}")
    private String operation;

    // ── Kafka fields ─────────────────────────────────────────────────────────

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.topic.name}")
    private String kafkaTopicName;

    @Value("${kafka.consumer.group.id}")
    private String consumerGroupId;

    // ── existing getters ─────────────────────────────────────────────────────

    public String getDbUserName()  { return dbUserName; }
    public String getOpertation()  { return operation; }

    // ── Kafka getters ────────────────────────────────────────────────────────

    public String getBootstrapServers() { return bootstrapServers; }
    public String getKafkaTopicName()   { return kafkaTopicName; }
    public String getConsumerGroupId()  { return consumerGroupId; }

    // ── Producer beans ───────────────────────────────────────────────────────

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,      bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,   StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG,    "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // ── Consumer beans ───────────────────────────────────────────────────────

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,        bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,                 consumerGroupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,        "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}