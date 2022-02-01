package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model)
    {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemid}")
    public String item(@PathVariable long itemid, Model model)
    {
        Item item = itemRepository.findById(itemid);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm()
    {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String saveV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        model.addAttribute("item", item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String saveV2(@ModelAttribute("item") Item item, Model model) {   //modelAttribute에 이름을 부여하면 자동적으로 addAttribute로써 해당 모델이 추가됨
        //model.addAttribute("item", item);
        itemRepository.save(item);
        return "basic/item";
    }

   // @PostMapping("/add")
    public String saveV3(@ModelAttribute Item item) {
        //model.addAttribute("item", item);  //modelAttribute에 이름을 부여하지 않는다면 Item의 소문자형으로 addAttribute로 추가됨
        itemRepository.save(item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String saveV4(Item item) {
        //model.addAttribute("item", item);  //modelAttribute는 생략 가능 (but too much!)
        itemRepository.save(item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String saveV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();  //post후 뷰템플릿으로 리턴하면 새로고침하면 계속 post가 전달 (마지막 행위가 post였으므로), 하지만 리다이렉트하면 뷰템플릿으로 띄워주는 것이 아닌 GET요청으로 불러오는 것이므로 새로고침시 GET을 한번 더 수행하게 되므로 문제 해결
    }

    @PostMapping("/add")
    public String saveV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());   //쿼리 스트링, pathVariable로 사용할 수 있는 redirectAttribute
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";  //itemId는 {}형식으로 들어가고, 나머지 status는 쿼리스트링으로 들어감
    }



    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model)
    {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String update(@PathVariable Long itemId, @ModelAttribute Item item)
    {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }


    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000,10));
        itemRepository.save(new Item("itemB", 20000,20));

    }
}
