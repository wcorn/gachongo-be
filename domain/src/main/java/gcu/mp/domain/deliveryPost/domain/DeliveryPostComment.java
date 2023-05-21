package gcu.mp.domain.deliveryPost.domain;

import gcu.mp.domain.deliveryPost.vo.State;
import gcu.mp.domain.entity.BaseEntity;
import gcu.mp.domain.member.domin.Member;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeliveryPostComment extends BaseEntity {
    String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_post_id")
    DeliveryPost deliveryPost;
    @Enumerated(EnumType.STRING)
    State state;

    public void setDeliveryPost(DeliveryPost deliveryPost) {
        this.deliveryPost = deliveryPost;
        deliveryPost.addDeliveryPostComment(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.addDeliveryPostComment(this);
    }
}
