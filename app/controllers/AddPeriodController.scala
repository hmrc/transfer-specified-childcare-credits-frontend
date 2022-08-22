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

import controllers.actions._
import forms.AddPeriodFormProvider
import models.{Mode, UserAnswers}
import navigation.Navigator
import pages.AddPeriodPage
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.ApplicantAndChildNamesQuery
import repositories.SessionRepository
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.AddPeriodSummary
import views.html.AddPeriodView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddPeriodController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: AddPeriodFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: AddPeriodView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with AnswerExtractor {

  val form = formProvider()

  private def summaryList(answers: UserAnswers, mode: Mode)(implicit messages: Messages): Seq[ListItem] =
    AddPeriodSummary.rows(answers, mode)

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      getAnswer(ApplicantAndChildNamesQuery) { names =>
        Ok(view(form, names, summaryList(request.userAnswers, mode), mode))
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getAnswerAsync(ApplicantAndChildNamesQuery) { names =>
        form.bindFromRequest().fold(
          formWithErrors =>
            Future.successful(BadRequest(view(formWithErrors, names, summaryList(request.userAnswers, mode), mode))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(AddPeriodPage, value))
            } yield Redirect(navigator.nextPage(AddPeriodPage, mode, updatedAnswers))
        )
      }
  }
}
