# eff

[![Join the chat at https://gitter.im/atnos-org/eff](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/atnos-org/eff?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://img.shields.io/maven-central/v/org.atnos/eff_3.svg)](https://maven-badges.herokuapp.com/maven-central/org.atnos/eff_3)

Extensible effects are an alternative to monad transformers for computing with effects in a functional way.
This library is based on the "free-er" monad and extensible effects described in
Oleg Kiselyov in [Freer monads, more extensible effects](https://okmij.org/ftp/Haskell/extensible/more.pdf).

You can learn more in the User Guide:

 - [your first effects](https://atnos-org.github.io/eff/org.atnos.site.Introduction.html)
 - [included effects: `Reader`, `Writer`, `Eval`, `State`,...](https://atnos-org.github.io/eff/org.atnos.site.OutOfTheBox.html)
 - [create your own effects](https://atnos-org.github.io/eff/org.atnos.site.CreateEffects.html)
 - [use Member implicits](https://atnos-org.github.io/eff/org.atnos.site.MemberImplicits.html)
 - [working with different effect stacks](https://atnos-org.github.io/eff/org.atnos.site.TransformStack.html)
 - [a tutorial similar to the cats' tutorial for Free monads](https://atnos-org.github.io/eff/org.atnos.site.Tutorial.html)

You can also check out [this presentation](https://bit.ly/eff_flatmap_2016) at flatMap Oslo 2016 ([slides](https://www.slideshare.net/etorreborre/the-eff-monad-one-monad-to-rule-them-all)).

## Installation

Eff is published for Scala 3. `eff` core is available for the JVM, ScalaJS and scala-native. Sbt dependency:

```scala
// check maven badge above for latest version
libraryDependencies += "org.atnos" %% "eff" % "8.0.0"

// for Scala 3.3.x
scalacOptions += "-Ykind-projector"

// for latest Scala 3
scalacOptions += "-Xkind-projector"
```

for Scala 2.x

```scala
libraryDependencies += "org.atnos" %% "eff" % "7.0.6"
```

# Contributing

[eff](https://github.com/atnos-org/eff/) is a [Typelevel](https://typelevel.org) project. This means we embrace pure, typeful, functional programming,
and provide a safe and friendly environment for teaching, learning, and contributing as described in the [Scala Code of Conduct](https://www.scala-lang.org/conduct/).

Feel free to open an issue if you notice a bug, have an idea for a feature, or have a question about the code. Pull requests are also gladly accepted.

# Extensions

## Modules in this repository

- [scalaz](./scalaz)
- [monix](./monix)
- [doobie](./doobie)

## External

- [eff-zio](https://github.com/takayahilton/eff-zio) eff extension for ZIO effects.
