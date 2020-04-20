package aws.message.queue.poc.service;

import aws.message.queue.poc.request.SMSRequest;

public interface QueueMessageService {
    boolean sendMessageToQueue(SMSRequest request);

    boolean sendMessageToQueueAsync(SMSRequest request);
}
