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

package navigation

import base.SpecBase
import controllers.routes
import models._
import org.scalacheck.Arbitrary.arbitrary
import pages._

import java.time.LocalDate

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, NormalMode, emptyUserAnswers) mustBe routes.IndexController.onPageLoad
      }

      "must go from child name page to child date of birth page" in {
        navigator.nextPage(ChildNamePage, NormalMode, emptyUserAnswers) mustBe routes.ChildDateOfBirthController.onPageLoad(NormalMode)
      }

      "must go from child date of birth page to applicant name page" in {
        navigator.nextPage(ChildDateOfBirthPage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantNameController.onPageLoad(NormalMode)
      }

      "must go from applicant name page to applicant relationship to child page" in {
        navigator.nextPage(ApplicantNamePage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantRelationshipToChildController.onPageLoad(NormalMode)
      }

      "must go from applicant relationship to child page to does applicant already receives child benefit for this child page" in {
        navigator.nextPage(ApplicantRelationshipToChildPage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantClaimsChildBenefitForThisChildController.onPageLoad(NormalMode)
      }

      "must go from applicant already receives child benefit for this child page to applicant is partner of claimant page" in {
        navigator.nextPage(ApplicantClaimsChildBenefitForThisChildPage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantIsPartnerOfClaimantController.onPageLoad(NormalMode)
      }

      "must go from applicant is partner of claimant page to applicant is valid age page" in {
        navigator.nextPage(ApplicantIsPartnerOfClaimantPage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantIsValidAgeController.onPageLoad(NormalMode)
      }

      "must go from applicant is valid age page to was applicant UK resident page" in {
        navigator.nextPage(ApplicantIsValidAgePage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantWasUkResidentController.onPageLoad(NormalMode)
      }

      "must go from applicant was UK resident page to does applicant have full NI contributions page" in {
        navigator.nextPage(ApplicantWasUkResidentPage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantHasFullNIContributionsController.onPageLoad(NormalMode)
      }

      "must go from does applicant have full NI contributions page to the first period details page" in {
        navigator.nextPage(ApplicantHasFullNIContributionsPage, NormalMode, emptyUserAnswers) mustBe routes.PeriodController.onPageLoad(NormalMode, Index(0))
      }

      "must go from the period details page" - {

        "to the add period page when index is 0" in {
          navigator.nextPage(PeriodPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.AddPeriodController.onPageLoad(NormalMode)
        }

        "to the add period page when the index is 1" in {
          navigator.nextPage(PeriodPage(Index(1)), NormalMode, emptyUserAnswers) mustBe routes.AddPeriodController.onPageLoad(NormalMode)
        }
      }

      "must go from the add period page" - {

        "to the next period page when the answer is yes" - {

          def arbitraryDate: LocalDate = arbitrary[LocalDate].sample.value

          "when the user has a single period set" in {

            val answers = emptyUserAnswers
              .set(AddPeriodPage, true).success.value
              .set(PeriodPage(Index(0)), Period(arbitraryDate, arbitraryDate)).success.value
            navigator.nextPage(AddPeriodPage, NormalMode, answers) mustBe routes.PeriodController.onPageLoad(NormalMode, Index(1))
          }

          "when the user has multiple periods set" in {

            val answers = emptyUserAnswers
              .set(AddPeriodPage, true).success.value
              .set(PeriodPage(Index(0)), Period(arbitraryDate, arbitraryDate)).success.value
              .set(PeriodPage(Index(1)), Period(arbitraryDate, arbitraryDate)).success.value
            navigator.nextPage(AddPeriodPage, NormalMode, answers) mustBe routes.PeriodController.onPageLoad(NormalMode, Index(2))
          }
        }

        "to the applicant date of birth page when the answer is no" in {
          val answers = emptyUserAnswers.set(AddPeriodPage, false).success.value
          navigator.nextPage(AddPeriodPage, NormalMode, answers) mustBe routes.ApplicantDateOfBirthController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the answer is not present" in {
          navigator.nextPage(AddPeriodPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the remove period page" - {

        "to the first period page when there are no periods added" - {

          "when the index is 0" in {
            navigator.nextPage(RemovePeriodPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.PeriodController.onPageLoad(NormalMode, Index(0))
          }

          "when the index is 1" in {
            navigator.nextPage(RemovePeriodPage(Index(1)), NormalMode, emptyUserAnswers) mustBe routes.PeriodController.onPageLoad(NormalMode, Index(0))
          }
        }

        "to the add period page when there is at least one period added" - {

          val answers = emptyUserAnswers
            .set(PeriodPage(Index(0)), Period(LocalDate.of(2000, 2, 1), LocalDate.of(2001, 3, 2))).success.value

          "when the index is 0" in {
            navigator.nextPage(RemovePeriodPage(Index(0)), NormalMode, answers) mustBe routes.AddPeriodController.onPageLoad(NormalMode)
          }

          "when the index is 1" in {
            navigator.nextPage(RemovePeriodPage(Index(1)), NormalMode, answers) mustBe routes.AddPeriodController.onPageLoad(NormalMode)
          }
        }
      }

      "must go from applicant date of birth page to applicant address page" in {
        navigator.nextPage(ApplicantDateOfBirthPage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantAddressController.onPageLoad(NormalMode)
      }

      "must go from applicant address page to applicant telephone number page" in {
        navigator.nextPage(ApplicantAddressPage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantTelephoneNumberController.onPageLoad(NormalMode)
      }

      "must go from applicant telephone number page to applicant nino page" in {
        navigator.nextPage(ApplicantTelephoneNumberPage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantNinoController.onPageLoad(NormalMode)
      }

      "must go from applicant nino page to main carer name page" in {
        navigator.nextPage(ApplicantNinoPage, NormalMode, emptyUserAnswers) mustBe routes.MainCarerNameController.onPageLoad(NormalMode)
      }

      "must go from main carer name page to main carer date of birth page" in {
        navigator.nextPage(MainCarerNamePage, NormalMode, emptyUserAnswers) mustBe routes.MainCarerDateOfBirthController.onPageLoad(NormalMode)
      }

      "must go from main carer date of birth page to main carer address page" in {
        navigator.nextPage(MainCarerDateOfBirthPage, NormalMode, emptyUserAnswers) mustBe routes.MainCarerAddressController.onPageLoad(NormalMode)
      }

      "must go from main carer address page to main carer telephone number page" in {
        navigator.nextPage(MainCarerAddressPage, NormalMode, emptyUserAnswers) mustBe routes.MainCarerTelephoneNumberController.onPageLoad(NormalMode)
      }

      "must go from main carer telephone number page to main carer nino page" in {
        navigator.nextPage(MainCarerTelephoneNumberPage, NormalMode, emptyUserAnswers) mustBe routes.MainCarerNinoController.onPageLoad(NormalMode)
      }

      "must go from main carer nino page to check your answers page" in {
        navigator.nextPage(MainCarerNinoPage, NormalMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }
  }
}
