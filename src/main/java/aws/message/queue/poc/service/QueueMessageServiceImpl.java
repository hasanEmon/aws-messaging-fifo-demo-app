package aws.message.queue.poc.service;

import aws.message.queue.poc.request.SMSRequest;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueueMessageServiceImpl implements QueueMessageService{

    @Value("${cloud.aws.fifo.end-point.url}")
    private String queueEndpointUrl;
    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private AmazonSQSAsync amazonSQSAsync;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;


    @Override
    public boolean sendMessageToQueue(SMSRequest request) {
        try {
            // Sending the message
            System.out.println("Sending a message to SMSSenderQueue.fifo.\n");
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            sendMessageRequest.setQueueUrl(queueEndpointUrl);
            sendMessageRequest.setMessageGroupId("messageGroup1");
            sendMessageRequest.setMessageDeduplicationId("1");
            sendMessageRequest.setMessageBody(request.toString());


            SendMessageResult sendMessageResult = amazonSQS.sendMessage(sendMessageRequest);

            String sequenceNumber = sendMessageResult.getSequenceNumber();
            String messageId = sendMessageResult.getMessageId();
            System.out.println("SendMessage succeed with messageId " + messageId + ", sequence number " + sequenceNumber + "\n");

            // Receive messages.
            System.out.println("Receiving messages from SMSSenderQueue.fifo.\n");
            final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueEndpointUrl);

            final List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
            for (final Message message : messages) {
                System.out.println("Message");
                System.out.println("MessageId:     " + message.getMessageId());
                System.out.println("ReceiptHandle: " + message.getReceiptHandle());
                System.out.println("MD5OfBody:     " + message.getMD5OfBody());
                System.out.println("Body:" + message.getBody());
                for (Map.Entry<String, String> entry : message.getAttributes().entrySet()) {
                    System.out.println("Attribute");
                    System.out.println("  Name:  " + entry.getKey());
                    System.out.println("  Value: " + entry.getValue());
                }
            }
            System.out.println();
        } catch (final AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (final AmazonClientException ace) {
            System.out.println("Error Message: " + ace.getMessage());
        }
        return true;
    }

    @Override
    public boolean sendMessageToQueueAsync(SMSRequest request) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("message-group-id", "messageGroupId1");
        headers.put("message-deduplication-id", "messageDuplicationId1");
        queueMessagingTemplate.convertAndSend(queueEndpointUrl, request, headers);

        final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueEndpointUrl);
        final List<Message> messages = amazonSQSAsync.receiveMessage(receiveMessageRequest).getMessages();
        for (Message message : messages) {
            System.out.println("Message");
            System.out.println("MessageId:     " + message.getMessageId());
            System.out.println("ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("Body:          " + message.getBody());
            for (Map.Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("Attribute");
                System.out.println("  Name:  " + entry.getKey());
                System.out.println("  Value: " + entry.getValue());
            }
        }
        return true;
    }
}
