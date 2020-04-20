package aws.message.queue.poc.controller;

import aws.message.queue.poc.service.QueueMessageService;
import aws.message.queue.poc.request.SMSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
public class SQSController {

    @Autowired
    private QueueMessageService queueMessageService;

    @PostMapping("/sendMessage")
    public boolean sendMessageToQueue(@RequestBody @Valid SMSRequest request){
        return queueMessageService.sendMessageToQueue(request);
    }

    @PostMapping("/sendMessageAsync")
    public boolean sendMessageToQueue2(@RequestBody @Valid SMSRequest request){
        return queueMessageService.sendMessageToQueueAsync(request);
    }

//    @SqsListener("SMSSenderQueue.fifo")
//    public void queueListener(String message){
//        System.out.println("Sender Queue Response : "+message);
//    }

//    @SqsListener("SMSReceiverQueue.fifo")
//    public void queueListener2(String message){
//        System.out.println("Receiver Queue Response : "+message);
//    }

}
