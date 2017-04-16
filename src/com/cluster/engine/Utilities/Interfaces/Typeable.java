package com.cluster.engine.Utilities.Interfaces;

public interface Typeable<T extends Enum<T>> {
    T getType();
}
