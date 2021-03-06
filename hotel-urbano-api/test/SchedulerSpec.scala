import converters.reads._
import java.net.URL
import java.util.UUID
import models._
import org.joda.time.DateTime
import org.scalatest._
import org.scalatestplus.play.WsScalaTestClient
import play.api.libs.json._

import scala.util.Random._

trait SchedulerSpec extends BaseSpec with OptionValues with WsScalaTestClient {
  def toJson(city: City): JsObject =
    JsObject(Seq(
      "cityId" -> JsString(city.id.toString),
      "cityName" -> JsString(city.name.toString)
    ))

  def toJson(hotel: Hotel): JsObject = 
    JsObject(Seq(
      "hotelId" -> JsString(hotel.id.toString),
      "hotelName" -> JsString(hotel.name.toString),
      "hotelPrice" -> JsString(hotel.price.toString),
      "cityId" -> JsString(hotel.cityId.toString)
    ))
}

