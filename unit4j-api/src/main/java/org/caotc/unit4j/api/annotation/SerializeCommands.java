/*
 * Copyright (C) 2020 the original author or authors.
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

package org.caotc.unit4j.api.annotation;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

/**
 * 序列化指令集合{@link SerializeCommand}
 *
 * @author caotc
 * @date 2019-05-09
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
public class SerializeCommands implements List<SerializeCommand> {

  @NonNull
  @Singular
  ImmutableList<SerializeCommand> commands;

  @Override
  public int size() {
    return commands.size();
  }

  @Override
  public boolean isEmpty() {
    return commands.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return commands.contains(o);
  }

  @Override
  public Iterator<SerializeCommand> iterator() {
    return commands.iterator();
  }

  @Override
  public Object[] toArray() {
    return commands.toArray();
  }

  @SuppressWarnings("SuspiciousToArrayCall")
  @Override
  public <T> T[] toArray(@NonNull T[] a) {
    return commands.toArray(a);
  }

  @Deprecated
  @Override
  public boolean add(SerializeCommand serializeCommand) {
    return commands.add(serializeCommand);
  }

  @Deprecated
  @Override
  public boolean remove(Object o) {
    return commands.remove(o);
  }

  @Override
  public boolean containsAll(@NonNull Collection<?> c) {
    return commands.containsAll(c);
  }

  @Deprecated
  @Override
  public boolean addAll(@NonNull Collection<? extends SerializeCommand> c) {
    return commands.addAll(c);
  }

  @Deprecated
  @Override
  public boolean addAll(int index, @NonNull Collection<? extends SerializeCommand> c) {
    return commands.addAll(index, c);
  }

  @Deprecated
  @Override
  public boolean removeAll(@NonNull Collection<?> c) {
    return commands.removeAll(c);
  }

  @Deprecated
  @Override
  public boolean retainAll(@NonNull Collection<?> c) {
    return commands.retainAll(c);
  }

  @Deprecated
  @Override
  public void clear() {
    commands.clear();
  }

  @Override
  public SerializeCommand get(int index) {
    return commands.get(index);
  }

  @Deprecated
  @Override
  public SerializeCommand set(int index, SerializeCommand element) {
    return commands.set(index, element);
  }

  @Deprecated
  @Override
  public void add(int index, SerializeCommand element) {
    commands.add(index, element);
  }

  @Deprecated
  @Override
  public SerializeCommand remove(int index) {
    return commands.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return commands.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return commands.lastIndexOf(o);
  }

  @Override
  public ListIterator<SerializeCommand> listIterator() {
    return commands.listIterator();
  }

  @Override
  public ListIterator<SerializeCommand> listIterator(int index) {
    return commands.listIterator(index);
  }

  @Override
  public List<SerializeCommand> subList(int fromIndex, int toIndex) {
    return commands.subList(fromIndex, toIndex);
  }
}
