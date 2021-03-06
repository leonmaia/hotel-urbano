package repositories

import java.util.UUID
import org.joda.time.LocalDate

import models.{Hotel, HotelList}
import scala.concurrent.Future

trait HotelRepository {
  def find(id: UUID): Future[Option[Hotel]]

  def list(fromCity: Option[String] = None, dates: Option[Stream[LocalDate]] = None): Future[HotelList]

  def insert(hotel: Hotel): Future[UUID]
}
