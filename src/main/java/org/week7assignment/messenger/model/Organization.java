package org.week7assignment.messenger.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseEntity {

    @Column(nullable = false, length = 100, unique = true, name = "organization_name")
    private String name;

    @Column(nullable = false, length = 100, name = "organization_introduction")
    private String introduction;

    @OneToMany(mappedBy = "organization")
    private List<User> users; // 조직에 속한 사용자 목록
}
