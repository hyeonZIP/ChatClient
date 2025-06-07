package zip.hyeon.chatclient.domain.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import zip.hyeon.chatclient.domain.plan.Plan;

@RelationshipProperties
@Getter
@With
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscribed {

    @RelationshipId
    private final Long id;

    @TargetNode
    private final Plan plan;

    public static Subscribed of(final Plan plan){
        return Subscribed.builder()
                .plan(plan)
                .build();
    }
}