package com.hcmute.drink.collection.generator;

import com.hcmute.drink.collection.SequenceCollection;
import com.hcmute.drink.utils.GeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Component
public class SequenceGenerator {
    @Autowired
    private MongoOperations mongoOperations;

    public String generateId(String sequenceName, String prefix, int length) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("count", 1);
        SequenceCollection document = mongoOperations.findAndModify(query, update,
                options().returnNew(true).upsert(true),
                SequenceCollection.class);
        long count = !Objects.isNull(document) ? document.getCount() : 1;
        return GeneratorUtils.formatCodeItem(count, prefix, length);
    }
}
