package org.atnos.eff
package addon.scalaz

import cats.syntax.all.*
import scalaz.*

object safe extends safe

trait safe {

  def runSafeDisjunction[R, U, A](r: Eff[R, A])(using Member.Aux[Safe, R, U]): Eff[U, (Throwable \/ A, List[Throwable])] =
    org.atnos.eff.SafeEffect.runSafe(r).map(_.leftMap(_.fold(\/.left, \/.right)))

  def execSafeDisjunction[R, U, A](r: Eff[R, A])(using Member.Aux[Safe, R, U]): Eff[U, Throwable \/ A] =
    org.atnos.eff.SafeEffect.execSafe(r).map(_.fold(\/.left, \/.right))

  def attemptSafeDisjunction[R, A](r: Eff[R, A])(using Safe /= R): Eff[R, (Throwable \/ A, List[Throwable])] =
    org.atnos.eff.SafeEffect.attemptSafe(r).map(_.leftMap(_.fold(\/.left, \/.right)))

  def attemptDisjunction[R, A](action: Eff[R, A])(using Safe /= R): Eff[R, Throwable \/ A] =
    org.atnos.eff.SafeEffect.attempt(action).map(_.fold(\/.left, \/.right))

}
