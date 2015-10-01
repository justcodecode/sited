package org.app4j.site.module.comment.codec;

import com.google.common.collect.Lists;
import org.app4j.site.module.comment.domain.Comment;
import org.app4j.site.module.comment.domain.Reply;
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
public class CommentCodec implements Codec<Comment> {
    private final DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public Comment decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);

        Comment comment = new Comment();
        comment.setId(document.getObjectId("_id").toHexString());
        comment.setThreadId(document.getString("threadId"));
        comment.setContent(document.getString("content"));
        comment.setCreateTime(document.getDate("createTime"));
        comment.setLastUpdateTime(document.getDate("lastUpdateTime"));
        comment.setStatus(document.getInteger("status"));

        comment.setReplies(Lists.newArrayList());
        List<Document> replies = document.get("replies", List.class);
        if (replies != null) {
            for (Document doc : replies) {
                Reply reply = new Reply();
                reply.setUsername(doc.getString("username"));
                reply.setContent(doc.getString("content"));
                reply.setCreateTime(doc.getDate("createTime"));

                comment.getReplies().add(reply);
            }
        }

        return comment;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Comment comment, EncoderContext encoderContext) {
        Document document = new Document();
        if (comment.getId() != null) {
            document.put("_id", new ObjectId(comment.getId()));
        }

        document.put("threadId", comment.getThreadId());
        document.put("content", comment.getContent());
        document.put("createTime", comment.getCreateTime());
        document.put("lastUpdateTime", comment.getLastUpdateTime());
        document.put("status", comment.getStatus());

        if (comment.getReplies() != null) {
            List<Document> replies = Lists.newArrayList();
            for (Reply reply : comment.getReplies()) {
                Document doc = new Document();
                doc.put("content", reply.getContent());
                doc.put("username", reply.getUsername());
                doc.put("createTime", reply.getCreateTime());
                replies.add(doc);
            }
            document.put("replies", replies);
        }

        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<Comment> getEncoderClass() {
        return Comment.class;
    }
}
