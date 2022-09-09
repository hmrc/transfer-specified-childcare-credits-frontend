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

package controllers.actions

import base.SpecBase
import generators.Generators
import models.requests.DataRequest
import models.{Address, CheckMode, Index, Name, Period}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.concurrent.ScalaFutures
import pages._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.AnyContent
import play.api.mvc.Results.Ok
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.domain.Nino

import java.time.LocalDate
import scala.concurrent.Future

class JourneyModelFilterSpec extends SpecBase with ScalaFutures with Generators {

  "JourneyModelFilter" - {

    lazy val app = new GuiceApplicationBuilder()
      .build()

    lazy val filter = app.injector.instanceOf[JourneyModelFilter]

    "must not filter requests which contain data for a complete journey model" in {

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

      val completedDataRequest = DataRequest[AnyContent](FakeRequest(), "id", answers)
      val result = filter.invokeBlock[AnyContent](completedDataRequest, _ => Future.successful(Ok))
      status(result) mustEqual OK
    }

    "must redirect to the first page in the journey which fails" in {

      val completedDataRequest = DataRequest[AnyContent](FakeRequest(), "id", emptyUserAnswers)
      val result = filter.invokeBlock[AnyContent](completedDataRequest, _ => Future.successful(Ok))
      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual controllers.routes.ApplicantClaimsChildBenefitForThisChildController.onPageLoad(CheckMode).toString
    }
  }
}