package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item createItem(Long userId, Item item);

    Item getItem(Long userId, Long itemId);

    Item updateItem(Long userId, Long itemId, Item item);

    void deleteItem(Long userId, Long itemId);

    List<Item> getAllItems(Long userId);

    List<Item> searchItems(Long userId, String text);
}
