package com.usermanagement.mapper;

import com.usermanagement.dto.UserHistoryDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserHistoryMapper {
    /*
     * 사용자 히스토리 추가 : 사용자 행동 이력을 DB에 저장
     * @param userHistoryDTO : 추가할 사용자 행동 이력 정보
     * @return int : 성공 시 1, 실패 시 0 반환
     */
    int insertUserHistory(UserHistoryDTO userHistoryDTO);
}