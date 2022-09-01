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

import com.dmanchester.playfop.sapi.PlayFop
import controllers.actions._
import org.apache.fop.apps.FOUserAgent
import org.apache.xmlgraphics.util.MimeConstants
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.xml.xml.PrintTemplate

import javax.inject.Inject

class PrintController @Inject()(
                                 override val messagesApi: MessagesApi,
                                 identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 val controllerComponents: MessagesControllerComponents,
                                 fop: PlayFop,
                                 template: PrintTemplate
                               ) extends FrontendBaseController with I18nSupport {

  private val userAgentBlock: FOUserAgent => Unit = { foUserAgent: FOUserAgent =>
    foUserAgent.setAccessibility(true)
    foUserAgent.setPdfUAEnabled(true)
    foUserAgent.setAuthor("HMRC forms service")
    foUserAgent.setProducer("HMRC forms services")
    foUserAgent.setCreator("HMRC forms services")
    foUserAgent.setSubject("Apply for Specified Adult Childcare credits")
    foUserAgent.setTitle("Apply for Specified Adult Childcare credits")
  }

  def onDownload: Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      val pdf = fop.processTwirlXml(template.render(implicitly), MimeConstants.MIME_PDF, foUserAgentBlock = userAgentBlock)
      Ok(pdf).as("application/octet-stream").withHeaders(CONTENT_DISPOSITION -> "attachment; filename=apply-for-specified-adult-childcare-credits.pdf")
  }
}
