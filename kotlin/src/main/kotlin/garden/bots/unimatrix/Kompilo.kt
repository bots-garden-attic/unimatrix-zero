package garden.bots.unimatrix

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import javax.script.Invocable
import javax.script.ScriptEngineManager

class Kompilo {
  val engine = ScriptEngineManager().getEngineByExtension("kts")!!
  val invoker = engine as Invocable

  fun compileFunction(functionCode : String) : Either<Exception, Any> {
    return try {
        Right(engine.eval(functionCode))
    } catch (exception: Exception) {
      Left(exception)
    }
  }

  fun invokeHandleFunction(params : Any) : Either<Exception, Any> {
    return try {
      Right(invoker.invokeFunction("handle", params))
    } catch (exception: Exception) {
      Left(exception)
    }
  }
}
