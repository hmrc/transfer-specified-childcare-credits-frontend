package queries

import models.{ApplicantAndChild, Child, Name, UserAnswers}
import org.scalatest.{OptionValues, TryValues}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import pages.{ApplicantNamePage, ChildDateOfBirthPage, ChildNamePage}

import java.time.LocalDate

class ApplicantAndChildQuerySpec extends AnyFreeSpec with Matchers with TryValues with OptionValues {

  "must read out the applicant and child from user answers" in {

    val childName = Name("Foo", "Bar")
    val childDob = LocalDate.now
    val applicantName = Name("Bar", "Foo")

    val userAnswers = UserAnswers("id")
      .set(ChildNamePage, childName).success.value
      .set(ChildDateOfBirthPage, childDob).success.value
      .set(ApplicantNamePage, applicantName).success.value

    userAnswers.get(ApplicantAndChildQuery).value mustEqual ApplicantAndChild(applicantName, Child(childName, childDob))
  }
}
