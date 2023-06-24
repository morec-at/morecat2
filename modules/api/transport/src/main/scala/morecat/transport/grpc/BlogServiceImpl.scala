package morecat.transport.grpc

import scala.concurrent.Future

class BlogServiceImpl extends BlogService {
  override def createBlog(in: CreateBlogRequest): Future[Blog] = {
    Future.successful(Blog(id = "1", title = in.title))
  }

  override def getBlog(in: GetBlogRequest): Future[Blog] = {
    Future.successful(Blog(id = in.id, title = "My Blog"))
  }
}
