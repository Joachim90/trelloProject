package jke.trelloproject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TrelloController {

    private final TrelloService trelloService;

    public TrelloController(TrelloService trelloService) {
        this.trelloService = trelloService;
    }

    @GetMapping("/boards")
    public String getBoards(Model model) {
        model.addAttribute("boards", trelloService.getBoards());
        return "boards"; // boards.html i templates-mappen
    }

    @PostMapping("/addCard")
    public String addCard(@RequestParam String listId,
                          @RequestParam String name,
                          Model model) {
        trelloService.createCard(listId, name);
        model.addAttribute("boards", trelloService.getBoards());
        return "boards";
    }
}
