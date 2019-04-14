package idiomatic_mockito

import org.mockito.IdiomaticMockito
import org.scalatest.FeatureSpec

class Tutorial2_IdiomaticMockito_StubExceptionSpec
  extends FeatureSpec
    with IdiomaticMockito {

  import Tutorial2_IdiomaticMockito_StubExceptionSpec._

  feature("Stub with Exception") {
    scenario("Stub with RuntimeException") {
      test_RuntimeException()
    }

    scenario("Stub with Exception") {
      test_Exception()
    }
  }

  def test_RuntimeException(): Unit = {
    val myMock: MyClass = IdiomaticMockito.mock[MyClass]

    // Stub with RuntimeException
    myMock.bar().shouldThrow(new RuntimeException)

    assertThrows[RuntimeException] {
      myMock.bar()
    }
  }

  def test_Exception(): Unit = {
    val myMock: MyClass = IdiomaticMockito.mock[MyClass]

    // Stub with Exception
    myMock.bar().shouldThrow(new Exception)

    assertThrows[Exception] {
      myMock.bar()
    }
  }
}

object Tutorial2_IdiomaticMockito_StubExceptionSpec {

  class MyClass {
    def bar(): String = {
      "foo"
    }
  }

}
