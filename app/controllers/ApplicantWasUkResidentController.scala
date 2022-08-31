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
import forms.ApplicantWasUkResidentFormProvider
import models.Mode
import navigation.Navigator
import pages.ApplicantWasUkResidentPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.ApplicantAndChildNamesQuery
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.ApplicantWasUkResidentView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ApplicantWasUkResidentController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: ApplicantWasUkResidentFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: ApplicantWasUkResidentView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with AnswerExtractor {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      getAnswer(ApplicantAndChildNamesQuery) { names =>
        val form = formProvider(names)
        val preparedForm = request.userAnswers.get(ApplicantWasUkResidentPage) match {
          case None => form
          case Some(value) => form.fill(value)
        }
        Ok(view(preparedForm, names, mode))
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getAnswerAsync(ApplicantAndChildNamesQuery) { names =>
        formProvider(names).bindFromRequest().fold(
          formWithErrors =>
            Future.successful(BadRequest(view(formWithErrors, names, mode))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(ApplicantWasUkResidentPage, value))
              _              <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(ApplicantWasUkResidentPage, mode, updatedAnswers))
        )
      }
  }
}
