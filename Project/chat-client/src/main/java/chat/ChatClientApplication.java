package chat;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ChatClientApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ChatClientApplication.class)
            .headless(false)
            .web(WebApplicationType.NONE)
            .run(args);
    }
}