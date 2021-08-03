package com.chan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo implements Serializable {
    @Id
    private  String _id;
    private  int id;
    private  String name;
    private  int age;
}
