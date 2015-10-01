package org.app4j.site.module.page.service.index;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class Dictionary {
    Node root = new Node();
    int depth = 0;

    public Dictionary add(String phase) {
        StandardTokenizer standardTokenizer = new StandardTokenizer();
        try {
            standardTokenizer.setReader(new StringReader(phase));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TokenStream tokenStream = new StandardFilter(standardTokenizer);
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new StopFilter(tokenStream, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        Node current = root;
        try {
            tokenStream.reset();

            int currentDepth = 0;
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();

                if (current.children == null) {
                    current.children = Maps.newHashMap();
                }

                if (current.children.containsKey(term)) {
                    current = current.children.get(term);
                } else {
                    Node node = new Node();
                    current.children.put(term, node);
                    current = node;
                }
                currentDepth++;
            }

            current.leaf = true;
            if (currentDepth > depth) {
                depth = currentDepth;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public int depth() {
        return depth;
    }

    public boolean exists(String phase) {
        StandardTokenizer standardTokenizer = new StandardTokenizer();
        try {
            standardTokenizer.setReader(new StringReader(phase));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TokenStream tokenStream = new StandardFilter(standardTokenizer);
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new StopFilter(tokenStream, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        List<String> terms = Lists.newArrayList();
        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                terms.add(charTermAttribute.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return exists(terms);
    }

    public boolean exists(List<String> terms) {
        Node current = root;

        for (int i = 0; i < terms.size(); i++) {
            String term = terms.get(i);

            if (current.children != null && current.children.containsKey(term)) {
                current = current.children.get(term);

                if (i == terms.size() - 1) {
                    return current.leaf;
                }
            } else {
                break;
            }
        }
        return false;
    }


    class Node {
        boolean leaf;
        Map<String, Node> children;
    }
}
