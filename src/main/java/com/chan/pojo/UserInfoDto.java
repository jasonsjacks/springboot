package com.chan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document("userinfo_dto")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class UserInfoDto {
    @Id
    private  String _id;
    private  int id;
    private  String name;
    private  int age;
}
