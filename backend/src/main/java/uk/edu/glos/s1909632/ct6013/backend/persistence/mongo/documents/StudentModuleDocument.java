package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents;

import org.bson.codecs.pojo.annotations.BsonProperty;

public final class StudentModuleDocument {
    @BsonProperty
    private ModuleDocument moduleDocument;

    @BsonProperty
    private Long mark;

    public StudentModuleDocument() {
    }

    public ModuleDocument getModuleDocument() {
        return moduleDocument;
    }

    public void setModuleDocument(
            ModuleDocument moduleDocument) {
        this.moduleDocument = moduleDocument;
    }

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }

}
