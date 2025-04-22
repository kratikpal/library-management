package com.management.management.entity;

import com.management.management.Constants.UserRolesConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String email;

    private List<String> roles = List.of(UserRolesConstants.USER_ROLE);

    @NonNull
    private String name;
}
