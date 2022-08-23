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

package viewmodels.checkAnswers

import models.{Index, Mode, UserAnswers}
import play.api.i18n.Messages
import queries.PeriodsQuery
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem

import java.time.format.DateTimeFormatter

object AddPeriodSummary {

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def rows(answers: UserAnswers, mode: Mode)(implicit messages: Messages): Seq[ListItem] =
    answers.get(PeriodsQuery).getOrElse(Nil).zipWithIndex.map {
      case (period, index) =>
        ListItem(
          name = Messages("checkYourAnswers.period.label", dateFormatter.format(period.startDate), dateFormatter.format(period.endDate)),
          changeUrl = controllers.routes.PeriodController.onPageLoad(mode, Index(index)).url,
          removeUrl = controllers.routes.RemovePeriodController.onPageLoad(mode, Index(index)).url
        )
    }
}
