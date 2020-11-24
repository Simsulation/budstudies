/*
 * Copyright 2020 Google LLC
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package com.google.cloud.healthcare.fdamystudies.mapper;

import com.google.cloud.healthcare.fdamystudies.beans.AppSiteResponse;
import com.google.cloud.healthcare.fdamystudies.beans.AuditLogEventRequest;
import com.google.cloud.healthcare.fdamystudies.beans.SiteResponse;
import com.google.cloud.healthcare.fdamystudies.model.InviteParticipantEntity;
import com.google.cloud.healthcare.fdamystudies.model.SiteEntity;

public class SiteMapper {

  private SiteMapper() {}

  public static SiteResponse toSiteResponse(SiteEntity site) {
    SiteResponse response = new SiteResponse();
    response.setSiteId(site.getId());
    return response;
  }

  public static AppSiteResponse toAppSiteResponse(SiteEntity site) {
    AppSiteResponse appSiteResponse = new AppSiteResponse();
    appSiteResponse.setSiteId(site.getId());
    appSiteResponse.setCustomLocationId(site.getLocation().getCustomId());
    appSiteResponse.setLocationDescription(site.getLocation().getDescription());
    appSiteResponse.setLocationId(site.getLocation().getId());
    appSiteResponse.setLocationName(site.getLocation().getName());
    return appSiteResponse;
  }

  public static InviteParticipantEntity toInviteParticipantEntity(
      AuditLogEventRequest auditRequest) {
    InviteParticipantEntity inviteParticipantsEmail = new InviteParticipantEntity();
    inviteParticipantsEmail.setStudy(auditRequest.getStudyId());
    inviteParticipantsEmail.setAppVersion(auditRequest.getAppVersion());
    inviteParticipantsEmail.setCorrelationId(auditRequest.getCorrelationId());
    inviteParticipantsEmail.setSource(auditRequest.getSource());
    inviteParticipantsEmail.setMobilePlatform(auditRequest.getMobilePlatform());
    inviteParticipantsEmail.setUserId(auditRequest.getUserId());
    return inviteParticipantsEmail;
  }

  public static AuditLogEventRequest prepareAuditlogRequest(
      InviteParticipantEntity inviteParticipantDetails) {
    AuditLogEventRequest auditRequest = new AuditLogEventRequest();
    auditRequest.setAppId(inviteParticipantDetails.getAppId());
    auditRequest.setAppVersion(inviteParticipantDetails.getAppVersion());
    auditRequest.setCorrelationId(inviteParticipantDetails.getCorrelationId());
    auditRequest.setSource(inviteParticipantDetails.getSource());
    auditRequest.setMobilePlatform(inviteParticipantDetails.getMobilePlatform());
    auditRequest.setUserId(inviteParticipantDetails.getUserId());
    return auditRequest;
  }
}
