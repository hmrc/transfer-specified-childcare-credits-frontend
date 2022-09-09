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

package controllers

import audit.AuditService
import base.SpecBase
import com.dmanchester.playfop.sapi.PlayFop
import generators.Generators
import models.{Address, Index, JourneyModel, Name, Period}
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchersSugar.eqTo
import org.mockito.Mockito.{never, times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.EitherValues
import org.scalatestplus.mockito.MockitoSugar
import pages._
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.domain.Nino

import java.nio.charset.Charset
import java.time.LocalDate

class PrintControllerSpec extends SpecBase with MockitoSugar with Generators with EitherValues {

  "Print Controller" - {

    val childName = Name("Child", "Name")
    val childDob = LocalDate.now.minusYears(2)

    val applicantName = Name("Applicant", "Name")
    val applicantDob = LocalDate.now.minusYears(60)
    val applicantAddress = Address("1 Test Street", None, "Test Town", None, "ZZ1 1ZZ")
    val applicantPhone = "07777777777"
    val applicantNino = arbitrary[Nino].sample.value
    val applicantRelationshipToChild = "grandparent"

    val mainCarerName = Name("Main", "Carer")
    val mainCarerDob = LocalDate.now.minusYears(30)
    val mainCarerAddress = Address("2 Test Street", None, "Test Town", None, "ZZ1 1ZZ")
    val mainCarerPhone = "07777777778"
    val mainCarerNino = arbitrary[Nino].sample.value

    val period = Period(LocalDate.now.minusYears(1), LocalDate.now)

    val answers = emptyUserAnswers
      .set(ChildNamePage, childName).success.value
      .set(ChildDateOfBirthPage, childDob).success.value
      .set(ApplicantNamePage, applicantName).success.value
      .set(ApplicantRelationshipToChildPage, applicantRelationshipToChild).success.value
      .set(ApplicantClaimsChildBenefitForThisChildPage, false).success.value
      .set(ApplicantIsValidAgePage, true).success.value
      .set(ApplicantWasUkResidentPage, true).success.value
      .set(PeriodPage(Index(0)), period).success.value
      .set(ApplicantDateOfBirthPage, applicantDob).success.value
      .set(ApplicantAddressPage, applicantAddress).success.value
      .set(ApplicantTelephoneNumberPage, applicantPhone).success.value
      .set(ApplicantNinoPage, applicantNino).success.value
      .set(MainCarerNamePage, mainCarerName).success.value
      .set(MainCarerDateOfBirthPage, mainCarerDob).success.value
      .set(MainCarerAddressPage, mainCarerAddress).success.value
      .set(MainCarerTelephoneNumberPage, mainCarerPhone).success.value
      .set(MainCarerNinoPage, mainCarerNino).success.value

    val journeyModel = JourneyModel.from(answers).value

    "must return OK and the PDF contents when the user has valid answers" in {

      val mockFop = mock[PlayFop]
      when(mockFop.processTwirlXml(any(), any(), any(), any())) thenReturn "hello".getBytes

      val mockAuditService = mock[AuditService]

      val application = applicationBuilder(userAnswers = Some(answers))
        .overrides(
          bind[PlayFop].toInstance(mockFop),
          bind[AuditService].toInstance(mockAuditService)
        )
        .build()

      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onDownload.url)
        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsBytes(result).decodeString(Charset.defaultCharset()) mustEqual "hello"

        verify(mockAuditService, times(1)).auditDownload(eqTo(journeyModel))(any())
      }
    }

    "must redirect to the journey recovery page when the user does not have valid answers" in {

      val mockAuditService = mock[AuditService]

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers))
        .overrides(
          bind[AuditService].toInstance(mockAuditService)
        )
        .build()

      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onDownload.url)
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url

        verify(mockAuditService, never()).auditDownload(any())(any())
      }
    }
  }
}
