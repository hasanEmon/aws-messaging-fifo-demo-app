package aws.message.queue.poc.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSSQSConfig {

    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.credentials.access-key}")
    private String awsAssessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String awsSecretKey;


    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(){
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

    @Bean
    public AmazonSQSAsync amazonSQSAsync(){
        return AmazonSQSAsyncClientBuilder.standard().withRegion(region).withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAssessKey, awsSecretKey))
        ).build();
    }

    @Bean
    public AmazonSQS amazonSQS(){
        return AmazonSQSClientBuilder.standard().withRegion(region).withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAssessKey, awsSecretKey))
        ).build();
    }

}
