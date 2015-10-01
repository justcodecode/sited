package org.app4j.site.module.question.codec;

import org.app4j.site.module.question.domain.Answer;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.util.List;


/**
 * @author chi
 */
public class AnswerCodec implements Codec<Answer> {
    DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public Answer decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);

        Answer answer = new Answer();

        answer.setId(document.getObjectId("_id").toString());
        answer.setUserId(document.getString("userId"));
        answer.setQuestionId(document.getString("questionId"));
        answer.setContent(document.getString("content"));

        answer.setTotalUpVotes(document.getInteger("totalUpVotes"));
        answer.setTotalDownVotes(document.getInteger("totalDownVotes"));
        answer.setAccepted(document.getBoolean("accepted"));

        answer.setMentionedUsers(document.get("mentionedUsers", List.class));

        answer.setCreateTime(document.getDate("createTime"));
        answer.setLastUpdateTime(document.getDate("lastUpdateTime"));
        answer.setStatus(document.getInteger("status"));

        return answer;
    }

    @Override
    public void encode(BsonWriter writer, Answer answer, EncoderContext encoderContext) {
        Document document = new Document();
        if (answer.getId() != null) {
            document.put("_id", new ObjectId(answer.getId()));
        }
        document.put("userId", answer.getUserId());
        document.put("questionId", answer.getQuestionId());
        document.put("content", answer.getContent());

        document.put("totalUpVotes", answer.getTotalUpVotes());
        document.put("totalDownVotes", answer.getTotalDownVotes());
        document.put("accepted", answer.isAccepted());

        document.put("mentioned", answer.getMentionedUsers());

        document.put("createTime", answer.getCreateTime());
        document.put("lastUpdateTime", answer.getLastUpdateTime());
        document.put("status", answer.getStatus());
        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<Answer> getEncoderClass() {
        return Answer.class;
    }
}
