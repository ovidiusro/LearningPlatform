package com.hozan.univer.model;

import java.io.Serializable;

public interface Entity<I>  extends Serializable{
    /**
     * @return model identity
     */
    Long getId();

    /**
     * set model identity
     */
    void setId(Long id);

    /**
     * @return HashCode of model identity
     */
    int identityHashCode();

    /**
     * @param other
     *            Other model
     * @return true if identities of entities are equal
     */
    boolean  identityEquals(Entity<?> other);
}
