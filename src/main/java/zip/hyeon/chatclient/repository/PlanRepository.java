package zip.hyeon.chatclient.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import zip.hyeon.chatclient.domain.plan.Plan;

public interface PlanRepository extends Neo4jRepository<Plan, String> {
}
