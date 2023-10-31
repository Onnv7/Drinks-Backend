package com.hcmute.drink.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "employee_token")
public class EmployeeTokenCollection {
    @Id
    private String id;
    private String refreshToken;
    private ObjectId employeeId;
    private boolean used = false;
}