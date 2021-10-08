package com.einvoive.helper;

import com.einvoive.model.Company;
import com.einvoive.model.Logo;
import com.einvoive.repository.CompanyRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
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
    private CompanyRepository companyRepository;

    @Autowired
    private MongoOperations mongoOperation;

    @Autowired
    private GridFsOperations operations;

    public String uploadLogo(MultipartFile upload, String companyID) throws IOException {
        DBObject metadata = new BasicDBObject();
        String logoName =  getCompanyName(companyID)+"_logo";
        metadata.put("fileSize", upload.getSize());
        Object fileID = template.store(upload.getInputStream(), logoName, upload.getContentType(), metadata);
        return fileID.toString();
    }


    public Logo getLogo(String companyID) throws IOException {
        String logoName =  getCompanyName(companyID)+"_logo";
        GridFSFile gridFSFile = template.findOne( new Query(Criteria.where("filename").is(logoName)) );
        Logo loadFile = new Logo();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setFilename( gridFSFile.getFilename() );
            loadFile.setFileType( gridFSFile.getMetadata().get("_contentType").toString() );
            loadFile.setFileSize( gridFSFile.getMetadata().get("fileSize").toString() );
            loadFile.setFile( IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()) );
        }
        return loadFile;
    }

    private String getCompanyName(String companyID){
        Company company = mongoOperation.findOne(new Query(Criteria.where("companyID").is(companyID)),Company.class);
        return company.getCompanyName();
    }
}
