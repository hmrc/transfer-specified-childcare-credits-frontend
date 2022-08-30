package models

import pages.{ApplicantNamePage, ChildDateOfBirthPage, ChildNamePage}
import play.api.libs.json.Reads
import play.api.libs.functional.syntax._

import java.time.LocalDate

final case class ApplicantAndChild(applicantName: Name, child: Child)

object ApplicantAndChild {

  implicit lazy val reads: Reads[ApplicantAndChild] = (
    ApplicantNamePage.path.read[Name] ~
      (
        ChildNamePage.path.read[Name] ~
        ChildDateOfBirthPage.path.read[LocalDate]
      )(Child.apply _)
  )(ApplicantAndChild.apply _)
}