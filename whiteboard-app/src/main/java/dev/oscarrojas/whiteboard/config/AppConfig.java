package dev.oscarrojas.whiteboard.config;

import dev.oscarrojas.whiteboard.messaging.AppEventEmitter;
import dev.oscarrojas.whiteboard.messaging.AppEventListener;
import dev.oscarrojas.whiteboard.messaging.annotation.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@Configuration
@EnableAsync
public class AppConfig {

    @Bean
    public SimpleAsyncTaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

//    @Bean
//    public AppMessageBroker messageBroker(List<AppMessageConsumer> consumers) {
//        AppMessageBroker broker = new AppMessageBroker();
//
//        for (AppMessageConsumer consumer : consumers) {
//            Channel channel = consumer.getClass()
//                .getDeclaredAnnotation(Channel.class);
//
//            if (channel == null) {
//                // TODO: log non-annotated consumer
//                continue;
//            }
//
//            broker.subscribe(channel.value(), consumer);
//        }
//
//        return broker;
//    }

    @Bean
    public AppEventEmitter eventEmitter(List<AppEventListener> listeners) {
        AppEventEmitter emitter = new AppEventEmitter();

        for (AppEventListener listener : listeners) {
            Event event = listeners.getClass().getDeclaredAnnotation(Event.class);

            if (event == null) {
                // TODO: log non-annotated listener
                continue;
            }

            emitter.addEventListener(event.value(), listener);
        }

        return emitter;
    }
}
