package com.chan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Document("userinfo_dto")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class UserInfoDto implements Serializable {
    @Id
    public   String _id;
    public   int id;
    public   String name;
    public   int age;
}
