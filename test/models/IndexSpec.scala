package models

import base.SpecBase
import org.scalacheck.Gen
import org.scalatest.EitherValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.mvc.PathBindable

class IndexSpec extends SpecBase with EitherValues with ScalaCheckPropertyChecks {

  private val pathBindable = implicitly[PathBindable[Index]]

  "Index" - {

    "must bind positive integers to an index that is one less than the value" in {

      forAll(Gen.choose(1, Int.MaxValue)) { number =>
        pathBindable.bind("key", number.toString).value mustEqual Index(number - 1)
      }
    }

    "must not bind from negative numbers or 0" in {

      forAll(Gen.choose(Int.MinValue, 0)) { number =>
        pathBindable.bind("key", number.toString) mustBe a[Left[_, Index]]
      }
    }

    "must unbind to a number 1 greater than the index" in {

      forAll(Gen.choose(0, Int.MaxValue - 1)) { number =>
        pathBindable.unbind("key", Index(number)) mustEqual (number + 1).toString
      }
    }

    "+" - {

      "must return an index with a position equal to this index's position plus the new amount" in {

        forAll(Gen.choose(0, 100), Gen.choose(0, 100)) {
          case (original, additional) =>
            Index(original) + additional mustEqual Index(original + additional)
        }
      }
    }
  }
}