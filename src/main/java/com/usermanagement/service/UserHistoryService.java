package com.usermanagement.service;

import com.usermanagement.dto.UserHistoryDTO;
import com.usermanagement.mapper.UserHistoryMapper;
import org.springframework.stereotype.Service;

@Service
public class UserHistoryService {
    private final UserHistoryMapper userHistoryMapper;

    public UserHistoryService(UserHistoryMapper userHistoryMapper) {
        this.userHistoryMapper = userHistoryMapper;
    }

    /*
     * 사용자 히스토리 추가 : 사용자 행동 이력을 DB에 저장
     * @param userHistoryDTO : 추가할 사용자 행동 이력 정보
     * @return int : 성공 시 1, 실패 시 0 반환
     */
    public int addUserHistory(UserHistoryDTO userHistoryDTO) {
        return userHistoryMapper.insertUserHistory(userHistoryDTO);
    }
}
