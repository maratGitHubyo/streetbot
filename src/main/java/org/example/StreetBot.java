package org.example;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.methods.impl.messages.GetConversationMembers;
import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.basic.Message;
import api.longpoll.bots.model.objects.basic.User;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class StreetBot extends LongPollBot {

    private final List<String> citiesInGame = new LinkedList<>();

    @Override
    public void onMessageNew(MessageNew messageNew) {
        try {
            Message message = messageNew.getMessage();
            if (Boolean.TRUE.equals(message.hasText())) {
                String text = message.getText();
                if ("(. Y .) Привет".equals(text)) {
                    vk.messages.send()
                            .setPeerId(message.getPeerId())
                            .setMessage("Всем привет🤑 \n Я частенько обитаю на стрите, но в душе моей моей восьмуха♥ " +
                                    "\n и еще я очень люблю одного человека ☺\n\n\n" +
                                    "Я пока мало чего умею, но вы можете накидать идей и я обязательно научусь🤗")
                            .execute();
                } else if ("(. Y .) кого ты любишь?".equals(text)) {
                    GetConversationMembers.ResponseBody execute = vk.messages.getConversationMembers()
                            .setPeerId(message.getPeerId())
                            .execute();
                    List<User> profiles = execute.getResponse().getProfiles();
                    Random random = new Random();
                    int randomUser = random.nextInt(profiles.size());
                    User user = profiles.get(randomUser);
                    vk.messages.send()
                            .setPeerId(message.getPeerId())
                            .setMessage("Я просто обожаю @" + user.getScreen_name() + "♥🥰😍")
                            .execute();

                } else if ("(. Y .) Дай езжи".equals(text)) {
                    GetConversationMembers.ResponseBody execute = vk.messages.getConversationMembers()
                            .setPeerId(message.getPeerId())
                            .execute();
                    List<User> profiles = execute.getResponse().getProfiles();
                    Random random = new Random();
                    int randomUser = random.nextInt(profiles.size());
                    User user = profiles.get(randomUser);
                    vk.messages.send()
                            .setPeerId(message.getPeerId())
                            .setMessage("Долбаеб дня @" + user.getScreen_name())
                            .execute();

                } else if (text.contains("(. Y .) repeat ")) {
                    String replaced = text.replace("(. Y .) repeat ", "");
                    for (int i = 0; i < 5; i++) {
                        vk.messages.send()
                                .setPeerId(message.getPeerId())
                                .setMessage("@all " + replaced)
                                .execute();
                        Thread.sleep(5000);
                    }
                } else if (text.contains("(. Y .) города отчистить")) {
                    citiesInGame.clear();
                    vk.messages.send()
                            .setPeerId(message.getPeerId())
                            .setMessage("Списки ответов отчищены 🏙")
                            .execute();
                    Thread.sleep(5000);
                    } else if (text.contains("(. Y .) города")) {
                    String city = text.replace("(. Y .) города ", "");
                    String beforeCity = "";
                    String substring = "";
                    if (!citiesInGame.isEmpty()) {
                        beforeCity = citiesInGame.get(citiesInGame.size() - 1);
                        substring = beforeCity.substring(beforeCity.length() - 1);
                        if (substring.equals("ь") || substring.equals("ъ") || substring.equals("ы")) {
                            substring = beforeCity.substring(beforeCity.length() - 2, beforeCity.length() - 1);
                        }
                    }
                    if (!beforeCity.isEmpty() && !substring.isEmpty() && !city.startsWith(substring.toUpperCase())) {
                        vk.messages.send()
                                .setPeerId(message.getPeerId())
                                .setMessage("Буковка та не та 🤓")
                                .execute();
                        return;
                    }
                    CitiesBase citiesBase = CitiesBase.getInstance();
                    if (citiesInGame.contains(city)) {
                        vk.messages.send()
                                .setPeerId(message.getPeerId())
                                .setMessage("А повторяться запрещено! 🥶")
                                .execute();
                    } else if (CitiesBase.contains(city)) {
                        citiesInGame.add(city);
                        for (Iterator<String> it = CitiesBase.iterator(); it.hasNext(); ) {
                            String cityForUser = it.next();
                            if (cityForUser.startsWith(city.substring(city.length() - 1).toUpperCase()) && !citiesInGame.contains(cityForUser)) {
                                citiesInGame.add(cityForUser);
                                vk.messages.send()
                                        .setPeerId(message.getPeerId())
                                        .setMessage(cityForUser)
                                        .execute();
                                break;
                            }
                        }

                    } else {
                        vk.messages.send()
                                .setPeerId(message.getPeerId())
                                .setMessage("Нет, такого города не существует! 💀")
                                .execute();
                    }
                }
            }
        } catch (VkApiException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.onMessageNew(messageNew);
    }

    @Override
    public String getAccessToken() {
        return "vk1.a.t-8E_fXRH9p0c9sVc8D22RUtiV7lgUycRNd-zNi_GLiV0AD0kVjNYq8lrUZLx_YOASOLcYm70RaMQ5DHJCwI7ZtQKajU8ll4mUQ3gLgqvaVj91Lx5--lgtcXEkOowFoveHd8l5BeFN94aMSJ7YkaekSIA1zTd9d5vpWijkRF4kES8uC3LIFiw7sdeqXOKzE0";
    }

    public static void main(String[] args) throws VkApiException {
        new StreetBot().startPolling();
    }

}
