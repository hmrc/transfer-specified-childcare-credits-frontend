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

package forms

import forms.behaviours.OptionFieldBehaviours
import generators.Generators
import models.ApplicantRelationshipToChild._
import org.scalacheck.{Gen, Shrink}
import play.api.data.FormError

class ApplicantRelationshipToChildFormProviderSpec extends OptionFieldBehaviours with Generators {

  implicit val noShrink: Shrink[Any] = Shrink.shrinkAny

  val form = new ApplicantRelationshipToChildFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "applicantRelationshipToChild.error.required"
    val invalidKey = "applicantRelationshipToChild.error.invalid"

    val allValues = List(
      Grandparent.toString,
      AuntOrUncle.toString,
      BrotherOrSister.toString,
      GreatAuntOrGreatUncle.toString,
      NonResidentParent.toString,
      ResidentPartner.toString,
      "other"
    )

    "must bind all standard values" in {

      for {
        value <- allValues
      } yield {
        val data = Map(fieldName -> value)
        val result = form.bind(data)
        result(fieldName).errors mustBe empty
      }
    }

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )

    "must not bind invalid values" in {

      val gen = Gen.nonEmptyListOf(Gen.alphaChar)
        .map(_.mkString(""))
        .suchThat(!allValues.contains(_))

      forAll(gen) { value =>
        val data = Map(fieldName -> value)
        val result = form.bind(data)
        result(fieldName).errors must contain only FormError(fieldName, invalidKey)
      }
    }
  }

  ".detail" - {

    "must bind when value is `other` and detail is provided" in {

      val data = Map(
        "value" -> "other",
        "detail" -> "foo"
      )

      val result = form.bind(data)
      result.value.value mustEqual Other("foo")
      result.errors mustBe empty
    }

    "must not bind if value is `other` but there is no detail provided" in {

      val data = Map(
        "value" -> "other"
      )

      val result = form.bind(data)
      result.errors must contain(FormError("detail", "applicantRelationshipToChild.other.detail.error.required"))
    }

    "must not bind if value if `other` but an empty value is provided" in {

      val data = Map(
        "value" -> "other",
        "detail" -> ""
      )

      val result = form.bind(data)
      result.errors must contain(FormError("detail", "applicantRelationshipToChild.other.detail.error.required"))
    }

    "must not bind if the string is present but value is not `other`" in {

      val data = Map(
        "value" -> "grandparent",
        "detail" -> "foo"
      )

      val result = form.bind(data)
      result.value.value mustEqual Grandparent
      result.errors mustBe empty
    }
  }
}
