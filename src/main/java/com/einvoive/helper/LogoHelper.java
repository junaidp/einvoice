package com.einvoive.helper;

import com.einvoive.model.Logo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Component
public class LogoHelper {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    public String uploadLogo(MultipartFile upload) throws IOException {
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());
        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);
        return fileID.toString();
    }


    public Logo getLogo(String id) throws IOException {
        GridFSFile gridFSFile = template.findOne( new Query(Criteria.where("id").is(id)) );
        Logo logo = new Logo();
        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            logo.setFilename( gridFSFile.getFilename() );
            logo.setFileType( gridFSFile.getMetadata().get("_contentType").toString() );
            logo.setFileSize( gridFSFile.getMetadata().get("fileSize").toString() );
        }
        return logo;
    }

}
