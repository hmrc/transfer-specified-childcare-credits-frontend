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

import java.time.LocalDate

trait ModelGenerators {

  implicit lazy val arbitraryAddress: Arbitrary[Address] =
    Arbitrary {
      for {
        line1      <- arbitrary[String]
        line2      <- arbitrary[Option[String]]
        townOrCity <- arbitrary[String]
        county     <- arbitrary[Option[String]]
        postcode   <- arbitrary[String]
      } yield Address(line1, line2, townOrCity, county, postcode)
    }

  implicit lazy val arbitraryPeriod: Arbitrary[Period] =
    Arbitrary {
      for {
        startDate <- arbitrary[LocalDate]
        ndDate <- arbitrary[LocalDate]
      } yield Period(startDate, ndDate)
    }

  implicit lazy val arbitraryName: Arbitrary[Name] =
    Arbitrary {
      for {
        firstName <- arbitrary[String]
        lastName <- arbitrary[String]
      } yield Name(firstName, lastName)
    }

  implicit lazy val arbitraryApplicantRelationshipToChild: Arbitrary[ApplicantRelationshipToChild] = {
    import ApplicantRelationshipToChild._
    val otherGen: Gen[ApplicantRelationshipToChild] = Gen.alphaStr.map(Other)
    val basicGen: Gen[ApplicantRelationshipToChild] = Gen.oneOf(
      Grandparent,
      AuntOrUncle,
      BrotherOrSister,
      GreatAuntOrGreatUncle,
      NonResidentParent,
      ResidentPartner
    )
    Arbitrary(Gen.oneOf(basicGen, otherGen))
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
