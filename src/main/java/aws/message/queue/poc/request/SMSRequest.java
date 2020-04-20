package aws.message.queue.poc.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSRequest {
    @NotNull
    private List<SMSRecipient> recipients;
    @NotNull
    @NotBlank
    @Length(max = 140)
    private String message;
}
