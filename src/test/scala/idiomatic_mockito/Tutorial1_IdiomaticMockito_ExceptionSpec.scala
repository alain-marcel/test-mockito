package idiomatic_mockito

import org.mockito.IdiomaticMockito
import org.scalatest.FeatureSpec

class Tutorial1_IdiomaticMockito_ExceptionSpec
  extends FeatureSpec
    with IdiomaticMockito {

  import Tutorial1_IdiomaticMockito_ExceptionSpec._

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

    // Stubbing with RuntimeException is working fine
    myMock.bar().shouldThrow(new RuntimeException)
  }

  def test_Exception(): Unit = {
    val myMock: MyClass = IdiomaticMockito.mock[MyClass]

    // Stubbing with Exception: does not work : we have follwing excation
    //   Checked exception is invalid for this method!
    //     Invalid: java.lang.Exception
    //   org.mockito.exceptions.base.MockitoException:
    //     Checked exception is invalid for this method!
    //     Invalid: java.lang.Exception
    myMock.bar().shouldThrow(new Exception)
  }
}

object Tutorial1_IdiomaticMockito_ExceptionSpec {

  class MyClass {
    def bar(): String = {
      "foo"
    }
  }
}
