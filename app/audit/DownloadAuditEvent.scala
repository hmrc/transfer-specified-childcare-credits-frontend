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

package audit

import audit.DownloadAuditEvent._
import models.JourneyModel
import play.api.libs.json.{Json, Writes}
import uk.gov.hmrc.domain.Nino

import java.time.LocalDate

final case class DownloadAuditEvent(
                                     child: Child,
                                     applicant: Applicant,
                                     mainCarer: MainCarer,
                                     periods: List[Period]
                                   )

object DownloadAuditEvent {

  def from(model: JourneyModel): DownloadAuditEvent = {

    DownloadAuditEvent(
      child = Child(
        firstName = model.child.name.firstName,
        lastName = model.child.name.lastName,
        dateOfBirth = model.child.dateOfBirth
      ),
      applicant = Applicant(
        firstName = model.applicant.name.firstName,
        lastName = model.applicant.name.lastName,
        dateOfBirth = model.applicant.dateOfBirth,
        relationshipToChild = model.applicant.relationshipToChild,
        address = getAddress(model.applicant.address),
        telephoneNumber = model.applicant.telephoneNumber,
        nino = model.applicant.nino
      ),
      mainCarer = MainCarer(
        firstName = model.mainCarer.name.firstName,
        lastName = model.mainCarer.name.lastName,
        dateOfBirth = model.mainCarer.dateOfBirth,
        address = getAddress(model.mainCarer.address),
        telephoneNumber = model.mainCarer.telephoneNumber,
        nino = model.mainCarer.nino
      ),
      periods = model.periods.toList.map { period =>
        Period(period.startDate, period.endDate)
      }
    )
  }

  private def getAddress(address: models.Address): Address = Address(
    line1 = address.line1,
    line2 = address.line2,
    townOrCity = address.townOrCity,
    county = address.county,
    postcode = address.postcode
  )

  final case class Child(
                          firstName: String,
                          lastName: String,
                          dateOfBirth: LocalDate
                        )

  object Child {
    implicit lazy val writes: Writes[Child] = Json.writes
  }

  final case class Applicant(
                              firstName: String,
                              lastName: String,
                              dateOfBirth: LocalDate,
                              relationshipToChild: String,
                              address: Address,
                              telephoneNumber: String,
                              nino: Nino
                            )

  object Applicant {
    implicit lazy val writes: Writes[Applicant] = Json.writes
  }

  final case class MainCarer(
                              firstName: String,
                              lastName: String,
                              dateOfBirth: LocalDate,
                              address: Address,
                              telephoneNumber: String,
                              nino: Nino
                            )

  object MainCarer {
    implicit lazy val writes: Writes[MainCarer] = Json.writes
  }

  final case class Address(
                            line1: String,
                            line2: Option[String],
                            townOrCity: String,
                            county: Option[String],
                            postcode: String
                          )

  object Address {
    implicit lazy val address: Writes[Address] = Json.writes
  }

  final case class Period(
                           startDate: LocalDate,
                           endDate: LocalDate
                         )

  object Period {
    implicit lazy val writes: Writes[Period] = Json.writes
  }

  implicit lazy val writes: Writes[DownloadAuditEvent] = Json.writes
}
