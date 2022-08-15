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

package generators

import models.Index
import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

  implicit lazy val arbitraryMainCarerNinoPage: Arbitrary[MainCarerNinoPage.type] =
    Arbitrary(MainCarerNinoPage)

  implicit lazy val arbitraryMainCarerTelephoneNumberPage: Arbitrary[MainCarerTelephoneNumberPage.type] =
    Arbitrary(MainCarerTelephoneNumberPage)

  implicit lazy val arbitraryMainCarerAddressPage: Arbitrary[MainCarerAddressPage.type] =
    Arbitrary(MainCarerAddressPage)

  implicit lazy val arbitraryMainCarerDateOfBirthPage: Arbitrary[MainCarerDateOfBirthPage.type] =
    Arbitrary(MainCarerDateOfBirthPage)

  implicit lazy val arbitraryMainCarerNamePage: Arbitrary[MainCarerNamePage.type] =
    Arbitrary(MainCarerNamePage)

  implicit lazy val arbitraryApplicantNinoPage: Arbitrary[ApplicantNinoPage.type] =
    Arbitrary(ApplicantNinoPage)

  implicit lazy val arbitraryApplicntTelephoneNumberPage: Arbitrary[ApplicntTelephoneNumberPage.type] =
    Arbitrary(ApplicntTelephoneNumberPage)

  implicit lazy val arbitraryApplicantAddressPage: Arbitrary[ApplicantAddressPage.type] =
    Arbitrary(ApplicantAddressPage)

  implicit lazy val arbitraryApplicantDateOfBirthPage: Arbitrary[ApplicantDateOfBirthPage.type] =
    Arbitrary(ApplicantDateOfBirthPage)

  implicit lazy val arbitraryApplicantNamePage: Arbitrary[ApplicantNamePage.type] =
    Arbitrary(ApplicantNamePage)

  implicit lazy val arbitraryRemovePeriodPage: Arbitrary[RemovePeriodPage.type] =
    Arbitrary(RemovePeriodPage)

  implicit lazy val arbitraryAddPeriodPage: Arbitrary[AddPeriodPage.type] =
    Arbitrary(AddPeriodPage)

  implicit lazy val arbitraryPeriodPage: Arbitrary[PeriodPage] =
    Arbitrary(PeriodPage(Index(0)))

  implicit lazy val arbitraryChildDateOfBirthPage: Arbitrary[ChildDateOfBirthPage.type] =
    Arbitrary(ChildDateOfBirthPage)

  implicit lazy val arbitraryChildNamePage: Arbitrary[ChildNamePage.type] =
    Arbitrary(ChildNamePage)

  implicit lazy val arbitraryApplicantHasFullNIContributionsPage: Arbitrary[ApplicantHasFullNIContributionsPage.type] =
    Arbitrary(ApplicantHasFullNIContributionsPage)

  implicit lazy val arbitraryWasChildUnder12YearsOldPage: Arbitrary[WasChildUnder12YearsOldPage.type] =
    Arbitrary(WasChildUnder12YearsOldPage)

  implicit lazy val arbitraryApplicantWasUkResidentPage: Arbitrary[ApplicantWasUkResidentPage.type] =
    Arbitrary(ApplicantWasUkResidentPage)

  implicit lazy val arbitraryApplicantIsValidAgePage: Arbitrary[ApplicantIsValidAgePage.type] =
    Arbitrary(ApplicantIsValidAgePage)

  implicit lazy val arbitraryApplicantChildcareAfterCutoffPage: Arbitrary[ApplicantChildcareAfterCutoffPage.type] =
    Arbitrary(ApplicantChildcareAfterCutoffPage)

  implicit lazy val arbitraryApplicantIsPartnerOfClaimantPage: Arbitrary[ApplicantIsPartnerOfClaimantPage.type] =
    Arbitrary(ApplicantIsPartnerOfClaimantPage)

  implicit lazy val arbitraryApplicantClaimsChildBenefitForThisChildPage: Arbitrary[ApplicantClaimsChildBenefitForThisChildPage.type] =
    Arbitrary(ApplicantClaimsChildBenefitForThisChildPage)

  implicit lazy val arbitraryApplicantRelationshipToChildPage: Arbitrary[ApplicantRelationshipToChildPage.type] =
    Arbitrary(ApplicantRelationshipToChildPage)
}
