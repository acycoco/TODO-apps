package com.example.todo.api.user;

import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.user.request.UserJoinRequestDto;
import com.example.todo.dto.user.request.UserUpdateRequestDto;
import com.example.todo.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class})
@Transactional
@SpringBootTest
class UserApiControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Authentication authentication;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @DisplayName("회원가입 API 테스트")
    @Test
    void saveUser() throws Exception {
        // given
        final String username = "아이디";
        final String password = "비밀번호";
        UserJoinRequestDto joinDto = UserJoinRequestDto.builder()
                .username(username)
                .password(password)
                .build();
        String url = "http://localhost:8080/api/join";

        // when
        ResultActions perform = mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(joinDto)));

        // then
        perform.andExpect(status().isOk())
                .andDo(document("/api/join",
                        requestFields(
                                fieldWithPath("username").description("아이디"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("phone").description("연락처")
                        )));

    }

    @DisplayName("회원 정보 수정 Api 테스트")
    @Test
    void updateUser() throws Exception {
        // given
        final String username = "아이디";
        final String password = "비밀번호";
        User user = createUser(username, password);

        final String newPassword = "변경 하는 비밀번호";
        final String phone = "010-1234-5678";
        final String image = "profile.jpg";
        UserUpdateRequestDto updateDto = UserUpdateRequestDto.builder()
                .password(newPassword)
                .phone(phone)
                .profileImage("profile.jpg")
                .build();

        String url = "http://localhost:8080/api/users/update";

        // when
        ResultActions perform = mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(authentication)
                .content(objectMapper.writeValueAsString(updateDto)));

        // then
        perform.andExpect(status().isOk())
                .andDo(document("/api/users/update",
                        requestFields(
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("phone").description("연락처"),
                                fieldWithPath("profileImage").description("프로필사진")
                        )));

    }

    private User createUser(final String username, final String password) {
        User user = userRepository.save(User.builder()
                .username(username)
                .password(password)
                .build());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword(), new ArrayList<>()));

        authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("" + user.getId());
        return user;
    }
}