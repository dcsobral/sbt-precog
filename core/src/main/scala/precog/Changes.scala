/*
 * Copyright 2020 Precog Data
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package precog

import cats.implicits._
import precog.AutoBump.ChangeLabel
import sbttrickle.metadata.ModuleUpdateData

import scala.collection.immutable.Map

final case class Changes(changes: Map[ChangeLabel, List[Change]]) {
  val label: ChangeLabel = changes.keys.max
}

object Changes {

  def fromOutdatedDependencies(outdated: Seq[ModuleUpdateData]): Changes = ???

  /** Extract updated versions from trickleUpdateDependencies log */
  def fromSummary(lines: List[String]): Changes = {
    val changes = lines.flatMap(Change.fromString).groupBy(_.label)
    Changes(changes)
  }

}
