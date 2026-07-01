package de.sample.schulung.accounts.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.core.annotation.AliasFor;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Autoconfigures an {@link EmbeddedKafka}
 * and provides an extension to read the records from the topic.<br/>
 * We can get the following beans injected into our test class
 * <pre>
 * \u0040Autowired
 * EmbeddedKafkaBroker kafka;
 * </pre>
 * We can define the following parameters for our test methods:
 * <ul>
 *   <li>{@link Consumer}</li>
 *   <li>{@link RecordsSupplier}</li>
 * </ul>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EmbeddedKafka
@ExtendWith(AutoConfigureKafkaEmbedded.AutoConfigureKafkaEmbeddedExtension.class)
public @interface AutoConfigureKafkaEmbedded {

  @FunctionalInterface
  interface RecordsSupplier<K, V>
    extends Supplier<ConsumerRecords<K, V>> {
  }

  /**
   * @return partitions per topic
   */
  @AliasFor(
    annotation = EmbeddedKafka.class
  )
  int partitions() default 2;

  /**
   * Topics that should be created Topics may contain property place holders, e.g.
   * {@code topics = "${kafka.topic.one:topicOne}"} The topics will be created with
   * {@link #partitions()} partitions; to provision other topics with other partition
   * counts call the {@code addTopics(NewTopic... topics)} method on the autowired
   * broker.
   * Place holders will only be resolved when there is a Spring test application
   * context present (such as when using {@code @SpringJunitConfig or @SpringRunner}.
   *
   * @return the topics to create
   */
  @AliasFor(
    annotation = EmbeddedKafka.class
  )
  String[] topics() default {};

  @interface ConsumerConfiguration {
    boolean autoCommit() default true;

    String groupId() default "testGroup";

    Class<? extends Deserializer> keyDeserializer() default StringDeserializer.class;

    Class<? extends Deserializer> valueDeserializer() default StringDeserializer.class;
  }

  ConsumerConfiguration consumer() default @ConsumerConfiguration;

  class AutoConfigureKafkaEmbeddedExtension
    implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private Consumer<Object, Object> consumer;
    private RecordsSupplier<Object, Object> recordsSupplier;

    private EmbeddedKafkaBroker getEmbeddedKafkaBroker(ExtensionContext context) {
      return SpringExtension
        .getApplicationContext(context)
        .getBean(EmbeddedKafkaBroker.class);
    }

    private AutoConfigureKafkaEmbedded getConfiguration(ExtensionContext context) {
      return Objects.requireNonNull(
        context
          .getRequiredTestClass()
          .getAnnotation(AutoConfigureKafkaEmbedded.class)
      );
    }

    @Override
    public void beforeEach(@NonNull ExtensionContext context) throws Exception {
      final var embeddedKafka = getEmbeddedKafkaBroker(context);
      final var configuration = getConfiguration(context);
      final var consumerProps = KafkaTestUtils
        .consumerProps(
          embeddedKafka,
          configuration
            .consumer()
            .groupId(),
          configuration
            .consumer()
            .autoCommit()
        );
      //noinspection unchecked
      this.consumer = new DefaultKafkaConsumerFactory<>(
        consumerProps,
        configuration
          .consumer()
          .keyDeserializer()
          .getDeclaredConstructor()
          .newInstance(),
        configuration
          .consumer()
          .valueDeserializer()
          .getDeclaredConstructor()
          .newInstance()
      )
        .createConsumer();
      this.recordsSupplier = () -> KafkaTestUtils.getRecords(this.consumer);
      // consume all topics from the configuration
      for (String topic : configuration.topics()) {
        embeddedKafka.consumeFromAnEmbeddedTopic(
          consumer,
          true,
          topic
        );
      }
    }

    private final Map<Class<?>, Supplier<?>> SUPPORTED_PARAMETERS = Map.of(
      Consumer.class, () -> this.consumer,
      RecordsSupplier.class, () -> this.recordsSupplier
    );

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
      return SUPPORTED_PARAMETERS
        .containsKey(
          parameterContext
            .getParameter()
            .getType()
        );
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
      return
        SUPPORTED_PARAMETERS
          .get(
            parameterContext
              .getParameter()
              .getType()
          )
          .get();

    }

    @Override
    public void afterEach(@NonNull ExtensionContext context) {
      this.consumer.close();
      this.consumer = null;
      this.recordsSupplier = null;
    }

  }

}
