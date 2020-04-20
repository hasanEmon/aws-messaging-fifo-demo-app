package aws.message.queue.poc.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSRecipient {
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String mobileNumber;

}
