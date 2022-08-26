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

import generators.ModelGenerators
import models.ApplicantRelationshipToChild._
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, Json}

class ApplicantRelationshipToChildSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {

  "ApplicantRelationshipToChild" - {

    "must deserialise valid values" - {

      "for Grandparent" in {
        Json.obj("type" -> "grandparent").as[ApplicantRelationshipToChild] mustEqual Grandparent
      }

      "for Aunt or Uncle" in {
        Json.obj("type" -> "auntOrUncle").as[ApplicantRelationshipToChild] mustEqual AuntOrUncle
      }

      "for Brother or Sister" in {
        Json.obj("type" -> "brotherOrSister").as[ApplicantRelationshipToChild] mustEqual BrotherOrSister
      }

      "for Great aunt or great uncle" in {
        Json.obj("type" -> "greatAuntOrGreatUncle").as[ApplicantRelationshipToChild] mustEqual GreatAuntOrGreatUncle
      }

      "for Non-Resident Parent" in {
        Json.obj("type" -> "nonResidentParent").as[ApplicantRelationshipToChild] mustEqual NonResidentParent
      }

      "for Resident Partner" in {
        Json.obj("type" -> "residentPartner").as[ApplicantRelationshipToChild] mustEqual ResidentPartner
      }

      "for Other" in {
        forAll(Gen.alphaStr) { value =>
          Json.obj(
            "type" -> "other",
            "value" -> value
          ).as[ApplicantRelationshipToChild] mustEqual ApplicantRelationshipToChild.Other(value)
        }
      }
    }

    "must fail to deserialise invalid values" in {

      val validTypes = List(
        Grandparent.toString,
        AuntOrUncle.toString,
        BrotherOrSister.toString,
        GreatAuntOrGreatUncle.toString,
        NonResidentParent.toString,
        "other"
      )
      val gen = Gen.alphaStr.suchThat(!validTypes.contains(_))

      forAll(gen) { value =>
        Json.obj("type" -> value).validate[ApplicantRelationshipToChild].isError mustBe true
      }
    }

    "must fail to deserialise other with no value" in {
      Json.obj("type" -> "other").validate[ApplicantRelationshipToChild].isError mustBe true
    }

    "must serialise basic values" in {

      val gen: Gen[ApplicantRelationshipToChild] = Gen.oneOf(
        Grandparent,
        AuntOrUncle,
        BrotherOrSister,
        GreatAuntOrGreatUncle,
        NonResidentParent
      )

      forAll(gen) { model =>
        Json.toJson(model) mustEqual Json.obj("type" -> model.toString)
      }
    }

    "must serialise other values" in {

      val gen = Gen.alphaStr.map(Other)

      forAll(gen) { model =>
        Json.toJson[ApplicantRelationshipToChild](model) mustEqual Json.obj("type" -> "other", "value" -> model.value)
      }
    }
  }
}
