/*
 * Copyright (C) 2021 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.cash.redwood.generator

import app.cash.redwood.schema.Children
import app.cash.redwood.schema.Schema
import app.cash.redwood.schema.Widget
import app.cash.redwood.schema.parser.parseSchema
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ComposeGenerationTest {
  @Schema(
    [
      Row::class,
    ]
  )
  interface ScopedAndUnscopedSchema
  object RowScope
  @Widget(1)
  data class Row(
    @Children(1, RowScope::class) val scoped: List<Any>,
    @Children(2) val unscoped: List<Any>,
  )

  @Test fun scopedAndUnscopedChildren() {
    val schema = parseSchema(ScopedAndUnscopedSchema::class)

    val fileSpec = generateComposable(schema, schema.widgets.single())
    assertThat(fileSpec.toString()).apply {
      contains("scoped: @Composable RowScope.() -> Unit")
      contains("RowScope.scoped()")

      // Remove wrapping whitespace when https://github.com/square/kotlinpoet/issues/274 ships.
      contains("unscoped: @Composable\n    () -> Unit")
      contains("unscoped()")
    }
  }
}