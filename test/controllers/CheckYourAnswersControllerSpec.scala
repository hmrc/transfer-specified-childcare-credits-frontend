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

import base.SpecBase
import generators.Generators
import models.{Address, ApplicantRelationshipToChild, CheckMode, Index, Name, Period}
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.i18n.Messages
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.domain.Nino
import viewmodels.checkAnswers._
import viewmodels.govuk.SummaryListFluency
import views.html.CheckYourAnswersView

import java.time.LocalDate

class CheckYourAnswersControllerSpec extends SpecBase with SummaryListFluency with Generators {

  "Check Your Answers Controller" - {

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

    val answers = emptyUserAnswers
      .set(ChildNamePage, childName).success.value
      .set(ChildDateOfBirthPage, childDob).success.value
      .set(ApplicantNamePage, applicantName).success.value
      .set(ApplicantRelationshipToChildPage, ApplicantRelationshipToChild.Grandparent).success.value
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

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        implicit val m: Messages = messages(application)
        val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad.url)
        val result = route(application, request).value
        val view = application.injector.instanceOf[CheckYourAnswersView]

        val childDetails = SummaryListViewModel(Seq(
          ChildNameSummary.row(answers).value,
          ChildDateOfBirthSummary.row(answers).value
        ))

        val applicantDetails = SummaryListViewModel(Seq(
          ApplicantNameSummary.row(answers).value,
          ApplicantDateOfBirthSummary.row(answers).value,
          ApplicantAddressSummary.row(answers).value,
          ApplicantTelephoneNumberSummary.row(answers).value,
          ApplicantNinoSummary.row(answers).value
        ))

        val periods = AddPeriodSummary.rows(answers, CheckMode)

        val mainCarerDetails = SummaryListViewModel(Seq(
          MainCarerNameSummary.row(answers).value,
          MainCarerDateOfBirthSummary.row(answers).value,
          MainCarerAddressSummary.row(answers).value,
          MainCarerTelephoneNumberSummary.row(answers).value,
          MainCarerNinoSummary.row(answers).value
        ))

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(childDetails, applicantDetails, periods, mainCarerDetails)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad.url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
