package com.practice.jwttokenauth.mapper;

public interface Mapper<S, T> {

    T map(S source);

    default void map(S source, T target) {}
}
