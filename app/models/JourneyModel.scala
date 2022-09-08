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

import cats.data.{EitherNec, NonEmptyChain, NonEmptyList}
import cats.implicits._
import models.JourneyModel._
import pages._
import queries.{PeriodsQuery, Query}
import uk.gov.hmrc.domain.Nino

import java.time.LocalDate

final case class JourneyModel(
                               child: Child,
                               applicant: Applicant,
                               mainCarer: MainCarer,
                               periods: NonEmptyList[Period]
                             )

object JourneyModel {

  final case class Applicant(
                              name: Name,
                              dateOfBirth: LocalDate,
                              relationshipToChild: ApplicantRelationshipToChild,
                              address: Address,
                              telephoneNumber: String,
                              nino: Nino
                            )

  final case class MainCarer(
                              name: Name,
                              dateOfBirth: LocalDate,
                              address: Address,
                              telephoneNumber: String,
                              nino: Nino
                            )

  def from(answers: UserAnswers): EitherNec[Query, JourneyModel] =
    getEligibility(answers) >> getData(answers)

  private def getEligibility(answers: UserAnswers): EitherNec[Query, Unit] = (
    getApplicantClaimsChildBenefitEligibility(answers),
    getApplicantValidAgeEligibility(answers),
    getApplicantResidencyEligibility(answers)
  ).parTupled.void

  private def getApplicantClaimsChildBenefitEligibility(answers: UserAnswers): EitherNec[Query, Unit] =
    answers.getNec(ApplicantClaimsChildBenefitForThisChildPage).flatMap { applicantIsClaimant =>
      if (applicantIsClaimant) ApplicantClaimsChildBenefitForThisChildPage.leftNec else Right(())
    }

  private def getApplicantValidAgeEligibility(answers: UserAnswers): EitherNec[Query, Unit] =
    answers.getNec(ApplicantIsValidAgePage).flatMap { applicantIsValidAge =>
      if (!applicantIsValidAge) ApplicantIsValidAgePage.leftNec else Right(())
    }

  private def getApplicantResidencyEligibility(answers: UserAnswers): EitherNec[Query, Unit] =
    answers.getNec(ApplicantWasUkResidentPage).flatMap { applicantWasUkResident =>
      if (!applicantWasUkResident) ApplicantWasUkResidentPage.leftNec else Right(())
    }

  private def getData(answers: UserAnswers): EitherNec[Query, JourneyModel] = (
    getChild(answers),
    getApplicant(answers),
    getMainCarer(answers),
    getPeriods(answers)
  ).parMapN(JourneyModel.apply)

  private def getChild(answers: UserAnswers): EitherNec[Query, Child] = (
    answers.getNec(ChildNamePage),
    answers.getNec(ChildDateOfBirthPage)
  ).parMapN(Child)

  private def getApplicant(answers: UserAnswers): EitherNec[Query, Applicant] = (
    answers.getNec(ApplicantNamePage),
    answers.getNec(ApplicantDateOfBirthPage),
    getApplicantRelationshipToChild(answers),
    answers.getNec(ApplicantAddressPage),
    answers.getNec(ApplicantTelephoneNumberPage),
    answers.getNec(ApplicantNinoPage)
  ).parMapN(Applicant)

  private def getApplicantRelationshipToChild(answers: UserAnswers): EitherNec[Query, ApplicantRelationshipToChild] = {
    import ApplicantRelationshipToChild._
    answers.getNec(ApplicantRelationshipToChildPage).flatMap {
      case GreatAuntOrGreatUncle | ResidentPartner =>
        Left(NonEmptyChain.one(ApplicantRelationshipToChildPage))
      case value =>
        Right(value)
    }
  }

  private def getMainCarer(answers: UserAnswers): EitherNec[Query, MainCarer] = (
    answers.getNec(MainCarerNamePage),
    answers.getNec(MainCarerDateOfBirthPage),
    answers.getNec(MainCarerAddressPage),
    answers.getNec(MainCarerTelephoneNumberPage),
    answers.getNec(MainCarerNinoPage)
  ).parMapN(MainCarer)

  private def getPeriods(answers: UserAnswers): EitherNec[Query, NonEmptyList[Period]] =
    answers.getNec(PeriodsQuery).flatMap { periods =>
      NonEmptyList.fromList(periods)
        .toRight(NonEmptyChain.one(PeriodPage(Index(0))))
    }
}
