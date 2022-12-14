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
import forms.RemovePeriodFormProvider

import javax.inject.Inject
import models.{Index, Mode}
import navigation.Navigator
import pages.{PeriodPage, RemovePeriodPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.RemovePeriodView

import scala.concurrent.{ExecutionContext, Future}

class RemovePeriodController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: RemovePeriodFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: RemovePeriodView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with AnswerExtractor {

  val form = formProvider()

  def onPageLoad(mode: Mode, index: Index): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      getAnswer(PeriodPage(index)) { period =>
        Ok(view(form, period, mode, index))
      }
  }

  def onSubmit(mode: Mode, index: Index): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getAnswerAsync(PeriodPage(index)) { period =>
        form.bindFromRequest().fold(
          formWithErrors =>
            Future.successful(BadRequest(view(formWithErrors, period, mode, index))),
          value =>
            if (value) {
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.remove(PeriodPage(index)))
                _              <- sessionRepository.set(updatedAnswers)
              } yield Redirect(navigator.nextPage(RemovePeriodPage(index), mode, updatedAnswers))
            } else {
              Future.successful(Redirect(navigator.nextPage(RemovePeriodPage(index), mode, request.userAnswers)))
            }
        )
      }
  }
}
