package ru.practicum.shareit.requests.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "REQUESTS")
public class ItemRequest {
    @Id
    @Column(name = "ID_REQUEST")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "REQUESTOR_ID")
    private User requestor;

    @JsonFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest that = (ItemRequest) o;
        return id == that.id &&
                Objects.equals(description, that.description) &&
                Objects.equals(requestor, that.requestor) &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requestor, created);
    }
}
