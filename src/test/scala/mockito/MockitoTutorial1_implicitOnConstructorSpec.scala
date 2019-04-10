package mockito

import java.util.UUID

import org.mockito.{ArgumentMatchers, BDDMockito, Mockito, MockitoSugar}
import org.scalatest.FeatureSpec

import io.circe.Decoder.Result
import io.circe.{Decoder, Json, ParsingFailure}

/**
  * This suite pass.
  */
class MockitoTutorial1_implicitOnConstructorSpec extends FeatureSpec {

  import MockitoTutorial1_implicitOnConstructorSpec._

  feature("Mock") {
    scenario("Mock on MyClassB without implicit") {
      test_mockOnMyClassB()
    }

    scenario("Mock on MyClassB1 with implicit on class MyClassB1") {
      test_mockOnMyClassB1()
    }

  }

  def test_mockOnMyClassB(): Unit = {
    val mockClassB = MockitoSugar.mock[MyClassB]

    BDDMockito.given(mockClassB.fb(ArgumentMatchers.any())).willReturn("myResponse")

    val myClassA = new MyClassA(mockClassB)
    assert("myResponse" == myClassA.fa("az"))

    Mockito.verify(mockClassB).fb("az")
    Mockito.verifyNoMoreInteractions(mockClassB)
  }

  def test_mockOnMyClassB1(): Unit = {
    val uuid = UUID.randomUUID
    val mockClassB1 = MockitoSugar.mock[MyClassB1[Person]]

    BDDMockito.given(mockClassB1.fb(ArgumentMatchers.any())).willReturn(None)

    val myClassA1 = new MyClassA1(mockClassB1)
    assert(myClassA1.getPerson(uuid).isEmpty)

    Mockito.verify(mockClassB1).fb(uuid)
    Mockito.verifyNoMoreInteractions(mockClassB1)
  }

}


object MockitoTutorial1_implicitOnConstructorSpec {

  // MyClassB : simple
  class MyClassB {
    def fb(s: String): String = {
      s"MyClassB : $s"
    }
  }

  class MyClassA(val myClassB: MyClassB) {
    def fa(s: String): String = {
      this.myClassB.fb(s)
    }
  }

  // MyClassB1 : with implicit on constructor
  val bobUuid: UUID = UUID.fromString("95ee37a4-44bb-4768-b2fb-d4d84b196e0d")

  case class Person(name: String, size: Int)

  class MyClassB1[T]()(implicit val decoder: Decoder[T]) {
    def fb(uuid: UUID): Option[T] = {
      uuid match {
        case `bobUuid` =>
          val jsonString = """{ "name": "bob"", "size": 150}"""
          Some(objectFromJsonString[T](jsonString))
        case _ =>
          None
      }
    }
  }

  class MyClassA1(val myClassB1: MyClassB1[Person]) {
    def getPerson(uuid: UUID): Option[Person] = {
      this.myClassB1.fb(uuid)
    }
  }

  def objectFromJsonString[U](jsonString: String)(implicit decoder: Decoder[U]): U = {
    // Set json
    val e1: Either[ParsingFailure, Json] = io.circe.parser.parse(jsonString)
    if ( e1.isLeft ) {
      val s = e1.left.get.toString
      throw new Exception(s)
    }
    val json: Json = e1.right.get

    // Decode json
    val res: Result[U] = json.as[U]
    if ( res.isLeft ) {
      val s = res.left.get.toString
      throw new Exception(s)
    }
    res.right.get
  }

}
  
