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

package models

import cats.data.{NonEmptyChain, NonEmptyList}
import generators.Generators
import models.JourneyModel._
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{EitherValues, OptionValues}
import pages._
import uk.gov.hmrc.domain.Nino

import java.time.LocalDate

class JourneyModelSpec extends AnyFreeSpec with Matchers with OptionValues with EitherValues with Generators {

  "from" - {

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

    val completeAnswers = UserAnswers("id")
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

    "must return a journey model when given complete answers" in {

      val expected = JourneyModel(
        child = Child(
          name = childName,
          dateOfBirth = childDob
        ),
        applicant = Applicant(
          name = applicantName,
          dateOfBirth = applicantDob,
          relationshipToChild = applicantRelationshipToChild,
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

      JourneyModel.from(completeAnswers).value mustEqual expected
    }

    "must fail when there are no periods" in {
      val answers = completeAnswers.remove(PeriodPage(Index(0))).success.value
      JourneyModel.from(answers).swap.value mustEqual NonEmptyChain.one(PeriodPage(Index(0)))
    }

    "must fail when the applicant already receives child benefit for the child" in {
      val answers = completeAnswers.set(ApplicantClaimsChildBenefitForThisChildPage, true).success.value
      JourneyModel.from(answers).swap.value mustEqual NonEmptyChain.one(ApplicantClaimsChildBenefitForThisChildPage)
    }

    "must fail when the applicant's age is invalid" in {
      val answers = completeAnswers.set(ApplicantIsValidAgePage, false).success.value
      JourneyModel.from(answers).swap.value mustEqual NonEmptyChain.one(ApplicantIsValidAgePage)
    }

    "must fail when the applicant was not a UK resident when caring for the child" in {
      val answers = completeAnswers.set(ApplicantWasUkResidentPage, false).success.value
      JourneyModel.from(answers).swap.value mustEqual NonEmptyChain.one(ApplicantWasUkResidentPage)
    }
  }
}
