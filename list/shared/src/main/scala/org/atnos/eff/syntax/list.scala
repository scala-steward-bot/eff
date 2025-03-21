package org.atnos.eff.syntax

import org.atnos.eff.*

object list extends list

trait list {

  implicit class ListEffectOps[R, A](e: Eff[R, A]) {

    def runList(implicit member: Member[List, R]): Eff[member.Out, List[A]] =
      ListInterpretation.runList(e)(using member.aux)

  }

}
