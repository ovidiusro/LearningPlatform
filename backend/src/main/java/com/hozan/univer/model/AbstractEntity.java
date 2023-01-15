package com.hozan.univer.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Null;

import static java.util.function.DoubleUnaryOperator.identity;

@MappedSuperclass
public abstract class AbstractEntity<I>  implements Entity<I>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Null( groups = {Account.SignInForm.class, Account.SignUpForm.class})
    private Long id;

    @Override
    public Long getId() {
        return  id;
    }

    @Override
    public void setId(Long id) {this.id = id;}

    @Override
    public final boolean identityEquals(Entity<?> other) {
        if (getId() == null) {
            return false;
        }
        return getId().equals(other.getId());
    }

    @Override
    public final int identityHashCode() {
        return new HashCodeBuilder().append(this.getId()).toHashCode();
    }

    @Override
    public final int hashCode() {
        return identityHashCode();
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        return identityEquals((Entity<?>) o);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + identity();
    }
}
