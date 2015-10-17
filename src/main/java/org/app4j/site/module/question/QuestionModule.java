package org.app4j.site.module.question;

import org.app4j.site.Module;
import org.app4j.site.Site;
import org.app4j.site.module.question.codec.AnswerCodec;
import org.app4j.site.module.question.codec.QuestionCodec;
import org.app4j.site.module.question.service.AnswerService;
import org.app4j.site.module.question.service.QuestionService;
import org.app4j.site.module.question.web.QuestionController;

/**
 * @author chi
 */
public class QuestionModule extends Module {
    public QuestionModule(Site site) {
        super(site);
    }

    @Override
    protected void configure() throws Exception {
        site().database().codecs()
            .add(new QuestionCodec())
            .add(new AnswerCodec());


        QuestionService questionService = new QuestionService(site().database().get());
        bind(QuestionService.class).to(questionService).export();

        AnswerService answerService = new AnswerService(site().database().get());
        bind(AnswerService.class).to(answerService).export();

        QuestionController questionController = new QuestionController()
            .setQuestionService(questionService)
            .setAnswerService(answerService);

        site().route()
            .get("/api/question/{questionId}", questionController::getQuestion)
            .post("/api/question/{questionId}/answer", questionController::createAnswer);
    }

}
