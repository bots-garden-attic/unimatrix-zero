package garden.bots.unimatrix

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right

import org.graalvm.polyglot.*

class Kompilo {
  var language: String = ""
  val scriptContext = Context.create()

  fun compileFunction(functionCode : String, language: String) : Either<Exception, Any> {
    this.language = language
    return try {
      Right(scriptContext.eval(Source.create(language, functionCode)))
    } catch (exception: Exception) {
      Left(exception)
    }
  }

  fun invokeFunction(name : String, params : Any) : Either<Exception, Any> {
    return try {
      Right(scriptContext.getBindings(this.language).getMember(name).execute(params))
    } catch (exception: Exception) {
      Left(exception)
    }
  }
}
