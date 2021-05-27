package org.atnos.eff

import cats.Eval
import org.specs2._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import cats.syntax.all._
import org.scalacheck.Gen
import EitherEffect.{left => leftE, right => rightE}
import scala.collection.mutable.ListBuffer

class EffLastSpec extends Specification with ScalaCheck { def is = isolated ^ s2"""

  An action can run completely at the end, regardless of the number of flatmaps $runLast
    now with one very last action which fails, there should be no exception     $runLastFail

  `addLast` run several times and flatMap, then last actions should be executed by reverse order $runLastSeveralTimes
    the same result if `addLast` run at first of `for`                                           $runLastAtFirst

  bracket last triggers the last action regardless of the effects at play
    either right + ok $e1
    either right + protect exception $e2

    either left + ok $e3
    either left + protect exception $e4

  An expression must still be protected with `thenFinally` if there are further flatMaps $e5

"""

  def runLast = prop { (xs: List[String]) =>
    type R = Fx.fx2[Safe, Eval]
    val messages = new ListBuffer[String]

    import org.atnos.eff.all._
    import org.atnos.eff.syntax.all._

    val act = for {
      _ <- protect[R, Unit](messages.append("a")).addLast(protect[R, Unit](messages.append("end")))
      _ <- protect[R, Unit](messages.append("b"))
    } yield ()

    act.runSafe.runEval.run

    messages.toList ==== List("a", "b", "end")
  }.setGen(Gen.listOf(Gen.oneOf("a", "b", "c")))

  def runLastSeveralTimes = prop { (xs: List[String]) =>
    type R = Fx.fx2[Safe, Eval]
    val messages = new ListBuffer[String]

    import org.atnos.eff.all._
    import org.atnos.eff.syntax.all._

    val act = (for {
      _ <- protect[R, Unit](messages.append("a"))
      _ <- protect[R, Unit](messages.append("b")).addLast(protect[R, Unit](messages.append("end1")))
      _ <- protect[R, Unit](messages.append("c"))
      _ <- protect[R, Unit](messages.append("d")).addLast(protect[R, Unit](messages.append("end2")))
    } yield ()).addLast(
      protect[R, Unit](messages.append("end3"))
    )

    act.runSafe.runEval.run

    messages.toList ==== List("a", "b", "c", "d", "end2", "end1", "end3")
  }.setGen(Gen.listOf(Gen.oneOf("a", "b", "c")))

  def runLastAtFirst = prop { (xs: List[String]) =>
    type R = Fx.fx2[Safe, Eval]
    val messages = new ListBuffer[String]

    import org.atnos.eff.all._
    import org.atnos.eff.syntax.all._

    val act = (for {
      _ <- protect[R, Unit](messages.append("a")).addLast(protect[R, Unit](messages.append("end1")))
      _ <- protect[R, Unit](messages.append("b"))
      _ <- protect[R, Unit](messages.append("c")).addLast(protect[R, Unit](messages.append("end2")))
      _ <- protect[R, Unit](messages.append("d"))
    } yield ()).addLast(
      protect[R, Unit](messages.append("end3"))
    )

    act.runSafe.runEval.run

    messages.toList ==== List("a", "b", "c", "d", "end2", "end1", "end3")
  }.setGen(Gen.listOf(Gen.oneOf("a", "b", "c")))

  def runLastFail = prop { (xs: List[String]) =>
    type R = Fx.fx2[Safe, Eval]
    val messages = new ListBuffer[String]

    import org.atnos.eff.all._
    import org.atnos.eff.syntax.all._

    val act = for {
      _ <- protect[R, Unit](messages.append("a")).addLast(protect[R, Unit]{ messages.append("boom"); throw new Exception("boom")})
      _ <- protect[R, Unit](messages.append("b"))
    } yield ()

    act.runSafe.runEval.run

    messages.toList ==== List("a", "b", "boom")
  }.setGen(Gen.listOf(Gen.oneOf("a", "b", "c")))

  var i = 0

  def e1 = checkRelease {
    rightE[S, String, Int](1) >>= (v => protect[S, Int](v))
  }

  def e2 = checkRelease {
    rightE[S, String, Int](1) >>= (v => protect[S, Int] { sys.error("ouch"); v })
  }

  def e3 = checkRelease {
    leftE[S, String, Int]("Error") >>= (v => protect[S, Int](v))
  }

  def e4 = checkRelease {
    leftE[S, String, Int]("Error") >>= (v => protect[S, Int] { sys.error("ouch"); v })
  }

  def e5 = {
    var i = 0

    val action =
      (protect[S, Int](1) >>
       thenFinally(leftE[S, String, Int]("Error"), protect[S, Unit]({i = 1}))).
        map(i => i)

    action.execSafe.runEither.run
    i ==== 1
  }

  /**
   * HELPERS
   */
  type _eitherString[R] = Either[String, *] |= R

  def acquire[R :_Safe]: Eff[R, Int] = protect[R, Int] { i += 1; i }
  def release[R :_Safe]: Int => Eff[R, Int] = (_: Int) => protect[R, Int] { i -= 1; i }

  def checkRelease(use: Eff[S, Int]) = {
    eff(use).execSafe.flatMap(either => fromEither(either.leftMap(_.getMessage))).runEither.run
    i ==== 0
  }

  def eff[R :_Safe :_eitherString](use: Eff[R, Int]): Eff[R, Int] =
    bracketLast(acquire[R])(_ => use)(release[R])

  type S = Fx.fx2[Safe, Either[String, *]]

}
