package queries

import models.ApplicantAndChild
import play.api.libs.json.{JsPath, __}

case object ApplicantAndChildQuery extends Gettable[ApplicantAndChild] {
  override def path: JsPath = __
}
