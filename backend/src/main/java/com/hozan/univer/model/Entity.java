package com.hozan.univer.model;

import java.io.Serializable;

public interface Entity<I>  extends Serializable{
    Long getId();

    void setId(Long id);

    int identityHashCode();

    boolean  identityEquals(Entity<?> other);
}
