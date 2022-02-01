package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach(){
        itemRepository.clearStore();
    }

    @Test
    void save(){
        Item item = new Item("itemA", 10000,10);

        Item savedItem = itemRepository.save(item);

        Item findITem = itemRepository.findById(item.getId());

        Assertions.assertThat(findITem).isEqualTo(savedItem);
    }

    @Test
    void findAll(){

        Item item1 = new Item("itemA", 10000,10);
        Item item2 = new Item("itemB", 12000,13);

        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> list = itemRepository.findAll();

        Assertions.assertThat(list.size()).isEqualTo(2);
        Assertions.assertThat(list).contains(item1, item2);

    }


    @Test
    void update(){
        Item item1 = new Item("itemA", 10000,10);

        Item savedItem = itemRepository.save(item1);
        Long id = savedItem.getId();

        Item updateParam = new Item("itemV", 10001,30);
        itemRepository.update(id, updateParam);

        Item foundItem = itemRepository.findById(id);
        Assertions.assertThat(foundItem.getPrice()).isEqualTo(updateParam.getPrice());
        Assertions.assertThat(foundItem.getQuantity()).isEqualTo(updateParam.getQuantity());

    }

}
