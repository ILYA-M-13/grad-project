package main.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CaptchaServiceTest {

    @InjectMocks
    CaptchaService captchaService;

    private int stringLen = 3;

    @Test
    void genString() {

        String response = captchaService.genString(stringLen);
        assertEquals(response.length(),stringLen);

    }

}
