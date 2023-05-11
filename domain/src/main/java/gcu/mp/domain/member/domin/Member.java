package gcu.mp.domain.member.domin;

import gcu.mp.domain.entity.BaseEntity;
import gcu.mp.domain.member.vo.Role;
import gcu.mp.domain.member.vo.State;
import gcu.mp.domain.pay.domain.PayHistory;
import gcu.mp.domain.point.domin.PointHistory;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Member extends BaseEntity {

    private String fcm_id;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private State state;
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private SocialLogin socialLogin;
    private String nickname;
    private String email;
    private long point;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    List<PointHistory> pointHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    List<PayHistory> payHistoryList = new ArrayList<>();
    public void setSocialLogin(SocialLogin socialLogin) {
        this.socialLogin = socialLogin;
        socialLogin.setMember(this);
    }

    public void resignMember() {
        this.state = State.D;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addPayHistory(PayHistory payHistory) {
        payHistoryList.add(payHistory);
    }

    public void addPoint(int totalAmount) {
        this.point += totalAmount;
    }
}
