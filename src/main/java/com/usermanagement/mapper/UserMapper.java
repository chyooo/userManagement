package com.usermanagement.mapper;

import com.usermanagement.dto.UserDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    /*
     * 사용자 조회 : 사용자 ID로 사용자 정보를 가져옴
     * @param userId : 조회할 사용자 ID
     * @return UserDTO : 사용자 정보
     */
    UserDTO getUsers(@Param("userId") String userId);
    
    /*
     * 사용자 추가 : 새로운 사용자 정보를 DB에 추가
     * @param userDTO : 추가할 사용자 정보
     * @return int : 성공 시 1, 실패 시 0 반환
     */
    int addUser(UserDTO userDTO);
    
    /*
     * 사용자 정보 수정 : 사용자 ID와 사용자 이름을 업데이트
     * @param userId : 수정할 사용자 ID
     * @param userNm : 수정할 사용자 이름
     * @return int : 성공 시 1, 실패 시 0 반환
     */
    int updateUser(@Param("userId") String userId, @Param("userNm") String userNm);
    
    /*
     * 사용자 삭제 : 사용자 ID로 사용자를 삭제
     * @param userId : 삭제할 사용자 ID
     * @return int : 성공 시 1, 실패 시 0 반환
     */
    int deleteUser(@Param("userId") String userId);
    
    /*
     * 모든 사용자 조회 : DB에서 모든 사용자 정보를 가져옴
     * @return List<UserDTO> : 모든 사용자 정보 리스트
     */
    List<UserDTO> findAllUsers();
    
    /*
     * 사용자 기본키 조회 : 사용자 ID를 통해 해당 사용자의 기본키(user_idx)를 조회
     * @param userId : 조회할 사용자 ID
     * @return int : 사용자의 기본키
     */    
    int getUserIdx(@Param("userId")String uesrId);
}