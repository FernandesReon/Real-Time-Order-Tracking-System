package com.reon.order_backend.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user_info")
public class User {
    @Id
    private ObjectId id;
    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;
    private boolean accountEnabled = false;

    private Set<Role> roles = EnumSet.of(Role.USER);

    @CreatedDate
    private LocalDateTime createdOn;
    @LastModifiedDate
    private LocalDateTime updatedOn;

    public enum Role {
        USER,
        ADMIN
    }

    @DBRef
    private List<Order> orderList = new ArrayList<>();
}
