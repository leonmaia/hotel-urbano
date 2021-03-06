package repositories

import java.util.UUID

import converters.CityBSONConverter
import models.{City, CityList}
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONNull, BSONDocument}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{Promise, Future}
import scala.util.{Success, Failure, Try}
import scala.language.postfixOps

class MongoDBCityRepository(collection: BSONCollection) extends CityRepository {

  implicit val cityConverter = CityBSONConverter

  override def find(id: UUID): Future[Option[City]] = {
    val promise = Promise[Option[City]]()

    try {
      val query = BSONDocument("cityId" -> id.toString)
      val result = collection.find(query)
        .cursor[City](cityConverter, global)
        .collect[Seq]()

        for( cities <- result){
          promise success(cities headOption)
        }
    }
    catch {
      case error: Throwable => promise failure(error)
    }

    promise future
  }

  override def list(name: Option[String] = None): Future[CityList] = {

    val allSortedByCityName = BSONDocument(
      "$query" -> BSONDocument(),
    "$orderby" -> BSONDocument("cityName" -> 1)
  )

    val cities = collection.find(allSortedByCityName)
      .cursor[City](cityConverter, global)
      .collect[Seq]()

    name match {
      case None => cities map { cities => new CityList(cities) }
      case _ => cities.map { cities => {
        val (passed, failures) = cities.partition(_.name.contains(name.get))
        new CityList(passed)}
      }
    }
 }

  override def insert(city: City): Future[UUID] = {
    val promise = Promise[UUID]()

    collection.insert[City](city) onComplete {
      case Success(lastError) => promise success(city.id)
      case Failure(error) => promise failure(error)
    }

    promise.future
  }
}
