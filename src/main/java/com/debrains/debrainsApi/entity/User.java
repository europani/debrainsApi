package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.common.AuthProvider;
import com.debrains.debrainsApi.common.Tier;
import com.debrains.debrainsApi.common.UserRole;
import com.debrains.debrainsApi.common.UserState;
import com.debrains.debrainsApi.dto.user.UserInfoDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString(exclude = "profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_table")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @Column(unique = true)
    private String name;

    @Column(length = 2000)
    private String description;

    private String img;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserState state;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String githubUrl;
    private String blogUrl;
    private String snsUrl;

    private String icon;
    private Integer tier;
    private Long exp;
    private String memo;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginDate;

    private String refreshToken;

    @OneToOne(mappedBy = "user")
    private Profile profile;


    /*
    * 개인정보 수정
    */
    public void updateUserInfo(UserInfoDTO dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.img = dto.getImg();
        this.githubUrl = dto.getGithubUrl();
        this.blogUrl = dto.getBlogUrl();
        this.snsUrl = dto.getSnsUrl();
    }

    public void changeRole(UserRole role) {
        this.role = role;
    }

    public void changeState(UserState state) {
        this.state = state;
    }

    /*
    * 경험치 부여 및 티어 변경
    */
    public void calExp(Long exp) {
        this.exp += exp;
        promotion(this.exp);
    }

    public void promotion(Long exp) {
        for (Tier tier : Tier.values()) {
            if (exp >= tier.getStart() && exp <= tier.getEnd()) {
                this.tier = tier.getLevel();
            }
        }
    }

}
