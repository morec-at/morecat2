package morecat.api.blog

import com.akkaserverless.scalasdk.testkit.ValueEntityResult
import com.google.protobuf.empty.Empty
import morecat.api
import morecat.api.{BlogResponse, CreateBlogRequest}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BlogHandlerSpec extends AnyWordSpec with Matchers {

  "createBlog" must {
    class HappyPath {
      val testKit: BlogHandlerTestKit = BlogHandlerTestKit(new BlogHandler(_))
    }
    "create a blog" in new HappyPath {
      // When
      val result: ValueEntityResult[Empty] = testKit.createBlog(CreateBlogRequestGenerator.gen())
      // Then
      result.reply shouldBe Empty.defaultInstance
    }
  }

  "getBlog" must {
    class HappyPath {
      val testKit: BlogHandlerTestKit = BlogHandlerTestKit(new BlogHandler(_))
    }
    "return a blog has the id" in new HappyPath {
      // Given
      val request: CreateBlogRequest = CreateBlogRequestGenerator.gen()
      testKit.createBlog(request)
      // When
      val result: ValueEntityResult[BlogResponse] = testKit.getBlog(api.GetBlogRequest(request.id))
      // Then
      result.reply shouldBe BlogResponse(
        id = request.id,
        name = request.name,
        description = request.description,
        mode = "public"
      )
    }
    "return an error when the blog not created" in new HappyPath {
      // When
      val result: ValueEntityResult[BlogResponse] = testKit.getBlog(api.GetBlogRequest("blogId42"))
      // Then
      result.isError shouldBe true
    }
  }
}
