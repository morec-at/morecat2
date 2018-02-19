package test

import play.api.inject.guice.GuiceApplicationBuilder

import scala.reflect.ClassTag

trait InjectSupport {
  private val injector       = new GuiceApplicationBuilder().injector()
  def inject[T: ClassTag]: T = injector.instanceOf[T]
}
