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
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import uk.gov.hmrc.domain.Nino

trait ModelGenerators {

  implicit lazy val arbitraryMainCarerAddress: Arbitrary[MainCarerAddress] =
    Arbitrary {
      for {
        line1 <- arbitrary[String]
        line2 <- arbitrary[String]
      } yield MainCarerAddress(line1, line2)
    }

  implicit lazy val arbitraryMainCarerName: Arbitrary[MainCarerName] =
    Arbitrary {
      for {
        firstName <- arbitrary[String]
        lastName <- arbitrary[String]
      } yield MainCarerName(firstName, lastName)
    }

  implicit lazy val arbitraryApplicantAddress: Arbitrary[ApplicantAddress] =
    Arbitrary {
      for {
        line1 <- arbitrary[String]
        line2 <- arbitrary[String]
      } yield ApplicantAddress(line1, line2)
    }

  implicit lazy val arbitraryApplicantName: Arbitrary[ApplicantName] =
    Arbitrary {
      for {
        firstName <- arbitrary[String]
        lastName <- arbitrary[String]
      } yield ApplicantName(firstName, lastName)
    }

  implicit lazy val arbitraryPeriod: Arbitrary[Period] =
    Arbitrary {
      for {
        startDate <- arbitrary[String]
        ndDate <- arbitrary[String]
      } yield Period(startDate, ndDate)
    }

  implicit lazy val arbitraryChildName: Arbitrary[ChildName] =
    Arbitrary {
      for {
        firstName <- arbitrary[String]
        lastName <- arbitrary[String]
      } yield ChildName(firstName, lastName)
    }

  implicit lazy val arbitraryApplicantHasFullNIContributions: Arbitrary[ApplicantHasFullNIContributions] =
    Arbitrary {
      Gen.oneOf(ApplicantHasFullNIContributions.values.toSeq)
    }

  implicit lazy val arbitraryApplicantRelationshipToChild: Arbitrary[ApplicantRelationshipToChild] =
    Arbitrary {
      Gen.oneOf(ApplicantRelationshipToChild.values.toSeq)
    }

  implicit lazy val arbitraryNino: Arbitrary[Nino] = Arbitrary {
    for {
      firstChar <- Gen.oneOf('A', 'C', 'E', 'H', 'J', 'L', 'M', 'O', 'P', 'R', 'S', 'W', 'X', 'Y').map(_.toString)
      secondChar <- Gen.oneOf('A', 'B', 'C', 'E', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'R', 'S', 'T', 'W', 'X', 'Y', 'Z').map(_.toString)
      digits <- Gen.listOfN(6, Gen.numChar)
      lastChar <- Gen.oneOf('A', 'B', 'C', 'D')
    } yield Nino(firstChar ++ secondChar ++ digits :+ lastChar)
  }
}
