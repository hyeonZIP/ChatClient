package zip.hyeon.chatclient.domain.plan;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
import zip.hyeon.chatclient.domain.benefit.BundledBenefit;
import zip.hyeon.chatclient.domain.benefit.SingleBenefit;
import zip.hyeon.chatclient.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Node
@Getter
@With
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private final String id;

    @Property(name = "name")
    private final String name;

    private final State state;

    @Builder.Default
    @Relationship(type = "HAS_BENEFIT", direction = Relationship.Direction.OUTGOING)
    private List<BundledBenefit> bundledBenefits = new ArrayList<>();

    @Builder.Default
    @Relationship(type = "HAS_BENEFIT", direction = Relationship.Direction.OUTGOING)
    private List<SingleBenefit> singleBenefits = new ArrayList<>();

    public static Plan of(final String name) {
        return Plan.builder()
                .name(name)
                .build();
    }

    public void addBundledBenefit(final BundledBenefit bundledBenefit) {
        bundledBenefits.add(bundledBenefit);
    }

    public void addSingleBenefit(final SingleBenefit singleBenefit) {
        singleBenefits.add(singleBenefit);
    }
}