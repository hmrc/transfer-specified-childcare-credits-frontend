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
import forms.ChildDateOfBirthFormProvider
import models.Mode
import navigation.Navigator
import pages.{ChildDateOfBirthPage, ChildNamePage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.ChildDateOfBirthView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ChildDateOfBirthController @Inject()(
                                        override val messagesApi: MessagesApi,
                                        sessionRepository: SessionRepository,
                                        navigator: Navigator,
                                        identify: IdentifierAction,
                                        getData: DataRetrievalAction,
                                        requireData: DataRequiredAction,
                                        formProvider: ChildDateOfBirthFormProvider,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: ChildDateOfBirthView
                                      )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with AnswerExtractor {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      getAnswer(ChildNamePage) { childName =>
        val form = formProvider(childName)
        val preparedForm = request.userAnswers.get(ChildDateOfBirthPage) match {
          case None => form
          case Some(value) => form.fill(value)
        }
        Ok(view(preparedForm, childName, mode))
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getAnswerAsync(ChildNamePage) { childName =>
        formProvider(childName).bindFromRequest().fold(
          formWithErrors =>
            Future.successful(BadRequest(view(formWithErrors, childName, mode))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(ChildDateOfBirthPage, value))
              _              <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(ChildDateOfBirthPage, mode, updatedAnswers))
        )
      }
  }
}
