package chat;

import chat.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ChatServerCommandLineRunner implements CommandLineRunner {

    @Autowired
    private Server server;

    @Override
    public void run(String... args) {
        server.start();
    }
}