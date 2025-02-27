package com.snowroad.config.auth.dto;

import com.snowroad.entity.User;
import lombok.Getter;

import java.io.Serializable;


// 세션에 저장하려면 직렬화를 구현해야 한다.
// Serializable : 직렬화 인터페이스
// 직렬화 : 객체를 바이트 스트림으로 변환하여 저장하거나 전송할 수 있는 형태로 만드는 것.
// 직렬화된 객체는 파일, DB, 네트워크 등에 전송되어 저장되었다가 다시 역질렬화 할 수 없다.
@Getter
public class SessionUser implements Serializable {
    private String nickName;
    private Long id;

    public SessionUser(User user) {
        this.nickName = user.getNickname();
        this.id = user.getUserAccountNo();
    }
}
