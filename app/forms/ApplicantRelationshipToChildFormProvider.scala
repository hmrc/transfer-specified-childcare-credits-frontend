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

import javax.inject.Inject
import forms.mappings.Mappings
import play.api.data.Form
import play.api.data.Forms.mapping
import models.ApplicantRelationshipToChild
import models.ApplicantRelationshipToChild._
import uk.gov.voa.play.form.ConditionalMappings.mandatoryIf

class ApplicantRelationshipToChildFormProvider @Inject() extends Mappings {

  def apply(): Form[ApplicantRelationshipToChild] =
    Form(
      mapping(
        "value" -> text("applicantRelationshipToChild.error.required")
          .verifying("applicantRelationshipToChild.error.invalid", validTypes.contains(_)),
        "detail" -> mandatoryIf(isOther, text("applicantRelationshipToChild.other.detail.error.required"))
      )(toModel)(fromModel)
    )

  private val validTypes = List(
    "grandparent", "auntOrUncle", "brotherOrSister",
    "greatAuntOrGreatUncle", "nonResidentParent", "other"
  )

  private def isOther(data: Map[String, String]): Boolean =
    data.get("value").contains("other")

  private def toModel(value: String, detail: Option[String]): ApplicantRelationshipToChild =
    value match {
      case "grandparent"           => Grandparent
      case "auntOrUncle"           => AuntOrUncle
      case "brotherOrSister"       => BrotherOrSister
      case "greatAuntOrGreatUncle" => GreatAuntOrGreatUncle
      case "nonResidentParent"     => NonResidentParent
      case "other"                 => Other(detail.get)
    }

  private def fromModel(model: ApplicantRelationshipToChild): Option[(String, Option[String])] =
    model match {
      case Grandparent           => Some(("grandparent", None))
      case AuntOrUncle           => Some(("auntOrUncle", None))
      case BrotherOrSister       => Some(("brotherOrSister", None))
      case GreatAuntOrGreatUncle => Some(("greatAuntOrGreatUncle", None))
      case NonResidentParent     => Some(("nonResidentParent", None))
      case Other(value)          => Some(("other", Some(value)))
    }
}
