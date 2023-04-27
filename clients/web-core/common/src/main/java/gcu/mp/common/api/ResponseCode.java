package gcu.mp.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    //TEST
    TEST(HttpStatus.OK, "TEST 입니다"),
    // member
    USER_SIGNUP(HttpStatus.CREATED, "회원 가입에 성공하였습니다."),
    USER_LOGIN(HttpStatus.OK, "로그인에 성공하였습니다."),
    // member
    MAIL_CERTIFICATION(HttpStatus.OK, "이메일 인증에 성공적으로 전송하였습니다."),
    MAIL_CERTIFICATION_AUTH(HttpStatus.OK, "이메일 인증에 성공하였습니다."),
    MAIL_CERTIFICATION_FAIL(HttpStatus.CONFLICT, "이메일 인증에 실패하였습니다."),
    EXISTS_USER(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),
    EXISTS_USER_NICKNAME(HttpStatus.CONFLICT,"이미 존재하는 유저 닉네임입니다." );
    private final HttpStatus status;
    private final String message;

}