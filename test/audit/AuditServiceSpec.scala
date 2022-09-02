/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package audit

import cats.data.NonEmptyList
import generators.Generators
import models.JourneyModel.{Applicant, MainCarer}
import org.scalacheck.Arbitrary.arbitrary
import models.{Address, ApplicantHasFullNIContributions, ApplicantRelationshipToChild, Child, JourneyModel, Name, Period}
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchersSugar.eqTo
import org.mockito.Mockito.{times, verify}
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

import scala.concurrent.ExecutionContext.Implicits.global
import java.time.LocalDate

class AuditServiceSpec extends AnyFreeSpec with Matchers with MockitoSugar with Generators with OptionValues {

  "auditDownload" - {

    val mockAuditConnector: AuditConnector = mock[AuditConnector]
    val configuration: Configuration = Configuration(
      "auditing.downloadEventName" -> "downloadAuditEvent"
    )
    val service = new AuditService(mockAuditConnector, configuration)

    val childName = Name("Child", "Name")
    val childDob = LocalDate.now.minusYears(2)

    val applicantName = Name("Applicant", "Name")
    val applicantDob = LocalDate.now.minusYears(60)
    val applicantAddress = Address("1 Test Street", None, "Test Town", None, "ZZ1 1ZZ")
    val applicantPhone = "07777777777"
    val applicantNino = arbitrary[Nino].sample.value

    val mainCarerName = Name("Main", "Carer")
    val mainCarerDob = LocalDate.now.minusYears(30)
    val mainCarerAddress = Address("2 Test Street", None, "Test Town", None, "ZZ1 1ZZ")
    val mainCarerPhone = "07777777778"
    val mainCarerNino = arbitrary[Nino].sample.value

    val period = Period(LocalDate.now.minusYears(1), LocalDate.now)

    "must call the audit connector with the correct data event" in {

      val model = JourneyModel(
        child = Child(
          name = childName,
          dateOfBirth = childDob
        ),
        applicant = Applicant(
          name = applicantName,
          dateOfBirth = applicantDob,
          relationshipToChild = ApplicantRelationshipToChild.Grandparent,
          fullNiContributions = ApplicantHasFullNIContributions.No,
          address = applicantAddress,
          telephoneNumber = applicantPhone,
          nino = applicantNino
        ),
        mainCarer = MainCarer(
          name = mainCarerName,
          dateOfBirth = mainCarerDob,
          address = mainCarerAddress,
          telephoneNumber = mainCarerPhone,
          nino = mainCarerNino
        ),
        periods = NonEmptyList.one(period)
      )

      val expected = DownloadAuditEvent(
        child = DownloadAuditEvent.Child(
          firstName = childName.firstName,
          lastName = childName.lastName,
          dateOfBirth = childDob
        ),
        applicant = DownloadAuditEvent.Applicant(
          firstName = applicantName.firstName,
          lastName = applicantName.lastName,
          dateOfBirth = applicantDob,
          relationshipToChild = "grandparent",
          fullNiContributions = ApplicantHasFullNIContributions.No,
          address = DownloadAuditEvent.Address(
            line1 = applicantAddress.line1,
            line2 = applicantAddress.line2,
            townOrCity = applicantAddress.townOrCity,
            county = applicantAddress.county,
            postcode = applicantAddress.postcode
          ),
          telephoneNumber = applicantPhone,
          nino = applicantNino
        ),
        mainCarer = DownloadAuditEvent.MainCarer(
          firstName = mainCarerName.firstName,
          lastName = mainCarerName.lastName,
          dateOfBirth = mainCarerDob,
          address = DownloadAuditEvent.Address(
            line1 = mainCarerAddress.line1,
            line2 = mainCarerAddress.line2,
            townOrCity = mainCarerAddress.townOrCity,
            county = mainCarerAddress.county,
            postcode = mainCarerAddress.postcode
          ),
          telephoneNumber = mainCarerPhone,
          nino = mainCarerNino
        ),
        periods = List(DownloadAuditEvent.Period(period.startDate, period.endDate))
      )

      val hc = HeaderCarrier()
      service.auditDownload(model)(hc)

      verify(mockAuditConnector, times(1)).sendExplicitAudit(eqTo("downloadAuditEvent"), eqTo(expected))(eqTo(hc), any(), any())
    }

    "must call the audit connector with the correct data event when the applicants relationship to the child is other" in {

      val model = JourneyModel(
        child = Child(
          name = childName,
          dateOfBirth = childDob
        ),
        applicant = Applicant(
          name = applicantName,
          dateOfBirth = applicantDob,
          relationshipToChild = ApplicantRelationshipToChild.Other("foobar"),
          fullNiContributions = ApplicantHasFullNIContributions.No,
          address = applicantAddress,
          telephoneNumber = applicantPhone,
          nino = applicantNino
        ),
        mainCarer = MainCarer(
          name = mainCarerName,
          dateOfBirth = mainCarerDob,
          address = mainCarerAddress,
          telephoneNumber = mainCarerPhone,
          nino = mainCarerNino
        ),
        periods = NonEmptyList.one(period)
      )

      val expected = DownloadAuditEvent(
        child = DownloadAuditEvent.Child(
          firstName = childName.firstName,
          lastName = childName.lastName,
          dateOfBirth = childDob
        ),
        applicant = DownloadAuditEvent.Applicant(
          firstName = applicantName.firstName,
          lastName = applicantName.lastName,
          dateOfBirth = applicantDob,
          relationshipToChild = "foobar",
          fullNiContributions = ApplicantHasFullNIContributions.No,
          address = DownloadAuditEvent.Address(
            line1 = applicantAddress.line1,
            line2 = applicantAddress.line2,
            townOrCity = applicantAddress.townOrCity,
            county = applicantAddress.county,
            postcode = applicantAddress.postcode
          ),
          telephoneNumber = applicantPhone,
          nino = applicantNino
        ),
        mainCarer = DownloadAuditEvent.MainCarer(
          firstName = mainCarerName.firstName,
          lastName = mainCarerName.lastName,
          dateOfBirth = mainCarerDob,
          address = DownloadAuditEvent.Address(
            line1 = mainCarerAddress.line1,
            line2 = mainCarerAddress.line2,
            townOrCity = mainCarerAddress.townOrCity,
            county = mainCarerAddress.county,
            postcode = mainCarerAddress.postcode
          ),
          telephoneNumber = mainCarerPhone,
          nino = mainCarerNino
        ),
        periods = List(DownloadAuditEvent.Period(period.startDate, period.endDate))
      )

      val hc = HeaderCarrier()
      service.auditDownload(model)(hc)

      verify(mockAuditConnector, times(1)).sendExplicitAudit(eqTo("downloadAuditEvent"), eqTo(expected))(eqTo(hc), any(), any())
    }
  }
}
