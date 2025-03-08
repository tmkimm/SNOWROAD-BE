package com.snowroad.userContact.domain;

import com.snowroad.entity.User;
import com.snowroad.entity.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserContactRepository extends JpaRepository<UserContact, Long> {
}
