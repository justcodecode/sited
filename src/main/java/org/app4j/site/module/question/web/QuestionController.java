package org.app4j.site.module.question.web;

import org.app4j.site.module.question.domain.Answer;
import org.app4j.site.module.question.domain.Question;
import org.app4j.site.module.question.service.AnswerService;
import org.app4j.site.module.question.service.QuestionService;
import org.app4j.site.internal.database.FindView;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class QuestionController {
    private QuestionService questionService;
    private AnswerService answerService;

    public QuestionController setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
        return this;
    }

    public QuestionController setAnswerService(AnswerService answerService) {
        this.answerService = answerService;
        return this;
    }

    public Response getQuestion(Request request) throws IOException {
        int offset = request.query("offset", Integer.class).orElse(0).get();
        int fetchSize = request.query("fetchSize", Integer.class).orElse(10).get();
        String questionId = request.query("questionId").get();

        Question question = questionService.findById(questionId);

        if (question == null) {
            throw new NotFoundException("question " + questionId);
        }

        FindView<Answer> answers = answerService.findByQuestionId(questionId, offset, fetchSize);

        Map<String, Object> model = new HashMap<>();
        model.put("question", question);
        model.put("answers", answers);
        return Response.bean(model);
    }

    public Response createAnswer(Request request) throws IOException {
        String questionId = request.query("questionId").get();
        Answer answer = request.body(Answer.class);

        answer.setQuestionId(questionId);
        answerService.save(answer);

        return Response.bean(answer);
    }
}
