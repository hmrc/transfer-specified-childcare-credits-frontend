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

import base.SpecBase
import forms.AddPeriodFormProvider
import models.{ApplicantAndChildNames, Index, Name, NormalMode, Period}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{never, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.{AddPeriodPage, ApplicantNamePage, ChildNamePage, PeriodPage}
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import viewmodels.checkAnswers.AddPeriodSummary
import views.html.AddPeriodView

import java.time.LocalDate
import scala.concurrent.Future

class AddPeriodControllerSpec extends SpecBase with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new AddPeriodFormProvider()

  lazy val addPeriodRoute = routes.AddPeriodController.onPageLoad(NormalMode).url

  val childName = Name("Foo", "Bar")
  val applicantName = Name("Bar", "Foo")
  val names = ApplicantAndChildNames(applicantName, childName)
  val form = formProvider(names)

  val period = Period(LocalDate.of(2000, 2, 1), LocalDate.of(2001, 3, 2))
  val minimalUserAnswers = emptyUserAnswers
    .set(ChildNamePage, childName).success.value
    .set(ApplicantNamePage, applicantName).success.value
    .set(PeriodPage(Index(0)), period).success.value

  def expectedItems(implicit messages: Messages) =
    AddPeriodSummary.rows(minimalUserAnswers, NormalMode)

  "AddPeriod Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(minimalUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addPeriodRoute)
        val result = route(application, request).value
        val view = application.injector.instanceOf[AddPeriodView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, names, expectedItems(messages(application)), NormalMode)(request, messages(application)).toString
      }
    }

    "must redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[SessionRepository]
      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(minimalUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[SessionRepository].toInstance(mockSessionRepository)
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, addPeriodRoute)
            .withFormUrlEncodedBody(("value", "true"))
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url

        verify(mockSessionRepository, never()).set(any())
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(minimalUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addPeriodRoute)
            .withFormUrlEncodedBody(("value", ""))
        val boundForm = form.bind(Map("value" -> ""))
        val view = application.injector.instanceOf[AddPeriodView]
        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, names, expectedItems(messages(application)), NormalMode)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, addPeriodRoute)
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a GET if no minimal data is found" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addPeriodRoute)
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to the first Period page for a GET if there are no periods added" in {

      val answers = minimalUserAnswers.remove(PeriodPage(Index(0))).success.value
      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, addPeriodRoute)
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.PeriodController.onPageLoad(NormalMode, Index(0)).url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, addPeriodRoute)
            .withFormUrlEncodedBody(("value", "true"))
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no minimal data is found" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addPeriodRoute)
            .withFormUrlEncodedBody(("value", "true"))
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
