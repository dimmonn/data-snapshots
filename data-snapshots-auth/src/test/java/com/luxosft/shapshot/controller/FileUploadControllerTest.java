package com.luxosft.shapshot.controller;

import static org.junit.Assert.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/docs")
public class FileUploadControllerTest {

  @Autowired
  private Environment env;
  @Autowired
  private MockMvc mvc;
  private String token;
  private String test_upload_1;
  private String test_after_delete_call;
  private byte[] large_file;
  private byte[] wrongType;
  private String expected;
  private String DELETE_WRONG_ID;
  private final String body = "{\"username\":\"user\",\"password\":\"pass\"}";
  private final String urlSignUp = "/v1/users/sign-up";
  private final String loginUrl = "/login";
  private String authorization;
  private final String brokenFileResponse = "{\"code\":\"BAD_REQUEST\",\"message\":\"400 BAD_REQUEST\",\"errors\":[\"File is broken.\"]}";

  @Autowired
  private AuthUserRepository authUserRepository;
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private String uploadFileUrl = "/v1/files/upload-file";
  private String allEntriesUrl = "/v1//files/entries/";

  @Before
  public void before() throws Exception {
    initFileResources();
    authorization = env.getProperty("auth_header");
    mvc.perform(MockMvcRequestBuilders.post(urlSignUp)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
        .andExpect(status().isCreated()).andReturn();
    ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post(loginUrl).content(body))
        .andDo(document("login", responseHeaders(
            headerWithName(authorization).description(
                "The JWT RFC 7519 standard Token, ex: "
                    + "Authorization: Bearer eyJ0eXAiOiJ...."))));
    token = resultActions.andReturn().getResponse().getHeader(authorization);
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
        multipart(uploadFileUrl)
            .file(file)
            .accept(MediaType.APPLICATION_JSON)
            .header(authorization, token))
        .andDo(document("upload", requestParts(
            partWithName("file").description("The file to upload")),
            requestHeaders(headerWithName(authorization).description(
                "The JWT RFC 7519 standard Token, ex: "
                    + "Authorization: Bearer eyJ0eXAiOiJ....")),
            responseFields(fieldWithPath("[]").description("arrays of entries"),
                fieldWithPath("[].id").description("PRIMARY_KEY of the entry"),
                fieldWithPath("[].name").description("PRIMARY_KEY of the entry"),
                fieldWithPath("[].description").description("DESCRIPTION of the entry"),
                fieldWithPath("[].timestamp")
                    .description("TIMESTAMP of the entry in the format of ISO-8601"))
            )
        )
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
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "large_file.txt",
            MediaType.TEXT_PLAIN_VALUE, large_file);
    mvc.perform(
        multipart(uploadFileUrl)
            .file(file)
            .accept(MediaType.APPLICATION_JSON)
            .header(authorization, token)).
        andExpect(content().
            string(
                brokenFileResponse));
  }

  @Test
  public void wrongTypeFile() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "wrong_type_file.pdf",
            MediaType.TEXT_PLAIN_VALUE, wrongType);
    mvc.perform(
        multipart(uploadFileUrl)
            .file(file)
            .accept(MediaType.APPLICATION_JSON)
            .header(authorization, token)).
        andExpect(content().
            string(
                brokenFileResponse));
  }


  @Test
  public void deleteByIdTest() throws Exception {
    uploadFileCall();
    String actual = mvc.perform(MockMvcRequestBuilders.delete(allEntriesUrl+"1")
        .header(authorization, token)
        .contentType(MediaType.TEXT_PLAIN_VALUE))
        .andExpect(status().isOk())
        .andReturn().getResponse()
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
    String actual = mvc.perform(MockMvcRequestBuilders.delete(allEntriesUrl + id)
        .header(authorization, token)
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
    thrown.expect(EmptyResultDataAccessException.class);
    thrown.expectMessage("No class com.luxosft.shapshot.model.Entry entity with id 11111 exists!");
    uploadFileCall();
    int id = 11111;
    try {
      mvc.perform(MockMvcRequestBuilders.delete(allEntriesUrl + id)
          .header(authorization, token));
    } catch (NestedServletException e) {
      throw (Exception) e.getCause();
    }
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
        multipart(uploadFileUrl)
            .file(file)
            .accept(MediaType.APPLICATION_JSON)
            .header(authorization, token))
        .andExpect(status().isCreated()).andReturn().getResponse()
        .getContentAsString(Charset.defaultCharset());
  }
}