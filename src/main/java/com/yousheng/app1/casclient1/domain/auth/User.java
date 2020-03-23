package com.yousheng.app1.casclient1.domain.auth;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author : JoeTao
 * createAt: 2018/9/17
 */
@Builder
@Data
public class User {
    @Size(min=6, max=20)
    private String name;
    @Size(min=8, max=20)
    private String password;
}
