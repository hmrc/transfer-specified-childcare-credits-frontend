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

package controllers

import com.google.inject.Inject
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import models.CheckMode
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers._
import viewmodels.govuk.summarylist._
import views.html.CheckYourAnswersView

class CheckYourAnswersController @Inject()(
                                            override val messagesApi: MessagesApi,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: CheckYourAnswersView
                                          ) extends FrontendBaseController with I18nSupport {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      
      val answers = request.userAnswers

      val childDetails = SummaryListViewModel(Seq(
        ChildNameSummary.row(answers),
        ChildDateOfBirthSummary.row(answers)
      ).flatten)

      val applicantDetails = SummaryListViewModel(Seq(
        ApplicantNameSummary.row(answers),
        ApplicantDateOfBirthSummary.row(answers),
        ApplicantAddressSummary.row(answers),
        ApplicantTelephoneNumberSummary.row(answers),
        ApplicantNinoSummary.row(answers)
      ).flatten)

      val periods = AddPeriodSummary.rows(answers, CheckMode)

      val mainCarerDetails = SummaryListViewModel(Seq(
        MainCarerNameSummary.row(answers),
        MainCarerDateOfBirthSummary.row(answers),
        MainCarerAddressSummary.row(answers),
        MainCarerTelephoneNumberSummary.row(answers),
        MainCarerNinoSummary.row(answers)
      ).flatten)

      Ok(view(childDetails, applicantDetails, periods, mainCarerDetails))
  }
}
