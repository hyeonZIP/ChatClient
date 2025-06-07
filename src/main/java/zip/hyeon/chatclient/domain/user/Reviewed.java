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
public class Reviewed {

    @RelationshipId
    private final Long id = null;

    private final int point;

    private final String comment;

    @TargetNode
    private final Plan plan;

    public static Reviewed of(final int point, final Plan plan) {
        return Reviewed.builder()
                .point(point)
                .plan(plan)
                .build();
    }
}
