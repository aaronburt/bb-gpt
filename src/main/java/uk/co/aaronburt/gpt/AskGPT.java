package uk.co.aaronburt.gpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import uk.co.aaronburt.gpt.model.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bdavies.babblebot.api.command.Command;
import net.bdavies.babblebot.api.command.CommandParam;
import net.bdavies.babblebot.api.command.ICommandContext;
import net.bdavies.babblebot.api.obj.message.discord.DiscordMessage;
import net.bdavies.babblebot.api.plugins.Plugin;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.theokanning.openai.service.OpenAiService.*;

/**
 * @author author@aaronburt.co.uk (Aaron Burt)
 * @since 1.0.0
 *
 * MAKE SURE TO ADD THE OPENAIKEY BELOW OTHERWISE IT WILL FAIL
 */
@Plugin
@RequiredArgsConstructor
@Slf4j
public class AskGPT {
    private final TestRepository repository;

    @Command(aliases = "ask", description = "ask gpt a question")
    @CommandParam(value = "question", canBeEmpty = false, optional = false, exampleValue = "what happened in ww2?")
    public String askGPT(DiscordMessage message, ICommandContext context) {
        try {

            int maxTokenLength = 512;
            String systemCompletionPromptFormat = "You are an ai assistant for %s, you will reply to %s";
            String systemCompletionPrompt = String.format(systemCompletionPromptFormat, "Babble-bot", message.getAuthor().toString());
            String openAiKey = "";
            String model = "gpt-3.5-turbo";
            
            String chatCompletionPrompt = context.getParameter("question");
            ObjectMapper mapper = defaultObjectMapper();

            if (chatCompletionPrompt.length() == 0) throw new Exception("Prompt length is zero");
            OkHttpClient client = defaultClient(openAiKey, Duration.ofSeconds(180)).newBuilder().build();
            Retrofit retrofit = defaultRetrofit(client, mapper);
            OpenAiApi api = retrofit.create(OpenAiApi.class);
            OpenAiService service = new OpenAiService(api);

            List<ChatMessage> messages = new ArrayList<>();
            ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemCompletionPrompt);
            ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), chatCompletionPrompt);

            messages.add(systemMessage);
            messages.add(userMessage);

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                    .builder()
                    .model(model)
                    .messages(messages)
                    .maxTokens(maxTokenLength)
                    .temperature(0.7)
                    .build();

            ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
            return responseMessage.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return context.getParameter("question");
        }
    }
}

