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
package app.cash.redwood.protocol.widget

import app.cash.redwood.protocol.EventSink
import app.cash.redwood.protocol.PropertyDiff
import app.cash.redwood.widget.Widget

/**
 * A [Widget] which consumes protocol diffs and applies them to a platform-specific representation.
 */
public interface DiffConsumingWidget<T : Any> : Widget<T> {
  public fun apply(diff: PropertyDiff, eventSink: EventSink) {
    throw IllegalArgumentException("Widget has no properties")
  }

  public fun children(tag: Int): Widget.Children<T> {
    throw IllegalArgumentException("Widget does not support children")
  }

  public interface Factory<T : Any> {
    public fun create(kind: Int): DiffConsumingWidget<T>
  }
}