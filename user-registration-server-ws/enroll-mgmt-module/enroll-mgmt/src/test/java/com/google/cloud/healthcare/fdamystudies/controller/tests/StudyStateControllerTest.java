package com.google.cloud.healthcare.fdamystudies.controller.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.healthcare.fdamystudies.beans.StudiesBean;
import com.google.cloud.healthcare.fdamystudies.beans.StudyStateBean;
import com.google.cloud.healthcare.fdamystudies.beans.StudyStateReqBean;
import com.google.cloud.healthcare.fdamystudies.beans.StudyStateResponse;
import com.google.cloud.healthcare.fdamystudies.beans.WithdrawFromStudyBean;
import com.google.cloud.healthcare.fdamystudies.common.ApiEndpoint;
import com.google.cloud.healthcare.fdamystudies.common.BaseMockIT;
import com.google.cloud.healthcare.fdamystudies.common.JsonUtils;
import com.google.cloud.healthcare.fdamystudies.controller.StudyStateController;
import com.google.cloud.healthcare.fdamystudies.service.StudyStateService;
import com.google.cloud.healthcare.fdamystudies.testutils.Constants;
import com.google.cloud.healthcare.fdamystudies.testutils.TestUtils;
import com.google.cloud.healthcare.fdamystudies.util.AppConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudyStateControllerTest extends BaseMockIT {

  @Autowired private StudyStateController controller;
  @Autowired private StudyStateService studyStateService;

  @Autowired private ObjectMapper objectMapper;

  @Autowired protected MockMvc mockMvc;

  protected ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  @Test
  public void contextLoads() {
    assertNotNull(controller);
    assertNotNull(mockMvc);
    assertNotNull(studyStateService);
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void updateStudyStateSuccess() throws Exception {

    StudiesBean studiesBean =
        new StudiesBean(
            Constants.STUDYOF_HEALTH,
            Constants.BOOKMARKED,
            Constants.COMPLETION,
            Constants.ADHERENCE);

    List<StudiesBean> listStudies = new ArrayList<StudiesBean>();
    listStudies.add(studiesBean);

    String requestJson = getStudyStateJson(listStudies);

    HttpHeaders headers = TestUtils.getCommonHeaders();
    headers.add(Constants.USER_ID_HEADER, Constants.VALID_USER_ID);

    mockMvc
        .perform(
            post(ApiEndpoint.UPDATE_STUDY_STATE_PATH.getPath())
                .content(requestJson)
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isOk());

    MvcResult result =
        mockMvc
            .perform(
                get(ApiEndpoint.STUDY_STATE_PATH.getPath())
                    .content(JsonUtils.asJsonString(requestJson))
                    .headers(headers)
                    .contextPath(getContextPath()))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    StudyStateResponse response =
        getObjectMapper()
            .readValue(result.getResponse().getContentAsString(), StudyStateResponse.class);

    Optional<StudyStateBean> study =
        response
            .getStudies()
            .stream()
            .filter(s -> s.getStudyId().equals(Constants.STUDYOF_HEALTH))
            .findFirst();

    assertTrue(study.isPresent());
    assertTrue(study.get().getBookmarked());
    assertEquals(Constants.COMPLETION, study.get().getCompletion());
    assertEquals(Constants.ADHERENCE, study.get().getAdherence());
  }

  @Test
  public void updateStudyStateFailure() throws Exception {

    StudiesBean studiesBean =
        new StudiesBean(
            Constants.STUDYOF_HEALTH,
            Constants.BOOKMARKED,
            Constants.COMPLETION,
            Constants.ADHERENCE);

    List<StudiesBean> listStudies = new ArrayList<StudiesBean>();
    listStudies.add(studiesBean);

    String requestJson = getStudyStateJson(listStudies);

    HttpHeaders headers = TestUtils.getCommonHeaders();

    // not valid user id
    headers.set(Constants.USER_ID_HEADER, Constants.INVALID_USER_ID);

    mockMvc
        .perform(
            post(ApiEndpoint.UPDATE_STUDY_STATE_PATH.getPath())
                .content(requestJson)
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isBadRequest());

    // empty studylist
    listStudies = new ArrayList<StudiesBean>();
    requestJson = getStudyStateJson(listStudies);
    headers.set(Constants.USER_ID_HEADER, Constants.VALID_USER_ID);

    mockMvc
        .perform(
            post(ApiEndpoint.UPDATE_STUDY_STATE_PATH.getPath())
                .content(requestJson)
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void getStudyStateSuccess() throws Exception {

    HttpHeaders headers = TestUtils.getCommonHeaders();
    headers.add(Constants.USER_ID_HEADER, Constants.VALID_USER_ID);

    mockMvc
        .perform(
            get(ApiEndpoint.STUDY_STATE_PATH.getPath())
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void getStudyStateUnauthorizedUserId() throws Exception {

    HttpHeaders headers = TestUtils.getCommonHeaders();
    headers.add(Constants.USER_ID_HEADER, Constants.INVALID_USER_ID);
    mockMvc
        .perform(
            get(ApiEndpoint.STUDY_STATE_PATH.getPath())
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void withdrawFromStudySuccess() throws Exception {

    HttpHeaders headers = TestUtils.getCommonHeaders();
    headers.add(Constants.USER_ID_HEADER, Constants.VALID_USER_ID);

    String requestJson =
        getWithDrawJson(
            Constants.PARTICIPANT_ID, Constants.STUDY_ID_OF_PARTICIPANT, Constants.DELETE);

    mockMvc
        .perform(
            post(ApiEndpoint.WITHDRAW_FROM_STUDY_PATH.getPath())
                .content(requestJson)
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isOk());

    MvcResult result =
        mockMvc
            .perform(
                get(ApiEndpoint.STUDY_STATE_PATH.getPath())
                    .headers(headers)
                    .contextPath(getContextPath()))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    StudyStateResponse response =
        getObjectMapper()
            .readValue(result.getResponse().getContentAsString(), StudyStateResponse.class);
    Optional<StudyStateBean> study =
        response
            .getStudies()
            .stream()
            .filter(s -> s.getParticipantId().equals(Constants.PARTICIPANT_ID))
            .findFirst();

    assertTrue(study.isPresent());
    assertEquals(AppConstants.WITHDRAWN, study.get().getStatus());
  }

  @Test
  public void withdrawFromStudyFailure() throws Exception {

    // empty participant Id
    HttpHeaders headers = TestUtils.getCommonHeaders();
    headers.add(Constants.USER_ID_HEADER, Constants.VALID_USER_ID);

    String requestJson = getWithDrawJson("", Constants.STUDY_ID_OF_PARTICIPANT, Constants.DELETE);

    mockMvc
        .perform(
            post(ApiEndpoint.WITHDRAW_FROM_STUDY_PATH.getPath())
                .content(requestJson)
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isBadRequest());

    // empty study Id
    requestJson = getWithDrawJson(Constants.PARTICIPANT_ID, "", Constants.DELETE);

    mockMvc
        .perform(
            post(ApiEndpoint.WITHDRAW_FROM_STUDY_PATH.getPath())
                .content(requestJson)
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isBadRequest());

    // study Id not exists
    requestJson =
        getWithDrawJson(Constants.PARTICIPANT_ID, Constants.STUDYID_NOT_EXIST, Constants.DELETE);

    mockMvc
        .perform(
            post(ApiEndpoint.WITHDRAW_FROM_STUDY_PATH.getPath())
                .content(requestJson)
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  private String getWithDrawJson(String participatId, String studyId, boolean delete)
      throws JsonProcessingException {
    WithdrawFromStudyBean withdrawFromStudyBean =
        new WithdrawFromStudyBean(participatId, studyId, delete);
    return getObjectMapper().writeValueAsString(withdrawFromStudyBean);
  }

  private String getStudyStateJson(List<StudiesBean> listStudies) throws JsonProcessingException {
    StudyStateReqBean studyStateReqBean = new StudyStateReqBean(listStudies);
    return getObjectMapper().writeValueAsString(studyStateReqBean);
  }
}