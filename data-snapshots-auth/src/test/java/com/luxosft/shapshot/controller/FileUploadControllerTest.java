package com.luxosft.shapshot.controller;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.luxosft.shapshot.repository.AuthUserRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerTest {

  @Autowired
  private MockMvc mvc;
  private String token;
  private String test_upload_1;
  private String test_after_delete_call;
  private byte[] large_file;
  private byte[] wrongType;
  private String expected;
  private String DELETE_WRONG_ID;
  @Autowired
  private AuthUserRepository authUserRepository;

  @Before
  public void before() throws Exception {
    initFileResources();
    String body = "{\"username\":\"user\",\"password\":\"pass\"}";
    mvc.perform(MockMvcRequestBuilders.post("/v1/users/sign-up")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
        .andExpect(status().isCreated()).andReturn();
    ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/login").content(body));
    token = resultActions.andReturn().getResponse().getHeader("Authorization");
  }

  @After
  public void tearDown() {
    authUserRepository.deleteAll();
  }

  @Test
  public void uploadFileTest() throws Exception {
    String actual = uploadFileCall();
    assertEquals(
        expected.replaceAll(
            System.lineSeparator(), ""
        ).replaceAll(" ", ""), actual
    );
  }

  @Test
  public void largeFileTest() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "large_file.txt",
            MediaType.TEXT_PLAIN_VALUE, large_file);
    mvc.perform(
        multipart("/v1/files/upload-file")
            .file(file)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", token)).
        andExpect(content().string("File is broken."));
  }

  @Test
  public void wrongTypeFile() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "wrong_type_file.pdf",
            MediaType.TEXT_PLAIN_VALUE, wrongType);
    mvc.perform(
        multipart("/v1/files/upload-file")
            .file(file)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", token)).
        andExpect(content().string("File is broken."));
  }


  @Test
  public void deleteByIdTest() throws Exception {
    uploadFileCall();
    String actual = mvc.perform(MockMvcRequestBuilders.delete("/v1//files/entries/1")
        .header("Authorization", token)
        .contentType(MediaType.TEXT_PLAIN_VALUE))
        .andExpect(status().isOk()).andReturn().getResponse()
        .getContentAsString(Charset.defaultCharset());
    assertEquals(
        test_after_delete_call.replaceAll(
            System.lineSeparator(), ""
        ).replaceAll(" ", ""), actual
    );
  }

  @Test
  public void deleteByWrongIdTest() throws Exception {
    uploadFileCall();
    String id = "wrong";
    String actual = mvc.perform(MockMvcRequestBuilders.delete("/v1//files/entries/" + id)
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest()).andReturn().getResponse()
        .getContentAsString(Charset.defaultCharset());
    assertEquals(
        DELETE_WRONG_ID.replaceAll(
            System.lineSeparator(), ""
        ).replaceAll(" ", ""), actual.replaceAll(" ", "")
    );
  }

  @Test
  public void deleteByNonExistingIdTest() throws Exception {
    uploadFileCall();
    int id = 11111;
    String actual = mvc.perform(MockMvcRequestBuilders.delete("/v1/files/entries/" + id)
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest()).andReturn().getResponse()
        .getContentAsString(Charset.defaultCharset());
    assertEquals(
        "No class com.luxosft.shapshot.model.Entry entity with id " + 11111 + " exists!",
        actual
    );
  }

  private void initFileResources() throws IOException {
    try (InputStream ioFile = new FileInputStream("src/test/resources/test_upload_1.txt");
        InputStream ioLarge = new FileInputStream("src/test/resources/large_file.txt");
        InputStream ioWrongFormat = new FileInputStream("src/test/resources/wrong_type_file.pdf");
        InputStream ioResultFile = new FileInputStream(
            "src/test/resources/test_upload_1_result.json");
        InputStream ioAfterDeleteCall = new FileInputStream(
            "src/test/resources/expected_after_delete.json");
        InputStream ioDeleteWrongId = new FileInputStream(
            "src/test/resources/deleteWrongId.json")) {
      DELETE_WRONG_ID = IOUtils.toString(
          ioDeleteWrongId, StandardCharsets.UTF_8
      );
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
      test_after_delete_call = IOUtils.toString(ioAfterDeleteCall, StandardCharsets.UTF_8);
    }
  }

  private String uploadFileCall() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "test_upload_1.txt",
            MediaType.TEXT_PLAIN_VALUE,
            test_upload_1.getBytes(StandardCharsets.UTF_8));
    return mvc.perform(
        multipart("/v1/files/upload-file")
            .file(file)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", token))
        .andExpect(status().isCreated()).andReturn().getResponse()
        .getContentAsString(Charset.defaultCharset());
  }
}