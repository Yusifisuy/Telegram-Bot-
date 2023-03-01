package az.code.telegrambotv2.configs.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class RabbitMqConfiguration {

    @Value("${sr.rabbit.queue.name}")
    private String queueName;
    @Value("${sr.rabbit.routing.name}")
    private String routingName;
    @Value("${sr.rabbit.exchange.name}")
    private String exchangeName;


    @Bean
    public Queue queue(){
        return new Queue(queueName);
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(exchangeName);
    }

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Binding binding(final FanoutExchange fanoutExchange, final Queue queue){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }



}
