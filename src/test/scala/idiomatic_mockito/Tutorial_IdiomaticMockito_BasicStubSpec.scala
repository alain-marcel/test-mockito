package idiomatic_mockito

import org.mockito.ArgumentMatchersSugar._
import org.mockito.IdiomaticMockito._
import org.mockito.captor.ArgCaptor
import org.scalatest.FeatureSpec

class Tutorial_IdiomaticMockito_BasicStubSpec
  extends FeatureSpec {

  import Tutorial_IdiomaticMockito_BasicStubSpec._

  feature("Stub method with asterisk") {
    scenario("Use shouldReturn") {
      test_asterisk__shouldReturn()
    }
    scenario("With shouldAnswer (show that behavior is identical to shouldReturn)") {
      test_asterisk__shouldAnswer()
    }
  }

  feature("Stub method with varargs") {
    scenario("Nominal") {
      test_varargs()
    }
  }

  feature("Stub method with Exception") {
    scenario("With RuntimeException") {
      test_RuntimeException()
    }

    scenario("With Exception") {
      test_Exception()
    }
  }

  def test_asterisk__shouldReturn(): Unit = {
    val myMock = mock[MyClass]

    // Mock a method with expected values (order is important)
    myMock.f1(*).shouldReturn("default_result")
    myMock.f1("s1_specific_value").shouldReturn("s1_specific_result")

    assertResult("default_result") {
      myMock.f1("any_value_different_from_s1_specific_value")
    }
    assertResult("s1_specific_result") {
      myMock.f1("s1_specific_value")
    }
  }

  def test_asterisk__shouldAnswer(): Unit = {
    val myMock = mock[MyClass]

    // Mock a method with expected values (order is important)
    myMock.f1(*).shouldAnswer(
      (s: String) => s match {
        case "s1_specific_value" => "s1_specific_result"
        case _ => "default_result"
      }
    )

    assertResult("default_result") {
      myMock.f1("any_value_different_from_s1_specific")
    }
    assertResult("s1_specific_result") {
      myMock.f1("s1_specific_value")
    }
  }

  def test_varargs(): Unit = {

    val myMock = mock[MyClass]
    val cap = ArgCaptor[String]

    //    myMock.f2("a").shouldReturn("a")
    //    myMock.f2("a","b").shouldReturn("a.b")
    myMock.f2("a", "b", "c").shouldReturn("a.b.c")

    assertResult("a.b.c") {
      myMock.f2("a", "b", "c")
    }

    // Verify that f2 was called once, without capture
    myMock.f2(*, "b", *).wasCalled(once)
    myMock.f2("a", "b", *).wasCalled(once)
    myMock.f2("a", "b", "c").wasCalled(once)

    // Verify that f2 was called once, with capture on second argument
    myMock.f2("a", cap.capture, *).wasCalled(once)
    cap.hasCaptured("b")
  }

  def test_RuntimeException(): Unit = {
    val myMock: MyClass = mock[MyClass]

    // Stub with RuntimeException is working fine
    myMock.f0().shouldThrow(new RuntimeException("blabla"))

    // Call
    val caught = intercept[RuntimeException] {
      myMock.f0()
    }

    assertResult("blabla") {
      caught.getMessage
    }
  }

  def test_Exception(): Unit = {
    val myMock: MyClass = mock[MyClass]

    // Stub with Exception
    myMock.f0().shouldThrow(new Exception("blabla"))

    // Call
    val caught = intercept[Exception] {
      myMock.f0()
    }
    
    assertResult("blabla") {
      caught.getMessage
    }
  }
}

object Tutorial_IdiomaticMockito_BasicStubSpec {

  class MyClass {
    def f0(): String = {
      "foo"
    }

    def f1(s1: String, s2: String = "test"): String = {
      s"Hello $s1"
    }

    def f2(s: String*): String = {
      s.mkString(".")
    }

  }

}
