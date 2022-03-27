package morecat.api

import com.akkaserverless.scalasdk.testkit.AkkaServerlessTestKit
import com.google.protobuf.empty.Empty
import io.grpc.StatusRuntimeException
import morecat.api
import morecat.api.blog.CreateBlogRequestGenerator
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.wordspec.AnyWordSpec

import scala.util.control.NonFatal

class MoreCatServiceIntegrationSpec
    extends AnyWordSpec
    with Matchers
    with BeforeAndAfterAll
    with ScalaFutures {

  implicit private val patience: PatienceConfig =
    PatienceConfig(Span(5, Seconds), Span(500, Millis))

  private val testKit = AkkaServerlessTestKit(Main.createAkkaServerless()).start()
  import testKit.executionContext

  private val client = testKit.getGrpcClient(classOf[MoreCatService])

  "createBlog" must {
    "create a blog" in {
      // When
      val result = client.createBlog(CreateBlogRequestGenerator.gen()).futureValue
      // Then
      result shouldBe Empty.defaultInstance
    }
  }

  "getBlog" must {
    "return a blog has the id" in {
      // Given
      val request: CreateBlogRequest = CreateBlogRequestGenerator.gen()
      // When
      val result = (for {
        _ <- client.createBlog(request)
        blog <- client.getBlog(GetBlogRequest(request.id))
      } yield blog).futureValue
      // Then
      result shouldBe BlogResponse(
        id = request.id,
        name = request.name,
        description = request.description,
        mode = "public"
      )
    }
    "return an error when the blog not created" in {
      // When
      val result =
        client
          .getBlog(api.GetBlogRequest("blogId42"))
          .map(_ => Right("not expected"))
          .recover { case NonFatal(err) =>
            Left(err)
          }
          .futureValue
      // Then
      result.fold(
        _ shouldBe a[StatusRuntimeException],
        suc => s"must be failed but $suc"
      )
    }
  }

  override def afterAll(): Unit = {
    testKit.stop()
    super.afterAll()
  }
}
