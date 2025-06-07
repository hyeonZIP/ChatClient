package zip.hyeon.chatclient.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import zip.hyeon.chatclient.domain.plan.Plan;
import zip.hyeon.chatclient.repository.PlanRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatClientController {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final Neo4jVectorStore vectorStore;
    private final PlanRepository planRepository;

    /**
     * 논스트리밍 요청(모든 답변이 한번에 옴)
     */
    @GetMapping("/ai")
    String generation(String userInput) {
        return this.chatClient.prompt().user(userInput).call().content();
    }

    /**
     * 스트리밍 요청(답변 일부가 생성되자마자 옴)
     */
    @GetMapping(value = "/ai/stream", produces = "text/plain;charset=UTF-8")
    Flux<String> streamGeneration(String userInput, String userId) {
        return this.chatClient.prompt().advisors(a -> a.param(CONVERSATION_ID, userId)).user(userInput).stream().content();
    }

    @GetMapping("/save/dummyPlan")
    public void saveDummyPlan() {

        List<Plan> plans = List.of(Plan.of("너겟 69"), Plan.of("너겟 65"), Plan.of("너겟 59"), Plan.of("너겟 51"), Plan.of("너겟 47"), Plan.of("너겟 46"), Plan.of("너겟 45"), Plan.of("너겟 44"), Plan.of("너겟 43"), Plan.of("너겟 42"), Plan.of("너겟 41"), Plan.of("너겟 40"), Plan.of("너겟 39"), Plan.of("너겟 38"), Plan.of("너겟 37"), Plan.of("너겟 36"), Plan.of("너겟 35"), Plan.of("너겟 34"), Plan.of("너겟 33"), Plan.of("너겟 32"), Plan.of("너겟 31"), Plan.of("너겟 30"), Plan.of("너겟 26"));
        planRepository.saveAll(plans);
    }

    @GetMapping("/save/dummyBenefit")
    public void saveDummyBenefit() {


    }

    @GetMapping("/ai/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "가로길이가 3cm이고 이와 직각인 세로의 길이가 4cm인 직각삼각형이 있어 이 도형의 빗변의 길이는?") String message) {
        EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }

    @GetMapping("/ai/vectorSearch")
    List<Document> vectorSearch(String userInput) {
        List<Document> documents = vectorStore.similaritySearch(userInput);
        for (Document document : documents) {
            log.info("getId() = {}", document.getId());
            log.info("getMedia() = {}", document.getMedia());
            log.info("getText = {}", document.getText());
            log.info("getFormattedContent = {}", document.getFormattedContent());
            log.info("document.getScore() ={}", document.getScore());
        }

        return documents;
    }


    @GetMapping("/ai/embedding/plan")
    public void embedPlan() {

        List<Plan> all = planRepository.findAll();
        List<Document> documents = new ArrayList<>();
        for (Plan plan : all) {
            String s = convertPlanToText(plan);

            log.info("convertPlanToText Result = {}", s);

            String content = chatClient.prompt("""
                    임베딩을 통해 벡터 유사도 검색을 위한 적절한 한줄 설명문 생성해줘.
                    해당 설명문에는 요금제 이름, 혜택 정보가 모두 포함되어야 해.
                    응답으로는 경어체를 쓰지 않고 "어떠한 요금제." 로 끝나도록 생성해줘
                    """).advisors(p -> p.param(CONVERSATION_ID, "관리자")).user(s).call().content();

            log.info("임베딩된 contest = {}", content);

            documents.add(new Document(content));
        }

        vectorStore.add(documents);

    }

    private String convertPlanToText(Plan plan) {
        StringBuilder sb = new StringBuilder();
        sb.append("요금제 이름: ").append(plan.getName()).append("\n");

        return sb.toString();
    }

}
