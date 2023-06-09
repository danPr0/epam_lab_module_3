package com.epam.esm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Entity class for "gift_certificates" table.
 *
 * @author Danylo Proshyn
 */

@Entity
@Table(name = "gift_certificates")
@Audited
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GiftCertificate {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "duration")
    private int duration;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "active")
    private boolean isActive;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable (name="gift_certificates_tags",
                joinColumns=@JoinColumn (name="gc_id"),
                inverseJoinColumns=@JoinColumn(name="tag_id"))
    private List<Tag> tags;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof GiftCertificate that)) {
            return false;
        }
        return Double.compare(that.price, price) == 0 && duration == that.duration && id.equals(that.id) &&
                name.equals(that.name) && description.equals(that.description) && createDate.equals(that.createDate) &&
                lastUpdateDate.equals(that.lastUpdateDate) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate, tags);
    }
}
