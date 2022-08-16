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

import javax.inject.{Inject, Singleton}
import play.api.mvc.Call
import controllers.routes
import pages._
import models._
import queries.PeriodsQuery

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case ChildNamePage => _ => routes.ChildDateOfBirthController.onPageLoad(NormalMode)
    case ChildDateOfBirthPage => _ => routes.ApplicantRelationshipToChildController.onPageLoad(NormalMode)
    case ApplicantRelationshipToChildPage => _ => routes.ApplicantClaimsChildBenefitForThisChildController.onPageLoad(NormalMode)
    case ApplicantClaimsChildBenefitForThisChildPage => _ => routes.ApplicantIsPartnerOfClaimantController.onPageLoad(NormalMode)
    case ApplicantIsPartnerOfClaimantPage => _ => routes.ApplicantChildcareAfterCutoffController.onPageLoad(NormalMode)
    case ApplicantChildcareAfterCutoffPage => _ => routes.ApplicantIsValidAgeController.onPageLoad(NormalMode)
    case ApplicantIsValidAgePage => _ => routes.ApplicantWasUkResidentController.onPageLoad(NormalMode)
    case ApplicantWasUkResidentPage => _ => routes.WasChildUnder12YearsOldController.onPageLoad(NormalMode)
    case WasChildUnder12YearsOldPage => _ => routes.ApplicantHasFullNIContributionsController.onPageLoad(NormalMode)
    case ApplicantHasFullNIContributionsPage => _ => routes.PeriodController.onPageLoad(NormalMode, Index(0))
    case PeriodPage(_) => _ => routes.AddPeriodController.onPageLoad(NormalMode)
    case AddPeriodPage => addPeriodRoutes
    case RemovePeriodPage(_) => _ => routes.AddPeriodController.onPageLoad(NormalMode)
    case ApplicantNamePage => _ => routes.ApplicantDateOfBirthController.onPageLoad(NormalMode)
    case ApplicantDateOfBirthPage => _ => routes.ApplicantAddressController.onPageLoad(NormalMode)
    case ApplicantAddressPage => _ => routes.ApplicntTelephoneNumberController.onPageLoad(NormalMode)
    case ApplicntTelephoneNumberPage => _ => routes.ApplicantNinoController.onPageLoad(NormalMode)
    case ApplicantNinoPage => _ => routes.MainCarerNameController.onPageLoad(NormalMode)
    case MainCarerNamePage => _ => routes.MainCarerDateOfBirthController.onPageLoad(NormalMode)
    case MainCarerDateOfBirthPage => _ => routes.MainCarerAddressController.onPageLoad(NormalMode)
    case MainCarerAddressPage => _ => routes.MainCarerTelephoneNumberController.onPageLoad(NormalMode)
    case MainCarerTelephoneNumberPage => _ => routes.MainCarerNinoController.onPageLoad(NormalMode)
    case MainCarerNinoPage => _ => routes.CheckYourAnswersController.onPageLoad
    case _ => _ => routes.IndexController.onPageLoad
  }

  private def addPeriodRoutes(answers: UserAnswers): Call =
    answers.get(AddPeriodPage).map {
      case true =>
        val index = answers.get(PeriodsQuery).getOrElse(Nil).length
        routes.PeriodController.onPageLoad(NormalMode, Index(index))
      case false =>
        routes.ApplicantNameController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private val checkRouteMap: Page => UserAnswers => Call = {
    case _ => _ => routes.CheckYourAnswersController.onPageLoad
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
