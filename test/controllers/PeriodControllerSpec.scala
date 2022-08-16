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
import forms.PeriodFormProvider
import models.{Index, NormalMode, Period, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.TryValues
import org.scalatestplus.mockito.MockitoSugar
import pages.PeriodPage
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.PeriodView

import java.time.{Clock, Instant, LocalDate, ZoneOffset}
import scala.concurrent.Future

class PeriodControllerSpec extends SpecBase with MockitoSugar with TryValues {

  def onwardRoute = Call("GET", "/foo")

  val clock = Clock.fixed(Instant.now, ZoneOffset.UTC)
  val formProvider = new PeriodFormProvider(clock)
  val form = formProvider()

  lazy val periodRoute = routes.PeriodController.onPageLoad(NormalMode, Index(0)).url

  val validPeriod = Period(LocalDate.now.minusDays(10), LocalDate.now.minusDays(9))
  val userAnswers = UserAnswers(userAnswersId)
    .set(PeriodPage(Index(0)), validPeriod).success.value

  "Period Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, periodRoute)
        val view = application.injector.instanceOf[PeriodView]
        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, Index(0))(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, periodRoute)
        val view = application.injector.instanceOf[PeriodView]
        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(validPeriod), NormalMode, Index(0))(request, messages(application)).toString
      }
    }

    "must redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[SessionRepository].toInstance(mockSessionRepository)
          )
          .build()

      val date = LocalDate.now(clock)

      running(application) {
        val request =
          FakeRequest(POST, periodRoute)
            .withFormUrlEncodedBody(
              "startDate.year" -> date.getYear.toString,
              "startDate.month" -> date.getMonthValue.toString,
              "startDate.day" -> date.getDayOfMonth.toString,
              "endDate.year" -> date.getYear.toString,
              "endDate.month" -> date.getMonthValue.toString,
              "endDate.day" -> date.getDayOfMonth.toString,
            )

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, periodRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))
        val boundForm = form.bind(Map("value" -> "invalid value"))
        val view = application.injector.instanceOf[PeriodView]
        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode, Index(0))(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, periodRoute)
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, periodRoute)
            .withFormUrlEncodedBody(("startDate", "value 1"), ("endDate", "value 2"))
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
