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

import controllers.routes
import models._
import pages._
import play.api.mvc.Call
import queries.PeriodsQuery

import javax.inject.{Inject, Singleton}

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case ApplicantNamePage => _ => routes.ApplicantDateOfBirthController.onPageLoad(NormalMode)
    case ApplicantDateOfBirthPage => _ => routes.ChildNameController.onPageLoad(NormalMode)
    case ChildNamePage => _ => routes.ChildDateOfBirthController.onPageLoad(NormalMode)
    case ChildDateOfBirthPage => _ => routes.ApplicantClaimsChildBenefitForThisChildController.onPageLoad(NormalMode)
    case ApplicantClaimsChildBenefitForThisChildPage => applicantClaimsChildBenefitForThisChildRoutes
    case ApplicantWasUkResidentPage => applicantWasUkResidentRoutes
    case ApplicantIsValidAgePage => applicantIsValidAgeRoutes
    case PeriodPage(_) => _ => routes.AddPeriodController.onPageLoad(NormalMode)
    case AddPeriodPage => addPeriodRoutes
    case RemovePeriodPage(_) => removePeriodRoutes
    case ApplicantRelationshipToChildPage => _ => routes.ApplicantAddressController.onPageLoad(NormalMode)
    case ApplicantAddressPage => _ => routes.ApplicantTelephoneNumberController.onPageLoad(NormalMode)
    case ApplicantTelephoneNumberPage => _ => routes.ApplicantNinoController.onPageLoad(NormalMode)
    case ApplicantNinoPage => _ => routes.MainCarerNameController.onPageLoad(NormalMode)
    case MainCarerNamePage => _ => routes.MainCarerDateOfBirthController.onPageLoad(NormalMode)
    case MainCarerDateOfBirthPage => _ => routes.MainCarerAddressController.onPageLoad(NormalMode)
    case MainCarerAddressPage => _ => routes.MainCarerTelephoneNumberController.onPageLoad(NormalMode)
    case MainCarerTelephoneNumberPage => _ => routes.MainCarerNinoController.onPageLoad(NormalMode)
    case MainCarerNinoPage => _ => routes.CheckYourAnswersController.onPageLoad
    case _ => _ => routes.IndexController.onPageLoad
  }

  private def applicantClaimsChildBenefitForThisChildRoutes(answers: UserAnswers): Call =
    answers.get(ApplicantClaimsChildBenefitForThisChildPage).map {
      case true  => routes.KickOutIneligibleController.onPageLoad()
      case false => routes.ApplicantWasUkResidentController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def applicantWasUkResidentRoutes(answers: UserAnswers): Call =
    answers.get(ApplicantWasUkResidentPage).map {
      case true  => routes.ApplicantIsValidAgeController.onPageLoad(NormalMode)
      case false => routes.KickOutIneligibleController.onPageLoad()
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def applicantIsValidAgeRoutes(answers: UserAnswers): Call =
    answers.get(ApplicantIsValidAgePage).map {
      case true  => routes.PeriodController.onPageLoad(NormalMode, Index(0))
      case false => routes.KickOutIneligibleController.onPageLoad()
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def addPeriodRoutes(answers: UserAnswers): Call =
    answers.get(AddPeriodPage).map {
      case true =>
        val index = answers.get(PeriodsQuery).getOrElse(Nil).length
        routes.PeriodController.onPageLoad(NormalMode, Index(index))
      case false =>
        routes.ApplicantRelationshipToChildController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def removePeriodRoutes(answers: UserAnswers): Call = {
    val periods = answers.get(PeriodsQuery).getOrElse(List.empty)
    if (periods.isEmpty) {
      routes.PeriodController.onPageLoad(NormalMode, Index(0))
    } else {
      routes.AddPeriodController.onPageLoad(NormalMode)
    }
  }

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
