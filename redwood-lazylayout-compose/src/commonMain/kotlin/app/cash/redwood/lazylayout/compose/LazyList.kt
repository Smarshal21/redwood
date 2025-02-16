/*
 * Copyright (C) 2022 Square, Inc.
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
@file:JvmName("LazyList") // Conflicts with generated LazyList compose widget

package app.cash.redwood.lazylayout.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.redwood.Modifier
import app.cash.redwood.layout.api.Constraint
import kotlin.jvm.JvmName

private const val OffscreenItemsBufferCount = 30

@Composable
internal fun LazyList(
  isVertical: Boolean,
  width: Constraint,
  height: Constraint,
  modifier: Modifier = Modifier,
  placeholder: @Composable () -> Unit,
  content: LazyListScope.() -> Unit,
) {
  val itemProvider = rememberLazyListItemProvider(content)
  var firstVisibleItemIndex by remember { mutableStateOf(0) }
  var lastVisibleItemIndex by remember { mutableStateOf(0) }
  val itemsBefore = remember(firstVisibleItemIndex) { (firstVisibleItemIndex - OffscreenItemsBufferCount / 2).coerceAtLeast(0) }
  val itemsAfter = remember(lastVisibleItemIndex, itemProvider.itemCount) { (itemProvider.itemCount - (lastVisibleItemIndex + OffscreenItemsBufferCount / 2).coerceAtMost(itemProvider.itemCount)).coerceAtLeast(0) }
  LazyList(
    isVertical,
    itemsBefore = itemsBefore,
    itemsAfter = itemsAfter,
    onViewportChanged = { localFirstVisibleItemIndex, localLastVisibleItemIndex ->
      firstVisibleItemIndex = localFirstVisibleItemIndex
      lastVisibleItemIndex = localLastVisibleItemIndex
    },
    width = width,
    height = height,
    modifier = modifier,
    placeholder = { repeat(75) { placeholder() } },
    items = {
      for (index in itemsBefore until itemProvider.itemCount - itemsAfter) {
        key(index) {
          itemProvider.Item(index)
        }
      }
    },
  )
}

@Composable
internal fun RefreshableLazyList(
  isVertical: Boolean,
  refreshing: Boolean = false,
  onRefresh: (() -> Unit)? = null,
  width: Constraint,
  height: Constraint,
  modifier: Modifier = Modifier,
  placeholder: @Composable () -> Unit,
  content: LazyListScope.() -> Unit,
) {
  val itemProvider = rememberLazyListItemProvider(content)
  var firstVisibleItemIndex by remember { mutableStateOf(0) }
  var lastVisibleItemIndex by remember { mutableStateOf(0) }
  val itemsBefore = remember(firstVisibleItemIndex) { (firstVisibleItemIndex - OffscreenItemsBufferCount / 2).coerceAtLeast(0) }
  val itemsAfter = remember(lastVisibleItemIndex, itemProvider.itemCount) { (itemProvider.itemCount - (lastVisibleItemIndex + OffscreenItemsBufferCount / 2).coerceAtMost(itemProvider.itemCount)).coerceAtLeast(0) }
  RefreshableLazyList(
    isVertical,
    itemsBefore = itemsBefore,
    itemsAfter = itemsAfter,
    onViewportChanged = { localFirstVisibleItemIndex, localLastVisibleItemIndex ->
      firstVisibleItemIndex = localFirstVisibleItemIndex
      lastVisibleItemIndex = localLastVisibleItemIndex
    },
    refreshing = refreshing,
    onRefresh = onRefresh,
    width = width,
    height = height,
    modifier = modifier,
    placeholder = { repeat(75) { placeholder() } },
    items = {
      for (index in itemsBefore until itemProvider.itemCount - itemsAfter) {
        key(index) {
          itemProvider.Item(index)
        }
      }
    },
  )
}
