package com.snowroad.domain.marks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MarkRepositoryTest {

    @Autowired
    MarkRepository markRepository;
    @AfterEach
    public void cleanup() {
        markRepository.deleteAll();
    }

    @Test
    public void 즐겨찾기저장_불러오기() {
        //given
        Long userAcntNo = 001L;
        String eventId = "001";
        String likeYn = "Y";
        markRepository.save(Mark.builder()
                .userAcntNo(userAcntNo)
                .eventId(eventId)
                .likeYn(likeYn)
                .build());
        // when
        List<Mark> marksList = markRepository.findAll();
        LocalDateTime now = LocalDateTime.of(2024, 12 ,9, 0, 0, 0);

        // then
        Mark marks = marksList.get(0);
        System.out.println(">>> createdDate : " + marks.getCreatedDate());
        assertThat(marks.getEventId()).isEqualTo(eventId);
        assertThat(marks.getLikeYn()).isEqualTo(likeYn);
        assertThat(marks.getCreatedDate()).isAfter(now);
        assertThat(marks.getModifiedDate()).isAfter(now);
    }
}