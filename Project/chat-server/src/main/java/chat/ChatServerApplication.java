package chat;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ChatServerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ChatServerApplication.class)
			.headless(false)
			.web(WebApplicationType.NONE)
			.run(args);
	}
}
