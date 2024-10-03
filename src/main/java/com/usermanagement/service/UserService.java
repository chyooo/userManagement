package com.usermanagement.service;

import com.usermanagement.dto.UserDTO;
import com.usermanagement.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /*
     * 회원 조회 : 사용자 ID로 user_id, user_pw, user_nm, user_auth 조회
     * @param userId : 조회할 사용자 ID
     * @return UserDTO : 사용자 정보
     */
    public UserDTO getUsers(String userId) {
        return userMapper.getUsers(userId);
    }

    /*
     * 회원 추가 : 비밀번호를 암호화하여 사용자 추가
     * @param userDTO : 추가할 사용자 정보
     * @return UserDTO : 추가된 사용자 정보
     */
    public int addUser(UserDTO userDTO) {
        userDTO.setUserPw(passwordEncoder.encode(userDTO.getUserPw())); // 비밀번호 암호화
        return userMapper.addUser(userDTO);
    }

    /*
     * 회원 정보 수정 : 사용자 ID와 사용자 이름을 업데이트
     * @param userId: 수정할 사용자 ID
     * @param userNm: 수정할 사용자 이름
     */
    public int updateUser(String userId, String userNm) {
        return userMapper.updateUser(userId, userNm);
    }

    /*
     * 회원 삭제 : 사용자 ID로 사용자를 삭제
     * @param userId: 삭제할 사용자 ID
     */
    public int deleteUser(String userId) {
        return userMapper.deleteUser(userId);
    }

    /*
     * 모든 사용자 조회 : DB에서 모든 사용자 정보를 가져옴
     * @return List<UserDTO>: 모든 사용자 정보 리스트
     */
    public List<UserDTO> getAllUsers() {
        return userMapper.findAllUsers();
    }

    /*
     * 사용자 ID 중복 확인 : 주어진 사용자 ID가 이미 존재하는지 확인
     * @param userId : 확인할 사용자 ID
     * @return boolean : ID 존재 여부 (존재하면 true, 없으면 false)
     */
    public boolean isUserIdExists(String userId) {
        return userMapper.getUsers(userId) != null;
    }

    /*
     * 사용자 기본키 조회 : 사용자 ID를 통해 해당 사용자의 기본키(user_idx)를 조회
     * @param userId : 조회할 사용자 ID
     * @return int : 사용자의 기본키
     */
    public int getUserIdx(String userId) {
    	return userMapper.getUserIdx(userId);
    }
}
