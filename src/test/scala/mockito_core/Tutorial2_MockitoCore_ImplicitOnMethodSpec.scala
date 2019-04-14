package mockito_core

import java.util.UUID

import org.mockito.{ArgumentMatchers, BDDMockito, Mockito, MockitoSugar}
import org.scalatest.FeatureSpec
import io.circe.generic.auto._
import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor, Json, ParsingFailure}

class Tutorial2_MockitoCore_ImplicitOnMethodSpec extends FeatureSpec {

  import Tutorial2_MockitoCore_ImplicitOnMethodSpec._

  feature("Mock a method with implicit") {
    scenario("Nominal") {
      test()
    }
  }

  def test(): Unit = {
    val uuid = UUID.randomUUID
    val mockClassB = MockitoSugar.mock[MyClass[Person]]

    BDDMockito.given(mockClassB.fb(ArgumentMatchers.any())(ArgumentMatchers.any())).willReturn(None)

    val myClassA = new MyClassA(mockClassB)
    assert(myClassA.getPerson(uuid).isEmpty)

    Mockito.verify(mockClassB).fb(ArgumentMatchers.eq(uuid))(ArgumentMatchers.any())
    Mockito.verifyNoMoreInteractions(mockClassB)
  }
}


object Tutorial2_MockitoCore_ImplicitOnMethodSpec {

  val bobUuid: UUID = UUID.fromString("95ee37a4-44bb-4768-b2fb-d4d84b196e0d")

  trait Base_JsonDecoder {

    //---- java.time
    // java.time.Instant
    implicit val decode_Instant: Decoder[java.time.Instant] = {
      io.circe.java8.time.decodeInstant
    }
  }
  
  case class Person(name: String, size: Int)

  trait Person_JsonDecoder extends Base_JsonDecoder {
    implicit val decode_Person: Decoder[Person] = (c: HCursor) => c.as[Person]
  }

  object Person_JsonDecoder extends Person_JsonDecoder


  class MyClass[T] {
    def fb(uuid: UUID)(implicit decoder: Decoder[T]): Option[T] = {
      uuid match {
        case `bobUuid` =>
          val jsonString = """{ "name": "bob"", "size": 150}"""
          Some(objectFromJsonString[T](jsonString))
        case _ =>
          None
      }
    }
  }

  class MyClassA(val myClassB: MyClass[Person])(implicit decoder: Decoder[Person]) {
    def getPerson(uuid: UUID): Option[Person] = {
      this.myClassB.fb(uuid)
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
