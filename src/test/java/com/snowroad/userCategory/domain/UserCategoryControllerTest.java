package com.snowroad.userCategory.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowroad.config.auth.dto.CustomUserDetails;
import com.snowroad.entity.Events;
import com.snowroad.entity.User;
import com.snowroad.event.domain.Category;
import com.snowroad.event.domain.EventsRepository;
import com.snowroad.event.web.dto.EventsResponseDto;
import com.snowroad.event.web.dto.EventsSaveRequestDto;
import com.snowroad.event.web.dto.PagedResponseDto;
import com.snowroad.user.domain.Role;
import com.snowroad.user.domain.UserRepository;
import com.snowroad.userCategory.web.dto.UserCategoriesResponseDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserCategoryControllerTest {

    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCategoryRepository userCategoryRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private User savedUser;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(User.builder()
                .nickname("테스트유저")
                .role(Role.USER)
                .build());


        CustomUserDetails userDetails = new CustomUserDetails(1L, "testuser", "ROLE_USER", "Y");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @AfterEach
    void cleanUp() {
        userCategoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void 관심카테고리_추가_조회_수정_성공() throws Exception {
        // given
        Set<Category> addCategories = Set.of(Category.PASH, Category.BEAU);
        Set<Category> updateCategories = Set.of(Category.IT, Category.SPORT);

        String addUrl = "http://localhost:" + port + "/api/user-categories";
        String getUrl = "http://localhost:" + port + "/api/user-categories";

        // when: 추가 요청
        mvc.perform(post(addUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCategories)))
                .andExpect(status().isOk());

        // then: 조회 요청 후 추가 확인
        MvcResult result = mvc.perform(get(getUrl))
                .andExpect(status().isOk())
                .andReturn();

        UserCategoriesResponseDto responseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserCategoriesResponseDto.class
        );

        assertThat(responseDto.getData()).containsExactlyInAnyOrder(Category.PASH, Category.BEAU);

        // when: 수정 요청 (add -> updateCategories로 변경)
        mvc.perform(put(addUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategories)))
                .andExpect(status().isOk());

        // then: 조회 요청 후 수정 확인
        MvcResult afterUpdateResult = mvc.perform(get(getUrl))
                .andExpect(status().isOk())
                .andReturn();

        UserCategoriesResponseDto updatedResponse = objectMapper.readValue(
                afterUpdateResult.getResponse().getContentAsString(),
                UserCategoriesResponseDto.class
        );

        assertThat(updatedResponse.getData()).containsExactlyInAnyOrder(Category.IT, Category.SPORT);
    }

    @Test
    @WithMockUser(roles = "USER")
    void 관심카테고리_잘못된코드값_입력시_400에러() throws Exception {
        // given
        String url = "http://localhost:" + port + "/api/user-categories";

        String invalidPayload = "[\"PASH\", \"INVALID\"]";

        // when & then
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("잘못된 코드값이 포함되어 있습니다. 올바른 값을 입력해주세요."));
    }
}