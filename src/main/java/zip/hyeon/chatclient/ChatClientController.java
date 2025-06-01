package zip.hyeon.chatclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatClientController {

    private final ChatClient chatClient;

    public ChatClientController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 논스트리밍 요청(모든 답변이 한번에 옴)
     */
    @GetMapping("/ai")
    String generation(String userInput) {

        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    /**
     * 스트리밍 요청(답변 일부가 생성되자마자 옴)
     */
    @GetMapping(value = "/ai/stream", produces = "text/plain;charset=UTF-8")
    Flux<String> streamGeneration(String userInput, String userId) {
        return this.chatClient.prompt()
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID, userId))
                .user(userInput)
                .stream()
                .content();
    }
}
