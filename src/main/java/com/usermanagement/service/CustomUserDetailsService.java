package com.usermanagement.service;

import com.usermanagement.dto.UserDTO;
import com.usermanagement.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDTO user = userMapper.getUsers(userId); // 사용자 정보 조회

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + userId); // 사용자 미발견 예외
        }

        // UserDetails 객체 반환
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getUserPw()) // 해시된 비밀번호
                .roles(user.getUserAuth()) // 권한 설정
                .build();
    }
}
