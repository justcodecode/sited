package org.app4j.site.module.question.codec;

import org.app4j.site.module.question.domain.Question;
import org.bson.BsonObjectId;
import org.bson.BsonReader;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.util.List;


/**
 * @author chi
 */
public class QuestionCodec implements CollectibleCodec<Question> {
    DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public Question decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);

        Question question = new Question();

        question.setId(document.getObjectId("_id").toString());
        question.setUserId(document.getString("userId"));
        question.setSubject(document.getString("subject"));
        question.setContent(document.getString("content"));
        question.setTags(document.get("tags", List.class));

        question.setTotalAnswers(document.getInteger("totalAnswers"));
        question.setMentionedUsers(document.get("mentionedUsers", List.class));

        question.setCreateTime(document.getDate("createTime"));
        question.setLastUpdateTime(document.getDate("lastUpdateTime"));
        question.setStatus(document.getInteger("status"));

        return question;
    }

    @Override
    public void encode(BsonWriter writer, Question question, EncoderContext encoderContext) {
        Document document = new Document();
        if (question.getId() != null) {
            document.put("_id", new ObjectId(question.getId()));
        }
        document.put("userId", question.getUserId());
        document.put("subject", question.getSubject());
        document.put("content", question.getContent());
        document.put("tags", question.getTags());

        document.put("totalAnswers", question.getTotalAnswers());
        document.put("mentionedUsers", question.getMentionedUsers());

        document.put("createTime", question.getCreateTime());
        document.put("lastUpdateTime", question.getLastUpdateTime());
        document.put("status", question.getStatus());
        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<Question> getEncoderClass() {
        return Question.class;
    }

    @Override
    public Question generateIdIfAbsentFromDocument(Question document) {
        document.setId(new ObjectId().toString());
        return document;
    }

    @Override
    public boolean documentHasId(Question document) {
        return document.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(Question document) {
        return new BsonObjectId(new ObjectId(document.getId()));
    }
}
