package morecat.api.blog

import com.akkaserverless.scalasdk.valueentity.{ValueEntity, ValueEntityContext}
import com.google.protobuf.empty.Empty
import morecat.api

class BlogHandler(context: ValueEntityContext) extends AbstractBlogHandler {
  override def emptyState: BlogState = BlogState()

  override def createBlog(
      currentState: BlogState,
      createBlogRequest: api.CreateBlogRequest
  ): ValueEntity.Effect[Empty] =
    effects.updateState(convertToState(createBlogRequest)).thenReply(Empty.defaultInstance)

  def convertToState(blog: api.CreateBlogRequest): BlogState =
    BlogState(
      id = blog.id,
      name = blog.name,
      description = blog.description,
      mode = blog.mode match {
        case api.BlogMode.PUBLIC | api.BlogMode.Unrecognized(_) => "public"
        case api.BlogMode.PRIVATE => "private"
      }
    )

  override def getBlog(
      currentState: BlogState,
      getBlogRequest: api.GetBlogRequest
  ): ValueEntity.Effect[api.BlogResponse] = {
    if (currentState.id == "") {
      effects.error(s"The Blog(ID: ${getBlogRequest.id}) Not Found")
    } else {
      effects.reply(convertToApi(currentState))
    }
  }

  def convertToApi(blog: BlogState): api.BlogResponse =
    api.BlogResponse(
      id = blog.id,
      name = blog.name,
      description = blog.description,
      mode = blog.mode
    )

}
