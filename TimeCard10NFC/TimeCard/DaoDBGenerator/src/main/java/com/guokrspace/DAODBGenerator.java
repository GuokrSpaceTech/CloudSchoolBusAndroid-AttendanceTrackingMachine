package com.guokrspace;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;
import de.greenrobot.daogenerator.ToOne;

public class DAODBGenerator {
    /**
     * Generates entities and DAOs for the example project DaoExample.
     * <p/>
     * Run it as a Java application (not Android).
     *
     * @author Markus
     */

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1000, "com.guokrspace");

        addStudent(schema);

        new DaoGenerator().generateAll(schema, "src-gen");
    }

    private static void addStudent(Schema schema) {

        Entity parents = schema.addEntity("ParentsEntity");
        parents.addIdProperty().autoincrement().primaryKey();
        parents.addStringProperty("relation");
        parents.addStringProperty("avatar");
        Property studentidParents = parents.addStringProperty("studentid").notNull().getProperty();

        Entity cards = schema.addEntity("CardEntity");
        cards.addIdProperty().autoincrement().primaryKey();
        cards.addStringProperty("cardnumber");
        Property studentIdCards = cards.addStringProperty("studentid").notNull().getProperty();

        Entity student = schema.addEntity("StudentEntity");
        student.addStringProperty("studentid");
        student.addStringProperty("pid");
        student.addStringProperty("cnnname");
        student.addStringProperty("schoolname");
        student.addStringProperty("avatar");
        student.addStringProperty("classname");

        Entity timerecord = schema.addEntity("TimeRecordEntity");
        timerecord.addIdProperty().autoincrement().primaryKey();
        timerecord.addStringProperty("cardid");
        timerecord.addStringProperty("machine");
        timerecord.addFloatProperty("temperature");
        timerecord.addStringProperty("healthstate");
        timerecord.addLongProperty("createtime");
        timerecord.addStringProperty("fbody");
        timerecord.addStringProperty("fext");
    }
}
