package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ITEMS")
public class Item {
    @Id
    @Column(name = "ID_ITEM")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME_ITEM", nullable = false)
    private String name;
    private String description;
    @Column(name = "IS_AVAILABLE")
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    public Item(long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && Objects.equals(name, item.name) &&
                Objects.equals(description, item.description) &&
                Objects.equals(available, item.available) &&
                Objects.equals(owner, item.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, owner);
    }
}
