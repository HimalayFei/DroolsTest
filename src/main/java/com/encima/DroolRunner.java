package com.encima;
 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.runtime.StatefulKnowledgeSession;

import com.encima.utils.DBTools;
import com.encima.utils.DroolsTools;
 
public class DroolRunner {
 
    private static KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    private static Collection<KnowledgePackage> pkgs;
    private static KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
    private static StatefulKnowledgeSession ksession;
 
    public static void main(final String[] args) {
 
        initDrools();
        initMessageObject();
        fireRules();
 
    }
 
    private static void initDrools(){
    	
    	DBTools.addDBRulesFromFile("drools", "root", "root", "/home/encima/development/java/drools/DroolsTest/src/main/rules/Sample.drl");
    	DBTools.loadDBRules("drools", "root", "root", 2, kbuilder);

        // Check the builder for errors
        if ( kbuilder.hasErrors() ) {
            System.out.println( kbuilder.getErrors().toString() );
            throw new RuntimeException( "Unable to compile drl\"." );
        }
 
        // get the compiled packages (which are serializable)
        pkgs = kbuilder.getKnowledgePackages();
 
        // add the packages to a knowledgebase (deploy the knowledge packages).
        kbase.addKnowledgePackages( pkgs );
 
        ksession = kbase.newStatefulKnowledgeSession();
        
    }
 
    private static void fireRules(){
        ksession.fireAllRules();
    }
 
    private static void initMessageObject() {
        Message msg = new Message();
        msg.setType("Test");
        ksession.insert(msg);
    }
}