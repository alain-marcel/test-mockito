package idiomatic_mockito

import org.mockito.integrations.scalatest.IdiomaticMockitoFixture
import org.scalatest.FeatureSpec

class Tutorial1_IdiomaticMockito_BasicStubSpec
  extends FeatureSpec
    with IdiomaticMockitoFixture {

  import Tutorial1_IdiomaticMockito_BasicStubSpec._

  feature("Stub with asterisk") {
    scenario("shouldReturn") {
      test_asterisk()
    }
  }

  def test_asterisk(): Unit = {
    val myMock = mock[MyClass]

    // Mock a method with expected values (order is important)
    myMock.f1(*).shouldReturn("defaultResult")
    myMock.f1("s1_specificValue").shouldReturn("Hi s1")

    assertResult("defaultResult") {
      myMock.f1("a value")
    }
    assertResult("Hi s1") {
      myMock.f1("s1_specificValue")
    }
  }

}

object Tutorial1_IdiomaticMockito_BasicStubSpec {

  class MyClass {
    def f1(s1: String, s2: String = "test"): String = {
      s"Hello $s1"
    }

  }

}
