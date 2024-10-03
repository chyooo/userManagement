package com.usermanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {
    /*
     * 회원 기본키
     */
    private String userIdx;
    
    /*
     * 회원 아이디
     */
    private String userId;

    /*
     * 회원 비밀번호
     */    
    private String userPw;

    /*
     * 회원 이름
     */    
    private String userNm;

    /*
     * 회원 권한 (USER : 일반회원 / SYSTEM_ADMIN : 관리자)
     */    
    private String userAuth;
}
