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

import precog.AutoBump.ChangeLabel
import sbt.librarymanagement.VersionNumber

import scala.util.matching.Regex

final case class Change(org: String, repoSlug: String, oldRevision: String, newRevision: String) {
  val label: ChangeLabel = {
    val oldVersion = VersionNumber(oldRevision)
    val newVersion = VersionNumber(newRevision)
    val isRevision = VersionNumber.SecondSegment.isCompatible(oldVersion, newVersion)
    val isBreaking = !VersionNumber.SemVer.isCompatible(oldVersion, newVersion)
    ChangeLabel.fromName(Change.getChange(isRevision, isBreaking))
  }
}

object Change {
  val versionPattern: String = raw"""\d+\.\d+\.\d+(?:-[a-f0-9]+)?"""
  val updatePattern: Regex =
    raw""".*Updated ${AutoBump.ChangeLabel.namePattern} (\S+?)-(\S+) ($versionPattern) -> ($versionPattern).*""".r

  def getChange(isRevision: Boolean, isBreaking: Boolean): String =
    if (isRevision) "revision"
    else if (isBreaking) "breaking"
    else "feature"

  // TODO: support old format when migrating to new format
  def fromString(line: String): Option[Change] = {
    line match {
      case updatePattern(org, repoSlug, oldVersion, newVersion) => Some(Change(org, repoSlug, oldVersion, newVersion))
      case _                                                    => None
    }
  }
}
