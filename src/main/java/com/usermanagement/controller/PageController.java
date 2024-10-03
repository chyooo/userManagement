package com.usermanagement.controller;

import com.usermanagement.dto.UserDTO;
import com.usermanagement.dto.UserHistoryDTO;
import com.usermanagement.service.UserHistoryService;
import com.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PageController {
    private final UserService userService; // 사용자 관련 서비스
    private final UserHistoryService userHistoryService; // 사용자 기록 관련 서비스

    @Autowired
    public PageController(UserService userService, UserHistoryService userHistoryService) {
        this.userService = userService;
        this.userHistoryService = userHistoryService;
    }

    /*
     * 로그인 페이지를 반환하는 GET 요청 메서드
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // 로그인 페이지 이동
    }
    
    /*
     * 회원가입 페이지를 반환하는 GET 요청 메서드
     */
    @GetMapping("/signup")
    public String signup() {
        return "signup"; // 회원가입 페이지로 이동
    }

    /*
     * 회원가입 처리하는 POST 요청 메서드
     */
    @PostMapping("/signup")
    public String signupUser(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            // 1. 회원 ID 중복 확인
            if (userService.isUserIdExists(userDTO.getUserId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "이미 사용 중인 아이디입니다.");
                return "redirect:/signup"; // 중복 시 다시 가입 페이지로 리다이렉트
            }
            
            // 2. 회원 가입
        	userDTO.setUserAuth("USER"); // 기본 권한 설정
            int result = userService.addUser(userDTO);
            
            // 3-1. 회원 가입 성공 : history 추가, 성공 메시지 세팅
            if (result == 1) {
                UserHistoryDTO historyDTO = new UserHistoryDTO();
                historyDTO.setUrl("/signup");
                historyDTO.setActionType("C"); // 추가
                historyDTO.setRegIp(getClientIp());
                historyDTO.setRegUserIdx(userService.getUserIdx(userDTO.getUserId()));

                userHistoryService.addUserHistory(historyDTO); // 사용자 기록 추가
                
                redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
            
            // 3-2. 회원 가입 실패 : 실패 메시지 세팅
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다.");
            return "redirect:/signup"; // 실패 시 다시 가입 페이지로 리다이렉트
        }
        
        // 4. 로그인 페이지 리다이렉트
        return "redirect:/login";
    }

    /*
     * 메인 페이지를 반환하는 GET 요청 메서드
     */
    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	// 1. userId로 회원 정보 select 후 model 세팅
        UserDTO user = userService.getUsers(userDetails.getUsername());
        if (user != null) {
            model.addAttribute("userNm", user.getUserNm());
            model.addAttribute("userAuth", user.getUserAuth());
        }
        
        // 2. 메인 페이지 이동
        return "main";
    }


    /*
     * 회원 목록 페이지를 반환하는 GET 요청 메서드
     */
    //@PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping("/userList")
    public String userList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	// 1. 회원 목록 조회
        List<UserDTO> users = userService.getAllUsers();
        
        // 2. model 세팅
        model.addAttribute("users", users); // 사용자 목록 추가
        model.addAttribute("userNm", userService.getUsers(userDetails.getUsername()).getUserNm()); // 로그인 사용자 이름 추가
        
        // 3. 회원 목록 페이지로 이동
        return "userList";
    }
    
    /*
     * 회원 상세 정보를 보여주는 GET 요청 메서드
     */
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping("/userDetails/{userId}")
    public String userDetails(@PathVariable("userId") String userId,
                              @AuthenticationPrincipal UserDetails userDetails, Model model, RedirectAttributes redirectAttributes) {
        // 1. 요청 회원 정보 조회
        UserDTO user = userService.getUsers(userId);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다."); // 에러 메시지 추가
            return "redirect:/userList"; // 사용자 목록 페이지로 리다이렉트
        }
        model.addAttribute("user", user); // 사용자 정보를 모델에 추가
        
        // 2. 회원 상세 정보 페이지 이동
        return "userDetails"; 
    }

    /*
     * 회원 정보를 수정하는 POST 요청 메서드
     */
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping("/userDetails/{userId}")
    public String updateUser(@PathVariable("userId") String userId, @RequestParam("userNm") String userNm,
                             RedirectAttributes redirectAttributes) {
    	// 1. 회원 정보 수정
        int result = userService.updateUser(userId, userNm);
        
        // 2-1. 회원 정보 수정 성공 : history 추가, 성공 메시지 세팅
        if (result == 1) {
            UserHistoryDTO historyDTO = new UserHistoryDTO();
            historyDTO.setUrl("/userDetails/" + userId);
            historyDTO.setActionType("U"); // 수정
            historyDTO.setRegIp(getClientIp()); // IP 설정
            historyDTO.setRegUserIdx(userService.getUserIdx(userId)); // user_idx 설정
            userHistoryService.addUserHistory(historyDTO); // 사용자 수정 기록 추가
            
            redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
            
        // 2-2. 회원 정보 수정 실패 : 실패 메시지 세팅
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "회원 정보 수정에 실패했습니다.");
        }

        // 3. 회원 상세 페이지로 리다이렉트
        return "redirect:/userDetails/" + userId;
    }

    /*
     * 회원 정보를 삭제하는 POST 요청 메서드
     */
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping("/userDetails/{userId}/delete")
    public String deleteUser(@PathVariable("userId") String userId, RedirectAttributes redirectAttributes) {
    	// 1. 요청 회원 인덱스 조회 (history 기록용)
        int userIdx = userService.getUserIdx(userId);
        
        // 2. 회원 삭제
        int result = userService.deleteUser(userId);
        
        // 3-1. 회원 삭제 성공 : history 추가, 성공 메시지 세팅
        if (result == 1) {
            UserHistoryDTO historyDTO = new UserHistoryDTO();
            historyDTO.setUrl("/userDetails/" + userId);
            historyDTO.setActionType("D"); // 삭제
            historyDTO.setRegIp(getClientIp()); // IP 설정
            historyDTO.setRegUserIdx(userIdx); // user_idx 설정
            userHistoryService.addUserHistory(historyDTO); // 사용자 삭제 기록 추가
            
            redirectAttributes.addFlashAttribute("message", "회원 정보가 삭제되었습니다.");
            
        // 3-2. 회원 삭제 실패 : 실패 메시지 세팅
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "회원 정보 삭제에 실패했습니다.");
        }
        
        // 4. 회원 목록 페이지로 리다이렉트
        return "redirect:/userList"; 
    }

    /*
     * 로직 수행 중 오류 발생 : 500 Internal Server Error 를 반환 및 500.html 이동
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException() {
        return "500"; // 500 오류 페이지 반환
    }

    /*
     * API 요청 가능 메서드 : GET, POST, PUT, DELETE
     */
    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public void handleAllowedMethods() {
        // 지원되는 메서드에 대한 처리 (특별한 로직 없음)
    }

    /*
     * API 요청 가능 메서드(GET, POST, PUT, DELETE) 외의 요청 메서드는 405 METHOD_NOT_ALLOWED 반환 및 405.html 이동
     */
    @RequestMapping(value = "/**")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleUnsupportedMethods() {
        return "405"; // 405 오류 페이지 반환
    }

    /*
     * 클라이언트의 IP 주소를 반환하는 메서드
     */
    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "unknown"; // 요청이 없을 경우 처리
        }

        HttpServletRequest request = attributes.getRequest();
        String remoteAddr = request.getRemoteAddr(); // 요청한 클라이언트의 IP 주소
        String xForwardedFor = request.getHeader("X-Forwarded-For"); // 프록시 서버를 통한 요청의 경우

        return (xForwardedFor != null && !xForwardedFor.isEmpty()) ? xForwardedFor.split(",")[0] : remoteAddr; // 클라이언트 IP 반환
    }
}
