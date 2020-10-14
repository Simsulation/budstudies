/*
 * Copyright 2020 Google LLC
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package com.google.cloud.healthcare.fdamystudies.repository;

import com.google.cloud.healthcare.fdamystudies.model.AuthInfoEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
    value = "participant.manager.repository.enabled",
    havingValue = "true",
    matchIfMissing = false)
public interface AuthInfoRepository extends JpaRepository<AuthInfoEntity, String> {

  @Query(
      "SELECT a FROM UserAppDetailsEntity u,AuthInfoEntity a where u.userDetails = a.userDetails and u.app.id in (?1) and a.remoteNotificationFlag=1 and "
          + "(a.deviceToken is not NULL and a.deviceToken != '' and a.deviceType is not NULL and a.deviceType != '') ")
  public List<AuthInfoEntity> findDevicesTokens(List<String> appInfoIds);
}