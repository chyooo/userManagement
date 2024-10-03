package com.usermanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserHistoryDTO {
	/*
	 * URL
	 */	
    private String url;
    
	/*
	 * 회원 기록 유형 (추가: C / 수정: U / 삭제: D)
	 */    
    private String actionType;
    
	/*
	 * 최초 등록 회원 기본키
	 */	    
    private int regUserIdx;
    
    /*
     * 최초 등록 IP
     */
    private String regIp;
}