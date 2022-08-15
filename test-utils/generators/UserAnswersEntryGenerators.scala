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

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryMainCarerAddressUserAnswersEntry: Arbitrary[(MainCarerAddressPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MainCarerAddressPage.type]
        value <- arbitrary[MainCarerAddress].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMainCarerDateOfBirthUserAnswersEntry: Arbitrary[(MainCarerDateOfBirthPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MainCarerDateOfBirthPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMainCarerNameUserAnswersEntry: Arbitrary[(MainCarerNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MainCarerNamePage.type]
        value <- arbitrary[MainCarerName].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantNinoUserAnswersEntry: Arbitrary[(ApplicantNinoPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantNinoPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicntTelephoneNumberUserAnswersEntry: Arbitrary[(ApplicntTelephoneNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicntTelephoneNumberPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantAddressUserAnswersEntry: Arbitrary[(ApplicantAddressPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantAddressPage.type]
        value <- arbitrary[ApplicantAddress].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantDateOfBirthUserAnswersEntry: Arbitrary[(ApplicantDateOfBirthPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantDateOfBirthPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantNameUserAnswersEntry: Arbitrary[(ApplicantNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantNamePage.type]
        value <- arbitrary[ApplicantName].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRemovePeriodUserAnswersEntry: Arbitrary[(RemovePeriodPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RemovePeriodPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddPeriodUserAnswersEntry: Arbitrary[(AddPeriodPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddPeriodPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPeriodUserAnswersEntry: Arbitrary[(PeriodPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PeriodPage.type]
        value <- arbitrary[Period].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryChildDateOfBirthUserAnswersEntry: Arbitrary[(ChildDateOfBirthPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ChildDateOfBirthPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryChildNameUserAnswersEntry: Arbitrary[(ChildNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ChildNamePage.type]
        value <- arbitrary[ChildName].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantHasFullNIContributionsUserAnswersEntry: Arbitrary[(ApplicantHasFullNIContributionsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantHasFullNIContributionsPage.type]
        value <- arbitrary[ApplicantHasFullNIContributions].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWasChildUnder12YearsOldUserAnswersEntry: Arbitrary[(WasChildUnder12YearsOldPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WasChildUnder12YearsOldPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantWasUkResidentUserAnswersEntry: Arbitrary[(ApplicantWasUkResidentPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantWasUkResidentPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantIsValidAgeUserAnswersEntry: Arbitrary[(ApplicantIsValidAgePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantIsValidAgePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantChildcareAfterCutoffUserAnswersEntry: Arbitrary[(ApplicantChildcareAfterCutoffPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantChildcareAfterCutoffPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantIsPartnerOfClaimantUserAnswersEntry: Arbitrary[(ApplicantIsPartnerOfClaimantPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantIsPartnerOfClaimantPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantClaimsChildBenefitForThisChildUserAnswersEntry: Arbitrary[(ApplicantClaimsChildBenefitForThisChildPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantClaimsChildBenefitForThisChildPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantRelationshipToChildUserAnswersEntry: Arbitrary[(ApplicantRelationshipToChildPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantRelationshipToChildPage.type]
        value <- arbitrary[ApplicantRelationshipToChild].map(Json.toJson(_))
      } yield (page, value)
    }
}
