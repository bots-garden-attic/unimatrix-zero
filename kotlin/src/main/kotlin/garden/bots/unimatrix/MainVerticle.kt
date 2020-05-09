package garden.bots.unimatrix

import arrow.core.Either
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

class MainVerticle : AbstractVerticle() {

  override fun stop(stopFuture: Future<Void>) {
    super.stop()
  }

  override fun start(startPromise: Promise<Void>) {

    val router = Router.router(vertx)
    router.route().handler(BodyHandler.create())
    val kompilo = Kompilo()
    val httpPort = System.getenv("PORT")?.toInt() ?: 8080
    val readme = System.getenv("README") ?: "👋 Hello World 🌍"
    val contentType = System.getenv("CONTENT_TYPE") ?: "application/json;charset=UTF-8"
    val functionName = System.getenv("FUNCTION_NAME") ?: "hello"

    // should I name the function as I want?
    val functionCode = System.getenv("FUNCTION_CODE") ?: """
      fun handle(params: Any): Any {
        return json {
          obj("message" to "👋 Hello World 🌍")
          obj("params" to params)
        }.encodePrettily()
      }
    """.trimIndent()

    val compiledFunction = kompilo.compileFunction(functionCode)

    router.route("/*").handler(StaticHandler.create().setCachingEnabled(false))


    compiledFunction.let {
      when(it) {
        is Either.Left -> { // compilation error
          router.post("/${functionName}").handler { context ->
            context.response().putHeader("content-type", "application/json;charset=UTF-8")
              .end(
                json {
                  obj("error" to it.a.message)
                }.encodePrettily()
              )
          }
        }
        is Either.Right -> { // compilation is OK, and name of the function to invoke is "handle"
          //val function = it.b
          router.post("/${functionName}").handler { context ->

            val params = context.bodyAsJson
            // call the function
            kompilo.invokeHandleFunction(params).let {
              when(it) {
                is Either.Left -> { // execution error
                  context.response().putHeader("content-type", "application/json;charset=UTF-8")
                    .end(
                      json {
                        obj("error" to it.a.message)
                      }.encodePrettily()
                    )
                }
                is Either.Right -> { // execution is OK
                  val result = it.b
                  context.response().putHeader("content-type", contentType)
                    .end(result.toString())
                }
              }
            }
          }
        }
      }
    }

    router.get("/README").handler { context ->
      context.response().putHeader("content-type", "text/plain;charset=UTF-8")
        .end(readme)
    }

    router.get("/${functionName}").handler { context ->
      context.response().putHeader("content-type", "application/json;charset=UTF-8")
        .end(
          json {
            obj("message" to "🖐️ please use POST to call the ${functionName}")
          }.encodePrettily()
        )
    }

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(httpPort) { http ->
        when {
          http.failed() -> {
            startPromise.fail(http.cause())
          }
          http.succeeded() -> {
            println("🌍 Kotlin UniMatrix-Zero runtime for $functionName function started on port $httpPort")
            startPromise.complete()
          }
        }
      }
  }
}
