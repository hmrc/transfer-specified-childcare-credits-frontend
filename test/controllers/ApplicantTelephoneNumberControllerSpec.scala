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
import forms.ApplicantTelephoneNumberFormProvider
import models.{Name, NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.{ApplicantNamePage, ApplicantTelephoneNumberPage}
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.ApplicantTelephoneNumberView

import scala.concurrent.Future

class ApplicantTelephoneNumberControllerSpec extends SpecBase with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new ApplicantTelephoneNumberFormProvider()
  val form = formProvider()

  lazy val ApplicantTelephoneNumberRoute = routes.ApplicantTelephoneNumberController.onPageLoad(NormalMode).url

  val applicantName = Name("Foo", "Bar")
  val minimalUserAnswers = emptyUserAnswers.set(ApplicantNamePage, applicantName).success.value

  "ApplicantTelephoneNumber Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(minimalUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, ApplicantTelephoneNumberRoute)
        val result = route(application, request).value
        val view = application.injector.instanceOf[ApplicantTelephoneNumberView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, applicantName, NormalMode)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = minimalUserAnswers.set(ApplicantTelephoneNumberPage, "07777777777").success.value
      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, ApplicantTelephoneNumberRoute)
        val view = application.injector.instanceOf[ApplicantTelephoneNumberView]
        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill("07777777777"), applicantName, NormalMode)(request, messages(application)).toString
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
          FakeRequest(POST, ApplicantTelephoneNumberRoute)
            .withFormUrlEncodedBody(("value", "07777777777"))
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(minimalUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, ApplicantTelephoneNumberRoute)
            .withFormUrlEncodedBody(("value", ""))
        val boundForm = form.bind(Map("value" -> ""))
        val view = application.injector.instanceOf[ApplicantTelephoneNumberView]
        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, applicantName, NormalMode)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, ApplicantTelephoneNumberRoute)
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a GET if no applicant name is found" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, ApplicantTelephoneNumberRoute)
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, ApplicantTelephoneNumberRoute)
            .withFormUrlEncodedBody(("value", "answer"))
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no applicant name is found" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, ApplicantTelephoneNumberRoute)
            .withFormUrlEncodedBody(("value", "answer"))
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
