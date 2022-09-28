package ru.practicum.shareit.requests.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;
import java.util.Set;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findItemRequestByRequestor_Id(long userId);

    @Query("select new ru.practicum.shareit.item.dto.ItemDto(" +
            "i.id, " +
            "i.name, " +
            "i.description, " +
            "i.available," +
            "i.owner.id," +
            "i.requestId) " +
            "from Item i " +
            "where i.requestId = ?1 ")
    Set<ItemDto> findItemsByRequestId(long requestId);

    @Query("select new ru.practicum.shareit.requests.dto.ItemRequestDto(" +
            "ir.id, " +
            "ir.description, " +
            "ir.created) " +
            "from ItemRequest ir " +
            "where ir.requestor.id <> ?1 ")
    Page<ItemRequestDto> findAllItemRequestByOtherUser(long userId, Pageable pageable);
}