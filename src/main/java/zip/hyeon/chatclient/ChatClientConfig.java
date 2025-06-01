package zip.hyeon.chatclient;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.PostgresChatMemoryRepositoryDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final JdbcTemplate jdbcTemplate;

    /**
     * ChatClient Build
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {

        return chatClientBuilder
                .defaultSystem("너는 사용자가 요청한 상품 정보 또는 표현에 대해 유사항 상품을 추천하는 AI 에이전트야.")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory()).build())
                .build();
    }

    /**
     * ChatMemory Build
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository())
                .maxMessages(10)
                .build();
    }

    /**
     * ChatMemoryRepository Build
     */
    @Bean
    public ChatMemoryRepository chatMemoryRepository() {
        return JdbcChatMemoryRepository.builder()
                .jdbcTemplate(jdbcTemplate)
                .dialect(new PostgresChatMemoryRepositoryDialect())
                .build();
    }
}
