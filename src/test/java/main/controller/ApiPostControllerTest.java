package main.controller;

import main.model.ModerationStatus;
import main.model.Post;
import main.model.Tag;
import main.model.User;
import main.repository.PostInfoRepository;
import main.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;


@SpringBootTest
@AutoConfigureMockMvc
class ApiPostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostInfoRepository postInfoRepository;

    private Post postTest;
    private User userTest;
    private PasswordEncoder BCRYPT = new BCryptPasswordEncoder(12);

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        String name = "Илья Тест";
        String email = "test@test.ru";
        String password = BCRYPT.encode("123456");

        userTest = new User();
        userTest.setName(name);
        userTest.setEmail(email);
        userTest.setPassword(password);
        userTest.setModerator(false);
        userTest.setRegTime(new Date());
        userRepository.save(userTest);

        postTest = new Post();
        postTest.setText("Test Test Test Test Test Test Test Test Test Test Test ");
        postTest.setTitle("Title ");
        postTest.setViewCount(1);
        postTest.setModerationStatus(ModerationStatus.ACCEPTED);
        postTest.setTime(new Date());
        postTest.setUser(userTest);
        postInfoRepository.save(postTest);
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
        postInfoRepository.deleteAll();

    }

    @Test
    void searchById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/post/{id}",postTest.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(postTest.getId()));
    }

}