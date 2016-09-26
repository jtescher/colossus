package colossus.examples

import colossus._

import protocols.http._
import stream._
import core.{DataBlock, ServerContext}
import controller._
import service._

import scala.util.{Failure, Success, Try}

object StreamServiceExample {

  def start(port: Int)(implicit sys: IOSystem) = {
    StreamHttpServiceServer.basic("stream-service", port, new GenRequestHandler[StreamingHttp](_) {

      def handle = {
        case StreamingHttpRequest(head, source) => source.collected.map{_ => 
          StreamingHttpResponse(
            HttpResponseHead(head.version, HttpCodes.OK,  HttpHeaders(HttpHeader("transfer-encoding",TransferEncoding.Chunked.value))), 
            Source.fromIterator(List("hello", "world", "blah").toIterator.map{s => BodyData(DataBlock(s))})
          )
        }
      }
    })

  }
}
