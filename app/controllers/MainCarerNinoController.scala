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
import forms.MainCarerNinoFormProvider

import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.{MainCarerNamePage, MainCarerNinoPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.MainCarerNinoView

import scala.concurrent.{ExecutionContext, Future}

class MainCarerNinoController @Inject()(
                                        override val messagesApi: MessagesApi,
                                        sessionRepository: SessionRepository,
                                        navigator: Navigator,
                                        identify: IdentifierAction,
                                        getData: DataRetrievalAction,
                                        requireData: DataRequiredAction,
                                        formProvider: MainCarerNinoFormProvider,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: MainCarerNinoView
                                    )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with AnswerExtractor {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      getAnswer(MainCarerNamePage) { mainCarerName =>
        val form = formProvider(mainCarerName)
        val preparedForm = request.userAnswers.get(MainCarerNinoPage) match {
          case None => form
          case Some(value) => form.fill(value)
        }
        Ok(view(preparedForm, mainCarerName, mode))
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getAnswerAsync(MainCarerNamePage) { mainCarerName =>
        formProvider(mainCarerName).bindFromRequest().fold(
          formWithErrors =>
            Future.successful(BadRequest(view(formWithErrors, mainCarerName, mode))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(MainCarerNinoPage, value))
              _              <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(MainCarerNinoPage, mode, updatedAnswers))
        )
      }
  }
}
