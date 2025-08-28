package jke.trelloproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Service
public class TrelloService {

    private final WebClient webClient;
    private final String apiKey;
    private final String apiToken;

    public TrelloService(
            @Value("${trello.api.base-url}") String baseUrl,
            @Value("${trello.api.key}") String apiKey,
            @Value("${trello.api.token}") String apiToken) {

        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.apiToken = apiToken;
    }

    /** Hämta alla boards för inloggad användare */
    public List<TrelloBoard> getBoards() {
        TrelloBoard[] boards = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/members/me/boards")
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .build())
                .retrieve()
                .bodyToMono(TrelloBoard[].class)
                .block();

        return Arrays.asList(boards);
    }

    /** Skapa ett nytt kort i en lista */
    public String createCard(String listId, String name) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/cards")
                        .queryParam("idList", listId)
                        .queryParam("name", name)
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /** Hämta alla listor för en specifik board */
    public List<TrelloList> getLists(String boardId) {
        TrelloList[] lists = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/boards/{boardId}/lists")
                        .queryParam("key", apiKey)
                        .queryParam("token", apiToken)
                        .build(boardId))
                .retrieve()
                .bodyToMono(TrelloList[].class)
                .block();

        return Arrays.asList(lists);
    }

    record TrelloList(String id, String name) {}
}



