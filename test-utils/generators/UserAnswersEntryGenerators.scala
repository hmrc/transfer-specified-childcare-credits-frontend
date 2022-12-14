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

  implicit lazy val arbitraryMainCarerNinoUserAnswersEntry: Arbitrary[(MainCarerNinoPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MainCarerNinoPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMainCarerTelephoneNumberUserAnswersEntry: Arbitrary[(MainCarerTelephoneNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MainCarerTelephoneNumberPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMainCarerAddressUserAnswersEntry: Arbitrary[(MainCarerAddressPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MainCarerAddressPage.type]
        value <- arbitrary[Address].map(Json.toJson(_))
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
        value <- arbitrary[Name].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantNinoUserAnswersEntry: Arbitrary[(ApplicantNinoPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantNinoPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicntTelephoneNumberUserAnswersEntry: Arbitrary[(ApplicantTelephoneNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantTelephoneNumberPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantAddressUserAnswersEntry: Arbitrary[(ApplicantAddressPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantAddressPage.type]
        value <- arbitrary[Address].map(Json.toJson(_))
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
        value <- arbitrary[Name].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPeriodUserAnswersEntry: Arbitrary[(PeriodPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PeriodPage]
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
        value <- arbitrary[Name].map(Json.toJson(_))
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
        value <- arbitrary[String].map(Json.toJson(_))
      } yield (page, value)
    }
}
