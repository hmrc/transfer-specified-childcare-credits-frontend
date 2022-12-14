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
import forms.PeriodFormProvider
import models.{ApplicantAndChildNames, Index, Mode}
import navigation.Navigator
import pages.PeriodPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.ApplicantAndChildQuery
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.PeriodView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PeriodController @Inject()(
                                      override val messagesApi: MessagesApi,
                                      sessionRepository: SessionRepository,
                                      navigator: Navigator,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      formProvider: PeriodFormProvider,
                                      val controllerComponents: MessagesControllerComponents,
                                      view: PeriodView
                                     )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with AnswerExtractor {

  def onPageLoad(mode: Mode, index: Index): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      getAnswer(ApplicantAndChildQuery) { people =>
        val form = formProvider(people)
        val preparedForm = request.userAnswers.get(PeriodPage(index)) match {
          case None => form
          case Some(value) => form.fill(value)
        }
        Ok(view(preparedForm, ApplicantAndChildNames(people.applicantName, people.child.name), mode, index))
      }
  }

  def onSubmit(mode: Mode, index: Index): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getAnswerAsync(ApplicantAndChildQuery) { people =>
        val form = formProvider(people)
        form.bindFromRequest().fold(
          formWithErrors =>
            Future.successful(BadRequest(view(formWithErrors, ApplicantAndChildNames(people.applicantName, people.child.name), mode, index))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(PeriodPage(index), value))
              _              <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(PeriodPage(index), mode, updatedAnswers))
        )
      }
  }
}
