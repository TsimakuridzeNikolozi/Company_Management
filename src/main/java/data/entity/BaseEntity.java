package data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class BaseEntity implements Comparable<BaseEntity>, Serializable {
    private UUID id;

    @Override
    public int compareTo(BaseEntity otherEntity) {
        return this.id.compareTo(otherEntity.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BaseEntity other = (BaseEntity) obj;
        return Objects.equals(id, other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
