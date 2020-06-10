package com.luxosft.shapshot.auth.controller;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.luxosft.shapshot.auth.exceptions.BrokenFileException;
import com.luxosft.shapshot.auth.repository.AuthUserRepository;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerTest {

  @Autowired
  private MockMvc mvc;
  private String token;
  private String test_upload_1;
  private byte[] large_file;
  private byte[] wrongType;
  private String expected;
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  @Autowired
  private AuthUserRepository authUserRepository;


  @Before
  public void beforeClass() throws Exception {
    String body = "{\"username\":\"user\",\"password\":\"pass\"}";
    mvc.perform(MockMvcRequestBuilders.post("/users/sign-up")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
        .andExpect(status().isCreated()).andReturn();
    ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/login").content(body));
    token = resultActions.andReturn().getResponse().getHeader("Authorization");
    try (InputStream ioFile = new FileInputStream("src/test/resources/test_upload_1.txt");
        InputStream ioLarge = new FileInputStream("src/test/resources/large_file.txt");
        InputStream ioWrongFormat = new FileInputStream("src/test/resources/wrong_type_file.pdf");
        InputStream ioResultFile = new FileInputStream(
            "src/test/resources/test_upload_1_result.json")) {
      test_upload_1 = IOUtils.toString(
          ioFile, StandardCharsets.UTF_8
      );
      large_file = IOUtils.toByteArray(
          ioLarge
      );
      wrongType = IOUtils.toByteArray(
          ioWrongFormat
      );
      expected = IOUtils.toString(ioResultFile, StandardCharsets.UTF_8);
    }
  }

  @After
  public void tearDown() {
    authUserRepository.deleteAll();
  }

  @Test
  public void uploadFileTest() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "test_upload_1.txt",
            MediaType.TEXT_PLAIN_VALUE,
            test_upload_1.getBytes(StandardCharsets.UTF_8));
    String actual = mvc.perform(
        multipart("/files/upload-file/")
            .file(file)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", token))
        .andExpect(status().isCreated()).andReturn().getResponse()
        .getContentAsString(Charset.defaultCharset());
    assertEquals(
        expected.replaceAll(
            System.lineSeparator(), ""
        ).replaceAll(" ", ""), actual
    );
  }

  @Test
  public void largeFileTest() throws Exception {
    expectedException.expectCause(isA(BrokenFileException.class));
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "large_file.txt",
            MediaType.TEXT_PLAIN_VALUE, large_file);
    mvc.perform(
        multipart("/files/upload-file/")
            .file(file)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", token)).
        andExpect(content().string("test"));
  }

  @Test
  public void wrongTypeFile() throws Exception {
    expectedException.expectCause(isA(BrokenFileException.class));
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "wrong_type_file.pdf",
            MediaType.TEXT_PLAIN_VALUE, wrongType);
    mvc.perform(
        multipart("/files/upload-file/")
            .file(file)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", token)).
        andExpect(content().string("test"));
  }
}